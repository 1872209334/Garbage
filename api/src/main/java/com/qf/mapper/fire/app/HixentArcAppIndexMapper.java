package com.qf.mapper.fire.app;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcAppMapSite;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.util.CustomerMapper;



/**
 * author RuanYu
 */
@Service
public interface HixentArcAppIndexMapper extends CustomerMapper<HixentArcEfmDevice> {
	

	//app用户项目数
	Integer siteCount(@Param("appUserId") Integer appUserId);
	//app用户中控数
	Integer deviceCount(@Param("appUserId") Integer appUserId);
	//某一类型终端设备数 
	Integer typeOfEquipCount(@Param("appUserId") Integer appUserId,@Param("type") Integer type);
	//中控某问题类型数量
	Integer deviceCountOfAlarm(@Param("appUserId") Integer appUserId,@Param("warnIndex") Integer warnIndex);
    //某一类型某一报警类型终端设备数
	Integer arcCountOfEquipAlarm(@Param("appUserId") Integer appUserId,@Param("warnIndex") Integer warnIndex,@Param("type") Integer type);

    //已处理某一报警类型 
	Integer dealAlarmCount(@Param("appUserId") Integer appUserId,@Param("warnIndex") Integer warnIndex,@Param("siteCode") String siteCode);
    //未处理终端报警数
	Integer unDealAlarmEquipCount(@Param("appUserId") Integer appUserId,@Param("siteCode") String siteCode);
	//未处理中控报警数 
	Integer unDealAlarmDeviceCount(@Param("appUserId") Integer appUserId,@Param("siteCode") String siteCode);
    //地图显示项目
	List<HixentArcAppMapSite> mapData(@Param("appUserId") Integer appUserId);
	//项目下的终端数
	Integer getEquipCountBySiteCode(@Param("appUserId") Integer appUserId,@Param("siteCode") String siteCode);
	//项目下的中控数
	Integer getDeviceCountBySiteCode(@Param("appUserId") Integer appUserId,@Param("siteCode") String siteCode);

    //项目下的中控某类型问题数
	Integer getDeviceWarnCountBySiteCode(@Param("appUserId") Integer appUserId,@Param("siteCode") String siteCode,@Param("warnIndex")Integer warnIndex);
    //项目下的终端某类型问题数
	Integer getEquipWarnCountBySiteCode(@Param("appUserId") Integer appUserId,@Param("siteCode") String siteCode,@Param("warnIndex")Integer warnIndex);
}