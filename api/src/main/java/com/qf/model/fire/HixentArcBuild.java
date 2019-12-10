package com.qf.model.fire;




import java.io.Serializable;
import java.util.List;



/**
 *建筑类型Model
 * author RuanYu
 */
public class HixentArcBuild implements Serializable {
    
    private Integer id;//建筑ID
    private String buildName;//建筑名称
    private Integer equipCount;
    
    
    public HixentArcBuild() {
		// TODO Auto-generated constructor stub
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getBuildName() {
		return buildName;
	}


	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}


	public Integer getEquipCount() {
		return equipCount;
	}


	public void setEquipCount(Integer equipCount) {
		this.equipCount = equipCount;
	}

	
}