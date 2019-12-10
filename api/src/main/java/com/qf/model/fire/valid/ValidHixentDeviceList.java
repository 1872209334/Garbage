package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentDeviceList  extends Base {

	

	@NotEmpty(message="请选择虚拟分组！")
    private Integer pid;
	

  
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    
    
}