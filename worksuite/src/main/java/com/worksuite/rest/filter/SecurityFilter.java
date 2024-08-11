package com.worksuite.rest.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worksuite.db.util.HttpConstants;
import com.worksuite.rest.api.common.AuthorizationUtils;
import com.worksuite.rest.api.common.ConfigConstants;

public class SecurityFilter implements Filter {
	
	private static final Logger LOGGER = LogManager.getLogger(SecurityFilter.class.getName());
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = ((HttpServletRequest)request);
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String csrfToken = httpRequest.getHeader(ConfigConstants.X_CSRF_TOKEN);
		String reqUri = httpRequest.getRequestURI();
		
		Pattern urlPattern = Pattern.compile("/(worksuite/)api/v");
		Matcher apiUrlMatcher = urlPattern.matcher(reqUri);
		
		if(csrfToken == null) {
			if(reqUri.matches("/(worksuite/)index.jsp")) {
				String token = httpRequest.getParameter("token");
				if(token != null) {
					if(!AuthorizationUtils.isValidToken(token)) {
						httpResponse.sendRedirect("login.jsp");
					}
				}else {
					LOGGER.log(Level.INFO, "Token not exist");
					httpResponse.sendRedirect("login.jsp");	
				}
			}else if(Pattern.matches("/(worksuite/)api/v\\d+/account", reqUri) && HttpConstants.POST_METHOD.equalsIgnoreCase(httpRequest.getMethod())) {
				
			} else if(apiUrlMatcher.find()) {
				httpResponse.sendError(401, "UNAUTHORIZED");
			}else if(!reqUri.matches("/(worksuite/)login.jsp")) {
				httpResponse.sendRedirect("login.jsp");	
			}
		}else {
			String token = csrfToken.split("=")[1];
			if(!AuthorizationUtils.isValidToken(token)) {
				if((Pattern.matches("/(worksuite/)api/v\\d+/account", reqUri) && HttpConstants.POST_METHOD.equalsIgnoreCase(httpRequest.getMethod()))) {
					
				}else if(apiUrlMatcher.find()) {
					httpResponse.sendError(401, "UNAUTHORIZED");
				}else {
					httpResponse.sendRedirect("login.jsp");
				}
			}
		}
		chain.doFilter(request, response);
	}
}
