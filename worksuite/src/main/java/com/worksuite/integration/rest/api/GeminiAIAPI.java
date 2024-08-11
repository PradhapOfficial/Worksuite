package com.worksuite.integration.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.external.rest.api.GeminiAI;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.util.GeminiAIUtil;
import com.worksuite.integration.util.IntegrationConstants;
import com.worksuite.rest.api.common.APIUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("{orgId}/geminiai")
public class GeminiAIAPI extends APIUtil{

	private static final Logger LOGGER = LogManager.getLogger(GeminiAIAPI.class);
	
	@POST
	@Path("{integrationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getChat(@PathParam("orgId") long orgId, @PathParam("integrationId") long integrationId, String payloadStr) throws RestException {
		try {
			isScopeRegistered(orgId, IntegrationConstants.Apps.GEMINI_AI_APP_ID.getValue());
			isValidIntegId(integrationId);
			
			JsonObject payloadJson = new Gson().fromJson(payloadStr, JsonObject.class);
			IntegrationMasterPOJO integrationMasterPojo = this.getIntegrationMasterPOJO();
			String apiKey = integrationMasterPojo.getAuthDetails().getToken();
			
			String message = payloadJson.get("content").getAsString();
			payloadJson = GeminiAIUtil.setRequiredFields(message);
			
			LOGGER.log(Level.INFO, "getChat info :: " + payloadJson.toString());
			return GeminiAI.getChat(apiKey, payloadJson).toString();
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getChat :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
