<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css">
<link rel="stylesheet" type="text/css" href="/res/js/h5/mobiscroll/css/mobiscroll.scroller.css">
<link rel="stylesheet" type="text/css" href="/res/js/h5/mobiscroll/css/mobiscroll.scroller.sense-ui.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/userCenter.css?v=<%=uc_version%>">
<script>
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<style type="text/css">
	body{
		background-color: #f4f3f0;overflow-x: hidden;
	}
	.wrapper{
		margin-bottom: .5rem;
	}
</style>
<body>
	<div class="userTop">
		<a href="/ucenter/accountInfo.do">
			<div class="userLogo">
				<!-- 点击头像跳转至账户信息页面 -->
				<img src="/res/images/detail/people_avatar.jpg" id="headImageUrl">
			</div>
			<div class="userInfo hasInfo">
				<h2 class="name" id="nickName"></h2>
			</div>
		</a>
	</div>
	<div class="wrapper">
		<div class="mainBox">
			<div class="mainTitle flex fcen">
				<i class="function-icon icon3"></i>
				<h2>地址管理</h2>
			</div>
		</div>
		<div class="addressForm">
			<div class="formItem">
				<label>姓名</label>
				<input type="text" id="name" placeholder="请输入姓名">
			</div>
			<div class="formItem">
				<label>电话</label>
				<input type="text" id="phone" placeholder="请输入联系电话" value="${user.mobileNum }">
			</div>
			<div class="formItem">
				<label>省份</label>
				<select id="province"></select>
			</div>
			<div class="formItem">
				<label>城市</label>
				<select id="city"></select>
			</div>
			<div class="formItem">
				<label>地区</label>
				<select id="area"></select>
			</div>
			<div class="formItem">
				<label>地址</label>
				<input type="text" id="detailAddress" placeholder="请输入你的详细地址">
			</div>
			<div class="formItem">
				<label>邮箱</label>
				<input type="text" id="email" placeholder="请输入您的联系邮箱">
			</div>
		</div>
		<div class="btnBox">
			<input type="button" value="提&nbsp;&nbsp;交" class="submitBtn" id="submitBtn">
		</div>
	</div>

	<div class="pageT" id="type" style="display: none;"></div>
	<!-- 错误提示信息 控制显示/消失  -->
	<div class="cue2" id="msg"></div>
		
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<!--<script src="/res/js/h5/zepto.min.js"></script>-->
	<script src="/res/js/h5/mobiscroll/js/mobiscroll.zepto.js"></script>
	<script src="/res/js/h5/mobiscroll/js/mobiscroll.core.js"></script>
	<script src="/res/js/h5/mobiscroll/js/mobiscroll.scroller.js"></script>
	<script src="/res/js/h5/mobiscroll/js/mobiscroll.select.js"></script>
	<script src="/res/js/h5/mobiscroll/js/mobiscroll.i18n.zh.js"></script>
	<script src="/res/js/h5/selarea/js/selarea.js"></script>
	<script src="/res/js/h5/ucenter/invoiceAddress.js?v=20180208"></script>
</body>
</html>