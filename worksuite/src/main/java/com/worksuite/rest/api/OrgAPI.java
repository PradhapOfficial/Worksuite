package com.worksuite.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.core.bean.OrgBean;
import com.worksuite.core.bean.OrgBeanImpl;
import com.worksuite.core.bean.OrgPOJO;

@Path("{userId}/org")
public class OrgAPI {

	@GET
	@Path("{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrgPOJO getOrgDetails(@PathParam("userId") long userId, @PathParam("orgId") long orgId) {

		OrgBean orgBean = new OrgBeanImpl();
		return orgBean.getOrgDetails(userId, orgId);
	}

	@PUT
	@Path("{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrgPOJO updateOrgDetails(@PathParam("userId") long userId, @PathParam("orgId") long orgId, String jsonStr) {

		JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
		OrgPOJO orgPojo = new OrgPOJO(jsonObj).setModifiedBy(userId);

		OrgBean orgBean = new OrgBeanImpl();
		return orgBean.updateOrgDetails(userId, orgId, orgPojo);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrgPOJO addOrgDetails(@PathParam("userId") long userId, String jsonStr) {

		JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
		OrgPOJO orgPojo = new OrgPOJO(jsonObj).setCreatedBy(userId).setModifiedBy(userId).setStatus(1);

		OrgBean orgBean = new OrgBeanImpl();
		return orgBean.addOrgDetails(userId, orgPojo);
	}

	@DELETE
	@Path("{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteOrgDetails(@PathParam("userId") long userId, @PathParam("orgId") long orgId) {
		OrgBean orgBean = new OrgBeanImpl();
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("status", orgBean.deleteOrgDetails(userId, orgId));
		return jsonObj.toString();
	}
}
