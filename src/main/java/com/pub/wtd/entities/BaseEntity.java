/**
* @Title: baseEntity.java 
* @Package com.pub.WTD.entities 
* @Description: To do something
* @author hekun 158109016@qq.com
* @date 2014年8月29日 上午11:57:24 
* @version V1.0   
 */
package com.pub.wtd.entities;

/**
 * @author hekun<158109016@qq.com>
 *
 */
public class BaseEntity implements Cloneable{
    public Object clone() {  
    	BaseEntity o = null;  
        try {  
            o = (BaseEntity) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    } 
}
