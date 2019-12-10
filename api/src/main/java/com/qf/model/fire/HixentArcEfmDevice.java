package com.qf.model.fire;

import java.util.Date;

public class HixentArcEfmDevice {
    private String id;

    private String site_code="";

    private String device_code="";

    private String device_ip="";

    private String device_port="";

    private String manufacture_code="";

    private String category="";

    private String model="";

    private String serial_number="";

    private String longitute="";

    private String latitude="";

    private String version="";

    private String specificator="";

    private String message_phone="";

    private String phone_number1="";

    private String phone_number2="";

    private String phone_number3="";

    private String phone_number4="";
    
    private String phone_number5="";

    private String report_number;

    private String device_domain="";

    private String alternate_ip="";

    private String alternate_port="";

    private String device_place="";
    
 
	private String date_time="";

    private Integer device_total;

    private Byte device_bus1;

    private Byte device_bus2;

    private Byte device_bus3;

    private Byte device_bus4;

    private String state_bus1="";

    private String state_bus2="";

    private String state_bus3="";

    private String state_bus4="";

    private String device_info="";

    private Integer fire_num;

    private Integer fire_current;

    private String fire_info="";

    private Integer broken_num;

    private Integer broken_current;

    private String broken_info="";

    private Integer other_num;

    private Integer other_current;

    private String other_info="";

    private Boolean power_cut_enable;

    private Boolean no_battery_enable;

    private Boolean low_voltage_enable;

    private Boolean printer_offline_enable;

    private Boolean printer_out_paper_enable;

    private Boolean printer_communication_failure_enable;

    private Boolean communication_offline_enable;

    private Boolean device_offline_enable;

    private String bus1_offline_enable="";

    private String bus2_offline_enable="";

    private String bus3_offline_enable="";

    private String bus4_offline_enable="";

    private String bus1_fire_enable="";

    private String bus2_fire_enable="";

    private String bus3_fire_enable="";

    private String bus4_fire_enable="";

    private String bus1_temperature_enable="";

    private String bus2_temperature_enable="";

    private String bus3_temperature_enable="";

    private String bus4_temperature_enable="";

    private Boolean power_cut_alarm;

    private Boolean no_battery_alarm;

    private Boolean low_voltage_alarm;

    private Boolean printer_offline_alarm;

    private Boolean printer_out_paper_alarm;

    private Boolean printer_communication_failure_alarm;

    private Boolean communication_offline_alarm;

    private Boolean device_offline_alarm;

    private String outline_alarm="";

    private String bus1_offline_alarm="";

    private String bus2_offline_alarm="";

    private String bus3_offline_alarm="";

    private String bus4_offline_alarm="";

    private String fire_alarm="";

    private String bus1_fire_alarm="";

    private String bus2_fire_alarm="";

    private String bus3_fire_alarm="";

    private String bus4_fire_alarm="";

    private String temperature_alarm="";

    private String bus1_temperature_alarm="";

    private String bus2_temperature_alarm="";

    private String bus3_temperature_alarm="";

    private String bus4_temperature_alarm="";

    private Boolean fire_autoprint;

    private Boolean broken_autoprint;

    private Boolean other_autoprint;

    private Byte clear_alarm;

    private Integer clear_device_noise;

    private Byte clear_broadcast_noise;

    private Integer device_mute;

    private Byte broadcast_mute;

    private Integer reset_alarm;

    private Byte reset_broadcast_alarm;

    private String niName="";
    
    private Integer is_online;
    
    private String buildName="";
    
    private Integer equipCount;
    
    private Integer equipOffLineCount;
    
    private Integer equipAlarmCount;
    
    private Integer equipFaultCount;
    
    private String siteName;
    
    private Integer offLineTime;//设置离线时间
    
    private Integer offlineEnable;//离线使能

    private String tel;//预留电话

    private Date publish_date;//预留电话

    public Date getPublish_date() {
        return publish_date;
    }

