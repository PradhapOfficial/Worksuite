package com.worksuite.rest.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import com.worksuite.core.bean.AccountsBean;
import com.worksuite.core.bean.AccountsBeanImpl;
import com.worksuite.core.bean.TokenMappingBean;
import com.worksuite.core.bean.TokenMappingBeanImpl;
import com.worksuite.core.bean.TokenMappingPojo;
import com.worksuite.core.bean.UserPOJO;
import com.worksuite.rest.api.common.AuthorizationUtils;
import com.worksuite.rest.api.common.ConfigConstants;

public class CustomeRequestFilter {
//
//	
//	public void filter(ContainerRequestContext request) throws IOException {
//		HttpServletRequest httpRequest = 	((HttpServletRequest)request);
//		String csrfToken = httpRequest.getHeader(ConfigConstants.X_CSRF_TOKEN);
//		if(csrfToken == null) {
//			String reqUri = httpRequest.getRequestURI();
//			if((!reqUri.matches("/worksuite/api/v1/login") || !reqUri.matches("/worksuite/index"))) {
//				httpRequest.getRequestDispatcher("/login.jsp").forward(httpRequest, null);
//			}
//			
//			String userName = httpRequest.getParameter(ConfigConstants.USER_NAME);
//			String password = httpRequest.getParameter(ConfigConstants.PASSWORD);
//			
//			AccountsBean accountBean = new AccountsBeanImpl();
//			UserPOJO userPojo = accountBean.getAccountDetails(userName, password);
//			
//			if(userPojo == null) {
//				LOGGER.info("Invalid user name and password");
//				//httpResponse.sendError(401, "UNAUTHORIZED");
//				return;
//			}
//			
//			long userId = userPojo.getUserId();
//			AuthorizationUtils authUtils = new AuthorizationUtils();
//			String token = authUtils.getToken(userPojo.toString());
//			
//			TokenMappingPojo tokenMappingPojo = new TokenMappingPojo().setUserId(userId).setToken(token);
//			
//			TokenMappingBean tokenMappingBean = new TokenMappingBeanImpl();
//			if((tokenMappingPojo = tokenMappingBean.addToken(tokenMappingPojo)) == null) {
//				//httpResponse.sendError(401, "UNAUTHORIZED");
//				return;
//			}
//			
//			StringBuilder tokenBuilder = new StringBuilder(ConfigConstants.WS_CSRF_BEARER);
//			tokenBuilder.append(token);
//			
//			httpResponse.addHeader(ConfigConstants.X_CSRF_TOKEN, tokenBuilder.toString());
//			//request.getRequestDispatcher("/index.jsp").forward(request, response);
//			((HttpServletResponse)response).sendRedirect("http://localhost:8081/worksuite/index.jsp?userName=pradhap@gmail.com&passWord=Pradhap@123");
//			return;
//		}else {
//			String token = csrfToken.split("=")[1];
//			AuthorizationUtils authUtils = new AuthorizationUtils();
//			if(!authUtils.isValidToken(token)) {
//				request.getRequestDispatcher("/login.jsp").forward(request, response);
//			}
//		}
//		chain.doFilter(request, response);
//	}

}
