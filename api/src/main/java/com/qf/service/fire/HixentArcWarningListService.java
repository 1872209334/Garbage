package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.qf.model.fire.HixentArcWarningList;
import com.qf.util.DateUtil;
import com.qf.mapper.fire.HixentArcWarningListMapper;
import java.util.List;
import java.util.Set;



/**
 * 告警通知信息相关服务
 * author Vareck
 */
@Service
public class HixentArcWarningListService {
	
	
	
	@Autowired
    private HixentArcWarningListMapper hixentArcWarningListMapper;
	
	
	
    //根据设备id获取所有信息
    public List<HixentArcWarningList> getByDeviceId(String eid) {
    	return hixentArcWarningListMapper.getByDeviceId(eid);
    }
	//手机app火灾告警列表
    public List<HixentArcWarningList> getAllByDeviceId(List<String> eidList) {
    	return hixentArcWarningListMapper.getAllByDeviceId(eidList);
    }
    //获取所有设备报警列表
    public List<HixentArcWarningList> getAdminWarning(long time1,long time2) {
    	return hixentArcWarningListMapper.getAdminWarning(time1,time2);
    }
    public List<HixentArcWarningList> getCommonWarning(long time1,long time2,Set bid) {
    	return hixentArcWarningListMapper.getCommonWarning(time1,time2,bid);
    }
    
  
    
    
    
    //处理报警项
    public void dealWarningList(String id) {
    	long dtime = DateUtil.getTimeNumberToday();
    	hixentArcWarningListMapper.dealWarningList(id,dtime);
    }
    
  //获取所有设备分页报警列表
    public List<HixentArcWarningList> getPageAdminWarning(Integer limit,Integer page,long time1,long time2) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcWarningListMapper.getPageAdminWarning(limits,time1,time2);
    }
    
    public List<HixentArcWarningList> getPageCommonWarning(Integer limit,Integer page,long time1,long time2,Set bid) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcWarningListMapper.getPageCommonWarning(limits,time1,time2,bid);
    }
    
    public List<HixentArcWarningList> getPageByDeviceId(List<String> eidList,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcWarningListMapper.getPageByDeviceId(eidList,limits);
    }
    
	//手机app设备故障告警列表（未处理）
    public List<HixentArcWarningList> getAllByDeviceId2(List<String> eidList) {
    	return hixentArcWarningListMapper.getAllByDeviceId2(eidList);
    }
    public List<HixentArcWarningList> getPageByDeviceId2(List<String> eidList,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcWarningListMapper.getPageByDeviceId2(eidList,limits);
    }
    //手机app设备故障告警列表（已处理）
    public List<HixentArcWarningList> getAllByDeviceId4(List<String> eidList) {
    	return hixentArcWarningListMapper.getAllByDeviceId4(eidList);
    }
    
	//手机app设备异常历史记录列表
    public List<HixentArcWarningList> getAllByDeviceId3(List<String> eidList,Integer time1,Integer time2) {
    	return hixentArcWarningListMapper.getAllByDeviceId3(eidList,time1,time2);
    }
    public List<HixentArcWarningList> getPageByDeviceId3(List<String> eidList,Integer time1,Integer time2,Integer limit,Integer page) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentArcWarningListMapper.getPageByDeviceId3(eidList,time1,time2,limits);
    }
    
    //获取所有信息
    public List<HixentArcWarningList> selectAll() {
    	return hixentArcWarningListMapper.selectAll();
    }
	
	//获取详细信息
    public HixentArcWarningList selectOne(HixentArcWarningList hixentArcWarningList) {
    	return hixentArcWarningListMapper.selectOne(hixentArcWarningList);
    }
    public HixentArcWarningList getOneByWid(Integer wid){
    	return hixentArcWarningListMapper.getOneByWid(wid);
    }
    
    //删除
    public int delete(HixentArcWarningList hixentArcWarningList) {
    	return hixentArcWarningListMapper.delete(hixentArcWarningList);
    }
    
    //新增
    public int insert(HixentArcWarningList data){
    	return hixentArcWarningListMapper.insert(data);
    }
    
    //更新
    public void update(HixentArcWarningList hixentArcWarningList, Example example){
    	hixentArcWarningListMapper.updateByExample(hixentArcWarningList,example);
    }
    
    
    
}