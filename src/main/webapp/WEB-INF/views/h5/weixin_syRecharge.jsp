
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<html>
<head>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,minimal-ui" />
<!--<meta name="viewport" content="width=device-width; initial-scale=1.0">-->
<!--<meta name="viewport" content="height=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=0">-->
<title>确认订单</title>
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/h5/wenxin_3.css?v=20160329" />
</head>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>

<body>
	<div id="pageContainer">
		<div class="project">
				<!-----微信支付-//--->
				<div class="pay" style="top:0;left:0;">
					<div class="t">
						<div class="close">
							<img src="<%=resource_url%>res/images/h5/images/icon_close.png">
						</div>
						<span>确认订单</span>
					</div>
					<div class="pay_use">
				   		<div class="recieve"><i>善款接收</i><img src="<%=resource_url%>res/images/h5/images/arrow.jpg" width="6" height="10">
				   		<b>宁波市善园公益基金会</b></div>
				       	<div class="uses"><i>善款处理</i><img src="<%=resource_url%>res/images/h5/images/arrow.jpg" width="6" height="10">
				       	<b>善款将根据求助项目实际发生的费用拨付给相关单位或求助者；实施救助后如善款尚有剩余，剩余部分并入基金会善库，用于其他求助者的求援；善款使用全程公示。</b></div>
				   	</div>
					<div class="box">
						<%--<div class="pay_subject">认捐主题：${pName}</div>--%>
	<div class="pay_ren">
	<p>捐赠人:</p>
	</div>
	
	<div class="pay_nc">
	<p>昵称：<span id="touristName">${nickName}</span></p>
	</div>
	
	<div class="pay_nc">
	<p>电话：<span id="touristMobile">${mobile}</span></p>
	</div>
	
	<div class="pay_ren">
	<p>充值信息:</p>
	</div>

	<div class="pay_nc">
	<p>充值金额：<span>${amount}</span>元</p>
	</div>

						<%--<div class="pay_anonymous">--%>
							<%--<label>捐助模式:</label>--%>
							<%--<span id="num1" style="display:none;"><i class="nm"><b></b></i><label>爱心人士</label></span>--%>
							<%--<span id="num2"><i class="gk"><b></b></i><label>公开</label></span>--%>
						<%--</div>--%>
						<div class="pay_tip" style="font-weight:bold;">请选择支付方式：</div>
						<c:choose>
					        <c:when test="${payway == 'wx'}">
							   <a href="javascript:void(0)" class="btn_a" onclick="pay()"><div class="wx_btn">
								<img src="<%=resource_url%>res/images/h5/images/wx-icon.jpg" width="40" height="32"><span>微信支付</span>
							</div></a> 
							</c:when>
							<c:otherwise >
							  <a href="javascript:void(0)" class="btn_a" onclick="alipay()"><div class="alipay_btn">
								<img src="<%=resource_url%>res/images/h5/images/alipay-icon.jpg" width="87" height="28">
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
	<input type="hidden" value ="${projectId}" id = "projectId">
	<input type="hidden" value ="${message}" id = "message">
	
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
				var projectId = $('#projectId').val();
				var message = $('#message').val();
				if(projectId==''||projectId==0||projectId==285){
					window.location.href ="http://www.17xs.org/h5GardenProject/project_list.do?message="+message;
				}
				else{
					window.location.href ="http://www.17xs.org/h5GardenProject/projectDetail.do?projectId="+projectId+"&message="+message;
				}
				}
			});
		}

		$('#closeDetail').click(function() {
			$('#payTips').hide();
		});
	</script>
</body>
</html>