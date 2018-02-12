<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/project_detail_share.css">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/base.css?v=201512111">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/donate.css?v=201512112">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="jkHeadBox">
		<div class="userLogo">
			<img src="" id="headImageUrl"></img>
		</div>
		<div class="userInfo hasInfo">
			<h2 class="name" id="nickName"></h2>
			<p class="spec phone" id="mobile"></p>
			<p class="address" id="address"></p>
		</div>
	</div>
	<div class="jkMain02Box">
		<div class="jkMainTitle"><i class="gift"></i>礼品记录</div>
		<ul class="fpList">
			 <div class="mainer"></div>
			<div class="loadMore">
				<div style="display:none">
				 <span id="page"></span>
				 <span id="pageNum"></span>
				 <span id="total"></span>
				</div >
				<div class="moreRecord">
				   <a href="javascript:;" class="loadMoreA" id="loadMoreA" style="color: #707070;">查看更多礼品</a>
				 </div>
				<div class="noneRecord" style="display:none">
				<span class="loadMoreA" style="color: #707070;">没有更多了</span></div>
			</div>
		</ul>
	</div>
	<script type="text/javascript">
	    $(function () {
				$.ajax({
					url: 'http://www.17xs.org/user/userInfo.do',
					dataType: 'json',
					type: 'post',
					data: {
					},
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
							   var r = ret[i];
							   mobile = r.mobile ;
							   address = r.province + r.city + r.area + r.detailAddress;
							   $('#mobile').text(mobile);
							   $('#address').text(address);
						   }

					   }
					}					
				});
				
				var pageNum = 10;
				var page = 1;
				$.ajax({
					url: 'http://www.17xs.org/user/giftRecordList.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page
					},
					success: function(data) {
						//var str = JSON.stringify(data);
				        //alert(str);
						   if(data.flag == 1){
						   var ret = data.obj.data;
						   var page = data.obj.page;
						   $('#page').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNum').text(pageNum);
						   var giftHeadImageUrl = '';
						   var gName = '';
						   var giftContent = '';
						   var htmlStr = '';
						   var nums = data.obj.nums;
						   var pageNum = data.obj.pageNum;
						   var state = '';
						   var stateContent = '';
						   for (var i in ret) {
							   var r = ret[i];
							   giftHeadImageUrl = r.giftHeadImageUrl;
							   giftContent = r.giftContent ;
							   gName = r.gName ;
							   state = r.state;
							   if(state == 200){
								   stateContent = '待发货';
							   }else if(state == 203){
								   stateContent = '已发货';
							   }else if(state == 207){
								   stateContent = '已领取';
							   }
                               htmlStr = htmlStr + '<li class="giftListItem clearfix"><div class="giftItemImg"><img src="'+giftHeadImageUrl+'"></div><div class="giftRight"><h3 class="giftName">'+gName+'<span class="giftState">'+stateContent+'</span></h3><p>'+giftContent+'</p>';
						   }
						   $('.mainer').append(htmlStr);
						   //alert(pageNum);
						   if(nums <= pageNum){
							   $(".moreRecord").css('display','none'); 
							   $(".noneRecord").css('display','block'); 
						   }
					   }
					}					
				});
				
				$(document).on('click','#loadMoreA',function(){
					var nums = $("#nums").text() ;
					var page = $("#page").text() ;
					var pageNum = $("#pageNum").text();
					page = Number(page) + 1;
					$('#page').text(page);
				$.ajax({
					url: 'http://www.17xs.org/user/giftRecordList.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page
					},
					success: function(data) {
						//var str = JSON.stringify(data);
				        //alert(str);
						   if(data.flag == 1){
							$('#page').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNum').text(pageNum);
						   var sum = page * pageNum;
						   var ret = data.obj.data;
						   var giftHeadImageUrl = '';
						   var gName = '';
						   var giftContent = '';
						   var htmlStr = '';
						   var nums = data.obj.nums;
						   var pageNum = data.obj.pageNum;
						   var state = '';
						   var stateContent = '';
						   for (var i in ret) {
							   var r = ret[i];
							   giftHeadImageUrl = r.giftHeadImageUrl;
							   giftContent = r.giftContent ;
							   gName = r.gName ;
							   state = r.state;
							   if(state == 200){
								   stateContent = '待发货';
							   }else if(state == 203){
								   stateContent = '已发货';
							   }else if(state == 207){
								   stateContent = '已领取';
							   }
                               htmlStr = htmlStr + '<li class="giftListItem clearfix"><div class="giftItemImg"><img src="'+giftHeadImageUrl+'"></div><div class="giftRight"><h3 class="giftName">'+gName+'<span class="giftState">'+stateContent+'</span></h3><p>'+giftContent+'</p>';
						   }
						   $('.mainer').append(htmlStr);
						   //alert(pageNum);
						   if(sum >= nums){
							   $(".moreRecord").css('display','none'); 
							   $(".noneRecord").css('display','block');  
						   }
					   }
					}					
				});
			});
				
		});
	</script>
</body>
</html>