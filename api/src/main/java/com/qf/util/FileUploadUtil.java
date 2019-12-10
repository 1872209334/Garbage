package com.qf.util;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.qf.service.admin.CommonService;



public class FileUploadUtil {
	
	 /**
     * 保存文件，直接以multipartFile形式
     * @param  multipartFile
     * @param  path 文件保存绝对路径
     * @return 返回文件名
     * @throws IOException
     */ 
	 public static List<String> saveImg(MultipartFile[] files,String path,Integer adminId) throws IOException { 
		
		 List<String> uploadMsg = new ArrayList<String>();
		 String failName="";
		 int sucCount = 0; //文件数统计
			for(int i=0;i<files.length;i++) {
				if (!files[i].isEmpty()) {
					try {
						
						File file = new File(path); 
						if (!file.exists()) { 
							file.mkdirs(); 
						} 
						
						FileInputStream fileInputStream = (FileInputStream) files[i].getInputStream(); 
						String fileName = UuidUtil.getUUID() + ".png"; 
						//文件绝对路径
						String paths =path + File.separator + fileName;
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(paths)); 
						byte[] bs = new byte[1024]; 
						int len; 
						while ((len = fileInputStream.read(bs)) != -1) { 
							bos.write(bs, 0, len); 
						} 
						bos.flush(); 
						bos.close(); 
						sucCount++;
						//文件原名
						String upName = files[i].getName();
					   //插入数据库
						
						
					} catch (Exception e) {
						failName = failName+files[i].getOriginalFilename();
						
						continue;
					}
				}
			}
			uploadMsg.add(sucCount+"");
			uploadMsg.add(failName);
			return uploadMsg; 
	}


    
}