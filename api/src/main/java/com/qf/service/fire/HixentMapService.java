package com.qf.service.fire;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qf.mapper.fire.HixentArcEquipmentInfoMapper;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.util.StringUtil;
import com.qf.util.ToolUtil;


/**
 * 地图相关服务
 * author lg
 */
@Service
public class HixentMapService {
	
	@Autowired
    private HixentArcEquipmentInfoMapper hixentArcEquipmentInfoMapper;
	
	
	//获取项目下的设备
    public List<HixentArcEquipmentInfo> getDevice(Set siteCode){
    	List<HixentArcEquipmentInfo> dataList=this.hixentArcEquipmentInfoMapper.getAllDeviceListByBid(siteCode, null);
    	for (int i = 0; i < dataList.size(); i++) {
    		//设备经纬度
    		String longitute = dataList.get(i).getLngBmap();
    		String latitude = dataList.get(i).getLatBmap();
    		if(StringUtil.strIsNullOrEmpty(longitute) ||
    				StringUtil.strIsNullOrEmpty(latitude)){
    			dataList.remove(i);
    			i--;
    			continue;
    		}
    		String Strlongitute = ToolUtil.hexStr2Str(longitute);
    		String Strlatitude = ToolUtil.hexStr2Str(latitude);
    		dataList.get(i).setLngBmap(Strlongitute);
    		dataList.get(i).setLatBmap(Strlatitude);
    	}
    	return dataList;
    }
}
     