package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.worksuite.db.util.DBUtil;

public class UserBeanImpl implements UserBean{

	@Override
	public UserPOJO getUserDetails(long userId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM User WHERE USER_ID = ?";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			prep.setLong(1, userId);
			rs = prep.executeQuery();
			if(rs.next()) {
				return UserPOJO.convertResultSetToPojo(rs);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public UserPOJO updateUserDetails(long userId, UserPOJO userPojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			StringBuilder queryBuilder = new StringBuilder("UPDATE User SET ");
			if(userPojo.getFirstName() != null) {
				queryBuilder.append("FIRST_NAME = ?");
			}
			
			if(userPojo.getLastName() != null) {
				queryBuilder.append(userPojo.getFirstName() != null ? ", " : "");
				queryBuilder.append("LAST_NAME = ?");
			}
			
			queryBuilder.append(" WHERE USER_ID = ?");
			
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(queryBuilder.toString());
			
			if(userPojo.getFirstName() != null) {
				prep.setString(1, userPojo.getFirstName());
			}
			
			if(userPojo.getLastName() != null) {
				prep.setString(2, userPojo.getLastName());
			}
			
			prep.setLong(3, userId);
			
			if(prep.executeUpdate() > 0) {
				
				String query = new StringBuilder("SELECT * FROM User WHERE USER_ID = ").append(userId).toString();
				
				rs = prep.executeQuery(query);
				
				if(rs.next()) {
					return UserPOJO.convertResultSetToPojo(rs);	
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}
	
	@Override
	public boolean deleteUserDetails(long userId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM User WHERE USER_ID = ?";
			
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			
			prep.setLong(1, userId);
			
			rs = prep.executeQuery();
			
			if(rs.next()) {
				UserPOJO userPojo =  UserPOJO.convertResultSetToPojo(rs);
				if(userPojo.getEmailId() != null) {
					String delQuery = new StringBuilder("DELETE FROM Accounts WHERE EMAIL_ID = ").append("\'" + userPojo.getEmailId() + "\'").toString();
					return prep.executeUpdate(delQuery) > 0; 
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return false;
	}

}
