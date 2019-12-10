package com.qf.service.fire.impl;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.HixentArcZipperInfoMapper;
import com.qf.model.fire.HixentArcSite;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.model.fire.HixentArcZipperInfo;
import com.qf.model.fire.HixentArcZipperUnionInfo;
import com.qf.service.fire.HixentArcZipperInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HixentArcZipperInfoServiceImpl implements HixentArcZipperInfoService {

    @Autowired
    private HixentArcZipperInfoMapper hixentArcZipperInfoMapper;

    @Override
    public List<HixentArcZipperInfo> selectAllZipperInfo(String deviceId,Integer pageSize,Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        log.info("");
        return hixentArcZipperInfoMapper.selectAllZipperInfo(deviceId,limits);
    }

    @Override
    public Integer countZipperInfoByDeviceId(String deviceId) {
        return hixentArcZipperInfoMapper.countZipperInfoByDeviceId(deviceId);
    }

    @Override
    public List<HixentArcZipperInfo> selectAlarmLog(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId,isAlarm,limits);
//        for(int i = 0;i <alarmList.size();i++){
//            HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmList.get(i).getProjectId());
//            alarmList.get(i).setSiteName(siteName.getSiteName());
//            alarmList.get(i).setSiteId(siteName.getSiteId());
//        }
        return alarmList;
    }
    @Override
    public List<HixentArcZipperInfo> selectAlarmLogBySiteId( String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId,isAlarm,limits);
//        HixentArcSite site =new HixentArcSite();
////        if(siteId>0) {
////            site = hixentArcZipperInfoMapper.selectAlarmLogBySiteId(siteId);
//
//            List<HixentArcZipperInfo> resultAlarmList = new ArrayList<>();
//            for (int i = 0; i < alarmList.size(); i++) {
//                if (site.getSiteCode().equals(alarmList.get(i).getProjectId())) {
//                    HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmList.get(i).getProjectId());
//                    alarmList.get(i).setSiteName(siteName.getSiteName());
//                    alarmList.get(i).setSiteId(siteName.getSiteId());
//                    resultAlarmList.add(alarmList.get(i));
//                }
//            }
            return alarmList;
