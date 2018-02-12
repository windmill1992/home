<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>加入善库</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/basic.css?v=201712211600" />
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/bind.css?v=201712211600">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/feedback.css?v=201712211600">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/register.css?v=201712211600">
	<title>h5绑定页面</title>
</head>
<body>
	<div class="feed">
		<div class="header">
			<a href="javascript:history.go(-1)" class="back">返回</a>
			<div>完善信息</div>
		</div>
		<section>
			<div class="bind_b1">
				<div class="bind_fl1"> <img src="<%=resource_url%>res/images/goodlibrary/zh.png"> </div>
				<div class="bind_fr1"> <input type="text" placeholder="请输入您的姓名" id="relName"> </div>
			</div>
			<div class="bind_b1">
				<div class="bind_fl1"> <img src="<%=resource_url%>res/images/goodlibrary/sj1.png"> </div>
				<div class="bind_fr1"> 
					<input type="text" placeholder="请输入手机号" id="phone" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)">
					<a href="javascript:;" class="fsyz" id="pSend">发送验证码</a>
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fl1"> <img src="<%=resource_url%>res/images/goodlibrary/sj.png"> </div>
				<div class="bind_fr1"> <input type="text" placeholder="请输入手机验证码" id="code"> </div>
			</div>
		</section>
		<footer>
			<a href="javascript:void(0);">
				<div class="foot_qt">
					<p>确认提交</p>
				</div>
			</a>
			<div class="prompt_box" style="display:none">
				<div class="cue_back"></div>
				<div class="cue1">
					<div class="cue_center">
						<div class="cue_center1">
							<p class="cue_p1">恭喜您</p>
							<p class="cue_p3">您已经是该善库成员，无需提交申请，现在可以开始行善了</p>
						</div>
						<div class="cue_center2">
							<a href="javascript:void(0);" class="ui-link" id="submit">
								<div class="cue_fl">
									<p class="cue_pl">立即行善</p>
								</div>
							</a>
							<a href="javascript:void(0);" class="ui-link" id="cancel1">
								<div class="cue_fr">
									<p class="cue_pr">取消</p>
								</div>
							</a>
						</div>
					</div>
				</div>
			</div>
		</footer>
	</div>
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
	<div class="cue2" id="msg"></div> 
	<input type="hidden" id="userId" value="${user.id }"> 
	<input type="hidden" id="lirbraryId" value="${lirbraryId }"> 
	
	<script data-main="<%=resource_url%>res/js/h5/goodlibrary.js?v=201712211600" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>