package com.qf.service.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import top.snailclimb.common.constants.AliyunOSSConfigConstant;

import com.alibaba.fastjson.JSONObject;
import com.qf.mapper.fire.app.HixentArcAppSiteMapper;
import com.qf.model.fire.HixentArcAppDeviceAndEquipInfo;
import com.qf.model.fire.HixentArcSite;
import com.qf.model.fire.valid.ValidHixentEditSiteNiName;
import com.qf.util.AliyunOSSUtil;
import com.qf.util.StringUtil;

/**
 * 手机app用户相关服务 author Vareck
 */
@Service
public class HixentAppUserSiteService {

	@Autowired
	private HixentArcAppSiteMapper hixentArcAppSiteMapper;

	// 项目详情
	public HixentArcSite getSiteInfo(String siteCode) {
		HixentArcSite siteInfo = hixentArcAppSiteMapper.getSiteInfo(siteCode);
		return siteInfo;

	}
	
	// 项目详情
	public HixentArcSite getSite(String siteCode) {
		HixentArcSite siteInfo = hixentArcAppSiteMapper.getSite(siteCode);
		return siteInfo;

	}
	// 项目列表
	public List<HixentArcSite> getSiteList(Integer appUserId) {
		return hixentArcAppSiteMapper.getSiteList(appUserId);
	}

	// 中控列表
	public List<HixentArcAppDeviceAndEquipInfo> getDeviceList(
			Integer appUserId, String siteCode, Integer warnIndex,
			String inquire, Integer limit, Integer page) {
		String limits = " " + Integer.toString((page - 1) * limit) + ","
				+ Integer.toString(limit) + " ";
		return hixentArcAppSiteMapper.getDeviceList(appUserId, siteCode,
				warnIndex, inquire, limits);
	}

	public Integer getDeviceListCount(Integer appUserId, String siteCode,
			Integer warnIndex, String inquire) {
		return hixentArcAppSiteMapper.getDeviceListCount(appUserId, siteCode,
				warnIndex, inquire);
	}

	// 终端列表
	public List<HixentArcAppDeviceAndEquipInfo> getEquipList(Integer appUserId,
			String siteCode, Integer warnIndex, Integer equipType,
			Integer isMqttEquip, String inquire, Integer limit, Integer page) {
		String limits = " " + Integer.toString((page - 1) * limit) + ","
				+ Integer.toString(limit) + " ";
		return hixentArcAppSiteMapper.getEquipList(appUserId, siteCode,
				warnIndex, equipType, isMqttEquip, inquire, limits);
	}

	public Integer getEquipListCount(Integer appUserId, String siteCode,
			Integer warnIndex, Integer equipType, Integer isMqttEquip,
			String inquire) {
		return hixentArcAppSiteMapper.getEquipListCount(appUserId, siteCode,
				warnIndex, equipType, isMqttEquip, inquire);
	}

	// 中控详情
	public HixentArcAppDeviceAndEquipInfo getDeviceInfo(String id) {
		return hixentArcAppSiteMapper.getDeviceInfo(id);
	}

	// 终端详情
	public HixentArcAppDeviceAndEquipInfo getEquipInfo(String id) {
		return hixentArcAppSiteMapper.getEquipInfo(id);
	}
	
	//上传站点LOGO
	@Transactional
	public Integer upload(MultipartFile[] file,Integer siteId) throws IOException {
		//logo
		String filename="",absoluteFileUrl="",relativeFileUrl="";
		for(int i=0;i<file.length;i++) {
    	  filename =file[i].getOriginalFilename();  
    	  if (file != null) {
              if (!"".equals(filename.trim())) {
                  File newFile = new File(filename);
                  FileOutputStream os = new FileOutputStream(newFile);
                  os.write(file[i].getBytes());
                  os.close();
                  file[i].transferTo(newFile);
                  // 上传到OSS
                  JSONObject upLoad = AliyunOSSUtil.upLoad2(newFile,AliyunOSSConfigConstant.COMPANY_FLODER_KEY);
                  //logo绝对路径
                  absoluteFileUrl = (String) upLoad.get("FILE_URL");
                  //logo相对路径
                  relativeFileUrl =  (String) upLoad.get("fileUrl");
              }
          }
    	  break;
        }
		//获取上次的LOGO信息进行OSS删除
		HixentArcSite tempBean=this.hixentArcAppSiteMapper.getSiteById(siteId);
		if(!StringUtil.strIsNullOrEmpty(tempBean.getLogoRelativeFileUrl())){
			AliyunOSSUtil.delFile(tempBean.getLogoRelativeFileUrl());
		}
		tempBean.setLogoFileName(filename);
		tempBean.setLogoFileUrl(absoluteFileUrl);
		tempBean.setLogoRelativeFileUrl(relativeFileUrl);
		return hixentArcAppSiteMapper.update(tempBean);
	}
	
	//删除站点LOGO
	@Transactional
	public Integer delImage(Integer siteId) throws IOException {
		//获取上次的LOGO信息进行OSS删除
		HixentArcSite tempBean=this.hixentArcAppSiteMapper.getSiteById(siteId);
		if(!StringUtil.strIsNullOrEmpty(tempBean.getLogoRelativeFileUrl())){
			AliyunOSSUtil.delFile(tempBean.getLogoRelativeFileUrl());
		}
		tempBean.setLogoFileName("");
		tempBean.setLogoFileUrl("");
		tempBean.setLogoRelativeFileUrl("");
		return hixentArcAppSiteMapper.update(tempBean);
	}
	
	
	//修改站点信息
	@Transactional
	public Integer update(ValidHixentEditSiteNiName device) throws IOException {
		HixentArcSite tempBean=this.hixentArcAppSiteMapper.getSiteById(device.getSiteId());
		tempBean.setSiteName(device.getSiteNiName());
		tempBean.setSiteBuildId(device.getSiteBuildId());
		tempBean.setSitePlace(device.getSiteRemark());
		tempBean.setLongitute(device.getLongitute());
		tempBean.setLatitude(device.getLatitude());
		tempBean.setMapNum(device.getMapNum());
		return hixentArcAppSiteMapper.update(tempBean);
	}
}