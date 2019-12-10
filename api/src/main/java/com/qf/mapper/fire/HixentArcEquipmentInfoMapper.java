package com.qf.mapper.fire;



import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcAppMapSite;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.util.CustomerMapper;



/**
 * author Vareck
 */
@Service
public interface HixentArcEquipmentInfoMapper extends CustomerMapper<HixentArcEquipmentInfo> {
	
	/*列表(手机app虚拟分组)*/
	List<HixentArcEquipmentInfo> getAllDeviceList( @Param("pid") Integer  pid );
	List<HixentArcEquipmentInfo> getPageDeviceList( 
		@Param("pid")      Integer   pid,
		@Param("order")    String    order,
		@Param("limits")   String    limits
	);
	
	/*列表(后台站点)*/
	List<HixentArcEquipmentInfo> getAllDeviceListByBid( 
		@Param("bid")   Set       bid,
		@Param("efmId") Integer   efmId
	);
	/*列表(后台站点)*/
	List<HixentArcEquipmentInfo> getAllMqttDeviceList();
	
	List<HixentArcEquipmentInfo> getCommonMqttList(
			@Param("allDid")      Set    allDid
			);
	
	List<HixentArcEquipmentInfo> getPageCommonMqttList(
			@Param("allDid")      Set    allDid,
			@Param("limits")   String    limits
			);
	
	
	/*列表(后台站点)*/
	List<HixentArcEquipmentInfo> getPageMqttDeviceList( 
		@Param("limits")   String    limits
	);
	
	List<HixentArcEquipmentInfo> getPageDeviceListByBid( 
		@Param("bid")      Set       bid,
		@Param("efmId")    Integer   efmId,
		@Param("order")    String    order,
		@Param("limits")   String    limits,
		@Param("lineCode")   String    lineCode
	);
	
	List<HixentArcEquipmentInfo> getPageEfmTerminalList( 
			@Param("efmId")    String   efmId,
			@Param("lineCode")   String    lineCode,
			@Param("limits")   String    limits
	);
	
	List<HixentArcEquipmentInfo> getAllEfmTerminalList( 
			@Param("efmId")    String   efmId,
			@Param("lineCode")   String    lineCode
	);
	
	
	
	Integer getEfmTerminalCount(
			@Param("efmId")    String   efmId,
			@Param("lineCode")   String    lineCode
	);
	
	HixentArcEquipmentInfo getOne( String  eid );
	List<HixentArcEquipmentInfo> getDeviceListByProjectId(String[] pid);
	
   //删除中控
	Integer delEfmDevice(@Param("efmId")String efmId);
	
	//删除中控下的终端
	Integer delEquipByEfmId(@Param("deviceIds")String[] deviceIds);
	
	//删除终端
	Integer delEquips(@Param("equipIds")String[] equipIds);
	
	//查询项目Id
	Integer getSiteIdBySiteCode(@Param("siteCode")String siteCode);
	
	
	//删除项目
	Integer delSite(@Param("siteCode")String siteCode);
	
	//获取项目下的中控ID 拼接字符串
	String getDeviceIdsBySitecode(@Param("siteCode")String siteCode);
	
	//删除项目下的中控
	Integer delDeviceBySiteCode(@Param("siteCode")String siteCode);
	
	//删除项目下的中控
	Integer delEquipBySiteCode(@Param("siteCode")String siteCode);
	
	//获取报警表的ID 拼接字符串(通过中控Id)
	String getWarnIdByEfmId(@Param("deviceIds")String[] deviceIds);
	
	//删除报警表数据通过中控IDs
	Integer delWarnByEfmId(@Param("deviceIds")String[] deviceIds);
	
	//删除反馈表数据通过报警表IDs
	Integer delFeedBackByWarnIds(@Param("warnIds")String[] warnIdByEfmId);
	
	//删除用户表bid里面的站点
	Integer updateUserBySiteCode(@Param("siteCode")String siteCode);
	
