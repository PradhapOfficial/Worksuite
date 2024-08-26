package com.worksuite.core.bean;

import com.worksuite.rest.api.common.RestException;

public interface AccountsBean {

	public UserPOJO addAccountDetails(AccountsPOJO accountsPojo, UserPOJO userPojo) throws RestException;

	public boolean isAccountExists(String emailId) throws RestException;

	public boolean updateAccountDetails(AccountsPOJO accountsPojo) throws RestException;

	public boolean deleteAccountDetails(String emailId) throws RestException;

	public UserPOJO getAccountDetails(String emailId, String password) throws RestException;
}
