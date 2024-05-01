package com.worksuite.integration.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class ScopePOJO {
	
	private Long scopeId;
	
	private Long appId;
	
	private Long orgId;
	
	private Integer level;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	private Long createdTime;
	
	private Long modifiedTime;
	
	public ScopePOJO() {}
	
	public ScopePOJO(JsonObject jsonObj) {
		
		if(jsonObj.has("scopeId")) {
			this.scopeId = jsonObj.get("scopeId").getAsLong();
		}
		
		if(jsonObj.has("appId")) {
			this.appId = jsonObj.get("appId").getAsLong();
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
	
	public Long getScopeId() {
		return scopeId;
	}

	public ScopePOJO setScopeId(Long scopeId) {
		this.scopeId = scopeId;
		return this;
	}

	public Long getAppId() {
		return appId;
	}

	public ScopePOJO setAppId(Long appId) {
		this.appId = appId;
		return this;
	}

	public Long getOrgId() {
		return orgId;
	}

	public ScopePOJO setOrgId(Long orgId) {
		this.orgId = orgId;
		return this;
	}

	public Integer getLevel() {
		return level;
	}

	public ScopePOJO setLevel(Integer level) {
		this.level = level;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public ScopePOJO setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public ScopePOJO setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public ScopePOJO setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public Long getModifiedTime() {
		return modifiedTime;
	}

	public ScopePOJO setModifiedTime(Long modifiedTime) {
		this.modifiedTime = modifiedTime;
		return this;
	}
	
	public static ScopePOJO convertResultSetToPojo(ResultSet rs) throws SQLException {
		ResultSetMetaData resMeta = rs.getMetaData();
		DBUtil dbUtil = new DBUtil();
		
		ScopePOJO scopePojo = new ScopePOJO();
		if(dbUtil.hasColumn(resMeta, "SCOPE_ID")) {
			scopePojo.setScopeId(rs.getLong("SCOPE_ID"));
		}
		
		if(dbUtil.hasColumn(resMeta, "APP_ID")) {
			scopePojo.setAppId(rs.getLong("APP_ID"));
		}
		
		if(dbUtil.hasColumn(resMeta, "ORG_ID")) {
			scopePojo.setOrgId(rs.getLong("ORG_ID"));
		}
		
		if(dbUtil.hasColumn(resMeta, "LEVEL")) {
			scopePojo.setLevel(rs.getInt("LEVEL"));
		}
		
		if(dbUtil.hasColumn(resMeta, "CREATED_BY")) {
			scopePojo.setCreatedBy(rs.getLong("CREATED_BY"));
		}
		
		if(dbUtil.hasColumn(resMeta, "MODIFIED_BY")) {
			scopePojo.setModifiedBy(rs.getLong("MODIFIED_BY"));
		}
		
		if(dbUtil.hasColumn(resMeta, "CREATED_TIME")) {
			scopePojo.setCreatedTime(rs.getLong("CREATED_TIME"));
		}
		
		if(dbUtil.hasColumn(resMeta, "MODIFIED_TIME")) {
			scopePojo.setModifiedTime(rs.getLong("MODIFIED_TIME"));
		}
		return scopePojo;
	}
	
	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, ScopePOJO.class);
	}
	
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
}
