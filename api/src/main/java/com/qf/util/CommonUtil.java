package com.qf.util;



import java.util.HashSet;
import java.util.Set;



public class CommonUtil {
    
	

	/**
	 * 将协议中固定的16进制字符串转换成10进制字符串
	 * @param hexString 16进制字符串
	 * @return 10进制字符串
	 */
	public static String parsePostData(String hexString){
		String[] hexArr = hexString.split(",");
		for(int i=0;i<hexArr.length;i++){
			hexArr[i] = String.valueOf(Integer.parseInt(hexArr[i],16));
		}
		return String.join(",",hexArr);
	}

	/**
	 * 数组去重
	 * @param originalArray 原数组
	 * @return 无重复元素数组
	 */
	public static Object[] deleteSameItemInArray(Object[] originalArray){
		Set<Object> set = new HashSet<>();
        for(int i=0;i<originalArray.length;i++){
        	if(originalArray[i] instanceof String){
        		if(!"".equals(originalArray[i].toString().trim())){
            		set.add(originalArray[i]);
            	}
        	}else{
        		set.add(originalArray[i]);
        	}
        }
        return set.toArray(new String[set.size()]);
	}
	
	/**
	 * 将16进制字符串转换成16进制byte数组
	 * @param hexString 16进制字符串
	 * @return byte数组
	 */
	public static byte[] hexStringToByte(String hexString){
		int len = hexString.length()/2;
        char[] hexChars = hexString.toCharArray();  
        byte[] buf = new byte[len];  
        for (int i = 0; i < len; i++) {  
        	int pos = i * 2;  
        	buf[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }
		return buf;
	}
	
	/**
	 * 将char转换成16进制byte数组
	 * @param hexString 16进制字符串
	 * @return byte数据
	 */
	private static byte charToByte(char c) { 
		return (byte) "0123456789ABCDEF".indexOf(c);
	}


	
}