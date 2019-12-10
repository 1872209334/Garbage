package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;



/**
 * author RuanYu
 */
public class ValidHixentFile  {

	

	
    private Integer type = -1;
    private Integer siteId = 0;
    private String startTime = "";
    private String endTime = "";
    private Integer currentPage=1;
    private Integer pageSize=10;
    private Integer folderId=0;
    
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getFolderId() {
		return folderId;
	}
	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}
    
	
	

	


	
	
   
    
}