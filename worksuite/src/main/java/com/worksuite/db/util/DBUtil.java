package com.worksuite.db.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DBUtil {

	private static final String JDBC_URL = System.getProperty("JDBC_URL") != null ? System.getProperty("JDBC_URL")
			: "jdbc:mysql://localhost:3306/worksuite";

	private static final String USER_NAME = System.getProperty("USER_NAME") != null ? System.getProperty("USER_NAME")
			: "root";

	private static final String PASSWORD = System.getProperty("PASSWORD") != null ? System.getProperty("PASSWORD")
			: "Pahp@123";

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
			System.out.println("Exception occured in hasColumn :: " + e);
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
			System.out.println("Exception occured while close connection : " + e);
		}
	}
}
