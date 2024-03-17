package com.worksuite.core.bean;

public interface UserBean {

	public UserPOJO getUserDetails(long userId);

	public UserMasterPOJO getUserDetails(long userId, long orgId);

	public UserPOJO updateUserDetails(long userId, UserPOJO userPojo);

	public boolean deleteUserDetails(long userId);
}
