package com.worksuite.rest.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.core.bean.OrgPOJO;
import com.worksuite.core.bean.UserBean;
import com.worksuite.core.bean.UserBeanImpl;
import com.worksuite.core.bean.UserMasterPOJO;
import com.worksuite.core.bean.UserPOJO;

@Path("{orgId}/users")
public class UserAPI {

	@GET
	@Path("{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserMasterPOJO getUserDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId) {
		try {
			UserBean userBean = new UserBeanImpl();
			return userBean.getUserDetailsById(userId, orgId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserMasterPOJO> getListOfUserDetails(@PathParam("orgId") long orgId) {
		try {
			UserBean userBean = new UserBeanImpl();
			return userBean.getListOfUserDetails(orgId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@POST
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String mapUserToOrg(@PathParam("orgId") long orgId, @PathParam("userId") long userId, String jsonStr) {
		try {
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			
			UserBean userBean = new UserBeanImpl();
			
			if(userBean.getUserDetails(jsonObj.get("userId").getAsLong()) == null) {
				throw new Exception("Invalid User Id");
			}
			
			boolean resStatus = userBean.addUserDetails(userId, orgId, jsonObj);
			
			JsonObject resutJson = new JsonObject();
			resutJson.addProperty("status", resStatus);
			
			return resutJson.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("{userId}/bulk")
	@Produces(MediaType.APPLICATION_JSON)
	public String mapListUsersToOrg(@PathParam("orgId") long orgId, @PathParam("userId") long userId, String jsonStr) {
		try {
			JsonArray jsonar = new JsonParser().parse(jsonStr).getAsJsonArray();
			
			UserBean userBean = new UserBeanImpl();
			
			if(userBean.getUserDetails(userId) == null) {
				throw new Exception("Invalid User Id");
			}
			
			boolean resStatus = userBean.addListOfUserDetails(userId, orgId, jsonar);
			
			JsonObject resutJson = new JsonObject();
			resutJson.addProperty("status", resStatus);
			
			return resutJson.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@PUT
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO updateUserDetails(@PathParam("userId") long userId, String jsonStr) {
		try {
			JsonObject jsonObj = new JsonParser().parse(jsonStr).getAsJsonObject();
			UserPOJO userPojo = new UserPOJO(jsonObj);
			UserBean userBean = new UserBeanImpl();
			return userBean.updateUserDetails(userId, userPojo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@DELETE
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserDetails(@PathParam("userId") long userId) {
		try {

			UserBean userBean = new UserBeanImpl();
			JsonObject resJson = new JsonObject();
			resJson.addProperty("status", userBean.deleteUserDetails(userId));
			return resJson.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
