package com.qf.mapper.fire;



import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcControlGroup;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.util.CustomerMapper;



/**
 * author RuanYu
 */
@Service
public interface HixentArcDeviceAlarmMapper extends CustomerMapper<HixentArcEfmDevice> {
	//查询报警表数据是否已存在
	Integer selWarnId(@Param("deviceId")String deviceId,@Param("warmType")String warmType);
	//插入报警表数据(故障)
	Integer  addWarn(@Param("deviceId")String deviceId,@Param("warmType")String warmType,@Param("time")Long time);
	
	//插入报警表数据(离线)
	Integer  addOffLine(@Param("deviceId")String deviceId,@Param("warmType")String warmType,@Param("time")Long time);
	
	Integer  updateDeviceInfo(@Param("deviceId")String deviceId,@Param("warn")String warn);
	
	Integer  updateDeviceEnable(@Param("deviceId")String deviceId,@Param("enable")String enable);
	
	List<HixentArcEfmDevice> getDeviceBySiteId(@Param("siteId")String siteId);
	
	//查询中文报警类型
	String warnType(@Param("warnType")String warnType);
	
	//终端报警插入数据
	Integer addEquipWarn(@Param("deviceId")String deviceId,@Param("equipId")String equipId,
			@Param("warmType")String warmType,@Param("warnIndex")Integer warnIndex,
			@Param("eType")Integer eType,@Param("time")Integer time);
	
	//删除报警表数据
	Integer delWarnByDeviceId(@Param("deviceId")String deviceId,@Param("warnType")String warnType);
	//删除反馈表
	Integer delFeedBackByWarnId(@Param("warnId")Integer warnId);
	
	//查询设备所在的项目所有管理员
	String getAdminByEquipId(@Param("eid")String eid);
	
	//查询中控所在的项目所有管理员
	String getAdminByDeviceId(@Param("efmId")String efmId);
	
	//查询终端设备是否已存在报警
	Integer selWarnIdByEid(@Param("eid")String eid,@Param("warmType")String warmType);
	
	//跟新终端使能
	Integer updateEquipEnable(@Param("enable")String enable,@Param("valueEnable")String valueEnable,@Param("eid")String eid);
}