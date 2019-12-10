package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qf.mapper.fire.HixentArcDeviceAlarmMapper;



/**
 * 中控报警
 * author RuanYu
 */
@Service
public class HixentArcDeviceAlarmService {
	
	
	
	@Autowired
    private HixentArcDeviceAlarmMapper hixentArcDeviceAlarmMapper;
	
	
	
	 //报警记录表插入数据
    public Integer selWarnTimeByEid(String deviceId,String warmType,Long time) {
    	
    	Integer selWarnTimeByEid = hixentArcDeviceAlarmMapper.selWarnId(deviceId, warmType);
         return selWarnTimeByEid;
    	
    }
    //插入中控报警
    public void addWarnDevice(String deviceId,String warmType,Long time) {
    	Integer addWarn = hixentArcDeviceAlarmMapper.addWarn(deviceId, warmType,time);
    }
    //中控表报警字段更新
    public boolean updateDeviceInfo(String deviceId,String warn) {
    	Integer updateDeviceInfo = hixentArcDeviceAlarmMapper.updateDeviceInfo(deviceId,warn);
    	 if(updateDeviceInfo>0) {
       	  return true;  
         }
    	return false;
    }
    //中控表使能字段更新
    public boolean updateDeviceEnable(String deviceId,String enable) {
    	Integer updateDeviceInfo = hixentArcDeviceAlarmMapper.updateDeviceEnable(deviceId, enable);
    	 if(updateDeviceInfo>0) {
       	  return true;  
         }
    	return false;
    }
    //查询中文报警类型
    public String warnType(String warnType) {
    	String warnTypeCh= hixentArcDeviceAlarmMapper.warnType(warnType);
    	return warnTypeCh;
    }
  //报警记录表插入终端报警数据
    public Integer equipAlarm(String eid,String deviceId,Integer warnIndex,String warnType,Integer eType,Integer warnTime) {	
    	return hixentArcDeviceAlarmMapper.addEquipWarn(deviceId,eid,warnType,warnIndex,eType,warnTime);
    }
    //查询设备所在的项目所有管理员
    public String getAdminByEquipId(String eid) {
    	return hixentArcDeviceAlarmMapper.getAdminByEquipId(eid);
    	
    }
    //查询中控所在的项目所有管理员
    public String getAdminByDeviceId(String efmId) {
    	return hixentArcDeviceAlarmMapper.getAdminByDeviceId(efmId);
    }
  //查询终端设备是否已存在报警
    public Integer selWarnIdByEid(String eid,String warmType) {
    	return hixentArcDeviceAlarmMapper.selWarnIdByEid(eid,warmType);
    }
    
    //终端使能
    public Integer updateEquipEnable(String enable,String valueEnable,String eid) {
    	return hixentArcDeviceAlarmMapper.updateEquipEnable(enable,valueEnable,eid);
    }
}