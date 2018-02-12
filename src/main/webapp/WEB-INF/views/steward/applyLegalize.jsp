<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<c:if test="${legalize== 3}">
<title>善园 - 我要求助</title>
</c:if>
<c:if test="${legalize== 2}">
<title>善园 - 加入善管家</title>
</c:if>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/applyStep.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/> 
</head> 
<body>
<%@ include file="./../common/newhead.jsp"%>
<div id="bodyer" class="bodyer applySteps applyLeg">
	<div class="page">
    	<%-- <div class="page-hd">
    	<c:if test="${legalize== 3}">
			<div class="page-tit"><h2 class="w1000">我要求助</h2></div>
		</c:if>
		<c:if test="${legalize== 2}">
		<div class="page-tit"><h2 class="w1000">加入善管家</h2></div>
		</c:if>
        </div> --%>
        <div class="page-bd"> 
            <div class="applyStp-main">
            	<div class="applyStp-hd">
            	<c:if test="${legalize== 3}">
			<div class="applyStp-tit">我要求助申请流程</div>
			</c:if>
			<c:if test="${legalize== 2}">
				<div class="applyStp-tit">善管家申请流程</div>
			</c:if>
                    <ul class="applyStp-tab">
                    	<li class="curTab"><i>1</i><span>实名认证</span></li>
                        <li><i>2</i><span>完善资料</span></li>
                        <li><i>3</i><span>等待审核</span></li>
                    </ul>
                    
                </div>
                <div class="applyStp-bd">
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
								<c:if test="${state == null}">
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
											value="下一步" />
									</div>
								</div>
							</div>
                </div>
            </div>
        </div>
    </div>

</div>
<input type="hidden" value="${legalize}" name="legalize" id="legalize">
<%@ include file="./../common/newfooter.jsp"%>
<script data-main="<%=resource_url%>res/js/dev/userlegalize.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
