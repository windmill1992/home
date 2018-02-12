<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="cache-control" content="no-store" />
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport" />
<title>重置密码</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entReg.css" />

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/password.css?v=201712201400" />
</head>

<style type="text/css">
	.password .pwCon .rlitem .rltext .wrinfo{
		width: 200px;
	}
</style>
<body>
	<div class="w1000 wrnav">
		<h1>
			<a href="http://www.17xs.org" title="">
				<img src="<%=resource_url%>res/images/logo-lg.jpg" alt="" />
			</a>
		</h1>
		<div class="wrnavlb yh"> 
			<span><em></em>捐款方式多元化</span> 
			<span><em></em>捐款过程透明、可追溯</span> 
			<span><em></em>社会大众多元参与</span> 
		</div>
	</div>
	<div class="wPs wPsphone" id="password">
		<div class="password">
			<div class="number-prim">
				<dl class="number first"> <dt class="s-num">1</dt>
					<dd class="s-text s-stext">验证身份</dd>
				</dl>
				<dl class="number secend"> <dt class="s-num_pw">2</dt>
					<dd class="s-text">修改登录密码</dd>
				</dl>
				<dl class="number end"> <dt class="s-num s-ok"></dt>
					<dd class="s-text">完成</dd>
				</dl>
			</div>
			<h2>您正在使用手机验证码验证身份，请完成以下操作</h2>
			<div class="pwCon">
				<div class="rlitem"> 
					<label>手机号：</label>
					<div class="rltext"> 
						<input class="inputtext inputcode" id="phone" value="" readonly />
					</div>
				</div>
				<div class="rlitem"> 
					<label></label>
					<div class="rltext">
						<a class="sendPhone" id="pSend" href="javascript:;">获取手机验证码</a>
						<a class="sendPhone_ag" id="pSend1" href="javascript:;"></a> 
						<span class="wrsdpt"></span> 
					</div>
				</div>
				<div class="rlitem"> 
					<label>验证码：</label>
					<div class="rltext"> 
						<input type="text" class="inputtext inputcode" placeholder="请输入6位数字验证码" id="mcode"/> 
						<span class="wrinfo mt5"></span> 
					</div>
				</div>
				<div class="rlitem"> 
					<label></label>
					<div class="rltext">
						<a class="nextbtn" id="signin1" href="javascript:;">下一步</a>
						<p><a class="color4" id="getWay" href="javascript:;">重新选取验证方式</a></p>
					</div>
				</div>
			</div>
		</div> 
	</div>
	<div class="wrroot">
		<p>
			<a href="<%=resource_url%>" target="_blank">首页</a><i></i>
			<a href="<%=resource_url%>help/about.do" target="_blank">关于善园基金会</a><i></i>
			<a href="<%=resource_url%>help/service.do" target="_blank">服务协议</a><i></i>
			<a href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes" target="_blank">联系客服</a><i></i>
			<a href="http://www.miitbeian.gov.cn/" target="_blank">浙ICP备15018913号-1</a>
		</p>
		<p>&nbsp;&nbsp;Copyright&nbsp;&nbsp;©&nbsp;&nbsp;&nbsp;&nbsp;宁波市善园公益基金会&nbsp;&nbsp;版权所有</p>
		<p>杭州智善网络科技有限公司&nbsp;&nbsp;提供技术支持</p>
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
	<div class="cue2" id="msg"></div>
	<input type="hidden" id="username" value="${name}" /> 
	<input type="hidden" id="phoneNum" value="${phone}" /> 
	<input type="hidden" name="pageType" id="pageType" value="1" />
	
	<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
	<script data-main='<%=resource_url%>res/js/dev/password.js?v=201712201400' src="<%=resource_url%>res/js/require.min.js" charset="UTF-8"></script>
</body>
</html>