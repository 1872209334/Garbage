package com.qf.controller.api.fire;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
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
import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcIndex;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcIndexService;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/statistics")
public class ApiFireStatisticsMangerController {

	@Autowired
	private HixentArcIndexService hixentArcIndexService;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

	/**
	 *统计管理 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "统计管理显示")
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
			
			//Integer fireRole = userinfo.getFireRole();
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			//角色类型
			Integer roleType =  userinfo.getRoleType();
			
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
			//拥有的项目
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			
			List<HixentProvince> provinceList = new ArrayList<HixentProvince>();
			
			JSONObject outjson = new JSONObject();
           //管理员获取省份
			if (roleType == 0 || roleType==1) {
				if(roleType == 0) {
					// 超级管理员
					provinceIdInt=0;
				}
				areaIdInt=0;
				// 获取省份
				provinceList = hixentUserService.getPrinvince(provinceIdInt);
			}
			
			HixentArcIndex statisticsData = hixentArcIndexService.getStatisticsData(areaIdInt,provinceIdInt,siteCordArr);
			outjson.put("statisticsData", statisticsData);
			outjson.put("provinceList", provinceList);
			outjson.put("provinceId", provinceIdInt);
			return ReturnUtil.Success("获取统计数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 *统计管理切换省份 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "统计管理切换省份")
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
		
			Integer fireRole = userinfo.getFireRole();
			if(fireRole!=1) {
				return ReturnUtil.Error("只有管理员才有这个权限！");
			}
			
			//拥有的项目
			String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			
			
			
			JSONObject outjson = new JSONObject();
          
			
			HixentArcIndex statisticsData = hixentArcIndexService.getStatisticsData(0,provinceId,siteCordArr);
			outjson.put("statisticsData", statisticsData);
			
			return ReturnUtil.Success("获取统计数据成功！", outjson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}