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
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeAlipay.do">支付宝</a>
                            <em>0手续费</em>
                        </li>
                        <li  class="active">
                            <a href="<%=resource_url%>ucenter/core/chargeTransfer.do">银行汇款</a>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/chargeRecord.do">充值记录</a>
                        </li>
                    </ul>
                </div>
                <div class="charge-wrap">
                    <div class="charge-transfer">
                        <div class="transfer-top">
                            <h6>大额充值，可通过网上银行或银行柜台向善园基金会转账</h6>
                            <p>转账时，请务必把您的手机号填写在转账备注或用途里，以便工作人员查实。</p>
                        </div>
                        <div class="transfer-list">
                            <dl>
                                <dt>
                                    <img src="<%=resource_url%>res/images/charge/bank-logo.jpg" />
                                </dt>
                                <dd>
                                    <p>
                                        <span>开户名：宁波市善园公益基金会</span>
                                        <span>开户账号：3901150019000368944</span>
                                        <span>开户行：中国工商银行鄞州支行营业部</span>
                                    </p>
                                </dd>
                            </dl>
                            <%-- <dl>
                                <dt>
                                    <img src="<%=resource_url%>res/images/charge/alipay-logo.jpg">
                                </dt>
                                <dd>
                                    <p>
                                        <span>开户名：宁波市善园公益基金会</span>
                                        <span>开户账号：zfb@17xs.org</span>
                                    </p>
                                </dd>
                            </dl> --%>
                        </div>
                        <div class="warm-tips">
                            <h6><font>*</font>温馨提示</h6>
                            <p>转账完成后，请务必联系在线客服（9:00-17:00 节假日除外）</p>
                            <p>告知您的帐号名，及汇款凭证截图，以便我们及时充值到您的账号内。</p>
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
