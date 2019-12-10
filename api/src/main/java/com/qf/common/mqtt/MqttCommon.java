package com.qf.common.mqtt;



import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;



@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel") 



/**
 * Mqtt接口
 * author Vareck
 */ 
public interface MqttCommon { 
	
	void sendToMqtt(String payload);
	
    void sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);

	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
	
	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos,  @Header(MqttHeaders.RETAINED) String retain,String payload);
	
}