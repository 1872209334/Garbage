package com.qf.common.mqtt;



import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.qf.service.mqtt.MqttCustomerService;
import com.qf.util.DataParsingUtil;

import java.util.UUID;



/**
 * Mqtt消费者配置
 * author Vareck
 */

@Configuration
@IntegrationComponentScan
public class MqttCustomerConfig {

	
	
	@Value("${mqtt.host}")
    private String host;
	@Value("${mqtt.clientid}")
    private String clientid;
	@Value("${mqtt.username}")
    private String username;
	@Value("${mqtt.password}")
    private String password;
	@Value("${mqtt.receive_topic}")
    private String receive_topic;
	@Value("${mqtt.timeout}")
    private int timeout;
	@Value("${mqtt.qos}")
    private int qos;
    
	@Autowired
	private MqttCustomerService mqttCustomerService;
	
	

    //通道
	@Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }
	
	
	
	//创建工厂
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(host);
		factory.setUserName(username);
		factory.setPassword(password);
		return factory;
	}
	
	
	
	//监听的topic
	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter( clientid + UUID.randomUUID().toString() , mqttClientFactory() , receive_topic );
	    adapter.setCompletionTimeout(timeout);
	    adapter.setConverter(new DefaultPahoMessageConverter());
	    adapter.setQos(qos);
	    adapter.setOutputChannel(mqttInputChannel());
		System.out.println("监听的消息-->"+clientid);
	    return adapter;
	}
	
	
	
	//通过通道获取数据
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler() {
			public void handleMessage(Message<?> message) throws MessagingException { //MqttMessage
				String topic      = message.getHeaders().get("mqtt_topic").toString();
				String content    = message.getPayload().toString();
				System.out.println("接收到消息："+topic+"--->"+content);
//				JSONObject jsonmsg = new JSONObject(new String(message.getPayload()));
				JSONObject jsonmsg = JSONObject.parseObject(content);
				System.out.println(jsonmsg.get("name"));
//				String[] msg = content.split("/");
				mqttCustomerService.MqttData(jsonmsg);
				//下面是原来的处理过程
//				if( !topic.equals("") || !content.equals("") ){
//			    	String[] topicArr = topic.split("/");
//			    	Integer n 	  	  = topicArr.length;
//			    	String ID 	  	  = topicArr[1];
//			    	String action 	  = topicArr[n-2];
//			    	String con        = topicArr[n-1];
//		        	//指令去空格
//			    	content = DataParsingUtil.remove(content);
//			    	//提交数据
//			    	try {
//						mqttCustomerService.reportingData(action,con,ID,content);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			}
		};
	}
	
	
	
}