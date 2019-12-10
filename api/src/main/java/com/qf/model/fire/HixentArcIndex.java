package com.qf.model.fire;



import java.io.Serializable;
import java.util.List;

import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentProvince;



/**
 * author Vareck
 */
public class HixentArcIndex implements Serializable {



    private Integer arcCountOfEquip;
	
    private Integer arcCountOfEquipAlarm;
	
    private Integer arcCountOfEquipOffLine;
    
    private Integer arcCountOfEquipFault;
	
    private Integer currentCountOfEquip;
	
    private Integer currentCountOfEquipAlarm;
	
    private Integer currentCountOfEquipOffLine;
    
    private Integer currentCountOfEquipFault;
	
    private Integer deviceCountOfEquip;
	
    private Integer deviceCountOfEquipAlarm;
 	
    private Integer deviceCountOfEquipOffLine;
    
    private Integer deviceCountOfEquipFault;
	
    private Integer equipCount;
	
    private Integer equipCountAlarm;
	
    private Integer equipCountOffLine;
    
    private Integer equipCountFault;
 	
    private Integer siteCount;
    
    private List<HixentArcBuild> buildCount;
	
    private List<HixentProvince> provinceCount;
 	
    private List<HixentArea> areaCount;
    
    private Integer equipCountOnLine;
    
    private Integer equipEffectCount;
  
    public HixentArcIndex() {
		// TODO Auto-generated constructor stub
	}
	public HixentArcIndex(Integer arcCountOfEquip, Integer arcCountOfEquipAlarm, Integer arcCountOfEquipOffLine,
			Integer currentCountOfEquip, Integer currentCountOfEquipAlarm, Integer currentCountOfEquipOffLine,
			Integer deviceCountOfEquip, Integer deviceCountOfEquipAlarm, Integer deviceCountOfEquipOffLine,
			Integer equipCount, Integer equipCountAlarm, Integer equipCountOffLine, Integer siteCount) {
		super();
		this.arcCountOfEquip = arcCountOfEquip;
		this.arcCountOfEquipAlarm = arcCountOfEquipAlarm;
		this.arcCountOfEquipOffLine = arcCountOfEquipOffLine;
		this.currentCountOfEquip = currentCountOfEquip;
		this.currentCountOfEquipAlarm = currentCountOfEquipAlarm;
		this.currentCountOfEquipOffLine = currentCountOfEquipOffLine;
		this.deviceCountOfEquip = deviceCountOfEquip;
		this.deviceCountOfEquipAlarm = deviceCountOfEquipAlarm;
		this.deviceCountOfEquipOffLine = deviceCountOfEquipOffLine;
		this.equipCount = equipCount;
		this.equipCountAlarm = equipCountAlarm;
		this.equipCountOffLine = equipCountOffLine;
		this.siteCount = siteCount;
	}
   
	
	public Integer getArcCountOfEquipFault() {
		return arcCountOfEquipFault;
	}
	public void setArcCountOfEquipFault(Integer arcCountOfEquipFault) {
		this.arcCountOfEquipFault = arcCountOfEquipFault;
	}
	public Integer getCurrentCountOfEquipFault() {
		return currentCountOfEquipFault;
	}
	public void setCurrentCountOfEquipFault(Integer currentCountOfEquipFault) {
		this.currentCountOfEquipFault = currentCountOfEquipFault;
	}
	public Integer getDeviceCountOfEquipFault() {
		return deviceCountOfEquipFault;
	}
	public void setDeviceCountOfEquipFault(Integer deviceCountOfEquipFault) {
		this.deviceCountOfEquipFault = deviceCountOfEquipFault;
	}
	public HixentArcIndex(Integer equipCount, Integer equipCountAlarm, Integer equipCountOffLine,
			List<HixentArcBuild> buildCount, List<HixentProvince> provinceCount, List<HixentArea> areaCount,
			Integer equipCountOnLine,Integer equipEffectCount) {
		super();
		this.equipCount = equipCount;
		this.equipCountAlarm = equipCountAlarm;
		this.equipCountOffLine = equipCountOffLine;
		this.buildCount = buildCount;
		this.provinceCount = provinceCount;
		this.areaCount = areaCount;
		this.equipCountOnLine = equipCountOnLine;
		this.setEquipEffectCount(equipEffectCount);
	}
	public List<HixentArcBuild> getBuildCount() {
		return buildCount;
	}
	public void setBuildCount(List<HixentArcBuild> buildCount) {
		this.buildCount = buildCount;
	}
	public List<HixentProvince> getProvinceCount() {
		return provinceCount;
	}
	public void setProvinceCount(List<HixentProvince> provinceCount) {
		this.provinceCount = provinceCount;
	}
	public List<HixentArea> getAreaCount() {
		return areaCount;
	}
	public void setAreaCount(List<HixentArea> areaCount) {
		this.areaCount = areaCount;
	}
	public Integer getEquipCountOnLine() {
		return equipCountOnLine;
	}
	public void setEquipCountOnLine(Integer equipCountOnLine) {
		this.equipCountOnLine = equipCountOnLine;
	}
	public Integer getArcCountOfEquip() {
		return arcCountOfEquip;
	}

