<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园网 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/red_payment.css?v=20171128">
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body>
	<div class="red">
	    <div class="header">
	        <a href="/redPackets/myRedPackets.do" class="back">返回</a>
	        <div>红包</div>
	    </div>
	    <div class="red_center1">
	        <p class="center_p1">红包个数</p>
	        <input type="number" placeholder="填写个数" class="center_inp1" id="totalNum">
	        <span class="center_sp1">个</span>
	    </div>
	    <div class="red_center1">
	        <p class="center_p1">红包金额</p>
	        <input type="number" placeholder="填写金额" class="center_inp1" id="totalAmount">
	        <span class="center_sp1">元</span>
	    </div>
	    <div class="red_center1">
	        <p class="center_p1">留言</p>
	        <input type="text" placeholder="关注公益，大吉大利" class="center_inp1" id="slogans">
	    </div>
	    <div class="red_center3">
	        <p class="center_p3" id="amount">￥0.00</p>
	    </div>
	    <a href="javascript:void(0);" id="sendRedPackets">
	        <div class="red_center4">
	        	<p class="center_p3">塞钱进红包</p>
	    	</div>
	    </a>
	</div>
	<!--拆红包-->
	<div class="cue_f1"  id="redpacketsSuccess" style="display:none">
	    <div class="cue_background"></div>
	    <div class="red_back">
	        <img src="/res/images/h5/images/redPaper/hongbao.png">
	        <!--红包返回链接-->
	        <a href="javascript:void(0);" id="closeRed" class="red_back1"></a>
	        <!--拆红包链接-->
	        <a href="javascript:void(0);" class="red_lianjie" id="shareRed"></a>
	    </div>
	</div>
	
	<div class="red_fx" style="display:none" id="sharePersonRed">
	    <div class="cue_background"></div>
	    <div class="red_box">
	        <p>点击右上角，分享红包</p>
	    	<img src="/res/images/h5/images/redPaper/jiantou.png" class="red_icon1" />
	    </div>
	</div>
	    
	<div class="impot" id="info"></div>
	<input type="hidden" id="balance" value="${user.balance}">
	<input type="hidden" id="redpacketsId" value="${redpacketsId}">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript" src="/res/js/h5/redInput.js?v=20171128"></script>
	<script type="text/javascript">
	
		wx.config({
			debug : false,
			appId : '${appId}',
			timestamp : '${timestamp}',
			nonceStr : '${noncestr}',
			signature : '${signature}',
			jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline' ]
		});
	
		wx.ready(function(){
			wx.onMenuShareAppMessage({
				title : '${user.nickName}提着行善礼包来了，大家一起来抢红包做公益吧。', // 分享标题
				desc : '点击领取礼包，开启行善之路', // 分享描述
				link : 'http://www.17xs.org/redPackets/getPersonRedPaket.do?id='+$('#redpacketsId').val(), // 分享链接
				imgUrl : 'http://www.17xs.org/res/images/h5/images/redPaper/redpacket.png', // 分享图标
				type : 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
				success : function() {
					//用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/redPackets/myRedPackets.do';
				},
				cancel : function() {
					//用户取消分享后执行的回调函数
				}
			});
					
			wx.onMenuShareTimeline({
				title : '${user.nickName}提着行善礼包来了，大家一起来抢红包做公益吧。', // 分享标题
				link : 'http://www.17xs.org/redPackets/getPersonRedPaket.do?id='+$('#redpacketsId').val(), // 分享链接
				imgUrl : 'http://www.17xs.org/res/images/h5/images/redPaper/redpacket.png', // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/redPackets/myRedPackets.do';
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
		});
	</script>
</body>
</html>