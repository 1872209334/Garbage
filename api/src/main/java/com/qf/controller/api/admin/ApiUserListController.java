package com.qf.controller.api.admin;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.DataParsingUtil;
import com.qf.util.DateUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

import com.qf.model.Base;
import com.qf.common.JwtConfig;
import com.qf.model.admin.HixentDictionary;
import com.qf.model.admin.HixentRole;

import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentPermissions;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.HixentLog;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentCompany;
import com.qf.model.admin.HixentMessage;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcEquipmentInfoMqtt;
import com.qf.model.fire.HixentArcLog;
import com.qf.model.fire.HixentArcProjectType;
import com.qf.model.fire.HixentArcSiteEquipmentRelevance;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.model.admin.valid.ValidSaveDictionary;
import com.qf.model.admin.valid.ValidHixentRole;
import com.qf.model.admin.valid.ValidHixentPermissions;
import com.qf.model.admin.valid.ValidHixentUserInfo;
import com.qf.model.admin.valid.ValidHixentLog;
import com.qf.model.admin.valid.ValidHixentAppUserMore2;
import com.qf.model.admin.valid.ValidHixentCompany;
import com.qf.model.admin.valid.ValidHixentMessage;
import com.qf.model.fire.valid.ValidHixentBackDeviceList;
import com.qf.model.fire.valid.ValidHixentDeviceInfo;
import com.qf.model.fire.valid.ValidHixentArcEfmDevice;
import com.qf.service.admin.HixentDictionaryService;
import com.qf.service.admin.HixentPermissionsRoleService;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.fire.HixentArcEfmDeviceService;
import com.qf.service.fire.HixentArcEquipmentInfoMqttService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.service.fire.HixentArcProjectTypeService;
import com.qf.service.fire.HixentArcWarningListService;
import com.qf.service.fire.HixentArcSiteEquipmentRelevanceService;
import com.qf.service.admin.HixentLogService;
import com.qf.service.admin.HixentCompanyService;
import com.qf.service.admin.HixentMessageService;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.common.apiLimit.ApiLimitConfig;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.SecurityUtils;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.validation.Valid;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping("/api/user/list")
public class ApiUserListController {

	
    
    @Autowired
    private HixentLogService hixentLogService;
    
    @Autowired
    private HixentDictionaryService hixentDictionaryService;
    
    @Autowired
    private HixentPermissionsRoleService hixentPermissionsRoleService;
    
    @Autowired
    private HixentCompanyService hixentCompanyService;
    
    @Autowired
    private HixentUserService hixentUserService;
    
    @Autowired
    private HixentAppUserService hixentAppUserService;
    
    @Autowired
    private HixentMessageService hixentMessageService;
    
    @Autowired
    private HixentArcEfmDeviceService hixentArcEfmDeviceService;
    
    @Autowired
    private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;

    @Autowired
    private HixentArcEquipmentInfoMqttService hixentArcEquipmentInfoMqttService;

    @Autowired
    private HixentArcWarningListService hixentArcWarningListService;

    @Autowired
    private HixentArcSiteEquipmentRelevanceService hixentArcSiteEquipmentRelevanceService;
    
    @Autowired
    private HixentArcProjectTypeService hixentArcProjectTypeService;

    @Resource
    private RedisUtil redisUtil;
    
    @Resource
    private JwtConfig jwtConfig;
    
    @Resource
    private JwtUtil jwtUtil;

    
    
