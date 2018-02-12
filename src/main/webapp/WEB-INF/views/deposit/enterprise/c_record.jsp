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
                        <li >
                            <a href="<%=resource_url%>ucenter/core/chargeAlipay.do">支付宝</a>
                            <em>0手续费</em>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeTransfer.do">银行汇款</a>
                        </li>
                        <li class="active">
                            <a href="<%=resource_url%>ucenter/core/chargeRecord.do">充值记录</a>
                        </li>
                    </ul>
                </div>
                <div class="charge-wrap">
                    <div class="charge-record">
                        <div class="record-top">
                            <span class="total">合计：<b id="tmoney"></b>元</span>
                            <dl class="select">
                                <dt class="sel-checked">
                                    <input class="sel-val" v="0" type="text" value="三个月内" readonly="">
                                    <span class="triangle"><i class="triangle-up"></i></span>
                                    <input id="sel-checked" type="hidden" value="0">
                                </dt>
                                <dd class="sel-options" style="display: none;">
                                    <a class="sel-option sel-check">三个月内</a>
                                    <a class="sel-option">半年内</a>
                                    <a class="sel-option">一年内</a>
                                    <a class="sel-option">全部</a>
                                </dd>
                            </dl>
                        </div>
                        <div class="record-list">
                            <ul class="color-bg ul1">
                                <li class="li1">时间</li>
                                <li class="li2">充值方式</li>
                                <li class="li3">金额（元）</li>
                                <li class="li4">流水号</li>
                                <li class="li5">状态</li>
                            </ul>
                            <div class="list-bd" id="czlist">
                             </div>
                              <p class="pageNum" id='pageNum'></p>
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
	<script data-main="<%=resource_url%>res/js/dev/charge/charge_record.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
