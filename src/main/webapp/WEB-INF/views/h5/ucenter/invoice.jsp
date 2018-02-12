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
	<link rel="stylesheet" type="text/css" href="/res/css/h5/userCenter.css?v=<%=uc_version%>">
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<!-- 头部 -->
	<%@ include file="./../common/head.jsp" %>
	<div class="wrapper">
		<div class="mainBox">
			<div class="mainTitle flex spb fcen">
				<div class="left flex fcen"> <i class="function-icon icon3"></i>
					<h2>开票记录</h2> 
				</div>
			</div>
			<ul class="tabBox flex">
				<li class="stateItem w33 active">
					<p>可开票</p> <span>${totalAmount}元</span> 
				</li>
				<li class="stateItem w33" id="mark2">
					<p>待发货</p> <span>${dnum}张</span> 
				</li>
				<li class="stateItem w33 last">
					<p>已发货</p> <span>${ynum}张</span> 
				</li>
			</ul>
		</div>
		<div class="tabInfo" id="tab1">
			<div class="mainBox">
				<div class="fpSumItem">
					<p>开票金额：<span id="money_sum">0.00</span>元</p>
					<p class="remarkItem">注：开票金额少于100元，邮寄费用由爱心人士承担</p>
				</div>
				<div class="fpSumItem last"> 
					<label>抬头</label> 
					<input type="text" id="invoiceHead" placeholder="请输入发票抬头(姓名或单位名称)"> 
				</div>
			</div>
			<div class="mainBox">
				<ul class="fpdonateList">
					<div class="mainerLeft"></div>
				</ul>
				<div class="roadMoreBox" id="moreRecordLeft">
					<a href="javascript:;" id="loadMoreALeft"></a> 
					<div class="selAll flex fcen">
						<label for="chkAll">全选</label>
						<input type="checkbox" id="chkAll" value="0" data-id="0" class="check"/>
					</div>
				</div>
			</div> 
			<input type="button" value="提&nbsp;&nbsp;交" class="submitBtn disabled"> 
		</div>
		<div class="tabInfo" id="tab2">
			<ul class="fpList">
				<div class="mainerCenter"></div>
			</ul>
			<div class="roadMoreBox" id="moreRecordCenter">
				<a href="javascript:;" id="loadMoreACenter"></a>
			</div>
		</div>
		<div class="tabInfo" id="tab3">
			<ul class="fpList">
				<div class="mainerRight"></div>
			</ul>
			<div class="roadMoreBox" id="moreRecordRight">
				<a href="javascript:;" id="loadMoreARight"></a>
			</div>
		</div>
	</div>
	<div class="dialog tips-dialog" id="addrDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h2 id="d-title"></h2>
			<p id="d-ask"></p>
			<div class="btn-close flex spb">
				<a href="javascript:;" id="cancel"></a>
				<a href="javascript:;" id="sure"></a>
			</div>
			<a href="javascript:;" id="closeIcon"></a>
		</div>
	</div>
	<div id="msg" class="cue2"></div>
	<div id="loading">
		<div id="loadingIcon">
			<img src="/res/images/h5/images/volunteer/loading9.gif"/>
		</div>
	</div>
	
	<script src="/res/js/h5/ucenter/invoice.js?v=20180102"></script>
	<script>
		$(function(){
			$('#linkToUcenter')[0].href = 'http://www.17xs.org/ucenter/userCenter_h5.do';
		})
	</script>
</body>
</html>