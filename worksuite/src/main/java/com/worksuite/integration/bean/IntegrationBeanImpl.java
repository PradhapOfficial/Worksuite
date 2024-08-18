package com.worksuite.integration.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.DBUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

public class IntegrationBeanImpl implements IntegrationBean {

	private static final Logger LOGGER = LogManager.getLogger(IntegrationBeanImpl.class.getName());
	
	private String getIntegrationInsertQuery(int level) {
		String query = "INSERT INTO Integration (APP_ID, STATUS, ORG_ID, CREATED_BY, MODIFIED_BY, CREATED_TIME, MODIFIED_TIME";
		StringBuilder queryBuilder  = new StringBuilder(query.length() + 55)
				.append(query);
		if(level == 2) {
			queryBuilder.append(", DEPARTMENT_ID ");
		}else if(level == 3) {
			queryBuilder.append(", USER_ID ");
		}
		
		queryBuilder.append(") VALUES (?, ?, ?, ?, ?, ?, ?");
		if(level != 1) {
			queryBuilder.append(", ?");
		}
		
		queryBuilder.append(")");
		
		return queryBuilder.toString();
	}

	@Override
	public IntegrationMasterPOJO addIntegDetails(Long orgId, Long uniqueId, IntegrationPOJO integrationPojo, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo, int level) throws RestException{
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			
			try {
				return getIntegrationDetailsByScope(conn, orgId, uniqueId, getIntegrationByScopeQuery(level), level);
			}catch(RestException re) {
				LOGGER.log(Level.INFO, "Proceed to addIntegDetails :: ");
			}
			
			String query = getIntegrationInsertQuery(level);
			
			long currentTime = System.currentTimeMillis();
			integrationPojo.setCreatedTime(currentTime)
				.setModifiedTime(currentTime);
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, integrationPojo.getAppId());
			prep.setBoolean(2, integrationPojo.getStatus());
			prep.setLong(3, integrationPojo.getOrgId());
			prep.setLong(4, integrationPojo.getCreatedBy());
			prep.setLong(5, integrationPojo.getModifiedBy());
			prep.setLong(6, integrationPojo.getCreatedTime());
			prep.setLong(7, integrationPojo.getModifiedTime());
			
			if(level != 1) {
				prep.setLong(8, uniqueId);
			}
			
