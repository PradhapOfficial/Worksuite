package com.worksuite.rest.api.common;

import javax.servlet.http.HttpServletResponse;

public enum ErrorCode {

	INTERNAL_SERVER_ERROR("1000", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
	
	SCOPE_NOT_REGISTERED("1001", HttpServletResponse.SC_FORBIDDEN, "Integration Scope Is Not Registered"),
	
	INVALID_INTEGRATION_ID("1002", HttpServletResponse.SC_FORBIDDEN, "Invalid Integration Id"),
	
	INVALID_USER_ID("1003", HttpServletResponse.SC_FORBIDDEN, "Invalid UserId"),
	
	INVALID_USER_NOT_PRESENT_IN_ORG("1004", HttpServletResponse.SC_FORBIDDEN, "User Not Present In Specified Org"),
	
	INVALID_APP_ID("1005", HttpServletResponse.SC_FORBIDDEN, "Invalid App Id"),
	
	NOT_ORG_ADMIN("1006", HttpServletResponse.SC_FORBIDDEN, "Not An Org Admin"),
	
	INVALID_TOKEN("1007", HttpServletResponse.SC_UNAUTHORIZED, "Invalid token"),
	
	INVALID_EMAIL_ID("1008", HttpServletResponse.SC_FORBIDDEN, "Invalid emailId"),
	
	INVALID_EMAIL_ID_AND_PASSWORD("1009", HttpServletResponse.SC_FORBIDDEN, "Invalid emailId and Password"),
	
	UNABLE_TO_PROCESS("1010", HttpServletResponse.SC_FORBIDDEN, "Unable to proccess the request"),
	
	INVALID_ROLE("1011", HttpServletResponse.SC_FORBIDDEN, "Invalid Role"),
	
	SCOPE_ALREADY_REGISTERED("1012", HttpServletResponse.SC_FORBIDDEN, "Integration Scope Is Already Registered"),
	
	INTEGRATION_DETAILS_NOT_EXISTS("1013", HttpServletResponse.SC_FORBIDDEN, "Integration Details Not Exist");
	
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
