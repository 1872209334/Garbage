package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;



/**
 * author RuanYu
 */
public class ValidHixentDelEquip  {

	

	@NotEmpty(message="请选中设备！")
	private String deviceIds;
	
	
	
	public String getDeviceIds() {
		return deviceIds;
	}

	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}

	

	


	
	
   
    
}