package com.qf.common.systemLog;



import java.lang.annotation.*; 



/** 
 * 管理员使用日志注解类 
 * author Vareck
 */ 
@Target(ElementType.METHOD) 		    //注解放置的目标位置,METHOD是可注解在方法级别上 
@Retention(RetentionPolicy.RUNTIME)     //注解在哪个阶段执行 
@Documented 						    //生成文档 
public @interface SystemHistoryAnnotation { 
	String opration() 	  default "";
}