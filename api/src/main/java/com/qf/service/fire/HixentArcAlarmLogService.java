package com.qf.service.fire;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.HixentArcWarningListMapper;
import com.qf.model.fire.HixentArcAlarmDealFeedBack;
import com.qf.model.fire.HixentArcEfmBeat;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.util.DateUtil;
import com.qf.util.ToolUtil;

/**
 * 报警日志 author RuanYu
 */
@Service
public class HixentArcAlarmLogService {

	@Autowired
	private HixentArcWarningListMapper hixentArcWarningListMapper;

	// 报警日志 中控
	public JSONObject alarmLog(Integer siteId, String deviceId, Integer page, Integer limit, Integer provinceId,
			Integer areaId, String[] siteCordArr, Integer warnIndex) {
		String limits = " " + Integer.toString((page - 1) * limit) + "," + Integer.toString(limit) + " ";
		JSONObject outJson = new JSONObject();
		Integer alarmLogForDeviceCount = hixentArcWarningListMapper.alarmLogForDeviceCount(siteId, deviceId, provinceId,
				areaId, siteCordArr, warnIndex);
		List<HixentArcWarningList> alarmLogForDevice = hixentArcWarningListMapper.alarmLogForDevice(siteId, deviceId,
				limits, provinceId, areaId, siteCordArr, warnIndex);
		for (int i = 0; i < alarmLogForDevice.size(); i++) {
			alarmLogForDevice.get(i).setSpecificator(ToolUtil.formatDevStr(alarmLogForDevice.get(i).getSpecificator()));
		}

		outJson.put("alarmLogForDeviceCount", alarmLogForDeviceCount);
		outJson.put("alarmLogForDevice", alarmLogForDevice);
		return outJson;
	}

	// 报警日志 终端
	public JSONObject alarmLogForEquip(Integer siteId, String deviceId, Integer page, Integer limit, Integer provinceId,
			Integer areaId, String[] siteCordArr, Integer type, String inquire, Integer warnIndex) {
		String limits = " " + Integer.toString((page - 1) * limit) + "," + Integer.toString(limit) + " ";
		JSONObject outJson = new JSONObject();
		deviceId=deviceId.trim();
		Integer alarmLogForEquipCount = hixentArcWarningListMapper.alarmLogForEquipCount(siteId, deviceId, provinceId,
				areaId, siteCordArr, type, inquire, warnIndex);
		List<HixentArcWarningList> alarmLogForEquip = hixentArcWarningListMapper.alarmLogForEquip(siteId, deviceId,
				limits, provinceId, areaId, siteCordArr, type, inquire, warnIndex);

		for (int i = 0; i < alarmLogForEquip.size(); i++) {
			alarmLogForEquip.get(i).setSpecificator(ToolUtil.formatDevStr(alarmLogForEquip.get(i).getSpecificator()));
		}

		outJson.put("alarmLogForDeviceCount", alarmLogForEquipCount);
		outJson.put("alarmLogForDevice", alarmLogForEquip);
		return outJson;
	}

	// 报警历史 中控
	public JSONObject alarmHistory(Integer page, Integer limit, Integer provinceId, Integer areaId,
			String[] siteCordArr, Long startTime, Long endTime) {
		String limits = " " + Integer.toString((page - 1) * limit) + "," + Integer.toString(limit) + " ";
		JSONObject outJson = new JSONObject();
		Integer alarmHistoryForDeviceCount = hixentArcWarningListMapper.alarmHistoryForDeviceCount(provinceId, areaId,
				siteCordArr, startTime, endTime);
		List<HixentArcWarningList> alarmHistoryForDevice = hixentArcWarningListMapper.alarmHistoryForDevice(limits,
				provinceId, areaId, siteCordArr, startTime, endTime);
		for (int i = 0; i < alarmHistoryForDevice.size(); i++) {
			alarmHistoryForDevice.get(i)
					.setSpecificator(ToolUtil.formatDevStr(alarmHistoryForDevice.get(i).getSpecificator()));
		}

		outJson.put("alarmLogForDeviceCount", alarmHistoryForDeviceCount);
		outJson.put("alarmLogForDevice", alarmHistoryForDevice);
		return outJson;
	}

