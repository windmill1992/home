<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/auction.css"/>
</head> 
<body>
<div class="verify">
    <!--头部-->
   <div class="verify_top">
       <a href="javascript:history.go(-1)">&lt;返回</a>
       <p>验证信息（不公开）</p>
   </div>
<!--content-->
    <div class="verify_center">
        <!--用户名-->
       <div class="verify_center1">
    <label><img src="<%=resource_url%>res/images/aution/spricpt1.png" class="rname">姓名</label>
        <div class="rltext">
            <div class="rlinput">

                <input type="text" class="rgray" dvalue="用户名/手机号码" value="" id="uName"/>
            </div>
        </div>
       </div>
        <div class="verify_ts"><span></span><P id ="uNameE"></P></div>
        <!--手机验证-->
        <div class="verify_center1">
        <label><img src="<%=resource_url%>res/images/aution/spricpt2.png" class="rname">手机号</label>
        <div class="rltext">
            <div class="rlinput">
                <input type="text" class="rgray1" dvalue="用户名/手机号码" value="" id="phone"/>
                <a href="javascript:;" class="rgray1_1" id="pSend">发送验证码</a>
            </div>
        </div>
        </div>
        <div class="verify_ts" ><span></span><P id ="phoneE"></P></div>
        <!--验证信息-->
        <div class="verify_center1" style="background: #f6f6f6">
            <label><img src="<%=resource_url%>res/images/aution/spricpt3.png" class="rname">验证码</label>
            <div class="rltext">
                <div class="rlinput">

                    <input type="text" class="rgray" dvalue="用户名/手机号码" value="" id="code"/>
                </div>
            </div>
            <div class="verify_ts"><span></span><P id ="codeE"></P></div>
    	</div>
    <!--尾部-->
    <div class="verify_footer">
        <a href="javascript:;" class="verify_footer1" id="submit"><p>确认提交</p></a>
    </div>
</div>
<input type="hidden" id="auctionId" value="${auctionId}">
<script data-main="<%=resource_url%>res/js/other/auction.js?v=5" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>