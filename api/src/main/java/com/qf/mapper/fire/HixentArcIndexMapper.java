package com.qf.mapper.fire;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.qf.model.admin.HixentArea;
import com.qf.model.admin.HixentProvince;
import com.qf.model.fire.HixentArcBuild;
import com.qf.model.fire.HixentArcControlGroup;
import com.qf.util.CustomerMapper;



/**
 * author RuanYu
 */
@Service
public interface HixentArcIndexMapper extends CustomerMapper<HixentArcControlGroup> {
	
	//项目数
	Integer siteCount(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
    //终端设备数
	Integer equipCount(@Param("type")Integer type,@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	//设备数下中控
	Integer deviceCount(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	//设备数下故障电弧
	//Integer arcCountOfEquip(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	//设备数下剩余电流
	//Integer currentCountOfEquip(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);

//	
//	Integer equipCountOffLine(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
//	//中控某问题类型数量
//	Integer deviceCountOfEquipOffLine(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
//	//中控某问题类型数量
//	Integer arcCountOfEquipOffLine(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
//	//中控某问题类型数量
//	Integer currentCountOfEquipOffLine(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
//

	//报警设备数
//	Integer equipCountAlarm(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	
	//中控某问题类型数量
	Integer deviceCountOfEquipAlarm(@Param("warnIndex")Integer warnIndex,@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	//电弧设备某问题类型数量
	Integer arcCountOfEquipAlarm(@Param("warnIndex")Integer warnIndex,@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	//组合式设备某问题类型数量
	Integer currentCountOfEquipAlarm(@Param("warnIndex")Integer warnIndex,@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);

    //在线设备数
	Integer equipCountOnLine(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
   
	//建筑下的设备数
	List<HixentArcBuild> buildCount(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	
	//全国下的设备数
	List<HixentProvince> provinceCount();
	//省下的设备数
	List<HixentArea> areaCount(@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);

   //故障数
	Integer equipEffectCount (@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);

   //头部报警数（有线）
	Integer headAlarmCount(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
	  //头部报警数（无线）
	Integer headAlarmCountWireLess(@Param("areaId")Integer areaId,@Param("provinceId")Integer provinceId,@Param("siteCordArr")String[] siteCordArr);
}