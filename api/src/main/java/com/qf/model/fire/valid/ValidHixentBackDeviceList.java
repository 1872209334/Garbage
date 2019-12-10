package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentBackDeviceList  extends Base {


    
    private Integer efmId;
    private String lineCode;
	
    
    
    public Integer getEfmId() {
        return efmId;
    }

    public void setEfmId(Integer efmId) {
        this.efmId = efmId;
    }
    
    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    
    
}