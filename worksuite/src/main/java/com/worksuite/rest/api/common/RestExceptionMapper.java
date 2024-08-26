package com.worksuite.rest.api.common;

import java.util.HashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger LOGGER = LogManager.getLogger(RestExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(Throwable throwable) {
		
		LOGGER.log(Level.ERROR, "Error Respone in RestExceptionMapper :: ", throwable);
		if(throwable instanceof RestException) {
			return errorHandler((RestException)throwable);
		}
		return errorHandler(ErrorCode.INTERNAL_SERVER_ERROR);
	}

	public Response errorHandler(RestException restException) {
		HashMap<String, Object> errorHashMap = new HashMap<String, Object>();
		
		errorHashMap.put(ExceptionConstants.ERROR_CODE, restException.getErrorCode());
		errorHashMap.put(ExceptionConstants.ERROR_MESSAGE, restException.getErrorMessage());
		
		ResponseBuilder resp = Response.status(restException.getStatus());
		resp.type(MediaType.APPLICATION_JSON);
		resp.entity(errorHashMap);
		
		return resp.build();
	}
	
	public Response errorHandler(ErrorCode errorCode) {
		return errorHandler(new RestException(errorCode));
	}
}
