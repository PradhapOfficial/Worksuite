package com.worksuite.integration.bean;

import java.util.List;

public class IntegrationMasterPOJO {

	private IntegrationPOJO integrationDetails;
	
	private List<IntegrationPropertyPOJO> propertyDetails;
	
	private AuthPOJO authDetails;

	public IntegrationPOJO getIntegrationDetails() {
		return integrationDetails;
	}

	public IntegrationMasterPOJO setIntegrationDetails(IntegrationPOJO integrationDetails) {
		this.integrationDetails = integrationDetails;
		return this;
	}

	public List<IntegrationPropertyPOJO> getPropertyDetails() {
		return propertyDetails;
	}

	public IntegrationMasterPOJO setPropertyDetails(List<IntegrationPropertyPOJO> propertyDetails) {
		this.propertyDetails = propertyDetails;
		return this;
	}

	public AuthPOJO getAuthDetails() {
		return authDetails;
	}

	public IntegrationMasterPOJO setAuthDetails(AuthPOJO authDetails) {
		this.authDetails = authDetails;
		return this;
	}
}
