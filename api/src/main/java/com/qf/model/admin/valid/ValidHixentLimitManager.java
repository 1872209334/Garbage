package com.qf.model.admin.valid;



import com.qf.model.Base;



/**
 * author RuanYu
 */
public class ValidHixentLimitManager extends Base {

	private String  limitId;
    private String  type;
    private String  roleId;
    
	public String getLimitId() {
		return limitId;
	}
	public void setLimitId(String limitId) {
		this.limitId = limitId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
    
   
}