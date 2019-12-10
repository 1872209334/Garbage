package com.qf.common.mqtt;



import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.outbound.*;
import org.springframework.messaging.MessageChannel;
import org.springframework.integration.channel.DirectChannel;
import java.util.UUID;



/**
 * Mqtt生产者配置
 * author Vareck
 */ 
@Configuration 
public class MqttProducerConfig { 
	
	
	
	@Value("${mqtt.host}")
    private String host;
	@Value("${mqtt.clientid}")
    private String clientid;
	@Value("${mqtt.username}")
    private String username;
	@Value("${mqtt.password}")
    private String password;
	@Value("${mqtt.send_topic_default}")
    private String send_topic_default;
	@Value("${mqtt.timeout}")
    private int timeout;
 
	
	
	@Bean 
	public MqttPahoClientFactory mqttClientFactory() { 
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory(); 
		factory.setServerURIs(host); 
		factory.setUserName(username);
		factory.setPassword(password);
		factory.setConnectionTimeout(timeout);
		return factory; 
	} 
	
	
	
	@Bean 
	@ServiceActivator(inputChannel = "mqttOutboundChannel") 
	public MqttPahoMessageHandler mqttOutbound() { 
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler( clientid + UUID.randomUUID().toString() , mqttClientFactory() ); 
		messageHandler.setAsync(true); 
		//messageHandler.setDefaultTopic(send_topic_default);
		System.out.println("生产消息-->"+clientid);
		return messageHandler; 
	} 
	
	
	
	@Bean public MessageChannel mqttOutboundChannel() { 
		return new DirectChannel(); 
	} 
	
	
	
}