package com.worksuite.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.ApplicationConstants.DATA_BASE;

public class DBUtil {

	private static final String dbUrl = System.getProperty("JDBC_URL");
	private static final String dbUserName = System.getProperty("JDBC_USER_NAME");
	private static final String dbPassWord = System.getProperty("JDBC_PASSWORD");
	
	private static final String JDBC_URL = dbUrl != null ? dbUrl : ApplicationUtils.getProperty(DATA_BASE.DB_URL.toString());

	private static final String USER_NAME = dbUserName != null ? dbUserName : ApplicationUtils.getProperty(DATA_BASE.DB_USER_NAME.toString());

	private static final String PASSWORD = dbPassWord != null ? dbPassWord : ApplicationUtils.getProperty(DATA_BASE.DB_PASSWORD.toString());

	private static final Logger LOGGER = LogManager.getLogger(DBUtil.class);
	
	public static Connection getConnection() throws Exception {
		// Load DB Driver
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
		return connection;
	}

	public boolean hasColumn(ResultSetMetaData resultSetMetaData, String columnName) {
		try {
			int totalColumns = resultSetMetaData.getColumnCount();
			for (int i = 1; i <= totalColumns; i++) {
				if (columnName.equalsIgnoreCase(resultSetMetaData.getColumnName(i))) {
					return true;
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while hasColumn :: ", e);
		}
		return false;
	}

	public void closeConnection(Connection conn) {
		closeConnection(conn, null, null);
	}

	public void closeConnection(PreparedStatement prep) {
		closeConnection(null, prep, null);
	}

	public void closeConnection(ResultSet resultSet) {
		closeConnection(null, null, resultSet);
	}

	public void closeConnection(Connection conn, PreparedStatement prep, ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (prep != null) {
				prep.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while closeConnection :: ", e);
		}
	}
}
