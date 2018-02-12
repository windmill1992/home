<!DOCTYPE html>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp"%>
<div class="bodyer userCenter goodhelpList"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            <c:choose>
			<c:when test="${user.userType=='enterpriseUsers'}">
         		<%@ include file="./../common/eleft_menu.jsp"%>
         	</c:when>
         	<c:otherwise>
         		<%@ include file="./../common/pleft_menu.jsp"%>
         	</c:otherwise>
         	</c:choose>
            </div>
            <div class="uCEN-R"> 
                <div class="uCEN-list list">
                	<div class="uCEN-hd">
                    	<span class="uCEN-tit">助善项目</span> 
                        <a class="more" title="" id="entExplain">我要助善</a>  
                    </div>
                    <div class="uCEN-bd">
                        <div class="list wlist-data"> 
                        	 <ul class="list-hd">
                             	<li class="lst-col1">项目名称</li>
                                <li class="lst-col2">助捐金额/人</li>
                                <li class="lst-col3">已号召人数</li>
                                <li class="lst-col4">助善进度（已助善/总预算）</li>
                                <li class="lst-col5">发起时间</li>
                                <li class="lst-col6">操作</li>
                             </ul>
                             <div class="list-bd" id="ghlist">
                             </div>
                           <p class="pageNum" id="pageNum"><p>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/goodhelpList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>