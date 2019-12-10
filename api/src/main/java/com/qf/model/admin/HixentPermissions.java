package com.qf.model.admin;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentPermissions implements Serializable  {


	
	private Integer  menu_id;
	
	private Integer  parent_id;	
	
	private String   action_name;	

	private String   menu_name;	
	
	private String   menu_icon;
	
	private String   menu_url;	

	
	
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
	
    
    
}