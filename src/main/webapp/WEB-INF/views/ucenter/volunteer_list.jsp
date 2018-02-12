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

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css" />
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
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
                <span class="uCEN-tit">志愿者名单</span>
                
            </div>
            <table width="100%" class="uCenterTable" id="listShow">
                <colgroup>
                    <col width="18%"></col>
                    <col width="16%"></col>
                    <col width="18%"></col>
                    <col width="20%"></col>
                    <col width="16%"></col>
                    <col width="10%"></col>
                </colgroup>
                <thead>
                    <tr>
                        <th>姓名（联系人）</th>
                        <th>类型</th>
                        <th>联系电话</th>
                        <th>参与项目</th>
                        <th>报名时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <!-- 数据 -->
                </tbody>
            </table>
            <!-- 分页调用原先的 start -->
            
            <!-- 分页 end -->
            <a href="http://www.17xs.org/ucenter/pindex.do" class="backBtn">点击返回</a>
        </div>
        <!--right end-->
    </div>
</div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="projectId" value="${projectId }">
<script data-main="<%=resource_url%>res/js/dev/projectVolunteer.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>