			if(prep.executeUpdate() == 0) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}
			
			//integrationPojo = getDetailsIntegDetails(conn, integrationPojo);
			long integrationId = getIntegrationDetailsByScope(conn, orgId, uniqueId, level);
			addToTables(conn, uniqueId, integrationId, level, authPojo, listOfIntegPropPojo);
			return getIntegrationDetailsByIntergrationId(conn, orgId, integrationId, getIntegrationByIntegrationIdQuery());
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
	}

	@Override
	public IntegrationPOJO updateIntegDetails(long orgId, long userId, IntegrationPOJO integrationPojo) throws RestException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegrationMasterPOJO getIntegrationDetailsByIntegrationId(long orgId, long integrationId) throws RestException{
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			
			return getIntegrationDetailsByIntergrationId(conn, orgId, integrationId, getIntegrationByIntegrationIdQuery());
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
	}
	
	@Override
	public IntegrationMasterPOJO getIntegDetailsByLevel(long orgId, long uniqueId, int level) throws RestException{
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			return getIntegrationDetailsByScope(conn, orgId, uniqueId, getIntegrationByScopeQuery(level), level);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegDetailsByLevel :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			dbUtil.closeConnection(conn, prep, rs);
		}
	}

	@Override
	public List<IntegrationPOJO> getListOfIntegDetails(long orgId, long userId, long integrationId) throws RestException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteIntegnDetails(long orgId, long userId, long integrationId) throws RestException{
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
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteIntegnDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return false;
	}
	
	private IntegrationMasterPOJO getIntegrationDetailsByScope(Connection conn, long orgId, long uniqueId, String query, int level) throws RestException{
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			
			prep = conn.prepareStatement(query);
			
			setDetailsForIntegrationScopQuery(prep, orgId, uniqueId, level);
			
			return getIntegrationDetails(conn, prep);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegrationDetailsByScope :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private Long getIntegrationDetailsByScope(Connection conn, long orgId, long uniqueId, int level) throws RestException{
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			
			String query = "SELECT INTEGRATION_ID FROM Integration WHERE Integration.ORG_ID = ?";
			StringBuilder queryBuilder = new StringBuilder(query.length() + 30)
					.append(query);
			if(level == 2) {
				queryBuilder.append(" AND Integration.DEPARTMENT_ID = ?");
			}else if(level == 3) {
				queryBuilder.append(" AND Integration.USER_ID = ?");
			}
			
			prep = conn.prepareStatement(queryBuilder.toString());
			
			setDetailsForIntegrationScopQuery(prep, orgId, uniqueId, level);
			
			rs = prep.executeQuery();
			if(rs.next()) {
				return rs.getLong("INTEGRATION_ID");
			}
			return null;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegrationDetailsByScope :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private IntegrationMasterPOJO getIntegrationDetailsByIntergrationId(Connection conn, long orgId, long integrationId, String query) throws RestException{
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, integrationId);
			
			return getIntegrationDetails(conn, prep);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegrationDetailsByIntergrationId :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private IntegrationMasterPOJO getIntegrationDetails(Connection conn, PreparedStatement prep) throws RestException{
		ResultSet rs = null;
		try {
			rs = prep.executeQuery();
			List<IntegrationPropertyPOJO> listOfIntegProperty = new ArrayList<IntegrationPropertyPOJO>(rs.getFetchSize());
			IntegrationPOJO integrationPojo = null;
			AuthPOJO authPojo = null;
			if(rs.next()) {
				integrationPojo =  IntegrationPOJO.convertResultSetToPojo(rs);
				authPojo = AuthPOJO.convertResultSetToPojo(rs);
				listOfIntegProperty.add(IntegrationPropertyPOJO.convertResultSetToPojo(rs));
			}
			
			while(rs.next()) {
				authPojo = AuthPOJO.convertResultSetToPojo(rs);
				listOfIntegProperty.add(IntegrationPropertyPOJO.convertResultSetToPojo(rs));
			}
			
			if(integrationPojo == null) {
				throw new RestException(ErrorCode.INTEGRATION_DETAILS_NOT_EXISTS);
			}
			
			return  new IntegrationMasterPOJO()
					.setIntegrationDetails(integrationPojo)
					.setAuthDetails(authPojo)
					.setPropertyDetails(listOfIntegProperty);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegrationDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private String getIntegrationByIntegrationIdQuery() {
		String query = "SELECT * FROM Integration INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND Integration.INTEGRATION_ID = ?";
		return query;
	}
	
	private String getIntegrationByScopeQuery(int level) {
		String query = "SELECT * FROM Integration INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ?";
		StringBuilder queryBuilder = new StringBuilder(query.length() + 70)
				.append(query);
		if(level == 2) {
			queryBuilder.append(" AND Integration.DEPARTMENT_ID = ?");
		}else if(level == 3) {
			queryBuilder.append(" AND Integration.USER_ID = ?");
		}else {
			queryBuilder.append(" AND Integration.DEPARTMENT_ID IS NULL AND Integration.USER_ID IS NULL");
		}
		return queryBuilder.toString();
	}
	
	private void setDetailsForIntegrationScopQuery(PreparedStatement prep, Long orgId, Long uniqueId, int level) throws SQLException {
		prep.setLong(1, orgId);
		
		if(level != 1) {
			prep.setLong(2, uniqueId);
		}
		
	}
	
	private boolean addToTables(Connection conn, final long uniqueId,  final long integrationId, final int level, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo) throws RestException{
		String query = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			
			query = "INSERT INTO auth(SCOPE, TOKEN, INTEGRATION_ID) VALUES (?, ?, ?)";
			prep = conn.prepareStatement(query);
			prep.setString(1, authPojo.getScope());
			prep.setString(2, authPojo.getToken());
			prep.setLong(3, integrationId);
			
			if(!(prep.executeUpdate() > 0)) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
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
			
			int[] resultArray = prep.executeBatch();
			return resultArray.length > 0;
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addToTables :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			dbUtil.closeConnection(null, prep, rs);
		}
	}
}
