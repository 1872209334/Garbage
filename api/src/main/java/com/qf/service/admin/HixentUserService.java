package com.qf.service.admin;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.admin.HixentUserMapper;
import com.qf.mapper.fire.HixentArcEfmDeviceMapper;
import com.qf.mapper.fire.HixentArcEquipmentInfoMapper;
import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentProvince;
import com.qf.model.admin.HixentRole;
import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcAppMapSite;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.util.DateUtil;
import com.qf.util.ToolUtil;

import tk.mybatis.mapper.entity.Example;



/**
 * web管理后台管理员相关服务
 * author   Vareck
 */
@Service
public class HixentUserService {

	
	
    @Autowired
    private HixentUserMapper hixentUserMapper;
    @Autowired
    private HixentArcEfmDeviceMapper hixentArcEfmDeviceMapper;
    
    @Autowired
    private HixentArcEquipmentInfoMapper hixentArcEquipmentInfoMapper;
    /*用户列表*/
    public List<HixentUser> selectBySystem() {
    	return hixentUserMapper.selectBySystem();
    }
    
    public List<HixentUser> getUserAllList(Integer roleType,Integer id,String name){
    	return hixentUserMapper.getUserAllList(name,id,roleType);
    }
    
    public List<HixentUser> getUserAllList( String name,Integer id,Integer roleType ) {
    	return hixentUserMapper.getUserAllList( name,id,roleType );
    }
    public List<HixentUser> getUserPageList( String name,String order,Integer limit,Integer page,Integer id,Integer roleType ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	System.out.println("roleType"+roleType);
    	return hixentUserMapper.getUserPageList( name,order,limits,id,roleType );
    }
    
    public Integer getCount(Example example){
        return hixentUserMapper.selectCountByExample(example);
    }

    public HixentUser getAdmin(HixentUser hixentUser){
        return hixentUserMapper.selectOne(hixentUser);
    }
    
    public HixentUser getAdminById( Integer id ){
        return hixentUserMapper.getAdminById(id);
    }

    public HixentUser findByUserId(String uid) {
        return hixentUserMapper.selectByUserId(uid);
    }
    
    public HixentUser findByUsername(String username) {
        return hixentUserMapper.selectByUsername(username);
    }
    
    public HixentUser getUserinfoByMobile(String mobile){
    	return hixentUserMapper.getUserinfoByMobile(mobile);
    }
    
    public void updateToken(String uid,String access_token){
    	hixentUserMapper.updateToken(uid,access_token);
    }

    public void deleteUser(List<Integer> uidList) {
    	hixentUserMapper.deleteUser(uidList);
    }

    //添加
    public void insertUser( String uid,String account,String password,
    		String mobile,String salt,Integer cid,Integer fireRole,
    		String bid,Integer provinceId,Integer areaId,String remark,Integer pid) throws Exception {
    	
    	
    	//获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long utime = DateUtil.getTimestamp(currentDate);
    	long ctime = DateUtil.getTimestamp(currentDate);
    	hixentUserMapper.insertUser( uid,account,password,mobile,salt,cid,fireRole,ctime,utime,bid,provinceId,areaId,remark,pid );
    }
    //编辑
    public void updateUser( Integer id,String account,String mobile,
    		Integer fireRole ,String bid,Integer provinceId,Integer cityId,String remark) throws Exception {
    	//获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long utime = DateUtil.getTimestamp(currentDate);
    	hixentUserMapper.updateUser( id,account,mobile,fireRole,utime,bid,provinceId,cityId,remark);
    }
    //修改密码
    public void updateUserPassWord( Integer id , String password ){
    	hixentUserMapper.updateUserPassWord( id,password );
    }
    
    public void insert(HixentUser hixentUser){
    	hixentUserMapper.insert(hixentUser);
    }
    
