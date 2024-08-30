package com.worksuite.rest.api.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.core.bean.TokenMappingBean;
import com.worksuite.core.bean.TokenMappingBeanImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthorizationUtils {

	private static Logger LOGGER = LogManager.getLogger(AuthorizationUtils.class.getName());
	
	public static String getToken(String userId) {
		try {
			String token = Jwts.builder().setSubject("user" + System.currentTimeMillis())
					.claim(ConfigConstants.USER_DETAILS_CLAIM, userId)
					.signWith(SignatureAlgorithm.HS512, ConfigConstants.SECRET_KEY)
				    .compact();
			return token;
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getToken :: ", e);
		}
		return null;
	}
	
	public static boolean isValidToken(String token) {
		try {
			
			Long userId = Long.valueOf(claimDetailsFromToken(token));
			
			TokenMappingBean tokenMappingBean = new TokenMappingBeanImpl();
			if(tokenMappingBean.getToken(userId, token) != null) {
				return true;
			}
				    
			LOGGER.log(Level.INFO, "InValid token ::  ");
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while isValidToken :: ", e);
		}
		return false;
	}
	
	public static String claimDetailsFromToken(String token) throws Exception{
		Claims claims = Jwts.parser()
		        .setSigningKey(ConfigConstants.SECRET_KEY)
		        .parseClaimsJws(token)
		        .getBody();
	
		return claims.get(ConfigConstants.USER_DETAILS_CLAIM) + "";
//		try {
//			Claims claims = Jwts.parser()
//			        .setSigningKey(ConfigConstants.SECRET_KEY)
//			        .parseClaimsJws(token)
//			        .getBody();
//		
//			return claims.get(ConfigConstants.USER_DETAILS_CLAIM) + "";
//		}catch(Exception e) {
//			LOGGER.log(Level.ERROR, "Exception occured while claimDetailsFromToken :: ", e);
//		}
	}
	
	public static Long getUserIdFromToken(String token){
		try {
			String userId = claimDetailsFromToken(token);
			return Long.parseLong(userId);
		}catch(Exception e) {
			LOGGER.log(Level.ERROR, "Exception occured while getUserIdFromToken :: ", e);
		}
		return null;
	}
}
