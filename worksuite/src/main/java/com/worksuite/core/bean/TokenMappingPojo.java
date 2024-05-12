package com.worksuite.core.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.worksuite.db.util.DBUtil;

public class TokenMappingPojo {

	private Long tokenMappingId;
	
	private Long userId;
	
	private String token;
	
	public TokenMappingPojo() {}
	
	public TokenMappingPojo(JsonObject tokenJson) {
		if(tokenJson.has("tokenMappingId")) {
			this.tokenMappingId = tokenJson.get("tokenMappingId").getAsLong();
		}
		
		if(tokenJson.has("userId")) {
			this.userId = tokenJson.get("userId").getAsLong();
		}
		
		if(tokenJson.has("token")) {
			this.token = tokenJson.get("token").getAsString();
		}
	}
	
	public static TokenMappingPojo convertResultSetToPojo(ResultSet rs) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		TokenMappingPojo tokenMappingPojo = new TokenMappingPojo();
		ResultSetMetaData rsMeta = rs.getMetaData();
		
		if(dbUtil.hasColumn(rsMeta, "TOKEN_MAPPING_ID")) {
			tokenMappingPojo.setTokenMappingId(rs.getLong("TOKEN_MAPPING_ID"));
		}
		
		if(dbUtil.hasColumn(rsMeta, "USER_ID")) {
			tokenMappingPojo.setUserId(rs.getLong("USER_ID"));
		}
		
		if(dbUtil.hasColumn(rsMeta, "TOKEN")) {
			tokenMappingPojo.setToken(rs.getString("TOKEN"));
		}
		
		return tokenMappingPojo;
	}

	public Long getTokenMappingId() {
		return tokenMappingId;
	}

	public TokenMappingPojo setTokenMappingId(Long tokenMappingId) {
		this.tokenMappingId = tokenMappingId;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public TokenMappingPojo setUserId(Long userId) {
		this.userId = userId;
		return this;
	}
	
	public String getToken() {
		return token;
	}

	public TokenMappingPojo setToken(String token) {
		this.token = token;
		return this;
	}
	
	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, TokenMappingPojo.class);
	}
	
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
	
}
