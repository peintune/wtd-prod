/**
 * @Title: UrlGenerator.java 
 * @Package com.pub.WTD.util 
 * @Description: The tool class to generate url and send request
 * @author hekun
 * @date 
 * @version V1.0   
 */
package com.pub.wtd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.pub.wtd.entities.InterfaceParam;
import com.pub.wtd.entities.InterfaceTestEntity;

/**
 * @author hekun
 * 
 */
public class UrlGenerator {
	/**
	 * @return the String generate the default and right request url
	 */

	String testApi = "";

	 InterfaceTestEntity interfaceTest=null;
	String[] typeArray = { "int", "string" };
	boolean isRightUrl = true;
	String url="";
	String currentCookieString="";

	public UrlGenerator(String testApi, InterfaceTestEntity interfaceTest) {
		this.testApi = testApi;
		this.interfaceTest=interfaceTest;
	}

	/**
	 * @return the String the main method of send by Get type
	 * @throws Exception
	 */
	public String doGet(boolean isRandom, String testApi,
			InterfaceTestEntity interfaceTest) throws Exception {
		String requestUrl = testApi;
		BufferedReader in = null;
		String result = "";
		if (!isRandom) {
			requestUrl = generateRightUrl4Get(testApi,
					interfaceTest.getInterfaceParams());
		} else {
			requestUrl = generateRandomUrl4Get(testApi,
					interfaceTest.getInterfaceParams());
		}
		String cookieStrings = currentCookieString;
		HttpResponse response = doGet(requestUrl, cookieStrings);
		String cookies = getCookie(response);
		if (!GlobalInfo.varibles.containsKey("Cookie")) {
			GlobalInfo.varibles.put("Cookie", cookies);
		} else if(cookies.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookies);
		}
		Header[] headers;
		String referUrl = "";
		List<String> cookieList = new ArrayList<String>();
		boolean requestOk = true;
		if (response.getStatusLine().toString().contains("Moved Temporarily")) {
			requestOk = false;
		}
		while (!requestOk) {
			if (response.getStatusLine().toString()
					.contains("Moved Temporarily")) {
				headers = response.getAllHeaders();
				for (Header location : headers) {
					if (location.getName().equalsIgnoreCase("Set-Cookie")) {
						cookieList.add(location.getValue());
					}
				}
				for (Header location : headers) {
					if (location.getName().equalsIgnoreCase("location")) {
						referUrl = location.getValue();
						for (String cookie : cookieList) {
							//if (cookie.startsWith("guid=")|| cookie.startsWith("per_")|| cookie.startsWith("cityid")) {
								currentCookieString += cookie.split(";")[0]
										+ ";";
							//}

						}
						if (currentCookieString.endsWith(";")) {
							currentCookieString = currentCookieString
									.substring(0,
											currentCookieString.length() - 1);
						}

						response = doGet(referUrl, currentCookieString);
					}
				}
			} else {
				requestOk = true;
			}
		}

