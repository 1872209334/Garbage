package com.qf.controller.common;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Example;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.ComConstant;
import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.shiro.CustomerAuthenticationToken;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentPermissionsRole;
import com.qf.model.admin.HixentSite;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.valid.ValidHixentAppUserMore;
import com.qf.model.admin.valid.ValidHixentUser;
import com.qf.model.admin.valid.ValidVerificationCode;
import com.qf.model.fire.HixentArcBuild;
import com.qf.service.admin.CommonService;
import com.qf.service.admin.HixentCompanyService;
import com.qf.service.admin.HixentUserService;
import com.qf.util.AliyunSmsUtil;
import com.qf.util.JwtUtil;
import com.qf.util.PasswordUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;



@RestController
@RequestMapping("/get")
public class PublicController {
	
	

	@Value("${loginSet.num}")         
    private Integer num;               //允许错误密码的次数
	@Value("${loginSet.timeIn}") 
    private long timeIn;               //错误密码提交的时间间隔
	@Value("${loginSet.timeOut}")
    private long timeOut;              //被限制登录后重新启用的时间间隔
	
	

    @Autowired
    private HixentUserService hixentUserService;
    
    @Autowired
    private HixentCompanyService hixentCompanyService;

    @Autowired
    private CommonService commonService;
    
    @Resource
    private JwtConfig jwtConfig;
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Resource
    private RedisUtil redisUtil;
    
    
    
