<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,minimal-ui" />
	<title>确认订单</title>
	<link rel="stylesheet" type="text/css" href="/res/css/dev/base.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/dialog.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/head.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/form.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/wenxin_2.css?v=20180124" /> 
</head>
<body>
	<div id="pageContainer">
		<div class="project">
			<!-----微信支付-//--->
			<div class="pay" style="top:0;left:0;">
				<div class="t">
					<div class="close">
						<a href="javascript:;" onclick="history.go(-1);"><img src="/res/images/h5/images/icon_close.png"></a>
					</div> 
					<span>确认订单</span> 
				</div>
				<div class="pay_use">
					<div class="recieve"><i>善款接收</i><img src="/res/images/h5/images/arrow.jpg" width="6" height="10"> <b>宁波市善园公益基金会</b></div>
					<div class="uses"><i>善款处理</i><img src="/res/images/h5/images/arrow.jpg" width="6" height="10"> <b>善款将根据求助项目实际发生的费用拨付给相关单位或求助者；实施救助后如善款尚有剩余，剩余部分并入基金会善库，用于其他求助者的求援；善款使用全程公示。</b></div>
				</div>
				<div class="box">
					<div class="pay_subject">认捐主题：${pName}</div>
					<div class="pay_tip"> 捐助金额：<span>${amount}</span>元 </div>
					<div class="pay_anonymous flex"> 
						<label>是否匿名:</label> 
						<input class="tgl tgl-ios" id="cb2" type="checkbox" /> 
						<label class="tgl-btn" for="cb2"></label> 
					</div>
					<div class="pay_tip" style="font-weight:bold;">请选择支付方式：</div>
					<c:choose>
						<c:when test="${payway == 'wx'}">
							<a href="javascript:void(0);" class="btn_a" onclick="pay()">
								<div class="wx_btn"> <img src="/res/images/h5/images/wx-icon.jpg" width="40" height="32"><span>微信支付</span> </div>
							</a>
						</c:when>
						<c:otherwise>
							<div class="btn_a" onclick="alipay()">
								<div class="alipay_btn"> 
									<img src="/res/images/h5/images/alipay-icon.jpg" width="87" height="28" style="margin-top: 2px;">
									<span></span> 
								</div>
							</div>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div> 
	
	<input type="hidden" value="${realName }" id="realName" /> 
	<input type="hidden" value="${mobileNum }" id="mobileNum" /> 
	<input type="hidden" value="${tradeNo}" id="tradeNo" /> 
	<input type="hidden" value="${pName}" id="pName" /> 
	<input type="hidden" value="${amount}" id="amount" /> 
	<input type="hidden" value="${projectId}" id="projectId" /> 
	<input type="hidden" value="${copies}" id="copies" /> 
	<input type="hidden" value="${extensionPeople}" id="extensionPeople" /> 
	<input type="hidden" value="${user.id}" id="userId" /> 
	<input type="hidden" value="${user.nickName}" id="nickName" /> 
	<input type="hidden" value="${user.coverImageUrl}" id="headImage" /> 
	<input type="hidden" value="${crowdFunding}" id="crowdFunding" /> 
	<input type="hidden" value="${donateWord}" id="donateWord" />
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		var isHideName = false,
			nameOpen = 0;
		wx.config({
			debug: false,
			appId: '${appId}',
			timestamp: '${config_timestamp}',
			nonceStr: '${config_noncestr}',
			signature: '${signature}',
			jsApiList: ['chooseWXPay']
		});

		function pay() {
			if(isHideName) {
				nameOpen = 1;
			}
			wx.chooseWXPay({
				timestamp: '${timestamp}',
				nonceStr: '${noncestr}',
				package: '${packageValue}',
				signType: 'MD5',
				paySign: '${paySign}',
				success: function(res) {
					var yunhu_type = '${slogans}';
					var url = '';
					if(yunhu_type == 'yunhu') {
						url = "http://www.17xs.org/export/donate_detail_view.do?extensionPeople=4557&projectId=" + $('#projectId').val();
					} else {
						url = "http://www.17xs.org/project/view_paysuccess_h5.do" + "?pName=" + $('#pName').val() + "&amount=" + $('#amount').val() + "&extensionPeople=" + $('#extensionPeople').val() + "&tradeNo=" + $('#tradeNo').val() + "&userId=" + $('#userId').val() + "&nickName=" + $('#nickName').val() + "&headImage=" + $('#headImage').val() + "&projectId=" + $('#projectId').val() + "&realName=" + $('#realName').val() + "&mobileNum=" + $('#mobileNum').val() + "&donateWord=" + $('#donateWord').val() + "&crowdFunding=" + $("#crowdFunding").val() + "&slogans=${slogans}" + "&nameOpen=" + nameOpen;
					}
					location.href = url;
				}
			});
		}

		function alipay() {
			if(isHideName) {
				nameOpen = 1;
			}
			var url = "http://www.17xs.org/WapAlipay/deposit.do?projectId=" + $('#projectId').val() + "&amount=" + $('#amount').val() + "&extensionPeople=" + $('#extensionPeople').val() + "&payType=5&copies=" + $("#copies").val() + "&donateWord=" + $("#donateWord").val() + "&realName=" + $('#realName').val() + "&mobileNum=" + $('#mobileNum').val() + "&crowdFunding=" + $("#crowdFunding").val() + "&tradeNo=" + $('#tradeNo').val() + "&slogans=${slogans}" + "&nameOpen=" + nameOpen;
			location.href = url;
		}
		$(function() {
			$('body').on('change', '#cb2', function() { //匿名
				console.log(this.checked);
				if(this.checked) {
					isHideName = true;
				} else {
					isHideName = false;
				}
			}).on('click', '#closeDetail', function() {
				$('#payTips').hide();
			});
		});
	</script>
</body>
</html>