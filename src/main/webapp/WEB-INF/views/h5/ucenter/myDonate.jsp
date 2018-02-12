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
	<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20171212">
	<script src="http://libs.baidu.com/jquery/2.0.0/jquery.js"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<div class="topBg">
		<a href="javascript:;" class="topBack" onclick="history.go(-1);"></a>捐赠记录
	</div>
	<ul id="donateList"></ul>
	<div class="roadMoreBox">
		<a href="javascript:;" class="loadMoreA" id="loadMoreA">查看更多记录</a>
	</div>
	<input type="hidden" id="userId" value="${user.id}">
	<input type="hidden" id="extensionPeople" value="${extensionPeople }">
	
	<script type="text/javascript">
		var page = 1;
		var donationRecord = {
			init:function(){
				var that = this;
				that.getDonationList(page,10);
				
				$('body').on('click','#donateList a',function(){
					var arr = [],
						temp = $(this).children().eq(0).text(),
						exp = $("#extensionPeople").val();
					arr = temp.split("_");
					var id = arr[0],
						field = arr[1];
					if('garden' == field){
						location.href = "/project/gardenview_h5.do?projectId="+ id +"&extensionPeople="+ exp;
					}else{
						location.href = "/project/view_h5.do?projectId="+ id +"&extensionPeople="+ exp;
					}
				}).on('click','#loadMoreA',function(){
					if($(this).hasClass('no')) return;
					that.getDonationList(++page,10);
				});
			},
			getDonationList:function(pageNum,pageSize){
				var that = this,
					userId = $('#userId').val();
				$.ajax({
					url: 'http://www.17xs.org/project/donationlist_h5.do',
					dataType: 'json',
					type: 'get',
					data: {
						pageNum: pageSize,
						page: pageNum,
						userId: userId
					},
					success: function(data) {
						if(data.flag == 1){
							var ret = data.obj.data,
								htmlStr = '',
						   		nums = data.obj.nums;   
							for (var i in ret) {
								var r = ret[i];
								if(r.imagesurl == null || r.imagesurl == ''){
									r.imagesurl = '/res/images/logo-def.jpg';
								}
								htmlStr = htmlStr + '<li class="donateItem clearfix"><a href="javascript:void(0);"><i style="display:none">'+
							   		r.pid +'_'+ r.field +'</i><div class="donateLeft"><img src="'+ r.imagesurl +'"></div><div class="donateRight"><h2 class="title">'+
							   		r.title +'</h2><p>捐赠次数：<span>'+ r.donateNum +'</span>次</p><p>已捐金额：<span>'+ r.dMoney +'</span>元</p></div></a></li>';
							}
							$('#donateList').append(htmlStr);
							if(nums == 0){
						   		$('#loadMoreA').text('暂无捐赠记录，快去帮助他们吧~').addClass('no');
							}else if(nums <= pageNum * page){
								$('#loadMoreA').text('没有更多数据了~').addClass('no');
							}else{
								$('#loadMoreA').text('查看更多记录').removeClass('no');
							}
						}else{
							$('#loadMoreA').text('暂无捐赠记录，快去帮助他们吧~').addClass('no');
						}
					}					
				});
			}
		};
	    $(function() {
			donationRecord.init();
		});
	</script>
</body>
</html>