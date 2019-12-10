package com.qf.controller.api.app;

import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.valid.ValidHixentAppUserMore;
import com.qf.model.admin.valid.ValidHixentAppUser;
import com.qf.model.admin.valid.ValidVerificationCode;
import com.qf.model.admin.valid.ValidHixentAppMobileLogin;
import com.qf.service.admin.CommonService;
import com.qf.service.app.HixentAppUserService;

import com.qf.util.PasswordUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.StringUtil;

import io.jsonwebtoken.Claims;

import com.qf.util.AliyunSmsUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;

import tk.mybatis.mapper.entity.Example;
import com.alibaba.fastjson.JSONObject;

import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.common.apiLimit.ApiLimitConfig;
import com.qf.common.shiro.CustomerAuthenticationToken;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/getApp")
public class AppPublicController {

	@Value("${loginSet.num}")
	private Integer num; // 允许错误密码的次数
	@Value("${loginSet.timeIn}")
	private long timeIn; // 错误密码提交的时间间隔
	@Value("${loginSet.timeOut}")
	private long timeOut; // 被限制登录后重新启用的时间间隔

	@Autowired
	private HixentAppUserService hixentAppUserService;

	@Autowired
	private CommonService commonService;

	@Resource
	private JwtConfig jwtConfig;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private JwtUtil jwtUtil;

