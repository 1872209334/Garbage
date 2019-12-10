package com.qf.controller.common;



import com.qf.common.JwtConfig;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcProtocol;
import com.qf.model.fire.valid.ValidMqttIssuingInstructions;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

import com.qf.util.DataParsingUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcProtocolService;
import com.qf.service.mqtt.MqttProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
@RequestMapping("/api")
public class MqttController {

	
	
    @Autowired
    private MqttProductService mqttProductService;
    
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
    
    /**
     * author Vareck
     */
    @Async
    @RequestMapping(value = "/mqttIssuingInstructions", method = RequestMethod.POST)
    public ModelMap mqttIssuingInstructions(HttpServletRequest request, @Valid ValidMqttIssuingInstructions vmii , BindingResult bindingResult ) {
    	if ( bindingResult.hasErrors() ) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return ReturnUtil.Error("请选择填写相关数据！",allErrors.toArray());
        }else{
        	
        	String auth      = request.getHeader(jwtConfig.getJwtHeader());
        	System.out.println("auth"+auth);
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
            	   System.out.println("pdu"+pdu);
            	   System.out.println("pInfo"+pInfo.getProtocolId());
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
        	System.out.println("TYPESSS"+type);
        	mqttProductService.issuingInstructions(type, qos, ID, directive);
        	return ReturnUtil.Success("指令发送成功！",crcStr);
        }
    }

    /**
     * author Vareck
     */
    @Async
    @RequestMapping(value = "/setMqttIssuingInstructions", method = RequestMethod.POST)
    public ModelMap setMqttIssuingInstructions(HttpServletRequest request, @Valid ValidMqttIssuingInstructions vmii , BindingResult bindingResult ) {
    	if ( bindingResult.hasErrors() ) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            return ReturnUtil.Error("请选择填写相关数据！",allErrors.toArray());
        }else{
        	
        	String auth      = request.getHeader(jwtConfig.getJwtHeader());
        	System.out.println("auth"+auth);
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
            	if(pInfo.getProtocolLength().equals("01")) {
            		Integer length = hexToten(pInfo.getProtocolLength());
            		String tenToHex = TenToHex(extraDataArr[i],length);
            		pdu = pInfo.getProtocolId()+pInfo.getProtocolLength()+tenToHex; 
            		
            	}else {
            		Integer length = hexToten(pInfo.getProtocolLength());
            		String strTohex = strTohex(extraDataArr[i],length);
            		if(strTohex.equals("数据长度过长")) {
            			return ReturnUtil.Error(strTohex);	
            		}
            		pdu = pInfo.getProtocolId()+pInfo.getProtocolLength()+strTohex; 
            	}
//            	if(extraData.equals("")){
//            		pdu = pInfo.getProtocolId()+pInfo.getProtocolLength()+pInfo.getProtocolValue(); 
//            	}
//            	else{
//            		pdu = pInfo.getProtocolId()+pInfo.getProtocolLength()+extraDataArr[i]; 
//            	}
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
  //字符串转16进制
   public String strTohex(String s,Integer length) {
	   String ss = s;
	    byte[] bt = new byte[0];

	    try {
	        bt = ss.getBytes("GBK");
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    String s1 = "";
	    for (int i = 0; i < bt.length; i++)
	    {
	        String tempStr = Integer.toHexString(bt[i]);
	        if (tempStr.length() > 2)
	            tempStr = tempStr.substring(tempStr.length() - 2);
	        s1 = s1 + tempStr + "";
	    }
	   
	     String upperCase = s1.toUpperCase();
		
		 String trim = upperCase.toString().trim();
		 
		if(trim.length()>=length*2) {
			return "数据长度过长";
		}else {
			for (int i = trim.length(); i < length*2; i++) {
				trim=trim+"0" ;
			}
		}
		trim = trim.toUpperCase();
		 return trim;
   }
   
   //16进制转10进制
   public  Integer hexToten(String lengthStr) {
	   return    Integer.parseInt(lengthStr,16);
   }
   
   //10进制转16进制
   public String TenToHex(String ten,Integer length) {
	   int parseInt = Integer.parseInt(ten);
	   return   String.format("%0"+length+"x",parseInt);
   }
}