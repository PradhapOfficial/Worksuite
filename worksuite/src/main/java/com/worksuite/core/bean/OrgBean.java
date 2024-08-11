package com.worksuite.core.bean;

import com.worksuite.rest.api.common.RestException;

public interface OrgBean {

	public OrgPOJO getOrgDetails(long userId, long orgId) throws RestException;
	
	public OrgPOJO getFirstOrgDetails(long userId) throws RestException;

	public OrgPOJO addOrgDetails(long userId, OrgPOJO orgPojo) throws RestException;

	public OrgPOJO updateOrgDetails(long userId, long orgId, OrgPOJO orgPojo) throws RestException;

	public boolean deleteOrgDetails(long userId, long orgId) throws RestException;
}
