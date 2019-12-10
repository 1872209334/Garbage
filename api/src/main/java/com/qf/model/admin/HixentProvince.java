package com.qf.model.admin;

public class HixentProvince {
   private Integer provinceId;
   private String provinceName;
   private String provinceCord;
   private Integer provinceCount;
   
   public HixentProvince() {
	// TODO Auto-generated constructor stub
}

public Integer getProvinceId() {
	return provinceId;
}

public void setProvinceId(Integer provinceId) {
	this.provinceId = provinceId;
}

public String getProvinceName() {
	return provinceName;
}

public void setProvinceName(String provinceName) {
	this.provinceName = provinceName;
}

public String getProvinceCord() {
	return provinceCord;
}

public void setProvinceCord(String provinceCord) {
	this.provinceCord = provinceCord;
}

public HixentProvince(Integer provinceId, String provinceName, String provinceCord) {
	super();
	this.provinceId = provinceId;
	this.provinceName = provinceName;
	this.provinceCord = provinceCord;
}

public Integer getProvinceCount() {
	return provinceCount;
}

public void setProvinceCount(Integer provinceCount) {
	this.provinceCount = provinceCount;
}


}
