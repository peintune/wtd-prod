/**
 * @Title: SessionData.java 
 * @Package com.pub.WTD.common 
 * @Description: To store some common variables
 * @author hekun 158109016@qq.com
 * @date 2014��7��3�� ����10:50:03 
 * @version V1.0   
 */
package com.pub.wtd.common;


import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class SessionData {

	private WebDriver webDriver;// webdriver to launch the senenium webdriver

	private String hostName;// the hostName which test this time

	private String caseName;// the caseName which test this time

	private String logFolder;// the logFolder which generated this time

	private Logger logger;// the logger which can generate log file

	public SessionData() {

	}

	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	public String getLogFolder() {
		return logFolder;
	}

	public void setLogFolder(String logFolder) {
		this.logFolder = logFolder;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
