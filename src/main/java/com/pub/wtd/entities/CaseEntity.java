/**
* @Title: caseEntity.java 
* @Package com.pub.WTD.entities 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年10月14日 下午5:09:49 
* @version V1.0   
 */
package com.pub.wtd.entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author hekun<158109016@qq.com>
 * for UI caseEntity
 */
public class CaseEntity extends BaseEntity{
	private StringProperty caseName;

	public void setCaseName(String value) {
		caseNameProperty().set(value);
	}

	public String getFirstName() {
		return caseNameProperty().get();
	}

	public StringProperty caseNameProperty() {
		if (caseName == null)
			caseName = new SimpleStringProperty(this, "caseName");
		return caseName;
	}

	
	private StringProperty caseModule;

	public void setCaseModule(String value) {
		caseModuleProperty().set(value);
	}

	public String getCaseModule() {
		return caseModuleProperty().get();
	}

	public StringProperty caseModuleProperty() {
		if (caseModule == null)
			caseModule = new SimpleStringProperty(this, "caseModule");
		return caseModule;
	}

	
	private StringProperty caseType;

	public void setCaseType(String value) {
		caseTypeProperty().set(value);
	}

	public String getCaseType() {
		return caseTypeProperty().get();
	}

	public StringProperty caseTypeProperty() {
		if (caseType == null)
			caseType = new SimpleStringProperty(this, "caseType");
		return caseType;
	}
}
