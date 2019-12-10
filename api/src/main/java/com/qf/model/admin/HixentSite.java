package com.qf.model.admin;

public class HixentSite {
//   private Integer siteId;
//   private String sitePlace;
//   private String siteName;
//   private Integer siteBuildId;
   private static final long serialVersionUID = 1L;
	//站点id
	private Integer siteId;
	//站点编号
//	private String siteCode;
	private String siteCord;
	//站点名称
	private String siteName;
	//项目位置
	private String sitePlace;
	//建筑ID
	private Integer siteBuildId;
	//建筑名称
	private String buildName;
	//经度
	private String longitute;
	//维度
	private String latitude;
	//地图精确到米数，单位为米
	private Integer mapNum;
	//logo原文件名
	private String logoFileName;
	//logo地址
	private String logoFileUrl;
	//logo相对地址(OSS位置)
	private String logoRelativeFileUrl;
	//背景原文件名
	private String backFileName;
	//背景地址
	private String backFileUrl;
	//背景相对地址(OSS位置)
	private String backRelativeFileUrl;
	
	
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
	public String getSiteCord() {
		return siteCord;
	}
	public void setSiteCord(String siteCord) {
		this.siteCord = siteCord;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSitePlace() {
		return sitePlace;
	}
	public void setSitePlace(String sitePlace) {
		this.sitePlace = sitePlace;
	}
	public Integer getSiteBuildId() {
		return siteBuildId;
	}
	public void setSiteBuildId(Integer siteBuildId) {
		this.siteBuildId = siteBuildId;
	}
	public String getBuildName() {
		return buildName;
	}
	public void setBuildName(String buildName) {
		this.buildName = buildName;
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
	public String getLogoFileName() {
		return logoFileName;
	}
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	public String getLogoFileUrl() {
		return logoFileUrl;
	}
	public void setLogoFileUrl(String logoFileUrl) {
		this.logoFileUrl = logoFileUrl;
	}
	public String getLogoRelativeFileUrl() {
		return logoRelativeFileUrl;
	}
	public void setLogoRelativeFileUrl(String logoRelativeFileUrl) {
		this.logoRelativeFileUrl = logoRelativeFileUrl;
	}
	public String getBackFileName() {
		return backFileName;
	}
	public void setBackFileName(String backFileName) {
		this.backFileName = backFileName;
	}
	public String getBackFileUrl() {
		return backFileUrl;
	}
	public void setBackFileUrl(String backFileUrl) {
		this.backFileUrl = backFileUrl;
	}
	public String getBackRelativeFileUrl() {
		return backRelativeFileUrl;
	}
	public void setBackRelativeFileUrl(String backRelativeFileUrl) {
		this.backRelativeFileUrl = backRelativeFileUrl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
