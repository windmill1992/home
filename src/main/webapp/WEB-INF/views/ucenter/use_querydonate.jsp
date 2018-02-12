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
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
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
			
<!-- =======================新版捐赠记录======================= -->
			
			<div class="uCEN-R donation_right">
           		
    			<div class="uCEN-hd uCEN-hd1">
                    <span class="uCEN-tit">获捐记录</span>
                    <dl class="select">
                        <dt class="sel-checked">
                            <input class="sel-val" v="0" type="text" value="全部" readonly>
                            <span class="triangle"><i class="triangle-up"></i></span>
                            <input id="sel-checked" type="hidden" value="0">
                        </dt>
                        <dd class="sel-options" style="display: none;">
                            <a class="sel-option">全部</a>
                            <a class="sel-option sel-check">三个月内</a>
                            <a class="sel-option">半年内</a>
                            <a class="sel-option">一年内</a>
                        </dd>
                    </dl>
                </div>
                
                <div class="donation_conn" id="listShow">
                    <dl>
                        <dt>
	                        <span class="naidt_01">主题</span>
	                        <span class="naidt_02">捐助金额</span>
	                        <span class="naidt_07">捐助人</span>
	                        <span class="naidt_05">捐助时间</span>
	                        <span class="naidt_06">捐助类型</span>
                        </dt>
                    </dl>
                    <div id="list-bd" class="list-bd">
                    	<div class="prompt"><div class="loading"></div></div>
                    </div>
                </div>
                
            </div>
          </div>
       </div>
   </div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/use_querydonation.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