    /**
     * 用户列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_userList"}) 
    //@SystemHistoryAnnotation(opration = "查看用户列表") 
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    public ModelMap userList( @Valid ValidHixentUserInfo user ){
    	try{
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
            Integer id = userinfo.getId();
            //角色类型
			Integer roleType = hixentUserService.getRoleType(userinfo.getFireRole());
    		//传递的参数
    	    Integer isPage 	 = user.getIsPage();
    	    String  name     = user.getName();
    	    String  order    = user.getOrder();
    	    Integer  limit   = user.getLimit();
    	    Integer  page    = user.getPage();
        	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
        	List<HixentUser> rlist;
        	List<HixentUser> alllist   = hixentUserService.getUserAllList(name,id,roleType);
        	List<HixentUser> dlist;
        	List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
        	if( isPage == 1 ){
            	rlist = hixentUserService.getUserPageList(name,order,limit,page,id,roleType);
            	dlist = rlist;
            	jsonMap.put("total" , alllist.size());
                jsonMap.put("page"  , page);
                jsonMap.put("limit" , limit);
        	}else{
        		dlist = alllist;
        	}
        	
			for(HixentUser dl:dlist){
				List<HixentSite> getsiteList = new ArrayList<HixentSite>();
				
				if(dl.getBid()==null) {
					
				}else {
					
					if(!dl.getBid().equals("0") ) {
						
						//获取项目的详细信息
						
						String[] siteCodeArr = dl.getBid().split(",");
						if(siteCodeArr!=null) {
							getsiteList = hixentUserService.getsiteList(siteCodeArr);
							
						}
				}
				
				 
				
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id"			, dl.getId());
				map.put("uid"			, dl.getUid());
				map.put("account"		, dl.getAccount());
				map.put("mobile"        , dl.getMobile());
				map.put("bid"        , dl.getBid());
				map.put("areaId"        , dl.getAreaId());
				map.put("areaName"        , dl.getAreaName());
				map.put("provinveId"        , dl.getProvinceId());
				map.put("provinceName"        , dl.getProvinceName());
				map.put("getsiteList"        , getsiteList);
				map.put("roleId"        , dl.getFireRole());
				map.put("remark"        , dl.getRemark());
				//获取公司信息
				if( dl.getCid()>0 ){
					if( redisUtil.hasKey("company_name_"+String.valueOf(dl.getCid())) ){   //获取缓存
						map.put("company_name"  , String.valueOf(redisUtil.get("company_name_"+String.valueOf(dl.getCid()))));
					}else{
						HixentCompany hixentCompany = new HixentCompany();
						hixentCompany.setCid(dl.getCid());
						HixentCompany cInfo = hixentCompanyService.selectOne(hixentCompany);
						if( cInfo == null ){
							map.put("company_name"  , "");
						}else{
							map.put("company_name"  , cInfo.getName());
							redisUtil.set("company_name_"+String.valueOf(dl.getCid()),cInfo.getName());
							redisUtil.set("company_logo_"+String.valueOf(dl.getCid()),cInfo.getLogoUrl());
						}
					}
				}else{
					map.put("company_name"  , "");
				}
                //获取角色信息
				if( dl.getFireRole()>0 ){
					HixentRole role_info = hixentPermissionsRoleService.selectRoleInfo(dl.getFireRole());
					map.put("fire_role_name" , role_info.getRoleName());
				}
				
				long date1 = dl.getCreatedAt();
				long date2 = dl.getUpdatedAt();
				map.put("created_at" , DateUtil.longToString(date1, "yyyy-MM-dd HH:mm:ss"));
				map.put("updated_at" , DateUtil.longToString(date2, "yyyy-MM-dd HH:mm:ss"));
				userList.add(map);
			}
            //发送数据
			jsonMap.put("user_list" , userList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取用户列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * App用户列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_appUserList"}) 
    //@SystemHistoryAnnotation(opration = "查看App用户列表") 
    @RequestMapping(value = "/userAppList", method = RequestMethod.POST)
    public ModelMap userAppList( @Valid ValidHixentAppUserMore2 user ){
    	try{
    		//传递的参数
    	    String   name    = user.getName();
    	    Integer  statek  = user.getStatek();
    	    String   mobile  = user.getMobile();
    	    String   order   = user.getOrder();
    	    Integer  limit   = user.getLimit();
    	    Integer  page    = user.getPage();
    	    //获取用户
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
    	    Integer    userId2  = userinfo.getId();
    	    Integer    rid   	= userinfo.getFireRole();
    	    
    	     //查询角色类型
			Integer roleType = hixentUserService.getRoleType(rid);
			
        	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
	    	
	    	
	    	List<HixentAppUser> dlist = hixentAppUserService.getAppUserPageList(statek,name,order,limit,page,roleType,userId2 );
	    	Integer appUserPageCount = hixentAppUserService.getAppUserPageCount(statek,name,roleType,userId2 );
        	List<Map<String,Object>> appUserList = new ArrayList<Map<String,Object>>();
			for(HixentAppUser dl:dlist){
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id"			, dl.getId());
				map.put("uid"			, dl.getUid());
				map.put("account"		, dl.getAccount());
				map.put("mobile"        , dl.getMobile());
				map.put("email"         , dl.getEmail());
				map.put("state"         , dl.getState());
				map.put("remark"         , dl.getRemark());
				//获取公司信息
				if( dl.getCid()>0 ){
					if( redisUtil.hasKey("company_name_"+String.valueOf(dl.getCid())) ){   //获取缓存
						map.put("company_name"  , String.valueOf(redisUtil.get("company_name_"+String.valueOf(dl.getCid()))));
					}else{
						HixentCompany hixentCompany = new HixentCompany();
						hixentCompany.setCid(dl.getCid());
						HixentCompany cInfo = hixentCompanyService.selectOne(hixentCompany);
						if( cInfo == null ){
							map.put("company_name"  , "");
						}else{
							map.put("company_name"  , cInfo.getName());
							redisUtil.set("company_name_"+String.valueOf(dl.getCid()),cInfo.getName());
							redisUtil.set("company_logo_"+String.valueOf(dl.getCid()),cInfo.getLogoUrl());
						}
					}
				}else{
					map.put("company_name"  , "");
				}
				//获取日期
				long date1 = dl.getCreatedAt();
				long date2 = dl.getUpdatedAt();
				map.put("created_at" , DateUtil.longToString(date1, "yyyy-MM-dd HH:mm:ss"));
				map.put("updated_at" , DateUtil.longToString(date2, "yyyy-MM-dd HH:mm:ss"));
				appUserList.add(map);
			}
            //发送数据
        	jsonMap.put("total" , appUserPageCount);
            jsonMap.put("page"  , page);
            jsonMap.put("limit" , limit);
			jsonMap.put("appUserList" , appUserList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取App用户列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 站内信列表
     * author Vareck
     */
    //@SystemHistoryAnnotation(opration = "查看站内信列表") 
    @RequestMapping(value = "/messageList", method = RequestMethod.POST)
    public ModelMap messageList( @Valid ValidHixentMessage message ){
    	try{
    		//传递的参数
    	    String   userName = message.getUserName();
    	    String   mobile   = message.getMobile();
    	    String   order    = message.getOrder();
    	    Integer  type     = message.getType();
    	    Integer  state    = message.getState();
    	    Integer  limit    = message.getLimit();
    	    Integer  page     = message.getPage();
    		//获取用户信息
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
        	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
	    	List<HixentMessage> rlist;
	    	List<HixentMessage> alllist;
	    	if( type==0 ){
	        	rlist    = hixentMessageService.getPageList(userinfo.getId(),userName,mobile,state,limit,page,order);
	        	alllist  = hixentMessageService.getSelectList(userinfo.getId(),userName,mobile,state);
	    	}else{
	        	rlist    = hixentMessageService.getPageList2(userinfo.getId(),mobile,state,limit,page,order);
	        	alllist  = hixentMessageService.getSelectList2(userinfo.getId(),mobile,state);
	    	}
            jsonMap.put("total" , alllist.size());
            jsonMap.put("page"  , page);
            jsonMap.put("limit" , limit);
            jsonMap.put("Message_list" , rlist);
            //发送数据
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取站内信列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 角色列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_roleList"}) 
    //@SystemHistoryAnnotation(opration = "查看角色列表")
    @RequestMapping(value = "/roleList", method = RequestMethod.POST)
    public ModelMap roleList( @Valid ValidHixentRole role ){
    	try{
    		//传递的参数
    	    Integer isPage 	   = role.getIsPage();
    	    String  name       = role.getName();
    	    String  order      = role.getOrder();
    	    Integer  limit     = role.getLimit();
    	    Integer  page      = role.getPage();
        	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
        	List<HixentRole> rlist;
        	List<HixentRole> alllist   = hixentPermissionsRoleService.getRoleAllList(name);
        	if( isPage == 1 ){
            	rlist = hixentPermissionsRoleService.getRolePageList(name,order,limit,page);
                jsonMap.put("total" , alllist.size());
                jsonMap.put("page"  , page);
                jsonMap.put("limit" , limit);
                jsonMap.put("role_list" , rlist);
        	}else{
                jsonMap.put("role_list" , alllist);
        	}
            //发送数据
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取角色列表成功",json);
    	} catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
        }
    }

    
    
    /**
     * 系统后台告警列表
     * author wjr
     */
    @ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "系统后台告警列表")
    @RequestMapping(value = "/fireWarningList", method = RequestMethod.POST)
    public ModelMap fireWarningList(Base base){
    	try{
        	//获取上传数据
    	    Integer isPage = base.getIsPage();
    	    Integer limit  = base.getLimit();
    	    Integer page   = base.getPage();
    	    //获取用户信息
        	//HixentUser userinfo  = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
	        long time1,time2;
	        if(request.getParameter("time1") == null || request.getParameter("time1").contentEquals("")){
	        	time1 = 0;
	        }
	        else{
	        	String t1 = request.getParameter("time1");
	        	time1 = DateUtil.dateToStamp(t1,"yyyy-MM-dd");
	        }
	        if(request.getParameter("time2") == null|| request.getParameter("time2").contentEquals("")){	
	        	time2 = 0;
	        }
	        else{
	        	String t2 = request.getParameter("time2");
	        	time2 = DateUtil.dateToStamp(t2,"yyyy-MM-dd");
	        }
	        Map<String,Object> jsonMap = new HashMap<>();
	        if(userinfo.getBid() == null || userinfo.getBid().equals("")){
              	jsonMap.put("warning_list" , "");
              	jsonMap.put("total" , 0);
            }
	        //bid=0为超级管理员设备权限，获取全部站点设备
        	else if(userinfo.getBid().equals("0")){
        		List<HixentArcWarningList> wList = hixentArcWarningListService.getAdminWarning(time1,time2);
        		List<HixentArcWarningList> wpList = hixentArcWarningListService.getPageAdminWarning(limit,page,time1,time2);
        		jsonMap.put("total" , wList.size()); 
	            jsonMap.put("warning_list" , wpList);
        	}
        	else{
        		String bStr = userinfo.getBid().trim();
            	String[] bid  =  bStr.split(",");
            	Set allDid    =  new HashSet();
    			for (int i = 0; i < bid.length; i++) {
    				allDid.add(bid[i]);
    			}
        		List<HixentArcWarningList> wList = hixentArcWarningListService.getCommonWarning(time1,time2,allDid);
        		List<HixentArcWarningList> wpList = hixentArcWarningListService.getPageCommonWarning(limit,page,time1,time2,allDid);
        		jsonMap.put("total" , wList.size()); 
	            jsonMap.put("warning_list" , wpList);
        	}
//	        //设备权限判断（站点）
//	        String bStr   =  userinfo.getBid().trim();
//	        if( bStr.equals("0") || bStr.equals("") ){
//	        	return ReturnUtil.Error("没有站点分组设备权限！");
//	        }
//	  
//	        String[] bid  =  bStr.split(",");
//			Set allDid    =  new HashSet();
//	        List<HixentArcSiteEquipmentRelevance> sidList = hixentArcSiteEquipmentRelevanceService.getDataByBid(bid);
//        	for(HixentArcSiteEquipmentRelevance al:sidList){
//        		allDid.add(al.getDid());
//        	}
//	        List<String> eidList = new ArrayList<>(); 
//    		//获取设备列表
//	    	List<HixentArcEquipmentInfo> eList = hixentArcEquipmentInfoService.getAllDeviceListByBid(allDid,null);
//            for(HixentArcEquipmentInfo el:eList){
//            	eidList.add(el.getId());
//            }
//            //获取告警列表
//        	List<HixentArcWarningList> wList = hixentArcWarningListService.getAllByDeviceId(eidList);
//        	List<HixentArcWarningList> tList = hixentArcWarningListService.getPageByDeviceId(eidList,limit,page);
//        	//数据整合
//	    	Map<String,Object> jsonMap = new HashMap<>();
//	    	if( isPage==0 ){
//	    		int n = 0;
//	    		for(HixentArcWarningList wl:wList){
//	    			if(wl.getIsDeal()==0){
//	    				n++;
//	    			}
//	    		}
//	            jsonMap.put("no_total" , n);          //为完成的条数
//	            jsonMap.put("warning_list" , wList);
//	    	}else{
//	            jsonMap.put("total" , wList.size());
//	            jsonMap.put("page"  , page);
//	            jsonMap.put("limit" , limit);
//	            jsonMap.put("warning_list" , tList);
//	    	}
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("告警列表获取成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    

    
    /**
     * 公司列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_companyList"}) 
    //@ApiLimitConfig(count=60,time=60000)
    //@SystemHistoryAnnotation(opration = "查看公司列表")
    @RequestMapping(value = "/companyList", method = RequestMethod.POST)
    public ModelMap companyList( @Valid ValidHixentCompany company ){
    	try{
    		//传递的参数
    	    Integer isPage 	   = company.getIsPage();
    	    String  name       = company.getName();
    	    String  order      = company.getOrder();
    	    Integer  limit     = company.getLimit();
    	    Integer  page      = company.getPage();
        	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
        	List<HixentCompany> rlist;
        	List<HixentCompany> alllist   = hixentCompanyService.getCompanyAllList(name);
        	if( isPage == 1 ){
            	rlist = hixentCompanyService.getCompanyPageList(name,order,limit,page);
                jsonMap.put("total" , alllist.size());
                jsonMap.put("page"  , page);
                jsonMap.put("limit" , limit);
                jsonMap.put("company_list" , rlist);
        	}else{
                jsonMap.put("company_list" , alllist);
        	}
            //发送数据
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取公司列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * efm6000设备表
     * author wjr
     */
    @ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "查看efm6000设备列表")
    @RequestMapping(value = "/efmDeviceList", method = RequestMethod.POST)
    public ModelMap efmDeviceList( @Valid ValidHixentArcEfmDevice device ){
    	try{
    		//传递的参数
    	    Integer  isPage 	= device.getIsPage();
    	    String   siteCode   = device.getSiteCode();
    	    String   deviceCode = device.getDeviceCode();
    	    String   order      = device.getOrder();
    	    Integer  limit      = device.getLimit();
    	    Integer  page       = device.getPage();
    	    //获取用户
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
	    	Map<String,Object> jsonMap = new HashMap<>();
        	if(userinfo.getBid() == null || userinfo.getBid().equals("")){
              	jsonMap.put("efm_device_list" , "");
              	jsonMap.put("total" , 0);
            }
	        //bid=0为超级管理员设备权限，获取全部站点设备
        	else if(userinfo.getBid().equals("0")){
            	List<HixentArcEfmDevice> adminlist = hixentArcEfmDeviceService.getAllAdminDeviceList();
            	//字符型字段16进制转字符串操作
            	for(HixentArcEfmDevice efmOne:adminlist){
            		//描述符
            		efmOne.setSpecificator(formatDevStr(efmOne.getSpecificator()));
              		efmOne.setModel(formatDevStr(efmOne.getModel()));
              		efmOne.setSerial_number(formatDevStr(efmOne.getSerial_number()));
              		efmOne.setLongitute(formatDevStr(efmOne.getLongitute()));
              		efmOne.setLatitude(formatDevStr(efmOne.getLatitude()));
              		efmOne.setVersion(formatDevStr(efmOne.getVersion()));
              		efmOne.setMessage_phone(formatDevStr(efmOne.getMessage_phone()));
              		efmOne.setPhone_number1(formatDevStr(efmOne.getPhone_number1()));
              		efmOne.setPhone_number2(formatDevStr(efmOne.getPhone_number2()));
              		efmOne.setPhone_number3(formatDevStr(efmOne.getPhone_number3()));
              		efmOne.setPhone_number4(formatDevStr(efmOne.getPhone_number4()));
              		efmOne.setPhone_number5(formatDevStr(efmOne.getPhone_number5()));
              		efmOne.setReport_number(formatDevStr(efmOne.getReport_number()));
            	}
            	jsonMap.put("efm_device_list" , adminlist);
            	jsonMap.put("total" , adminlist.size());
           
            }
            //非超级管理员权限
            else{
            	String bStr = userinfo.getBid().trim();
            	String[] bid  =  bStr.split(",");
            	Set allDid    =  new HashSet();
    			for (int i = 0; i < bid.length; i++) {
    				allDid.add(bid[i]);
    			}
    			 //数据整合
            	List<HixentArcEfmDevice> rlist;
            	List<HixentArcEfmDevice> alllist = hixentArcEfmDeviceService.getAllDeviceList(allDid,siteCode, deviceCode);
            	for(HixentArcEfmDevice efmOne:alllist){
            		//描述符
            		efmOne.setSpecificator(formatDevStr(efmOne.getSpecificator()));
              		efmOne.setModel(formatDevStr(efmOne.getModel()));
              		efmOne.setSerial_number(formatDevStr(efmOne.getSerial_number()));
              		efmOne.setLongitute(formatDevStr(efmOne.getLongitute()));
              		efmOne.setLatitude(formatDevStr(efmOne.getLatitude()));
              		efmOne.setVersion(formatDevStr(efmOne.getVersion()));
              		efmOne.setMessage_phone(formatDevStr(efmOne.getMessage_phone()));
              		efmOne.setPhone_number1(formatDevStr(efmOne.getPhone_number1()));
              		efmOne.setPhone_number2(formatDevStr(efmOne.getPhone_number2()));
              		efmOne.setPhone_number3(formatDevStr(efmOne.getPhone_number3()));
              		efmOne.setPhone_number4(formatDevStr(efmOne.getPhone_number4()));
              		efmOne.setPhone_number5(formatDevStr(efmOne.getPhone_number5()));
              		efmOne.setReport_number(formatDevStr(efmOne.getReport_number()));
            	}
                jsonMap.put("efm_device_list" , alllist);
                jsonMap.put("total" , alllist.size());
            }
            //发送数据
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取efm6000设备列表成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * efm6000主机详情
     * author wjr
     */
    //@ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "查看efm6000主机详情")
    @RequestMapping(value = "/efmDeviceInfo", method = RequestMethod.POST)
    public ModelMap efmDeviceInfo(){
    	try{
    	    //获取用户
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
	      
	        //传递的参数
    		String efmId = request.getParameter("efmId");
        	String lineCode = request.getParameter("lineCode");
        	String pages = request.getParameter("page");
        	int page = Integer.parseInt(pages);
        	String limits = request.getParameter("limit");
        	if(limits == null){
        		limits = "10";
        	}
        	if(limits.equals("")){
        		limits = "10";
        	}
       
        	int limit = Integer.parseInt(limits);
        	List<HixentArcEfmDevice> efmInfo = hixentArcEfmDeviceService.getEfmDeviceInfo(efmId);
        	for(HixentArcEfmDevice efmOne:efmInfo){
        		efmOne.setSpecificator(formatDevStr(efmOne.getSpecificator()));
          		efmOne.setModel(formatDevStr(efmOne.getModel()));
          		efmOne.setSerial_number(formatDevStr(efmOne.getSerial_number()));
          		efmOne.setLongitute(formatDevStr(efmOne.getLongitute()));
          		efmOne.setLatitude(formatDevStr(efmOne.getLatitude()));
          		efmOne.setVersion(formatDevStr(efmOne.getVersion()));
          		efmOne.setMessage_phone(formatDevStr(efmOne.getMessage_phone()));
          		efmOne.setPhone_number1(formatDevStr(efmOne.getPhone_number1()));
          		efmOne.setPhone_number2(formatDevStr(efmOne.getPhone_number2()));
          		efmOne.setPhone_number3(formatDevStr(efmOne.getPhone_number3()));
          		efmOne.setPhone_number4(formatDevStr(efmOne.getPhone_number4()));
          		efmOne.setPhone_number5(formatDevStr(efmOne.getPhone_number5()));
          		efmOne.setReport_number(formatDevStr(efmOne.getReport_number()));
        	}
       
        	Map<String,Object> jsonMap = new HashMap<>();
        	jsonMap.put("efm_device_info" , efmInfo);
        	//int count = hixentArcEquipmentInfoService.getEfmTerminalCount(efmId,lineCode);
        	List<HixentArcEquipmentInfo> aList = hixentArcEquipmentInfoService.getAllEfmTerminalList(efmId,lineCode);
        	List<HixentArcEquipmentInfo> tList = hixentArcEquipmentInfoService.getPageEfmTerminalList(efmId,lineCode,limit,page);
        	jsonMap.put("total" , aList.size());
            List<Map<String,Object>> dListMap = new ArrayList<Map<String,Object>>();
            for(HixentArcEquipmentInfo dl:tList){
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("id", dl.getId());
        		map.put("address", dl.getAddress());
        		map.put("module_code", dl.getModuleCode());
        		map.put("line_code", dl.getLineCode());
        		map.put("device_code", dl.getDeviceCode());
        		map.put("site_code", dl.getSiteCode());
        		map.put("lng_bmap", dl.getLngBmap());
        		map.put("lat_bmap", dl.getLatBmap());
        		map.put("addr", dl.getAddr());
        		map.put("message", formatDevStr(dl.getMessage()));
        		//告警信息
	        	List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(dl.getId());
        		if( wl.size()>0 ){
	        		map.put("is_warning", 1);
        		}else{
	        		map.put("is_warning", 0);
        		}
	        	dListMap.add(map);
        	}
            jsonMap.put("device_list" , dListMap);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取设备终端列表成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 中控设备表
     * author Vareck
     */
    // @ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "获取终端设备列表")
    @RequestMapping(value = "/allDeviceList", method = RequestMethod.POST)
    public ModelMap allDeviceList( @Valid ValidHixentBackDeviceList device ){
    	try{
    		//传递的参数
    	    Integer  isPage  = device.getIsPage();
    	    Integer  efmId 	 = device.getEfmId();
    	    String lineCode = device.getLineCode();
    	    String   order   = device.getOrder();
    	    Integer  limit   = device.getLimit();
    	    Integer  page    = device.getPage();
    	    //获取用户
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
	        Map<String,Object> jsonMap = new HashMap<>();
	        if(userinfo.getBid() == null || userinfo.getBid().equals("")){
              	jsonMap.put("efm_device_list" , "");
              	jsonMap.put("total" , 0);
            }
	        //bid=0为超级管理员设备权限，获取全部站点设备
        	else if(userinfo.getBid().equals("0")){
        		List<HixentArcEquipmentInfo> aList = hixentArcEquipmentInfoService.getAllMqttDeviceList();
        		List<HixentArcEquipmentInfo> pList = hixentArcEquipmentInfoService.getPageMqttDeviceList(limit,page);
          
            	List<Map<String,Object>> dListMap = new ArrayList<Map<String,Object>>();
            	for(HixentArcEquipmentInfo dl:pList){
            		Map<String,Object> map = new HashMap<String,Object>();
            		map.put("id", dl.getId());
            		map.put("address", dl.getAddress());
            		map.put("module_code", dl.getModuleCode());
            		map.put("line_code", dl.getLineCode());
            		map.put("device_code", dl.getDeviceCode());
            		map.put("site_code", dl.getSiteCode());
            		map.put("lng_bmap", dl.getLngBmap());
            		map.put("lat_bmap", dl.getLatBmap());
            		map.put("addr", dl.getAddr());
            		map.put("message", DataParsingUtil.toStringHex(dl.getMessage()));
            		//告警信息
    	        	List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(dl.getId());
            		if( wl.size()>0 ){
    	        		map.put("is_warning", 1);
            		}else{
    	        		map.put("is_warning", 0);
            		}
    	        	dListMap.add(map);
            	}
                jsonMap.put("device_list" , dListMap);
            	//jsonMap.put("mqtt_device_list" , pList);
            	jsonMap.put("total" , aList.size());
        	}
        	else{
        		String bStr = userinfo.getBid().trim();
            	String[] bid  =  bStr.split(",");
            	Set allDid    =  new HashSet();
    			for (int i = 0; i < bid.length; i++) {
    				allDid.add(bid[i]);
    			}
    			List<HixentArcEquipmentInfo> alllist = hixentArcEquipmentInfoService.getCommonMqttList(allDid);
    			List<HixentArcEquipmentInfo> plist = hixentArcEquipmentInfoService.getPageCommonMqttList(allDid,limit,page);
              
                List<Map<String,Object>> dListMap = new ArrayList<Map<String,Object>>();
            	for(HixentArcEquipmentInfo dl:plist){
            		Map<String,Object> map = new HashMap<String,Object>();
            		map.put("id", dl.getId());
            		map.put("address", dl.getAddress());
            		map.put("module_code", dl.getModuleCode());
            		map.put("line_code", dl.getLineCode());
            		map.put("device_code", dl.getDeviceCode());
            		map.put("site_code", dl.getSiteCode());
            		map.put("lng_bmap", dl.getLngBmap());
            		map.put("lat_bmap", dl.getLatBmap());
            		map.put("addr", dl.getAddr());
            		map.put("message", DataParsingUtil.toStringHex(dl.getMessage()));
            		//告警信息
    	        	List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(dl.getId());
            		if( wl.size()>0 ){
    	        		map.put("is_warning", 1);
            		}else{
    	        		map.put("is_warning", 0);
            		}
    	        	dListMap.add(map);
            	}
                jsonMap.put("device_list" , dListMap);
                //jsonMap.put("mqtt_device_list" , plist);
                jsonMap.put("total" , alllist.size());
        	}
//	        //设备权限判断（站点）
//	        String bStr   =  userinfo.getBid().trim();
//	        if( bStr.equals("0") || bStr.equals("") ){
//	        	//return ReturnUtil.Error("没有站点分组设备权限！");
//	        }
//	        String[] bid  =  bStr.split(",");
//	        if( bid.length==0 ){
//	        	//return ReturnUtil.Error("没有站点分组设备权限！");
//	        }
//			Set allDid    =  new HashSet();
//	        List<HixentArcSiteEquipmentRelevance> sidList = hixentArcSiteEquipmentRelevanceService.getDataByBid(bid);
//        	for(HixentArcSiteEquipmentRelevance al:sidList){
//        		allDid.add(al.getDid());
//        	}
//    		//获取列表
//	    	List<HixentArcEquipmentInfo> tList = hixentArcEquipmentInfoService.getAllDeviceListByBid(allDid,efmId);
//        	List<HixentArcEquipmentInfo> dList = hixentArcEquipmentInfoService.getPageDeviceListByBid(allDid,efmId,limit,page,order,lineCode);
//        	//数据整合
//	    	Map<String,Object> jsonMap = new HashMap<>();
//	    	List<HixentArcEquipmentInfo> aList;
//        	if( isPage == 1 ){
//                jsonMap.put("total" , tList.size());
//                jsonMap.put("page"  , page);
//                jsonMap.put("limit" , limit);
//                aList = dList;
//        	}else{
//        		aList = tList;
//        	}
        	//获取有效数据
//        	List<Map<String,Object>> dListMap = new ArrayList<Map<String,Object>>();
//        	for(HixentArcEquipmentInfo dl:plist){
//        		Map<String,Object> map = new HashMap<String,Object>();
//        		map.put("id", dl.getId());
//        		map.put("address", dl.getAddress());
//        		map.put("module_code", dl.getModuleCode());
//        		map.put("line_code", dl.getLineCode());
//        		map.put("device_code", dl.getDeviceCode());
//        		map.put("site_code", dl.getSiteCode());
//        		map.put("lng_bmap", dl.getLngBmap());
//        		map.put("lat_bmap", dl.getLatBmap());
//        		map.put("addr", dl.getAddr());
//        		map.put("message", DataParsingUtil.toStringHex(dl.getMessage()));
//        		//告警信息
//	        	List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(dl.getId());
//        		if( wl.size()>0 ){
//	        		map.put("is_warning", 1);
//        		}else{
//	        		map.put("is_warning", 0);
//        		}
//	        	dListMap.add(map);
//        	}
//            jsonMap.put("device_list" , dListMap);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取设备列表成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 权限列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_permissionsList"}) 
    //@SystemHistoryAnnotation(opration = "查看权限列表")
    @RequestMapping(value = "/permissionsList", method = RequestMethod.POST)
    public ModelMap permissionsList( @Valid ValidHixentPermissions permissions ){
    	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = requestAttributes.getRequest();  
	   String servletPath = request.getServletPath();
	   
	   
    	try{
    		//传递的参数
    	    Integer isPage 	   = permissions.getIsPage();
    	    String  name       = permissions.getName();
    	    String  order      = permissions.getOrder();
    	    Integer  limit     = permissions.getLimit();
    	    Integer  page      = permissions.getPage();
        	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
        	List<HixentPermissions> rlist;
        	List<HixentPermissions> alllist   = hixentPermissionsRoleService.getPermissionsAllList(name);
        	if( isPage == 1 ){
            	rlist = hixentPermissionsRoleService.getPermissionsPageList(name,order,limit,page);
                jsonMap.put("total" , alllist.size());
                jsonMap.put("page"  , page);
                jsonMap.put("limit" , limit);
                jsonMap.put("Permissions_list" , rlist);
        	}else{
                jsonMap.put("Permissions_list" , alllist);
        	}
            //发送数据
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取权限列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }

    
    
    /**
     * 获取select下拉框数据
     * author Vareck
     */
    @RequestMapping(value = "/getAllSelectData", method = RequestMethod.GET)
    //@SystemHistoryAnnotation(opration = "获取select下拉框数据")
    public ModelMap getAllSelectData(){
    	if( !redisUtil.hasKey("fire_dictionary_info") ){
        	Map<String,Object> jsonMap = new HashMap<>();
            /*数据字典*/
            List<HixentDictionary> dictionaryGroupList = hixentDictionaryService.selectGroupData();               //分类数据
            for( HixentDictionary gl:dictionaryGroupList ){	
            	List<HixentDictionary> dictionaryList = hixentDictionaryService.selectAllData(gl.getTypename());  //分类下所有数据
            	HashMap<String,String> data = new HashMap<String,String>();
            	for( HixentDictionary dl:dictionaryList ){	
            		data.put( dl.getDkey() ,dl.getDvalue() );
            	}
            	jsonMap.put( gl.getTypename() , data );
    		}
            //转json数据
            JSONObject json = new JSONObject(jsonMap);
            redisUtil.set("fire_dictionary_info",json.toString());
            return ReturnUtil.Success("获取select下拉框数据成功",json);
    	}else{
    		JSONObject json = JSON.parseObject(String.valueOf(redisUtil.get("fire_dictionary_info")));
    		return ReturnUtil.Success("获取select下拉框数据成功",json);
    	}
    }

    
    
    /**
     * 数据字典列表
     * author Vareck
     */
    //@RequiresRoles(value = {"rarc_1"})
    @SystemHistoryAnnotation(opration = "使用数据字典列表")
    @RequestMapping(value = "/dictionaryList", method = RequestMethod.POST)
    public ModelMap dictionaryList( @Valid ValidSaveDictionary dictionary, BindingResult bindingResult ){
    	try{
    		//获取总数
	    	List<HixentDictionary> tList = hixentDictionaryService.getSelectList(dictionary.getTypename(),dictionary.getDkey(),dictionary.getDvalue());
    		//获取列表
	    	List<HixentDictionary> dList = hixentDictionaryService.getPageList(dictionary.getTypename(),dictionary.getDkey(),dictionary.getDvalue(),dictionary.getLimit(),dictionary.getPage(),dictionary.getOrder());
            //数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("total" , tList.size());
            jsonMap.put("page"  , dictionary.getPage());
            jsonMap.put("limit" , dictionary.getLimit());
            jsonMap.put("dictionary_list" , dList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取数据字典列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }

    
    
    /**
     * 系统日志列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_systemLogList"}) 
    @RequestMapping(value = "/logList", method = RequestMethod.POST)
    //@SystemHistoryAnnotation(opration = "查看系统日志列表")
    public ModelMap logList( @Valid ValidHixentLog log ){
    	try{
            //传递的参数
            String  username    = log.getUsername().trim();
    	    String  opration    = log.getOpration().trim();
    	    String  controller  = log.getController().trim();
    	    String  method	    = log.getMethod().trim();
    	    String  ip		    = log.getIp().trim();
    	    Integer isException = log.getIsException();
    	    String  time1;
    	    String  time2;
    	    if( log.getTime1().trim().equals("") ){
        	    time1  = null;
    	    }else{
        	    time1  = log.getTime1().trim()+" 00:00:00";
    	    }
    	 
    	    if( log.getTime2().trim().equals("") ){
        	    time2  = null;
    	    }else{
        	    time2  = log.getTime2().trim()+" 23:59:59";
    	    }
    	    Integer limit      = log.getLimit();
    	    Integer page       = log.getPage();
    		//获取用户信息
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
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
	        Integer roleType = userinfo.getRoleType();
	         Integer id = userinfo.getId();
	         
	         List<HixentUser> userList = new ArrayList<HixentUser>();
        	/*用户名集合*/
        	Set<String>  userNameArray = new HashSet<>();  
        	//检索自己的日志
        	userNameArray.add(userinfo.getAccount());       
        	
         	
        	userList = hixentUserService.getUserAllList(roleType,id,null);
        	
        	for(HixentUser dl:userList){
    			userNameArray.add(dl.getAccount());
    		}
        	/*所有数据整合*/
        	//从主表中读取有哪些分表
        	Set<String>  dateArray = new HashSet<>(); 
        	List<HixentLog> mList  = hixentLogService.mainLogAllList();
        	for(HixentLog ml:mList){
        		dateArray.add(ml.getParams());
        	}
           
    		//分表数据
    		List<HixentLog> pList = hixentLogService.getLogPageList(isException,username,opration,controller,method,ip,time1,time2,limit,page,userNameArray,dateArray);
    		List<HixentLog> logAllList = hixentLogService.getLogAllList(isException,username,opration,controller,method,ip,time1,time2,userNameArray,dateArray);
    		
    		for (int i = 0; i < pList.size(); i++) {
    			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			Date date = fmt.parse(pList.get(i).getCreatedAt());//将数据库出的 timestamp 类型的时间转换为java的Date类型  
    			String s = fmt.format(date);
    			pList.get(i).setCreatedAt(s);
			}
    		//发送数据
	    	Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("total" , logAllList.size());
            jsonMap.put("page"  , log.getPage());
            jsonMap.put("limit" , log.getLimit());
            jsonMap.put("log_list" , pList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取日志列表成功",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    /**
     * 获取终端设备信息详情
     * author wjr
     */
    // @ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "获取终端设备信息详情")
    @RequestMapping(value = "/getDeviceInfo", method = RequestMethod.POST)
    public ModelMap getDeviceInfo( @Valid ValidHixentDeviceInfo device ,BindingResult bindingResult){
    	try{
	    	if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("参数错误！");
	        }else{
	        	//获取数据
	        	String eid     = device.getEid();
	    	    /*数据权限检验*/
	    	    //获取用户信息
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
	        	//设备详细信息
	        	HixentArcEquipmentInfo info = hixentArcEquipmentInfoService.getOne(eid);  
	        	String line_code = info.getLineCode();
	        	String addr = info.getAddr();
	            String type      = info.getType();
	            String subType = info.getSubtype();
	            String version = info.getVersion();
	            String sens = info.getSens();
	            String erelay = info.getErelay();
	            String tempration = info.getTempration();
	            String trelay = info.getTrelay();
	            String time = info.getTime();
	            String cnt = info.getCnt();
	            String message = DataParsingUtil.toStringHex(info.getMessage());
	            String temp_alarm_en = info.getTempAlarmEn();
	            String arc_alarm_en = info.getArcAlarmEn();
	            String alive_alarm_en = info.getAliveEn();
	        	//数据整合
		    	Map<String,Object> jsonMap = new HashMap<>();
	            jsonMap.put("line_code"    , line_code);
	            jsonMap.put("addr" , addr);
	            jsonMap.put("type"    , type);
	            jsonMap.put("subType" , subType);
	            jsonMap.put("version"    , version);
	            jsonMap.put("sens" , sens);
	            jsonMap.put("erelay"    , erelay);
	            jsonMap.put("tempration" , tempration);
	            jsonMap.put("trelay" , trelay);
	            jsonMap.put("time"    , time);
	            jsonMap.put("cnt" , cnt);
	            jsonMap.put("message"    , message);
	            jsonMap.put("temp_alarm_en" , temp_alarm_en);
	            jsonMap.put("arc_alarm_en" , arc_alarm_en);
	            jsonMap.put("alive_alarm_en" , alive_alarm_en);
	            JSONObject json = new JSONObject(jsonMap);
		    	return ReturnUtil.Success("获取设备详细信息成功!",json);
	        }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    private String  formatDevStr(String str){
    	if(!str.equals("")){
    		return DataParsingUtil.toStringHex(str);
		}
    	return str;
    }
    
    /**
     * 获取站点列表
     * author wjr
     */
    //@ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "获取站点列表")
    @RequestMapping(value = "/getSiteList", method = RequestMethod.POST)
    public ModelMap getSiteList(){
    	try{
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
        	
        
        	String areaId = (String) redisUtil.get("areaId_"+userinfo.getUid());
			String provinceId= (String) redisUtil.get("provinceId_"+userinfo.getUid());
			int areaIdInt =Integer.parseInt(areaId);
			int provinceIdInt =Integer.parseInt(provinceId);
			String bid = userinfo.getBid();
			if(!ToolUtil.StringNotBlank(bid)) {
				return ReturnUtil.Success("无数据!");
			}
			String[] siteCordArr = bid.split(",");
			List<HixentSite> getsiteList = hixentUserService.getsite(provinceIdInt, areaIdInt,siteCordArr);
			
			JSONObject outjson = new JSONObject();
			if(getsiteList.size()==0) {
				return ReturnUtil.Success("无数据!");
			} 
			outjson.put("getsiteList", getsiteList);
	    	return ReturnUtil.Success("获取站点列表成功!",outjson);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ReturnUtil.Success("获取站点列表异常，原因是："+e.getMessage());
        }
    }
    /**
     * 通过项目获取中控
     * author wjr
     */
    //@ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    @SystemHistoryAnnotation(opration = "通过项目获取中控")
    @RequestMapping(value = "/getDeviceBySite", method = RequestMethod.POST)
    public ModelMap getDeviceBySite(String siteId){
    	try{
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
        	
        
    
			//获取中控数据
			List<HixentArcEfmDevice> deviceBySite = hixentUserService.getDeviceBySite(siteId);
	    	for (int i = 0; i < deviceBySite.size(); i++) {
	    		HixentArcEfmDevice hixentArcEfmDevice = deviceBySite.get(i);
	    		hixentArcEfmDevice.setSpecificator(formatDevStr(hixentArcEfmDevice.getSpecificator()));
	    		hixentArcEfmDevice.setModel(formatDevStr(hixentArcEfmDevice.getModel()));
	    		hixentArcEfmDevice.setSerial_number(formatDevStr(hixentArcEfmDevice.getSerial_number()));
	    		hixentArcEfmDevice.setLongitute(formatDevStr(hixentArcEfmDevice.getLongitute()));
	    		hixentArcEfmDevice.setLatitude(formatDevStr(hixentArcEfmDevice.getLatitude()));
	      		hixentArcEfmDevice.setVersion(formatDevStr(hixentArcEfmDevice.getVersion()));
	      		hixentArcEfmDevice.setMessage_phone(formatDevStr(hixentArcEfmDevice.getMessage_phone()));
	      		hixentArcEfmDevice.setPhone_number1(formatDevStr(hixentArcEfmDevice.getPhone_number1()));
	      		hixentArcEfmDevice.setPhone_number2(formatDevStr(hixentArcEfmDevice.getPhone_number2()));
	      		hixentArcEfmDevice.setPhone_number3(formatDevStr(hixentArcEfmDevice.getPhone_number3()));
	      		hixentArcEfmDevice.setPhone_number4(formatDevStr(hixentArcEfmDevice.getPhone_number4()));
	      		hixentArcEfmDevice.setPhone_number5(formatDevStr(hixentArcEfmDevice.getPhone_number5()));
	      		hixentArcEfmDevice.setReport_number(formatDevStr(hixentArcEfmDevice.getReport_number()));
			
	    	}
	    	
	 
	    	//查询项目是否有无线终端
	    	String mqttEquipBySite = hixentArcEquipmentInfoService.getMqttEquipBySite(siteId);
			if(ToolUtil.StringNotBlank(mqttEquipBySite)) {
				
				HixentArcEfmDevice haed = new HixentArcEfmDevice();
		    	haed.setNiName("无线终端");
		    	deviceBySite.add(haed);
			}
	    	
	    	
			return ReturnUtil.Success("获取中控列表成功!",deviceBySite);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * 获取虚拟分组列表
     * author wjr
     */
    // @ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
    public ModelMap getProjectList(){
    	try{
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
        	String pages = request.getParameter("page");
        	int page = Integer.parseInt(pages);
        	String limits = request.getParameter("limit");
        	if(limits == null){
        		limits = "20";
        	}
        	if(limits.equals("")){
        		limits = "20";
        	}
        	int limit = Integer.parseInt(limits);
        	List<HixentArcProjectType> aList = hixentArcProjectTypeService.getProjectSize(userinfo.getId());
        	List<HixentArcProjectType> pList = hixentArcProjectTypeService.getPageProject(userinfo.getId(),limit,page);
        	Map<String,Object> jsonMap = new HashMap<>();  
        	jsonMap.put("total" , aList.size()); 
            jsonMap.put("project_list" , pList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取分组列表成功!",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 获取项目详情
     * author wjr
     */
    //@ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    @SystemHistoryAnnotation(opration = "获取项目详情")
    @RequestMapping(value = "/getSiteInfo", method = RequestMethod.POST)
    public ModelMap getSiteInfo(Integer siteId){
    	try{
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
	        if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
        	if(siteId==null || siteId<0) {
        		return ReturnUtil.Error("请选择站点！");
        	}
        	
			
			JSONObject sizeInfo = hixentArcEfmDeviceService.getSizeInfo(siteId);
            
	    	return ReturnUtil.Success("获取站点详情成功!",sizeInfo);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取终端设备信息详情
     * author ywh
     */
    @ApiLimitConfig(count=60,time=60000)
    //@RequiresPermissions(value = {"parc_device"}) 
    //@SystemHistoryAnnotation(opration = "获取终端设备信息详情")
    @RequestMapping(value = "/getDeviceInfoMqtt", method = RequestMethod.POST)
    public ModelMap getDeviceInfoMqtt( @Valid ValidHixentDeviceInfo device ,BindingResult bindingResult){
    	try{
	    	if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("参数错误！");
	        }else{
	        	//获取数据
	        	String eid       = device.getEid();
	    	    /*数据权限检验*/
	    	    //获取用户信息
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest 		 request = requestAttributes.getRequest();
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
	        	//设备详细信息
	        	HixentArcEquipmentInfoMqtt info = hixentArcEquipmentInfoMqttService.getOne(eid);  
	        	String line_code 		= info.getLineCode();
	        	String addr 	 		= info.getAddr();
	            String type      		= info.getType();
	            String subType   		= info.getSubtype();
	            String version   		= info.getVersion();
	            String sens      		= info.getSens();
	            String erelay    		= info.getErelay();
	            String tempration		= info.getTempration();
	            String trelay    		= info.getTrelay();
	            String time      		= info.getTime();
	            String cnt       		= info.getCnt();
	            String message   		= DataParsingUtil.toStringHex(info.getMessage());
	            String temp_alarm_en  	= info.getTempAlarmEn();
	            String arc_alarm_en   	= info.getArcAlarmEn();
	            String alive_alarm_en 	= info.getAliveEn();

	            System.out.println(" getDeviceInfoMqtt subType  "+subType);
	            //System.out.println(" getDeviceInfoMqtt subType  "+subType);

	        	//数据整合
		    	Map<String,Object> jsonMap = new HashMap<>();
	            jsonMap.put("line_code"    	, line_code);
	            jsonMap.put("addr" 			, addr);
	            jsonMap.put("type"    		, type);
	            jsonMap.put("subtype" 		, subType);
	            jsonMap.put("version"    	, version);
	            jsonMap.put("sens" 			, sens);
	            jsonMap.put("erelay"    	, erelay);
	            jsonMap.put("tempration" 	, tempration);
	            jsonMap.put("trelay" 		, trelay);
	            jsonMap.put("time"    		, time);
	            jsonMap.put("cnt" 			, cnt);
	            jsonMap.put("message"    	, message);
	            jsonMap.put("temp_alarm_en" , temp_alarm_en);
	            jsonMap.put("arc_alarm_en" 	, arc_alarm_en);
	            jsonMap.put("alive_en", alive_alarm_en);
	            JSONObject json = new JSONObject(jsonMap);
		    	return ReturnUtil.Success("获取设备详细信息成功!",json);
	        }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
   

    
}