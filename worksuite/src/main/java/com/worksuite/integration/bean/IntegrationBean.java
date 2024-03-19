package com.worksuite.integration.bean;

import java.util.List;

public interface IntegrationBean {
	
	public IntegrationPOJO addIntegDetails(final long orgId, final long userId, IntegrationPOJO integrationPojo);
	
	public IntegrationPOJO updateIntegDetails(final long orgId, final long userId, IntegrationPOJO integrationPojo);
	
	public IntegrationPOJO getIntegDetails(final long orgId, final long userId, final long integrationId);
	
	public List<IntegrationPOJO> getListOfIntegDetails(final long orgId, final long userId, final long integrationId);
	
	public boolean deleteIntegnDetails(final long orgId, final long userId);
	
}
