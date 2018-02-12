
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<html>
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,minimal-ui" />
<title>确认订单</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/detail.css"/>
</head>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" /></script>

<body>
<div class="project">
	<!-----微信支付-//--->
	<div class="pay">
		<div class="t">
			<div class="close" id="closeDetail">
				<img src="<%=resource_url%>res/images/h5/images/icon_close.png">
			</div>
			<span>请选择支付方式</span>
		</div>
		<div class="box">
			<div class="pay_tip">
				捐助金额<span>${amount}</span>元
			</div>
			<div class="pay_subject">捐助主题：${pName}</div>
			<a href="javascript:void(0)" class="btn_a" onclick="pay()">
				<div class="wx_btn">
					<img src="<%=resource_url%>res/images/h5/images/wx-icon.jpg" width="40" height="32"><span>微信支付</span>
				</div>
			</a>
		</div>
	</div>
	</div>
	<input type="hidden" value="${tradeNo}" id="tradeNo" />
	<input type="hidden" value="${pName}" id="pName" />
	<input type="hidden" value="${amount}" id="amount" />
	<input type="hidden" value="${projectId}" id="projectId" />
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		wx.config({
			debug : false,
			appId : '${appId}',
			timestamp : '${config_timestamp}',
			nonceStr : '${config_noncestr}',
			signature : '${signature}',
			jsApiList : [ 'chooseWXPay' ]
		});
		function pay() {
			wx.chooseWXPay({
				timestamp : '${timestamp}',
				nonceStr : '${noncestr}',
				package : '${packageValue}',
				signType : 'MD5',
				paySign : '${paySign}',
				success : function(res) {
					// 					alert("支付成功");
					var url = "http://www.17xs.org/tenpay/h5result.do?OrderId="
							+ $('#tradeNo').val() + "&projectId="
							+ $('#projectId').val() + "&t="
							+ new Date().getTime();
					window.location.href = url;
				}
			});
		}
		$('#closeDetail').click(function(){
				$('#payTips').hide();
			});
	</script>
</body>
</html>