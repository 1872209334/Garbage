package com.qf.controller.api.fire;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.qf.model.fire.HixentArcZipperInfo;
import com.qf.service.fire.HixentArcZipperInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcAlarmDealFeedBack;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcAlarmLogService;
import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

import java.util.List;

@RestController
@RequestMapping("/api/alarm")
public class ApiFireAlarmListController {

	@Autowired
	private HixentArcAlarmLogService hixentArcAlarmLogService;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HixentArcZipperInfoService hixentArcZipperInfoService;

	@Resource
	private RedisUtil redisUtil;


    /**
     * 查询故障列表 author 张君
     */
    @SystemHistoryAnnotation(opration = "删除报警列表")
    @PostMapping(value = "/deleteZipperLogById")
    public ModelMap deleteZipperLogById(int unid) {
        try {
            // 获取用户信息
            // HixentAppUser userinfo = (HixentAppUser)
            // SecurityUtils.getSubject().getPrincipal();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String auth = request.getHeader(jwtConfig.getJwtHeader());
            auth = auth.substring(7, auth.length());
            Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
            String userId = claims.getId();
            String[] userArr = userId.split("_");
            if (!userArr[0].equals("admin")) {
                return ReturnUtil.Error("已退出，请重新登录！");
            }
            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
            if (userinfo == null) {
                return ReturnUtil.Error("已退出，请重新登录！");
            }
            String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
            String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
            int areaIdInt = Integer.parseInt(areaId);
            int provinceIdInt = Integer.parseInt(provinceId);
            String bid = userinfo.getBid();
            String[] siteCordArr = bid.split(",");

            JSONObject json = new JSONObject();
            int result = hixentArcZipperInfoService.deleteAlramLog(unid);
            json.put("result", result);
            return ReturnUtil.Success("删除报警列表数据成功！", json);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

	/**
	 * 查询故障列表 author 张君
	 */
	@SystemHistoryAnnotation(opration = "查询离线列表")
	@PostMapping(value = "/offLineLog")
	public ModelMap offLineLog(String projectId, String deviceId, String isAlarm, Integer currentPage, Integer pageSize) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");

			JSONObject json = new JSONObject();
			List<HixentArcZipperInfo> zipperOffLineLog = hixentArcZipperInfoService.selectOffLineLog(projectId,deviceId,isAlarm,pageSize,currentPage);
			int zipperOffLineLogCount = hixentArcZipperInfoService.countZipperOffLineLog(deviceId, isAlarm);
			json.put("zipperOffLineLogCount", zipperOffLineLogCount);
			json.put("zipperOffLineLog", zipperOffLineLog);
			return ReturnUtil.Success("获取离线日志数据成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 查询故障列表 author 张君
	 */
	@SystemHistoryAnnotation(opration = "按设备id查询离线列表")
	@PostMapping(value = "/offLineLogBySiteId")
	public ModelMap offLineLogBySiteId(String projectId, String deviceId, String isAlarm, Integer currentPage, Integer pageSize) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");

			JSONObject json = new JSONObject();
			List<HixentArcZipperInfo> zipperOffLineLog = hixentArcZipperInfoService.selectOffLineLogBySiteId(projectId,deviceId,isAlarm,pageSize,currentPage);
			int zipperOffLineLogCount = hixentArcZipperInfoService.countZipperOffLineLog(projectId,deviceId, isAlarm);
			json.put("zipperOffLineLogCount", zipperOffLineLogCount);
			json.put("zipperOffLineLog", zipperOffLineLog);
			return ReturnUtil.Success("按设备id获取离线日志数据成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 查询故障列表 author 张君
	 */
	@SystemHistoryAnnotation(opration = "查询故障列表")
	@PostMapping(value = "/faultLog")
	public ModelMap faultLog(String projectId, String deviceId, String isAlarm, Integer currentPage, Integer pageSize) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");

			JSONObject json = new JSONObject();
			List<HixentArcZipperInfo> zipperFaultLog = hixentArcZipperInfoService.selectFaultLog(projectId,deviceId,isAlarm,pageSize,currentPage);
			int zipperFaultLogCount = hixentArcZipperInfoService.countZipperFaultLog(projectId,deviceId, isAlarm);
			json.put("zipperFaultLogCount", zipperFaultLogCount);
			json.put("zipperFaultLog", zipperFaultLog);
			return ReturnUtil.Success("获取故障日志数据成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
			* 查询故障列表 author 张君
	 */
	@SystemHistoryAnnotation(opration = "按项目id查询故障列表")
	@PostMapping(value = "/faultLogBySiteId")
	public ModelMap faultLogBySiteId(String projectId, String deviceId, String isAlarm, Integer currentPage, Integer pageSize) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");

			JSONObject json = new JSONObject();
			List<HixentArcZipperInfo> zipperFaultLog = hixentArcZipperInfoService.selectFaultLogBySiteId(projectId, deviceId,isAlarm,pageSize,currentPage);
			int zipperFaultLogCount = hixentArcZipperInfoService.countZipperFaultLog(projectId,deviceId,isAlarm);
			json.put("zipperFaultLogCount", zipperFaultLogCount);
			json.put("zipperFaultLog", zipperFaultLog);
			return ReturnUtil.Success("按项目id获取故障日志数据成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 查询全部报警列表 author zhangjun
	 */
	@SystemHistoryAnnotation(opration = "查询全部报警列表")
	@PostMapping(value = "/alarmLog")
	public ModelMap alarmLog(String projectId, String deviceId, String isAlarm, Integer currentPage, Integer pageSize) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");

			JSONObject json = new JSONObject();
			List<HixentArcZipperInfo> zipperAlarmLog = hixentArcZipperInfoService.selectAlarmLog(projectId,deviceId,isAlarm,pageSize,currentPage);
			int zipperAlarmLogCount = hixentArcZipperInfoService.countZipperAlarmLog(projectId,deviceId, isAlarm);
//			int zipperAlarmLogCount = zipperAlarmLog.size();
			json.put("zipperAlarmLogCount", zipperAlarmLogCount);
			json.put("zipperAlarmLog", zipperAlarmLog);
			return ReturnUtil.Success("获取报警日志数据成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
    /**
     * 按项目id查询报警列表 author zhangjun
     */
    @SystemHistoryAnnotation(opration = "按项目id查询报警列表")
    @PostMapping(value = "/alarmLogBySiteId")
    public ModelMap alarmLogBySiteId(String projectId, String deviceId, String isAlarm, Integer currentPage, Integer pageSize) {
        try {

            // 获取用户信息
            // HixentAppUser userinfo = (HixentAppUser)
            // SecurityUtils.getSubject().getPrincipal();
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = requestAttributes.getRequest();
            String auth = request.getHeader(jwtConfig.getJwtHeader());
            auth = auth.substring(7, auth.length());
            Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
            String userId = claims.getId();
            String[] userArr = userId.split("_");
            if (!userArr[0].equals("admin")) {
                return ReturnUtil.Error("已退出，请重新登录！");
            }
            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
            if (userinfo == null) {
                return ReturnUtil.Error("已退出，请重新登录！");
            }
            String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
            String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
            int areaIdInt = Integer.parseInt(areaId);
            int provinceIdInt = Integer.parseInt(provinceId);
            String bid = userinfo.getBid();
            String[] siteCordArr = bid.split(",");

            JSONObject json = new JSONObject();
            List<HixentArcZipperInfo> zipperAlarmLog = hixentArcZipperInfoService.selectAlarmLogBySiteId(projectId,deviceId,isAlarm,pageSize,currentPage);
			int zipperAlarmLogCount = hixentArcZipperInfoService.countZipperAlarmLog(projectId,deviceId, isAlarm);
//            int zipperAlarmLogCount = zipperAlarmLog.size();
            json.put("zipperAlarmLogCount", zipperAlarmLogCount);
            json.put("zipperAlarmLog", zipperAlarmLog);
            return ReturnUtil.Success("按项目id获取报警日志数据成功！", json);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
//	@SystemHistoryAnnotation(opration = "查询报警日志")
//	@RequestMapping(value = "/alarmLog", method = RequestMethod.POST)
//	public ModelMap alarmLog(Integer isDevice, Integer siteId, String deviceId, Integer equipId, String inquir,
//			Integer currentPage, Integer pageSize, Integer type,Integer warnIndex) {
//		try {
//
//			// 获取用户信息
//			// HixentAppUser userinfo = (HixentAppUser)
//			// SecurityUtils.getSubject().getPrincipal();
//			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
//					.getRequestAttributes();
//			HttpServletRequest request = requestAttributes.getRequest();
//			String auth = request.getHeader(jwtConfig.getJwtHeader());
//			auth = auth.substring(7, auth.length());
//			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
//			String userId = claims.getId();
//			String[] userArr = userId.split("_");
//			if (!userArr[0].equals("admin")) {
//				return ReturnUtil.Error("已退出，请重新登录！");
//			}
//			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
//			if (userinfo == null) {
//				return ReturnUtil.Error("已退出，请重新登录！");
//			}
//			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
//			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
//			int areaIdInt = Integer.parseInt(areaId);
//			int provinceIdInt = Integer.parseInt(provinceId);
//			String bid = userinfo.getBid();
//			String[] siteCordArr = bid.split(",");
//
//			JSONObject alarmLog = new JSONObject();
//			if (isDevice == 0) {
//				// 中控
//				alarmLog = hixentArcAlarmLogService.alarmLog(siteId, deviceId, currentPage, pageSize, provinceIdInt,
//						areaIdInt, siteCordArr,warnIndex);
//			} else if (isDevice == 1) {
//				// 终端
//				alarmLog = hixentArcAlarmLogService.alarmLogForEquip(siteId, deviceId, currentPage, pageSize,
//						provinceIdInt, areaIdInt, siteCordArr, type, inquir,warnIndex);
//			}
//
//			return ReturnUtil.Success("获取报警日志数据成功！", alarmLog);
//
//		} catch (Exception e) {
//			throw new RuntimeException(e.getMessage());
//		}
//	}

	/**
	 * 查询报警历史 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "查询报警历史")
	@RequestMapping(value = "/alarmHistory", method = RequestMethod.POST)
	public ModelMap alarmHistory(Integer isDevice, String startTime, String endTime, String inquir, Integer currentPage,
			Integer pageSize, Integer type) {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			long startTimeToStamp = 0;
			long endTimeToStamp = 0;
             if(startTime==null) {
            	 startTime="";
             }
             if(endTime==null) {
            	 endTime="";
             }
			if (!startTime.equals("")) {
				String runStartTime = startTime + " 00:00:00"; 
				startTimeToStamp = DateUtil.getTimestamp(runStartTime);
				
			} 
			if (!endTime.equals("") ) {
				String runEndTime = endTime + " 23:59:59";
				endTimeToStamp = DateUtil.getTimestamp(runEndTime);
			} 
			
			JSONObject alarmHistoryForAll = hixentArcAlarmLogService.alarmHistoryForAll(currentPage, pageSize, provinceIdInt, areaIdInt, siteCordArr, startTimeToStamp, endTimeToStamp);
			return ReturnUtil.Success("获取报警历史数据成功！", alarmHistoryForAll);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 处理反馈列表 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "处理反馈列表")
	@RequestMapping(value = "/alarmDealFeedback", method = RequestMethod.POST)
	public ModelMap alarmDealFeedback(String startTime, String endTime, String inquire,
			Integer currentPage, Integer pageSize, Integer type) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);	
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			
			long startTimeToStamp = 0;
			long endTimeToStamp = 0;
			if(startTime.isEmpty()) {
				startTime="";
			}
			if(endTime.isEmpty()) {
				endTime="";
			}
			if (!startTime.equals("") ) {
				String runStartTime = startTime + " 00:00:00"; 
				startTimeToStamp = DateUtil.getTimestamp(runStartTime);
				
			} 
			if (!endTime.equals("") ) {
				String runEndTime = endTime + " 00:00:00";
				endTimeToStamp = DateUtil.getTimestamp(runEndTime);
			} 
			
			
			JSONObject alarmDealFeedBack = hixentArcAlarmLogService.alarmDealFeedBack(currentPage, pageSize, provinceIdInt, areaIdInt, siteCordArr, type, inquire, startTimeToStamp, endTimeToStamp);
			return ReturnUtil.Success("获取处理反馈列表成功！", alarmDealFeedBack);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 审核 处理反馈 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "审核 处理反馈")
	@RequestMapping(value = "/auditDealFeedback", method = RequestMethod.POST)
	public ModelMap auditDealFeedback(Integer id) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
             if(id<0) {
            	 return ReturnUtil.Error("请选择一个处理反馈！");	 
             }
             boolean auditDealFeedBack = hixentArcAlarmLogService.auditDealFeedBack(id);
			
             return ReturnUtil.Success("审核处理反馈成功！", auditDealFeedBack);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 离线报警 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "离线报警")
	@RequestMapping(value = "/offLineAlarm", method = RequestMethod.POST)
	public ModelMap offLineAlarm() {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
            
           hixentArcAlarmLogService.getAllOffLineTables();
			 
             
             return ReturnUtil.Success("插入离线数据！");

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 *派单 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "派单")
	@RequestMapping(value = "/dispatch", method = RequestMethod.POST)
	public ModelMap dispatch(Integer appUserId,Integer warnId) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
            
           boolean dispach = hixentArcAlarmLogService.dispach(warnId,userinfo.getId(),appUserId);
			 
             if(dispach) {
            	 return ReturnUtil.Success("派单成功！"); 
             }else {
            	 return ReturnUtil.Error("派单失败，重新派单！"); 
             }
             

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 *删除报警 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "删除报警信息")
	@RequestMapping(value = "/delWarn", method = RequestMethod.POST)
	public ModelMap delWarn(Integer warnId) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			boolean delWarn = hixentArcAlarmLogService.delWarn(warnId);
			 if(delWarn) {
            	 return ReturnUtil.Success("删除成功！"); 
             }else {
            	 return ReturnUtil.Error("删除失败，重新删除！"); 
             }
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 *报警详情 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "报警详情")
	@RequestMapping(value = "/getWarnInfo", method = RequestMethod.POST)
	public ModelMap getWarnInfo(Integer warnId) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentArcAlarmDealFeedBack warnInfo = hixentArcAlarmLogService.getWarnInfo(warnId);
			
			return ReturnUtil.Success("报警详情成功！",warnInfo); 
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 *某类型警报数 author RuanYu
	 */
//@Transactional
//@ApiLimitConfig(count=1,time=1000)
//@RequiresPermissions(value = {"parc_saveDevice"}) 
	@SystemHistoryAnnotation(opration = "某类型警报数")
	@RequestMapping(value = "/getWarnCountByWarnIndex", method = RequestMethod.POST)
	public ModelMap getWarnCountByWarnIndex(Integer warnIndex) {
		try {

			// 获取用户信息
			// HixentAppUser userinfo = (HixentAppUser)
			// SecurityUtils.getSubject().getPrincipal();
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("admin")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);	
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			 Integer warnCountByWarnIndex = hixentArcAlarmLogService.getWarnCountByWarnIndex(warnIndex,provinceIdInt,areaIdInt,siteCordArr);
			
			return ReturnUtil.Success("某类型警报数！",warnCountByWarnIndex); 
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}