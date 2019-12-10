package com.qf.service.admin;



import com.qf.mapper.admin.HixentCompanyMapper;
import com.qf.model.admin.HixentCompany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;



/**
 * web管理后台公司信息相关服务
 * author   Vareck
 */
@Service
public class HixentCompanyService {

    @Autowired
    private HixentCompanyMapper hixentCompanyMapper;

    //公司列表
    public List<HixentCompany> getSelectList() {
    	return hixentCompanyMapper.getSelectList();
    }
    
    public List<HixentCompany> getCompanyAllList( String name ) {
    	return hixentCompanyMapper.getCompanyAllList( name );
    }
    public List<HixentCompany> getCompanyPageList( String name,String order,Integer limit,Integer page ) {
    	String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentCompanyMapper.getCompanyPageList( name,order,limits );
    }

    public HixentCompany selectOne(HixentCompany hixentCompany) {
    	return hixentCompanyMapper.selectOne(hixentCompany);
    }
    
    public int delete(HixentCompany hixentCompany) {
    	return hixentCompanyMapper.delete(hixentCompany);
    }
    
    public int insert(HixentCompany data){
    	return hixentCompanyMapper.insert(data);
    }
    
    public void update(HixentCompany company, Example example){
    	hixentCompanyMapper.updateByExample(company,example);
    }
    
}