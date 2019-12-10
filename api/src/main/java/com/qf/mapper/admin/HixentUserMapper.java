package com.qf.mapper.admin;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentProvince;
import com.qf.model.admin.HixentRole;
import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentUser;
import com.qf.util.CustomerMapper;



/**
 * author Vareck
 */
@Service
public interface HixentUserMapper extends CustomerMapper<HixentUser> {
	
	/*列表*/
	List<HixentUser> selectBySystem();
	
	
	
	List<HixentUser> getUserAllList( 
		@Param("name")          String    name,
		@Param("id")          Integer    id,
		@Param("roleType")   Integer    roleType
	);
	List<HixentUser> getUserPageList( 
		@Param("name")          String    name,
		@Param("order")         String    order,
		@Param("limits")        String    limits,
		@Param("id")          Integer    id,
		@Param("roleType")   Integer    roleType
	);
	Integer getUserPageCount(
			@Param("name")          String    name,
			@Param("id")          Integer    id,
			@Param("roleType")   Integer    roleType
	);
	
	
	
	
	//删除
	void deleteUser(List<Integer> uidList);
	HixentUser selectByUsername(String username);
	HixentUser getUserinfoByMobile(String mobile);
	void updateToken(@Param("uid") String uid,@Param("access_token") String  access_token);
	HixentUser selectByUserId(String uid);
	HixentUser getAdminById(Integer id);
	//新增
	void insertUser( 
		@Param("uid")          String    uid,
		@Param("account")      String   account,
		@Param("password")     String   password,
		@Param("mobile")       String   mobile,
		@Param("salt")         String    salt,
		@Param("cid")          Integer    cid,
		@Param("fireRole")     Integer    fireRole,
		@Param("ctime")        long    ctime,
		@Param("utime")        long    utime,
		@Param("bid")          String    bid,
		@Param("provinceId")   Integer  provinceId,
		@Param("areaId")       Integer    areaId,
		@Param("remark")       String    remark,
		@Param("pid")          Integer    pid
    );
	
	//编辑
	void updateUser( 
		@Param("id")           Integer   id,
		@Param("account")      String   account,
		@Param("mobile")       String   mobile,
		@Param("fireRole")     Integer    fireRole,
		@Param("utime")        long    utime,
		@Param("bid")        String    bid,
		@Param("provinceId")   Integer  provinceId,
		@Param("areaId")       Integer    areaId,
		@Param("remark")        String    remark
    );
	//修改密码
	void updateUserPassWord(
		@Param("id")           Integer   id,
		@Param("password")     String  	 password
	);
	
	//获取省份
	List<HixentProvince> getPrinvince(@Param("privinceId")Integer privinceId);
	//获取地区
	List<HixentArea> getCity(@Param("privinceId")Integer privinceId);
	//获取角色
	List<HixentRole> getRole(@Param("roleType")Integer roleType);
	//联动获取项目
	List<HixentSite> getSite(@Param("provinceId")Integer provinceId,@Param("areaId")Integer areaId,@Param("siteCordArr")String[] siteCordArr);

	//通过地区ID获取地区
	List<HixentArea> getCityByAreaId(@Param("areaId")Integer areaId);
	//获取角色类型
	Integer getRoleType(@Param("roleId")Integer roleId);
	
	List<HixentSite> getsiteList(@Param("siteCordArr")String[] siteCordArr);

    

}

