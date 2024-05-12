package com.worksuite.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.rest.api.common.ConfigConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class CustomResponseFilter implements ContainerResponseFilter{

	private static Logger LOGGER = LogManager.getLogger(CustomResponseFilter.class.getName());
	
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		
		String token = requestContext.getHeaderString(ConfigConstants.X_CSRF_TOKEN);
		if(token == null) {
			token = getToken();
			StringBuilder tokenBuilder = new StringBuilder(ConfigConstants.WS_CSRF_BEARER);
			tokenBuilder.append(token);
			responseContext.getHeaders().add(ConfigConstants.X_CSRF_TOKEN, tokenBuilder.toString());
		}
	}
	
	public String getToken() {
		try {
			String token = Jwts.builder().setSubject("user")
					.claim("userId", 123)
					.signWith(SignatureAlgorithm.HS512, ConfigConstants.SECRET_KEY)
				    .compact();
			return token;
		}catch(Exception e) {
			LOGGER.error("Exception occured while getToken :: " + e);
		}
		return null;
	}
	
	public boolean isValidToken(String token) {
		try {
			Claims claims = Jwts.parser()
				        .setSigningKey(ConfigConstants.SECRET_KEY)
				        .parseClaimsJws(token)
				        .getBody();
				    
			LOGGER.log(Level.INFO, "valid token ::  " + claims.toString());
			return true;
		}catch(Exception e) {
			LOGGER.error("Exception occured while isValidToken :: " + e);
		}
		return false;
	} 

}
