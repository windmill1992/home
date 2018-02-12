<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
<head>
<meta name="viewport"
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,minimal-ui" />
<link rel="stylesheet" type="text/css" href="http://www.17xs.org:8080/res/css/dev/base.css"/>
<title>充值</title>
<link rel="stylesheet" type="text/css" href="http://www.17xs.org:8080/res/css/h5/mui.min.css"/>
<link rel="stylesheet" type="text/css" href="http://www.17xs.org:8080/res/css/h5/huzhu.css" />
</head>
<style>
	body{font-family: "Helvetica Neue",Helvetica,STHeiTi,sans-serif;}
</style>
<body>
<div class="recharge">
	<div class="recharge_num">
		<h3>充值金额</h3>
		<div class="recharge_num_sel clearfix">
			<div class="rmb on">
				<span class="num">50</span>元
			</div>
			<div class="rmb">
				<span class="num">100</span>元
			</div>
			<div class="rmb">
				<span class="num">200</span>元
			</div>
		</div>
		<div class="other_num">
			<input type="number" id="" value="" placeholder="其他金额"/>元
		</div>
	</div>
	
	<!-- <h3>会员专享服务</h3>
	<div class="vip_service">
		<div class="green_passway">
			<div class="green_top clearfix">
				<div class="passway">
					<img src="http://www.17xs.org:8080/res/images/h5/images/huzhu/green.png" width="30"/>
					<span>绿色就医通道</span>
				</div>
				<div class="passway_switch">
					<span>10元</span>
					<div class="mui-table-view-cell">
						<div class="mui-switch mui-switch-mini">
							<div class="mui-switch-handle"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="green_bottom">
				<p>专享贵宾医疗服务，享受顶级救治通道</p>
				<a href="">了解更多>></a>
			</div>
		</div>
	</div> -->
	
	<h3>支付信息</h3>
	<div class="pay_info">
		<div class="total_pay clearfix">
			<h3>总计支付</h3>
			<div class="total_money">
				50元
			</div>
		</div>
		<div class="total_pay pay_way clearfix">
			<h3>支付方式</h3>
			<div class="wx_pay">
				<img src="http://www.17xs.org:8080/res/images/h5/images/huzhu/wx_pay.png"/><span>微信支付</span>
			</div>
		</div>
	</div>
	
	<a class="wx_btn" onclick="pay()">微信充值</a>
</div>

<script src="http://www.17xs.org:8080/res/js/jquery-1.8.2.min.js" /></script>
<script type="text/javascript" src="http://www.17xs.org:8080/res/js/h5/dialog.js"></script>
<script type="text/javascript" src="http://www.17xs.org:8080/res/js/h5/mui.min.js" ></script>
<script type="text/javascript" src="http://www.17xs.org:8080/res/js/h5/huzhu/huzhu.js"></script>
<script>
	mui.init({
		swipeBack:true //启用右滑关闭功能
	});
	mui('.mui-switch').each(function() { //循环所有toggle
		//toggle.classList.contains('mui-active') 可识别该toggle的开关状态
		console.log(this.classList.contains('mui-active') ? 'true' : 'false');
		/**
		 * toggle 事件监听
		 */
		this.addEventListener('toggle', function(event) {
			//event.detail.isActive 可直接获取当前状态
			console.log(event.detail.isActive ? 'true' : 'false');
		});
	});
</script>
	<input type="hidden" value="${tradeNo}" id="tradeNo" />
	<input type="hidden" value="${pName}" id="pName" />
	<input type="hidden" value="${amount}" id="amount" />
	<input type="hidden" value="${projectId}" id="projectId" />
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		function pay() {
			$.ajax({
				url:"http://www.17xs.org:8080/exportTenpay/tenPayh5Recharge.do",
				data:{amount:100},
				success:function(result){
					var data=result;
					wx.config({
						debug : false,
						appId : data.appId,
						timestamp : data.config_timestamp,
						nonceStr : data.config_noncestr,
						signature : data.signature,
						jsApiList : [ 'chooseWXPay' ]
					});
					wx.chooseWXPay({
						timestamp : data.timestamp,
						nonceStr :  data.noncestr,
						package : data.packageValue,
						signType : 'MD5',
						paySign : data.paySign,
						success : function(res) {
							 alert("支付成功");
						}
					}); 
				},
				error:function(result){
				
				}
			})
		}
		$('#closeDetail').click(function(){
				$('#payTips').hide();
			});
	</script>
</body>
</html>