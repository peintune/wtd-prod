/**
å * @Title: BaseCase.java 
 * @Package com.pub.WTD.common 
 * @Description: Support Base function to sub-cases
 * @author hekun 158109016@qq.com
 * @date 2014��7��3�� ����10:28:56 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import com.pub.wtd.dao.SmsDao;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public abstract class BaseCase extends BaseAction{

	protected String result = "pass";
	protected String description = "Default Description";
	protected String message = "Default message";
	protected String caseName;
	protected String logFolder;

	/**
	 * sub-class must override this function , this function is where the case
	 * execution entrance
	 */
	public abstract void execute() throws Exception;

	/**
	 * initial the SessionData
	 */
	public void initialSessionData(SessionData sessionData) {
		this.sessionData=sessionData;
		wtdGlobal = new WTDGlobal();// initial the Global Objective
		webDriver = sessionData.getWebDriver();
		hostName = sessionData.getHostName();
		logger = sessionData.getLogger();
	}
	/**
	 * get the SessionData
	 */
	public SessionData getSessionData() {
		return sessionData;
	}
	
	public void setSessionData(SessionData sessionData) {
		this.sessionData=sessionData;
		this.webDriver=sessionData.getWebDriver();
		this.hostName=sessionData.getHostName();
		this.logger=sessionData.getLogger();
	}
	public String getTestResult() {
		return result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTestResult(String result) {
		this.result = result;
	}

	public void setErrorInfo(String message) {
		this.message = message;
	}

	public String getErrorInfo() {
		return message;
	}

	/**
	 * send sms to the phone 
	 * @return String:the receive message
	 */
	public String sendSms(String phoneNum,String inputId,String clickBtId){
		SmsDao smD=new SmsDao(sessionData);
		String authCod="";
		long beforeExecute=smD.getLatestId();
		if(!sendKeysById(inputId,phoneNum)){
			logger.error("input the phone number failed");
		}
		if(!clickById(clickBtId)){
			logger.error("click the send message button failed");
		}
		ResultSet rs=smD.getLatestRecord();
		
		int j=0;
		while(j<30){
		try {
			if(beforeExecute<Long.parseLong(rs.getString("iAutoID"))){
				for(int i=0;i<=Long.parseLong(rs.getString("iAutoID"))-beforeExecute;i++){
					ResultSet rs1=smD.executeSql("select * from  t_sms where iAutoID="+beforeExecute+i+1);
					if(rs1.getString("sCellPhone").equalsIgnoreCase(phoneNum)){
						
						String message=rs1.getString("sMessage");
								
						authCod=message.substring(message.indexOf("��֤���ǣ�"),message.indexOf("��֤���ǣ�")+6);
						break;
						
								}
				}
			
			}
			Thread.sleep(2000);
			j++;
		} catch (Exception e) {
			logger.info("Exception",e);
		} 
		
		}
		smD.closeConnection();
		return authCod;
	}
	/**
	 * send sms to the phone 
	 * @return String:the receive message
	 */
	public String getSms(String phoneNum){
		SmsDao smD=new SmsDao(sessionData);
		String authCod="";
		long beforeExecute=smD.getLatestId();
	
		ResultSet rs=smD.getLatestRecord();
		
		int j=0;
		while(j<30){
			j++;
		try {
			if(beforeExecute<Long.parseLong(rs.getString("iAutoID"))){
				for(int i=0;i<=Long.parseLong(rs.getString("iAutoID"))-beforeExecute;i++){
					ResultSet rs1=smD.executeSql("select * from  t_sms where iAutoID="+beforeExecute+i+1);
					if(rs1.getString("sCellPhone").equalsIgnoreCase(phoneNum)){
						
						String message=rs1.getString("sMessage");
								
						authCod=message.substring(message.indexOf("��֤���ǣ�"),message.indexOf("��֤���ǣ�")+6);
						break;
						
								}
				}
			
			}
			Thread.sleep(2000);
		
		} catch (Exception e) {
			logger.info("Exception",e);
		} 
		
		}
		smD.closeConnection();
		return authCod;
	}
	/**
	 * Set the each test point result,description,name,errorInfo to the
	 * result.xml and Log file.
	 */
	protected void setPointResult(String name, String description,
			String result, String errorInfo) {

		String resultFile = sessionData.getLogFolder()
				+ System.getProperty("file.separator") + "result.xml";// generate
																		// the
																		// result.xml

		String temString = result.toLowerCase().equals("pass") ? "" : " {"
				+ errorInfo + "}";// if result pass don't print errorInfo

		sessionData.getLogger().info(
				"~~~~~~ Test Point 【 " + name + " 】  : (" + description
						+ ")  is 【 " + result + " 】 " + temString);

		// ----->>>>write information of test point to result.xml
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(resultFile));
			Element root = document.getRootElement();
			List<Element> elements = document.selectNodes("//TestCase[@name='"
					+ sessionData.getCaseName() + "']");
			for (Element el : elements) {
				Element temEl = el.addElement("TestPoint");
				temEl.addAttribute("name", name);
				temEl.addAttribute("caseType", "common");
				temEl.addAttribute("result", result);
				temEl.addAttribute("description", description);
				temEl.addAttribute("errorInfo", errorInfo);
			}

			OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");
			org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter( 
                    new FileOutputStream(resultFile));
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			sessionData.getLogger().error(
					" ****** Modify Result XML Failed ******** ");
			sessionData.getLogger().error("errorInfo", e);
		}

		// -----<<<<write information of test point to result.xml
	}

}
