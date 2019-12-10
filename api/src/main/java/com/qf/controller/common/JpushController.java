package com.qf.controller.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcProtocol;
import com.qf.service.app.HixentAppUserWarnService;
import com.qf.service.email.EmailService;
import com.qf.service.fire.HixentArcAlarmLogService;
import com.qf.service.fire.HixentArcDeviceAlarmService;
import com.qf.service.fire.HixentArcEfmDeviceService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.service.fire.HixentArcProtocolService;
import com.qf.service.jpush.JpushService;
import com.qf.service.mqtt.MqttCustomerService;
import com.qf.service.websocket.WebSocketService;
import com.qf.util.AliyunOSSUtil;
import com.qf.util.AliyunSmsUtil;
import com.qf.util.DateUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import sun.misc.BASE64Decoder;

@RestController
@RequestMapping("/allGetApp")
public class JpushController {

	@Autowired
	private JpushService jpushService;

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	private HixentArcDeviceAlarmService hixentArcDeviceAlarmService;

	@Autowired
	private HixentArcEfmDeviceService hixentArcEfmDeviceService;

	@Autowired
	private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;

	@Autowired
	private HixentArcProtocolService hixentArcProtocolService;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private MqttCustomerService mqttCustomerService;

	@Resource
	private HixentArcAlarmLogService hixentArcAlarmLogService;

	@Resource
	private HixentAppUserWarnService hixentAppUserWarnService;
	@Resource
	private EmailService emailService;

	/**
	 * author Vareck
	 */
	@RequestMapping(value = "/sendJpushMessage", method = RequestMethod.POST)
	public ModelMap sendJpushMessage(HttpServletRequest request) {
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String key = request.getParameter("key");
		Integer type = Integer.valueOf(request.getParameter("type"));
		if (type == null) {
			type = 1;
		}
		String[] appUserArr = key.split(",");
		for (int i = 0; i < appUserArr.length; i++) {

			Map<String, String> extrasMap = new HashMap<String, String>();
			// 此Map必须创建和实例化，但可以不添加内容
			extrasMap.put("key", "123456");
			if (type == 1) {
				jpushService.sendPush(title, content, extrasMap, appUserArr[i]);
			} else if (type == 2) {
				jpushService.sendPushWithCallback(title, content, extrasMap, appUserArr[i]);
			} else if (type == 3) {
				jpushService.sendCustomPush(title, content, extrasMap, appUserArr[i]);
			} else {
				return ReturnUtil.Error("参数错误！");
			}

		}

		return ReturnUtil.Success("发送成功！");
	}

