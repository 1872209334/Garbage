/*
 * Copyright (c) 2018 <1253850806@qq.com> All rights reserved.
 */

package com.qf.service.admin;



import com.qf.mapper.admin.HixentLogMapper;
import com.qf.model.admin.HixentLog;
import com.qf.model.admin.HixentMainLog;
import com.qf.util.DateUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;



/**
 * web管理后台用户使用历史记录信息相关服务
 * author   Vareck
 */
@Service
public class HixentLogService {

    @Autowired
    private HixentLogMapper hixentLogMapper;
	
	//所有列表
    @SuppressWarnings("rawtypes")
    public List<HixentLog> getLogAllList( Integer isException,String username,String opration,String controller,String method,String ip,String time1,String time2,Set userNameArray,Set dateArray ) {
    	return hixentLogMapper.getLogAllList( isException,username,opration,controller,method,ip,time1,time2,userNameArray,dateArray );
    }
    
    //主表所有列表
    public List<HixentLog> mainLogAllList(){
    	return hixentLogMapper.mainLogAllList();
    }

    //页面列表
    @SuppressWarnings("rawtypes")
    public List<HixentLog> getLogPageList( Integer isException,String username,String opration,String controller,String method,String ip,String time1,String time2,Integer limit,Integer page,Set userNameArray,Set dateArray ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
		return hixentLogMapper.getLogPageList( isException,username,opration,controller,method,ip,time1,time2,limits,userNameArray,dateArray );
    }
    
    
    //插入数据
    public void insert(HixentLog data){
    	String  username    = data.getUsername();
        String  opration    = data.getOpration();
        String  controller  = data.getController();
        String  method      = data.getMethod();
        String  ip          = data.getIp();
        String  message     = data.getMessage();
        String  params      = data.getParams();
        String  date        = DateUtil.getCurrentTime4();
        List<HixentMainLog> dl  = hixentLogMapper.mainLogSelect(date);
        Integer n = dl.size();
        //判断是否已创建表
    	List table              = hixentLogMapper.existTable(date);
        if( table.size() == 0 ){
            hixentLogMapper.createTable(date);
        }
        this.insertLog(n,message,username,opration,controller,method,ip,params,date);
    }
    @Transactional
    public void insertLog(Integer n,String message,String username,String opration,String controller,String method,String ip,String params,String date){
        if( n==0 ){
            hixentLogMapper.mainLogInsert(date);
        }
    	hixentLogMapper.logInsert(message,username,opration,controller,method,ip,params,date);
    }
   

}