package com.qf.mapper.admin;



import com.qf.model.admin.HixentDictionary;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentDictionaryMapper extends CustomerMapper<HixentDictionary> {
	List<HixentDictionary> getSelectList( 
		@Param("typename")    String  typename,
		@Param("dkey")        String  dkey,
		@Param("dvalue")      String  dvalue
	);
	List<HixentDictionary> getPageList( 
		@Param("typename")    String  typename,
		@Param("dkey")        String  dkey,
		@Param("dvalue")      String  dvalue,
		@Param("order")       String  order,
		@Param("limits")      String  limits
	);
	List<HixentDictionary> selectAllData( String typename );
	List<HixentDictionary> selectGroupData();
	void deleteDictionary( String[] did_array );
}