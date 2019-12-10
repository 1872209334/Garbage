package com.qf.mapper.fire;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcWarningList;
import com.qf.util.CustomerMapper;



/**
 * author RuanYu
 */
@Service
public interface HixentArcAlarmLogMapper extends CustomerMapper<HixentArcEfmDevice> {
	

	
 List<HixentArcWarningList>	alarmLogForEquip();
}