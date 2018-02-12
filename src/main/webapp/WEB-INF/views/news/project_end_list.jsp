<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园-结项报告</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
</head> 
<body>
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="" >首页</a> > 关于善园基金会
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight">
            <div class="boederbox">
            	<h2>
                	<span class="lText">结项报告</span>
                </h2>
                <div class="adList" id="adList">
                	 <!--13条-->
                    <!--<p><a href="#" title=""><span class="listIcon"></span>【公告】关于长期无法联系的主题的处理公告（5）</a><span class="more">2014-12-31</span></p> -->
            	</div>
        	</div>
        <!--right end-->
    	</div>
	</div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="type" value="7">
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/noticeList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>