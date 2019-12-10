package com.qf.model.fire;




import java.io.Serializable;
import java.util.List;



/**
 *处理反馈Model
 * author RuanYu
 */
public class HixentArcAlarmDealFeedBack implements Serializable {
    
    private Integer id;
    private Integer appUserId;
    private Integer warnId;
    private Integer dealTime;
    private Integer warnTime;
    private String  appUserAccount;
    private String  appUserPhone;
    private String  dealRemark;
    private String  imgUrl;
    private Integer isAudit;
    
    
    public HixentArcAlarmDealFeedBack() {
		// TODO Auto-generated constructor stub
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public Integer getAppUserId() {
		return appUserId;
	}



	public void setAppUserId(Integer appUserId) {
		this.appUserId = appUserId;
	}



	public Integer getWarnId() {
		return warnId;
	}



	public void setWarnId(Integer warnId) {
		this.warnId = warnId;
	}



	public Integer getDealTime() {
		return dealTime;
	}



	public void setDealTime(Integer dealTime) {
		this.dealTime = dealTime;
	}



	public Integer getWarnTime() {
		return warnTime;
	}



	public void setWarnTime(Integer warnTime) {
		this.warnTime = warnTime;
	}





	public String getAppUserPhone() {
		return appUserPhone;
	}



	public void setAppUserPhone(String appUserPhone) {
		this.appUserPhone = appUserPhone;
	}



	public String getDealRemark() {
		return dealRemark;
	}



	public void setDealRemark(String dealRemark) {
		this.dealRemark = dealRemark;
	}



	public String getImgUrl() {
		return imgUrl;
	}



	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}



	public Integer getIsAudit() {
		return isAudit;
	}



	public void setIsAudit(Integer isAudit) {
		this.isAudit = isAudit;
	}



	public String getAppUserAccount() {
		return appUserAccount;
	}



	public void setAppUserAccount(String appUserAccount) {
		this.appUserAccount = appUserAccount;
	}


	
	
}