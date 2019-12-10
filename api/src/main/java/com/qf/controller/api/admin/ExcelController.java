package com.qf.controller.api.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qf.model.fire.HixentArcZipperInfo;
import com.qf.service.fire.HixentArcZipperInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.model.admin.HixentProvince;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcAlarmDealFeedBack;
import com.qf.model.fire.HixentArcIndex;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcAlarmLogService;
import com.qf.service.fire.HixentArcIndexService;
import com.qf.util.DateUtil;
import com.qf.util.ExcelUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

/**
 * Created by zouLu on 2017-12-14.
 */

@RestController
public class ExcelController {
	@Autowired
	private HixentArcAlarmLogService hixentArcAlarmLogService;
	
	@Autowired
	private HixentArcIndexService hixentArcIndexService;
	
	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private HixentUserService hixentUserService;

	@Autowired
	private HixentArcZipperInfoService hixentArcZipperInfoService;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	* author zhangjun
    * @return
	* @throws Exception
     */
	@RequestMapping(value = "/excelForOffLineLogByNewParam", method = RequestMethod.POST)
	public ModelMap excelForOffLineLogByNewParam(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) throws Exception {

		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletResponse  response = servletRequestAttributes.getResponse();

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

		String[] files = new String[3];
		files[1] = "xlsx";
		files[0] = files[2] = "Sheel1";
		/*表样式开关(0表示关1表示开，多表用逗号隔开)*/
		String[] excelSwitch = new String[4];
		//是否有单元格合并
		excelSwitch[0] = "0";
		//文字水平居*（1居中2居左3居右）
		excelSwitch[1] = "1";
		//文字垂直居*（1居中2居左3居右）
		excelSwitch[2] = "1";
		//表格背景颜色
		excelSwitch[3] = "0";

		/*样式相关详细参数*/
		Map<String ,String> map = new HashMap<String ,String>();

		//表头部数据
		List<List<String>> titleData = new ArrayList<List<String>>();
		List<String> x = new ArrayList<String>();
		x.add("项目");
		x.add("设备id");
		x.add("离线时间");
//		x.add("故障类型");
		x.add("设备类型");

		titleData.add(x);

		String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
		String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
		int areaIdInt = Integer.parseInt(areaId);
		int provinceIdInt = Integer.parseInt(provinceId);
		String bid = userinfo.getBid();
		String[] siteCordArr = bid.split(",");

		List<HixentArcZipperInfo> alarmLog = new ArrayList<HixentArcZipperInfo>();
		JSONObject alarmLogForEquipExcel = hixentArcZipperInfoService.offLineLogExcel(projectId, deviceId, isAlarm, pageSize, currentPage);
		alarmLog=(List<HixentArcZipperInfo>)alarmLogForEquipExcel.get("alarmLogForDevice");


		//表数据
		List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
		List<List<String>> datak         = new ArrayList<List<String>>();
		for(HixentArcZipperInfo wl:alarmLog){
			List<String> y = new ArrayList<String>();

			if(ToolUtil.StringNotBlank(wl.getSiteName())) {

				y.add(wl.getSiteName());
			}else{
				y.add("");
			}
			if(ToolUtil.StringNotBlank(wl.getDeviceId())) {
				y.add(wl.getDeviceId());
			}else {
				y.add("");
			}
			if(ToolUtil.StringNotBlank(wl.getCreateTime().toString())){
				y.add(wl.getCreateTime().toString());
			}else {
				y.add("");
			}
			if(ToolUtil.StringNotBlank(wl.getType())){
				y.add(wl.getType());
			}else {
				y.add("");
			}
			datak.add(y);
		}
		allData.add(datak);
		//文件导出
		ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
		return ReturnUtil.Success("离线日志导出成功！");
	}

