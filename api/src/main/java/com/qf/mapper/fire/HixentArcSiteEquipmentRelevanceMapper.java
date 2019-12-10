package com.qf.mapper.fire;



import com.qf.model.fire.HixentArcSiteEquipmentRelevance;
import com.qf.util.CustomerMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;



/**
 * author Vareck
 */
@Service
public interface HixentArcSiteEquipmentRelevanceMapper extends CustomerMapper<HixentArcSiteEquipmentRelevance> {

	List<HixentArcSiteEquipmentRelevance> getDataByBid(String[]  bid);

}