		in = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent()));
		StringBuffer sb = new StringBuffer("");
		String line = "";
		String NL = System.getProperty("line.separator");
		while ((line = in.readLine()) != null) {
			sb.append(line + NL);
		}
		in.close();
		result = sb.toString();
		return result;
	}
	
	
	/**
	 * @return the String the  method of send by Get type
	 * @throws Exception
	 */
	private HttpResponse doGet(String urlString, String cookieStrings) {

		HttpResponse response = null;

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		String cookieString = "";

		if (currentCookieString.length() > 2) {
			request.addHeader("Cookie", currentCookieString);
		} else if (null != cookieString && cookieString.length() > 3) {
			request.addHeader("Cookie", cookieString);
		}
		if(!GlobalInfo.varibles.containsKey("Cookie")){
			GlobalInfo.varibles.put("Cookie", cookieStrings);
		}else if(cookieStrings.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookieStrings);
		}
		try {
			request.setURI(new URI(urlString));

			response = client.execute(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}  
	
	/**
	 * @return the String the main method of send by Post type
	 * @throws Exception
	 */
	
	public String doTest(boolean isRandom,String type){
		String result=doTest(isRandom,type,testApi,interfaceTest);
		if(!GlobalInfo.varibles.containsKey("Response")){
			GlobalInfo.varibles.put("Response", result);
		}else{
			GlobalInfo.varibles.replace("Response", result);
		}
		return result;
	}
	
	private String doTest(String type,String testApi, InterfaceTestEntity interfaceTest) {
		String result="";
		if(null!=interfaceTest.getPreExcution()&&!interfaceTest.getPreExcution().equals("")){
			result=doTest(false, type,testApi,interfaceTest);
		}
		
		if(type.equalsIgnoreCase("post")){
				try {
				
					result=	doPost(false,testApi,interfaceTest);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}else if(type.equalsIgnoreCase("get")){
					try {
						result=	doGet(false,testApi,interfaceTest);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}else if(type.equalsIgnoreCase("put")){
			try {
				result=	doPut(false,testApi,interfaceTest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(type.equalsIgnoreCase("delete")){
			try {
				result=	doDelete(false,testApi,interfaceTest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	public String doTest(boolean isRandom,String type,String testApi, InterfaceTestEntity interfaceTest){
		String sp = System.getProperty("file.separator");
		String result="";
		if(null!=interfaceTest.getPreExcution()&&interfaceTest.getPreExcution().length()>1){
			String preExcution=interfaceTest.getPreExcution();
			if(!preExcution.contains(".")){
				preExcution=preExcution+".xml";
			}
			SAXReader saxReader = new SAXReader();
			Document doc = null;
			try {
				
				doc = saxReader.read(new File(GlobalInfo.rootPath + sp + "wtdapicases"
						+ sp +"interfaces"+ sp+"public"+sp+preExcution));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
				
			Element caseElem=(Element)doc.selectSingleNode("//case");
			Element e=(Element) caseElem.selectSingleNode("test");
			Element caseApiElement=(Element)caseElem.selectSingleNode("testApi");
			String sendType=caseElem.attributeValue("type");
			String testApiNew=caseApiElement.attributeValue("api");
			String realApiUrl="";
//			if(testApiNew.contains("mainhost.com")){
//			String localUrl=GlobalInfo.HostName;
//			String[] testApiArray=testApiNew.split("://");
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
//			testApiNew=realApiUrl;
//		}
			//testApiNew=realApiUrl;

			List<Element> paramElems = e.selectNodes("parameter");
			InterfaceTestEntity interfaceTestEntity=new InterfaceTestEntity();
			List<InterfaceParam> parrams=new ArrayList<InterfaceParam>();
			for (Element el : paramElems) {
				InterfaceParam paramEntity=new InterfaceParam();
				paramEntity.setName(el.attributeValue("name"));
				paramEntity.setRandom(false);
				paramEntity.setValue(el.attributeValue("value"));
				parrams.add(paramEntity);
		}
			interfaceTestEntity.setExpectContansString(e.attributeValue("expectContansString"));
			interfaceTestEntity.setInterfaceParams(parrams);
			interfaceTestEntity.setName(e.attributeValue("name"));	
			interfaceTestEntity.setPreExcution(e.attributeValue("preExcution"));
			if(GlobalInfo.varibles.containsKey("PreCookie")){
				currentCookieString=GlobalInfo.varibles.get("PreCookie");
			}
			result=doTest(sendType,testApiNew,interfaceTestEntity);
			if(!GlobalInfo.varibles.containsKey("PreResponse")){
				GlobalInfo.varibles.put("PreResponse", result);
			}else{
				GlobalInfo.varibles.replace("PreResponse", result);
			}
			if(!GlobalInfo.varibles.containsKey("Cookie")){
				GlobalInfo.varibles.put("Cookie", currentCookieString);
			}

			if(!GlobalInfo.varibles.containsKey("PreCookie")){
				GlobalInfo.varibles.put("PreCookie", GlobalInfo.varibles.get("Cookie"));
			}else{
				GlobalInfo.varibles.replace("PreCookie", GlobalInfo.varibles.get("Cookie"));
			}
			if(GlobalInfo.varibles.containsKey("PreCookie")){
				currentCookieString=GlobalInfo.varibles.get("PreCookie");
			}
		}
		interfaceTest.setPreExcution("");
		result=doTest(type,testApi,interfaceTest);
		currentCookieString = "";
		if(GlobalInfo.varibles.containsKey("PreCookie"))
		GlobalInfo.varibles.replace("PreCookie","");
		if(GlobalInfo.varibles.containsKey("Cookie"))
		GlobalInfo.varibles.put("Cookie", "");
		return result;
	}
	
	public String doPost(boolean isRandom,String testApi, InterfaceTestEntity interfaceTest) throws Exception {
		String requestUrl = testApi;
		if (!isRandom) {
			requestUrl = generateRightUrl4Get(testApi,interfaceTest.getInterfaceParams());
		} else {
			requestUrl = generateRandomUrl4Get(testApi,interfaceTest.getInterfaceParams());
		}
		String cookieStrings="";
		cookieStrings=currentCookieString;
		List<String> cookieList=new ArrayList<String>();
		HttpResponse response=doPost(requestUrl,cookieStrings);
		String cookies=getCookie(response);
		if(!GlobalInfo.varibles.containsKey("Cookie")){
			GlobalInfo.varibles.put("Cookie", cookies);
		}else if(cookies.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookies);
		}
		String strResult="";
		String referUrl="";
		Header[]	headers;
		boolean requestOk=true;
		if(response.getStatusLine().toString().contains("Moved Temporarily")){
			requestOk=false;
		}
		while(!requestOk){
		if(response.getStatusLine().toString().contains("Moved Temporarily")){
			headers=response.getAllHeaders();
				for(Header location:headers){
					if(location.getName().equalsIgnoreCase("Set-Cookie")){
						cookieList.add(location.getValue());
					}
				}
			for(Header location:headers) {
				if (location.getName().equalsIgnoreCase("location")) {
					referUrl = location.getValue();
					for (String cookie : cookieList) {
						//if(cookie.startsWith("guid=")||cookie.startsWith("per_")||cookie.startsWith("cityid")){
						currentCookieString += cookie.split(";")[0] + ";";
						//}

					}
					if (currentCookieString.endsWith(";")) {
						currentCookieString = currentCookieString.substring(0, currentCookieString.length() - 1);
					}
					response = doPost(referUrl, currentCookieString);
				}
			}

		}else{
			requestOk=true;
		}
		}
		strResult = EntityUtils.toString(response.getEntity());
		return strResult;
	}

	/**
	 * @return the String the main method of send by Post type
	 * @throws Exception
	 */
	public HttpResponse doPost(String url, String cookieStrings)
			throws Exception {
		String requestUrl = url;
		String[] urlArrayStrings = requestUrl.split("\\?");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlArrayStrings[0]);
		String cookieString = "";
		httpPost.addHeader("charset", HTTP.UTF_8);
		if (currentCookieString.length() > 2) {
			httpPost.addHeader("Cookie", currentCookieString);
		} else if (null != cookieString && cookieString.length() > 3) {
			httpPost.addHeader("Cookie", cookieString);
		}
		if(!GlobalInfo.varibles.containsKey("Cookie")&&!cookieStrings.equalsIgnoreCase("")){
			GlobalInfo.varibles.put("Cookie", cookieStrings);
		}else if(cookieStrings.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookieStrings);
		}

		String contentType= this.interfaceTest.getContentType();
		if(contentType.contains("application/json")){
			httpPost.addHeader("Content-type","application/json; charset=utf-8");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setEntity(new StringEntity(interfaceTest.getBody(), Charset.forName("UTF-8")));

		}else{
			if (urlArrayStrings.length > 1) {
				String[] parmsArry = urlArrayStrings[1].split("&");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						parmsArry.length);
				for (int i = 0; i < parmsArry.length; i++) {
					String valueString ="";
					if(parmsArry[i].split("=").length>1){
						valueString =parmsArry[i].split("=")[1];
					}

					nameValuePairs.add(new BasicNameValuePair(parmsArry[i]
							.split("=")[0], valueString.replace("\"", "")));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			}

		}

		HttpResponse response;
		response = httpclient.execute(httpPost);
		return response;
	}

	public String doPut(boolean isRandom,String testApi, InterfaceTestEntity interfaceTest) throws Exception {
		String requestUrl = testApi;
		if (!isRandom) {
			requestUrl = generateRightUrl4Get(testApi,interfaceTest.getInterfaceParams());
		} else {
			requestUrl = generateRandomUrl4Get(testApi,interfaceTest.getInterfaceParams());
		}
		String cookieStrings="";
		cookieStrings=currentCookieString;
		List<String> cookieList=new ArrayList<String>();
		HttpResponse response=doPut(requestUrl,cookieStrings);
		String cookies=getCookie(response);
		if(!GlobalInfo.varibles.containsKey("Cookie")){
			GlobalInfo.varibles.put("Cookie", cookies);
		}else if(cookies.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookies);
		}
		String strResult="";
		String referUrl="";
		Header[]	headers;
		boolean requestOk=true;
		if(response.getStatusLine().toString().contains("Moved Temporarily")){
			requestOk=false;
		}
		while(!requestOk){
			if(response.getStatusLine().toString().contains("Moved Temporarily")){
				headers=response.getAllHeaders();
				for(Header location:headers){
					if(location.getName().equalsIgnoreCase("Set-Cookie")){
						cookieList.add(location.getValue());
					}
				}
				for(Header location:headers) {
					if (location.getName().equalsIgnoreCase("location")) {
						referUrl = location.getValue();
						for (String cookie : cookieList) {
							//if(cookie.startsWith("guid=")||cookie.startsWith("per_")||cookie.startsWith("cityid")){
							currentCookieString += cookie.split(";")[0] + ";";
							//}

						}
						if (currentCookieString.endsWith(";")) {
							currentCookieString = currentCookieString.substring(0, currentCookieString.length() - 1);
						}
						response = doPut(referUrl, currentCookieString);
					}
				}

			}else{
				requestOk=true;
			}
		}
		strResult = EntityUtils.toString(response.getEntity());
		return strResult;
	}

	/**
	 * @return the String the main method of send by Post type
	 * @throws Exception
	 */
	public HttpResponse doPut(String url, String cookieStrings)
			throws Exception {
		String requestUrl = url;
		String[] urlArrayStrings = requestUrl.split("\\?");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httput = new HttpPut(urlArrayStrings[0]);
		String cookieString = "";
		httput.addHeader("charset", HTTP.UTF_8);
		if (currentCookieString.length() > 2) {
			httput.addHeader("Cookie", currentCookieString);
		} else if (null != cookieString && cookieString.length() > 3) {
			httput.addHeader("Cookie", cookieString);
		}
		if(!GlobalInfo.varibles.containsKey("Cookie")&&!cookieStrings.equalsIgnoreCase("")){
			GlobalInfo.varibles.put("Cookie", cookieStrings);
		}else if(cookieStrings.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookieStrings);
		}

		String contentType= this.interfaceTest.getContentType();
		if(contentType.contains("application/json")){
			httput.addHeader("Content-type","application/json; charset=utf-8");
			httput.setHeader("Accept", "application/json");
			httput.setEntity(new StringEntity(interfaceTest.getBody(), Charset.forName("UTF-8")));

		}else{
			if (urlArrayStrings.length > 1) {
				String[] parmsArry = urlArrayStrings[1].split("&");
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						parmsArry.length);
				for (int i = 0; i < parmsArry.length; i++) {
					String valueString ="";
					if(parmsArry[i].split("=").length>1){
						valueString =parmsArry[i].split("=")[1];
					}

					nameValuePairs.add(new BasicNameValuePair(parmsArry[i]
							.split("=")[0], valueString.replace("\"", "")));
				}
				httput.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
			}

		}

		HttpResponse response;
		response = httpclient.execute(httput);
		return response;
	}




	/**
	 * @return the String the main method of send by Get type
	 * @throws Exception
	 */
	public String doDelete(boolean isRandom, String testApi,
						InterfaceTestEntity interfaceTest) throws Exception {
		String requestUrl = testApi;
		BufferedReader in = null;
		String result = "";
		if (!isRandom) {
			requestUrl = generateRightUrl4Get(testApi,
					interfaceTest.getInterfaceParams());
		} else {
			requestUrl = generateRandomUrl4Get(testApi,
					interfaceTest.getInterfaceParams());
		}
		String cookieStrings = currentCookieString;
		HttpResponse response = doDelete(requestUrl, cookieStrings);
		String cookies = getCookie(response);
		if (!GlobalInfo.varibles.containsKey("Cookie")) {
			GlobalInfo.varibles.put("Cookie", cookies);
		} else if(cookies.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookies);
		}
		Header[] headers;
		String referUrl = "";
		List<String> cookieList = new ArrayList<String>();
		boolean requestOk = true;
		if (response.getStatusLine().toString().contains("Moved Temporarily")) {
			requestOk = false;
		}
		while (!requestOk) {
			if (response.getStatusLine().toString()
					.contains("Moved Temporarily")) {
				headers = response.getAllHeaders();
				for (Header location : headers) {
					if (location.getName().equalsIgnoreCase("Set-Cookie")) {
						cookieList.add(location.getValue());
					}
				}
				for (Header location : headers) {
					if (location.getName().equalsIgnoreCase("location")) {
						referUrl = location.getValue();
						for (String cookie : cookieList) {
							//if (cookie.startsWith("guid=")|| cookie.startsWith("per_")|| cookie.startsWith("cityid")) {
							currentCookieString += cookie.split(";")[0]
									+ ";";
							//}

						}
						if (currentCookieString.endsWith(";")) {
							currentCookieString = currentCookieString
									.substring(0,
											currentCookieString.length() - 1);
						}

						response = doDelete(referUrl, currentCookieString);
					}
				}
			} else {
				requestOk = true;
			}
		}

		in = new BufferedReader(new InputStreamReader(response.getEntity()
				.getContent()));
		StringBuffer sb = new StringBuffer("");
		String line = "";
		String NL = System.getProperty("line.separator");
		while ((line = in.readLine()) != null) {
			sb.append(line + NL);
		}
		in.close();
		result = sb.toString();
		return result;
	}


	/**
	 * @return the String the  method of send by Get type
	 * @throws Exception
	 */
	private HttpResponse doDelete(String urlString, String cookieStrings) {

		HttpResponse response = null;

		HttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete();
		String cookieString = "";

		if (currentCookieString.length() > 2) {
			request.addHeader("Cookie", currentCookieString);
		} else if (null != cookieString && cookieString.length() > 3) {
			request.addHeader("Cookie", cookieString);
		}
		if(!GlobalInfo.varibles.containsKey("Cookie")){
			GlobalInfo.varibles.put("Cookie", cookieStrings);
		}else if(cookieStrings.length()>1){
			GlobalInfo.varibles.replace("Cookie", cookieStrings);
		}
		try {
			request.setURI(new URI(urlString));

			response = client.execute(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}
	/**
	 * @return the String generate the right request url
	 */
	private String generateRightUrl4Get(String testApi,List<InterfaceParam> interfaceParams) {
		isRightUrl = true;
		String requestUrl = testApi;
		for (InterfaceParam inParam : interfaceParams) {
			inParam.setRealType(inParam.getType());
		}
		requestUrl = generateTheUrl(testApi,interfaceParams);
		return requestUrl;
	}

	/**
	 * @return the String generate the random request url
	 */
	private String generateRandomUrl4Get(String testApi,List<InterfaceParam> interfaceParams) {
		String requestUrl = testApi;
		requestUrl = requestUrl + "?";
		String paramString = randomParameters(interfaceParams);

		requestUrl = paramString;

		return requestUrl;
	}

	/**
	 * @return random the request parameter
	 */
	private String randomParameters(List<InterfaceParam> paraList) {

		String requestUrl = testApi;
		isRightUrl = true;
 		int randomType = 2;
		int randomParmCount = paraList.size();
		List<InterfaceParam> temParaList = new ArrayList<InterfaceParam>();
		if (paraList.size() > 0) {
			/*
			 * random a length of parameters and get the random parameter
			 */
			randomParmCount = (int) ((Math.random()) * (paraList.size()));
			if (randomParmCount != paraList.size()) {
				for (int i = 0; i < randomParmCount; i++) {
					InterfaceParam interfaceParam = paraList.get((int) ((Math
							.random()) * (paraList.size())));
					if(temParaList.size()==0){
						InterfaceParam temParaEntity=(InterfaceParam) interfaceParam.clone();		
						temParaList.add(temParaEntity);
						}
					for(int j=0;j<temParaList.size();j++){
						if(interfaceParam.getName().equalsIgnoreCase(temParaList.get(j).getName())){
							--i;
							break;
						}else{
							if(j==temParaList.size()-1){
								InterfaceParam temParaEntity=(InterfaceParam) interfaceParam.clone();		
								temParaList.add(temParaEntity);
								break;
							}
							
						}
					}
				}
			} else {
				temParaList = paraList;
			}

//			for(int i =0;i<2;i++){
//				InterfaceParam temParaEntity=(InterfaceParam) paraList.get(i).clone();	
//				temParaList.add(temParaEntity);
//			}
			
			/*
			 * generate the random vaule and random value type
			 */
			if (null != temParaList) {
				for (InterfaceParam inpara : temParaList) {
					String value = null;
					String typeString = typeArray[(int) ((Math.random()) * (randomType))];
					if (typeString.equalsIgnoreCase("String")) {
						value = randomString((int) ((Math.random()) * (20)));
						inpara.setRealType("string");
					} else if (typeString.equalsIgnoreCase("int")) {
						value = randomNumber() + "";
						inpara.setRealType("int");
					}
					inpara.setValue(value);
				}
			}

		}

		/*
		 * will judge the whether the url correct
		 */ 
		if (null != temParaList) {
			for (InterfaceParam inpara : paraList) {
				for(int i=0;i<temParaList.size();i++){
					if(temParaList.get(i).getName().equalsIgnoreCase(inpara.getName())){
					break;
					}else{
						if(i==temParaList.size()-1){
							if(inpara.getNotNull()){
								isRightUrl = false;
								break;
						}
						}
					
					} 
				}
			}
				if (isRightUrl) {
					for (InterfaceParam subInpara : temParaList) {
						if(!subInpara.getType().equalsIgnoreCase("string")){
						if (!subInpara.getRealType().equalsIgnoreCase(
								subInpara.getType())) {
							isRightUrl = false;
							break;
						}
					}
					}
				}
			
		}
		
		if(paraList.size()>0){
			if(null == temParaList||temParaList.size()==0){
				isRightUrl = false;
			}
		}
		return generateTheUrl(testApi,temParaList);

	}

	/**
	 * @return generate the url
	 */
	private String generateTheUrl(String testApi,List<InterfaceParam> paraList) {
		String requestUrl = testApi;
		if (paraList.size() > 0) {
			requestUrl = requestUrl + "?";
			String paramString = "";
			for (InterfaceParam inParam : paraList) {
				String paraValue = getVarible(inParam.getValue());
				if(inParam.isRandom()){
					paraValue=getUnixTime()+"";
				}
//				if (inParam.getRealType().equalsIgnoreCase("string")) {
//					paraValue = "\"" + paraValue + "\"";
//				}
				paramString += ("&" + inParam.getName() + "=" + paraValue);
			}
			paramString = paramString.substring(1);
			requestUrl += paramString;
		}
		
		url=requestUrl;
		return requestUrl;
	}

	/**
	 * @return random the char
	 */
	private String randomString(int length) {
		String generateString = "";
		for (int i = 0; i < length; i++) {
			generateString += randomChar();
		}
		return generateString;
	}

	/**
	 * @return random the char
	 */
	private String randomChar() {
		String lowChars = "abcdefghijklmnopqrstuvwxyz";
		int toUpChar = (int) ((Math.random()) * (2));
		String generateChar = "a";
		if (toUpChar == 1) {
			generateChar = lowChars.charAt((int) ((Math.random()) * (26))) + "";
			generateChar = generateChar.toUpperCase();
		} else {
			generateChar = lowChars.charAt((int) ((Math.random()) * (26))) + "";
		}
		return generateChar;
	}

	/**
	 * @return random the number
	 */
	private int randomNumber() {
		return (int) ((Math.random()) * (100000));
	}
	
	/**
	 * @return random the number
	 */
	private long getUnixTime() {
		return System.currentTimeMillis()/100;
	}

	/**
	 * @return transform the data type
	 */
	private Object transformDataType(String value, String type) {
		String lowType = type.toLowerCase();
		switch (lowType) {
		case "string":
			return value.toString();
		case "int":
			return Integer.parseInt(value);
		default:
			return value.toString();
		}
	}

	/**
	 * @return boolean
	 */
	public boolean getIsRightUrl() {
		return isRightUrl;
	}
	public String getUrl(){
		return url;
	}
	/**
	 * @return get the cookie
	 */
	private String getCookie(HttpResponse response) {
		Header[] headers;
		String cookieString = "";
		List<String> cookieList = new ArrayList<String>();
		headers = response.getAllHeaders();
		for (Header location : headers) {
			if (location.getName().equalsIgnoreCase("Set-Cookie")) {
					cookieList.add(location.getValue());
				}

			}

//		for (String cookie : cookieList) {
//			cookieString += cookie.split(";")[0] + ";";
//		}
		for (String cookie : cookieList) {
			if(cookie.endsWith(";")){
				cookieString += cookie ;
			}else{
				cookieString += cookie + ";";
			}
		}

		if (cookieString.endsWith(";")) {
			cookieString = cookieString.substring(0,
					cookieString.length() - 1);
		}

		return cookieString;
	}
	/**
	 * @return get the cookie
	 */
	public String getVarible(String varibleName) {
		String VaribleValue = "";
		if (varibleName.startsWith("$")) {
			String valueString="";
			if(varibleName.contains(".")){
				valueString = GlobalInfo.varibles.get(varibleName.substring(
						1).split("\\.")[0]);
			}else{
				valueString = GlobalInfo.varibles.get(varibleName.substring(
						1));
			}
			if (varibleName.contains(".")) {
				String realVarible = varibleName
						.substring(1).split("\\.")[1];
				if (valueString.contains(";")&&!valueString.contains("{")&&!valueString.contains(":")) {
					String[] variblesStrings = valueString.split(";");
					for (String varible : variblesStrings) {
						if(varible.split("=")[0].equalsIgnoreCase(realVarible)){
							VaribleValue = varible.split("=")[1];
						}
					}
				}else if(valueString.contains("{")&&valueString.contains(":")){
					//JSONObject json= new JSONObject(valueString);
					String subString=valueString.substring(valueString.indexOf(realVarible));
					int fistIndex=subString.indexOf(",");
					int secondIndex=subString.indexOf("}");
					if(fistIndex<secondIndex){
						VaribleValue=subString.split(",")[0].split(":")[1].replace("\"", "");
					}else{
						VaribleValue=subString.split("}")[0].split(":")[1].replace("\"", "");
					}
				}else if(valueString.contains("=")) {
					if(valueString.contains(";")){
						String[] variblesStrings= valueString.split(";");
						for (String varible : variblesStrings) {
							if(varible.split("=")[0].equalsIgnoreCase(realVarible)){
								VaribleValue = varible.split("=")[1];
							}
						}
					}else{
						if(valueString.split("=")[0].equalsIgnoreCase(realVarible)){
							VaribleValue = valueString.split("=")[1];
						}
					}
				}
			} else {
				VaribleValue = valueString;
			}
		} else {
			VaribleValue = varibleName;
		}
		return VaribleValue;
	}
}
