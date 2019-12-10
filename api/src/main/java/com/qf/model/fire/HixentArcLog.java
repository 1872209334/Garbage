package com.qf.model.fire;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentArcLog implements Serializable {



    private Integer id;
	
    private Integer post_time;
	
    private String  eid;
	
    private float   temperature;
	
    private float   electricity;


  
    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	
    public Integer getPostTime() {
        return post_time;
    }

    public void setPostTime(Integer post_time) {
        this.post_time = post_time;
    }
	
	public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
	
    public float getElectricity() {
        return electricity;
    }

    public void setElectricity(float electricity) {
        this.electricity = electricity;
    }
	
 

}