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
		<div class="jkMainTitle">
			<i class="receipt" ></i>开票记录
		</div>
		<ul class="fpList">
			 <div class="mainer"></div>
			<div class="loadMore">
				<div style="display:none">
				 <span id="page"></span>
				 <span id="pageNum"></span>
				 <span id="total"></span>
				</div >
				<div class="moreRecord">
				   <a href="javascript:;" class="loadMoreA" id="loadMoreA" style="color: #707070;">查看更多记录</a>
				 </div>
				<div class="noneRecord" style="display:none">
				<span class="loadMoreA" style="color: #707070;background-color: rgb(244, 243, 240);">没有更多记录了</span></div>
			</div>
		</ul>
	</div>

	<script type="text/javascript">
	$(function() {
				var pageNum = 10;
				var page = 1;
				$.ajax({
					url: 'http://www.17xs.org/user/invoiceList.do',
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
						   var nums = data.obj.nums;
						   var total = data.obj.total;
						   var result = new Array;
						   var id = "";
						   var invoiceAmount = "";
						   var invoiceHead = "";
						   var state = "";
						   var stateContent = "";
						   var createTime = "";
						   var info = "";
						   var htmlStr = "";
						   var ret = data.obj.data;
						   for (var i in ret) {
							  var r = ret[i];
							  invoiceHead = r.invoiceHead;
							  title = r.title;
							  dMoney = r.dMoney;
							  createTime = r.createTime;
							  invoiceAmount = r.invoiceAmount;
							  state = r.state;
							  id = r.id;
							  if(state == '300'){
								  stateContent = '待开票';
							  }else{
								  stateContent = '已开票';
							  }
							  showTime = r.showTime;
							  htmlStr = htmlStr + '<li class="fpListItem"><a href="javascript:void(0);"><i style="display:none">'+id+'</i><div class="fpListTitle clearfix"><p>'+invoiceHead+'</p><span>¥'+invoiceAmount+'</span></div><div class="fpListState clearfix"><span class="fl" id="time">'+createTime+'</span><span class="fr" id="">'+stateContent+'</span></div><span class="fpListPiont"></span>';
						   }
						   $('.mainer').append(htmlStr);
						   	if(nums <= pageNum){
								   $(".moreRecord").css('display','none'); 
								   $(".noneRecord").css('display','block'); 
							}
						}
					}
				});
				
				$(document).on('click','li > a',function(){
					var id = $(this).children().eq(0).text();
                    location.href = "http://www.17xs.org/user/detailInvoice.do?id="+id;
					
				});
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
							   var r = ret[0];
							   mobile = r.mobile ;
							   address = r.province + r.city + r.area + r.detailAddress;
							   $('#mobile').text(mobile);
							   $('#address').text(address);
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
						url: 'http://www.17xs.org/user/invoiceList.do',
						dataType: 'json',
						type: 'post',
						data: {
							pageNum: pageNum,
							page: page
						},
						success: function(data) {
						if(data.flag == 1){
							$('#page').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNum').text(pageNum);
						   var sum = page * pageNum;
						   var nums = data.obj.nums;
						   var total = data.obj.total;
						   var result = new Array;
						   var id = "";
						   var invoiceAmount = "";
						   var invoiceHead = "";
						   var state = "";
						   var stateContent = "";
						   var createTime = "";
						   var info = "";
						   var htmlStr = "";
						   var ret = data.obj.data;
						   for (var i in ret) {
							  var r = ret[i];
							  invoiceHead = r.invoiceHead;
							  title = r.title;
							  dMoney = r.dMoney;
							  createTime = r.createTime;
							  invoiceAmount = r.invoiceAmount;
							  state = r.state;
							  id = r.id;
							  if(state == '300'){
								  stateContent = '待开票';
							  }else{
								  stateContent = '已开票';
							  }
							  showTime = r.showTime;
							  htmlStr = htmlStr + '<li class="fpListItem"><a href="javascript:void(0);"><i style="display:none">'+id+'</i><div class="fpListTitle clearfix"><p>'+invoiceHead+'</p><span>¥'+invoiceAmount+'</span></div><div class="fpListState clearfix"><span class="fl" id="time">'+createTime+'</span><span class="fr" id="">'+stateContent+'</span></div><span class="fpListPiont"></span>';
						   }
						   $('.mainer').append(htmlStr);
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