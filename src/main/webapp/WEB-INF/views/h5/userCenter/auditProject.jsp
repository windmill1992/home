<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>填写信息</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=201712211200" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=201712211200">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/personRelease.css?v=201712211200">
</head>
<body>
	<div class="detail_head2">
	    <a href="javascript:;" onclick="history.go(-1);" class="back">返回</a>
	    <div>我要证实</div>
	</div>
	<div class="person_text">感谢您的热心，如果您参与、探访、深入了解过此微爱通道项目，希望您如实填写以下真实身份、真实情况，这将为受助人赢取更多信任。</div>
	<p class="person_text1"></p>
	<input type="text" class="person_sel"  placeholder="您与受助人的关系(亲属/朋友/同学/同事/邻居等)"/>
	<input placeholder="填写你的姓名" type="text" class="person_input" id="realName" value="${user.realName }">
	<input placeholder="填写你的身份证" type="text" class="person_input" id="idCard" value="${user.idCard }">
	<textarea placeholder="请介绍一些详细的情况" rows="3" style="line-height: 20px;padding:10px 3%;" class="person_input" id="information"></textarea>
	<div class="phoneTest">
	    <input placeholder="手机号" type="text" class="person_input" id="mobileNum" value="${user.mobileNum }">
	    <div class="test_yzm">
	        <input placeholder="验证码" type="text" class="person_input1">
	        <a class="fsyz" href="javascript:;">发送验证码</a>
	    </div>
	</div>
	<a class="footer_btn disabled" href="javascript:;">提交</a>
	
	<div class="dialog" id="yzmDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h3>请输入验证码</h3>
			<div class="yzm-pic">
				<img src="" id="codepic"/>
				<a href="javascript:void(0);" class="refreshCode"></a>
				<input type="number" name="yzmCode" id="yzmCode" maxlength="4" value="" />
			</div>
			<div class="close">
				<a href="javascript:void(0);" class="close-a left" id="cancel">取消</a>
				<a href="javascript:void(0);" class="close-a right" id="sure">确定</a>
			</div>
		</div>
	</div>
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input id="projectId" type="hidden" value="${projectId}">
	<input id="type" type="hidden" value="${type}">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/h5/releaseProject2/card.js"></script>
	<script src="/res/js/h5/releaseProject/auditProject.js?v=201712211200"></script>
</body>
</html>