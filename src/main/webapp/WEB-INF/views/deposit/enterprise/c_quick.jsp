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
                        <li class="active">
                            <a href="<%=resource_url%>ucenter/core/chargeQuick.do">快捷支付</a>
                        </li>
                        <li >
                            <a href="<%=resource_url%>ucenter/core/chargeonlineBank.do">网银支付</a>
                            <em>0手续费</em>
                        </li>
                        <li>
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
                    <div class="charge-quick">
                        <div class="explain">
                            <img src="<%=resource_url%>res/images/charge/quick.jpg"/>
                            <p>无需开通网银，有银行卡就能支付（单卡单笔最高50万，单日最高100万）</p>
                        </div>
                        <div class="quick-wrap">
                            <div class="quick-remind">
                                <p><i class="icon-charge i_1"></i>为了保障您的快捷支付安全，请先完善和认证个人信息。 <a>【立即认证】</a></p>
                            </div>
                            <div class="cont">
                                <div class="quick-form">
                                    <dl>
                                        <dt>持卡人：</dt>
                                        <dd>
                                            <p class="text">${user.realName}</p>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>身份证号：</dt>
                                        <dd>
                                            <p class="text">${user.idCard}</p>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>银行卡号：</dt>
                                        <dd>
                                            <input type="text" class="inp inp01"/>
                                            <div class="bank-icon">
                                                <img src="<%=resource_url%>res/images/charge/bank-01.jpg"/>
                                            </div>
                                            <p class="weak-remind">单笔限额50万，单日限额100万</p>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>充值金额：</dt>
                                        <dd>
                                            <input type="text" class="inp inp02"/>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>实际充值金额：</dt>
                                        <dd>
                                            <p class="text price">
                                                <span>0</span>元(充值手续费 0 元)
                                            </p>
                                        </dd>
                                    </dl>
                                </div>
                                <div class="quick-form-btn">
                                <c:if test="${user.realState == 203}">
                                	<a class="charge-btn charge-btn-green">立即支付</a>
                                </c:if>
                                <c:if test="${user.realState != 203}">
                                    <a class="charge-btn charge-btn-gray">立即支付</a>
                                </c:if>
                                    
                                </div>
                            </div>
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
