
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,minimal-ui" />
<title>确认订单</title>
<link rel="stylesheet" type="text/css" href="/res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/wenxin_3.css?v=20160329" />
</head>
<body>
	<div id="pageContainer">
		<div class="project">
			<!-----微信支付-//--->
			<div class="pay" style="top:0;left:0;">
				<div class="t">
					<div class="close">
						<img src="/res/images/h5/images/icon_close.png">
					</div>
					<span>确认订单</span>
				</div>
				<div class="pay_use">
			   		<div class="recieve"><i>善款接收</i><img src="/res/images/h5/images/arrow.jpg" width="6" height="10">
			   		<b>宁波市善园公益基金会（公募资质）</b></div>
			       	<div class="uses"><i>提醒</i><img src="/res/images/h5/images/arrow.jpg" width="6" height="10">
			       	<b>行善“红包”发出后，被领取的“红包”只能用于行善，不可提现或购物；行善“红包”只限于善园网使用；“红包”未领取或者领取后未使用，7天后将退回您的善园网余额，可再次用于行善！</b></div>
			   	</div>
				<div class="box">
					<div class="pay_ren">
						<p>发起人:</p>
					</div>
					<div class="pay_nc">
						<p>昵称：<span id="touristName">${user.nickName}</span></p>
					</div>
					<div class="pay_nc">
						<p>宣言：<span id="touristMobile">${slogans}</span></p>
					</div>
					<div class="pay_ren">
						<p>红包信息:</p>
					</div>
					<div class="pay_nc">
						<p>随机红包：<span>${pcount}</span>个</p>
					</div>
					<div class="pay_nc">
						<p>红包金额：<span>${amount}</span>元</p>
					</div>
					<div class="pay_tip" style="font-weight:bold;">请选择支付方式：</div>
						<c:choose>
					        <c:when test="${payway == 'wx'}">
							   <a href="javascript:void(0)" class="btn_a" onclick="pay()"><div class="wx_btn">
								<img src="/res/images/h5/images/wx-icon.jpg" width="40" height="32"><span>微信支付</span>
							</div></a> 
							</c:when>
							<c:otherwise >
							  <a href="javascript:void(0)" class="btn_a" onclick="alipay()"><div class="alipay_btn">
								<img src="/res/images/h5/images/alipay-icon.jpg" width="87" height="28">
							<span></span></div></a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="${tradeNo}" id="tradeNo" />
	<input type="hidden" value="${pName}" id="pName" />
	<input type="hidden" value="${amount}" id="amount" />
	<input type="hidden" value ="${copies}" id = "copies">
	<input type="hidden" value ="${user.id}" id = "userId">
	<input type="hidden" value ="${user.nickName}" id = "nickName">
	<input type="hidden" value ="${user.coverImageUrl}" id = "headImage">
	<input type="hidden" value ="${plistId}" id = "plistId">
	<input type="hidden" value ="${redpacketsId}" id = "redpacketsId">
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		$('#num1').click(function(){
			$('#num1').hide();
			$('#num2').show();
		});
		$('#num2').click(function(){
			$('#num1').show();
			$('#num2').hide();
		});

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
					location.href = "http://www.17xs.org/redPackets/getPersonRedPaket.do?id="+$('#redpacketsId').val()+"&type=110";
				}
			});
		}
		function alipay(){
			var touristName = $('#touristName').html(),
				touristMobile = $('#touristMobile').html(),
				plistId = $('#plistId').val(),
				projectId = $('#projectId').val(),
				amount = $('#amount').val(),
				copies = $("#copies").val(),
				urls = "http://www.17xs.org/rechargeWapAlipay/deposit.do?amount="+ amount +"&t="+ new Date().getTime();
			location.href = urls;
		}
		$('#closeDetail').click(function() {
			$('#payTips').hide();
		});
	</script>
</body>
</html>