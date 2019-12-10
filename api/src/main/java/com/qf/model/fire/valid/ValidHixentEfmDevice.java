package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;



/**
 * author RuanYu
 */
public class ValidHixentEfmDevice  {

	

	@NotEmpty(message="请选择中控！")
    private String efmid;
    

	
	
	public String getEfmid() {
		return efmid;
	}

	public void setEfmid(String efmid) {
		this.efmid = efmid;
	}


	


	
	
   
    
}