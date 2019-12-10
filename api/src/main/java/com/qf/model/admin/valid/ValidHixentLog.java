package com.qf.model.admin.valid;



import com.qf.model.Base;



/**
 * author Vareck
 */
public class ValidHixentLog extends Base {



	private String  username;

    private String  opration;
    
    private String  controller;
    
    private String  method;
    
    private Integer isException = 0;
    
    private String  ip;

	private String  time1;
		
	private String  time2;
    
	
	
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getOpration() {
        return opration;
    }

    public void setOpration(String opration) {
        this.opration = opration;
    }
	
    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }
	
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
	
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Integer getIsException() {
        return isException;
    }

    public void setIsException(Integer isException) {
        this.isException = isException;
    }
	
    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }
	
    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    
    
}