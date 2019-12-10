package com.qf.common;



import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;



@EnableAsync
@Configuration
@ComponentScan("com.qf")



/**
 * 多线程配置类
 * author Vareck
 */ 
public class AsyncTaskConfig implements AsyncConfigurer {

	
	
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(15);    // 最小线程数
        taskExecutor.setMaxPoolSize(30);     // 最大线程数
        taskExecutor.setQueueCapacity(50);   // 等待队列
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
	
    
    
}