package com.qf.model.fire;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentArcProjectType implements Serializable {



    private Integer id;
	
    private Integer admin_id;
	
    private String  address;
	
    private String  name;
	
    private String  phone;
    
    private String  img_url;
  
    private double  lng_bmap;
	
    private double  lat_bmap;
 

  
    public String getImgUrl() {
        return img_url;
    }

    public void setImgUrl(String img_url) {
        this.img_url = img_url;
    }
    
    public Integer getAdminId() {
        return admin_id;
    }

    public void setAdminId(Integer admin_id) {
        this.admin_id = admin_id;
    }
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	
	public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
	


}