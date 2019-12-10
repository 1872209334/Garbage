package com.qf.common.mybatis;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;



/**
 * MyBatis扫描接口
 * author Vareck
 */ 
@Configuration
public class MyBatisMapperScannerConfig {
	
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("adminSqlSessionFactory");
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("fireSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.qf.mapper");
        Properties properties = new Properties();
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }
    
}