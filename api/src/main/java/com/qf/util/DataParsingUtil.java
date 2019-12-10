package com.qf.util;



import java.util.HashMap;




/**
 * Mqtt数据解析相关工具类（crc16）
 * author Vareck
 * date 2018/12/29 
 */
public class DataParsingUtil {
	

    
    private static final int[] table = { 
    	0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241, 0xC601, 0x06C0, 0x0780, 0xC741,
        0x0500, 0xC5C1, 0xC481, 0x0440, 0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40, 0x0A00, 0xCAC1, 0xCB81, 0x0B40,
        0xC901, 0x09C0, 0x0880, 0xC841, 0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40, 0x1E00, 0xDEC1, 0xDF81, 0x1F40,
        0xDD01, 0x1DC0, 0x1C80, 0xDC41, 0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641, 0xD201, 0x12C0, 0x1380, 0xD341,
        0x1100, 0xD1C1, 0xD081, 0x1040, 0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240, 0x3600, 0xF6C1, 0xF781, 0x3740,
        0xF501, 0x35C0, 0x3480, 0xF441, 0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41, 0xFA01, 0x3AC0, 0x3B80, 0xFB41,
        0x3900, 0xF9C1, 0xF881, 0x3840, 0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41, 0xEE01, 0x2EC0, 0x2F80, 0xEF41,
        0x2D00, 0xEDC1, 0xEC81, 0x2C40, 0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640, 0x2200, 0xE2C1, 0xE381, 0x2340,
        0xE101, 0x21C0, 0x2080, 0xE041, 0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240, 0x6600, 0xA6C1, 0xA781, 0x6740,
        0xA501, 0x65C0, 0x6480, 0xA441, 0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41, 0xAA01, 0x6AC0, 0x6B80, 0xAB41,
        0x6900, 0xA9C1, 0xA881, 0x6840, 0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41, 0xBE01, 0x7EC0, 0x7F80, 0xBF41,
        0x7D00, 0xBDC1, 0xBC81, 0x7C40, 0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640, 0x7200, 0xB2C1, 0xB381, 0x7340,
        0xB101, 0x71C0, 0x7080, 0xB041, 0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241, 0x9601, 0x56C0, 0x5780, 0x9741,
        0x5500, 0x95C1, 0x9481, 0x5440, 0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40, 0x5A00, 0x9AC1, 0x9B81, 0x5B40,
        0x9901, 0x59C0, 0x5880, 0x9841, 0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40, 0x4E00, 0x8EC1, 0x8F81, 0x4F40,
        0x8D01, 0x4DC0, 0x4C80, 0x8C41, 0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641, 0x8201, 0x42C0, 0x4380, 0x8341,
        0x4100, 0x81C1, 0x8081, 0x4040, 
    };
     
        
        
    public static byte[] getCRCBytes(byte[] data, int start, int len) {
    	int crc = 0x0000;
        for (byte b : data) {
            crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
        }
        return new byte[] { (byte) (0xff & crc), (byte) ((0xff00 & crc) >> 8) };
    }
     
        
        
    public static byte[] getCRCBytes(byte[] data) {
	    return getCRCBytes(data, 0, data.length);
    }
     

        
    public static String bytesToHexString(byte[] bArr) { 
        StringBuffer sb = new StringBuffer(bArr.length);
        String sTmp;
        for (int i = 0; i < bArr.length; i++) { sTmp = Integer.toHexString(0xFF & bArr[i]);
            if (sTmp.length() < 2) sb.append(0);
            sb.append(sTmp.toUpperCase());
        } 
        return sb.toString();
    }

        
        
    /**
    * 获取CRT16字符串
    * @param data     需要计算的数组
    * @return         CRC16字符串
    */
    public static String getCRT(byte[] data){
    	byte[] crc = getCRCBytes(data);
        return bytesToHexString(crc);
    }
    
    
     
    /**
     * 16进制字符串转换为byte[]
     * @param str
     * @return
     */ 
	public static byte[] toBytes(String str) { 
		if(str == null || str.trim().equals("")) { 
			return new byte[0]; 
		} 
		byte[] bytes = new byte[str.length() / 2]; 
		for(int i = 0; i < str.length() / 2; i++) { 
			String subStr = str.substring(i * 2, i * 2 + 2); 
			bytes[i] = (byte) Integer.parseInt(subStr, 16); 
		} 
		return bytes; 
	} 

    
    
    //获取协议响应
    public static HashMap<String,String> getProtocolResponse(){
		HashMap<String,String> protocol_response = new HashMap<String,String>();
        return protocol_response;
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
	
	/**
     * 16进制字符串转换为int[]
     * @param str
     * @return
     */ 
	public static int[] toInts(String str) { 
		if(str == null || str.trim().equals("")) { 
			return new int[0]; 
		} 
		int[] ints = new int[str.length() / 2]; 
		for(int i = 0; i < str.length() / 2; i++) { 
			String subStr = str.substring(i * 2, i * 2 + 2); 
			ints[i] = Integer.parseInt(subStr, 16); 
		} 
		return ints; 
	} 
	
	/**
     * crc16校验
     * @param str
     * @return
     */ 
	public static int[] getUdpCrc(byte[] data_arr, int data_len){
		char crc;
		int crc16 = 0;
        int i;
        for(i =0; i < (data_len); i++) {
	        crc16 = (char)(( crc16 >> 8) | (crc16 << 8));
	        crc16 ^= data_arr[i]& 0xFF;
	        crc16 ^= (char)(( crc16 & 0xFF) >> 4);
	        crc16 ^= (char)(( crc16 << 8) << 4);
	        crc16 ^= (char)((( crc16 & 0xFF) << 4) << 1);
        }
        int [] result=new int[2];
        result[0]= (crc16/256) ;
        result[1]= (crc16%256) ;
        return result;
	}
	
	/**
	  * 转化十六进制编码为字符串
	  */
	 public static String toStringHex(String s){
		 //去掉空格
		 String t = s.trim(); 
		 //空掉字符串头部的0
		 t = t.replaceAll("^(0+)", "");
		 //转大写
		 t = t.toUpperCase();
//		 int m = t.indexOf("00");
//		 String messageString = "";
//		 //去掉字符串尾部的0
//		 if(m > 0){
//			 messageString = t.substring(0,m);
//		 }
		
	      String b="";
	      for (int i = 0; i < t.length()-1; i=i+2) {
	    	  String substring = t.substring(i, i+2);
	    	  if(substring.equals("00")) {
	    		 break;
	    	  }
	    	  b=b+substring;
	    	 
		 }
		 if(! b.equals("")){
			 byte[] baKeyword = new byte[ b.length()/2];
			 for(int i = 0; i < baKeyword.length; i++){
				 try{
					 baKeyword[i] = (byte)(0xff & Integer.parseInt( b.substring(i*2, i*2+2),16));
				 }catch(Exception e){
					    e.printStackTrace();
				 }
			 }
			 try{
				 s = new String(baKeyword, "GBK");
			 }catch (Exception e1){
				   e1.printStackTrace();
			 }
			 return s;
		 }
		 return "";
		
	 }
	
	
	
	
    
}