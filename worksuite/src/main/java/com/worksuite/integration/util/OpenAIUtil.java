package com.worksuite.integration.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.IntegrationPropertyPOJO;

public class OpenAIUtil {

	public static JsonObject setChatRequiredFileds(IntegrationMasterPOJO integrationMasterPOJO, JsonObject contentJsonObj) throws Exception{
		JsonObject outJsonObj = new JsonObject();
		
//		String version = "";
//		for(IntegrationPropertyPOJO  integrationPropertyPOJO: integrationMasterPOJO.getPropertyDetails()) {
//			if(integrationPropertyPOJO.getPropertyName().equals(IntegrationConstants.VERSION)) {
//				version = integrationPropertyPOJO.getPropertyValue();
//				break;
//			}
//		}
//		
//		outJsonObj.addProperty(OpenAIConstants.MODEL, version);
//		
//		String content = contentJsonObj.get(OpenAIConstants.CONTENT).getAsString();
//		JsonObject messageJsonObj = new JsonObject();
//		messageJsonObj.addProperty(OpenAIConstants.ROLE, OpenAIConstants.USER);
//		messageJsonObj.addProperty(OpenAIConstants.CONTENT, content);
//		
//		JsonArray messageJsonArray = new JsonArray();
//		messageJsonArray.add(messageJsonObj);
//		
//		outJsonObj.add(OpenAIConstants.MESSAGES, messageJsonArray);
		return outJsonObj;
	}
}
