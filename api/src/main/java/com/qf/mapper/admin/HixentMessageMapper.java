package com.qf.mapper.admin;



import com.qf.model.admin.HixentMessage;
import com.qf.util.CustomerMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import java.util.List;



/**
 * author Vareck
 */
@Service
public interface HixentMessageMapper extends CustomerMapper<HixentMessage> {
	
	List<HixentMessage> getSelectList( 
		@Param("id")          Integer id,
		@Param("username")    String  username,
		@Param("mobile")      String  mobile,
		@Param("state")       Integer state
	);
	List<HixentMessage> getPageList( 
		@Param("id")          Integer id,
		@Param("username")    String  username,
		@Param("mobile")      String  mobile,
		@Param("state")       Integer state,
		@Param("order")       String  order,
		@Param("limits")      String  limits
	);
	List<HixentMessage> getSelectList2( 
		@Param("id")          Integer id,
		@Param("mobile")      String  mobile,
		@Param("state")       Integer state
	);
	List<HixentMessage> getPageList2( 
		@Param("id")          Integer id,
		@Param("mobile")      String  mobile,
		@Param("state")       Integer state,
		@Param("order")       String  order,
		@Param("limits")      String  limits
	);
	
	void deleteSendMessage(
		@Param("midArray")   String[]  midArray,
		@Param("id")          Integer   id
	);
	
	void deleteReceiveMessage(
		@Param("midArray")   String[]  midArray,
		@Param("id")          Integer   id
	);
}