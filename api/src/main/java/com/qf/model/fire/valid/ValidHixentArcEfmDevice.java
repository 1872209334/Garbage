package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentArcEfmDevice  extends Base {
	

    
    private String siteCode;
	
	private String deviceCode;

    
    
    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

	public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
    
   
   
}