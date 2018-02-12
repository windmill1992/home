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
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body>
	<div class="red">
	    <div class="header">
	        <a href="http://www.17xs.org/ucenter/userCenter_h5.do" class="back">返回</a>
	        <div>我的红包</div>
	    </div>
	    <a href="/redPackets/getRedPaper.do">
	    	<div class="red_o">
		        <p class="red_fl fl">我收到的红包</p>
		        <p class="red_fr fr">${usedGetedCount}/${userGetedCount}</p>
	   		</div>
	    </a>
	    <a href="/redPackets/mySendRedPaper.do">
	    	<div class="red_o">
		        <p class="red_fl fl">我发出的红包</p>
		        <p class="red_fr fr">${getedCount}/${sendCount}</p>
	    	</div>
	    </a>
	    <a href="/redPackets/sendRedPackets.do">
	    	<div class="red_t">
	        	<p>发红包</p>
	   		</div>
	    </a>
	</div>
</body>
</html>