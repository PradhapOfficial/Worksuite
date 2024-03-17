package com.worksuite.core.bean;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class AccountsPOJO {

	private Long accountId;

	private String emailId;

	private String password;

	public AccountsPOJO() {
	}

	public AccountsPOJO(JsonObject jsonObject) {
		if (jsonObject.has("userName")) {
			this.emailId = jsonObject.get("userName").getAsString();
		}

		if (jsonObject.has("password")) {
			this.password = jsonObject.get("password").getAsString();
		}
	}

	public Long getAccountId() {
		return accountId;
	}

	public AccountsPOJO setAccountId(Long accountId) {
		this.accountId = accountId;
		return this;
	}

	public String getEmailId() {
		return emailId;
	}

	public AccountsPOJO setEmailId(String userName) {
		this.emailId = userName;
		return this;
	}

	public String getPassWord() {
		return password;
	}

	public AccountsPOJO setPassWord(String password) {
		this.password = password;
		return this;
	}

	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, AccountsPOJO.class);
	}

	@Override
	public String toString() {
		return toJsonObject().toString();
	}
}
