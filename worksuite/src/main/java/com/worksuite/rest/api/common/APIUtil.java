package com.worksuite.rest.api.common;

import com.worksuite.core.bean.UserBean;
import com.worksuite.core.bean.UserBeanImpl;
import com.worksuite.core.bean.UserMasterPOJO;
import com.worksuite.core.bean.UserPOJO;
import com.worksuite.integration.bean.IntegrationBean;
import com.worksuite.integration.bean.IntegrationBeanImpl;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.ScopeBean;
import com.worksuite.integration.bean.ScopeBeanImpl;
import com.worksuite.integration.bean.ScopePOJO;
import com.worksuite.integration.util.IntegrationConstants;

public class APIUtil {
	
	private ScopePOJO scopePojo;
	
	private IntegrationMasterPOJO integrationMasterPOJO;
	
	private UserMasterPOJO userMasterPOJO;
	
	public boolean isValidAppId(long appId) throws RestException {
		if(!IntegrationConstants.isValidAppId(appId)) {
			throw new RestException(ErrorCode.INVALID_APP_ID);
		}
		return true;
	}
	
	public boolean isScopeRegistered(long orgId, long appId) throws RestException {
		
		isValidAppId(appId);
		
		ScopeBean scopeBean = new ScopeBeanImpl();
		this.scopePojo = scopeBean.getScopeDetails(orgId, appId);
		if(scopePojo == null) {
			throw new RestException(ErrorCode.SCOPE_NOT_REGISTERED);
		}
		return true;
	}
	
	public boolean isValidIntegId(long integId) throws RestException {
		IntegrationBean integBean = new IntegrationBeanImpl();
		this.integrationMasterPOJO = integBean.getIntegDetails(integId, scopePojo.getLevel());
	
		if(integrationMasterPOJO == null) {
			throw new RestException(ErrorCode.INVALID_INTEGRATION_ID);
		}
		return true;
	}
	
	public boolean isUserPresentInOrg(long orgId, long userId) throws RestException {
		UserBean userBean = new UserBeanImpl();
		this.userMasterPOJO = userBean.getUserDetailsById(userId, orgId);
		
		if(this.userMasterPOJO == null) {
			throw new RestException(ErrorCode.INVALID_USER_NOT_PRESENT_IN_ORG);
		}
		return true;
	}
	
	public boolean isAdminUser(long orgId, long userId) throws RestException{
		isUserPresentInOrg(orgId, userId);
		
		int role = this.userMasterPOJO.getOrgDetails().getRoleDetails().getRoleValue();
		
		if(!(role == IntegrationConstants.Roles.SUPER_ADMIN.getValue() || role == IntegrationConstants.Roles.ADMIN.getValue())) {
			throw new RestException(ErrorCode.NOT_ORG_ADMIN);
		}
		return true;
	}
	
	public boolean isValidUserId(long userId) throws RestException {
		UserBean userBean = new UserBeanImpl();
		UserPOJO userPojo = userBean.getUserDetails(userId);
		
		if(userPojo == null) {
			throw new RestException(ErrorCode.INVALID_USER_ID);
		}
		return true;
	}
	
	public ScopePOJO getScopePojo() {
		return this.scopePojo;
	}
	
	public IntegrationMasterPOJO getIntegrationMasterPOJO() {
		return this.integrationMasterPOJO;
	}
}
