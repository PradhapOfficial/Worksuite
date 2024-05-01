package com.worksuite.integration.bean;

import java.util.List;

public interface IntegrationBean {
	
	public IntegrationMasterPOJO addIntegDetails(final Long orgId, final Long userId, final Long departmentId, IntegrationPOJO integrationPojo, AuthPOJO authPojo,  List<IntegrationPropertyPOJO> listOfIntegPropPojo);
	
	public IntegrationPOJO updateIntegDetails(final long orgId, final long userId, IntegrationPOJO integrationPojo);
	
	public IntegrationPOJO getIntegDetails(final long orgId, final long userId, final long integrationId);
	
	public List<IntegrationPOJO> getListOfIntegDetails(final long orgId, final long userId, final long integrationId);
	
	public boolean deleteIntegnDetails(long orgId, long userId, long integrationId) throws Exception;

	public IntegrationMasterPOJO getIntegDetails(long integrationId, int level);
	
}
