/*
 * Copyright (c) 2018 <1253850806@qq.com> All rights reserved.
 */



package com.qf.util;



/**
 * Math工具类
 * author Vareck
 */
public class MathUtil {
	
	/**
	 * 任意字节转换成10进制数
	 */
	public static String getByteToDec(Integer[] data_arr , float multiple) {
		String str = "";
		int i;
		for(i=0;i<data_arr.length;i++){
			String strHex = Integer.toHexString(data_arr[i]);   //十进制转十六进制
			if( strHex.length() < 2 ){
				strHex = "0"+strHex;
			}
			str += strHex;
		}
		float num  = Long.parseLong(str,16);                    //十六进制转十进制
		float numk = num/multiple;
		int n      = (int) numk;
		return String.valueOf(n);
	}
	

	
	/**
	 * 10进制数转换任意长度字节
	 */
	public static String getDecToByte(Integer dec_num,Integer byte_length){
		//1.转换成16进制
		String hex_num_str   = Integer.toHexString(dec_num);
        Integer total_length = byte_length * 2;
		//2.补位到 $byte_length*2
		if( hex_num_str.length() < total_length ){
			//补0操作
			String str = "";
			for( int i=0 ; i<total_length-hex_num_str.length();i++ ){
				str+="0";
			}
			hex_num_str = str+hex_num_str;
		}
		//3.按照每2位分割一次 并返回
		String[] hex_split_arr  = hex_num_str.split("");
		String hex_split_arrk   = "";
		//4.将分割完的数组中的16进制转换成10进制
		for( int j=0;j<hex_split_arr.length;j++ ){
			if( j==0 || j==1 ){
				String strk = hex_split_arr[0]+hex_split_arr[1];
				hex_split_arrk+=Integer.toString(Integer.parseInt(strk,16))+",";
			}else{
				if( j%2>0 ){
					String strk       = hex_split_arr[(j-1)]+hex_split_arr[j];
					hex_split_arrk+=Integer.toString(Integer.parseInt(strk,16));
				}
			}

		}
		return hex_split_arrk;
	}
	
	
}