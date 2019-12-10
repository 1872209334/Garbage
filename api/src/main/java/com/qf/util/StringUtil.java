package com.qf.util;



import java.util.Random;
import java.lang.StringBuffer;



public class StringUtil {
	
	
	
	public static boolean isHave( String a,String[] b ){
        boolean flag = false;  
        for ( int i = 0; i < b.length; i++ ) {  
        	if ( a.equals(b[i]) ) {  
        		flag = true;  
                break;  
            } else {  
                flag = false;  
            }  
        }  
        return flag; 
	}

	
	
    public static boolean strIsNullOrEmpty(String s) {
        return (null == s || s.trim().length() < 1);
    }
    
    
    
    //将字符串按照一定长度分割
    public  static String[] stringSpilt(String s,int len){
    	int spiltNum;             //len->想要分割获得的子字符串的长度
    	int i;
    	int cache = len;          //存放需要切割的数组长度
    	spiltNum  = (s.length())/len;
    	String[] subs;			  //创建可分割数量的数组
    	if((s.length()%len)>0){
    		subs = new String[spiltNum+1];
    	}else{
    		subs = new String[spiltNum];
    	}
    	//可用一个全局变量保存分割的数组的长度
    	int start = 0;
    	if(spiltNum>0){
    		for(i=0;i<subs.length;i++){
    			if(i==0){
    				subs[0] = s.substring(start, len);
    				start = len;
    			}else if(i>0 && i<subs.length-1){
    				len = len + cache ;
    				subs[i] = s.substring(start,len);
    				start = len ;
    			}else{
    				subs[i] = s.substring(len,s.length());
    			}
    		}
    	}
    	return subs;
    }

    
    
	//获取末端时间
    public static String getDeviceTimeStr(Integer[] device_time){
		//拼接时间
        String[] device_time_array = new String[6];
        for( int i=0; i<device_time.length ; i++ ){
        	if(i==0){
        		Integer n            = 2000+device_time[i];    //由于年份只获取到2位所以要加2000
        		device_time_array[i] = String.valueOf(n);
        	}else{
    			if( device_time[i] < 10 ){
    				device_time_array[i] = "0"+device_time[i];
    			}else{
    				device_time_array[i] = String.valueOf(device_time[i]);
    			}
        	}
        }
        String device_time_str = device_time_array[0];
		device_time_str += "-"+device_time_array[1];
		device_time_str += "-"+device_time_array[2];
		device_time_str += " "+device_time[3];
		device_time_str += ":"+device_time[4];
		device_time_str += ":"+device_time[5];
		return device_time_str;
	}

    
    
    public static String getRandomString(int length){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random   = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
        	int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    
    
    
    /**
     * 去除字符串所有空格
     * @param str
     * @return
     */ 
	public static String remove(String str) { 
		str = str.replace(" ", ""); 
		return str; 
	} 
	
	
	
    /**
     * 给字符串每2位加一个空格
     * @param str
     * @return
     */ 
	public static String add(String str) { 
        String regex = "(.{2})";
        str = str.replaceAll (regex, "$1 ");
		return str; 
	} 

	
	
}