package com.qf.model.fire;



import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;



/**
 * author Vareck
 */
public class HixentArcWarningList implements Serializable {

    private Integer resetTime;

    private Integer warning_no;
	
    private Integer app_id;
	
    private Integer admin_id;
	
    private Integer warning_index;
	
    private Integer warning_time;
    
    private Integer deal_time;
	
    private Integer isDeal;
   
    private Integer dispatch_time;
	
    private Integer etype;
    
    private String efm_id;
	
    private String  warning_type;
	
    private String  eid;
	
    private String  address;
    
    private String  app_name;
	
    private String  admin_name;
    
    private String  node;
	
    private double  lng_bmap;
	
    private double  lat_bmap;
    
    private String protocol_node;
    
    private String site_code;
    
    private String device_code;
	
    private String siteName;
    
    private String addr;
    
    private String lineCode;
    
    private Integer type;
	
    private String huid;
    
    private String hmobile;
    
    private String equipPlace;
    
    private String tempration;
    
    private String niName;//中控备注
    
    private String account;//处理人账号
    
    private String appMobile;//处理人电话
    
    private String dealRemark;//处理备注
    
    private String specificator;
    
    private Integer isReset;//是否设备复位 1 是
    
    
    public Integer getWarning_no() {
		return warning_no;
	}

	public void setWarning_no(Integer warning_no) {
		this.warning_no = warning_no;
	}

	public Integer getApp_id() {
		return app_id;
	}

	public void setApp_id(Integer app_id) {
		this.app_id = app_id;
	}

	public Integer getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}

	public Integer getWarning_index() {
		return warning_index;
	}

	public void setWarning_index(Integer warning_index) {
		this.warning_index = warning_index;
	}

	public Integer getDeal_time() {
		return deal_time;
	}

	public void setDeal_time(Integer deal_time) {
		this.deal_time = deal_time;
	}

	

	public Integer getDispatch_time() {
		return dispatch_time;
	}

	public void setDispatch_time(Integer dispatch_time) {
		this.dispatch_time = dispatch_time;
	}



	public String getWarning_type() {
		return warning_type;
	}

	public void setWarning_type(String warning_type) {
		this.warning_type = warning_type;
	}

	public String getApp_name() {
		return app_name;
	}

	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public double getLng_bmap() {
		return lng_bmap;
	}

	public void setLng_bmap(double lng_bmap) {
		this.lng_bmap = lng_bmap;
	}

	public double getLat_bmap() {
		return lat_bmap;
	}

	public void setLat_bmap(double lat_bmap) {
		this.lat_bmap = lat_bmap;
	}

	public String getProtocol_node() {
		return protocol_node;
	}

	public void setProtocol_node(String protocol_node) {
		this.protocol_node = protocol_node;
	}

	public String getSite_code() {
		return site_code;
	}

	public void setSite_code(String site_code) {
		this.site_code = site_code;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHuid() {
		return huid;
	}

	public void setHuid(String huid) {
		this.huid = huid;
	}

	public String getHmobile() {
		return hmobile;
	}

	public void setHmobile(String hmobile) {
		this.hmobile = hmobile;
	}

	public String getEquipPlace() {
		return equipPlace;
	}

	public void setEquipPlace(String equipPlace) {
		this.equipPlace = equipPlace;
	}

	public String getTempration() {
		return tempration;
	}

	public void setTempration(String tempration) {
		this.tempration = tempration;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public Integer getWarningNo() {
        return warning_no;
    }

    public void setWarningNo(Integer warning_no) {
        this.warning_no = warning_no;
    }
  
    public Integer getAppId() {
        return app_id;
    }

    public void setAppId(Integer app_id) {
        this.app_id = app_id;
    }
  
    public Integer getAdminId() {
        return admin_id;
    }

    public void setAdminId(Integer admin_id) {
        this.admin_id = admin_id;
    }
  
    public Integer getWarningIndex() {
        return warning_index;
    }

    public void setWarningIndex(Integer warning_index) {
        this.warning_index = warning_index;
    }
  
 
    
   

	public Integer getWarning_time() {
		return warning_time;
	}

	public void setWarning_time(Integer warning_time) {
		this.warning_time = warning_time;
	}

	public Integer getDealTime() {
        return deal_time;
    }

    public void setDealTime(Integer deal_time) {
        this.deal_time = deal_time;
    }
    
    
    public String getEfm_id() {
		return efm_id;
	}

	public void setEfm_id(String efm_id) {
		this.efm_id = efm_id;
	}

	
  
    public Integer getDispatchTime() {
        return dispatch_time;
    }

    public void setDispatchTime(Integer dispatch_time) {
        this.dispatch_time = dispatch_time;
    }
  
    public Integer getEtype() {
        return etype;
    }

    public void setEtype(Integer etype) {
        this.etype = etype;
    }
  
    public String getWarningType() {
        return warning_type;
    }

    public void setWarningType(String warning_type) {
        this.warning_type = warning_type;
    }
    
    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }
	
    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }
	
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
	
    public String getAppName() {
        return app_name;
    }

    public void setAppName(String app_name) {
        this.app_name = app_name;
    }
	
    public String getAdminName() {
        return admin_name;
    }

    public void setAdminName(String admin_name) {
        this.admin_name = admin_name;
    }

    public double getLatBmap() {
        return lat_bmap;
    }

    public void setLatBmap(double lat_bmap) {
        this.lat_bmap = lat_bmap;
    }
	
    public double getLngBmap() {
        return lng_bmap;
    }

    public void setLngBmap(double lng_bmap) {
        this.lng_bmap = lng_bmap;
    }
    
    public void setProtocolNode(String protocol_node) {
        this.protocol_node = protocol_node;
    }
    
    public String getProtocolNode() {
        return protocol_node;
    }
    
    public void setSiteCode(String site_code) {
        this.site_code = site_code;
    }
    
    public String getSiteCode() {
        return site_code;
    }
	
    public void setDeviceCode(String device_code) {
        this.device_code = device_code;
    }
    
    public String getDeviceCode() {
        return device_code;
    }

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getNiName() {
		return niName;
	}

	public void setNiName(String niName) {
		this.niName = niName;
	}

	public String getAppMobile() {
		return appMobile;
	}

	public void setAppMobile(String appMobile) {
		this.appMobile = appMobile;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(Integer isDeal) {
		this.isDeal = isDeal;
	}

	public String getDealRemark() {
		return dealRemark;
	}

	public void setDealRemark(String dealRemark) {
		this.dealRemark = dealRemark;
	}

	public String getSpecificator() {
		return specificator;
	}

	public void setSpecificator(String specificator) {
		this.specificator = specificator;
	}

	public Integer getIsReset() {
		return isReset;
	}

	public void setIsReset(Integer isReset) {
		this.isReset = isReset;
	}

	public Integer getResetTime() {
		return resetTime;
	}

	public void setResetTime(Integer resetTime) {
		this.resetTime = resetTime;
	}

	


}