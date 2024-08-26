package com.worksuite.integration.bean;

import java.util.List;

import com.worksuite.rest.api.common.RestException;

public interface IntegrationBean {
	
	public IntegrationMasterPOJO addIntegDetails(Long orgId, Long uniqueId, IntegrationPOJO integrationPojo, AuthPOJO authPojo, List<IntegrationPropertyPOJO> listOfIntegPropPojo, int level) throws RestException;
	
	public IntegrationPOJO updateIntegDetails(final long orgId, final long userId, IntegrationPOJO integrationPojo) throws RestException;
	
	public List<IntegrationPOJO> getListOfIntegDetails(final long orgId, final long userId, final long integrationId) throws RestException;
	
	public boolean deleteIntegnDetails(long orgId, long userId, long integrationId) throws RestException;
	
	public IntegrationMasterPOJO getIntegrationDetailsByIntegrationId(long orgId, long integrationId) throws RestException;
	
	public IntegrationMasterPOJO getIntegDetailsByLevel(long orgId, long uniqueId, int level) throws RestException;
}
