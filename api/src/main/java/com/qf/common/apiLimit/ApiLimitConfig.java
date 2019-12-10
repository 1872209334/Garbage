package com.qf.common.apiLimit;  



import org.springframework.core.Ordered;  
import org.springframework.core.annotation.Order;  
import java.lang.annotation.*;  



@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
@Documented   
@Order(Ordered.HIGHEST_PRECEDENCE)  



/**
 * 接口调用频率限制配置类
 * author Vareck
 */ 
public @interface ApiLimitConfig {  
	
    //允许访问的次数，默认值MAX_VALUE 
    int count() default Integer.MAX_VALUE;  
      
    //时间段，单位为毫秒，默认值一分钟 
    long time() default 60000;  
		
}  