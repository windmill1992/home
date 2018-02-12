<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>用户中心</title>
	<link rel="stylesheet" type="text/css" href="/res/css/dev/base.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/dialog.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/head.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/dev/form.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/center.css?v=20180102" /> 
</head>
<body>
	<div id="pageContainer">
		<div class="user_center">
			<!--个人信息卡//-->
			<div class="profile">
				<a class="info" href="http://www.17xs.org/ucenter/userCenter_h5.do">
					<p class="avatar"><img src="${user.coverImageUrl}"></p>
					<p class="nickname">${user.nickName}</p>
				</a>
				<ul class="data_tab">
					<a href="javascript:;" id="myCry">
						<li><i>我的求助</i><b id="needHelpNum"></b></li>
					</a>
					<a href="javascript:;" id="myDonate">
						<li><i>我的捐赠</i><b>￥${user.totalAmount}</b></li>
					</a>
					<a href="javascript:;" id="careProject">
						<li class="last"><i>最新动态</i><b>${careCount}</b></li>
					</a>
				</ul>
				<div class="clear"></div>
			</div>
			<!--我的求助-->
			<div class="list" id="list_1"></div>
			<!----我的捐赠--//-->
			<div class="list donate_list" id="list_2"></div>
			<!--最新动态//-->
			<div class="list status" id="list_3"></div>
			<!--页脚//-->
			<div class="footer"> <span>© 2015 善园基金会  版权所有</span> <img src="/res/images/h5/images/min-logo.jpg"> </div>
		</div>
	</div>
	<div id="bigImg">
		<div class="bd"></div>
	</div> 
	<input type="hidden" value="${itemType}" id="itemType"> 
	<input type="hidden" value="${pstate}" id="pstate"> 
</body>
<script src="/res/js/jquery-1.8.2.min.js" /></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	wx.config({
		debug: false,
		appId: '${appId}',
		timestamp: '${timestamp}',
		nonceStr: '${noncestr}',
		signature: '${signature}',
		jsApiList: ['previewImage']
	});
</script>
<script data-main="/res/js/h5/needhelplist.js?v=2018" src="/res/js/require.min.js"></script>
</html>