package com.qf.service.fire;



import java.util.Set;
import java.util.List;
import tk.mybatis.mapper.entity.Example;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.qf.mapper.fire.HixentArcEquipmentInfoMqttMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qf.model.fire.HixentArcEquipmentInfoMqtt;
import com.qf.util.ToolUtil;



/**
 * 设备详情相关服务
 * author Vareck
 */
@Service
public class HixentArcEquipmentInfoMqttService {
	
	
	
	@Autowired
    private HixentArcEquipmentInfoMqttMapper hixentArcEquipmentInfoMqttMapper;
	
	//@Autowired
    //private HixentArcEquipmentInfoMqttMqttMapper hixentArcEquipmentInfoMqttMqttMapper;	
	
    //根据虚拟分组id获取设备号
    public List<HixentArcEquipmentInfoMqtt> getDeviceListByProjectId(String[] pid) {
    	return hixentArcEquipmentInfoMqttMapper.getDeviceListByProjectId(pid);
    }
    
	//获取列表信息(app通过虚拟分组)
    public List<HixentArcEquipmentInfoMqtt> getAllDeviceList(Integer pid) {
    	return hixentArcEquipmentInfoMqttMapper.getAllDeviceList(pid);
    }
    public List<HixentArcEquipmentInfoMqtt> getPageDeviceList(Integer pid,Integer limit,Integer page,String order) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMqttMapper.getPageDeviceList(order,limits);
    }
	//获取列表信息(后台通过站点)
    public List<HixentArcEquipmentInfoMqtt> getAllDeviceListByBid(Set bid,Integer efmId) {
    	return hixentArcEquipmentInfoMqttMapper.getAllDeviceListByBid(bid,efmId);
    }
  //获取mqtt列表信息(后台通过站点)
    public List<HixentArcEquipmentInfoMqtt> getAllMqttDeviceList() {
    	return hixentArcEquipmentInfoMqttMapper.getAllMqttDeviceList();
    }
    
  //获取mqtt列表信息(后台通过站点)
    public List<HixentArcEquipmentInfoMqtt> getCommonMqttList(Set allDid) {
    	return hixentArcEquipmentInfoMqttMapper.getCommonMqttList(allDid);
    }
    
  //获取mqtt分页列表信息(后台通过站点)
    public List<HixentArcEquipmentInfoMqtt> getPageCommonMqttList(Set allDid,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMqttMapper.getPageCommonMqttList(allDid,limits);
    }
    
    
    
    
  //获取mqtt分页列表信息(后台通过站点)
    public List<HixentArcEquipmentInfoMqtt> getPageMqttDeviceList(Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMqttMapper.getPageMqttDeviceList(limits);
    }
    
    
    
    
    public List<HixentArcEquipmentInfoMqtt> getPageDeviceListByBid(Set bid,Integer efmId,Integer limit,Integer page,String order,String lineCode) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMqttMapper.getPageDeviceListByBid(bid,efmId,order,limits,lineCode);
    }
	//获取详细信息
    public HixentArcEquipmentInfoMqtt selectOne(HixentArcEquipmentInfoMqtt hixentArcEquipmentInfoMqtt) {
    	return hixentArcEquipmentInfoMqttMapper.selectOne(hixentArcEquipmentInfoMqtt);
    }

  

    public HixentArcEquipmentInfoMqtt getOne(String eid) {
    	return hixentArcEquipmentInfoMqttMapper.getOne(eid);
    }
    
    public List<HixentArcEquipmentInfoMqtt> getOneList(String eid) {
    	return hixentArcEquipmentInfoMqttMapper.getOneList(eid);
    }
    
    //删除
    public int delete(HixentArcEquipmentInfoMqtt hixentArcEquipmentInfoMqtt) {
    	return hixentArcEquipmentInfoMqttMapper.delete(hixentArcEquipmentInfoMqtt);
    }
    
    //新增
    public void insertMqtt(String id,String module_code, String line_code,String device_code, String site_code,String addr){
    	hixentArcEquipmentInfoMqttMapper.insertMqtt(id, module_code,line_code,device_code, site_code,addr);
    }

    //update
    public void UpdateMqtt(String fieldname,String fieldvalue,String id){
    	hixentArcEquipmentInfoMqttMapper.UpdateMqtt(fieldname,fieldvalue,id);
    }
    
    //新增
    public int insert(HixentArcEquipmentInfoMqtt data){
    	return hixentArcEquipmentInfoMqttMapper.insert(data);
    }

    //更新
    public void update(HixentArcEquipmentInfoMqtt hixentArcEquipmentInfoMqtt, Example example){
    	hixentArcEquipmentInfoMqttMapper.updateByExample(hixentArcEquipmentInfoMqtt,example);
    }
    //获取主机下终端列表
    public List<HixentArcEquipmentInfoMqtt> getPageEfmTerminalList(String efmId,String lineCode,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcEquipmentInfoMqttMapper.getPageEfmTerminalList(efmId,lineCode,limits);
    }
    
  //获取主机下终端
    public List<HixentArcEquipmentInfoMqtt> getAllEfmTerminalList(String efmId,String lineCode) {
    	return hixentArcEquipmentInfoMqttMapper.getAllEfmTerminalList(efmId,lineCode);
    }
    
    
    //获取主机下终端总数
    public int getEfmTerminalCount(String efmId,String lineCode) {
    	return hixentArcEquipmentInfoMqttMapper.getEfmTerminalCount(efmId,lineCode).intValue();
    }
    
    //查询项目通过项目ID
    public String getsiteCodeBysiteCode(String siteCode) {
    	return hixentArcEquipmentInfoMqttMapper.getSiteCodeBySiteCode(siteCode);
    }
    //插入项目
    public Integer addSite(String siteCode) {
    	return	hixentArcEquipmentInfoMqttMapper.addSite(siteCode);
    }
    //查询无线设备通过无线设备ID
    public HixentArcEquipmentInfoMqtt selEquipMqttByEquipId(String equipId) {
    	return hixentArcEquipmentInfoMqttMapper.selEquipMqttByEquipId(equipId);
    }
    //更新无线信息
    public Integer updateEquipMqtt(String equipId,String message,String  field) {
    	return	hixentArcEquipmentInfoMqttMapper.updateEquipMqtt(equipId,message,field);
    }
    
    //获取无线分页数据
    public JSONObject getPageMqttEquipListData(String siteCode,Integer equipmentCategory,
    		Integer currentPage,Integer pageSize) {
    	String limits="";
    	if(currentPage==0 && pageSize==0) {
    		limits="";
    	}else {
    		limits = " "+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+" ";
    	}
    	List<HixentArcEquipmentInfoMqtt> pageMqttEquipList = hixentArcEquipmentInfoMqttMapper.getPageMqttEquipList(siteCode,equipmentCategory,limits);
    	Integer pageMqttEquipCount = hixentArcEquipmentInfoMqttMapper.getPageMqttEquipCount(siteCode,equipmentCategory);
    	for (int i = 0; i < pageMqttEquipList.size(); i++) {
    		pageMqttEquipList.get(i).setMessage(ToolUtil.formatDevStr(pageMqttEquipList.get(i).getMessage()));
    		pageMqttEquipList.get(i).setLongitude(ToolUtil.formatDevStr(pageMqttEquipList.get(i).getLongitude()));
    		pageMqttEquipList.get(i).setLatitude(ToolUtil.formatDevStr(pageMqttEquipList.get(i).getLatitude()));
		}
    	JSONObject outJson = new JSONObject();
    	outJson.put("total", pageMqttEquipCount);
    	outJson.put("pageList", pageMqttEquipList);
    	return outJson;
    }
    
 //删除设备Ruanyu
    
    public boolean delMqttEquips(String[] equipIds) {
       
    	Integer delDevices = hixentArcEquipmentInfoMqttMapper.delMqttEquips(equipIds);
    	//获取报警表的ID 拼接字符串(通过终端Id)
    	String warnIdByEquipIds="";
        warnIdByEquipIds = hixentArcEquipmentInfoMqttMapper.getWarnIdByEquipIds(equipIds);
      //获取报警表的ID 拼接字符串(通过终端Id)
        Integer delWarnByEquipIds = hixentArcEquipmentInfoMqttMapper.delWarnByEquipIds(equipIds);
        if(warnIdByEquipIds==null) {
    		
    	}else {
    		//删除反馈表数据通过报警表IDs
    		String[] warnIdByEfmIdArr = warnIdByEquipIds.split(",");
    		Integer delFeedBackByWarnIds = hixentArcEquipmentInfoMqttMapper.delFeedBackByWarnIds(warnIdByEfmIdArr);
    	}
    	if(delDevices>0) {
    		return true;
    	}else {
    		return false;
    	}
    	
    }
    //删除终端
    public boolean delMqttEquipsBySiteCode(String siteCode) {
    	 //获取项目下无线终端
        String mqttEquipBySiteCode = hixentArcEquipmentInfoMqttMapper.getMqttEquipBySiteCode(siteCode);
        if(ToolUtil.StringNotBlank(mqttEquipBySiteCode)) {
        	String[] mqttEquipBySiteCodeArr = mqttEquipBySiteCode.split(",");
        	
        	//获取报警表的ID 拼接字符串(通过终端Id)
        	String warnIdByEquipIds="";
            warnIdByEquipIds = hixentArcEquipmentInfoMqttMapper.getWarnIdByEquipIds(mqttEquipBySiteCodeArr);
           //获取报警表的ID 拼接字符串(通过终端Id)
             hixentArcEquipmentInfoMqttMapper.delWarnByEquipIds(mqttEquipBySiteCodeArr);
            if(warnIdByEquipIds==null) {
        		
        	}else {
        		//删除反馈表数据通过报警表IDs
        		String[] warnIdByEfmIdArr = warnIdByEquipIds.split(",");
        		 hixentArcEquipmentInfoMqttMapper.delFeedBackByWarnIds(warnIdByEfmIdArr);
        	}
            Integer delMqttEquipBySiteCode = hixentArcEquipmentInfoMqttMapper.delMqttEquipBySiteCode(siteCode);
        	if(delMqttEquipBySiteCode>0) {
        		return true;
        	}
        }
        return false;
    }
}