package com.qf.model.admin.valid;



/**
 * author Vareck
 */
public class ValidHixentSendMailTest {
	
	
	
    private String  emailStr;

    private Integer type = 1;
	
	private String  content;

	private String  subject;
	
	private String  filePath;
	
	private String  rscPath;
	
	private String  rscId;
    

	
	public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
	
    public String getEmailStr() {
        return emailStr;
    }

    public void setEmailStr(String emailStr) {
        this.emailStr = emailStr;
    }

    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getRscPath() {
        return rscPath;
    }
    
    public void setRscPath(String rscPath) {
        this.rscPath = rscPath;
    }
    
    public String getRscId() {
        return rscId;
    }
    
    public void setRscId(String rscId) {
        this.rscId = rscId;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    
    
}