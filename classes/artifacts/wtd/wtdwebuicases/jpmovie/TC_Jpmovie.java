/**
 * @Title: TC_BaiDuSearch.java
 * @Package member
 * @Description: Test Case to search
 * @author zhoujing
 * @version V1.0   
 */
package jpmovie;

import com.pub.wtd.common.BaseCase;
import jpmovie.util.HttpSender;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TC_Jpmovie extends BaseCase {
	static String hostString="http://localhost:8080";
	String currentproxy="";
	public void execute() {

		webDriver.close();


		start();
	}

	public void start() {

			for (int i = 0; i < 100; i++) {
				try {
					reNewWebDriver();
					webDriver.get("http://www.jpmovie.cn/movie/index15048.html");
					if (isTextExist("精品电影") && webDriver.getCurrentUrl().contains("www.jpmovie")) {
						testSearch();
					} else {
						//postproxyResult(currentproxy,"jpmovie","failed");
						logger.info("ip failed!!  host:" + currentproxy);
					}

					sleep(1000);
					webDriver.quit();
				}catch ( Exception e){
					logger.info("ip failed  host:" + currentproxy);

				}finally {
					try{
						webDriver.quit();
					}catch (Exception ignore){}
				}
			}
	}

	public void testSearch(){
		try {

			//webDriver.findElement(By.xpath("/html/body/div[2]/div/div[1]/div[2]/div[1]/ul/li[2]/a")).click();
			sleep(500);
			//webDriver.switchTo().frame(0);
			//webDriver.findElements(By.tagName("a")).get(0).click();

			//webDriver.findElement(By.id("v_ads")).click();
			logger.info("ip success  host:" + currentproxy);

		}catch (Exception ee){
			logger.info("ip failed!!  host:" + currentproxy);
			ee.printStackTrace();
		}finally {
			sleep(2000);
		}
	}

	public void reNewWebDriver(){
		currentproxy =getOneProxy("null");
		String[] proxyarry= currentproxy.split(":");

		String host ="123.115.28.17";
		host = proxyarry[0];
		//host="125.106.111.107";
		int port =Integer.parseInt(proxyarry[1]);
		//port=7305;


		String proxyIpAndPort= host+":"+port;
		DesiredCapabilities cap = new DesiredCapabilities();
		Proxy proxy=new Proxy();
		proxy.setHttpProxy(proxyIpAndPort).setFtpProxy(proxyIpAndPort).setSslProxy(proxyIpAndPort);
		cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
		cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
		//System.setProperty("http.nonProxyHosts", "localhost");
		cap.setCapability(CapabilityType.PROXY, proxy);
		webDriver=new ChromeDriver(cap);
		webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		webDriver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);
	}

	public  String getOneProxy(String groupname){
		String url="";

		/** best proxy and good proxy*/
		//url = hostString+"/proxyip/rest/message/getonebestproxyip/"+groupname;

		if(null!=groupname&&groupname.trim()!=""){
			url = hostString+"/proxyip/rest/message/getonebestproxyip/"+groupname;
		}else{
			url = hostString+"/proxyip/rest/message/getonebestproxyip/null";
		}
		String result="";
		try {
			result= HttpSender.doGet4HttpClient(url,false).trim();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally {

		}
		return result;
	}

	public void postproxyResult(String proxyip,String groupname,String issucess){
		String url = hostString+"/proxyip/rest/message/postgoodproxyip";
		String message=proxyip+":"+groupname+"-"+issucess;
		try{

			String result= HttpSender.doPostJsonRequest(url,message,"",false);
		}catch (Exception e){logger.warn(e.getMessage());}

	}
}
