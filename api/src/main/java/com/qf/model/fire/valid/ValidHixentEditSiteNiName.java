package com.qf.model.fire.valid;

/**
 * author RuanYu
 */
public class ValidHixentEditSiteNiName  {
	private String siteNiName;
	
	private Integer siteId=0;
	
	private String siteRemark;
	
	private Integer siteBuildId =0;
	
	private String longitute;
	
	private String latitude;
	
	private Integer mapNum;
	
	
	public String getSiteRemark() {
		return siteRemark;
	}

	public void setSiteRemark(String siteRemark) {
		this.siteRemark = siteRemark;
	}

	public Integer getSiteBuildId() {
		return siteBuildId;
	}

	public void setSiteBuildId(Integer siteBuildId) {
		this.siteBuildId = siteBuildId;
	}

	public String getSiteNiName() {
		return siteNiName;
	}

	public void setSiteNiName(String siteNiName) {
		this.siteNiName = siteNiName;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getLongitute() {
		return longitute;
	}

	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public Integer getMapNum() {
		return mapNum;
	}

	public void setMapNum(Integer mapNum) {
		this.mapNum = mapNum;
	}
}