package com.qf.model.admin;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentCompany implements Serializable {

    private Integer  cid;

    private String   name;
    
    private String   logo_url;

  
	
    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public String getLogoUrl() {
        return logo_url;
    }

    public void setLogoUrl(String logo_url) {
        this.logo_url = logo_url;
    }
 
 
 
}