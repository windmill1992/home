<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="viewport" />
<title>善园 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/charge.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/withdraw.css" />
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
	<%@ include file="./../../common/head.jsp"%>
	<div class="bodyer">
	<div class="bodyer userCenter withdraw"> 
	<div class="page"> 
                 <%@ include file="./../../common/model1.jsp"%>
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../../common/eleft_menu.jsp" %>
            </div>
            <div class="uCEN-R"> 
                <div class="charge-menu">
                    <ul>
                        <li><a
									href="<%=resource_url%>ucenter/core/viewWithdrawDeposit.do">我要提现</a>
								</li>
								<li ><a
									href="<%=resource_url%>ucenter/core/WithdrawDepositData.do">提现记录</a>
								</li>
								<li class="active"><a href="<%=resource_url%>ucenter/core/WithdrawDepositManage.do">银行卡管理</a></li>
                        
                    </ul>
                </div>
                <div class="charge-wrap">
                    <div class="charge-quick">
                        <div class="quick-wrap withdraw-wrap">
                        	<div class="manage-list" id="manage-list">
                            </div>
                        	<div class="manage-title">添加银行卡</div>
                            <div class="cont">
                                <div class="quick-form">
                                    <dl>
                                        <dt>开户名：</dt>
                                        <dd>
                                            <p class="text islegalize">${realname}<em id="managePrim"><span><i></i>
                                            您的账户已实名认证，提现的开户名必须为”${realname}”，且不可修改。</span></em></p>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>开户行：</dt>
                                        <dd>
                                           	<div class="select-bank">
                                                <div class="select-bank-item">
                                                    <div class="select-placeholder" data-id="2">
                                                    	<label>
                                                        	<span>- 请选择银行 -</span>
                                                            <%-- <img src="<%=resource_url%>res/images/charge/bank-03.png">
                                                            <span>（到账快）</span> --%>
                                                        </label>
                                                        
                                                        <i class="icon-charge i_2"></i>
                                                    </div>
                                                    
                                                </div>
                                                <div class="bank-list">
                                                	<label>
                                                    <input type="hidden" name="bank" id="中国工商银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-01.png">
                                                    <span>（到账快）</span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="建设银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-02.png">
                                                    <span>（到账快）</span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="农行银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-03.png">
                                                    <span>（到账快）</span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="招商银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-04.png">
                                                    <span>（到账快）</span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="中国银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-05.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="邮政银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-06.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="交通银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-07.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="广发银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-08.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="广大银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-09.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="兴业银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-10.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="平安银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-11.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="中兴银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-12.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="浦发银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-13.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="民生银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-14.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="北京银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-15.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="上海银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-16.png">
                                                    <span></span>
                                                    </label>
                                                    <label>
                                                    <input type="hidden" name="bank" id="杭州银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-17.png">
                                                    <span></span>
                                                    </label>
                                                     <label>
                                                    <input type="hidden" name="bank" id="宁波银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-18.png">
                                                    <span></span>
                                                    </label>
                                                     <label>
                                                    <input type="hidden" name="bank" id="富镇银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-19.png">
                                                    <span></span>
                                                    </label>
                                                     <label>
                                                    <input type="hidden" name="bank" id="北京农业银行">
                                                    <img src="<%=resource_url%>res/images/charge/bank-20.png">
                                                    <span></span>
                                                    </label>
                                                </div>
                               			   </div>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>开户行所在地：</dt>
                                        <dd>
                                            
                                            <div class="bank-adress">
                                                <span><select id="province"></select>省</span>
                                                <span><select id="city"></select>市</span>
                                                <span><select id="county"></select>区</span>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>储蓄卡号：</dt>
                                        <dd>
                                            <input type="text" class="inp inp01" id="cardNo"/>
                                            <p class="weak-remind">此银行卡开户名必须为“浙江宾果信息科技有限公司”,否则提现会失败</p>
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>再次输入银行卡号：</dt>
                                        <dd>
                                           <input type="text" class="inp inp01" id="cardNoC"/>
                                           <input type="hidden" id ="cardType">
                                        </dd>
                                    </dl>
                                </div>
                                <div class="quick-form-btn">
                                    <a class="charge-btn charge-btn-green" id="add-bank">添加</a>
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
	<%@ include file="./../../common/footer.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/withdraw/withdraw_manage.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
