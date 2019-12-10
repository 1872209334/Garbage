package com.qf.mapper.fire;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcControlGroup;
import com.qf.model.fire.HixentArcEquipmentInfo;
import com.qf.util.CustomerMapper;



/**
 * author RuanYu
 */
@Service
public interface HixentArcControllGroupMapper extends CustomerMapper<HixentArcControlGroup> {
	
	
	

	/*管控分组列表*/
	List<HixentArcControlGroup> getControllGroupList( 
			@Param("limits") String   limits,
		@Param("siteId")   Integer    siteId,
		@Param("uid") String   uid,
		@Param("adminId") Integer   adminId
	);
	
	Integer getControllGroupCount(
			@Param("siteId")   Integer    siteId,
			@Param("uid") String   uid,
			@Param("adminId") Integer   adminId
	);
	List<HixentAppUser> getAppUser(   Integer  adminId);
	
	Integer getGroupIdFromAdd(HixentArcControlGroup hs);
	
	Integer addUserGroupLink(@Param("arrUser")String[] arrUser,@Param("groupId")Integer groupId);
	
	Integer updateEquipInfo(@Param("arrEquip")String[] arrEquip,@Param("groupId")Integer groupId);

	Integer deleteUserGroupLink(@Param("groupId")Integer  groupId);
	
	Integer updateEquipGroupId(@Param("groupId")Integer  groupId);
	
	Integer delGroup(@Param("groupId")Integer  groupId);
	
	Integer updateGroup(@Param("groupId")Integer  groupId,@Param("groupPlace")String  groupPlace);
}