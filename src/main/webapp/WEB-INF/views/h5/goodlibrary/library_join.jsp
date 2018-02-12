<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<!-- <meta name="viewport"/> -->
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/bind.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/feedback.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/register.css?v=20160404">
<title>加入善库</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/library_bind.css">
</head> 
<body>
<div class="join">
    <div class="join_top">
        <span class="join_fl fl"></span>
        <p class="join_fr ">恭喜您！</p>
    </div>
    <p class="join_text">成为“${goodlibrary.nickName }”善库成员~</p>
</div>
<div class="join_z">
    <p class="joinz_fl fl">当前善库可用余额</p>
    <p class="joinz_fr fr">￥${goodlibrary.balance }</p>
</div>
<div class="join_z">
    <p class="joinz_fl fl">您今天可用余额</p>
    <p class="joinz_fr joinz_color fr">￥${goodlibrary.defaultMoney }</p>
</div>
<a href='<%=resource_url%>index/index_h5.do'>
<div class="foot_qt">
    <p>开始行善</p>
</div>
</a>
<%-- <script data-main="<%=resource_url%>res/js/h5/goodlibrary.js?v=20151106133311" src="<%=resource_url%>res/js/require.min.js"></script> --%>
</body>
</html>