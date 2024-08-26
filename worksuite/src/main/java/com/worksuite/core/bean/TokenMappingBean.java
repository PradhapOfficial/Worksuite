package com.worksuite.core.bean;

public interface TokenMappingBean {

	public TokenMappingPojo getToken(long userId, String token);
	
	public TokenMappingPojo addToken(TokenMappingPojo tokenMappingPojo);
	
	public TokenMappingPojo updateToken(TokenMappingPojo tokenMappingPojo);
	
	public boolean deleteToken(long userId, String token);
}
