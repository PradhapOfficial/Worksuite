package com.worksuite.rest.api.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.worksuite.core.bean.TokenMappingBean;
import com.worksuite.core.bean.TokenMappingBeanImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthorizationUtils {

	private static Logger LOGGER = LogManager.getLogger(AuthorizationUtils.class.getName());
	
	public String getToken(String userDetails) {
		try {
			String token = Jwts.builder().setSubject("user")
					.claim(ConfigConstants.USER_DETAILS_CLAIM, userDetails)
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
			
			String userDetailsStr = (String) claims.get(ConfigConstants.USER_DETAILS_CLAIM);
			JsonObject userDetailsJson = new Gson().fromJson(userDetailsStr, JsonObject.class);
			
			long userId = userDetailsJson.get(ConfigConstants.USER_ID).getAsLong();
			
			TokenMappingBean tokenMappingBean = new TokenMappingBeanImpl();
			if(tokenMappingBean.getToken(userId, token) != null) {
				return true;
			}
				    
			LOGGER.log(Level.INFO, "InValid token ::  ");
		}catch(Exception e) {
			LOGGER.error("Exception occured while isValidToken :: " + e);
		}
		return false;
	} 
}
