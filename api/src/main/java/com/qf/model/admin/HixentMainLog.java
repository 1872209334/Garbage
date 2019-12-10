package com.qf.model.admin;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentMainLog implements Serializable  {



	private Integer id;
	
	private String  params;
	
    private String  created_at;


	
    public String getName() {
        return params;
    }

    public void setName(String params) {
        this.params = params;
    }
	
    public String getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	
    
    
}