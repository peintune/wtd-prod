/**
 * @Title: FileSearch.java 
 * @Package com.pub.WTD.util 
 * @Description: To do something
 * @author hekun 158109016@qq.com
 * @date 2014年10月17日 下午3:50:18 
 * @version V1.0   
 */
package com.pub.wtd.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hekun
 * 
 */
public class FileSearch{
	List<File> listFiles = new ArrayList<File>();
	String sp = System.getProperty("file.separator");

	public List<File> findFiles(String baseDirName) {

		if (!baseDirName.contains(":")) {
			baseDirName = GlobalInfo.rootPath + sp + baseDirName;
		}
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");
		}
		File tempFile;
		File[] files = baseDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			tempFile = files[i];
			if (tempFile.isDirectory()) {
				findFiles(tempFile.getAbsolutePath());
			} else if (tempFile.isFile()) {
				listFiles.add(files[i]);

			}
		}
		return listFiles;
	}
}
