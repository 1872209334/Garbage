package com.qf.mapper.fire.app;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcAppDeviceAndEquipInfo;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcSite;
import com.qf.util.CustomerMapper;



/**
 * author RuanYu
 */
@Service
public interface HixentArcAppSiteMapper extends CustomerMapper<HixentArcEfmDevice> {
	//项目详情
	HixentArcSite getSiteInfo(@Param("siteCode")String siteCode);
	//项目详情
	HixentArcSite getSite(String siteCode);
	//项目详情
	HixentArcSite getSiteById(Integer siteId);
	//项目列表
	List<HixentArcSite> getSiteList(@Param("appUserId")Integer appUserId);
	//中控列表
	List<HixentArcAppDeviceAndEquipInfo> getDeviceList(@Param("appUserId")Integer appUserId,
			@Param("siteCode")String siteCode,@Param("warnIndex")Integer warnIndex,
			@Param("inquire")String inquire,@Param("limits")String limits);
	Integer getDeviceListCount(@Param("appUserId")Integer appUserId,
			@Param("siteCode")String siteCode,@Param("warnIndex")Integer warnIndex,
			@Param("inquire")String inquire);
	
	
    //终端列表
	List<HixentArcAppDeviceAndEquipInfo> getEquipList(@Param("appUserId")Integer appUserId,
			@Param("siteCode")String siteCode,@Param("warnIndex")Integer warnIndex,
			@Param("equipType")Integer equipType,@Param("isMqttEquip")Integer isMqttEquip,
			@Param("inquire")String inquire,@Param("limits")String limits);
	
	Integer getEquipListCount(@Param("appUserId")Integer appUserId,
	@Param("siteCode")String siteCode,@Param("warnIndex")Integer warnIndex,
	@Param("equipType")Integer equipType,@Param("isMqttEquip")Integer isMqttEquip,
	@Param("inquire")String inquire);
	//中控详情
	HixentArcAppDeviceAndEquipInfo getDeviceInfo(@Param("deviceId")String deviceId);
	//终端详情
	HixentArcAppDeviceAndEquipInfo getEquipInfo(@Param("equipId")String equipId);
	//修改站点信息
	Integer update(HixentArcSite arcSite);
}