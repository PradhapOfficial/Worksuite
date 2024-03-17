package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.worksuite.db.util.DBUtil;

public class OrgBeanImpl implements OrgBean {

	@Override
	public OrgPOJO getOrgDetails(final long userId, final long orgId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();

			return getOrgDetailsPrep(conn, userId, orgId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public OrgPOJO addOrgDetails(final long userId, OrgPOJO orgPojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = null;
		try {
			String query = "INSERT INTO Organization(ORG_NAME, CREATED_BY, MODIFIED_BY, CREATED_TIME, MODIFIED_TIME, STATUS) VALUES (?, ?, ?, ?, ?, ?)";

			conn = DBUtil.getConnection();

			prep = conn.prepareStatement(query);

			final long currentTime = System.currentTimeMillis();
			orgPojo.setCreatedTime(currentTime).setModifiedTime(currentTime);

			new OrgPOJO().convertPojoToPrep(prep, orgPojo);

			if (prep.executeUpdate() == 0) {
				throw new Exception("Unable to insert");
			}

			query = new StringBuilder("SELECT * FROM Organization WHERE CREATED_BY = ").append(userId)
					.append(" AND CREATED_TIME = ").append(currentTime).toString();

			rs = prep.executeQuery(query);
			if (!rs.next()) {
				return null;
			}

			orgPojo = OrgPOJO.convertResultSetToPojo(rs);
			final long orgId = orgPojo.getOrgId();
			(dbUtil = new DBUtil()).closeConnection(prep);

			query = "INSERT INTO OrganizationUserMapping(ORG_ID, USER_ID) VALUES ( ?, ? )";
			prep = conn.prepareStatement(query);

			prep.setLong(1, orgId);
			prep.setLong(2, userId);

			if (prep.executeUpdate() == 0) {
				throw new Exception("Failed to update in mapping table");
			}

			return orgPojo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public OrgPOJO updateOrgDetails(final long userId, final long orgId, OrgPOJO orgPojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = null;
		try {
			conn = DBUtil.getConnection();

			if (getOrgDetailsPrep(conn, userId, orgId) == null) {
				throw new Exception("Invalid input");
			}

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
				throw new Exception("Unable to insert");
			}

			String query = new StringBuilder("SELECT * FROM Organization WHERE ORG_ID = ").append(orgId).toString();

			rs = prep.executeQuery(query);
			if (!rs.next()) {
				return null;
			}

			orgPojo = OrgPOJO.convertResultSetToPojo(rs);
			return orgPojo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public boolean deleteOrgDetails(final long userId, final long orgId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection();

			boolean isUserExistsInOrg = getOrgDetailsPrep(conn, userId, orgId) != null;
			if (!isUserExistsInOrg) {
				throw new Exception("User is not exisiting in org");
			}

			String query = "DELETE FROM Organization WHERE ORG_ID = ?";
			prep = conn.prepareStatement(query);

			prep.setLong(1, orgId);

			return (prep.executeUpdate() > 0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return false;
	}

	private OrgPOJO getOrgDetailsPrep(Connection conn, final long userId, final long orgId) throws Exception {
		String query = "SELECT * FROM OrganizationUserMapping INNER JOIN Organization ON OrganizationUserMapping.ORG_ID = Organization.ORG_ID WHERE OrganizationUserMapping.USER_ID = ? AND OrganizationUserMapping.ORG_ID = ?";
		return getOrgDetailsPrep(conn, userId, orgId, query);
	}

	private OrgPOJO getOrgDetailsPrep(Connection conn, final long userId, final long orgId, String query)
			throws Exception {
		PreparedStatement prep = null;
		ResultSet rs = null;

		try {
			prep = conn.prepareStatement(query);

			prep.setLong(1, userId);
			prep.setLong(2, orgId);

			rs = prep.executeQuery();
			if (rs.next()) {
				return OrgPOJO.convertResultSetToPojo(rs);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return null;
	}

}
