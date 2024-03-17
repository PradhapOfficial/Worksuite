package com.worksuite.core.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class OrgPOJO {
	
	private Long orgId;
	
	private String orgName;
	
	private Long createdBy;
	
	private Long modifiedBy;
	
	private Long createdTime;
	
	private Long modifiedTime;
	
	private Integer status;

	public OrgPOJO() {}
	
	public OrgPOJO(JsonObject jsonObj) {
		if(jsonObj.has("orgId")) {
			this.orgId = jsonObj.get("orgId").getAsLong();
		}
		
		if(jsonObj.has("orgName")) {
			this.orgName = jsonObj.get("orgName").getAsString();
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
		
		if(jsonObj.has("status")) {
			this.status = jsonObj.get("status").getAsInt();
		}
	}
	
	public Long getOrgId() {
		return orgId;
	}
	
	public OrgPOJO setOrgId(Long orgId) {
		this.orgId = orgId;
		return this;
	}

	public String getOrgName() {
		return orgName;
	}

	public OrgPOJO setOrgName(String orgName) {
		this.orgName = orgName;
		return this;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public OrgPOJO setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public OrgPOJO setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public OrgPOJO setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public Long getModifiedTime() {
		return modifiedTime;
	}

	public OrgPOJO setModifiedTime(Long modifiedTime) {
		this.modifiedTime = modifiedTime;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public OrgPOJO setStatus(Integer status) {
		this.status = status;
		return this;
	}
	
	public static OrgPOJO convertResultSetToPojo(ResultSet resultSet) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		OrgPOJO orgPOJO = new OrgPOJO();
		
		ResultSetMetaData resultSetMeta = resultSet.getMetaData();
		
		if(dbUtil.hasColumn(resultSetMeta, "ORG_ID")) {
			orgPOJO.setOrgId(Long.parseLong(resultSet.getString("ORG_ID")));
		}
		
		if(dbUtil.hasColumn(resultSetMeta, "ORG_NAME")) {
			orgPOJO.setOrgName(resultSet.getString("ORG_NAME"));
		}
		
		if(dbUtil.hasColumn(resultSetMeta, "CREATED_BY")) {
			orgPOJO.setCreatedBy(Long.parseLong(resultSet.getString("CREATED_BY")));
		}
		
		if(dbUtil.hasColumn(resultSetMeta, "MODIFIED_BY")) {
			orgPOJO.setModifiedBy(Long.parseLong(resultSet.getString("MODIFIED_BY")));
		}
		
		if(dbUtil.hasColumn(resultSetMeta, "CREATED_TIME")) {
			orgPOJO.setCreatedTime(Long.parseLong(resultSet.getString("CREATED_TIME")));
		}
		
		if(dbUtil.hasColumn(resultSetMeta, "MODIFIED_TIME")) {
			orgPOJO.setModifiedTime(Long.parseLong(resultSet.getString("MODIFIED_TIME")));
		}
		
		if(dbUtil.hasColumn(resultSetMeta, "STATUS")) {
			orgPOJO.setStatus(Integer.parseInt(resultSet.getString("STATUS")));
		}
		
		return orgPOJO;
	}
	
	public void convertPojoToPrep(PreparedStatement prep, OrgPOJO orgPojo) throws Exception{
		if(orgPojo.getOrgName() != null) {
			prep.setString(1, orgPojo.getOrgName());
		}
		
		if(orgPojo.getCreatedBy() != null) {
			prep.setLong(2, orgPojo.getCreatedBy());
		}
		
		if(orgPojo.getModifiedBy() != null) {
			prep.setLong(3, orgPojo.getModifiedBy());
		}
		
		if(orgPojo.getCreatedTime() != null) {
			prep.setLong(4, orgPojo.getCreatedTime());
		}
		
		if(orgPojo.getModifiedTime() != null) {
			prep.setLong(5, orgPojo.getModifiedTime());
		}
		
		if(orgPojo.getStatus() != null) {
			prep.setInt(6, orgPojo.getStatus());
		}
	}
	
	public JsonObject toJsonObject() {
		return new Gson().toJsonTree(this, OrgPOJO.class).getAsJsonObject();
	}
	
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
}
