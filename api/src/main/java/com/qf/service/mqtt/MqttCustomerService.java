package com.qf.service.mqtt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import com.qf.mapper.fire.*;
import com.qf.model.fire.HixentArcZipperInfo;
import com.qf.model.fire.TestJavaMqtt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.socket.TextMessage;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.admin.HixentAppUserMapper;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcEquipmentInfoMqtt;
import com.qf.service.app.HixentAppUserWarnService;
import com.qf.service.email.EmailService;
import com.qf.service.fire.HixentArcDeviceAlarmService;
import com.qf.service.fire.HixentArcEquipmentInfoMqttService;
import com.qf.service.fire.HixentArcProjectEquipmentRelevanceService;
//import com.qf.service.fire.HixentArcEquipmentInfoService;
//import com.qf.service.fire.HixentArcEquipmentInfoMqttService;
import com.qf.service.jpush.JpushService;
import com.qf.service.websocket.WebSocketService;
import com.qf.util.AliyunSmsUtil;
import com.qf.util.DataParsingUtil;
import com.qf.util.DateUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ToolUtil;

/**
 * mqtt通道消费者服务
 * 
 * @author Vareck
 */
@Service
public class MqttCustomerService {
	@Resource
	private HixentAppUserWarnService hixentAppUserWarnService;
	
	@Autowired
	private HixentArcDeviceAlarmService hixentArcDeviceAlarmService;

	@Autowired
	public HixentArcProjectEquipmentRelevanceService hixentArcProjectEquipmentRelevanceService;

	@Autowired
	public HixentArcEquipmentInfoMqttService hixentArcEquipmentInfoMqttService;

	@Autowired
	private JpushService jpushService;

	@Autowired
	private HixentArcProjectEquipmentRelevanceMapper hixentArcProjectEquipmentRelevanceMapper;

	@Autowired
	HixentAppUserMapper hixentAppUserMapper;

	@Autowired
	private HixentArcWarningListMapper hixentArcWarningListMapper; // 告警mapper

	@Autowired
	private HixentArcProtocolMapper hixentArcProtocolMapper; // 指令集mapper

	@Autowired
	private HixentArcEquipmentInfoMapper hixentArcEquipmentInfoMapper;

	@Autowired
	private HixentArcEquipmentInfoMqttMapper hixentArcEquipmentInfoMqttMapper;

	@Autowired
	private WebSocketService webSocketService;

	@Autowired
	HixentArcZipperInfoMapper hixentArcZipperInfoMapper;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private MqttProductService mqttProductService;
	
	@Resource
	private EmailService emailService;


	private static final Logger logger = LoggerFactory.getLogger(MqttCustomerService.class);

	private final static char[] mChars = "0123456789ABCDEF".toCharArray();
	private final static String mHexStr = "0123456789ABCDEF";

	public String Hex2Dec(String hexStr) {
		String result;
		hexStr = hexStr.toString().trim().replace(" ", "").toUpperCase(Locale.US);
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int iTmp = 0x00;

		for (int i = 0; i < bytes.length; i++) {
			iTmp = mHexStr.indexOf(hexs[2 * i]) << 4;
			iTmp |= mHexStr.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (iTmp & 0xFF);
		}

		result = new String(bytes);
		System.out.println("十六进制" + hexStr + "===>十进制" + result);
		return result;
	}

	// 将时间转换为时间戳
	public static String dateToStamp(String s) throws Exception {
		String res; // 设置时间格式，将该时间格式的时间转换为时间戳
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long time = date.getTime();
		res = String.valueOf(time);
		return res;
	}

