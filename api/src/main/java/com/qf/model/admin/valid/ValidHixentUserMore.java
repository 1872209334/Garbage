package com.qf.model.admin.valid;



import com.qf.model.Base;
import javax.validation.constraints.Pattern;



/**
 * author Vareck
 */
public class ValidHixentUserMore extends Base {
	
	
	
    private Integer id   	   = 0;
    
    private String bid = "";
    
    private Integer cid        = 0;
    
    private Integer fireRole   = 0;

    private String name        = "";
    
    private String mobile      = "";
    
    private String account     = "";
    
    //@Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$",message="密码必须是由8到16的数字和字母的组合！")
    private String password    = "";
    
    //@Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$",message="密码校验必须是由8到16的数字和字母的组合！")
    private String password2   = "";
    
    private String idStr       = "0";
    
    private String order       = "id asc";
    
    private String remark     ="" ;
    
    private Integer provinceId =0;
    
    private Integer areaId =0;
    
    

    
    
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

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
    
    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
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
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
    
    
    
}