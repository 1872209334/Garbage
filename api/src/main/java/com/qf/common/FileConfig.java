package com.qf.common;



import org.springframework.context.annotation.Configuration; 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import javax.servlet.MultipartConfigElement;



/**
 * 文件上传配置类
 * author Vareck
 */ 
@Configuration 
public class FileConfig extends WebMvcConfigurerAdapter{ 
	
	
	
	/**
     * 在配置文件中配置的文件保存路径
     */ 
	@Value("${image.location}") 
	private String location; 
	
	
	
	@Bean 
	public MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory(); 
		//文件最大KB,MB 
		factory.setMaxFileSize("2MB"); 
		//设置总上传数据总大小 
		factory.setMaxRequestSize("10MB"); 
		return factory.createMultipartConfig(); 
	} 
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("http://116.62.189.63/resource/");
	    super.addResourceHandlers(registry);
	}

}