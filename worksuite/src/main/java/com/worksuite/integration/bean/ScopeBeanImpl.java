package com.worksuite.integration.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.worksuite.db.util.DBUtil;

public class ScopeBeanImpl implements ScopeBean {

	@Override
	public ScopePOJO addScopeDetails(final long orgId, final long appId, ScopePOJO scopePojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		DBUtil dbUtil = new DBUtil();
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			if(getScopeDetails(conn, orgId, appId) != null) {
				throw new Exception("Already Exists");
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
				throw new Exception("Exception while add scope");
			}

			return getScopeDetails(conn, orgId, appId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public ScopePOJO getScopeDetails(long orgId, long appId) {
		Connection conn = null;
		PreparedStatement prep = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			return getScopeDetails(conn, orgId, appId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(conn, prep, null);
		}
		return null;
	}

	@Override
	public ScopePOJO updateScopeDetails(long orgId, long appId, ScopePOJO scopePojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		DBUtil dbUtil = new DBUtil();
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			if(getScopeDetails(conn, orgId, appId) == null) {
				throw new Exception("No data found");
			}

			String query = "UPDATE Scope SET LEVEL = ?, MODIFIED_BY = ?, MODIFIED_TIME = ? WHERE ORG_ID = ? AND APP_ID = ?";

			prep = conn.prepareStatement(query);
			prep.setLong(1, scopePojo.getLevel());
			prep.setLong(2, scopePojo.getModifiedBy());
			prep.setLong(3, System.currentTimeMillis());
			prep.setLong(4, orgId);
			prep.setLong(5, appId);
			
			if (prep.executeUpdate() == 0) {
				throw new Exception("Exception while update scope");
			}

			return getScopeDetails(conn, orgId, appId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public boolean deleteScopeDetails(long orgId, long appId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			
			if(getScopeDetails(conn, orgId, appId) == null) {
				throw new Exception("No data found");
			}
			
			String query = "DELETE FROM Scope WHERE ORG_ID = ? AND APP_ID = ?";
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, appId);

			return prep.executeUpdate() != 0;
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return false;
	}

	private ScopePOJO getScopeDetails(Connection conn, final long orgId, final long appId) {
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM Scope WHERE ORG_ID = ? AND APP_ID = ?";
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, appId);

			rs = prep.executeQuery();
			if (rs.next()) {
				return ScopePOJO.convertResultSetToPojo(rs);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return null;
	}

}
