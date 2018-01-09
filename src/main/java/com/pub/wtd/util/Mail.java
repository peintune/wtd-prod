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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author hekun
 * 
 */
public class Mail {
	String host = "smtp.163.com";// the host of mail server

	String user = "AutoTest_QA@163.com";// the user name of sender

	String password = "1688888";// the passwd of sender

	private List<String> receptions = new ArrayList<String>();// the reception people

	private LinkedList<Attachment> attachments = new LinkedList<Attachment>();// attachement

	private String content = "";// the mail main content

	private String subject="Automation Test Report";// the main subject

	private String from = "";// the mail from which mail

	private String proxy ="";

	public void setHost(String host) {

		this.host = host;

	}

	public void setProxy(String proxy){
		this.proxy = proxy;
	}

	public void setAccount(String user, String password) {

		this.user = user;

		this.password = password;

	}

	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * send the email
	 * 
	 */
	public void doSend() {

		Properties props = new Properties();

		props.put("mail.smtp.host", host);

		props.put("mail.smtp.auth", "true");

		if(!proxy.isEmpty() && !proxy.equals("") ){
			if(!proxy.contains(":")){

			}else{
				String[] proxys = proxy.split(":");
				props.setProperty("proxySet", "true");
				props.setProperty("socksProxyHost", proxys[0].trim());
				props.setProperty("socksProxyPort", proxys[1].trim());
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
			//message.addRecipient(Message.RecipientType.CC, new InternetAddress("AutoTest_QA@163.com"));
			
			message.setSubject(subject);
	

			message.setContent(content, "text/html;charset=UTF-8");

			message.setSentDate(new Date());

			message.saveChanges();

			Transport transport = mailSession.getTransport("smtp");

			transport.connect(host, user, password);

			transport.sendMessage(message, message.getAllRecipients());

			transport.close();

		} catch (Exception e) {

			System.out.println(e);

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

	public void setSubject(String subject) {
		this.subject = subject;
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
