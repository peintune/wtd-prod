package com.pub.wtd.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

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
public class BaseDao {

	protected String hostName;
	private String url = "";
	private String user = "devadmin";
	private String pwd = "devadmin1";
	Logger logger;
	Connection conn;

	/**
	 * initial the connection with DB
	 */
	public BaseDao(SessionData sessionData) {
		this.hostName = sessionData.getHostName();
		this.logger = sessionData.getLogger();
		chooseDB();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("Connect to DB Error", e);
		}

	}

	/**
	 * choose which DB to connection by the hostName
	 */
	private void chooseDB() {

		if (hostName.contains("release.com")) {
			url = "jdbc:mysql://127.0.0.1:3306/release_envdb";
			user = "user";
			pwd = "passwd";
		} else if (hostName.contains("onlinehost.com")) {
			url = "jdbc:mysql://127.0.0.1:3306/online_db";
			user = "user";
			pwd = "passwd";
		} else if (hostName.contains("other.host.com")) {
			url = "jdbc:mysql://127.0.0.1:3306/db_db";
			user = "user";
			pwd = "passwd";
		}
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
	protected void closeConnection() {
		try {
			conn.commit();
			conn.close();
		} catch (Exception e) {
			logger.info("Close DB connection failed", e);
		}
	}
}
