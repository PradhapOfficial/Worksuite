package com.worksuite.integration.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.external.rest.api.OpenAI;
import com.worksuite.integration.bean.IntegrationBean;
import com.worksuite.integration.bean.IntegrationBeanImpl;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.ScopeBean;
import com.worksuite.integration.bean.ScopeBeanImpl;
import com.worksuite.integration.bean.ScopePOJO;

@Path("{orgId}/openai/{userId}")
public class OpenAIAPI {
	@POST
	@Path("{appId}/{integrationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getChat(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, @PathParam("integrationId") long integrationId, String jsonStr) {
		try {
			ScopeBean scopeBean = new ScopeBeanImpl();
			ScopePOJO scopePojo = scopeBean.getScopeDetails(orgId, appId);
			if(scopePojo == null) {
				throw new Exception("Not Rows Found");
			}
			
			IntegrationBean integBean = new IntegrationBeanImpl();
			IntegrationMasterPOJO integrationMasterPOJO = integBean.getIntegDetails(integrationId, scopePojo.getLevel());
		
			if(integrationMasterPOJO == null) {
				throw new Exception("No data");
			}
			
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			OpenAI openAI = new OpenAI();
			return openAI.getChat(integrationMasterPOJO.getAuthDetails().getToken(), jsonObj).toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
