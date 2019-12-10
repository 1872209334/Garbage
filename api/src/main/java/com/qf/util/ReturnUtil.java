/*
 * Copyright (c) 2018 <1253850806@qq.com> All rights reserved.
 */



package com.qf.util;



import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;

/**
 * JSON统一返回数据格式
 * author Vareck
 */
public class ReturnUtil {

    public static ModelMap Success(String code , String msg, Object obj) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作成功" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", code);
        alldata.put("message", msg);
        alldata.put("data", obj);
        return alldata;
    }

    public static ModelMap Success(String msg, Object obj) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作成功" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", 200);
        alldata.put("message", msg);
        alldata.put("data", obj);
        return alldata;
    }
    
    public static ModelMap Success(String msg) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作成功" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", 200);
        alldata.put("message", msg);
        alldata.put("data", null);
        return alldata;
    }
    
    public static ModelMap Error(String code , String msg, Object obj) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作失败" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", code);
        alldata.put("message", msg);
        alldata.put("data", obj);
        return alldata;
    }

    public static ModelMap Error(String msg, Object obj) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作失败" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", 0);
        alldata.put("message", msg);
        alldata.put("data", obj);
        return alldata;
    }
    
    public static ModelMap Error(String msg) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作失败" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", 0);
        alldata.put("message", msg);
        alldata.put("data", null);
        return alldata;
    }
    
    public static ModelMap ErrorLogin(String msg) {
        msg = StringUtils.isEmpty(msg) || StringUtils.isBlank(msg) ? "操作失败" : msg;
        ModelMap alldata = new ModelMap();
        alldata.put("status", 10);
        alldata.put("message", msg);
        alldata.put("data", null);
        return alldata;
    }
    
}