	// 报警历史 终端
	public JSONObject alarmHistoryForEquip(Integer page, Integer limit, Integer provinceId, Integer areaId,
			String[] siteCordArr, Integer type, String inquire, Long startTime, Long endTime) {
		String limits = " " + Integer.toString((page - 1) * limit) + "," + Integer.toString(limit) + " ";
		JSONObject outJson = new JSONObject();
		Integer alarmHistoryForEquipCount = hixentArcWarningListMapper.alarmHistoryForEquipCount(provinceId, areaId,
				siteCordArr, type, inquire, startTime, endTime);
		List<HixentArcWarningList> alarmHistoryForEquip = hixentArcWarningListMapper.alarmHistoryForEquip(limits,
				provinceId, areaId, siteCordArr, type, inquire, startTime, endTime);
		for (int i = 0; i < alarmHistoryForEquip.size(); i++) {
			alarmHistoryForEquip.get(i)
					.setSpecificator(ToolUtil.formatDevStr(alarmHistoryForEquip.get(i).getSpecificator()));
		}

		outJson.put("alarmLogForDeviceCount", alarmHistoryForEquipCount);
		outJson.put("alarmLogForDevice", alarmHistoryForEquip);
		return outJson;
	}

	// 处理反馈列表
	public JSONObject alarmDealFeedBack(Integer page, Integer limit, Integer provinceId, Integer areaId,
			String[] siteCordArr, Integer type, String inquire, Long startTime, Long endTime) {
		String limits = " " + Integer.toString((page - 1) * limit) + "," + Integer.toString(limit) + " ";
		JSONObject outJson = new JSONObject();
		Integer alarmDealFeedBackCount = hixentArcWarningListMapper.alarmDealFeedBackCount(provinceId, areaId,
				siteCordArr, type, inquire, startTime, endTime);
		List<HixentArcAlarmDealFeedBack> alarmDealFeedBack = hixentArcWarningListMapper.alarmDealFeedBack(limits,
				provinceId, areaId, siteCordArr, type, inquire, startTime, endTime);
		outJson.put("alarmDealFeedBackCount", alarmDealFeedBackCount);
		outJson.put("alarmDealFeedBack", alarmDealFeedBack);

		return outJson;
	}

	// 审核处理反馈
	public boolean auditDealFeedBack(Integer id) {

		Integer updateDealFeedBack = hixentArcWarningListMapper.updateDealFeedBack(id);
		Integer updateWarnlist = hixentArcWarningListMapper.updateWarnlist(id);

		if (updateWarnlist > 0 && updateDealFeedBack > 0) {
			return true;
		} else {
			return false;
		}
	}

	// 查询心跳表，并插入离线数据
	public void getAllOffLineTables() throws ParseException {

		List<HixentArcEfmBeat> beatList = new ArrayList<HixentArcEfmBeat>();

		List<String> dataList = hixentArcWarningListMapper.dataList();
		for (int i = 0; i < dataList.size(); i++) {
			String tableName = "hixent_arc_system." + dataList.get(i);
			// 获取设备心跳表的时间和设备ID
			HixentArcEfmBeat lastData = hixentArcWarningListMapper.lastData(tableName);
			// 获取时间并转成时间戳
			String currentDate = DateUtil.getCurrentTime();
			long nowtimestamp = DateUtil.getTimestamp(currentDate);
			int nowtimestampInt = (int) nowtimestamp;
			Integer lastlogTime = lastData.getLogTime();
			// 判断心跳包时间是否大于2小时
			if (nowtimestamp - lastlogTime > 7200) {
				// 超过离线时间
				// 报警表插入离线数据
				Integer offLineDevice = hixentArcWarningListMapper.offLineDevice(lastData.getEfmId(), nowtimestamp);
			}
		}
	}

	// 报警历史 中控
	public List<HixentArcWarningList> alarmHistoryExcel(Integer provinceId, Integer areaId, String[] siteCordArr,
			Long startTime, Long endTime) {
		String limits = null;
		JSONObject outJson = new JSONObject();

		List<HixentArcWarningList> alarmHistoryForDevice = hixentArcWarningListMapper.alarmHistoryForDevice(limits,
				provinceId, areaId, siteCordArr, startTime, endTime);

		return alarmHistoryForDevice;
	}

	// 报警历史 终端
	public List<HixentArcWarningList> alarmHistoryForEquipExcel(Integer provinceId, Integer areaId,
			String[] siteCordArr, Integer type, String inquire, Long startTime, Long endTime) {
		String limits = null;
		JSONObject outJson = new JSONObject();

		List<HixentArcWarningList> alarmHistoryForEquip = hixentArcWarningListMapper.alarmHistoryForEquip(limits,
				provinceId, areaId, siteCordArr, type, inquire, startTime, endTime);

		return alarmHistoryForEquip;
	}

	// 派单
	public boolean dispach(Integer warnId, Integer adminId, Integer appUserId) throws Exception {
		// 获取时间并转成时间戳
		String currentDate = DateUtil.getCurrentTime();
		long nowtimestamp = DateUtil.getTimestamp(currentDate);

		Integer updateWarnTableForDispach = hixentArcWarningListMapper.updateWarnTableForDispach(warnId);
		Integer addDispach = hixentArcWarningListMapper.addDispach(warnId, adminId, appUserId, nowtimestamp);

		if (updateWarnTableForDispach > 0 && addDispach > 0) {
			return true;
		}
		return false;
	}

