package com.qf.service.admin;



import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qf.mapper.admin.HixentPermissionsRoleMapper;
import com.qf.mapper.admin.HixentUserMapper;
import com.qf.mapper.fire.HixentArcEfmDeviceMapper;
import com.qf.mapper.fire.HixentUploadFileMapper;
import com.qf.model.admin.HixentPermissionsRole;
import com.qf.model.fire.HixentArcBuild;
import com.qf.util.RedisUtil;



/**
 * web管理后台公共服务
 * author   Vareck
 * datetime 2018-10-27 10:20
 */
@Service
public class CommonService {
	
	
	
	@Value("${loginSet.num}")         
    private Integer num;               //允许错误密码的次数
	@Value("${loginSet.timeIn}") 
    private long timeIn;               //错误密码提交的时间间隔
	@Value("${loginSet.timeOut}")
    private long timeOut;              //被限制登录后重新启用的时间间隔

	 @Autowired
	private HixentUserMapper hixentUserMapper;
	
    @Autowired
    private HixentPermissionsRoleMapper hixentPermissionsRoleMapper;
    
    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private HixentUploadFileMapper hixentUploadFileMapper;
    
    @Autowired
    private HixentArcEfmDeviceMapper hixentArcEfmDeviceMapper;
    /**
     * 清除登录相关redis
     */
    public void delLoginRedis(String account){
    	redisUtil.del(account+"_forbid_time");
		redisUtil.del(account+"_forbid_start");
		redisUtil.del(account+"_forbid_n");
    }
    
    
    
    /**
     * 错误密码重复登录判断
     */
    public void getWPassword(String account){
		//记录初次错误的时间
        if( redisUtil.hasKey(account+"_forbid_start") && redisUtil.hasKey(account+"_forbid_n") ){
        	Integer n = Integer.parseInt(redisUtil.get(account+"_forbid_n").toString());
        	if(n>=num){
        		//记录禁用时间
                redisUtil.set(account+"_forbid_time","no",timeOut);  
                //清空其余记录
                redisUtil.del(account+"_forbid_start");
                redisUtil.del(account+"_forbid_n");
        	}else{
                //刷新错误次数
                redisUtil.set(account+"_forbid_n",Integer.toString((n+1)),timeIn);                           
        	}
        }else{
			//记录时间
        	redisUtil.set(account+"_forbid_start","no",timeIn);  
        	//记录错误次数
        	redisUtil.set(account+"_forbid_n","1",timeIn);  
        }
    }
    
    
    
    /**
     * 递归搜索子菜单(角色权限)
     * @param id     当前菜单id
     * @param Menu   菜单集合
     * @return
     */
    public static List<HixentPermissionsRole> getChild( Integer pid, List<HixentPermissionsRole> Menu ) {
        List<HixentPermissionsRole> childList = new ArrayList<>();    
        //父类菜单下所有子菜单
        for (HixentPermissionsRole menuList : Menu) {
        	if ( menuList.getParentId().equals(pid)) {
        		childList.add(menuList);
            }
        }
        if (childList.size() == 0) {
            return null;
        }else{
            for ( HixentPermissionsRole menuList : childList ) { 
            	menuList.setChildMenus( getChild(menuList.getMenuId(),Menu) );
            } 
            return childList;
        }
    }

    
    
    /**
     * 获取权限菜单
     * @param  uid         用户列表
     * @return 
     */
    public List<HixentPermissionsRole> getPermissions( String uid ){
        List<HixentPermissionsRole> mList           = new ArrayList<HixentPermissionsRole>();   //最后的结果
        List<HixentPermissionsRole> menuList        = new ArrayList<HixentPermissionsRole>();   //所有集合
        HixentPermissionsRole hixentPermissionsRole = hixentPermissionsRoleMapper.selectForData(uid);
        Integer role_id = hixentPermissionsRole.getRoleId();
        Integer roleType = hixentUserMapper.getRoleType(role_id);
        /*根据权限获取菜单功能集合*/
        if( roleType == 0 ){     //管理员角色拥有所有权限
      	 	 menuList  = hixentPermissionsRoleMapper.selectAllData();
      	 	 for( HixentPermissionsRole ml:menuList ){
	   			 if( StringUtils.isNotEmpty(ml.getActionName()) && ml.getParentId() == 0 ){
	   				 mList.add(ml);
	   			 }
      	 	 }
        }else{
            String   permission_str = hixentPermissionsRole.getMenuIdList();
            String[] permission_arr = permission_str.split(",");
            menuList = hixentPermissionsRoleMapper.findActionNameByMenuId(permission_arr);
            for( HixentPermissionsRole ml:menuList ){
            	if( StringUtils.isNotEmpty(ml.getActionName()) && ml.getParentId() == 0 ){
            		mList.add(ml);
            	}
            }
        }
        for ( HixentPermissionsRole menu : mList ) {
            menu.setChildMenus(getChild(menu.getMenuId(),menuList));
        }
    	return mList;
    }
   //获取 3级权限
    public List<HixentPermissionsRole> getThirdPermissions(String uid){
    	HixentPermissionsRole hixentPermissionsRole = hixentPermissionsRoleMapper.selectForData(uid);
        Integer role_id = hixentPermissionsRole.getRoleId();
        Integer roleType = hixentUserMapper.getRoleType(role_id);

        List<HixentPermissionsRole> thirdPermissions=new ArrayList<HixentPermissionsRole>();
    	/*根据权限获取菜单功能集合*/
        if( roleType == 0 ){     //管理员角色拥有所有权限
      	 	  thirdPermissions = hixentPermissionsRoleMapper.getThirdPermissions();
      	 	
        }else{
        	String   permission_str = hixentPermissionsRole.getMenuIdList();
            String[] permission_arr = permission_str.split(","); 
            thirdPermissions= hixentPermissionsRoleMapper.getThirdPermissionsBybidId(permission_arr);
        }
    	return thirdPermissions;
    }

   //获取建筑类型
    public List<HixentArcBuild> getbuildList(){
    	return hixentArcEfmDeviceMapper.getBuildList();
    }
    
}