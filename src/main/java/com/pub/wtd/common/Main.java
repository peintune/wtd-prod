/**
 * @Title: MainFlow.java 
 * @Package com.pub.WTD.common 
 * @Description: The main entrance to this driver
 * @author hekun 158109016@qq.com
 * @date 2014��7��3�� ����1:33:32 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.List;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class Main implements Runnable{

	
	/**
	 * The main entrance to this driver
	 */
	static InitialEnvironment initialEnv;
	static List<String> caseList;
	static HashMap<String, String> configMap;
	private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();

	public static void main(String[] args) {
		initialEnv = new InitialEnvironment();
		configMap=initialEnv.getWebDrivers();//read all kinds of webdriver from config file
		caseList = new FindCaseNames().findCaseList();//read cases from config file
	//	webDriverList = new InitWebDriver(configMap).getWebDriverList();//initial all kinds of webdriver 
		runCase(classloader);
	}

	public static void start(URLClassLoader urlClassLoader){
		initialEnv = new InitialEnvironment();
		configMap=initialEnv.getWebDrivers();//read all kinds of webdriver from config file
		caseList = new FindCaseNames().findCaseList();//read cases from config file
		//	webDriverList = new InitWebDriver(configMap).getWebDriverList();//initial all kinds of webdriver

		runCase(urlClassLoader);
	}

	public static void runCase(URLClassLoader urlClassLoader) {
		//start to run the engine
		if(urlClassLoader!=null){
			Engine engine= new Engine(configMap, caseList, initialEnv,urlClassLoader);
		}else{
			Engine engine= new Engine(configMap, caseList, initialEnv);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		initialEnv = new InitialEnvironment();
		configMap=initialEnv.getWebDrivers();//read all kinds of webdriver from config file
		caseList = new FindCaseNames().findCaseList();//read cases from config file
		runCase(classloader);
	}
}