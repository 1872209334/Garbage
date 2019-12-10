package com.qf.service.app;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.app.HixentArcAppIndexMapper;
import com.qf.model.fire.HixentArcAppMapSite;



/**
 * 手机app用户相关服务
 * author Vareck
 */
@Service
public class HixentAppUserIndexService {

	
	
    @Autowired
    private HixentArcAppIndexMapper hixentArcAppIndexMapper;
   //首页数据
    public JSONObject getIndexData(Integer appUserId){
    	JSONObject indexData = new JSONObject();
    	
    	 Integer type1=0;//0电弧探测器
    	 Integer type2=1;//1组合式探测器
    	 Integer warnIndex1=0;//0电弧预警
    	 Integer warnIndex2=1;//1设备故障
    	 Integer warnIndex3=2;//2设备离线
    	 //app用户项目数
    	 Integer siteCount = hixentArcAppIndexMapper.siteCount(appUserId);
    	 //app用户中控数
    	 Integer deviceCount = hixentArcAppIndexMapper.deviceCount(appUserId);
    	 //app用户电弧探测器数
    	 Integer arcOfEquipCount = hixentArcAppIndexMapper.typeOfEquipCount(appUserId,type1);
    	 //app用户组合式探测器数
    	 Integer currentOfEquipCount = hixentArcAppIndexMapper.typeOfEquipCount(appUserId,type2);
    	//app用户中控故障数
    	 Integer faultCountOfAlarm = hixentArcAppIndexMapper.deviceCountOfAlarm(appUserId,warnIndex2);
    	//app用户中控离线数
    	 Integer offlineCountOfAlarm = hixentArcAppIndexMapper.deviceCountOfAlarm(appUserId,warnIndex3);
    	//app用户电弧探测器报警数
    	 Integer alarmOfArcEquipAlarm = hixentArcAppIndexMapper.arcCountOfEquipAlarm(appUserId,warnIndex1,type1);
    	//app用户电弧探测器故障数
    	 Integer faultOfArcEquipAlarm = hixentArcAppIndexMapper.arcCountOfEquipAlarm(appUserId,warnIndex2,type1);
    	//app用户电弧探测器离线数
    	 Integer offlineOfArcEquipAlarm = hixentArcAppIndexMapper.arcCountOfEquipAlarm(appUserId,warnIndex3,type1);
    	//app用户组合式探测器报警数
    	 Integer alarmOfCurrentEquipAlarm = hixentArcAppIndexMapper.arcCountOfEquipAlarm(appUserId,warnIndex1,type2);
    	//app用户组合式探测器故障数
    	 Integer faultOfCurrentEquipAlarm = hixentArcAppIndexMapper.arcCountOfEquipAlarm(appUserId,warnIndex2,type2);
    	//app用户组合式探测器离线数
    	 Integer offlineOfCurrentEquipAlarm = hixentArcAppIndexMapper.arcCountOfEquipAlarm(appUserId,warnIndex3,type2);
    	 indexData.put("siteCount", siteCount);
    	 indexData.put("deviceCount", deviceCount);
    	 indexData.put("arcOfEquipCount", arcOfEquipCount);
    	 indexData.put("currentOfEquipCount", currentOfEquipCount);
    	 indexData.put("faultCountOfAlarm", faultCountOfAlarm);
    	 indexData.put("offlineCountOfAlarm", offlineCountOfAlarm);
    	 indexData.put("alarmOfArcEquipAlarm", alarmOfArcEquipAlarm);
    	 indexData.put("faultOfArcEquipAlarm", faultOfArcEquipAlarm);
    	 indexData.put("offlineOfArcEquipAlarm", offlineOfArcEquipAlarm);
    	 indexData.put("alarmOfCurrentEquipAlarm", alarmOfCurrentEquipAlarm);
    	 indexData.put("faultOfCurrentEquipAlarm", faultOfCurrentEquipAlarm);
    	 indexData.put("offlineOfCurrentEquipAlarm", offlineOfCurrentEquipAlarm);
    	
    	 return indexData;
    	 
    }
    
