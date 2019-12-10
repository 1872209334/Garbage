package com.qf.controller.api.app;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.jsonwebtoken.Claims;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.valid.ValidHixentAppUserMore;
import com.qf.model.admin.valid.ValidVerificationCode;
import com.qf.service.app.HixentAppUserService;
import com.qf.util.AliyunSmsUtil;
import com.qf.util.JwtUtil;
import com.qf.util.PasswordUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;

@RestController
@RequestMapping("/app/user/common")
public class ApiAppCommonController {

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private JwtConfig jwtConfig;

	@Resource
	private JwtUtil jwtUtil;

	@Autowired
	private HixentAppUserService hixentAppUserService;

	/**
	 * App端登出 author Vareck
	 */
	@RequestMapping(value = "/logoutApp", method = RequestMethod.GET)
	public ModelMap logout() {
		try {
			// 获取request
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = (HttpServletRequest) requestAttributes
					.resolveReference(RequestAttributes.REFERENCE_REQUEST);
			// jwt登出
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			redisUtil.del(claims.getId());
			// 获取当前的Subject
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.logout();
			return ReturnUtil.Success("登出成功");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * app修改手机号 author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app修改手机号")
	@RequestMapping(value = "/changeMobile", method = RequestMethod.POST)
	public ModelMap changeAccount(String newMobile, String ncode) {
		try {
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
			String code = String.valueOf(redisUtil.get(newMobile + "_changeMobile"));
			if (!ncode.equals(code)) {
				return ReturnUtil.Error("验证码错误！");
			}
			
			// 唯一性判断
			HixentAppUser userinfo1 = hixentAppUserService.getAppUserinfoByMobile(newMobile);
			if (userinfo1 != null && userinfo.getId() != userinfo1.getId()) {
				return ReturnUtil.Error("手机号重复,请重试！");
			}
			Integer updateAppUserMobile = hixentAppUserService.updateAppUserMobile(userinfo.getId(), newMobile);
			if (updateAppUserMobile > 0) {
				return ReturnUtil.Success("手机号修改成功！");
			} else {
				return ReturnUtil.Error("手机号修改失败！");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 修改手机号获取手机验证码 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=60000)
	@SystemHistoryAnnotation(opration = "修改密码获取手机验证码 ")
	@RequestMapping(value = "/changeMobileCode", method = RequestMethod.POST)
	public ModelMap changeMobileCode(@Valid ValidVerificationCode verificationCode, BindingResult bindingResult) {
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
				
				// 唯一性判断
				HixentAppUser userinfo1 = hixentAppUserService.getAppUserinfoByMobile(mobile);
				if (userinfo1 != null && userinfo.getId() != userinfo1.getId()) {
					return ReturnUtil.Error("手机号重复,请重试！");
				} else {

					double random = Math.pow(10, 5) + Math.random() * (Math.pow(10, 6) - Math.pow(10, 5) - 1);
					long l = new Double(random).longValue();
					String code = Long.toString(l);
					String signName = "莱茵斯科技";
					String templateCode;
					String templateParam = "{\"code\":\"" + code + "\"}";
					String outId = userinfo.getUid();
					templateCode = "SMS_160300429";
					redisUtil.set(mobile + "_changeMobile", code, 60);
					AliyunSmsUtil.sendSms(mobile, signName, templateCode, templateParam, outId);
					return ReturnUtil.Success("验证码已经发到您的手机上了，请查收！");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 修改密码 author Vareck
	 */
	// @ApiLimitConfig(count=1,time=1000)
	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	@SystemHistoryAnnotation(opration = "修改密码")
	public ModelMap changePassword(@Valid ValidHixentAppUserMore user) {
		try {
			// 获取数据
			String password = user.getPassword().trim();
			String mobile = user.getMobile().trim();
			String ncode = user.getNcode().trim();
			// 数据验证
			if (password.equals("")) {
				return ReturnUtil.Error("密码需填写！");
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
				
				String code = String.valueOf(redisUtil.get(mobile + "_password"));
				if (!ncode.equals(code)) {
					return ReturnUtil.Error("验证码错误！");
				} else {
					String salt = userinfo.getSalt();
					String passwordStart = userinfo.getPassword();
					String passwordEnd = PasswordUtil.createCustomPwd(password, salt);
					if (passwordStart.trim().equals(passwordEnd)) {
						return ReturnUtil.Error("与旧密码一致，密码修改失败！");
					} else {
						hixentAppUserService.updateAppUserPassWord(userinfo.getId(), passwordEnd);
						return ReturnUtil.Success("密码修改成功！");
					}
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}