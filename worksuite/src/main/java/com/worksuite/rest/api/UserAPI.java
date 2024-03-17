package com.worksuite.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.core.bean.UserBean;
import com.worksuite.core.bean.UserBeanImpl;
import com.worksuite.core.bean.UserPOJO;

@Path("user")
public class UserAPI {

	@GET
	@Path("{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO getUserDetails(@PathParam("userId") long userId) {
		try {
			UserBean userBean = new UserBeanImpl();
			return userBean.getUserDetails(userId);
		}catch(Exception e) {
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
		}catch(Exception e) {
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
			JsonObject resJson =  new JsonObject();
			resJson.addProperty("status", userBean.deleteUserDetails(userId));
			return resJson.toString();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
