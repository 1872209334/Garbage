package com.qf.mapper.admin;



import com.qf.model.admin.HixentCompany;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentCompanyMapper extends CustomerMapper<HixentCompany> {
	List<HixentCompany> getSelectList();
	List<HixentCompany> getCompanyAllList( 
		@Param("name")          String    name
	);
	List<HixentCompany> getCompanyPageList( 
		@Param("name")          String    name,
		@Param("order")         String    order,
		@Param("limits")        String    limits
	);
}