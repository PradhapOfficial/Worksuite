package com.worksuite.integration.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.worksuite.db.util.DBUtil;

public class IntegrationBeanImpl implements IntegrationBean {

	@Override
	public IntegrationMasterPOJO addIntegDetails(final Long orgId, final Long userId, final Long departmentId, IntegrationPOJO integrationPojo, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			getDetails(conn, orgId, integrationPojo);
			
			String query = "INSERT INTO Integration (APP_ID, STATUS, ORG_ID, LEVEL, CREATED_BY, MODIFIED_BY, CREATED_TIME, MODIFIED_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			long currentTime = System.currentTimeMillis();
			integrationPojo.setCreatedTime(currentTime)
				.setModifiedTime(currentTime);
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, integrationPojo.getAppId());
			prep.setBoolean(2, integrationPojo.getStatus());
			prep.setLong(3, integrationPojo.getOrgId());
			prep.setLong(4, integrationPojo.getLevel());
			prep.setLong(5, integrationPojo.getCreatedBy());
			prep.setLong(6, integrationPojo.getModifiedBy());
			prep.setLong(7, integrationPojo.getCreatedTime());
			prep.setLong(8, integrationPojo.getModifiedTime());
			
			if(prep.executeUpdate() == 0) {
				throw new Exception("Unable to add the data");
			}
			
			int level = integrationPojo.getLevel();
			final long uniqueId;
			switch(level) {
				case 2:
					uniqueId = departmentId;
					break;
				case 3:
					uniqueId = userId;
					break;
				default:
					uniqueId = orgId;
					break;
			}
			
