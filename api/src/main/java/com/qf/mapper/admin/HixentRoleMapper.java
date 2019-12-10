package com.qf.mapper.admin;


import com.qf.model.admin.HixentRole;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentRoleMapper extends CustomerMapper<HixentRole> {
	
	/*角色*/
	List<HixentRole> getRoleAllList( @Param("name") String name );
	List<HixentRole> getRoleAllList2( @Param("name") String name );
	List<HixentRole> getRolePageList( 
		@Param("name")          String   name,
		@Param("order")         String   order,
		@Param("limits")        String   limits
	);
	HixentRole selectRoleInfo( 
		@Param("roleId")        Integer  roleId
	);
	void insertRole( 
		@Param("role_name")        String   role_name,
		@Param("menu_id_list")     String   menu_id_list,
		@Param("role_desc")        String   role_desc
	);
	void updateRole( 
		@Param("roleId")           Integer  roleId,
		@Param("role_name")        String   role_name,
		@Param("menu_id_list")     String   menu_id_list,
		@Param("role_desc")        String   role_desc
	);
	void deleteRole( List<Integer> ridList );
}