//        }else{
//            List<HixentArcZipperInfo> allAlarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId,isAlarm,limits);
//            for(int i = 0;i <allAlarmList.size();i++){
//                HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(allAlarmList.get(i).getProjectId());
//                allAlarmList.get(i).setSiteName(siteName.getSiteName());
//                allAlarmList.get(i).setSiteId(siteName.getSiteId());
//            }
//            return allAlarmList;
//        }

    }

    @Override
    public List<HixentArcZipperInfo> selectFaultLogBySiteId(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmList = hixentArcZipperInfoMapper.selectFaultLog1(projectId,deviceId,isAlarm,limits);
//        HixentArcSite site =new HixentArcSite();
//        if(siteId>0) {
//            site = hixentArcZipperInfoMapper.selectAlarmLogBySiteId(siteId);
//
//            List<HixentArcZipperInfo> resultAlarmList = new ArrayList<>();
//            for (int i = 0; i < alarmList.size(); i++) {
//                if (site.getSiteCode().equals(alarmList.get(i).getProjectId())) {
//                    HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmList.get(i).getProjectId());
//                    alarmList.get(i).setSiteName(siteName.getSiteName());
//                    alarmList.get(i).setSiteId(siteName.getSiteId());
//                    resultAlarmList.add(alarmList.get(i));
//                }
//            }
//            return resultAlarmList;
//        }else{
//            List<HixentArcZipperInfo> allAlarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId,isAlarm,limits);
//            for(int i = 0;i <allAlarmList.size();i++){
//                HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(allAlarmList.get(i).getProjectId());
//                allAlarmList.get(i).setSiteName(siteName.getSiteName());
//                allAlarmList.get(i).setSiteId(siteName.getSiteId());
//            }
            return alarmList;
//        }

    }

    @Override
    public Integer countZipperAlarmLog(String projectId, String deviceId,String isAlarm) {
        return hixentArcZipperInfoMapper.countZipperAlarmLog(projectId,deviceId,isAlarm);
    }

    @Override
    public List<HixentArcZipperInfo> selectFaultLog(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmList = hixentArcZipperInfoMapper.selectFaultLog1(projectId,deviceId,isAlarm,limits);
//        for(int i = 0;i <alarmList.size();i++){
//            HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmList.get(i).getProjectId());
//            alarmList.get(i).setSiteName(siteName.getSiteName());
//            alarmList.get(i).setSiteId(siteName.getSiteId());
//        }
        return alarmList;
    }

    @Override
    public Integer countZipperFaultLog(String projectId, String deviceId, String isAlarm) {
        return hixentArcZipperInfoMapper.countZipperFaultLog(projectId,deviceId,isAlarm);
    }

    @Override
    public List<HixentArcZipperInfo> selectOffLineLog(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmList = hixentArcZipperInfoMapper.selectOffLineLog1(projectId,deviceId,isAlarm,limits);
        for(int i = 0;i <alarmList.size();i++){
            HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmList.get(i).getProjectId());
            alarmList.get(i).setSiteName(siteName.getSiteName());
            alarmList.get(i).setSiteId(siteName.getSiteId());
        }
        return alarmList;
    }

    @Override
    public List<HixentArcZipperInfo> selectOffLineLogBySiteId(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmList = hixentArcZipperInfoMapper.selectOffLineLog1(projectId,deviceId,isAlarm,limits);
//        HixentArcSite site =new HixentArcSite();
//        if(siteId>0) {
//            site = hixentArcZipperInfoMapper.selectAlarmLogBySiteId(siteId);
//
//            List<HixentArcZipperInfo> resultAlarmList = new ArrayList<>();
//            for (int i = 0; i < alarmList.size(); i++) {
//                if (site.getSiteCode().equals(alarmList.get(i).getProjectId())) {
//                    HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmList.get(i).getProjectId());
//                    alarmList.get(i).setSiteName(siteName.getSiteName());
//                    alarmList.get(i).setSiteId(siteName.getSiteId());
//                    resultAlarmList.add(alarmList.get(i));
//                }
//            }
//            return resultAlarmList;
//        }else{
//            List<HixentArcZipperInfo> allAlarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId,isAlarm,limits);
//            for(int i = 0;i <allAlarmList.size();i++){
//                HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(allAlarmList.get(i).getProjectId());
//                allAlarmList.get(i).setSiteName(siteName.getSiteName());
//                allAlarmList.get(i).setSiteId(siteName.getSiteId());
//            }
            return alarmList;
//        }
    }

    @Override
    public Integer countZipperOffLineLog(String projectId, String deviceId, String isAlarm) {
        return hixentArcZipperInfoMapper.countZipperOffLineLog1(projectId,deviceId,isAlarm);
    }
    @Override
    public Integer countZipperOffLineLog( String deviceId, String isAlarm) {
        return hixentArcZipperInfoMapper.countZipperOffLineLog(deviceId,isAlarm);
    }

    // 报警日志Excel
   @Override
    public JSONObject alarmLogExcel(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        JSONObject outJson = new JSONObject();
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmLogForDevice = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId,isAlarm,limits);
//       HixentArcSite site = new HixentArcSite();
//       List<HixentArcZipperInfo> resultAlarmList = new ArrayList<>();
//       if (siteId > 0) {
//           site = hixentArcZipperInfoMapper.selectAlarmLogBySiteId(siteId);
//           for (int i = 0; i < alarmLogForDevice.size(); i++) {
//               if (site.getSiteCode().equals(alarmLogForDevice.get(i).getProjectId())) {
//                   HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmLogForDevice.get(i).getProjectId());
//                   alarmLogForDevice.get(i).setSiteName(siteName.getSiteName());
//                   alarmLogForDevice.get(i).setSiteId(siteName.getSiteId());
//                   resultAlarmList.add(alarmLogForDevice.get(i));
//               }
//           }
//       } else {
//           List<HixentArcZipperInfo> allAlarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId, isAlarm, limits);
//           for (int i = 0; i < allAlarmList.size(); i++) {
//               HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(allAlarmList.get(i).getProjectId());
//               allAlarmList.get(i).setSiteName(siteName.getSiteName());
//               allAlarmList.get(i).setSiteId(siteName.getSiteId());
//               resultAlarmList.add(allAlarmList.get(i));
//           }
//       }
//       Integer alarmLogForDeviceCount = resultAlarmList.size();
       outJson.put("alarmLogForDeviceCount", alarmLogForDevice.size());
       outJson.put("alarmLogForDevice", alarmLogForDevice);
       return outJson;
    }

    // 故障日志Excel
    @Override
    public JSONObject faultLogExcel(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        JSONObject outJson = new JSONObject();
        String limits = ""+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+"";
        List<HixentArcZipperInfo> alarmLogForDevice = hixentArcZipperInfoMapper.selectFaultLog1(projectId,deviceId,isAlarm,limits);
//        HixentArcSite site = new HixentArcSite();
//        List<HixentArcZipperInfo> resultAlarmList = new ArrayList<>();
//        if (siteId > 0) {
//            site = hixentArcZipperInfoMapper.selectAlarmLogBySiteId(siteId);
//            for (int i = 0; i < alarmLogForDevice.size(); i++) {
//                if (site.getSiteCode().equals(alarmLogForDevice.get(i).getProjectId())) {
//                    HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmLogForDevice.get(i).getProjectId());
//                    alarmLogForDevice.get(i).setSiteName(siteName.getSiteName());
//                    alarmLogForDevice.get(i).setSiteId(siteName.getSiteId());
//                    resultAlarmList.add(alarmLogForDevice.get(i));
//                }
//            }
//        } else {
//            List<HixentArcZipperInfo> allAlarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId, deviceId, isAlarm, limits);
//            for (int i = 0; i < allAlarmList.size(); i++) {
//                HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(allAlarmList.get(i).getProjectId());
//                allAlarmList.get(i).setSiteName(siteName.getSiteName());
//                allAlarmList.get(i).setSiteId(siteName.getSiteId());
//                resultAlarmList.add(allAlarmList.get(i));
//            }
//        }
        Integer alarmLogForDeviceCount = alarmLogForDevice.size();
        outJson.put("alarmLogForDeviceCount", alarmLogForDeviceCount);
        outJson.put("alarmLogForDevice", alarmLogForDevice);
        return outJson;
    }

    // 离线日志Excel
    @Override
    public JSONObject offLineLogExcel(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) {
        JSONObject outJson = new JSONObject();
        String limits = "" + Integer.toString((currentPage - 1) * pageSize) + "," + Integer.toString(pageSize) + "";
        List<HixentArcZipperInfo> alarmLogForDevice = hixentArcZipperInfoMapper.selectOffLineLog1(projectId,deviceId, isAlarm, limits);
//        HixentArcSite site = new HixentArcSite();
//        List<HixentArcZipperInfo> resultAlarmList = new ArrayList<>();
//        if (siteId > 0) {
//            site = hixentArcZipperInfoMapper.selectAlarmLogBySiteId(siteId);
//
//
//            for (int i = 0; i < alarmLogForDevice.size(); i++) {
//                if (site.getSiteCode().equals(alarmLogForDevice.get(i).getProjectId())) {
//                    HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(alarmLogForDevice.get(i).getProjectId());
//                    alarmLogForDevice.get(i).setSiteName(siteName.getSiteName());
//                    alarmLogForDevice.get(i).setSiteId(siteName.getSiteId());
//                    resultAlarmList.add(alarmLogForDevice.get(i));
//                }
//            }
//        } else {
//            List<HixentArcZipperInfo> allAlarmList = hixentArcZipperInfoMapper.selectAlarmLog1(projectId,deviceId, isAlarm, limits);
//            for (int i = 0; i < allAlarmList.size(); i++) {
//                HixentArcZipperUnionInfo siteName = hixentArcZipperInfoMapper.selectSiteName(allAlarmList.get(i).getProjectId());
//                allAlarmList.get(i).setSiteName(siteName.getSiteName());
//                allAlarmList.get(i).setSiteId(siteName.getSiteId());
//                resultAlarmList.add(allAlarmList.get(i));
//            }
//        }
        Integer alarmLogForDeviceCount = alarmLogForDevice.size();
        outJson.put("alarmLogForDeviceCount", alarmLogForDeviceCount);
        outJson.put("alarmLogForDevice", alarmLogForDevice);
        return outJson;
    }

    @Override
    public int deleteAlramLog(int unid) {
        return hixentArcZipperInfoMapper.deleteAlarmLog(unid);
    }
}