    /**
     * author zhangjun
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/excelForFaultLogByNewParam", method = RequestMethod.POST)
    public ModelMap excelForFaultLogByNewParam(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) throws Exception {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse  response = servletRequestAttributes.getResponse();

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

        String[] files = new String[3];
        files[1] = "xlsx";
        files[0] = files[2] = "Sheel1";
        /*表样式开关(0表示关1表示开，多表用逗号隔开)*/
        String[] excelSwitch = new String[4];
        //是否有单元格合并
        excelSwitch[0] = "0";
        //文字水平居*（1居中2居左3居右）
        excelSwitch[1] = "1";
        //文字垂直居*（1居中2居左3居右）
        excelSwitch[2] = "1";
        //表格背景颜色
        excelSwitch[3] = "0";

        /*样式相关详细参数*/
        Map<String ,String> map = new HashMap<String ,String>();

        //表头部数据
        List<List<String>> titleData = new ArrayList<List<String>>();
        List<String> x = new ArrayList<String>();
        x.add("项目");
        x.add("设备id");
        x.add("故障时间");
        x.add("故障类型");
        x.add("设备类型");

        titleData.add(x);

        String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
        String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
        int areaIdInt = Integer.parseInt(areaId);
        int provinceIdInt = Integer.parseInt(provinceId);
        String bid = userinfo.getBid();
        String[] siteCordArr = bid.split(",");

        List<HixentArcZipperInfo> alarmLog = new ArrayList<HixentArcZipperInfo>();
        JSONObject alarmLogForEquipExcel = hixentArcZipperInfoService.faultLogExcel(projectId, deviceId, isAlarm, pageSize, currentPage);
        alarmLog=(List<HixentArcZipperInfo>)alarmLogForEquipExcel.get("alarmLogForDevice");


        //表数据
        List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
        List<List<String>> datak         = new ArrayList<List<String>>();
        for(HixentArcZipperInfo wl:alarmLog){
            List<String> y = new ArrayList<String>();

            if(ToolUtil.StringNotBlank(wl.getSiteName())) {

                y.add(wl.getSiteName());
            }else{
                y.add("");
            }
            if(ToolUtil.StringNotBlank(wl.getDeviceId())) {
                y.add(wl.getDeviceId());
            }else {
                y.add("");
            }
            if(ToolUtil.StringNotBlank(wl.getCreateTime().toString())){
                y.add(wl.getCreateTime().toString());
            }else {
                y.add("");
            }

            if("1".equals(wl.getFaultType())){
                y.add("运转故障");
            }else if("2".equals(wl.getFaultType())){
                y.add("启动故障");
            }else {
                y.add("");
            }
            if(ToolUtil.StringNotBlank(wl.getType())){
                y.add(wl.getType());
            }else {
                y.add("");
            }
            datak.add(y);
        }
        allData.add(datak);
        //文件导出
        ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
        return ReturnUtil.Success("故障日志导出成功！");
    }

	/**
	 * author zhangjun
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelForWarnLogByNewParam", method = RequestMethod.POST)
	public ModelMap excelForWarnLogByNewParam(String projectId, String deviceId, String isAlarm, Integer pageSize, Integer currentPage) throws Exception {

		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletResponse  response = servletRequestAttributes.getResponse();

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

		String[] files = new String[3];
		files[1] = "xlsx";
		files[0] = files[2] = "Sheel1";
		/*表样式开关(0表示关1表示开，多表用逗号隔开)*/
		String[] excelSwitch = new String[4];
		//是否有单元格合并
		excelSwitch[0] = "0";
		//文字水平居*（1居中2居左3居右）
		excelSwitch[1] = "1";
		//文字垂直居*（1居中2居左3居右）
		excelSwitch[2] = "1";
		//表格背景颜色
		excelSwitch[3] = "0";

		/*样式相关详细参数*/
		Map<String ,String> map = new HashMap<String ,String>();

		//表头部数据
		List<List<String>> titleData = new ArrayList<List<String>>();
		List<String> x = new ArrayList<String>();
		x.add("项目");
		x.add("设备id");
//		if(isDevice==1) {
//			x.add("终端编号");
//			x.add("设备类型");
//		}
		x.add("报警时间");
		x.add("报警类型");
		x.add("设备类型");
//		x.add("处理人");
//		x.add("联系方式");
//		x.add("处理时间");
//		x.add("处理备注");

		titleData.add(x);

		String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
		String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
		int areaIdInt = Integer.parseInt(areaId);
		int provinceIdInt = Integer.parseInt(provinceId);
		String bid = userinfo.getBid();
		String[] siteCordArr = bid.split(",");

		List<HixentArcZipperInfo> alarmLog = new ArrayList<HixentArcZipperInfo>();

