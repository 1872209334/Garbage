package com.qf.model.fire;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentArcEquipmentInfo implements Serializable {



    private String  id;
	
    private String  module_code;
	
    private String  line_code;
	
    private String  device_code;
	
    private String  site_code;
	
    private String  address;
	
    private String  device_ip;
	
    private String  node;
	
    private Integer city_id;
	
    private Integer province_id;
	
    private Integer area_id;
	
    private Integer status;
	
    private Integer is_effect;
	
    private Integer device_type;
	
    private Integer report_time;
	
    private Integer register_time;
  
    private String  lng_bmap;
	
    private String  lat_bmap;
    
    private String temp_alarm_en;
    
    private String arc_alarm_en;
    
    private String alive_en;
    
    private Integer admin_id;
    
    private String efm_id;
    
    private String type;
    
    private String subtype;
    
    private String version;
    
    private String sens;
    
    private String erelay;
    
    private String tempration;
    
    private String trelay;
    
    private String time;
    
    private String cnt;
    
    private String message;
    
    private String addr;
    
    private String fire_info;
    
    private String broken_info;
    
    private String other_info;

    private String equip_place; //设备位置
    
    private Integer equip_isGroup;//判断是否已被管控分组  0否 1是
    
    private Integer equip_group_id;//管控分组Id
    
    private Integer is_alarm;//是否报警
    
    private String niNameDevice;
    
    private String specificatorDevice;
    
    private String siteName;
    
    
    private Integer isOfflineAlarm;
	
    private Integer offlineEnable;
	
    private Integer isFireAlarm;;
	
    private Integer fireEnable;
	
    private Integer isTemperatureAlarm;
	
    private Integer temperatureEnable;
    
    private String upgradeFlag;
    
    private String boardVersion;
    
    private String softVersion;
    
    private String releaseData;
    
    private String equipmentManufacturerCode;
    
    private String nbPhoneNumber;
    
    private String serverIpAddress;
    
    private Integer serverPortNumber;
    
    private String nbChipSerialNumber;
    
    private String equipmentType;
    
    private String equipmentProductionSequenceNumber;
    
    
    private String acurrentWarningEnable;
    
    private String bcurrentWarningEnable;
    
    private String ccurrentWarningEnable;
    
    private String avoltageWarningEnable;
    
    private String bvoltageWarningEnable;
    
    private String cvoltageWarningEnable;
    
    private String temperatureWarning0Enable;
    
    private String temperatureWarning1Enable;
    
    private String temperatureWarning2Enable;
    
    private String temperatureWarning3Enable;
    
    private String temperatureWarning4Enable;
    
    private String leakageWarningEnable;
     
     private String entranceWarningEnable;
     
     private Integer  buzzerEnable;
     
     private Integer temLimit0;
     private Integer temLimit1;
     private Integer temLimit2;
     private Integer temLimit3;
     private Integer temLimit4;
     private Integer temLimit;
     
     private Integer leakageCurrentUpLimit;
     private Integer aVoltageUpLimit;
     private Integer bVoltageUpLimit;
     private Integer cVoltageUpLimit;
     private Integer aVoltageDownLimit;
     private Integer bVoltageDownLimit;
     private Integer cVoltageDownLimit;
     private Integer aCurrentUpLimit;
     private Integer bCurrentUpLimit;
     private Integer cCurrentUpLimit;
     
     private Integer tem0;
     private Integer tem1;
     private Integer tem2;
     private Integer tem3;
     private Integer tem4;
     
     private Integer leakageCurrent;
     private Integer aCurrent;
     private Integer bCurrent;
     private Integer cCurrent;
     
     private Integer aVoltage;
     private Integer bVoltage;
     private Integer cVoltage;
     
     private String arcWarningSensitivity;
     private String seartbeatTime;
     
    public Integer getTem0() {
		return tem0;
	}

	public void setTem0(Integer tem0) {
		this.tem0 = tem0;
	}

	public Integer getTem1() {
		return tem1;
	}

	public void setTem1(Integer tem1) {
		this.tem1 = tem1;
	}

	public Integer getTem2() {
		return tem2;
	}

	public void setTem2(Integer tem2) {
		this.tem2 = tem2;
	}

	public Integer getTem3() {
		return tem3;
	}

	public void setTem3(Integer tem3) {
		this.tem3 = tem3;
	}

	public Integer getTem4() {
		return tem4;
	}

	public void setTem4(Integer tem4) {
		this.tem4 = tem4;
	}

	public Integer getLeakageCurrent() {
		return leakageCurrent;
	}

	public void setLeakageCurrent(Integer leakageCurrent) {
		this.leakageCurrent = leakageCurrent;
	}

	
	public Integer getaCurrent() {
		return aCurrent;
	}

	public void setaCurrent(Integer aCurrent) {
		this.aCurrent = aCurrent;
	}

	public Integer getbCurrent() {
		return bCurrent;
	}

	public void setbCurrent(Integer bCurrent) {
		this.bCurrent = bCurrent;
	}

	public Integer getcCurrent() {
		return cCurrent;
	}

	public void setcCurrent(Integer cCurrent) {
		this.cCurrent = cCurrent;
	}

	public Integer getaVoltage() {
		return aVoltage;
	}

	public Integer getLeakageCurrentUpLimit() {
		return leakageCurrentUpLimit;
	}

	public void setLeakageCurrentUpLimit(Integer leakageCurrentUpLimit) {
		this.leakageCurrentUpLimit = leakageCurrentUpLimit;
	}

	public Integer getaVoltageUpLimit() {
		return aVoltageUpLimit;
	}

	public void setaVoltageUpLimit(Integer aVoltageUpLimit) {
		this.aVoltageUpLimit = aVoltageUpLimit;
	}

	public Integer getbVoltageUpLimit() {
		return bVoltageUpLimit;
	}

	public void setbVoltageUpLimit(Integer bVoltageUpLimit) {
		this.bVoltageUpLimit = bVoltageUpLimit;
	}

	public Integer getcVoltageUpLimit() {
		return cVoltageUpLimit;
	}

	public void setcVoltageUpLimit(Integer cVoltageUpLimit) {
		this.cVoltageUpLimit = cVoltageUpLimit;
	}

	public Integer getaVoltageDownLimit() {
		return aVoltageDownLimit;
	}

	public void setaVoltageDownLimit(Integer aVoltageDownLimit) {
		this.aVoltageDownLimit = aVoltageDownLimit;
	}

	public Integer getbVoltageDownLimit() {
		return bVoltageDownLimit;
	}

	public void setbVoltageDownLimit(Integer bVoltageDownLimit) {
		this.bVoltageDownLimit = bVoltageDownLimit;
	}

	public Integer getcVoltageDownLimit() {
		return cVoltageDownLimit;
	}

	public void setcVoltageDownLimit(Integer cVoltageDownLimit) {
		this.cVoltageDownLimit = cVoltageDownLimit;
	}

	public Integer getaCurrentUpLimit() {
		return aCurrentUpLimit;
	}

	public void setaCurrentUpLimit(Integer aCurrentUpLimit) {
		this.aCurrentUpLimit = aCurrentUpLimit;
	}

	public Integer getbCurrentUpLimit() {
		return bCurrentUpLimit;
	}

	public void setbCurrentUpLimit(Integer bCurrentUpLimit) {
		this.bCurrentUpLimit = bCurrentUpLimit;
	}

	public Integer getcCurrentUpLimit() {
		return cCurrentUpLimit;
	}

	public void setcCurrentUpLimit(Integer cCurrentUpLimit) {
		this.cCurrentUpLimit = cCurrentUpLimit;
	}

	public void setaVoltage(Integer aVoltage) {
		this.aVoltage = aVoltage;
	}

	public Integer getbVoltage() {
		return bVoltage;
	}

	public void setbVoltage(Integer bVoltage) {
		this.bVoltage = bVoltage;
	}

	public Integer getcVoltage() {
		return cVoltage;
	}

	public void setcVoltage(Integer cVoltage) {
		this.cVoltage = cVoltage;
	}

	public String getAcurrentWarningEnable() {
		return acurrentWarningEnable;
	}

	public void setAcurrentWarningEnable(String acurrentWarningEnable) {
		this.acurrentWarningEnable = acurrentWarningEnable;
	}

	public String getBcurrentWarningEnable() {
		return bcurrentWarningEnable;
	}

	public void setBcurrentWarningEnable(String bcurrentWarningEnable) {
		this.bcurrentWarningEnable = bcurrentWarningEnable;
	}

	public String getCcurrentWarningEnable() {
		return ccurrentWarningEnable;
	}

	public void setCcurrentWarningEnable(String ccurrentWarningEnable) {
		this.ccurrentWarningEnable = ccurrentWarningEnable;
	}

	public String getAvoltageWarningEnable() {
		return avoltageWarningEnable;
	}

	public void setAvoltageWarningEnable(String avoltageWarningEnable) {
		this.avoltageWarningEnable = avoltageWarningEnable;
	}

	public Integer getTemLimit0() {
		return temLimit0;
	}

	public void setTemLimit0(Integer temLimit0) {
		this.temLimit0 = temLimit0;
	}

	public Integer getTemLimit1() {
		return temLimit1;
	}

	public void setTemLimit1(Integer temLimit1) {
		this.temLimit1 = temLimit1;
	}

	public Integer getTemLimit2() {
		return temLimit2;
	}

	public void setTemLimit2(Integer temLimit2) {
		this.temLimit2 = temLimit2;
	}

	public Integer getTemLimit3() {
		return temLimit3;
	}

	public void setTemLimit3(Integer temLimit3) {
		this.temLimit3 = temLimit3;
	}

	public Integer getTemLimit4() {
		return temLimit4;
	}

	public void setTemLimit4(Integer temLimit4) {
		this.temLimit4 = temLimit4;
	}

	public String getBvoltageWarningEnable() {
		return bvoltageWarningEnable;
	}

	public void setBvoltageWarningEnable(String bvoltageWarningEnable) {
		this.bvoltageWarningEnable = bvoltageWarningEnable;
	}

	public String getCvoltageWarningEnable() {
		return cvoltageWarningEnable;
	}

	public void setCvoltageWarningEnable(String cvoltageWarningEnable) {
		this.cvoltageWarningEnable = cvoltageWarningEnable;
	}

	public String getTemperatureWarning0Enable() {
		return temperatureWarning0Enable;
	}

	public void setTemperatureWarning0Enable(String temperatureWarning0Enable) {
		this.temperatureWarning0Enable = temperatureWarning0Enable;
	}

	public String getTemperatureWarning1Enable() {
		return temperatureWarning1Enable;
	}

	public void setTemperatureWarning1Enable(String temperatureWarning1Enable) {
		this.temperatureWarning1Enable = temperatureWarning1Enable;
	}

	public String getTemperatureWarning2Enable() {
		return temperatureWarning2Enable;
	}

	public void setTemperatureWarning2Enable(String temperatureWarning2Enable) {
		this.temperatureWarning2Enable = temperatureWarning2Enable;
	}

	public String getTemperatureWarning3Enable() {
		return temperatureWarning3Enable;
	}

	public void setTemperatureWarning3Enable(String temperatureWarning3Enable) {
		this.temperatureWarning3Enable = temperatureWarning3Enable;
	}

	public String getTemperatureWarning4Enable() {
		return temperatureWarning4Enable;
	}

	public void setTemperatureWarning4Enable(String temperatureWarning4Enable) {
		this.temperatureWarning4Enable = temperatureWarning4Enable;
	}

	public String getLeakageWarningEnable() {
		return leakageWarningEnable;
	}

	public void setLeakageWarningEnable(String leakageWarningEnable) {
		this.leakageWarningEnable = leakageWarningEnable;
	}

	public String getEntranceWarningEnable() {
		return entranceWarningEnable;
	}

	public void setEntranceWarningEnable(String entranceWarningEnable) {
		this.entranceWarningEnable = entranceWarningEnable;
	}

	
    public String getEquipmentManufacturerCode() {
		return equipmentManufacturerCode;
	}

	public void setEquipmentManufacturerCode(String equipmentManufacturerCode) {
		this.equipmentManufacturerCode = equipmentManufacturerCode;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getNbPhoneNumber() {
		return nbPhoneNumber;
	}

	public void setNbPhoneNumber(String nbPhoneNumber) {
		this.nbPhoneNumber = nbPhoneNumber;
	}

	public String getServerIpAddress() {
		return serverIpAddress;
	}

	public void setServerIpAddress(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}

	public Integer getServerPortNumber() {
		return serverPortNumber;
	}

	public void setServerPortNumber(Integer serverPortNumber) {
		this.serverPortNumber = serverPortNumber;
	}

	public String getEquipmentProductionSequenceNumber() {
		return equipmentProductionSequenceNumber;
	}

	public void setEquipmentProductionSequenceNumber(String equipmentProductionSequenceNumber) {
		this.equipmentProductionSequenceNumber = equipmentProductionSequenceNumber;
	}

	
    
    
    public Integer getIsOfflineAlarm() {
		return isOfflineAlarm;
	}

	public void setIsOfflineAlarm(Integer isOfflineAlarm) {
		this.isOfflineAlarm = isOfflineAlarm;
	}

	public Integer getOfflineEnable() {
		return offlineEnable;
	}

	public void setOfflineEnable(Integer offlineEnable) {
		this.offlineEnable = offlineEnable;
	}

	public Integer getIsFireAlarm() {
		return isFireAlarm;
	}

	public void setIsFireAlarm(Integer isFireAlarm) {
		this.isFireAlarm = isFireAlarm;
	}

	public Integer getFireEnable() {
		return fireEnable;
	}

	public void setFireEnable(Integer fireEnable) {
		this.fireEnable = fireEnable;
	}

	public Integer getIsTemperatureAlarm() {
		return isTemperatureAlarm;
	}

	public void setIsTemperatureAlarm(Integer isTemperatureAlarm) {
		this.isTemperatureAlarm = isTemperatureAlarm;
	}

	public Integer getTemperatureEnable() {
		return temperatureEnable;
	}

	public void setTemperatureEnable(Integer temperatureEnable) {
		this.temperatureEnable = temperatureEnable;
	}

	public String getUpgradeFlag() {
		return upgradeFlag;
	}

	public void setUpgradeFlag(String upgradeFlag) {
		this.upgradeFlag = upgradeFlag;
	}

	public String getBoardVersion() {
		return boardVersion;
	}

	public void setBoardVersion(String boardVersion) {
		this.boardVersion = boardVersion;
	}

	public String getSoftVersion() {
		return softVersion;
	}

	public void setSoftVersion(String softVersion) {
		this.softVersion = softVersion;
	}

	public String getReleaseData() {
		return releaseData;
	}

	public void setReleaseData(String releaseData) {
		this.releaseData = releaseData;
	}

	public void setOtherInfo(String other_info) {
        this.other_info = other_info;
    }
	
    public String getOtherInfo() {
        return other_info;
    }
    
    public void setBrokenInfo(String broken_info) {
        this.broken_info = broken_info;
    }
	
    public String getBrokenInfo() {
        return broken_info;
    }
    
    public void setFireInfo(String fire_info) {
        this.fire_info = fire_info;
    }
	
    public String getFireInfo() {
        return fire_info;
    }
    
  
	
    public Integer getRegisterTime() {
        return register_time;
    }

    public void setRegisterTime(Integer register_time) {
        this.register_time = register_time;
    }
	
    public Integer getReportTime() {
        return report_time;
    }

    public void setReportTime(Integer report_time) {
        this.report_time = report_time;
    }
  
    public Integer getDeviceType() {
        return device_type;
    }

    public void setDeviceType(Integer device_type) {
        this.device_type = device_type;
    }
	
    public Integer getIsEffect() {
        return is_effect;
    }

    public void setIsEffect(Integer is_effect) {
        this.is_effect = is_effect;
    }
	
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
	
    public Integer getAreaId() {
        return area_id;
    }

    public void setAreaId(Integer area_id) {
        this.area_id = area_id;
    }
	
    public Integer getProvinceId() {
        return province_id;
    }

    public void setProvinceId(Integer province_id) {
        this.province_id = province_id;
    }
	
    public Integer getCityId() {
        return city_id;
    }

    public void setCityId(Integer city_id) {
        this.city_id = city_id;
    }
	
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
	
    public String getDeviceIp() {
        return device_ip;
    }

    public void setDeviceIp(String device_ip) {
        this.device_ip = device_ip;
    }
	
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
	
    public String getSiteCode() {
        return site_code;
    }

    public void setSiteCode(String site_code) {
        this.site_code = site_code;
    }
	
    public String getDeviceCode() {
        return device_code;
    }

    public void setDeviceCode(String device_code) {
        this.device_code = device_code;
    }
	
    public String getLineCode() {
        return line_code;
    }

    public void setLineCode(String line_code) {
        this.line_code = line_code;
    }
	
    public String getModuleCode() {
        return module_code;
    }

    public void setModuleCode(String module_code) {
        this.module_code = module_code;
    }
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }
    
    public String getErelay() {
        return erelay;
    }

    public void setErelay(String erelay) {
        this.erelay = erelay;
    }
    
    public String getTempration() {
        return tempration;
    }

    public void setTempration(String tempration) {
        this.tempration = tempration;
    }
    
    public String getTrelay() {
        return trelay;
    }

    public void setTrelay(String trelay) {
        this.trelay = trelay;
    }
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    
    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTempAlarmEn() {
        return temp_alarm_en;
    }

    public void setTempAlarmEn(String temp_alarm_en) {
        this.temp_alarm_en = temp_alarm_en;
    }
    
    public String getArcAlarmEn() {
        return arc_alarm_en;
    }

    public void setArcAlarmEn(String arc_alarm_en) {
        this.arc_alarm_en = arc_alarm_en;
    }
    
    public String getAliveEn() {
        return alive_en;
    }

    public void setAliveEn(String alive_en) {
        this.alive_en = alive_en;
    }
    
    public Integer getAdminId() {
        return admin_id;
    }

    public void setAdminId(Integer admin_id) {
        this.admin_id = admin_id;
    }
    
    
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

	public String getModule_code() {
		return module_code;
	}

	public void setModule_code(String module_code) {
		this.module_code = module_code;
	}

	public String getLine_code() {
		return line_code;
	}

	public void setLine_code(String line_code) {
		this.line_code = line_code;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getSite_code() {
		return site_code;
	}

	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	public String getDevice_ip() {
		return device_ip;
	}

	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}

	public Integer getArea_id() {
		return area_id;
	}

	public void setArea_id(Integer area_id) {
		this.area_id = area_id;
	}

	public Integer getIs_effect() {
		return is_effect;
	}

	public void setIs_effect(Integer is_effect) {
		this.is_effect = is_effect;
	}

	public Integer getDevice_type() {
		return device_type;
	}

	public void setDevice_type(Integer device_type) {
		this.device_type = device_type;
	}

	public Integer getReport_time() {
		return report_time;
	}

	public void setReport_time(Integer report_time) {
		this.report_time = report_time;
	}

	public Integer getRegister_time() {
		return register_time;
	}

	public void setRegister_time(Integer register_time) {
		this.register_time = register_time;
	}

	

	public String getTemp_alarm_en() {
		return temp_alarm_en;
	}

	public void setTemp_alarm_en(String temp_alarm_en) {
		this.temp_alarm_en = temp_alarm_en;
	}

	public String getArc_alarm_en() {
		return arc_alarm_en;
	}

	public void setArc_alarm_en(String arc_alarm_en) {
		this.arc_alarm_en = arc_alarm_en;
	}

	public String getAlive_en() {
		return alive_en;
	}

	public void setAlive_en(String alive_en) {
		this.alive_en = alive_en;
	}

	public Integer getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}

	
	public String getEfm_id() {
		return efm_id;
	}

	public void setEfm_id(String efm_id) {
		this.efm_id = efm_id;
	}

	public String getFire_info() {
		return fire_info;
	}

	public void setFire_info(String fire_info) {
		this.fire_info = fire_info;
	}

	public String getBroken_info() {
		return broken_info;
	}

	public void setBroken_info(String broken_info) {
		this.broken_info = broken_info;
	}

	public String getOther_info() {
		return other_info;
	}

	public void setOther_info(String other_info) {
		this.other_info = other_info;
	}

	public String getEquip_place() {
		return equip_place;
	}

	public void setEquip_place(String equip_place) {
		this.equip_place = equip_place;
	}

	public Integer getEquip_isGroup() {
		return equip_isGroup;
	}

	public void setEquip_isGroup(Integer equip_isGroup) {
		this.equip_isGroup = equip_isGroup;
	}

	public Integer getEquip_group_id() {
		return equip_group_id;
	}

	public void setEquip_group_id(Integer equip_group_id) {
		this.equip_group_id = equip_group_id;
	}

	public Integer getIs_alarm() {
		return is_alarm;
	}

	public void setIs_alarm(Integer is_alarm) {
		this.is_alarm = is_alarm;
	}

	public String getNiNameDevice() {
		return niNameDevice;
	}

	public void setNiNameDevice(String niNameDevice) {
		this.niNameDevice = niNameDevice;
	}

	public String getSpecificatorDevice() {
		return specificatorDevice;
	}

	public void setSpecificatorDevice(String specificatorDevice) {
		this.specificatorDevice = specificatorDevice;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getLngBmap() {
		return lng_bmap;
	}

	public void setLngBmap(String lng_bmap) {
		this.lng_bmap = lng_bmap;
	}

	public String getLatBmap() {
		return lat_bmap;
	}

	public void setLatBmap(String lat_bmap) {
		this.lat_bmap = lat_bmap;
	}

	public String getNbChipSerialNumber() {
		return nbChipSerialNumber;
	}

	public void setNbChipSerialNumber(String nbChipSerialNumber) {
		this.nbChipSerialNumber = nbChipSerialNumber;
	}

	public Integer getBuzzerEnable() {
		return buzzerEnable;
	}

	public void setBuzzerEnable(Integer buzzerEnable) {
		this.buzzerEnable = buzzerEnable;
	}

	public Integer getTemLimit() {
		return temLimit;
	}

	public void setTemLimit(Integer temLimit) {
		this.temLimit = temLimit;
	}

	public String getArcWarningSensitivity() {
		return arcWarningSensitivity;
	}

	public void setArcWarningSensitivity(String arcWarningSensitivity) {
		this.arcWarningSensitivity = arcWarningSensitivity;
	}

	public String getSeartbeatTime() {
		return seartbeatTime;
	}

	public void setSeartbeatTime(String seartbeatTime) {
		this.seartbeatTime = seartbeatTime;
	}

	
   
    
}