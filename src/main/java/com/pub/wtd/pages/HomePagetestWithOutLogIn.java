/**
* @Title: HomePagetest.java
* @Package com.pub.WTD.pages 
* @Description: home page
* @author hekun 158109016@qq.com
* @date 2014��7��3�� ����10:27:41 
* @version V1.0   
 */
package com.pub.wtd.pages;

import org.openqa.selenium.By;

import com.pub.wtd.common.SessionData;

/**
 * @author hekun<158109016@qq.com>
 *
 */
public class HomePagetestWithOutLogIn extends HomePagetest {
	/**
	 * @param sessionData
	 */
	public HomePagetestWithOutLogIn(SessionData sessionData) {
		super(sessionData);
		// TODO Auto-generated constructor stub
	}


	/**
	 * go to the destination page
	 */
	public boolean goToPage(){
		
		webDriver.get(hostName);
		LogOut();
		return true;
	}
	
	/**
	 * log out after go to the home page
	 */
	public void LogOut(){
		if(!isTextExist("登录")){
			if (isExist(By.id("topLogout"))) {
				clickById("topLogout");
			}
		}			
	}	
}
