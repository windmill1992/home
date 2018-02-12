<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="viewport" />
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/charge.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<style>

.page .page-bd {
	padding: 0 0 15px 0;
	line-height: 22px;
	font-size: 14px;
}

strong {
	font-weight: bold
}
</style>
</head>
<body>
	<%@ include file="./../../common/newhead.jsp"%>
	<div class="bodyer">
	<div class="bodyer userCenter goodhelpList"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../../common/eleft_menu.jsp" %>
            </div>
            <div class="uCEN-R"> 
                <div class="charge-menu">
                   <ul>
                        <li >
                            <a href="<%=resource_url%>ucenter/core/chargeonlineBank.do">网银支付</a>
                            <em>0手续费</em>
                        </li>
                        <li class="active">
                            <a href="<%=resource_url%>ucenter/core/chargeAlipay.do">支付宝</a>
                            <em>0手续费</em>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeTransfer.do">银行汇款</a>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeRecord.do">充值记录</a>
                        </li>
                    </ul>
                </div>
                <div class="charge-wrap">
                    <div class="charge-alipay">
                        <div class="alipay-cont alipay-money">
                            <span>支付金额：</span>
                            <input type="text" class="inp inp02" id="amount" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)"/>
                            <span>元</span>
                            <em>（0手续费）</em>
                        </div>
                        <div class="alipay-logo">
                            <img src="<%=resource_url%>res/images/charge/alipay-logo.jpg"/>
                        </div>
                        <div class="alipay-cont alipay-payMoney">
                            <div class="payMoney-type">
                                <span><em></em>扫码付款方式</span>
                                <span><em></em>电脑支付方式</span>
                            </div>
                            <div class="payMoney-cont">
                                <div class="codeComputer"><img src="<%=resource_url%>res/images/charge/pay-code.jpg"/></div>
                                <div class="codeComputer"><img src="<%=resource_url%>res/images/charge/pay-computer.jpg"/></div>
                            </div>
                        </div>
                        <div class="payMoney-btn">
                            <a class="charge-btn charge-btn-green" id="submitPay">立即支付</a>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div> 
</div>
	</div>
	<input type="hidden" value="${page}" id="page"/>
	<%@ include file="./../../common/newfooter.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/charge/charge_onlineBank.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
