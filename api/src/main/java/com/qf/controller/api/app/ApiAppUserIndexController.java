package com.qf.controller.api.app;

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
import com.qf.model.fire.HixentArcAppMapSite;
import com.qf.service.app.HixentAppUserIndexService;
import com.qf.service.app.HixentAppUserService;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/app/fire/index")
public class ApiAppUserIndexController {

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private HixentAppUserService hixentAppUserService;
	
	@Autowired
	private HixentAppUserIndexService hixentAppUserIndexService;
	
	/**
	 * app首页数据 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app首页数据")
	@RequestMapping(value = "/default", method = RequestMethod.POST)
	public ModelMap indexDefault() {
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
			 JSONObject indexData = hixentAppUserIndexService.getIndexData(id);
			
			return ReturnUtil.Success("获取首页数据成功！", indexData);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 已处理数量 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "已处理数量")
	@RequestMapping(value = "/dealCount", method = RequestMethod.POST)
	public ModelMap dealCount(String siteCode) {
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
			JSONObject dealCount = hixentAppUserIndexService.getDealCount(id,siteCode);
			
			return ReturnUtil.Success("获取已处理数量数据成功！", dealCount);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 未处理数量 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "未处理数量")
	@RequestMapping(value = "/unDealCount", method = RequestMethod.POST)
	public ModelMap unDealCount(String siteCode) {
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
			 Integer unDealCount = hixentAppUserIndexService.unDealCount(id,siteCode);
			
			return ReturnUtil.Success("获取未处理数量数据成功！", unDealCount);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 地图数据 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "地图数据")
	@RequestMapping(value = "/mapData", method = RequestMethod.POST)
	public ModelMap mapData() {
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
			
			 List<HixentArcAppMapSite> mapData = hixentAppUserIndexService.mapData(id);
			for (int i = 0; i < mapData.size(); i++) {
				HixentArcAppMapSite hixentArcAppMapSite = mapData.get(i);
				if(ToolUtil.StringNotBlank(hixentArcAppMapSite.getLongitute())) {
					hixentArcAppMapSite.setLongitute(ToolUtil.hexStr2Str(hixentArcAppMapSite.getLongitute()));
				}
				if(ToolUtil.StringNotBlank(hixentArcAppMapSite.getLatitude())) {
					hixentArcAppMapSite.setLatitude(ToolUtil.hexStr2Str(hixentArcAppMapSite.getLatitude()));
		  		}	
				if(ToolUtil.StringNotBlank(hixentArcAppMapSite.getLngBmap())) {
					hixentArcAppMapSite.setLngBmap(ToolUtil.hexStr2Str(hixentArcAppMapSite.getLngBmap()));
				}
				if(ToolUtil.StringNotBlank(hixentArcAppMapSite.getLatBmap())) {
					hixentArcAppMapSite.setLatBmap(ToolUtil.hexStr2Str(hixentArcAppMapSite.getLatBmap()));
		  		}
				JSONObject countBySiteCode = hixentAppUserIndexService.getCountBySiteCode(id,hixentArcAppMapSite.getSiteCode());
				hixentArcAppMapSite.setCountData(countBySiteCode);
			}
			
			return ReturnUtil.Success("获取地图数据成功！", mapData);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 项目详情author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "项目详情")
	@RequestMapping(value = "/siteInfo", method = RequestMethod.POST)
	public ModelMap siteInfo(String siteCode) {
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
			
			 List<HixentArcAppMapSite> mapData = hixentAppUserIndexService.mapData(id);
			
			
			return ReturnUtil.Success("获取地图数据成功！", mapData);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}