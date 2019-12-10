package com.qf.mapper.admin;



import com.qf.model.admin.HixentPermissionsRole;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentPermissionsRoleMapper extends CustomerMapper<HixentPermissionsRole> {
	

	/*智能消防*/
	HixentPermissionsRole selectForData( String uid );
	List<HixentPermissionsRole> findActionNameByMenuId( String[] menuId );
	List<HixentPermissionsRole> selectAllData();
	
	//获取3级权限
	List<HixentPermissionsRole> getThirdPermissions();
	
	//获取3级权限
	List<HixentPermissionsRole> getThirdPermissionsBybidId(@Param("menuId")String[] menuId);
}