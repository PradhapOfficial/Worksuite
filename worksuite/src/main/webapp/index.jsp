<%@page import="com.worksuite.rest.api.common.APIUtil"%>
<%@page import="com.worksuite.core.bean.OrgPOJO"%>
<%@page import="com.worksuite.core.bean.OrgBeanImpl"%>
<%@page import="com.worksuite.core.bean.OrgBean"%>
<%@page import="com.worksuite.core.bean.UserBean"%>
<%@page import="com.worksuite.core.bean.UserBeanImpl"%>
<%@page import="com.worksuite.rest.api.common.AuthorizationUtils"%>
<%@page import="com.worksuite.rest.api.common.ConfigConstants"%>
<%@page import="com.worksuite.db.util.ApplicationUtils"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>Worksuite</title>
</head>
<body>
<%
	String token = request.getHeader(ConfigConstants.X_CSRF_TOKEN);
	if(token == null){
		String tempToken = request.getParameter("token");
		if(tempToken != null){
			StringBuilder tokenBuilder = new StringBuilder(tempToken.length() + 10)
					.append(ConfigConstants.WS_CSRF_BEARER).append(tempToken);
			
			token = tokenBuilder.toString();
		}
	}
	
	String orgId = null;
	String userId = null;
	if(token != null){
		//userId = AuthorizationUtils.claimDetailsFromToken(token.split("=")[1]);
		userId = String.valueOf(APIUtil.getUserId(request));
		try{
			OrgBean orgBean = new OrgBeanImpl();
			OrgPOJO orgPojo = orgBean.getFirstOrgDetails(Long.parseLong(userId));
			orgId = String.valueOf(orgPojo.getOrgId());
		}catch(Exception e){
			
		}
	}
	boolean isDevelopment = ApplicationUtils.isDevelopment();
	String staticURL = (isDevelopment ? "http://localhost:3001/static/js/bundle.js" : "static/react-client.js");
%>
<script>
	var isDevelopment = <%=isDevelopment%>;
	var token = "<%=token%>"
	var orgId = "<%=orgId%>"
	var userId = "<%=userId%>"
	
</script>
	<div id="worksuiteMainContainer" style="height:100%;weight:100%"></div>
	
	<script src="<%=staticURL%>" type="text/javascript"></script>
</body>
</html>