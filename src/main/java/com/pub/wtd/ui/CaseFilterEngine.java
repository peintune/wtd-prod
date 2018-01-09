/**
* @Title: CaseFilter.java 
* @Package com.pub.WTD.ui 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年10月16日 下午3:11:03 
* @version V1.0   
 */
package com.pub.wtd.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.pub.wtd.entities.CaseEntity;
import com.pub.wtd.util.GlobalInfo;

/**
 * @author hekun<158109016@qq.com>
 *
 */
public class CaseFilterEngine {

	
	public CaseFilterEngine(){
		
	}
	public HashMap<String,HashSet<String>> getCaseModules() {
		List<File> listFiles = new ArrayList<File>();
		String toolFolderString="tools";
		HashMap<String,HashSet<String>> caseFrameMap=new HashMap<>(); 
		String sp = System.getProperty("file.separator");	
		toolFolderString = GlobalInfo.rootPath + sp +"bin"+sp + toolFolderString;
	
			File baseDir = new File(toolFolderString);
			File tempFile;
			File[] files = baseDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				tempFile = files[i];
				if (!tempFile.isDirectory()&&tempFile.getName().contains("Case.txt")) {
					
					try {
						  InputStreamReader read = new InputStreamReader(
				                    new FileInputStream(tempFile));
						  	BufferedReader br=new BufferedReader(read);
						  	String singeCase="";
							HashSet<String> hashSet=new HashSet<>();
						while(null!=(singeCase=br.readLine())){
							hashSet.add(singeCase.split(":")[1]);
						}
						caseFrameMap.put(tempFile.getName().split("Case.t")[0], hashSet);
						br.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					listFiles.add(files[i]);
				}
			}
	return caseFrameMap;
	}
	
	public List<CaseEntity> getAllInterfaceCases() {
		List<CaseEntity> listCaseEntities=new ArrayList<CaseEntity>();
		CaseEntity caseEntity=new CaseEntity();
		String sp = System.getProperty("file.separator");
		String caseDirectory = GlobalInfo.rootPath + sp + "cases"+sp+"interfaces";
		File dir = new File(caseDirectory);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.getName().startsWith("TC_")) {
				String fileName = file.getName().split("\\.")[0];
				caseEntity.setCaseName(fileName);
				caseEntity.setCaseModule(file.getParentFile().getName());
				caseEntity.setCaseType("Interface");
				listCaseEntities.add(caseEntity);
			}
		}
		return listCaseEntities;
	}
	
	public List<CaseEntity> getAllCommonCases() {
		List<CaseEntity> listCaseEntities=new ArrayList<CaseEntity>();
		CaseEntity caseEntity=new CaseEntity();
		String sp = System.getProperty("file.separator");
		String caseDirectory = GlobalInfo.rootPath + sp + "src" + sp + "cases";
		File dir = new File(caseDirectory);
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.getName().startsWith("TC_")) {
				String fileName = file.getName().split("\\.")[0];
				caseEntity.setCaseName(fileName);
				caseEntity.setCaseModule(file.getParentFile().getName());
				caseEntity.setCaseType("Common");
				listCaseEntities.add(caseEntity);
			}
		}
		return listCaseEntities;
	}

	public HashSet<?> getCasesByModule(String caseType,String caseModule){
		String sp = System.getProperty("file.separator");	
		String caseFileString = GlobalInfo.rootPath + sp + "bin"+sp+ "tools"+sp+caseType+"Case.txt";
		HashSet<String> caseSet=new HashSet<>();
		File caseFile = new File(caseFileString);	
				try {
					  InputStreamReader read = new InputStreamReader(
			                    new FileInputStream(caseFile));
					  	BufferedReader br=new BufferedReader(read);
					  	String singeCase="";
					while(null!=(singeCase=br.readLine())){
						String[] caseArray=singeCase.split(":");
						if(caseArray[1].equalsIgnoreCase(caseModule)){
							caseSet.add(caseArray[0]);
						}
					}
					br.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				return caseSet;
	}
		
}