    public void update(HixentUser hixentUser, Example example){
    	hixentUserMapper.updateByExample(hixentUser,example);
    }
    //获取省份
    public List<HixentProvince> getPrinvince(Integer privinceId){
    	List<HixentProvince> prinvince = hixentUserMapper.getPrinvince(privinceId);
    	return prinvince;
    }
    //获取市区(通过省份ID)
    public List<HixentArea> getCity(Integer privinceId){
    	List<HixentArea> city = hixentUserMapper.getCity(privinceId);
    	return city;
    }
    //获取市区(通过地区ID)
    public List<HixentArea> getCityByAreaId(Integer areaId){
    	List<HixentArea> city = hixentUserMapper.getCityByAreaId(areaId);
    	return city;
    }
    //获取角色
    public List<HixentRole> getRole(Integer roleType){
    	List<HixentRole> roleList = hixentUserMapper.getRole(roleType);
    	return roleList;
    }
    //获取项目
    public List<HixentSite> getsite(Integer privinceId,Integer areaId,String[] siteCordArr){
    	List<HixentSite> site = hixentUserMapper.getSite(privinceId,areaId,siteCordArr);
    	return site;
    }
    //获取角色类型
    public Integer getRoleType(Integer roleId){
    	Integer roleType = hixentUserMapper.getRoleType(roleId);
    	return roleType;
    } 
    //获取中控
    public List<HixentArcEfmDevice> getDevice(Integer privinceId,Integer areaId,String[] siteCordArr){
    List<HixentArcEfmDevice> deviceList = hixentArcEfmDeviceMapper.getDeviceList(privinceId, areaId, siteCordArr);
    for (int i = 0; i < deviceList.size(); i++) {
		//中控经纬度
		String longitute = deviceList.get(i).getLongitute();
		String latitude = deviceList.get(i).getLatitude();
		String Strlongitute = ToolUtil.hexStr2Str(longitute);
		String Strlatitude = ToolUtil.hexStr2Str(latitude);
		deviceList.get(i).setLongitute(Strlongitute);
		deviceList.get(i).setLatitude(Strlatitude);
		deviceList.get(i).setSpecificator(ToolUtil.formatDevStr(deviceList.get(i).getSpecificator()));
		String id = deviceList.get(i).getId();
		
		Integer equipCount = hixentArcEfmDeviceMapper.getEquipCount(id);
		//0电弧预警，1设备故障，2设备离线
    	Integer equipOffLineCount = hixentArcEfmDeviceMapper.getEquipTypeCountByDevice(id,2);
    	Integer equipAlarmCount = hixentArcEfmDeviceMapper.getEquipTypeCountByDevice(id,0);
    	Integer equipFaultCount = hixentArcEfmDeviceMapper.getEquipTypeCountByDevice(id,1);
    	deviceList.get(i).setEquipAlarmCount(equipAlarmCount);
    	deviceList.get(i).setEquipCount(equipCount);
    	deviceList.get(i).setEquipFaultCount(equipFaultCount);
    	deviceList.get(i).setEquipOffLineCount(equipOffLineCount);
    }
    
    return deviceList;
    }
    //地图获取无线终端
    public List<HixentArcAppMapSite> getWirelessEquiplist(Integer privinceId,Integer areaId,String[] siteCordArr){
    	
    	List<HixentArcAppMapSite> wirelessEquiplist = hixentArcEquipmentInfoMapper.getWirelessEquiplist(privinceId,areaId,siteCordArr);
    	if(wirelessEquiplist.size()==1&&null==wirelessEquiplist.get(0)||wirelessEquiplist.size()==0){
    		wirelessEquiplist=null;
		}else {
			for (int i = 0; i < wirelessEquiplist.size(); i++) {
	    		HixentArcAppMapSite hixentArcAppMapSite = wirelessEquiplist.get(i);
	    		hixentArcAppMapSite.setLatBmap(ToolUtil.formatDevStr(hixentArcAppMapSite.getLatBmap()));
	    		hixentArcAppMapSite.setLngBmap(ToolUtil.formatDevStr(hixentArcAppMapSite.getLngBmap()));
	    		hixentArcAppMapSite.setMessage(ToolUtil.formatDevStr(hixentArcAppMapSite.getMessage()));
	    	}
		}
    	
    	 return wirelessEquiplist;
    }
    //项目
    public List<HixentArcAppMapSite> mapData(Integer privinceId,Integer areaId,String[] siteCordArr){
    	List<HixentArcAppMapSite> mapData = hixentArcEquipmentInfoMapper.mapData(privinceId, areaId, siteCordArr);
    	if(mapData.size()==1&&null==mapData.get(0)||mapData.size()==0){
    		mapData=null;
    		
		}else {
			
			for (int i = 0; i < mapData.size(); i++) {
	    		HixentArcAppMapSite hixentArcAppMapSite = mapData.get(i);
	    		hixentArcAppMapSite.setLatBmap(ToolUtil.formatDevStr(hixentArcAppMapSite.getLatBmap()));
	    		hixentArcAppMapSite.setLngBmap(ToolUtil.formatDevStr(hixentArcAppMapSite.getLngBmap()));
	    		
	    	}
		}
    	 return mapData;
    }
  //某项目设备数和问题数
    public JSONObject getCountBySiteCode(String siteCode) {
    	Integer warnIndex0=0;//报警
    	Integer warnIndex1=1;//故障
    	Integer warnIndex2=2;//离线
    
    	Integer equipWarn0CountBySiteCode = hixentArcEquipmentInfoMapper.getEquipWarnCountBySiteCode(siteCode,warnIndex0);
    	Integer equipWarn1CountBySiteCode = hixentArcEquipmentInfoMapper.getEquipWarnCountBySiteCode(siteCode,warnIndex1);
    	Integer equipWarn2CountBySiteCode = hixentArcEquipmentInfoMapper.getEquipWarnCountBySiteCode(siteCode,warnIndex2);
    
//    	Integer alarmCount=deviceWarn0CountBySiteCode+equipWarn0CountBySiteCode;
//    	Integer faultCount=deviceWarn1CountBySiteCode+equipWarn1CountBySiteCode;
//    	Integer offlineCount=deviceWarn2CountBySiteCode+equipWarn2CountBySiteCode;
//    	
  	Integer equipCountBySiteCode = hixentArcEquipmentInfoMapper.getEquipCountBySiteCode(siteCode);
//    	Integer deviceCountBySiteCode = hixentArcAppIndexMapper.getDeviceCountBySiteCode(appUserId,siteCode);
//       
//    	Integer equipCount=equipCountBySiteCode+deviceCountBySiteCode;
    	JSONObject countData = new JSONObject();
    	
    	countData.put("alarmCount", equipWarn0CountBySiteCode);
    	countData.put("faultCount", equipWarn1CountBySiteCode);
    	countData.put("offlineCount", equipWarn2CountBySiteCode);
    	countData.put("equipCount", equipCountBySiteCode);
    	 return countData;
    }
    
    
    //通过项目获取中控
    public List<HixentArcEfmDevice> getDeviceBySite(String siteId){
    	List<HixentArcEfmDevice> deviceBySiteId = hixentArcEfmDeviceMapper.getDeviceBySiteId(siteId);
      return deviceBySiteId;
    }
    
    //获取项目
    public List<HixentSite> getsiteList(String[] siteCordArr){
    	return hixentUserMapper.getsiteList(siteCordArr);
    }
    
}