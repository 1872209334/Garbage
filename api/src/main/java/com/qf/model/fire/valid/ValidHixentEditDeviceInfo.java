package com.qf.model.fire.valid;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;

/**
 * author RuanYu
 */
public class ValidHixentEditDeviceInfo {

	
	private String devicePlace ;
	
	private String deviceId ;
	
	private String niName;
	
	private String siteCode;
	
	private String latitude;
	
	private String longitute;
	
	private Integer offlineTime;
	
	private Integer offlineEnable=-1;
	
	
	
	public String getDevicePlace() {
		return devicePlace;
	}
	public void setDevicePlace(String devicePlace) {
		this.devicePlace = devicePlace;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getNiName() {
		return niName;
	}
	public void setNiName(String niName) {
		this.niName = niName;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitute() {
		return longitute;
	}
	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}
	
	public Integer getOfflineTime() {
		return offlineTime;
	}
	public void setOfflineTime(Integer offlineTime) {
		this.offlineTime = offlineTime;
	}
	public Integer getOfflineEnable() {
		return offlineEnable;
	}
	public void setOfflineEnable(Integer offlineEnable) {
		this.offlineEnable = offlineEnable;
	}
	
  
}