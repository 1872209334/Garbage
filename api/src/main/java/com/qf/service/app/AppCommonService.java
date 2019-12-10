package com.qf.service.app;



import com.qf.mapper.fire.HixentArcEquipmentInfoMapper;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcEquipmentInfo;

import java.util.List;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 手机app相关公共服务
 * author Vareck
 */
@Service
public class AppCommonService {

	
    
	@Autowired
    private HixentArcEquipmentInfoMapper hixentArcEquipmentInfoMapper;
	
    
    
    /*获取用户信息*/
    public HixentAppUser getAppUserInfo() {
	    //获取用户信息
        HixentAppUser userinfo = new HixentAppUser();
        String key    = SecurityUtils.getSubject().getPrincipal().getClass().getName();
        if( key.equals("com.qf.model.admin.HixentAppUser") ){
	        userinfo  = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
        }
        return userinfo;
    }

    
    
    /**
     * 虚拟分组设备权限判断
     * @param  userinfo  用户信息
     * @param  pid       虚拟分组id
     * @return
     */
    public boolean  isHaveDeviceType(HixentAppUser userinfo,Integer  pid) {
        String  pStr  = userinfo.getProjectId();
        String[] pids = pStr.split(",");
        int k         = 0;
        for( int i=0;i<pids.length;i++ ){
        	if( Integer.valueOf(pids[i]) == pid ){
        		k = 1;
        		break;
        	}
        }
        if( k==0 ){
        	return false;
        }else{
        	return true;
        }
    }
    
    
    
    /**
     * 设备权限判断
     * @param  userinfo  用户信息
     * @param  eid       设备编号
     * @return
     */
    public boolean  isHaveDevice(HixentAppUser userinfo,String eid) {
        String pStr   = userinfo.getProjectId();
        String[] pids = pStr.split(",");
		//获取设备列表
        int k = 0;
    	List<HixentArcEquipmentInfo> eList = hixentArcEquipmentInfoMapper.getDeviceListByProjectId(pids);
        for(HixentArcEquipmentInfo el:eList){
        	if( eid.equals(el.getId()) ){
        		k = 1;
        		break;
        	}
        }
        if( k==0 ){
        	return false;
        }else{
        	return true;
        }
    }
    
    
     
}