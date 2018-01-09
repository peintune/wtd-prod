/**
 * @Title: FindCaseNames.java 
 * @Package com.pub.WTD.common 
 * @Description: find cases from casList.cfg.xml
 * @author hekun 158109016@qq.com
 * @date 2014��7��3�� ����4:19:12 
 * @version V1.0   
 */
package com.pub.wtd.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class FindCaseNames {

	/**
	 * find cases from casList.cfg.xml
	 * 
	 */
	public List<String> findCaseList() {
		List<String> caseList = new ArrayList<String>();
		SAXReader saxReader = new SAXReader();
		Document doc = null;
		HashMap<String, String> hash = new HashMap<String, String>();
		try {
			String sp = System.getProperty("file.separator");
			doc = saxReader.read(new File(GlobalInfo.rootPath + sp+"config"+sp
					+ "caselist.cfg.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		List<Element> caseElList = doc.selectNodes("//CaseList/*");
		for (Element e : caseElList) {
			String caseName = e.attributeValue("name");
			String module= e.attributeValue("module");
			String type= e.attributeValue("type");
			caseList.add(caseName+":"+module+":"+type);
		}
		return caseList;
	}
}
