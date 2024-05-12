package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.DBUtil;

public class TokenMappingBeanImpl implements TokenMappingBean {

	private static final Logger LOGGER = LogManager.getLogger(TokenMappingBeanImpl.class);
	
	@Override
	public TokenMappingPojo getToken(long userId, String token) {
		Connection conn = null;
		try {
			return getToken(conn, userId, token);
		}catch(Exception e) {
			LOGGER.error("Exception Occured while getToken :: " + e);
		} finally {
			new DBUtil().closeConnection(conn);
		}
		return null;
	}
	
	public TokenMappingPojo getToken(Connection conn, long userId, String token) throws Exception {
		String query = "SELECT * FROM TokenMapping WHERE USER_ID = ? AND TOKEN = ?";
		
		conn = DBUtil.getConnection();
		PreparedStatement prep = conn.prepareStatement(query);
		
		prep.setLong(1, userId);
		prep.setString(2, token);
		ResultSet rs = prep.executeQuery();
		
		TokenMappingPojo tokenMappingPojo = null;
		if(rs.next()) {
			tokenMappingPojo = TokenMappingPojo.convertResultSetToPojo(rs);
		}
		if(rs != null) {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return tokenMappingPojo;
	}

	@Override
	public TokenMappingPojo addToken(TokenMappingPojo tokenMappingPojo) {
		Connection conn = null;
		TokenMappingPojo resultTokenPojo = null;
		PreparedStatement prep = null;
		try {
			resultTokenPojo = getToken(conn, tokenMappingPojo.getUserId(), tokenMappingPojo.getToken());
			if(resultTokenPojo != null) {
				return resultTokenPojo;
			}
			
			String query = "INSERT INTO TokenMapping (USER_ID, TOKEN) VALUES(?, ?)";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			
			prep.setLong(1, tokenMappingPojo.getUserId());
			prep.setString(2, tokenMappingPojo.getToken());
			
			int status = prep.executeUpdate();
			if(status > 0) {
				return getToken(conn, tokenMappingPojo.getUserId(), tokenMappingPojo.getToken());
			}
			
			LOGGER.info("No Rows Found");
		}catch(Exception e) {
			LOGGER.error("Exception Occured while addToken :: " + e);
		} finally {
			new DBUtil().closeConnection(conn, prep, null);
		}
		return resultTokenPojo;
	}

	@Override
	public TokenMappingPojo updateToken(TokenMappingPojo tokenMappingPojo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteToken(long userId, String token) {
		Connection conn = null;
		TokenMappingPojo resultTokenPojo = null;
		PreparedStatement prep = null;
		try {
			resultTokenPojo = getToken(conn, userId, token);
			if(resultTokenPojo == null) {
				LOGGER.info("No Rows Found");
				return false;
			}
			
			String query = "DELETE FROM TokenMapping WHERE USER_ID = ?, TOKEN = ?";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			
			prep.setLong(1, userId);
			prep.setString(2, token);
			
			int status = prep.executeUpdate();
			if(status > 0) {
				return true;
			}
			
			LOGGER.info("Unable to delete the row");
		}catch(Exception e) {
			LOGGER.error("Exception Occured while deleteToken :: " + e);
		} finally {
			new DBUtil().closeConnection(conn, prep, null);
		}
		return false;
	}

}
