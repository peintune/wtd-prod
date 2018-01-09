/**
* @Title: HomePagetest.java
* @Package com.pub.WTD.pages 
* @Description: home page
* @author hekun 158109016@qq.com
* @date 2014��7��3�� ����10:27:41 
* @version V1.0   
 */
package com.pub.wtd.pages;


import com.pub.wtd.common.BasePage;
import com.pub.wtd.common.SessionData;

/**
 * @author hekun<158109016@qq.com>
 *
 */
public class HomePagetest extends BasePage{
	/**
	 * @param sessionData
	 */
	public HomePagetest(SessionData sessionData){
		super(sessionData);
	}
	//go to home page
	public boolean goToPage(){
		
		webDriver.get(hostName);
		
		return true;
	}
}
