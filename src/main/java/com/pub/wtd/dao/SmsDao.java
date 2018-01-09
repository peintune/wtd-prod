package com.pub.wtd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.pub.wtd.common.SessionData;

/**
 * @Title: BaseDao.java 
 * @Package  
 * @Description: connection DB and supply base function
 * @author hekun 158109016@qq.com
 * @date 2014��7��15�� ����9:57:57 
 * @version V1.0   
 */
/**
 * @author hekun<158109016@qq.com>
 * 
 */
public class SmsDao extends BaseDao{

	/**
	 * initial the connection with DB
	 */
	public SmsDao(SessionData sessionData) {
		super(sessionData);
	}
	
	/**
	 * get the latest iAutoID from DB
	 * @return long:iAuotID
	 */
	public long getLatestId() {
		long id = 0;
		ResultSet rs = null;
		try {
			rs = executeSql("select iAutoID from smstable limit 1");
			while (rs.next()) {
				id = Long.parseLong(rs.getString("iAutoID"));
			}
		} catch (SQLException e) {
			logger.info("Get ID from DB failed", e);
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e) {
				logger.info("Close the result set connection failed", e);
			}
		}
		return id;
	}
	
	/**
	 * get the latest record from DB
	 * @return long:iAuotID
	 */
	public ResultSet getLatestRecord(){
		
		ResultSet rs=null;
		String[] recordArray=new String[2];
		try{
			rs=executeSql("select * from  tmstable order by iAutoID desc limit 1");
			while (rs.next()) {
				recordArray[0]=Long.parseLong(rs.getString("iAutoID"))+"";
				recordArray[1]=Long.parseLong(rs.getString("sMessage"))+"";
			}
		}catch(Exception e){
			logger.info("Get latest Record failed");
		}finally{
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e) {
				logger.info("Close the result set connection failed", e);
			}
		}
		return rs;		
	}
	/**
	 * execute the sql
	 */
	public ResultSet executeSql(String sql) {
		Statement stmt;
		ResultSet rs;
		try {
			stmt = conn.createStatement();

			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			logger.info("execute the sql failed", e);
			return null;
		}
		return rs;
	}
	
	/**
	 * close the connection
	 */
	public void closeConnection(){
		try{
		conn.commit();
		conn.close();
		}catch(Exception e){
			logger.info("Close DB connection failed",e);
		}
	}
}
