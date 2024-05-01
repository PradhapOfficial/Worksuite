package com.worksuite.rest.api.common;

public class RestException extends Exception {

	private String errorCode;
	
	private String errorMessage;
	
	private int status;
	
	public RestException(ErrorCode errorCode) {
		this.errorCode = errorCode.getErrorCode();
		this.errorMessage = errorCode.getErrorMessage();
		this.status = errorCode.getStatus();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
