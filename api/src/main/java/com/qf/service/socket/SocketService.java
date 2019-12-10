package com.qf.service.socket;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.qf.util.CommonUtil;

import java.net.*;
import java.io.IOException;



@Service
public class SocketService  {
	
	
	
	@Value("${socket.port}")
    private int port;
	@Value("${socket.send.url}")
    private String send_url;
	@Value("${socket.send.port}")
    private int send_port;

	
    
    /** 
     * socket指令下发
     * author vareck
     */  
    @Async
    public void sendSocket( String message ) throws IOException {
    	DatagramSocket ds = new DatagramSocket(port);
    	byte[] buf = CommonUtil.hexStringToByte(message);
        DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(send_url), send_port);
        ds.send(dp);
        ds.close();
    }
    
    
    
}