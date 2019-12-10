package com.qf.util;



import com.alibaba.fastjson.JSON;



public class JsonUtil {

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }
    
}