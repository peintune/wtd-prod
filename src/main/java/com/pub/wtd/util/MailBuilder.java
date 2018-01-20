/**
 * @Title: MailBuilder.java 
 * @Package com.pub.WTD.util 
 * @Description: new mail obj and input the content ,then send the mail
 * @author hekun 158109016@qq.com
 * @date 2014��7��9�� ����1:52:17 
 * @version V1.0   
 */
package com.pub.wtd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * @author hekun
 * 
 */
public class MailBuilder {

	protected Mail mail = new Mail();// new a mail objective

	private String sp = System.getProperty("file.separator");// separator

	private String from = "AutoTest_QA@163.com";

	private List<String> receptions =new ArrayList<String>();

	private String content = "";

	private String proxy="";

	private String mailSubject="";

	/**
	 * generate the resutl.html file
	 * 
	 */
	private String getResultHtml() {
		return GlobalInfo.rootPath + sp+"bin"+sp + "resultHtml" + sp + UUID.randomUUID()
				+ ".html";
	}

	/**
	 * transfer the allResult.xml file and xsl file to a html file,and then get
	 * the content
	 * 
	 */
	public void setContent(Reader contentReader, String template)
			throws Exception {
		String outputName = getResultHtml();

		OutputStream out = null;

		StreamSource streamSrc = new StreamSource(GlobalInfo.rootPath +sp+"config"+ sp
				+ template);

		// StreamSource streamSrc = new StreamSource(template);
		TransformerFactory tFactory = TransformerFactory.newInstance();

		try {
			Templates tpl = null;
			if (tpl == null) {
				tpl = tFactory.newTemplates(streamSrc);
			}

			Transformer trans = tpl.newTransformer();

			trans.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING,
					"UTF-8");
			out = new FileOutputStream(outputName);

			StreamSource inStream = new StreamSource(contentReader);
			StreamResult outStream = new StreamResult(out);
			trans.transform(inStream, outStream);

			inStream = null;
			outStream = null;
			tpl = null;
			trans = null;
			tFactory = null;
			out.close();

			contentReader.close();
			contentReader = null;

			contentReader = new InputStreamReader(new FileInputStream(
					outputName), "UTF-8");
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} finally {
			out.close();

			tFactory = null;
			out.close();
		}

		StringBuilder sb = new StringBuilder();
		char[] buffer = new char[10240];
		int rd;
		while ((rd = contentReader.read(buffer)) != -1) {
			sb.append(buffer, 0, rd);
		}
		content = sb.toString();
		contentReader.close();
		contentReader = null;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setReceptions(List<String> receptions) {
		this.receptions = receptions;
	}

	public void setProxy(String proxy){
		this.proxy = proxy;
	}

	public void setMailSubject(String mailSubject){
		this.mailSubject = mailSubject;
	}

	/**
	 * send the mail
	 */
	public void doSendMail() {
		mail.setFrom(from);
		mail.setReceptions(receptions);
		mail.setProxy(proxy);
		mail.setContent(content);
		mail.setMailSubject(mailSubject);
		mail.doSend();
	}

}
