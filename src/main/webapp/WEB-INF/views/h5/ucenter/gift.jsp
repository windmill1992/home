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
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<script src="http://apps.bdimg.com/libs/jquerymobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<!-- 头部 -->
	<%@ include file="./../common/head.jsp" %>
	<div class="wrapper">
		<div class="mainBox">
			<div class="mainTitle clearfix">
				<i class="function-icon icon4"></i>
				<h2>我的礼品</h2>
				<a href="javascript:;" class="moreInfo black">共有积分：<i id="" style="color: #f45156;">0</i></a>
			</div>
			<ul class="tabBox clearfix">
				<li class="stateItem w33 active">
					<p>可兑换</p>
					<span>${num}件</span>
				</li>
				<li class="stateItem w33" id="mark2">
					<p>已拥有</p>
					<span>${hasNum}件</span>
				</li>
				<li class="stateItem w33">
					<p>已失效</p>
					<span>${sxNum}件</span>
				</li>
			</ul>
		</div>
		<div class="tabInfo" id="tab1">
			<ul class="giftList">
			    <div class="mainerLeft"></div>
			</ul>
			<div style="display:none">
					 <span id="pageLeft"></span>
					 <span id="pageNumLeft"></span>
			</div>
			<div class="roadMoreBox" id="moreRecordLeft"><a href="javascript:;" class="loadMoreA" id="loadMoreALeft" style="color: #707070;">查看更多礼品</a></div>
			<div class="roadMoreBox" id="noneRecordLeft" style="display:none">没有更多了</div>
		</div>
		<div class="tabInfo" id="tab2" style="display: none;">
			<ul class="giftList">
                  <div class="mainerCenter"></div>
			</ul>
			<div style="display:none">
				 <span id="pageCenter"></span>
				 <span id="pageNumCenter"></span>
			</div>
			<div class="roadMoreBox" id="moreRecordCenter"><a href="javascript:;" class="loadMoreA" id="loadMoreACenter" style="color: #707070;">查看更多礼品</a></div>
			<div class="roadMoreBox" id="noneRecordCenter" style="display:none">没有更多了</div>
		</div>
		<div class="tabInfo" id="tab3" style="display: none;">
			<ul class="giftList">
                <div class="mainerRight"></div>
			</ul>
			<div style="display:none">
				 <span id="pageRight"></span>
				 <span id="pageNumRight"></span>
			</div>
			<div class="roadMoreBox" id="moreRecordRight"><a href="javascript:;" class="loadMoreA" id="loadMoreARight" style="color: #707070;">查看更多礼品</a></div>
			<div class="roadMoreBox" id="noneRecordRight" style="display:none">没有更多了</div>
		</div>
		<script type="text/javascript">
	
		$.ajax({
				url: 'http://www.17xs.org/user/userAddressList.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: 10,
					page: 1
				},
				success: function(data) {
					if(data.flag == 1){
					   var nums = data.obj.nums;
					   if(nums > 0){
						   ;
					   }else{
						   location.href = "http://www.17xs.org/user/toAddress.do?type=5";
					   }
					}
				}
		});
		$(document).ready(function(){
			//标签页点击
			$(".tabBox li").each(function(index,domEle){ 
				$(domEle).click(function(){	
					$(this).addClass("active").siblings().removeClass("active");
					$(".tabInfo").hide();		
					$("#tab"+Math.floor(index+1)).show();
				});
			});
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
			var mark = getParamString("mark");
			if(mark == 2){
				$("#mark2").addClass("active").siblings().removeClass("active");
				$(".tabInfo").hide();		
				$("#tab2").show();
			}
			//<!-- 可兑换start-->
			var pageNumLeft = 10;
			var pageLeft = 1;
			$.ajax({
				url: 'http://www.17xs.org/user/giftInfo.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNumLeft,
					page: pageLeft,
					type: 0
				},
				success: function(data) {
					//var str = JSON.stringify(data);
					//alert(str);
					   if(data.flag == 1){
					   var ret = data.obj.data;
					   var page = data.obj.page;
					   $('#pageLeft').text(page);
					   var pageNum = data.obj.pageNum;
					   $('#pageNumLeft').text(pageNum);
					   var coverImageUrl = '';
					   var giftName = '';
					   var content = '';
					   var htmlStr = '';
					   var id = '';
					   var nums = data.obj.nums;   
					   var pageNum = data.obj.pageNum;
					   var state = '';
					   var sendType = '';
					   var score = '';
					   var number = '';
					   for (var i in ret) {
						   var r = ret[i];
						   coverImageUrl = r.coverImageUrl;
						   content = r.content ;
						   content = cutstr(content,90);
						   giftName = r.giftName ;
						   id = r.id ;
						   sendType = r.sendType;
						   score = r.score;
						   number = r.number;
						   htmlStr = htmlStr + '<li class="giftItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+"_0"+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+giftName+'<span class="giftState">数量:'+number+'</span></div><p>'+content+'</p></div><span class="jfSum">积分：<i>'+score+'</i></span></a></li>';
							
					   }
					   $('.mainerLeft').append(htmlStr);
					   if(nums <= pageNum){
						   $("#moreRecordLeft").css('display','none'); 
						   $("#noneRecordLeft").css('display','block'); 
					   }
				   }else{
					   $("#moreRecordLeft").css('display','none'); 
					   $("#noneRecordLeft").css('display','block'); 
				   }
				   
				}					
			});
			
			$(document).on('tap','#loadMoreALeft',function(){
				var page = $("#pageLeft").text() ;
				var pageNum = $("#pageNumLeft").text();
				page = Number(page) + 1;
				$.ajax({
					url: 'http://www.17xs.org/user/giftInfo.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page,
						type: 0
					},
					success: function(data) {
						//var str = JSON.stringify(data);
				        //alert(str);
						   if(data.flag == 1){
						   $('#pageLeft').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNumLeft').text(pageNum);
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
						   var score = '';
						   var number = '';
						   for (var i in ret) {
							   var r = ret[i];
							   coverImageUrl = r.coverImageUrl;
							   content = r.content ;
							   content = cutstr(content,90);
							   giftName = r.giftName ;
							   id = r.id ;
							   sendType = r.sendType;
							   score = r.score;
							   number = r.number;
                               htmlStr = htmlStr + '<li class="giftItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+"_0"+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+giftName+'<span class="giftState">数量:'+number+'</span></div><p>'+content+'</p></div><span class="jfSum">积分：<i>'+score+'</i></span></a></li>';

						   }
						   $('.mainerLeft').append(htmlStr);
						   if(sum >= nums){
							   $("#moreRecordLeft").css('display','none'); 
							   $("#noneRecordLeft").css('display','block'); 
						   }
					   }else{
						   $("#moreRecordLeft").css('display','none'); 
						   $("#noneRecordLeft").css('display','block'); 
				       }
					  
					}					
				});
			});
			//<!-- 可兑换end-->
			//<!-- 已拥有start-->
			var pageNumCenter = 10;
			var pageCenter = 1;
			$.ajax({
				url: 'http://www.17xs.org/user/giftInfo.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNumCenter,
					page: pageCenter,
					type: 2
				},
				success: function(data) {
					//var str = JSON.stringify(data);
					//alert(str);
					   if(data.flag == 1){
					   var ret = data.obj.data;
					   var page = data.obj.page;
					   $('#pageCenter').text(page);
					   var pageNum = data.obj.pageNum;
					   $('#pageNumCenter').text(pageNum);
					   var coverImageUrl = '';
					   var giftName = '';
					   var content = '';
					   var htmlStr = '';
					   var id = '';
					   var nums = data.obj.nums;   
					   var pageNum = data.obj.pageNum;
					   var state = '';
					   var sendType = '';
					   var score = '';
					   var number = '';
					   for (var i in ret) {
						   var r = ret[i];
						   coverImageUrl = r.coverImageUrl;
						   content = r.content ;
						   content = cutstr(content,85);
						   giftName = r.giftName ;
						   id = r.id ;
						   sendType = r.sendType;
						   score = r.score;
						   number = r.number;
						   htmlStr = htmlStr + '<li class="giftItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+"_0"+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+giftName+'<span class="giftState">数量:'+number+'</span></div><p style="width:4rem">'+content+'</p></div><span class="jfSum">积分：<i>'+score+'</i></span></a></li>';
							
					   }
					   $('.mainerCenter').append(htmlStr);
					   if(nums <= pageNum){
						   $("#moreRecordCenter").css('display','none'); 
						   $("#noneRecordCenter").css('display','block'); 
					   }
				   }else{
					   $("#moreRecordCenter").css('display','none'); 
					   $("#noneRecordCenter").css('display','block'); 
				   }
				   
				}					
			});
			
			$(document).on('tap','#loadMoreACenter',function(){
				var page = $("#pageCenter").text() ;
				var pageNum = $("#pageNumCenter").text();
				page = Number(page) + 1;
				$.ajax({
					url: 'http://www.17xs.org/user/giftInfo.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page,
						type: 2
					},
					success: function(data) {
						//var str = JSON.stringify(data);
				        //alert(str);
						   if(data.flag == 1){
						   $('#pageCenter').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNumCenter').text(pageNum);
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
						   var score = '';
						   var number = '';
						   for (var i in ret) {
							   var r = ret[i];
							   coverImageUrl = r.coverImageUrl;
							   content = r.content ;
							   content = cutstr(content,85);
							   giftName = r.giftName ;
							   id = r.id ;
							   sendType = r.sendType;
							   score = r.score;
							   number = r.number;
                               htmlStr = htmlStr + '<li class="giftItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+"_0"+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+giftName+'<span class="giftState">数量:'+number+'</span></div><p>'+content+'</p></div><span class="jfSum">积分：<i>'+score+'</i></span></a></li>';

						   }
						   $('.mainerCenter').append(htmlStr);
						   if(sum >= nums){
							   $("#moreRecordCenter").css('display','none'); 
							   $("#noneRecordCenter").css('display','block'); 
						   }
					   }else{
						   $("#moreRecordCenter").css('display','none'); 
						   $("#noneRecordCenter").css('display','block'); 
				       }
					  
					}					
				});
			});
			//<!-- 已拥有end-->
			//<!-- 已失效start-->
			var pageNumRight = 10;
			var pageRight = 1;
			$.ajax({
				url: 'http://www.17xs.org/user/giftInfo.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNumRight,
					page: pageRight,
					type: 1
				},
				success: function(data) {
					//var str = JSON.stringify(data);
					//alert(str);
					   if(data.flag == 1){
					   var ret = data.obj.data;
					   var page = data.obj.page;
					   $('#pageRight').text(page);
					   var pageNum = data.obj.pageNum;
					   $('#pageNumRight').text(pageNum);
					   var coverImageUrl = '';
					   var giftName = '';
					   var content = '';
					   var htmlStr = '';
					   var id = '';
					   var nums = data.obj.nums;   
					   var pageNum = data.obj.pageNum;
					   var state = '';
					   var sendType = '';
					   var score = '';
					   var number = '';
					   for (var i in ret) {
						   var r = ret[i];
						   coverImageUrl = r.coverImageUrl;
						   content = r.content ;
						   content = cutstr(content,85);
						   giftName = r.giftName ;
						   id = r.id ;
						   sendType = r.sendType;
						   score = r.score;
						   number = r.number;
						   htmlStr = htmlStr + '<li class="giftItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+"_1"+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+giftName+'<span class="giftState">已过期</span></div><p>'+content+'</p></div><span class="jfSum">积分：<i>'+score+'</i></span></a></li>';
							
					   }
					   $('.mainerRight').append(htmlStr);
					   if(nums <= pageNum){
						   $("#moreRecordRight").css('display','none'); 
						   $("#noneRecordRight").css('display','block'); 
					   }
				   }else{
					   $("#moreRecordRight").css('display','none'); 
					   $("#noneRecordRight").css('display','block'); 
				   }
				   
				}					
			});
			
			$(document).on('tap','#loadMoreARight',function(){
				var page = $("#pageRight").text() ;
				var pageNum = $("#pageNumRight").text();
				page = Number(page) + 1;
				$.ajax({
					url: 'http://www.17xs.org/user/giftInfo.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page,
						type: 1
					},
					success: function(data) {
						//var str = JSON.stringify(data);
				        //alert(str);
						   if(data.flag == 1){
						   $('#pageRight').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNumRight').text(pageNum);
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
						   var score = '';
						   var number = '';
						   for (var i in ret) {
							   var r = ret[i];
							   coverImageUrl = r.coverImageUrl;
							   content = r.content ;
							   content = cutstr(content,85);
							   giftName = r.giftName ;
							   id = r.id ;
							   sendType = r.sendType;
							   score = r.score;
							   number = r.number;
                               htmlStr = htmlStr + '<li class="giftItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+id+"_1"+'</i><div class="fl"><img src="'+coverImageUrl+'"></div><div class="itemRight"><div class="itemTitle">'+giftName+'<span class="giftState">已过期</span></div><p>'+content+'</p></div><span class="jfSum">积分：<i>'+score+'</i></span></a></li>';

						   }
						   $('.mainerRight').append(htmlStr);
						   if(sum >= nums){
							   $("#moreRecordRight").css('display','none'); 
							   $("#noneRecordRight").css('display','block'); 
						   }
					   }else{
						   $("#moreRecordRight").css('display','none'); 
						   $("#noneRecordRight").css('display','block'); 
				       }
					  
					}					
				});
			});
			//<!-- 已失效end-->
			//<!-- 具体详情页start-->
			$(document).on('click','.giftList li > a',function(){
				var arr = new Array();
				var temp = $(this).children().eq(0).text();
				arr = temp.split("_");
				var id = arr[0];
				var shixiao = arr[1];
				location.href = "http://www.17xs.org/ucenter/exchangeGift.do?shixiao="+shixiao+"&id="+id;
				
			});			
			
		});
	</script>
	</div>
	<!-- 公共底部 -->
<%@ include file="./../common/footer.jsp" %>
</body>
</html>