package com.qf.mapper.admin;



import com.qf.model.admin.HixentPermissions;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentPermissionsMapper extends CustomerMapper<HixentPermissions> {
	
	/*权限*/
	List<HixentPermissions> getPermissionsAllList( 
		@Param("name")          String   name
	);
	List<HixentPermissions> getPermissionsAllList2( 
		@Param("name")          String   name
	);
	List<HixentPermissions> getPermissionsPageList( 
		@Param("name")          String   name,
		@Param("order")         String   order,
		@Param("limits")        String   limits
	);
	//新增
	void insertPermissions(
		@Param("menuIcon")      String   menuIcon,
		@Param("menuUrl")       String   menuUrl,
		@Param("parentId")      Integer  parentId,
		@Param("actionName")    String   actionName,
		@Param("menuName")      String   menuName,
		@Param("setOrder")      Integer  setOrder
	);
	//更新
	void updatePermissions(
		@Param("menuId")        Integer  menuId,
		@Param("menuIcon")      String   menuIcon,
		@Param("menuUrl")       String   menuUrl,
		@Param("parentId")      Integer  parentId,
		@Param("actionName")    String   actionName,
		@Param("menuName")      String   menuName,
		@Param("setOrder")      Integer  setOrder
	);
	//删除
	void deletePermissions( List<Integer> pidList );
	
}