	/**
	 * websocket发送广播消息 author wjr
	 */
	@RequestMapping(value = "/sendWebsocket", method = RequestMethod.POST)
	public void sendWebsocket() {
		try {
			// 获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String deviceId = request.getParameter("deviceId");

			JSONObject obj = new JSONObject();
			{
				obj.put("msg", "alarm");
				obj.put("deviceId", deviceId);
			}
			// java对象变成json对象
			String Jsonmessage = JSONObject.toJSONString(obj);

			webSocketService.sendMessageToUser("888",
					new TextMessage("websocket：发给指定用户的一条消息。" + deviceId + "," + DateUtil.getCurrentTime()));
			webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 中控故障（中控设备上报） websocket author Ruanyu
	 */
	@RequestMapping(value = "/alarmWebsocket", method = RequestMethod.POST)
	public void alarmWebsocket() {
		try {
			// 获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String code = request.getParameter("code");
			String deviceId = request.getParameter("deviceId");

			int codeInt = Integer.parseInt(code);
			String codeFormatTwo = formatTwo(codeInt);
			// 将2进制报警信息存入数据库
			hixentArcDeviceAlarmService.updateDeviceInfo(deviceId, codeFormatTwo);
			// 获取时间并转成时间戳
			String currentDate = DateUtil.getCurrentTime();
			long timestamp = DateUtil.getTimestamp(currentDate);
			int nowtimestamp = (int) timestamp;
			String nowTimesToString = DateUtil.timestampToString(nowtimestamp);
			String device = deviceInfo(deviceId);

			String adminByEquipId = hixentArcDeviceAlarmService.getAdminByDeviceId(deviceId);
			String[] adminByEquipIdArr = adminByEquipId.split(",");

			
			
			String[] appUserArrDevice = appUserArrDevice(deviceId).get(0);
			String[] appUserEmailArrDevice = appUserArrDevice(deviceId).get(1);
			String[] appUserMoblieArrDevice = appUserArrDevice(deviceId).get(2);
			// 报警记录表插入数据
			for (int i = 0; i < codeFormatTwo.length(); i++) {

				if (codeFormatTwo.charAt(i) == '1') {
					String warnType = "udp_" + ToolUtil.getwarnTypeMap().get(i);
					// 报警类型
					String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
					String currentTime = DateUtil.getCurrentTime();
					 Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnTimeByEid(deviceId, warnType, timestamp);
                    if(selWarnTimeByEid==null ||selWarnTimeByEid==0) {
                    	hixentArcDeviceAlarmService.addWarnDevice(deviceId, warnType, timestamp);
                    	
                    	JSONObject obj = new JSONObject();
						{
							obj.put("msg", "alarm");
							obj.put("deviceId", device);
							obj.put("warnType", warnTypeCh);
						}
						// java对象变成json对象
						String Jsonmessage = JSONObject.toJSONString(obj);
						for (int j = 0; j < adminByEquipIdArr.length; j++) {
							webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[j],
									new TextMessage(Jsonmessage));

						}
						//推送
						String title = "莱茵斯科技";
						String content = device + "设备出现" + warnTypeCh + "问题。请及时处理！最早报警时间是："+nowTimesToString;
						// String[] appUserArrDevice= {"user","user1"};
						Integer type = 1;
						if (appUserArrDevice != null) {
							jpushSendMessage(title, content, type, appUserArrDevice);
						}
						if (appUserEmailArrDevice != null) {
							String subject="莱茵斯科技";
							
							for (int j = 0; j < appUserMoblieArrDevice.length; j++) {
								emailService.sendSimpleEmail(appUserEmailArrDevice[j], subject, content);
							}
							
						}
//						if (appUserMoblieArrDevice != null) {
//							
//		            		String  signName      = "莱茵斯科技";
//		        			String  templateCode;
//		        			
//			    			String  templateParam = "{\"deviceId\":\""+device+"\",\"warnType\":\""+warnTypeCh+"\"}";
//		        			String  outId = null;
//		                    templateCode  = "SMS_158942123";
//		                   for (int j = 0; j < appUserMoblieArrDevice.length; j++) {
//		                	   AliyunSmsUtil.sendSms( appUserMoblieArrDevice[j],signName,templateCode,templateParam,outId );
//						}
//		                  
//							
//						}
                    }else {
                    	nowTimesToString=DateUtil.timestampToString(selWarnTimeByEid);
                    }
					
						
					}
				}
			

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 中控使能 author Ruanyu
	 */
	@RequestMapping(value = "/enableDevice", method = RequestMethod.POST)
	public void enableDevice() {
		try {
			// 获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String code = request.getParameter("code");
			String deviceId = request.getParameter("deviceId");
			int codeInt = Integer.parseInt(code);
			// 10进制转2进制
			String codeFormatTwo = formatTwo(codeInt);
			// 将2进制报警信息存入数据库
			hixentArcDeviceAlarmService.updateDeviceEnable(deviceId, codeFormatTwo);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 终端报警（终端设备上报） websocket报警处理websocket author Ruanyu
	 */
	@RequestMapping(value = "/equipAlarm", method = RequestMethod.POST)
	public void equipAlarmWebsocket() {
		try {
			// 获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			// String code = request.getParameter("code");
			String warnType = request.getParameter("warning_type");
			Integer warnTime = Integer.valueOf(request.getParameter("warning_time"));
			String eid = request.getParameter("eid");
			Integer etype = Integer.valueOf(request.getParameter("etype"));
			String deviceId = request.getParameter("efm_id");
			Integer warnIndex = Integer.valueOf(request.getParameter("warning_index"));
			// 获取时间并转成时间戳
			String currentDate = DateUtil.getCurrentTime();
			long timestamp = DateUtil.getTimestamp(currentDate);
			int nowtimestamp = (int) timestamp;
			String nowTimesToString = DateUtil.timestampToString(nowtimestamp);
			
			// 查询该报警是否已存在
			Integer selWarnIdByEid = hixentArcDeviceAlarmService.selWarnIdByEid(eid, warnType);
			if (selWarnIdByEid == null||selWarnIdByEid==0) {
				// 终端报警存入数据库
				Integer equipAlarm = hixentArcDeviceAlarmService.equipAlarm(eid, deviceId, warnIndex, warnType, etype,
						warnTime);
				
				HixentArcEquipmentInfo equipInfoById = hixentArcEquipmentInfoService.getEquipInfoById(eid);
				String addr = equipInfoById.getAddr();
				String lineCode = equipInfoById.getLineCode();
				String device_code = equipInfoById.getDevice_code();
				String niNameDevice = equipInfoById.getNiNameDevice();
				String specificatorDevice = equipInfoById.getSpecificatorDevice();

				// 报警类型
				String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
				String formatDevStr = ToolUtil.formatDevStr(specificatorDevice);
				String device = "";
				if (niNameDevice == null || niNameDevice.equals("")) {

					if (formatDevStr == null || formatDevStr.equals("")) {
						device = device_code;
					} else {
						device = formatDevStr;
					}
				} else {
					device = niNameDevice;
				}
				// 查询设备所在的项目所有管理员
				String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(eid);
				System.out.println(adminByEquipId);
				String[] adminByEquipIdArr = adminByEquipId.split(",");

				JSONObject obj = new JSONObject();
				{
					obj.put("msg", "equipAlarm");
					obj.put("deviceId", device);
					obj.put("warnType", warnTypeCh);
					obj.put("number", (Integer.valueOf(lineCode) + 1) + "-" + addr);
				}
				// java对象变成json对象
				String Jsonmessage = JSONObject.toJSONString(obj);
				// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
				for (int i = 0; i < adminByEquipIdArr.length; i++) {
					webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[i], new TextMessage(Jsonmessage));
				}
				String[] appUserArrEquip = appUserArrEquip(eid).get(0);
				String[] appUserEmailArrEquip = appUserArrEquip(eid).get(1);
				String[] appUserMobileArrEquip = appUserArrEquip(eid).get(2);
				// String[] appUserArrEquip= {"user","user1"};
				Integer type = 1;
				String title = "莱茵斯科技";
				String content = "中控：" + device + " 终端：" + (Integer.valueOf(lineCode) + 1) + "-" + addr + "设备出现"
						+ warnTypeCh + "问题。请及时处理！最早报警时间是："+nowTimesToString;
				if (appUserArrEquip != null) {
					jpushSendMessage(title, content, type, appUserArrEquip);
				}
				if (appUserEmailArrEquip != null) {
					String subject="莱茵斯科技";
					
					for (int j = 0; j < appUserEmailArrEquip.length; j++) {
						emailService.sendSimpleEmail(appUserEmailArrEquip[j], subject, content);
					}
				}
//				if (appUserMobileArrEquip != null) {
//					String  signName      = "莱茵斯科技";
//	    			String  templateCode;
//	    			String number=(Integer.valueOf(lineCode) + 1) + "-" + addr;
//	    			String  templateParam = "{\"deviceId\":\""+device+"\",\"number\":\""+number+"\",\"warnType\":\""+warnTypeCh+"\"}";
//	    			String  outId = null;
//	                templateCode  = "SMS_158942130";
//	               for (int j = 0; j < appUserMobileArrEquip.length; j++) {
//	            	   AliyunSmsUtil.sendSms( appUserMobileArrEquip[j],signName,templateCode,templateParam,outId );
//				}
//				}
			}else {
            	nowTimesToString=DateUtil.timestampToString(selWarnIdByEid);
            }

			
			
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 设置指令应答推送(有线) author wjr
	 */
	@RequestMapping(value = "/replySetCmd", method = RequestMethod.POST)
	public void replySetCmdWebsocket() {
		try {
			// 获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String cmdId = request.getParameter("cmdId");
			String efmId = request.getParameter("efmId");
			HixentArcProtocol pd = new HixentArcProtocol();
			pd.setProtocolId(cmdId);
			pd.setProtocolAttribute("03");
			;
			HixentArcProtocol pInfo = hixentArcProtocolService.selectOne(pd);
			String protocolName = pInfo.getProtocolName();
			JSONObject obj = new JSONObject();
			{
				obj.put("msg", "replySet");
				obj.put("cmd", protocolName);
				obj.put("efmId", efmId);
			}
			// java对象变成json对象
			String Jsonmessage = JSONObject.toJSONString(obj);
			webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 检测中控是否离线 websocket报警处理websocket author Ruanyu
	 */
	@RequestMapping(value = "/checkDeviceOnline", method = RequestMethod.POST)
	public void checkDeviceOnline() {
		try {
			// 查询中控ID和设置离线时间
			List<HixentArcEfmDevice> deviceOfflineTime = hixentArcEfmDeviceService.getDeviceOfflineTime();
			// 报警类型
			String warnType = "udp_device_offline";
			String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
			for (int i = 0; i < deviceOfflineTime.size(); i++) {
				boolean checkSuc = false;
				String deviceId = deviceOfflineTime.get(i).getId();

				String deviceTime = (String) redisUtil.get("report_" + deviceId);
				// 获取当前时间戳
				String currentDate = DateUtil.getCurrentTime();
				long nowtimestamp = DateUtil.getTimestamp(currentDate);
				int nowtimestampInt = (int) nowtimestamp;
				String nowTimesToString = DateUtil.timestampToString(nowtimestampInt);
				
				// 拥有该中控的管理员
				String adminByDeviceId = hixentArcDeviceAlarmService.getAdminByDeviceId(deviceId);
				String[] adminByDeviceIdArr = adminByDeviceId.split(",");
				
				if (ToolUtil.StringNotBlank(deviceTime)) {
					
					Integer deviceTimeInt = Integer.valueOf(deviceTime);

					if (deviceOfflineTime.get(i).getOfflineEnable() == 0) {
						// 使能开
						if (nowtimestamp - deviceTimeInt > deviceOfflineTime.get(i).getOffLineTime()) {
							// 离线
							checkSuc = hixentArcEfmDeviceService.updateDeviceOfflineState(deviceId, 0, warnType,
									nowtimestamp);
							redisUtil.del("report_" + deviceId);
							String device = deviceInfo(deviceId);
							// 发Websocket
							sendWebsocketForDevice(warnTypeCh, device, adminByDeviceIdArr);
							String[] appUserArrDevice = appUserArrDevice(deviceId).get(0);
							String[] appUserEmailArrDevice = appUserArrDevice(deviceId).get(1);
							String[] appUserMoblieArrDevice = appUserArrDevice(deviceId).get(2);
							// String[] appUserArrDevice= {"user"};
							Integer type = 1;
							String title = "莱茵斯科技";
							String content = device + "设备出现" + warnTypeCh + "问题。请及时处理！最早报警时间："+nowTimesToString;
							if (appUserArrDevice != null) {

								jpushSendMessage(title, content, type, appUserArrDevice);
							}
							if (appUserEmailArrDevice != null) {
								String subject="莱茵斯科技";
								
								for (int j = 0; j < appUserMoblieArrDevice.length; j++) {
									emailService.sendSimpleEmail(appUserEmailArrDevice[j], subject, content);
								}
								
							}
//							if (appUserMoblieArrDevice != null) {
//								
//			            		String  signName      = "莱茵斯科技";
//			        			String  templateCode;
//			        			String  templateParam = "{\"deviceId\":\""+device+"\",\"warnType\":\""+warnTypeCh+"\"}";
//			        			String  outId = null;
//			                    templateCode  = "SMS_158942123";
//			                   for (int j = 0; j < appUserMoblieArrDevice.length; j++) {
//			                	   AliyunSmsUtil.sendSms( appUserMoblieArrDevice[j],signName,templateCode,templateParam,outId );
//							}
//			                  
//								
//							}
							
						} else {
							// 在线
							checkSuc = hixentArcEfmDeviceService.updateDeviceOnlineState(deviceId, 1, warnType);
						}
					} else {
						// 使能关
						if (nowtimestamp - deviceTimeInt > deviceOfflineTime.get(i).getOffLineTime()) {
							// 离线
							checkSuc = hixentArcEfmDeviceService.updateDeviceOfflineStateOffEnable(deviceId, 0);
							redisUtil.del("report_" + deviceId);

						} else {
							// 在线
							checkSuc = hixentArcEfmDeviceService.updateDeviceOnlineState(deviceId, 1, warnType);

						}
						
					}
				} else {
					
					// 离线
					if (deviceOfflineTime.get(i).getOfflineEnable() == 0) {
						
						checkSuc = hixentArcEfmDeviceService.updateDeviceOfflineState(deviceId, 0, warnType,
								nowtimestamp);
						String device = deviceInfo(deviceId);
						
						// 发Websocket
						sendWebsocketForDevice(warnTypeCh, device, adminByDeviceIdArr);
						String[] appUserArrDevice = appUserArrDevice(deviceId).get(0);
						String[] appUserEmailArrDevice = appUserArrDevice(deviceId).get(1);
						String[] appUserMoblieArrDevice = appUserArrDevice(deviceId).get(2);
						//String[] appUserArrDevice = { "user", "user1" };
						Integer type = 1;
						String title = "莱茵斯科技";
						String content = device + "设备出现" + warnTypeCh + "问题。请及时处理！最早报警时间："+nowTimesToString;
						if (appUserArrDevice != null) {
							
							jpushSendMessage(title, content, type, appUserArrDevice);
						}
						if (appUserEmailArrDevice != null) {
							
							String subject="莱茵斯科技";
							
							for (int j = 0; j < appUserEmailArrDevice.length; j++) {
								emailService.sendSimpleEmail(appUserEmailArrDevice[j], subject, content);
							}
							
						}
//						if (appUserMoblieArrDevice != null) {
//							
//		            		String  signName      = "莱茵斯科技";
//		        			String  templateCode;
//		        			String  templateParam = "{\"deviceId\":\""+device+"\",\"warnType\":\""+warnTypeCh+"\"}";
//		        			String  outId = null;
//		                    templateCode  = "SMS_158942123";
//		                   for (int j = 0; j < appUserMoblieArrDevice.length; j++) {
//		                	   AliyunSmsUtil.sendSms( appUserMoblieArrDevice[j],signName,templateCode,templateParam,outId );
//						   }
//		                  
//							
//						}
					} else {
						checkSuc = hixentArcEfmDeviceService.updateDeviceOfflineStateOffEnable(deviceId, 0);
					}

					redisUtil.del("report_" + deviceId);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 中控复位 author Ruanyu
	 */
	@RequestMapping(value = "/alarmReset", method = RequestMethod.POST)
	public void testMqtt(String deviceId) {
		try {
			// 拥有该中控的管理员
			String adminByDeviceId = hixentArcDeviceAlarmService.getAdminByDeviceId(deviceId);
			String[] adminByDeviceIdArr = adminByDeviceId.split(",");

			Integer deviceReset = hixentArcAlarmLogService.deviceReset(deviceId);
			if (deviceReset > 0) {
				sendWebsocketForDeviceReset(adminByDeviceIdArr);
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 终端告警（终端告警上报） websocket author Ruanyu
	 */
	@RequestMapping(value = "/combineEquipAlarm", method = RequestMethod.POST)
	public void alarmEquipWebsocket() {
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String code1 = request.getParameter("code1");
			String code2 = request.getParameter("code2");
			Integer warnTime = Integer.valueOf(request.getParameter("warning_time"));
			String eid = request.getParameter("eid");
			Integer etype = Integer.valueOf(request.getParameter("etype"));
			String deviceId = request.getParameter("efm_id");
			Integer warnIndex = Integer.valueOf(request.getParameter("warning_index"));
			int nowtimestampInt = (int) warnTime;
			String nowTimesToString = DateUtil.timestampToString(nowtimestampInt);
			
			String cmdid = request.getParameter("cmdid");
			// 存入数据库
			String equipField = ToolUtil.udpEquipMapping().get(cmdid);
			hixentArcEquipmentInfoService.updateEquipInfo(equipField, code1);
			// 告警16进制转2进制
			String hexString2binaryString = ToolUtil.hexString2binaryString(code1);
			Map<Integer, String> warnMap = new HashMap<Integer, String>();

			if (cmdid.equals("0305")) {
				warnMap = ToolUtil.equipCurrentWarn();
			}
			if (cmdid.equals("0306")) {
				warnMap = ToolUtil.equipVoltageWarn();
			}
			if (cmdid.equals("0307")) {
				warnMap = ToolUtil.equipTemperatureWarn();
			}
			if (cmdid.equals("0308")) {
				warnMap = ToolUtil.equipOtherWarn();
			}
			for (int i = 0; i < hexString2binaryString.length(); i++) {
				if (hexString2binaryString.charAt(i) == '1') {
					String warnType = warnMap.get(i);
					if (ToolUtil.StringNotBlank(warnType)) {
						// 报警中文
						String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
						// 查询该报警是否已存在
						Integer selWarnIdByEid = hixentArcDeviceAlarmService.selWarnIdByEid(eid, warnType);
						if (selWarnIdByEid == null ||selWarnIdByEid==0) {
							// 终端报警存入数据库
							Integer equipAlarm = hixentArcDeviceAlarmService.equipAlarm(eid, deviceId, warnIndex,
									warnType, etype, warnTime);
							
							HixentArcEquipmentInfo equipInfoById = hixentArcEquipmentInfoService.getEquipInfoById(eid);
							String addr = equipInfoById.getAddr();
							String lineCode = equipInfoById.getLineCode();
							String device_code = equipInfoById.getDevice_code();
							String niNameDevice = equipInfoById.getNiNameDevice();
							String specificatorDevice = equipInfoById.getSpecificatorDevice();

							String formatDevStr = ToolUtil.formatDevStr(specificatorDevice);
							String device = "";
							if (niNameDevice == null || niNameDevice.equals("")) {

								if (formatDevStr == null || formatDevStr.equals("")) {
									device = device_code;
								} else {
									device = formatDevStr;
								}
							} else {
								device = niNameDevice;
							}
							// 查询设备所在的项目所有管理员
							String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(eid);

							String[] adminByEquipIdArr = adminByEquipId.split(",");

							JSONObject obj = new JSONObject();
							{
								obj.put("msg", "equipAlarm");
								obj.put("deviceId", device);
								obj.put("warnType", warnTypeCh);
								obj.put("number", (Integer.valueOf(lineCode) + 1) + "-" + addr);
							}
							// java对象变成json对象
							String Jsonmessage = JSONObject.toJSONString(obj);
							// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
							for (int m = 0; m < adminByEquipIdArr.length; m++) {
								webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[m],
										new TextMessage(Jsonmessage));
							}
							String[] appUserArrEquip = appUserArrEquip(eid).get(0);
							String[] appUserEmailArrEquip = appUserArrEquip(eid).get(1);
							String[] appUserMobileArrEquip = appUserArrEquip(eid).get(2);
							// String[] appUserArrEquip= {"user","user1"};
							Integer type = 1;
							String title = "莱茵斯科技";
							String content = "中控：" + device + " 终端：" + (Integer.valueOf(lineCode) + 1) + "-" + addr + "设备出现"
									+ warnTypeCh + "问题。请及时处理！最早报警时间是："+nowTimesToString;
							if (appUserArrEquip != null) {
								jpushSendMessage(title, content, type, appUserArrEquip);
							}
						
							
							if (appUserEmailArrEquip != null) {
								String subject="莱茵斯科技";
								
								for (int j = 0; j < appUserEmailArrEquip.length; j++) {
									emailService.sendSimpleEmail(appUserEmailArrEquip[j], subject, content);
								}
							}
//							if (appUserMobileArrEquip != null) {
//								String  signName      = "莱茵斯科技";
//				    			String  templateCode;
//				    			String number=(Integer.valueOf(lineCode) + 1) + "-" + addr;
//				    			String  templateParam = "{\"deviceId\":\""+device+"\",\"number\":\""+number+"\",\"warnType\":\""+warnTypeCh+"\"}";
//				    			String  outId = null;
//				                templateCode  = "SMS_158942130";
//				               for (int j = 0; j < appUserMobileArrEquip.length; j++) {
//				            	   AliyunSmsUtil.sendSms( appUserMobileArrEquip[j],signName,templateCode,templateParam,outId );
//							}
//							}
						}else {
			            	nowTimesToString=DateUtil.timestampToString(selWarnIdByEid);
			            }
						
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 终端使能（终端使能上报） websocket author Ruanyu
	 */
	@RequestMapping(value = "/combineEquipAlarmEnable", method = RequestMethod.POST)
	public void combineEquipAlarmEnable() {
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String code1 = request.getParameter("code1");
			String code2 = request.getParameter("code2");
			String eid = request.getParameter("eid");
			String cmdid = request.getParameter("cmdid");

			// 告警16进制转2进制
			String hexString2binaryString = ToolUtil.hexString2binaryString(code1);
			Map<Integer, String> warnMap = new HashMap<Integer, String>();

			if (cmdid.equals("0240")) {
				warnMap = ToolUtil.equipCurrentWarnEnable();
			}
			if (cmdid.equals("0241")) {
				warnMap = ToolUtil.equipVoltageWarnEnable();
			}
			if (cmdid.equals("0242")) {
				warnMap = ToolUtil.equipTemperatureWarnEnable();
			}
			if (cmdid.equals("0243")) {
				warnMap = ToolUtil.equipOtherWarnEnable();
			}

			for (int i = 0; i < hexString2binaryString.length(); i++) {
				// 获取使能字段
				String enable = warnMap.get(i);

				if (ToolUtil.StringNotBlank(enable)) {
					// 使能值
					char charAt = hexString2binaryString.charAt(i);

					String valueEnable = String.valueOf(charAt);

					// 将使能值存入数据库
					hixentArcDeviceAlarmService.updateEquipEnable(enable, valueEnable, eid);
				}

			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 终端使能（终端使能上报） websocket author Ruanyu
	 */
	@RequestMapping(value = "/arcEquipAlarmEnable", method = RequestMethod.POST)
	public void arcEquipAlarmEnable() {
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String enable = request.getParameter("pseq");

			String eid = request.getParameter("eid");
			String valueEnable = request.getParameter("enable");
			if (ToolUtil.StringNotBlank(enable)) {
				// 将使能值存入数据库
				hixentArcDeviceAlarmService.updateEquipEnable(enable, valueEnable, eid);
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 中控报警测试 author Ruanyu
	 */
	@RequestMapping(value = "/testMqtt", method = RequestMethod.POST)
	public void testMqtt(String id, String message) {
		try {
			id = "010E2B0006010109";
			//message = "7E2E01BB0201000614000000000000000000000000000000000000000007140000000000000000000000000000000000000000242D000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000600A7E";
			message = "7E2D041234000099BF7E";
			System.out.println("adfasdf");
			mqttCustomerService.replyAlarms(id, message);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	// 中控Websocket
	public void sendWebsocketForDevice(String warnTypeCh, String deviceId, String[] adminByEquipIdArr) {
		// 发websocket

		JSONObject obj = new JSONObject();
		{
			obj.put("msg", "alarm");
			obj.put("deviceId", deviceId);
			obj.put("warnType", warnTypeCh);
		}
		// java对象变成json对象
		String Jsonmessage = JSONObject.toJSONString(obj);
		for (int j = 0; j < adminByEquipIdArr.length; j++) {
			webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[j], new TextMessage(Jsonmessage));
		}
    System.out.println("asdfsdf");
	}

	// 复位中控Websocket
	public void sendWebsocketForDeviceReset(String[] adminByEquipIdArr) {
		// 发websocket

		JSONObject obj = new JSONObject();
		{
			obj.put("msg", "deviceReset");

		}
		// java对象变成json对象
		String Jsonmessage = JSONObject.toJSONString(obj);
		for (int j = 0; j < adminByEquipIdArr.length; j++) {
			webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[j], new TextMessage(Jsonmessage));
		}

	}

	// 中控信息
	public String deviceInfo(String deviceId) throws ParseException {
		JSONObject deviceInfo = hixentArcEfmDeviceService.getDeviceInfo(deviceId);
		HixentArcEfmDevice haed = (HixentArcEfmDevice) deviceInfo.get("getdeviceInfo");
		String specificator = haed.getSpecificator();
		String niName = haed.getNiName();
		String device_code = haed.getDevice_code();
		String device = "";
		if (niName == null || niName.equals("")) {
			if (specificator == null || specificator.equals("")) {
				device = device_code;
			} else {
				device = specificator;
			}
		} else {
			device = niName;
		}
		return device;
	}

	// 查询拥有某中控的用户
	public List<String[]> appUserArrDevice(String deviceId) {
		
		HixentAppUser appUsers= hixentAppUserWarnService.selAppUserByDeviceId(deviceId);
	
		String[] accountArr = null;
		String[] emailArr = null;
		String[] mobileArr = null;
		if(appUsers!=null) {
			if (ToolUtil.StringNotBlank(appUsers.getAccount())) {
				accountArr = appUsers.getAccount().split(",");
			}
			if (ToolUtil.StringNotBlank(appUsers.getEmail())) {
				emailArr = appUsers.getEmail().split(",");
			}
			if (ToolUtil.StringNotBlank(appUsers.getMobile())) {
				mobileArr = appUsers.getMobile().split(",");
			}
		
		}
		
		List<String[]> outList = new ArrayList<String[]>();
		outList.add(accountArr);
		outList.add(emailArr);
		outList.add(mobileArr);
		
		return outList;
	}

	// 查询拥有某终端的用户
	public List<String[]> appUserArrEquip(String equipId) {
		HixentAppUser appUsers= hixentAppUserWarnService.selAppUserByEquipId(equipId);
		String[] accountArr = null;
		String[] emailArr = null;
		String[] mobileArr = null;
		if(appUsers!=null) {
		if (ToolUtil.StringNotBlank(appUsers.getAccount())) {
			accountArr = appUsers.getAccount().split(",");
		}
		if (ToolUtil.StringNotBlank(appUsers.getEmail())) {
			emailArr = appUsers.getEmail().split(",");
		}
		if (ToolUtil.StringNotBlank(appUsers.getMobile())) {
			mobileArr = appUsers.getMobile().split(",");
		}
		}
		List<String[]> outList = new ArrayList<String[]>();
		outList.add(accountArr);
		outList.add(emailArr);
		outList.add(mobileArr);
		return outList;
	}

	// 极光推送
	public void jpushSendMessage(String title, String content, Integer type, String[] appUserArr) {

		for (int i = 0; i < appUserArr.length; i++) {

			Map<String, String> extrasMap = new HashMap<String, String>();
			// 此Map必须创建和实例化，但可以不添加内容
			extrasMap.put("key", "123456");
			if (type == 1) {
				jpushService.sendPush(title, content, extrasMap, appUserArr[i]);
			} else if (type == 2) {
				jpushService.sendPushWithCallback(title, content, extrasMap, appUserArr[i]);
			} else if (type == 3) {
				jpushService.sendCustomPush(title, content, extrasMap, appUserArr[i]);
			}

		}

	}
	
	

	// 10进制转2进制
	public String formatTwo(int n) {
		String result = Integer.toBinaryString(n);
		// 转成8位数2进制
		result = String.format("%0" + 8 + "d", Integer.parseInt(result));
		return result;
	}

}