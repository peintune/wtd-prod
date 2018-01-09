/**
* @Title: InterfaceParam.java 
* @Package com.pub.WTD.entities 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年8月21日 下午2:18:06 
* @version V1.0   
 */
package com.pub.wtd.entities;

/**
 * @author hekun<158109016@qq.com>
 * the interface test case's parameter entity
 */
public class InterfaceParam extends BaseEntity{

	private String  name;// the name of the parameter
	
	private String type="string";// the type of the parameter

	private String value;// the value of the parameter

	private boolean notNull=true;// is true,then this parameter  should not null
	
	private String realType="string";// is true,then this parameter  should not null
	
	private boolean isRandom=false;
	/**
	 * @return the isRandom
	 */
	public boolean isRandom() {
		return isRandom;
	}

	/**
	 * @param isRandom the isRandom to set
	 */
	public void setRandom(boolean isRandom) {
		this.isRandom = isRandom;
	}

	/**
	 * @return the realType
	 */
	public String getRealType() {
		return realType;
	}

	/**
	 * @param realType the realType to set
	 */
	public void setRealType(String realType) {
		this.realType = realType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the notNull
	 */
	public boolean getNotNull() {
		return notNull;
	}

	/**
	 * @param notNull the notNull to set
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}


}
