package com.qf.service.admin;



import com.qf.mapper.admin.HixentPermissionsRoleMapper;
import com.qf.mapper.admin.HixentRoleMapper;
import com.qf.mapper.admin.HixentPermissionsMapper;
import com.qf.model.admin.HixentRole;
import com.qf.model.admin.HixentPermissions;
import com.qf.model.admin.HixentPermissionsRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



/**
 * web管理后台权限控制相关服务
 * author   Vareck
 */
@Service
public class HixentPermissionsRoleService {

    @Autowired
    private HixentPermissionsRoleMapper hixentPermissionsRoleMapper;

    @Autowired
    private HixentRoleMapper hixentRoleMapper;
    
    @Autowired
    private HixentPermissionsMapper hixentPermissionsMapper;
    
    
    
    /*角色*/
    //列表
    public List<HixentRole> getRoleAllList( String name ) {
    	return hixentRoleMapper.getRoleAllList( name );
    }
    public List<HixentRole> getRoleAllList2( String name ) {
    	return hixentRoleMapper.getRoleAllList2( name );
    }
    public List<HixentRole> getRolePageList( String name,String order,Integer limit,Integer page ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentRoleMapper.getRolePageList( name,order,limits );
    }
    
    //基础信息
    public HixentRole selectRoleInfo( Integer roleId ) {
        return hixentRoleMapper.selectRoleInfo( roleId );
    }
    
    //新增
    public void insertRole( String role_name , String menu_id_list, String role_desc ) {
        hixentRoleMapper.insertRole( role_name,menu_id_list,role_desc );
    }
    
    //更新
    public void updateRole( Integer roleId , String role_name , String menu_id_list, String role_desc ) {
        hixentRoleMapper.updateRole( roleId,role_name,menu_id_list,role_desc );
    }
    
    //批量删除
    public void deleteRole( List<Integer> ridList ) {
        hixentRoleMapper.deleteRole( ridList );
    }

    /*权限*/
    //列表
    public List<HixentPermissions> getPermissionsAllList( String name ) {
    	return hixentPermissionsMapper.getPermissionsAllList( name );
    }
    public List<HixentPermissions> getPermissionsAllList2( String name ) {
    	return hixentPermissionsMapper.getPermissionsAllList2( name );
    }
    public List<HixentPermissions> getPermissionsPageList( String name,String order,Integer limit,Integer page ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentPermissionsMapper.getPermissionsPageList( name,order,limits );
    }
    //新增
    public void insertPermissions( String menuIcon,String menuUrl,Integer parentId,String actionName,String menuName,Integer setOrder ) {
    	hixentPermissionsMapper.insertPermissions( menuIcon,menuUrl,parentId,actionName,menuName,setOrder );
    }
    //更新
    public void updatePermissions( Integer menuId,String menuIcon,String menuUrl,Integer parentId,String actionName,String menuName,Integer setOrder ) {
    	hixentPermissionsMapper.updatePermissions(menuId,menuIcon,menuUrl,parentId,actionName,menuName,setOrder);
    }
    //删除
    public void deletePermissions( List<Integer> pidList  ) {
    	hixentPermissionsMapper.deletePermissions( pidList );
    }

    /*智能消防*/
    public HixentPermissionsRole findRoleByUid( String uid ) {
        return hixentPermissionsRoleMapper.selectForData( uid );
    }
    public List<HixentPermissionsRole> findActionNameByMenuId( String[] menuId ) {
        return hixentPermissionsRoleMapper.findActionNameByMenuId( menuId );
    }
    //获取所有智能消防权限
    public List<HixentPermissionsRole> selectAllData() {
        return hixentPermissionsRoleMapper.selectAllData();
    }
    //
    
}