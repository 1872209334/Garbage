package com.qf.common.websocket;
 


import java.util.Map;
 
//import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;



/**
 * websocket拦截器
 * author Vareck
 */ 
public class WebSocketInterceptor implements HandshakeInterceptor {
	
	
	
    //握手前
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            String ID = request.getURI().toString().split("ID=")[1];
            //ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            //HttpSession session = serverHttpRequest.getServletRequest().getSession();
            map.put("WEBSOCKET_USERID",ID);
        }
        return true;
    }
 
    
    
    //握手后
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        //TODO
    }
    
    
    
}