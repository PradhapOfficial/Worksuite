package com.worksuite.rest.api;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.core.bean.OrgBean;
import com.worksuite.core.bean.OrgBeanImpl;
import com.worksuite.core.bean.OrgPOJO;
import com.worksuite.rest.api.common.APIUtil;
import com.worksuite.rest.api.common.ErrorCode;
import com.worksuite.rest.api.common.RestException;

@Path("orgs")
public class OrgAPI extends APIUtil {

	private static final Logger LOGGER = LogManager.getLogger(OrgAPI.class.getName());
	
	@GET
	@Path("{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrgPOJO getOrgDetails(@PathParam("orgId") long orgId, @Context HttpServletRequest request) throws RestException {
		try {
			long userId = APIUtil.getUserId(request);
			isUserPresentInOrg(orgId, userId);
			
			return new OrgBeanImpl().getOrgDetails(userId, orgId);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while getOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@PUT
	@Path("{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrgPOJO updateOrgDetails(@PathParam("orgId") long orgId, String jsonStr, @Context HttpServletRequest request) throws RestException {
		try {
			long userId = APIUtil.getUserId(request);
			isAdminUser(orgId, userId);
			
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			OrgPOJO orgPojo = new OrgPOJO(jsonObj).setModifiedBy(userId);

			OrgBean orgBean = new OrgBeanImpl();
			return orgBean.updateOrgDetails(userId, orgId, orgPojo);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while updateOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public OrgPOJO addOrgDetails(String jsonStr, @Context HttpServletRequest request) throws RestException  {
		try {
			long userId = APIUtil.getUserId(request);
			isValidUserId(userId);
			isSuperAdminInExistingOrg(userId);
			
			JsonObject jsonObj = new Gson().fromJson(jsonStr, JsonObject.class);
			OrgPOJO orgPojo = new OrgPOJO(jsonObj).setCreatedBy(userId).setModifiedBy(userId).setStatus(1);

			OrgBean orgBean = new OrgBeanImpl();
			return orgBean.addOrgDetails(userId, orgPojo);
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while addOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@DELETE
	@Path("{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteOrgDetails(@PathParam("orgId") long orgId, @Context HttpServletRequest request) throws RestException  {
		try {
			long userId = APIUtil.getUserId(request);
			isAdminUser(orgId, userId);
			
			OrgBean orgBean = new OrgBeanImpl();
			JsonObject jsonObj = new JsonObject();
			jsonObj.addProperty("status", orgBean.deleteOrgDetails(userId, orgId));
			return jsonObj.toString();
		}catch(RestException re) {
			throw re;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception Occured while deleteOrgDetails :: ", e);
			throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
