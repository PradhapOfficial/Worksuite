package com.worksuite.integration.rest.api;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.worksuite.integration.bean.AuthPOJO;
import com.worksuite.integration.bean.IntegrationBean;
import com.worksuite.integration.bean.IntegrationBeanImpl;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.IntegrationPOJO;
import com.worksuite.integration.bean.IntegrationPropertyPOJO;
import com.worksuite.integration.bean.ScopeBean;
import com.worksuite.integration.bean.ScopeBeanImpl;
import com.worksuite.integration.bean.ScopePOJO;
import com.worksuite.rest.api.common.APIUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("{orgId}/integration/{userId}")
public class IntegrationAPI extends APIUtil {

	private static final Logger LOGGER = LogManager.getLogger(IntegrationAPI.class.getName());
	
	@POST
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IntegrationMasterPOJO addIntegDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, String jsonStr) throws RestException {
		try {
			
			isValidAppId(appId);
			isUserPresentInOrg(orgId, userId);
			isScopeRegistered(orgId, appId);
			
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
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
			
			ScopeBean scopeBean = new ScopeBeanImpl();
			ScopePOJO scopePojo = scopeBean.getScopeDetails(orgId, appId);
			integPojo.setLevel(scopePojo.getLevel());
			
			IntegrationBean integBean = new IntegrationBeanImpl();
			return integBean.addIntegDetails(orgId, userId, null, integPojo, authPojo, listOfintegPropPojo);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GET
	@Path("{appId}/{integrationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IntegrationMasterPOJO getIntegDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, @PathParam("integrationId") long integrationId) throws RestException {
		try {
			isValidAppId(appId);
			isUserPresentInOrg(orgId, userId);
			isScopeRegistered(orgId, appId);
			isValidIntegId(integrationId);
			
			IntegrationBean integBean = new IntegrationBeanImpl();
			return integBean.getIntegDetails(integrationId, getScopePojo().getLevel());
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GET
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IntegrationMasterPOJO getIntegDetailsByAppId(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId) throws RestException {
		try {
			isValidAppId(appId);
			isUserPresentInOrg(orgId, userId);
			isScopeRegistered(orgId, appId);
			
			IntegrationBean integBean = new IntegrationBeanImpl();
			return integBean.getIntegDetailsByLevel(userId, getScopePojo().getLevel());
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DELETE
	@Path("{appId}/{integrationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteIntegDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, @PathParam("integrationId") long integrationId) throws RestException {
		try {
			isValidAppId(appId);
			isUserPresentInOrg(orgId, userId);
			isScopeRegistered(orgId, appId);
			isValidIntegId(integrationId);
			
			IntegrationBean integBean = new IntegrationBeanImpl();
			boolean deleteStatus = integBean.deleteIntegnDetails(orgId, userId, integrationId);
			
			JsonObject resultJson = new JsonObject();
			resultJson.addProperty("status", false);
			if(deleteStatus) {
				resultJson.addProperty("status", true);
			}
			return resultJson.toString();
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteIntegDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
