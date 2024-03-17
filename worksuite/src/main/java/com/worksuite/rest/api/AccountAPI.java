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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.worksuite.core.bean.AccountsBean;
import com.worksuite.core.bean.AccountsBeanImpl;
import com.worksuite.core.bean.AccountsPOJO;
import com.worksuite.core.bean.UserPOJO;

@Path("account")
public class AccountAPI {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO getAccountDetails(@QueryParam("emailId") String emailId, @QueryParam("password") String password)
			throws Exception {
		try {
			AccountsBean accountsBean = new AccountsBeanImpl();
			return accountsBean.getAccountDetails(emailId, password);
		} catch (Exception e) {
			throw e;
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserPOJO addAccountDetails(String jsonStr) {
		try {
			JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
			AccountsPOJO accountsPojo = new AccountsPOJO(jsonObject);

			UserPOJO userPojo = new UserPOJO(jsonObject).setEmailId(accountsPojo.getEmailId());

			AccountsBean accountsBean = new AccountsBeanImpl();
			userPojo = accountsBean.addAccountDetails(accountsPojo, userPojo);
			return userPojo;
		} catch (Exception e) {
			throw e;
		}
	}

	@PUT
	@Path("{emailId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String updateAccountDetails(@PathParam("emailId") String emailId, String jsonStr) throws Exception {
		try {

			isValidEmailId(emailId);

			JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
			AccountsPOJO accountsPojo = new AccountsPOJO(jsonObject);
			accountsPojo.setEmailId(emailId);

			AccountsBean accountsBean = new AccountsBeanImpl();
			boolean isUpdate = accountsBean.updateAccountDetails(accountsPojo);

			jsonObject = new JsonObject();
			jsonObject.addProperty("status", isUpdate);

			return jsonObject.toString();
		} catch (Exception e) {
			throw e;
		}

	}

	@DELETE
	@Path("{emailId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteAccountDetails(@PathParam("emailId") String emailId) throws Exception {
		try {
			isValidEmailId(emailId);

			AccountsBean accountsBean = new AccountsBeanImpl();
			boolean isUpdate = accountsBean.deleteAccountDetails(emailId);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("status", isUpdate);
			return jsonObject.toString();
		} catch (Exception e) {
			throw e;
		}
	}

	private boolean isValidEmailId(String emailId) throws Exception {
		if (new AccountsBeanImpl().isAccountExists(emailId)) {
			return true;
		}
		throw new Exception();
	}
}
