package com.qf.controller.api.fire;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentProvince;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcAppMapSite;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcIndex;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcIndexService;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/index")
public class ApiFireIndexMapController {

	@Autowired
	private HixentArcIndexService HixentArcIndexService;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

	/**
	 * 首页显示 author RuanYu
	 */
	@Transactional
	@ApiLimitConfig(count = 1, time = 1000)
	//@RequiresPermissions(value = {"parc_aabb"})
	@SystemHistoryAnnotation(opration = "首页显示")
	@RequestMapping(value = "/default", method = RequestMethod.POST)
	public ModelMap device() {
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
			Integer roleType= userinfo.getRoleType();
			
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			//获取首页数据
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			HixentArcIndex hai = HixentArcIndexService.getCount(areaIdInt, provinceIdInt,siteCordArr);
			
			List<HixentProvince> provinceList = new ArrayList<HixentProvince>();
			List<HixentArea> cityList = new ArrayList<HixentArea>();
			JSONObject outjson = new JSONObject();
           //超级管理员获取省份和地区
			if (roleType == 0) {
				// 超级管理员
				if (areaIdInt != 0) {
					// 获取省份
					provinceList = hixentUserService.getPrinvince(0);
					// 获取地区
					cityList = hixentUserService.getCity(provinceIdInt);
				} else {
					if (provinceIdInt != 0) {
						// 获取省份
						provinceList = hixentUserService.getPrinvince(0);
						// 获取地区
						cityList = hixentUserService.getCity(provinceIdInt);
					} else {
						// 获取省份
						provinceList = hixentUserService.getPrinvince(0);
					}
				}

			}
			outjson.put("countData", hai);
			outjson.put("provinceList", provinceList);
			outjson.put("cityList", cityList);
			outjson.put("areaId", areaIdInt);
			outjson.put("provinceId", provinceIdInt);
			return ReturnUtil.Success("获取首页数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 首页省份切换 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "首页省份切换")
	@RequestMapping(value = "/changeProvince", method = RequestMethod.POST)
	public ModelMap changeProvince(Integer provinceId) {
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
			if (provinceId == null || provinceId < 0) {
				return ReturnUtil.Error("请选择省份！");
			}
			Integer areaId = 0;
			redisUtil.set("areaId_" + userinfo.getUid(), areaId + "", jwtConfig.getExpiration() / 1000);
			redisUtil.set("provinceId_" + userinfo.getUid(), provinceId + "", jwtConfig.getExpiration() / 1000);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			HixentArcIndex hai = HixentArcIndexService.getCount(areaId, provinceId,siteCordArr);

			List<HixentArea> cityList = new ArrayList<HixentArea>();
			JSONObject outjson = new JSONObject();
			// 获取地区
			cityList = hixentUserService.getCity(provinceId);

			outjson.put("countData", hai);
			outjson.put("cityList", cityList);
			return ReturnUtil.Success("切换省份获取数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 首页地区切换 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "首页地区切换")
	@RequestMapping(value = "/changeArea", method = RequestMethod.POST)
	public ModelMap changeArea(Integer provinceId, Integer areaId) {
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
			if (provinceId == null || provinceId < 0) {
				return ReturnUtil.Error("请选择省份！");
			}
			if (areaId == null || areaId < 0) {
				return ReturnUtil.Error("请选择地区！");
			}
			redisUtil.set("areaId_" + userinfo.getUid(), areaId + "", jwtConfig.getExpiration() / 1000);
			redisUtil.set("provinceId_" + userinfo.getUid(), provinceId + "", jwtConfig.getExpiration() / 1000);
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			HixentArcIndex hai = HixentArcIndexService.getCount(areaId, provinceId,siteCordArr);

			List<HixentArea> cityList = new ArrayList<HixentArea>();
			JSONObject outjson = new JSONObject();

			outjson.put("countData", hai);

			return ReturnUtil.Success("切换地区获取数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 地图数据 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "地图数据")
	@RequestMapping(value = "/mapSite", method = RequestMethod.POST)
	public ModelMap mapSiteCount() {
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

			Integer fireRole = userinfo.getFireRole();
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			List<HixentProvince> provinceList = new ArrayList<HixentProvince>();
			List<HixentArea> cityList = new ArrayList<HixentArea>();
			JSONObject outjson = new JSONObject();
			
			//获取角色类型
//			Integer roleType = hixentUserService.getRoleType(fireRole);
//			if (roleType == 0 || roleType == 1) {
//				// 二级管理员和超级管理员
//				areaIdInt = 0;
//			}
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			if(areaIdInt!=0) {
				cityList = hixentUserService.getCityByAreaId(areaIdInt);	
			}
			if(provinceIdInt!=0) {
				provinceList = hixentUserService.getPrinvince(provinceIdInt);
			}
			
			
			List<HixentArcEfmDevice> getDeviceList = hixentUserService.getDevice(provinceIdInt, areaIdInt, siteCordArr);
			
			outjson.put("getDeviceList", getDeviceList);
			outjson.put("provinceList", provinceList);
			outjson.put("cityList", cityList);
			outjson.put("areaId", areaIdInt);
			outjson.put("provinceId", provinceIdInt);
			return ReturnUtil.Success("获取地图数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	//头部报警数
	/**
	 * 头部报警数 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "头部报警数")
	@RequestMapping(value = "/headAlarmCount", method = RequestMethod.POST)
	public ModelMap headAlarmCount() {
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

			Integer fireRole = userinfo.getFireRole();
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			Integer headAlarmCount = HixentArcIndexService.headAlarmCount(areaIdInt,provinceIdInt,siteCordArr);
			
			return ReturnUtil.Success("获取头部报警数据成功！", headAlarmCount);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 *地图无线终端数 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "地图无线终端数据")
	@RequestMapping(value = "/mapWireless", method = RequestMethod.POST)
	public ModelMap mapWireless() {
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

			Integer fireRole = userinfo.getFireRole();
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
		
			JSONObject outjson = new JSONObject();
			
			//获取角色类型
//			Integer roleType = hixentUserService.getRoleType(fireRole);
//			if (roleType == 0 || roleType == 1) {
//				// 二级管理员和超级管理员
//				areaIdInt = 0;
//			}
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			
			 List<HixentArcAppMapSite> wirelessEquiplist = hixentUserService.getWirelessEquiplist(provinceIdInt, areaIdInt, siteCordArr);
			
			outjson.put("wirelessEquiplist", wirelessEquiplist);
			
			return ReturnUtil.Success("获取地图数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 *地图无线终端项目数 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "地图无线终端项目数据")
	@RequestMapping(value = "/mapData", method = RequestMethod.POST)
	public ModelMap mapData() {
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

			Integer fireRole = userinfo.getFireRole();
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
		
			JSONObject outjson = new JSONObject();
			

			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			
			List<HixentArcAppMapSite> mapData = hixentUserService.mapData(provinceIdInt,areaIdInt,siteCordArr);
			if(mapData==null) {
				
				outjson.put("mapData", null);
				return ReturnUtil.Success("获取地图数据成功！", outjson);
			}
			
			for (int i = 0; i < mapData.size(); i++) {
				
				String siteCode = mapData.get(i).getSiteCode();
				JSONObject countBySiteCode = hixentUserService.getCountBySiteCode(siteCode);
				mapData.get(i).setCountData(countBySiteCode);
			}
			outjson.put("mapData", mapData);
			return ReturnUtil.Success("获取地图数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}