package com.worksuite.integration.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	@Override
	public IntegrationMasterPOJO addIntegDetails(Long orgId, Long uniqueId, IntegrationPOJO integrationPojo, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo) throws RestException{
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			
			IntegrationMasterPOJO integrationMasterPOJO = getDetails(conn, orgId, uniqueId, integrationPojo);
			if(integrationMasterPOJO.getAuthDetails() != null) {
				return integrationMasterPOJO;
			}
			
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
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}
			
			int level = integrationPojo.getLevel();
			integrationPojo = getDetailsIntegDetails(conn, integrationPojo);
			addToTables(conn, uniqueId, integrationPojo.getIntegrationId(), level, authPojo, listOfIntegPropPojo);
			return getIntegrationDetailsByScope(conn, orgId, integrationPojo.getIntegrationId(), getIntegrationIdQueryByLevel(level));
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
	public IntegrationMasterPOJO getIntegDetails(long orgId, long integrationId, int level) throws RestException{
		Connection conn = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			conn = DBUtil.getConnection();
			
			return getIntegrationDetailsByScope(conn, orgId, integrationId, getIntegrationIdQueryByLevel(level));
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
			return getIntegrationDetailsByScope(conn, orgId, uniqueId, getQueryByLevel(level));
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
	
	private IntegrationMasterPOJO getIntegrationDetailsByScope(Connection conn, long orgId, long uniqueId, String query) throws RestException{
		PreparedStatement prep = null; 
		ResultSet rs = null;
		try {
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, uniqueId);
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
			LOGGER.log(Level.ERROR, "Exception Occured while getDetailsByIntegId :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private String getIntegrationIdQueryByLevel(int level) {
		String query;
		if(level == 1) {
			query = "SELECT * FROM IntegrationOrgMapping INNER JOIN Integration ON IntegrationOrgMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND Integration.INTEGRATION_ID = ?";
		}else if(level == 2) {
			query = "SELECT * FROM IntegrationDepartmentMapping INNER JOIN Integration ON IntegrationDepartmentMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND Integration.INTEGRATION_ID = ?";
		}else {
			query = "SELECT * FROM IntegrationUserMapping INNER JOIN Integration ON IntegrationUserMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND Integration.INTEGRATION_ID = ?";
		}
		return query;
	}
	
	private String getQueryByLevel(int level) {
		String query;
		if(level == 1) {
			query = "SELECT * FROM IntegrationOrgMapping INNER JOIN Integration ON IntegrationOrgMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND IntegrationOrgMapping.ORG_ID = ?";
		}else if(level == 2) {
			query = "SELECT * FROM IntegrationDepartmentMapping INNER JOIN Integration ON IntegrationDepartmentMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND IntegrationDepartmentMapping.DEPARTMENT_ID = ?";
		}else {
			query = "SELECT * FROM IntegrationUserMapping INNER JOIN Integration ON IntegrationUserMapping.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN IntegrationProperty ON IntegrationProperty.INTEGRATION_ID = Integration.INTEGRATION_ID INNER JOIN Auth ON Auth.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND IntegrationUserMapping.USER_ID = ?";
		}
		return query;
	}
	
	private IntegrationPOJO getDetailsIntegDetails(Connection conn, IntegrationPOJO integPojo) throws RestException{
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
			LOGGER.log(Level.ERROR, "Exception Occured while getDetailsIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
		return null;
	}
	
	private IntegrationMasterPOJO getDetails(Connection conn, long orgId, long uniqueId, IntegrationPOJO integrationPojo) throws RestException{
		String query = null;
		PreparedStatement prep = null; 
		ResultSet rs = null;
		int level = integrationPojo.getLevel();
		try {
			if(level == 1) {
				query = "SELECT * FROM IntegrationOrgMapping INNER JOIN Integration ON IntegrationOrgMapping.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND IntegrationOrgMapping.ORG_ID = ? AND Integration.APP_ID = ? AND Integration.LEVEL = ?";
			}else if(level == 2) {
				query = "SELECT * FROM IntegrationDepartmentMapping INNER JOIN Integration ON IntegrationDepartmentMapping.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND IntegrationDepartmentMapping.DEPARTMENT_ID = ? AND Integration.APP_ID = ? AND Integration.LEVEL = ?";
			}else{
				query = "SELECT * FROM IntegrationUserMapping INNER JOIN Integration ON IntegrationUserMapping.INTEGRATION_ID = Integration.INTEGRATION_ID WHERE Integration.ORG_ID = ? AND IntegrationUserMapping.USER_ID = ? AND Integration.APP_ID = ? AND Integration.LEVEL = ?";
			}
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, orgId);
			prep.setLong(2, uniqueId);
			prep.setLong(3, integrationPojo.getAppId());
			prep.setLong(4, integrationPojo.getLevel());
			rs = prep.executeQuery();
		
			List<IntegrationPropertyPOJO> listOfIntegProperty = new ArrayList<IntegrationPropertyPOJO>(rs.getFetchSize());
			AuthPOJO authPojo = null;
			if(rs.next()) {
				integrationPojo =  IntegrationPOJO.convertResultSetToPojo(rs);
				authPojo = AuthPOJO.convertResultSetToPojo(rs);
				listOfIntegProperty.add(IntegrationPropertyPOJO.convertResultSetToPojo(rs));
			}
			
			while(rs.next()) {
				listOfIntegProperty.add(IntegrationPropertyPOJO.convertResultSetToPojo(rs));
			}
			return  new IntegrationMasterPOJO()
					.setIntegrationDetails(integrationPojo)
					.setAuthDetails(authPojo)
					.setPropertyDetails(listOfIntegProperty);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}finally {
			new DBUtil().closeConnection(null, prep, rs);
		}
	}
	
	private boolean addToTables(Connection conn, final long uniqueId,  final long integrationId, final int level, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo) throws RestException{
		String query = null;
		PreparedStatement prep = null;
		ResultSet rs = null;
		DBUtil dbUtil = new DBUtil();
		try {
			
			if(level == 1) {
				query = "INSERT INTO IntegrationOrgMapping(ORG_ID, INTEGRATION_ID) VALUES (?, ?)";
			}else if(level == 2) {
				query = "INSERT INTO IntegrationDepartmentMapping(DEPARTMENT_ID, INTEGRATION_ID) VALUES (?, ?)";
			}else{
				query = "INSERT INTO IntegrationUserMapping(USER_ID, INTEGRATION_ID) VALUES (?, ?)";
			}
			
			prep = conn.prepareStatement(query);
			prep.setLong(1, uniqueId);
			prep.setLong(2, integrationId);
			if(!(prep.executeUpdate() > 0)) {
				throw new RestException(ErrorCode.UNABLE_TO_PROCESS);
			}
			dbUtil.closeConnection(prep);
			
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
