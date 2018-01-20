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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
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

	private String mailSubject="Automation Test Report";// the main subject

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



	public void doSend2(){
		Properties props = System.getProperties();
		//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		if(!proxy.isEmpty() && !proxy.equals("") ){
			if(!proxy.contains(":")){

			}else{
				String[] proxys = proxy.split(":");
				props.setProperty("proxySet","true");
				props.setProperty("socksProxyHost",proxys[0].trim());
				props.setProperty("socksProxyPort",proxys[1].trim());
			}
		}

		props.setProperty("mail.smtp.host", host);
		//props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		Session session = Session.getDefaultInstance(props,
				new Authenticator(){
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("AutoTest_QA@163.com", "1688888");
					}});

		// -- Create a new message --
		Message msg = new MimeMessage(session);

		// -- Set the FROM and TO fields --
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("158109016@qq.com", false));
			msg.setSubject("Hello");
			msg.setText("How are you");
			msg.setSentDate(new Date());
			Transport.send(msg);
			System.out.println("success");
		}catch ( Exception d){
			d.printStackTrace();
		}
	}
	public void doSend() {

		Properties props = new Properties();

		props.put("mail.smtp.host", host);

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
			//message.addRecipient(Message.RecipientType.CC, new InternetAddress("AutoTest_QA@163.com"));
			
			message.setSubject(mailSubject);


			message.setContent(content, "text/html;charset=UTF-8");

			//message.setContent("hello", "text/html;charset=UTF-8");


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

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
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
