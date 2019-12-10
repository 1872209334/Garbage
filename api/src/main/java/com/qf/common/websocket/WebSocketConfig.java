package com.qf.common.websocket;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.qf.common.websocket.WebSocketInterceptor;
import com.qf.service.websocket.WebSocketService;
 
 

/**
 * websocket配置类
 * author Vareck
 */ 
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer{
 
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
	    registry.addHandler(new WebSocketService(), "/webSocketService/{ID}").setAllowedOrigins("*").addInterceptors(new WebSocketInterceptor());
		
	}

}