package com.qf.controller.api.admin;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcProtocol;
import com.qf.model.fire.HixentArcSiteEquipmentRelevance;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.service.admin.HixentUserService;
import com.qf.service.fire.HixentArcAlarmLogService;
import com.qf.service.fire.HixentArcEquipmentInfoService;
import com.qf.service.fire.HixentArcProtocolService;
import com.qf.service.fire.HixentArcSiteEquipmentRelevanceService;
import com.qf.service.fire.HixentArcWarningListService;
import com.qf.util.DateUtil;
import com.qf.util.ExcelUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;



@RestController
@RequestMapping("/api/user/excel")
public class ApiUserExcelController {
	@Autowired
	private HixentArcAlarmLogService  hixentArcAlarmLogService;
    @Autowired
    private HixentArcEquipmentInfoService hixentArcEquipmentInfoService;
    
    @Autowired
    private HixentArcWarningListService hixentArcWarningListService;

    @Autowired
    private HixentArcSiteEquipmentRelevanceService hixentArcSiteEquipmentRelevanceService;

	@Autowired
    private HixentArcProtocolService hixentArcProtocolService;   
	
    @Resource
    private RedisUtil redisUtil;
    
	@Autowired 
	private HixentUserService hixentUserService; 
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;

    
    
    /**
     * 系统后台告警列表
     * author Vareck
     */
    //@ApiLimitConfig(count=1,time=1000)
    //@RequiresPermissions(value = {"parc_device"}) 
    @SystemHistoryAnnotation(opration = "导出告警列表")
    @RequestMapping(value = "/excelForWarnLogs", method = RequestMethod.GET)
    public ModelMap fireWarningList(Integer isDevice, Integer siteId, String deviceId, Integer equipId, String inquir,
			 Integer type,Integer warnIndex) throws Exception {
    	warnIndex=0;
    	try{
        	//获取request和response
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
	        Map<String,Object> jsonMap = new HashMap<>();
        	
	        //设备权限判断（站点）
	        String bStr = userinfo.getBid().trim();
//	        if( bStr.equals("0") || bStr.equals("") ){
//	        	return ReturnUtil.Error("没有站点分组设备权限！");
//	        }
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
            //获取告警列表
        	//List<HixentArcWarningList> wList = hixentArcWarningListService.getAllByDeviceId(eidList);
	        String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
			String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
			int areaIdInt = Integer.parseInt(areaId);
			int provinceIdInt = Integer.parseInt(provinceId);
    		String bid = userinfo.getBid();
			String[] siteCordArr = bid.split(",");
			//JSONObject alarmLog = hixentArcAlarmLogService.alarmLog(0,"", 1, 5, provinceIdInt, areaIdInt, siteCordArr,0);
			//List<HixentArcWarningList> alarmLogForDevice =(List<HixentArcWarningList>) alarmLog.get("alarmLogForDevice");
			/*文件名与文件后缀*/
			 JSONObject alarmLogForEquipExcel = hixentArcAlarmLogService.alarmLogForEquipExcel(siteId, deviceId, 0, 0, provinceIdInt, areaIdInt, siteCordArr, type, inquir, warnIndex);
			 List<HixentArcWarningList> alarmLog=(List<HixentArcWarningList>)alarmLogForEquipExcel.get("alarmLogForDevice");
			String[] files = new String[3];
        	files[1] = "xlsx";
        	files[0] = files[2] = userinfo.getAccount()+"负责的设备的告警列表信息";
        	
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
        	x.add("告警id");      
        	x.add("报警类型名称");    
        	x.add("报警时间");     
        	x.add("是否处理");        
        	x.add("派单时间");        
        	x.add("设备终端编号");    
        	x.add("设备类型");
        	x.add("经度");
        	x.add("纬度");
        	x.add("设备详细地址");
        	x.add("处理人员名称");
        	x.add("管理员姓名");
        	x.add("备注");
        	titleData.add(x);
        	
        	String[] eType = new String[3];
        	eType[0] = "无线";
        	eType[1] = "有线主机";
        	eType[2] = "有线终端";
        	//表数据
        	List<List<List<String>>> allData = new ArrayList<List<List<String>>>();
        	List<List<String>> datak         = new ArrayList<List<String>>();
        	for(HixentArcWarningList wl:alarmLog){
            	List<String> y = new ArrayList<String>();
            	y.add(Integer.toString(wl.getWarningNo()));
//            	HixentArcProtocol pInfo = hixentArcProtocolService.selectByName(wl.getWarningType());
//            	y.add(pInfo.getProtocolNode());
//            	
//            	Date date1 = DateUtil.stringToDate(wl.getWarning_time()+"", "f");
//            	//Date date1 = new Date();
//            	y.add(DateUtil.getCurrentTime(date1));
//            	if(wl.getIsDeal()==0){
//            		y.add("未处理");
//            	}else{
//            		y.add("已处理");
//            	}
//            	long date2 = wl.getDispatchTime();
//            	y.add(DateUtil.longToString(date2, "yyyy-MM-dd HH:mm:ss"));
//            	y.add(wl.getEid());
//            	y.add(eType[wl.getEtype()]);
//            	y.add(Double.toString(wl.getLngBmap()));
//            	y.add(Double.toString(wl.getLatBmap()));
//            	y.add(wl.getAddress());
//            	y.add(wl.getAppName());
//            	y.add(wl.getAdminName());
//            	y.add(wl.getNode());
            	datak.add(y);
        	}
        	allData.add(datak);
        	//文件导出
        	ExcelUtil.createExcel(response,files,excelSwitch,map,titleData,allData);
        	return ReturnUtil.Success("导出成功！");
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    

    
}