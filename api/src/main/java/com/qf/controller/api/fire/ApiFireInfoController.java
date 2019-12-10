package com.qf.controller.api.fire;



import java.util.Map;
import java.util.List;
import java.util.HashMap;
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
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.fire.HixentArcLog;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.model.fire.valid.ValidHixentDeviceInfo;
import com.qf.model.fire.valid.ValidHixentWarningInfo;
import com.alibaba.fastjson.JSONObject;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.AppCommonService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.service.fire.HixentArcLogService;
import com.qf.service.fire.HixentArcWarningListService;
import com.qf.util.ReturnUtil;
import com.qf.util.DataParsingUtil;
import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.qf.service.app.HixentAppUserService;
import io.jsonwebtoken.Claims;



@RestController
@RequestMapping("/app/fire/info")
public class ApiFireInfoController {

	@Autowired
	private HixentUserService hixentUserService;
	    
    
    @Autowired
    private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;
    
    @Autowired
    private AppCommonService appCommonService;
    
	@Autowired
    private HixentArcWarningListService hixentArcWarningListService;
    
    @Autowired
    private HixentArcLogService hixentArcLogService;
    
	@Autowired 
	private HixentAppUserService hixentAppUserService; 
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
    

    
    /**
     * 手机App首页相关数据
     * author Vareck
     */ 
    //@RequiresPermissions(value = {"parc_app_all"}) 
    @SystemHistoryAnnotation(opration = "手机App首页相关数据")
    @RequestMapping(value = "/homePageData", method = RequestMethod.POST)
    public ModelMap homePageData(){
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
            for(HixentArcEquipmentInfo el:eList){
            	eidList.add(el.getId());
            }
            //获取未处理设备故障告警列表
        	List<HixentArcWarningList> wList  = hixentArcWarningListService.getAllByDeviceId2(eidList);
            //获取已处理设备故障告警列表
        	List<HixentArcWarningList> wList2 = hixentArcWarningListService.getAllByDeviceId4(eidList);
        	Map<String,Object> jsonMap = new HashMap<>();
	    	jsonMap.put("today_task_num",1);              				   //今日任务数，做法待定
	    	jsonMap.put("equipment_failure_num",wList.size());  		   //存在设备故障数量
	    	jsonMap.put("processed_equipment_failure_num",wList2.size());  //已处理的设备故障数量
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取手机App首页相关数据成功!",json);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    

    
    /**
     * 手机App获取设备详细信息
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"}) 
    @SystemHistoryAnnotation(opration = "手机App获取设备详细信息")
    @RequestMapping(value = "/deviceAppInfo", method = RequestMethod.POST)
    public ModelMap deviceAppInfo(ValidHixentDeviceInfo device, BindingResult bindingResult){
    	try{
	    	if ( bindingResult.hasErrors() ) {
	            return ReturnUtil.Error("参数错误！");
	        }else{
	        	//获取数据
	        	String eid     = device.getEid();
	        	String name    = device.getName();
	        	String address = device.getAddress();
	    	    /*数据权限检验*/
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
	        	//设备权限判断
		        boolean isHave = appCommonService.isHaveDevice(userinfo,eid);
		        if( !isHave ){
		        	return ReturnUtil.Error("没有该设备的权限！");
		        }
	        	//设备详细信息
	        	HixentArcEquipmentInfo info = hixentArcEquipmentInfoService.getOne(eid);  
	            Integer device_type = info.getDeviceType();
	            Integer status      = info.getStatus();
	            String  node        = info.getNode();
	            String  moduleCode  = info.getModuleCode();
	            String  deviceCode  = info.getDeviceCode();
	            String  statusStr;
	            String  deviceStr;
	            if( status>0 ){
	            	statusStr = "在线";
	            }else{
	            	statusStr = "离线";
	            }
	            if( device_type>0 ){
	            	deviceStr = "终端模块";
	            }else{
	            	deviceStr = "NB模块";
	            }
	            //获取心跳包数据
	            Integer n = hixentArcLogService.isTable(eid);
            	float temperature;
            	float electricity;
	            if(n>0){
	            	HixentArcLog log = hixentArcLogService.getOne(eid);
	            	temperature = log.getTemperature();
	            	electricity = log.getElectricity();
	            }else{
	            	temperature = 0;
	            	electricity = 0;
	            }
	        	//数据整合
		    	Map<String,Object> jsonMap = new HashMap<>();
	            jsonMap.put("project_name"    , name);
	            jsonMap.put("project_address" , address);
	            jsonMap.put("eid"        , eid);
	            jsonMap.put("statusStr"  , statusStr);
	            jsonMap.put("deviceStr"  , deviceStr);
	            jsonMap.put("moduleCode" , moduleCode);
	            jsonMap.put("deviceCode" , deviceCode);
	            jsonMap.put("voltageValue" , "220V");
	            jsonMap.put("temperature" , temperature);
	            jsonMap.put("electricity" , electricity);
	            jsonMap.put("node" , node);
	            JSONObject json = new JSONObject(jsonMap);
		    	return ReturnUtil.Success("获取设备详细信息成功!",json);
	        }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 手机App获取设备历史故障详细信息
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_app_all"}) 
    @SystemHistoryAnnotation(opration = "手机App获取设备历史故障详细信息")
    @RequestMapping(value = "/warningAppInfo", method = RequestMethod.POST)
    public ModelMap warningAppInfo(ValidHixentWarningInfo warning, BindingResult bindingResult){
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
        	Integer warningNo = warning.getWarningNo();
        	//获取告警详细信息
        	HixentArcWarningList wInfo = hixentArcWarningListService.getOneByWid(warningNo);
    	    /*数据权限检验*/
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
	        //设备权限判断
	        boolean isHave = appCommonService.isHaveDevice(userinfo,wInfo.getEid());
	        if( !isHave ){
	        	return ReturnUtil.Error("没有该设备的权限！");
	        }
        	/*数据整合*/
        	String warning_index_str;
        	if( wInfo.getWarningIndex()>0 ){
        		warning_index_str = "设备故障报警";
        	}else{
        		warning_index_str = "火灾报警";
        	}
        	String is_deal_str;
        	if( wInfo.getIsDeal()>0 ){
        		is_deal_str = "未处理";
        	}else{
        		is_deal_str = "已处理";
        	}
	    	Map<String,Object> jsonMap = new HashMap<>();
	    	jsonMap.put("deal_time"          , DateUtil.stampToDate(Integer.toString(wInfo.getDealTime())+"000"));
	    	jsonMap.put("warning_time"       ,wInfo.getWarning_time());
	    	jsonMap.put("is_deal_str"        , is_deal_str);
	    	jsonMap.put("warning_index_str"  , warning_index_str);
            jsonMap.put("eid"                , wInfo.getEid());
            jsonMap.put("address"            , wInfo.getAddress());
            jsonMap.put("node"               , wInfo.getNode());
            JSONObject json = new JSONObject(jsonMap);
	    	return ReturnUtil.Success("获取设备历史故障详细信息成功!",json);
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
    @RequestMapping(value = "/getTerminalInfo", method = RequestMethod.POST)
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
	            if( !userArr[0].equals("app") ){
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
    
    
    
}