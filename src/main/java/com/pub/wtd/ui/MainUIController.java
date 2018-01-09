/**
* @Title: MainUIController.java 
* @Package com.pub.WTD.ui 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年10月13日 下午4:08:16 
* @version V1.0   
 */
package com.pub.wtd.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import com.pub.wtd.common.Main;
import com.pub.wtd.entities.CaseEntity;
import com.pub.wtd.util.GlobalInfo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * @author hekun<158109016@qq.com>
 *
 */
public class MainUIController implements Initializable{
	
		OfferData4UI uiData4ui;//offer the Data to UI component
		CaseFilterEngine caseFilterEngine=new CaseFilterEngine();
		ObservableList<CaseEntity> caseEnties=FXCollections.observableArrayList();
		List<String> historySelectedCase=null;
		Boolean isFirstRun=true;
		 Main main=new Main();
		 Thread thread1 =null;
		/* 
		 * mapping the component of case selector and configeration
		 */
	    @FXML
	    private TextField yuming;
	    
	    @FXML
	    private CheckBox firefox;
	 
	    @FXML
	    private CheckBox chrome;
	    
	    @FXML
	    private CheckBox iexplore;
	    
	    @FXML
	    private TextField firefoxLocation;
	 
	    @FXML
	    private TextField chromeLocation;
	    
	    @FXML
	    private TextField ieLocation;
	    
	    @FXML
	    private TextArea maileList;

	    @FXML
	    private TreeView treeView;
	   
	    @FXML
	    private Button selectCases;
	    
	    @FXML
	    private Button runCaseBt;
	    
	    @FXML
	    private Button pauseBt;
	    
	    @FXML
	    private Button stopBt;
	    
	    @FXML
	    private Button saveConfigBt;
	    
	    @FXML
	    private TableView<CaseEntity> caseTable;

	    @FXML
	    private TableColumn<CaseEntity, String> caseNameCol;
	    
	    @FXML
	    private TableColumn<CaseEntity, String> caseModuleCol;
	    
	    @FXML
	    private TableColumn<CaseEntity, String> caseTypeCol;
	    
	    @FXML
	    private TitledPane x3;

	    @FXML
	    private TitledPane runCasePane;
	    
	    @FXML
	    public ProgressBar progressBar;
	    
	    @FXML
	    public ProgressIndicator  progressText;
	    
	    @FXML
	    public Label caseName;
	    
	    @FXML
	    public Label totalSitutation;
	    
	    
	    @FXML
	    public PieChart currentPieChart;
	    
