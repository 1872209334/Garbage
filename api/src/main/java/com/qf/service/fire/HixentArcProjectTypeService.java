package com.qf.service.fire;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;

import com.qf.model.fire.HixentArcEfmDevice;
import com.qf.model.fire.HixentArcProjectType;
import com.qf.mapper.fire.HixentArcProjectTypeMapper;
import java.util.List;



/**
 * 虚拟分组相关服务
 * author Vareck
 */
@Service
public class HixentArcProjectTypeService {
	
	
	
	@Autowired
    private HixentArcProjectTypeMapper hixentArcProjectTypeMapper;
	
	
	
    //根据分组id获取所有信息
    public List<HixentArcProjectType> getAllByProjectId(String[] pid) {
    	return hixentArcProjectTypeMapper.getAllByProjectId(pid);
    }
    
    //获取所有信息
    public List<HixentArcProjectType> selectAll() {
    	return hixentArcProjectTypeMapper.selectAll();
    }
	
	//获取详细信息
    public HixentArcProjectType selectOne(HixentArcProjectType hixentArcProjectType) {
    	return hixentArcProjectTypeMapper.selectOne(hixentArcProjectType);
    }
    
    public HixentArcProjectType getOne(Integer pid) {
    	return hixentArcProjectTypeMapper.getOne(pid);
    }
    
    //删除
    public int delete(HixentArcProjectType hixentArcProjectType) {
    	return hixentArcProjectTypeMapper.delete(hixentArcProjectType);
    }
    
    //新增
    public int insert(HixentArcProjectType data){
    	return hixentArcProjectTypeMapper.insert(data);
    }
    
    //更新
    public void update(HixentArcProjectType hixentArcProjectType, Example example){
    	hixentArcProjectTypeMapper.updateByExample(hixentArcProjectType,example);
    }
    
    //获取所有分组
    public List<HixentArcProjectType> getProjectSize(Integer id){
    	return hixentArcProjectTypeMapper.getProjectSize(id);
    }
    
    public List<HixentArcProjectType> getPageProject(Integer id,Integer limit,Integer page){
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return  hixentArcProjectTypeMapper.getPageProject(id,limits);
    }
    
    //添加分组
    public void insertProject(String name,String address,Integer id,String phone){
    	hixentArcProjectTypeMapper.insertProject(name,address,id,phone);
    }
    
  //检查分组是否存在
    public List<HixentArcProjectType> checkName(String name){
    	return hixentArcProjectTypeMapper.checkName(name);
    }
    
  //编辑分组
    public void updateProject(String name,String address,String phone,Integer type){
    	hixentArcProjectTypeMapper.updateProject(name,address,phone,type);
    }
    
    
    
    
    
    
    
    
    
    
    
}