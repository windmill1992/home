<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>项目数据</title>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=20171201">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/project_initiator.css?v=20171206">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/initator_detal.css?v=20171206">
</head>
<body>
	<div id="pageContainer">
		<div id="pageScroll">
			<a class="inDetal_top" id="select" href="javascript:;">
			    <div class="detal_p"><p class="fl">${project.title }</p>&nbsp;<span class="detal_sp"></span></div>
			</a>
			<!--捐款-->
			<div class="inDetal_center">
			    <div class="det_top">
			        <p class="det_fl">捐款</p>
			        <p class="det_fr"><a href="/project/view_h5.do?projectId=${project.id}">看看明细</a></p>
			    </div>
			    <div class="det_center">
			        <ul class="det_ulone">
			            <li class="det_li">
			                <div class="detli_one">今日</div>
			                <p class="detli_two">${donate1.goodHelpCount }笔</p>
			                <p class="detli_two">${donate1.goodHelpAmount==null?0:donate1.goodHelpAmount }元</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">昨日</div>
			                <p class="detli_two">${donate2.goodHelpCount }笔</p>
			                <p class="detli_two">${donate2.goodHelpAmount ==null?0:donate2.goodHelpAmount}元</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">全部</div>
			                <p class="detli_two">${donate3.goodHelpCount }笔</p>
			                <p class="detli_two">${project.donatAmount==null?0:project.donatAmount }元</p>
			            </li>
			        </ul>
			    </div>
			</div>
			<!--访问-->
			<div class="inDetal_center">
			    <div class="det_top">
			        <p class="det_fl">访问</p>
			    </div>
			    <div class="det_center">
			        <ul class="det_ultwo">
			            <li class="det_li">
			                <div class="detli_one">访问量</div>
			                <p class="detli_two">${clickNum }人</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">捐款人数</div>
			                <p class="detli_two">${countUserNum }人</p>
			            </li>
			        </ul>
			    </div>
			</div>
			<!--评论-->
			<div class="inDetal_center">
			    <div class="det_top">
			        <p class="det_fl">评论</p>
			        <p class="det_fr">
			        	<a href="/uCenterProject/gotoCommentBatch.do?projectId=${project.id }">查看明细</a>
			        </p>
			        
			    </div>
			    <div class="det_center">
			        <ul class="det_ultwo">
			            <li class="det_li">
			                <div class="detli_one">收到</div>
			                <p class="detli_two">${totalLeaveNum }条</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">已回复</div>
			                <p class="detli_two">${leavedNum }条</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">未回复</div>
			                <p class="detli_two">${leaveNum }条</p>
			            </li>
			        </ul>
			    </div>
			</div>
			<!--参与-->
			<div class="inDetal_center">
			    <div class="det_top">
			        <p class="det_fl">参与</p>
			    </div>
			    <div class="det_center">
			        <ul class="det_ultwo">
			            <li class="det_li">
			                <div class="detli_one">转发</div>
			                <p class="detli_two">${shareNum }次</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">证实</div>
			                <p class="detli_two">${auditProjectCount }人</p>
			            </li>
			            <li class="det_li">
			                <div class="detli_one">报名</div>
			                <p class="detli_two">暂无</p>
			            </li>
			        </ul>
			    </div>
			</div>
		</div>
		
	</div>
	<!--弹层-->
	<a class="back1" href="javascript:;"></a>
	<div class="layer">
	    <div class="layer_detal">
	    	<div class="layer_scroll">
		    	<c:forEach items="${list}" var = "li" >
		    		<a href="/uCenterProject/gotoInitatorDetal.do?projectId=${li.id }">
		    			<div class="layer_li">${li.title }</div>
		    		</a>
		    	</c:forEach>
	    	</div>
	        <a href="javascript:;"><div class="layer_li" id="layer_qx">取消</div></a>
	    </div>
	</div>
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script>
		$(function() {
			var onOff = true;
			var $layer = $(".layer").find('.layer_detal');
			$layer.slideUp(0);
			$("#select").on('click',function(){
				if(onOff){
					$('.back1').fadeIn();
					$layer.slideDown();
					$('.detal_sp').addClass('up');
				}else{
					$('.back1').fadeOut();
					$layer.slideUp();
					$('.detal_sp').removeClass('up');
				}
				onOff = !onOff;
			});
			$('#layer_qx,.back1').on('click',function(){
				$('.back1').fadeOut();
				$layer.slideUp();
				$('.detal_sp').removeClass('up');
				onOff = true;
			});
		});
	</script>
</body>
</html>