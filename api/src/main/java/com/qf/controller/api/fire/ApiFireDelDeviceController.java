package com.qf.controller.api.fire;

import com.qf.model.fire.HixentArcZipperInfo;
import com.qf.service.fire.*;
import io.jsonwebtoken.Claims;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.model.fire.valid.ValidHixentDelEquip;
import com.qf.model.fire.valid.ValidHixentEditDeviceInfo;
import com.qf.model.fire.valid.ValidHixentEditEquipInfo;
import com.qf.model.fire.valid.ValidHixentEditSiteNiName;
import com.qf.model.fire.valid.ValidHixentEfmDevice;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserSiteService;
import com.qf.util.JwtUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

@RestController
@RequestMapping("/api/equip")
public class ApiFireDelDeviceController {
	@Autowired
	private HixentArcWarningListService hixentArcWarningListService;
	@Autowired
	private HixentArcControllGroupService hixentArcaControllGroupService;
	@Autowired
	private HixentArcEfmDeviceService hixentArcEfmDeviceService;

	@Autowired
	private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;
	
	@Autowired
	private HixentAppUserSiteService hixentAppUserSiteService;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HixentArcZipperInfoService hixentArcZipperInfoService;

	/**
	 * 删除主机和下面终端 author RuanYu
	 */
	@Transactional
	// @ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "删除主机和下面终端")
	@RequestMapping(value = "/deleteEfm", method = RequestMethod.POST)
	public ModelMap device(@Valid ValidHixentEfmDevice device, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("参数错误！");
			} else {
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
				if (userinfo.getRoleType() != 0) {
					return ReturnUtil.Error("你不是超级管理员，无法删除中控！");
				}

				boolean deleteEquip = hixentArcEquipmentInfoService.deleteEquip(device.getEfmid());

				if (deleteEquip) {
					String message = "主机删除成功！";
					return ReturnUtil.Success(message);
				} else {
					String message = "主机删除失败！";
					return ReturnUtil.Success(message);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 删除终端 author RuanYu
	 */
	@Transactional
	// @ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "删除终端")
	@RequestMapping(value = "/deleteDevice", method = RequestMethod.POST)
	public ModelMap deldevice(@Valid ValidHixentDelEquip device, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("参数错误！");
			} else {
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
				if (userinfo.getRoleType() != 0) {
					return ReturnUtil.Error("你不是超级管理员，无法删除终端！");
				}

				String deviceIds = device.getDeviceIds();

				String[] split = deviceIds.split(",");

				boolean deleteEquip = hixentArcEquipmentInfoService.deleteDevices(split);

				if (deleteEquip) {
					String message = "设备删除成功！";
					return ReturnUtil.Success(message);
				} else {
					String message = "设备删除失败！";
					return ReturnUtil.Success(message);
				}

			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	
	/**
     * 上传站点LOGO
     * author ligang
     */
    @RequestMapping(value = "/uploadLogo", method = RequestMethod.POST)
    public ModelMap uploadLogo(HttpServletRequest request, 
    		@RequestParam("file") MultipartFile[] file,Integer siteId) { 
    	try {
    		Integer reInt=this.hixentAppUserSiteService.upload(file, siteId);
			if (reInt>0) {
				return ReturnUtil.Success("上传Logo成功");
			} else {
	    		throw new RuntimeException("上传Logo失败");
			}
    	}catch (Exception e) {
    		throw new RuntimeException("上传Logo失败");
		}
    }
    
    /**
     * 删除站点LOGO
     * author ligang
     */
    @RequestMapping(value = "/delLogo", method = RequestMethod.POST)
    public ModelMap delLogo(HttpServletRequest request,Integer siteId) {  
    	try {
    		Integer reInt=this.hixentAppUserSiteService.delImage(siteId);
			if (reInt>0) {
				return ReturnUtil.Success("删除Logo成功");
			} else {
	    		throw new RuntimeException("删除Logo失败");
			}
    	}catch (Exception e) {
    		throw new RuntimeException("删除Logo失败");
		}
    }
	
	/**
	 * 修改站点备注 author ligang
	 */
	@SystemHistoryAnnotation(opration = "修改站点备注 ")
	@RequestMapping(value = "/editSiteNiName", method = RequestMethod.POST)
	public ModelMap editSiteNiName(@Valid ValidHixentEditSiteNiName device, BindingResult bindingResult) {
	try {
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
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
			
			Integer reInt=this.hixentAppUserSiteService.update(device);
			String message = "";
			if (reInt>0) {
				message = "站点修改成功！";
			} else {
				message = "站点修改失败！";
			}
			return ReturnUtil.Success(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 修改中控信息 author RuanYu
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	// @ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "修改中控信息 ")
	@RequestMapping(value = "/editDeviceInfo", method = RequestMethod.POST)
	public ModelMap editDeviceInfo(@Valid ValidHixentEditDeviceInfo device, BindingResult bindingResult) {
		try {
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
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
			String niName = device.getNiName();
			String deviceId = device.getDeviceId();
			String devicePlace = device.getDevicePlace();
			String siteCode = device.getSiteCode();
			String latitude = device.getLatitude();
			String longitute = device.getLongitute();
			Integer offlineTime = device.getOfflineTime();
			Integer offlineEnable = device.getOfflineEnable();

			if (offlineTime != null && offlineTime <= 0) {
				return ReturnUtil.Error("设置时间必须大于0！");
			}
			String specificator=null;
			if(niName==null) {
				
			}else {
				specificator = ToolUtil.str2HexStr(niName);
			}
			//字符串转16进制
			String latitudeHex=null;
			if(latitude==null) {
				
			}else {
				latitudeHex = ToolUtil.str2HexStr(latitude);
			}
			
			String longituteHex =null;
             if(longitute==null) {
				
			}else {
				longituteHex = ToolUtil.str2HexStr(longitute);
			}
			boolean editDeviceInfo = hixentArcEquipmentInfoService.editDeviceInfo(devicePlace, niName, siteCode,
					deviceId, latitudeHex, longituteHex, offlineTime, offlineEnable,specificator);
			String message = "";
			if (editDeviceInfo) {
				return ReturnUtil.Success("中控信息修改成功！");

			} else {
				return ReturnUtil.Error("中控信息修改失败！");
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 修改设备信息 author RuanYu
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	// @ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "修改设备信息 ")
	@RequestMapping(value = "/editEquipInfo", method = RequestMethod.POST)
	public ModelMap editEquipInfo(@Valid ValidHixentEditEquipInfo device, BindingResult bindingResult) {
		try {
			// 获取用户信息
			// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
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
			String deviceCode = device.getDeviceCode();
			String equipCode = device.getEquipId();
			String equipPlace = device.getEquipPlace();
			String siteCode = device.getSiteCode();
			String temUpLimit = device.getTemUpLimit();
			String newEquipCode = device.getNewEquipCode();

//			boolean selEquipIdRepeat = hixentArcEquipmentInfoService.selEquipIdRepeat(deviceCode, newEquipCode,
//					siteCode, equipCode);
//			if (selEquipIdRepeat) {
			boolean editEquipInfo = hixentArcEquipmentInfoService.editEquipInfo(equipCode, equipPlace);
			String message = "";
			if (editEquipInfo) {
				message = "终端设备信息修改成功！";
			} else {
				message = "终端设备信息修改失败！";
			}
			return ReturnUtil.Success(message);
//			} else {
//				return ReturnUtil.Error("设备号已经使用，请重新设置！");
//			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 中控设备信息 author RuanYu
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	// @ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "中控设备信息 ")
	@RequestMapping(value = "/devicesInfo", method = RequestMethod.POST)
	public ModelMap deviceInfo(String deviceId) {
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
			// 查询设备的所有信息
			JSONObject deviceInfo = hixentArcEfmDeviceService.getDeviceInfo(deviceId);

			return ReturnUtil.Success("中控信息获取成功！", deviceInfo);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 终端分页信息 author RuanYu
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	// @ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "终端分页信息 ")
	@RequestMapping(value = "/equipList", method = RequestMethod.POST)
	public ModelMap equipList(String deviceId, Integer pagesize, Integer currentPage, Integer type, String inquire,
			String message, Integer parameterType) {

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
			if (parameterType == null) {
				parameterType = 0;
			}

			// 查询终端的所有信息
			int deviceType = 1;
			List<HixentArcEquipmentInfo> equipList = hixentArcaControllGroupService.getEquipList(type, null, deviceId,
					pagesize, currentPage, inquire, message, null, -1, parameterType, deviceType);
			if (equipList != null) {
				for (int i = 0; i < equipList.size(); i++) {
					// 告警信息
					List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(equipList.get(i).getId());
					if (wl.size() > 0) {
						equipList.get(i).setIs_alarm(1);

					} else {
						equipList.get(i).setIs_alarm(0);
					}
				}
			}

			Integer equipListCount = hixentArcaControllGroupService.getEquipListCount(type, null, deviceId, inquire,
					message, null, -1, parameterType, deviceType);

			JSONObject json = new JSONObject();
			json.put("equipListCount", equipListCount);
			json.put("equipList", equipList);
			return ReturnUtil.Success("终端信息获取成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 终端详细信息 author RuanYu
	 */
	// @RequiresPermissions(value = {"parc_savePermissions"})
	// @ApiLimitConfig(count = 1, time = 1000)
	@SystemHistoryAnnotation(opration = "终端详细信息 ")
	@RequestMapping(value = "/equipInfo", method = RequestMethod.POST)
	public ModelMap equipInfo(String equipId) {

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

			HixentArcEquipmentInfo equipInfo = hixentArcEquipmentInfoService.getOne(equipId);
			if (equipInfo != null) {
				equipInfo.setMessage(ToolUtil.formatDevStr(equipInfo.getMessage()));
				equipInfo.setBoardVersion(ToolUtil.formatDevStr(equipInfo.getBoardVersion()));
				equipInfo.setSoftVersion(ToolUtil.formatDevStr(equipInfo.getSoftVersion()));
				equipInfo.setReleaseData(ToolUtil.formatDevStr(equipInfo.getReleaseData()));
				equipInfo.setLatBmap(ToolUtil.formatDevStr(equipInfo.getLatBmap()));
				equipInfo.setLngBmap(ToolUtil.formatDevStr(equipInfo.getLngBmap()));
				equipInfo.setEquipmentProductionSequenceNumber(
						ToolUtil.formatDevStr(equipInfo.getEquipmentProductionSequenceNumber()));
				
				equipInfo.setNbPhoneNumber(ToolUtil.formatDevStr(equipInfo.getNbPhoneNumber()));		
				equipInfo.setNbChipSerialNumber(ToolUtil.formatDevStr(equipInfo.getNbChipSerialNumber()));
				equipInfo.setEquipmentType(ToolUtil.formatDevStr(equipInfo.getEquipmentType()));
			}
			return ReturnUtil.Success("终端信息获取成功！", equipInfo);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 删除项目 author RuanYu
	 */
	@Transactional
	// @ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "删除项目")
	@RequestMapping(value = "/delSite", method = RequestMethod.POST)
	public ModelMap delSite(String siteCode) {
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
			if (userinfo.getRoleType() != 0) {
				return ReturnUtil.Error("你不是超级管理员，无法删除项目！");
			}

			boolean deleteSite = hixentArcEquipmentInfoService.deleteSite(siteCode);

			if (deleteSite) {
				String message = "项目删除成功！";
				return ReturnUtil.Success(message);
			} else {
				String message = "项目删除失败！";
				return ReturnUtil.Error(message);
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 查询拉链信息 author zhangjun
	 */
	@Transactional
	@SystemHistoryAnnotation(opration = "查询拉链信息")
	@PostMapping(value = "/selectAllZipperInfo")
	public ModelMap selectAllZipperInfo(String deviceId,Integer pageSize,Integer currentPage) {

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
			// 查询终端的所有信息
			List<HixentArcZipperInfo> zipperList = hixentArcZipperInfoService.selectAllZipperInfo(deviceId,pageSize,currentPage);
			int zipperListCount = hixentArcZipperInfoService.countZipperInfoByDeviceId(deviceId);
			JSONObject json = new JSONObject();
			json.put("zipperListCount", zipperListCount);
			json.put("zipperList", zipperList);
			return ReturnUtil.Success("拉链终端信息获取成功！", json);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}