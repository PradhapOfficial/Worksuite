package com.worksuite.integration.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.DBUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

public class ScopeBeanImpl implements ScopeBean {

	private static final Logger LOGGER = LogManager.getLogger(IntegrationBeanImpl.class.getName());
	
	@Override
	public ScopePOJO addScopeDetails(final long orgId, final long appId, ScopePOJO scopePojo) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		DBUtil dbUtil = new DBUtil();
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			try {
				getScopeDetails(conn, orgId, appId);
				throw new RestException(ErrorCode.SCOPE_ALREADY_REGISTERED);
			}catch(RestException re) {
				if(re.getErrorCode() == ErrorCode.SCOPE_ALREADY_REGISTERED.getErrorCode()) {
					throw re;
				}
			}

			String query = "INSERT INTO Scope (APP_ID, ORG_ID, LEVEL, CREATED_BY, MODIFIED_BY, CREATED_TIME, MODIFIED_TIME) VALUES (?, ?, ?, ?, ?, ?, ?)";

			final long currentTime = System.currentTimeMillis();
			prep = conn.prepareStatement(query);
			prep.setLong(1, appId);
			prep.setLong(2, orgId);
			prep.setLong(3, scopePojo.getLevel());
			prep.setLong(4, scopePojo.getCreatedBy());
			prep.setLong(5, scopePojo.getModifiedBy());
			prep.setLong(6, currentTime);
			prep.setLong(7, currentTime);

			if (prep.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			return getScopeDetails(conn, orgId, appId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
	}

	@Override
	public ScopePOJO getScopeDetails(long orgId, long appId) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			return getScopeDetails(conn, orgId, appId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			dbUtil.closeConnection(conn, prep, null);
		}
	}

	@Override
	public ScopePOJO updateScopeDetails(long orgId, long appId, ScopePOJO scopePojo) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		DBUtil dbUtil = new DBUtil();
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			getScopeDetails(conn, orgId, appId);

			String query = "UPDATE Scope SET LEVEL = ?, MODIFIED_BY = ?, MODIFIED_TIME = ? WHERE ORG_ID = ? AND APP_ID = ?";

			prep = conn.prepareStatement(query);
			prep.setLong(1, scopePojo.getLevel());
			prep.setLong(2, scopePojo.getModifiedBy());
			prep.setLong(3, System.currentTimeMillis());
			prep.setLong(4, orgId);
			prep.setLong(5, appId);
			
			if (prep.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			return getScopeDetails(conn, orgId, appId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while updateScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
	}

	@Override
	public boolean deleteScopeDetails(long orgId, long appId) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			
			getScopeDetails(conn, orgId, appId);
			
			String query = "DELETE FROM Scope WHERE ORG_ID = ? AND APP_ID = ?";
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, appId);

			return prep.executeUpdate() != 0;
			
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}

	private ScopePOJO getScopeDetails(Connection conn, final long orgId, final long appId) throws RestException {
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM Scope WHERE ORG_ID = ? AND APP_ID = ?";
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, appId);

			rs = prep.executeQuery();
			if (!rs.next()) {
				throw new RestException(ErrorCode.SCOPE_NOT_REGISTERED);
			}
			return ScopePOJO.convertResultSetToPojo(rs);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}

}
