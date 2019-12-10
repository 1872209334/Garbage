package com.qf.controller.common;

import com.qf.util.DataParsingUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcProtocol;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcProtocolService;
import com.qf.service.socket.SocketService;

@RestController
@RequestMapping("/api")
public class SocketAdminController {

	@Autowired
	private SocketService socketService;

	@Autowired
	private HixentArcProtocolService hixentArcProtocolService;

	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	// 当前消息id
	private static int messageId = 0;
	// 最大消息id
	private static int maxMessageId = 32767;
	// 消息id字符串长度
	private static int messageLen = 4;
	// 站点编号字符串长度
	private static int siteCodeLen = 8;

	/**
	 * 通过socket下发指令 author wjr
	 */
	//@SystemHistoryAnnotation(opration = "管理员通过socket下发指令") 
	@RequestMapping(value = "/sendAdminSocket", method = RequestMethod.POST)
	public ModelMap sendSocketMessage(HttpServletRequest request) throws IOException {
		
		String auth      = request.getHeader(jwtConfig.getJwtHeader());
    	auth             = auth.substring(7, auth.length());
    	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
        String userId    = claims.getId();
        String[] userArr = userId.split("_");
        if( !userArr[0].equals("admin") ){
        	return ReturnUtil.Error("已退出，请重新登录！");
        }
    	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
        if(userinfo == null){
        	return ReturnUtil.Error("已退出，请重新登录！");
        }
		
		String protocolName = request.getParameter("protocolName");
		String siteCode = request.getParameter("siteCode");
		String deviceCode = request.getParameter("deviceCode");
		String extraData = request.getParameter("extraData");
		System.out.println("extraData"+extraData);
		System.out.println("deviceCode"+deviceCode);
		String messageStr = packageMessage(protocolName, siteCode, deviceCode, extraData);
		socketService.sendSocket(messageStr);
		System.out.println("message:" + messageStr);
		return ReturnUtil.Success("发送成功！");
	}
	/**
	 * 通过socket下发指令 author wjr
	 */
	//@SystemHistoryAnnotation(opration = "app通过socket下发指令") 
	@RequestMapping(value = "/sendSocketMessageForStringtoHex", method = RequestMethod.POST)
	public ModelMap sendSocketMessageForStringtoHex(HttpServletRequest request) throws IOException {
		

		String auth      = request.getHeader(jwtConfig.getJwtHeader());
    	auth             = auth.substring(7, auth.length());
    	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
        String userId    = claims.getId();
        String[] userArr = userId.split("_");
        if( !userArr[0].equals("admin") ){
        	return ReturnUtil.Error("已退出，请重新登录！");
        }
    	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
        if(userinfo == null){
        	return ReturnUtil.Error("已退出，请重新登录！");
        }
		
		
		String protocolName = request.getParameter("protocolName");
    	String siteCode = request.getParameter("siteCode");
    	String deviceCode = request.getParameter("deviceCode");
    	String extraData = request.getParameter("extraData");
    	String hexExtraData = ToolUtil.str2HexStr(extraData);
    	for (int i = hexExtraData.length(); i < 80; i++) {
    		hexExtraData=hexExtraData+"0";
		}
    	System.out.println("hexExtraData"+hexExtraData);
    	String messageStr = packageMessage(protocolName,siteCode,deviceCode,hexExtraData);
    	socketService.sendSocket(messageStr);
    	
    	return ReturnUtil.Success("发送成功！");
	}
	/**
	 * 组成udp数据包 author wjr
	 */
	private String packageMessage(String protocolName, String siteCodeStr, String deviceCode, String extraData) {
		String messageHeader = "0101";
		// 生成站点编号，低字节在前，高字节在后
		String siteCode = "";
		for (int i = siteCodeLen - 2; i >= 0; i -= 2) {
			siteCode += siteCodeStr.substring(i, i + 2);
		}
		siteCode = siteCode.toUpperCase();
		// 生成通信报标识号
		String messageId = createMessageId();
		String messageFix = "8001";

		// 获取指令数据单元
		HixentArcProtocol pd = new HixentArcProtocol();
		/*****************/
		// 分割协议字符串
		String[] protocolArr = protocolName.split(",");
		String[] extraArr = extraData.split(",");
		// 取第一条协议判断命令标识
		pd.setProtocolName(protocolArr[0]);
		
		HixentArcProtocol pone = hixentArcProtocolService.selectOne(pd);
		
		String commandMark = pone.getProtocolAttribute();
		String responseMark = "";
		if (commandMark.equals("02") || commandMark.equals("03")) {
			responseMark = "FF";
		} else {
			responseMark = "00";
		}
		// 完整pdu
		String pdu = "";
		for (int i = 0; i < protocolArr.length; i++) {
			// 取所有协议
			pd.setProtocolName(protocolArr[i]);
			HixentArcProtocol pInfo = hixentArcProtocolService.selectOne(pd);
			// 监控标识号
			String protocolId = "";
			// 低字节在前，高字节在后
			for (int j = 4 - 2; j >= 0; j -= 2) {
				protocolId += pInfo.getProtocolId().substring(j, j + 2);
			}
			// 取协议长度
			String protocolLength = pInfo.getProtocolLength();
			System.out.println("protocolLength"+protocolLength);
			// 取协议数值
			String protocolValue = pInfo.getProtocolValue();
			if (protocolValue == null) {
				protocolValue = "";
			}
			// 查询不需额外数据
			String pduOne = "";
			if (commandMark.equals("02")) {
				pduOne = protocolId + protocolLength + protocolValue;
			}
			// 设置类指令需加额外数据
			else if (commandMark.equals("03")) {
//    			String extraStr = "";
//            	System.out.println("extraArr[i]:"+extraArr[i]);
//            	for (int j = 0; j < extraArr.length; j++) {
//            		extraStr += extraArr[j].toUpperCase();
//				}
				extraArr[i] = extraArr[i].toUpperCase();
				pduOne = protocolId + protocolLength + protocolValue + extraArr[i];
			}
			pdu += pduOne;
		}
		System.out.println("pdu:" + pdu);
		String directive = messageHeader + siteCode + deviceCode + "0000" + messageFix + commandMark + responseMark
				+ pdu;
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
		String messageStr = "7E" + messageHeader + siteCode + deviceCode + "0000" + messageFix + commandMark
				+ responseMark + pdu + crcData + "7E";
		System.out.println("messageStr:" + messageStr);
		return messageStr;
		/*****************/
		/*****************/
//    	pd.setProtocolName(protocolName);
//    	HixentArcProtocol pInfo = hixentArcProtocolService.selectOne(pd);
//    	String commandMark = pInfo.getProtocolAttribute();
//     	String responseMark = "";
//     	if(commandMark.equals("02") || commandMark.equals("03")){
//     		responseMark = "FF";
//     	}
//     	else{
//     		responseMark = "00";
//     	}
		/*****************/
		// 监控标识号
//    	String protocolId = "";
//    	//低字节在前，高字节在后
//		for(int i = 4 - 2;i >= 0;i-=2){
//			protocolId += pInfo.getProtocolId().substring(i,i + 2);
//		}
//		String protocolLength = pInfo.getProtocolLength();
//		String protocolValue = pInfo.getProtocolValue();
//		if(protocolValue == null){
//			protocolValue = "";
//		}
//    	String protocolMark = protocolId + protocolLength + protocolValue; 
//     	
//     	if(extraData == null){
//     		extraData = "";
//     	}
//     	String directive = messageHeader + siteCode + deviceCode + "0000" + messageFix + commandMark + responseMark + protocolMark + extraData;
//     	byte[] c = DataParsingUtil.toBytes(directive.replace(" ", ""));       //16进制字符串转换为byte[]
//		int[] result = DataParsingUtil.getUdpCrc(c,c.length);
//		String crcData = Integer.toHexString(result[1]).toUpperCase() + Integer.toHexString(result[0]).toUpperCase();
//    	String messageStr = "7E" + messageHeader + siteCode + deviceCode + "0000" + messageFix + commandMark + responseMark + protocolMark + extraData + crcData + "7E";
//    	return messageStr;
	}

	/**
	 * 生成消息id author wjr
	 */
	private String createMessageId() {
		if (messageId > maxMessageId) {
			messageId = 0;
		}
		String str = Integer.toHexString(messageId);
		int num = messageLen - str.length();
		for (int i = 0; i < num; i++) {
			str = "0" + str;
		}
		String idStr = "";
		// 低字节在前，高字节在后
		for (int i = messageLen - 2; i >= 0; i -= 2) {
			idStr += str.substring(i, i + 2);
		}
		idStr = idStr.toUpperCase();
		messageId++;
		return idStr;
	}

}