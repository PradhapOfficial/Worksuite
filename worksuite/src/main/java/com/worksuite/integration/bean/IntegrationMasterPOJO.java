package com.worksuite.integration.bean;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
	
	public JsonObject toJsonObject() {
		return (JsonObject) new Gson().toJsonTree(this, IntegrationMasterPOJO.class);
	}
	
	@Override
	public String toString() {
		return toJsonObject().toString();
	}
	
	public IntegrationMasterPOJO encrptToken() {
		this.getAuthDetails().setToken("****");
		return this;
	}
}
