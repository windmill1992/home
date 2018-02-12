<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" />
	<title>项目反馈</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=20171124">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectRelease.css?v=20171124">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectFeedback.css?v=20171124" />
</head>
<body>
	<div id="pageContainer">
		<div class="publish">
			<!--标题//-->
			<div class="top">
				<a href="javascript:history.go(-1);" class="back">返回</a>
				<div>近况反馈</div>
			</div>

			<textarea id="msg-text" placeholder="请输入求助人近期状况" class="fd-area"></textarea>
			<div class="upload-pic">
			 	<form id="form1" action="/file/upload3.do" method="post" enctype="multipart/form-data">	
					<div class="addList" id="imgList1">
						<div class="item add">
							<a class="add_btn" href="javascript:;">
								<label for="fileInput1"></label>
								<input type="file" name="file" hidefocus="true" id="fileInput1" class="file-input">
						    <input type="hidden" name="type" id="type" value="4">
							</a>
           				 </div>
			  		</div>
				</form> 	
		 	</div>
			<a href="javascript:;" class="pub_btn" id="msg-submit">提 交</a>
				<!--页脚
	      <div class="footer">
	      	<span>© 2015 杭州智善  版权所有</span>
	        <img src="images/min-logo.jpg">
	    	</div>-->
	   </div>
	</div>
	<div id="bigImg">
		<div class="bd"></div>
	</div>
	<div class="cue2" id="msg"></div>
	<input id="projectId" type="hidden" value="${projectId}">
	<input id="state" type="hidden" value="${state}">
	<span id="mediaId" style="display:none;"></span>
	
	<script src="/res/js/jquery-1.8.2.min.js" /></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="/res/js/util/ajaxform.js"></script>
	<script src="/res/js/h5/releaseProject/feedback.js?v=20171124"></script>
</body>
</html>