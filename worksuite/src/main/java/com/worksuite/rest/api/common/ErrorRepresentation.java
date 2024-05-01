package com.worksuite.rest.api.common;

import java.util.HashMap;

public class ErrorRepresentation {
	
	private static final String ERROR_CODE = "errorCode";
	
	private static final String ERROR_MESSAGE = "errorMessage";
	
	HashMap<String, Object> errorRep = new HashMap<String, Object>();
	
	public void setCode(String errorCode) {
		errorRep.put(ERROR_CODE, errorCode);
	}

	public void setMessage(String errorMessage) {
		errorRep.put(ERROR_MESSAGE, errorMessage);
	}
}