//		if (isDevice == 0) {
//			// 中控
//
//			JSONObject alarmLogForEquipExcel = hixentArcAlarmLogService.alarmLogExcel(siteId, deviceId, 0, 0, provinceIdInt, areaIdInt, siteCordArr, warnIndex);
//			alarmLog=(List<HixentArcWarningList>)alarmLogForEquipExcel.get("alarmLogForDevice");
//
//		} else if (isDevice == 1) {
//			// 终端
//			JSONObject alarmLogForEquipExcel = hixentArcAlarmLogService.alarmLogForEquipExcel(siteId, deviceId, 0, 0, provinceIdInt, areaIdInt, siteCordArr, type, inquir, warnIndex);
//			alarmLog=(List<HixentArcWarningList>)alarmLogForEquipExcel.get("alarmLogForDevice");
//
//		}
		JSONObject alarmLogForEquipExcel = hixentArcZipperInfoService.alarmLogExcel(projectId, deviceId, isAlarm, pageSize, currentPage);
		alarmLog=(List<HixentArcZipperInfo>)alarmLogForEquipExcel.get("alarmLogForDevice");

		//获得处理结果和处理人信息和反馈
//		for (int i = 0; i < alarmLog.size(); i++) {
//			HixentArcAlarmDealFeedBack warnInfo = hixentArcAlarmLogService.getWarnInfo(alarmLog.get(i).getWarning_no());
//			if(warnInfo!=null) {
//
//				alarmLog.get(i).setAccount(warnInfo.getAppUserAccount());
//				alarmLog.get(i).setAppMobile(warnInfo.getAppUserPhone());
//				alarmLog.get(i).setDealTime(warnInfo.getDealTime());
//				alarmLog.get(i).setDealRemark(warnInfo.getDealRemark());
//			}
//		}

		//表数据
		List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
		List<List<String>> datak         = new ArrayList<List<String>>();
		for(HixentArcZipperInfo wl:alarmLog){
			List<String> y = new ArrayList<String>();

			if(ToolUtil.StringNotBlank(wl.getSiteName())) {

				y.add(wl.getSiteName());
			}else{
				y.add("");
			}
			if(ToolUtil.StringNotBlank(wl.getDeviceId())) {
				y.add(wl.getDeviceId());
			}else {
				y.add("");
			}
//			if(isDevice==1) {
//				y.add((Integer.parseInt(wl.getLineCode())+1)+"-"+wl.getAddr());
//				if(wl.getType()==0) {
//					y.add("电弧探测器");
//				}else {
//					y.add("组合式探测器");
//				}
//			}
			if(ToolUtil.StringNotBlank(wl.getCreateTime().toString())){
				y.add(wl.getCreateTime().toString());
			}else {
				y.add("");
			}

//			y.add(DateUtil.timestampToString(wl.getWarning_time())+" ");//时间戳转换为时间字符串
//			yAdd(wl.getAccount(),y);
//			yAdd(wl.getAppMobile(),y);
//			yAddTime(wl.getDealTime(),y);
//			yAdd(wl.getDealRemark(),y);
			if("1".equals(wl.getAlarmType())){
				y.add("停电");
			}else if("2".equals(wl.getAlarmType())){
				y.add("机器故障");
			}else {
				y.add("");
			}
			if(ToolUtil.StringNotBlank(wl.getType())){
				y.add(wl.getType());
			}else {
				y.add("");
			}
			datak.add(y);
		}
		allData.add(datak);
		//文件导出
		ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
		return ReturnUtil.Success("报警日志导出成功！");
	}

	@RequestMapping(value = "/excelForWarnLog", method = RequestMethod.POST)
	public ModelMap excelForWarnLog(Integer isDevice, Integer siteId, String deviceId, Integer equipId, String inquir,
			 Integer type,Integer warnIndex) throws Exception {
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes(); 
    	HttpServletResponse  response = servletRequestAttributes.getResponse();
	   
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
        
        String[] files = new String[3];
    	files[1] = "xlsx";
    	files[0] = files[2] = "Sheel1";
    	/*表样式开关(0表示关1表示开，多表用逗号隔开)*/
    	String[] excelSwitch = new String[4];
    	//是否有单元格合并
    	excelSwitch[0] = "0";
    	//文字水平居*（1居中2居左3居右）
    	excelSwitch[1] = "1";
    	//文字垂直居*（1居中2居左3居右）
    	excelSwitch[2] = "1";
    	//表格背景颜色
    	excelSwitch[3] = "0";
    	
    	/*样式相关详细参数*/
    	Map<String ,String> map = new HashMap<String ,String>();

      //表头部数据
    	List<List<String>> titleData = new ArrayList<List<String>>();
    	List<String> x = new ArrayList<String>();
    	x.add("项目");
    	x.add("中控");
    	if(isDevice==1) {
    		x.add("终端编号");
    		x.add("设备类型");
    	}

    	x.add("报警类型");
    	x.add("报警时间");
    	x.add("处理人");
    	x.add("联系方式");
    	x.add("处理时间");
    	x.add("处理备注");
    	
    	titleData.add(x);
    	
    	

    	String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
		String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
		int areaIdInt = Integer.parseInt(areaId);
		int provinceIdInt = Integer.parseInt(provinceId);
		String bid = userinfo.getBid();
		String[] siteCordArr = bid.split(",");
      
		List<HixentArcWarningList> alarmLog = new ArrayList<HixentArcWarningList>();
		
		if (isDevice == 0) {
			// 中控

			 JSONObject alarmLogForEquipExcel = hixentArcAlarmLogService.alarmLogExcel(siteId, deviceId, 0, 0, provinceIdInt, areaIdInt, siteCordArr, warnIndex);
			 alarmLog=(List<HixentArcWarningList>)alarmLogForEquipExcel.get("alarmLogForDevice");
		   
		} else if (isDevice == 1) {
			// 终端
		 JSONObject alarmLogForEquipExcel = hixentArcAlarmLogService.alarmLogForEquipExcel(siteId, deviceId, 0, 0, provinceIdInt, areaIdInt, siteCordArr, type, inquir, warnIndex);
		 alarmLog=(List<HixentArcWarningList>)alarmLogForEquipExcel.get("alarmLogForDevice");
		
		}
       for (int i = 0; i < alarmLog.size(); i++) {
    	   HixentArcAlarmDealFeedBack warnInfo = hixentArcAlarmLogService.getWarnInfo(alarmLog.get(i).getWarning_no());
    	   if(warnInfo!=null) {
    		  
    		   alarmLog.get(i).setAccount(warnInfo.getAppUserAccount()); 
    		   alarmLog.get(i).setAppMobile(warnInfo.getAppUserPhone());
    		   alarmLog.get(i).setDealTime(warnInfo.getDealTime());
    		   alarmLog.get(i).setDealRemark(warnInfo.getDealRemark());
    	   }
       }

		//表数据
    	List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
    	List<List<String>> datak         = new ArrayList<List<String>>();
    	for(HixentArcWarningList wl:alarmLog){
    		List<String> y = new ArrayList<String>();
    		
    		if(ToolUtil.StringNotBlank(wl.getSiteName())) {
    			
    			y.add(wl.getSiteName());
    		}else {
    			
    			y.add(wl.getSiteCode());	
    		}
    		if(ToolUtil.StringNotBlank(wl.getNiName())) {
    			y.add(wl.getNiName());		
    		}else {
    			if(ToolUtil.StringNotBlank(wl.getSpecificator())){
    				y.add(ToolUtil.formatDevStr(wl.getSpecificator()));
    			}else {
    				y.add(wl.getDeviceCode());
    			}
    			
    		}
    		if(isDevice==1) {
    			y.add((Integer.parseInt(wl.getLineCode())+1)+"-"+wl.getAddr());
    			if(wl.getType()==0) {
    				y.add("电弧探测器");
    			}else {
    				y.add("组合式探测器");
    			}
    			
    		}
        	
        	y.add(wl.getProtocolNode());
        	y.add(DateUtil.timestampToString(wl.getWarning_time())+" ");
        	yAdd(wl.getAccount(),y);
        	yAdd(wl.getAppMobile(),y);
        	yAddTime(wl.getDealTime(),y);
        	yAdd(wl.getDealRemark(),y);
        	
           
        	
        	datak.add(y);
    	}
    	allData.add(datak);
    	//文件导出
    	ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
    	return ReturnUtil.Success("导出成功！");
	}
	/**
	 * 导出报警历史
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/excelForWarnHistory", method = RequestMethod.POST)
	public ModelMap excelForWarnHistory(String startTime, String endTime
			) throws Exception {
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes(); 
    	HttpServletResponse  response = servletRequestAttributes.getResponse();
	   
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
        
        String[] files = new String[3];
    	files[1] = "xlsx";
    	files[0] = files[2] = "Sheel1";
    	/*表样式开关(0表示关1表示开，多表用逗号隔开)*/
    	String[] excelSwitch = new String[4];
    	//是否有单元格合并
    	excelSwitch[0] = "0";
    	//文字水平居*（1居中2居左3居右）
    	excelSwitch[1] = "1";
    	//文字垂直居*（1居中2居左3居右）
    	excelSwitch[2] = "1";
    	//表格背景颜色
    	excelSwitch[3] = "0";
    	
    	/*样式相关详细参数*/
    	Map<String ,String> map = new HashMap<String ,String>();

      //表头部数据
    	List<List<String>> titleData = new ArrayList<List<String>>();
    	List<String> x = new ArrayList<String>();
    	x.add("项目");
    	x.add("中控"); 
    	x.add("终端");
    	x.add("报警类型");    
    	x.add("报警时间");     
    	x.add("处理人");  
    	x.add("联系方式");
    	x.add("处理时间");
    	x.add("处理备注");
    	
    	titleData.add(x);
    	
		String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
		String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
		int areaIdInt = Integer.parseInt(areaId);
		int provinceIdInt = Integer.parseInt(provinceId);
		String bid = userinfo.getBid();
		String[] siteCordArr = bid.split(",");

    	long startTimeToStamp = 0;
		long endTimeToStamp = 0;

		
		 if(startTime==null) {
        	 startTime="";
         }
         if(endTime==null) {
        	 endTime="";
         }
		if (!startTime.equals("")) {
			String runStartTime = startTime + " 00:00:00"; 
			startTimeToStamp = DateUtil.getTimestamp(runStartTime);
			
		} 
		if (!endTime.equals("") ) {
			String runEndTime = endTime + " 23:59:59";
			endTimeToStamp = DateUtil.getTimestamp(runEndTime);
		} 
		int currentPage=0;
		int pageSize=0;
		JSONObject alarmHistoryForAll = hixentArcAlarmLogService.alarmHistoryForAll(currentPage, pageSize, provinceIdInt, areaIdInt, siteCordArr, startTimeToStamp, endTimeToStamp);
		List<HixentArcWarningList> alarmHistoryList=(List<HixentArcWarningList>) alarmHistoryForAll.get("alarmHistoryForAll");
	
