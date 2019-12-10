package com.qf.mapper.fire;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.fire.HixentArcFile;
import com.qf.util.CustomerMapper;



/**
 * author Vareck
 */
@Service
public interface HixentUploadFileMapper extends CustomerMapper<HixentArcFile> {
	//上传图片
	Integer uploadPhoto(@Param("fileName")String fileName,
			@Param("filePath")String filePath,@Param("relativeFileUrl")String relativeFileUrl,
			@Param("folderId")Integer folderId);

   //查询图片 分页
	List<HixentArcFile> getPhotoList(@Param("folderId")Integer folderId,@Param("limits")String limits);
	
	 //查询图片 总数	
    Integer getPhotoListCount(@Param("folderId")Integer folderId);

    
    //通过文件夹Id查询文件夹
    HixentArcFile selFolderById(@Param("folderId")Integer folderId);
    
    //通过图片Id查询图片
    HixentArcFile selPhotoById(@Param("photoId")Integer photoId);
    
    //删除图片
    Integer delPhotoById(@Param("photoId")Integer photoId);
    
    //删除文件夹
    Integer delFolderByFoldId(@Param("folderId")Integer folderId);
    
    //编辑图片 
    Integer editPhotoById(@Param("photoId")Integer photoId,@Param("fileName")String fileName,
			@Param("filePath")String filePath,@Param("time")Long time,
			@Param("siteId")Integer siteId,@Param("remark")String remark,@Param("relativeFileUrl")String relativeFileUrl);

    //查询文件夹名是否重复
    HixentArcFile checkFolderName(@Param("folderName")String folderName,@Param("adminId")Integer adminId);
   
    //文件夹表插入数据
    Integer folderData(HixentArcFile hixentArcFile);
    
    //通过文件夹删除文件
    Integer delFileByFoldId(@Param("folderId")Integer folderId);
    
    //更新文件夹数据
    Integer updateFolder(@Param("folderId")Integer folderId,@Param("siteId")Integer siteId,
    		@Param("folderName")String folderName,@Param("time")Long time);

   //查询文件夹 分页
  	List<HixentArcFile> getFolderList(@Param("adminId")Integer adminId,@Param("roleType")Integer roleType,
  			@Param("type")Integer type ,@Param("siteId")Integer siteId,@Param("startTime")long startTime,@Param("endTime")long endTime,
  			@Param("limits")String limits,@Param("provinceId")Integer provinceId,
  			@Param("areaId")Integer areaId,@Param("siteCordArr")String[] siteCordArr);
  	 //查询文件夹 总数	
      Integer getFolderListCount(@Param("adminId")Integer adminId,@Param("roleType")Integer roleType,
  			@Param("type")Integer type ,@Param("siteId")Integer siteId,@Param("startTime")long startTime,
  			@Param("endTime")long endTime,@Param("provinceId")Integer provinceId,
  			@Param("areaId")Integer areaId,@Param("siteCordArr")String[] siteCordArr);
}