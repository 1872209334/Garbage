package com.qf.model.admin.valid;



import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentMessage extends Base {
	
	
	
	private String  idStr     = "";
	
    private Integer id   	  = 0;
    
    private Integer type   	  = 0;
	
	private Integer state;

    private String  username  = "";
    
    private String  mobile    = "";


   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
	
	public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    
    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }
    
    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }
    
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    
    
}