package com.qf.controller.common;


import com.qf.common.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MqttTestController {


    @Autowired
    private MqttGateway mqttGateway;

    @PostMapping("/sendMqtt.do")
    public String sendMqtt(@RequestParam("topic") String topic,@RequestParam("sendData")String sendData) {
        System.out.println(topic+"-------"+sendData);
        mqttGateway.sendToMqtt(sendData, topic);
        return "ok";
    }


}
