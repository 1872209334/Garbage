package com.qf.controller.api.admin;



import tk.mybatis.mapper.entity.Example;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentCompany;
import com.qf.model.admin.HixentMessage;
import com.qf.model.admin.HixentRole;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.valid.ValidHixentDictionary;
import com.qf.model.admin.valid.ValidHixentMessage;
import com.qf.model.admin.valid.ValidHixentRole;
import com.qf.model.admin.valid.ValidHixentPermissions;
import com.qf.model.admin.valid.ValidHixentAppUserMore;
import com.qf.model.admin.valid.ValidHixentCompany;
import com.qf.model.admin.valid.ValidHixentUserInfo;
import com.qf.service.admin.CommonService;
import com.qf.service.admin.HixentCompanyService;
import com.qf.service.admin.HixentDictionaryService;
import com.qf.service.admin.HixentMessageService;
import com.qf.service.admin.HixentPermissionsRoleService;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.SecurityUtils;

import javax.validation.Valid;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;



@RestController
@RequestMapping("/api/user/delete")
public class ApiUserDeleteController {
    
	
	
    @Autowired
    private HixentDictionaryService hixentDictionaryService;
    
    @Autowired
    private HixentPermissionsRoleService hixentPermissionsRoleService;
    
    @Autowired
    private HixentMessageService hixentMessageService;
    
    @Autowired
    private HixentCompanyService hixentCompanyService;
    
    @Autowired
    private HixentAppUserService hixentAppUserService;
    
    @Autowired
    private CommonService commonService;

    @Resource
    private RedisUtil redisUtil;
    
	@Autowired 
	private HixentUserService hixentUserService; 
    
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
 
    
    
