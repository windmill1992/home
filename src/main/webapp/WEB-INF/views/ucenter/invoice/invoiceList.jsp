<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网 - 开票记录</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css" />
</head>
<body>
<!--头部 start-->
<%@ include file="./../../common/newhead.jsp" %>
<!--头部 end-->

<!--主体 start-->
<div class="bodyer userCenter">
    <div class="page">
        <div class="page-bd funlist_main">
            <div class="uCEN-L funlist_left">
                <div class="uCEN-L">
					<c:choose>
					<c:when test="${user.userType=='enterpriseUsers'}">
		         		<%@ include file="./../../common/eleft_menu.jsp"%>
		         	</c:when>
		         	<c:otherwise>
		         		<%@ include file="./../../common/pleft_menu.jsp"%>
		         	</c:otherwise>
		         	</c:choose>
				</div>
            </div>
        </div>
        <!-- left end -->
        <!--right start-->
        <div class="uCEN-R invoice_right">
            <div class="uCEN-hd uCEN-hd1">
                <span class="uCEN-tit">开票记录</span>
                <div class="bindBox">
                    <ul class="bindList">
                        <li><a class="mobile"></a></li>
                        <li><a class="email"></a></li>
                        <li><a class="qq"></a></li>
                        <li><a class="wx"></a></li>
                        <li><a class="wb"></a></li>
                        <li><a class="zfb"></a></li>
                    </ul>
                    <span>(绑定账号更方便了解捐助信息)</span>
                </div>
                <a href="<%=resource_url%>ucenter/getInvoicePage.do" class="invoiceBtn">索取发票</a>
            </div>
            <table width="100%" class="uCenterTable" id="listShow">
                <colgroup>
                    <col width="18%"></col>
                    <col width="12%"></col>
                    <col width="10%"></col>
                    <col width="15%"></col>
                    <col width="18%"></col>
                    <col width="14%"></col>
                    <col width="13%"></col>
                </colgroup>
                <thead>
                    <tr>
                        <th>发票抬头</th>
                        <th>发票金额</th>
                        <th>收件人</th>
                        <th>电话</th>
                        <th>收件地址</th>
                        <th>索票时间</th>
                        <th>状态</th>
                    </tr>
                </thead>
                <tbody>
                    
                </tbody>
            </table>
            <!-- 分页调用原先的 start -->
            <div class="panigation" style="height: 75px;"></div>
            <!-- 分页 end -->
            <c:choose>
			<c:when test="${user.userType=='enterpriseUsers'}">
         		<a href="<%=resource_url%>ucenter/core/enterpriseCenter.do" class="backBtn">返回首页</a>
         	</c:when>
         	<c:otherwise>
         		<a href="<%=resource_url%>ucenter/core/personalCenter.do" class="backBtn">返回首页</a>
         	</c:otherwise>
         	</c:choose>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/invoiceList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
