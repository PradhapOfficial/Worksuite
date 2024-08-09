package com.worksuite.integration.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GeminiAIUtil {
	
	public static JsonObject setRequiredFields(String inputMessage) {
		JsonObject outerJson = new JsonObject();
		
		JsonObject messageJson = new JsonObject();
		messageJson.addProperty(GeminiAIConstants.TEXT, inputMessage);
		
		JsonArray partsArray = new JsonArray();
		partsArray.add(messageJson);
		
		JsonObject partsJsonObj = new JsonObject();
		partsJsonObj.add(GeminiAIConstants.PARTS, partsArray);
		
		JsonArray contentsArray = new JsonArray();
		contentsArray.add(partsJsonObj);
		
		outerJson.add(GeminiAIConstants.CONTENTS, contentsArray);
		
		return outerJson;
	}
}
