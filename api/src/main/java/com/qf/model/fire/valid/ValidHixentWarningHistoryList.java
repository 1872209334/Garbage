package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentWarningHistoryList  extends Base {

	

	@NotEmpty(message="请选择虚拟分组！")
    private Integer pid;
	
    private String startTime;
	
    private String endTime;

  
    
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
    
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    
    
}