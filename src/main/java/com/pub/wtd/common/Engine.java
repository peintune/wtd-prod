/**
 * @Title: Engine.java 
 * @Package com.pub.WTD.common 
 * @Description: The Main Engine Class to run case,log the log,send email.
 * @author hekun 158109016@qq.com
 * @date 2014��7��4�� ����10:33:56 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.pub.wtd.util.*;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.TTCCLayout;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.openqa.selenium.WebDriver;

import com.pub.wtd.entities.InterfaceParam;
import com.pub.wtd.entities.InterfaceTestEntity;
import com.pub.wtd.interfaceTest.InterfaceTestEngine;
import com.pub.wtd.pages.HomePagetestWithOutLogIn;
import com.pub.wtd.ui.GenerateTheRunningData;
import com.pub.wtd.ui.RunCaseBll;

import static com.pub.wtd.common.Main.initialEnv;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class Engine {
	String sp = System.getProperty("file.separator");// the separator
	String currentTestResult="PASS";
	String caseDescription="";
	String currentTestCase="";
	JavaCompilerTool javaCompilerTool ;
	public Engine(HashMap<String, String> webDriverList, List<String> caseList,
				  InitialEnvironment initialEnvx) {

		new Engine(webDriverList,  caseList,initialEnv,null);
	}


	/**
	 * The run cases entrance webDriverList: may include
	 * firefoxDriver,IEDriver,ChromeDriver.... caseList:contains all cases you
	 * want to run this time. initialEnv:all necessary variables begin the test
	 * must be set to this objective
	 */
	public Engine(HashMap<String, String> webDriverList, List<String> caseList,
			InitialEnvironment initialEnv,URLClassLoader urlClassLoader) {

		if(null!=urlClassLoader){
			javaCompilerTool=new JavaCompilerTool(urlClassLoader);
		}else{
			javaCompilerTool=new JavaCompilerTool(null);
		}

		combineAllPages();

		PropertyConfigurator.configure(GlobalInfo.rootPath + sp + "config"+sp+"conf" + sp
				+ "log4j.properties");
		
		/**
		 * Each webDriver will executed with all cases
		 */
		Iterator iter = webDriverList.entrySet().iterator();
		GenerateTheRunningData generateTheRunningData=new GenerateTheRunningData();
		RunCaseBll runCase=null;
		if(GlobalInfo.isLaunchByUI){
		 runCase=new RunCaseBll();//update the of case running state on UI
		}
		while(iter.hasNext()) {
			WebDriver webDriver;
			Map.Entry entry = (Map.Entry) iter.next();
			String browserName = (String) entry.getKey();
			String browserLocation ="";
			browserLocation= (String) entry.getValue();
			webDriver=new InitWebDriver().initWebDrvier(browserName, browserLocation);
			GlobalInfo.currentWebDriver=webDriver;
			generateTheRunningData.initialDataFile(webDriver);
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String logFolder = GlobalInfo.rootPath + sp +"bin"+sp+ "logs" + sp
					+ sf.format(date).replace(":", "_") + "_"
					+ webDriver.toString().split(":")[0];

			File testFolder = new File(logFolder);
			if (!testFolder.exists()) {
				testFolder.mkdir();
			}

			SessionData sessionData = initialEnv.getSessionData();
			GlobalInfo.HostName=sessionData.getHostName();
			//webDriver.manage().window().maximize();
			webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			sessionData.setWebDriver(webDriver);// set the webDriver and
												// transport it to cases.this
												// webDriver must be unique.

			/**
			 * find each cases and execute the execute function
			 */
			for (String singleCase : caseList) {
				if(GlobalInfo.isLaunchByUI){
					runCase.updateCaseNameText(singleCase);
				}
				Logger logger=null;
				try {

					String[] singleCaseArry = singleCase.split(":");
					if (singleCaseArry[2].equalsIgnoreCase("common")) {
						String className = singleCaseArry[0] + ".class";
						logger = Logger.getLogger(className);
					} else if (singleCaseArry[2].equalsIgnoreCase("interface")) {
						logger = Logger.getRootLogger();
					} else {
					
					}
					logger.info("###########################################################################################");
					logger.info("###################################### WTD Start ##########################################");
					logger.info("################## Start to Execute Case: 【 "
							+ singleCase.replace(":", "~") + " 】####################");

					currentTestCase=singleCase.replace(":", "~")+"~"+ logFolder + sp + singleCaseArry[2]+"_"+singleCaseArry[1]+"_"+singleCaseArry[0];
					if(GlobalInfo.isLaunchByUI){
					runCase.setCase(currentTestCase);
					runCase.updateCurrentCase();
					}
					/*
					 * run case
					 */

					if (singleCaseArry[2].equalsIgnoreCase("common")) {

						String className = singleCaseArry[0] + ".class";
						
						runCommonCases(singleCase, sessionData, logger,
								logFolder);
					} else if (singleCaseArry[2].equalsIgnoreCase("interface")) {
						runInterfaceCases(singleCase, sessionData, logger, logFolder);
					} else {
					
					}


					
					
					logger.info("################## Finish to Execute Case: 【 "
							+ singleCase.replace(":", "~") + " 】####################");
					logger.info("#################### Test Result is : 【 "
							+ currentTestResult + " 】####################");
					logger.info("################## The Test Description is : 【 "
							+ caseDescription + " 】####################");
					logger.info("######################################  WTD Finished ######################################");
					logger.info("###########################################################################################");
					logger.info("###########################################################################################");
					logger.info("###########################################################################################");

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.info("########### [ Error ]: Run Case Faild ,Exception Ocurrs !!!!!!###########");
					logger.info("Exception", e);
				}
				
				if(GlobalInfo.isLaunchByUI){
					generateTheRunningData.createData(currentTestCase,currentTestResult);//generate the local case running data
		
					runCase.setResult(currentTestResult);
					runCase.updateCompleteUI();
				}
			}

			tearDown(logFolder, webDriver, initialEnv);// clear
																			// and
																			// tear
																			// down
																			// the
																			// environment
		}
		clearEnv();
	}
	
	
	/**
	 * run the interface cases
	 * @throws Exception 
	 */
	private void runInterfaceCases(String caseName, SessionData sessionData,Logger logger
			,String logFolder) throws Exception{



		String[] singleCase=caseName.split(":");
		
		
		try {
			sessionData.setLogger(logger);
			
			sessionData.setCaseName(singleCase[0]);
			
			String caseLogFolder = logFolder + sp + singleCase[1]+"_"+singleCase[0];

			File caseFolder = new File(caseLogFolder);
			if (!caseFolder.exists()) {
				caseFolder.mkdir();
			}

			sessionData.setLogFolder(caseLogFolder);

			SAXReader saxReader = new SAXReader();
			String sp = System.getProperty("file.separator");
			String caseFolderString=GlobalInfo.rootPath + sp+ "wtdapicases" + sp +"interfaces";
			String[] caseArray=caseName.split(":");
			Document doc = saxReader.read(new File(caseFolderString+sp+caseArray[1]+sp+caseArray[0]+".xml"));
			
			Element caseElem=(Element)doc.selectSingleNode("//case");
			String caseDescription=caseElem.attributeValue("description");
			String contentType=caseElem.attributeValue("contenttype");

			if(null == contentType)contentType="";

			String sendType=caseElem.attributeValue("type");
			
			List<Element> globalVaribles=caseElem.selectNodes("//setGlobalVarible");
			
			
			Element caseApiElement=(Element)caseElem.selectSingleNode("testApi");
			String testApi=caseApiElement.attributeValue("api");
			String realApiUrl="";
			String localUrl=sessionData.getHostName();
			//mark by kun 2017-1-21
//			if(!localUrl.contains("mainhost.com")){	
//			//String localUrl=sessionData.getHostName();
//			String[] testApiArray=testApi.split("://");
//			String[] localUrlArray=localUrl.split("://");
//			String[] tempApiArray=testApiArray[1].split("/");
//			String[] tempApiArray2=tempApiArray[0].split("\\.");
//			String[] templocalArray2=localUrlArray[1].split("\\.");
//			realApiUrl=localUrlArray[0]+"://"+tempApiArray2[0];
//			for(int i=1;i<templocalArray2.length;i++){
//				realApiUrl+="."+templocalArray2[i];
//			}
//			for(int i=1;i<tempApiArray.length;i++){
//				realApiUrl+="/"+tempApiArray[i];
//			}
//			}else{
//				realApiUrl=testApi;
//			}
			
			realApiUrl=testApi;

			
			List<Element> tests=caseElem.selectNodes("test");
			List<InterfaceTestEntity> interfaceTests=new ArrayList<InterfaceTestEntity>();
			for(Element e:tests){
				List<Element> paramElems = e.selectNodes("parameter");

				InterfaceTestEntity interfaceTestEntity=new InterfaceTestEntity();
				List<Element>  bodyElems = e.selectNodes("body");
				if(bodyElems.size()>0){
					Element el = bodyElems.get(0);
					String body = el.attributeValue("value");
					interfaceTestEntity.setBody(body);
				}
				List<InterfaceParam> parrams=new ArrayList<InterfaceParam>();
				for (Element el : paramElems) {
					InterfaceParam paramEntity=new InterfaceParam();
					String isRandom="false";
					if(null!=el.attributeValue("isRandom")){
						isRandom=el.attributeValue("isRandom");
						}
					paramEntity.setName(el.attributeValue("name"));
					paramEntity.setRandom(isRandom.equalsIgnoreCase("true"));
					paramEntity.setValue(el.attributeValue("value"));
					parrams.add(paramEntity);
			}
				interfaceTestEntity.setExpectContansString(e.attributeValue("expectContansString"));
				interfaceTestEntity.setInterfaceParams(parrams);
				interfaceTestEntity.setName(e.attributeValue("name"));	
				interfaceTestEntity.setPreExcution(e.attributeValue("preExcution"));
				interfaceTestEntity.setContentType(contentType);
				interfaceTests.add(interfaceTestEntity);
			}
			FileAppender appender = new FileAppender(new TTCCLayout(),
					caseLogFolder + sp +singleCase[0] + ".log", false);
			logger.addAppender(appender);
			generateResultXml(caseFolder.toString(), logger, caseName);
			

			
			InterfaceTestEngine interfaceTestEngine=new InterfaceTestEngine(realApiUrl, interfaceTests, sendType, logger,globalVaribles);
			setInterfaceTestCaseResult(caseFolder.toString(), sessionData,
					singleCase, interfaceTestEngine.getTestResult());
			//generateResultXml(caseFolder.toString(), logger, singleCase[0]);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	
		try {
	
		//new ScreenCapture(sessionData);// catpure the screen after
										// case execution finished

	//	setTestCaseResult(caseFolder.toString(), logger,
		//		singleCase, testObj);
		} catch(Exception e){
			throw new Exception(e.toString());
		}
	}
	
	/**
	 * run the common cases
	 * @throws Exception 
	 */
	private void runCommonCases(String caseName, SessionData sessionData,Logger logger
			,String logFolder) throws Exception{


		// 1, --->>>find the java class which is matching to the
		// case name
		String[] singleCase=caseName.split(":");

		String caseJaveFile=GlobalInfo.rootPath+sp+"wtdwebuicases"+sp+singleCase[1]+sp+singleCase[0]+".java";

		boolean status = javaCompilerTool.comiple(GlobalInfo.rootPath+sp+"bin"+sp+"webuicaseclasses",caseJaveFile);

		Class cl =null;

		try {
			if(status) {
				try {

						cl = javaCompilerTool.getClassLoader().loadClass(singleCase[1]+"."+singleCase[0]);
				}catch (Exception e2){
					e2.printStackTrace();
				}
			}
		
		// ---<<<find the java class which is matching to the case
		// name	

			
		sessionData.setLogger(logger);
			
		sessionData.setCaseName(singleCase[0]);
		
		String caseLogFolder = logFolder + sp + singleCase[2]+"_"+singleCase[1]+"_"+singleCase[0];

		File caseFolder = new File(caseLogFolder);
		if (!caseFolder.exists()) {
			caseFolder.mkdir();
		}

		sessionData.setLogFolder(caseLogFolder);

		// 2,--->>>find the constructor of this case
			BaseCase testObj=null;
			 if(cl!=null) {
				Constructor strCtor = cl.getConstructor();
				// ---<<<find the constructor of this case

				// 3,--->>>new a new case objective by the constructor
				 testObj = (BaseCase) strCtor
						.newInstance();
				testObj.initialSessionData(sessionData);
			}
		// ---<<<<new a new case objective by the constructor

		FileAppender appender = new FileAppender(new TTCCLayout(),
				caseLogFolder + sp +singleCase[0] + ".log", false);
		logger.addAppender(appender);

		generateResultXml(caseFolder.toString(), logger, caseName);// generate
																		// the
																		// result.xml
																		// file
																		// for
																		// each
																		// cases
		//new HomePagetestWithOutLogIn(sessionData).goToPage();

		try {

			// 4,--->>>run execute() function,through this
			// function,case will be launched
			if(testObj!=null) {
				testObj.execute();
			}else{
				testObj=new VirtualBaseCase();
				testObj.setTestResult("block");// if found exception,set
				testObj.setErrorInfo("编译case出错，请检查case的代码的语法错误,case 路径："+caseJaveFile);
				logger.error("编译case出错，请检查case的代码的语法错误，case 路径："+caseJaveFile);
			}
			// ---<<<run execute() function,through this
			// function,case will be launched

		} catch (Exception e) {
			testObj.setTestResult("block");// if found exception,set
											// the case result to
											// block
			//new ScreenCapture(sessionData);
			testObj.setErrorInfo(e.getMessage());
			logger.info("errorStack", e);
		}finally{
			try{
				if(testObj!=null) {
					new ScreenCapture(sessionData);
					caseDescription = testObj.getDescription();
					currentTestResult = testObj.getTestResult();
				}else{
					caseDescription="";
					currentTestResult="";
				}
			}catch(Exception e){
			}

		}

		setCommonTestCaseResult(caseFolder.toString(), sessionData,
				singleCase, testObj);
		} catch(Exception e){
			throw new Exception(e.toString());
		}
	}
	/**
	 * generate the result.xml for each cases
	 */
	@SuppressWarnings("deprecation")
	private String generateResultXml(String parentFolder, Logger logger,
			String caseName) {
		String[] singelCase=caseName.split(":");
		String fileName = parentFolder + System.getProperty("file.separator")
				+ "result.xml";

		try {
			Document document = DocumentHelper.createDocument();

			Element root = document.addElement("TestResult");
			Element testCase = root.addElement("TestCase");
			testCase.addAttribute("name", singelCase[0]);
			testCase.addAttribute("caseModule", singelCase[1]);
			testCase.addAttribute("caseType", singelCase[2]);
			testCase.addAttribute("description", "");
			testCase.addAttribute("result", "");
			testCase.addAttribute("errorInfo", "");

			OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");

			org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter( 
                    new FileOutputStream(fileName));
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			logger.error(" ****** Create Result XML Failed ******** ");
			logger.error("errorInfo", e);
		}
		return fileName;

	}

	/**
	 * set the Test case result pass by all test point is pass
	 */
	private void setCommonTestCaseResult(String parentFolder, SessionData sessionData,
			String[] caseName, BaseCase bs) {
		String resultFile = parentFolder + System.getProperty("file.separator")
				+ "result.xml";
		String singleTestFolder=parentFolder.replace(sp+caseName[2]+"_"+caseName[1]+"_"+caseName[0], "");
		String[] temArray=singleTestFolder.split("\\\\");
	
		singleTestFolder=temArray[temArray.length-1];
		
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(resultFile));
			Element root = document.getRootElement();
			Element element = (Element) document.selectSingleNode("//TestCase[@name='"
					+ caseName[0] + "' and @caseType='"+caseName[2]+"' and @caseModule='"+caseName[1]+"']");


				List<Element> testPointElements = element.selectNodes("TestPoint");
				List<Element> testPointPASS = element
						.selectNodes("TestPoint[@result='pass']");
				
				if(bs.getTestResult().equalsIgnoreCase("block")){
					element.setAttributeValue("result", "block");
					bs.setTestResult("block");
				}else{
				if (testPointElements.size() != testPointPASS.size()) {
					element.setAttributeValue("result", "fail");
					bs.setTestResult("fail");
				} else {
					element.setAttributeValue("result", "pass");
				}
				element.setAttributeValue("description", bs.getDescription());
				
			}
				element.setAttributeValue("errorInfo", bs.getErrorInfo());
				
				element.setAttributeValue("logUrl",sp+sp+ GlobalInfo.pcIP+sp+GlobalInfo.pcHostName+sp+singleTestFolder+sp+caseName[2]+"_"+caseName[1]+"_"+caseName[0]);
				element.setAttributeValue("webBrowser", sessionData.getWebDriver().toString().split(":")[0]);
			
			currentTestResult=bs.getTestResult();
			OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");
			org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter( 
                    new FileOutputStream(resultFile));
			xmlWriter.write(document);
			xmlWriter.close();

		} catch (Exception e) {
			sessionData.getLogger().error(" ****** Modify Result XML Failed ******** ");
			sessionData.getLogger().error("errorInfo", e);
		}
	}

	/**
	 * set the Test case result pass by all test point is pass
	 */
	private void setInterfaceTestCaseResult(String parentFolder, SessionData sessionData,
			String[] caseName, List<List<String>> testResult) {
		String resultFile = parentFolder + System.getProperty("file.separator")
				+ "result.xml";
		String singleTestFolder=parentFolder.replace(sp+caseName[2]+"_"+caseName[1]+"_"+caseName[0], "");
		String[] temArray=singleTestFolder.split("\\\\");
	
		singleTestFolder=temArray[temArray.length-1];
		
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(new File(resultFile));
			Element root = document.getRootElement();

			Element element = (Element) document.selectSingleNode("//TestCase[@name='"
					+ caseName[0] + "' and @caseType='"+caseName[2]+"' and @caseModule='"+caseName[1]+"']");
			int i=1;
			currentTestResult="pass";
			for (List<String> caseResult : testResult) {
				Element temEl = element.addElement("TestPoint");
				temEl.addAttribute("name", "Test "+i);
				temEl.addAttribute("caseType", "interface");
				temEl.addAttribute("result", caseResult.get(2));
				temEl.addAttribute("expectString", caseResult.get(3));
				temEl.addAttribute("response", caseResult.get(1));
				temEl.addAttribute("description", "");
				temEl.addAttribute("testContent", caseResult.get(0));
				if(!caseResult.get(2).contains("pass")){
					temEl.addAttribute("errorInfo", caseResult.get(1));
					currentTestResult="fail";
				}else{
					temEl.addAttribute("errorInfo", "");
				}
				i++;
			}
			
			element.setAttributeValue("errorInfo", "");
			element.setAttributeValue("result",currentTestResult);
			element.setAttributeValue("logUrl",sp+sp+ GlobalInfo.pcIP+sp+GlobalInfo.pcHostName+sp+singleTestFolder+sp+caseName[2]+"_"+caseName[1]+"_"+caseName[0]);
			element.setAttributeValue("webBrowser", sessionData.getWebDriver().toString().split(":")[0]);
			OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");
			org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter( 
                    new FileOutputStream(resultFile));
			xmlWriter.write(document);
			xmlWriter.close();

		} catch (Exception e) {
			sessionData.getLogger().error(" ****** Modify Result XML Failed ******** ");
			sessionData.getLogger().error("errorInfo", e);
		}
	}
	
	/**
	 * merge all result.xml to allResult.xml
	 */
	private void mergeAllResult(String logFolder) {
		String fileName = logFolder + System.getProperty("file.separator")
				+ "allResult.xml";

		try {
			Document document = DocumentHelper.createDocument();

			Element root = document.addElement("TestResult");
			List<File> listFiles = new ArrayList<File>();
			List<File> lists = search(logFolder, listFiles);
			for (File f : lists) {
				SAXReader reader = new SAXReader();
				Document tmpDoc = reader.read(f);
				List<Element> elements = tmpDoc.selectNodes("//TestResult");
				for (Element e : elements) {
					root.appendContent((Branch) e.clone());
				}

			}
			OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");

			org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter( 
                    new FileOutputStream(fileName));
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * search all the result.xml files
	 */
	private List<File> search(String folder, List<File> listFiles) {
		File[] files = new File(folder).listFiles();

		if (files == null) {
			return null;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				search(file.toString(), listFiles);
			} else {
				if (file.getName().toLowerCase().equalsIgnoreCase("result.xml")) {
					listFiles.add(file);
				}
			}

		}
		return listFiles;
	}

	/**
	 * merge result.xml and call send test report
	 */
	private void tearDown(String logFolder, WebDriver webDrvier,InitialEnvironment initialEnv) {
		try {
			webDrvier.close();
			webDrvier.quit();
		}catch (Exception ignore){}

		mergeAllResult(logFolder);


		sendReport(logFolder + "/allResult.xml", initialEnv.getMailReceptors() ,initialEnv.getProxy(),initialEnv.getMailSubject(),initialEnv.getMailSender());


		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * clear the evn
	 */
	private void clearEnv(){
		try {
			Thread.sleep(3000);// sleep 3s
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!GlobalInfo.isLaunchByUI){
			System.exit(0);
		}
	}
	/**
	 * send the test report
	 */
	private void sendReport(String allResultXml, List<String> receptions,String proxy,String mailSubject,HashMap<String,String> mailSender) {
		MailBuilder sm = new MailBuilder();

		sm.setReceptions(receptions);
		sm.setProxy(proxy);
		sm.setMailSubject(mailSubject);
		sm.setMailSender(mailSender);

		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(allResultXml),
					"UTF-8");

			sm.setContent(isr, "conf//report.xsl");
			sm.doSendMail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void combineAllPages(){
		File pagesFolder = new File(GlobalInfo.rootPath+sp+"wtdwebuicases"+sp+"pages");

		File[] files = pagesFolder.listFiles();

		for(File file:files){
			if(file.getName().endsWith(".java")){
				javaCompilerTool.comiple(GlobalInfo.rootPath+sp+"bin"+sp+"webuicaseclasses",file.getAbsolutePath());
			}
		}


	}
}