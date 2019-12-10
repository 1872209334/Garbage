package com.qf.model.admin.valid;



import com.qf.model.Base;
import javax.validation.constraints.Pattern;



/**
 * author Vareck
 */
public class ValidHixentAppUserMore extends Base {
	
	
	
    private Integer id   	 = 0;
	
	private Integer state    = 0;
	
	private Integer statek ;
	
    private Integer isPage 	 = 0;
    
    private Integer cid      = 0;
	
	private Integer pid      = 0;

    private String name      = "";
    
    private String projectId = "";
    
    private String ncode     = "";
    
    private String mobile    = "";
    
    private String email     = "";
    
    private String account   = "";
    
    //@Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$",message="密码必须是由8到16的数字和字母的组合！")
    private String password  = "";
    
    private String idStr     = "0";
    
    private String order     = "id asc";

    private String remark ="";
    
    public Integer getIsPage() {
        return isPage;
    }

    public void setIsPage(Integer isPage) {
        this.isPage = isPage;
    }
	
	public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    
	public Integer getStatek() {
        return statek;
    }

    public void setStatek(Integer statek) {
        this.statek = statek;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNcode() {
        return ncode;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
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
    
    public Integer getCid() {
        return cid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
	
    public Integer getPid() {
        return pid;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
    
}