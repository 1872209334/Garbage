package com.qf.service.fire;



import java.text.ParseException;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.HixentArcDeviceAlarmMapper;
import com.qf.mapper.fire.HixentArcEfmDeviceMapper;
import com.qf.model.fire.HixentArcBuild;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcSite;
import com.qf.util.DateUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ToolUtil;

import tk.mybatis.mapper.entity.Example;



/**
 * EFM6000设备详情相关服务
 * author Vareck
 */
@Service
public class HixentArcEfmDeviceService {
	
	
	
	@Autowired
    private HixentArcEfmDeviceMapper hixentArcEfmDeviceMapper;
	
	@Autowired
    private HixentArcDeviceAlarmMapper hixentArcDeviceAlarmMapper;
	
	@Resource
	private RedisUtil redisUtil;
	//普通管理员获取列表信息
    public List<HixentArcEfmDevice> getAllDeviceList(Set bid,String siteCode,String deviceCode) {
    	return hixentArcEfmDeviceMapper.getAllDeviceList(bid,siteCode,deviceCode);
    }
    public List<HixentArcEfmDevice> getPageDeviceList(Set bid,String siteCode,String deviceCode,Integer limit,Integer page,String order) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEfmDeviceMapper.getPageDeviceList(bid,siteCode,deviceCode,order,limits);
    }
    
    //删除
    public int delete(HixentArcEfmDevice hixentArcEfmDevice) {
    	return hixentArcEfmDeviceMapper.delete(hixentArcEfmDevice);
    }

    //新增
    public int insert(HixentArcEfmDevice data){
    	return hixentArcEfmDeviceMapper.insert(data);
    }

    //更新
    public void update(HixentArcEfmDevice hixentArcEfmDevice, Example example){
    	hixentArcEfmDeviceMapper.updateByExample(hixentArcEfmDevice,example);
    }
    //超级管理员获取全部设备
    public List<HixentArcEfmDevice> getAllAdminDeviceList() {
    	return hixentArcEfmDeviceMapper.getAllAdminDeviceList();
    }
    //获取efm设备详情
    public List<HixentArcEfmDevice> getEfmDeviceInfo(String efmId) {
    	return hixentArcEfmDeviceMapper.getEfmDeviceInfo(efmId);
    }
    
    //获取全部站点
    public List<HixentArcEfmDevice> getAllSiteList() {
    	return hixentArcEfmDeviceMapper.getAllSiteList();
    }
    
    //获取站点详情
    public JSONObject getSizeInfo(Integer sizeId) {
    	JSONObject outJson = new JSONObject();
    	
    	HixentArcSite has=hixentArcEfmDeviceMapper.getSiteInfo(sizeId);
    	Integer equipCount = hixentArcEfmDeviceMapper.equipCount(sizeId);
    	Integer deviceCount = hixentArcEfmDeviceMapper.deviceCount(sizeId);
    	List<HixentArcBuild> buildList = hixentArcEfmDeviceMapper.getBuildList();
    	outJson.put("hixentArcSite", has);
    	outJson.put("equipCount", equipCount);
    	outJson.put("deviceCount", deviceCount);
    	outJson.put("buildList", buildList);
    	return outJson;
    }
    
    //设备详情
    public JSONObject getDeviceInfo(String deviceId) throws ParseException {
    	
    	JSONObject outJson = new JSONObject();
    	HixentArcEfmDevice getdeviceInfo = hixentArcEfmDeviceMapper.getdeviceInfo(deviceId);
    	getdeviceInfo.setSpecificator(ToolUtil.formatDevStr(getdeviceInfo.getSpecificator()));
        getdeviceInfo.setModel(ToolUtil.formatDevStr(getdeviceInfo.getModel()));
    	
   		getdeviceInfo.setSerial_number(ToolUtil.formatDevStr(getdeviceInfo.getSerial_number()));
   	
  		if(ToolUtil.StringNotBlank(getdeviceInfo.getLongitute())) {
  			getdeviceInfo.setLongitute(ToolUtil.hexStr2Str(getdeviceInfo.getLongitute()));	
  		}
  		if(ToolUtil.StringNotBlank(getdeviceInfo.getLatitude())) {
  			getdeviceInfo.setLatitude(ToolUtil.hexStr2Str(getdeviceInfo.getLatitude()));
  		}	
  	  
  		
  		getdeviceInfo.setVersion(ToolUtil.formatDevStr(getdeviceInfo.getVersion()));
  		
  		getdeviceInfo.setMessage_phone(ToolUtil.formatDevStr(getdeviceInfo.getMessage_phone()));
  		
  		getdeviceInfo.setPhone_number1(ToolUtil.formatDevStr(getdeviceInfo.getPhone_number1()));
  		
  		getdeviceInfo.setPhone_number2(ToolUtil.formatDevStr(getdeviceInfo.getPhone_number2()));
  		
  		getdeviceInfo.setPhone_number3(ToolUtil.formatDevStr(getdeviceInfo.getPhone_number3()));
  		
  		getdeviceInfo.setPhone_number4(ToolUtil.formatDevStr(getdeviceInfo.getPhone_number4()));
  		
  		
  		getdeviceInfo.setPhone_number5(ToolUtil.formatDevStr(getdeviceInfo.getPhone_number5()));
  		
  		getdeviceInfo.setReport_number(ToolUtil.formatDevStr(getdeviceInfo.getReport_number()));
  		
    	
    	Integer equipCount = hixentArcEfmDeviceMapper.getEquipCount(deviceId);
    	//0电弧预警，1设备故障，2设备离线
    	Integer equipOffLineCount = hixentArcEfmDeviceMapper.getEquipTypeCountByDevice(deviceId,2);
    	Integer equipAlarmCount = hixentArcEfmDeviceMapper.getEquipTypeCountByDevice(deviceId,0);
    	Integer equipFaultCount = hixentArcEfmDeviceMapper.getEquipTypeCountByDevice(deviceId,1);
    	String deviceTime =  (String) redisUtil.get("report_"+deviceId);
    	if(deviceTime == null){
    		outJson.put("isOnline", 0);
    	}
    	else{
    		Integer deviceTimeInt = Integer.valueOf(deviceTime);
    		//获取当前时间戳
    		String currentDate = DateUtil.getCurrentTime();
    		long nowtimestamp = DateUtil.getTimestamp(currentDate);
    		Integer offlineTime = getdeviceInfo.getOffLineTime();
    		if(nowtimestamp - deviceTimeInt > offlineTime){
    			//离线
    			outJson.put("isOnline", 0);
    			//checkSuc = hixentArcEfmDeviceService.updateDeviceOfflineState(deviceId, 0, warnType, nowtimestamp);
    			redisUtil.del("report_"+deviceId);
    		}else {
    			//在线
    			outJson.put("isOnline", 1);
    			//checkSuc = hixentArcEfmDeviceService.updateDeviceOnlineState(deviceId,1,warnType);
    		}
    	}
    	outJson.put("getdeviceInfo", getdeviceInfo);
    	outJson.put("equipCount", equipCount);
    	outJson.put("equipOffLineCount", equipOffLineCount);
    	outJson.put("equipAlarmCount", equipAlarmCount);
    	outJson.put("equipFaultCount", equipFaultCount);
    	return outJson;
    }
    
    //查询中控ID和离线信息
    public List<HixentArcEfmDevice> getDeviceOfflineTime() {
    	
    	return hixentArcEfmDeviceMapper.getDeviceOfflineTime();
    
    }
   //中控离线（离线使能开）
    public boolean updateDeviceOfflineState(String deviceId,Integer isOnline,String warnType,long nowtimestamp) {
    	Integer updateDeviceIsOnline = hixentArcEfmDeviceMapper.updateDeviceIsOnline(deviceId,isOnline);
       //查询是否在离线表已存在离线数据
       Integer selWarnId = hixentArcDeviceAlarmMapper.selWarnId(deviceId, warnType);
    	
        if(selWarnId==null||selWarnId==0) {
        	Integer addWarn = hixentArcDeviceAlarmMapper.addOffLine(deviceId, warnType,nowtimestamp);
          if(addWarn>0) {
        	  return true;  
          }
        }
        
        if(updateDeviceIsOnline>0) {
        	 return true;  
        }
         return false;
    }
    //中控在线（离线使能开）
    public boolean updateDeviceOnlineState(String deviceId,Integer isOnline,String warnType) {
    	 Integer updateDeviceIsOnline = hixentArcEfmDeviceMapper.updateDeviceIsOnline(deviceId,isOnline);
       //查询是否在离线表已存在离线数据
       Integer selWarnId = hixentArcDeviceAlarmMapper.selWarnId(deviceId, warnType);
    	//离线数据
        if(selWarnId!=null && selWarnId!=0) {
        	//删除报警表数据和维修表数据
        	Integer delWarnByDeviceId = hixentArcDeviceAlarmMapper.delWarnByDeviceId(deviceId, warnType);
        	Integer delFeedBackByWarnId = hixentArcDeviceAlarmMapper.delFeedBackByWarnId(selWarnId);
            if(delWarnByDeviceId>0) {
            	 return true;  	
            }
        }
       
        if(updateDeviceIsOnline>0) {
        	 return true;  
        }
         return false;
    }
    
  //中控离线（离线使能关）
    public boolean updateDeviceOfflineStateOffEnable(String deviceId,Integer isOnline) {
    	Integer updateDeviceIsOnline = hixentArcEfmDeviceMapper.updateDeviceIsOnline(deviceId,isOnline);
    	 if(updateDeviceIsOnline>0) {
        	 return true;  
        }
         return false;
    }
  
}