package com.worksuite.core.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.worksuite.db.util.DBUtil;

public class UserPOJO {
	
	private Long userId;
	
	private String firstName;
	
	private String lastName;
	
	private String emailId;

	public UserPOJO() {}
	
	public UserPOJO(JsonObject jsonObject) {
		if(jsonObject.has("firstName")) {
			this.firstName = jsonObject.get("firstName").getAsString();	
		}
		
		if(jsonObject.has("lastName")) {
			this.lastName = jsonObject.get("lastName").getAsString();	
		}
		
		if(jsonObject.has("emailId")) {
			this.emailId = jsonObject.get("emailId").getAsString();
		}
	}
	
	public static UserPOJO convertResultSetToPojo(ResultSet resultSet) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		UserPOJO userPojo = new UserPOJO();
		
		if(dbUtil.hasColumn(resultSet.getMetaData(), "USER_ID")) {
			userPojo.setUserId(Long.parseLong(resultSet.getString("USER_ID")));
		}
		
		if(dbUtil.hasColumn(resultSet.getMetaData(), "FIRST_NAME")) {
			userPojo.setFirstName(resultSet.getString("FIRST_NAME"));
		}
		
		if(dbUtil.hasColumn(resultSet.getMetaData(), "LAST_NAME")) {
			userPojo.setLastName(resultSet.getString("LAST_NAME"));
		}
		
		if(dbUtil.hasColumn(resultSet.getMetaData(), "EMAIL_ID")) {
			userPojo.setEmailId(resultSet.getString("EMAIL_ID"));
		}
		return userPojo;
	}
	
	public Long getUserId() {
		return userId;
	}

	public UserPOJO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public UserPOJO setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public UserPOJO setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public String getEmailId() {
		return emailId;
	}

	public UserPOJO setEmailId(String emailId) {
		this.emailId = emailId;
		return this;
	}
	
	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, UserPOJO.class);
	}
	
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
	
}
