package com.qf.model.admin.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidHixentDictionary {
	
    @NotEmpty(message="请选择要删除的数据")
    private String idStr;

    
    public String getIdStr() {
        return idStr;
    }
    
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    
    
}