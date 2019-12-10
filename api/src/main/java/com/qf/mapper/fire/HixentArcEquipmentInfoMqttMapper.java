package com.qf.mapper.fire;



import java.util.Set;
import java.util.List;
import com.qf.util.CustomerMapper;
import org.apache.ibatis.annotations.Param;
import com.qf.model.fire.HixentArcEquipmentInfoMqtt;
import org.springframework.stereotype.Service;



/**
 * author Vareck
 */
@Service
public interface HixentArcEquipmentInfoMqttMapper extends CustomerMapper<HixentArcEquipmentInfoMqtt> {
	
	void UpdateMqtt(
			@Param("fieldname")    String       fieldname,
			@Param("fieldvalue")   String       fieldvalue,
			@Param("id")           String       id
			);
	
	void insertMqtt(
			@Param("id")        	String       id,
			@Param("module_code")   String       module_code,
			@Param("line_code")     String       line_code,
			@Param("device_code")   String       device_code,
			@Param("site_code")     String       site_code,
			@Param("addr")     String       addr
	);
	
	/*列表(手机app虚拟分组)*/
	List<HixentArcEquipmentInfoMqtt> getAllDeviceList( @Param("pid") Integer  pid );
	List<HixentArcEquipmentInfoMqtt> getPageDeviceList( 
		@Param("order")    String    order,
		@Param("limits")   String    limits
	);
	
	/*列表(后台站点)*/
	List<HixentArcEquipmentInfoMqtt> getAllDeviceListByBid( 
		@Param("bid")   Set       bid,
		@Param("efmId") Integer   efmId
	);
	/*列表(后台站点)*/
	List<HixentArcEquipmentInfoMqtt> getAllMqttDeviceList();
	
	List<HixentArcEquipmentInfoMqtt> getCommonMqttList(
			@Param("allDid")      Set    allDid
			);
	
	List<HixentArcEquipmentInfoMqtt> getPageCommonMqttList(
			@Param("allDid")      Set    allDid,
			@Param("limits")   String    limits
			);
	
	
	/*列表(后台站点)*/
	List<HixentArcEquipmentInfoMqtt> getPageMqttDeviceList( 
			
		@Param("limits")   String    limits
	);
	
	List<HixentArcEquipmentInfoMqtt> getPageDeviceListByBid( 
		@Param("bid")      Set       bid,
		@Param("efmId")    Integer   efmId,
		@Param("order")    String    order,
		@Param("limits")   String    limits,
		@Param("lineCode")   String    lineCode
	);
	
	List<HixentArcEquipmentInfoMqtt> getPageEfmTerminalList( 
			@Param("efmId")    String   efmId,
			@Param("lineCode")   String    lineCode,
			@Param("limits")   String    limits
	);
	
	List<HixentArcEquipmentInfoMqtt> getAllEfmTerminalList( 
			@Param("efmId")    String   efmId,
			@Param("lineCode")   String    lineCode
	);
	
	
	
	Integer getEfmTerminalCount(
			@Param("efmId")    String   efmId,
			@Param("lineCode")   String    lineCode
	);
	
	List<HixentArcEquipmentInfoMqtt> getOneList( String  eid );
	
	HixentArcEquipmentInfoMqtt       getOne( String  eid );
	List<HixentArcEquipmentInfoMqtt> getDeviceListByProjectId(String[] pid);

	//通过项目code查询项目
	String getSiteCodeBySiteCode(@Param("siteCode")   String    siteCode);
	
	//插入项目
	Integer addSite(@Param("siteCode")   String    siteCode);
	
	//查询无线设备通过无线设备ID
	HixentArcEquipmentInfoMqtt selEquipMqttByEquipId(@Param("equipId")   String    equipId);
	
	//更新信息
	Integer updateEquipMqtt(@Param("equipId")String equipId,
			@Param("message")String message,@Param("field")String  field);
   
	//获取无线设备列表 分页
	List<HixentArcEquipmentInfoMqtt> getPageMqttEquipList( 
			@Param("siteCode")   String    siteCode,
			@Param("equipmentCategory")   Integer    equipmentCategory,	
		@Param("limits")   String    limits
	);
	//获取无线设备列表  数量
	Integer getPageMqttEquipCount(@Param("siteCode")   String    siteCode,
			@Param("equipmentCategory")   Integer    equipmentCategory
			);
	
	//删除终端
	Integer delMqttEquips(@Param("equipIds")String[] equipIds);
	
	//获取报警表的ID 拼接字符串(通过终端Id)
	String getWarnIdByEquipIds(@Param("equipIds")String[] equipIds);
		
	//删除报警表数据通过终端IDs
	Integer delWarnByEquipIds(@Param("equipIds")String[] equipIds);
	
	//删除反馈表数据通过报警表IDs
	Integer delFeedBackByWarnIds(@Param("warnIds")String[] warnIdByEfmId);
	
	//获取
	String getMqttEquipBySiteCode(@Param("siteCode")   String    siteCode);
	
	//删除终端通过项目
	Integer delMqttEquipBySiteCode(@Param("siteCode")   String    siteCode);
}