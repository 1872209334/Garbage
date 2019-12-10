package com.qf.service.mqtt;



import com.qf.common.mqtt.MqttCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



/**
 * mqtt通道生产者服务
 * @author Vareck
 */
@Service
public class MqttProductService {

	
	
    @Autowired
    private MqttCommon mqttCommon;
    
    

    /**
     * 指令下发
     * @param  qos         
     * @param  type       指令类型
     * @param  ID         设备ID集
     * @param  directive  数据单元
     * @return 
     */
    @Async
    public void issuingInstructions(int type,int qos,String[] ID,String directive) {
    	System.out.println("TYPE"+type);
    	switch(type){
	    	case 1:
	    		this.setAlarms(qos,ID,directive);
	    		break;
	    	case 2:
	    		this.setStates(qos, ID, directive);
	    	    break;
	    	case 3:
	    		this.setParameters(qos, ID, directive);
	    		break;
	    	case 4:
	    		this.setRoot(qos, ID, directive);
	    		break;
	    	case 5:
	    		this.queryParameters(qos, ID, directive);
	    		break;
	    	default:
	    		this.setAlarms(qos,ID,directive);
	    	    break;
    	}
    }
    
    
    
    /**
     * 告警确认（后台发布，终端订阅）
     * @param  qos       
     * @param  ID         设备ID集
     * @param  directive  数据单元
     * @return 
     */
    @Async
    public void setAlarms(int qos,String[] ID,String directive) {
    	for(int i=0;i<ID.length;i++){
        	String topic = "ArcDetector/"+ID[i]+"/Set/Alarms"; 
        	System.out.println("d"+ID);
        	System.out.println("topic"+topic);
        	mqttCommon.sendToMqtt(directive,topic);
    	}
    }

	
	
    /**
     * 终端状态回复（后台发布，终端订阅，心跳包回复）
     * @param  qos         
     * @param  ID         设备ID集
     * @param  directive  数据单元
     * @return 
     */
    @Async
	public void setStates(int qos,String[] ID,String directive) {
    	for(int i=0;i<ID.length;i++){
        	String topic = "ArcDetector/"+ID[i]+"/Set/States";         
        	mqttCommon.sendToMqtt(directive,topic);
    	}
    	
    }
	
	
	
    /**
     * 终端参数设置（后台发布，终端订阅）
     * @param  qos        
     * @param  ID         设备ID集
     * @param  directive  数据单元
     * @return 
     */
    @Async
	public void setParameters(int qos,String[] ID,String directive) {
    	for(int i=0;i<ID.length;i++){
        	String topic = "ArcDetector/"+ID[i]+"/Set/Parameters";         
        	mqttCommon.sendToMqtt(directive,topic);
    	}
    	
    }
    /**
     * 终端参数查询（后台发布，终端订阅）
     * @param  qos        
     * @param  ID         设备ID集
     * @param  directive  数据单元
     * @return 
     */
    @Async
	public void queryParameters(int qos,String[] ID,String directive) {
    	
    	for(int i=0;i<ID.length;i++){
    		
        	String topic = "ArcDetector/"+ID[i]+"/Query/Parameters";     
        	System.out.println("topic"+topic);
        	mqttCommon.sendToMqtt(directive,topic);
    	}
    }
	
	
    /**
     * 特殊参数设置（后台发布，终端订阅），包括远程升级配置
     * @param  qos       
     * @param  ID         设备ID集
     * @param  directive  数据单元
     * @return 
     */
    @Async
	public void setRoot(int qos,String[] ID,String directive) {
    	for(int i=0;i<ID.length;i++){
        	String topic = "ArcDetector/"+ID[i]+"/Set/Root";         
        	mqttCommon.sendToMqtt(directive,topic);
    	}
    }
	
	
	
}