	// 删除报警
	public boolean delWarn(Integer warnId) {

		Integer delWarnByWarnId = hixentArcWarningListMapper.delWarnByWarnId(warnId);
		hixentArcWarningListMapper.delFeedbackByWarnId(warnId);
		if (delWarnByWarnId > 0) {
			return true;
		}
		return false;
	}

	// 报警日志Excel 中控
	public JSONObject alarmLogExcel(Integer siteId, String deviceId, Integer page, Integer limit, Integer provinceId,
			Integer areaId, String[] siteCordArr, Integer warnIndex) {
		String limits = "";
		JSONObject outJson = new JSONObject();
		Integer alarmLogForDeviceCount = hixentArcWarningListMapper.alarmLogForDeviceCount(siteId, deviceId, provinceId,
				areaId, siteCordArr, warnIndex);
		List<HixentArcWarningList> alarmLogForDevice = hixentArcWarningListMapper.alarmLogForDevice(siteId, deviceId,
				limits, provinceId, areaId, siteCordArr, warnIndex);
		outJson.put("alarmLogForDeviceCount", alarmLogForDeviceCount);
		outJson.put("alarmLogForDevice", alarmLogForDevice);
		return outJson;
	}

	// 报警日志Excel 终端
	public JSONObject alarmLogForEquipExcel(Integer siteId, String deviceId, Integer page, Integer limit,
			Integer provinceId, Integer areaId, String[] siteCordArr, Integer type, String inquire, Integer warnIndex) {
		String limits = "";
		JSONObject outJson = new JSONObject();
		Integer alarmLogForEquipCount = hixentArcWarningListMapper.alarmLogForEquipCount(siteId, deviceId, provinceId,
				areaId, siteCordArr, type, inquire, warnIndex);
		List<HixentArcWarningList> alarmLogForEquip = hixentArcWarningListMapper.alarmLogForEquip(siteId, deviceId,
				limits, provinceId, areaId, siteCordArr, type, inquire, warnIndex);
		outJson.put("alarmLogForDeviceCount", alarmLogForEquipCount);
		outJson.put("alarmLogForDevice", alarmLogForEquip);
		return outJson;
	}

	// 查询报警信息
	public HixentArcAlarmDealFeedBack getWarnInfo(Integer warnId) {
		HixentArcAlarmDealFeedBack warnInfo = hixentArcWarningListMapper.getWarnInfo(warnId);

		return warnInfo;
	}

	// 处理历史，不区分中控和终端
	public JSONObject alarmHistoryForAll(Integer page, Integer limit, Integer provinceId, Integer areaId,
			String[] siteCordArr, Long startTime, Long endTime) {
		String limits="";
		if(page!=0 && limit!=0) {
			limits = " " + Integer.toString((page - 1) * limit) + "," + Integer.toString(limit) + " ";
		}
		
		JSONObject outJson = new JSONObject();
		List<HixentArcWarningList> alarmHistoryForAll = hixentArcWarningListMapper.alarmHistoryForAll(limits,
				provinceId, areaId, siteCordArr, startTime, endTime);
		Integer alarmHistoryForAllCount = hixentArcWarningListMapper.alarmHistoryForAllCount(provinceId, areaId,
				siteCordArr, startTime, endTime);

		for (int i = 0; i < alarmHistoryForAll.size(); i++) {
			alarmHistoryForAll.get(i)
					.setSpecificator(ToolUtil.formatDevStr(alarmHistoryForAll.get(i).getSpecificator()));
		}
		outJson.put("total", alarmHistoryForAllCount);
		outJson.put("alarmHistoryForAll", alarmHistoryForAll);
		return outJson;
	}
	//设备复位
	public Integer deviceReset(String deviceId) throws ParseException {
		// 获取时间并转成时间戳
				String currentDate = DateUtil.getCurrentTime();
				long nowtimestamp = DateUtil.getTimestamp(currentDate);

		return	hixentArcWarningListMapper.devicesReset(deviceId,nowtimestamp);
	}
	//某类型警报数
	public Integer getWarnCountByWarnIndex(Integer warnIndex,Integer provinceId,Integer areaId,
			 String[] siteCordArr) {
			Integer warnCountByWarnIndex = hixentArcWarningListMapper.getWarnCountByWarnIndex(warnIndex,provinceId,areaId,siteCordArr);
			Integer warnCountByWarnIndexWireless = hixentArcWarningListMapper.getWarnCountByWarnIndexWireless(warnIndex,provinceId,areaId,siteCordArr);
		return warnCountByWarnIndex+warnCountByWarnIndexWireless;
	}
}