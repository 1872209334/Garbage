package com.qf.service.fire;



import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qf.mapper.fire.HixentArcEquipmentInfoMapper;
import com.qf.mapper.fire.HixentArcEquipmentInfoMqttMapper;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.util.ToolUtil;

import tk.mybatis.mapper.entity.Example;



/**
 * 设备详情相关服务
 * author Vareck
 */
@Service
public class HixentArcEquipmentInfoService {
	
	
	
	@Autowired
    private HixentArcEquipmentInfoMapper hixentArcEquipmentInfoMapper;
	
	@Autowired
    private HixentArcEquipmentInfoMqttMapper hixentArcEquipmentInfoMqttMapper;
	
    //根据虚拟分组id获取设备号
    public List<HixentArcEquipmentInfo> getDeviceListByProjectId(String[] pid) {
    	return hixentArcEquipmentInfoMapper.getDeviceListByProjectId(pid);
    }
    
	//获取列表信息(app通过虚拟分组)
    public List<HixentArcEquipmentInfo> getAllDeviceList(Integer pid) {
    	return hixentArcEquipmentInfoMapper.getAllDeviceList(pid);
    }
    public List<HixentArcEquipmentInfo> getPageDeviceList(Integer pid,Integer limit,Integer page,String order) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMapper.getPageDeviceList(pid,order,limits);
    }
	//获取列表信息(后台通过站点)
    public List<HixentArcEquipmentInfo> getAllDeviceListByBid(Set bid,Integer efmId) {
    	return hixentArcEquipmentInfoMapper.getAllDeviceListByBid(bid,efmId);
    }
  //获取mqtt列表信息(后台通过站点)
    public List<HixentArcEquipmentInfo> getAllMqttDeviceList() {
    	return hixentArcEquipmentInfoMapper.getAllMqttDeviceList();
    }
    
  //获取mqtt列表信息(后台通过站点)
    public List<HixentArcEquipmentInfo> getCommonMqttList(Set allDid) {
    	return hixentArcEquipmentInfoMapper.getCommonMqttList(allDid);
    }
    
  //获取mqtt分页列表信息(后台通过站点)
    public List<HixentArcEquipmentInfo> getPageCommonMqttList(Set allDid,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMapper.getPageCommonMqttList(allDid,limits);
    }
    
    
    
    
  //获取mqtt分页列表信息(后台通过站点)
    public List<HixentArcEquipmentInfo> getPageMqttDeviceList(Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMapper.getPageMqttDeviceList(limits);
    }
    
    
    
    
    public List<HixentArcEquipmentInfo> getPageDeviceListByBid(Set bid,Integer efmId,Integer limit,Integer page,String order,String lineCode) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMapper.getPageDeviceListByBid(bid,efmId,order,limits,lineCode);
    }
	//获取详细信息
    public HixentArcEquipmentInfo selectOne(HixentArcEquipmentInfo hixentArcEquipmentInfo) {
    	return hixentArcEquipmentInfoMapper.selectOne(hixentArcEquipmentInfo);
    }
    
    public HixentArcEquipmentInfo getOne(String eid) {
    	return hixentArcEquipmentInfoMapper.getOne(eid);
    }
    
    //删除
    public int delete(HixentArcEquipmentInfo hixentArcEquipmentInfo) {
    	
    	return hixentArcEquipmentInfoMapper.delete(hixentArcEquipmentInfo);
    }

    //新增
    public int insert(HixentArcEquipmentInfo data){
    	return hixentArcEquipmentInfoMapper.insert(data);
    }

    //更新
    public void update(HixentArcEquipmentInfo hixentArcEquipmentInfo, Example example){
    	hixentArcEquipmentInfoMapper.updateByExample(hixentArcEquipmentInfo,example);
    }
    //获取主机下终端列表
    public List<HixentArcEquipmentInfo> getPageEfmTerminalList(String efmId,String lineCode,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMapper.getPageEfmTerminalList(efmId,lineCode,limits);
    }
    
  //获取主机下终端
    public List<HixentArcEquipmentInfo> getAllEfmTerminalList(String efmId,String lineCode) {
    	return hixentArcEquipmentInfoMapper.getAllEfmTerminalList(efmId,lineCode);
    }
    
    
    //获取主机下终端总数
    public int getEfmTerminalCount(String efmId,String lineCode) {
    	return hixentArcEquipmentInfoMapper.getEfmTerminalCount(efmId,lineCode).intValue();
    }
    
    //删除主机和设备Ruanyu
   
    public boolean deleteEquip(String efmId) {
    	Integer delEfmDevice = hixentArcEquipmentInfoMapper.delEfmDevice(efmId);
    	String[] efmIdArr = efmId.split(",");
    	
    	Integer delEquipInfo = hixentArcEquipmentInfoMapper.delEquipByEfmId(efmIdArr);
    	//获取报警表的ID 拼接字符串(通过中控Id)
    	String warnIdByEfmId="";
    	warnIdByEfmId = hixentArcEquipmentInfoMapper.getWarnIdByEfmId(efmIdArr);
    	
    	//删除报警表数据通过中控IDs
    	Integer delWarnByEfmId = hixentArcEquipmentInfoMapper.delWarnByEfmId(efmIdArr);
    	
    	if(warnIdByEfmId==null) {
    		
    	}else {
    		//删除反馈表数据通过报警表IDs
    		String[] warnIdByEfmIdArr = warnIdByEfmId.split(",");
    		Integer delFeedBackByWarnIds = hixentArcEquipmentInfoMapper.delFeedBackByWarnIds(warnIdByEfmIdArr);
    	}
    	if(delEfmDevice>0) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    //删除设备Ruanyu
    
    public boolean deleteDevices(String[] equipIds) {
       
    	Integer delDevices = hixentArcEquipmentInfoMapper.delEquips(equipIds);
    	//获取报警表的ID 拼接字符串(通过终端Id)
    	String warnIdByEquipIds="";
        warnIdByEquipIds = hixentArcEquipmentInfoMapper.getWarnIdByEquipIds(equipIds);
      //获取报警表的ID 拼接字符串(通过终端Id)
        Integer delWarnByEquipIds = hixentArcEquipmentInfoMapper.delWarnByEquipIds(equipIds);
        if(warnIdByEquipIds==null) {
    		
    	}else {
    		//删除反馈表数据通过报警表IDs
    		String[] warnIdByEfmIdArr = warnIdByEquipIds.split(",");
    		Integer delFeedBackByWarnIds = hixentArcEquipmentInfoMapper.delFeedBackByWarnIds(warnIdByEfmIdArr);
    	}
    	if(delDevices>0) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    //删除项目
    public boolean deleteSite(String siteCode) {
    	//查询siteId
    	Integer siteId = hixentArcEquipmentInfoMapper.getSiteIdBySiteCode(siteCode);
    	
    	//获取项目下的中控ID 拼接字符串
    	String deviceIdsBySitecode="";
    	deviceIdsBySitecode = hixentArcEquipmentInfoMapper.getDeviceIdsBySitecode(siteCode);
    	//获取管控分组的ID 拼接字符串(通过项目Id)
    	
    	String groupIdBySiteCode = hixentArcEquipmentInfoMapper.getGroupIdBySiteCode(siteId);
    
        
    	if(ToolUtil.StringNotBlank(groupIdBySiteCode)) {
        	//删除管控分组(通过项目Id)
        	String[] groupIdBySiteCodeArr = groupIdBySiteCode.split(",");	
        	
        	Integer delGroupBySiteCode = hixentArcEquipmentInfoMapper.delGroupBySiteCode(siteId);
        
        	//删除用户-分组连接表
        	Integer delUserGruopLinkByGroupIds = hixentArcEquipmentInfoMapper.delUserGruopLinkByGroupIds(groupIdBySiteCodeArr);
        
        }
    	
        if(ToolUtil.StringNotBlank(deviceIdsBySitecode)) {
        	
        	
        	String[] deviceIdsBySitecodeArr = deviceIdsBySitecode.split(",");
        	//获取报警表的ID 拼接字符串(通过中控Id)
        	
        	String warnIdByEfmId = hixentArcEquipmentInfoMapper.getWarnIdByEfmId(deviceIdsBySitecodeArr);
        	//删除项目下的中控
        	Integer delDeviceBySiteCode = hixentArcEquipmentInfoMapper.delDeviceBySiteCode(siteCode);
        	//删除中控下的终端
        	//Integer delEquipByEfmId = hixentArcEquipmentInfoMapper.delEquipByEfmId(deviceIdsBySitecodeArr);
        	//删除报警表数据通过中控IDs
        	Integer delWarnByEfmId = hixentArcEquipmentInfoMapper.delWarnByEfmId(deviceIdsBySitecodeArr);
        	
        	
        	if(ToolUtil.StringNotBlank(warnIdByEfmId)) {
        		//删除反馈表数据通过报警表IDs
        		String[] warnIdByEfmIdArr = warnIdByEfmId.split(",");
        		Integer delFeedBackByWarnIds = hixentArcEquipmentInfoMapper.delFeedBackByWarnIds(warnIdByEfmIdArr);
        	}
        	
        }
      //删除项目下的终端
    	Integer delEquipBySiteCode = hixentArcEquipmentInfoMapper.delEquipBySiteCode(siteCode);

        //去除用户该项目
        Integer updateUserBySiteCode = hixentArcEquipmentInfoMapper.updateUserBySiteCode(siteCode+",");
        Integer delSite = hixentArcEquipmentInfoMapper.delSite(siteCode);
        if(delSite>0) {
        	return true;
    	}else {
    		return false;
    	}
    }
    
    //修改站点备注
    public boolean editSiteNiName(String niname,Integer siteId,Integer siteBuildId,String siteRemark) {
    	Integer editSiteNiName = hixentArcEquipmentInfoMapper.editSiteNiName(niname, siteId,siteBuildId,siteRemark);
    	boolean updateCount = ToolUtil.updateCount(editSiteNiName);
    	return updateCount;
    }
    
   //查询中控设备是否重复 
    public boolean selDeviceIdRepeat(String deviceCode,String siteCode,String deviceId) {
    	String selDeviceId = hixentArcEquipmentInfoMapper.selDeviceId(deviceCode, siteCode);
    	if(deviceId.equals(selDeviceId)) {
    		
    		return true;
    	}else {
    		return false;
    	}
    }
    //修改中控信息
    public boolean editDeviceInfo(String devicePlace,String niName,
    		String siteCode,String deviceId,String latitude,String longitute,
    		Integer offlineTime,Integer offlineEnable,String specificator) {
    	
    	Integer editDeviceInfo = hixentArcEquipmentInfoMapper.editDeviceInfo(devicePlace, niName, deviceId,latitude,longitute,offlineTime,offlineEnable,specificator);
    	return ToolUtil.updateCount(editDeviceInfo);
    	
    }
    
  //查询终端设备号是否重复 
    public boolean selEquipIdRepeat(String deviceCode,String newEquipCode,String siteCode,String equipCode ) {
    	String selEquipId = hixentArcEquipmentInfoMapper.selEquipId(deviceCode, siteCode, newEquipCode);
    	if(selEquipId==null ||selEquipId.length()==0 ||equipCode.equals(selEquipId)) {
    		return true;
    	}else {
    		return false;
    	}
    }
   //修改终端设备信息
    public boolean editEquipInfo(String equipCode,String equipPlace) {
    	Integer editEquipInfo = hixentArcEquipmentInfoMapper.editEquipInfo(equipCode,  equipPlace);
    	return ToolUtil.updateCount(editEquipInfo);
    	
    }
    //获取终端信息
    public HixentArcEquipmentInfo  getEquipInfoById(String eid) {
    	return hixentArcEquipmentInfoMapper.getEquipInfoById(eid);
    }
   // 查询项目是否有无线终端
    public String getMqttEquipBySite(String siteId) {
    	return	hixentArcEquipmentInfoMapper.getMqttEquipBySite(siteId);
    }
    //删除无线终端
    public Integer delMqttEquipBySiteCode(String siteCode) {
    	return	hixentArcEquipmentInfoMapper.delMqttEquipBySiteCode(siteCode);
    }
    //修改终端某字段
    public Integer updateEquipInfo(String field,String value) {
    	return	hixentArcEquipmentInfoMapper.updateEquipInfo(field,value);
    }
}