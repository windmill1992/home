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
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20171128">
<link rel="stylesheet" type="text/css" href="/res/css/h5/red_payment.css?v=20171128">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="red">
		<div class="header">
			<a href="/redPackets/myRedPackets.do" class="back">返回</a>
			<div>我收到的红包</div>
		</div>
	</div>
	<ul class="tabBox">
		<li class="active" data-state="401"><a href="javascript:;">未使用</a></li>
		<li data-state="402"><a href="javascript:;">已使用</a></li>
		<li data-state="403"><a href="javascript:;">已过期</a></li>
	</ul>
	<div class="tabWrap" id="tab1">
		<ul id="redPaperList"></ul>
		<div class="roadMoreBox">
			<a href="javascript:;" id="loadMoreA">查看更多红包</a>
		</div>
	</div>
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript">
		$(function(){
			var page = 1, state = '401';
			getRedPaperList(page,10,state);
			$(".tabBox li").on('click',function(){
				state = $(this).attr('data-state');
				page = 1;
				$('#redPaperList').html('');
				$(this).addClass("active").siblings().removeClass("active");
				getRedPaperList(page,10,state);
			});
			$('#loadMoreA').on('click',function(){
				if($(this).hasClass('no'))return;
				getRedPaperList(++page,10,state);
			})
			function getRedPaperList(pageNum,pageSize,state){
				$.ajax({
					url: 'http://www.17xs.org/redPackets/redPaper.do',
					dataType: 'json',
					type: 'post',
					data: {
						pageNum: pageSize,
						page: pageNum,
						status: state
					},
					success: function(data) {
						if(data.flag == 1){
							var ret = data.obj.data,
								html = [];
								total = data.obj.nums;  
							if(state == '401'){
								for (var i in ret) {
									var r = ret[i],
									detailUrl="/redPackets/sendRedPaper.do?id=" + r.id;
									if(r.appointProject == 'person'){
								   		detailUrl = detailUrl + "&redpacketsId=" + r.redpackets_id;
									}
									html.push('<a href="' + detailUrl + '" class="left"><li>');
									html.push('<h3>“' + r.companyName + '”赞助</h3><p>有效期：' + r.showDate + '</p>');
									html.push('<span class="money">¥ '+ r.amount+'元</span></li></a>');
							 	}	
							}else if(state == '402'){
								for (var i in ret) {
									var r = ret[i];
									html.push('<a href="javascript:;" class="center"><li><div class="redPaperLeft">');
									html.push('<img src="' + r.coverImageUrl + '"></div><div class="redPaperRight">');
									html.push('<h3>' + r.pTitle + '</h3><p><span>献出<i>' + r.amount + '</i>元红包</span>');
									html.push('<span class="fr date">' + r.showDate + '</span></p></div></li></a>');
								}
							}else if(state == '403'){
								for (var i in ret) {
									var r = ret[i];
									html.push('<a href="javascript:;" class="right"><li><h3>“' + r.companyName + '”赞助</h3><p>已过期</p>');
									html.push('<span class="money">¥ ' + r.amount + '元</span></li></a>');
								}
							}
							$('#redPaperList').append(html.join(''));
							if(total > 0 && total <= pageNum * pageSize){
								$("#loadMoreA").text('没有更多数据了').addClass('no'); 
							}else if(total == 0){
						 		$("#loadMoreA").text('暂无红包数据！').addClass('no');
							}else{
								$('#loadMoreA').html('查看更多红包！').removeClass('no');
							}
						}else{
							$("#loadMoreA").text('暂无红包数据！').addClass('no');
					 	}
					}					
				});	
			}
		});
	</script>
</body>
</html>