package com.qf.model.fire;




import java.io.Serializable;
import java.util.List;



/**
 * 管控分组Model
 * author RuanYu
 */
public class HixentArcControlGroup implements Serializable {
    
    private Integer id;//组ID
    private String appUsers;//组app用户
    private String siteName;//项目名
    private String groupPlace;//位置
    private String mobiles;//组联系电话
    private Integer cnEquip;//组管控设备数量
    private Integer adminId;
    private Integer siteId;
    private String place;
    private String hid;
    private String equipIds;
    private String siteCode;
    
    public HixentArcControlGroup() {
		// TODO Auto-generated constructor stub
	}
    
	public HixentArcControlGroup(Integer id, Integer adminId, Integer siteId, String place) {
		super();
		this.id = id;
		this.adminId = adminId;
		this.siteId = siteId;
		this.place = place;
	}

	public HixentArcControlGroup(Integer id, String appUsers, String siteName, String groupPlace, String mobiles,
			Integer cnEquip) {
		super();
		this.id = id;
		this.appUsers = appUsers;
		this.siteName = siteName;
		this.groupPlace = groupPlace;
		this.mobiles = mobiles;
		this.cnEquip = cnEquip;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAppUsers() {
		return appUsers;
	}
	public void setAppUsers(String appUsers) {
		this.appUsers = appUsers;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getGroupPlace() {
		return groupPlace;
	}
	public void setGroupPlace(String groupPlace) {
		this.groupPlace = groupPlace;
	}
	public String getMobiles() {
		return mobiles;
	}
	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}
	public Integer getCnEquip() {
		return cnEquip;
	}
	public void setCnEquip(Integer cnEquip) {
		this.cnEquip = cnEquip;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(String equipIds) {
		this.equipIds = equipIds;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
    
    
   
}