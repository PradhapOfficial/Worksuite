package com.worksuite.rest.api.common;

import javax.servlet.http.HttpServletResponse;

public enum ErrorCode {

	INTERNAL_SERVER_ERROR("1000", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
	
	SCOPE_NOT_REGISTERED("1001", HttpServletResponse.SC_FORBIDDEN, "Integration Scope Is Not Registered"),
	
	INVALID_INTEGRATION_ID("1002", HttpServletResponse.SC_FORBIDDEN, "Invalid Integration Id"),
	
	INVALID_USER_ID("1003", HttpServletResponse.SC_FORBIDDEN, "Invalid UserId"),
	
	INVALID_USER_NOT_PRESENT_IN_ORG("1004", HttpServletResponse.SC_FORBIDDEN, "User Not Present In Specified Org"),
	
	INVALID_APP_ID("1005", HttpServletResponse.SC_FORBIDDEN, "Invalid App Id"),
	
	NOT_ORG_ADMIN("1006", HttpServletResponse.SC_FORBIDDEN, "Not An Org Admin");
	
	String errorCode;
	
	int status;
	
	String errorMessage;
	
	private ErrorCode(String errorCode, int status, String errorMessage){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public int getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
