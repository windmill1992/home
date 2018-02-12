<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/base.css">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/userCenter.css">
	<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<!-- 头部 -->
	<%@ include file="./../common/head.jsp" %>
	<div class="wrapper">
		<div class="mainBox">
			<div class="mainTitle clearfix">
				<i class="function-icon icon5"></i>
				<h2>我的证书</h2>
			</div>
		</div>
		<ul class="rewardList">
			<li class="rewardItem">
				<h2 class="rewardTitle"><i class="point"></i>获得慈善公益证书</h2>
				<div class="rewardInner">
					<p class="rewardInfo">为了表彰慈善公益，特此感谢“小感叹”您对公益的支持，请再接再厉。</p>
					<img src="../res/images/h5/images/userCenter/reward.png" class="rewardPic">
					<div class="rewardDate clearfix">
						<span>2015-12-16</span>
						<span class="shareBtn"></span>
					</div>
				</div>
			</li>
			<li class="rewardItem">
				<a href="javascript:;" class="rewardLoadMore"><i class="point"></i>查看更多证书</a>
			</li>
		</ul>
	</div>
	<!-- 公共底部 -->
	<%@ include file="./../common/footer.jsp" %>
</body>
</html>