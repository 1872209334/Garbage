package com.qf.common.filter;



import com.qf.util.ReturnUtil;
import com.qf.util.RedisUtil;
import com.qf.util.JwtUtil;
import com.qf.util.JsonUtil;
import com.qf.util.PasswordUtil;
import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



/**
 * author Vareck
 */ 
@Component
@WebFilter(filterName = "apiFilter", urlPatterns = "/api/*")
public class ApiFilter extends OncePerRequestFilter {


	
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private RedisUtil redisUtil;
    
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    	/*跨域配置*/
        HttpServletResponse rep  = (HttpServletResponse)response;
		HttpServletRequest  req  = (HttpServletRequest)request; 
        //允许的IP域名
        String []  allowDomain     = {"http://116.62.189.63","http://127.0.0.1:8086","http://firecontrol.hixent.com","firecontrol.hixent.com"};
        Set<String> allowedOrigins = new HashSet<String>(Arrays.asList(allowDomain));
        String originHeader        = ((HttpServletRequest) req).getHeader("Origin");
        //if (allowedOrigins.contains(originHeader)) {
//            response.setHeader("Access-Control-Allow-Origin", originHeader);
        	response.setHeader("Access-Control-Allow-Origin", "*");
            //允许的方法
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
            //时间限制
            response.setHeader("Access-Control-Max-Age", "7200");
            response.setHeader("Access-Control-Allow-Headers", "Origin,Authorization, X-Requested-With, Content-Type, Accept");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        //}
        /*token判断*/
    	if (request.getMethod().equals("OPTIONS")){
    		rep.setStatus(HttpServletResponse.SC_OK);
    	}else{
        	String auth = request.getHeader(jwtConfig.getJwtHeader());
            if ((auth != null) && (auth.length() > 7)) {
                String headStr    = auth.substring(0, 6);
                if (headStr.compareTo(jwtConfig.getTokenHead()) == 0) {
                    auth          = auth.substring(7, auth.length());
                    Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
                    if (claims != null) {
                        String userId       = claims.getId();
                        String userName     = claims.getSubject();
                        String sessionId    = claims.getAudience();
                    	//判断是否是webAPP最新的token
                        if( redisUtil.get(userId) != null ){
                            String[] userArr = userId.split("_");
                        	if( redisUtil.get("admin_"+userArr[1]).equals(PasswordUtil.createCustomPwd(auth.trim(),jwtConfig.getSecret())) ){
                        		request.setAttribute("userId", userArr[1]);
                                request.setAttribute("userName", userName);
                                CustomHttpServletRequest customHttpServletRequest = new CustomHttpServletRequest(request);
                                customHttpServletRequest.putHeader("Cookie", "WEBID="+ sessionId);
                                chain.doFilter(customHttpServletRequest, response);
                                //缓存token
                               // redisUtil.del("admin_"+userArr[1]);
                                redisUtil.set("admin_"+userArr[1],PasswordUtil.createCustomPwd(auth.trim(),jwtConfig.getSecret()),7200);
                                return;
                        	}else {
                        		//异地登录
                        		 response.setCharacterEncoding("UTF-8");
                                 response.setContentType("application/json; charset=utf-8");
                                 response.setStatus(HttpServletResponse.SC_OK);
                                 response.getWriter().write(JsonUtil.toJson(ReturnUtil.ErrorLogin("异地登录")));
                                 return;
                        	}
                        }
                    }
                }
            }
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
           response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(JsonUtil.toJson(ReturnUtil.ErrorLogin("验证失败")));
            return;
    	}
    }
    
    
    
}