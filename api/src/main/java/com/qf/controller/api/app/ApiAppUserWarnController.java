package com.qf.controller.api.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
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
import com.qf.model.admin.HixentAppUser;
import com.qf.model.fire.HixentArcAppDeviceAndEquipInfo;
import com.qf.model.fire.HixentArcAppDeviceAndEquipWarn;
import com.qf.model.fire.HixentArcFile;
import com.qf.model.fire.HixentArcSite;
import com.qf.service.admin.HixentFileService;
import com.qf.service.app.HixentAppUserService;
import com.qf.service.app.HixentAppUserWarnService;
import com.qf.util.AliyunOSSUtil;
import com.qf.util.DateUtil;
import com.qf.util.JwtUtil;
import com.qf.util.RedisUtil;
import com.qf.util.ReturnUtil;
import com.qf.util.ToolUtil;

import io.jsonwebtoken.Claims;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping("/app/fire/warn")
public class ApiAppUserWarnController {

	@Autowired
	private AliyunOSSUtil aliyunOSSUtil;

	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private JwtUtil jwtUtil;

	@Resource
	private RedisUtil redisUtil;

	@Autowired
	private HixentAppUserService hixentAppUserService;

	@Autowired
	private HixentAppUserWarnService hixentAppUserWarnService;

	@Autowired
	private HixentFileService hixentFileService;

