package com.qf.model;



/**
 * 基础信息
 * author Vareck
 */
public class Base  {

	
	
    private Integer  isPage  = 1;
	
    private Integer  page    = 1;
    
    private Integer  limit   = 10;
    
    private String   order   = "id desc" ;

	
	
	public Integer getIsPage() {
        return isPage;
    }

    public void setIsPage(Integer isPage) {
        this.isPage = isPage;
    }
    
	public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
	
	
	
}