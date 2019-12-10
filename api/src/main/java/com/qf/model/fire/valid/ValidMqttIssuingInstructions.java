package com.qf.model.fire.valid;



import org.hibernate.validator.constraints.NotEmpty;



/**
 * author Vareck
 */
public class ValidMqttIssuingInstructions  {

	
	
	@NotEmpty(message="请填写指令字符串")
	private String message;
	
	private Integer qos = 0;
	
	@NotEmpty(message="请填写设备号")
	private String ID;
	
	private int type = 1;
	
	private String extraData;
	
    private String node;
	
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
	
	public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getQos() {
        return qos;
    }

    public void setQos(Integer qos) {
        this.qos = qos;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

	public String getExtraData() {
		return extraData;
	}

	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
    
    
    
}