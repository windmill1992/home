<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,minimal-ui" />
<title>用户信息</title>
</head>
<style>
	body{font-family: "Helvetica Neue",Helvetica,STHeiTi,sans-serif;}
</style>
<body>用户名：${user.realName };ID:${user.id };身份证:${user.idCard }
</body>
</html>