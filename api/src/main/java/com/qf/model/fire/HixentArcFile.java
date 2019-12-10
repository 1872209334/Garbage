package com.qf.model.fire;

public class HixentArcFile {
   private Integer id;
   private String fileName;
   private String filePath;//绝对路径
   private String uploader;
   private Integer uploaderId;
   private Integer type;
   private Integer  siteId;
   private String siteName;
   private Long uploadTime;
   private String siteCode;
   private String remark;
   private String relativeFileUrl;//相对路径
   private String folderName;//文件夹名
   private Integer folderId;
   private String gFilePath;
   public String getFolderName() {
	return folderName;
}

public void setFolderName(String folderName) {
	this.folderName = folderName;
}

public Integer getFolderId() {
	return folderId;
}

public void setFolderId(Integer folderId) {
	this.folderId = folderId;
}

public HixentArcFile() {
	// TODO Auto-generated constructor stub
}

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public String getFileName() {
	return fileName;
}

public void setFileName(String fileName) {
	this.fileName = fileName;
}

public String getFilePath() {
	return filePath;
}

public void setFilePath(String filePath) {
	this.filePath = filePath;
}

public String getUploader() {
	return uploader;
}

public void setUploader(String uploader) {
	this.uploader = uploader;
}

public Integer getUploaderId() {
	return uploaderId;
}

public void setUploaderId(Integer uploaderId) {
	this.uploaderId = uploaderId;
}

public Integer getType() {
	return type;
}

public void setType(Integer type) {
	this.type = type;
}

public Integer getSiteId() {
	return siteId;
}

public void setSiteId(Integer siteId) {
	this.siteId = siteId;
}

public String getSiteName() {
	return siteName;
}

public void setSiteName(String siteName) {
	this.siteName = siteName;
}

public Long getUploadTime() {
	return uploadTime;
}

public void setUploadTime(Long uploadTime) {
	this.uploadTime = uploadTime;
}

public String getSiteCode() {
	return siteCode;
}

public void setSiteCode(String siteCode) {
	this.siteCode = siteCode;
}

public String getRemark() {
	return remark;
}

public void setRemark(String remark) {
	this.remark = remark;
}

public String getRelativeFileUrl() {
	return relativeFileUrl;
}

public void setRelativeFileUrl(String relativeFileUrl) {
	this.relativeFileUrl = relativeFileUrl;
}

public String getgFilePath() {
	return gFilePath;
}

public void setgFilePath(String gFilePath) {
	this.gFilePath = gFilePath;
}

}
