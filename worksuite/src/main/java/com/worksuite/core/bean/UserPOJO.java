package com.worksuite.core.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.db.util.DBUtil;

public class UserPOJO {

	private Long userId;

	private String firstName;

	private String lastName;

	private String emailId;

	public UserPOJO() {
	}

	public UserPOJO(JsonObject jsonObject) {
		if (jsonObject.has("userId")) {
			this.userId = jsonObject.get("userId").getAsLong();
		}

		if (jsonObject.has("firstName")) {
			this.firstName = jsonObject.get("firstName").getAsString();
		}

		if (jsonObject.has("lastName")) {
			this.lastName = jsonObject.get("lastName").getAsString();
		}

		if (jsonObject.has("emailId")) {
			this.emailId = jsonObject.get("emailId").getAsString();
		}
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

	public static UserPOJO convertResultSetToPojo(ResultSet resultSet) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		UserPOJO userPojo = new UserPOJO();

		ResultSetMetaData resultSetMeta = resultSet.getMetaData();

		if (dbUtil.hasColumn(resultSetMeta, "USER_ID")) {
			userPojo.setUserId(Long.parseLong(resultSet.getString("USER_ID")));
		}

		if (dbUtil.hasColumn(resultSetMeta, "FIRST_NAME")) {
			userPojo.setFirstName(resultSet.getString("FIRST_NAME"));
		}

		if (dbUtil.hasColumn(resultSetMeta, "LAST_NAME")) {
			userPojo.setLastName(resultSet.getString("LAST_NAME"));
		}

		if (dbUtil.hasColumn(resultSetMeta, "EMAIL_ID")) {
			userPojo.setEmailId(resultSet.getString("EMAIL_ID"));
		}
		return userPojo;
	}

	public void convertPojoToPrepStm(UserPOJO userPojo) {
		convertPojoToPrepStm(userPojo, false);
	}

	public void convertPojoToPrepStm(UserPOJO userPojo, boolean isUpdateQuery) {
		if (isUpdateQuery) {
			StringBuilder queryBuilder = new StringBuilder("UPDATE User SET ");
			if (userPojo.getFirstName() != null) {
				queryBuilder.append("FIRST_NAME = ?");
			}

			if (userPojo.getLastName() != null) {
				queryBuilder.append(userPojo.getFirstName() != null ? ", " : "");
				queryBuilder.append("LAST_NAME = ?");
			}

		} else {

		}
	}

	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, UserPOJO.class);
	}

	@Override
	public String toString() {
		return toJsonObject().toString();
	}

}
