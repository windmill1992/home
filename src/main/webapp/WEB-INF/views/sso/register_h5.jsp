<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>注册</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/feedback.css?v=20160317">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/register.css?v=201712211000">
<script type="text/javascript">
  document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>

<body>
	<div class="bind">
	    <div class="bind_top">
	    <img src="<%=resource_url%>res/images/h5/images/topbg2.png">
	    </div>
	    <section>
	        <div class="bind_b1">
	            <div class="bind_fl1">
	                <img src="<%=resource_url%>res/images/h5/images/zh.png">
	            </div>
	            <div class="bind_fr1">
	                <input type="text" placeholder="请输入昵称" id="nickName" maxlength="20">
	            </div>
	        </div>
	        <div class="bind_b1">
	            <div class="bind_fl1">
	                <img src="<%=resource_url%>res/images/h5/images/sj1.png">
	            </div>
	            <div class="bind_fr1">
	                <input type="text" placeholder="请输入手机号" id="uPhone" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)">
	                <a  href="javaScript:;" class="fsyz rgray1_1" id="pSend" >发送验证码</a>
	            </div>
	        </div>
	        <div class="bind_b1">
	            <div class="bind_fl1">
	                <img src="<%=resource_url%>res/images/h5/images/sj.png">
	            </div>
	            <div class="bind_fr1">
	                <input type="text" placeholder="请输入验证码" id="code" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)">
	            </div>
	        </div>
	        <div class="bind_b1">
	            <div class="bind_fl1">
	                <img src="<%=resource_url%>res/images/h5/images/mm.png">
	            </div>
	            <div class="bind_fr1">
	                <input type="password" placeholder="请输入密码" id="password">
	            </div>
	        </div>
	    </section>
	    <footer>
	        <a class="foot_tz disabled" id="confirmRegister" href="javascript:;">提交注册</a>
	    </footer>
	</div>
	<div class="dialog" id="yzmDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h3>请输入验证码</h3>
			<div class="yzm-pic">
				<img src="" id="codepic"/>
				<a href="javascript:void(0);" class="refreshCode"></a>
				<input type="number" name="yzmCode" id="yzmCode" maxlength="4" value="" />
			</div>
			<div class="close">
				<a href="javascript:void(0);" class="close-a left" id="cancel">取消</a>
				<a href="javascript:void(0);" class="close-a right" id="sure">确定</a>
			</div>
		</div>
	</div>
	
	<!--绑定提示框-->
	<div class="cue">
	    <div class="cue_center">
	        <div class="cue_center1">
	        <p class="cue_p1">您还没有绑定善园账号</p>
	        <p class="cue_p2">绑定账号记录您行善的点点滴滴</p>
	        </div>
	        <div class="cue_center2">
	           <a href=""><div class="cue_fl"><p class="cue_pl">立即绑定</p></div></a>
	            <a href=""><div class="cue_fr"><p class="cue_pr">稍后再说</p></div></a>
	        </div>
	    </div>
	</div>
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" id="flag" value="${flag }">
	
	<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
	<script data-main='<%=resource_url%>res/js/h5/register.js?v=201802061705' src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
