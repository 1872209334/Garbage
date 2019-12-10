package com.qf.controller.api.fire;

import io.jsonwebtoken.Claims;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.ComConstant;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentRole;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcSite;
import com.qf.service.admin.HixentPermissionsRoleService;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserSiteService;
import com.qf.service.fire.HixentArcIndexService;
import com.qf.service.fire.HixentMapService;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.StringUtil;
import com.qf.util.TextUtil;

@RestController
@RequestMapping("/api/map")
public class ApiMapController {
	@Autowired
	private HixentArcIndexService HixentArcIndexService;

	@Autowired
	private HixentUserService hixentUserService;
	
	@Autowired
	private HixentPermissionsRoleService hixentRoleService;
	
	@Autowired
	private HixentAppUserSiteService hixentArcSiteService;
	
	@Autowired
	private HixentMapService hixentMapService;
	
	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;
	
	
	/**
	 * 地图数据展示 author lg
	 */
	@Transactional
	@SystemHistoryAnnotation(opration = "地图数据")
	@RequestMapping(value = "/mapSite", method = RequestMethod.POST)
	public ModelMap getMapData(String siteCode) {
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
			HixentRole hixentRole=this.hixentRoleService.selectRoleInfo(userinfo.getFireRole());
			//获取当前用户绑定项目:
			//1、区域管理员/管控管理员-当前用户绑定多个与一个，获取缓存项目ID
			//2、区域管理员/管控管理员-当前用户绑定一个，获取缓存项目ID
			//3、区域管理员/管控管理员-都没有绑定项目，提示：未绑定项目，请联系管理员设置
			//4、超级管理员-直接显示默认地图
			//0超级管理员，1二级管理员，2三级管理员
			Integer mapSign=0;//mapSign为0代表显示默认地图，为1代表显示项目地图
			HixentArcSite hixentArcSite=null;
			if(ComConstant.SUPER_MANAGER_KEY.equals(hixentRole.getRole_type())){
			}else{
				if(StringUtil.strIsNullOrEmpty(userinfo.getBid())
						 || "0".equals(userinfo.getBid())){
					return ReturnUtil.Error("该用户未绑定项目，请联系管理员设置");
				}else{
					//获取当前登录项目
					siteCode=StringUtil.strIsNullOrEmpty(siteCode)?"":siteCode;
					hixentArcSite=this.hixentArcSiteService.getSite(siteCode);
					if(hixentArcSite!=null){
						//判断经纬度与精确度不能为空，为空提示管理配置
						if(StringUtil.strIsNullOrEmpty(hixentArcSite.getLatitude()) ||
								StringUtil.strIsNullOrEmpty(hixentArcSite.getLongitute()) ||
								hixentArcSite.getMapNum()==null){
							return ReturnUtil.Error("项目经纬度或者地图比例级别未设置，请到【项目管理->项目列表】进行设置");
						}
						mapSign=1;
					}else{
						return ReturnUtil.Error("该用户未绑定项目，请联系管理员设置");
					}
				}
			}
			//获取站点分组
			String bid =StringUtil.strIsNullOrEmpty(userinfo.getBid())?"0":userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			

			int areaIdInt =TextUtil.getInt(redisUtil.get("areaId_" + userinfo.getUid()));
			int provinceIdInt =TextUtil.getInt(redisUtil.get("provinceId_" + userinfo.getUid())); 
			JSONObject outjson = new JSONObject();
			if(mapSign==0){
				List<HixentArcEfmDevice> deviceList = hixentUserService.getDevice(provinceIdInt, areaIdInt, siteCordArr);
				outjson.put("getDeviceList", deviceList); 
			}else{
				Set<String> siteCodeSet=new HashSet<String>();
				siteCodeSet.add(siteCode);
				List<HixentArcEquipmentInfo> deviceList=this.hixentMapService.getDevice(siteCodeSet);
				outjson.put("getDeviceList", deviceList);
				outjson.put("latitude", hixentArcSite.getLatitude());
				outjson.put("longitute", hixentArcSite.getLongitute());
				outjson.put("mapNum", hixentArcSite.getMapNum());
			}
			
			outjson.put("areaId", areaIdInt);
			outjson.put("provinceId", provinceIdInt);
			outjson.put("mapSign", mapSign);
			return ReturnUtil.Success("获取地图数据成功！", outjson);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
