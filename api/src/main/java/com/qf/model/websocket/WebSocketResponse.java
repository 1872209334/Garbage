package com.qf.model.websocket;



/**
 * author   Vareck
 */
public class WebSocketResponse {

    private String responseMessage;

    public WebSocketResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    
    
    
}