	/**
	 * app获取历史报警列表author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app获取历史报警列表 ")
	@RequestMapping(value = "/getDeviceWarnHistoryList", method = RequestMethod.POST)
	public ModelMap getDeviceWarnHistoryList(String siteCode, Integer equipType, Integer warnIndex, Integer currentPage,
			Integer pageSize, String startTime, String endTime) {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer appUserId = userinfo.getId();
			List<HixentArcAppDeviceAndEquipWarn> deviceWarnList = new ArrayList<HixentArcAppDeviceAndEquipWarn>();
			Integer total=0;
			long startTimeToStamp = 0;
			long endTimeToStamp = 0;
			if (startTime == null) {
				startTime = "";
			}
			if (endTime == null) {
				endTime = "";
			}
			if (!startTime.equals("")) {
				String runStartTime = startTime + " 00:00:00";
				startTimeToStamp = DateUtil.getTimestamp(runStartTime);

			}
			if (!endTime.equals("")) {
				String runEndTime = endTime + " 23:59:59";
				endTimeToStamp = DateUtil.getTimestamp(runEndTime);
			}

			if (equipType == -1) {
				// 中控SD

				deviceWarnList = hixentAppUserWarnService.dealHistory(appUserId, warnIndex, siteCode, pageSize,
						currentPage, startTimeToStamp, endTimeToStamp);
				if (deviceWarnList != null) {
					for (int i = 0; i < deviceWarnList.size(); i++) {

						HixentArcAppDeviceAndEquipWarn hde = deviceWarnList.get(i);

						if (hde.getSpecificator() == null) {

						} else {
							hde.setSpecificator(ToolUtil.formatDevStr(hde.getSpecificator()));
						}

					}
				}
				total=hixentAppUserWarnService.dealHistoryCount(appUserId, warnIndex, siteCode, startTimeToStamp, endTimeToStamp);
			} else {
				deviceWarnList = hixentAppUserWarnService.dealHistoryEquip(appUserId, warnIndex, equipType, siteCode,
						pageSize, currentPage, startTimeToStamp, endTimeToStamp);
				
				if (deviceWarnList != null) {
					for (int i = 0; i < deviceWarnList.size(); i++) {
						HixentArcAppDeviceAndEquipWarn hde = deviceWarnList.get(i);

						if (ToolUtil.StringNotBlank(hde.getMessage())) {
							hde.setMessage(ToolUtil.formatDevStr(hde.getMessage()));
						}

					}
				}
				total=hixentAppUserWarnService.dealHistoryEquipCount(appUserId, warnIndex, equipType, siteCode, startTimeToStamp, endTimeToStamp);
			}
			JSONObject outJson = new JSONObject();
            outJson.put("total", total);
            outJson.put("deviceWarnList", deviceWarnList);
			return ReturnUtil.Success("获取历史报警列表成功！", outJson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * app获取设备警报列表author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app获取设备警报列表 ")
	@RequestMapping(value = "/getDeviceWarnList", method = RequestMethod.POST)
	public ModelMap getDeviceWarnList(String siteCode, Integer equipType, Integer warnIndex, Integer currentPage,
			Integer pageSize) {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer appUserId = userinfo.getId();
			List<HixentArcAppDeviceAndEquipWarn> deviceWarnList = new ArrayList<HixentArcAppDeviceAndEquipWarn>();
			Integer total=0;
			if (equipType == -1) {
				// 中控
				deviceWarnList = hixentAppUserWarnService.getDeviceWarnList(appUserId, warnIndex, siteCode, pageSize,
						currentPage);
				for (int i = 0; i < deviceWarnList.size(); i++) {
					HixentArcAppDeviceAndEquipWarn hde = deviceWarnList.get(i);
					hde.setIsDevice(0);
					hde.setSpecificator(ToolUtil.formatDevStr(hde.getSpecificator()));
				}
				total=hixentAppUserWarnService.getDeviceWarnCount(appUserId, warnIndex, siteCode);
			} else {
				deviceWarnList = hixentAppUserWarnService.getEquipWarnList(appUserId, warnIndex, equipType, siteCode,
						pageSize, currentPage);
				for (int i = 0; i < deviceWarnList.size(); i++) {
					HixentArcAppDeviceAndEquipWarn hde = deviceWarnList.get(i);
					hde.setIsDevice(1);
					hde.setMessage(ToolUtil.formatDevStr(hde.getMessage()));
				}
				total=hixentAppUserWarnService.getEquipWarnCount(appUserId, warnIndex, equipType, siteCode);
			}
             JSONObject outJson = new JSONObject();
             outJson.put("total", total);
             outJson.put("deviceWarnList", deviceWarnList);
			return ReturnUtil.Success("获取警报列表成功！", outJson);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * app处理反馈author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	// @SystemHistoryAnnotation(opration = "app处理反馈 ")
	@RequestMapping(value = "/dealFeedback", method = RequestMethod.POST)
	public ModelMap dealFeedback(@RequestParam("imgFile") String file, Integer warnId, Integer dealId) {

		try {
			//
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer appUserId = userinfo.getId();
			// 获取当前时间戳
			String currentDate = DateUtil.getCurrentTime();
			long nowtimestamp = DateUtil.getTimestamp(currentDate);
			String folderName = "";
			Integer type = 2;
			Integer siteId = 0;
			if (dealId == null) {
				// 新增
				// 新增文件夹
				Integer folderId = hixentAppUserWarnService.addFolder(folderName, type, appUserId, siteId,
						nowtimestamp);
				int sucCount = 0; // 文件数统计
				

				// 解密
				String[] split = file.split(",");
				
				for (int i = 0; i < split.length; i++) {
					if (!split[i].equals("data:image/jpeg;base64")) {
						File newFile = new File(".jpg");
						FileOutputStream os = new FileOutputStream(newFile);
						BASE64Decoder decoder = new BASE64Decoder();
						byte[] b = decoder.decodeBuffer(split[i]);
						// 处理数据
						for (int j = 0; j < b.length; ++j) {
							if (b[j] < 0) {
								b[j] += 256;
							}
						}
						os.write(b);
						os.close();
						// file[i].transferTo(newFile);
						// 上传到OSS
						JSONObject upLoad = AliyunOSSUtil.upLoad(newFile, folderName, appUserId);
						// 绝对路径
						String absoluteFileUrl = (String) upLoad.get("FILE_URL");
						// 相对路径
						String relativeFileUrl = (String) upLoad.get("fileUrl");
						
						Integer uploadFile = hixentFileService.uploadFile(folderId, "123.jpg", absoluteFileUrl,
								relativeFileUrl);
						// 删除本地文件
						newFile.delete();
						if (uploadFile > 0) {
							sucCount++;
						}
						
					}

				}
				// 添加反馈数据

				Integer addDealFeedback = hixentAppUserWarnService.addDealFeedback(appUserId, warnId,
						nowtimestamp, folderId);
				Integer updateWarn = hixentAppUserWarnService.updateWarn(warnId,nowtimestamp);
			}else {
				Integer selDealAppUserId = hixentAppUserWarnService.selDealAppUserId(warnId);
				if(selDealAppUserId!=userinfo.getId()) {
					return ReturnUtil.Error("该报警是别人处理，你无法编辑！");
				}
				
				Integer selFoldId = hixentAppUserWarnService.selFoldId(warnId);
				
	    		int sucCount = 0; // 文件数统计
	    		JSONObject photoList = hixentFileService.getPhotoList(selFoldId, 0, 0);
	    		
	    		List<HixentArcFile> fileList=(List<HixentArcFile>) photoList.get("photoList");
	    		
	    		if(fileList!=null) {
	    			for (int i = 0; i < fileList.size(); i++) {
	    				String relativeFileUrl = fileList.get(i).getRelativeFileUrl();
	    				// 删除图片
	    				aliyunOSSUtil.delFile(relativeFileUrl);
	    			}
	    			
	    			// 删除数据库文件数据
	    			hixentFileService.delFileByFoldId(selFoldId);
	    		}
	    		// 解密
				String[] split = file.split(",");
				
				for (int i = 0; i < split.length; i++) {
					if (!split[i].equals("data:image/jpeg;base64")) {
						File newFile = new File(".jpg");
						FileOutputStream os = new FileOutputStream(newFile);
						BASE64Decoder decoder = new BASE64Decoder();
						byte[] b = decoder.decodeBuffer(split[i]);
						// 处理数据
						for (int j = 0; j < b.length; ++j) {
							if (b[j] < 0) {
								b[j] += 256;
							}
						}
						os.write(b);
						os.close();
						// file[i].transferTo(newFile);
						// 上传到OSS
						JSONObject upLoad = AliyunOSSUtil.upLoad(newFile, folderName, appUserId);
						// 绝对路径
						String absoluteFileUrl = (String) upLoad.get("FILE_URL");
						// 相对路径
						String relativeFileUrl = (String) upLoad.get("fileUrl");
						
						Integer uploadFile = hixentFileService.uploadFile(selFoldId, "123.jpg", absoluteFileUrl,
								relativeFileUrl);
						// 删除本地文件
						newFile.delete();
						if (uploadFile > 0) {
							sucCount++;
						}
						
					}
			}
				hixentAppUserWarnService.updateDealFeedback(warnId,nowtimestamp);
				 hixentAppUserWarnService.updateWarn(warnId,nowtimestamp);
		}


			return ReturnUtil.Success("处理反馈成功！");

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * app报警数author RuanYu
	 */
	// @Transactional
	// @ApiLimitConfig(count=1,time=1000)
	@SystemHistoryAnnotation(opration = "app报警数 ")
	@RequestMapping(value = "/getWarnCount", method = RequestMethod.POST)
	public ModelMap getWarnCount() {
		try {

			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			String auth = request.getHeader(jwtConfig.getJwtHeader());
			auth = auth.substring(7, auth.length());
			Claims claims = jwtUtil.parseJWT(auth, jwtConfig.getSecret());
			String userId = claims.getId();
			String[] userArr = userId.split("_");
			if (!userArr[0].equals("app")) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			HixentAppUser userinfo = hixentAppUserService.findByAppUserId(userArr[1]);
			if (userinfo == null) {
				return ReturnUtil.Error("已退出，请重新登录！");
			}
			Integer appUserId = userinfo.getId();

			JSONObject warnCount = hixentAppUserWarnService.warnCount(appUserId);

			return ReturnUtil.Success("app报警数！", warnCount);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}