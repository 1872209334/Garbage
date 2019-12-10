package com.qf.mapper.fire;



import com.qf.model.fire.HixentArcLog;
import com.qf.util.CustomerMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;



/**
 * author Vareck
 */
@Service
public interface HixentArcLogMapper extends CustomerMapper<HixentArcLog> {
	
	//判断表是否存在
	List existTable( @Param("eid") String  eid );
	HixentArcLog getOne( @Param("eid") String  eid );

}