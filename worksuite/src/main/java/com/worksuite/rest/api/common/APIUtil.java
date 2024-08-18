package com.worksuite.rest.api.common;

import com.worksuite.core.bean.UserBean;
import com.worksuite.core.bean.UserBeanImpl;
import com.worksuite.core.bean.UserMasterPOJO;
import com.worksuite.core.bean.UserPOJO;
import com.worksuite.integration.bean.IntegrationBean;
import com.worksuite.integration.bean.IntegrationBeanImpl;
import com.worksuite.integration.bean.IntegrationMasterPOJO;
import com.worksuite.integration.bean.IntegrationPOJO;
import com.worksuite.integration.bean.ScopeBean;
import com.worksuite.integration.bean.ScopeBeanImpl;
import com.worksuite.integration.bean.ScopePOJO;
import com.worksuite.integration.util.IntegrationConstants;

public class APIUtil {
	
	private ScopePOJO scopePojo;
	
	private IntegrationMasterPOJO integrationMasterPOJO;
	
	private UserMasterPOJO userMasterPOJO;
	
	private UserPOJO userPojo;
	
	public boolean isValidAppId(long appId) throws RestException {
		if(!IntegrationConstants.isValidAppId(appId)) {
			throw new RestException(ErrorCode.INVALID_APP_ID);
		}
		return true;
	}
	
	public boolean isValidScopeByIntegrationId(long orgId, long appId, long integrationId, Long userId, Long departmentId) throws RestException {
		
		isValidAppId(appId);
		
		isUserPresentInOrg(orgId, userId);
		
		if(this.scopePojo == null) {
			isScopeRegistered(orgId, appId);
		}
		
		if(this.integrationMasterPOJO == null) {
			isValidIntegId(orgId, integrationId);
		}
		
		int level = scopePojo.getLevel();
		IntegrationPOJO integrationPojo = this.integrationMasterPOJO.getIntegrationDetails();
		if(level == 1) {
			if(integrationPojo.getDepartmentId() != null || integrationPojo.getUserId() != null) {
				throw new RestException(ErrorCode.INTEGRATION_SCOPE_CHANGED);
			}
		}else if(level == 2) {
			if(integrationPojo.getDepartmentId() == null) {
				throw new RestException(ErrorCode.INTEGRATION_SCOPE_CHANGED);
			}
		}else {
			if(integrationPojo.getUserId() == null) {
				throw new RestException(ErrorCode.INTEGRATION_SCOPE_CHANGED);
			}
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
	
	public boolean isValidIntegId(long orgId, long integId) throws RestException {
		if(integrationMasterPOJO == null) {
			IntegrationBean integBean = new IntegrationBeanImpl();
			this.integrationMasterPOJO = integBean.getIntegrationDetailsByIntegrationId(orgId, integId);
			if(integrationMasterPOJO == null) {
				throw new RestException(ErrorCode.INVALID_INTEGRATION_ID);
			}
		}
		return true;
	}
	
	public boolean isUserPresentInOrg(long orgId, long userId) throws RestException {
		UserBean userBean = new UserBeanImpl();
		this.userMasterPOJO = userBean.getUserDetailsById(userId, orgId);
		return true;
	}
	
	public boolean isAdminUser(long orgId, long userId) throws RestException{
		return isAdminUser(orgId, userId, true);
	}
	
	public boolean isAdminUser(long orgId, long userId, boolean isExceptionNeeded) throws RestException{
		isUserPresentInOrg(orgId, userId);
		
		int role = this.userMasterPOJO.getOrgDetails().getRoleDetails().getRoleValue();
		
		if(role == IntegrationConstants.Roles.MEMBER.getValue()) {
			if(isExceptionNeeded) {
				throw new RestException(ErrorCode.NOT_ORG_ADMIN);
			}
			return false;
		}
		return true;
	}
	
	public boolean isValidUserId(long userId) throws RestException {
		UserBean userBean = new UserBeanImpl();
		this.userPojo = userBean.getUserDetails(userId);
		
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
	
	public UserPOJO getUserPojo() {
		return this.userPojo;
	}
	
	public static long getUniqueIdByLevel(int level, Long orgId, Long departmentId, Long userId) {
		switch(level) {
			case 1: return orgId;
			case 2: return departmentId;
			default: return userId;
		}
	}
	
	public boolean isUserAllowedToIntegrateByScope(long orgId, Long userId, Long departmentId, int level) throws RestException{
		if(isAdminUser(orgId, userId, false)) {
			return true;
		}
		
		if(level == 1) {
			throw new RestException(ErrorCode.NOT_ORG_ADMIN);
		}
		
		if(level == 2) {
			//throw Exception if current user is department member
		}
		
		//Allow members of an org.
		return true;
	}
	
	public boolean isUserAllowedToModifyIntegration(long orgId, Long userId, Long departmentId, int level) throws RestException{
		IntegrationBean integBean = new IntegrationBeanImpl();
		IntegrationMasterPOJO integrationMasterPojo = integBean.getIntegDetailsByLevel(orgId, getUniqueIdByLevel(level, orgId, departmentId, userId), level);
		
		return isUserAllowedToModifyIntegration(integrationMasterPojo.getIntegrationDetails().getIntegrationId(), orgId, userId, departmentId, level);
	}
	
	public boolean isUserAllowedToModifyIntegration(long integrationId, long orgId, Long userId, Long departmentId, int level) throws RestException{
		if(this.integrationMasterPOJO == null) {
			isValidIntegId(orgId, integrationId);
		}
		
		if(isAdminUser(orgId, userId, false)) {
			return true;
		}
		
		if(level == 1) {
			throw new RestException(ErrorCode.NOT_ORG_ADMIN);
		}
		
		if(level == 2) {
			//throw Exception if current user is department member
			return true;
		}
		
		if(!userId.equals(integrationMasterPOJO.getIntegrationDetails().getUserId())) {
			throw new RestException(ErrorCode.USER_NOT_ASSOCIATED_INTEGRATION);
		}
		return true;
	}
	
}
