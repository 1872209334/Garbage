package com.qf.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.ModelMap;

/**
 * 该类是包含一些公用的方法 author RuanYu
 */
public class ToolUtil {

	// 增删改是否成功
	public static boolean updateCount(Integer countName) {
		if (countName > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 中控报警类型
	public static Map<Integer, String> getwarnTypeMap() {
		Map<Integer, String> warnTypeMap = new HashMap<>();
		warnTypeMap.put(7, "power_cut_alarm");
		warnTypeMap.put(6, "no_battery_alarm");
		warnTypeMap.put(5, "low_voltage_alarm");
		warnTypeMap.put(4, "printer_offline_alarm");
		warnTypeMap.put(3, "printer_out_paper_alarm");
		warnTypeMap.put(2, "printer_communication_failure_alarm");
		warnTypeMap.put(1, "communication_offline_alarm");
		warnTypeMap.put(0, "device_offline_alarm");
		return warnTypeMap;
	}

	// 验证权限
	public void checkLimit(String url) {

	}

	// 16进制转字符串
	public static String hexStr2Str(String s) {

		if (s == null || s.equals("")) {

			s = "";
		} else {
			return DataParsingUtil.toStringHex(s);
		}
		return s;
	}

	// 字符串转16进制
	public static String str2HexStr(String s) {
		String ss = s;
	    byte[] bt = new byte[0];

	    try {
	        bt = ss.getBytes("GBK");
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	    String s1 = "";
	    for (int i = 0; i < bt.length; i++)
	    {
	        String tempStr = Integer.toHexString(bt[i]);
	        if (tempStr.length() > 2)
	            tempStr = tempStr.substring(tempStr.length() - 2);
	        s1 = s1 + tempStr + "";
	    }
	    System.out.println(s1.toUpperCase());
	    return s1.toUpperCase();
	}

	// 16进制转字符串
	public static String formatDevStr(String str) {
		if (str == null || str.equals("")) {

			str = "";
		} else {
			return DataParsingUtil.toStringHex(str);
		}
		return str;
	}

	// 非空判斷
	public static boolean StringNotBlank(String str) {
		if (str == null || str.equals("") || str.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	// Mqtt映射
	public static Map<String, String> mqttMapping() {
		Map<String, String> warnTypeMap = new HashMap<>();
		warnTypeMap.put("01", "equipment_manufacturer_code");
		warnTypeMap.put("02", "type");
		warnTypeMap.put("03", "equipment_type");
		warnTypeMap.put("04", "equipment_production_sequence_number");

		warnTypeMap.put("06", "lng_bmap");
		warnTypeMap.put("07", "lat_bmap");
		warnTypeMap.put("10", "softversion");
		warnTypeMap.put("11", "boardversion");
		warnTypeMap.put("12", "issue_time");

		warnTypeMap.put("13", "boot_version_number");
		warnTypeMap.put("14", "subtype");
		warnTypeMap.put("15", "version_information");
		warnTypeMap.put("20", "site_code");
		warnTypeMap.put("21", "device_code");

		warnTypeMap.put("22", "line_code");
		warnTypeMap.put("23", "addr");
		warnTypeMap.put("24", "message");
		warnTypeMap.put("25", "nb_category");
		warnTypeMap.put("26", "nb_chip_type");

		warnTypeMap.put("27", "nb_chip_serial_number");
		warnTypeMap.put("28", "nb_phone_number");
		warnTypeMap.put("29", "server_ip_address");
		warnTypeMap.put("2A", "server_port_number");
		warnTypeMap.put("2B", "standby_server_ip_address");

		warnTypeMap.put("2C", "standby_server_port_number");
		warnTypeMap.put("2D", "seartbeat_time");
		warnTypeMap.put("2E", "voluntary_reporting");
		warnTypeMap.put("30", "device_restart");
		warnTypeMap.put("31", "restore_factory");

		warnTypeMap.put("32", "make_formal_model");
		warnTypeMap.put("33", "make_formal_debugging");
		warnTypeMap.put("35", "alarm_sound");
		warnTypeMap.put("40", "arc_warning_sensitivity");
		warnTypeMap.put("41", "time");

		warnTypeMap.put("42", "cnt");
		warnTypeMap.put("43", "tempration");
		warnTypeMap.put("44", "leakageCurrentUpLimit");
		warnTypeMap.put("46", "AVoltageUpLimit");
		warnTypeMap.put("47", "BVoltageUpLimit");
		warnTypeMap.put("48", "CVoltageUpLimit");
		warnTypeMap.put("49", "AVoltageDownLimit");
		warnTypeMap.put("4A", "BVoltageDownLimit");
		warnTypeMap.put("4B", "CVoltageDownLimit");
		
		warnTypeMap.put("50", "ACurrentUpLimit");
		warnTypeMap.put("51", "BCurrentUpLimit");
		warnTypeMap.put("52", "CCurrentUpLimit");
		
	
		warnTypeMap.put("55", "tem_limit0");
		warnTypeMap.put("56", "tem_limit1");
		warnTypeMap.put("57", "tem_limit2");
		warnTypeMap.put("58", "tem_limit3");
		warnTypeMap.put("59", "tem_limit4");
		
		warnTypeMap.put("60", "buzzer_enable");
		warnTypeMap.put("61", "over_temperature_relay");
		warnTypeMap.put("62", "leakage_relay");
		warnTypeMap.put("66", "arc_relay");
		warnTypeMap.put("70", "alarm_reset");
		warnTypeMap.put("71", "alarm_silencing");
		warnTypeMap.put("90", "fire_alarm");
		warnTypeMap.put("91", "over_temperature_alarm");
		warnTypeMap.put("92", "current_warning");
		warnTypeMap.put("93", "voltage_warning");
		warnTypeMap.put("94", "temperature_warning");
		warnTypeMap.put("95", "other_warning");
		warnTypeMap.put("96", "over_voltage_warning");
		
		warnTypeMap.put("A0", "tem0");
		warnTypeMap.put("A1", "tem1");
		warnTypeMap.put("A2", "tem2");
		warnTypeMap.put("A3", "tem3");
		warnTypeMap.put("A4", "tem4");
		
		warnTypeMap.put("B0", "leakageCurrent");
		warnTypeMap.put("B1", "ACurrent");
		warnTypeMap.put("B2", "BCurrent");
		warnTypeMap.put("B3", "CCurrent");
		
		warnTypeMap.put("B8", "AVoltage");
		warnTypeMap.put("B9", "BVoltage");
		warnTypeMap.put("BA", "CVoltage");
		return warnTypeMap;
	}
   //udp映射
	public static Map<String, String> udpEquipMapping() {
		Map<String, String> warnTypeMap = new HashMap<>();
		warnTypeMap.put("0305", "current_warning");
		warnTypeMap.put("0306", "voltage_warning");
		warnTypeMap.put("0307", "temperature_warning");
		warnTypeMap.put("0308", "other_warning");
		
		
		return warnTypeMap;
	}
	
	// 16进制转2进制
	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		
		return bString;
	}
	 // 无线92标识报警类型
		public static Map<Integer, String> wirelessCurrentWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(4, "CphaseCurrentWarn");
			warnTypeMap.put(5, "BphaseCurrentWarn");
			warnTypeMap.put(6, "AphaseCurrentWarn");
			warnTypeMap.put(7, "leakageCurrentWarn");
			return warnTypeMap;
		}
		 // 无线93标识报警类型
		public static Map<Integer, String> wirelessVoltageWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(5, "CphaseVoltageWarn");
			warnTypeMap.put(6, "BphaseVoltageWarn");
			warnTypeMap.put(7, "AphaseVoltageWarn");
			
			return warnTypeMap;
		}
		 // 无线94标识报警类型
		public static Map<Integer, String> wirelessTemperatureWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			warnTypeMap.put(3, "4routeWarn");
			warnTypeMap.put(4, "3routeWarn");
			warnTypeMap.put(5, "2routeWarn");
			warnTypeMap.put(6, "1routeWarn");
			warnTypeMap.put(7, "0routeWarn");
			
			return warnTypeMap;
		}
		 // 无线95标识报警类型
		public static Map<Integer, String> wirelessOtherWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(6, "entranceControlWarn");
			warnTypeMap.put(7, "leakage_alarm");
			
			return warnTypeMap;
		}
		 // 无线96标识报警类型
		public static Map<Integer, String> wirelessOverVoltageWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(5, "CphaseOverVoltageWarn");
			warnTypeMap.put(6, "BphaseOverVoltageWarn");
			warnTypeMap.put(7, "AphaseOverVoltageWarn");
			
