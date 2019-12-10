package com.qf.common.systemLog;



import com.alibaba.fastjson.JSON;

import com.qf.model.admin.HixentLog;
import com.qf.model.admin.HixentUser;
import com.qf.common.JwtConfig;
import com.qf.model.admin.HixentAppUser;
import com.qf.service.admin.HixentLogService;
import com.qf.service.admin.HixentUserService;
import com.qf.service.app.HixentAppUserService;
import com.qf.util.JwtUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;

import org.aspectj.lang.JoinPoint; 
import org.aspectj.lang.annotation.Aspect;  
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;  
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder; 
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest; 



/** 
 * 管理员使用日志配置类
 * author Vareck
 */ 
@Aspect 
@Component 
public class SystemLogConfig { 


	
    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private JwtUtil jwtUtil;
    
	@Autowired 
	private HixentLogService hixentLogService; 
	
	@Autowired 
	private HixentUserService hixentUserService; 
	
	@Autowired 
	private HixentAppUserService hixentAppUserService; 
	
    
    
	@Pointcut("@annotation(com.qf.common.systemLog.SystemHistoryAnnotation)") 
	public void controllerAspect() {
		
	}


	
	@AfterReturning("controllerAspect()")
	public void doServiceAfterReturning(JoinPoint joinPoint) { 
	    try{
	        //获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		    HttpServletRequest request = requestAttributes.getRequest();
	        //数据日志
			HixentLog hixentHistory = new HixentLog();
			//从切面织入点处通过反射机制获取织入点处的方法 
			MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
			//获取切入点所在的方法 
			Method method = signature.getMethod();
			//获取操作
			SystemHistoryAnnotation sLog = method.getAnnotation(SystemHistoryAnnotation.class); 
			if ( sLog != null ) { 
				hixentHistory.setOpration(sLog.opration());
			}
			//获取请求的类名 
			String controllerName = joinPoint.getTarget().getClass().getName(); 
			hixentHistory.setController(controllerName);
			//获取请求的方法名 
			String methodName = method.getName(); 
			hixentHistory.setMethod(methodName);
	        //请求的参数
	        Object[] param = joinPoint.getArgs();
	        String params  = JSON.toJSONString(param);
	        hixentHistory.setParams(params);
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
	        hixentHistory.setUsername(username);
	        //获取用户ip地址 
	        hixentHistory.setIp(request.getRemoteAddr());
	        //错误消息
	        hixentHistory.setMessage("");
	        /*录入数据*/
	        hixentLogService.insert(hixentHistory);
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
	} 

	

	@AfterThrowing(value="controllerAspect()",throwing="e")
	public void doServiceAfterThrowing(JoinPoint joinPoint,RuntimeException e) { 
	    try{
	        //获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		    HttpServletRequest request = requestAttributes.getRequest();
	   
	        //数据日志
			HixentLog hixentHistory = new HixentLog();
			//从切面织入点处通过反射机制获取织入点处的方法 
			MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
			
			//获取切入点所在的方法 
			Method method = signature.getMethod();
			
			//获取操作
			SystemHistoryAnnotation sLog = method.getAnnotation(SystemHistoryAnnotation.class); 
			if ( sLog != null ) { 
				hixentHistory.setOpration(sLog.opration());
			}
			
			//获取请求的类名 
			String controllerName = joinPoint.getTarget().getClass().getName(); 
			hixentHistory.setController(controllerName);
			//获取请求的方法名 
			String methodName = method.getName(); 
			hixentHistory.setMethod(methodName);
	        //请求的参数
	        Object[] param = joinPoint.getArgs();
	       
	        String params  = JSON.toJSONString(param);
	        
	        hixentHistory.setParams(params);
	        String username;
	       
	        if( methodName.equals("doLogin") || methodName.equals("registerCode") || methodName.equals("passwordCode") || methodName.equals("register") || methodName.equals("forgetPassword") ){
	        	
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
	        	 
	        	if(!ToolUtil.StringNotBlank(auth)||auth.equals("") ){
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
	        
	        hixentHistory.setUsername(username);
	        //获取用户ip地址 
	        hixentHistory.setIp(request.getRemoteAddr());
	        //错误消息
	       
	        hixentHistory.setMessage("异常："+e.getMessage());
	        /*录入数据*/
	       
	        hixentLogService.insert(hixentHistory);
	       
	    } catch (Exception ke) {
	    	
	    	throw new RuntimeException(ke.getMessage());
	    }
	}
	
	
	
}




/*package com.qf.common.systemLog;



import com.alibaba.fastjson.JSON;

import com.qf.model.admin.HixentLog;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.HixentAppUser;
import com.qf.service.admin.HixentLogService;

import org.aspectj.lang.JoinPoint; 
import org.aspectj.lang.annotation.Aspect;  
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;  
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder; 
import org.springframework.web.context.request.ServletRequestAttributes;
import org.apache.shiro.SecurityUtils;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest; 



*//** 
 * 管理员使用日志配置类
 * author Vareck
 *//* 
@Aspect 
@Component 
public class SystemLogConfig { 


	 
	@Autowired 
	private HixentLogService hixentLogService; 
	
    
    
	@Pointcut("@annotation(com.qf.common.systemLog.SystemHistoryAnnotation)") 
	public void controllerAspect() {
		
	}


	
	@AfterReturning("controllerAspect()")
	public void doServiceAfterReturning(JoinPoint joinPoint) { 
	    try{
	        //获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		    HttpServletRequest request = requestAttributes.getRequest();
	        //数据日志
			HixentLog hixentHistory = new HixentLog();
			//从切面织入点处通过反射机制获取织入点处的方法 
			MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
			//获取切入点所在的方法 
			Method method = signature.getMethod();
			//获取操作
			SystemHistoryAnnotation sLog = method.getAnnotation(SystemHistoryAnnotation.class); 
			if ( sLog != null ) { 
				hixentHistory.setOpration(sLog.opration());
			}
			//获取请求的类名 
			String controllerName = joinPoint.getTarget().getClass().getName(); 
			hixentHistory.setController(controllerName);
			//获取请求的方法名 
			String methodName = method.getName(); 
			hixentHistory.setMethod(methodName);
	        //请求的参数
	        Object[] param = joinPoint.getArgs();
	        String params  = JSON.toJSONString(param);
	        hixentHistory.setParams(params);
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
		        	username     		   = userinfo.getAccount();
	            }else{
	            	username = "undefined";
	            }
	        }
	        hixentHistory.setUsername(username);
	        //获取用户ip地址 
	        hixentHistory.setIp(request.getRemoteAddr());
	        //错误消息
	        hixentHistory.setMessage("");
	        录入数据
	        hixentLogService.insert(hixentHistory);
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
	} 

	

	@AfterThrowing(value="controllerAspect()",throwing="e")
	public void doServiceAfterThrowing(JoinPoint joinPoint,RuntimeException e) { 
	    try{
	        //获取request
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		    HttpServletRequest request = requestAttributes.getRequest();
	   
	        //数据日志
			HixentLog hixentHistory = new HixentLog();
			//从切面织入点处通过反射机制获取织入点处的方法 
			MethodSignature signature = (MethodSignature) joinPoint.getSignature(); 
			
			//获取切入点所在的方法 
			Method method = signature.getMethod();
			//获取操作
			SystemHistoryAnnotation sLog = method.getAnnotation(SystemHistoryAnnotation.class); 
			if ( sLog != null ) { 
				hixentHistory.setOpration(sLog.opration());
			}
			//获取请求的类名 
			String controllerName = joinPoint.getTarget().getClass().getName(); 
			hixentHistory.setController(controllerName);
			//获取请求的方法名 
			String methodName = method.getName(); 
			hixentHistory.setMethod(methodName);
	        //请求的参数
	        Object[] param = joinPoint.getArgs();
	        String params  = JSON.toJSONString(param);
	        hixentHistory.setParams(params);
	        String username;
	        if( methodName.equals("doLogin") || methodName.equals("registerCode") || methodName.equals("passwordCode") || methodName.equals("register") || methodName.equals("forgetPassword") ){
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
	            }else if(key.equals("com.qf.model.admin.HixentAppUser")){
		        	HixentAppUser userinfo = (HixentAppUser) SecurityUtils.getSubject().getPrincipal();
		        	username     		= userinfo.getAccount();
	            }else{
	            	username = "undefined";
	            }
	        }
	        hixentHistory.setUsername(username);
	        //获取用户ip地址 
	        hixentHistory.setIp(request.getRemoteAddr());
	        //错误消息
	        hixentHistory.setMessage("异常："+e.getMessage());
	        录入数据
	        hixentLogService.insert(hixentHistory);
	    } catch (Exception ke) {
	    	throw new RuntimeException(ke.getMessage());
	    }
	}
	
	
	
}*/