    //未处理数
    public Integer unDealCount(Integer appUserId,String siteCode) {
    	
    	Integer unDealAlarmEquipCount = hixentArcAppIndexMapper.unDealAlarmEquipCount(appUserId,siteCode);
    	Integer unDealAlarmDeviceCount = hixentArcAppIndexMapper.unDealAlarmDeviceCount(appUserId,siteCode);
    	Integer unDealCount=unDealAlarmEquipCount+unDealAlarmDeviceCount;
    	if(unDealCount==null) {
    		unDealCount=0;
    	}
    	return unDealCount;
    }
    //已处理数
    public JSONObject getDealCount(Integer appUserId,String siteCode){
    	JSONObject indexData = new JSONObject();
    	 Integer warnIndex1=0;//0电弧预警
    	 Integer warnIndex2=1;//1设备故障
    	 Integer warnIndex3=2;//2设备离线
    	 Integer dealAlarmCount = hixentArcAppIndexMapper.dealAlarmCount(appUserId,warnIndex1,siteCode);
    	 Integer dealFaultCount = hixentArcAppIndexMapper.dealAlarmCount(appUserId,warnIndex2,siteCode);
    	
    	 indexData.put("dealAlarmCount", dealAlarmCount);
    	 indexData.put("dealFaultCount", dealFaultCount);
    	
    	 return indexData;
    	 
    }
    //地图项目
    public List<HixentArcAppMapSite> mapData(Integer appUserId){
    	return	hixentArcAppIndexMapper.mapData(appUserId);
    }
    //某项目设备数和问题数
    public JSONObject getCountBySiteCode(Integer appUserId,String siteCode) {
    	Integer warnIndex0=0;//报警
    	Integer warnIndex1=1;//故障
    	Integer warnIndex2=2;//离线
    	Integer deviceWarn0CountBySiteCode = hixentArcAppIndexMapper.getDeviceWarnCountBySiteCode(appUserId,siteCode,warnIndex0);
    	Integer deviceWarn1CountBySiteCode = hixentArcAppIndexMapper.getDeviceWarnCountBySiteCode(appUserId,siteCode,warnIndex1);
    	Integer deviceWarn2CountBySiteCode = hixentArcAppIndexMapper.getDeviceWarnCountBySiteCode(appUserId,siteCode,warnIndex2);
    	
    	Integer equipWarn0CountBySiteCode = hixentArcAppIndexMapper.getEquipWarnCountBySiteCode(appUserId,siteCode,warnIndex0);
    	Integer equipWarn1CountBySiteCode = hixentArcAppIndexMapper.getEquipWarnCountBySiteCode(appUserId,siteCode,warnIndex1);
    	Integer equipWarn2CountBySiteCode = hixentArcAppIndexMapper.getEquipWarnCountBySiteCode(appUserId,siteCode,warnIndex2);
    
    	Integer alarmCount=deviceWarn0CountBySiteCode+equipWarn0CountBySiteCode;
    	Integer faultCount=deviceWarn1CountBySiteCode+equipWarn1CountBySiteCode;
    	Integer offlineCount=deviceWarn2CountBySiteCode+equipWarn2CountBySiteCode;
    	
    	Integer equipCountBySiteCode = hixentArcAppIndexMapper.getEquipCountBySiteCode(appUserId,siteCode);
    	Integer deviceCountBySiteCode = hixentArcAppIndexMapper.getDeviceCountBySiteCode(appUserId,siteCode);
       
    	Integer equipCount=equipCountBySiteCode+deviceCountBySiteCode;
    	JSONObject countData = new JSONObject();
    	
    	countData.put("alarmCount", alarmCount);
    	countData.put("faultCount", faultCount);
    	countData.put("offlineCount", offlineCount);
    	countData.put("equipCount", equipCount);
    	 return countData;
    }
}