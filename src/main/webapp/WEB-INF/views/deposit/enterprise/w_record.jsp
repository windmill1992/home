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
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/charge.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/withdraw.css" />
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
								<li class="active"><a
									href="<%=resource_url%>ucenter/core/WithdrawDepositData.do">提现记录</a>
								</li>
								<li><a href="<%=resource_url%>ucenter/core/WithdrawDepositManage.do">银行卡管理</a></li>
							</ul>
						</div>
						<div class="charge-wrap">
							<div class="charge-record">
								<div class="record-top">
									<span class="total"></span>
									<dl class="select">
										<dt class="sel-checked">
											<input class="sel-val" v="0" type="text" value="三个月内"
												readonly> <span class="triangle"><i
												class="triangle-up"></i></span> <input id="sel-checked"
												type="hidden" value="0">
										</dt>
										<dd class="sel-options" style="display: none;">
											<a class="sel-option sel-check">三个月内</a> <a
												class="sel-option">半年内</a> <a class="sel-option">一年内</a> <a
												class="sel-option">全部</a>
										</dd>
									</dl>
								</div>
								<div class="record-list" id="record-list">
								</div>
								 <p class="pageNum" id='pageNum'></p>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="${page}" id="page"/>
	<%@ include file="./../../common/footer.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/withdraw/withdraw_record.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
