package com.qf.model.admin;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentMessage implements Serializable  {



	private Integer id;
	
	private Integer state;
	
	private String  title;
	
	private String  content;
	
	private String  from_name;
	
	private String  from_mobile;
	
	private String  to_mobile;
	
	private String  add_date;
	
	private String  update_date;
	
	private Integer from_id;
	
	private Integer send_delete;
	
	private Integer receive_delete;
	
	private Integer to_id;

	
	
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
	
	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
	public String getFromMobile() {
        return from_mobile;
    }

    public void setFromMobile(String from_mobile) {
        this.from_mobile = from_mobile;
    }
	
	public String getFromName() {
        return from_name;
    }

    public void setFromName(String from_name) {
        this.from_name = from_name;
    }

	public String getToMobile() {
        return to_mobile;
    }

    public void setToMobile(String to_mobile) {
        this.to_mobile = to_mobile;
    }
	
    public String getAddDate() {
        return add_date;
    }

    public void setAddDate(String add_date) {
        this.add_date = add_date;
    }
	
	public String getUpdateDate() {
        return update_date;
    }

    public void setUpdateDate(String update_date) {
        this.update_date = update_date;
    }
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
	
	public Integer getFromId() {
        return from_id;
    }

    public void setFromId(Integer from_id) {
        this.from_id = from_id;
    }
	
	public Integer getToId() {
        return to_id;
    }

    public void setToId(Integer to_id) {
        this.to_id = to_id;
    }
    
	public Integer getSendDelete() {
        return send_delete;
    }

    public void setSendDelete(Integer send_delete) {
        this.send_delete = send_delete;
    }
    
	public Integer getReceiveDelete() {
        return receive_delete;
    }

    public void setReceiveDelete(Integer receive_delete) {
        this.receive_delete = receive_delete;
    }
	
    
	
}