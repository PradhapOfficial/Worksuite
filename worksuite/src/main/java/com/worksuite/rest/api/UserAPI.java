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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.worksuite.core.bean.UserBean;
import com.worksuite.core.bean.UserBeanImpl;
import com.worksuite.core.bean.UserMasterPOJO;
import com.worksuite.core.bean.UserPOJO;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("{orgId}/users")
public class UserAPI {

	private static final Logger LOGGER = LogManager.getLogger(UserAPI.class.getName());
	
	@GET
	@Path("{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserMasterPOJO getUserDetails(@PathParam("orgId") long orgId, @PathParam("userId") long userId) throws RestException{
		try {
			UserBean userBean = new UserBeanImpl();
			return userBean.getUserDetailsById(userId, orgId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getUserDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserMasterPOJO> getListOfUserDetails(@PathParam("orgId") long orgId) throws RestException {
		try {
			UserBean userBean = new UserBeanImpl();
			return userBean.getListOfUserDetails(orgId);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getListOfUserDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String mapUserToOrg(@PathParam("orgId") long orgId, @PathParam("userId") long userId, String jsonStr) throws RestException {
		try {
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			
			UserBean userBean = new UserBeanImpl();
			
			userBean.getUserDetails(userId);
			
			boolean resStatus = userBean.addUserDetails(userId, orgId, jsonObj);
			
			JsonObject resutJson = new JsonObject();
			resutJson.addProperty("status", resStatus);
			
			return resutJson.toString();
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while mapUserToOrg :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@POST
	@Path("{userId}/bulk")
	@Produces(MediaType.APPLICATION_JSON)
	public String mapListUsersToOrg(@PathParam("orgId") long orgId, @PathParam("userId") long userId, String jsonStr) throws RestException {
		try {
			
			JsonArray jsonar = new Gson().fromJson(jsonStr, JsonArray.class);
			UserBean userBean = new UserBeanImpl();
			
			if(userBean.getUserDetails(userId) == null) {
				throw new RestException(ErrorCode.INVALID_USER_ID);
			}
			
			boolean resStatus = userBean.addListOfUserDetails(userId, orgId, jsonar);
			
			JsonObject resutJson = new JsonObject();
			resutJson.addProperty("status", resStatus);
			
			return resutJson.toString();
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while mapListUsersToOrg :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PUT
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO updateUserDetails(@PathParam("userId") long userId, String jsonStr) throws RestException {
		try {
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			UserPOJO userPojo = new UserPOJO(jsonObj);
			UserBean userBean = new UserBeanImpl();
			return userBean.updateUserDetails(userId, userPojo);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while updateUserDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteUserDetails(@PathParam("userId") long userId) throws RestException {
		try {

			UserBean userBean = new UserBeanImpl();
			JsonObject resJson = new JsonObject();
			resJson.addProperty("status", userBean.deleteUserDetails(userId));
			return resJson.toString();
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteUserDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
