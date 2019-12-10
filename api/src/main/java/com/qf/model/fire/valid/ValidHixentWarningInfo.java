package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidHixentWarningInfo  {

	

	@NotEmpty(message="请选择告警信息")
	private Integer warningNo;
	

	
    public Integer getWarningNo() {
        return warningNo;
    }

    public void setWarningNo(Integer warningNo) {
        this.warningNo = warningNo;
    }

    
    
}