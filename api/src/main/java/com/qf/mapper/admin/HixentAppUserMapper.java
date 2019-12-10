package com.qf.mapper.admin;



import com.qf.model.admin.HixentAppUser;
import com.qf.util.CustomerMapper;
import org.springframework.stereotype.Service;
import org.apache.ibatis.annotations.Param;
import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentAppUserMapper extends CustomerMapper<HixentAppUser> {
	
	
	
	//根据虚拟分组，获取用户
	List<HixentAppUser> getDataByPid( @Param("pid") String pid );
	
	/*列表*/
	List<HixentAppUser> getAppUserAllList( 
	    @Param("statek")        Integer   statek,
		@Param("name")          String    name,
		@Param("mobile")        String    mobile
	);
	List<HixentAppUser> getAppUserPageList( 
		@Param("statek")        Integer   statek,
		@Param("name")          String    name,
		@Param("order")         String    order,
		@Param("limits")        String    limits,
		@Param("roleType")      Integer    roleType,
		@Param("adminId")       Integer    adminId
	);
	Integer getAppUserPageCount(
			@Param("statek")        Integer   statek,
			@Param("name")          String    name,
			@Param("roleType")      Integer    roleType,
			@Param("adminId")       Integer    adminId	
			
	);
	
	
	List<HixentAppUser> getAppUserAllList2( 
	    @Param("statek")        Integer   statek,
		@Param("userId")        Integer   userId,
		@Param("name")          String    name,
		@Param("mobile")        String    mobile
	);
	List<HixentAppUser> getAppUserPageList2( 
		@Param("statek")        Integer   statek,
	    @Param("userId")        Integer   userId,
		@Param("name")          String    name,
		@Param("mobile")        String    mobile,
		@Param("order")         String    order,
		@Param("limits")        String    limits
	);
	
	HixentAppUser selectByAppUsername(String username);
	List<HixentAppUser> selectByPid(Integer id);
	HixentAppUser getAppUserinfoByMobile(String mobile);
	HixentAppUser selectByAppUserId(String uid);
	HixentAppUser getAppAdminById( @Param("id") Integer id );
	//通過手机号或者用户名获取app用户
	HixentAppUser getAppUserByMobileOrUserName( @Param("mobOrName")String mobOrName);
	//注册
	void insertAppUser( 
		@Param("uid")          String     uid,
		@Param("account")      String     account,
		@Param("password")     String     password,
		@Param("mobile")       String     mobile,
		@Param("email")        String     email,
		@Param("salt")         String     salt,
		@Param("cid")          Integer    cid,
		@Param("pid")     	   Integer    pid,
		@Param("ctime")        long       ctime,
		@Param("utime")        long       utime
    );
	
	//新增
		void addAppUser( 
				@Param("account")      String     account,
				@Param("salt")         String     salt,
				@Param("password")     String     password,
				@Param("mobile")       String     mobile,
				@Param("email")        String     email,
				@Param("state")          Integer    state,
				@Param("pid")     	   Integer    pid,
				@Param("ctime")        long       ctime,
				@Param("utime")        long       utime,
				@Param("uid")          String     uid,
				@Param("remark")          String     remark
	    );
	
	
	
	//编辑
	void updateAppUser( 
		@Param("id")           Integer    id,
		@Param("account")      String     account,
		@Param("mobile")       String     mobile,
		@Param("email")        String     email,
		@Param("utime")        long    	  utime,
		@Param("remark")    String     remark
    );
	
	//删除
	void deleteAppUser(@Param("list") String[]  mid_arr );
	
	void deleteGroupUserLink(@Param("list") String[]  mid_arr );
	//修改密码
	void updateAppUserPassWord(
		@Param("id")           Integer   id,
		@Param("password")     String  	 password
	);
	//修改手机号
	Integer updateAppUserMobile(
			@Param("id")           Integer   id,
			@Param("mobile")     String  	 mobile
		);
	//审核
	Integer auditAppUser(@Param("appUserId")   Integer   appUserId);
	
}