package com.qf.service.fire;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.HixentArcControllGroupMapper;
import com.qf.mapper.fire.HixentArcEfmDeviceMapper;
import com.qf.mapper.fire.HixentArcEquipmentInfoMapper;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcControlGroup;
import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.util.PageUtil;
import com.qf.util.ToolUtil;



/**
 * 管控分组
 * author RuanYu
 */
@Service
public class HixentArcControllGroupService {
	
	@Autowired
    private HixentArcControllGroupMapper hixentArcControllGroupMapper;
	
	@Autowired
    private HixentArcEfmDeviceMapper hixentArcEfmDeviceMapper;
	
	@Autowired
    private HixentArcEquipmentInfoMapper hixentArcEquipmentInfoMapper;
	
	@Autowired
    private HixentArcWarningListService hixentArcWarningListService;
    //根据虚拟分组id获取设备号
    public PageUtil<HixentArcControlGroup> getGroupInfo(String uid,Integer siteId,Integer adminId,
    		Integer currentPage,Integer pageSize) {
    	 
    	//PageHelper.startPage(currentPage, pageSize);
    	String limits = " "+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+" ";
         List<HixentArcControlGroup> allItems = hixentArcControllGroupMapper.getControllGroupList(limits,siteId, uid, adminId);  //分组结果
         int countNums = hixentArcControllGroupMapper.getControllGroupCount(siteId, uid, adminId);            //总记录数
         PageUtil<HixentArcControlGroup> pageData = new PageUtil<>(currentPage, pageSize, countNums);
         pageData.setItems(allItems);
         return pageData;
    	
    }
    //获取app用户
     public List<HixentAppUser> getAppUserList(Integer adminId){
    	 List<HixentAppUser> appUser = hixentArcControllGroupMapper.getAppUser(adminId);
       return appUser;
     
     }
     //终端分页
     public List<HixentArcEquipmentInfo> getEquipList(Integer type,String siteId,String deviceId,Integer limit,Integer page,
    		 String inquire,String massage,String checkGroup,Integer isGroup,Integer parameterType,Integer deviceType){
    	 String limits = ""+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+"";
    	 
    	 List<HixentArcEquipmentInfo> equipList = hixentArcEquipmentInfoMapper.getEquipList(type,siteId,deviceId,limits,inquire,massage,checkGroup,isGroup,parameterType,deviceType);
      
        for (int i = 0; i < equipList.size(); i++) {
        	equipList.get(i).setMessage(ToolUtil.formatDevStr(equipList.get(i).getMessage()));
        	equipList.get(i).setSpecificatorDevice(ToolUtil.formatDevStr(equipList.get(i).getSpecificatorDevice()));
        	
        }
    	
    	 return equipList;
     
     }
     //终端数量
     public Integer getEquipListCount(Integer type,String siteId,String deviceId,String inquire,String equipInquire,String checkGroup,Integer isGroup,Integer parameterType,Integer deviceType) {
    	
    	 Integer equipListCount = hixentArcEquipmentInfoMapper.getEquipListCount(type,siteId, deviceId,inquire,equipInquire,checkGroup,isGroup,parameterType,deviceType);
    	 return equipListCount;
     }
     //项目联动出中控
     public List<HixentArcEfmDevice> getDeviceBySiteId(String siteId){
    	 List<HixentArcEfmDevice> deviceBySiteId = hixentArcEfmDeviceMapper.getDeviceBySiteId(siteId);
    	 for (int i = 0; i < deviceBySiteId.size(); i++) {
    		 String specificator = deviceBySiteId.get(i).getSpecificator();
		   String strSpecificator = ToolUtil.hexStr2Str(specificator);
		   deviceBySiteId.get(i).setSpecificator(strSpecificator);
    	 }
    	 return deviceBySiteId;
     }
     //新建分组
     public boolean addNewControllerGroup(String[] appUserList,String[] equipList,Integer siteId,String place,Integer adminId) {
    	 HixentArcControlGroup hg = new HixentArcControlGroup(null,adminId,siteId,place);
    	 Integer groupIdFromAdd = hixentArcControllGroupMapper.getGroupIdFromAdd(hg);
    	 Integer groupId = hg.getId();
    	 //hixent_app_user_group_link插入数据
    	 Integer addUserGroupLink = hixentArcControllGroupMapper.addUserGroupLink(appUserList, groupId);
    	//设备表更新数据
    	 Integer updateEquipInfo = hixentArcControllGroupMapper.updateEquipInfo(equipList,groupId);
    	if(groupIdFromAdd>0 && addUserGroupLink>0 && updateEquipInfo>0 ) {
    		return true;
    	}else {
    		return false;
    	}
    	 
     }
     //编辑
     public boolean upadateControllerGroup(String[] appUserList,String[] equipList,Integer siteId,String place,Integer adminId,Integer groupId) {
    	 HixentArcControlGroup hg = new HixentArcControlGroup(null,adminId,siteId,place);
    	//删除hixent_app_user_group_link该组的数据
    	 Integer deleteUserGroupLink = hixentArcControllGroupMapper.deleteUserGroupLink(groupId);
     	//设备表更新数据
    	 Integer updateEquipGroupId = hixentArcControllGroupMapper.updateEquipGroupId(groupId);
    	 
    	 //hixent_app_user_group_link插入数据
    	 Integer addUserGroupLink = hixentArcControllGroupMapper.addUserGroupLink(appUserList, groupId);
    	//设备表更新数据
    	 Integer updateEquipInfo = hixentArcControllGroupMapper.updateEquipInfo(equipList,groupId);
    	 Integer updateGroup = hixentArcControllGroupMapper.updateGroup(groupId,place);
    	 if( addUserGroupLink>0 && updateEquipInfo>0 &&deleteUserGroupLink>=0 && updateEquipGroupId>=0 && updateGroup>0) {
    		return true;
    	}else {
    		return false;
    	}
    	 
     }
     //删除管控分组
     public boolean delGroup(Integer id) {
    	//删除hixent_app_user_group_link该组的数据
    	 Integer deleteUserGroupLink = hixentArcControllGroupMapper.deleteUserGroupLink(id);
     	//设备表更新数据
    	 Integer updateEquipGroupId = hixentArcControllGroupMapper.updateEquipGroupId(id);
    	 //删除 hixent_app_control_group该组的数据
    	 Integer delGroup = hixentArcControllGroupMapper.delGroup(id);
    	 
    	 if(delGroup>0) {
    		 return true;
     	}else {
     		return false;
     	}

     }
     //编辑管控分组显示已有设备
     public PageUtil<HixentArcEquipmentInfo>  getEquipListByGroupId(Integer id,Integer currentPage,Integer pageSize ){
    	 String limits=null;
    	 if(currentPage==0 && pageSize==0) {
    		 limits="";
    		 currentPage=1;
    		 pageSize=10;
    	 }else {
    		 limits = " "+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+" "; 
    	 }
    	
       List<HixentArcEquipmentInfo> selEquipListByGroupId = hixentArcEquipmentInfoMapper.selEquipListByGroupId(id, limits);
       Integer selEquipListByGroupIdCount = hixentArcEquipmentInfoMapper.selEquipListByGroupIdCount(id);
       for (int i = 0; i < selEquipListByGroupId.size(); i++) {
    	   selEquipListByGroupId.get(i).setMessage(ToolUtil.formatDevStr(selEquipListByGroupId.get(i).getMessage()));
	  }
     
         PageUtil<HixentArcEquipmentInfo> pageData = new PageUtil<>(currentPage, pageSize, selEquipListByGroupIdCount);
         pageData.setItems(selEquipListByGroupId);
         return pageData;
    	 
     }
}