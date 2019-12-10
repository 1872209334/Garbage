package com.qf.controller.api.app;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcAppDeviceAndEquipInfo;
import com.qf.model.fire.HixentArcSite;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.app.HixentAppUserSiteService;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/app/fire/site")
public class ApiAppUserSiteController {

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private HixentAppUserService hixentAppUserService;
	
	@Autowired
	private HixentAppUserSiteService hixentAppUserSiteService;
	
	/**
	 * app获取项目详情 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app获取项目详情 ")
	@RequestMapping(value = "/getSiteInfo", method = RequestMethod.POST)
	public ModelMap getSiteInfo(String siteCode) {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentArcSite siteInfo = hixentAppUserSiteService.getSiteInfo(siteCode);
			return ReturnUtil.Success("获取项目详情成功！", siteInfo);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * app获取项目列表 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app获取项目列表 ")
	@RequestMapping(value = "/getSiteList", method = RequestMethod.POST)
	public ModelMap getSiteList() {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer id = userinfo.getId();
			 List<HixentArcSite> siteList = hixentAppUserSiteService.getSiteList(id);
			return ReturnUtil.Success("获取项目列表成功！", siteList);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * app获取设备列表 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app获取设备列表 ")
	@RequestMapping(value = "/getDeviceList", method = RequestMethod.POST)
	public ModelMap getDeviceList(String siteCode,Integer equipType,Integer warnIndex,
			Integer isMqttEquip, Integer currentPage, Integer pageSize) {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer id = userinfo.getId();
			List<HixentArcAppDeviceAndEquipInfo> deviceList = new ArrayList<HixentArcAppDeviceAndEquipInfo>();
			Integer total=0;
			String inquire=null;
			if(equipType==-1) {
				//中控
				 deviceList = hixentAppUserSiteService.getDeviceList(id,siteCode,warnIndex,inquire,pageSize,currentPage);
				 for (int i = 0; i < deviceList.size(); i++) {
					 HixentArcAppDeviceAndEquipInfo hde=	 deviceList.get(i);
					 hde.setIsDevice(0);
					 hde.setSpecificator(ToolUtil.formatDevStr(hde.getSpecificator()));
				 }
				 total=hixentAppUserSiteService.getDeviceListCount(id, siteCode, warnIndex, inquire);
			}else {
				deviceList = hixentAppUserSiteService.getEquipList(id,siteCode,warnIndex,equipType,isMqttEquip,inquire,pageSize,currentPage);
				for (int i = 0; i < deviceList.size(); i++) {
					 HixentArcAppDeviceAndEquipInfo hde=	 deviceList.get(i);
					 hde.setIsDevice(1);
					 hde.setMessage(ToolUtil.formatDevStr(hde.getMessage()));
				 }
				total=hixentAppUserSiteService.getEquipListCount(id, siteCode, warnIndex, equipType, isMqttEquip, inquire);
			}
			JSONObject outJson = new JSONObject();
			outJson.put("deviceList", deviceList);
			outJson.put("total", total);
			return ReturnUtil.Success("获取项目列表成功！", outJson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * app搜索设备 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app搜索设备列表 ")
	@RequestMapping(value = "/inquireDeviceList", method = RequestMethod.POST)
	public ModelMap inquireDeviceList(String siteCode,String inquire,Integer equipType,Integer currentPage, Integer pageSize) {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer id = userinfo.getId();
			List<HixentArcAppDeviceAndEquipInfo> deviceList = new ArrayList<HixentArcAppDeviceAndEquipInfo>();
			Integer total=0;
			Integer warnIndex=-2;
			Integer isMqttEquip=-1;
			if(equipType==-1) {
				//中控
				 deviceList = hixentAppUserSiteService.getDeviceList(id,siteCode,warnIndex,inquire,pageSize,currentPage);
				 for (int i = 0; i < deviceList.size(); i++) {
					 HixentArcAppDeviceAndEquipInfo hde=	 deviceList.get(i);
					 hde.setIsDevice(0);
					 hde.setSpecificator(ToolUtil.formatDevStr(hde.getSpecificator()));
				 }
				 total=hixentAppUserSiteService.getDeviceListCount(id, siteCode, warnIndex, inquire); 
			}else {
				deviceList = hixentAppUserSiteService.getEquipList(id,siteCode,warnIndex,equipType,isMqttEquip,inquire,pageSize,currentPage);
				for (int i = 0; i < deviceList.size(); i++) {
					 HixentArcAppDeviceAndEquipInfo hde=	 deviceList.get(i);
					 hde.setIsDevice(1);
					 hde.setMessage(ToolUtil.formatDevStr(hde.getMessage()));
				 }
				total=hixentAppUserSiteService.getEquipListCount(id, siteCode, warnIndex, equipType, isMqttEquip, inquire);
			}
			
			JSONObject outJson = new JSONObject();
			outJson.put("deviceList", deviceList);
			outJson.put("total", total);
			return ReturnUtil.Success("获取项目列表成功！", outJson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * app设备详情 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app获取设备详情 ")
	@RequestMapping(value = "/getDeviceInfo", method = RequestMethod.POST)
	public ModelMap getDeviceInfo(String id,Integer equipType) {
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentArcAppDeviceAndEquipInfo deviceInfo=null;
			if(equipType==-1) {
				//中控
				 deviceInfo = hixentAppUserSiteService.getDeviceInfo(id);
				 deviceInfo.setSpecificator(ToolUtil.formatDevStr(deviceInfo.getSpecificator())); 
			}else {
				//终端
				 deviceInfo = hixentAppUserSiteService.getEquipInfo(id);
				 deviceInfo.setMessage(ToolUtil.formatDevStr(deviceInfo.getMessage()));
			}
			return ReturnUtil.Success("获取设备详情成功！", deviceInfo);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}