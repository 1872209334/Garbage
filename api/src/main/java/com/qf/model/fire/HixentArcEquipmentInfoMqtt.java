package com.qf.model.fire;



import java.io.Serializable;

import tk.mybatis.mapper.entity.Example;



/**
 * author Vareck
 */
public class HixentArcEquipmentInfoMqtt implements Serializable {



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
    
    private String equipmentManufacturerCode;
    
    private String equipmentCategory;
    
   private String longitude;
    
    private String latitude;
    
    
    ///////////////////////////////////////////////////////////
    private Integer temperature_value;
    public Integer getTemperature_value() {
        return temperature_value;
    }

    public void setTemperature_value(Integer addr) {
        this.temperature_value = addr;
    }

    ///////////////////////////////////////////////////////////
    private String equipment_manufacturer_code;
    public void setEquipment_manufacturer_code(String equipment_manufacturer_code) {
        this.equipment_manufacturer_code = equipment_manufacturer_code;
    }
	
    public String getEquipment_manufacturer_code() {
        return equipment_manufacturer_code;
    }
    ///////////////////////////////////////////////////////////
    private Integer equipment_category;
    public Integer getEquipment_category() {
        return equipment_category;
    }

    public void setEquipment_category(Integer equipment_category) {
        this.equipment_category = equipment_category;
    }
    
    public String equipment_type;
    public void setEquipment_type(String equipment_type) {
        this.equipment_type = equipment_type;
    }
    public String getEquipment_type() {
        return equipment_type;
    }
    
    public String equipment_production_sequence_number;
    public void setEquipment_production_sequence_number(String equipment_production_sequence_number) {
        this.equipment_production_sequence_number = equipment_production_sequence_number;
    }
    public String getEquipment_production_sequence_number() {
        return equipment_production_sequence_number;
    }
    ///////////////////////////////////////////////////////////

    
    public String software_version;
    public String getEquipmentManufacturerCode() {
		return equipmentManufacturerCode;
	}

	public void setEquipmentManufacturerCode(String equipmentManufacturerCode) {
		this.equipmentManufacturerCode = equipmentManufacturerCode;
	}

	public String getEquipmentCategory() {
		return equipmentCategory;
	}

