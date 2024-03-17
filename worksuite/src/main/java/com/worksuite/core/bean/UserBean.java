package com.worksuite.core.bean;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface UserBean {

	public UserPOJO getUserDetails(long userId);

	public UserMasterPOJO getUserDetailsById(long userId, long orgId);
	
	public List<UserMasterPOJO> getListOfUserDetails(long orgId);

	public UserPOJO updateUserDetails(long userId, UserPOJO userPojo);

	public boolean deleteUserDetails(long userId);
	
	public boolean addUserDetails(long userId, long orgId, JsonObject newUserObj);
	
	public boolean addListOfUserDetails(long userId, long orgId, JsonArray newUsersList);
}
