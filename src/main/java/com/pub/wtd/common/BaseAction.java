/**
 * @Title: BaseAction.java 
 * @Package com.pub.WTD.common 
 * @Description: the BaseAction supply base action for case and pages
 * @author hekun 158109016@qq.com
 * @date 2014��7��14�� ����4:04:10 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pub.wtd.util.IdentifyCode;
import com.pub.wtd.util.ScreenCapture;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class BaseAction {

	protected WTDGlobal wtdGlobal;
	protected SessionData sessionData;
	protected WebDriver webDriver;
	protected String hostName;
	protected Logger logger;

	public BaseAction() {

	}

	public BaseAction(SessionData sessionData) {
		this.sessionData = sessionData;
		wtdGlobal = new WTDGlobal();// initial the Global Objective
		webDriver = sessionData.getWebDriver();
		hostName = sessionData.getHostName();
		logger = sessionData.getLogger();

	}

	/**
	 * Capture a Picture
	 */
	public String screenCapture() {
		String path = new ScreenCapture(sessionData).getPicPath();
		sleep(500);
		return path;
	}

	/**
	 * @return String get the picture code value
	 */
	public String getPicCode(String bid) {
		IdentifyCode identifyCode = new IdentifyCode(sessionData);
		sleep(300);
		return identifyCode.getPicCodeVaule(bid);
	}

	/**
	 * @return String get the picture code value
	 */
	public String getSmsCode(String phoneNum, String bid) {
		IdentifyCode identifyCode = new IdentifyCode(sessionData);
		sleep(300);
		return identifyCode.getSmsCodeVaule(phoneNum, bid);
	}

	/**
	 * click element by all kinds of parameters
	 */
	protected boolean clickBy(By by) {
		try {
			WebElement el = webDriver.findElement(by);
			el.click();
			sleep(500);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + by.toString()
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return true;
	}

	/**
	 * click element by id
	 */
	protected boolean clickById(String id) {
		try {
			WebElement el = webDriver.findElement(By.id(id));
			el.click();
			sleep(500);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find ElementById:" + id
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return true;
	}

	/**
	 * click element by linkText
	 */
	protected boolean clickByLinkText(String link) {
		try {
			WebElement el = webDriver.findElement(By.linkText(link));
			el.click();
			sleep(500);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find ElementByLinkText:" + link
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return true;
	}

	/**
	 * input keyvalues into a text box
	 */
	protected boolean sendKeys(By by, String value) {
		try {
			WebElement el = webDriver.findElement(by);
			el.sendKeys(value);
			sleep(500);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + by.toString()
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return true;
	}

	/**
	 * input keyvalues by id into a text box
	 */
	protected boolean sendKeysById(String id, String value) {
		try {
			WebElement el = webDriver.findElement(By.id(id));
			el.sendKeys(value);
			sleep(500);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find ElementById:" + id
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return true;
	}

	/**
	 * judge the existing of one element
	 */
	protected boolean isExist(By by) {
		List<WebElement> el = new ArrayList<WebElement>();
		try {
			el = webDriver.findElements(by);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + by.toString()
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return !(el.size() == 0 || null == el);
	}

	/**
	 * judge the existing of one element
	 */
	protected boolean isTextExist(String content) {
		List<WebElement> el = new ArrayList<WebElement>();
		try {
			el = webDriver.findElements(By.xpath("//*[contains(.,'" + content
					+ "')]"));
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + content
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return false;
		}
		return !(el.size() == 0 || null == el);
	}

	/**
	 * judge the equals between current url and the given url
	 */
	protected boolean isCurrentURL(String url) {

		try {
			if (null == url || url.equals(""))
				return false;

			return webDriver.getCurrentUrl().toLowerCase()
					.equals(url.toLowerCase());

		} catch (Exception e) {
			logger.info("Exception", e);
			return false;
		}
	}

	/**
	 * find the text value of element
	 */
	protected String findElVaule(By by) {
		WebElement el;
		try {
			el = webDriver.findElement(by);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + by.toString()
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
			return "";
		}
		return el.getText();
	}

	/**
	 * find the web elements By by
	 */
	protected List<WebElement> findElements(By by) {
		List<WebElement> el = new ArrayList<WebElement>();
		try {
			el = webDriver.findElements(by);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + by.toString()
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
		}
		return el;
	}

	/**
	 * find the web element By by
	 */
	protected WebElement findElement(By by) {
		WebElement el = null;
		try {
			el = webDriver.findElement(by);
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + by.toString()
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
		}
		return el;
	}

	/**
	 * find the web element by id
	 */
	protected WebElement findElementById(String id) {
		WebElement el = null;
		try {
			el = webDriver.findElement(By.id(id));
		} catch (Exception e) {
			logger.error("~~~~~~~~~~~~~~ find Element:" + id
					+ " is failed ~~~~~~~~~~~~");
			logger.info("Exception", e);
		}
		return el;
	}

	/**
	 * sleep the thread
	 */
	protected void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * get a randome phone number
	 */
	protected String getRandomPhoneNum() {
		String currentTime = System.currentTimeMillis() / 100 + "";
		String phoneNum = "184";
		for (int i = 3; i < currentTime.length(); i++) {
			phoneNum += currentTime.charAt(i);
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return phoneNum;
	}

	/**
	 * acceptAlert
	 */
	public String alertAccept() {
		long waitForAlert = System.currentTimeMillis() + 6000;
		boolean boolFound = false;
		String text = "";
		do {
			try {
				Alert alert = this.webDriver.switchTo().alert();
				if (alert != null) {
					text = alert.getText();
					alert.accept();
					boolFound = true;
				}
			} catch (Exception ex) {
			}
		} while ((System.currentTimeMillis() < waitForAlert) && (!boolFound));
		return text;
	}

	/**
	 * dismiss Alert
	 */
	public String alertDismiss() {
		long waitForAlert = System.currentTimeMillis() + 6000;
		boolean boolFound = false;
		String text = "";
		do {
			try {
				Alert alert = this.webDriver.switchTo().alert();
				if (alert != null) {
					text = alert.getText();
					alert.dismiss();
					boolFound = true;
				}
			} catch (Exception ex) {
			}
		} while ((System.currentTimeMillis() < waitForAlert) && (!boolFound));
		return text;
	}

	/**
	 * switch a new Window
	 */
	public void switchNewWindow() {
		String currentHandle = webDriver.getWindowHandle();
		Set<String> windowshandles = webDriver.getWindowHandles();
		for (String s : windowshandles) {
			if (s.contains(currentHandle)) {
				continue;
			} else {
				webDriver.switchTo().window(s);
			}
		}
	}
}