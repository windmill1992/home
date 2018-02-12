<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport" />
<title>善园网 - 我要行善支付</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entPay.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/charge.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entAlipay.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<input type="hidden" value="${error}" name="msg" id="msg">
<div class="bodyer wxAlipay">
	<div class="page">
    	<!-- <div class="page-hd">
           <div class="page-tit"><h2 class="w1000">我要行善</h2></div>
        </div> -->
        <div class="page-bd"> 
        	<div class="wx_alipay">
            	<div class="wx-l">
                	<h2>请使用微信扫描二维码进行支付</h2>
                    <img src="<%=resource_url%>/tenpay/code.do?codeurl=${codeurl}" alt="">
                </div>
                <div class="wx-r">
                	<p class="txt"><span class="label">总计金额：</span><span class="money">￥${amount}</span></p>
                    <p><span class="label">项目主题：</span>${title}</p>
                    <p><span class="label">订单号：</span>${tradeNo}</p>
                    <p><span class="label">下单日期：</span>${date}</p>
                    
                </div>
            </div>
        </div>

    </div>
</div>
<input type="hidden" value="${tradeNo}" id="tradeNo" />
<input type="hidden" value="${payType}" id="payType" />
<input type="hidden" value="${projectId}" id="projectId" />
<input type="hidden" value="${nickName}" id="nickName" />
<input type="hidden" value="${amount}" id="amount" />
<input type="hidden" value="${title}" id="title" />
<input type="hidden" value="${date}" id="date" />
<input type="hidden" value="${list}" id="list" />
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/h5/releaseProject/exportWeixinPay.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
