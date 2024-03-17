package com.worksuite.core.bean;

public interface OrgBean {
	
	public OrgPOJO getOrgDetails(long userId, long orgId);
	
	public OrgPOJO addOrgDetails(long userId, OrgPOJO orgPojo);
	
	public OrgPOJO updateOrgDetails(long userId, long orgId, OrgPOJO orgPojo);
	
	public boolean deleteOrgDetails(long userId, long orgId);
}
