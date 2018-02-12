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
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=201712131635">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<style type="text/css">
	.cue2{bottom: 100px;}
	.topBg{position: fixed;top: 0;z-index: 999;}
</style>
<body style="background-color: #f4f3f0;">
	<div class="topBg">
		<a href="/ucenter/accountInfo.do" class="topBack"></a>
		<h1 class="topTitle">收货地址</h1>
		<a href="javascript:;" id="manageAddr" class="top-r">管理</a>
	</div>
	<ul class="addressList"></ul>
	<div class="select2 flex spb fcen">
		<div class="left">
			<input type="checkbox" name="selAll" id="selAll" class="chk" /><label for="selAll">全选</label>
			<input type="checkbox" name="selReverse" id="selReverse" class="chk" /><label for="selReverse">反选</label>
		</div>
		<a href="javascript:void(0);" id="delSelected" class="right"></a>
	</div>
	<div class="addAddressBox">
		<!-- 连接到地址填写页面 -->
		<a href="javascript:;" id="addAddress">
			<img src="/res/images/h5/images/userBinding/addAddress.png">
			<span>新增地址</span>
		</a>
	</div>
	<div id="nodata"></div>
	<div class="dialog tips-dialog" id="delDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h2>删除地址</h2>
			<p>您确定删除这个地址吗？</p>
			<div class="btn-close flex">
				<a href="javascript:void(0);" id="cancel">取消</a>
				<a href="javascript:void(0);" id="sure">删除</a>
			</div>
		</div>
	</div>
	<div id="msg" class="cue2"></div>
	
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<script src="/res/js/h5/ucenter/addressList.js?v=201712131635"></script>
</body>
</html>