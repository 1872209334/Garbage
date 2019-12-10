package com.qf.model.admin;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentRole implements Serializable  {


	
    private Integer  role_id;

    private String   role_name;
    
    private String   menu_id_list;
	
	private String   role_desc;
	
	 private Integer  role_type;
	
    public Integer getRoleId() {
        return role_id;
    }

    public void setRoleId(Integer role_id) {
        this.role_id = role_id;
    }

    public String getRoleName() {
        return role_name;
    }

    public void setRoleName(String role_name) {
        this.role_name = role_name;
    }
	
    public String getMenuIdList() {
        return menu_id_list;
    }

    public void setMenuIdList(String menu_id_list) {
        this.menu_id_list = menu_id_list;
    }
	
    public String getRoleDesc() {
        return role_desc;
    }

    public void setRoleDesc(String role_desc) {
        this.role_desc = role_desc;
    }

	public Integer getRole_type() {
		return role_type;
	}

	public void setRole_type(Integer role_type) {
		this.role_type = role_type;
	}
	
    
    
}