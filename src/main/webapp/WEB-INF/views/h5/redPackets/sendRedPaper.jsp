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
	<link rel="stylesheet" type="text/css" href="/res/css/h5/userCenter.css?v=20171128">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/red_payment.css?v=20171128">
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background: #ce3a37">
	<div class="sendBox">
		<div class="redTicket">
			<c:if test="${ur.id == null }">
			 	<p class="sum">已抢完</p>
			 	<p class="source">该红包由<span>${ur.companyName }</span>提供</p>
			</c:if>
			<c:if test="${ur.status == 402 }">
			 	<p class="sum">${ur.amount }元已捐赠</p>
			 	<p class="source">该红包由<span>${ur.companyName }</span>提供</p>
			</c:if>
			<c:if test="${ur.id != null && ur.status == 401}">
				<p class="sum"><i><fmt:formatNumber type="number" value="${ur.amount }" maxFractionDigits="2"/></i>元</p>
				<p class="source">该红包由<span>${ur.companyName }</span>提供</p>
			</c:if>
			<c:if test="${ur.status == 403 }">
			 	<p class="sum">已失效</p>
			 	<p class="source">该红包由<span>${ur.companyName }</span>提供</p>
			</c:if>
		</div>
		<div class="redPaperBtn">
			<!-- 若已使用显示 已使用，未使用显示 立即使用 -->
			<c:if test="${ur.id == null || ur.status == 402 || ur.status == 403}">
				<a href="http://www.17xs.org/h5/" id="useBtn">我要行善</a>
			</c:if>
			<c:if test="${ur.id != null  && ur.status == 401}">
			<a href="http://www.17xs.org/h5/" id="useBtn">立即使用</a>
			<a href="javascript:;" id="donateBtn">随机捐助</a>
			</c:if>
		</div>
	<%--红包end--%>
	<div class="sendBoxend">
		<div class="redPaperBox">
			<%--<div class="title"><span>看看朋友手气如何</span></div>--%>
			<ul class="redPaperList">
				<!-- 数据 -->
			</ul>
			<div style="display:none">
			 <span id="pageRight"></span>
			 <span id="pageNumRight"></span>
			</div> 
			<div class="roadMoreBox" id="moreRecordRight">
				<a href="javascript:;" class="loadMoreA" id="loadMoreARight" style="color: #707070;">查看更多红包</a>
			</div>
			<div class="roadMoreBox" id="noneRecordRight" style="display:none">没有更多了</div>
		</div>
	</div>

	<!-- 提示信息 -->
	<div class="errorBox" style="display: none; top: 45%;">这是一个抢过的红包哟！</div>

	<!-- 大派送成功 -->
	<div class="popup" style="display: none;"></div>
	<div class="confirmBox" style="display: none;">
		<span class="close">x</span>
		<div class="confirmInner">
			<div class="top">
				<img src="${p.coverImageUrl }">
			</div>
			<div class="bottom">
				<h4 id="pTitle">${p.title }</h4>
				<p>${p.information }</p>
				<p class="hasDonate">已募集<i>${p.donatAmount }</i>元,共捐款<i>${p.donationNum }</i>人次</p>
			</div>
			<p class="otherInfo">为TA捐助<span><i>${ur.amount }</i>元</span>行善红包</p>
			<input type="button" value="确 定" class="btn" id="confirmBtn">
		</div>
	</div>
	<div class="rotate" style="display: none;">
		<img src="/res/images/h5/images/redPaper/turntable.gif">
	</div>
	<input type="hidden" id="extensionpeople" value="${ur.extensionpeople }">
	<input type="hidden" id="spare" value="${ur.spare }">
	<input type="hidden" id="projectId" value="${p.id }">
	<input type="hidden" id="uredId" value="${ur.id }">
	<input type="hidden" id="amount" value="${ur.amount }">
	<input type="hidden" id="redpacketsId" value="${redpacketsId}">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		wx.config({
			debug : false,
			appId : '${appId}',
			timestamp : '${timestamp}',
			nonceStr : '${noncestr}',
			signature : '${signature}',
			jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline' ]
		});
		wx.ready(function(){
			var redpacketsId = Number($('#redpacketsId').val());
			var linkUrl = "http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${ur.userId}&tradeNo=${tradeNo}&projectId=${projectId}" ;
			if(redpacketsId > 0){
				linkUrl = 'http://www.17xs.org/redPackets/getPersonRedPaket.do?id=${redpacketsId}';
			}
			wx.onMenuShareAppMessage({
				title : '${nickName}提着行善礼包来了，大家一起来抢红包做公益吧。', // 分享标题
				desc : '点击领取礼包，开启行善之路', // 分享描述
				link : linkUrl, // 分享链接
				imgUrl : 'http://www.17xs.org/res/images/h5/images/redPaper/redpacket.png', // 分享图标
				type : 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
				success : function() {
					//用户确认分享后执行的回调函数
					if(redpacketsId > 0){
						location.href = 'http://www.17xs.org/redPackets/myRedPackets.do';
					}else{
						location.href = 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${ur.userId}&tradeNo=${tradeNo}&projectId=${projectId}';
					}
				},
				cancel : function() {
					//用户取消分享后执行的回调函数
				}
			});
			wx.onMenuShareTimeline({
				title : '${nickName}提着行善礼包来了，大家一起来抢红包做公益吧。', // 分享标题
				link : linkUrl, // 分享链接
				imgUrl : 'http://www.17xs.org/res/images/h5/images/redPaper/redpacket.png', // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
					if(redpacketsId > 0){
						location.href = 'http://www.17xs.org/redPackets/myRedPackets.do';
					}else{
						location.href = 'http://www.17xs.org/redPackets/getRedPaket.do?extensionpeople=${ur.userId}&tradeNo=${tradeNo}&projectId=${projectId}';
					}
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
		});
	</script>
	<script type="text/javascript">
		$(function(){
			// 点击随机捐赠
			$("#donateBtn").click(function(){
				$(".popup, .rotate").show();
				setTimeout(function(){
					$(".rotate").css("display", "none");
					$(".confirmBox").css("display", "block");
				}, 2000);
			});
			
			//大派送关闭按钮
			$(".close").click(function(){
				$(".popup, .confirmBox").hide();
				var id = $("#uredId").val();
				location.href='http://www.17xs.org/redPackets/sendRedPaper.do?id='+id;
			});

			// 大派送成功，点击确定
			$("#confirmBtn").click(function(){
				$(".popup, .confirmBox").hide();
				var title = $("#pTitle").text(),money =$("#amount").val(),
				uredId=$("#uredId").val(),projectId = $("#projectId").val(),uredId=$("#uredId").val();
				$.ajax( {
					url: 'http://www.17xs.org/redPackets/payRedpacket.do',
					dataType: 'json',
					type: 'post',
					data: {uredId:uredId,projectId:projectId,t:new Date().getTime()},
					success : function(result) { 
						if(result.flag==1){
							location.href="http://www.17xs.org/project/paysuccess_h5/?pName="
								+title+"&uredId="+uredId+"&amount="+money+"&projectId="+projectId+"&red=1&tradeNo="+result.obj.data;
						}else{
							alert("失败");
						}
					}
				});
			});
			
			//<!-- 朋友手气start-->
			var pageNumLeft = 10;
			var pageLeft = 1;
			var extensionpeople= $("#extensionpeople").val();
			var spare = $("#spare").val();
			var redpacketsId = $('#redpacketsId').val();
			$.ajax({
				url: 'http://www.17xs.org/redPackets/redPaperList.do',
				dataType: 'json',
				type: 'post',
				data: {
					pageNum: pageNumLeft,
					page: pageLeft,
					extensionpeople:extensionpeople,
					spare:spare,
					redpacketsId:redpacketsId
				},
				success: function(data) {
					if(data.flag == 1){
					   var ret = data.obj.data;
					   var page = data.obj.page;
					   $('#pageRight').text(page);
					   var pageNum = data.obj.pageNum;
					   $('#pageNumRight').text(pageNum);
					   var htmlStr = '';
					   var nums = data.obj.nums;   
					   var pageNum = data.obj.pageNum;
					   for (var i in ret) {
						   var r = ret[i];
						   amount = r.amount ;
						   nickName = r.nickName ;
						   id = r.id ;
						   if(r.headImageUrl != null){
						   		headImageUrl=r.headImageUrl;
						   }else{
						   		headImageUrl="http://www.17xs.org/res/images/user/user-icon-gt.png";
						   }
						   htmlStr = htmlStr + '<li><img src="'+headImageUrl+'"><span>'+nickName+'</span><span class="money">'+amount+'元</span></li>';
					   }
					   $('.redPaperList').append(htmlStr);
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
			
			$(document).on('click','#loadMoreARight',function(){
				var extensionpeople= $("#extensionpeople").val();
				var spare = $("#spare").val();
				var page = $("#pageRight").text() ;
				var pageNum = $("#pageNumRight").text();
				page = Number(page) + 1;
				var redpacketsId = $('#redpacketsId').val();
				$.ajax({
					url: 'http://www.17xs.org/redPackets/redPaperList.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageNum,
						page: page,
						extensionpeople:extensionpeople,
						spare:spare,
						redpacketsId:redpacketsId
					},
					success: function(data) {
						   if(data.flag == 1){
						   $('#pageRight').text(page);
						   var pageNum = data.obj.pageNum;
						   $('#pageNumRight').text(pageNum);
						   var sum = page * pageNum;
						   var ret = data.obj.data;
						   var htmlStr = '';
						   var nums = data.obj.nums;   
						   var pageNum = data.obj.pageNum;
						   for (var i in ret) {
							   var r = ret[i];
							   amount = r.amount ;
							   nickName = r.nickName ;
							   id = r.id ;
							   if(r.headImageUrl != null){
						   			headImageUrl=r.headImageUrl;
							   }else{
							   		headImageUrl="http://www.17xs.org/res/images/user/user-icon-gt.png";
							   }
                               htmlStr = htmlStr + '<li><img src="'+headImageUrl+'"><span>'+nickName+'</span><span class="money">'+amount+'元</span></li>';
						   }
						   $('.redPaperList').append(htmlStr);
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
			//<!-- 朋友手气end-->
		});
	</script>
	<c:if test="${type == 110}">
		 <div class="red_fx" style="display:block" id="sharePersonRed">
		    <div class="cue_background"></div>
		    <div class="red_box">
		        <p>点击右上角，分享红包</p>
		    <img src="/res/images/h5/images/redPaper/jiantou.png" class="red_icon1" />
		    </div>
		  </div>
	</c:if>
</body>
</html>