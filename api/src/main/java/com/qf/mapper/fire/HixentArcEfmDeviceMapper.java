package com.qf.mapper.fire;



import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcBuild;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcSite;
import com.qf.util.CustomerMapper;



/**
 * author zhangjun
 */
@Service
public interface HixentArcEfmDeviceMapper extends CustomerMapper<HixentArcEfmDevice> {
	
	
	
	HixentArcEquipmentInfo getOne(Integer id);
	/*列表*/
	List<HixentArcEfmDevice> getAllDeviceList(
            @Param("bid") Set bid,
            @Param("siteCode") String siteCode,
            @Param("deviceCode") String deviceCode
    );
	List<HixentArcEfmDevice> getPageDeviceList(
            @Param("bid") Set bid,
            @Param("siteCode") String siteCode,
            @Param("deviceCode") String deviceCode,
            @Param("order") String order,
            @Param("limits") String limits
    );
	List<HixentArcEfmDevice> getAllAdminDeviceList();
	List<HixentArcEfmDevice> getEfmDeviceInfo(
            @Param("efmId") String efmId
    );
	
	List<HixentArcEfmDevice> getAllSiteList();
	//站点详情
	HixentArcSite getSiteInfo(@Param("siteId") Integer siteId);
	//站点中控数量
	Integer deviceCount(@Param("siteId") Integer siteId);
	//站点终端设备数量
	Integer equipCount(@Param("siteId") Integer siteId);
	//建筑类型
	List<HixentArcBuild> getBuildList();
	//项目联动出中控
	List<HixentArcEfmDevice> getDeviceBySiteId(@Param("siteId") String siteId);
	//获取中控
	List<HixentArcEfmDevice> getDeviceList(@Param("provinceId") Integer provinceId, @Param("areaId") Integer areaId, @Param("siteCordArr") String[] siteCordArr);
	//中控详情
	HixentArcEfmDevice getdeviceInfo(@Param("deviceId") String id);
	//获取中控下终端数
	Integer getEquipCount(@Param("deviceId") String id);
	//获取中控下设备的某问题类型数量
	Integer getEquipTypeCountByDevice(@Param("deviceId") String id, @Param("warnIndex") Integer warnIndex);
	
	//查询中控离线时间
	List<HixentArcEfmDevice> getDeviceOfflineTime();
	
	//修改中控在线状态
	Integer updateDeviceIsOnline(@Param("deviceId") String deviceId, @Param("isOnline") Integer isOnline);
    
	
}