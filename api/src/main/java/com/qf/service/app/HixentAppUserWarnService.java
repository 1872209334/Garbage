package com.qf.service.app;



import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.HixentUploadFileMapper;
import com.qf.mapper.fire.app.HixentArcAppWarnMapper;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcAppDeviceAndEquipWarn;
import com.qf.model.fire.HixentArcFile;



/**
 * 手机app用户相关服务
 * author Vareck
 */
@Service
public class HixentAppUserWarnService {

	
	
    @Autowired
    private HixentArcAppWarnMapper hixentArcAppWarnMapper;
    
    @Autowired
    private HixentUploadFileMapper hixentUploadFileMapper;
    
    //中控报警列表
    public List<HixentArcAppDeviceAndEquipWarn> getDeviceWarnList(Integer appUserId,
			Integer warnIndex,String siteCode,Integer limit,Integer page){
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    return	hixentArcAppWarnMapper.getDeviceWarnList(appUserId,warnIndex,siteCode,limits);
    }
    public Integer getDeviceWarnCount(Integer appUserId,
			Integer warnIndex,String siteCode) {
    	return	hixentArcAppWarnMapper.getDeviceWarnCount(appUserId,warnIndex,siteCode);
    }
    
    
    //终端报警列表
    public List<HixentArcAppDeviceAndEquipWarn> getEquipWarnList(Integer appUserId,
			Integer warnIndex,Integer equipType,String siteCode,Integer limit,Integer page){
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    return	hixentArcAppWarnMapper.getEquipWarnList(appUserId,warnIndex,equipType,siteCode,limits);
    }
    public Integer getEquipWarnCount(Integer appUserId,
			Integer warnIndex,Integer equipType,String siteCode) {
    	return	hixentArcAppWarnMapper.getEquipWarnCount(appUserId,warnIndex,equipType,siteCode);
    }
    //处理反馈
    public Integer addDealFeedback(Integer appUserId,Integer warnId,Long time,Integer folderId) {
    	
    	return	hixentArcAppWarnMapper.addFeedback(appUserId,warnId,time,folderId);
    }
    //待审核
    public Integer updateWarn(Integer warnId,Long time) {
    	return	hixentArcAppWarnMapper.updateWarn(warnId,time);
    }
 
    //插入文件夹
    public Integer addFolder(String folderName,Integer type,Integer appUserId,Integer siteId,Long nowtimestamp) {
    	HixentArcFile haf = new HixentArcFile();
    	haf.setFolderName(folderName);
    	haf.setType(type);
    	haf.setUploaderId(appUserId);
    	haf.setSiteId(siteId);
    	haf.setUploadTime(nowtimestamp);
    	  hixentUploadFileMapper.folderData(haf);
    	 Integer folderId = haf.getFolderId();
    	
    	 return folderId;
    }
    //查询反馈表文件夹ID
    public Integer selFoldId(Integer warnId) {
    	
    	return	hixentArcAppWarnMapper.selFoldId(warnId);
    }
    //查询反馈表上传者ID
    public Integer selDealAppUserId(Integer warnId) {
    	return	hixentArcAppWarnMapper.selDealAppUserId(warnId);
    }
    //更新反馈表
    public Integer updateDealFeedback(Integer warnId,Long nowtimestamp) {
    	return hixentArcAppWarnMapper.updateDealFeedback(warnId,nowtimestamp);
    }
    //查询拥有某终端的用户
    public HixentAppUser selAppUserByEquipId(String equipId) {
    	return hixentArcAppWarnMapper.selAppUserByEquipId(equipId);
    }
   //查询拥有某终端的用户
    public HixentAppUser selAppUserByDeviceId(String deviceId) {
    	return hixentArcAppWarnMapper.selAppUserByDeviceId(deviceId);
    }
    
    //报警历史（中控）
    public List<HixentArcAppDeviceAndEquipWarn> dealHistory(Integer appUserId,
			Integer warnIndex,String siteCode,Integer limit,Integer page,
			Long startTime, Long endTime){
    	
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	
    		List<HixentArcAppDeviceAndEquipWarn> dealHistory = hixentArcAppWarnMapper.dealHistory(appUserId,warnIndex,siteCode,limits,startTime,endTime);
    		if(dealHistory.size()==1&&null==dealHistory.get(0)||dealHistory.size()==0){
    			dealHistory=null;
    		}
    		return dealHistory;
    }
    public Integer dealHistoryCount(Integer appUserId,Integer warnIndex,String siteCode,Long startTime, Long endTime) {
			
    	return hixentArcAppWarnMapper.dealHistoryCount(appUserId,warnIndex,siteCode,startTime,endTime);
    }
    
    //报警历史（终端）
    public List<HixentArcAppDeviceAndEquipWarn> dealHistoryEquip(Integer appUserId,
			Integer warnIndex,Integer equipType,String siteCode,Integer limit,Integer page,
			Long startTime, Long endTime){
    	
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    
    	List<HixentArcAppDeviceAndEquipWarn> dealHistoryEquip = new ArrayList<HixentArcAppDeviceAndEquipWarn>();
    		dealHistoryEquip = hixentArcAppWarnMapper.dealHistoryEquip(appUserId,warnIndex,equipType,siteCode,limits,startTime,endTime);
    		if(dealHistoryEquip.size()==1&&null==dealHistoryEquip.get(0)||dealHistoryEquip.size()==0){
    			
    			dealHistoryEquip=null;
    		}
    		
    		return dealHistoryEquip;
    }
    
    public Integer dealHistoryEquipCount(Integer appUserId,Integer warnIndex,Integer equipType,String siteCode,Long startTime, Long endTime) {
			
    	return 	hixentArcAppWarnMapper.dealHistoryEquipCount(appUserId,warnIndex,equipType,siteCode,startTime,endTime);
    	
    }
    
    //报警数量
    public JSONObject warnCount(Integer appUserId){
			
    	Long startTime=(long) 0;
    	Long endTime=(long) 0;
    	Integer warnIndex=-2;
    	String siteCode=null;
    	Integer equipType=-1;
    	Integer equipType0=0;
    	Integer equipType1=1;
    	Integer deviceWarnCount = hixentArcAppWarnMapper.getDeviceWarnCount(appUserId,warnIndex,siteCode);
    	Integer arcWarnCount = hixentArcAppWarnMapper.getEquipWarnCount(appUserId,warnIndex,equipType0,siteCode);
    	Integer currentWarnCount = hixentArcAppWarnMapper.getEquipWarnCount(appUserId,warnIndex,equipType1,siteCode);
    	
    	Integer dealHistoryDeviceCount = hixentArcAppWarnMapper.dealHistoryCount(appUserId,warnIndex,siteCode,startTime,endTime);
    	Integer dealHistoryEquipCount = hixentArcAppWarnMapper.dealHistoryEquipCount(appUserId,warnIndex,equipType,siteCode,startTime,endTime);
    	Integer dealHistory=dealHistoryDeviceCount+dealHistoryEquipCount;
    	
    	List<Integer> warnCount = new ArrayList<Integer>();
    	JSONObject outJson = new JSONObject();
    	outJson.put("deviceWarnCount", deviceWarnCount);
    	outJson.put("arcWarnCount", arcWarnCount);
    	outJson.put("currentWarnCount", currentWarnCount);
    	outJson.put("dealHistory", dealHistory);
    	
    	return outJson;
    }
    
}