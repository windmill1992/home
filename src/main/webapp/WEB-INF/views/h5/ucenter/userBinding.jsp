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
	<div class="topLogo">
		<img src="../res/images/h5/images/userBinding/sjj_logo.png">
		<h1 class="topTitle">添加绑定账号</h1>
	</div>
	<div class="logoForm">
		<div class="form-item">
			<i class="user-icon"></i>
			<input type="text" placeholder="输入绑定账号">
		</div>
		<div class="form-item">
			<i class="pwd-icon"></i>
			<input type="password" placeholder="输入绑定密码">
			<a href="findPwd.html" class="findPwsA">忘记密码</a>
		</div>
		<input type="button" value="绑&nbsp;&nbsp;&nbsp;定" class="bindBtn">
		<p class="bindInfo">您还没有账号，马上 <a href="register.html">注册</a></p>
	</div>
	<!-- 绑定成功 successTip -->
	<div class="popup" style="display: none;" id="successTip">
		<div class="bindTip">绑定成功</div>
	</div>
	<!-- 绑定提示 warningTip -->
	<div class="popup" style="display: none;" id="warningTip">
		<div class="bindWarning">
			<div class="warningTop">
				<h2>登录失败</h2>
				<p>账号或密码错误，请重新输入。</p>
			</div>
			<div class="warningBtn" id="warningBtn">确&nbsp;定</div>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function(){
			/*绑定提示 确定js*/
			$("#warningBtn").click(function(){
				$("#warningTip").hide();
			});
		});
	</script>
</body>
</html>