package com.qf.model.admin.valid;



import com.qf.model.Base;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;



/**
 * author RuanYu
 */
public class ValidHixentArea extends Base {
	
	
	
    private Integer privinceId=0 ;
    
    private Integer areaId=0 ;
   
	public Integer getPrivinceId() {
		return privinceId;
	}

	public void setPrivinceId(Integer privinceId) {
		this.privinceId = privinceId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

    
  

    
   
    
    
}