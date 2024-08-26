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
import com.worksuite.external.rest.api.OpenAI;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.util.OpenAIUtil;
import com.worksuite.rest.api.common.APIUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("{orgId}/openai/{userId}")
public class OpenAIAPI extends APIUtil {
	
	private static Logger LOGGER = LogManager.getLogger(OpenAIAPI.class);
	
	@POST
	@Path("{appId}/{integrationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getChat(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, @PathParam("integrationId") long integrationId, String jsonStr) throws RestException {
		try {
			isScopeRegistered(orgId, appId);
			isValidIntegId(orgId, integrationId);
			LOGGER.log(Level.INFO, "Data while getChat called :: ");
			IntegrationMasterPOJO integrationMasterPOJO = getIntegrationMasterPOJO();
			
			LOGGER.log(Level.INFO, "Data while getChat :: " + integrationMasterPOJO.toString());
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			
			jsonObj = OpenAIUtil.setChatRequiredFileds(integrationMasterPOJO, jsonObj);
			return OpenAI.getChat(integrationMasterPOJO.getAuthDetails().getToken(), jsonObj).toString();
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getChat :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
