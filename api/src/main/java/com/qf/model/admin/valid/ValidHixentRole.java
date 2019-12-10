package com.qf.model.admin.valid;



import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentRole extends Base {

	
	
	private String  idStr      = "0";

    private Integer roleId 	   = 0;
    
    private String  name;
    
    private String  menuIdList;
    
    private String  roleDesc;
    
    private String  order      = "role_id asc";

    
    
    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    
    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
    
    public String getMenuIdList() {
        return menuIdList;
    }

    public void setMenuIdList(String menuIdList) {
        this.menuIdList = menuIdList;
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
    
    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

}