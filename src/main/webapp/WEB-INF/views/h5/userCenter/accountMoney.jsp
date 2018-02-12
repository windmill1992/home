<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="UTF-8">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<title>收款账号</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=20171127">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/aided_info.css?v=20171127">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectRelease.css?v=20171127">
</head>
<style type="text/css">
	.addList .item{
		float: left;margin-right: 20px;
	}
</style>
<body>
	<div class="header">
			<a href="javascript:history.go(-1);" class="back">返回</a>
		<div>打款金额</div>
	</div>
	<div class="bind_b1">
		<div class="bind_fr1 release_new">
			<input type="number" placeholder="请输入打款金额：" class="release_new" id="panMoney">
		</div>
	</div>
	<div class="bind_b1">
		<div class="bind_fr1 release_new">
			<input type="text" placeholder="" class="release_new" id="money" value="当前可提现：${money }元" readonly="true">
		</div>
	</div>
	<p class="form_text">请上传收据或发票。<a href="/resposibilityReport/resposibilityReport.do?id=406" class="underline">查看样例</a></p>
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
		
	<div class="foot_qt">
		<a href="javascript:void(0);">确认提交</a>
	</div>
	<div id="bigImg">
		<div class="bd"></div>
	</div>
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" id="tixian" value="${money}" />
	<input type="hidden" id="projectId" value="${projectId }">
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="/res/js/util/ajaxform.js"></script>
	<script src="/res/js/h5/releaseProject/accountMoney.js?v=20171128"></script>
</body>

</html>