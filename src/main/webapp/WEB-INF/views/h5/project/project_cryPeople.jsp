<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网-项目求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/h5/base.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>/res/css/h5/enlist.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/infoblocknew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/index_help.css"/>
<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head> 
<body STYLE="background-color: #EEEEEE;">
	<div class="topNav">
    <a href="javascript:history.go(-1)" class="backLink"></a>
    <h1>项目求助</h1>
	</div>
	<div>
	    <div class="form-item">
	        <span>姓&nbsp;名</span>
	        <input type="text" id="name" data-placeholder="请输入您的姓名" />
	    </div>
	    <div class="form-item">
	        <span>联系电话</span>
	        <input type="text" id="mobile" data-placeholder="请输入您的电话号码" />
	    </div>
	    <div class="form-item">
	        <span>单&nbsp;位</span>
	        <input type="text" id="workUnit" data-placeholder="请输入您的工作单位" />
	    </div>
	    <div class="form-item">
	        <span>联系地址</span>
	        <input type="text" id="linkAddress" data-placeholder="请输入您的联系地址" />
	    </div>
	</div>
	<div style="text-align: center">
    <button class="btnStyle" id = "crySubmit"  style="float: none; margin:0.7rem 0;">确认求助</button>
	</div>
</body>
<input type="hidden" id ="projectId" value="${projectId}">
<script data-main="<%=resource_url%>res/js/dev/addProjectCryPeople.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>
</html>