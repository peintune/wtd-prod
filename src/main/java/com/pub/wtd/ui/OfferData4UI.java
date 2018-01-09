/**
 * @Title: modifyConfig.java 
 * @Package com.pub.WTD.ui 
 * @Description: read config file and modify this config
 * @author hekun 158109016@qq.com
 * @date 2014��7��17�� ����11:06:11 
 * @version V1.0   
 */
package com.pub.wtd.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import com.pub.wtd.entities.CaseEntity;
import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun
 * 
 */
public class OfferData4UI {

	Document docConfig = null;
	Document docCaseList = null;
	List<String> allCaseList = new ArrayList<String>();
	String sp = System.getProperty("file.separator");
	/**
	 * constactor
	 */
	public  OfferData4UI() {
		String path = new File("").getAbsolutePath();// get the local project path

		GlobalInfo.rootPath = path;// the the static rootPath to the logcal
		// project path
		readCofigFile();
		readCaseListFile();
		allCaseList = findAllCommonCases();

	}

	/**
	 * read the config file
	 */
	private void readCofigFile() {
		try {
			SAXReader saxReader = new SAXReader();
			String sp = System.getProperty("file.separator");
			docConfig = saxReader.read(new File(GlobalInfo.rootPath + sp+
					"config"+sp+ "conf" + sp + "wtd.cfg.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * read the caseList file
	 */
	private void readCaseListFile() {
		try {
			SAXReader saxReader = new SAXReader();
			String sp = System.getProperty("file.separator");
			docCaseList = saxReader.read(new File(GlobalInfo.rootPath + sp+"config"+sp
					+ "caselist.cfg.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get the url from config file
	 */
	public String getUrl() {

		Element urlElement = (Element) docConfig
				.selectSingleNode("//host-url/url");
		return urlElement.attributeValue("name");
	}

	/**
	 * get the browser list from config file
	 */
	public HashMap<String, String> getBrowser() {
		List<Element> webDriveEles = docConfig.selectNodes("//Browser/*");
		HashMap browsers = new HashMap();
		for (Element e : webDriveEles) {
			String location = e.attributeValue("location");
			String browserName = e.getName();
			browsers.put(browserName, location);
		}
		return browsers;
	}

	/**
	 * get the emailList list from config file
	 */
	public List<String> getRecevier() {
		List<String> emails = new ArrayList<String>();// email array
		List<Element> emailElements = (List<Element>) docConfig
				.selectNodes("//MailReceptor/user");
		for (int i = 0; i < emailElements.size(); i++) {
			emails.add(emailElements.get(i).attributeValue("email"));
		}
		return emails;
	}

	/**
	 * get the caseList list from caseList file
	 */
	public List<String> getCaseList() {
		List<String> caseList = new ArrayList<String>();// email array
		List<Element> emailElements = (List<Element>) docCaseList
				.selectNodes("//CaseList/*");
		for (int i = 0; i < emailElements.size(); i++) {
			Element element=emailElements.get(i);
			caseList.add(element.attributeValue("name")+":"+element.attributeValue("module")+":"+element.attributeValue("type"));
		}
		return caseList;
	}

	/**
	 * find all cases from local file directory
	 */
	private List<String> findAllCommonCases() {
		String sp = System.getProperty("file.separator");
		String caseDirectory = GlobalInfo.rootPath + sp + "src" + sp + "cases";
		List<String> allCaseList = new ArrayList<String>();// cases array
		File dir = new File(caseDirectory);
		
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.getName().startsWith("TC_")) {
				String fileName = file.getName().split("\\.")[0];
				allCaseList.add(fileName);
			}
		}
		return allCaseList;
	}

	/**
	 * get All cases list
	 */
	public List<String> getAllCaseList() {
		return allCaseList;
	}

	/**
	 * set the url vaule into the config file
	 */
	public void setUrl(String url) {

		Element urlElement = (Element) docConfig
				.selectSingleNode("//host-url/url");
		urlElement.setAttributeValue("name", url);
	}

	/**
	 * set the receiver vaule into the config file
	 */
	public void setRecevier(String receivers) {
		String[] recevierArry=receivers.split("\n");
		List<Element> userElements = docConfig
				.selectNodes("//MailReceptor/user");
		Element userRoot = (Element) docConfig
				.selectSingleNode("//MailReceptor");
		for (Element e : userElements) {
			userRoot.remove(e);
		}
		for (String receiver : recevierArry) {
			if(null!=receiver&&receiver.contains("@")){
			Element e = userRoot.addElement("user");
			e.setAttributeValue("email", receiver);
			}
		}
	}

	/**
	 * set the browsers vaule into the config file
	 */
	public void setBrowsers(HashMap<String, String> browsers) {

		List<Element> browserElements = docConfig.selectNodes("//Browser/*");
		Element browserRoot = (Element) docConfig.selectSingleNode("//Browser");
		for (Element e : browserElements) {
			browserRoot.remove(e);
		}
		Iterator iterator = browsers.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			Element e = browserRoot.addElement((entry.getKey()).toString());
			e.setAttributeValue("location", (entry.getValue()).toString());
		}

	}

	/**
	 * set the CaseList vaule into the CaseList Value
	 */
	public void setCaseList(ObservableList<CaseEntity> caseEnties) {

		List<Element> caseListElements = docCaseList
				.selectNodes("//CaseList/*");
		Element caseListRoot = (Element) docCaseList
				.selectSingleNode("//CaseList");
		for (Element e : caseListElements) {
			caseListRoot.remove(e);
		}
		for(CaseEntity caseEntity:caseEnties){
			Element e = caseListRoot.addElement("testCase");
			e.setAttributeValue("name", caseEntity.getFirstName());
			e.setAttributeValue("module", caseEntity.getCaseModule());
			e.setAttributeValue("type", caseEntity.getCaseType());
		}
	}

	/**
	 * get all modules form all cases
	 */
	public List<String> getCaseModules() {
		List<String> moduleList = new ArrayList<String>();

		String caseDirectory = GlobalInfo.rootPath + sp + "src" + sp + "cases";
		File dir = new File(caseDirectory);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				moduleList.add(file.getName());
			}
		}
		return moduleList;

	}

	/**
	 * get cases by modules
	 */
	public List<String> getCaseByModules(String modules) {
		List<String> caseList = new ArrayList<String>();
		String[] modulesArray = modules.split(";");
		for (String module : modulesArray) {
			for (String caseName : allCaseList) {
				if (caseName.contains(module)) {
					caseList.add(caseName);
				}
			}
		}
		return caseList;

	}
	
	public void saveConfigFile(){
		if(null!=docConfig){
		OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");
		org.dom4j.io.XMLWriter xmlWriter;
		try {
			xmlWriter = new org.dom4j.io.XMLWriter( 
			        new FileOutputStream(new File(GlobalInfo.rootPath + sp+
							"config"+sp+ "conf" + sp + "wtd.cfg.xml")));
			xmlWriter.write(docConfig);
			xmlWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	
	public void saveCaseListFile(){
		if(null!=docCaseList){
		OutputFormat outFmt = new OutputFormat("\t", true, "UTF-8");
		org.dom4j.io.XMLWriter xmlWriter;
		try {
			xmlWriter = new org.dom4j.io.XMLWriter( 
			        new FileOutputStream(new File(GlobalInfo.rootPath + sp+"config"+sp
							+ "caselist.cfg.xml")));
			xmlWriter.write(docCaseList);
			xmlWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
}
