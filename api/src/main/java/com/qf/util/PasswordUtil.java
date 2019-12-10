/*
 * Copyright (c) 2018 <1253850806@qq.com> All rights reserved.
 */

package com.qf.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 加密工具类
 * author Vareck
 */
public class PasswordUtil {

    //加密
    public static String createCustomPwd(String password, String salt){
        return new SimpleHash("md5",password,ByteSource.Util.bytes(salt),1).toHex();
    }
    
}