package com.qf.mapper.fire;



import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcAlarmDealFeedBack;
import com.qf.model.fire.HixentArcEfmBeat;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.util.CustomerMapper;



/**
 * author Vareck
 */
@Service
public interface HixentArcWarningListMapper extends CustomerMapper<HixentArcWarningList> {
	
	//获取详细信息
	HixentArcWarningList getOneByWid(@Param("wid") Integer wid);
    //根据设备id获取所有信息
	List<HixentArcWarningList> getByDeviceId(String eid);
	//手机app火灾告警列表
	List<HixentArcWarningList> getAllByDeviceId(List<String> eidList);
	List<HixentArcWarningList> getPageByDeviceId( 
		@Param("eidList")  List<String> eidList,
		@Param("limits")   String    limits
	);
	List<HixentArcWarningList> getAdminWarning(
			@Param("time1")  long time1,
			@Param("time2")   long time2
	);
	List<HixentArcWarningList> getCommonWarning(
			@Param("time1")  long time1,
			@Param("time2")   long time2,
			@Param("bid")   Set bid
	);
	
	void dealWarningList(
			@Param("id")  		String  id,
			@Param("dtime")  		long  dtime
		);
	
	List<HixentArcWarningList> getPageAdminWarning( 
			@Param("limits")   String    limits,
			@Param("time1")  long time1,
			@Param("time2")   long time2

		);
	List<HixentArcWarningList> getPageCommonWarning( 
			@Param("limits")   String    limits,
			@Param("time1")  long time1,
			@Param("time2")   long time2,
			@Param("bid")   Set bid
		);
	//手机app设备故障告警列表(未处理)
	List<HixentArcWarningList> getAllByDeviceId2(List<String> eidList);
	List<HixentArcWarningList> getPageByDeviceId2( 
		@Param("eidList")  List<String> eidList,
		@Param("limits")   String    limits
	);
	//手机app设备故障告警列表(已处理)
	List<HixentArcWarningList> getAllByDeviceId4(List<String> eidList);
	//手机app设备异常历史记录列表
	List<HixentArcWarningList> getAllByDeviceId3(
		@Param("eidList")  List<String> eidList,
		@Param("time1")    Integer   time1,
		@Param("time2")    Integer   time2
	);
	List<HixentArcWarningList> getPageByDeviceId3( 
		@Param("eidList")  List<String> eidList,
		@Param("time1")    Integer   time1,
		@Param("time2")    Integer   time2,
		@Param("limits")   String    limits
	);
	
	void insertTo(
		@Param("ID")  		String  ID,
		@Param("wType")     String  wType,
		@Param("wTime")     Integer wTime,
		@Param("address")   String  address,
		@Param("latBmap")   double  latBmap,
		@Param("lngBmap")   double  lngBmap,
		@Param("eType")     Integer eType
	);

    //某设备未处理的告警信息
	List<HixentArcWarningList> getByDeviceId2(
		@Param("ID")  		String  ID,
		@Param("wtype")     String  wtype
	);
     //报警日志中控分页
	 List<HixentArcWarningList>	alarmLogForDevice(@Param("siteId")Integer siteId,
			 @Param("deviceId")String deviceId,@Param("limits")String limits,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,
			 @Param("warnIndex") Integer warnIndex);
	 
	 Integer alarmLogForDeviceCount(@Param("siteId")Integer siteId,
			 @Param("deviceId")String deviceId,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,
			 @Param("warnIndex") Integer warnIndex);
	 //报警日志终端分页
	 List<HixentArcWarningList>	alarmLogForEquip(@Param("siteId")Integer siteId,
			 @Param("deviceId")String deviceId,@Param("limits")String limits,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("type")Integer type,
			 @Param("inquire")String inquire,@Param("warnIndex") Integer warnIndex);
	 
	 Integer alarmLogForEquipCount(@Param("siteId")Integer siteId,
			 @Param("deviceId")String deviceId,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("type")Integer type,
			 @Param("inquire")String inquire,@Param("warnIndex") Integer warnIndex);
	
	 //报警历史中控分页
	 List<HixentArcWarningList>	alarmHistoryForDevice(@Param("limits")String limits,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	 
	 Integer alarmHistoryForDeviceCount(
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	 //报警历史终端分页
	 List<HixentArcWarningList>	alarmHistoryForEquip(@Param("limits")String limits,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("type")Integer type,
			 @Param("inquire")String inquire,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	 
	 Integer alarmHistoryForEquipCount(
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("type")Integer type,
			 @Param("inquire")String inquire,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	
	 //处理反馈分页
	 List<HixentArcAlarmDealFeedBack>	alarmDealFeedBack(@Param("limits")String limits,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("type")Integer type,
			 @Param("inquire")String inquire,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	 Integer alarmDealFeedBackCount(
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("type")Integer type,
			 @Param("inquire")String inquire,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
    //审核处理反馈 修改 处理反馈表状态
	 Integer updateDealFeedBack(Integer id);
	 //审核处理反馈 修改 报警表状态
	 Integer updateWarnlist(Integer id);
	 //查询所有心跳表
	 List<String> dataList();
	 //查询某个心跳表的最后一条数据
	 HixentArcEfmBeat lastData( @Param("tableName")String tableName);
	 //报警表插入离线数据
	 Integer offLineDevice(@Param("deviceId")String deviceId,@Param("time")Long time);
	 
	 //派单 修改 报警表状态
	 Integer updateWarnTableForDispach(@Param("warnId")Integer warnId);
	 //派单 插入 派单表数据
	 Integer addDispach(@Param("warnId")Integer warnId,@Param("adminId")Integer adminId,@Param("appUserId")Integer appUserId,@Param("time")Long time);

     //查询派单ID
	 Integer dispachId(@Param("warnId")Integer warnId);
	 //删除报警表数据
	 Integer delWarnByWarnId(@Param("warnId")Integer warnId);
	 //删除派单表数据
	 Integer delDispachByDispachId(@Param("dispachId")Integer dispachId);
	 //删除处理反馈表数据
	 Integer delFeedbackByWarnId(@Param("warnId")Integer warnId);
	 //查询报警信息和处理信息
	 HixentArcAlarmDealFeedBack getWarnInfo(@Param("warnId")Integer warnId);
	 
	 //	 报警历史分页 不分中控终端
	 List<HixentArcWarningList>	alarmHistoryForAll(@Param("limits")String limits,
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	 //	 报警历史总数 不分中控终端
	 Integer alarmHistoryForAllCount(
			 @Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr,@Param("startTime")Long startTime,
			 @Param("endTime")Long endTime);
	 //设备复位
	 Integer devicesReset(@Param("deviceId")String deviceId,@Param("time")Long time);
	 
	 //某类型警报数(有线)
	 Integer getWarnCountByWarnIndex(@Param("warnIndex")Integer warnIndex,@Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr);
	 //某类型警报数(无线)
	 Integer getWarnCountByWarnIndexWireless(@Param("warnIndex")Integer warnIndex,@Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,
			 @Param("siteCordArr")String[] siteCordArr);
}