    /**
     * 管理员登录后台
     * author Vareck
     */
    @ApiLimitConfig(count=1,time=1000)
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @SystemHistoryAnnotation(opration = "管理员登录后台") 
    public ModelMap doLogin(@Valid ValidHixentUser validHixentUser, BindingResult bindingResult) {
    	try{
            if ( bindingResult.hasErrors() ) {
                return ReturnUtil.Error("用户名或密码为空");
            }
           
            String account    = validHixentUser.getAccount().trim();
            String password   = validHixentUser.getPassword().trim();
            //验证会员
            Example example   = new Example(HixentUser.class);
            example.createCriteria().andCondition("account = ", account);
            Integer userCount = hixentUserService.getCount(example);
           
            //验证密码
            if ( userCount != 0 ) {
            	
            	HixentUser userinfo = hixentUserService.findByUsername(account);
            	//重复登录判断
                if( redisUtil.hasKey("admin_"+account+"_forbid_time") ){
            		return ReturnUtil.Error("提交错误密码多次，账号被锁，请在"+Long.toString(timeOut/60)+"分钟再登录！");
                }
            	String passwordk 	= PasswordUtil.createCustomPwd(password,userinfo.getSalt());
            	String passwordb 	= userinfo.getPassword().trim();
            	if( !passwordk.equals(passwordb) ){
            		commonService.getWPassword("admin_"+account);
            		return ReturnUtil.Error("密码错误"+redisUtil.get("admin_"+account+"_forbid_n").toString()+"次！在"+Long.toString(timeIn/60)+"分钟内连续错误"+Integer.toString(num)+"次，将会被锁"+Long.toString(timeOut/60)+"分钟！");
            	}else{
            		
                    //获取当前的Subject
                    Subject currentUser = SecurityUtils.getSubject();
                    currentUser.logout(); 
                    CustomerAuthenticationToken token = new CustomerAuthenticationToken("admin_"+account, passwordb, false);  
                    currentUser.login(token);
                    //验证是否登录成功
                    if ( currentUser.isAuthenticated() ) {
                    	
                    	/*获取token*/
                        String accessToken = jwtUtil.createJWT("admin_"+userinfo.getUid(), jwtConfig.getSecret());
                        //缓存最新的token
                        redisUtil.set("admin_"+userinfo.getUid(),PasswordUtil.createCustomPwd(accessToken,jwtConfig.getSecret()),7200);
                    
                        //缓存省份和地区ID
                        Integer areaId = userinfo.getAreaId();
                        Integer provinceId = userinfo.getProvinceId();
                       
                        redisUtil.set("areaId_"+userinfo.getUid(),areaId+"",jwtConfig.getExpiration()/1000);
                        redisUtil.set("provinceId_"+userinfo.getUid(),provinceId+"",jwtConfig.getExpiration()/1000);
                        
                        Map<String,Object> jsonMap = new HashMap<>();
                        
                        /*获取系统权限*/
                        if( userinfo.getFireRole() > 0 ){
                            jsonMap.put("fire_menu" , commonService.getPermissions(userinfo.getUid()));
                            System.out.println("dada"+commonService.getPermissions(userinfo.getUid()).get(0).getMenuName());
                        }else{
                            jsonMap.put("fire_menu" , "");
                		}
                        //获取登录用户绑定项目
            			//超级管理员-无需获取项目信息，其他用户需要获取
            			//0超级管理员，1二级管理员，2三级管理员
            			if(!ComConstant.SUPER_MANAGER_KEY.equals(userinfo.getRoleType())){
            				String[] siteCordArr=userinfo.getBid().split(",");
            				List<HixentSite> siteList = hixentUserService.getsite(provinceId,areaId,siteCordArr);
            				jsonMap.put("siteList", siteList);
            			} 
                        //用户信息
                        HashMap<String,String> user_data = new HashMap<String,String>();
                        user_data.put("roleId" , userinfo.getFireRole()+"");
                        user_data.put("bid" , userinfo.getBid());
                        user_data.put("username" , userinfo.getAccount());
                        user_data.put("uid"      , userinfo.getUid());
                        user_data.put("aid"      , Integer.toString(userinfo.getId()));
                        user_data.put("roleType"      , Integer.toString(userinfo.getRoleType()));
                        user_data.put("remark"      , userinfo.getRemark());
                        
//                        //获取公司信息
//        				if( userinfo.getCid()>0 ){
//        					
//        					if( redisUtil.hasKey("company_name_"+String.valueOf(userinfo.getCid())) && redisUtil.hasKey("company_logo_"+String.valueOf(userinfo.getCid())) ){   //获取缓存
//        						user_data.put("company_name" , String.valueOf(redisUtil.get("company_name_"+String.valueOf(userinfo.getCid()))));
//        						user_data.put("company_logo" , String.valueOf(redisUtil.get("company_logo_"+String.valueOf(userinfo.getCid()))));
//        					}else{
//        	    				HixentCompany hixentCompany = new HixentCompany();
//        	    				hixentCompany.setCid(userinfo.getCid());
//        	    				HixentCompany cInfo = hixentCompanyService.selectOne(hixentCompany);
//        	    				if( cInfo == null ){
//        	                        user_data.put("company_name" , "");
//        	                        user_data.put("logo_url"     , "");
//        	    				}else{
//        	                        user_data.put("company_name" , cInfo.getName());
//        	                        user_data.put("logo_url"     , cInfo.getLogoUrl());
//        							redisUtil.set("company_name_"+String.valueOf(userinfo.getCid()),cInfo.getName());
//        							redisUtil.set("company_logo_"+String.valueOf(userinfo.getCid()),cInfo.getLogoUrl());
//        	    				}
//        					}
//        				}else{
//                            user_data.put("company_name" , "");
//                            user_data.put("logo_url"     , "");
//        				}
        				List<HixentPermissionsRole> thirdPermissions = commonService.getThirdPermissions(userinfo.getUid());
        				List<HixentArcBuild> getbuildList = commonService.getbuildList();
        				//清除redis
        				commonService.delLoginRedis("admin_"+account);
                        /*发送的json数据*/
                        jsonMap.put("token"    , accessToken);
                        jsonMap.put("userinfo" , user_data);
                        jsonMap.put("thirdPermissions", thirdPermissions);
                        jsonMap.put("buildList", getbuildList);
                        JSONObject json = new JSONObject(jsonMap);
                        return ReturnUtil.Success("登录成功",json);
                    } else {
                        token.clear();
                        return ReturnUtil.Error("登录失败");
                    }
            	}
            }else{
            	return ReturnUtil.Error("没有该用户！");
            }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 忘记密码获取手机验证码
     * author RuanYu
     */
   // @ApiLimitConfig(count=1,time=60000)
    @SystemHistoryAnnotation(opration = "修改密码获取手机验证码")
    @RequestMapping(value = "/passwordCode", method = RequestMethod.POST)
    public ModelMap passwordCode( @Valid ValidVerificationCode verificationCode, BindingResult bindingResult ) {
    	try{
        	if ( bindingResult.hasErrors() ) {
        		
                return ReturnUtil.Error("请填写正确的手机！");
            }else{
            	//获取数据
                String mobile 		    = verificationCode.getMobile().trim();
                HixentUser userinfo  = hixentUserService.getUserinfoByMobile(mobile);
                if( userinfo == null ){
                	return ReturnUtil.Error("该手机号未注册！");
                }else{
            		double  random 		  = Math.pow(10,5)+Math.random()*(Math.pow(10,6)-Math.pow(10,5)-1);
            		long    l             = new Double(random).longValue();
            		String  code   		  = Long.toString(l);
            		String  signName      = "莱茵斯科技";
        			String  templateCode;
        			String  templateParam = "{\"code\":\""+code+"\"}";
        			String  outId = userinfo.getUid();
                    templateCode  = "SMS_158015094";
                    redisUtil.set(mobile+"_password",code,60);
                    AliyunSmsUtil.sendSms( mobile,signName,templateCode,templateParam,outId );
                    return ReturnUtil.Success("验证码已经发到您的手机上了，请查收！");
                }
            }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 修改密码
     * author Vareck
     */
   // @ApiLimitConfig(count=1,time=1000)
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    @SystemHistoryAnnotation(opration = "忘记密码后修改密码")
    public ModelMap changePassword( @Valid ValidHixentAppUserMore user ){
    	try{
            //获取数据
    	    String   password   = user.getPassword().trim();
    	    String   mobile     = user.getMobile().trim();
    	    String   ncode      = user.getNcode().trim();
    	    //数据验证
        	if( password.equals("") ){
    	    	return ReturnUtil.Error("密码需填写！");
    	    }else{
                //获取用户信息
    	    	 HixentUser userinfo  = hixentUserService.getUserinfoByMobile(mobile);
            	if( userinfo == null ){
            		return ReturnUtil.Error("没有该用户信息");
            	}else{
            		String     code     = String.valueOf(redisUtil.get(mobile+"_password"));
            		if( !ncode.equals(code) ){
            			return ReturnUtil.Error("验证码失效，请重新获取，验证码有效期60秒！");
            		}else{
                    	String     salt       = userinfo.getSalt();
                    	String  passwordStart = userinfo.getPassword();
                	    String  passwordEnd   = PasswordUtil.createCustomPwd(password,salt);
                	    if( passwordStart.trim().equals(passwordEnd) ){
                	    	return ReturnUtil.Error("与旧密码一致，密码修改失败！");
                	    }else{
                	    	hixentUserService.updateUserPassWord(userinfo.getId(), passwordEnd);;
                	    	return ReturnUtil.Success("密码修改成功！");
                	    }
            		}
            	}
    	    }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    } 
    
}