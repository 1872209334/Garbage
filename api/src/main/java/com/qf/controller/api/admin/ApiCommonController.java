package com.qf.controller.api.admin;



import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import org.springframework.ui.ModelMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.qf.common.JwtConfig;
import com.qf.common.systemLog.SystemHistoryAnnotation;
import com.qf.service.admin.CommonService;
import com.qf.service.admin.HixentFileService;
import com.qf.service.admin.HixentUserService;
import com.qf.service.email.EmailService;
import com.qf.service.websocket.WebSocketService;
import com.qf.model.admin.HixentAppUser;
import com.qf.model.admin.HixentUser;
import com.qf.model.admin.valid.ValidHixentSendMailTest;
import com.qf.model.fire.HixentArcFile;
import com.qf.util.AliyunOSSUtil;
import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.UuidUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.qf.util.FileUploadUtil;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;




@RestController
@RequestMapping("/api/user/common")
public class ApiCommonController {
    
	
	
	@Value("${image.location}")
    private String imageLocation;
	
	 @Autowired
	private AliyunOSSUtil aliyunOSSUtil;
	
	 @Autowired
	private HixentFileService hixentFileService;
	 
    @Autowired
	private HixentUserService hixentUserService;
	
	@Autowired
	private EmailService emailService;
	
    @Autowired
    private WebSocketService webSocketService;
    
    @Resource
    private RedisUtil redisUtil;
    
    @Resource
    private JwtConfig jwtConfig;
    
    @Resource
    private JwtUtil jwtUtil;
    
    @Autowired
	private CommonService  commonService;
    
    /**
     * 邮件发送测试
     * author Vareck
     */ 
    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    @SystemHistoryAnnotation(opration = "邮件发送测试")
    public ModelMap sendMail(@Valid ValidHixentSendMailTest vhsmt) { 
    	try{
    		String content  = vhsmt.getContent();
    		Integer type    = vhsmt.getType();
    		String subject  = vhsmt.getSubject();
    		String emailStr = vhsmt.getEmailStr();
    		String filePath = vhsmt.getFilePath();
    		String rscPath  = vhsmt.getRscPath();
    		String rscId    = vhsmt.getRscId();
            switch(type){
	            case 1:                                   		//发送文本邮件
	            	emailService.sendSimpleEmail(emailStr,subject,content); 
	            	break;
	            case 2:                                   		//发送html格式邮件
	            	emailService.sendHtmlEmail(emailStr,subject,content); 
	            	break;
	            case 3:                                   		//发送带附件的邮件
	            	emailService.sendAttachmentsEmail(emailStr,subject,content,filePath); 
	            	break;
	            case 4:                                   		//发送带静态资源的邮件
	            	emailService.sendInlineResourceEmail(emailStr,subject,content,rscPath,rscId);  
	            	break;
	            default:                                  		//发送文本邮件
	            	emailService.sendSimpleEmail(emailStr,subject,content); 
	            	break;
            }
        	return ReturnUtil.Error("发送成功");
    	}catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
        }
    }
    
    
    
    /**
     * 图片上传
     * author Vareck
     */
    @RequestMapping(value = "/img/upload", method = RequestMethod.POST)
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
                      //file[i].transferTo(newFile);
                      // 上传到OSS
                       JSONObject upLoad = AliyunOSSUtil.upLoad(newFile,folderName,userinfo.getId());
                      //绝对路径
                     String absoluteFileUrl = (String) upLoad.get("FILE_URL");
                     //相对路径
                     String relativeFileUrl =  (String) upLoad.get("fileUrl");
                     Integer uploadFile = hixentFileService.uploadFile(folderId, originalFilename, absoluteFileUrl, relativeFileUrl);
                     //删除本地文件
                     newFile.delete();
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
     * websocket发送广播消息
     * author Vareck
     */  
    @RequestMapping(value = "/sendWebsocket", method = RequestMethod.POST)
    @SystemHistoryAnnotation(opration = "websocket测试")
	public void sendWebsocket(){
    	try{
            //获取request
    		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    	    HttpServletRequest request = requestAttributes.getRequest();
        	String message = request.getParameter("message");
        	

            JSONObject obj = new JSONObject();
            {
            	obj.put("msg",message);
            	obj.put("deviceId",DateUtil.getCurrentTime());
            }
            //java对象变成json对象
            String Jsonmessage=JSONObject.toJSONString(obj);
                    	
    		webSocketService.sendMessageToUser("888", new TextMessage("websocket：发给指定用户的一条消息。"+message+","+DateUtil.getCurrentTime()));
    		webSocketService.sendMessageToAllUsers(new TextMessage(Jsonmessage));
    		
    	}catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
        }
	}
   
    
    /**
     * 管理员登出后台
     * author Vareck
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelMap logout(){
    	try{
            //获取request
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();  
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            //jwt登出
        	String auth    = request.getHeader(jwtConfig.getJwtHeader());
        	auth           = auth.substring(7, auth.length());
        	Claims claims  = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
        	redisUtil.del(claims.getId());
            //获取当前的Subject
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.logout();
            return ReturnUtil.Success("登出成功");
    	} catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
        }
    }
   
    /**
	 * 编辑文件夹 author RuanYu
	 */

	 //@SystemHistoryAnnotation(opration = " 编辑文件夹")
	@RequestMapping(value = "/img/editFolder", method = RequestMethod.POST)
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
		
		// System.out.println("checkFolderName"+checkFolderName.getFolderId());
		 if (checkFolderName!=null && checkFolderName.getFolderId()!=folderId) {
			
			 return ReturnUtil.Error("该文件夹已存在，请重新填写！");
		}
		//获取文件夹里面的图片
		
		JSONObject photoList = hixentFileService.getPhotoList(folderId, 0, 0);
		
		List<HixentArcFile> fileList=(List<HixentArcFile>) photoList.get("photoList");
		
//		if(fileList!=null) {
//			for (int i = 0; i < fileList.size(); i++) {
//				String relativeFileUrl = fileList.get(i).getRelativeFileUrl();
//				// 删除图片
//				aliyunOSSUtil.delFile(relativeFileUrl);
//			}
//			
//			// 删除数据库文件数据
//			hixentFileService.delFileByFoldId(folderId);
//		}
		
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
		                      //file[i].transferTo(newFile);
		                      // 上传到OSS
		                       JSONObject upLoad = AliyunOSSUtil.upLoad(newFile,folderName,userinfo.getId());
		                      //绝对路径
		                     String absoluteFileUrl = (String) upLoad.get("FILE_URL");
		                     //相对路径
		                     String relativeFileUrl =  (String) upLoad.get("fileUrl");
		                     Integer uploadFile = hixentFileService.uploadFile(folderId, originalFilename, absoluteFileUrl, relativeFileUrl);
		                     newFile.delete();
		                      if(uploadFile>0) {
		                    	  sucCount++;
		                      }
		                     
		                  }

		              }
		          }
		          
		            return ReturnUtil.Success("编辑成功",sucCount);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		       
		        return ReturnUtil.Error("编辑失败！");
		        }
}