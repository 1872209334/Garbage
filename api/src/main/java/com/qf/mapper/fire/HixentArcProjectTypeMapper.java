package com.qf.mapper.fire;



import com.qf.model.fire.HixentArcProjectType;
import com.qf.util.CustomerMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;



/**
 * author wjr
 */
@Service
public interface HixentArcProjectTypeMapper extends CustomerMapper<HixentArcProjectType> {
	
    //根据分组id获取所有信息
	List<HixentArcProjectType> getAllByProjectId(String[] pid);
	HixentArcProjectType getOne(Integer pid);
	
	List<HixentArcProjectType> getProjectSize(
			@Param("id")        Integer       id
	);
	
	List<HixentArcProjectType> getPageProject(
			@Param("id")        Integer       id,
			@Param("limits")        String       limits
	);
	
	List<HixentArcProjectType> checkName(
			@Param("name")     String       name
	);
	
	void insertProject(
			@Param("name")        String       name,
			@Param("address")        String       address,
			@Param("id")        Integer       id,
			@Param("phone")        String       phone
	);
	
	void updateProject(
			@Param("name")        String       name,
			@Param("address")        String       address,
			@Param("phone")        String       phone,
			@Param("type")        Integer       type
	);
	
	
	
	
	

}