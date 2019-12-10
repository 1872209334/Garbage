package com.qf.model.admin;



import java.util.List;
import javax.persistence.*;
import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentPermissionsRole implements Serializable  {

	/*角色hixent_fire_role表相关*/
	
    private Integer  role_id;

    private String   role_name;
    
    private String   menu_id_list;
   

	/*菜单功能hixent_fire_menu表相关*/
	
	private Integer  menu_id;
	
	private Integer  parent_id;	
	
	private String   action_name;	

	private String   menu_name;	
	
	private String   menu_icon;
	
	private String   menu_url;	
	
	@Transient
    private List<HixentPermissionsRole> childMenus;
	

	
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
	

	
	
    public String getActionName() {
        return action_name;
    }

    public void setActionName(String action_name) {
        this.action_name = action_name;
    }
	
    public String getMenuName() {
        return menu_name;
    }

    public void setMenuName(String menu_name) {
        this.menu_name = menu_name;
    }
	
    public String getMenuIcon() {
        return menu_icon;
    }

    public void setMenuIcon(String menu_icon) {
        this.menu_icon = menu_icon;
    }
	
    public String getMenuUrl() {
        return menu_url;
    }

    public void setMenuUrl(String menu_url) {
        this.menu_url = menu_url;
    }
	
    public Integer getParentId() {
        return parent_id;
    }

    public void setParentId(Integer parent_id) {
        this.parent_id = parent_id;
    }
    
    public Integer getMenuId() {
        return menu_id;
    }

    public void setMenuId(Integer menu_id) {
        this.menu_id = menu_id;
    }
    
    public List<HixentPermissionsRole> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<HixentPermissionsRole> childMenus) {
        this.childMenus = childMenus;
    }
	
    
    
}