package com.qf.mapper.fire.app;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcAppDeviceAndEquipWarn;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.util.CustomerMapper;

/**
 * author RuanYu
 */
@Service
public interface HixentArcAppWarnMapper extends CustomerMapper<HixentArcEfmDevice> {
	// 中控报警列表
	List<HixentArcAppDeviceAndEquipWarn> getDeviceWarnList(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("siteCode") String siteCode, @Param("limits") String limits);
	// 中控报警数量
	Integer getDeviceWarnCount(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("siteCode") String siteCode);

	
	// 终端报警列表
	List<HixentArcAppDeviceAndEquipWarn> getEquipWarnList(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("equipType") Integer equipType,
			@Param("siteCode") String siteCode, @Param("limits") String limits);
	// 终端报警数量
	Integer getEquipWarnCount(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("equipType") Integer equipType,
			@Param("siteCode") String siteCode);
	// 新增处理反馈
	Integer addFeedback(@Param("appUserId") Integer appUserId, @Param("warnId") Integer warnId,
			@Param("time") Long time, @Param("folderId") Integer folderId);

	// 查询反馈表上传者ID
	Integer selDealAppUserId(@Param("warnId") Integer warnId);

	// 查询反馈表文件夹ID
	Integer selFoldId(@Param("warnId") Integer warnId);

	
	// 更新反馈表
	Integer updateDealFeedback(@Param("warnId") Integer warnId,
			@Param("time") Long time);

	// 查询拥有某终端的用户
	HixentAppUser selAppUserByEquipId(@Param("equipId") String equipId);

	// 查询拥有某终端的用户
	HixentAppUser selAppUserByDeviceId(@Param("deviceId") String deviceId);

	// 报警历史（中控）
	List<HixentArcAppDeviceAndEquipWarn> dealHistory(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("siteCode") String siteCode, @Param("limits") String limits,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);
	Integer dealHistoryCount(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("siteCode") String siteCode,
			@Param("startTime") Long startTime, @Param("endTime") Long endTime);

	// 报警历史（终端）
	List<HixentArcAppDeviceAndEquipWarn> dealHistoryEquip(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("equipType") Integer equipType,
			@Param("siteCode") String siteCode, @Param("limits") String limits, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime);
	Integer dealHistoryEquipCount(@Param("appUserId") Integer appUserId,
			@Param("warnIndex") Integer warnIndex, @Param("equipType") Integer equipType,
			@Param("siteCode") String siteCode, @Param("startTime") Long startTime,
			@Param("endTime") Long endTime);
	
	//
	Integer updateWarn(@Param("warnId") Integer warnId,@Param("time") Long time);
}