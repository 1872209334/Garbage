package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcProtocol;
import com.qf.mapper.fire.HixentArcProtocolMapper;



/**
 * 设备心跳包数据相关服务
 * author Vareck
 */
@Service
public class HixentArcProtocolService {
	
	
	
	@Autowired
    private HixentArcProtocolMapper hixentArcProtocolMapper;
	


	//获取详细信息
	public HixentArcProtocol selectOne(HixentArcProtocol data){
		return hixentArcProtocolMapper.selectOne(data);
	}
    
	
	//获取详细信息
	public HixentArcProtocol selectByName(String name){
		return hixentArcProtocolMapper.selectByName(name);
	}
	
    
    //新增
    public void insert(HixentArcProtocol data){
    	hixentArcProtocolMapper.insert(data);
    }

    
	
}