    public void setTel(Date publish_date) {
        this.publish_date = publish_date;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getEquipCount() {
		return equipCount;
	}

	public void setEquipCount(Integer equipCount) {
		this.equipCount = equipCount;
	}

	public Integer getEquipOffLineCount() {
		return equipOffLineCount;
	}

	public void setEquipOffLineCount(Integer equipOffLineCount) {
		this.equipOffLineCount = equipOffLineCount;
	}

	public Integer getEquipAlarmCount() {
		return equipAlarmCount;
	}

	public void setEquipAlarmCount(Integer equipAlarmCount) {
		this.equipAlarmCount = equipAlarmCount;
	}

	public Integer getEquipFaultCount() {
		return equipFaultCount;
	}

	public void setEquipFaultCount(Integer equipFaultCount) {
		this.equipFaultCount = equipFaultCount;
	}

	public String getNiName() {
		return niName;
	}

	public void setNiName(String niName) {
		this.niName = niName;
	}

	public Integer getIs_online() {
		return is_online;
	}

	public void setIs_online(Integer is_online) {
		this.is_online = is_online;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code == null ? null : site_code.trim();
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code == null ? null : device_code.trim();
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip == null ? null : device_ip.trim();
    }

    public String getDevice_port() {
        return device_port;
    }

    public void setDevice_port(String device_port) {
        this.device_port = device_port == null ? null : device_port.trim();
    }

    public String getManufacture_code() {
        return manufacture_code;
    }

    public void setManufacture_code(String manufacture_code) {
        this.manufacture_code = manufacture_code == null ? null : manufacture_code.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number == null ? null : serial_number.trim();
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute == null ? null : longitute.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getSpecificator() {
        return specificator;
    }

    public void setSpecificator(String specificator) {
        this.specificator = specificator == null ? null : specificator.trim();
    }

    public String getMessage_phone() {
        return message_phone;
    }

    public void setMessage_phone(String message_phone) {
        this.message_phone = message_phone == null ? null : message_phone.trim();
    }

    public String getPhone_number1() {
        return phone_number1;
    }

    public void setPhone_number1(String phone_number1) {
        this.phone_number1 = phone_number1 == null ? null : phone_number1.trim();
    }

    public String getPhone_number2() {
        return phone_number2;
    }

    public void setPhone_number2(String phone_number2) {
        this.phone_number2 = phone_number2 == null ? null : phone_number2.trim();
    }

    public String getPhone_number3() {
        return phone_number3;
    }

    public void setPhone_number3(String phone_number3) {
        this.phone_number3 = phone_number3 == null ? null : phone_number3.trim();
    }

    public String getPhone_number4() {
        return phone_number4;
    }

    public void setPhone_number4(String phone_number4) {
        this.phone_number4 = phone_number4 == null ? null : phone_number4.trim();
    }
    
    public String getPhone_number5() {
        return phone_number5;
    }

    public void setPhone_number5(String phone_number5) {
        this.phone_number5 = phone_number5 == null ? null : phone_number5.trim();
    }

    public String getReport_number() {
        return report_number;
    }

    public void setReport_number(String report_number) {
        this.report_number = report_number == null ? null : report_number.trim();
    }

    public String getDevice_domain() {
        return device_domain;
    }

    public void setDevice_domain(String device_domain) {
        this.device_domain = device_domain == null ? null : device_domain.trim();
    }

    public String getAlternate_ip() {
        return alternate_ip;
    }

    public void setAlternate_ip(String alternate_ip) {
        this.alternate_ip = alternate_ip == null ? null : alternate_ip.trim();
    }

    public String getAlternate_port() {
        return alternate_port;
    }

    public void setAlternate_port(String alternate_port) {
        this.alternate_port = alternate_port == null ? null : alternate_port.trim();
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time == null ? null : date_time.trim();
    }

    public Integer getDevice_total() {
        return device_total;
    }

    public void setDevice_total(Integer device_total) {
        this.device_total = device_total;
    }

    public Byte getDevice_bus1() {
        return device_bus1;
    }

    public void setDevice_bus1(Byte device_bus1) {
        this.device_bus1 = device_bus1;
    }

    public Byte getDevice_bus2() {
        return device_bus2;
    }

    public void setDevice_bus2(Byte device_bus2) {
        this.device_bus2 = device_bus2;
    }

    public Byte getDevice_bus3() {
        return device_bus3;
    }

    public void setDevice_bus3(Byte device_bus3) {
        this.device_bus3 = device_bus3;
    }

    public Byte getDevice_bus4() {
        return device_bus4;
    }

    public void setDevice_bus4(Byte device_bus4) {
        this.device_bus4 = device_bus4;
    }

    public String getState_bus1() {
        return state_bus1;
    }

    public void setState_bus1(String state_bus1) {
        this.state_bus1 = state_bus1 == null ? null : state_bus1.trim();
    }

    public String getState_bus2() {
        return state_bus2;
    }

    public void setState_bus2(String state_bus2) {
        this.state_bus2 = state_bus2 == null ? null : state_bus2.trim();
    }

    public String getState_bus3() {
        return state_bus3;
    }

    public void setState_bus3(String state_bus3) {
        this.state_bus3 = state_bus3 == null ? null : state_bus3.trim();
    }

    public String getState_bus4() {
        return state_bus4;
    }

    public void setState_bus4(String state_bus4) {
        this.state_bus4 = state_bus4 == null ? null : state_bus4.trim();
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info == null ? null : device_info.trim();
    }

    public Integer getFire_num() {
        return fire_num;
    }

    public void setFire_num(Integer fire_num) {
        this.fire_num = fire_num;
    }

    public Integer getFire_current() {
        return fire_current;
    }

    public void setFire_current(Integer fire_current) {
        this.fire_current = fire_current;
    }

    public String getFire_info() {
        return fire_info;
    }

    public void setFire_info(String fire_info) {
        this.fire_info = fire_info == null ? null : fire_info.trim();
    }

    public Integer getBroken_num() {
        return broken_num;
    }

    public void setBroken_num(Integer broken_num) {
        this.broken_num = broken_num;
    }

    public Integer getBroken_current() {
        return broken_current;
    }

    public void setBroken_current(Integer broken_current) {
        this.broken_current = broken_current;
    }

    public String getBroken_info() {
        return broken_info;
    }

    public void setBroken_info(String broken_info) {
        this.broken_info = broken_info == null ? null : broken_info.trim();
    }

    public Integer getOther_num() {
        return other_num;
    }

    public void setOther_num(Integer other_num) {
        this.other_num = other_num;
    }

    public Integer getOther_current() {
        return other_current;
    }

    public void setOther_current(Integer other_current) {
        this.other_current = other_current;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info == null ? null : other_info.trim();
    }

    public Boolean getPower_cut_enable() {
        return power_cut_enable;
    }

    public void setPower_cut_enable(Boolean power_cut_enable) {
        this.power_cut_enable = power_cut_enable;
    }

    public Boolean getNo_battery_enable() {
        return no_battery_enable;
    }

    public void setNo_battery_enable(Boolean no_battery_enable) {
        this.no_battery_enable = no_battery_enable;
    }

    public Boolean getLow_voltage_enable() {
        return low_voltage_enable;
    }

    public void setLow_voltage_enable(Boolean low_voltage_enable) {
        this.low_voltage_enable = low_voltage_enable;
    }

    public Boolean getPrinter_offline_enable() {
        return printer_offline_enable;
    }

    public void setPrinter_offline_enable(Boolean printer_offline_enable) {
        this.printer_offline_enable = printer_offline_enable;
    }

    public Boolean getPrinter_out_paper_enable() {
        return printer_out_paper_enable;
    }

    public void setPrinter_out_paper_enable(Boolean printer_out_paper_enable) {
        this.printer_out_paper_enable = printer_out_paper_enable;
    }

    public Boolean getPrinter_communication_failure_enable() {
        return printer_communication_failure_enable;
    }

    public void setPrinter_communication_failure_enable(Boolean printer_communication_failure_enable) {
        this.printer_communication_failure_enable = printer_communication_failure_enable;
    }

    public Boolean getCommunication_offline_enable() {
        return communication_offline_enable;
    }

    public void setCommunication_offline_enable(Boolean communication_offline_enable) {
        this.communication_offline_enable = communication_offline_enable;
    }

    public Boolean getDevice_offline_enable() {
        return device_offline_enable;
    }

    public void setDevice_offline_enable(Boolean device_offline_enable) {
        this.device_offline_enable = device_offline_enable;
    }

    public String getBus1_offline_enable() {
        return bus1_offline_enable;
    }

    public void setBus1_offline_enable(String bus1_offline_enable) {
        this.bus1_offline_enable = bus1_offline_enable == null ? null : bus1_offline_enable.trim();
    }

    public String getBus2_offline_enable() {
        return bus2_offline_enable;
    }

    public void setBus2_offline_enable(String bus2_offline_enable) {
        this.bus2_offline_enable = bus2_offline_enable == null ? null : bus2_offline_enable.trim();
    }

    public String getBus3_offline_enable() {
        return bus3_offline_enable;
    }

    public void setBus3_offline_enable(String bus3_offline_enable) {
        this.bus3_offline_enable = bus3_offline_enable == null ? null : bus3_offline_enable.trim();
    }

    public String getBus4_offline_enable() {
        return bus4_offline_enable;
    }

    public void setBus4_offline_enable(String bus4_offline_enable) {
        this.bus4_offline_enable = bus4_offline_enable == null ? null : bus4_offline_enable.trim();
    }

    public String getBus1_fire_enable() {
        return bus1_fire_enable;
    }

    public void setBus1_fire_enable(String bus1_fire_enable) {
        this.bus1_fire_enable = bus1_fire_enable == null ? null : bus1_fire_enable.trim();
    }

    public String getBus2_fire_enable() {
        return bus2_fire_enable;
    }

    public void setBus2_fire_enable(String bus2_fire_enable) {
        this.bus2_fire_enable = bus2_fire_enable == null ? null : bus2_fire_enable.trim();
    }

    public String getBus3_fire_enable() {
        return bus3_fire_enable;
    }

    public void setBus3_fire_enable(String bus3_fire_enable) {
        this.bus3_fire_enable = bus3_fire_enable == null ? null : bus3_fire_enable.trim();
    }

    public String getBus4_fire_enable() {
        return bus4_fire_enable;
    }

    public void setBus4_fire_enable(String bus4_fire_enable) {
        this.bus4_fire_enable = bus4_fire_enable == null ? null : bus4_fire_enable.trim();
    }

    public String getBus1_temperature_enable() {
        return bus1_temperature_enable;
    }

    public void setBus1_temperature_enable(String bus1_temperature_enable) {
        this.bus1_temperature_enable = bus1_temperature_enable == null ? null : bus1_temperature_enable.trim();
    }

    public String getBus2_temperature_enable() {
        return bus2_temperature_enable;
    }

    public void setBus2_temperature_enable(String bus2_temperature_enable) {
        this.bus2_temperature_enable = bus2_temperature_enable == null ? null : bus2_temperature_enable.trim();
    }

    public String getBus3_temperature_enable() {
        return bus3_temperature_enable;
    }

    public void setBus3_temperature_enable(String bus3_temperature_enable) {
        this.bus3_temperature_enable = bus3_temperature_enable == null ? null : bus3_temperature_enable.trim();
    }

    public String getBus4_temperature_enable() {
        return bus4_temperature_enable;
    }

    public void setBus4_temperature_enable(String bus4_temperature_enable) {
        this.bus4_temperature_enable = bus4_temperature_enable == null ? null : bus4_temperature_enable.trim();
    }

    public Boolean getPower_cut_alarm() {
        return power_cut_alarm;
    }

    public void setPower_cut_alarm(Boolean power_cut_alarm) {
        this.power_cut_alarm = power_cut_alarm;
    }

    public Boolean getNo_battery_alarm() {
        return no_battery_alarm;
    }

    public void setNo_battery_alarm(Boolean no_battery_alarm) {
        this.no_battery_alarm = no_battery_alarm;
    }

    public Boolean getLow_voltage_alarm() {
        return low_voltage_alarm;
    }

    public void setLow_voltage_alarm(Boolean low_voltage_alarm) {
        this.low_voltage_alarm = low_voltage_alarm;
    }

    public Boolean getPrinter_offline_alarm() {
        return printer_offline_alarm;
    }

    public void setPrinter_offline_alarm(Boolean printer_offline_alarm) {
        this.printer_offline_alarm = printer_offline_alarm;
    }

    public Boolean getPrinter_out_paper_alarm() {
        return printer_out_paper_alarm;
    }

    public void setPrinter_out_paper_alarm(Boolean printer_out_paper_alarm) {
        this.printer_out_paper_alarm = printer_out_paper_alarm;
    }

    public Boolean getPrinter_communication_failure_alarm() {
        return printer_communication_failure_alarm;
    }

    public void setPrinter_communication_failure_alarm(Boolean printer_communication_failure_alarm) {
        this.printer_communication_failure_alarm = printer_communication_failure_alarm;
    }

    public Boolean getCommunication_offline_alarm() {
        return communication_offline_alarm;
    }

    public void setCommunication_offline_alarm(Boolean communication_offline_alarm) {
        this.communication_offline_alarm = communication_offline_alarm;
    }

    public Boolean getDevice_offline_alarm() {
        return device_offline_alarm;
    }

    public void setDevice_offline_alarm(Boolean device_offline_alarm) {
        this.device_offline_alarm = device_offline_alarm;
    }

    public String getOutline_alarm() {
        return outline_alarm;
    }

    public void setOutline_alarm(String outline_alarm) {
        this.outline_alarm = outline_alarm == null ? null : outline_alarm.trim();
    }

    public String getBus1_offline_alarm() {
        return bus1_offline_alarm;
    }

    public void setBus1_offline_alarm(String bus1_offline_alarm) {
        this.bus1_offline_alarm = bus1_offline_alarm == null ? null : bus1_offline_alarm.trim();
    }

    public String getBus2_offline_alarm() {
        return bus2_offline_alarm;
    }

    public void setBus2_offline_alarm(String bus2_offline_alarm) {
        this.bus2_offline_alarm = bus2_offline_alarm == null ? null : bus2_offline_alarm.trim();
    }

    public String getBus3_offline_alarm() {
        return bus3_offline_alarm;
    }

    public void setBus3_offline_alarm(String bus3_offline_alarm) {
        this.bus3_offline_alarm = bus3_offline_alarm == null ? null : bus3_offline_alarm.trim();
    }

    public String getBus4_offline_alarm() {
        return bus4_offline_alarm;
    }

    public void setBus4_offline_alarm(String bus4_offline_alarm) {
        this.bus4_offline_alarm = bus4_offline_alarm == null ? null : bus4_offline_alarm.trim();
    }

    public String getFire_alarm() {
        return fire_alarm;
    }

    public void setFire_alarm(String fire_alarm) {
        this.fire_alarm = fire_alarm == null ? null : fire_alarm.trim();
    }

    public String getBus1_fire_alarm() {
        return bus1_fire_alarm;
    }

    public void setBus1_fire_alarm(String bus1_fire_alarm) {
        this.bus1_fire_alarm = bus1_fire_alarm == null ? null : bus1_fire_alarm.trim();
    }

    public String getBus2_fire_alarm() {
        return bus2_fire_alarm;
    }

    public void setBus2_fire_alarm(String bus2_fire_alarm) {
        this.bus2_fire_alarm = bus2_fire_alarm == null ? null : bus2_fire_alarm.trim();
    }

    public String getBus3_fire_alarm() {
        return bus3_fire_alarm;
    }

    public void setBus3_fire_alarm(String bus3_fire_alarm) {
        this.bus3_fire_alarm = bus3_fire_alarm == null ? null : bus3_fire_alarm.trim();
    }

    public String getBus4_fire_alarm() {
        return bus4_fire_alarm;
    }

    public void setBus4_fire_alarm(String bus4_fire_alarm) {
        this.bus4_fire_alarm = bus4_fire_alarm == null ? null : bus4_fire_alarm.trim();
    }

    public String getTemperature_alarm() {
        return temperature_alarm;
    }

    public void setTemperature_alarm(String temperature_alarm) {
        this.temperature_alarm = temperature_alarm == null ? null : temperature_alarm.trim();
    }

    public String getBus1_temperature_alarm() {
        return bus1_temperature_alarm;
    }

    public void setBus1_temperature_alarm(String bus1_temperature_alarm) {
        this.bus1_temperature_alarm = bus1_temperature_alarm == null ? null : bus1_temperature_alarm.trim();
    }

    public String getBus2_temperature_alarm() {
        return bus2_temperature_alarm;
    }

    public void setBus2_temperature_alarm(String bus2_temperature_alarm) {
        this.bus2_temperature_alarm = bus2_temperature_alarm == null ? null : bus2_temperature_alarm.trim();
    }

    public String getBus3_temperature_alarm() {
        return bus3_temperature_alarm;
    }

    public void setBus3_temperature_alarm(String bus3_temperature_alarm) {
        this.bus3_temperature_alarm = bus3_temperature_alarm == null ? null : bus3_temperature_alarm.trim();
    }

    public String getBus4_temperature_alarm() {
        return bus4_temperature_alarm;
    }

    public void setBus4_temperature_alarm(String bus4_temperature_alarm) {
        this.bus4_temperature_alarm = bus4_temperature_alarm == null ? null : bus4_temperature_alarm.trim();
    }

    public Boolean getFire_autoprint() {
        return fire_autoprint;
    }

    public void setFire_autoprint(Boolean fire_autoprint) {
        this.fire_autoprint = fire_autoprint;
    }

    public Boolean getBroken_autoprint() {
        return broken_autoprint;
    }

    public void setBroken_autoprint(Boolean broken_autoprint) {
        this.broken_autoprint = broken_autoprint;
    }

    public Boolean getOther_autoprint() {
        return other_autoprint;
    }

    public void setOther_autoprint(Boolean other_autoprint) {
        this.other_autoprint = other_autoprint;
    }

    public Byte getClear_alarm() {
        return clear_alarm;
    }

    public void setClear_alarm(Byte clear_alarm) {
        this.clear_alarm = clear_alarm;
    }

    public Integer getClear_device_noise() {
        return clear_device_noise;
    }

    public void setClear_device_noise(Integer clear_device_noise) {
        this.clear_device_noise = clear_device_noise;
    }

    public Byte getClear_broadcast_noise() {
        return clear_broadcast_noise;
    }

    public void setClear_broadcast_noise(Byte clear_broadcast_noise) {
        this.clear_broadcast_noise = clear_broadcast_noise;
    }

    public Integer getDevice_mute() {
        return device_mute;
    }

    public void setDevice_mute(Integer device_mute) {
        this.device_mute = device_mute;
    }

    public Byte getBroadcast_mute() {
        return broadcast_mute;
    }

    public void setBroadcast_mute(Byte broadcast_mute) {
        this.broadcast_mute = broadcast_mute;
    }

    public Integer getReset_alarm() {
        return reset_alarm;
    }

    public void setReset_alarm(Integer reset_alarm) {
        this.reset_alarm = reset_alarm;
    }

    public Byte getReset_broadcast_alarm() {
        return reset_broadcast_alarm;
    }

    public void setReset_broadcast_alarm(Byte reset_broadcast_alarm) {
        this.reset_broadcast_alarm = reset_broadcast_alarm;
    }
    public String getDevice_place() {
 		return device_place;
 	}

 	public void setDevice_place(String device_place) {
 		this.device_place = device_place;
 	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getOffLineTime() {
		return offLineTime;
	}

	public void setOffLineTime(Integer offLineTime) {
		this.offLineTime = offLineTime;
	}

	public Integer getOfflineEnable() {
		return offlineEnable;
	}

	public void setOfflineEnable(Integer offlineEnable) {
		this.offlineEnable = offlineEnable;
	}

	

}