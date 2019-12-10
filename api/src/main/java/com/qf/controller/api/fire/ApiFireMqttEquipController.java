package com.qf.controller.api.fire;

import java.util.List;

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
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcControllGroupService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.service.fire.HixentArcWarningListService;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/mqttEquip")
public class ApiFireMqttEquipController {

	@Autowired
	private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;

	@Autowired
	private HixentUserService hixentUserService;
	
	@Autowired
    private HixentArcWarningListService hixentArcWarningListService;
	
	@Autowired
	private HixentArcControllGroupService hixentArcaControllGroupService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

	/**
	 * 无线列表 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "查询无线列表")
	@RequestMapping(value = "/getMqttEquipList", method = RequestMethod.POST)
	public ModelMap getMqttEquipList(Integer type, String siteId,
			Integer currentPage, Integer pageSize,String checkgroup,String inquire) {
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
			int deviceType=0;
			int parameterType=0;
			Integer isGroup=0;
			String checkGroup="";
            if(ToolUtil.StringNotBlank(checkgroup)) {
				 isGroup=0;
				 checkGroup="yes";
			}
			
			List<HixentArcEquipmentInfo> equipList = hixentArcaControllGroupService.getEquipList(type, siteId, null,
					pageSize, currentPage, inquire, null,checkGroup,isGroup,parameterType,deviceType);
			
			if(equipList!=null) {
				for (int i = 0; i < equipList.size(); i++) {
					//告警信息
		        	List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(equipList.get(i).getId());
	        		if(wl!=null) {
	        			if( wl.size()>0 ){
		        			equipList.get(i).setIs_alarm(1);
			        		
		        		}else{
		        			equipList.get(i).setIs_alarm(0);
		        		}		
	        		}
		        	
				}	
			}
			
			
			Integer equipListCount = hixentArcaControllGroupService.getEquipListCount(type, siteId, null,inquire, 
					null,checkGroup,isGroup,parameterType,deviceType);
			JSONObject pageMqttEquipListData = new JSONObject();
			
			pageMqttEquipListData.put("total", equipListCount);
			pageMqttEquipListData.put("pageList", equipList);
			return ReturnUtil.Success("获取无线列表数据成功！", pageMqttEquipListData);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	
	/**
	 * 删除无线终端 author RuanYu
	 */
	@Transactional
	//@ApiLimitConfig(count = 1, time = 1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "删除无线终端 ")
	@RequestMapping(value = "/deleteMqttEquipBySiteCode", method = RequestMethod.POST)
	public ModelMap deleteMqttEquipBySiteCode(String siteCode) {
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
				if (userinfo.getRoleType() != 0) {
					return ReturnUtil.Error("你不是超级管理员，无法删除终端！");
				}
               if(!ToolUtil.StringNotBlank(siteCode)) {
            	   return ReturnUtil.Error("请选择终端！");
               }
	
               Integer delMqttEquipBySiteCode = hixentArcEquipmentInfoService.delMqttEquipBySiteCode(siteCode);
				

				if (delMqttEquipBySiteCode>0) {
					String message = "设备删除成功！";
					return ReturnUtil.Success(message);
				} else {
					String message = "设备删除失败！";
					return ReturnUtil.Error(message);
				}

			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}