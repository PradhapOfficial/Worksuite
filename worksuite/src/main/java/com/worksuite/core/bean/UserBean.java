package com.worksuite.core.bean;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.worksuite.rest.api.common.RestException;

public interface UserBean {

	public UserPOJO getUserDetails(long userId) throws RestException;

	public UserMasterPOJO getUserDetailsById(long userId, long orgId) throws RestException;
	
	public List<UserMasterPOJO> getListOfUserDetails(long orgId) throws RestException;

	public UserPOJO updateUserDetails(long userId, UserPOJO userPojo) throws RestException;

	public boolean deleteUserDetails(long userId) throws RestException;
	
	public boolean addUserDetails(long userId, long orgId, JsonObject newUserObj) throws RestException;
	
	public boolean addListOfUserDetails(long userId, long orgId, JsonArray newUsersList) throws RestException;
}
