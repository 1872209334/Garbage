package com.qf.mapper.admin;



import com.qf.model.admin.HixentLog;
import com.qf.model.admin.HixentMainLog;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;



/**
 * author Vareck
 */
@Service
public interface HixentLogMapper extends CustomerMapper<HixentLog> {
	
	@SuppressWarnings("rawtypes")
	List<HixentLog> getLogAllList(
		@Param("isException")   Integer  isException,
		@Param("username")    	String   username,
		@Param("opration")      String   opration,
		@Param("controller")    String   controller,
		@Param("method")    	String   method,
		@Param("ip")      		String   ip,
		@Param("time1")       	String   time1,
		@Param("time2")    		String   time2,
		@Param("userNameArray") Set      userNameArray,
		@Param("dateArray")     Set      dateArray
	);
	@SuppressWarnings("rawtypes")
	List<HixentLog> getLogPageList(
		@Param("isException")   Integer  isException,
		@Param("username")    	String   username,
		@Param("opration")      String   opration,
		@Param("controller")    String   controller,
		@Param("method")    	String   method,
		@Param("ip")      		String   ip,
		@Param("time1")       	String   time1,
		@Param("time2")    		String   time2,
		@Param("limits")    	String   limits,
		@Param("userNameArray") Set      userNameArray,
		@Param("dateArray")     Set      dateArray
	);
	
	//判断表是否存在
	List existTable(@Param("date") String  date);
	
	//创建分表
	void createTable(@Param("date") String  date);
	
	//搜索主表
	List<HixentLog> mainLogAllList();
	List<HixentMainLog> mainLogSelect(@Param("date") String  date);
	
	//插入数据
	void mainLogInsert(
		@Param("date") 		    String   date
	);
	void logInsert(
		@Param("message") 		String   message,
		@Param("username") 		String   username,
		@Param("opration")      String   opration,
		@Param("controller")    String   controller,
		@Param("method")    	String   method,
		@Param("ip")      		String   ip,
		@Param("params")      	String   params,
		@Param("date")      	String   date
	);
	//删除日志
	Integer delLog(@Param("ids")      	String[]   ids);
}