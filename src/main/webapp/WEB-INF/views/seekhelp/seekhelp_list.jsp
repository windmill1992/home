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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/helpMe.css"/>
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
            <div class="uCEN-R">
           		<!----我的求助-///--->
            	<div class="help_list spage_list">
                	<div class="list_hd">
                	<span class="title">
                	<c:choose>
                		<c:when test="${flag == 'withdraw' }">我要提现</c:when>
                		<c:when test="${flag == 'realtionProject' }">相关项目</c:when>
                		<c:otherwise>我的求助</c:otherwise>
                	</c:choose>
                	</span>

					<div class="form">
						<input type="text" id="projectTitle" placeholder="请输入项目名称/受助人姓名..."/>
						<button id = "search">搜索</button>
					</div>
					<a href="<%=resource_url%>project/appealFirest.do" class="help_btn">我要筹款</a></div>
                    <dl class="list_bd" id="listShow">
                    	<dt><ul>
			<li class="col1">项目标题</li>
			<li class="col2"><p class="zt">状态</p><span class="ztIcon"></span>
			<ul class="col2_ul" style="display:none">
					<a href="javaScript:void(0);"><li class="col2_li state1">全部</li></a>
					<a href="javaScript:void(0);"><li class="col2_li state2">草稿</li></a>
					<a href="javaScript:void(0);"><li class="col2_li state3">审核中</li></a>
					<a href="javaScript:void(0);"><li class="col2_li state4">待发布</li></a>
					<a href="javaScript:void(0);"><li class="col2_li state5">审核未通过</li></a>
					<a href="javaScript:void(0);"><li class="col2_li state6">募捐中</li></a>
					<a href="javaScript:void(0);"><li class="col2_li state7">结束</li></a>
				</ul>
			</li>
			<li class="col3">筹款金额</li>
			<li class="col4">已筹集</li>
			<li class="col5">可提现</li>
			<li class="col6">操作</li>
			</ul></dt>
                    </dl>	
            	</div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="flag" value="${flag}" />
<input type="hidden" id="state" value="-1" />
	<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script data-main="<%=resource_url%>res/js/dev/needhelplist.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>