package com.worksuite.integration.bean;

import java.util.List;

import com.worksuite.rest.api.common.RestException;

public interface IntegrationBean {
	
	public IntegrationMasterPOJO addIntegDetails(final Long orgId, final Long userId, final Long departmentId, IntegrationPOJO integrationPojo, AuthPOJO authPojo,  List<IntegrationPropertyPOJO> listOfIntegPropPojo) throws RestException;
	
	public IntegrationPOJO updateIntegDetails(final long orgId, final long userId, IntegrationPOJO integrationPojo) throws RestException;
	
	public IntegrationPOJO getIntegDetails(final long orgId, final long userId, final long integrationId) throws RestException;
	
	public List<IntegrationPOJO> getListOfIntegDetails(final long orgId, final long userId, final long integrationId) throws RestException;
	
	public boolean deleteIntegnDetails(long orgId, long userId, long integrationId) throws RestException;

	public IntegrationMasterPOJO getIntegDetails(long integrationId, int level) throws RestException;
	
	public IntegrationMasterPOJO getIntegDetailsByLevel(long orgId, int level) throws RestException;
	
}
