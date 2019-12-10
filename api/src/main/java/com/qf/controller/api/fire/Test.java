package com.qf.controller.api.fire;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class Test {

    @Resource
    RedisTemplate redisTemplate;

    public static void main(String[] args){
        String str1 = "123456789";
        System.out.println(str1.substring(2));
        Test test = new Test();
        test.test();
    }


    public  void test(){
        redisTemplate.opsForValue().append("msg","zhangjun");
    }

}
