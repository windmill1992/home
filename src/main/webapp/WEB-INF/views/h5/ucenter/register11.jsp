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
<body>
	<div class="topBg">
		<h1 class="topTitle">注&nbsp;册</h1>
	</div>
	<div class="regForm">
		<div class="form-item">
			<i class="user-icon"></i>
			<input type="text" placeholder="请输入你的手机号注册为善园账号">
		</div>
		<div class="form-item">
			<i class="code-icon"></i>
			<input type="text" placeholder="请输入验证码">
			<a href="javascript:;" class="getCodeA">获取验证码</a>
		</div>
		<div class="form-item">
			<i class="pwd-icon"></i>
			<input type="password" placeholder="密码不少于6位">
		</div>
		<input type="button" value="注&nbsp;&nbsp;&nbsp;册" class="regBtn">
		<p class="bindInfo">*点击“注册”按钮，表示同意<a href="javascript:;">《善园用户协议》</a></p>
	</div>
	<!-- 验证码发送提示 codeSuccess -->
	<div class="popup" style="display: block;" id="codeSuccess">
		<div class="codeInner">
			<p>我们已给你的手机号码</p>
			<p id="phoneNumber">+86-18888888888</p>
			<p>发送了一条验证短信。</p>
		</div>
	</div>
	<!-- 注册错误信息提示 -->
	<div class="errorBox" style="display: none;"><i></i>请输入正确的验证码</div>
	<script type="text/javascript">
		$(document).ready(function(){
			// 点击获取验证码
			$("#getCodeA").click(function(){
				getPhoneCode();
			});
			// 获取验证码getPhoneCode
			function getPhoneCode() {
				var btn = $("#getCodeA");
				var count = 60;
				var countdown = setInterval(function() {
					addBtnDisable(btn);
					btn.text("重新发送("+count+")");
					if (count == 0) {
						btn.text("获取验证码");
						removeBtnDisable(btn);
						clearInterval(countdown);
					}
					count--;
				}, 1000);
			}
			// 增加按钮disable
			function addBtnDisable(clickitem) {
				clickitem.attr("disabled", "disabled");
			}
			// 移除按钮disable
			function removeBtnDisable(clickitem) {
				clickitem.removeAttr("disabled");
			}
		});
	</script>
</body>
</html>