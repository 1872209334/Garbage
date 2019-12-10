package com.qf.model.admin;



import java.io.Serializable;
import java.sql.Timestamp;



/**
 * author Vareck
 */
public class HixentLog implements Serializable  {

	
	
	private Integer id;
	
	private String  username;
	
	private String  message;
	
	private Integer  isBad;

    private String  opration;
    
    private String  controller;
    
    private String  method;
    
    private String  ip;
	
	private String  params;
	
    private String  created_at;


	
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Integer getIsBad() {
        return isBad;
    }

    public void setIsBad(Integer isBad) {
        this.isBad = isBad;
    }
	
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
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

	public Timestamp getTimestamp(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
    
    
}