package com.worksuite.external.rest.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.db.util.HttpConstants;
import com.worksuite.db.util.HttpRequest;
import com.worksuite.db.util.HttpResponse;
import com.worksuite.integration.util.OpenAIConstants;

public class OpenAI {
	
	public JsonObject getChat(String apiKey, JsonObject contentObj) throws Exception {
		HttpRequest httpRequest = new HttpRequest(OpenAIConstants.CHAT_URL);
		httpRequest.setRequestProperty(HttpConstants.AUTHORIZATION, OpenAIConstants.BEARER + " " + apiKey);
		httpRequest.setRequestProperty(HttpConstants.CONTENT_TYPE, HttpConstants.APPLICATION_JSON);
		HttpResponse response = httpRequest.post(contentObj.toString().getBytes());
		
		if(response.getResponseCode() == 200) {
			return new Gson().fromJson(response.getResponseMessageAsString(), JsonObject.class);
 		}
		
		return new Gson().fromJson(response.getErrorResponseMessageAsString(), JsonObject.class);
	}
}
