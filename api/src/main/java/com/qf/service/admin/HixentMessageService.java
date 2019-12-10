package com.qf.service.admin;



import com.qf.mapper.admin.HixentMessageMapper;
import com.qf.model.admin.HixentMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import java.util.List;



/**
 * web管理后台站内信相关服务
 * author   Vareck
 */
@Service
public class HixentMessageService {

	
	
    @Autowired
    private HixentMessageMapper hixentMessageMapper;

    
    
    //列表
    public List<HixentMessage> getSelectList(Integer id,String username,String mobile,Integer state) {
    	return hixentMessageMapper.getSelectList(id,username,mobile,state);
    }
    
    public List<HixentMessage> getPageList(Integer id,String username,String mobile,Integer state,Integer limit,Integer page,String order) {
        String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentMessageMapper.getPageList(id,username,mobile,state,order,limits);
    }
    
    public List<HixentMessage> getSelectList2(Integer id,String mobile,Integer state) {
    	return hixentMessageMapper.getSelectList2(id,mobile,state);
    }
    
    public List<HixentMessage> getPageList2(Integer id,String mobile,Integer state,Integer limit,Integer page,String order) {
        String limits = " "+Integer.toString((page-1)*limit)+","+Integer.toString(limit)+" ";
    	return hixentMessageMapper.getPageList2(id,mobile,state,order,limits);
    }
    
    public void insert(HixentMessage data){
    	hixentMessageMapper.insert(data);
    }
    
    public void update(HixentMessage message, Example example){
    	hixentMessageMapper.updateByExample(message,example);
    }
    
    //发送方删除
    public void deleteSendMessage( String[] midArray,Integer id ) {
    	hixentMessageMapper.deleteSendMessage( midArray,id );
    }
    
    //接收方删除
    public void deleteReceiveMessage( String[] midArray,Integer id ) {
    	hixentMessageMapper.deleteReceiveMessage( midArray,id );
    }

    
    
}