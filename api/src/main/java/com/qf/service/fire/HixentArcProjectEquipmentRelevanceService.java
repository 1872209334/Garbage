package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcProjectEquipmentRelevance;
import com.qf.mapper.fire.HixentArcProjectEquipmentRelevanceMapper;



/**
 * 虚拟分组和设备对应关系相关服务
 * author Vareck
 */
@Service
public class HixentArcProjectEquipmentRelevanceService {
	
	
	
	@Autowired
    private HixentArcProjectEquipmentRelevanceMapper hixentArcProjectEquipmentRelevanceMapper;
	
	
	
	//获取详细信息
    public HixentArcProjectEquipmentRelevance selectOne(HixentArcProjectEquipmentRelevance hixentArcProjectEquipmentRelevance) {
    	return hixentArcProjectEquipmentRelevanceMapper.selectOne(hixentArcProjectEquipmentRelevance);
    }
    
    
    public void insert(HixentArcProjectEquipmentRelevance hixentArcProjectEquipmentRelevance) {
    	hixentArcProjectEquipmentRelevanceMapper.insert(hixentArcProjectEquipmentRelevance);
    }
	

}