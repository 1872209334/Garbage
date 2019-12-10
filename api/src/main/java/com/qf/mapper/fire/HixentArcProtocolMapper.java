package com.qf.mapper.fire;



import com.qf.util.CustomerMapper;
import com.qf.model.fire.HixentArcProtocol;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;



/**
 * author Vareck
 */
@Service
public interface HixentArcProtocolMapper extends CustomerMapper<HixentArcProtocol> {

	
	
	HixentArcProtocol getOne( 
		@Param("protocolId")     String  protocolId,
		@Param("protocolLength") String  protocolLength,
		@Param("protocolValue")  String  protocolValue
	);
	
	HixentArcProtocol selectByName(@Param("name") String  name);
	
	HixentArcProtocol getOneByprotocolId( 
		@Param("protocolId")     String  protocolId
	);
	
	List<HixentArcProtocol> ListgetOne( 
			@Param("protocolId")     String  protocolId,
			@Param("protocolLength") String  protocolLength,
			@Param("protocolValue")  String  protocolValue
		);
		
	List<HixentArcProtocol> ListgetOneByprotocolId( 
			@Param("protocolId")     String  protocolId
			);

	List<HixentArcProtocol> ListselectByName(@Param("name") String  name);

	
}