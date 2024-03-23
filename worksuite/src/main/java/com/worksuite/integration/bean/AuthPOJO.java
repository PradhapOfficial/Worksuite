package com.worksuite.integration.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class AuthPOJO {
	
	private Long authId;
	
	private String scope;
	
	private String token;
	
	private Long integrationId;
	
	public AuthPOJO() {}
	
	public AuthPOJO(JsonObject jsonObj) {
		if(jsonObj.has("authId")) {
			this.authId = jsonObj.get("authId").getAsLong();
		}
		
		if(jsonObj.has("scope")) {
			this.scope = jsonObj.get("scope").getAsString();
		}
		
		if(jsonObj.has("token")) {
			this.token = jsonObj.get("token").getAsString();
		}
		
		if(jsonObj.has("integrationId")) {
			this.integrationId = jsonObj.get("integrationId").getAsLong();
		}
	}

	public Long getAuthId() {
		return authId;
	}

	public AuthPOJO setAuthId(Long authId) {
		this.authId = authId;
		return this;
	}

	public String getScope() {
		return scope;
	}

	public AuthPOJO setScope(String scope) {
		this.scope = scope;
		return this;
	}

	public String getToken() {
		return token;
	}

	public AuthPOJO setToken(String token) {
		this.token = token;
		return this;
	}

	public Long getIntegrationId() {
		return integrationId;
	}

	public AuthPOJO setIntegrationId(Long integrationId) {
		this.integrationId = integrationId;
		return this;
	}
	
	public static AuthPOJO convertResultSetToPojo(ResultSet rs) throws Exception {
		ResultSetMetaData rMeta = rs.getMetaData();
		AuthPOJO authPojo = new AuthPOJO();
		DBUtil dbUtil = new DBUtil();
		
		if(dbUtil.hasColumn(rMeta, "AUTH_ID")) {
			authPojo.setAuthId(rs.getLong("AUTH_ID"));
		}
		
		if(dbUtil.hasColumn(rMeta, "SCOPE")) {
			authPojo.setScope(rs.getString("SCOPE"));
		}
		
		if(dbUtil.hasColumn(rMeta, "TOKEN")) {
			authPojo.setToken(rs.getString("TOKEN"));
		}
		
		if(dbUtil.hasColumn(rMeta, "INTEGRATION_ID")) {
			authPojo.setIntegrationId(rs.getLong("INTEGRATION_ID"));
		}
		return authPojo;
	}
}
