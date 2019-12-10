package com.qf.model.fire;

public class HixentArcAppDeviceAndEquipWarn extends  HixentArcAppDeviceAndEquipInfo{
	
	private Integer warnId;
	
	private String warnName;
	
	private Integer warnTime;
	
	private Integer dealTime;
	
	private Integer dealId;
	
	private Integer isDeal;
	
	private String filePath;
	
	private Integer etype;

	public Integer getWarnId() {
		return warnId;
	}

	public void setWarnId(Integer warnId) {
		this.warnId = warnId;
	}

	public String getWarnName() {
		return warnName;
	}

	public void setWarnName(String warnName) {
		this.warnName = warnName;
	}

	public Integer getWarnTime() {
		return warnTime;
	}

	public void setWarnTime(Integer warnTime) {
		this.warnTime = warnTime;
	}

	public Integer getDealTime() {
		return dealTime;
	}

	public void setDealTime(Integer dealTime) {
		this.dealTime = dealTime;
	}

	public Integer getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(Integer isDeal) {
		this.isDeal = isDeal;
	}

	public Integer getDealId() {
		return dealId;
	}

	public void setDealId(Integer dealId) {
		this.dealId = dealId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getEtype() {
		return etype;
	}

	public void setEtype(Integer etype) {
		this.etype = etype;
	}

	
	
}