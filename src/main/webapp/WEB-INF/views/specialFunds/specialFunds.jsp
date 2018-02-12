<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<title>专项基金</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/specialFunds/specialFunds.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/> 
</head> 
<body>
<%-- <%@ include file="./../common/newhead.jsp" %> --%>
<!--主体 start-->
<!--开始-->
<div class="fundsContent">
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg1.jpg">
        <ul>
            <a href="<%=resource_url%>project/view/?projectId=3208"><li></li></a>
            <a href="<%=resource_url%>project/view/?projectId=3209"><li></li></a>
            <a href="<%=resource_url%>project/view/?projectId=3210"><li></li></a>
            <a href="<%=resource_url%>project/view/?projectId=3211"><li></li></a>
            <a href="<%=resource_url%>project/view/?projectId=3212"><li></li></a>
        </ul>
    </div>
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg2.jpg">
        <a href="<%=resource_url%>project/index/?keyWords=专项" class="linkTwo"></a>
    </div>
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg3.jpg">
        <a href="<%=resource_url%>project/view/?projectId=3208" class="linkTgree"></a>
    </div>
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg4.jpg">
        <a href="<%=resource_url%>project/view/?projectId=3209" class="linkFour"></a>
    </div>
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg5.jpg">
        <a href="<%=resource_url%>project/view/?projectId=3210" class="linkFive"></a>
    </div>
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg6.jpg">
        <a href="<%=resource_url%>project/view/?projectId=3211" class="linkSix"></a>
    </div>
    <div class="funcdsFrist">
        <img src="<%=resource_url%>res/images/specialFunds/specialImg7.jpg">
        <a href="<%=resource_url%>project/view/?projectId=3212" class="linkSeven"></a>
    </div>
</div>
<!--主体 end-->
<%@ include file="./../common/newfooter.jsp" %>
<%-- <script data-main="<%=resource_url%>res/js/dev/about.js" src="<%=resource_url%>res/js/require.min.js"></script> --%>
</body>
</html>