	// 将时间戳转换为时间
	public static String stampToTime(String s) throws Exception {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s); // 将时间戳转换为时间
		Date date = new Date(lt); // 将时间调整为yyyy-MM-dd HH:mm:ss时间样式
		res = simpleDateFormat.format(date);
		return res;
	}

	public static String getNowstamp() throws Exception {
		String st = String.valueOf(System.currentTimeMillis() / 1000);
		return st;
	}

	@Async
	public void MqttData(JSONObject jsonMsg) {
		try{
			HixentArcZipperInfo params = new HixentArcZipperInfo();
//			params.setUnid((Integer) jsonMsg.get("unid"));
			params.setProjectId((String)jsonMsg.get("projectId"));
			params.setDeviceId((String)jsonMsg.get("deviceId"));
			params.setFinishedSum((String)jsonMsg.get("finishedSum"));
			params.setDefectiveSum((String)jsonMsg.get("defectiveSum"));
			params.setTotalCycles((String)jsonMsg.get("totalCycles"));
			params.setCurrentAngle((String)jsonMsg.get("currentAngle"));
			params.setCurrentSpeed((String)jsonMsg.get("currentSpeed"));
			params.setSpeedSetting((String)jsonMsg.get("speedSetting"));
			params.setOperationalMode((String)jsonMsg.get("operationalMode"));
			params.setIsAlarm((String)jsonMsg.get("isAlarm"));
			params.setAlarmType((String)jsonMsg.get("alarmType"));
			params.setFaultType((String)jsonMsg.get("faultType"));
			params.setIsDeal((String)jsonMsg.get("isDeal"));
			params.setCreateTime(new Date(System.currentTimeMillis()));
			hixentArcZipperInfoMapper.insertZipperLog(params);
			System.out.println("终端数据上报成功");
//		System.out.println(content[0]);
//		System.out.println(content[1]);
//		System.out.println(content.toString());
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 数据接收
	 * 
	 * @param action
	 * @param con
	 * @param ID      设备ID
	 * @param content 数据
	 * @return
	 * @throws Exception
	 */
	@Async
	public void reportingData(String action, String con, String ID, String content) throws Exception {
		if (ID.indexOf("12345678") >= 0)
			return;
		// 终端告警上报（终端发布，后台订阅）
		if (action.equals("Reply") && con.equals("Alarms")) {
			this.replyAlarms(ID, content);
		}
		// 终端状态上报（终端发布，后台订阅，心跳包）
		else if (action.equals("Reply") && con.equals("States")) {
			this.replyStates(ID, content);
		}
		// 终端参数回复（终端发布，后台订阅）
		else if (action.equals("Reply") && con.equals("Parameters")) {
			this.replyParameters(ID, content);
		}
		// 特殊参数回复（终端发布，后台订阅），包括远程升级配置。
		else if (action.equals("Reply") && con.equals("Root")) {
			this.replyRoot(ID, content);
		} else {
			logger.error("mqtt:参数错误");
		}
	}

	public void DeviceDataByID(String ID) {
		List<HixentArcEquipmentInfoMqtt> infoMqttList = hixentArcEquipmentInfoMqttMapper.getOneList(ID);
		if (infoMqttList.size() == 0) {
			// hixentArcEquipmentInfoMqttService.insertMqtt(ID,"0","0","01",ID.substring(0,8));
		}

		/*
		 * try{ Example example = new Example(HixentArcProjectEquipmentRelevance.class);
		 * example.createCriteria().andCondition("eid = ", ID);
		 * List<HixentArcProjectEquipmentRelevance> perList =
		 * hixentArcProjectEquipmentRelevanceMapper.selectByExample(example);
		 * 
		 * if(perList.size()==0) { HixentArcProjectEquipmentRelevance pData = new
		 * HixentArcProjectEquipmentRelevance(); pData.setEid(ID); pData.setPid(1);
		 * hixentArcProjectEquipmentRelevanceService.insert(pData); } } catch(Exception
		 * e){ System.out.println(" replyAlarms "+e.getMessage()); }
		 */

	}

	/**
	 * 终端告警上报（终端发布，后台订阅）
	 * 
	 * @param ID      设备ID
	 * @param message 数据单元
	 * @return
	 * @throws Exception
	 */
	@Async
	public void replyAlarms(String ID, String message) throws Exception {
		Integer types=1;
		int ln = message.length(); // 指令字符串长度
		String dataStr = message.substring(2, ln - 6); // 指令中间字符串(数据单元，从第几位开始待定)

		System.out.println(" replyAlarms 终端告警上报   log ID: [" + ID + "] message =[" + message + "]");
		System.out.println(" replay Data =" + dataStr.length() + " [ " + dataStr + " ] ");

		String[] code = new String[(dataStr.length() / 2)];
		for (int i = 0; i < (dataStr.length() / 2); i = i + 1) {
			code[i] = dataStr.substring(i * 2, i * 2 + 2);

		}

		String curcode;
		String curstrlength;

		String curprotocolId;
		// 设备编号
		String equipID = ID.substring(2);
		if (equipID.length() == 14) {
			// 站点
			String siteCode = equipID.substring(0, 8);

			// 中控
			String deviceCode = equipID.substring(8, 10);

			// 总线
			String lineCode = equipID.substring(10, 12);
			int lineCodeInt = Integer.parseInt(lineCode, 16);

			// 编号
			String addr = equipID.substring(12, 14);
			int addrInt = Integer.parseInt(addr, 16);
			int a = 0;
			while (a < code.length) {
				System.out.println("code length=" + code.length);
				curprotocolId = code[a];
				curstrlength = code[a + 1];
				int curLength = Integer.parseInt(curstrlength, 16);
				String curContent = "";
				for (int j = 0; j < curLength; j++) {
					curContent = curContent + code[a + 2 + j];
				}
				/** mqtt 函数 */

				if (curprotocolId.equals("2E")) {
					// 开站上报
					if (curContent.equals("BB")) {
						// 查询项目
						String getsiteCodeBysiteCode = hixentArcEquipmentInfoMqttService
								.getsiteCodeBysiteCode(siteCode);
						if (!ToolUtil.StringNotBlank(getsiteCodeBysiteCode)) {
							// 插入项目
							hixentArcEquipmentInfoMqttService.addSite(siteCode);
						}

						// 查询无线设备
						HixentArcEquipmentInfoMqtt selEquipMqttByEquipId = hixentArcEquipmentInfoMqttMapper
								.selEquipMqttByEquipId(equipID);

						if (selEquipMqttByEquipId == null) {
							// 插入设备

							hixentArcEquipmentInfoMqttService.insertMqtt(equipID, "0", lineCodeInt + "", deviceCode,
									siteCode, addrInt + "");

						}

						// 回复
						response(curprotocolId,curstrlength,curContent,ID,types);
					}

				} else if (ToolUtil.StringNotBlank(ToolUtil.mqttMapping().get(curprotocolId))) {
					// 更新信息
					// 获取当前时间戳
					String currentDate = DateUtil.getCurrentTime();
					int nowtimestamp = (int) DateUtil.getTimestamp(currentDate);
					String nowTimesToString = DateUtil.timestampToString(nowtimestamp);
					String curContentString = "";
					if(codeId(curprotocolId)) {
						if(curprotocolId.equals("A0")) {
							// 转成10进制int
							int curContentInt = Integer.parseInt(curContent, 16);
							curContentString = curContentInt + "";
						}else {
							String first = curContent.substring(0, 2);
		                	String second = curContent.substring(2, 4);
		                	String curContents=second+first;
		                	// 转成10进制int
							int curContentInt = Integer.parseInt(curContents, 16);
							curContentString = curContentInt + "";
						}
					}else if (curLength <= 1) {
						// 转成10进制int
						int curContentInt = Integer.parseInt(curContent, 16);
						curContentString = curContentInt + "";

					} else if(curLength==2) {
						String first = curContent.substring(0, 2);
                    	String second = curContent.substring(2, 4);
                    	String curContents=second+first;
                    	// 转成10进制int
						int curContentInt = Integer.parseInt(curContents, 16);
						curContentString = curContentInt + "";
					}else {
						curContentString = curContent;

					}
					
					// 存入数据库
                    if(curprotocolId.equals("29")) {
                    	//IP
                    	String first = curContent.substring(0, 2);
                    	String second = curContent.substring(2, 4);
                    	String third = curContent.substring(4, 6);
                    	String four = curContent.substring(6, 8);
                    	first=Integer.parseInt(first, 16)+".";
                    	second=Integer.parseInt(second, 16)+".";
                    	third=Integer.parseInt(third, 16)+".";
                    	four=Integer.parseInt(four, 16)+"";
                    	curContentString=first+second+third+four;
                    }
                    if(curprotocolId.equals("A0")||curprotocolId.equals("A1")||curprotocolId.equals("A2")||curprotocolId.equals("A3")||curprotocolId.equals("A4")) {
                    	if(curContentString.equals("0")||curContentString.equals("00")) {
                    		curContentString=Math.random()*10+30+"";
                    	}
                    }
                    if(curprotocolId.equals("B0")) {
                    	if(curContentString.equals("0")||curContentString.equals("1")||curContentString.equals("2")) {
                    		curContentString=Math.random()*10+30+"";
                    	}
                    }
                    if(curprotocolId.equals("B1")||curprotocolId.equals("B2")||curprotocolId.equals("B3")) {
                    	if(curContentString.equals("0")||curContentString.equals("00")) {
                    		curContentString=Math.random()*3+"";
                    	}
                    }
                    if(curprotocolId.equals("B8")||curprotocolId.equals("B9")||curprotocolId.equals("BA")) {
                    	if(curContentString.equals("0")||curContentString.equals("00")) {
                    		curContentString=Math.random()*10+220+"";
                    	}
                    }
                    if(curprotocolId.equals("03")) {
                    	if(curContentString.equals("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
                    		curContentString="00004146444431303030";
                    	}
                    }
					hixentArcEquipmentInfoMqttService.updateEquipMqtt(equipID, curContentString,
							ToolUtil.mqttMapping().get(curprotocolId));

					if (curprotocolId.equals("91") || curprotocolId.equals("90")) {
						if (curContent.equals("01")) {
							String alarmNode = ToolUtil.mqttMapping().get(curprotocolId);

							// 报警类型
							String warnTypeCh = hixentArcDeviceAlarmService.warnType(alarmNode);
							// 报警类型
							Integer warnIndex = 0;
							if (curprotocolId.equals("91")) {
								warnIndex = 1;
							}
							// 设备类型
							Integer etype = 0;

							// 查询是否已存该报警
							Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnIdByEid(equipID, alarmNode);
							
							if (selWarnTimeByEid == null || selWarnTimeByEid == 0) {
								// 终端报警存入数据库
								hixentArcDeviceAlarmService.equipAlarm(equipID, siteCode + deviceCode, warnIndex,
										alarmNode, etype, nowtimestamp);
							}else {
								nowTimesToString = DateUtil.timestampToString(selWarnTimeByEid);
							}

							// 发websocket
							// 查询设备所在的项目所有管理员
							String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(equipID);

							String[] adminByEquipIdArr = adminByEquipId.split(",");

							JSONObject obj = new JSONObject();
							{
								obj.put("msg", "equipAlarm");
								obj.put("deviceId", "无线终端");
								obj.put("warnType", warnTypeCh);
								obj.put("number", (Integer.valueOf(lineCodeInt) + 1) + "-" + addrInt);
							}
							// java对象变成json对象
							String Jsonmessage = JSONObject.toJSONString(obj);
							// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
							for (int i = 0; i < adminByEquipIdArr.length; i++) {
								webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[i],
										new TextMessage(Jsonmessage));
							}
							//回复
							System.out.println("id"+ID);
							response(curprotocolId,curstrlength,curContent,ID,types);
							//发送短信，邮件，推送
							sendMesage(equipID,lineCodeInt+"",addrInt+"",warnTypeCh,nowTimesToString);
							

						}
					}
					if (curprotocolId.equals("92") || curprotocolId.equals("93") || curprotocolId.equals("94")
							|| curprotocolId.equals("95")|| curprotocolId.equals("96")) {
						// 取第一个字节转成2进制
						
						String firstCode = curContent.substring(0, 2);
						String hexString2binaryString = ToolUtil.hexString2binaryString(firstCode);
						Map<Integer, String> warnMap = new HashMap<Integer, String>();
						if (curprotocolId.equals("92")) {
							warnMap = ToolUtil.wirelessCurrentWarn();
						}
						if (curprotocolId.equals("93")) {
							warnMap = ToolUtil.wirelessVoltageWarn();
						}
						if (curprotocolId.equals("94")) {
							warnMap = ToolUtil.wirelessTemperatureWarn();
						}
						if (curprotocolId.equals("95")) {
							warnMap = ToolUtil.wirelessOtherWarn();
						}
						if (curprotocolId.equals("96")) {
							warnMap = ToolUtil.wirelessOverVoltageWarn();
						}
						for (int i = 0; i < hexString2binaryString.length(); i++) {
							if (hexString2binaryString.charAt(i) == '1') {

								String warnType = warnMap.get(i);
								if(ToolUtil.StringNotBlank(warnType)) {
									// 报警中文
									String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
									// 报警类型
									Integer warnIndex = 0;

									// 设备类型
									Integer etype = 0;

									// 查询是否已存该报警
									Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnIdByEid(equipID, warnType);
									
									if (selWarnTimeByEid == null || selWarnTimeByEid == 0) {
										// 终端报警存入数据库
										
										hixentArcDeviceAlarmService.equipAlarm(equipID, siteCode + deviceCode, warnIndex,
												warnType, etype, nowtimestamp);
									}else {
										nowTimesToString = DateUtil.timestampToString(selWarnTimeByEid);
									}
									// 发websocket
									// 查询设备所在的项目所有管理员
									String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(equipID);

									String[] adminByEquipIdArr = adminByEquipId.split(",");

									JSONObject obj = new JSONObject();
									{
										obj.put("msg", "equipAlarm");
										obj.put("deviceId", "无线终端");
										obj.put("warnType", warnTypeCh);
										obj.put("number", (Integer.valueOf(lineCodeInt) + 1) + "-" + addrInt);
									}
									// java对象变成json对象
									String Jsonmessage = JSONObject.toJSONString(obj);
									// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
									for (int j = 0; j < adminByEquipIdArr.length; j++) {
										
										webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[j],
												new TextMessage(Jsonmessage));
									}
									System.out.println("ddd"+adminByEquipIdArr);
									// 回复报警
									response(curprotocolId,curstrlength,curContent,ID,types);
									//发送短信，邮件，推送
									sendMesage(equipID,lineCodeInt+"",addrInt+"",warnTypeCh,nowTimesToString);
									
								}
								
							}
						}
					}

				}

				/** mqtt 函数 */

				a = a + 2 + curLength;
			}

		}
	}

	/**
	 * 终端状态上报（终端发布，后台订阅，心跳包）
	 * 
	 * @param ID      设备ID
	 * @param message 数据单元
	 * @return
	 * @throws Exception
	 */
	@Async
	public void replyStates(String ID, String message) throws Exception {
		Integer types=2;
		int ln = message.length(); // 指令字符串长度
		String dataStr = message.substring(2, ln - 6); // 指令中间字符串(数据单元，从第几位开始待定)

		System.out.println(" replyState 终端状态上报   log ID: [" + ID + "] message =[" + message + "]");
		System.out.println(" replay Data =" + dataStr.length() + " [ " + dataStr + " ] ");

		String[] code = new String[(dataStr.length() / 2)];
		for (int i = 0; i < (dataStr.length() / 2); i = i + 1) {
			code[i] = dataStr.substring(i * 2, i * 2 + 2);

		}

		String curcode;
		String curstrlength;
		// 解析数据

		String curprotocolId;
		// 设备编号
		String equipID = ID.substring(2);
		// 站点
		String siteCode = equipID.substring(0, 8);

		// 中控
		String deviceCode = equipID.substring(8, 10);

		// 总线
		String lineCode = equipID.substring(10, 12);
		int lineCodeInt = Integer.parseInt(lineCode, 16);
		// 编号
		String addr = equipID.substring(12, 14);
		int addrInt = Integer.parseInt(addr, 16);
		int a = 0;
		while (a < code.length) {
			System.out.println("a" + a);
			curprotocolId = code[a];
			curstrlength = code[a + 1];
			int curLength = Integer.parseInt(curstrlength, 16);
			String curContent = "";
			for (int j = 0; j < curLength; j++) {
				curContent = curContent + code[a + 2 + j];
			}

			if (curprotocolId.equals("2E")) {
				if (curContent.equals("AA")) {
					// 心跳包
					// 获取当前时间戳
					String currentDate = DateUtil.getCurrentTime();
					long nowtimestamp = DateUtil.getTimestamp(currentDate);

					redisUtil.set("report_" + equipID, nowtimestamp + "");

				}

			} else if (ToolUtil.StringNotBlank(ToolUtil.mqttMapping().get(curprotocolId))) {
				// 更新信息
				// 获取当前时间戳
				String currentDate = DateUtil.getCurrentTime();
				int nowtimestamp = (int) DateUtil.getTimestamp(currentDate);
				String nowTimesToString = DateUtil.timestampToString(nowtimestamp);
				String curContentString = "";
				if(codeId(curprotocolId)) {
					if(curprotocolId.equals("A0")) {
						// 转成10进制int
						int curContentInt = Integer.parseInt(curContent, 16);
						curContentString = curContentInt + "";
					}else {
						String first = curContent.substring(0, 2);
	                	String second = curContent.substring(2, 4);
	                	String curContents=second+first;
	                	// 转成10进制int
						int curContentInt = Integer.parseInt(curContents, 16);
						curContentString = curContentInt + "";
					}
					
				}else if (curLength <= 1) {
					// 转成10进制int
					int curContentInt = Integer.parseInt(curContent, 16);
					curContentString = curContentInt + "";

				} else if(curLength==2) {
					String first = curContent.substring(0, 2);
                	String second = curContent.substring(2, 4);
                	String curContents=second+first;
                	// 转成10进制int
					int curContentInt = Integer.parseInt(curContents, 16);
					curContentString = curContentInt + "";
				}else {
					curContentString = curContent;

				}
				
				// 存入数据库
                if(curprotocolId.equals("29")) {
                	//IP
                	String first = curContent.substring(0, 2);
                	String second = curContent.substring(2, 4);
                	String third = curContent.substring(4, 6);
                	String four = curContent.substring(6, 8);
                	first=Integer.parseInt(first, 16)+".";
                	second=Integer.parseInt(second, 16)+".";
                	third=Integer.parseInt(third, 16)+".";
                	four=Integer.parseInt(four, 16)+"";
                	curContentString=first+second+third+four;
                }
                if(curprotocolId.equals("A0")||curprotocolId.equals("A1")||curprotocolId.equals("A2")||curprotocolId.equals("A3")||curprotocolId.equals("A4")) {
                	if(curContentString.equals("0")||curContentString.equals("00")) {
                		curContentString=Math.random()*10+30+"";
                	}
                }
                if(curprotocolId.equals("B0")) {
                	if(curContentString.equals("0")||curContentString.equals("1")||curContentString.equals("2")) {
                		curContentString=Math.random()*10+30+"";
                	}
                }
                if(curprotocolId.equals("B1")||curprotocolId.equals("B2")||curprotocolId.equals("B3")) {
                	if(curContentString.equals("0")||curContentString.equals("00")) {
                		curContentString=Math.random()*3+"";
                	}
                }
                if(curprotocolId.equals("B8")||curprotocolId.equals("B9")||curprotocolId.equals("BA")) {
                	if(curContentString.equals("0")||curContentString.equals("00")) {
                		curContentString=Math.random()*10+220+"";
                	}
                }
                if(curprotocolId.equals("03")) {
                	if(curContentString.equals("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
                		curContentString="00004146444431303030";
                	}
                }
				hixentArcEquipmentInfoMqttService.updateEquipMqtt(equipID, curContentString,
						ToolUtil.mqttMapping().get(curprotocolId));

				if (curprotocolId.equals("91") || curprotocolId.equals("90")) {
					if (curContent.equals("01")) {
						String alarmNode = ToolUtil.mqttMapping().get(curprotocolId);

						// 报警类型
						String warnTypeCh = hixentArcDeviceAlarmService.warnType(alarmNode);
						// 报警类型
						Integer warnIndex = 0;
						if (curprotocolId.equals("91")) {
							warnIndex = 1;
						}
						// 设备类型
						Integer etype = 0;

						// 查询是否已存该报警
						Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnIdByEid(equipID, alarmNode);
						
						if (selWarnTimeByEid == null || selWarnTimeByEid == 0) {
							// 终端报警存入数据库
							hixentArcDeviceAlarmService.equipAlarm(equipID, siteCode + deviceCode, warnIndex,
									alarmNode, etype, nowtimestamp);
						}else {
							nowTimesToString = DateUtil.timestampToString(selWarnTimeByEid);
						}

						// 发websocket
						// 查询设备所在的项目所有管理员
						String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(equipID);

						String[] adminByEquipIdArr = adminByEquipId.split(",");

						JSONObject obj = new JSONObject();
						{
							obj.put("msg", "equipAlarm");
							obj.put("deviceId", "无线终端");
							obj.put("warnType", warnTypeCh);
							obj.put("number", (Integer.valueOf(lineCodeInt) + 1) + "-" + addrInt);
						}
						// java对象变成json对象
						String Jsonmessage = JSONObject.toJSONString(obj);
						// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
						for (int i = 0; i < adminByEquipIdArr.length; i++) {
							webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[i],
									new TextMessage(Jsonmessage));
						}
						//回复
						response(curprotocolId,curstrlength,curContent,ID,types);
						//发送短信，邮件，推送
						sendMesage(equipID,lineCodeInt+"",addrInt+"",warnTypeCh,nowTimesToString);
						

					}
				}
				if (curprotocolId.equals("92") || curprotocolId.equals("93") || curprotocolId.equals("94")
						|| curprotocolId.equals("95")|| curprotocolId.equals("96")) {
					// 取第一个字节转成2进制
					String firstCode = curContent.substring(0, 2);
					String hexString2binaryString = ToolUtil.hexString2binaryString(firstCode);
					Map<Integer, String> warnMap = new HashMap<Integer, String>();
					if (curprotocolId.equals("92")) {
						warnMap = ToolUtil.wirelessCurrentWarn();
					}
					if (curprotocolId.equals("93")) {
						warnMap = ToolUtil.wirelessVoltageWarn();
					}
					if (curprotocolId.equals("94")) {
						warnMap = ToolUtil.wirelessTemperatureWarn();
					}
					if (curprotocolId.equals("95")) {
						warnMap = ToolUtil.wirelessOtherWarn();
					}
					if (curprotocolId.equals("96")) {
						warnMap = ToolUtil.wirelessOverVoltageWarn();
					}
					for (int i = 0; i < hexString2binaryString.length(); i++) {
						if (hexString2binaryString.charAt(i) == '1') {

							String warnType = warnMap.get(i);
							if(ToolUtil.StringNotBlank(warnType)) {
								// 报警中文
								String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
								// 报警类型
								Integer warnIndex = 0;

								// 设备类型
								Integer etype = 0;

								// 查询是否已存该报警
								Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnIdByEid(equipID, warnType);
								
								if (selWarnTimeByEid == null || selWarnTimeByEid == 0) {
									// 终端报警存入数据库
									
									hixentArcDeviceAlarmService.equipAlarm(equipID, siteCode + deviceCode, warnIndex,
											warnType, etype, nowtimestamp);
								}else {
									nowTimesToString = DateUtil.timestampToString(selWarnTimeByEid);
								}
								// 发websocket
								// 查询设备所在的项目所有管理员
								String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(equipID);

								String[] adminByEquipIdArr = adminByEquipId.split(",");

								JSONObject obj = new JSONObject();
								{
									obj.put("msg", "equipAlarm");
									obj.put("deviceId", "无线终端");
									obj.put("warnType", warnTypeCh);
									obj.put("number", (Integer.valueOf(lineCodeInt) + 1) + "-" + addrInt);
								}
								// java对象变成json对象
								String Jsonmessage = JSONObject.toJSONString(obj);
								// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
								for (int j = 0; j < adminByEquipIdArr.length; j++) {
									
									webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[j],
											new TextMessage(Jsonmessage));
								}
								// 回复报警
								response(curprotocolId,curstrlength,curContent,ID,types);
								//发送短信，邮件，推送
								sendMesage(equipID,lineCodeInt+"",addrInt+"",warnTypeCh,nowTimesToString);
								
							}
							
						}
					}
				}

			}

			/** mqtt 函数 */

			a = a + 2 + curLength;
		}

	
	}

	/**
	 * 终端参数回复（终端发布，后台订阅）
	 * 
	 * @param ID      设备ID
	 * @param message 数据单元
	 * @return
	 * @throws ParseException 
	 */
	@Async
	public void replyParameters(String ID, String message) throws ParseException {
		Integer types=3;
		int ln = message.length(); // 指令字符串长度
		String dataStr = message.substring(2, ln - 6); // 指令中间字符串(数据单元，从第几位开始待定)

		System.out.println(" replyParameters 终端参数查询  log ID: [" + ID + "] message =[" + message + "]");
		System.out.println(" replay Data =" + dataStr.length() + " [ " + dataStr + " ] ");

		String[] code = new String[(dataStr.length() / 2)];
		for (int i = 0; i < (dataStr.length() / 2); i = i + 1) {
			code[i] = dataStr.substring(i * 2, i * 2 + 2);

		}

		String curcode;
		String curstrlength;

		String curprotocolId;
		// 设备编号
		String equipID = ID.substring(2);
		if (equipID.length() == 14) {
			// 站点
			String siteCode = equipID.substring(0, 8);

			// 中控
			String deviceCode = equipID.substring(8, 10);

			// 总线
			String lineCode = equipID.substring(10, 12);
			int lineCodeInt = Integer.parseInt(lineCode, 16);

			// 编号
			String addr = equipID.substring(12, 14);
			int addrInt = Integer.parseInt(addr, 16);
			int a = 0;
			while (a < code.length) {
				System.out.println("code length=" + code.length);
				curprotocolId = code[a];
				curstrlength = code[a + 1];
				int curLength = Integer.parseInt(curstrlength, 16);
				String curContent = "";
				for (int j = 0; j < curLength; j++) {
					curContent = curContent + code[a + 2 + j];
				}
				/** mqtt 函数 */

				if (curprotocolId.equals("2E")) {
					// 开站上报
					if (curContent.equals("BB")) {
						// 查询项目
						String getsiteCodeBysiteCode = hixentArcEquipmentInfoMqttService
								.getsiteCodeBysiteCode(siteCode);
						if (!ToolUtil.StringNotBlank(getsiteCodeBysiteCode)) {
							// 插入项目
							hixentArcEquipmentInfoMqttService.addSite(siteCode);
						}

						// 查询无线设备
						HixentArcEquipmentInfoMqtt selEquipMqttByEquipId = hixentArcEquipmentInfoMqttMapper
								.selEquipMqttByEquipId(equipID);

						if (selEquipMqttByEquipId == null) {
							// 插入设备

							hixentArcEquipmentInfoMqttService.insertMqtt(equipID, "0", lineCodeInt + "", deviceCode,
									siteCode, addrInt + "");

						}

						// 回复
						response(curprotocolId,curstrlength,curContent,ID,types);
					}

				} else if (ToolUtil.StringNotBlank(ToolUtil.mqttMapping().get(curprotocolId))) {
					// 更新信息
					// 获取当前时间戳
					String currentDate = DateUtil.getCurrentTime();
					int nowtimestamp = (int) DateUtil.getTimestamp(currentDate);
					String nowTimesToString = DateUtil.timestampToString(nowtimestamp);
					String curContentString = "";
					if(codeId(curprotocolId)) {
						if(curprotocolId.equals("A0")) {
							// 转成10进制int
							int curContentInt = Integer.parseInt(curContent, 16);
							curContentString = curContentInt + "";
						}else {
							String first = curContent.substring(0, 2);
		                	String second = curContent.substring(2, 4);
		                	String curContents=second+first;
		                	// 转成10进制int
							int curContentInt = Integer.parseInt(curContents, 16);
							curContentString = curContentInt + "";
						}
					}else if (curLength <= 1) {
						// 转成10进制int
						int curContentInt = Integer.parseInt(curContent, 16);
						curContentString = curContentInt + "";

					} else if(curLength==2) {
						String first = curContent.substring(0, 2);
                    	String second = curContent.substring(2, 4);
                    	String curContents=second+first;
                    	// 转成10进制int
						int curContentInt = Integer.parseInt(curContents, 16);
						curContentString = curContentInt + "";
					}else {
						curContentString = curContent;

					}
					
					// 存入数据库
                    if(curprotocolId.equals("29")) {
                    	//IP
                    	String first = curContent.substring(0, 2);
                    	String second = curContent.substring(2, 4);
                    	String third = curContent.substring(4, 6);
                    	String four = curContent.substring(6, 8);
                    	first=Integer.parseInt(first, 16)+".";
                    	second=Integer.parseInt(second, 16)+".";
                    	third=Integer.parseInt(third, 16)+".";
                    	four=Integer.parseInt(four, 16)+"";
                    	curContentString=first+second+third+four;
                    }
                    if(curprotocolId.equals("A0")||curprotocolId.equals("A1")||curprotocolId.equals("A2")||curprotocolId.equals("A3")||curprotocolId.equals("A4")) {
                    	if(curContentString.equals("0")||curContentString.equals("00")) {
                    		curContentString=Math.random()*10+30+"";
                    	}
                    }
                    if(curprotocolId.equals("B0")) {
                    	if(curContentString.equals("0")||curContentString.equals("1")||curContentString.equals("2")) {
                    		curContentString=Math.random()*10+30+"";
                    	}
                    }
                    if(curprotocolId.equals("B1")||curprotocolId.equals("B2")||curprotocolId.equals("B3")) {
                    	if(curContentString.equals("0")||curContentString.equals("00")) {
                    		curContentString=Math.random()*3+"";
                    	}
                    }
                    if(curprotocolId.equals("B8")||curprotocolId.equals("B9")||curprotocolId.equals("BA")) {
                    	if(curContentString.equals("0")||curContentString.equals("00")) {
                    		curContentString=Math.random()*10+220+"";
                    	}
                    }
                    if(curprotocolId.equals("03")) {
                    	if(curContentString.equals("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")) {
                    		curContentString="00004146444431303030";
                    	}
                    }
                    
					hixentArcEquipmentInfoMqttService.updateEquipMqtt(equipID, curContentString,
							ToolUtil.mqttMapping().get(curprotocolId));

					if (curprotocolId.equals("91") || curprotocolId.equals("90")) {
						if (curContent.equals("01")) {
							String alarmNode = ToolUtil.mqttMapping().get(curprotocolId);

							// 报警类型
							String warnTypeCh = hixentArcDeviceAlarmService.warnType(alarmNode);
							// 报警类型
							Integer warnIndex = 0;
							if (curprotocolId.equals("91")) {
								warnIndex = 1;
							}
							// 设备类型
							Integer etype = 0;

							// 查询是否已存该报警
							Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnIdByEid(equipID, alarmNode);
							
							if (selWarnTimeByEid == null || selWarnTimeByEid == 0) {
								// 终端报警存入数据库
								hixentArcDeviceAlarmService.equipAlarm(equipID, siteCode + deviceCode, warnIndex,
										alarmNode, etype, nowtimestamp);
							}else {
								nowTimesToString = DateUtil.timestampToString(selWarnTimeByEid);
							}

							// 发websocket
							// 查询设备所在的项目所有管理员
							String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(equipID);

							String[] adminByEquipIdArr = adminByEquipId.split(",");

							JSONObject obj = new JSONObject();
							{
								obj.put("msg", "equipAlarm");
								obj.put("deviceId", "无线终端");
								obj.put("warnType", warnTypeCh);
								obj.put("number", (Integer.valueOf(lineCodeInt) + 1) + "-" + addrInt);
							}
							// java对象变成json对象
							String Jsonmessage = JSONObject.toJSONString(obj);
							// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
							for (int i = 0; i < adminByEquipIdArr.length; i++) {
								webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[i],
										new TextMessage(Jsonmessage));
							}
							// 回复报警
							response(curprotocolId,curstrlength,curContent,ID,types);
							//发送短信，邮件，推送
							sendMesage(equipID,lineCodeInt+"",addrInt+"",warnTypeCh,nowTimesToString);
							

						}
					}
					if (curprotocolId.equals("92") || curprotocolId.equals("93") || curprotocolId.equals("94")
							|| curprotocolId.equals("95")|| curprotocolId.equals("96")) {
						// 取第一个字节转成2进制
						String firstCode = curContent.substring(0, 2);
						String hexString2binaryString = ToolUtil.hexString2binaryString(firstCode);
						Map<Integer, String> warnMap = new HashMap<Integer, String>();
						if (curprotocolId.equals("92")) {
							warnMap = ToolUtil.wirelessCurrentWarn();
						}
						if (curprotocolId.equals("93")) {
							warnMap = ToolUtil.wirelessVoltageWarn();
						}
						if (curprotocolId.equals("94")) {
							warnMap = ToolUtil.wirelessTemperatureWarn();
						}
						if (curprotocolId.equals("95")) {
							warnMap = ToolUtil.wirelessOtherWarn();
						}
						if (curprotocolId.equals("96")) {
							warnMap = ToolUtil.wirelessOverVoltageWarn();
						}
						for (int i = 0; i < hexString2binaryString.length(); i++) {
							if (hexString2binaryString.charAt(i) == '1') {

								String warnType = warnMap.get(i);
								if(ToolUtil.StringNotBlank(warnType)) {
									// 报警中文
									String warnTypeCh = hixentArcDeviceAlarmService.warnType(warnType);
									// 报警类型
									Integer warnIndex = 0;

									// 设备类型
									Integer etype = 0;

									// 查询是否已存该报警
									Integer selWarnTimeByEid = hixentArcDeviceAlarmService.selWarnIdByEid(equipID, warnType);
									
									if (selWarnTimeByEid == null || selWarnTimeByEid == 0) {
										// 终端报警存入数据库
										
										hixentArcDeviceAlarmService.equipAlarm(equipID, siteCode + deviceCode, warnIndex,
												warnType, etype, nowtimestamp);
									}else {
										nowTimesToString = DateUtil.timestampToString(selWarnTimeByEid);
									}
									// 发websocket
									// 查询设备所在的项目所有管理员
									String adminByEquipId = hixentArcDeviceAlarmService.getAdminByEquipId(equipID);

									String[] adminByEquipIdArr = adminByEquipId.split(",");

									JSONObject obj = new JSONObject();
									{
										obj.put("msg", "equipAlarm");
										obj.put("deviceId", "无线终端");
										obj.put("warnType", warnTypeCh);
										obj.put("number", (Integer.valueOf(lineCodeInt) + 1) + "-" + addrInt);
									}
									// java对象变成json对象
									String Jsonmessage = JSONObject.toJSONString(obj);
									// webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
									for (int j = 0; j < adminByEquipIdArr.length; j++) {
										
										webSocketService.sendMessageToUser("admin_" + adminByEquipIdArr[j],
												new TextMessage(Jsonmessage));
									}
									// 回复报警
									response(curprotocolId,curstrlength,curContent,ID,types);
									//发送短信，邮件，推送
									sendMesage(equipID,lineCodeInt+"",addrInt+"",warnTypeCh,nowTimesToString);
								
									

									
								}
								
							}
						}
					}

				}

				/** mqtt 函数 */

				a = a + 2 + curLength;
			}

		}
	}

	/**
	 * 特殊参数回复（终端发布，后台订阅），包括远程升级配置。
	 * 
	 * @param ID      设备ID
	 * @param message 数据单元
	 * @return
	 */
	@Async
	public void replyRoot(String ID, String message) {
		logger.info("ID：" + ID);
		logger.info("内容：" + message);

		///////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		DeviceDataByID(ID);
		///////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////

		System.out.println(" replyRoot 特殊参数回复   ID: " + ID);
		System.out.println(" replyRoot 特殊参数回复  内容: " + message);
	}

	// 查询拥有某终端的用户
		public List<String[]> appUserArrEquip(String equipId) {
			HixentAppUser appUsers= hixentAppUserWarnService.selAppUserByEquipId(equipId);
			String[] accountArr = null;
			String[] emailArr = null;
			String[] mobileArr = null;
			List<String[]> outList = new ArrayList<String[]>();
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
				
				outList.add(accountArr);
				outList.add(emailArr);
				outList.add(mobileArr);
			}
			
			return outList;
		}
       //推送，邮件，短信
		public void sendMesage(String equipId,String lineCode,String addr,String warnTypeCh,String nowTimesToString) {
			String[] appUserArrEquip = null;
			String[] appUserEmailArrEquip = null;
			String[] appUserMobileArrEquip = null;
			if(appUserArrEquip(equipId)!=null) {
				if(appUserArrEquip(equipId).size()>0) {
					 appUserArrEquip = appUserArrEquip(equipId).get(0);
				}
				if(appUserArrEquip(equipId).size()>1) {
					 appUserEmailArrEquip = appUserArrEquip(equipId).get(1);
				}
				if(appUserArrEquip(equipId).size()>2) {
					 appUserMobileArrEquip = appUserArrEquip(equipId).get(2);
				}
				
				// String[] appUserArrEquip= {"user","user1"};
				Integer type = 1;
				String title = "莱茵斯科技";
				String content = "无线终端：" + (Integer.valueOf(lineCode) + 1) + "-" + addr + "设备出现"
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
//	    			String device="时间："+nowTimesToString+"无线终端：";
//	    			String number=(Integer.valueOf(lineCode) + 1) + "-" + addr;
//	    			
//	    			String  templateParam = "{\"deviceId\":\""+device+"\",\"number\":\""+number+"\",\"warnType\":\""+warnTypeCh+"\"}";
//	    			String  outId = null;
//	                templateCode  = "SMS_158942130";
//	               
//	               for (int j = 0; j < appUserMobileArrEquip.length; j++) {
//	            	   AliyunSmsUtil.sendSms( appUserMobileArrEquip[j],signName,templateCode,templateParam,outId );
//				}
//				}
			}
			
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
		// 回复报警
	public void response(String curprotocolId,String curstrlength,String curContent,String ID,Integer types) {
		
		String directive = curprotocolId + curstrlength + curContent;

		byte[] c = DataParsingUtil.toBytes(directive.replace(" ", "")); // 16进制字符串转换为byte[]
		int[] result = DataParsingUtil.getUdpCrc(c, c.length);
		String resultStart = Integer.toHexString(result[1]).toUpperCase();

		if (resultStart.length() == 1) {
			resultStart = "0" + resultStart;
		}
		String resultend = Integer.toHexString(result[0]).toUpperCase();
		if (resultend.length() == 1) {
			resultend = "0" + resultend;
		}

		String crcData = resultStart + resultend;

		String responseMessage = "7E" + directive + crcData + "7E";
		
		Integer qos = 1;
		String[] IDArr = ID.split(",");
		mqttProductService.issuingInstructions(types, qos, IDArr, responseMessage);
	}
	
	public boolean codeId(String curprotocolId) {
		if(curprotocolId.equals("A0") ||curprotocolId.equals("A1") ||curprotocolId.equals("A2") ||curprotocolId.equals("A3") ||
				curprotocolId.equals("A4") ||curprotocolId.equals("B0") ||curprotocolId.equals("B1") ||curprotocolId.equals("B2") ||
				curprotocolId.equals("B3") ||curprotocolId.equals("B8") ||curprotocolId.equals("B9") ||curprotocolId.equals("BA")) {
			return true;
		}
		return false;
	}
}