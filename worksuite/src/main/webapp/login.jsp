<%@page import="com.worksuite.core.bean.TokenMappingBeanImpl"%>
<%@page import="com.worksuite.core.bean.TokenMappingBean"%>
<%@page import="com.worksuite.core.bean.TokenMappingPojo"%>
<%@page import="com.worksuite.rest.api.common.AuthorizationUtils"%>
<%@page import="com.worksuite.core.bean.AccountsBeanImpl"%>
<%@page import="com.worksuite.core.bean.UserPOJO"%>
<%@page import="com.worksuite.core.bean.AccountsBean"%>
<%@page import="com.worksuite.rest.api.common.ConfigConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
<style type="text/css">
	body{
		eight: 100vh;
	    background-color: #393E46;
	    color: #d1d0c5;
	    overflow: hidden;
	}
	
	.formContainer{
		display: flex;
	    flex-direction: column;
	    width: 250px;
	}
	
	.fieldContainer{
		background: #222831;
	    color: #d1d0c5;
	    padding: 10px;
	    border: none;
	    border-radius: 10px;
	}
	
	.btnContainer{
		cursor: pointer;
	}

</style>
</head>
	<center>
		<form action="login.jsp" method="POST" class="formContainer">
	        Username: <input type="text" name="username" class="fieldContainer"><br>
	        Password: <input type="password" name="password" class="fieldContainer"><br>
	        <input type="submit" value="Login" class="fieldContainer btnContainer">
	    </form>
    </center>
   <% 
   		HttpServletRequest httpRequest = ((HttpServletRequest)request);
   		HttpServletResponse httpResponse = (HttpServletResponse)response;
   
   		String userName = httpRequest.getParameter(ConfigConstants.USER_NAME);
		String passWord = httpRequest.getParameter(ConfigConstants.PASSWORD);
   		
   		if(userName != null && passWord != null){
   			AccountsBean accountBean = new AccountsBeanImpl();
			UserPOJO userPojo = accountBean.getAccountDetails(userName, passWord);
			if(userPojo == null){
				out.println("Invalid user name and password");
				return;
			}
			
			long userId = userPojo.getUserId();
			AuthorizationUtils authUtils = new AuthorizationUtils();
			String token = authUtils.getToken(userPojo.toString());
			
			TokenMappingPojo tokenMappingPojo = new TokenMappingPojo().setUserId(userId).setToken(token);
			
			TokenMappingBean tokenMappingBean = new TokenMappingBeanImpl();
			if((tokenMappingPojo = tokenMappingBean.addToken(tokenMappingPojo)) == null) {
				httpResponse.sendError(401, "UNAUTHORIZED");
				return;
			}
			
			StringBuilder tokenBuilder = new StringBuilder(ConfigConstants.WS_CSRF_BEARER);
			tokenBuilder.append(token);
			
			response.addHeader(ConfigConstants.X_CSRF_TOKEN, tokenBuilder.toString());
			//request.getRequestDispatcher("index.jsp").forward(request, response);
			httpResponse.sendRedirect("index.jsp?token=" + token);
   		}
   %>
</html>