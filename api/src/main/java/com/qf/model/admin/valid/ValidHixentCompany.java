package com.qf.model.admin.valid;



import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentCompany extends Base {

	
	
	private String  idStr      = "0";
	
	private Integer  id        = 0;
    
    private String  name;
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    
    
}