//       for (int i = 0; i < alarmHistoryList.size(); i++) {
//    	   HixentArcAlarmDealFeedBack warnInfo = hixentArcAlarmLogService.getWarnInfo(alarmHistoryList.get(i).getWarning_no());
//    	   if(warnInfo!=null) {
//    		  
//    		   alarmHistoryList.get(i).setAccount(warnInfo.getAppUserAccount()); 
//    		   alarmHistoryList.get(i).setAppMobile(warnInfo.getAppUserPhone());
//    		   alarmHistoryList.get(i).setDealTime(warnInfo.getDealTime());
//    		   alarmHistoryList.get(i).setDealRemark(warnInfo.getDealRemark());
//    	   }
//       }

		//表数据
    	List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
    	List<List<String>> datak         = new ArrayList<List<String>>();
    	for(HixentArcWarningList wl:alarmHistoryList){
    		List<String> y = new ArrayList<String>();
    		
    		if(ToolUtil.StringNotBlank(wl.getSiteName())) {
    			
    			y.add(wl.getSiteName());
    		}else {
    			
    			y.add(wl.getSiteCode());	
    		}
    		if(ToolUtil.StringNotBlank(wl.getNiName())) {
    			y.add(wl.getNiName());		
    		}else {
    			if(ToolUtil.StringNotBlank(wl.getSpecificator())){
    				y.add(ToolUtil.formatDevStr(wl.getSpecificator()));
    			}else {
    				y.add(wl.getDeviceCode());
    			}
    			
    		}
    		if(ToolUtil.StringNotBlank(wl.getLineCode()) && ToolUtil.StringNotBlank(wl.getAddr()) ) {
    			y.add((Integer.parseInt(wl.getLineCode())+1)+"-"+wl.getAddr());
    		}else {
    			y.add("");
    		}
        	y.add(wl.getProtocolNode());
        	y.add(DateUtil.timestampToString(wl.getWarning_time())+" ");
        	yAdd(wl.getAccount(),y);
        	yAdd(wl.getAppMobile(),y);
        	yAddTime(wl.getDealTime(),y);
        	yAdd(wl.getDealRemark(),y);
        	
           
        	
        	datak.add(y);
    	}
    	allData.add(datak);
    	//文件导出
    	ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
    	return ReturnUtil.Success("导出成功！");
	}
	
	/**
	 * 导出设备状态数据
	 * 
	 * 
	 */
	@RequestMapping(value = "/excelForEquipStatus", method = RequestMethod.POST)
	public ModelMap excelForEquipStatus() throws Exception {
		
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes(); 
    	HttpServletResponse  response = servletRequestAttributes.getResponse();
	   
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
        List<Object> userData = getData(userinfo);
        Integer areaId = (Integer) userData.get(0);
        Integer provinceId = (Integer) userData.get(1);
        String[] siteCordArr =(String[]) userData.get(2);
        
        String[] files = new String[3];
    	files[1] = "xlsx";
    	files[0] = files[2] = "Sheel1";
    	/*表样式开关(0表示关1表示开，多表用逗号隔开)*/
    	String[] excelSwitch = new String[4];
    	//是否有单元格合并
    	excelSwitch[0] = "0";
    	//文字水平居*（1居中2居左3居右）
    	excelSwitch[1] = "1";
    	//文字垂直居*（1居中2居左3居右）
    	excelSwitch[2] = "1";
    	//表格背景颜色
    	excelSwitch[3] = "0";
    	
    	/*样式相关详细参数*/
    	Map<String ,String> map = new HashMap<String ,String>();

      //表头部数据
    	List<List<String>> titleData = new ArrayList<List<String>>();
    	List<String> x = new ArrayList<String>();
    	x.add("报警数");
    	x.add("离线数");
    	x.add("故障数");
    	
    	titleData.add(x);
    	//表数据
    	List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
    	List<List<String>> datak         = new ArrayList<List<String>>();
    	List<String> y = new ArrayList<String>();
    	HixentArcIndex equipStatus = hixentArcIndexService.getEquipStatus(areaId,provinceId,siteCordArr);
    	y.add(equipStatus.getEquipCountAlarm()+"");
    	y.add(equipStatus.getEquipCountOffLine()+"");
    	y.add(equipStatus.getEquipCountFault()+"");
    	datak.add(y);
    	allData.add(datak);
    	ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
    	return ReturnUtil.Success("导出成功！");
	}
	
	
	//用户相关信息
	public  List<Object> getData(HixentUser userinfo){
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
				
			}
			List<Object> outData = new ArrayList<Object>();
			outData.add(areaIdInt);
			outData.add(provinceIdInt);
			outData.add(siteCordArr);
			return outData;
	}
	public void yAdd(String data,List<String> y) {
		if(ToolUtil.StringNotBlank(data)) {
    		y.add(data+" ");
    	}else {
    		y.add("");
    	}
	}
	public void yAddTime(Integer data,List<String> y) {
		if(data!=null) {
			y.add(DateUtil.timestampToString(data)+" ");	
    	}else {
    		y.add("");
    	}
	}
}
