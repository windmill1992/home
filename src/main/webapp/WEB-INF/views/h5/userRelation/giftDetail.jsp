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
	<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="container">
	<div class="jkHeadBox">
		<div class="userLogo">
			<img src="" id="headImageUrl"></img>
		</div>
		<div class="userInfo hasInfo">
			<h2 class="name" id="nickName"></h2>
			<p class="spec phone" id="mobile"></p>
			<p class="address" id="address"></p>
			<p id="addressId" style="display:none"></p>
		</div>
	</div>
		<div class="jkMain02Box">
			<div class="jkMainTitle">
				<i class="gift"></i>选择礼品
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
					   <a href="javascript:;" class="loadMoreA" id="loadMoreA" style="color: #707070;">查看更多礼品</a>
					 </div>
					<div class="noneRecord" style="display:none">
					<span class="loadMoreA" style="color: #707070;">更多礼品尽请期待</span></div>
				</div>
			</ul>
		</div>
		<div class="fpSumBox">
		</div>
		<div class="btnGroup">
			<input type="button" id="submitId" value="提&nbsp;&nbsp;交" class="submitBtn">
		</div>
		<div class="giftId_f" id="giftId_f" style="display:none"></div>
		<div class="giftName_f" id="giftName_f" style="display:none"></div>
		<div class="sendType_f" id="sendType_f" style="display:none"></div>
		<div class="errorBox" style="display: none;"></div>
		<div class="payTips" id="payTips" style="display: none;">
			<div class="investTip2">领取成功</div>
		</div>
	</div>
		 <script type="text/javascript">
		function codefans(){

           $('.errorBox').css('display','none');
        }
	    $(function () {
			        var seeGift = 0;
				    var pageNum = 10;
					var page = 1;
					$.ajax({
						url: 'http://www.17xs.org/user/userAddressList.do',
						dataType: 'json',
						type: 'post',
						data: {
							pageNum: pageNum,
							page: page
						},
						success: function(data) {
							if(data.flag == 1){
							   var nums = data.obj.nums;
                               if(nums > 0){
                            	   ;
                               }else{
                            	   location.href = "http://www.17xs.org/user/toAddress.do?type="+'2';
                               }
							}
						}
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
						   var addressId = '';
						   for (var i in ret) {
							   var r = ret[0];
							   mobile = r.mobile ;
							   address = r.province + r.city + r.area + r.detailAddress;
							   addressId = r.id ;
							   $('#mobile').text(mobile);
							   $('#address').text(address);
							   $('#addressId').text(addressId);
						   }

					   }
					}					
				});
				
				var pageNum = 10;
				var page = 1;
				$.ajax({
					url: 'http://www.17xs.org/user/giftInfo.do',
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
						   var coverImageUrl = '';
						   var giftName = '';
						   var content = '';
						   var htmlStr = '';
						   var id = '';
						   var nums = data.obj.nums;
						   if(nums == 0){
							   $("#submitId").val("查看礼物");
							   seeGift = 1;
						   }    
						   var pageNum = data.obj.pageNum;
						   var state = '';
						   var sendType = '';
						   for (var i in ret) {
							   var r = ret[i];
							   coverImageUrl = r.coverImageUrl;
							   content = r.content ;
							   giftName = r.giftName ;
							   id = r.id ;
							   sendType = r.sendType;
                              // htmlStr = htmlStr + '<li class="giftListItem clearfix"><span id="gift" class="checkItem"></span><i style="display:none">'+id+'|'+giftName+'</i><p style="display:none">'+sendType+'</p><div class="giftItemImg"><img src="'+coverImageUrl+'"></div><div class="giftRight"><h3 class="giftName">'+giftName+'<span class="giftState">待领取</span></h3><p>'+content+'</p></div></li>';
						 		htmlStr = htmlStr + '<li class="giftListItem clearfix"><span id="gift" class="checkItem"></span><i style="display:none"><span id="gId" style="display:none">'+id+'</span><span id="giftName" style="display:none">'+giftName+'</span></i><p style="display:none"><span id="sendType" style="display:none">'+sendType+'</span></p><div class="giftItemImg"><img src="'+coverImageUrl+'"></div><div class="giftRight"><h3 class="giftName">'+giftName+'<span class="giftState">待领取</span></h3><p>'+content+'</p></div></li>';
						 		
						   }
						   //alert(htmlStr);
						   $('.mainer').append(htmlStr);
						   //alert(nums);
						   if(nums <= pageNum){
							   $(".moreRecord").css('display','none'); 
							   $(".noneRecord").css('display','block'); 
						   }
					   }else{
						       $("#submitId").val("查看礼物");
							   seeGift = 1;
						   	   $(".moreRecord").css('display','none'); 
							   $(".noneRecord").css('display','block'); 
					   }
					   
					}					
				});
				$(document).on('touchend click','#loadMoreA',function(){
					var nums = $("#nums").text() ;
					var page = $("#page").text() ;
					var pageNum = $("#pageNum").text();
					page = Number(page) + 1;
					$('#page').text(page);
									$.ajax({
					url: 'http://www.17xs.org/user/giftInfo.do',
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
						   var coverImageUrl = '';
						   var giftName = '';
						   var content = '';
						   var htmlStr = '';
						   var id = '';
						   var nums = data.obj.nums;   
						   var pageNum = data.obj.pageNum;
						   var state = '';
						   var sendType = '';
						   for (var i in ret) {
							   var r = ret[i];
							   coverImageUrl = r.coverImageUrl;
							   content = r.content ;
							   giftName = r.giftName ;
							   id = r.id ;
							   sendType = r.sendType;
                               htmlStr = htmlStr + '<li class="giftListItem clearfix"><span class="checkItem no"></span><i style="display:none">'+id+'|'+giftName+'</i><p style="display:none">'+sendType+'</p><div class="giftItemImg"><img src="'+coverImageUrl+'"></div><div class="giftRight"><h3 class="giftName">'+giftName+'<span class="giftState">待领取</span></h3><p>'+content+'</p></div></li>';
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
				$(document).on('touchend click','li > span',function(){
					var cssAttr = $(this).attr('class');
					 if(cssAttr == 'checkItem'){
						arr = new Array();
						$(this).removeClass("no");
						var temp = $(this).next("i").text();
					    arr = temp.split('|');
						$('#giftId_f').text(arr[0]);
						$('#giftName_f').text(arr[1]);
						$('#sendType_f').text($(this).next().next("p").text());
						$(this).parent().siblings().children().addClass("no");
					}
					/*
                    if(cssAttr != 'checkItem'){
						arr = new Array();
						$(this).removeClass("no");
						var temp = $(this).next("i").text();
					    arr = temp.split('|');
						$('#giftId_f').text(arr[0]);
						$('#giftName_f').text(arr[1]);
						$('#sendType_f').text($(this).next().next("p").text());
						$(this).parent().siblings().children().addClass("no");
					}else{
						$(this).addClass("no");
						$('#giftId_f').text('');
						$('#giftName_f').text('');
						$('#sendType_f').text('');
						$(this).parent().siblings().children().removeClass("no");
					}
					*/
					
				});
				
				$(document).on('click','.submitBtn',function(){
				/*
					var giftId = $("#giftId_f").text() ;
					var giftName = $("#giftName_f").text() ;
                    var sendType = $("#sendType_f").text() ;
                */
               		var giftId = $("#gId").text() ;
					var giftName = $("#giftName").text() ;
                    var sendType = $("#sendType").text() ;
                 
					//alert(giftId);
					if(seeGift == 1){
						location.href = "http://www.17xs.org/user/giftList.do";
						return;
					}
					if(giftId == ''){
						$('.errorBox').html('请选择一件礼物！');
						$('.errorBox').css('display','block');
						setTimeout("codefans()",2000);//2秒
						return;
					}					
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
								window.location.href="http://www.17xs.org/user/giftList.do";
								},1000);
								  //sleep(2);//2秒
						   }
						}					
					});
					
				});
		});
		
		
		
	</script>
</body>
</html>