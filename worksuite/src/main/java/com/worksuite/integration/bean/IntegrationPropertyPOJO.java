package com.worksuite.integration.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class IntegrationPropertyPOJO {
	
	private Long integrationPropertyId;
	
	private String propertyName;
	
	private String propertyValue;
	
	private Long integrationId;

	public IntegrationPropertyPOJO() {}
	
	public IntegrationPropertyPOJO(JsonObject jsonObj) {
		if(jsonObj.has("integrationPropertyId")) {
			this.integrationPropertyId = jsonObj.get("integrationPropertyId").getAsLong();
		}
		
		if(jsonObj.has("propertyName")) {
			this.propertyName = jsonObj.get("propertyName").getAsString();
		}
		
		if(jsonObj.has("propertyValue")) {
			this.propertyValue = jsonObj.get("propertyValue").getAsString();
		}
		
		if(jsonObj.has("integrationId")) {
			this.integrationId = jsonObj.get("integrationId").getAsLong();
		}
	}
	
	public Long getIntegrationPropertyId() {
		return integrationPropertyId;
	}

	public IntegrationPropertyPOJO setIntegrationPropertyId(Long integrationPropertyId) {
		this.integrationPropertyId = integrationPropertyId;
		return this;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public IntegrationPropertyPOJO setPropertyName(String propertyName) {
		this.propertyName = propertyName;
		return this;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public IntegrationPropertyPOJO setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
		return this;
	}

	public Long getIntegrationId() {
		return integrationId;
	}

	public IntegrationPropertyPOJO setIntegrationId(Long integrationId) {
		this.integrationId = integrationId;
		return this;
	}
	
	public static IntegrationPropertyPOJO convertResultSetToPojo(ResultSet rs) throws SQLException {
		ResultSetMetaData rMeta = rs.getMetaData();
		DBUtil dbUtil = new DBUtil();
		
		IntegrationPropertyPOJO integPropPojo = new IntegrationPropertyPOJO();
		if(dbUtil.hasColumn(rMeta, "INTEGRATION_PROPERTY_ID")) {
			integPropPojo.setIntegrationPropertyId(rs.getLong("INTEGRATION_PROPERTY_ID"));
		}
		
		if(dbUtil.hasColumn(rMeta, "PROPERTY_NAME")) {
			integPropPojo.setPropertyName(rs.getString("PROPERTY_NAME"));
		}
		
		if(dbUtil.hasColumn(rMeta, "PROPERTY_VALUE")) {
			integPropPojo.setPropertyValue(rs.getString("PROPERTY_VALUE"));
		}
		
		if(dbUtil.hasColumn(rMeta, "INTEGRATION_ID")) {
			integPropPojo.setIntegrationId(rs.getLong("INTEGRATION_ID"));
		}
		
		return integPropPojo;
	}
}
