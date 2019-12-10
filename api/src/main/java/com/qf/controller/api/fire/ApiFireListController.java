package com.qf.controller.api.fire;



import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.Base;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcProjectType;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.model.fire.valid.ValidHixentDeviceList;
import com.qf.model.fire.valid.ValidHixentWarningHistoryList;
import com.alibaba.fastjson.JSONObject;
import com.qf.service.fire.HixentArcProjectTypeService;
import com.qf.service.fire.HixentArcWarningListService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.service.app.AppCommonService;
import com.qf.util.ReturnUtil;
import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.qf.service.app.HixentAppUserService;
import io.jsonwebtoken.Claims;



@RestController
@RequestMapping("/app/fire/list")
public class ApiFireListController {


	
    @Autowired
    private AppCommonService appCommonService;
    
    @Autowired
    private HixentArcProjectTypeService hixentArcProjectTypeService;
    
	@Autowired
    private HixentArcWarningListService hixentArcWarningListService;

    @Autowired
    private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
    
	@Autowired 
	private HixentAppUserService hixentAppUserService; 
    
    
    
    /**
     * 手机APP虚拟分组信息列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"}) 
    @SystemHistoryAnnotation(opration = "获取手机APP虚拟分组信息列表")
    @RequestMapping(value = "/projectTypeMapList", method = RequestMethod.POST)
    public ModelMap projectTypeMapList(){
    	try{
    		//获取App用户
        	//HixentAppUser userinfo = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
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
	        String pStr   = userinfo.getProjectId();
	        String[] pid  = pStr.split(",");
    		//获取列表
	    	List<HixentArcProjectType> tList = hixentArcProjectTypeService.getAllByProjectId(pid);
            //数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("total" , tList.size());
            jsonMap.put("project_type_list" , tList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取手机APP虚拟分组地图信息列表",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    

    /**
     * 手机APP火灾告警列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"}) 
    @SystemHistoryAnnotation(opration = "手机APP火灾告警列表")
    @RequestMapping(value = "/fireWarningAppList", method = RequestMethod.POST)
    public ModelMap fireWarningAppList(Base base){
    	try{
        	//获取上传数据
    	    Integer limit = base.getLimit();
    	    Integer page  = base.getPage();
    	    //获取用户信息
        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
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
	        String pStr  = userinfo.getProjectId();
	        String[] pid = pStr.split(",");
	        List<String> eidList = new ArrayList<>(); 
    		//获取设备列表
	    	List<HixentArcEquipmentInfo> eList = hixentArcEquipmentInfoService.getDeviceListByProjectId(pid);
            //for(HixentArcEquipmentInfo el:eList){
            //	eidList.add(el.getId());
            //}
        	int idx = 0;			    	
	    	for (Iterator<HixentArcEquipmentInfo> it = eList.iterator(); it.hasNext(); idx++) {
	    		HixentArcEquipmentInfo o = it.next();
	    	    // ...
	    		if( o != null )
	    		{
	    			//System.out.println( o.getId() );
	    			eidList.add(o.getId());
	    		}
	    	}

            //获取告警列表
        	List<HixentArcWarningList> wList = hixentArcWarningListService.getAllByDeviceId(eidList);
        	List<HixentArcWarningList> tList = hixentArcWarningListService.getPageByDeviceId(eidList,limit,page);
	    	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("total" , wList.size());
            jsonMap.put("page"  , page);
            jsonMap.put("limit" , limit);
            jsonMap.put("warning_list" , tList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("火灾告警列表获取成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }

    
    
    /**
     * 手机APP设备故障告警列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"}) 
    @SystemHistoryAnnotation(opration = "手机APP设备故障告警列表")
    @RequestMapping(value = "/deviceWarningAppList", method = RequestMethod.POST)
    public ModelMap deviceWarningAppList(Base base){
    	try{
        	//获取上传数据
    	    Integer limit = base.getLimit();
    	    Integer page  = base.getPage();
    	    //获取用户信息
        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
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
    	    if( userinfo == null ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
	        String pStr  = userinfo.getProjectId();
	        String[] pid = pStr.split(",");
	        List<String> eidList = new ArrayList<>(); 
    		//获取设备列表
	    	List<HixentArcEquipmentInfo> eList = hixentArcEquipmentInfoService.getDeviceListByProjectId(pid);
            //for(HixentArcEquipmentInfo el:eList){
            //	eidList.add(el.getId());
            //}
        	int idx = 0;			    	
	    	for (Iterator<HixentArcEquipmentInfo> it = eList.iterator(); it.hasNext(); idx++) {
	    		HixentArcEquipmentInfo o = it.next();
	    	    // ...
	    		if( o != null )
	    		{
	    			//System.out.println( o.getId() );
	    			eidList.add(o.getId());
	    		}
	    	}

            //获取告警列表
        	List<HixentArcWarningList> wList = hixentArcWarningListService.getAllByDeviceId2(eidList);
        	List<HixentArcWarningList> tList = hixentArcWarningListService.getPageByDeviceId2(eidList,limit,page);
	    	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("total" , wList.size());
            jsonMap.put("page"  , page);
            jsonMap.put("limit" , limit);
            jsonMap.put("warning_list" , tList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("设备故障告警列表获取成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 手机app设备异常历史记录列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"})
    @SystemHistoryAnnotation(opration = "手机app设备异常历史记录列表")
    @RequestMapping(value = "/warningHistoryAppList", method = RequestMethod.POST)
    public ModelMap warningHistoryAppList(ValidHixentWarningHistoryList warning, BindingResult bindingResult){
    	try{
    		//上传数据验证
	    	if ( bindingResult.hasErrors() ) {
	            String message = "";
	            List<FieldError> list = bindingResult.getFieldErrors();
	            for (int i = 0; i < list.size(); i++) {
	            	message += list.get(i).getDefaultMessage()+";";
	            }
	            return ReturnUtil.Error(message);
	        }
        	//获取上传数据
	        Integer  pid   	   = warning.getPid();
    	    Integer  limit     = warning.getLimit();
    	    Integer  page      = warning.getPage();
    	    String   startTime = warning.getStartTime();
    	    String   endTime   = warning.getEndTime(); 
    	    Integer  time1;
    	    Integer  time2;
    	    if( startTime.equals("") ){
    	    	time1 = Integer.parseInt(Long.toString(DateUtil.getTimeNumberToday()));   
    	    }else{
    	    	time1 = Integer.parseInt(Long.toString(DateUtil.dateToStamp(startTime,"yyyy-MM-dd")/1000));
    	    }
    	    if( startTime.equals("") ){
    	    	time2 = time1+86400;
    	    }else{
    	    	time2 = Integer.parseInt(Long.toString(DateUtil.dateToStamp(endTime,"yyyy-MM-dd")/1000));
    	    }
    	    if( time1>time2 ){
	        	return ReturnUtil.Error("检索的结束时间不小于开始时间！");
    	    }
    	    //获取用户信息
        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
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
	        //虚拟分组设备权限判断
	        boolean isHave = appCommonService.isHaveDeviceType(userinfo, pid);
	        if( !isHave ){
	        	return ReturnUtil.Error("没有该虚拟分组的权限！");
	        }
	        /*获取数据*/
    		//获取设备列表
	        List<String> eidList = new ArrayList<>(); 
	    	List<HixentArcEquipmentInfo> eList = hixentArcEquipmentInfoService.getAllDeviceList(pid);
            //for(HixentArcEquipmentInfo el:eList){
            //	eidList.add(el.getId());
            //}
        	int idx = 0;			    	
	    	for (Iterator<HixentArcEquipmentInfo> it = eList.iterator(); it.hasNext(); idx++) {
	    		HixentArcEquipmentInfo o = it.next();
	    	    // ...
	    		if( o != null )
	    		{
	    			//System.out.println( o.getId() );
	    			eidList.add(o.getId());
	    		}
	    	}

