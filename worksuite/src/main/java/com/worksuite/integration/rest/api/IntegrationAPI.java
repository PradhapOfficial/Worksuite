package com.worksuite.integration.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.integration.bean.AuthPOJO;
import com.worksuite.integration.bean.IntegrationBean;
import com.worksuite.integration.bean.IntegrationBeanImpl;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.IntegrationPOJO;
import com.worksuite.integration.bean.IntegrationPropertyPOJO;

@Path("{orgId}/integration/{userId}")
public class IntegrationAPI {

	@POST
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IntegrationMasterPOJO addIntegDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, String jsonStr) {
		try {
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			
			IntegrationPOJO integPojo = new IntegrationPOJO(jsonObj.get("integDetails").getAsJsonObject())
					.setAppId(appId)
					.setStatus(true)
					.setOrgId(orgId)
					.setCreatedBy(userId)
					.setModifiedBy(userId);
			
			AuthPOJO authPojo = new AuthPOJO(jsonObj.get("authDetails").getAsJsonObject());
			JsonArray propertiesArray = jsonObj.get("propertyDetails").getAsJsonArray();
			
			List<IntegrationPropertyPOJO> listOfintegPropPojo = new ArrayList<IntegrationPropertyPOJO>();
			for(JsonElement jsonEle : propertiesArray) {
				IntegrationPropertyPOJO integPropPojo = new IntegrationPropertyPOJO(((JsonObject)jsonEle));
				listOfintegPropPojo.add(integPropPojo);
			}
			
			IntegrationBean integBean = new IntegrationBeanImpl();
			return integBean.addIntegDetails(orgId, userId, null, integPojo, authPojo, listOfintegPropPojo);
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
