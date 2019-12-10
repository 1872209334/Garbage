package com.qf.service.email; 



import java.io.File; 
import javax.mail.MessagingException; 
import javax.mail.internet.MimeMessage; 

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.core.io.FileSystemResource; 
import org.springframework.mail.SimpleMailMessage; 
import org.springframework.mail.javamail.JavaMailSender; 
import org.springframework.mail.javamail.MimeMessageHelper; 
import org.springframework.stereotype.Component; 



/**
 * 发送邮件服务
 * @author Vareck
 */ 
@Component 
public class EmailServiceImp implements EmailService { 



	@Autowired 
	private JavaMailSender mailSender; 
	
	@Value("${mail.fromMail.addr}") 
	private String from; 
	
	
	
	@Override 
	public void sendSimpleEmail(String allp, String subject, String content) { 
		
		String[] allArray = allp.split(",");
		
		SimpleMailMessage message = new SimpleMailMessage();       //创建简单邮件消息 
		message.setFrom(from);                                     //设置发送人 
        message.setTo(allArray);                                   //发送给多人
		message.setSubject(subject);       //设置主题 
		message.setText(content);          //设置内容 
		try { 
			mailSender.send(message);	   //执行发送邮件 
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	} 
	
	
	@Override 
	public void sendHtmlEmail(String allp , String subject, String content) { 
		
		String[] allArray = allp.split(",");
		
		MimeMessage message = mailSender.createMimeMessage();       //创建一个MINE消息 
		try { 
			//true表示需要创建一个multipart message 
			MimeMessageHelper helper = new MimeMessageHelper(message, true); 
			helper.setFrom(from); 
			helper.setTo(allArray); 
			helper.setSubject(subject); 
			helper.setText(content, true); 
			mailSender.send(message); 
		} catch (MessagingException e) { 
			e.printStackTrace();
		} 
	} 
	
	
	
	@Override 
	public void sendAttachmentsEmail(String allp , String subject, String content, String filePath) { 
		
		String[] allArray = allp.split(",");
		
		MimeMessage message = mailSender.createMimeMessage();       //创建一个MINE消息 
		try { 
			MimeMessageHelper helper = new MimeMessageHelper(message, true); 
			helper.setFrom(from); 
			helper.setTo(allArray); 
			helper.setSubject(subject); 
			helper.setText(content, true);
			// true表示这个邮件是有附件的 
			FileSystemResource file = new FileSystemResource(new File(filePath));
			// 创建文件系统资源 
			String fileName = filePath.substring(filePath.lastIndexOf(File.separator)); 
			helper.addAttachment(fileName, file);
			// 添加附件 
			mailSender.send(message); 
		} catch (MessagingException e) { 
			e.printStackTrace(); 
		} 
	} 
	
	
	@Override 
	public void sendInlineResourceEmail(String allp , String subject, String content, String rscPath, String rscId) { 
		
		String[] allArray = allp.split(",");
		
		MimeMessage message = mailSender.createMimeMessage(); 
		try { 
			MimeMessageHelper helper = new MimeMessageHelper(message, true); 
			helper.setFrom(from); 
			helper.setTo(allArray); 
			helper.setSubject(subject); 
			helper.setText(content, true); 
			FileSystemResource res = new FileSystemResource(new File(rscPath)); 
			//添加内联资源，一个id对应一个资源，最终通过id来找到该资源 
			helper.addInline(rscId, res);
			mailSender.send(message); 
		} catch (MessagingException e) { 
			e.printStackTrace();
		} 
	} 
	
	
	
}