	public void setArcCountOfEquip(Integer arcCountOfEquip) {
		this.arcCountOfEquip = arcCountOfEquip;
	}

	public Integer getArcCountOfEquipAlarm() {
		return arcCountOfEquipAlarm;
	}

	public void setArcCountOfEquipAlarm(Integer arcCountOfEquipAlarm) {
		this.arcCountOfEquipAlarm = arcCountOfEquipAlarm;
	}

	public Integer getArcCountOfEquipOffLine() {
		return arcCountOfEquipOffLine;
	}

	public void setArcCountOfEquipOffLine(Integer arcCountOfEquipOffLine) {
		this.arcCountOfEquipOffLine = arcCountOfEquipOffLine;
	}

	public Integer getCurrentCountOfEquip() {
		return currentCountOfEquip;
	}

	public void setCurrentCountOfEquip(Integer currentCountOfEquip) {
		this.currentCountOfEquip = currentCountOfEquip;
	}

	public Integer getCurrentCountOfEquipAlarm() {
		return currentCountOfEquipAlarm;
	}

	public void setCurrentCountOfEquipAlarm(Integer currentCountOfEquipAlarm) {
		this.currentCountOfEquipAlarm = currentCountOfEquipAlarm;
	}

	public Integer getCurrentCountOfEquipOffLine() {
		return currentCountOfEquipOffLine;
	}

	public void setCurrentCountOfEquipOffLine(Integer currentCountOfEquipOffLine) {
		this.currentCountOfEquipOffLine = currentCountOfEquipOffLine;
	}

	public Integer getDeviceCountOfEquip() {
		return deviceCountOfEquip;
	}

	public void setDeviceCountOfEquip(Integer deviceCountOfEquip) {
		this.deviceCountOfEquip = deviceCountOfEquip;
	}

	public Integer getDeviceCountOfEquipAlarm() {
		return deviceCountOfEquipAlarm;
	}

	public void setDeviceCountOfEquipAlarm(Integer deviceCountOfEquipAlarm) {
		this.deviceCountOfEquipAlarm = deviceCountOfEquipAlarm;
	}

	public Integer getDeviceCountOfEquipOffLine() {
		return deviceCountOfEquipOffLine;
	}

	public void setDeviceCountOfEquipOffLine(Integer deviceCountOfEquipOffLine) {
		this.deviceCountOfEquipOffLine = deviceCountOfEquipOffLine;
	}

	public Integer getEquipCount() {
		return equipCount;
	}

	public void setEquipCount(Integer equipCount) {
		this.equipCount = equipCount;
	}

	public Integer getEquipCountAlarm() {
		return equipCountAlarm;
	}

	public void setEquipCountAlarm(Integer equipCountAlarm) {
		this.equipCountAlarm = equipCountAlarm;
	}

	public Integer getEquipCountOffLine() {
		return equipCountOffLine;
	}

	public void setEquipCountOffLine(Integer equipCountOffLine) {
		this.equipCountOffLine = equipCountOffLine;
	}

	public Integer getSiteCount() {
		return siteCount;
	}

	public void setSiteCount(Integer siteCount) {
		this.siteCount = siteCount;
	}
	public Integer getEquipEffectCount() {
		return equipEffectCount;
	}
	public void setEquipEffectCount(Integer equipEffectCount) {
		this.equipEffectCount = equipEffectCount;
	}
	public Integer getEquipCountFault() {
		return equipCountFault;
	}
	public void setEquipCountFault(Integer equipCountFault) {
		this.equipCountFault = equipCountFault;
	}
	
    

 
 

}