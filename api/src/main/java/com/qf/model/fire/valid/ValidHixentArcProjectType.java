package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import com.qf.model.Base;



/**
 * author wjr
 */
public class ValidHixentArcProjectType  extends Base {
	

    
    private String name;
	
	private String address;
	
	private Integer type;
	
	private Integer admin_id;

    
    
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    public Integer getAdminId() {
        return admin_id;
    }

    public void setAdminId(Integer admin_id) {
        this.admin_id = admin_id;
    }
    
   
   
}