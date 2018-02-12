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
	<div class="userTop">
		<div class="userLogo">
			<!-- 点击头像跳转至账户信息页面 -->
			<a href="http://www.17xs.org/ucenter/accountInfo.do?v=1">
				<img src="/res/images/h5/images/temp/userLogo.png" id="headImageUrl">
			</a>
		</div>
		<div class="userInfo hasInfo">
			<h2 class="name" id="nickName"><i class="medal"></i></h2>
			<p class="spec phone" id="mobile"></p>
			<p class="address" id="address"></p>
		</div>
	</div>
	<div class="wrapper">
		<div class="mainBox pd">
			<div class="mainTitle flex fcen spb">
				<div class="left flex fcen">
					<i class="function-icon icon3"></i>
					<h2>开票历史详情页</h2>	
				</div>
			</div>
		</div>
		<ul class="mainBox pd">
			<li class="fpListItemP clearfix">
				<label>快递公司</label>
				<span id='mailCompany'></span>
			</li>
			<li class="fpListItemP clearfix">
				<label>快递单号</label>
				<span id='mailCode'></span>
			</li>
			<li class="fpListItemP clearfix">
				<label>快递费用</label>
				<span id='mailAmount'></span>
			</li>
		</ul>
		<ul class="mainBox pd">
			<li class="fpListItemP clearfix">
				<label>提交时间</label>
				<span id='lastUpdateTime'></span>
			</li>
			<li class="fpListItemP clearfix">
				<label>开票抬头</label>
				<span id='invoiceHead'></span>
			</li>
			<li class="fpListItemP clearfix">
				<label>开票内容</label>
				<span id='content'>爱心捐助</span>
			</li>
			<li class="fpListItemP clearfix">
				<label>开票金额</label>
				<span><i style="color: #ff6c0a;" id='invoiceAmount'></i>元</span>
			</li>
			<li class="fpListItemP clearfix">
				<label>开票张数</label>
				<span>1张</span>
			</li>
		</ul>
		<ul class="mainBox pd">
			<li class="fpListItemP clearfix">
				<label>收件人</label>
				<span id='name'></span>
			</li>
			<li class="fpListItemP clearfix">
				<label>收件电话</label>
				<span id='mobile2'></span>
			</li>
			<li class="fpListItemP clearfix">
				<label>收件地址</label>
				<span id='address2'></span>
			</li>
		</ul>
		<ul class="mainBox pd">
			<div class="mainer"></div>
		</ul>
	</div>
	<script type="text/javascript">
	    $(function () {
			function getParamString(name) {
				var url = window.location.href;
				var paramString = url.substring(url.indexOf("?") + 1, url.length).split("&");
				var param = null;
				var value = null;
				for (var i = 0; i < paramString.length; i++) {
					param = paramString[i];
					if (param.substring(0, param.indexOf("=")) == name) {
						value = param.substring(param.indexOf("=") + 1, param.length);
						return value;
					}
				}
				return null;
			}
			var pageNum = 10;
			var page = 1;
			var id = getParamString("id");
			$.ajax({
				url: 'http://www.17xs.org/user/detailInvoiceInfo.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNum,
					page: page,
					id:id
				},
				success: function(data) {
					if(data.result == 0){
					   var item = data.item;
					   var mailCode = item.mailCode;
					   if(mailCode == ''){
						   mailCode = '无';
					   }
					   $('#mailCode').text(mailCode);
					   var mailCompany = item.mailCompany;
					   if(mailCompany == ''){
						   mailCompany = '无';
					   }
					   $('#mailCompany').text(mailCompany);
					   var mailAmount = item.mailAmount;
					   if(mailAmount == '0' || mailAmount == ''){
						   mailAmount = '无';
					   }
					   $('#mailAmount').text(mailAmount);
					   var lastUpdateTime = item.lastUpdateTime;
					   $('#lastUpdateTime').text(lastUpdateTime);
					   var invoiceHead = item.invoiceHead;
					   $('#invoiceHead').text(invoiceHead);
					   //var content = item.content;
					   //$('#content').text(content);
					   var invoiceAmount = item.invoiceAmount;
					   $('#invoiceAmount').text(invoiceAmount);
					   var name = item.name;
					   $('#name').text(name);
					   var mobile = item.mobile;
					   $('#mobile2').text(mobile || '');
					   var address = item.province + item.city + item.area + item.detailAddress;
					   $('#address2').text(address || '');
					   var htmlStr = '';
					   var ret = data.donateList;
					   for (var i in ret) {						
						   var r = ret[i];
						   title = r.title;
						   donateAmount = r.donateAmount;
						   htmlStr = htmlStr + '<li class="fpListItemP1">为"<i>'+title+'</i>" 捐助了'+donateAmount+'元</p></li>';
					   }
					   $('.mainer').append(htmlStr);
				   }
				}					
			});

			$.ajax({
				url: 'http://www.17xs.org/user/userInfo.do',
				dataType: 'json',
				type: 'post',
				data: {},
				success: function(data) {
					if(data.result == 0){
					   var item = data.item;
					   var headImageUrl = item.headImageUrl;
					   $('#headImageUrl').attr("src",headImageUrl);
					   var nickName = item.nickName;
					   $('#nickName').text(nickName);
				   }
				}					
			});
			
			var pageNum1 = 10;
			var page1 = 1;
			$.ajax({
				url: 'http://www.17xs.org/user/userAddressList.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNum1,
					page: page1
				},
				success: function(data) {
					   if(data.flag == 1){
					   var ret = data.obj.data;
					   var mobile = '';
					   var address = '';
					   for (var i in ret) {
						   var r = ret[0];
						   mobile = r.mobile ;
						   address = r.province + r.city + r.area + r.detailAddress;
						   $('#mobile').text(mobile);
						   $('#address').text(address);
					   }
				   }
				}					
			});
		});
	</script>
</body>
</html>