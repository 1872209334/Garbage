package com.qf.common.filter;



import java.io.IOException; 
import java.util.Set; 
import java.util.HashSet;
import java.util.Arrays;

import javax.servlet.Filter; 
import javax.servlet.FilterChain; 
import javax.servlet.FilterConfig; 
import javax.servlet.ServletException; 
import javax.servlet.ServletRequest; 
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component; 



/**
 * author Vareck
 */ 
@Component 
@WebFilter(filterName = "crosFilter", urlPatterns = "/get/*")
public class CrosFilter implements Filter{ 
	


    @Override
    public void destroy() {

    }
    
	@Override 
	public void doFilter( ServletRequest req, ServletResponse rep, FilterChain chain) throws IOException, ServletException { 
        HttpServletResponse response = (HttpServletResponse) rep;
        //允许的IP域名
        String []  allowDomain     = {"http://116.62.189.63","http://127.0.0.1:8086","http://firecontrol.hixent.com","firecontrol.hixent.com"};
        Set<String> allowedOrigins = new HashSet<String>(Arrays.asList(allowDomain));
        String originHeader        = ((HttpServletRequest) req).getHeader("Origin");
        if (allowedOrigins.contains(originHeader)) {
            response.setHeader("Access-Control-Allow-Origin", originHeader);
            //允许的方法
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT");
            //时间限制
            response.setHeader("Access-Control-Max-Age", "7200");
            response.setHeader("Access-Control-Allow-Headers", "Origin,Authorization,X-Requested-With, Content-Type, Accept");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
		chain.doFilter(req, rep); 
	} 
	
    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    
    
}