package com.qf.common.apiLimit; 



/**
 * 接口调用频率限制
 * author Vareck
 */ 
public class ApiLimitException extends Exception {  

	public ApiLimitException() {  
		super("请求过于频繁！");  
    } 
      
    public ApiLimitException(String message) {  
		super(message);  
    }  
      
}  