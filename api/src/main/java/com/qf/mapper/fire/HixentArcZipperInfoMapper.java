package com.qf.mapper.fire;


import com.qf.model.fire.*;
import com.qf.util.CustomerMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * author zhangjun
 */
@Repository
public interface HixentArcZipperInfoMapper {

	//依据项目id查询设备报警信息
	HixentArcSite selectAlarmLogBySiteId(@Param("siteId")int siteId);

	HixentArcZipperUnionInfo selectSiteName(String projectId);

	//查询拉链设备全部数据
	List<HixentArcZipperInfo> selectAllZipperInfo(@Param("deviceId")String deviceId,@Param("limits")String limits);

	//查询拉链设备总数据量
	Integer countZipperInfoByDeviceId(@Param("deviceId")String deviceId);

	//查询拉链设备报警数据
	List<HixentArcZipperInfo> selectAlarmLog1(@Param("projectId")String projectId, @Param("deviceId")String deviceId, @Param("isAlarm")String isAlarm,@Param("limits")String limits);

	//查询拉链设备报警数据
	List<HixentArcZipperInfo> selectAlarmLog( @Param("deviceId")String deviceId, @Param("isAlarm")String isAlarm,@Param("limits")String limits);

	//查询拉链设备相应报警数据的总数据量
	Integer countZipperAlarmLog(@Param("projectId")String projectId, @Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm);

	//查询拉链设备故障数据
	List<HixentArcZipperInfo> selectFaultLog(@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm,@Param("limits")String limits);
	//查询拉链设备故障数据
	List<HixentArcZipperInfo> selectFaultLog1(@Param("projectId")String projectId,@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm,@Param("limits")String limits);

	//查询拉链设备相应故障数据的总数据量
	Integer countZipperFaultLog(@Param("projectId")String projectId,@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm);

	//查询拉链设备离线数据
	List<HixentArcZipperInfo> selectOffLineLog(@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm,@Param("limits")String limits);
	//查询拉链设备离线数据
	List<HixentArcZipperInfo> selectOffLineLog1(@Param("projectId")String projectId,@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm,@Param("limits")String limits);

	//查询拉链设备相应离线数据的总数据量
	Integer countZipperOffLineLog1(@Param("projectId")String projectId,@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm);
	//查询拉链设备相应离线数据的总数据量
	Integer countZipperOffLineLog(@Param("deviceId")String deviceId,@Param("isAlarm")String isAlarm);

	//删除拉链设备报警数据
	int deleteAlarmLog(@Param("unid")int unid);

	int insertZipperLog(HixentArcZipperInfo params);
}