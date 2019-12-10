package com.qf.model.fire.valid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * author RuanYu
 */
public class ValidHixentEditEquipInfo {

	
	private String equipPlace;
	
	
	private String newEquipCode ;
	
	
	private String deviceCode;
	
	
	private String siteCode;
	
	
	private String equipId;
	
    
	
//	@NotEmpty(message = "请填写温度上限！")
//	@Pattern(message ="只能输入数字", regexp = "^[0-9]+(.[0-9]{1})?$")
//	@Min(value=0,message ="温度值不能低于0")
//	@Max(value=300,message ="温度值不能高于300")
	private String temUpLimit;
	
	
	public String getEquipPlace() {
		return equipPlace;
	}
	public void setEquipPlace(String equipPlace) {
		this.equipPlace = equipPlace;
	}
	
	public String getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getTemUpLimit() {
		return temUpLimit;
	}
	public void setTemUpLimit(String temUpLimit) {
		this.temUpLimit = temUpLimit;
	}
	public String getNewEquipCode() {
		return newEquipCode;
	}
	public void setNewEquipCode(String newEquipCode) {
		this.newEquipCode = newEquipCode;
	}
	public String getEquipId() {
		return equipId;
	}
	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}
	
  
	
}