			return warnTypeMap;
		}
		 // 有限终端0305标识报警类型
		public static Map<Integer, String> equipCurrentWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(5, "udp_CphaseCurrentWarn");
			warnTypeMap.put(6, "udp_BphaseCurrentWarn");
			warnTypeMap.put(7, "udp_AphaseCurrentWarn");
			
			return warnTypeMap;
		}
		 // 有限终端0306标识报警类型
		public static Map<Integer, String> equipVoltageWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(5, "udp_CphaseVoltageWarn");
			warnTypeMap.put(6, "udp_BphaseVoltageWarn");
			warnTypeMap.put(7, "udp_AphaseVoltageWarn");
			
			return warnTypeMap;
		}
		 //有限终端0307标识报警类型
		public static Map<Integer, String> equipTemperatureWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			warnTypeMap.put(3, "udp_4routeWarn");
			warnTypeMap.put(4, "udp_3routeWarn");
			warnTypeMap.put(5, "udp_2routeWarn");
			warnTypeMap.put(6, "udp_1routeWarn");
			warnTypeMap.put(7, "udp_0routeWarn");
			
			return warnTypeMap;
		}
		 //有限终端0308标识报警类型
		public static Map<Integer, String> equipOtherWarn() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(6, "udp_entranceControlWarn");
			warnTypeMap.put(7, "udp_leakage_alarm");
			
			return warnTypeMap;
		}
		
		 // 有限终端0240使能
		public static Map<Integer, String> equipCurrentWarnEnable() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(5, "ccurrent_warning_enable");
			warnTypeMap.put(6, "bcurrent_warning_enable");
			warnTypeMap.put(7, "acurrent_warning_enable");
			
			return warnTypeMap;
		}
		 // 有限终端0241使能
		public static Map<Integer, String> equipVoltageWarnEnable() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(5, "cvoltage_warning_enable");
			warnTypeMap.put(6, "bvoltage_warning_enable");
			warnTypeMap.put(7, "avoltage_warning_enable");
			
			return warnTypeMap;
		}
		 //有限终端0242使能
		public static Map<Integer, String> equipTemperatureWarnEnable() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			warnTypeMap.put(3, "temperature_warning4_enable");
			warnTypeMap.put(4, "temperature_warning3_enable");
			warnTypeMap.put(5, "temperature_warning2_enable");
			warnTypeMap.put(6, "temperature_warning1_enable");
			warnTypeMap.put(7, "temperature_warning0_enable");
			
			return warnTypeMap;
		}
		 //有限终端0243使能
		public static Map<Integer, String> equipOtherWarnEnable() {
			Map<Integer, String> warnTypeMap = new HashMap<>();
			
			warnTypeMap.put(6, "entrance_warning_enable");
			warnTypeMap.put(7, "leakage_warning_enable");
			
			return warnTypeMap;
		}
		public static String str2HexStr2(String str) {
			char[] chars = "0123456789abcdef".toCharArray();
			StringBuilder sb = new StringBuilder("");
			byte[] bs = str.getBytes();
			int bit;
			for (int i = 0; i < bs.length; i++) {
				bit = (bs[i] & 0x0f0) >> 4;
				sb.append(chars[bit]);
				bit = bs[i] & 0x0f;
				sb.append(chars[bit]);

			}
			return sb.toString().trim();
		}
}
