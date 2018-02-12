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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/volunteer_detail.css"/>
</head>
<body>
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->

<!--主体 start-->
<div class="bodyer userCenter">
    <div class="page">
        <div class="page-bd funlist_main">
            <div class="uCEN-L funlist_left">
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
            </div>
        </div>
        <!-- left end -->
        <!--right start-->
        <div class="uCEN-R invoice_right">
            <div class="uCEN-hd uCEN-hd1">
                <span class="uCEN-tit">志愿者详情</span>
            </div>
            <div class="volunteer_form changebox">
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">组织名称</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write">${pv.groupName }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">联系电话</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write">${pv.mobile }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">身份证号</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write">${pv.indentity }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">组织类型</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write_1">${pv.groupType }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">参加人数</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write">${pv.number }人</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1" style="width:70px;">联系人姓名</span>
		                <span class="term2" style="margin-left:13px;">:</span>
		            </li>
		            <li class="write">${pv.linkMan }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">电子邮件</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write">${pv.email }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1">联系地址</span>
		                <span class="term2">:</span>
		            </li>
		            <li class="write">${pv.address }</li>
		        </ul>
		        <ul class="volunteer_thedetail">
		            <li class="need"></li>
		            <li class="term">
		                <span class="term1" style="width:70px;">服务时间段</span>
		                <span class="term2" style="margin-left:13px;">:</span>
		            </li>
		            <li class="write">${pv.serviceTime }</li>
		        </ul>
		    </div>
            <a href="http://www.17xs.org/ucenter/ProjectVolunteerView.do?projectId=${pv.projectId }" class="backBtn">点击返回</a>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/projectCryPeople.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
