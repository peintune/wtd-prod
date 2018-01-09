/**
* @Title: InterfaceTestEngine.java 
* @Package com.pub.WTD.interfaceTest 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年8月21日 下午4:05:37 
* @version V1.0   
 */
package com.pub.wtd.interfaceTest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.dom4j.Element;

import com.pub.wtd.entities.InterfaceParam;
import com.pub.wtd.entities.InterfaceTestEntity;
import com.pub.wtd.util.GlobalInfo;
import com.pub.wtd.util.UrlGenerator;

/**
 * @author hekun
 *
 */
public class InterfaceTestEngine {

	String testApi = "";
	List<InterfaceTestEntity> interfaceTests = null;

	String checkString = "";
	String sendType = "post";
	Logger logger;
	List<List<String>> resultList=new ArrayList<List<String>>();
	List<Element> globalVaribles=null;
	UrlGenerator urlGenerator ;
	public InterfaceTestEngine(String testApi,
			List<InterfaceTestEntity> interfaceTests, String sendType,
			Logger logger,List<Element> globalVaribles) {
		this.globalVaribles=globalVaribles;
		this.testApi = testApi;
		this.sendType = sendType;
		this.interfaceTests = interfaceTests;
		this.logger = logger;
		runCase();
	}

	private void runCase() {
		int i=1;
		for (InterfaceTestEntity interfaceTest : interfaceTests) {
			List<String> singleResult=new ArrayList<>();
			List<InterfaceParam> paraList = interfaceTest.getInterfaceParams();
			urlGenerator = new UrlGenerator(testApi, interfaceTest);
			try {
				
				String response = urlGenerator.doTest(false,sendType);
				String result="pass";
				Pattern pat = Pattern.compile(interfaceTest.getExpectContansString());  
				//Pattern pat = Pattern.compile();  
				Matcher mat = pat.matcher(response);  
				boolean rs = mat.find();   
				if(rs){
					result="pass";
				}else{
					result="fail";
				}		
				if(response.contains(interfaceTest.getExpectContansString())){
					result="pass";
				}
				if(response.contains("Notice: Undefined index")&&response.contains("on line")){
					result="fail";
				}
				String replaceResponse="";
				if(response.length()>50){
					replaceResponse= response.substring(0,50)+"...";
				}else{
					replaceResponse=response;
				}
				singleResult.add(urlGenerator.getUrl());
				singleResult.add(response);
				singleResult.add(result);
				singleResult.add(interfaceTest.getExpectContansString());
				logger.info("【Test "+i+"】          "+urlGenerator.getUrl());
				logger.info("【Response "+i+"】     "+replaceResponse);
				logger.info("【expect words】     "+interfaceTest.getExpectContansString());
				logger.info("【Result "+i+"】       "+result);
				logger.info("............");
				i++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
			this.setVaribles();
			resultList.add(singleResult);
		}
	}
	
	/**
	 * @return return the result list 
	 */
	public List<List<String>> getTestResult() {
		return resultList;	
	}	

	/**
	 * @return the send type
	 */
	public String getSendType() {
		return sendType;
	}

	/**
	 * @param sendType
	 *            the send type to set
	 */
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	/**
	 * @return the testApi
	 */
	public String getTestApi() {
		return testApi;
	}

	/**
	 * @param testApi
	 *            the testApi to set
	 */
	public void setTestApi(String testApi) {
		this.testApi = testApi;
	}

	/**
	 * @return the checkString
	 */
	public String getCheckString() {
		return checkString;
	}

	/**
	 * @param checkString
	 *            the checkString to set
	 */
	public void setCheckString(String checkString) {
		this.checkString = checkString;
	}

	/**
	 * @return the paraList
	 */
	public List<InterfaceTestEntity> getParaList() {
		return interfaceTests;
	}

	/**
	 * @param interfaceTests
	 *            the paraList to set
	 */
	public void setParaList(List<InterfaceTestEntity> interfaceTests) {
		this.interfaceTests = interfaceTests;
	}
	
	/**
	 */
	private void setVaribles() {
		for (Element e : globalVaribles) {
			String varibleName = e.attributeValue("name");
			String varibleVaule = urlGenerator.getVarible(e
					.attributeValue("value"));
			if (!GlobalInfo.varibles.containsKey(varibleName)) {
				GlobalInfo.varibles.put(varibleName, varibleVaule);
			} else {
				GlobalInfo.varibles.replace(varibleName, varibleVaule);
			}

		}
	}

}
