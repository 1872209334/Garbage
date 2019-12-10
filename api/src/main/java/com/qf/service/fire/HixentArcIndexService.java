package com.qf.service.fire;

import java.util.ArrayList;
import java.util.List;

import com.qf.mapper.fire.HixentArcZipperInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qf.mapper.fire.HixentArcIndexMapper;
import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentProvince;
import com.qf.model.fire.HixentArcBuild;
import com.qf.model.fire.HixentArcIndex;

/**
 * 首页显示 author RuanYu
 */
@Service
public class HixentArcIndexService {

	@Autowired
	private HixentArcIndexMapper hixentArcIndexMapper;

	@Autowired
	HixentArcZipperInfoMapper hixentArcZipperInfoMapper;

	// 新首页数据
	public HixentArcIndex getCountNew(Integer areaId, Integer provinceId, String[] siteCordArr) {
		HixentArcIndex hai = new HixentArcIndex();

		//电弧探测器  问题 统计
		Integer arcCountOfEquipAlarm = hixentArcIndexMapper.arcCountOfEquipAlarm(0,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipOffLine = hixentArcIndexMapper.arcCountOfEquipAlarm(2,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipFault = hixentArcIndexMapper.arcCountOfEquipAlarm(1,areaId, provinceId, siteCordArr);

		//组合式探测器  问题 统计
		Integer currentCountOfEquipAlarm = hixentArcIndexMapper.currentCountOfEquipAlarm(0,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipOffLine = hixentArcIndexMapper.currentCountOfEquipAlarm(2,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipFault = hixentArcIndexMapper.currentCountOfEquipAlarm(1,areaId, provinceId,siteCordArr);

		//中控  问题  统计
		Integer deviceCountOfEquipAlarm = hixentArcIndexMapper.deviceCountOfEquipAlarm(0,areaId, provinceId, siteCordArr);
		Integer deviceCountOfEquipOffLine = hixentArcIndexMapper.deviceCountOfEquipAlarm(2,areaId, provinceId, siteCordArr);
		Integer deviceCountOfEquipFault = hixentArcIndexMapper.deviceCountOfEquipAlarm(1,areaId, provinceId, siteCordArr);

		//项目数
		Integer siteCount = hixentArcIndexMapper.siteCount(areaId, provinceId, siteCordArr);
		//中控数
		Integer deviceCountOfEquip = hixentArcIndexMapper.deviceCount(areaId, provinceId, siteCordArr);
		//电弧探测器数
		Integer arcCount = hixentArcIndexMapper.equipCount(0,areaId, provinceId, siteCordArr);
		//组合式探测器数
		Integer currentCount = hixentArcIndexMapper.equipCount(1,areaId, provinceId, siteCordArr);

		hai.setArcCountOfEquip(arcCount);
		hai.setArcCountOfEquipAlarm(arcCountOfEquipAlarm);
		hai.setArcCountOfEquipOffLine(arcCountOfEquipOffLine);
		hai.setArcCountOfEquipFault(arcCountOfEquipFault);

		hai.setCurrentCountOfEquip(currentCount);
		hai.setCurrentCountOfEquipAlarm(currentCountOfEquipAlarm);
		hai.setCurrentCountOfEquipFault(currentCountOfEquipFault);
		hai.setCurrentCountOfEquipOffLine(currentCountOfEquipOffLine);

		hai.setDeviceCountOfEquip(deviceCountOfEquip);
		hai.setDeviceCountOfEquipAlarm(deviceCountOfEquipAlarm);
		hai.setDeviceCountOfEquipFault(deviceCountOfEquipFault);
		hai.setDeviceCountOfEquipOffLine(deviceCountOfEquipOffLine);

		hai.setSiteCount(siteCount);

		return hai;

	}


	// 首页数据
	public HixentArcIndex getCount(Integer areaId, Integer provinceId, String[] siteCordArr) {
		HixentArcIndex hai = new HixentArcIndex();
		
		//电弧探测器  问题 统计
		Integer arcCountOfEquipAlarm = hixentArcIndexMapper.arcCountOfEquipAlarm(0,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipOffLine = hixentArcIndexMapper.arcCountOfEquipAlarm(2,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipFault = hixentArcIndexMapper.arcCountOfEquipAlarm(1,areaId, provinceId, siteCordArr);
		
		//组合式探测器  问题 统计
		Integer currentCountOfEquipAlarm = hixentArcIndexMapper.currentCountOfEquipAlarm(0,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipOffLine = hixentArcIndexMapper.currentCountOfEquipAlarm(2,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipFault = hixentArcIndexMapper.currentCountOfEquipAlarm(1,areaId, provinceId,siteCordArr);
		
		//中控  问题  统计
		Integer deviceCountOfEquipAlarm = hixentArcIndexMapper.deviceCountOfEquipAlarm(0,areaId, provinceId, siteCordArr);
		Integer deviceCountOfEquipOffLine = hixentArcIndexMapper.deviceCountOfEquipAlarm(2,areaId, provinceId, siteCordArr);
		Integer deviceCountOfEquipFault = hixentArcIndexMapper.deviceCountOfEquipAlarm(1,areaId, provinceId, siteCordArr);
		
		//项目数
		Integer siteCount = hixentArcIndexMapper.siteCount(areaId, provinceId, siteCordArr);
		//中控数
		Integer deviceCountOfEquip = hixentArcIndexMapper.deviceCount(areaId, provinceId, siteCordArr);
		//电弧探测器数
		Integer arcCount = hixentArcIndexMapper.equipCount(0,areaId, provinceId, siteCordArr);
		//组合式探测器数
		Integer currentCount = hixentArcIndexMapper.equipCount(1,areaId, provinceId, siteCordArr);
		
		hai.setArcCountOfEquip(arcCount);
		hai.setArcCountOfEquipAlarm(arcCountOfEquipAlarm);
		hai.setArcCountOfEquipOffLine(arcCountOfEquipOffLine);
		hai.setArcCountOfEquipFault(arcCountOfEquipFault);
		
		hai.setCurrentCountOfEquip(currentCount);
		hai.setCurrentCountOfEquipAlarm(currentCountOfEquipAlarm);
		hai.setCurrentCountOfEquipFault(currentCountOfEquipFault);
		hai.setCurrentCountOfEquipOffLine(currentCountOfEquipOffLine);
		
		hai.setDeviceCountOfEquip(deviceCountOfEquip);
		hai.setDeviceCountOfEquipAlarm(deviceCountOfEquipAlarm);
		hai.setDeviceCountOfEquipFault(deviceCountOfEquipFault);
		hai.setDeviceCountOfEquipOffLine(deviceCountOfEquipOffLine);
		
		hai.setSiteCount(siteCount);
		
		return hai;

	}

	public Integer siteCount(Integer areaId, Integer provinceId, String[] siteCordArr) {
		Integer siteCount = hixentArcIndexMapper.siteCount(areaId, provinceId, siteCordArr);
		return siteCount;
	}

	// 统计页面数据
	public HixentArcIndex getStatisticsData(Integer areaId, Integer provinceId, String[] siteCordArr) {
		HixentArcIndex hai = new HixentArcIndex();

		List<HixentArcBuild> buildCount = hixentArcIndexMapper.buildCount(areaId, provinceId, siteCordArr);
		List<HixentArea> areaCount = new ArrayList<HixentArea>();
		List<HixentProvince> provinceCount = new ArrayList<HixentProvince>();
		if (areaId == 0) {
			if (provinceId != 0) {
				areaCount = hixentArcIndexMapper.areaCount(provinceId, siteCordArr);

			} else {
				provinceCount = hixentArcIndexMapper.provinceCount();

			}
		}
		//电弧探测器  问题 统计
		Integer arcCountOfEquipAlarm = hixentArcIndexMapper.arcCountOfEquipAlarm(0,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipOffLine = hixentArcIndexMapper.arcCountOfEquipAlarm(2,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipFault = hixentArcIndexMapper.arcCountOfEquipAlarm(1,areaId, provinceId, siteCordArr);
				
		//组合式探测器  问题 统计
		Integer currentCountOfEquipAlarm = hixentArcIndexMapper.currentCountOfEquipAlarm(0,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipOffLine = hixentArcIndexMapper.currentCountOfEquipAlarm(2,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipFault = hixentArcIndexMapper.currentCountOfEquipAlarm(1,areaId, provinceId,siteCordArr);
		//终端设备总数
		Integer equipCount = hixentArcIndexMapper.equipCount(-1,areaId, provinceId, siteCordArr);
		
		hai.setBuildCount(buildCount);
		hai.setAreaCount(areaCount);
		hai.setProvinceCount(provinceCount);
		
		hai.setEquipCountAlarm(arcCountOfEquipAlarm+currentCountOfEquipAlarm);
		hai.setEquipCountOffLine(arcCountOfEquipOffLine+currentCountOfEquipOffLine);
		hai.setEquipCountFault(arcCountOfEquipFault+currentCountOfEquipFault);
		
		hai.setEquipCount(equipCount);
		
	
		return hai;
	}
	//设备状态数
	public HixentArcIndex getEquipStatus(Integer areaId, Integer provinceId, String[] siteCordArr) {
		HixentArcIndex hai = new HixentArcIndex();
		//电弧探测器  问题 统计
		Integer arcCountOfEquipAlarm = hixentArcIndexMapper.arcCountOfEquipAlarm(0,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipOffLine = hixentArcIndexMapper.arcCountOfEquipAlarm(2,areaId, provinceId, siteCordArr);
		Integer arcCountOfEquipFault = hixentArcIndexMapper.arcCountOfEquipAlarm(1,areaId, provinceId, siteCordArr);
				
		//组合式探测器  问题 统计
		Integer currentCountOfEquipAlarm = hixentArcIndexMapper.currentCountOfEquipAlarm(0,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipOffLine = hixentArcIndexMapper.currentCountOfEquipAlarm(2,areaId, provinceId,siteCordArr);
		Integer currentCountOfEquipFault = hixentArcIndexMapper.currentCountOfEquipAlarm(1,areaId, provinceId,siteCordArr);
		hai.setEquipCountAlarm(arcCountOfEquipAlarm+currentCountOfEquipAlarm);
		hai.setEquipCountOffLine(arcCountOfEquipOffLine+currentCountOfEquipOffLine);
		hai.setEquipCountFault(arcCountOfEquipFault+currentCountOfEquipFault);
		return hai;
	}
	
	
	//头部报警数
	public Integer headAlarmCount(Integer areaId, Integer provinceId, String[] siteCordArr) {
		
		Integer headAlarmCount = hixentArcIndexMapper.headAlarmCount(areaId,provinceId,siteCordArr);
		Integer headAlarmCountWireLess = hixentArcIndexMapper.headAlarmCountWireLess(areaId,provinceId,siteCordArr);
		return headAlarmCount+headAlarmCountWireLess;
	}
}