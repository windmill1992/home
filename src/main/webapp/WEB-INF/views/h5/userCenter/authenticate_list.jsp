<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@ include file="./../../common/file_url.jsp"%>
	<title>证实列表</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=20180125">
</head>
<style type="text/css">
	#pageScroll::-webkit-scrollbar {
		display: none;
	}
	#more{
		background: #fff;
	}
</style>

<body>
	<div id="pageScroll">
		<div class="detail_head">
			<a href="javascript:history.go(-1);" class="to-index">返回</a>
			<div class="detailconter">
				<img src="/res/images/releaseProject/det_log.png"> <span>善园网</span>
			</div>
			<a href="javascript:;" class="detailfr">
				<img src="/res/images/releaseProject/det_pr.png"> <span>我要证实</span>
			</a>
		</div>
		<div class="authenticate_top">
			有<span>${auditProjectCount }</span>人实名证实
		</div>
		<div id="add">
			<c:forEach items="${auditProjectList }" var="ap">
				<div class="section_center">
					<input type="hidden" name="count" />
					<div class="section_fl">
						<img src="${ap.headUrl }">
					</div>
					<div class="section_fr">
						<p class="autFl">${ap.realName }</p>
						<div style="clear: both"></div>
						<p class="guanxi">${ap.relationship!=''?ap.relationship:'爱心人士' }</p>
						<p class="center_text">${ap.information }</p>
					</div>
					<div style="clear: both"></div>
				</div>
			</c:forEach>
		</div>
		<a class="jiazai" href="javascript:;" id="more">点击加载更多</a>
	</div>

	<input type="hidden" value="${projectId}" id="projectId">
	<input type="hidden" value="${auditProjectTips}" id="tips">
	<input type="hidden" value="${total }" id="total">
	<input type="hidden" value="${type }" id="type">

	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="/res/layer/layer.js"></script>
	<script type="text/javascript">
		$(function() {
			var page = 1, onOff = true;
			var tips = $("#tips").val();
			var total = $("#total").val();
			var counts = $('input[name="count"]');
			if(total > counts.length) {
				$("#more").html('上拉加载更多');
			} else {
				$("#more").html('没有更多数据了').css({color:'#999'});
				onOff = false;
			}
			if(tips == 1) {
				layer.msg('暂无数据！');
			}
			var projectId = $("#projectId").val();
			$('#more').click(function() {
				if($(this).html() == '没有更多数据了') return false;
				$('#more').html('<img src="/res/images/h5/images/loading.gif" />正在加载...');
				setTimeout(function(){
					onOff = false;
					getConfirmPeople(++page);
				},400);
			});
			
			pageScroll.addEventListener('scroll',function(){
				if(onOff){
					var st = this.scrollTop,
						h = $(this).height(),
						sh = this.scrollHeight - 65;
					if(st + h > sh){
						$('#more').html('<img src="/res/images/h5/images/loading.gif" />正在加载...');
						onOff = false;
						setTimeout(function(){
							getConfirmPeople(++page);
						},400);
					}
				}
			},false);
			function getConfirmPeople(pageNum){
				$.ajax({
					type:"get",
					url:"http://www.17xs.org/uCenterProject/lookMoreConfirmPeople.do",
					data:{
						projectId:projectId,
						currentPage:pageNum
					},
					success:function(res){
						var html = [];
						if(res.result == 0) {
							var data = res.items,
								datas = data,
								len = data.length;
							for(var i = 0; i < len; i++) {
								html.push('<div class="section_center">');
								html.push('<input type="hidden" name="count" />');
								html.push('<div class="section_fl">');
								html.push('<img src="' + datas[i].headUrl + '">');
								html.push('</div>');
								html.push('<div class="section_fr">');
								html.push('<p class="autFl">' + datas[i].realName + '</p>');
								html.push('<div style="clear: both"></div>');
								if(datas[i].relationship == null) {
									datas[i].relationship = '爱心人士';
								}
								html.push('<p class="guanxi">' + datas[i].relationship + '</p>');
								html.push('<p class="center_text">' + datas[i].information + '</p>');
								html.push('</div>');
								html.push('<div style="clear: both"></div>');
								html.push('</div>');
							}
							$('#add').append(html.join(''));
							counts = $('input[name="count"]');
							if(total > counts.length) {
								$("#more").html('上拉加载更多');
								onOff = true;
							} else {
								$("#more").html('没有更多数据了').css({color:'#999'});
								onOff = false;
							}
						} else {
							layer.msg('没有更多数据了！');
							$("#more").html('没有更多数据了').css({color:'#999'});
							onOff = false;
						}
					}
				});
			}
			$(".detailfr").click(function() {
				var type = $('#type').val();
				$.ajax({
					url: 'http://www.17xs.org/uCenterProject/isOrNotAuditProject.do',
					async: false,
					data: {
						projectId: projectId,
						type: type
					},
					success: function(result) {
						if(result.flag == 1) {
							//已经证实
							layer.msg('您已证实过了，不需要重新证实！');
						} else if(result.flag == 0) {
							location.href = result.errorMsg;
						} else if(result.flag == -2) {
							//未登录,先登录
							location.href = result.errorMsg;
						} else if(result.flag == -1) {
							location.href = result.errorMsg;
						}
					},
					error: function() {
						layer.msg('服务器异常，请联系客服！');
					}
				});
			});
		});
	</script>
</body>
</html>