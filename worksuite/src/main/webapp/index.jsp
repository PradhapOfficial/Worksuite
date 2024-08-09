<%@page import="com.worksuite.db.util.ApplicationUtils"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>Worksuite</title>
</head>
	
<body>
<script>
	var isDevelopment = <%=ApplicationUtils.isDevelopment()%>;
</script>
	<div id="worksuiteMainContainer" style="height:100%;weight:100%"></div>
	<script src="http://localhost:3001/static/js/bundle.js" type="text/javascript"></script>
</body>
</html>