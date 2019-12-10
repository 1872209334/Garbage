package com.qf.model.admin;

public class HixentArea {
   private Integer areaId;
   private Integer parId;
   private String areaName;
   private String areaCord;
   private Integer areaCount;
   public HixentArea() {
	// TODO Auto-generated constructor stub
}

public HixentArea(Integer areaId, Integer parId, String areaName, String areaCord) {
	super();
	this.areaId = areaId;
	this.parId = parId;
	this.areaName = areaName;
	this.areaCord=areaCord;
}

public Integer getAreaId() {
	return areaId;
}

public void setAreaId(Integer areaId) {
	this.areaId = areaId;
}

public Integer getParId() {
	return parId;
}

public void setParId(Integer parId) {
	this.parId = parId;
}



public String getAreaName() {
	return areaName;
}

public void setAreaName(String areaName) {
	this.areaName = areaName;
}

public String getAreaCord() {
	return areaCord;
}

public void setAreaCord(String areaCord) {
	this.areaCord = areaCord;
}

public Integer getAreaCount() {
	return areaCount;
}

public void setAreaCount(Integer areaCount) {
	this.areaCount = areaCount;
}


   

}
