package com.qf.common.shiro;



import org.apache.shiro.authc.UsernamePasswordToken;



/**
 * author Vareck
 */
public class CustomerAuthenticationToken extends UsernamePasswordToken {

    public CustomerAuthenticationToken(String username, String password, boolean rememberMe) {
        super(username, password, rememberMe);
    }
    
    public CustomerAuthenticationToken(String username, String password, boolean rememberMe,String host) {
        super(username, password, rememberMe,host);
    }

}