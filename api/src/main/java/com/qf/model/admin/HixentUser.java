package com.qf.model.admin;



import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentUser implements Serializable {
	
	
	
    @Id
    @Column(name = "uid")
    @GeneratedValue(generator = "UUID")
    private String  uid;
    
    private Integer id;
    
    private String  img_url;
    
    private String  bid;

    private String  account;

    private String  password;

    private String  salt;

    private Integer createdAt;

    private Integer updatedAt;
    
    private Integer  cid;    
    
    private String  mobile;

    private Integer fire_role;
    
    private Integer areaId;
    
    private Integer provinceId;

    private String remark;
    
    private String areaName;
    
    private String provinceName;
    
    private Integer roleType;
    
    private String roleName;
    
    public String getImgUrl() {
        return img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }
    
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Integer updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    
    public Integer getFireRole() {
        return fire_role;
    }

    public void setFireRole(Integer fire_role) {
        this.fire_role = fire_role;
    }

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getRoleType() {
		return roleType;
	}

	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}
    
    
    
}