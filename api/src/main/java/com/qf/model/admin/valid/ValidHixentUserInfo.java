package com.qf.model.admin.valid;



import com.qf.model.Base;
import javax.validation.constraints.Pattern;



/**
 * author Vareck
 */
public class ValidHixentUserInfo extends Base {
	
	
	
    private Integer id   	   = 0;
    
    private Integer cid        = 0;
    
    private Integer fireRole   = 0;

    private String name        = "";
    
    private String mobile      = "";
    
    private String account     = "";
    
    private String idStr       = "0";
    
    private String order       = "id asc";

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    
    public Integer getFireRole() {
        return fireRole;
    }

    public void setFireRole(Integer fireRole) {
        this.fireRole = fireRole;
    }
    
    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
    
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    
    
    
}