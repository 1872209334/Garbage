package com.qf.service.websocket;



import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.qf.common.websocket.WebSocketInterceptor;
import com.qf.util.DateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
 

/**
 * websocket广播和点对点发送
 * @author Vareck
 */
@Service
public class WebSocketService implements WebSocketHandler {
	
    private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptor.class);
    
    //在线用户列表
    private static final Map<String, WebSocketSession> users;
 
   
    static {
        users = new HashMap<>();
    }
    
    
    
    //新增websocket
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	String ID = session.getUri().toString().split("ID=")[1];
        if (ID != null) {
            users.put(ID, session);
            JSONObject obj = new JSONObject();
            {
            	obj.put("msg","成功建立socket连接");
            }
            //java对象变成json对象
            String Jsonmessage=JSONObject.toJSONString(obj);
            session.sendMessage(new TextMessage(Jsonmessage));
        }
    }
 
    
    
    //接收websocket信息
    @Override
	public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
    	try{
	    	 String message = webSocketMessage.getPayload().toString();
	    	 //System.out.println(jsonobject.get("id"));
	    	 //System.out.println(jsonobject.get("message")+":来自"+(String)webSocketSession.getAttributes().get("WEBSOCKET_USERID")+"的消息");
	    	 //sendMessageToUser(jsonobject.get("id")+"",new TextMessage("服务器收到了，hello!"));
    	 }catch(Exception e){
    		 e.printStackTrace();
         }
		
	}
 
    
    
    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String clientId, TextMessage message) {
        if ( users.get(clientId) == null ) {
        	return false;
        }
        WebSocketSession session = users.get(clientId);
        if (!session.isOpen()) {
        	return false;
        }
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
 
    
    
    /**
     * 广播信息
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess   = true;
        Set<String> clientIds    = users.keySet();
        WebSocketSession session = null;
        for (String clientId : clientIds) {
            try {
                session = users.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }
        return  allSendSuccess;
    }
 
    
 
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
    	logger.error("连接出错");
        users.remove(getClientId(session));
    }
 
    
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.error("连接已关闭：" + status);
        users.remove(getClientId(session));
    }
 
    
    
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
 
    
    
    /**
     * 获取用户标识
     * @param session
     * @return
     */
    private Integer getClientId(WebSocketSession session) {
        try {
            Integer clientId = (Integer) session.getAttributes().get("WEBSOCKET_USERID");
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
    
    
    
}