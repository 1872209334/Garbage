package com.qf.controller.common;



import com.qf.model.fire.valid.ValidMqttIssuingInstructions;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

import com.qf.util.DataParsingUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.common.JwtConfig;
import com.qf.common.mqtt.MqttCommon;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcProtocol;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.fire.HixentArcProtocolService;
import com.qf.service.mqtt.MqttProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;



@RestController
@RequestMapping("/app")
public class MqttAppController {
	
	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

    @Autowired
    private MqttCommon mqttCommon;
    
    @Autowired
    private MqttProductService mqttProductService;
    
    @Autowired
    private HixentArcProtocolService hixentArcProtocolService;
    
    @Autowired
	private HixentAppUserService hixentAppUserService;
    
    /**
     * author Vareck
     */
    @RequestMapping(value = "/mqttRemoveWarning", method = RequestMethod.POST)
    public ModelMap mqttRemoveWarning(HttpServletRequest request, @Valid ValidMqttIssuingInstructions vmii , BindingResult bindingResult ) {
    	if ( bindingResult.hasErrors() ) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return ReturnUtil.Error("请选择填写相关数据！",allErrors.toArray());
        }else{
        	
        	String auth = request.getHeader(jwtConfig.getJwtHeader());
    		auth = auth.substring(7, auth.length());
    		Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
    		String userId = claims.getId();
    		String[] userArr = userId.split("_");
    		if (!userArr[0].equals("app")) {
    			return ReturnUtil.Error("已退出，请重新登录！");
    		}
    		HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
    		if (userinfo == null) {
    			return ReturnUtil.Error("已退出，请重新登录！");
    		}
        	
        	
    		 //指令下发
        	String  id_str     = vmii.getID();
        	String  message    = vmii.getMessage();
        	Integer qos        = vmii.getQos();
        	int type           = vmii.getType();
        	String  extraData     = vmii.getExtraData();
        	String[] ID        = id_str.split(",");
        	String[] extraDataArr  = extraData.split(",");
        	System.out.println("message:"+message);
        	String[] protocolArr = message.split(",");
        	String directive = "";
        	for(int i = 0;i < protocolArr.length;i++){
        		String pdu = "";
        		System.out.println("protocolArr:"+protocolArr[i]);
        		//获取指令数据单元
            	HixentArcProtocol pd = new HixentArcProtocol();
            	pd.setProtocolName(protocolArr[i]);
            	HixentArcProtocol pInfo = hixentArcProtocolService.selectOne(pd);
            	if(extraData.equals("")){
            		pdu = pInfo.getProtocolId()+pInfo.getProtocolLength()+pInfo.getProtocolValue(); 
            	}
            	else{
            		pdu = pInfo.getProtocolId()+pInfo.getProtocolLength()+extraDataArr[i]; 
            	}
            	directive += pdu;
        	}
         
        	System.out.println("directive:"+directive);
        	//CRC16数据校验
        	byte[] c = DataParsingUtil.toBytes(directive.replace(" ", ""));       //16进制字符串转换为byte[]
    		int[] result = DataParsingUtil.getUdpCrc(c,c.length);
    		String resultStart = Integer.toHexString(result[1]).toUpperCase();
    		if(resultStart.length() == 1){
    			resultStart = "0" + resultStart;
    		}
    		String resultend = Integer.toHexString(result[0]).toUpperCase();
    		if(resultend.length() == 1){
    			resultend = "0" + resultend;
    		}
    		String crcStr = resultStart + resultend;
        	//整合整条指令
        	directive = "7E"+directive+crcStr+"7E";
        	System.out.println("directive2:"+directive);
        	mqttProductService.issuingInstructions(type, qos, ID, directive);
        	return ReturnUtil.Success("指令发送成功！",crcStr);
        }
    }

    /**
     * author yelenn
     */
    @RequestMapping(value = "/mqttUpdateDeviceInfo", method = RequestMethod.POST)
    public ModelMap mqttUpdateDeviceInfo( @Valid ValidMqttIssuingInstructions vmii , BindingResult bindingResult ) {
    	String 		directive;
    	
    	if ( bindingResult.hasErrors() ) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return ReturnUtil.Error("请选择填写相关数据！",allErrors.toArray());
        }else{
	        //指令下发
        	String  id_str  = vmii.getID();
        	String  message = vmii.getMessage();
        	Integer qos     = vmii.getQos();
        	int 	type    = vmii.getType();
        	String[] ID     = id_str.split(",");
        	
        	System.out.println(" mqttUpdateDeviceInfo device = " + id_str +" message = "+message);

        	directive = SendMqttCmd(ID,"address_number");
        	System.out.println(" address_number = "+directive);
        	directive = SendMqttCmd(ID,"equipment_category");
        	System.out.println(" equipment_category = "+directive);
        	directive = SendMqttCmd(ID,"subtype");
        	System.out.println(" subtype = "+directive);
        	
        	directive = SendMqttCmd(ID,"version_information");
        	System.out.println(" version_information = "+directive);
        	directive = SendMqttCmd(ID,"arc_warning_sensitivity");
        	System.out.println(" arc_warning_sensitivity = "+directive);
        	directive = SendMqttCmd(ID,"arc_relay");
        	System.out.println(" arc_relay = "+directive);
        	
        	directive = SendMqttCmd(ID,"over_temperature_relay");
        	System.out.println(" over_temperature_relay = "+directive);
        	directive = SendMqttCmd(ID,"leakage_relay");
        	System.out.println(" leakage_relay = "+directive);
        	directive = SendMqttCmd(ID,"arc_time_range");
        	System.out.println(" arc_time_range = "+directive);
        	
        	directive = SendMqttCmd(ID,"arc_number");
        	System.out.println(" arc_number = "+directive);
        	directive = SendMqttCmd(ID,"device_descriptor");
        	System.out.println(" device_descriptor = "+directive);
        	directive = SendMqttCmd(ID,"overtemperature_relay_enabler_on");
        	System.out.println(" overtemperature_relay_enabler_on = "+directive);    
        	
        	directive = SendMqttCmd(ID,"arc_relay_enabler_on");
        	System.out.println(" arc_relay_enabler_on = "+directive);  
        	directive = SendMqttCmd(ID,"leakage_relay_enabler_on"); 
        	System.out.println(" leakage_relay_enabler_on = "+directive); 

        	return ReturnUtil.Success("指令发送成功！","");
        }
    }

    public String SendMqttCmd(String[] ID,String message)
    {
        //获取指令数据单元
    	HixentArcProtocol 	pd 		  = new HixentArcProtocol();
    	pd.setProtocolName(message);
    	HixentArcProtocol 	pInfo	  = hixentArcProtocolService.selectOne(pd);
    	String 				directive = pInfo.getProtocolId()+pInfo.getProtocolLength()+pInfo.getProtocolValue(); 
    	//CRC16数据校验
    	        	
     	byte[] c 			= DataParsingUtil.toBytes(directive.replace(" ", ""));       	//16进制字符串转换为byte[]
		int[] result 		= DataParsingUtil.getUdpCrc(c,c.length);
		String resultStart 	= Integer.toHexString(result[1]).toUpperCase();
		if(resultStart.length() == 1){
			resultStart 	= "0" + resultStart;
		}
		String resultend 	= Integer.toHexString(result[0]).toUpperCase();
		if(resultend.length() == 1){
			resultend 		= "0" + resultend;
		}
		String crcStr 		= resultStart + resultend;
    	
    	//整合整条指令
    	directive 			= "7E"+directive+crcStr+"7E";
    	for(int i=0;i<ID.length;i++){
        	String topic = "ArcDetector/"+ID[i]+"/Set/Parameters";
        	mqttCommon.sendToMqtt(directive,topic);
        	System.out.println("终端参数设置 setParameters : topic="+topic+" directive= "+directive);
    	}

    	return directive;
    }
    
}