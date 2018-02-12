<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta charset="UTF-8">
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<title>项目发布</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=201712211100" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=201712211100">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectRelease.css?v=20180102">
</head>
<body>
	<div class="feed">
		<div class="header">
			<a href="javascript:history.go(-1);" class="back">返回</a>
			<div id="changeWord">我是个人</div>
		</div>

		<!--导航-->
		<div class="release_nav">
			<a class="nav_fl nav_gg1" href="javascript:;">我是个人</a>
			<a class="nav_fr" href="javascript:;">我代表机构/组织</a>
		</div>
		<!--我是个人-->
		<div class="geren" style="display:block">
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" id="userName" placeholder="请输入您的姓名" class="release_in" value="${user.realName }">
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" id="idCard" placeholder="请输入您的身份证号" class="release_in" value="${user.idCard }">
				</div>
			</div>

			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" id="MobileNum" placeholder="请输入您的手机号" class="release_in" value="${user.mobileNum }">
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" id="personalVerCode" placeholder="请输入您的验证码" class="release_ins">
					<a href="javascript:;" class="fsyz" id="usercode">发送验证码</a>
				</div>
			</div>
			
			<div class="upload-pic">
				<form action="/file/upload3.do" id="form1" method="post" enctype="multipart/form-data">
					<div class="addList" id="imgList1">
						<c:if test="${userimg1!=null && userimg1!=''}">
							<div class="item" id="${userimgid1 }">
								<a href="javascript:;" class="preview">
									<img src="${userimg1 }" class="io1" name="io1"/>
								</a>
								<i class="del"><img src="/res/images/h5/images/close.png"/></i>
							</div>		
						</c:if>
						<div class="add item">
							<a href="javascript:void(0);" class="add-btn">
								<label for="fileInput1"></label>
								<input type="file" name="file" class="file-input" id="fileInput1" hidefocus="true" />
								<input type="hidden" name="type" id="type1" value="4">
							</a>
						</div>
						<p>身份证正面</p>	
					</div>
				</form>
				<form action="/file/upload3.do" id="form2" method="post" enctype="multipart/form-data">
					<div class="addList" id="imgList2">
						<c:if test="${userimg2!=null && userimg2!=''}">
							<div class="item" id="${userimgid2 }">
								<a href="javascript:;" class="preview">
									<img src="${userimg2 }" class="io2" name="io2"/>
								</a>
								<i class="del"><img src="/res/images/h5/images/close.png"/></i>
							</div>		
						</c:if>
						<div class="add item">
							<a href="javascript:void(0);" class="add-btn">
								<label for="fileInput2"></label>
								<input type="file" name="file" class="file-input" id="fileInput2" hidefocus="true" />
								<input type="hidden" name="type" id="type2" value="4">
							</a>
						</div>
						<p>身份证反面</p>	
					</div>
				</form>
				<form action="/file/upload3.do" id="form3" method="post" enctype="multipart/form-data">
					<div class="addList" id="imgList3">	
						<c:if test="${userimg3!=null && userimg3!=''}">
							<div class="item" id="${userimgid3 }">
								<a href="javascript:;" class="preview">
									<img src="${userimg3 }" class="io3" name="io3"/>
								</a>
								<i class="del"><img src="/res/images/h5/images/close.png"/></i>
							</div>		
						</c:if>
						<div class="add item">
							<a href="javascript:void(0);" class="add-btn">
								<label for="fileInput3"></label>
								<input type="file" name="file" class="file-input" id="fileInput3" hidefocus="true" />
								<input type="hidden" name="type" id="type3" value="4">
							</a>
						</div>
						<p>手持身份证</p>	
					</div>
				</form>
			</div>
		</div>
		<!--我是机构-->
		<div class="jigou" style="display:none">
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" placeholder="请输入贵单位全称" id="name" class="release_in" value="${company.name }">
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" placeholder="请输入贵单位的结构代码" id="groupCode" class="release_in" value="${company.groupCode }">
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" placeholder="请输入详细地址" id="address" class="release_in" value="${company.address }">
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" placeholder="请输入联系人姓名" id="head" class="release_in" value="${company.head }">
				</div>
			</div>

			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" placeholder="请输入联系人手机号码" id="mobile" class="release_in" value="${company.mobile }">
				</div>
			</div>
			<div class="bind_b1">
				<div class="bind_fr1 release_fr1">
					<input type="text" id="teamVerCode" placeholder="请输入验证码" class="release_ins">
					<a href="javascript:;" class="fsyz" id="sendTeamVerCode">发送验证码</a>
				</div>
			</div>
			
			<div class="upload-pic">
				<form action="/file/upload3.do" id="form4" method="post" enctype="multipart/form-data">
					<div class="addList" id="imgList4">
						<c:if test="${companyimg1!=null && companyimg1!=''}">
							<div class="item" id="${companyimgid1 }">
								<a href="javascript:;" class="preview">
									<img src="${companyimg1 }" class="io4" name="io4"/>
								</a>
								<i class="del"><img src="/res/images/h5/images/close.png"/></i>
							</div>		
						</c:if>
						<div class="add item">
							<a href="javascript:void(0);" class="add-btn">
								<label for="fileInput4"></label>
								<input type="file" name="file" class="file-input" id="fileInput4" hidefocus="true" />
								<input type="hidden" name="type" id="type4" value="4">
							</a>
						</div>
						<p>机构证件</p>	
					</div>
				</form>
				<form action="/file/upload3.do" id="form5" method="post" enctype="multipart/form-data">
					<div class="addList" id="imgList5">		
						<c:if test="${companyimg2!=null && companyimg2!=''}">
							<div class="item" id="${companyimgid2 }">
								<a href="javascript:;" class="preview">
									<img src="${companyimg2 }" class="io5" name="io5"/>
								</a>
								<i class="del"><img src="/res/images/h5/images/close.png"/></i>
							</div>		
						</c:if>
						<div class="add item">
							<a href="javascript:void(0);" class="add-btn">
								<label for="fileInput5"></label>
								<input type="file" name="file" class="file-input" id="fileInput5" hidefocus="true" />
								<input type="hidden" name="type" id="type5" value="4">
							</a>
						</div>
						<p>单位授权书</p>	
					</div>
				</form>
			</div>
		</div>
			
		<div class="foot_qt">
			<a href="javascript:;">下一步</a>
		</div>
			
		<div id="bigImg">
			<div class="bd"></div>
		</div>
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
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input id="typestate" type="hidden" value="0" />
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="/res/js/h5/releaseProject2/card.js"></script>
	<script data-main="/res/js/h5/projectRelease.js?v=201712211100" src="/res/js/require.min.js"></script>
</body>
</html>