            //获取告警列表
        	List<HixentArcWarningList> wList = hixentArcWarningListService.getAllByDeviceId3(eidList,time1,time2);
        	List<HixentArcWarningList> tList = hixentArcWarningListService.getPageByDeviceId3(eidList,time1,time2,limit,page);
	    	//数据整合
	    	Map<String,Object> jsonMap = new HashMap<>();
            jsonMap.put("total" , wList.size());
            jsonMap.put("page"  , page);
            jsonMap.put("limit" , limit);
            jsonMap.put("warning_list" , tList);
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取设备异常历史记录列表成功！",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 手机APP终端设备列表
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"})
    @SystemHistoryAnnotation(opration = "获取手机APP终端设备列表")
    @RequestMapping(value = "/deviceAppList", method = RequestMethod.POST)
    public ModelMap deviceAppList(ValidHixentDeviceList device, BindingResult bindingResult){
    	try{
	    	if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("请选择虚拟分组！");
	        }else{
	        	
	        	//获取上传数据
		        Integer  pid   	= device.getPid();
	    	    String   order  = device.getOrder();
	    	    Integer  limit  = device.getLimit();
	    	    Integer  page   = device.getPage();
	    	    //获取用户信息
	        	//HixentAppUser userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
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
		        //虚拟分组设备权限判断
		        boolean isHave = appCommonService.isHaveDeviceType(userinfo, pid);
		        if( !isHave ){
		        	return ReturnUtil.Error("没有该虚拟分组的权限！");
		        }

	        	//虚拟分组数据
	        	HixentArcProjectType pinfo = hixentArcProjectTypeService.getOne(pid);
		        String  address = pinfo.getAddress();
		        String  name    = pinfo.getName();
	    		//获取列表
		    	List<HixentArcEquipmentInfo> tList = hixentArcEquipmentInfoService.getAllDeviceList(pid);
	        	List<HixentArcEquipmentInfo> dList = hixentArcEquipmentInfoService.getPageDeviceList(pid,limit,page,order);
	        	//获取有效数据
	        	List<Map<String,Object>> dListMap = new ArrayList<Map<String,Object>>();
	        	for(HixentArcEquipmentInfo dl:dList){
	        		Map<String,Object> map = new HashMap<String,Object>();
	        		map.put("id", dl.getId());
	        		map.put("module_code", dl.getModuleCode());
	        		map.put("device_code", dl.getDeviceCode());
	        		//告警信息
		        	List<HixentArcWarningList> wl = hixentArcWarningListService.getByDeviceId(dl.getId());
	        		if( wl.size()>0 ){
		        		map.put("is_warning", 1);
	        		}else{
		        		map.put("is_warning", 0);
	        		}
		        	dListMap.add(map);
	        	}
	        	
	        	//统计告警数量
		        //List<String> eidList = new ArrayList<>(); 
	        	//for(HixentArcEquipmentInfo tl:tList){
	            //	eidList.add(tl.getId());
	        	//}
	        	
	        	List<String> eidList = new ArrayList<>(); 
	        	int idx = 0;			    	
		    	for (Iterator<HixentArcEquipmentInfo> it = tList.iterator(); it.hasNext(); idx++) {
		    		HixentArcEquipmentInfo o = it.next();
		    	    // ...
		    		if( o != null )
		    		{
		    			//System.out.println( o.getId() );
		    			eidList.add(o.getId());
		    		}
		    	}


	        	List<HixentArcWarningList> wll = hixentArcWarningListService.getAllByDeviceId(eidList);

	        	//数据整合
		    	Map<String,Object> jsonMap = new HashMap<>();
	            jsonMap.put("total" , tList.size());
	            jsonMap.put("page"  , page);
	            jsonMap.put("limit" , limit);
	            jsonMap.put("project_id" , pid);              //项目id
	            jsonMap.put("project_address" , address);     //项目地址
	            jsonMap.put("project_name" , name);           //项目名称
	            jsonMap.put("warning_total" , wll.size());    //告警数量
	            jsonMap.put("device_list" , dListMap);
	            JSONObject json = new JSONObject(jsonMap);
	            
		    	return ReturnUtil.Success("获取设备列表成功！",json);
	        }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
}