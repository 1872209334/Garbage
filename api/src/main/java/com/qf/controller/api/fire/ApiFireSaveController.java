package com.qf.controller.api.fire;



import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;

import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.fire.HixentArcProjectEquipmentRelevance;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.valid.ValidHixentDeviceSave;
import com.qf.service.fire.HixentArcProjectEquipmentRelevanceService;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.fire.HixentArcEquipmentInfoService;



@RestController
@RequestMapping("/api/fire/save")
public class ApiFireSaveController {

	
	
    @Autowired
    private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;
    
    @Autowired
    private HixentArcProjectEquipmentRelevanceService hixentArcProjectEquipmentRelevanceService;
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
    
	@Autowired 
	private HixentAppUserService hixentAppUserService; 
	
	
    
    /**
     * 添加/编辑设备信息
     * author Vareck
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = "添加/编辑设备信息")
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    public ModelMap device( @Valid ValidHixentDeviceSave device, BindingResult bindingResult ){
    	try{
	    	if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("参数错误！");
	        }else{
	    	    //获取用户
		        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("app") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	        	HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
	        	if(userinfo == null){
	        		
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
		        //整理数据
	        	HixentArcEquipmentInfo data = new HixentArcEquipmentInfo();
		    	data.setId(device.getId());
		    	data.setModuleCode(device.getModuleCode());
		    	data.setLineCode(device.getLineCode());
		    	data.setDeviceCode(device.getDeviceCode());
		    	data.setSiteCode(device.getSiteCode());
		    	data.setAddress(device.getAddress());
		    	data.setDeviceIp(device.getDeviceIp());
		    	data.setNode(device.getNode());
		    	data.setCityId(device.getCityId());
		    	data.setProvinceId(device.getProvinceId());
		    	data.setAreaId(device.getAreaId());
		    	data.setStatus(device.getStatus());
		    	data.setIsEffect(device.getIsEffect());
		    	data.setDeviceType(device.getDeviceType());
		    	data.setRegisterTime(Integer.valueOf(Long.toString(DateUtil.getTimes()/1000)));
		    	data.setReportTime(device.getReportTime());
		    	data.setLngBmap(device.getLngBmap());
		    	data.setLatBmap(device.getLatBmap());
		    	String message = "";
		    	//操作
		        if ( device.getDid() == 0 ) {
			    	//有线设备(默认值做测试)
		        	data.setArcAlarmEn("0");
		        	data.setTempAlarmEn("0");
		        	data.setAliveEn("0");
		        	data.setAdminId(userinfo.getId());
		        	data.setEfm_id("0");
		        	data.setType("");
		        	data.setSubtype("");
		        	data.setVersion("");
		        	data.setSens("");
		        	data.setErelay("");
		        	data.setTempration("");
		        	data.setTrelay("");
		        	data.setTime("");
		        	data.setCnt("");
		        	data.setMessage("");
		        	data.setAddr("");
		        	//添加设备数据
		        	hixentArcEquipmentInfoService.insert(data);
		        	//添加设备和虚拟分组管理数据
		        	HixentArcProjectEquipmentRelevance pData = new HixentArcProjectEquipmentRelevance();
		        	pData.setEid(device.getId());
		        	pData.setPid(device.getProjectId());
		        	hixentArcProjectEquipmentRelevanceService.insert(pData);
		        	message = "添加设备成功！";
		        }else{
		        	//编辑
		            Example example = new Example(HixentArcEquipmentInfo.class);
		            example.createCriteria().andCondition("id=",device.getId());
		            hixentArcEquipmentInfoService.update(data, example);
		            message = "修改设备成功！";
		        }
	        	return ReturnUtil.Success(message);
	        }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
}