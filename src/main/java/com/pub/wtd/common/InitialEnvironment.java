/**
 * @Title: InitialEnvironment.java 
 * @Package com.pub.WTD.common 
 * @Description: initial the Env by set all necessary variables
 * @author hekun 158109016@qq.com
 * @date 2014��7��3�� ����1:06:34 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class InitialEnvironment {
	SessionData sessionData = new SessionData();
	Document doc = null;
	List<String> emails = new ArrayList<String>();// email array
	HashMap<String, String> webDriverhash = new HashMap<String, String>();// webDrvier
																			// hash
																			// map
	HashMap<String,String> mailSender = new HashMap<String,String>();

	public InitialEnvironment() {
		String path = new File("").getAbsolutePath();// get the local project
														// path

		GlobalInfo.rootPath = path;// the the static rootPath to the logcal
									// project path

		
		SAXReader saxReader = new SAXReader();// read the config file


		try {
			String sp = System.getProperty("file.separator");
			doc = saxReader.read(new File(GlobalInfo.rootPath + sp + "config"+sp+"conf"
					+ sp + "wtd.cfg.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		setPcHost();
		setShareFolder();
		setHostNameToSession(doc);

		setWebDrivers(doc);

		setMailReceptors(doc);
		
		setOnlineVarible();
	}

	/**
	 * get Email Address
	 */
	public List<String> getMailReceptors() {

		return emails;
	}

	/**
	 * get Email Address
	 */
	public void setMailReceptors(Document doc) {

		List<Element> emailElements = (List<Element>) doc
				.selectNodes("//MailReceptor/user");
		for (int i = 0; i < emailElements.size(); i++) {
			emails.add(emailElements.get(i).attributeValue("email"));
		}

	}

	/**
	 * get Email Address
	 */
	public HashMap<String,String> getMailSender() {
		boolean enableStr = true;
		boolean useDefault = true;
		try {
			Element urlElement = (Element) doc
					.selectSingleNode("//MailSender");
			enableStr = urlElement.attributeValue("enable").equalsIgnoreCase("false")?false:true;
			useDefault = urlElement.attributeValue("usedefault").equalsIgnoreCase("false")?false:true;
		}catch (Exception ignore){

		}

		if(enableStr){
			mailSender.put("isEnable","true");
			mailSender.put("useDefault",useDefault?"true":"false");
			Element protocolEl = (Element) doc
					.selectSingleNode("//MailSender/protocol");
			Element hostEl = (Element) doc
					.selectSingleNode("//MailSender/host");
			Element portEl = (Element) doc
					.selectSingleNode("//MailSender/port");
			Element mailEl = (Element) doc
					.selectSingleNode("//MailSender/mail");
			Element passwordEl = (Element) doc
					.selectSingleNode("//MailSender/password");

			mailSender.put("protocol",protocolEl.attributeValue("value"));
			mailSender.put("host",hostEl.attributeValue("value"));
			mailSender.put("port",portEl.attributeValue("value"));
			mailSender.put("user",mailEl.attributeValue("value"));
			mailSender.put("password",passwordEl.attributeValue("value"));
		}else{
			mailSender.put("isEnable","false");
		}
		return mailSender;
	}

	public String getProxy(){
		String proxy = "";
		try {
			Element urlElement = (Element) doc
					.selectSingleNode("//Proxy");
			proxy = urlElement.attributeValue("value");
		}catch (Exception ignore){

		}
		return proxy;
	}


	public String getMailSubject(){
		String mailSubject = "Automation Test Report";
		try {
			Element urlElement = (Element) doc
					.selectSingleNode("//MailSubject");
			mailSubject = urlElement.attributeValue("value");
		}catch (Exception ignore){

		}
		return mailSubject;
	}

	/**
	 * get WebDrivers
	 */
	public HashMap<String, String> getWebDrivers() {

		return webDriverhash;
	}

	/**
	 * set WebDrivers
	 */
	public void setWebDrivers(Document doc) {
		List<Element> webDriveEles = doc.selectNodes("//Browser/*");
		for (Element e : webDriveEles) {
			String location = e.attributeValue("location");
			String browserName = e.getName();
			webDriverhash.put(browserName, location);
		}

	}

	/**
	 * get sessionData obj
	 */
	public SessionData getSessionData() {
		return sessionData;
	}

	/**
	 * set the hostName
	 */
	public void setHostName(String hostName) {
		sessionData.setHostName(hostName);
	}

	/**
	 * set the hostName to Session Data
	 */
	public void setHostNameToSession(Document doc) {

		String hostName = "";
		try {
			Element urlElement = (Element) doc
					.selectSingleNode("//host-url/url");
			hostName = urlElement.attributeValue("name");
		}catch (Exception ignore){

		}
		sessionData.setHostName(hostName);
	}
	
	/**
	 * set the internet PC host
	 */
	public void setPcHost() {

		InetAddress a;
		try {
			a = InetAddress.getLocalHost();
			GlobalInfo.pcHostName=a.getHostName();
			GlobalInfo.pcIP=a.getHostAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * set the share log folder on this PC
	 */
	public void setShareFolder() {
		try{
			String sp = System.getProperty("file.separator");
			if(GlobalInfo.os.contains("win")){
			Runtime.getRuntime().exec("net share "+GlobalInfo.pcHostName+"="+GlobalInfo.rootPath+sp+"bin"+sp+"logs");
			}else{
				//linux 找到设置共享目录的方法即可
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * get the internet PC host
	 */
	public String getPcHost() {

		return GlobalInfo.pcHostName;
	}
	
	/**
	 * get the internet PC Ip address
	 */
	public String getPcIp() {

		return GlobalInfo.pcIP;
	}

	
	/**
	 * set the isNoline varible
	 */
	private void setOnlineVarible() {
        GlobalInfo.isOnlineTest = sessionData.getHostName().contains("onlinehost.com");
	}
	
}
