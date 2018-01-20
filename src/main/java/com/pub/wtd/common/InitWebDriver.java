/**
 * @Title: InitWebDriver.java 
 * @Package com.pub.WTD.common 
 * @Description: new all WebDriver objective
 * @author hekun 158109016@qq.com
 * @date 2014��7��2�� ����7:47:33 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.pub.wtd.util.HttpSender;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import org.openqa.selenium.safari.SafariDriver;

import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class InitWebDriver {
	List<WebDriver> webDriverList = new ArrayList<WebDriver>();
	public InitWebDriver(){
	}
	/**
	 * new all webdrivers and return the webderiver objective
	 * 
	 */
	public InitWebDriver(HashMap driverHash) {

		Iterator iter = driverHash.entrySet().iterator();
		while (iter.hasNext()) {
			WebDriver webDriver;
			Map.Entry entry = (Map.Entry) iter.next();
			String sp = System.getProperty("file.separator");
			String browserName = (String) entry.getKey();
			String browserLocation = (String) entry.getValue();
			if (browserName.toLowerCase().contains("ie")) {
				System.setProperty("webdriver.ie.driver",GlobalInfo.rootPath + sp +"bin"+sp+ "tools" + sp
						+ "IEDriverServer.exe");
				webDriver = new InternetExplorerDriver();
				webDriverList.add(webDriver);
			} else if (browserName.toLowerCase().contains("firefox")) {
				if (browserLocation.equals("")) {
					webDriver = new FirefoxDriver();
					webDriverList.add(webDriver);
				} else {
					File pathToFirefoxBinary = new File(
							browserLocation.replace("\\", "\\\\"));
					FirefoxBinary firefoxbin = new FirefoxBinary(
							pathToFirefoxBinary);
					webDriver = new FirefoxDriver(firefoxbin, null);
					webDriverList.add(webDriver);
				}
			} else if (browserName.toLowerCase().contains("chrome")) {
				System.setProperty("webdriver.chrome.driver",GlobalInfo.rootPath + sp +"bin"+sp+ "tools" + sp
						+ "chromedriver.exe");
				if(browserLocation.equals("")){
					webDriver=new ChromeDriver();
				}else{
					webDriver=new ChromeDriver();
				}
				webDriverList.add(webDriver);
			}
		}
	}
	/**
	 * init the single webdriver
	 * 
	 */
	public WebDriver initWebDrvier(String browserName,String browserLocation) {
			WebDriver webDriver = null;
			String sp = System.getProperty("file.separator");
			if (browserName.toLowerCase().contains("ie")) {
				System.setProperty("webdriver.ie.driver",GlobalInfo.rootPath + sp+"bin"+sp + "tools" + sp
						+ "IEDriverServer.exe");
				webDriver = new InternetExplorerDriver();
		
			} else if (browserName.toLowerCase().contains("firefox")) {
				if (browserLocation.equals("")) {
					webDriver = new FirefoxDriver();
	
				} else {
					File pathToFirefoxBinary = new File(
							browserLocation.replace("\\", "\\\\"));
					FirefoxBinary firefoxbin = new FirefoxBinary(
							pathToFirefoxBinary);
					webDriver = new FirefoxDriver(firefoxbin, null);
	
				}
			} else if (browserName.toLowerCase().contains("chrome")) {
				if(GlobalInfo.os.contains("mac")){
					//System.setProperty("webdriver.chrome.driver","//Applications//Google\\ Chrome.app//Contents//MacOS//Google\\ Chrome");
				}else if(GlobalInfo.os.contains("win")){

					System.setProperty("webdriver.chrome.driver",GlobalInfo.rootPath + sp +"bin"+sp + "tools" + sp
							+ "chromedriver.exe");

				}else{
					System.setProperty("webdriver.chrome.driver",GlobalInfo.rootPath + sp + "bin"+sp +"tools" + sp
							+ "chromedriver.exe");

				}

					webDriver=new ChromeDriver();

			}else if(browserName.toLowerCase().contains("safari")){
				System.setProperty("webdriver.safari.driver", browserLocation);
				webDriver = new SafariDriver();

			}
			return webDriver;
	}
	/**
	 * get the webDriver list
	 * 
	 */
	public List<WebDriver> getWebDriverList() {
		return webDriverList;
	}

	static String hostString="http://localhost:8080";

	public  String getOneProxy(String groupname){
		String url="";

		/** best proxy and good proxy*/
		//url = hostString+"/proxyip/rest/message/getonebestproxyip/"+groupname;

		if(null!=groupname&&groupname.trim()!=""){
			url = hostString+"/proxyip/rest/message/getonebestproxyip/"+groupname;
		}else{
			url = hostString+"/proxyip/rest/message/getonebestproxyip/null";
		}
		String result="";
		try {
			result= HttpSender.doGet4HttpClient(url,false).trim();
		} catch (Exception e) {

e.printStackTrace();
		}finally {

		}
		return result;
	}


	public void postproxyResult(String proxyip,String groupname,String issucess){
		String url = hostString+"/proxyip/rest/message/postgoodproxyip";
		String message=proxyip+":"+groupname+"-"+issucess;
		try{

			String result= HttpSender.doPostJsonRequest(url,message,"",false);
		}catch (Exception e){e.printStackTrace();}

	}
}
