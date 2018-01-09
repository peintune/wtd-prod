/**
* @Title: Application.java 
* @Package com.pub.WTD.ui 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年10月10日 下午1:40:17 
* @version V1.0   
 */
package com.pub.wtd.ui;


import java.io.File;

import com.pub.wtd.util.GlobalInfo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

/**
 * @author administrator
 *
 */
public class ApplicationLaunch extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 InitUIEnv initUIEnv=new InitUIEnv();
		 initUIEnv.initalAllCase2Local();
		 Application.launch(ApplicationLaunch.class, (java.lang.String[])null);
	
	}
	Stage stage;
	Scene scene;
	Parent root;
    private final double MINIMUM_WINDOW_WIDTH = 1000.0;
    private final double MINIMUM_WINDOW_HEIGHT = 1000.0;
	String path = new File("").getAbsolutePath();
	String sp = System.getProperty("file.separator");
	@Override
	public void start(Stage primaryStage) {
		try {
			GlobalInfo.isLaunchByUI=true;
			root = FXMLLoader
					.load(getClass().getResource("MainUI.fxml"));

			Scene scene = new Scene(root);
			primaryStage.setTitle("Web Auto Test And Development tools");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
}
