package com.qf.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qf.common.JwtConfig;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;

import io.jsonwebtoken.Claims;


public class UserLoginUtil {
	
	@Autowired
	private HixentUserService hixentUserService;
	
	@Autowired 
	private HixentAppUserService hixentAppUserService; 
	
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
    
   
	public HixentAppUser checkAppUserLogin() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	    HttpServletRequest request = requestAttributes.getRequest();
		String auth      = request.getHeader(jwtConfig.getJwtHeader());
    	auth             = auth.substring(7, auth.length());
    	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
        String userId    = claims.getId();
        String[] userArr = userId.split("_");
        if( !userArr[0].equals("app") ){
        ReturnUtil.Error("已退出，请重新登录！");
        }
    	HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
		if(userinfo == null){
         ReturnUtil.Error("已退出，请重新登录！");
        }
		return userinfo;
	}
	
    public HixentUser checkAdminUserLogin() {
    	ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		String auth = request.getHeader(jwtConfig.getJwtHeader());
		auth = auth.substring(7, auth.length());
		Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
		String userId = claims.getId();
		String[] userArr = userId.split("_");
		if (!userArr[0].equals("admin")) {
		    ReturnUtil.Error("已退出，请重新登录！");
		}
		HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
		if (userinfo == null) {
			ReturnUtil.Error("已退出，请重新登录！");
		}
		return userinfo;
    }
}
