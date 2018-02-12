<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<title>编辑项目</title>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />

<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/h5/bind.css">
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/h5/red_payment.css">
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/h5/releaseProject/projectRelease.css">
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/h5/releaseProject/aided_info.css">
<link rel="stylesheet" type="text/css"
	href="<%=resource_url%>res/css/h5/releaseProject/releaseList.css">
	   	<link href="<%=resource_url%>res/css/h5/basic.css" rel="stylesheet" type="text/css" />
	
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>

</head>
<script type="text/javascript">
	$(function(){
	     var content=$("content").html();
	     $("content").html(content);
	})
</script>
<body>
	<div class="red redLeft">
		<div class="header">
			<div class="head_fl fl">
				<a id="backPersonAided" href="javascript:history.go(-1)">&lt;返回</a>
			</div>
			<div class="head_fr fr">
				<p>请输入项目信息</p>
			</div>
		</div>

		<!-- 		<div class="red_center1 red_center2"> -->
		<!-- 			<p class="center_p1">您与受助人的关系:</p> -->
		<!-- 			<input type="text" placeholder="" class="center_inp1" id="relation" -->
		<!-- 				value="${projectUserInfo.relation}"> <span class="center_sp1"></span> -->
		<!-- 		</div> -->

		<div class="red_center1 red_center2">
			<input type="text" placeholder="请输入受助人姓名" class="center_inp1" id="appealName"
				value="${projectUserInfo.realName}"> <span
				class="center_sp1"></span>
		</div>

		<div class="red_center1 red_center2">
			<input type="text" placeholder="请输入受助人身份证" class="center_inp1"
				id="appealIdcard" value="${projectUserInfo.indetity}"> <span
				class="center_sp1"></span>
		</div>

		<div class="red_center1 red_center2">
			<input type="text" placeholder="请输入联系人电话" class="center_inp1"
				id="appealPhone" value="${projectUserInfo.linkMobile}"> <span
				class="center_sp1"></span>
		</div>

		<div class="red_center1 red_center2">
			<input type="text" placeholder="请输入受助人地址" class="center_inp1"
				id="familyAddress" value="${projectUserInfo.familyAddress}">
			<span class="center_sp1"></span>
		</div>

		<div class="red_center1 red_center2">
			<input type="text" placeholder="请输入项目标题" class="center_inp1" id="title"
				value="${project.title}"> <span class="center_sp1"></span>
		</div>
		<div class="red_center1 red_center2">
			<input type="text" placeholder="请输入目标金额" class="center_inp1"
				disabled="disabled" id="cryMoney"
				value="<fmt:formatNumber value="${project.cryMoney}" pattern="0.00"/>"
				onKeyUp="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">
			<span class="center_sp1">元</span>
		</div>
		<div class="red_center1 red_center2">
			<input type="number" placeholder="募捐时间默认为30天" class="center_inp1"
				id="donateTimeNum" value="${donateTimeNum }"> <span
				class="center_sp1">天</span>
		</div>

		<div class="red_centerfl">
			<p class="center_p2">项目内容:</p>
			<textarea name="textArea1" class="textArea1" cols="35" rows="8"
				id="content" placeholder="${atc.model }">${project.content }</textarea>
		</div>
		<p class="listText"
			style="border-bottom: 1px dashed #e0e0e0; padding-bottom: 10px">
			${atc.uploadImageDirection }</p>
	</div>
	<div class="form_box">
		<p class="form_text">请上传相关照片，不超过6张</p>
		<ul class="box_ul">
			<li class="line4"><span class="value">
					<form id="form1" action="http://www.17xs.org/file/upload3.do"
						method="post" enctype="multipart/form-data">
						<div class="add_list" id="imgList">
							<c:forEach var="record" items="${project.bfileList}"
								varStatus="status">
								<div class="item" id="${record.id}">
									<img src="${record.url}" width="80px" height="70px"><i
										style="display: none;">×</i>
								</div>
							</c:forEach>
							<div class="item add">
								<a class="add_btn">+<input type="file" name="file"
									hidefocus="true" id="imgFile" class="file-input"> <input
									type="hidden" name="type" id="type" value="4"></a>
							</div>
					</form>
			</span></li>
		</ul>
		<div class="clearDiv" style="clear:both"></div>
	</div>
	<footer>
		<div class="foot_qt">
			<p id="needhelpsubmit2">保存</p>
		</div>
	</footer>
	<%--提示信息--%>
	<div class="cue2" style="display:none" id="msg"></div>
	<input type="hidden" id="projectId" value="${project.id }" />
	<input id="state" type="hidden" value="${state}">
	<input type="hidden" id="projectUserInfoId" value="${projectUserInfo.id }" />
	<script
		data-main="<%=resource_url%>res/js/h5/releaseProject/editPublicRelease.js"
		src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>