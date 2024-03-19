package com.worksuite.integration.bean;

public interface ScopeBean {
	
	public ScopePOJO addScopeDetails(final long orgId, final long appId, ScopePOJO scopePojo);
	
	public ScopePOJO getScopeDetails(final long orgId, final long appId);
	
	public ScopePOJO updateScopeDetails(final long orgId, final long appId, ScopePOJO scopePojo);
	
	public boolean deleteScopeDetails(final long orgId, final long appId);
}