		/* (non-Javadoc)
		 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
		 */ 
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Auto-generated method stub
			uiData4ui=new OfferData4UI();
			historySelectedCase=uiData4ui.getCaseList();
			setYuming();
			setBrowsers();
			setMailers();
			setCaseModules();
			generateTree();
			setTableCase();
			GlobalInfo.mainUIController=this;
		}
		
		
		private void setYuming () {
			yuming.setPromptText(uiData4ui.getUrl());
		}
		
		private void setBrowsers() {
			
			HashMap<String, String> browsersMap=uiData4ui.getBrowser();
			Iterator it = browsersMap.entrySet().iterator();
			while(it.hasNext()){
				Entry entry = (java.util.Map.Entry)it.next();
				String browserName=(String) entry.getKey();
				String location=(String) entry.getValue();
				switch (browserName.toLowerCase()) {
				case "firefox":
					firefox.setSelected(true);
					firefoxLocation.setPromptText(location);
					firefoxLocation.setDisable(false);
					break;
				case "chrome":
					chrome.setSelected(true);
					chromeLocation.setPromptText(location);
					chromeLocation.setDisable(false);
					break;
				case "ie":
					iexplore.setSelected(true);
					ieLocation.setPromptText(location);
					ieLocation.setDisable(false);
					break;
				default:
					break;
				}
			}
		}
		
		private void setTableCase() {
			ObservableList<CaseEntity> localCaseEnties=FXCollections.observableArrayList();
			for(String caseLocal:historySelectedCase){
				String[] caseArray=caseLocal.split(":");
				CaseEntity caseEntity=new CaseEntity();
				caseEntity.setCaseModule(caseArray[1]);
				caseEntity.setCaseName(caseArray[0]);
				caseEntity.setCaseType(caseArray[2]);
				localCaseEnties.add(caseEntity);
			}
			 caseTable.setItems(localCaseEnties);
			 caseNameCol.setCellValueFactory(new PropertyValueFactory("caseName"));
			 caseModuleCol.setCellValueFactory(new PropertyValueFactory("caseModule"));
			 caseTypeCol.setCellValueFactory(new PropertyValueFactory("caseType"));
		}
		
		private void setMailers () {
			List<String> receiverList=uiData4ui.getRecevier();
			String recevierString="";
			for(String recevier :receiverList){
				recevierString+=recevier+"\n";
			}
			maileList.setText(recevierString);
		}
		
		private void setAllCases () {
			List<String> receiverList=uiData4ui.getRecevier();
			String recevierString="";
			for(String recevier :receiverList){
				recevierString+=recevier+"\n";
			}
			maileList.setText(recevierString);
		}
		
		private void setCaseModules () {
			 ObservableList<String> names = FXCollections.observableArrayList(
			        );
			List<String> moduleList=uiData4ui.getCaseModules();
			for(String module:moduleList){
				names.add(module);
			}
		}
		
		 private void generateTree() {
			 
				HashMap<?, ?> caseModulesHashMap=caseFilterEngine.getCaseModules();
				CheckBoxTreeItem<String> root = new CheckBoxTreeItem<String>("AllCases");
				Iterator iter = caseModulesHashMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry entry=(Entry) iter.next();
					String key = (String)entry.getKey();
					HashSet<?> val = (HashSet<?>)entry.getValue();
					CheckBoxTreeItem<String> secondLevel = new CheckBoxTreeItem<String>(key);
					for(Object module:val){
						CheckBoxTreeItem<String> thirdLevel = new CheckBoxTreeItem<String>((String) module);
						
						HashSet<?> casesHashSet=caseFilterEngine.getCasesByModule(key, (String)module);
						for(Object singleCase:casesHashSet){
							CheckBoxTreeItem<String> caseCheckBox = new CheckBoxTreeItem<String>((String) singleCase);
							for(String caseLocal:historySelectedCase){
							if(caseLocal.contains(singleCase +":"+ module +":"+ key)){
								caseCheckBox.setSelected(true);
								root.setExpanded(true);
								secondLevel.setExpanded(true);
								//thirdLevel.setSelected(true);
								thirdLevel.setIndeterminate(true);
								secondLevel.setSelected(true);
								break;
							}
							}
							thirdLevel.getChildren().add(caseCheckBox);
						}
						secondLevel.getChildren().add(thirdLevel);
					}
					
					root.getChildren().addAll(secondLevel);
					}
		
				 root.setExpanded(true);
				 treeView.setRoot(root);
				 treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
			}

		 public void onChromeSelected(ActionEvent event) {
			 
			 if(!chrome.isSelected()){
				 chromeLocation.setDisable(true);
			 }else{
				 chromeLocation.setDisable(false);
			 }
			 
		 }
		 
		 public void onFirefoxSelected(ActionEvent event) {
			 
			 if(!firefox.isSelected()){
				 firefoxLocation.setDisable(true);
			 }else{
				 firefoxLocation.setDisable(false);
			 }
			 
		 }
		 
		 public void onIeSelected(ActionEvent event) {
		 
			 if(!iexplore.isSelected()){
				 ieLocation.setDisable(true);
			 }else{
				 ieLocation.setDisable(false);
			 }	 
	 }
		 
		 public void onFunctionSelected(ActionEvent event) {
			 
			 if(!iexplore.isSelected()){
				 ieLocation.setDisable(true);
			 }else{
				 ieLocation.setDisable(false);
			 }	 
	 }
		 
		 public void onInterfaceSelected(ActionEvent event) {
			 
			 if(!iexplore.isSelected()){
				 ieLocation.setDisable(true);
			 }else{
				 ieLocation.setDisable(false);
			 }	 
	 }
		 
	  
		 public void onCasesSelected(ActionEvent event) {
			 if(caseEnties.size()>0){
				 caseEnties.retainAll();
				 //caseEnties.removeAll();
			 }
			 ObservableList caseTypeList= treeView.getRoot().getChildren();
			 for(int i=0;i<caseTypeList.size();i++){
				CheckBoxTreeItem<String> caseType=(CheckBoxTreeItem<String>) caseTypeList.get(i);
				ObservableList moduleList=caseType.getChildren();
				for(int j=0;j<moduleList.size();j++){
					CheckBoxTreeItem<String> module=(CheckBoxTreeItem<String>) moduleList.get(j);
					ObservableList caseList=module.getChildren();
					for(int k=0;k<caseList.size();k++){
						CheckBoxTreeItem<String> singleCase=(CheckBoxTreeItem<String>) caseList.get(k);
						if(singleCase.isSelected()){
							CaseEntity caseEntity=new CaseEntity();
							caseEntity.setCaseModule(module.getValue());
							caseEntity.setCaseName(singleCase.getValue());
							caseEntity.setCaseType(caseType.getValue());
							caseEnties.add(caseEntity);
						}
					}
				
				}
			 }
			 caseTable.setItems(caseEnties);
			 caseNameCol.setCellValueFactory(new PropertyValueFactory("caseName"));
			 caseModuleCol.setCellValueFactory(new PropertyValueFactory("caseModule"));
			 caseTypeCol.setCellValueFactory(new PropertyValueFactory("caseType"));
			 caseTable.getColumns().setAll(caseNameCol,caseModuleCol,caseTypeCol);
			 
	 }
		 
		 public void onSaveConfig() {
		if (null != yuming.getText() && !yuming.getText().equals("")) {
			uiData4ui.setUrl(yuming.getText());
		}
		uiData4ui.setRecevier(maileList.getText());
		HashMap<String, String> browsers = new HashMap<>();

		if (firefox.isSelected()) {
			if(null==firefoxLocation.getText()||firefoxLocation.getText().equals("")||firefoxLocation.getText().equals(" ")){
				browsers.put("fireFox", firefoxLocation.getPromptText() + "");
			}else{
				browsers.put("fireFox", firefoxLocation.getText() + "");
			}	
		}
		if (chrome.isSelected()) {
			if(null==chromeLocation.getText()||chromeLocation.getText().equals("")||chromeLocation.getText().equals(" ")){
				browsers.put("chrome", chromeLocation.getPromptText() + "");
			}else{
				browsers.put("chrome", chromeLocation.getText() + "");
			}	
		}

		if (iexplore.isSelected()) {
			if(null==ieLocation.getText()||ieLocation.getText().equals("")||ieLocation.getText().equals(" ")){
				browsers.put("ie", ieLocation.getPromptText() + "");
			}else{
				browsers.put("ie", ieLocation.getText() + "");
			}	
		}
		if (browsers.size() > 0) {
			uiData4ui.setBrowsers(browsers);
		}

		if (caseEnties.size() > 0) {
			uiData4ui.setCaseList(caseEnties);
		}
		uiData4ui.saveConfigFile();
		uiData4ui.saveCaseListFile();
	}
		 
		 public void onRunCase() {
			 if(isFirstRun==true){
				 thread1= new Thread(main);
				 saveConfigBt.setDisable(true);
				 runCaseBt.setDisable(true);
				 x3.setExpanded(false);
				 runCasePane.setExpanded(true);
				 onSaveConfig();
				 thread1.start();
			 }
			 if(isFirstRun==false){
				 thread1.resume();
			 }
			 isFirstRun=false;
			}
		 
		 public void onPause() {
			 runCaseBt.setDisable(false);
			 thread1.suspend();
			}
		 
		 public void onStop() {
			 saveConfigBt.setDisable(false);
			 runCaseBt.setDisable(false);
			// thread1.stop();
			 GlobalInfo.currentWebDriver.close();
			 GlobalInfo.currentWebDriver.quit();
			// GlobalInfo.currentWebDriver.quit();
			 Thread.currentThread().interrupt();
			 isFirstRun=true;
			}
		 
}
