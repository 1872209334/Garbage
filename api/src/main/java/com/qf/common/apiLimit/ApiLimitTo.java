package com.qf.common.apiLimit;  
	 
	 

import java.util.Map;
import java.util.HashMap;    
import java.util.Timer;  
import java.util.TimerTask;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;  

import org.aspectj.lang.JoinPoint;  
import org.aspectj.lang.annotation.Aspect;  
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.qf.common.JwtConfig;
import com.qf.common.apiLimit.ApiLimitException;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;
import com.qf.util.JwtUtil;

import io.jsonwebtoken.Claims;
   


/**
 * 接口调用频率限制
 * author Vareck
 */ 
@Aspect  
@Component  
public class ApiLimitTo {  


	
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
	
	@Autowired 
	private HixentUserService hixentUserService; 
	
	@Autowired 
	private HixentAppUserService hixentAppUserService; 
    
    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLogger");
    private Map<String, Integer> rmap = new HashMap<String,Integer>();  
		
    
    
    @Before("@annotation(limit)")  
    public void apiLimit(final JoinPoint joinPoint, ApiLimitConfig limit) throws ApiLimitException {  
    	try {  
            //获取request
    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
            if (request == null) {  
                throw new ApiLimitException("方法中缺失必要的参数");  
            }  
    		//从切面织入点处通过反射机制获取织入点处的方法 
    		MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
    		//获取切入点所在的方法 
    		Method method = signature.getMethod();
            //请求的参数
            Object[] param = joinPoint.getArgs();
            String params  = JSON.toJSONString(param);
    		//获取请求的方法名 
    		String methodName = method.getName(); 
    		String username;
	        String   uri    = request.getRequestURI();
	        String[] uriArr = uri.split("/");
            if( uriArr[1].equals("get") || uriArr[1].equals("getApp") ){
            	if( methodName.equals("forgetPassword") ){
            		username = "forgetPassword";
            	}else{
                	String[] arr1 	= params.split(":");
                	if( arr1.length > 0 ){
                    	String[] arr2 	= arr1[1].split(",");
                    	username = arr2[0];
                    	username = username.substring(1,username.length() - 1);
                	}else{
                		username = "undefined";
                	}
            	}
            }else{
	            //获取用户名 
	        	String auth      = request.getHeader(jwtConfig.getJwtHeader());
	        	if(auth.equals("") || auth==null){
	        		username = "undefined";
	        	}else{
		        	auth             = auth.substring(7, auth.length());
		        	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
	                String userId    = claims.getId();
	                String[] userArr = userId.split("_");
	                if(userArr[0].equals("admin")){
	                	HixentUser datau = hixentUserService.findByUserId(userArr[1]);
	                	username = datau.getAccount();
	                }else if(userArr[0].equals("app")){
	                	HixentAppUser datau = hixentAppUserService.findByAppUserId(userArr[1]);
	                	username = datau.getAccount();
	                }else{
	                	username = "undefined";
	                }
	        	}
            }
            String key = "api_limit_"+username+"_"+methodName;  
            if( rmap.get(key) == null || rmap.get(key) == 0 ){  
                rmap.put(key,1);  
            }else{  
                rmap.put(key,rmap.get(key)+1);  
            }  
            int count = rmap.get(key);  
            if (count > 0) {  
                Timer timer = new Timer();  
                TimerTask task  = new TimerTask(){    //创建一个新的计时器任务。  
                    @Override  
                    public void run() {  
                        rmap.remove(key);  
                    }  
                };  
                timer.schedule(task, limit.time());  
  
                }  
                if (count > limit.count()) {   
                    throw new ApiLimitException();  
                }  
            } catch (ApiLimitException e) {  
                throw e;  
            } catch (Exception e) {  
                logger.error("发生异常: ", e);  
            }  
        }  
    }  



/*     
package com.qf.common.apiLimit;  
	 
	 

import java.util.Map;
import java.util.HashMap;    
import java.util.Timer;  
import java.util.TimerTask;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;  

import org.aspectj.lang.JoinPoint;  
import org.aspectj.lang.annotation.Aspect;  
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.qf.common.apiLimit.ApiLimitException;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
   


*//**
 * 接口调用频率限制
 * author Vareck
 *//* 
@Aspect  
@Component  
public class ApiLimitTo {  


	
    private static final Logger logger = LoggerFactory.getLogger("RequestLimitLogger");
    private Map<String, Integer> rmap = new HashMap<String,Integer>();  
		
    
    
    @Before("@annotation(limit)")  
    public void apiLimit(final JoinPoint joinPoint, ApiLimitConfig limit) throws ApiLimitException {  
    	try {  
            //获取request
    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
            if (request == null) {  
                throw new ApiLimitException("方法中缺失必要的参数");  
            }  
    		//从切面织入点处通过反射机制获取织入点处的方法 
    		MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
    		//获取切入点所在的方法 
    		Method method = signature.getMethod();
            //请求的参数
            Object[] param = joinPoint.getArgs();
            String params  = JSON.toJSONString(param);
    		//获取请求的方法名 
    		String methodName = method.getName(); 
    		String username;
	        String   uri    = request.getRequestURI();
	        String[] uriArr = uri.split("/");
            if( uriArr[1].equals("get") || uriArr[1].equals("getApp") ){
            	if( methodName.equals("forgetPassword") ){
            		username = "forgetPassword";
            	}else{
                	String[] arr1 	= params.split(":");
                	if( arr1.length > 0 ){
                    	String[] arr2 	= arr1[1].split(",");
                    	username = arr2[0];
                    	username = username.substring(1,username.length() - 1);
                	}else{
                		username = "undefined";
                	}
            	}
            }else{
	            //获取用户名 
	            String key   = SecurityUtils.getSubject().getPrincipal().getClass().getName();
	            if( key.equals("com.qf.model.admin.HixentUser") ){
		        	HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
		        	username     		= userinfo.getAccount();
	            }else if( key.equals("com.qf.model.admin.HixentAppUser") ){
		        	HixentAppUser userinfo = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
		        	username     		= userinfo.getAccount();
	            }else{
	            	username = "undefined";
	            }
            }
            String key = "api_limit_"+username+"_"+methodName;  
            if( rmap.get(key) == null || rmap.get(key) == 0 ){  
                rmap.put(key,1);  
            }else{  
                rmap.put(key,rmap.get(key)+1);  
            }  
            int count = rmap.get(key);  
            if (count > 0) {  
                Timer timer = new Timer();  
                TimerTask task  = new TimerTask(){    //创建一个新的计时器任务。  
                    @Override  
                    public void run() {  
                        rmap.remove(key);  
                    }  
                };  
                timer.schedule(task, limit.time());  
  
                }  
                if (count > limit.count()) {   
                    throw new ApiLimitException();  
                }  
            } catch (ApiLimitException e) {  
                throw e;  
            } catch (Exception e) {  
                logger.error("发生异常: ", e);  
            }  
        }  
    }  */