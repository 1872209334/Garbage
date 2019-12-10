package com.qf.model.admin.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidHixentAppMobileLogin {
	
	
	
    @NotEmpty(message="手机号不能为空")
    private String mobile;

    @NotEmpty(message="验证码不能为空")
    private String ncode;

    

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNcode() {
        return ncode;
    }
    
    public void setNcode(String ncode) {
        this.ncode = ncode;
    }
    
    
    
}