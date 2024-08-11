package com.worksuite.integration.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.integration.bean.ScopeBean;
import com.worksuite.integration.bean.ScopeBeanImpl;
import com.worksuite.integration.bean.ScopePOJO;
import com.worksuite.rest.api.common.APIUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("{orgId}/scope/{userId}")
public class ScopeAPI extends APIUtil{
	
	private static final Logger LOGGER = LogManager.getLogger(ScopeAPI.class.getName());
	
	@POST
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScopePOJO addScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, String jsonStr) throws RestException {
		try {
			isValidAppId(appId);
			isUserPresentInOrg(orgId, userId);
			
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			ScopePOJO scopePojo = new ScopePOJO(jsonObj)
					.setOrgId(orgId)
					.setAppId(appId)
					.setCreatedBy(userId)
					.setModifiedBy(userId);
					
			ScopeBean scopeBean = new ScopeBeanImpl();
			return scopeBean.addScopeDetails(orgId, appId, scopePojo);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PUT
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScopePOJO updateScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, String jsonStr) throws RestException {
		try {
			isAdminUser(orgId, userId);
			isUserPresentInOrg(orgId, userId);
			isScopeRegistered(orgId, appId);
			
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			ScopePOJO scopePojo = new ScopePOJO(jsonObj)
					.setOrgId(orgId)
					.setAppId(appId)
					.setModifiedBy(userId);
					
			ScopeBean scopeBean = new ScopeBeanImpl();
			return scopeBean.updateScopeDetails(orgId, appId, scopePojo);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while updateScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GET
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScopePOJO getScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId) throws RestException {
		try {
			
			isValidAppId(appId);
			isUserPresentInOrg(orgId, userId);
			
			ScopeBean scopeBean = new ScopeBeanImpl();
			return scopeBean.getScopeDetails(orgId, appId);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DELETE
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId) throws RestException {
		try {
			
			isAdminUser(orgId, userId);
			isUserPresentInOrg(orgId, userId);
			isScopeRegistered(orgId, appId);
			
			ScopeBean scopeBean = new ScopeBeanImpl();
			boolean delStatus =  scopeBean.deleteScopeDetails(orgId, appId);
			JsonObject resJson = new JsonObject();
			resJson.addProperty("status", delStatus);
			return resJson.toString();
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteScopeDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
}
