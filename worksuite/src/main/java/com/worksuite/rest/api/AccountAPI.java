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

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.core.bean.AccountsBean;
import com.worksuite.core.bean.AccountsBeanImpl;
import com.worksuite.core.bean.AccountsPOJO;
import com.worksuite.core.bean.UserPOJO;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("account")
public class AccountAPI {
	
	private static final Logger LOGGER = LogManager.getLogger(AccountAPI.class);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO getAccountDetails(@QueryParam("emailId") String emailId, @QueryParam("password") String password)
			throws RestException {
		try {
			AccountsBean accountsBean = new AccountsBeanImpl();
			return accountsBean.getAccountDetails(emailId, password);
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO addAccountDetails(String jsonStr) throws RestException {
		try {
			JsonObject jsonObject = new Gson().fromJson(jsonStr, JsonObject.class);
			AccountsPOJO accountsPojo = new AccountsPOJO(jsonObject);

			UserPOJO userPojo = new UserPOJO(jsonObject).setEmailId(accountsPojo.getEmailId());

			AccountsBean accountsBean = new AccountsBeanImpl();
			userPojo = accountsBean.addAccountDetails(accountsPojo, userPojo);
			return userPojo;
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@PUT
	@Path("{emailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateAccountDetails(@PathParam("emailId") String emailId, String jsonStr) throws RestException {
		try {

			isValidEmailId(emailId);

			JsonObject jsonObject = new Gson().fromJson(jsonStr, JsonObject.class);
			AccountsPOJO accountsPojo = new AccountsPOJO(jsonObject);
			accountsPojo.setEmailId(emailId);

			AccountsBean accountsBean = new AccountsBeanImpl();
			boolean isUpdate = accountsBean.updateAccountDetails(accountsPojo);

			jsonObject = new JsonObject();
			jsonObject.addProperty("status", isUpdate);

			return jsonObject.toString();
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while updateAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("{emailId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAccountDetails(@PathParam("emailId") String emailId) throws RestException {
		try {
			isValidEmailId(emailId);

			AccountsBean accountsBean = new AccountsBeanImpl();
			boolean isUpdate = accountsBean.deleteAccountDetails(emailId);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("status", isUpdate);
			return jsonObject.toString();
		}catch(RestException re) {
			throw re;
		}catch (Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while deleteAccountDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean isValidEmailId(String emailId) throws RestException {
		if (new AccountsBeanImpl().isAccountExists(emailId)) {
			return true;
		}
		throw new RestException(ErrorCode.INVALID_EMAIL_ID);
	}
}
