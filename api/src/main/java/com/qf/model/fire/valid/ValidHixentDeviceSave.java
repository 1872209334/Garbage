package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidHixentDeviceSave  {

	

    private Integer did = 0;
    
	@NotEmpty(message="请填写设备号！")
    private String  id;
	
	@NotEmpty(message="请填写模块编号！")
    private String  moduleCode;
	
	@NotEmpty(message="请填写总线编号！")
    private String  lineCode;
	
	@NotEmpty(message="请填写设备编号！")
    private String  deviceCode;
	
	@NotEmpty(message="请填写站点编号！")
    private String  siteCode;
	
    private String  address;
	
    private String  deviceIp;
	
    private String  node;
	
    private Integer cityId;
	
    private Integer provinceId;
    
    private Integer projectId;
	
    private Integer areaId;
	
    private Integer status = 0;
	
    private Integer isEffect   = 0;
	
    private Integer deviceType = 0;
  
    private Integer reportTime = 20;
    
    private String  lngBmap;
	
    private String  latBmap;
  

  
    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }
    
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    
    public Integer getReportTime() {
        return reportTime;
    }

    public void setReportTime(Integer reportTime) {
        this.reportTime = reportTime;
    }
    
   
  
    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }
	
    public Integer getIsEffect() {
        return isEffect;
    }

    public void setIsEffect(Integer isEffect) {
        this.isEffect = isEffect;
    }
	
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
	
    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
	
    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }
	
    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
	
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
	
    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
	
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
	
    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
	
    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
	
    public String getLineCode() {
        return lineCode;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }
	
    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getLngBmap() {
		return lngBmap;
	}

	public void setLngBmap(String lngBmap) {
		this.lngBmap = lngBmap;
	}

	public String getLatBmap() {
		return latBmap;
	}

	public void setLatBmap(String latBmap) {
		this.latBmap = latBmap;
	}

    
    
}