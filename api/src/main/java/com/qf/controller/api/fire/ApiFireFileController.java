package com.qf.controller.api.fire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.model.admin.HixentUser;
import com.qf.model.fire.HixentArcFile;
import com.qf.model.fire.valid.ValidHixentFile;
import com.qf.service.admin.HixentFileService;
import com.qf.service.admin.HixentUserService;
import com.qf.util.AliyunOSSUtil;
import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/file")
public class ApiFireFileController {

	@Autowired
	private AliyunOSSUtil aliyunOSSUtil;

	@Autowired
	private HixentFileService hixentFileService;

	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private HixentUserService hixentUserService;
    
    /**
     * 图片上传
     * author Vareck
     */
    @RequestMapping(value = "/img/upload", method = RequestMethod.PUT)
    public ModelMap uploadImg(HttpServletRequest request, 
    		@RequestParam("imgFile") MultipartFile[] file,Integer siteId,Integer type,String folderName) {  

        String auth      = request.getHeader(jwtConfig.getJwtHeader());
    	auth             = auth.substring(7, auth.length());
    	Claims claims    = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
        String userId    = claims.getId();
        String[] userArr = userId.split("_");
        if( !userArr[0].equals("admin") ){
        	return ReturnUtil.Error("已退出，请重新登录");
        	
        }
        HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
		if(userinfo == null){
			return ReturnUtil.Error("已退出，请重新登录");
        }
		List<String> uploadMsg = new ArrayList<String>();
		//查询文件夹名是否重复
		 HixentArcFile checkFolderName = hixentFileService.checkFolderName(folderName,userinfo.getId());
		if(checkFolderName!=null) {
			return ReturnUtil.Error("该名称已使用，请重新填写！");
		}
		//文件夹数据插入
		try {
			Integer folderId = hixentFileService.folderData(folderName,type,userinfo.getId(),siteId);
    		
    		int sucCount = 0; // 文件数统计
          for(int i=0;i<file.length;i++) {
        	  String filename = file[i].getOriginalFilename();  
        	  String originalFilename = file[i].getOriginalFilename();
        	  if (file != null) {
                  if (!"".equals(filename.trim())) {
                      File newFile = new File(filename);
                      FileOutputStream os = new FileOutputStream(newFile);
                      os.write(file[i].getBytes());
                      os.close();
                      file[i].transferTo(newFile);
                      // 上传到OSS
                       JSONObject upLoad = AliyunOSSUtil.upLoad(newFile,folderName,userinfo.getId());
                      //绝对路径
                     String absoluteFileUrl = (String) upLoad.get("FILE_URL");
                     //相对路径
                     String relativeFileUrl =  (String) upLoad.get("fileUrl");
                     Integer uploadFile = hixentFileService.uploadFile(folderId, originalFilename, absoluteFileUrl, relativeFileUrl);

                      if(uploadFile>0) {
                    	  sucCount++;
                      }
                     
                  }

              }
          }
            
           
            uploadMsg.add(sucCount+"");
            return ReturnUtil.Success("上传成功",uploadMsg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       
        return ReturnUtil.Error("上传失败！");
    }
	/**
	 * 获取图片 author RuanYu
	 */
	@Transactional
	// @ApiLimitConfig(count=1,time=1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "获取图片")
	@RequestMapping(value = "/getPhotoList", method = RequestMethod.POST)
	public ModelMap getPhotoList(@Valid ValidHixentFile fileValid, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("参数错误！");
			} else {
				// 获取用户
				// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();
				String auth = request.getHeader(jwtConfig.getJwtHeader());
				auth = auth.substring(7, auth.length());
				Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
				String userId = claims.getId();
				String[] userArr = userId.split("_");
				if (!userArr[0].equals("admin")) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
				if (userinfo == null) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				Integer folderId = fileValid.getFolderId();
				
				if(folderId<=0) {
					return ReturnUtil.Error("请选择文件夹！");
				}
				Integer pageSize = fileValid.getPageSize();
				Integer currentPage = fileValid.getCurrentPage();
				JSONObject photoList = hixentFileService.getPhotoList(folderId,currentPage,pageSize);
				return ReturnUtil.Success("获取图片数据成功！", photoList);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 编辑文件夹 author RuanYu
	 */
	@RequestMapping(value = "/img/editFolder", method = RequestMethod.PUT)
	 public ModelMap editFolder(HttpServletRequest request, @RequestParam("imgFile") MultipartFile[] file, Integer siteId,
			String folderName, Integer folderId) {

		String auth = request.getHeader(jwtConfig.getJwtHeader());
		auth = auth.substring(7, auth.length());
		Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
		String userId = claims.getId();
		String[] userArr = userId.split("_");
		if (!userArr[0].equals("admin")) {
			return ReturnUtil.Error("已退出，请重新登录");

		}
		HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
		if (userinfo == null) {
			return ReturnUtil.Error("已退出，请重新登录");
		}

		// 管理员角色类型
		Integer roleType = userinfo.getRoleType();

		HixentArcFile selFolderById = hixentFileService.selFolderById(folderId);
		if (roleType != 0) {
			// 查询该管控人员是否是属于该管理员
			if (selFolderById == null || selFolderById.getUploaderId() != userinfo.getId()) {
				return ReturnUtil.Error("该文件夹不存在！");
			}

		}
		// 查询文件夹名是否重复
		 HixentArcFile checkFolderName = hixentFileService.checkFolderName(folderName, userinfo.getId());
		
		 if (checkFolderName!=null && checkFolderName.getFolderId()!=folderId) {
			
			 return ReturnUtil.Error("该请重新填写！");
		}
		//获取文件夹里面的图片
		
		JSONObject photoList = hixentFileService.getPhotoList(folderId, 0, 0);
		
		List<HixentArcFile> fileList=(List<HixentArcFile>) photoList.get("photoList");
		
		if(fileList!=null) {
			for (int i = 0; i < fileList.size(); i++) {
				String relativeFileUrl = fileList.get(i).getRelativeFileUrl();
				// 删除图片
				aliyunOSSUtil.delFile(relativeFileUrl);
			}
			// 删除数据库文件数据
			hixentFileService.delFileByFoldId(folderId);
		}
		//文件夹数据插入
				try {
					Integer updateFolder = hixentFileService.updateFolder(folderId, siteId, folderName);
		    		int sucCount = 0; // 文件数统计
		    		for(int i=0;i<file.length;i++) {
		        	  String filename = file[i].getOriginalFilename();  
		        	  String originalFilename = file[i].getOriginalFilename();
		        	  if (file != null) {
		                  if (!"".equals(filename.trim())) {
		                      File newFile = new File(filename);
		                      FileOutputStream os = new FileOutputStream(newFile);
		                      os.write(file[i].getBytes());
		                      os.close();
		                      file[i].transferTo(newFile);
		                      // 上传到OSS
		                       JSONObject upLoad = AliyunOSSUtil.upLoad(newFile,folderName,userinfo.getId());
		                      //绝对路径
		                     String absoluteFileUrl = (String) upLoad.get("FILE_URL");
		                     //相对路径
		                     String relativeFileUrl =  (String) upLoad.get("fileUrl");
		                     Integer uploadFile = hixentFileService.uploadFile(folderId, originalFilename, absoluteFileUrl, relativeFileUrl);

		                      if(uploadFile>0) {
		                    	  sucCount++;
		                      }
		                     
		                  }

		              }
		          }
		          if(sucCount==file.length) {
		        	  return ReturnUtil.Success("编辑成功"); 
		          }
		           
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		       
		        return ReturnUtil.Error("编辑失败！");


	}

	/**
	 * 删除图片 author Ruanyu
	 */
	@Transactional
	@SystemHistoryAnnotation(opration = "删除图片")
	@RequestMapping(value = "/img/delPhoto", method = RequestMethod.POST)
	public ModelMap delPhoto(Integer imgId) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		String auth = request.getHeader(jwtConfig.getJwtHeader());
		auth = auth.substring(7, auth.length());
		Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
		String userId = claims.getId();
		String[] userArr = userId.split("_");
		if (!userArr[0].equals("admin")) {
			return ReturnUtil.Error("已退出，请重新登录");

		}
		HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
		if (userinfo == null) {
			return ReturnUtil.Error("已退出，请重新登录");
		}
      
		// 如果不是超级管理员
		Integer roleType = userinfo.getRoleType();

		HixentArcFile selPhoneInfoById = hixentFileService.selPhotoById(imgId);

		
		// 删除图片
		aliyunOSSUtil.delFile(selPhoneInfoById.getRelativeFileUrl());
		Integer delPhonetById = hixentFileService.delPhotoById(imgId);
		if (delPhonetById > 0) {
			return ReturnUtil.Success("删除成功");
		} else {
			return ReturnUtil.Error("删除失败！");
		}

	}
	/**
	 * 获取文件夹列表 author RuanYu
	 */
	@Transactional
	// @ApiLimitConfig(count=1,time=1000)
	// @RequiresPermissions(value = {"parc_saveDevice"})
	@SystemHistoryAnnotation(opration = "获取文件夹列表")
	@RequestMapping(value = "/getFolderList", method = RequestMethod.POST)
	public ModelMap getFolderList(@Valid ValidHixentFile fileValid, BindingResult bindingResult) {
		try {
			if (bindingResult.hasErrors()) {
				return ReturnUtil.Error("参数错误！");
			} else {
				// 获取用户
				// HixentUser userinfo = (HixentUser) SecurityUtils.getSubject().getPrincipal();
				ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest request = requestAttributes.getRequest();
				String auth = request.getHeader(jwtConfig.getJwtHeader());
				auth = auth.substring(7, auth.length());
				Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
				String userId = claims.getId();
				String[] userArr = userId.split("_");
				if (!userArr[0].equals("admin")) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
				if (userinfo == null) {
					return ReturnUtil.Error("已退出，请重新登录！");
				}
				Integer adminId = userinfo.getId();
				Integer roleType = userinfo.getRoleType();

				String areaId = (String) redisUtil.get("areaId_" + userinfo.getUid());
				String provinceId = (String) redisUtil.get("provinceId_" + userinfo.getUid());
				int areaIdInt = Integer.parseInt(areaId);
				int provinceIdInt = Integer.parseInt(provinceId);
				// 获取项目数据
				String bid = userinfo.getBid();
				String[] siteCordArr = bid.split(",");

				Integer type = fileValid.getType();
				Integer siteId = fileValid.getSiteId();
				String startTime = fileValid.getStartTime();
				String endTime = fileValid.getEndTime();
				long startTimeToStamp = 0;
				long endTimeToStamp = 0;
				if (!startTime.equals("")) {
					String runStartTime = startTime + " 00:00:00";
					// 时间转换
					startTimeToStamp = DateUtil.getTimestamp(runStartTime);
				}
				if (!endTime.equals("")) {
					String runEndTime = endTime + " 23:59:59";
					// 时间转换
					endTimeToStamp = DateUtil.getTimestamp(runEndTime);
				}
				Integer pageSize = fileValid.getPageSize();
				Integer currentPage = fileValid.getCurrentPage();
				JSONObject photoList = hixentFileService.getFolderList(adminId, roleType, type, siteId, startTimeToStamp,
						endTimeToStamp, currentPage, pageSize, provinceIdInt, areaIdInt, siteCordArr);
				return ReturnUtil.Success("获取文件夹列表成功！", photoList);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	/**
	 * 删除文件夹 author Ruanyu
	 */
	@Transactional
	@SystemHistoryAnnotation(opration = "删除文件夹")
	@RequestMapping(value = "/delFolder", method = RequestMethod.POST)
	public ModelMap delFolder(Integer folderId) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		String auth = request.getHeader(jwtConfig.getJwtHeader());
		auth = auth.substring(7, auth.length());
		Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
		String userId = claims.getId();
		String[] userArr = userId.split("_");
		if (!userArr[0].equals("admin")) {
			return ReturnUtil.Error("已退出，请重新登录");

		}
		HixentUser userinfo = hixentUserService.findByUserId(userArr[1]);
		if (userinfo == null) {
			return ReturnUtil.Error("已退出，请重新登录");
		}

		//获取文件夹里面的图片
		JSONObject photoList = hixentFileService.getPhotoList(folderId, 0, 0);
		List<HixentArcFile> fileList=(List<HixentArcFile>) photoList.get("photoList");
		
		if(fileList!=null) {
			for (int i = 0; i < fileList.size(); i++) {
				String relativeFileUrl = fileList.get(i).getRelativeFileUrl();
				// 删除图片
				aliyunOSSUtil.delFile(relativeFileUrl);
			}
			// 删除数据库文件数据
			hixentFileService.delFileByFoldId(folderId);
		}
		// 删除文件夹数据
		Integer delFolderByFoldId = hixentFileService.delFolderByFoldId(folderId);
		
		if (delFolderByFoldId > 0) {
			return ReturnUtil.Success("删除成功");
		} else {
			return ReturnUtil.Error("删除失败！");
		}

	}
}