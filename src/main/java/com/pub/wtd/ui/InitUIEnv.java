/**
* @Title: initUIEnv.java 
* @Package com.pub.WTD.ui 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年10月17日 下午3:39:03 
* @version V1.0   
 */
package com.pub.wtd.ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.pub.wtd.util.FileSearch;
import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun<158109016@qq.com>
 *
 */
public class InitUIEnv {
	/**
	 * when the ui launch,find all case names and write to local file
	 */

	public InitUIEnv() {
		// TODO Auto-generated constructor stub
		String path = new File("").getAbsolutePath();// get the local project
		// path

		GlobalInfo.rootPath = path;// the the static rootPath to the logcal
		// project path
	}
	public void initalAllCase2Local() {
		initailCommonCase();
		initailInterfaceCase();
	}
	
	private void initailCommonCase() {

		String sp = System.getProperty("file.separator");
		File casefile = new File(GlobalInfo.rootPath + sp + "bin"+sp+ "tools" + sp
				+ "commonCase.txt");
		try {
			if (casefile.exists()) {
				casefile.delete();
			}

			String dataString = "";

			casefile.createNewFile();

			FileWriter fileWriter = new FileWriter(casefile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// init common case to local file
			
			List<File> commonCaseList=new FileSearch().findFiles("src"+sp+"cases");

			for (File file : commonCaseList) {
				if (file.getName().startsWith("TC_")) {
					String fileName = file.getName().split("\\.")[0];
					String caseModule = file.getParentFile().getName();
					dataString += fileName + ":" + caseModule + ":" + "Common"
							+ "\r";
				}
			}
			bufferedWriter.write(dataString);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
	private void initailInterfaceCase() {

		String sp = System.getProperty("file.separator");
		File casefile = new File(GlobalInfo.rootPath + sp +"bin"+sp+ "tools" + sp
				+ "interfaceCase.txt");
		try {
			if (casefile.exists()) {
				casefile.delete();
			}

			String dataString = "";

			casefile.createNewFile();

			FileWriter fileWriter = new FileWriter(casefile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// init common case to local file
			
			List<File> interfaceCaseList=new FileSearch().findFiles("cases"+sp+"interfaces");
			
			for (File file : interfaceCaseList) {
				if (file.getName().startsWith("TC_")) {
					String fileName = file.getName().split("\\.")[0];
					String caseModule = file.getParentFile().getName();
					dataString += fileName + ":" + caseModule + ":"
							+ "Interface" + "\r";
				}
			}
			bufferedWriter.write(dataString);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
}
