package com.qf.service.admin;



import java.text.ParseException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.HixentUploadFileMapper;
import com.qf.model.fire.HixentArcFile;
import com.qf.util.DateUtil;



/**
 * web管理后台管理员相关服务
 * author   Vareck
 */
@Service
public class HixentFileService {

	
	
    @Autowired
    private HixentUploadFileMapper hixentUploadFileMapper;
  
    public JSONObject getPhotoList(Integer folderId,Integer currentPage,Integer pageSize) {
    	String limits =null;
    	if(currentPage==0&& pageSize==0) {
    		limits="";
    	}else {
    		limits = " "+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+" ";	
    	}
    	
    	List<HixentArcFile> photoList = hixentUploadFileMapper.getPhotoList(folderId,limits);
    	Integer photoListCount = hixentUploadFileMapper.getPhotoListCount(folderId);
    	
    	JSONObject getPhotoList= new JSONObject();
    	getPhotoList.put("total", photoListCount);
    	getPhotoList.put("photoList", photoList);
    	
    	
    	return getPhotoList;
    }
    //查询文件夹
    public HixentArcFile selFolderById(Integer folderId) {
    	HixentArcFile selFolderById = hixentUploadFileMapper.selFolderById(folderId);
       return selFolderById;
    }
    //查询图片
    public HixentArcFile selPhotoById(Integer photoId) {
    	return hixentUploadFileMapper.selPhotoById(photoId);
    }
    
    
    //删除图片
    public Integer delPhotoById(Integer photoId) {
    	return hixentUploadFileMapper.delPhotoById(photoId);
    }
    //编辑图片
   
    public Integer editPhoneById(Integer photoId,String fileName,String filePath,Integer siteId,String remark,String relativeFileUrl) throws ParseException {
    	//获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long nowtimestamp = DateUtil.getTimestamp(currentDate);
    	Integer editPhotoById = hixentUploadFileMapper.editPhotoById(photoId, fileName, filePath, nowtimestamp, siteId, remark, relativeFileUrl);
    	return null;
    }
    //通过名字查询文件夹
    public HixentArcFile checkFolderName(String folderName,Integer adminId) {
    	
    	HixentArcFile checkFolderName = hixentUploadFileMapper.checkFolderName(folderName,adminId);
     
      return checkFolderName;
    }
    //文件夹数据插入
    public Integer folderData(String folderName,Integer type,Integer adminId,Integer siteId) throws ParseException {
    	//获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long nowtimestamp = DateUtil.getTimestamp(currentDate);
    	HixentArcFile haf = new HixentArcFile();
    	haf.setFolderName(folderName);
    	haf.setType(type);
    	haf.setUploaderId(adminId);
    	haf.setSiteId(siteId);
    	haf.setUploadTime(nowtimestamp);
    	  hixentUploadFileMapper.folderData(haf);
    	 Integer folderId = haf.getFolderId();
    	
    	 return folderId;
    }
   //上传文件、图片
   
    public Integer uploadFile(Integer folderId,String fileName,String filePath,String relativeFileUrl) throws Exception{
    	
    	Integer uploadPhoto = hixentUploadFileMapper.uploadPhoto(fileName, filePath, relativeFileUrl,folderId);
    	return uploadPhoto;
    }
    //通过文件夹删除文件
    public Integer delFileByFoldId(Integer ids) {
    	
    	return hixentUploadFileMapper.delFileByFoldId(ids);
    }
    
    //更新文件夹信息
    
    public Integer updateFolder(Integer folderId,Integer siteId,String folderName) throws ParseException {
    	//获取时间并转成时间戳
    	String currentDate = DateUtil.getCurrentTime();
    	long nowtimestamp = DateUtil.getTimestamp(currentDate);
    	return hixentUploadFileMapper.updateFolder(folderId,siteId,folderName,nowtimestamp);
    }
    //删除文件夹
    public Integer delFolderByFoldId(Integer folderId) {
    	return hixentUploadFileMapper.delFolderByFoldId(folderId);
    	
    }
    //获取文件夹列表
    public JSONObject getFolderList(Integer adminId,Integer roleType,Integer type,Integer siteId 
    		,long startTime,long endTime,Integer currentPage,Integer pageSize,Integer provinceId
    		,Integer areaId,String[] siteCordArr) {
    	String limits = " "+Integer.toString((currentPage-1)*pageSize)+","+Integer.toString(pageSize)+" ";
    	List<HixentArcFile> photoList = hixentUploadFileMapper.getFolderList(adminId, roleType, type, siteId, startTime, endTime, limits, provinceId, areaId, siteCordArr);
    	Integer photoListCount = hixentUploadFileMapper.getFolderListCount(adminId, roleType, type, siteId, startTime, endTime, provinceId, areaId, siteCordArr);
    	
    	JSONObject getPhotoList= new JSONObject();
    	getPhotoList.put("total", photoListCount);
    	getPhotoList.put("photoList", photoList);
    	
    	
    	return getPhotoList;
    }
}