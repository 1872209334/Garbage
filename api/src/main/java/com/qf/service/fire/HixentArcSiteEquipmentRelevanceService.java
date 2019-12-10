package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qf.model.fire.HixentArcSiteEquipmentRelevance;
import com.qf.mapper.fire.HixentArcSiteEquipmentRelevanceMapper;
import tk.mybatis.mapper.entity.Example;
import java.util.List;



/**
 * 站点分组和设备对应关系相关服务
 * author Vareck
 */
@Service
public class HixentArcSiteEquipmentRelevanceService {
	
	
	
	@Autowired
    private HixentArcSiteEquipmentRelevanceMapper hixentArcSiteEquipmentRelevanceMapper;
	
	
	
	//获取详细信息
    public HixentArcSiteEquipmentRelevance selectOne(HixentArcSiteEquipmentRelevance hixentArcSiteEquipmentRelevance) {
    	return hixentArcSiteEquipmentRelevanceMapper.selectOne(hixentArcSiteEquipmentRelevance);
    }
    
    
    
    public void insert(HixentArcSiteEquipmentRelevance hixentArcSiteEquipmentRelevance) {
    	hixentArcSiteEquipmentRelevanceMapper.insert(hixentArcSiteEquipmentRelevance);
    }
	
    
    
    public List<HixentArcSiteEquipmentRelevance> getDataByBid(String[] bid){
    	return hixentArcSiteEquipmentRelevanceMapper.getDataByBid(bid);
    }

    
	
}