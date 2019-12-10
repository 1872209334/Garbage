package com.qf.model.fire;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentArcSiteEquipmentRelevance implements Serializable {



    private Integer id;
	
    private Integer bid;
	
    private String  did;
 

  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }
	
    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

	

}