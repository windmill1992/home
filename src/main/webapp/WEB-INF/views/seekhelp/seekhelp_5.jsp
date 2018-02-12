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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/needhelp.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/> 
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer">
  <div class="page clear">
    <!-- <div class="page-hd">
      <h2 class="w1000">我要求助</h2>
    </div> -->
    <div class="page-bd">
    	<div class="publish">
        	<div class="step-progress">            	
            </div>
            <div class="step-detail">
            <div class="finish_info">
            	<ul>
                	<li class="line1_5">您的申请已提交，客服会在2小时内联系您</li>
                    <li class="line3">请保持您的联系方式通畅，以方便我们的客服及时与您联系</li>
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