	//获取管控分组的ID 拼接字符串(通过项目Id)
	String getGroupIdBySiteCode(@Param("siteId")Integer siteId);
	
	//删除管控分组(通过项目Id)
	Integer delGroupBySiteCode(@Param("siteId")Integer siteId);
	
	//删除用户-分组连接表
	Integer delUserGruopLinkByGroupIds(@Param("groupIds")String[] groupIds);
	
	//获取报警表的ID 拼接字符串(通过终端Id)
	String getWarnIdByEquipIds(@Param("equipIds")String[] equipIds);
		
	//删除报警表数据通过终端IDs
	Integer delWarnByEquipIds(@Param("equipIds")String[] equipIds);
	
	
	
	
	//修改站点备注
	Integer editSiteNiName(@Param("niname")String niname,@Param("siteId")Integer siteId,@Param("siteBuildId")Integer siteBuildId,@Param("siteRemark")String siteRemark);
	
	//查询中控设备号是否重复
	
	String selDeviceId(@Param("deviceCode")String deviceCode,@Param("siteCode")String siteCode);
	
	//修改中控信息
	Integer editDeviceInfo(@Param("devicePlace")String devicePlace,@Param("niName")String niName,
			@Param("deviceId")String deviceId,@Param("latitude")String latitude,
			@Param("longitute")String longitute,@Param("offlineTime")Integer offlineTime,
			@Param("offlineEnable")Integer offlineEnable,@Param("specificator")String specificator);
	
	//查询中控设备号是否重复
	String selEquipId(@Param("deviceCode")String deviceCode,@Param("siteCode")String siteCode,@Param("equipCode")String equipCode);

	//修改终端信息
		Integer editEquipInfo(@Param("equipCode")String equipCode,
				@Param("equipPlace")String equipPlace);
	//修改终端信息
	Integer updateEquipInfo(@Param("field")String field,
			@Param("value")String value);
    //终端数量
	Integer getEquipListCount(@Param("equipType")Integer equipType,@Param("siteId")String siteId,
			@Param("deviceId")String deviceId,@Param("inquire")String inquire,
			@Param("message")String message,
			@Param("checkGroup")String checkGroup,@Param("isGroup") Integer isGroup,
			@Param("parameterType")Integer parameterType,@Param("deviceType") Integer deviceType);
	 //终端分页
		List<HixentArcEquipmentInfo> getEquipList(@Param("equipType") Integer equipType,
			@Param("siteId")String siteId,@Param("deviceId")String deviceId,
			@Param("limits")String limits,@Param("inquire")String inquire,
			@Param("message")String message,
			@Param("checkGroup")String checkGroup,@Param("isGroup") Integer isGroup,
			@Param("parameterType")Integer parameterType,@Param("deviceType") Integer deviceType);
	//终端分页  通过分组ID
	List<HixentArcEquipmentInfo> selEquipListByGroupId(@Param("groupId")Integer  groupId,@Param("limits")String limits);
	//终端分页  通过分组ID
	Integer selEquipListByGroupIdCount(@Param("groupId")Integer  groupId);
	
	//查询终端
	HixentArcEquipmentInfo getEquipInfoById(@Param("eid")String  eid);
	
	//查询项目是否有无线终端
	String getMqttEquipBySite(@Param("siteId") String siteId);
	
	//删除无线终端通过项目
	Integer delMqttEquipBySiteCode(@Param("siteCode") String siteCode);
   
	//地图无线终端显示
	List<HixentArcAppMapSite> getWirelessEquiplist(@Param("provinceId")Integer provinceId,
			@Param("areaId")Integer areaId,@Param("siteCordArr")String[] siteCordArr);

	List<HixentArcAppMapSite> mapData(@Param("provinceId")Integer provinceId,
			@Param("areaId")Integer areaId,@Param("siteCordArr")String[] siteCordArr);

	Integer getEquipWarnCountBySiteCode(@Param("siteCode") String siteCode,@Param("warnIndex")Integer warnIndex);

	Integer getEquipCountBySiteCode(@Param("siteCode") String siteCode);
}