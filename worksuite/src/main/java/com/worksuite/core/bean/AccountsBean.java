package com.worksuite.core.bean;

public interface AccountsBean {
	
	public UserPOJO addAccountDetails(AccountsPOJO accountsPojo, UserPOJO userPojo);
	
	public boolean isAccountExists(String emailId);
	
	public boolean updateAccountDetails(AccountsPOJO accountsPojo);
	
	public boolean deleteAccountDetails(String emailId);
	
	public UserPOJO getAccountDetails(String emailId, String password);
}
