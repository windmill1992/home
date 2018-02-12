<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网 - 我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/needhelp.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/> 
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer">
  <div class="page clear">
    <!--  <div class="page-hd">
      <h2 class="w1000">我要求助</h2>
    </div>-->
    <div class="page-bd">
    	<div class="publish">
            <div class="step-detail">
            <div class="finish_info">
            	<ul>
                	<li class="line1">${error}</li>
                    <li class="line3"><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes" class="next">联系客服</a></li>
                </ul>
            </div>
          </div>
        </div>
    </div>
  </div>
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/needhelp.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
