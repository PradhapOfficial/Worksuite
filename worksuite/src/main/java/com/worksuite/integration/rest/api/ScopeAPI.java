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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.integration.bean.ScopeBean;
import com.worksuite.integration.bean.ScopeBeanImpl;
import com.worksuite.integration.bean.ScopePOJO;

@Path("{orgId}/scope/{userId}")
public class ScopeAPI {
	
	@POST
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScopePOJO addScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, String jsonStr) {
		try {
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			ScopePOJO scopePojo = new ScopePOJO(jsonObj)
					.setOrgId(orgId)
					.setAppId(appId)
					.setCreatedBy(userId)
					.setModifiedBy(userId);
					
			ScopeBean scopeBean = new ScopeBeanImpl();
			return scopeBean.addScopeDetails(orgId, appId, scopePojo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PUT
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScopePOJO updateScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId, String jsonStr) {
		try {
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			ScopePOJO scopePojo = new ScopePOJO(jsonObj)
					.setOrgId(orgId)
					.setAppId(appId)
					.setModifiedBy(userId);
					
			ScopeBean scopeBean = new ScopeBeanImpl();
			return scopeBean.updateScopeDetails(orgId, appId, scopePojo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ScopePOJO getScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId) {
		try {
			ScopeBean scopeBean = new ScopeBeanImpl();
			return scopeBean.getScopeDetails(orgId, appId);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@DELETE
	@Path("{appId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteScopeDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId, @PathParam("appId") long appId) {
		try {
			ScopeBean scopeBean = new ScopeBeanImpl();
			boolean delStatus =  scopeBean.deleteScopeDetails(orgId, appId);
			JsonObject resJson = new JsonObject();
			resJson.addProperty("status", delStatus);
			return resJson.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
