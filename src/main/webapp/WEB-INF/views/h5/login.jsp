<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/login.css"/>
</head> 

<body>
<div id="pageContainer">
  <div class="top_logo"><img src="<%=resource_url%>res/images/h5/images/logo_login.jpg" width="174" height="100"></div>
  <div class="login_box">
  	<div class="login_tabs"><a href="javascript:void(0)" class="tab_name cur">普通登录</a><a href="javascript:void(0)" class="tab_tel">手机登录</a></div>
    <ul id="form_1">
	    <li>
	      <input type="text" class="usr" id="username" value="" placeholder="输入用户名">
        </li>
	    <li>
	      <input type="text" class="psw" id="pwd" value="" placeholder="输入密码">
        </li>
	    <li>
	      <input type="button" class="confirm" value="确定">
        </li>
      </ul>
	  <ul id="form_2" style="display:none;">
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="get_code" value="获取验证码">
        </li>
	    <li>
	      <input type="button" class="confirm" value="确定">
        </li>        
      </ul>      
      <div class="login_ft">
      	<div class="reg_link">还没有账号？<a href="#">注册新用户</a></div>
        <div class="other_link"><span>其它登录方式：</span><a href="#" class="qq"></a><a href="#" class="wx"></a></div>
      </div>
	  <div class="clear"></div>
  </div>
  <!-- <div class="login_box">
    <div class="login_tabs"><a href="#" class="tab_name">普通登录</a><a href="#" class="tab_tel cur">手机登录</a></div>
    <ul>
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="get_code" value="重新获取">
        </li>
	    <li style="height:60px;padding:5px 0px;">
	      <input type="button" class="confirm" value="确定">
        </li>
      </ul>
	  <div class="clear"></div>
  </div>
	<div class="login_box">
	  <div class="login_tabs"><a href="#" class="tab_name">普通登录</a><a href="#" class="tab_tel cur">手机登录</a></div>
	  <ul>
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="reget_code" value="60秒后重新发送">
        </li>
	    <li style="height:45px;padding:5px 0px;">
	      <input type="button" class="confirm" value="确定">
        </li>
	  </ul>
	  <div class="clear"></div>
  </div>
	<div class="register_box">
	  <ul>
      	<li class="title">用户注册</li>
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="psw" value="输入密码">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="reget_code" value="60秒后重新发送">
        </li>
	    <li style="height:45px;padding:5px 0px;">
	      <input type="button" class="confirm" value="确定">
        </li>
      </ul>
	  <div class="clear"></div>
  </div> -->
  
</div>
<script data-main="<%=resource_url%>res/js/h5/login.js?v=20151106133311" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>