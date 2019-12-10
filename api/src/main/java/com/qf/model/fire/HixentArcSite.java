package com.qf.model.fire;




import java.io.Serializable;



/**
 * 项目Model
 * author RuanYu
 */
public class HixentArcSite implements Serializable {
	private static final long serialVersionUID = 1L;
	//站点id
	private Integer siteId;
	//站点编号
	private String siteCode;
	//站点名称
	private String siteName;
	//项目位置
	private String sitePlace;
	//建筑ID
	private Integer siteBuildId;
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

	/**
	 * 设置：站点id
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	/**
	 * 获取：站点id
	 */
	public Integer getSiteId() {
		return siteId;
	}
	/**
	 * 设置：站点编号
	 */
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	/**
	 * 获取：站点编号
	 */
	public String getSiteCode() {
		return siteCode;
	}
	/**
	 * 设置：站点名称
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	/**
	 * 获取：站点名称
	 */
	public String getSiteName() {
		return siteName;
	}
	/**
	 * 设置：项目位置
	 */
	public void setSitePlace(String sitePlace) {
		this.sitePlace = sitePlace;
	}
	/**
	 * 获取：项目位置
	 */
	public String getSitePlace() {
		return sitePlace;
	}
	/**
	 * 设置：建筑ID
	 */
	public void setSiteBuildId(Integer siteBuildId) {
		this.siteBuildId = siteBuildId;
	}
	/**
	 * 获取：建筑ID
	 */
	public Integer getSiteBuildId() {
		return siteBuildId;
	}
	/**
	 * 设置：经度
	 */
	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}
	/**
	 * 获取：经度
	 */
	public String getLongitute() {
		return longitute;
	}
	/**
	 * 设置：维度
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：维度
	 */
	public String getLatitude() {
		return latitude;
	}
	/**
	 * 设置：地图精确到米数，单位为米
	 */
	public void setMapNum(Integer mapNum) {
		this.mapNum = mapNum;
	}
	/**
	 * 获取：地图精确到米数，单位为米
	 */
	public Integer getMapNum() {
		return mapNum;
	}
	/**
	 * 设置：logo原文件名
	 */
	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}
	/**
	 * 获取：logo原文件名
	 */
	public String getLogoFileName() {
		return logoFileName;
	}
	/**
	 * 设置：logo地址
	 */
	public void setLogoFileUrl(String logoFileUrl) {
		this.logoFileUrl = logoFileUrl;
	}
	/**
	 * 获取：logo地址
	 */
	public String getLogoFileUrl() {
		return logoFileUrl;
	}
	/**
	 * 设置：logo相对地址(OSS位置)
	 */
	public void setLogoRelativeFileUrl(String logoRelativeFileUrl) {
		this.logoRelativeFileUrl = logoRelativeFileUrl;
	}
	/**
	 * 获取：logo相对地址(OSS位置)
	 */
	public String getLogoRelativeFileUrl() {
		return logoRelativeFileUrl;
	}
	/**
	 * 设置：背景原文件名
	 */
	public void setBackFileName(String backFileName) {
		this.backFileName = backFileName;
	}
	/**
	 * 获取：背景原文件名
	 */
	public String getBackFileName() {
		return backFileName;
	}
	/**
	 * 设置：背景地址
	 */
	public void setBackFileUrl(String backFileUrl) {
		this.backFileUrl = backFileUrl;
	}
	/**
	 * 获取：背景地址
	 */
	public String getBackFileUrl() {
		return backFileUrl;
	}
	/**
	 * 设置：背景相对地址(OSS位置)
	 */
	public void setBackRelativeFileUrl(String backRelativeFileUrl) {
		this.backRelativeFileUrl = backRelativeFileUrl;
	}
	/**
	 * 获取：背景相对地址(OSS位置)
	 */
	public String getBackRelativeFileUrl() {
		return backRelativeFileUrl;
	}
}