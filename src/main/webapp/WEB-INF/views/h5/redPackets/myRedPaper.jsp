<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园网 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20171128">
<link rel="stylesheet" type="text/css" href="/res/css/h5/red_payment.css?v=20171128">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="red">
		<div class="header">
			<a href="/redPackets/myRedPackets.do" class="back">返回</a>
			<div>我发出的红包</div>
		</div>
	</div>
	<ul class="tabBox">
		<li class="active" data-state="1">已发出</li>
		<li data-state="2">已使用</li>
		<li data-state="3">已过期</li>
	</ul>
	<div class="tabWrap" id="tab1">
		<ul id="redPaperList" ></ul>
		<div class="roadMoreBox">
			<a href="javascript:;" id="loadMoreA">查看更多红包</a>
		</div>
	</div>
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/res/js/h5/myRedPaper.js?v=20180102"></script>
</body>
</html>