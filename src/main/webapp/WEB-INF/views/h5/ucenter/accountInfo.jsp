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
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20171229">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="topBg">
		<a href="/ucenter/userCenter_h5.do" class="topBack"></a>
		<h1 class="topTitle">个人设置</h1>
	</div>
	<ul class="accountInfo">
		<li class="accountItem flex spb">
			<h2 class="infoTitle">头像</h2>
			<div class="upload-pic" id="wxAvatar">
				<a href="javascript:;" class="changeLogo">
					<img src="${user.coverImageUrl}" id="coverImageWx" >
				</a>
			</div>
			<div class="upload-pic" id="h5Avatar">
			 	<form id="form1" action="/file/upload3.do" method="post" enctype="multipart/form-data">	
					<a href="javascript:;" id="${user.coverImageId}">
						<label for="fileInput1">
							<img src="${user.coverImageUrl}" id="coverImageH5"/>
						</label>
						<input type="file" name="file" hidefocus="true" id="fileInput1" class="file-input">
				   		<input type="hidden" name="type" id="type" value="8">
					</a>
				</form> 	
		 	</div>
		</li>
		<li class="accountItem flex spb">
			<h2 class="infoTitle">昵称</h2>
			<a href="/ucenter/editUserName.do?userName=${user.nickName}" class="item-a">${user.nickName}</a>
		</li>
		<li class="accountItem last flex spb">
			<h2 class="infoTitle">收获地址</h2>
			<a href="/ucenter/addressList.do" class="item-a">${addsNum}个</a>
		</li>
	</ul>
	<ul class="accountInfo">
		<h2 class="infoTitle" style="border-bottom: 1px solid #e5e5e5;">账号绑定</h2>
		<li class="accountItem clearfix">
			<i class="info-icon sj"></i>
			<h2 class="infoTitle">手机</h2>
		</li>
		<li class="accountItem clearfix">
			<i class="info-icon wx"></i>
			<h2 class="infoTitle">微信</h2>
			<div class="infoRight red">未绑定</div>
		</li>
		<li class="accountItem clearfix">
			<i class="info-icon qq"></i>
			<h2 class="infoTitle">Q Q</h2>
			<div class="infoRight red">未绑定</div>
		</li>
		<li class="accountItem clearfix">
			<i class="info-icon syw"></i>
			<h2 class="infoTitle">善园网</h2>
		</li>
		<li class="accountItem clearfix last">
			<i class="info-icon zfb"></i>
			<h2 class="infoTitle">支付宝</h2>
		</li>
	</ul>
	<div class="login-out">
		<a href="javascript:;" id="loginOut">退出登录</a>
	</div>
	<div class="dialog tips-dialog" id="logOutDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h2>退出登录</h2>
			<p>你确定要退出当前账号吗？</p>
			<div class="btn-close flex spb">
				<a href="javascript:;" id="cancel">取消</a>
				<a href="javascript:;" id="sure">确定</a>
			</div>
		</div>
	</div>
	<div id="msg" class="cue2"></div>
</body>
<script src="/res/js/jquery-1.8.2.min.js" /></script>
<script src="/res/js/util/ajaxform.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	wx.config({
		debug : false,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${noncestr}',
		signature : '${signature}',
		jsApiList : ['chooseImage','previewImage','uploadImage' ]
	});
</script>
<script src="/res/js/h5/ucenter/accountInfo.js?v=20171211"></script>
</html>