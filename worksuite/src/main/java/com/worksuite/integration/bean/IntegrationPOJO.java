package com.worksuite.integration.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class IntegrationPOJO {
	
	private Long integrationId;
	
	private Long appId;
	
	private Boolean status;
	
	private Long orgId;
	
	private Integer level;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	private Long createdTime;
	
	private Long modifiedTime;

	
	public IntegrationPOJO() {}
	
	public IntegrationPOJO(JsonObject jsonObj) {
		
		if(jsonObj.has("integrationId")) {
			this.integrationId = jsonObj.get("integrationId").getAsLong();
		}
		
		if(jsonObj.has("appId")) {
			this.appId = jsonObj.get("appId").getAsLong();
		}
		
		if(jsonObj.has("status")) {
			this.status = jsonObj.get("status").getAsBoolean();
		}
		
		if(jsonObj.has("orgId")) {
			this.orgId = jsonObj.get("orgId").getAsLong();
		}
		
		if(jsonObj.has("level")) {
			this.level = jsonObj.get("level").getAsInt();
		}
		
		if(jsonObj.has("createdBy")) {
			this.createdBy = jsonObj.get("createdBy").getAsLong();
		}
		
		if(jsonObj.has("modifiedBy")) {
			this.modifiedBy = jsonObj.get("modifiedBy").getAsLong();
		}
		
		if(jsonObj.has("createdTime")) {
			this.createdTime = jsonObj.get("createdTime").getAsLong();
		}
		
		if(jsonObj.has("modifiedTime")) {
			this.modifiedTime = jsonObj.get("modifiedTime").getAsLong();
		}
	}
	
	public Long getIntegrationId() {
		return integrationId;
	}

	public IntegrationPOJO setIntegrationId(Long integrationId) {
		this.integrationId = integrationId;
		return this;
	}

	public Long getAppId() {
		return appId;
	}

	public IntegrationPOJO setAppId(Long appId) {
		this.appId = appId;
		return this;
	}

	public Boolean getStatus() {
		return status;
	}

	public IntegrationPOJO setStatus(Boolean status) {
		this.status = status;
		return this;
	}

	public Long getOrgId() {
		return orgId;
	}

	public IntegrationPOJO setOrgId(Long orgId) {
		this.orgId = orgId;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public IntegrationPOJO setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public IntegrationPOJO setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public IntegrationPOJO setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public IntegrationPOJO setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public Long getModifiedTime() {
		return modifiedTime;
	}

	public IntegrationPOJO setModifiedTime(Long modifiedTime) {
		this.modifiedTime = modifiedTime;
		return this;
	}
	
	public static IntegrationPOJO convertResultSetToPojo(ResultSet rs) throws SQLException {
		ResultSetMetaData rMeta = rs.getMetaData();
		DBUtil dbUtil = new DBUtil();
		IntegrationPOJO integrationPojo = new IntegrationPOJO();
		
		if(dbUtil.hasColumn(rMeta, "INTEGRATION_ID")) {
			integrationPojo.setIntegrationId(rs.getLong("INTEGRATION_ID"));
		}
		
		if(dbUtil.hasColumn(rMeta, "APP_ID")) {
			integrationPojo.setAppId(rs.getLong("APP_ID"));
		}
		
		if(dbUtil.hasColumn(rMeta, "STATUS")) {
			integrationPojo.setStatus(rs.getBoolean("STATUS"));
		}
		
		if(dbUtil.hasColumn(rMeta, "ORG_ID")) {
			integrationPojo.setOrgId(rs.getLong("ORG_ID"));
		}
		
		if(dbUtil.hasColumn(rMeta, "LEVEL")) {
			integrationPojo.setLevel(rs.getInt("LEVEL"));
		}
		
		if(dbUtil.hasColumn(rMeta, "CREATED_BY")) {
			integrationPojo.setCreatedBy(rs.getLong("CREATED_BY"));
		}
		
		if(dbUtil.hasColumn(rMeta, "MODIFIED_BY")) {
			integrationPojo.setModifiedBy(rs.getLong("MODIFIED_BY"));
		}
		
		if(dbUtil.hasColumn(rMeta, "CREATED_TIME")) {
			integrationPojo.setCreatedTime(rs.getLong("CREATED_TIME"));
		}
		
		if(dbUtil.hasColumn(rMeta, "MODIFIED_TIME")) {
			integrationPojo.setModifiedTime(rs.getLong("MODIFIED_TIME"));
		}
		return integrationPojo;
	}
	
	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, IntegrationPOJO.class);
	}
	
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
}
