<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园网 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/red_payment.css?v=20171128">
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20171128">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body>
	<div class="red">
	    <div class="header">
	        <a href="/redPackets/mySendRedPaper.do" class="back">返回</a>
	        <div>已领取的红包</div>
	    </div>
		<div id="redList"></div>
	
	    <!--点击加载更多-->
	    <div class="roadMoreBox">
	    	<a href="javascript:;" id="loadMoreA">查看更多红包</a>
	    </div>
	</div>
</body>
<input type="hidden" id="redpacketsId" value="${redpacketsId}">

<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="/res/js/h5/red_already.js?v=20171128"></script>
</html>