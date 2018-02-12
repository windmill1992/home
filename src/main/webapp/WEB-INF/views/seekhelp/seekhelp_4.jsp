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
        	<div class="step-progress step4">            	
            </div>
            <div class="step-detail">
            <div class="finish_info">
            	<ul>
                	<li class="line1">您提交的申请已进入审核期</li>
                    <li class="line2">我们的工作人员会在3个工作日内通过站内信告知您审核结果</li>
                    <li class="line3"><a href="<%=resource_url%>ucenter/pindex.do" class="next">我的求助</a></li>
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