			integrationPojo = getDetailsIntegDetails(conn, integrationPojo);
			addToTables(conn, uniqueId, integrationPojo.getIntegrationId(), level, authPojo, listOfIntegPropPojo);
			return getDetailsByIntegId(conn, integrationPojo.getIntegrationId(), level);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public IntegrationPOJO updateIntegDetails(long orgId, long userId, IntegrationPOJO integrationPojo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMasterPOJO getIntegDetails(long integrationId, int level) {
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			return getDetailsByIntegId(conn, integrationId, level);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
		return null;
	}

	@Override
	public List<IntegrationPOJO> getListOfIntegDetails(long orgId, long userId, long integrationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteIntegnDetails(long orgId, long userId, long integrationId) throws Exception{
		Connection conn = null;
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			String query = "DELETE FROM Integration WHERE INTEGRATION_ID = ?";
			
			conn = DBUtil.getConnection();
			prep = conn.prepareStatement(query);
			prep.setLong(1, integrationId);
			
			if(prep.executeUpdate() > 0) {
				return true;
			}
		}catch(Exception e) {
			throw e;
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return false;
	}
	
	private IntegrationMasterPOJO getDetailsByIntegId(Connection conn, final long integId, final int level) throws Exception{
		String query = null;
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			if(level == 1) {
				query = "SELECT * FROM IntegrationOrgMapping INNER JOIN Integration ON IntegrationOrgMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.INTEGRATION_ID = ?";
			}else if(level == 2) {
				query = "SELECT * FROM IntegrationDepartmentMapping INNER JOIN Integration ON IntegrationDepartmentMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.INTEGRATION_ID = ?";
			}else {
				query = "SELECT * FROM IntegrationUserMapping INNER JOIN Integration ON IntegrationUserMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.INTEGRATION_ID = ?";
			}
			
		
			prep = conn.prepareStatement(query);
			prep.setLong(1, integId);
			rs = prep.executeQuery();
			List<IntegrationPropertyPOJO> listOfIntegProperty = new ArrayList<IntegrationPropertyPOJO>();
			IntegrationPOJO integrationPojo = null;
			AuthPOJO authPojo = null;
			while(rs.next()) {
				integrationPojo =  IntegrationPOJO.convertResultSetToPojo(rs);
				authPojo = AuthPOJO.convertResultSetToPojo(rs);
				listOfIntegProperty.add(IntegrationPropertyPOJO.convertResultSetToPojo(rs));
			}
			
			if(integrationPojo == null) {
				return null;
			}
			
			return  new IntegrationMasterPOJO()
					.setIntegrationDetails(integrationPojo)
					.setAuthDetails(authPojo)
					.setPropertyDetails(listOfIntegProperty);
		}catch(Exception e) {
			throw e;
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private IntegrationPOJO getDetailsIntegDetails(Connection conn, IntegrationPOJO integPojo) throws Exception{
		String query = null;
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			query = "SELECT * FROM Integration WHERE CREATED_BY = ? AND CREATED_TIME = ? AND APP_ID = ? AND ORG_ID = ? AND LEVEL = ?";
			prep = conn.prepareStatement(query);
			prep.setLong(1, integPojo.getCreatedBy());
			prep.setLong(2, integPojo.getCreatedTime());
			prep.setLong(3, integPojo.getAppId());
			prep.setLong(4, integPojo.getOrgId());
			prep.setLong(5, integPojo.getLevel());
			
			rs = prep.executeQuery();
		
			if(rs != null && rs.next()) {
				return IntegrationPOJO.convertResultSetToPojo(rs);
			}
		}catch(Exception e) {
			throw e;
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return null;
	}
	
	private IntegrationMasterPOJO getDetails(Connection conn, final long uniqueId, IntegrationPOJO integrationPojo) throws Exception{
		String query = null;
		PreparedStatement prep = null; 
		ResultSet rs = null;
		int level = integrationPojo.getLevel();
		try {
			if(level == 1) {
				query = "SELECT * FROM IntegrationOrgMapping INNER JOIN Integration ON IntegrationOrgMapping.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE IntegrationOrgMapping.ORG_ID = ? AND Integration.APP_ID = ? AND Integration.LEVEL = ?";
			}else if(level == 2) {
				query = "SELECT * FROM IntegrationDepartmentMapping INNER JOIN Integration ON IntegrationDepartmentMapping.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE IntegrationDepartmentMapping.DEPARTMENT_ID = ? AND Integration.APP_ID = ? AND Integration.LEVEL = ?";
			}else if(level == 3) {
				query = "SELECT * FROM IntegrationUserMapping INNER JOIN Integration ON IntegrationUserMapping.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE IntegrationUserMapping.USER_ID = ? AND Integration.APP_ID = ? AND Integration.LEVEL = ?";
			}
			
			if(query == null) {
				throw new Exception("Query cannot be null");
			}
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, uniqueId);
			prep.setLong(2, integrationPojo.getAppId());
			prep.setLong(3, integrationPojo.getLevel());
			rs = prep.executeQuery();
		
			List<IntegrationPropertyPOJO> listOfIntegProperty = new ArrayList<IntegrationPropertyPOJO>();
			AuthPOJO authPojo = null;
			while(rs.next()) {
				integrationPojo =  IntegrationPOJO.convertResultSetToPojo(rs);
				authPojo = AuthPOJO.convertResultSetToPojo(rs);
				listOfIntegProperty.add(IntegrationPropertyPOJO.convertResultSetToPojo(rs));
			}
			return  new IntegrationMasterPOJO()
					.setIntegrationDetails(integrationPojo)
					.setAuthDetails(authPojo)
					.setPropertyDetails(listOfIntegProperty);
		}catch(Exception e) {
			throw e;
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private boolean addToTables(Connection conn, final long uniqueId,  final long integrationId, final int level, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo) throws Exception{
		String query = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			
			if(level == 1) {
				query = "INSERT INTO IntegrationOrgMapping(ORG_ID, INTEGRATION_ID) VALUES (?, ?)";
			}else if(level == 2) {
				query = "INSERT INTO IntegrationDepartmentMapping(DEPARTMENT_ID, INTEGRATION_ID) VALUES (?, ?)";
			}else if(level == 3) {
				query = "INSERT INTO IntegrationUserMapping(USER_ID, INTEGRATION_ID) VALUES (?, ?)";
			}
			
			if(query == null) {
				throw new Exception("Query cannot be null");
			}
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, uniqueId);
			prep.setLong(2, integrationId);
			if(!(prep.executeUpdate() > 0)) {
				
			}
			dbUtil.closeConnection(prep);
			
			query = "INSERT INTO auth(SCOPE, TOKEN, INTEGRATION_ID) VALUES (?, ?, ?)";
			prep = conn.prepareStatement(query);
			prep.setString(1, authPojo.getScope());
			prep.setString(2, authPojo.getToken());
			prep.setLong(3, integrationId);
			
			if(!(prep.executeUpdate() > 0)) {
				throw new Exception("Unable to add auth row");
			}	
			dbUtil.closeConnection(prep);
			
			query = "INSERT INTO IntegrationProperty(PROPERTY_NAME, PROPERTY_VALUE, INTEGRATION_ID) VALUES (?, ?, ?)";
			prep = conn.prepareStatement(query);
			for(IntegrationPropertyPOJO integProPojo : listOfIntegPropPojo) {
				prep.setString(1, integProPojo.getPropertyName());
				prep.setString(2, integProPojo.getPropertyValue());
				prep.setLong(3, integrationId);
				prep.addBatch();
			}
			
			int successCnt = 0;
			int[] resultArray = prep.executeBatch();
			for(int result : resultArray) {
				if(result == PreparedStatement.SUCCESS_NO_INFO || result >=0 ) {
					successCnt++;
				}
			}
			System.out.println("Total Count : " + listOfIntegPropPojo.size() + " Success count : " + successCnt + " Failure count : " + (listOfIntegPropPojo.size() - successCnt));
			return true;
		}catch(Exception e) {
			throw e;
		}finally {
			dbUtil.closeConnection(null, prep, rs);
		}
	}

	@Override
	public IntegrationPOJO getIntegDetails(long orgId, long userId, long integrationId) {
		// TODO Auto-generated method stub
		return null;
	}
}
