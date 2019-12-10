package com.qf.service.fire;

import com.alibaba.fastjson.JSONObject;
import com.qf.model.fire.HixentArcZipperInfo;

import java.util.List;

public interface HixentArcZipperInfoService {

    List<HixentArcZipperInfo> selectAllZipperInfo(String deviceId, Integer pageSize, Integer currentPage);

    Integer countZipperInfoByDeviceId(String deviceId);

    List<HixentArcZipperInfo> selectAlarmLog(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    List<HixentArcZipperInfo> selectAlarmLogBySiteId(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    Integer countZipperAlarmLog(String projectId, String deviceId, String isAlarm);

    List<HixentArcZipperInfo> selectFaultLog(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    List<HixentArcZipperInfo> selectFaultLogBySiteId(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    Integer countZipperFaultLog(String projectId, String deviceId, String isAlarm);

    List<HixentArcZipperInfo> selectOffLineLog(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    List<HixentArcZipperInfo> selectOffLineLogBySiteId(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    Integer countZipperOffLineLog(String projectId, String deviceId, String isAlarm);
    Integer countZipperOffLineLog(String deviceId, String isAlarm);

    JSONObject alarmLogExcel(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    JSONObject faultLogExcel(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    JSONObject offLineLogExcel(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage);

    int deleteAlramLog(int unid);
}
