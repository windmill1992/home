<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/base.css">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/userCenter.css">
	<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="giftImg">
		<img src="" id="imageUrl">
	</div>
	<div class="exchangeBox clearfix">
		<div class="exchangeLeft fl">
			<span id="score">0</span>
			<i class="jf-icon"></i>
		</div>
		<div class="fr">
			<input type="button" value="兑 换" class="exchangeBtn" id="exchangeBtn">
		</div>
	</div>
	<div class="giftDetailInfo">
<!-- 		<div>
			<h1>商品名称：</h1>
			<p>1000积分领取【善园年终感恩挂历】1个<br />颜色可选为：灰色、白色、红色<br />规格：380x600cm<br /><span class="red">价值1000元</span></p>
		</div>
		<div>
			<h1>兑换条件：</h1>
			<p>所有捐助者</p>
		</div>
		<div>
			<h1>兑换流程：</h1>
			<p>1、用户点击“兑换”若兑换成功后按提示填写收货信息</p>
			<p>2、善园工作人员将在15个工作日内安排发货，请耐心等待</p>
			<p>3、若因用户收货地址填错等问题引起的礼品丢失，善园概不负责</p>
			<p class="red">4、用户超过15天未提交收货信息，则系统默认您放弃该礼品，积分不可退，礼品不再发放</p>
		</div> -->
	</div>
	<!-- 礼品id -->
	<input type="hidden" id="sentType">
	<input type="hidden" id="number">
	<input type="hidden" id="id">
	<input type="hidden" id="giftName">
	<input type="hidden" id="isShow">
	<div id="addressId" style="display:none"></div>
		<script type="text/javascript">
	$(function() {
		
		    function getParamString(name) {
				var url = window.location.href;
				var paramString = url.substring(url.indexOf("?") + 1, url.length)
					.split("&");
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
					   var addressId = '';
					   for (var i in ret) {
						   var r = ret[0];
						   mobile = r.mobile ;
						   addressId = r.id ;
						   $('#addressId').text(addressId);
					   }

				   }
				}					
			});
			
			
		    var id = getParamString("id");
			var shixiao = getParamString("shixiao")
			if(shixiao == '1' ){
					$("#exchangeBtn").css('background-color','#E7E8ED');
					$('#isShow').text('1');
			}
			$.ajax({
				url: 'http://www.17xs.org/user/detailGiftInfo.do',
				dataType: 'json',
				type: 'post',
				data: {
					id: id
				},
				success: function(data) {
					   if(data.result == 0){
						   var item = data.item;
						   var id = '';
						   var coverImageUrl = '';
						   content = item.content;
						   var score = item.score;
						   var isShow = item.isShow;
						   $('.giftDetailInfo').append(content);
						   $('#score').text(score);
						   if(isShow == 1 ){
							   $("#exchangeBtn").css('background-color','#E7E8ED');
							   $('#isShow').text(isShow);
						   }
						   var sentType = item.sentType;
						   var number = item.number;
						   var giftName = item.giftName;
						   var id = item.id;
						   imageUrl = item.imageUrl;
						   $('#sentType').text(sentType);
						   $('#number').text(number);
						   $('#giftName').text(giftName);
						   $('#id').text(id);
						   $('#imageUrl').attr("src",imageUrl);
					   }
				   }					
			});
			$("h1").css('display','none');

			$(document).on('click','.exchangeBtn',function(){
                    var isShow = $("#isShow").text();
					if(isShow == 1 ){
						return;
					}
				    var giftId = $("#id").text() ;
					var giftName = $("#giftName").text() ;
                    var sendType = $("#sendType").text() ;					
					var num = 1;
					var state = '';
					if(sendType == 0){
						state = 207;
					}else if(sendType == 1){
						state = 200;
					}
					var addressId = $("#addressId").text();
					$.ajax({
						url: 'http://www.17xs.org/user/addGiftRecord.do',
						dataType: 'json',
						type: 'post',
						data: {
							giftId: giftId,
							giftName: giftName,
							num:num,
							state:state,
							addressId:addressId
						},
						success: function(data) {
							//var str = JSON.stringify(data);
							//alert(str);
							   if(data.flag == 1){
								$('#payTips').css('display','block');
								setTimeout(function(){
								window.location.href="http://www.17xs.org/ucenter/myGift.do?mark=2";
								},1000);
						   }
						}					
					});
			});					
	});
	</script>
</body>
</html>