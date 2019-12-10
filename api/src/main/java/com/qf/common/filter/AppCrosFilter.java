package com.qf.common.filter;



import java.io.IOException; 

import javax.servlet.Filter; 
import javax.servlet.FilterChain; 
import javax.servlet.FilterConfig; 
import javax.servlet.ServletException; 
import javax.servlet.ServletRequest; 
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse; 
import org.springframework.stereotype.Component; 



/**
 * author Vareck
 */ 
@Component 
@WebFilter(filterName = "AppCrosFilter", urlPatterns = "/getApp/*")
public class AppCrosFilter implements Filter{ 
	


    @Override
    public void destroy() {

    }
    
	@Override 
	public void doFilter( ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException { 
        HttpServletResponse response = (HttpServletResponse) rep;
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许的方法
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
        //时间限制
        response.setHeader("Access-Control-Max-Age", "7200");
        response.setHeader("Access-Control-Allow-Headers", "Origin,Authorization,X-Requested-With, Content-Type, Accept");
        response.setHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(req, rep); 
	} 
	
    @Override
    public void init(FilterConfig arg1) throws ServletException {

    }

    
    
}