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
%>
<script>
	var isDevelopment = <%=ApplicationUtils.isDevelopment()%>;
	var token = "<%=token%>"
</script>
	<div id="worksuiteMainContainer" style="height:100%;weight:100%"></div>
	<script src="http://localhost:3001/static/js/bundle.js" type="text/javascript"></script>
</body>
</html>