	public void setEquipmentCategory(String equipmentCategory) {
		this.equipmentCategory = equipmentCategory;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public void setSoftware_version(String software_version) {
        this.software_version = software_version;
    }
    public String getSoftware_version() {
        return software_version;
    }

    public String hardware_version;
    public void setHardware_version(String hardware_version) {
        this.hardware_version = hardware_version;
    }
    public String getHardware_version() {
        return hardware_version;
    }

    
    public String issue_time;
    public void setIssue_time(String issue_time) {
        this.issue_time = issue_time;
    }
    public String getIssue_time() {
        return issue_time;
    }
    
    
    public String boot_version_number;
    public void setBoot_version_number(String boot_version_number) {
        this.boot_version_number = boot_version_number;
    }
    public String getBoot_version_number() {
        return boot_version_number;
    }

    private String subtype;
    public String getSubtype() {
        return subtype;
    }
    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    private String version_information;
    public String getVersion_information() {
        return version_information;
    }
    public void setVersion_information(String version_information) {
        this.version_information = version_information;
    }
    
    private String site_number;
    public String getSite_number() {
        return site_number;
    }
    public void setSite_number(String site_number) {
        this.site_number = site_number;
    }
    
    private String equipment_number;
    public String getEquipment_number() {
        return equipment_number;
    }
    public void setEquipment_number(String equipment_number) {
        this.equipment_number = equipment_number;
    }    
    
    private String bus_number;
    public String getBus_number() {
        return bus_number;
    }
    public void setBus_number(String bus_number) {
        this.bus_number = bus_number;
    }
    
    private String address_number;
    public String getAddress_number() {
        return address_number;
    }
    public void setAddress_number(String address_number) {
        this.address_number = address_number;
    }
    
    private String device_descriptor;
    public String getDevice_descriptor() {
        return device_descriptor;
    }
    public void setDevice_descriptor(String device_descriptor) {
        this.device_descriptor = device_descriptor;
    }
    
    private String nb_category;
    public String getNb_category() {
        return nb_category;
    }
    public void setNb_category(String nb_category) {
        this.nb_category = nb_category;
    }
    
    private String nb_chip_type;
    public String getNb_chip_type() {
        return nb_chip_type;
    }
    public void setNb_chip_type(String nb_chip_type) {
        this.nb_chip_type = nb_chip_type;
    }    
    
    private String nb_chip_serial_number;
    public String getNb_chip_serial_number() {
        return nb_chip_serial_number;
    }
    public void setNb_chip_serial_number(String nb_chip_serial_number) {
        this.nb_chip_serial_number = nb_chip_serial_number;
    }
    
    private String nb_phone_number;
    public String getNb_phone_number() {
        return nb_phone_number;
    }
    public void setNb_phone_number(String nb_phone_number) {
        this.nb_phone_number = nb_phone_number;
    }
    
    private String server_ip_address;
    public String getServer_ip_address() {
        return server_ip_address;
    }
    public void setServer_ip_address(String server_ip_address) {
        this.server_ip_address = server_ip_address;
    }
    
    private String server_port_number;
    public String getServer_port_number() {
        return server_port_number;
    }
    public void setServer_port_number(String server_port_number) {
        this.server_port_number = server_port_number;
    }
    
    private String standby_server_ip_address;
    public String getStandby_server_ip_address() {
        return standby_server_ip_address;
    }
    public void setStandby_server_ip_address(String standby_server_ip_address) {
        this.standby_server_ip_address = standby_server_ip_address;
    }
    
    private String standby_server_port_number;
    public String getStandby_server_port_number() {
        return standby_server_port_number;
    }
    public void setStandby_server_port_number(String standby_server_port_number) {
        this.standby_server_port_number = standby_server_port_number;
    }
    
   
    ///////////////////////////////////////////////////////////
    private String seartbeat_time;
    public String getSeartbeat_time() {
        return seartbeat_time;
    }
    public void setSeartbeat_time(String seartbeat_time) {
        this.seartbeat_time = seartbeat_time;
    }
    ///////////////////////////////////////////////////////////
    
    private String voluntary_reporting;
    public String getVoluntary_reporting() {
        return voluntary_reporting;
    }
    public void setVoluntary_reporting(String voluntary_reporting) {
        this.voluntary_reporting = voluntary_reporting;
    } 
    
    private String device_restart;
    public String getDevice_restart() {
        return device_restart;
    }
    public void setDevice_restart(String device_restart) {
        this.device_restart = device_restart;
    }
    
    ////////////////////////////////////////////////////////////////////////
    private String restore_factory;
    public String getRestore_factory() {
        return restore_factory;
    }
    public void setRestore_factory(String restore_factory) {
        this.restore_factory = restore_factory;
    }
    
    private String make_formal_model;
    public String getMake_formal_model() {
        return make_formal_model;
    }
    public void setMake_formal_model(String make_formal_model) {
        this.make_formal_model = make_formal_model;
    }
    ////////////////////////////////////////////////////////////////////////
    
    private String make_formal_debugging;
    public String getMake_formal_debugging() {
        return make_formal_debugging;
    }
    public void setMake_formal_debugging(String make_formal_debugging) {
        this.make_formal_debugging = make_formal_debugging;
    }
    
    private String communication_mode_NB;
    public String getCommunication_mode_NB() {
        return communication_mode_NB;
    }
    public void setCommunication_mode_NB(String communication_mode_NB) {
        this.communication_mode_NB = communication_mode_NB;
    }    
    
    
    private String communication_mode_485;
    public String getCommunication_mode_485() {
        return communication_mode_485;
    }
    public void setCommunication_mode_485(String communication_mode_485) {
        this.communication_mode_485 = communication_mode_485;
    }
    
    private String alarm_sound;
    public String getAlarm_sound() {
        return alarm_sound;
    }
    public void setAlarm_sound(String alarm_sound) {
        this.alarm_sound = alarm_sound;
    }
    
    private String arc_warning_sensitivity;
    public String getArc_warning_sensitivity() {
        return arc_warning_sensitivity;
    }
    public void setArc_warning_sensitivity(String arc_warning_sensitivity) {
        this.arc_warning_sensitivity = arc_warning_sensitivity;
    }
    
    private String arc_time_range;
    public String getArc_time_range() {
        return arc_time_range;
    }
    public void setArc_time_range(String arc_time_range) {
        this.arc_time_range = arc_time_range;
    }
    
    ////////////////////////////////////////////////////////////////////////
    private String arc_number;
    public String getArc_number() {
        return arc_number;
    }
    public void setArc_number(String arc_number) {
        this.arc_number = arc_number;
    }

    private String temperature_warning_threshold;
    public String getTemperature_warning_threshold() {
        return temperature_warning_threshold;
    }
    public void setTemperature_warning_threshold(String temperature_warning_threshold) {
        this.temperature_warning_threshold = temperature_warning_threshold;
    }
    
    private String leakage_warning_threshold;
    public String getLeakage_warning_threshold() {
        return leakage_warning_threshold;
    }
    public void setLeakage_warning_threshold(String leakage_warning_threshold) {
        this.leakage_warning_threshold = leakage_warning_threshold;
    }

    private String buzzer_enabler;
    public String getBuzzer_enabler() {
        return buzzer_enabler;
    }
    public void setBuzzer_enabler(String buzzer_enabler) {
        this.buzzer_enabler = buzzer_enabler;
    }
    
    private String fire_warning_enabler;
    public String getFire_warning_enabler() {
        return fire_warning_enabler;
    }
    public void setFire_warning_enabler(String fire_warning_enabler) {
        this.fire_warning_enabler = fire_warning_enabler;
    }
    
    
    private String high_temperature_warning_enabler;
    public String getHigh_temperature_warning_enabler() {
        return high_temperature_warning_enabler;
    }
    public void setHigh_temperature_warning_enabler(String high_temperature_warning_enabler) {
        this.high_temperature_warning_enabler = high_temperature_warning_enabler;
    }
    
    private String leakage_alarm_enabler;
    public String getLeakage_alarm_enabler() {
        return leakage_alarm_enabler;
    }
    public void setLeakage_alarm_enabler(String leakage_alarm_enabler) {
        this.leakage_alarm_enabler = leakage_alarm_enabler;
    }
    
    private String arc_relay;
    public String getArc_relay() {
        return arc_relay;
    }
    public void setArc_relay(String arc_relay) {
        this.arc_relay = arc_relay;
    }
    
    private String overtemperature_relay;
    public String getOvertemperature_relay() {
        return overtemperature_relay;
    }
    public void setOvertemperature_relay(String overtemperature_relay) {
        this.overtemperature_relay = overtemperature_relay;
    }
    
    private String leak_replay;
    public String getLeak_replay() {
        return leak_replay;
    }
    public void setLeak_replay(String leak_replay) {
        this.leak_replay = leak_replay;
    }
    
    private String alarm_reset;
    public String getAlarm_reset() {
        return alarm_reset;
    }
    public void setAlarm_reset(String alarm_reset) {
        this.alarm_reset = alarm_reset;
    }
    
    private String alarm_silencing;
    public String getAlarm_silencing() {
        return alarm_silencing;
    }
    public void setAlarm_silencing(String alarm_silencing) {
        this.alarm_silencing = alarm_silencing;
    }
    
    private String fire_alarm;
    public String getFire_alarm() {
        return fire_alarm;
    }
    public void setFire_alarm(String fire_alarm) {
        this.fire_alarm = fire_alarm;
    }
    
    private String over_temperature_alarm;
    public String getOver_temperature_alarm() {
        return over_temperature_alarm;
    }
    public void setOver_temperature_alarm(String over_temperature_alarm) {
        this.over_temperature_alarm = over_temperature_alarm;
    }
    
    private String leakage_alarm;
    public String getLeakage_alarm() {
        return leakage_alarm;
    }
    public void setLeakage_alarm(String leakage_alarm) {
        this.leakage_alarm = leakage_alarm;
    }
    
    private String current_value;
    public String getCurrent_value() {
        return current_value;
    }
    public void setCurrent_value(String current_value) {
        this.current_value = current_value;
    }
    
    private String arcrelay;
    public String getArcrelay() {
        return arcrelay;
    }
    public void setArcrelay(String arcrelay) {
        this.arcrelay = arcrelay;
    }
    
    private String overtemprelay;
    public String getOvertemprelay() {
        return overtemprelay;
    }
    public void setOvertemprelay(String overtemprelay) {
        this.overtemprelay = overtemprelay;
    }
    
    private String leakagerelay;
    public String getLeakagerelay() {
        return leakagerelay;
    }
    public void setLeakagerelay(String leakagerelay) {
        this.leakagerelay = leakagerelay;
    }
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
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
    
   
    public String getEfm_id() {
		return efm_id;
	}

	public void setEfm_id(String efm_id) {
		this.efm_id = efm_id;
	}

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

	public String getLng_bmap() {
		return lng_bmap;
	}

	public void setLng_bmap(String lng_bmap) {
		this.lng_bmap = lng_bmap;
	}

	public String getLat_bmap() {
		return lat_bmap;
	}

	public void setLat_bmap(String lat_bmap) {
		this.lat_bmap = lat_bmap;
	}
	

    
    
}