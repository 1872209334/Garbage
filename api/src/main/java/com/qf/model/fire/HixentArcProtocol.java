package com.qf.model.fire;



import java.io.Serializable;



/**
 * author Vareck
 */
public class HixentArcProtocol implements Serializable {


	
	private String  protocol_name;
	
    private String  protocol_id;
	
	private String  protocol_length;
	
	private String  protocol_value;
	
    private String  protocol_attribute;
	
	private String  protocol_node;
	
    private Integer protocol_type;
	
	
  
    public String getProtocolName() {
        return protocol_name;
    }

    public void setProtocolName(String protocol_name) {
        this.protocol_name = protocol_name;
    }
	
    public String getProtocolId() {
        return protocol_id;
    }

    public void setProtocolId(String protocol_id) {
        this.protocol_id = protocol_id;
    }
	
    public String getProtocolLength() {
        return protocol_length;
    }

    public void setProtocolLength(String protocol_length) {
        this.protocol_length = protocol_length;
    }
	
    public String getProtocolValue() {
        return protocol_value;
    }

    public void setProtocolValue(String protocol_value) {
        this.protocol_value = protocol_value;
    }
	
    public String getProtocolAttribute() {
        return protocol_attribute;
    }

    public void setProtocolAttribute(String protocol_attribute) {
        this.protocol_attribute = protocol_attribute;
    }
	
    public String getProtocolNode() {
        return protocol_node;
    }

    public void setProtocolNode(String protocol_node) {
        this.protocol_node = protocol_node;
    }
	
    public Integer getProtocolType() {
        return protocol_type;
    }

    public void setProtocolType(Integer protocol_type) {
        this.protocol_type = protocol_type;
    }
	

	
}