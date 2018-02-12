<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="viewport"/>
<title>善园 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/charge.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/withdraw.css"/>
<style>
.page .page-bd{padding:0 0 15px 0; line-height:22px; font-size:14px;}
strong{ font-weight:bold}
</style>
</head> 
<body>
<%@ include file="./../../common/head.jsp" %>
<div class="bodyer">
<div class="bodyer userCenter withdraw"> 
	<div class="page"> 
                 <%@ include file="./../../common/model1.jsp"%>
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../../common/eleft_menu.jsp" %>
            </div>
            <div class="uCEN-R "> 
                <div class="charge-menu">
                    <ul>
                        <li class="active">
                            <a href="<%=resource_url%>ucenter/core/viewWithdrawDeposit.do">我要提现</a>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/WithdrawDepositData.do">提现记录</a>
                        </li>
                        <li>
                            <a href="<%=resource_url%>ucenter/core/WithdrawDepositManage.do">银行卡管理</a>
                        </li>
                    </ul>
                </div>
                <div class="charge-wrap">
                    <div class="charge-quick">
                        
                        <div class="quick-wrap">
                            <div class="quick-remind">
                                <p><i class="icon-charge i_1"></i>首次提现，请先添加提现银行卡，去【银行卡管理】</p>
                            </div>
                            <div class="cont">
                                <div class="quick-form">
                                    <dl>
                                        <dt>银行卡号：</dt>
                                        <dd  class="select-bank">
                                            <div class="select-bank-item">
                                                <div class="select-placeholder" data-id="${selectcard.id}">
                                                 <label>
                                                  	<c:if test="${selectcard ==null}">
                                                    	<span>- 请选择银行 -</span>
                                                    </c:if>
                                                    <c:if test="${selectcard !=null}">
				                                       <img src="<%=resource_url%>res/images/charge/bank-${selectcard.bankName}.jpg">
                                                    <span>${selectcard.card}</span>             
                                                    </c:if>
                                                    </label>
                                                   
                                                    <i class="icon-charge i_2"></i>
                                                </div>
                                            </div>
                                            <div class="bank-list">
                                            <c:forEach var="card" items="${cardList}">
                                            <label>
                                                    <input type="hidden" name="bank" id="${card.id}">
                                                    <img src="<%=resource_url%>res/images/charge/bank-${card.bankName}.jpg">
                                                    <span>${card.card}</span>
                                                    </label>
                                            </c:forEach>
                                                </div>
                                                <a class="bank-add" href="<%=resource_url%>ucenter/core/WithdrawDepositManage.do""> </a>
                                           
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>预计到账时间：</dt>
                                        <dd>
                                         	<p class="text"> --小时内到账</p>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>提现金额：</dt>
                                        <dd>
                                            <input type="text" class="inp inp02" id="money"><p class="text"> 元</p>
                                            </p>
                                        </dd>
                                    </dl>
                                    <dl class="dmoneyPrim">
                                        <dt></dt>
                                        <dd>
                                            <p class="text"> 可提现 <span class="red" id="balance"></span> 元</p>
                                        </dd>
                                    </dl>
                                </div>
                                <div class="quick-form-btn">
                                   <!-- <a class="charge-btn charge-btn-gray">提交</a>-->
                                    <a class="charge-btn charge-btn-green" id="withdraw-submit">提交</a>
                                 
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
<input type ="hidden" id="userbalance" value="${user.balance}"/>
<%@ include file="./../../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/withdraw/withdraw_quick.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
