<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta charset="UTF-8">  
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>批量评论</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/project_initiator.css?v=20171206">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/comment_batch.css?v=20171206">
</head>
<body>
	<div id="pageContainer">
		<div class="batch_top">
		    <div>收到评论</div>
		    <div class="batch_fr">
		    	<div class="batch_in">
		    		<input type="checkbox" name="selAll" id="selAll" class="duihao" />
		    	</div>
		    	<label for="selAll">全选</label>
			</div>
		</div>
		<div class="initator_nav flex spb">
	        <a href="javascript:;" class="tor_a ligg" data-state="0">全部</a>
	        <a href="javascript:;" class="tor_a" data-state="1">已回复</a>
	        <a href="javascript:;" class="tor_a tor_lig" data-state="2">未回复</a>
		</div>
		<div class="batch_center" id="batchList"></div>
		<!--加载 点击加载更多-->
		
		<a href="javascript:;" class="jiazai no">暂无数据！</a>
		<div class="batch_btn">
		    <!--<a href="javascript:;"><div class="btn_f yidu">标记已读</div></a>-->
		    <a href="javascript:;"><div class="btn_f batch">批量回复</div></a>
		</div>
		
		<!--回复留言-->
		<div class="moved_up" style="display:none">
		    <div class="back"></div>
		    <div class="end_center">
		        <div class="end_hfk">
		            <span class="end_r">回复：</span>
		            <textarea class="end_text" cols="45" rows="7" id="batchContent"></textarea>
		        </div>
		        <div class="end_foot">
		            <a href="javascript:;">
		                <div class="end_fl1 fl" id="cancel">取消</div>
		            </a>
		            <a href="javascript:;">
		                <div class="end_fl1 fr" id="confirm">确定</div>
		            </a>
		        </div>
		    </div>
		</div>
		
	</div>
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" id="mobile" value="${mobile }"/>
	<input type="hidden" id="projectId" value="${projectId }"/>
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/h5/releaseProject/commentBatch.js?v=20171206"></script> 
</body>
</html>