	/**
	 * App用户登录（用户名密码登录） author Vareck
	 */
	@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "App用户登录（用户名密码登录）")
	public ModelMap doAppLogin(@Valid ValidHixentAppUser validHixentAppUser, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("用户名或手机号以及密码不能为空！");
			}
			String mobOrName = validHixentAppUser.getAccount().trim();
			String password = validHixentAppUser.getPassword().trim();

			HixentAppUser userinfo = hixentAppUserService.getAppUserByMobileOrUserName(mobOrName);
			
            if(userinfo==null) {
            	return ReturnUtil.Error("账号错误！");	
            }
			if (userinfo.getState() == 0) {
				return ReturnUtil.Error("账户还未通过审核，可联系管理员！");
			}
			String account = userinfo.getAccount();
			
			// 重复登录判断
			if (redisUtil.hasKey("app_" + account + "_forbid_time")) {
				return ReturnUtil.Error("提交错误密码多次，账号被锁，请在" + Long.toString(timeOut / 3600) + "小时后再登录！");
			}
			String passwordk = PasswordUtil.createCustomPwd(password, userinfo.getSalt());
			String passwordb = userinfo.getPassword().trim();
			if (!passwordk.equals(passwordb)) {
				commonService.getWPassword("app_" + account);
				return ReturnUtil.Error("密码错误" + redisUtil.get("app_" + account + "_forbid_n").toString() + "次！在"
						+ Long.toString(timeIn / 60) + "分钟内连续错误" + Integer.toString(num) + "次，将会被锁"
						+ Long.toString(timeOut / 3600) + "小时！");
			} else {
				// 获取当前的Subject
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.logout();
				CustomerAuthenticationToken token = new CustomerAuthenticationToken("app_" + account, passwordb, false);
				currentUser.login(token);
				// 验证是否登录成功
				if (currentUser.isAuthenticated()) {
					/* 获取token */
					String accessToken = jwtUtil.createJWT("app_" + userinfo.getUid(), jwtConfig.getSecret());
					// 缓存最新的token
					redisUtil.set("app_" + userinfo.getUid(),
							PasswordUtil.createCustomPwd(accessToken, jwtConfig.getSecret()),
							jwtConfig.getExpiration() / 1000);
					// 要发送的集合
					Map<String, Object> jsonMap = new HashMap<>();
					HashMap<String, String> user_data = new HashMap<String, String>();
					user_data.put("username", userinfo.getAccount());
					user_data.put("uid", userinfo.getUid());
					user_data.put("aid", Integer.toString(userinfo.getId()));
					user_data.put("mobile", userinfo.getMobile());
					user_data.put("adminMobile", userinfo.getAdminMobile());
					user_data.put("adminAccount", userinfo.getAdminAccount());
					// 清除redis
					commonService.delLoginRedis("app_" + account);
					/* 发送的json数据 */
					jsonMap.put("token", accessToken);
					jsonMap.put("userinfo", user_data);
					JSONObject json = new JSONObject(jsonMap);
					return ReturnUtil.Success("登录成功", json);
				} else {
					token.clear();
					return ReturnUtil.Error("登录失败");
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * App用户登录（手机验证码登录） author Vareck
	 */
	@ApiLimitConfig(count = 1, time = 1000)
	@RequestMapping(value = "/mobileToken", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "App用户登录（手机验证码登录）")
	public ModelMap doAppMobileLogin(@Valid ValidHixentAppMobileLogin validHixentAppMobileLogin,
			BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("手机号或密码不能为空！");
			}
			String mobile = validHixentAppMobileLogin.getMobile().trim();
			String ncode = validHixentAppMobileLogin.getNcode().trim();
			// 验证会员
			Example example = new Example(HixentAppUser.class);
			example.createCriteria().andCondition("mobile = ", mobile);
			Integer userCount = hixentAppUserService.getCount(example);
			// 验证密码
			if (userCount != 0) {
				HixentAppUser userinfo = hixentAppUserService.getAppUserinfoByMobile(mobile);
				String code = String.valueOf(redisUtil.get(mobile + "_login"));
				if (!ncode.equals(code)) {
					return ReturnUtil.Error("验证码错误！");
				} else {
					// 获取当前的Subject
					Subject currentUser = SecurityUtils.getSubject();
					currentUser.logout();
					CustomerAuthenticationToken token = new CustomerAuthenticationToken("app_" + userinfo.getAccount(),
							userinfo.getPassword(), false);
					currentUser.login(token);
					// 验证是否登录成功
					if (currentUser.isAuthenticated()) {
						/* 获取token */
						String accessToken = jwtUtil.createJWT("app_" + userinfo.getUid(), jwtConfig.getSecret());
						// 缓存最新的token
						redisUtil.set("app_" + userinfo.getUid(),
								PasswordUtil.createCustomPwd(accessToken, jwtConfig.getSecret()),
								jwtConfig.getExpiration() / 1000);
						// 要发送的集合
						Map<String, Object> jsonMap = new HashMap<>();
						HashMap<String, String> user_data = new HashMap<String, String>();
						user_data.put("username", userinfo.getAccount());
						user_data.put("uid", userinfo.getUid());
						user_data.put("aid", Integer.toString(userinfo.getId()));
						/* 发送的json数据 */
						jsonMap.put("token", accessToken);
						jsonMap.put("userinfo", user_data);
						JSONObject json = new JSONObject(jsonMap);
						return ReturnUtil.Success("登录成功", json);
					} else {
						token.clear();
						return ReturnUtil.Error("登录失败");
					}
				}
			} else {
				return ReturnUtil.Error("请重新获取验证码重试！");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 注册用户 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=1000)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "注册用户")
	public ModelMap register(@Valid ValidHixentAppUserMore user, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				String message = "";
				List<FieldError> list = bindingResult.getFieldErrors();
				for (int i = 0; i < list.size(); i++) {
					message += list.get(i).getDefaultMessage() + ";";
				}
				return ReturnUtil.Error(message);
			}
			// 获取数据
			Integer pid = user.getPid();
			Integer cid = user.getCid();
			String mobile = user.getMobile().trim();
			String email = user.getEmail().trim();
			String account = user.getAccount().trim();
			String salt = StringUtil.getRandomString(32);
			String ncode = user.getNcode().trim();
			String password = user.getPassword().trim();
			String passwordn = PasswordUtil.createCustomPwd(password, salt);
			String uid = PasswordUtil.createCustomPwd(account, salt);
			// 数据验证
			if (account.equals("")) {
				return ReturnUtil.Error("用户名需填写！");
			} else if (mobile.equals("")) {
				return ReturnUtil.Error("手机号需填写！");
			} else if (user.getPassword().trim().equals("")) {
				return ReturnUtil.Error("密码需填写！");
			} else {
				String code = String.valueOf(redisUtil.get(mobile + "_register"));
				if (!ncode.equals(code)) {
					return ReturnUtil.Error("验证码错误！");
				} else {
					// 唯一性判断
					HixentAppUser userinfo1 = hixentAppUserService.getAppUserinfoByMobile(mobile);
					HixentAppUser userinfo2 = hixentAppUserService.findByAppUsername(account);
					HixentAppUser userinfo3 = hixentAppUserService.findByAppUserId(uid);
					if (userinfo1 != null || userinfo2 != null || userinfo3 != null) {
						return ReturnUtil.Error("用户名称或手机号重复,请重试！");
					}
					// 添加
					hixentAppUserService.insertAppUser(uid, account, passwordn, mobile, email, salt, cid, pid);
					return ReturnUtil.Success("添加成功");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 登录获取手机验证码 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=60000)
	@SystemHistoryAnnotation(opration = "登录获取手机验证码")
	@RequestMapping(value = "/loginCode", method = RequestMethod.POST)
	public ModelMap loginCode(@Valid ValidVerificationCode verificationCode, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("请填写正确的手机号！");
			} else {
				// 获取数据
				String mobile = verificationCode.getMobile().trim();
				HixentAppUser userinfo = hixentAppUserService.getAppUserinfoByMobile(mobile);
				if (userinfo == null) {
					return ReturnUtil.Error("该手机号未注册！");
				} else {
					double random = Math.pow(10, 5) + Math.random() * (Math.pow(10, 6) - Math.pow(10, 5) - 1);
					long l = new Double(random).longValue();
					String code = Long.toString(l);
					String signName = "莱茵斯科技";
					String templateCode;
					String templateParam = "{\"code\":\"" + code + "\"}";
					String outId = mobile + "_login";
					templateCode = "SMS_158015095";
					redisUtil.set(mobile + "_login", code, 60);
					AliyunSmsUtil.sendSms(mobile, signName, templateCode, templateParam, outId);
					return ReturnUtil.Success("验证码已经发到您的手机上了，请查收！");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 注册获取手机验证码 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=60000)
	@SystemHistoryAnnotation(opration = "注册获取手机验证码")
	@RequestMapping(value = "/registerCode", method = RequestMethod.POST)
	public ModelMap registerCode(@Valid ValidVerificationCode verificationCode, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("请填写正确的手机号！");
			} else {
				// 获取数据
				String mobile = verificationCode.getMobile().trim();
				HixentAppUser userinfo = hixentAppUserService.getAppUserinfoByMobile(mobile);
				if (userinfo == null) {
					double random = Math.pow(10, 5) + Math.random() * (Math.pow(10, 6) - Math.pow(10, 5) - 1);
					long l = new Double(random).longValue();
					String code = Long.toString(l);
					String signName = "莱茵斯科技";
					String templateCode;
					String templateParam = "{\"code\":\"" + code + "\"}";
					String outId = mobile + "_register";
					templateCode = "SMS_158015095";
					redisUtil.set(mobile + "_register", code, 60);
					AliyunSmsUtil.sendSms(mobile, signName, templateCode, templateParam, outId);
					return ReturnUtil.Success("验证码已经发到您的手机上了，请查收！");
				} else {
					return ReturnUtil.Error("该手机号已经注册！");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 修改密码获取手机验证码 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=60000)
	@SystemHistoryAnnotation(opration = "修改密码获取手机验证码")
	@RequestMapping(value = "/passwordCode", method = RequestMethod.POST)
	public ModelMap passwordCode(@Valid ValidVerificationCode verificationCode, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("请填写正确的手机号！");
			} else {
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();
				String auth = request.getHeader(jwtConfig.getJwtHeader());
				auth = auth.substring(7, auth.length());
				Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
				String userId = claims.getId();
				String[] userArr = userId.split("_");
				if (!userArr[0].equals("app")) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
				if (userinfo == null) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				// 获取数据
				String mobile = verificationCode.getMobile().trim();
				if(!mobile.equals(userinfo.getMobile())) {
					return ReturnUtil.Error("请输入账号关联的手机号！");
				}
				HixentAppUser userinfo1 = hixentAppUserService.getAppUserinfoByMobile(mobile);
				if (userinfo1 == null) {
					return ReturnUtil.Error("该手机号未注册！");
				} else {
					double random = Math.pow(10, 5) + Math.random() * (Math.pow(10, 6) - Math.pow(10, 5) - 1);
					long l = new Double(random).longValue();
					String code = Long.toString(l);
					String signName = "莱茵斯科技";
					String templateCode;
					String templateParam = "{\"code\":\"" + code + "\"}";
					String outId = userinfo1.getUid();
					templateCode = "SMS_158015094";
					redisUtil.set(mobile + "_password", code, 60);
					AliyunSmsUtil.sendSms(mobile, signName, templateCode, templateParam, outId);
					return ReturnUtil.Success("验证码已经发到您的手机上了，请查收！");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	
	/**
	 * 忘记密码获取手机验证码 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=60000)
	@SystemHistoryAnnotation(opration = "忘记密码获取手机验证码 ")
	@RequestMapping(value = "/forgetPasswordCode", method = RequestMethod.POST)
	public ModelMap forgetPasswordCode(@Valid ValidVerificationCode verificationCode, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("请填写正确的手机号！");
			} else {
				// 获取数据
				String mobile = verificationCode.getMobile().trim();
				HixentAppUser userinfo = hixentAppUserService.getAppUserinfoByMobile(mobile);
				if (userinfo == null) {
					return ReturnUtil.Error("没有该用户信息");
					
				} else {
					double random = Math.pow(10, 5) + Math.random() * (Math.pow(10, 6) - Math.pow(10, 5) - 1);
					long l = new Double(random).longValue();
					String code = Long.toString(l);
					 code = code.substring(0, 4);
					String signName = "莱茵斯科技";
					String templateCode;
					String templateParam = "{\"code\":\"" + code + "\"}";
					String outId = userinfo.getUid();
					templateCode = "SMS_158015094";
					redisUtil.set(mobile + "_forgetPassword", code, 60);
					AliyunSmsUtil.sendSms(mobile, signName, templateCode, templateParam, outId);
					return ReturnUtil.Success("验证码已经发到您的手机上了，请查收！");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	 /**
     * 忘记密码后修改密码
     * author Vareck
     */
   // @ApiLimitConfig(count=1,time=1000)
    @RequestMapping(value = "/forgetPasswordChange", method = RequestMethod.POST)
    @SystemHistoryAnnotation(opration = "忘记密码后修改密码")
    public ModelMap forgetPasswordChangeCode( @Valid ValidHixentAppUserMore user ){
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
    	    	HixentAppUser userinfo = hixentAppUserService.getAppUserinfoByMobile(mobile);
            	if( userinfo == null ){
            		return ReturnUtil.Error("没有该用户信息");
            	}else{
            		String     code     = String.valueOf(redisUtil.get(mobile+"_forgetPassword"));
            		if( !ncode.equals(code) ){
            			return ReturnUtil.Error("验证码错误！");
            		}else{
                    	String     salt       = userinfo.getSalt();
                    	String  passwordStart = userinfo.getPassword();
                	    String  passwordEnd   = PasswordUtil.createCustomPwd(password,salt);
                	    if( passwordStart.trim().equals(passwordEnd) ){
                	    	return ReturnUtil.Error("与旧密码一致，密码修改失败！");
                	    }else{
                	    	hixentAppUserService.updateAppUserPassWord(userinfo.getId(), passwordEnd);
							return ReturnUtil.Success("密码修改成功！");
                	    }
            		}
            	}
    	    }
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    } 
    /**
     * 验证验证码
     * author Vareck
     */
   // @ApiLimitConfig(count=1,time=1000)
    @RequestMapping(value = "/verifyCode", method = RequestMethod.POST)
    @SystemHistoryAnnotation(opration = " 验证验证码")
    public ModelMap verifyCode( @Valid ValidHixentAppUserMore user ){
    	try{
            //获取数据
    	 
    	    String   mobile     = user.getMobile().trim();
    	    String   ncode      = user.getNcode().trim();
    	    //数据验证
        	
                //获取用户信息
    	    	HixentAppUser userinfo = hixentAppUserService.getAppUserinfoByMobile(mobile);
            	if( userinfo == null ){
            		return ReturnUtil.Error("没有该用户信息");
            	}else{
            		String     code     = String.valueOf(redisUtil.get(mobile+"_forgetPassword"));
            		if( !ncode.equals(code) ){
            			return ReturnUtil.Error("验证码错误！");
            		}
            		return ReturnUtil.Success("验证成功！");
            	}
    	   
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
        }
    } 
}