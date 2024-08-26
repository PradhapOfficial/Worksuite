package com.worksuite.external.rest.api;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.db.util.HttpConstants;
import com.worksuite.db.util.HttpRequest;
import com.worksuite.db.util.HttpResponse;
import com.worksuite.integration.util.GeminiAIConstants;

public class GeminiAI {

	private static final Logger LOGGER = LogManager.getLogger(GeminiAI.class);
	
	public static JsonObject getChat(String apiKey, JsonObject contentJsonObj) throws Exception {
		StringBuilder chatUrlBuilder = new StringBuilder(GeminiAIConstants.URL.length() + 100);
		chatUrlBuilder.append(GeminiAIConstants.URL).append("?key=").append(apiKey);
		
		HttpRequest httpRequest = new HttpRequest(chatUrlBuilder.toString());
		httpRequest.setRequestProperty(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);
		HttpResponse response = httpRequest.post(contentJsonObj.toString().getBytes());
		
		if(response.getResponseCode() == 200) {
			return new Gson().fromJson(response.getResponseMessageAsString(), JsonObject.class);
		}
		String errorReponseMessage = response.getErrorResponseMessageAsString();
		LOGGER.log(Level.ERROR, "Error response from get chat :: ", errorReponseMessage);
		return new Gson().fromJson(errorReponseMessage, JsonObject.class);
	}

}
