<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" /> 
<title>我的报名</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/specialProject/fuwa-fund.css?v=20171130"/> 
<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript">
	$(function(){
		if(${num==0}){
			$('.load-more2').html("暂无报名数据！");
		}
	});
</script>
</head>
<body style="background: #f1f1f1;">
	<div class="entry_head">
		我的报名
		<a href="/ucenter/userCenter_h5.do" class="back">用户中心</a>
	</div>
	
	<div class="entry_list">
		<div class="entry_project">
			<ul>
			<c:forEach items="${list }" var="list">
				<a href="/resposibilityReport/entryForm_detail.do?id=${list.id }"><li>
					<div class="project_title">
						<p>${list.title }</p>
					</div>
					<div class="project_state">
						<span class="fl time"><fmt:formatDate value="${list.createTime }" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
					</div>
				</li></a>
			</c:forEach>
			</ul>
		</div>
	</div>
	<a class="load-more2" href="javascript:void(0);"></a>
	<input type="hidden" id="state" value="${state }"/>
</body>
</html>