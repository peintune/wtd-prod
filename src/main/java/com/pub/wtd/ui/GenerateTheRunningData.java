/**
* @Title: initUIEnv.java 
* @Package com.pub.WTD.ui 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年10月17日 下午3:39:03 
* @version V1.0   
 */
package com.pub.wtd.ui;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.openqa.selenium.WebDriver;

import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun<158109016@qq.com>
 *
 */
public class GenerateTheRunningData {
	/**
	 * when the case running , generate a file to save the runing data
	 */

	String sp = System.getProperty("file.separator");

	/**
	 * write the case data to the file
	 */
	public void createData(String caseName,String result){

		File casefile = new File(GlobalInfo.rootPath + sp + "bin"+sp+"tools" + sp
				+ "runningData.txt");
		try {
			if (!casefile.exists()) {
				casefile.createNewFile();

			}
			BufferedWriter 	fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(casefile, true), "UTF-8")); 
			fw.append(caseName+"~"+result);
			fw.newLine();
			fw.flush(); 
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	/**
	 * get case counts by case type
	 */
	private int getCaseNumByType(String type){
		File caseStoreFile = new File(GlobalInfo.rootPath + sp + "bin"+sp+"tools" + sp
				+ type+"Case.txt");
		int totalNumber=0;
		try {
			BufferedReader 	reader = new BufferedReader(new InputStreamReader(new FileInputStream(caseStoreFile), "UTF-8"));
			String str = "";
			while(null!=(str = reader.readLine())){
				totalNumber++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalNumber;
	}
	
	/**
	 * initial each webdriver data store file
	 */
	public void initialDataFile(WebDriver webDriver){
		File casefile = new File(GlobalInfo.rootPath + sp + "bin"+sp+"tools" + sp
				+ "runningData.txt");
		if(casefile.exists()){
			casefile.delete();
		}
		try {
			casefile.createNewFile();
			BufferedWriter 	fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(casefile, true), "UTF-8")); 
			fw.append(webDriver.toString());
			fw.newLine();
			fw.flush(); 
			fw.close();
			
			
			BufferedReader 	reader = new BufferedReader(new InputStreamReader(new FileInputStream(casefile), "UTF-8"));
			String str = null;
			int i=3;
			boolean isSetToltalNum=false;
			while (null!=(str = reader.readLine())&&i>0) {
				if(i==1&&str.contains("Total")){
					isSetToltalNum=true;
				}
				i--;
			}
		
			if(!isSetToltalNum){
				BufferedWriter 	fw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(casefile, true), "UTF-8")); 
				int commonTotalNum=this.getCaseNumByType("common");
				int interfaceTotalNum=this.getCaseNumByType("interface");
				int total=commonTotalNum+interfaceTotalNum;
				fw2.append("totalNum="+total);
				fw2.newLine();
				fw2.append("commonTotalNum="+commonTotalNum);
				fw2.newLine();
				fw2.append("interfaceTotalNum="+interfaceTotalNum);
				fw2.newLine();
				fw2.flush(); 
				fw2.close();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
