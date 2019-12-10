package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import com.qf.model.fire.HixentArcLog;
import com.qf.mapper.fire.HixentArcLogMapper;
import java.util.List;



/**
 * 设备心跳包数据相关服务
 * author Vareck
 */
@Service
public class HixentArcLogService {
	
	
	
	@Autowired
    private HixentArcLogMapper hixentArcLogMapper;
	
	
	
	public HixentArcLog getOne(String eid){
		return hixentArcLogMapper.getOne(eid);
	}
    
    public Integer isTable(String eid) {
    	List table = hixentArcLogMapper.existTable(eid);
		Integer n  = table.size();
		return n;
    }
	
    //删除
    public int delete(HixentArcLog hixentArcLog) {
    	return hixentArcLogMapper.delete(hixentArcLog);
    }
    
    //新增
    public int insert(HixentArcLog data){
    	return hixentArcLogMapper.insert(data);
    }
    
    //更新
    public void update(HixentArcLog hixentArcLog, Example example){
    	hixentArcLogMapper.updateByExample(hixentArcLog,example);
    }
    
    
    
}