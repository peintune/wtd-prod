/**
 * @Title: WTDGlobal.java 
 * @Package com.pub.WTD.common 
 * @Description: The Global variables
 * @author hekun 158109016@qq.com
 * @date 2014��7��3�� ����10:45:30 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.util.Date;
import java.util.HashMap;


/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class WTDGlobal {
	public static final String MESSAGE_KEY = "WTD_Message";

	// the log Mark
	public static String getLogPREFIX() {

		return new Date().toString().substring(0, 19) + " ~~~~~~~~~~ ";
	}

	public static String getHostName() {

		return new Date().toString().substring(0, 19) + " ~~~~~~~~~~ ";
	}

	public static HashMap getResultMap() {
		HashMap<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("pass", "PASS");
		resultMap.put("fail", "FAIL");
		resultMap.put("block", "BLOCK");
		resultMap.put("followup", "FOLLOWUP");
		return resultMap;
	}
}
