package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidHixentDeviceInfo  {

	

	@NotEmpty(message="请选择终端设备")
    private String eid;
	
	private String name;
		
    private String address;
	

  
    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    
    
}