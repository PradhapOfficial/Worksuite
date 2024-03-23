package com.worksuite.core.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class UserBeanImpl implements UserBean {

	@Override
	public UserPOJO getUserDetails(final long userId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM User WHERE USER_ID = ?";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			prep.setLong(1, userId);
			rs = prep.executeQuery();
			if (rs.next()) {
				return UserPOJO.convertResultSetToPojo(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public UserPOJO updateUserDetails(final long userId, UserPOJO userPojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			StringBuilder queryBuilder = new StringBuilder("UPDATE User SET ");
			if (userPojo.getFirstName() != null) {
				queryBuilder.append("FIRST_NAME = ?");
			}

			if (userPojo.getLastName() != null) {
				queryBuilder.append(userPojo.getFirstName() != null ? ", " : "");
				queryBuilder.append("LAST_NAME = ?");
			}

			queryBuilder.append(" WHERE USER_ID = ?");

			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(queryBuilder.toString());

			if (userPojo.getFirstName() != null) {
				prep.setString(1, userPojo.getFirstName());
			}

			if (userPojo.getLastName() != null) {
				prep.setString(2, userPojo.getLastName());
			}

			prep.setLong(3, userId);

			if (prep.executeUpdate() > 0) {

				String query = new StringBuilder("SELECT * FROM User WHERE USER_ID = ").append(userId).toString();

				rs = prep.executeQuery(query);

				if (rs.next()) {
					return UserPOJO.convertResultSetToPojo(rs);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public boolean deleteUserDetails(final long userId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM User WHERE USER_ID = ?";

			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);

			prep.setLong(1, userId);

			rs = prep.executeQuery();

			if (rs.next()) {
				UserPOJO userPojo = UserPOJO.convertResultSetToPojo(rs);
				if (userPojo.getEmailId() != null) {
					String delQuery = new StringBuilder("DELETE FROM Accounts WHERE EMAIL_ID = ")
							.append("\'").append(userPojo.getEmailId()).append("\'").toString();
					return prep.executeUpdate(delQuery) > 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return false;
	}

	@Override
	public List<UserMasterPOJO> getListOfUserDetails(long orgId){
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM User INNER JOIN OrganizationUserMapping ON User.USER_ID = OrganizationUserMapping.USER_ID INNER JOIN Organization ON OrganizationUserMapping.ORG_ID = Organization.ORG_ID INNER JOIN Role ON OrganizationUserMapping.ROLE_ID = Role.ROLE_ID WHERE OrganizationUserMapping.ORG_ID = ?";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			rs = prep.executeQuery();
			
			List<UserMasterPOJO> listOfUsers = new ArrayList<UserMasterPOJO>();
			while(rs.next()) {
				OrgPOJO orgPojo = OrgPOJO.convertResultSetToPojo(rs).setRoleDetails(RolePOJO.convertResultSetToPojo(rs));

				listOfUsers.add(new UserMasterPOJO().setUserDetails(UserPOJO.convertResultSetToPojo(rs)).setOrgDetails(orgPojo));
			}
			return listOfUsers;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}
	
	@Override
	public UserMasterPOJO getUserDetailsById(final long userId, final long orgId) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM User INNER JOIN OrganizationUserMapping ON User.USER_ID = OrganizationUserMapping.USER_ID INNER JOIN Organization ON OrganizationUserMapping.ORG_ID = Organization.ORG_ID INNER JOIN Role ON OrganizationUserMapping.ROLE_ID = Role.ROLE_ID WHERE OrganizationUserMapping.USER_ID = ? AND OrganizationUserMapping.ORG_ID = ?";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			prep.setLong(1, userId);
			prep.setLong(2, orgId);

			rs = prep.executeQuery();
			if (!rs.next()) {
				throw new Exception("No Row Found");
			}

			OrgPOJO orgPojo = OrgPOJO.convertResultSetToPojo(rs).setRoleDetails(RolePOJO.convertResultSetToPojo(rs));

			return new UserMasterPOJO().setUserDetails(UserPOJO.convertResultSetToPojo(rs)).setOrgDetails(orgPojo);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			new DBUtil().closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public boolean addUserDetails(final long userId, final long orgId, final JsonObject newUserObj) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			String query = "SELECT USER_ID FROM OrganizationUserMapping WHERE USER_ID = ? AND ORG_ID = ?";
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			prep.setLong(1, userId);
			prep.setLong(2, orgId);

			rs = prep.executeQuery();

			if (!rs.next()) {
				throw new Exception("Invalid userId");
			}
			
//			if(UserPOJO.convertResultSetToPojo(rs) == null) {
//				throw new Exception("User not a org space");
//			}
			
			dbUtil.closeConnection(rs);
			query = new StringBuilder("SELECT ROLE_ID FROM Role WHERE ROLE_VALUE = ").append(newUserObj.get("role").getAsLong()).toString();
			
			rs = prep.executeQuery(query);
			
			if(!rs.next()) {
				throw new Exception("Invalid Role Id");
			}
			
			final long roleId = rs.getLong("ROLE_ID");
			
			dbUtil.closeConnection(null, prep, rs);
			query = "INSERT INTO OrganizationUserMapping(ORG_ID, USER_ID, ROLE_ID) VALUES (?, ?, ?)";
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, newUserObj.get("userId").getAsLong());
			prep.setLong(3, roleId);
			return prep.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
		return false;
	}

	@SuppressWarnings("resource")
	@Override
	public boolean addListOfUserDetails(long userId, long orgId, JsonArray newUsersList) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			String query = "SELECT USER_ID FROM OrganizationUserMapping WHERE USER_ID = ? AND ORG_ID = ?";

			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);

			prep.setLong(1, userId);
			prep.setLong(2, orgId);

			rs = prep.executeQuery();

			if (!rs.next()) {
				throw new Exception("Invalid userId");
			}
			
//			if(UserPOJO.convertResultSetToPojo(rs) == null) {
//				throw new Exception("User not a org space");
//			}
			
			dbUtil.closeConnection(rs);
			
			query = "SELECT ROLE_ID, ROLE_VALUE FROM Role";
			
			rs = prep.executeQuery(query);
			
			Map<Integer, Long> roleIdMap = new HashMap<Integer, Long>();
			while(rs.next()) {
				long roleId = rs.getLong("ROLE_ID");
				int roleValue = rs.getInt("ROLE_VALUE");
				
				roleIdMap.put(roleValue, roleId);
			}
			
			long newUserId;
			long newUserroleId;
			
			query = "INSERT INTO OrganizationUserMapping(ORG_ID, USER_ID, ROLE_ID) VALUES (?, ?, ?)";
			prep = conn.prepareStatement(query);
			
			for(JsonElement jsonEle : newUsersList) {
				newUserId = jsonEle.getAsJsonObject().get("userId").getAsLong();
				newUserroleId = roleIdMap.getOrDefault(jsonEle.getAsJsonObject().get("role").getAsInt(), 3l);
				
				prep.setLong(1, orgId);
				prep.setLong(2, newUserId);
				prep.setLong(3, newUserroleId);
				prep.addBatch();
			}
			
			int[] resultArray = prep.executeBatch();
			int failedCount = 0;
			int totalCount = resultArray.length;
			
			for(int res : resultArray) {
				if(!(res == PreparedStatement.SUCCESS_NO_INFO || res >= 0)) {
					failedCount++;
				}
			}
			System.out.println("Summary total record : " + totalCount + " Success count : " + (totalCount - failedCount) + "failed Count : " + failedCount);
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
		return false;
	}

}
