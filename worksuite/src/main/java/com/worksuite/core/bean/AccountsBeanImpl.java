package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.worksuite.db.util.DBUtil;

public class AccountsBeanImpl implements AccountsBean{

	@Override
	public UserPOJO addAccountDetails(AccountsPOJO accountsPojo, UserPOJO userPojo) {
		DBUtil dbUtil = new DBUtil();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "INSERT INTO ACCOUNTS(EMAIL_ID, PASSWORD) VALUES(?, ?)";
			
			connection = DBUtil.getConnection();
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, accountsPojo.getEmailId());
			preparedStatement.setString(2, accountsPojo.getPassWord());
			
			if(preparedStatement.executeUpdate() > 0) {
				dbUtil.closeConnection(preparedStatement);
				
				query = "INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL_ID) VALUES(?, ?, ?)";
				preparedStatement = connection.prepareStatement(query);
				
				preparedStatement.setString(1, userPojo.getFirstName());
				preparedStatement.setString(2, userPojo.getLastName());
				preparedStatement.setString(3, userPojo.getEmailId());
				
				if(preparedStatement.executeUpdate() > 0) {
					dbUtil.closeConnection(preparedStatement);
					
					query = "SELECT * FROM USER WHERE EMAIL_ID = ?";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setString(1, userPojo.getEmailId());
					
					resultSet = preparedStatement.executeQuery();
					if(resultSet.next()) {
						return UserPOJO.convertResultSetToPojo(resultSet);
					}
				}
			}
		}catch(Exception e) {
			System.out.println("Exception occure while addAccountDetails bean impl :: " + e);
		} finally {
			dbUtil.closeConnection(connection, preparedStatement, resultSet);
		}
		return null;
	}

	@Override
	public boolean updateAccountDetails(AccountsPOJO accountsPojo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String query = "UPDATE Accounts SET PASSWORD = ?";
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, accountsPojo.getPassWord());
			return (preparedStatement.executeUpdate() > 0) ? true : false;
		}catch(Exception e) {
			System.out.println("Exception occure while updateAccountDetails bean impl :: " + e);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
		return false;
	}

	@Override
	public boolean deleteAccountDetails(String emailId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String query = "DELETE FROM Accounts WHERE EMAIL_ID = ?";
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			return (preparedStatement.executeUpdate() > 0) ? true : false;
		}catch(Exception e) {
			System.out.println("Exception occure while deleteAccountDetails bean impl :: " + e);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
		return false;
	}
	
	@Override
	public boolean isAccountExists(String emailId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String query = "SELECT * FROM Accounts WHERE EMAIL_ID = ?";
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}
		}catch(Exception e) {
			System.out.println("Exception occure while isAccountExists bean impl :: " + e);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
		return false;
	}
	
	@Override
	public UserPOJO getAccountDetails(String emailId, String password) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String query = "SELECT User.USER_ID, User.FIRST_NAME, User.LAST_NAME, User.EMAIL_ID FROM Accounts INNER JOIN User ON Accounts.EMAIL_ID = User.EMAIL_ID WHERE Accounts.EMAIL_ID = ? AND Accounts.PASSWORD = ?";
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			preparedStatement.setString(2, password);
			
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return UserPOJO.convertResultSetToPojo(resultSet);
			}
		}catch(Exception e) {
			System.out.println("Exception occure while getAccountDetails bean impl :: " + e);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
		return null;
	}
}
