package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.DBUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

public class AccountsBeanImpl implements AccountsBean {
	
	private static final Logger LOGGER = LogManager.getLogger(AccountsBeanImpl.class);

	@Override
	public UserPOJO addAccountDetails(AccountsPOJO accountsPojo, UserPOJO userPojo) throws RestException {
		DBUtil dbUtil = new DBUtil();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String query = "INSERT INTO ACCOUNTS(EMAIL_ID, PASSWORD) VALUES(?, ?)";

			connection = DBUtil.getConnection();

			if(isAccountExists(connection, userPojo.getEmailId())) {
				throw new RestException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
			}
			
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, accountsPojo.getEmailId());
			preparedStatement.setString(2, accountsPojo.getPassWord());

			if (preparedStatement.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			dbUtil.closeConnection(preparedStatement);

			query = "INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL_ID) VALUES(?, ?, ?)";
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, userPojo.getFirstName());
			preparedStatement.setString(2, userPojo.getLastName());
			preparedStatement.setString(3, userPojo.getEmailId());

			if (preparedStatement.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			dbUtil.closeConnection(preparedStatement);

			query = "SELECT * FROM USER WHERE EMAIL_ID = ?";
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, userPojo.getEmailId());

			resultSet = preparedStatement.executeQuery();
			if (!resultSet.next()) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			return UserPOJO.convertResultSetToPojo(resultSet);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while addAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			dbUtil.closeConnection(connection, preparedStatement, resultSet);
		}
	}

	@Override
	public boolean updateAccountDetails(AccountsPOJO accountsPojo) throws RestException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String query = "UPDATE Accounts SET PASSWORD = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, accountsPojo.getPassWord());
			return (preparedStatement.executeUpdate() > 0) ? true : false;
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while updateAccountDetails :: ", e);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
		return false;
	}

	@Override
	public boolean deleteAccountDetails(String emailId) throws RestException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			String query = "DELETE FROM Accounts WHERE EMAIL_ID = ?";

			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, emailId);
			return (preparedStatement.executeUpdate() > 0) ? true : false;
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while deleteAccountDetails :: ", e);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
		return false;
	}

	@Override
	public boolean isAccountExists(String emailId) throws RestException {
		Connection connection = null;
		
		ResultSet resultSet = null;
		try {
			connection = DBUtil.getConnection();
			return isAccountExists(connection, emailId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while isAccountExists :: ", e);
		} finally {
			new DBUtil().closeConnection(connection, null, resultSet);
		}
		return false;
	}
	
	private boolean isAccountExists(Connection connection, String emailId) throws SQLException {
		String query = "SELECT * FROM Accounts WHERE EMAIL_ID = ?";
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, emailId);

		ResultSet resultSet = preparedStatement.executeQuery();
		return resultSet.next();
	}

	@Override
	public UserPOJO getAccountDetails(String emailId, String password) throws RestException {
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
			if (!resultSet.next()) {
				throw new RestException(ErrorCode.INVALID_EMAIL_ID_AND_PASSWORD);
			}

			return UserPOJO.convertResultSetToPojo(resultSet);
		}catch(RestException re) {
			throw re;
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(connection, preparedStatement, resultSet);
		}
	}
}
