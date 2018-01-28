/**
 * @Title: Mail.java 
 * @Package com.pub.WTD.util 
 * @Description: the Mail objective class
 * @author hekun 158109016@qq.com
 * @date 2014��7��9�� ����2:02:28 
 * @version V1.0   
 */
package com.pub.wtd.util;

import java.io.File;
import java.security.Security;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author hekun
 * 
 */
public class Mail {
	String abdcd = "smtp.163.com";

	String dfdfd = "AutoTest_QA@163.com";

	private List<String> receptions = new ArrayList<String>();// the reception people

	private LinkedList<Attachment> attachments = new LinkedList<Attachment>();// attachement

	private String content = "";// the mail main content

	private String mailSubject="Automation Test Report";// the main subject

	private String from = "";// the mail from which mail

	private String proxy ="";
	String adfdfd = "1688888";
	private HashMap<String, String> mailSender;

	public void setHost(String host) {

		this.abdcd = host;

	}

	public void setProxy(String proxy){
		this.proxy = proxy;
	}

	public void setAccount(String user, String password) {

		this.dfdfd = user;

		this.adfdfd = password;

	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * send the email
	 * 
	 */

	public void doSend2() {

		Properties props = new Properties();

		props.put("mail.smtp.host", abdcd);
		props.put("mail.smtp.auth", "true");
		if(!proxy.isEmpty() && !proxy.equals("") ){
			if(!proxy.contains(":")){

			}else{
				String[] proxys = proxy.split(":");
				props.put("proxySet", "true");
				props.put("proxyHost", proxys[0].trim());
				props.setProperty("proxyPort", proxys[1].trim());
			}
		}
		try {

			Session mailSession = Session.getDefaultInstance(props);

			Message message = new MimeMessage(mailSession);

			message.setFrom(new InternetAddress(from));
			
			for(String to:receptions){
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(
						to));
			}

			message.setSubject(mailSubject);
			message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(dfdfd));

			message.setContent(content, "text/html;charset=UTF-8");
			message.setSentDate(new Date());
			message.saveChanges();

			Transport transport = mailSession.getTransport("smtp");

			transport.connect(abdcd, dfdfd, adfdfd);
			
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (Exception e) {
		}

	}


	public void doSend() {

		boolean useDefaultServer = mailSender.get("useDefault").equalsIgnoreCase("true");

		Properties props = new Properties();

		//props.put("mail.smtp.host", host);
		if(useDefaultServer){
			props.put("mail.smtp.host", abdcd);
			props.setProperty("mail.smtp.port", "25");
		}else{
			props.put("mail.smtp.host", mailSender.get("host"));
			props.setProperty("mail.smtp.port", mailSender.get("port"));
		}

		props.put("mail.smtp.auth", "true");

		if(!proxy.isEmpty() && !proxy.equals("") ){
			if(!proxy.contains(":")){

			}else{
				String[] proxys = proxy.split(":");
				props.put("proxySet", "true");
				props.put("proxyHost", proxys[0].trim());
				props.setProperty("proxyPort", proxys[1].trim());
			}
		}
		try {

			Session mailSession = Session.getDefaultInstance(props);

			Message message = new MimeMessage(mailSession);

			//message.setFrom(new InternetAddress(from));
			message.setFrom(new InternetAddress(mailSender.get("user")));


			for(String to:receptions){
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(
						to));

			}

			message.setSubject(mailSubject);


			message.setContent(content, "text/html;charset=UTF-8");

			message.setSentDate(new Date());

			message.saveChanges();
			message.addRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(mailSender.get("user")));
			if(useDefaultServer){
				Transport transport = mailSession.getTransport("smtp");
				transport.connect(abdcd, dfdfd, adfdfd);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			}else{
				Transport transport = mailSession.getTransport(mailSender.get("protocol"));
				transport.connect(mailSender.get("host"), mailSender.get("user"), mailSender.get("password"));
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	
	public void setReceptions(List<String> receptions) {
		this.receptions = receptions;
	}

	public void setAttachment(LinkedList<Attachment> attachments) {
		this.attachments = attachments;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public void setMailSender(HashMap<String,String> mailSender){
		this.mailSender = mailSender;
	}

	// attachment class contains File and fileName
	class Attachment {
		File file;

		String fileName;

		Attachment(File file, String name) {
			this.file = file;
			fileName = name;
		}

	}

}
