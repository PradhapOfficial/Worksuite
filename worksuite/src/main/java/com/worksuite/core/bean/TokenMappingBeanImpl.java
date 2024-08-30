package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.ApplicationUtils;
import com.worksuite.db.util.DBUtil;

public class TokenMappingBeanImpl implements TokenMappingBean {

	private static final Logger LOGGER = LogManager.getLogger(TokenMappingBeanImpl.class);
	
	@Override
	public TokenMappingPojo getToken(long userId, String token) {
		try(Connection conn = DBUtil.getConnection()) {
			return getToken(conn, userId, token);
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getToken :: ", e);
		}
		return null;
	}
	
	private TokenMappingPojo getToken(Connection conn, long userId, String token) throws Exception {
		String query = "SELECT * FROM TokenMapping WHERE USER_ID = ? AND TOKEN = ?";
		try(PreparedStatement prep = conn.prepareStatement(query)) {
			
			prep.setLong(1, userId);
			prep.setString(2, token);
			ResultSet rs = prep.executeQuery();
			
			if(rs.next()) {
				return TokenMappingPojo.convertResultSetToPojo(rs);
			}
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getToken :: ", e);
		}
		return null;
	}
	
	private TokenMappingPojo getToken(Connection conn, long userId) throws Exception {
		String query = "SELECT * FROM TokenMapping WHERE USER_ID = ?";
		try(PreparedStatement prep = conn.prepareStatement(query)) {
			
			prep.setLong(1, userId);
			ResultSet rs = prep.executeQuery();
			
			if(rs.next()) {
				return TokenMappingPojo.convertResultSetToPojo(rs);
			}
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getToken :: ", e);
		}
		return null;
	}

	@Override
	public TokenMappingPojo addToken(TokenMappingPojo tokenMappingPojo) {
		//Connection conn = null;
		TokenMappingPojo resultTokenPojo = null;
		String query = "INSERT INTO TokenMapping (USER_ID, TOKEN) VALUES(?, ?)";
		
		try(Connection conn = DBUtil.getConnection(); PreparedStatement prep = conn.prepareStatement(query)) {
			if(!ApplicationUtils.isMultiSessionAllowed()) {
				resultTokenPojo = getToken(conn, tokenMappingPojo.getUserId());
				if(resultTokenPojo != null) {
					deleteToken(conn, tokenMappingPojo.getUserId());
				}
			}
			
			prep.setLong(1, tokenMappingPojo.getUserId());
			prep.setString(2, tokenMappingPojo.getToken());
			
			int status = prep.executeUpdate();
			if(status > 0) {
				return getToken(conn, tokenMappingPojo.getUserId(), tokenMappingPojo.getToken());
			}
			
			LOGGER.info("No Rows Found");
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addToken :: ", e);
		}
		return resultTokenPojo;
	}
	
	private TokenMappingPojo updateToken(Connection conn, long tokenMappingId, TokenMappingPojo tokenMappingPojo) {
		String query = "UPDATE TokenMapping SET TOKEN = ? WHERE TOKEN_MAPPING_ID = ?";
		
		try(PreparedStatement prep = conn.prepareStatement(query)) {
			String newToken = tokenMappingPojo.getToken();
			prep.setString(1, newToken);
			prep.setLong(2, tokenMappingId);
			
			int status = prep.executeUpdate();
			if(status > 0) {
				return getToken(conn, tokenMappingPojo.getUserId(), newToken);
			}
			
			LOGGER.info("No Rows Found");
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while updateToken :: ", e);
		}
		return null;
	}

	@Override
	public TokenMappingPojo updateToken(TokenMappingPojo tokenMappingPojo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteToken(long userId, String token) {
		String query = "DELETE FROM TokenMapping WHERE USER_ID = ?, TOKEN = ?";
		try(Connection conn = DBUtil.getConnection(); PreparedStatement prep = conn.prepareStatement(query)) {
			
			prep.setLong(1, userId);
			prep.setString(2, token);
			
			int status = prep.executeUpdate();
			if(status > 0) {
				return true;
			}
			
			LOGGER.info("Unable to delete the row");
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteToken :: ", e);
		}
		return false;
	}
	
	private boolean deleteToken(Connection conn, long userId) {
		String query = "DELETE FROM TokenMapping WHERE USER_ID = ?";
		try(PreparedStatement prep = conn.prepareStatement(query)) {
			prep.setLong(1, userId);
			int status = prep.executeUpdate();
			if(status > 0) {
				return true;
			}
			
			LOGGER.info("Unable to delete the row");
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteToken :: ", e);
		}
		return false;
	}

}
