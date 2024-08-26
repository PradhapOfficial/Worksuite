package com.worksuite.integration.bean;

import com.worksuite.rest.api.common.RestException;

public interface ScopeBean {
	
	public ScopePOJO addScopeDetails(final long orgId, final long appId, ScopePOJO scopePojo) throws RestException;
	
	public ScopePOJO getScopeDetails(final long orgId, final long appId) throws RestException;
	
	public ScopePOJO updateScopeDetails(final long orgId, final long appId, ScopePOJO scopePojo) throws RestException;
	
	public boolean deleteScopeDetails(final long orgId, final long appId) throws RestException;
}
