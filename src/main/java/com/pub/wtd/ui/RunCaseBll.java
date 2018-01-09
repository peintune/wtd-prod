/**
* @Title: RunCaseController.java 
* @Package com.pub.WTD.ui 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年11月28日 下午2:13:01 
* @version V1.0   
 */
package com.pub.wtd.ui;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.pub.wtd.util.GlobalInfo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;



import javafx.scene.effect.Glow;
import javafx.scene.Node;

/**
 * @author hekun
 *
 */
public class RunCaseBll {

	
	String caseString="";
	String result="";
	String sp = System.getProperty("file.separator");
	double totalNumber=0.0;
	double perProcess=(double) 0.0;
	ProgressBar progessBar;
	ProgressIndicator  progessBarText;
	Label caseName;
	Label totalSitutation;
	PieChart currentPieChart;
	int passNumber=0;
	int failNumber=0;
	//private PieChart.Data selectedData;

	public RunCaseBll(){
		totalNumber=getTotalNumber();
		perProcess= 100/totalNumber /100;
		progessBar=	GlobalInfo.mainUIController.progressBar;
		progessBarText=	GlobalInfo.mainUIController.progressText;
		totalSitutation=GlobalInfo.mainUIController.totalSitutation;
		caseName=GlobalInfo.mainUIController.caseName;
		currentPieChart=GlobalInfo.mainUIController.currentPieChart;
		Platform.runLater(new Runnable() {
		    public void run() {
		    	totalSitutation.setText("("+0+"/"+(int)totalNumber+")");
		    	updateCurrentPieChart(0,0);
		    }
		});
	}
	
	/**
	 * call the method to update the case name of running UI 
	 */
	public void updateCaseNameText(final String casename){
		Platform.runLater(new Runnable() {
		    public void run() {
		    	caseName.setText("CaseName:"+casename.split(":")[0]);
		    }
		});
	}
	/**
	 * call all update UI method
	 */
	public void updateCompleteUI() {
		
		Platform.runLater(new Runnable() {
		    public void run() {
		    	double currentProces=progessBar.getProgress()+perProcess;
		    	progessBar.setProgress(currentProces);
		    	int currentCaseNum=Integer.parseInt(totalSitutation.getText().split("/")[0].substring(1))+1;
		    	progessBarText.setProgress(currentProces);
                totalSitutation.setText("("+currentCaseNum+"/"+(int)totalNumber+")");
		    	if(result.equalsIgnoreCase("pass")){
		    		passNumber++;
		    	}else{
		    		failNumber++;
		    	}
		    	updateCurrentPieChart(passNumber,failNumber);
		    	
		    }
		});
	}
	
	private void updateCurrentPieChart(int passNumber,int failNumber) {
		  ObservableList<PieChart.Data> pieChartData =
	                FXCollections.observableArrayList(
	                new PieChart.Data("failed number", failNumber),
	                new PieChart.Data("pass number", passNumber));
		  	currentPieChart.setData(pieChartData);	
		  	currentPieChart.setAnimated(true);
	        currentPieChart.setTitle("Current Pass Rate");
	        currentPieChart.setLabelLineLength(10);
	        currentPieChart.setLegendSide(Side.LEFT);	  
//	        
//	        final Label caption = new Label("");
//	        caption.setTextFill(Color.DARKORANGE);
//	        caption.setStyle("-fx-font: 24 arial;");
	
	    	
	        final Label caption = new Label("");
	        caption.setTextFill(Color.WHITE);
	        caption.setStyle("-fx-font: 24 arial;");

	        for (final PieChart.Data data : currentPieChart.getData()) {
	            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
	                new EventHandler<MouseEvent>() {
	                    @Override public void handle(MouseEvent e) {
	                        caption.setTranslateX(e.getSceneX());
	                        caption.setTranslateY(e.getSceneY());
	                        caption.setText(String.valueOf(data.getPieValue()) + "%");
	                     }
	                });
	        }
	        
	    	Tooltip  tooltip = new Tooltip("");

	        tooltip.setStyle("-fx-font: 14 arial;  -fx-font-smoothing-type: lcd;");// -fx-text-fill:black; -fx-background-color: linear-gradient(#e2ecfe, #99bcfd);");

	        for (final PieChart.Data data : currentPieChart.getData()) {
	            Tooltip.install(data.getNode(),tooltip);
	            applyMouseEvents(data,tooltip);
	        }

		
	}
	public void setResult(String result){
		this.result=result;
	}
	
	public void setCase(String caseString){
		this.caseString=caseString;
	}
	
	public void updateCurrentCase(){
		
		}
	
	private int getTotalNumber(){
		Document	docCaseList = null;
		try {
			SAXReader saxReader = new SAXReader();
			String sp = System.getProperty("file.separator");
			docCaseList = saxReader.read(new File(GlobalInfo.rootPath + sp+"config"+sp
					+ "caselist.cfg.xml"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		List<Element> caseListElements = docCaseList
				.selectNodes("//CaseList/testCase");
	return caseListElements.size();
	}
	
	private void applyMouseEvents(final PieChart.Data data,final Tooltip tooltip) {

        final Node node = data.getNode();

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                node.setEffect(new Glow());
                String styleString = "-fx-border-color: white; -fx-border-width: 3; -fx-border-style: dashed;";
                node.setStyle(styleString);
                tooltip.setText(String.valueOf(data.getName() + "\n" + (int)data.getPieValue()) ); 
            }
        });

        node.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                node.setEffect(null);
                node.setStyle("");
            }
        });

//        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
//
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                    selectedData = data;
//                    System.out.println("Selected data " + selectedData.toString());
//            }
//        });
    }
}