    /**
     * 删除管理员
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_deleteUser"}) 
    // @ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除管理员")
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public ModelMap deleteUser( @Valid ValidHixentUserInfo user ){
	    try{
	    	//获取数据
            if( user.getIdStr().trim().equals("0") || user.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的管理员！");
            }
            String   str      	  = "管理员ID：";              //不可删除的管理员id
	        String   idStr    	  = user.getIdStr().trim();  
	        String[] uidArray	  = idStr.split(",");
	        List<Integer> uidList = new ArrayList<>();       //可删除的管理员id
	        for( int i = 0;i<uidArray.length;i++ ){
	        	List<HixentAppUser> appList = hixentAppUserService.selectByPid(Integer.valueOf(uidArray[i]));
	        	if( appList.size()>0 ){
	        		str+=uidArray[i]+",";
	        	}else{
	        		uidList.add(Integer.valueOf(uidArray[i]));
	        	}
	        }
	        //批量删除操作
	        if( uidList.size()>0 ){
	        	hixentUserService.deleteUser(uidList);
        		if( !str.equals("管理员ID：") ){
    	        	str = str.substring(0,str.length() - 1);
        	    	return ReturnUtil.Success(str+"的管理员有从属app用户。得先删除相关app用户。只成功删除"+uidList.size()+"条数据！");
        		}else{
        	    	return ReturnUtil.Success("成功删除"+uidList.size()+"条数据！");
        		}
	        }else{
	        	return ReturnUtil.Error("所选择的管理员均在使用中！得先删除相关app用户！");
	        }
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    
    
    
    /**
     * 删除权限
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_deletePermissions"}) 
    // @ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除权限")
    @RequestMapping(value = "/deletePermissions", method = RequestMethod.POST)
    public ModelMap deletePermissions( @Valid ValidHixentPermissions permissions ){
	    try{
	    	//获取数据
            if( permissions.getIdStr().trim().equals("0") || permissions.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的菜单！");
            }
	        String  idStr      	  = permissions.getIdStr().trim();
	        String[] pidArray 	  = idStr.split(",");
	        List<Integer> pidList = new ArrayList<>(); //可删除的权限id集合
	        String mStr           = "";                //角色有用到的权限id字符串（逗号隔开）
	        String str            = "权限ID：";         //不可删除的权限id
        	List<HixentRole> roleList  = hixentPermissionsRoleService.getRoleAllList(null);
        	for( HixentRole rl:roleList ){
        		if( !rl.getMenuIdList().trim().equals("") ){
            		mStr+= rl.getMenuIdList()+",";
        		}
        	}
        	mStr = mStr.substring(0,mStr.length() - 1);
        	String[] mArr = mStr.split(",");
        	for( int i=0;i<pidArray.length;i++ ){
        		pidList.add(Integer.valueOf(pidArray[i]));
	        	for( int j=0;j<mArr.length;j++ ){
		        	if( pidArray[i].trim().equals(mArr[j].trim()) ){
		        		str+=pidArray[i]+",";
		        		pidList.remove(Integer.valueOf(pidArray[i]));
		        		break;
		        	}
		        }
	        }
	        //批量删除操作
	        if( pidList.size()>0 ){
		        hixentPermissionsRoleService.deletePermissions(pidList);
	    		//获取用户信息
		        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
	    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    	    HttpServletRequest request = requestAttributes.getRequest();
	    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	auth             = auth.substring(7, auth.length());
	        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	            String userId    = claims.getId();
	            String[] userArr = userId.split("_");
                if( !userArr[0].equals("admin") ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
                }
	        	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
		        if( userinfo == null ){
		        	return ReturnUtil.Error("已退出，请重新登录！");
		        }
	        	//重新生成权限树
	        	Map<String,Object> jsonMap  = new HashMap<>();
	    		if( userinfo.getFireRole() > 0 ){
	        		jsonMap.put("fire_menu" , commonService.getPermissions(userinfo.getUid()));
	    		}else{
	    			jsonMap.put("fire_menu" , "");
	    		}
		        JSONObject json = new JSONObject(jsonMap);
        		if( str.equals("权限ID：") ){
    	        	str = str.substring(0,str.length() - 1);
        	    	return ReturnUtil.Success(str+"角色中含有。请先删除相关角色！"+"只成功删除"+pidList.size()+"条数据！",json);
        		}else{
        	    	return ReturnUtil.Success("成功删除"+pidList.size()+"条数据！",json);
        		}
	        }else{
	        	return ReturnUtil.Error("这些权限都在使用中！得先删除相关角色！");
	        }
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    
    
    
    /**
     * 删除角色
     * author Vareck
     */
    //@RequiresPermissions(value = {"parc_deleteRole"}) 
    // @ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除角色")
    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public ModelMap deleteRole( @Valid ValidHixentRole role ){
	    try{
	    	//获取数据
            if( role.getIdStr().trim().equals("0") || role.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的角色！");
            }
	        String  idStr      	  = role.getIdStr().trim();
	        String[] ridArray 	  = idStr.split(",");
	        List<Integer> ridList = new ArrayList<>();      //可删除的角色id
            String   str      	  = "角色ID：";              //不可删除的角色id
	        for( int i = 0;i<ridArray.length;i++ ){
	            Example example   = new Example(HixentUser.class);
	            example.createCriteria().andCondition("fire_role = ", Integer.valueOf(ridArray[i]));
	            Integer userCount = hixentUserService.getCount(example);
	        	if( userCount == 0 ){
	        		ridList.add(Integer.valueOf(ridArray[i]));
	        	}else{
	        		str+=ridArray[i]+",";
	        	}
	        }
        	//批量删除操作
        	if( ridList.size()>0 ){
    	        hixentPermissionsRoleService.deleteRole(ridList);
        		if( str.equals("角色ID：") ){
                	str = str.substring(0,str.length() - 1);
        	    	return ReturnUtil.Success(str+"管理员有用到。请先删除相关管理员！"+"只成功删除"+ridList.size()+"条数据！");
        		}else{
        	    	return ReturnUtil.Success("成功删除"+ridList.size()+"条数据！");
        		}
        	}else{
	        	return ReturnUtil.Error("这些角色都在使用中！得先删除相关管理员！");
        	}
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    
    
    
    /**
     * 删除公司数据
     * author Vareck
     */
    //@RequiresRoles(value = {"rarc_1"})
    //@RequiresPermissions(value = {"parc_deleteCompany"}) 
    //@ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除公司数据")
    @RequestMapping(value = "/deleteCompany", method = RequestMethod.POST)
    public ModelMap deleteCompany( @Valid ValidHixentCompany company ){
	    try{
	    	//获取数据
            if( company.getIdStr().trim().equals("0") || company.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的公司数据！");
            }
	        String  idStr      	  = company.getIdStr().trim();
	        String[] cid_array 	  = idStr.split(",");
	        List<Integer> cidList = new ArrayList<>();      //可删除的公司id
            String   str      	  = "公司ID：";              //不可删除的公司id
	        for( int i=0;i<cid_array.length;i++ ){
	            Example example      = new Example(HixentUser.class);
	            example.createCriteria().andCondition("cid = ", Integer.valueOf(cid_array[i]));
	            Integer userCount    = hixentUserService.getCount(example);
	            Example example2     = new Example(HixentAppUser.class);
	            example2.createCriteria().andCondition("cid = ", Integer.valueOf(cid_array[i]));
	            Integer userAppCount = hixentAppUserService.getCount(example2);
	        	//删除并更新缓存
	        	if( userCount == 0 && userAppCount == 0 ){
		        	//删除公司数据
			    	HixentCompany info = new HixentCompany();
			    	info.setCid( Integer.valueOf(cid_array[i]) );
			        hixentCompanyService.delete(info);
		            //更新缓存
		        	redisUtil.del("company_name_"+Integer.valueOf(cid_array[i]));
		        	redisUtil.del("company_logo_"+Integer.valueOf(cid_array[i]));
		        	cidList.add(Integer.valueOf(cid_array[i]));
	        	}else{
	        		str+=cid_array[i]+",";
	        	}
	        }
        	if( cidList.size()>0 ){
        		if( str.equals("公司ID：") ){
                	str = str.substring(0,str.length() - 1);
        	    	return ReturnUtil.Success(str+"管理员或App用户有用到。请先删除相关管理员或App用户！"+"只成功删除"+cidList.size()+"条数据！");
        		}else{
        	    	return ReturnUtil.Success("成功删除"+cidList.size()+"条数据！");
        		}
        	}else{
	        	return ReturnUtil.Error("这些公司信息都在使用中！得先删除相关管理员或App用户！");
        	}
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    
    
    
    /**
     * 删除站内信
     * author Vareck
     */
    @Transactional
    // @ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除站内信")
    @RequestMapping(value = "/deleteMessage", method = RequestMethod.POST)
    public ModelMap deleteMessage( @Valid ValidHixentMessage message ){
	    try{
	    	//获取数据
            if( message.getIdStr().trim().equals("0") || message.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的站内信！");
            }
    		//获取用户信息
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
        	auth             = auth.substring(7, auth.length());
        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
            String userId    = claims.getId();
            String[] userArr = userId.split("_");
            if( !userArr[0].equals("admin") ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
            }
        	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
            if(userinfo == null){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
    	    Integer type         = message.getType();
	        String  idStr        = message.getIdStr().trim();
	        String[] midArray    = idStr.split(",");
	        if( type == 0 ){
		        hixentMessageService.deleteReceiveMessage(midArray,userinfo.getId());
	        }else{
		        hixentMessageService.deleteSendMessage(midArray,userinfo.getId());
	        }
	    	return ReturnUtil.Success("成功删除"+midArray.length+"条数据！");
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    
    
    
    /**
     * 删除数据字典
     * author Vareck
     */
    //@RequiresRoles(value = {"rarc_1"})
    //@ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除数据字典")
    @RequestMapping(value = "/deleteDictionary", method = RequestMethod.POST)
    public ModelMap deleteDictionary( @Valid ValidHixentDictionary validHixentDictionary , BindingResult bindingResult ){
	    try{
	    	//获取数据
	    	if (bindingResult.hasErrors()) {
	            return ReturnUtil.Error("请选择要删除的数据！");
	        }
            if( validHixentDictionary.getIdStr().trim().equals("0") || validHixentDictionary.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的数据字典！");
            }
	        String  idStr      = validHixentDictionary.getIdStr().trim();
	        String[] did_array = idStr.split(",");
	        hixentDictionaryService.deleteDictionary(did_array);
	        redisUtil.del("fire_dictionary_info");
	    	return ReturnUtil.Success("成功删除"+did_array.length+"条数据！");
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }

    
    
    /**
     * 删除App用户
     * author Vareck
     */
    @Transactional
    //@RequiresPermissions(value = {"parc_deleteAppUser"}) 
    // @ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除App用户")
    @RequestMapping(value = "/deleteAppUser", method = RequestMethod.POST)
    public ModelMap deleteAppUser( @Valid ValidHixentAppUserMore user ){
	    try{
	    	//获取数据
            if( user.getIdStr().trim().equals("0") || user.getIdStr().trim().equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的会员！");
            }
    		//获取用户信息
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
        	auth             = auth.substring(7, auth.length());
        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
            String userId    = claims.getId();
            String[] userArr = userId.split("_");
            if( !userArr[0].equals("admin") ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
            }
        	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
            if(userinfo == null){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
    	    //获取数据
    	    Integer  userId2      = userinfo.getId();
    	    Integer  rid   	      = userinfo.getFireRole();
	        String   idStr        = user.getIdStr().trim();
	        String[] uidArray     = idStr.split(",");
	        List<Integer> mid_arr = new ArrayList<>();
//	        for(int i = 0;i<uidArray.length;i++){
//	        	mid_arr.add(Integer.valueOf(uidArray[i]));
//		        if( userId2 == Integer.valueOf(uidArray[i]) || rid == 1 ){    //超级管理员或者从属的管理员
//		        	/*预留超级管理员删除操作时站内信通知或短信通知*/
//		        	if( rid == 1 ){
//		        		//发给从属的管理员
//		        		HixentAppUser hixentAppUser = new HixentAppUser();
//		        		hixentAppUser.setId(Integer.valueOf(uidArray[i]));
//		        		HixentAppUser info = hixentAppUserService.selectOne(hixentAppUser);  //app用户信息
//		        		HixentUser aData   = new HixentUser();
//		        		aData.setId(info.getPid());
//		        		HixentUser aInfo = hixentUserService.getAdmin(aData);       //从属的管理员信息
//		        		HixentMessage hixentMessageA = new HixentMessage();
//		        		hixentMessageA.setTitle("超级管理员"+userinfo.getAccount()+"("+userinfo.getMobile()+")"+"删除了"+info.getAccount()+"(App用户)");
//		        		hixentMessageA.setContent("超级管理员"+userinfo.getAccount()+"("+userinfo.getMobile()+")"+"删除了"+info.getAccount()+"(App用户——手机号："+info.getMobile()+")");
//		        		hixentMessageA.setFromId(userinfo.getId());
//		        		hixentMessageA.setFromMobile(userinfo.getMobile());
//		        		hixentMessageA.setFromName(userinfo.getAccount());
//		        		hixentMessageA.setToId(aInfo.getId());
//		        		hixentMessageA.setToMobile(aInfo.getMobile());
//		        		hixentMessageA.setId(0);
//		        		hixentMessageA.setReceiveDelete(0);
//		        		hixentMessageA.setSendDelete(0);
//		        		hixentMessageA.setState(0);
//		        		hixentMessageA.setAddDate(DateUtil.getCurrentTime());
//		        		hixentMessageA.setUpdateDate(DateUtil.getCurrentTime());
//		        		hixentMessageService.insert(hixentMessageA);
//		        		//收集可删除的id
//		        		mid_arr.add(Integer.valueOf(uidArray[i]));
//		        	}
//		        }
//	        }
	        
        	hixentAppUserService.deleteAppUser(uidArray);
        	
	    	return ReturnUtil.Success("成功删除"+mid_arr.size()+"条数据！");
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    /**
     * 删除操作记录
     * author Ruanyu
     */
    @Transactional
    //@RequiresPermissions(value = {"parc_deleteAppUser"}) 
    // @ApiLimitConfig(count=1,time=1000)
    @SystemHistoryAnnotation(opration = "删除操作记录")
    @RequestMapping(value = "/deleteOperaLog", method = RequestMethod.POST)
    public ModelMap deleteOperaLog( String id ){
	    try{
	    	//获取数据
            if( id==null || id.equals("") ){
    	    	return ReturnUtil.Error("请选择要删除的记录！");
            }
    		//获取用户信息
	        //HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
    		String auth      = request.getHeader(jwtConfig.getJwtHeader());
        	auth             = auth.substring(7, auth.length());
        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
            String userId    = claims.getId();
            String[] userArr = userId.split("_");
            if( !userArr[0].equals("admin") ){
	        	return ReturnUtil.Error("已退出，请重新登录！");
            }
        	HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
            if(userinfo == null){
	        	return ReturnUtil.Error("已退出，请重新登录！");
	        }
            String[] idsArr = id.split(",");
	        //查询该操作记录是否是该管理员及下面管理员的
            
            //刪除
        	
	    	return ReturnUtil.Success("成功删除条数据！");
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
    }
    
    
}