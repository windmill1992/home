<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="cache-control" content="no-store" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" />
<title>善园网 - 个人中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css" />
<style>
/*.wreg{width:754px;border:0;}
	.wregt{padding:0;} */
</style>
</head>
<body>
	<%@ include file="./../common/newhead.jsp"%>
	<input type="hidden" value="${error}" name="msg" id="msg">
	<input type="hidden" value="${state}" name="state" id="state">
	<div class="bodyer userCenter">
		<div class="page">
			<div class="page-bd">
				<div class="uCEN-L">
					   <%@ include file="./../common/pleft_menu.jsp"%>
				</div>
				<div class="uCEN-R" id="rzpage">
					<c:if test="${stauts== 1}">
						<div class="uCEN-hd">
							<span class="uCEN-tit">实名认证</span>
						</div>
						<!--<form action="http://www.17xs.org/user/perfect.do" method="post"
							name="form"  enctype="multipart/form-data">-->
							<div class="legalize">
								<div class="rlitem">
									<label>真实姓名：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="${realname}" id="realname"
												name="realname" placeholder="真实姓名" />
										</div>
										<span class="wrinfo error"></span>
									</div>
								</div>
								<div class="rlitem">
									<label>身份证：</label>
									<div class="rltext">
										<div class="rlinput">
											<input type="text" value="${idCard}" id="idCard"
												name="idCard" placeholder="身份证号码" />
										</div>
										<span class="wrinfo"><em></em></span>
									</div>
								</div>
								<c:if test="${state != 1}">
								<div id="ptcode">
									<div class="rlitem">
										<label>手机号：</label>
										<div class="rltext">
											<div class="rlinput">
												<input type="text" class="" value="${phone}" id="phone"
													name="phone"  placeholder="手机号码"/>
											</div>
											<span class="wrinfo"><em></em></span>
										</div>
									</div>
									<div class="rlitem rlmcode">
										<label>手机验证码：</label>
										<div class="rltext">
											<div class="rlinput">
												<input type="text" class="" placeholder="手机验证码" value=""
													id="phoneCode" name="phoneCode" />
											</div>
											<a class="wrsdcode" id="pSend">获取手机验证码</a> <span
												class="wrsdpt"></span> <span class="wrinfo"><em></em></span>
										</div>

									</div>
								</div>
								</c:if>
								<div class="rlgroup">
									<label>手持身份证照片</label>
									<div class="controls">
										<div class="uploader">
											<div class="pic" id="pic1">
												<img src="<%=resource_url%>res/images/login/uploader.jpg"
													alt="" />
											</div>
											<a class="uploadbtn"> <span class="btn-text">
													上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
												<form  id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
													<input type="file" name="file" hidefocus="true"
														id="imgFile1" class="file-input" />
												    <input type="hidden" name="type" id="type" value="3"/>
												</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphoto1"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/login/id-hand-s.jpg"
													alt="" /> <a title="" class="idphoto">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
								<div class="rlgroup">
									<label>身份证正面</label>
									<div class="controls">
										<div class="uploader">
											<div class="pic" id="pic2">
												<img src="<%=resource_url%>res/images/login/uploader.jpg"
													alt="" />
											</div>
											<a class="uploadbtn"> <span class="btn-text">
													上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form  id="form2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
													<input type="file" name="file" hidefocus="true"
														id="imgFile2" class="file-input" />
												    <input type="hidden" name="type" id="type" value="2"/>
													</form>
													
												</div>
											</a>
											<div class="wrinfo" id="msgphoto2"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/login/id-front-s.jpg"
													alt="" /> <a title="" class="idphoto">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
								<div class="rlgroup">
									<label>身份证背面</label>
									<div class="controls controlsn">
										<div class="uploader">
											<div class="pic" id="pic3">
												<img src="<%=resource_url%>res/images/login/uploader.jpg"
													alt="" />
											</div>
											<a class="uploadbtn"> <span class="btn-text">
													上传并预览 </span>
												<div class="filewrapper" style="overflow: hidden;">
													<form  id="form3" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
													<input type="file" name="file" hidefocus="true"
														id="imgFile3" class="file-input" />
												    <input type="hidden" name="type" id="type" value="1"/>
													</form>
												</div>
											</a>
											<div class="wrinfo" id="msgphoto3"></div>
										</div>
										<div class="example">
											<span class="title">示例：</span>
											<div class="pic">
												<img src="<%=resource_url%>res/images/login/id-back-s.jpg"
													alt="" /> <a title="" class="idphoto">查看详细要求</a>
											</div>
										</div>
									</div>
								</div>
								<div class="rlitem">
									<label></label>
									<div class="rltext">
										<input type="button" class="rlbtnok yh" id="rlbokbtn"
											value="提交" />
									</div>
								</div>
							</div>
							<input type="hidden" value="3,2,1" id="imageId" name="imageId" />
						<!--</form>-->
					</c:if>
					<c:if test="${stauts== 2}">
						<div class="lgl-state yh">
							<em></em>实名认证进入审核中...
						</div>
					</c:if>
					<c:if test="${stauts== 3}">
						<div class="lgl-state lgl-stateok yh">
							<em></em>您的实名认证已通过
						</div>
					</c:if>
				</div>

			</div>
		</div>
</div>
<input type="hidden" value="1" name="legalize" id="legalize">
		<%@ include file="./../common/newfooter.jsp"%>
		<script data-main="<%=resource_url%>res/js/dev/userlegalize.js"
			src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
