package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.DBUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

public class OrgBeanImpl implements OrgBean {

	private static final Logger LOGGER = LogManager.getLogger(OrgBeanImpl.class);
	
	@Override
	public OrgPOJO getOrgDetails(final long userId, final long orgId) throws RestException{
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			return getOrgDetailsPrep(conn, userId, orgId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while addAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
	}
	
	@Override
	public OrgPOJO getFirstOrgDetails(long userId) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();
			String query = "SELECT * FROM OrganizationUserMapping INNER JOIN Organization ON OrganizationUserMapping.ORG_ID = Organization.ORG_ID INNER JOIN Role ON OrganizationUserMapping.ROLE_ID = Role.ROLE_ID WHERE OrganizationUserMapping.USER_ID = ?";
			return getOrgDetailsPrep(conn, userId, null, query);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getFirstOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
	}

	@Override
	public OrgPOJO addOrgDetails(final long userId, OrgPOJO orgPojo) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "INSERT INTO Organization(ORG_NAME, CREATED_BY, MODIFIED_BY, CREATED_TIME, MODIFIED_TIME, STATUS) VALUES (?, ?, ?, ?, ?, ?)";

			conn = DBUtil.getConnection();

			prep = conn.prepareStatement(query);

			final long currentTime = System.currentTimeMillis();
			orgPojo.setCreatedTime(currentTime).setModifiedTime(currentTime);

			new OrgPOJO().convertPojoToPrep(prep, orgPojo);

			if (prep.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			query = new StringBuilder("SELECT * FROM Organization WHERE CREATED_BY = ").append(userId)
					.append(" AND CREATED_TIME = ").append(currentTime).toString();

			rs = prep.executeQuery(query);
			if (!rs.next()) {
				return null;
			}

			final long orgId = rs.getLong("ORG_ID");

			query = new StringBuilder("SELECT ROLE_ID FROM Role WHERE ROLE_VALUE = 1").toString();
			rs = prep.executeQuery(query);

			if (!rs.next()) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			final long roleId = rs.getLong("ROLE_ID");
			new DBUtil().closeConnection(null, prep, rs);

			query = "INSERT INTO OrganizationUserMapping(ORG_ID, USER_ID, ROLE_ID) VALUES (?, ?, ?)"; // Role Id - 1 -->
																										// Super Admin
			prep = conn.prepareStatement(query);

			prep.setLong(1, orgId);
			prep.setLong(2, userId);
			prep.setLong(3, roleId);

			if (prep.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			orgPojo = getOrgDetailsPrep(conn, userId, orgId);

			return orgPojo;
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while addOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
	}

	@Override
	public OrgPOJO updateOrgDetails(final long userId, final long orgId, OrgPOJO orgPojo) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();

			getOrgDetailsPrep(conn, userId, orgId);

			StringBuilder queryBuilder = new StringBuilder("UPDATE Organization SET ");
			if (orgPojo.getOrgName() != null) {
				queryBuilder.append("ORG_NAME = ?");
			}

			if (orgPojo.getStatus() != null) {
				queryBuilder.append(orgPojo.getOrgName() != null ? ", " : "");
				queryBuilder.append("STATUS = ?");
			}

			if (orgPojo.getModifiedBy() != null) {
				queryBuilder.append(", MODIFIED_BY = ?");
			}

			final long currentTime = System.currentTimeMillis();
			orgPojo.setModifiedTime(currentTime);

			if (orgPojo.getModifiedTime() != null) {
				queryBuilder.append(", MODIFIED_TIME = ?");
			}

			queryBuilder.append(" WHERE ORG_ID = ?");

			prep = conn.prepareStatement(queryBuilder.toString());

			if (orgPojo.getOrgName() != null) {
				prep.setString(1, orgPojo.getOrgName());
			}

			if (orgPojo.getStatus() != null) {
				prep.setInt(2, orgPojo.getStatus());
			}

			if (orgPojo.getModifiedBy() != null) {
				prep.setLong(3, orgPojo.getModifiedBy());
			}

			if (orgPojo.getModifiedTime() != null) {
				prep.setLong(4, orgPojo.getModifiedTime());
			}

			prep.setLong(5, orgId);

			if (prep.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}

			orgPojo = getOrgDetailsPrep(conn, userId, orgId);

			return orgPojo;
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while updateOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
	}

	@Override
	public boolean deleteOrgDetails(final long userId, final long orgId) throws RestException {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();

			getOrgDetailsPrep(conn, userId, orgId);

			String query = "DELETE FROM Organization WHERE ORG_ID = ?";
			prep = conn.prepareStatement(query);

			prep.setLong(1, orgId);

			return (prep.executeUpdate() > 0);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while deleteOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
	}

	private OrgPOJO getOrgDetailsPrep(Connection conn, final long userId, final long orgId) throws RestException {
		String query = "SELECT * FROM OrganizationUserMapping INNER JOIN Organization ON OrganizationUserMapping.ORG_ID = Organization.ORG_ID INNER JOIN Role ON OrganizationUserMapping.ROLE_ID = Role.ROLE_ID WHERE OrganizationUserMapping.USER_ID = ? AND OrganizationUserMapping.ORG_ID = ?";
		return getOrgDetailsPrep(conn, userId, orgId, query);
	}

	private OrgPOJO getOrgDetailsPrep(Connection conn, long userId, Long orgId, String query)
			throws RestException {
		PreparedStatement prep = null;
		ResultSet rs = null;

		try {
			prep = conn.prepareStatement(query);

			prep.setLong(1, userId);

			if(orgId != null) {
				prep.setLong(2, orgId);
			}
			
			rs = prep.executeQuery();

			if (!rs.next()) {
				throw new RestException(ErrorCode.INVALID_USER_NOT_PRESENT_IN_ORG);
			}

			return OrgPOJO.convertResultSetToPojo(rs).setRoleDetails(RolePOJO.convertResultSetToPojo(rs));
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getOrgDetailsPrep :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
}
