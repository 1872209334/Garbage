package com.qf.model.fire;

import com.alibaba.fastjson.JSONObject;

public class HixentArcAppMapSite {
	
	
	
	private String siteName;
	private String siteCode;
	private String id;
	private String latBmap;
	private String lngBmap;
	private String latitude;
	private String longitute;
	private String place;
	private String warnNo;
	private String message;
	private JSONObject countData;
	
	
	public String getWarnNo() {
		return warnNo;
	}
	public void setWarnNo(String warnNo) {
		this.warnNo = warnNo;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLatBmap() {
		return latBmap;
	}
	public void setLatBmap(String latBmap) {
		this.latBmap = latBmap;
	}
	public String getLngBmap() {
		return lngBmap;
	}
	public void setLngBmap(String lngBmap) {
		this.lngBmap = lngBmap;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitute() {
		return longitute;
	}
	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public JSONObject getCountData() {
		return countData;
	}
	public void setCountData(JSONObject countData) {
		this.countData = countData;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}