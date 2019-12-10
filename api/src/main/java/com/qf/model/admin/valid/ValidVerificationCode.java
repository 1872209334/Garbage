package com.qf.model.admin.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidVerificationCode {
	
	
	
    @NotEmpty(message="手机号不能为空")
    private String mobile;

    

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    
    
}