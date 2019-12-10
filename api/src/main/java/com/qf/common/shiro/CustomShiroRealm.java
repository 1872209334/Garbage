package com.qf.common.shiro;



import com.qf.model.admin.HixentUser;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentPermissionsRole;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.admin.HixentPermissionsRoleService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.List;
import java.util.HashSet;



/**
 * 身份校验核心类
 * author Vareck
 */
public class CustomShiroRealm extends AuthorizingRealm {

    @Autowired
    private HixentUserService hixentUserService;
    
    @Autowired
    private HixentAppUserService hixentAppUserService;
    
    @Autowired
    private HixentPermissionsRoleService hixentPermissionsRoleService;
    

    
    /**
     * 认证信息.(身份验证)
     * :
     * Authentication 是用来验证用户身份
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	//获取用户的输入的账号.
        String   userStr = (String)token.getPrincipal();
        String[] userArr = userStr.split("_");
        if( userArr.length<2 ){
            throw new UnknownAccountException();
        }
        String key       = userArr[0];
        String username  = userArr[1];
        //用户信息
        if( key.equals("admin") ){           //管理系统后台
            HixentUser userInfo = hixentUserService.findByUsername(username);
            if(userInfo == null){
                throw new UnknownAccountException();
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
            	userInfo,                                     //用户信息
                userInfo.getPassword(),                       //密码
                ByteSource.Util.bytes(userInfo.getSalt()),    //用户uid
                userInfo.getAccount()                         //用户名
            );
            return authenticationInfo;
        }else if( key.equals("app") ){      //App系统后台
            HixentAppUser userInfo = hixentAppUserService.findByAppUsername(username);
            if(userInfo == null){
                throw new UnknownAccountException();
            }
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
            	userInfo,                                     //用户信息
                userInfo.getPassword(),                       //密码
                ByteSource.Util.bytes(userInfo.getSalt()),    //用户uid
                userInfo.getAccount()                         //用户名
            );
            return authenticationInfo;
        }else{
            throw new UnknownAccountException();
        }
    }
    
    

    /**
     * 此方法调用  hasRole,hasPermission的时候会进行回调.
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	/*获取用户相关信息*/
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String key   = principals.getPrimaryPrincipal().getClass().getName();
        if( key.equals("com.qf.model.admin.HixentUser") ){
            HixentUser admin = (HixentUser)principals.getPrimaryPrincipal();
            /*获取权限*/
            Set<String> permissionSet = new HashSet<>();
            //智能消防权限
            if( admin.getFireRole() == 1 ){
                HixentPermissionsRole hixentPermissionsRole2 = hixentPermissionsRoleService.findRoleByUid(admin.getUid());
                if( admin.getFireRole() == 1 ){     		//获取所有智能消防权限
        	        List<HixentPermissionsRole> menuList2  = hixentPermissionsRoleService.selectAllData();
            		for(HixentPermissionsRole ml:menuList2){
            			if( StringUtils.isNotEmpty(ml.getActionName()) ){
            				permissionSet.add("parc_"+ml.getActionName());
            			}
            		}
                }else if( admin.getFireRole() != 0 ){
                    String   permission_str2 = hixentPermissionsRole2.getMenuIdList();
                    String[] permission_arr2 = permission_str2.split(",");
                    if( permission_arr2.length > 0 ){
                        List<HixentPermissionsRole> menuList2 = hixentPermissionsRoleService.findActionNameByMenuId(permission_arr2);
                		for(HixentPermissionsRole ml:menuList2){
                			if( StringUtils.isNotEmpty(ml.getActionName()) ){
                				permissionSet.add("parc_"+ml.getActionName());
                			}
                		}
                    }else{
                    	permissionSet.add("parc_no");
                    }
                }else{
                	permissionSet.add("parc_no");
                }
            }
            /*获取角色*/
            Set<String> roleSet = new HashSet<>();
            //电弧系统
            roleSet.add("rarc_"+admin.getFireRole());
            /*设置该用户拥有的角色和权限*/
            authorizationInfo.setRoles(roleSet);
            authorizationInfo.setStringPermissions(permissionSet);
            System.out.println("permissionSet"+permissionSet.size());
        }
        else if( key.equals("com.qf.model.admin.HixentAppUser") ){
            /*获取权限*/
            Set<String> permissionSet = new HashSet<>();
        	permissionSet.add("parc_app_all");
            authorizationInfo.setStringPermissions(permissionSet);
        }
       
        return authorizationInfo;
    }

    
    
}