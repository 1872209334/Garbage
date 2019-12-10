package com.qf.service.app;



import com.qf.util.DateUtil;

import tk.mybatis.mapper.entity.Example;

import com.qf.mapper.admin.HixentAppUserMapper;
import com.qf.model.admin.HixentAppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;



/**
 * 手机app用户相关服务
 * author Vareck
 */
@Service
public class HixentAppUserService {

	
	
    @Autowired
    private HixentAppUserMapper hixentAppUserMapper;


    public Integer getCount(Example example){
        return hixentAppUserMapper.selectCountByExample(example);
    }
    
    /*用户列表*/
    public List<HixentAppUser> getAppUserAllList( Integer statek,String name,String mobile ) {
    	return hixentAppUserMapper.getAppUserAllList( statek,name,mobile );
    }
    //用户列表分页
    public List<HixentAppUser> getAppUserPageList( Integer statek,String name,String order,Integer limit,Integer page,Integer roleType,Integer adminId ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentAppUserMapper.getAppUserPageList( statek,name,order,limits,roleType,adminId );
    }
    //用户列表总数
    public Integer getAppUserPageCount(Integer statek,String name,Integer roleType,Integer adminId ) {
    return	hixentAppUserMapper.getAppUserPageCount(statek, name, roleType, adminId);
    	
    }
    public List<HixentAppUser> getAppUserAllList2( Integer statek,Integer userId,String name,String mobile ) {
    	return hixentAppUserMapper.getAppUserAllList2( statek,userId,name,mobile );
    }
    public List<HixentAppUser> getAppUserPageList2( Integer statek,Integer userId,String name,String mobile,String order,Integer limit,Integer page ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentAppUserMapper.getAppUserPageList2( statek,userId,name,mobile,order,limits );
    }
    
    public HixentAppUser getAppAdmin(HixentAppUser hixentAppUser){
        return hixentAppUserMapper.selectOne(hixentAppUser);
    }
    
    public HixentAppUser getAppAdminById( Integer id ){
        return hixentAppUserMapper.getAppAdminById(id);
    }
    
    public HixentAppUser selectOne( HixentAppUser hixentAppUser ){
        return hixentAppUserMapper.selectOne(hixentAppUser);
    }

    public HixentAppUser findByAppUserId(String uid) {
        return hixentAppUserMapper.selectByAppUserId(uid);
    }
    
    public HixentAppUser findByAppUsername(String username) {
        return hixentAppUserMapper.selectByAppUsername(username);
    }
    
    public List<HixentAppUser> selectByPid(Integer id){
        return hixentAppUserMapper.selectByPid(id);
    }
    
    public HixentAppUser getAppUserinfoByMobile(String mobile){
    	return hixentAppUserMapper.getAppUserinfoByMobile(mobile);
    }
    //通過手机号或者用户名获取app用户
    public HixentAppUser getAppUserByMobileOrUserName(String mobOrName) {
    	
    	return hixentAppUserMapper.getAppUserByMobileOrUserName(mobOrName);
    }
    
    //添加
    public void insertAppUser( String uid,String account,String password,String mobile,String email,String salt,Integer cid,Integer pid ) {
    	long ctime = DateUtil.getTimeNumberToday();
        long utime = DateUtil.getTimeNumberToday();	
    	hixentAppUserMapper.insertAppUser( uid,account,password,mobile,email,salt,cid,pid,ctime,utime );
    }
    
    //管理员添加app用户
    public void addAppUser( String account,String salt,String password,String mobile,String email,Integer state,Integer pid,String uid,String remark ) throws Exception {
    	
      //获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long utime = DateUtil.getTimestamp(currentDate);
    	long ctime = DateUtil.getTimestamp(currentDate);
    	hixentAppUserMapper.addAppUser( account,salt,password,mobile,email,state,pid,ctime,utime,uid,remark );
    }
    
    
    
    //管理员编辑app用户
    public void updateAppUser( Integer id,String account,String mobile,String email,String remark ) throws Exception {
    	//获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long utime = DateUtil.getTimestamp(currentDate);
    	hixentAppUserMapper.updateAppUser( id,account,mobile,email,utime,remark );
    }
    
    //删除
    public void deleteAppUser( String[] mid_arr ){
    	
    	hixentAppUserMapper.deleteAppUser( mid_arr );
    	//删除管控分组
    	hixentAppUserMapper.deleteGroupUserLink( mid_arr );
    }
    
    
    
    //修改密码
    public void updateAppUserPassWord( Integer id , String password ){
    	hixentAppUserMapper.updateAppUserPassWord( id,password );
    }
    
    //修改手机号
    public Integer updateAppUserMobile( Integer id , String mobile ){
    	return	hixentAppUserMapper.updateAppUserMobile( id,mobile );
    }
    
    public void insert(HixentAppUser hixentAppUser){
    	hixentAppUserMapper.insert(hixentAppUser);
    }
    
    public void update(HixentAppUser hixentAppUser, Example example){
    	hixentAppUserMapper.updateByExample(hixentAppUser,example);
    }
    
    //审核管控人员
    
    public Integer auditAppUser(Integer id) {
    	return hixentAppUserMapper.auditAppUser(id);
    }
    
    
    
}