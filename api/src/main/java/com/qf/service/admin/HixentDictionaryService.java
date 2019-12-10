package com.qf.service.admin;



import com.qf.mapper.admin.HixentDictionaryMapper;
import com.qf.model.admin.HixentDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;



/**
 * web管理后台数据字典相关服务
 * author   Vareck
 */
@Service
public class HixentDictionaryService {

	
	
    @Autowired
    private HixentDictionaryMapper hixentDictionaryMapper;

    //数据字典列表
    public List<HixentDictionary> getSelectList(String typename,String dkey,String dvalue) {
    	return hixentDictionaryMapper.getSelectList(typename,dkey,dvalue);
    }
    
    public List<HixentDictionary> getPageList(String typename,String dkey,String dvalue,Integer limit,Integer page,String order) {
        String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentDictionaryMapper.getPageList(typename,dkey,dvalue,order,limits);
    }
    
    public List<HixentDictionary> selectAllData( String typename ) {
        return hixentDictionaryMapper.selectAllData( typename );
    }
    
    public List<HixentDictionary> selectGroupData() {
        return hixentDictionaryMapper.selectGroupData();
    }
    
    public void deleteDictionary( String[] did_array ) {
        hixentDictionaryMapper.deleteDictionary( did_array );
    }
    
    public void insert(HixentDictionary data){
    	hixentDictionaryMapper.insert(data);
    }
    
    public void update(HixentDictionary dictionary, Example example){
    	hixentDictionaryMapper.updateByExample(dictionary,example);
    }

    
    
}