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
<title>善园网 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css" />
</head> 
<body>
<!-- 头部start -->
<%@ include file="./../common/newhead.jsp" %>
<!-- 头部end -->

<!--主体 start-->
<div class="bodyer userCenter">
    <div class="page">
        <div class="successBox">
            <div class="top">
                <!-- <img src="../res/images/okicon.png"> -->
                捐助成功，感谢您的爱心善举
            </div>
            <div class="main">
                <p>捐助项目：<span>${pName}</span></p>
                <p>支付单号：<span>${tradeNo}</span></p>
                <p>捐助金额：<span>${amount}元</span></p>
                <p>捐助时间：<span>${time}</span></p>
                <p>捐助人名：<span>${nickName}</span><a href="javascript:void(0)" class="changeNickName">点此修改</a></p>
            </div>
            <div class="btnGroup">
                <a href="http://www.17xs.org/project/index/">继续捐</a>
                <a href="http://www.17xs.org/ucenter/core/mygood.do">查看捐赠记录</a>
                 <a href="http://www.17xs.org/ucenter/getInvoicePage.do">开具捐赠发票</a>
            </div>
        </div>
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/donationSuccess.js?v=20160114" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
