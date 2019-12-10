package com.qf.controller.api.fire;



import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.qf.model.fire.HixentArcSite;
import com.qf.service.fire.impl.HixentArcSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcControlGroup;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.valid.ValidHixentControllGroup;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcControllGroupService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.util.JwtUtil;
import com.qf.util.PageUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;



@RestController
@RequestMapping("/api/controlGroup")
public class ApiFireControlGroupController {

	@Autowired
	private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;
	
    @Autowired
    private HixentArcControllGroupService hixentArcaControllGroupService;
 
    @Autowired
	private HixentUserService hixentUserService;
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
	private HixentArcSiteService hixentArcSiteService;
    
    @Resource
    private RedisUtil redisUtil;

	/**
	 * 新建终端
	 * author zhangjun
	 */
	@Transactional
	// @ApiLimitConfig(count=1,time=1000)
	//@RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = " 新建终端")
	@RequestMapping(value = "/newDevice", method = RequestMethod.POST)
	public ModelMap newDevice( String siteCode,String deviceType ,String equipCode){
		try{
			if ( siteCode =="" ) {
				return ReturnUtil.Error("所属项目编码不能为空！");
			}else{
				//获取用户信息
				//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();
				String auth      = request.getHeader(jwtConfig.getJwtHeader());
				auth             = auth.substring(7, auth.length());
				Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
				String userId    = claims.getId();
				String[] userArr = userId.split("_");
				if( !userArr[0].equals("admin") ){
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
				if(userinfo == null){
					return ReturnUtil.Error("已退出，请重新登录！");
				}
//				Integer adminId = userinfo.getId();
//				String uid = controllerGoup.getUid();
//				Integer siteId = controllerGoup.getSiteId();
//				Integer currentPage = controllerGoup.getCurrentPage();
//				Integer pageSize = controllerGoup.getPageSize();
//				PageUtil<HixentArcControlGroup> groupInfo = hixentArcaControllGroupService.getGroupInfo(uid, siteId, adminId,currentPage,pageSize);
//				HixentArcSite hixentArcSite = new HixentArcSite();
//				hixentArcSite.setSiteName(siteName);
//				hixentArcSite.setSitePlace(sitePlace);
//				hixentArcSite.setSiteCode("11111111");
				hixentArcSiteService.insertNewDevice(siteCode,deviceType,equipCode);
				return ReturnUtil.Success("新建终端数据成功！");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 新建项目
	 * author zhangjun
	 */
	@Transactional
	// @ApiLimitConfig(count=1,time=1000)
	//@RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = " 新建项目")
	@RequestMapping(value = "/newProject", method = RequestMethod.POST)
	public ModelMap newProject( String siteName,String sitePlace ){
		try{
			if ( siteName =="" ) {
				return ReturnUtil.Error("项目名称不能为空！");
			}else{
				//获取用户信息
				//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();
				String auth      = request.getHeader(jwtConfig.getJwtHeader());
				auth             = auth.substring(7, auth.length());
				Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
				String userId    = claims.getId();
				String[] userArr = userId.split("_");
				if( !userArr[0].equals("admin") ){
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
				if(userinfo == null){
					return ReturnUtil.Error("已退出，请重新登录！");
				}
//				Integer adminId = userinfo.getId();
//				String uid = controllerGoup.getUid();
//				Integer siteId = controllerGoup.getSiteId();
//				Integer currentPage = controllerGoup.getCurrentPage();
//				Integer pageSize = controllerGoup.getPageSize();
//				PageUtil<HixentArcControlGroup> groupInfo = hixentArcaControllGroupService.getGroupInfo(uid, siteId, adminId,currentPage,pageSize);
				HixentArcSite hixentArcSite = new HixentArcSite();
				hixentArcSite.setSiteName(siteName);
				hixentArcSite.setSitePlace(sitePlace);
				hixentArcSite.setSiteCode("11111111");
				hixentArcSiteService.insertNewProject(hixentArcSite);
				return ReturnUtil.Success("新建项目数据成功！");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


    /**
     * 查询管控分组
     * author RuanYu
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = " 查询管控分组")
    @RequestMapping(value = "/groupInfo", method = RequestMethod.POST)
    public ModelMap device( @Valid ValidHixentControllGroup controllerGoup, BindingResult bindingResult ){
    	try{
	    	if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("参数错误！");
	        }else{
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
	    		if(userinfo == null){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	    		Integer adminId = userinfo.getId();
	    		String uid = controllerGoup.getUid();
	    		Integer siteId = controllerGoup.getSiteId();
	    		Integer currentPage = controllerGoup.getCurrentPage();
	    		Integer pageSize = controllerGoup.getPageSize();
	    		PageUtil<HixentArcControlGroup> groupInfo = hixentArcaControllGroupService.getGroupInfo(uid, siteId, adminId,currentPage,pageSize);
	            	
	    		return ReturnUtil.Success("获取管控分组数据成功！", groupInfo);
	        }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
   
    /**
     * 新建管控分组
     * author RuanYu
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = " 新建管控分组")
    @RequestMapping(value = "/saveGroup", method = RequestMethod.POST)
    public ModelMap addGroup(String appUserList,String equipList,Integer siteId,String place,Integer id ){
    	try{
	    	
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
	    		if(userinfo == null){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	    		Integer adminId = userinfo.getId();
	    	
	    		String[] appUserArr = appUserList.split(",");
    			String[] equipArr = equipList.split(",");
	    		if(id==null ||id==0 ) {
	    			//新建
	    			
	    			boolean addNewControllerGroup = hixentArcaControllGroupService.addNewControllerGroup(appUserArr, equipArr, siteId, place, adminId);
	    			if(addNewControllerGroup) {
	    				return ReturnUtil.Success("新增管控分组成功！");
	    			}
	    		}else {
	    			//修改
	    			boolean upadateControllerGroup = hixentArcaControllGroupService.upadateControllerGroup(appUserArr, equipArr, siteId, place, adminId,id);
	    			return ReturnUtil.Success("编辑管控分组数据成功！");
	    		}
	    		return ReturnUtil.Error("内容填写完整！");
	    		
	       
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 新建管控分组加载数据
     * author RuanYu
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = "新建管控分组加载数据")
    @RequestMapping(value = "/addGroupInitData", method = RequestMethod.POST)
    public ModelMap addGroupInitData(){
    	try{
	    	
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
	    		if(userinfo == null){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	    		//获取用户
	    		Integer adminId = userinfo.getId();
	    		List<HixentAppUser> appUserList = hixentArcaControllGroupService.getAppUserList(adminId);
	    		//获取项目
	    		String areaId = (String) redisUtil.get("areaId_"+userinfo.getUid());
				String provinceId= (String) redisUtil.get("provinceId_"+userinfo.getUid());
				int areaIdInt =Integer.parseInt(areaId);
				int provinceIdInt =Integer.parseInt(provinceId);
				
				String bid = userinfo.getBid();
				String[] siteCordArr = bid.split(",");
	    		List<HixentSite> getsiteList = hixentUserService.getsite(provinceIdInt, areaIdInt, siteCordArr);
	    		
	    		 JSONObject json = new JSONObject();
	    		 json.put("appUserList", appUserList);
	    		 json.put("getsiteList", getsiteList);
	    		return ReturnUtil.Success("获取app用户和项目数据成功！", json);
	      
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 联动出中控和终端
     * author RuanYu
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = "项目联动出中控和终端")
    @RequestMapping(value = "/getDeviceAndEquipList", method = RequestMethod.POST)
    public ModelMap getDeviceAndEquipList(String siteId,String deviceId,Integer page,Integer limit,Integer type){
    	try{
	    	
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
	    		if(userinfo == null){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	    		if(!ToolUtil.StringNotBlank(siteId)) {
	    			return ReturnUtil.Error("请选择项目！");
	    		}
	    		int deviceType=1;
	    		List<HixentArcEquipmentInfo> equipList = hixentArcaControllGroupService.getEquipList(type,siteId, deviceId, limit, page,null,null,"yes",0,0,deviceType);
	    		
	    		Integer equipListCount = hixentArcaControllGroupService.getEquipListCount(type,siteId, deviceId,null,null,"yes",0,0,deviceType);
	    		List<HixentArcEfmDevice> deviceBySiteId = hixentArcaControllGroupService.getDeviceBySiteId(siteId);
	    		//查询项目是否有无线终端
		    	String mqttEquipBySite = hixentArcEquipmentInfoService.getMqttEquipBySite(siteId);
				if(ToolUtil.StringNotBlank(mqttEquipBySite)) {
					
					HixentArcEfmDevice haed = new HixentArcEfmDevice();
					haed.setId("0");
			    	haed.setNiName("无线终端");
			    	deviceBySiteId.add(haed);
				}
	    		
	    		JSONObject json = new JSONObject();
	    		 json.put("equipList", equipList);
	    		 json.put("equipListCount", equipListCount);
	    		 json.put("deviceBySiteId", deviceBySiteId);
	    		return ReturnUtil.Success("获取中控和终端数据成功！", json);
	      
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    //删除管控分组
    /**
     * 删除管控分组
     * author RuanYu
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = "删除管控分组")
    @RequestMapping(value = "/delGroup", method = RequestMethod.POST)
    public ModelMap delGroup(Integer id){
    	try{
	    	
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
	    		if(userinfo == null){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	    		boolean delGroup = hixentArcaControllGroupService.delGroup(id);
	    		if(delGroup) {
	    			return ReturnUtil.Success("删除管控分组成功！");
	    		}else {
	    			return ReturnUtil.Error("删除管控分组失败！");
	    		}
	    		
	      
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 编辑分组显示已有设备
     * author RuanYu
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_saveDevice"}) 
    @SystemHistoryAnnotation(opration = "编辑分组显示已有设备")
    @RequestMapping(value = "/selHadEquip", method = RequestMethod.POST)
    public ModelMap selHadEquip(Integer id,Integer page,Integer limit){
    	try{
	    	
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
	            if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
	            }
	            HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
	    		if(userinfo == null){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	    		PageUtil<HixentArcEquipmentInfo> equipListByGroupId = hixentArcaControllGroupService.getEquipListByGroupId(id,page,limit);
	    		return ReturnUtil.Success("获取某分组数据成功！",equipListByGroupId);
	      
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
}