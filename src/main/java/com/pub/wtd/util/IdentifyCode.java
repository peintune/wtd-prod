/**
 * @Title: ScreenCapture.java 
 * @Package com.pub.WTD.util 
 * @Description: The tool class to get the picture verification code 
 * @date 2014��7��4�� ����2:30:00 
 * @version V1.0   
 */
package com.pub.wtd.util;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pub.wtd.common.InitWebDriver;
import com.pub.wtd.common.SessionData;

/**
 * @author hekun
 * 
 */
public class IdentifyCode{
	String manageHost;
	SessionData sessionData;
	WebDriver webDriver;
	String hostName="";
	Logger logger;
	WebDriver newDriver;
	String sp = System.getProperty("file.separator");
	/**
	 * get the picture verification code
	 * 
	 */
	
	public IdentifyCode(SessionData sessionData) {
		this.sessionData=sessionData;
		this.webDriver=sessionData.getWebDriver();
		this.hostName=sessionData.getHostName();
		this.logger=sessionData.getLogger();
		manageHost=hostName.replace("www", "manage");
		openNewWebDrvier();
		loginToManage();
	}
	
	/**
	 * @return String:the image code value
	 * 
	 */
	public String getPicCodeVaule(String bid) {
		sessionData
				.getLogger()
				.info("~~~~~~~~~~~~Start to get the picture verification code  ~~~~~~~~~~~~");
		String result = "";
		try {
			Set<Cookie> allCookies = sessionData.getWebDriver().manage()
					.getCookies();
			String guid = "";
			for (Cookie loadedCookie : allCookies) {
				if (loadedCookie.getName().equalsIgnoreCase("guid")) {
					guid = loadedCookie.getValue();
				}
			}
			String requestUrl = manageHost;
			
			newDriver.get(requestUrl);
			WebElement userName = newDriver.findElement(By
					.tagName("body"));
			result = userName.getText();
			sessionData
					.getLogger()
					.info("~~~~~~~~~~~~Finish to get the picture verification code  ~~~~~~~~~~~~");
		} catch (Exception e) {
			sessionData
					.getLogger()
					.error("~~~~~~~~~~~~【  error 】 Get the picture verification code failed! ~~~~~~~~~~~~");
			e.printStackTrace();
		}
		closeNewWebDrvier();
		return result;
	}
	/**
	 * @return String:the image code value
	 * 
	 */
	public String getSmsCodeVaule(String phoneNum,String bid) {
		sessionData
				.getLogger()
				.info("~~~~~~~~~~~~Start to get the picture verification code  ~~~~~~~~~~~~");
		String result = "";
		try {	
			String guid = phoneNum;
			String requestUrl = manageHost;
			newDriver.get(requestUrl);
			WebElement userName = newDriver.findElement(By
					.tagName("body"));
			result = userName.getText();
			sessionData
					.getLogger()
					.info("~~~~~~~~~~~~Finish to get the picture verification code  ~~~~~~~~~~~~");
		} catch (Exception e) {
			sessionData
					.getLogger()
					.error("~~~~~~~~~~~~【  error 】 Get the picture verification code failed! ~~~~~~~~~~~~");
			e.printStackTrace();
		}
		closeNewWebDrvier();
		return result;
	}
	
	/**
	 * open a new webdriver and then login to manage system.
	 * 
	 */
	public void loginToManage() {
		newDriver.get(manageHost);
		boolean isLogin = false;
		List<WebElement> el = new ArrayList<WebElement>();
		try {
			el = newDriver.findElements(By.linkText("退出"));
            isLogin = el.size() >= 1;
		} catch (Exception e) {
			isLogin = false;
		}

		if (!isLogin) {
			try {
				WebElement userName = newDriver.findElement(By
						.name("username"));
				userName.sendKeys("hekun");
				WebElement passwd = newDriver.findElement(By
						.name("password"));
				passwd.sendKeys("123456");
				WebElement submitBt = newDriver.findElement(By
						.name("submit"));
				submitBt.click();
			} catch (Exception e) {
				logger.error("~~~~~~~~~~~~~~ Login to manage failed! ~~~~~~~~~~~~");
				logger.info("Exception", e);
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				logger.error("~~~~~~~~~~~~ Log to manage failed! ~~~~~~~~~~~~");
				e.printStackTrace();
			}

		}

	}
	/**
	 * close the new webdriver which opened for get the verification code 
	 * 
	 */
	private void closeNewWebDrvier() {
		newDriver.close();
		newDriver.quit();

	}
	/**
	 * open the new webdriver which opened for get the verification code 
	 * 
	 */
	private void openNewWebDrvier() {
		SAXReader saxReader = new SAXReader();// read the config file
		Document doc = null;
		HashMap<String, String> webDriverhash = new HashMap<String, String>();
		HashMap<String, String> newWebDriverHash= new HashMap<String, String>();
		try {
			String sp = System.getProperty("file.separator");
			doc = saxReader.read(new File(GlobalInfo.rootPath + sp+"config"+sp + "conf"
					+ sp + "wtd.cfg.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		List<Element> webDriveEles = doc.selectNodes("//Browser/*");
		for (Element e : webDriveEles) {
			String location ="";
			location = e.attributeValue("location");
			String browserName = e.getName();
			webDriverhash.put(browserName, location);
		}

		String frontWebDriverName=webDriver.toString();
		Iterator iter = webDriverhash.entrySet().iterator();
		String webDriverKey="";
		String browserName = "";
		String browserLocation ="";
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			String sp = System.getProperty("file.separator");
			browserName = (String) entry.getKey();
			browserLocation = (String) entry.getValue();
		if(frontWebDriverName.toLowerCase().contains(browserName.toLowerCase())){
			break;
		}
		}
		
		newDriver = new InitWebDriver().initWebDrvier(browserName, browserLocation);

	}
}
