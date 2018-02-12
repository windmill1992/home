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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/help_detail.css"/>
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
                <span class="uCEN-tit">求助者详情</span>
            </div>
            <div class="help_box">
			    <div class="help_form">
			        <ul class="help_thedetail">
			            <li class="need"></li>
			            <li class="term">
			                <span class="term1" style="letter-spacing:8px;">姓&nbsp;名</span>
			                <span class="term2">:</span>
			            </li>
			            <li class="write">${cp.name }</li>
			        </ul>
			        <ul class="help_thedetail">
			            <li class="need"></li>
			            <li class="term">
			                <span class="term1">联系电话</span>
			                <span class="term2">:</span>
			            </li>
			            <li class="write">${cp.mobile }</li>
			        </ul>
			        <ul class="help_thedetail">
			            <li class="need"></li>
			            <li class="term">
			                <span class="term1" style="letter-spacing:8px;">单&nbsp;位</span>
			                <span class="term2">:</span>
			            </li>
			            <li class="write">${cp.workUnit }</li>
			        </ul>
			        <ul class="help_thedetail">
			            <li class="need"></li>
			            <li class="term">
			                <span class="term1">联系地址</span>
			                <span class="term2">:</span>
			            </li>
			            <li class="write">${cp.linkAddress }</li>
			        </ul>
			        <ul class="help_thedetail">
			            <li class="need"></li>
			            <li class="term">
			                <span class="term1" style="width:84px;">项目落实地址</span>
			                <span class="term2" style="margin-left:0px;">:</span>
			            </li>
			            <li class="write">${cp.actualAdress }</li>
			        </ul>
			        <ul class="help_thedetail1">
			            <li class="need"></li>
			            <li class="term">
			                <span class="term1" style="width:84px;letter-spacing:8px;">备&nbsp;注</span>
			                <span class="term2" style="margin-left:0px;">:</span>
			            </li>
			            <li class="write_2">${cp.remark }</li>
			            <div  style="clear:both;"></div>
			        </ul>
			       
			    </div>
			 </div>
            <a href="http://www.17xs.org/ucenter/ProjectCryPeopleView.do?projectId=${cp.projectId }" class="backBtn">点击返回</a>
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
