<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<title>添加项目详情</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=201712211100">
<link rel="stylesheet" type="text/css" href="/res/css/h5/red_payment.css?v=20171213">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/releaseList.css?v=20171213">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectRelease.css?v=201712211100">
</head>
<body>
	<div class="header">
	    <a href="javascript:history.go(-1)" class="back">返回</a>
	    <div>请输入项目信息</div>
	</div>
	<div class="red redLeft">
		<div class="red_center1 red_center2">
		    <input type="text" placeholder="项目标题" class="center_inp1"  id="title" value="${project.title}">
		    <span class="center_sp1"></span>
		</div>
		<div class="red_center1 red_center2">
		    <input type="text" placeholder="目标金额" class="center_inp1"  id="cryMoney" value="<fmt:formatNumber value="${project.cryMoney}" pattern="0.00"/>" onKeyUp="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)">
		    <span class="center_sp1">元</span>
		</div>
		<div class="red_center1 red_center2">
		    <input type="number" placeholder="募捐时间默认为30天" class="center_inp1"  id="donateTime" value=""  onkeyup="this.value=this.value.replace(/D/g,'')" onafterpaste="this.value=this.value.replace(/D/g,'')">
		    <span class="center_sp1">天</span>
		</div>
	    <div class="red_centerfl">
	        <textarea name="textArea1" placeholder="项目内容:" class="textArea1" cols="35" rows="8"  id="content"  placeholder="${atc.model }"></textarea>
	    </div>
	    <p class="listText" style="border-bottom: 1px dashed #e0e0e0; padding-bottom: 10px"></p>
    </div>
	<p class="form_text"> ${atc.uploadImageDirection }</p>
	<div class="upload-pic">
	 	<form id="form1" action="/file/upload3.do" method="post" enctype="multipart/form-data">	
			<div class="addList" id="imgList1">
				<c:forEach var="record" items="${bflist}" varStatus="status">
					<div class="item" id="${record.id}">
						<a href="javascript:;" class="preview">
							<img src="${record.url}" width="80px" height="70px">
						</a>
						<i class="del"><img src="/res/images/h5/images/close.png"/></i>
					</div>
				</c:forEach>
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
        <a href="javascript:;" id="needhelpsubmit2">确认发布</a>
    </div>
    <div id="bigImg">
    	<div class="bd"></div>
    </div>
    <%--提示信息--%>
    <div class="cue2" style="display: none"  id="msg"></div>
	<input type="hidden" id="institutionType" name="type" value="${type }">
	<input type="hidden" id="zlId" name="zlId" value="${zlId }">
	<input type="hidden" id="helpType" name="helpType" value="${helpType }"  />
	<input type="hidden" id="projectId" value="${projectId}">
	<input type="hidden" id="typeName_e" name="typeName_e" value="${typeName_e }"  />
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script> 
	<script src="/res/js/util/ajaxform.js"></script>
	<script src="/res/js/h5/releaseProject/mobileProjectDetails.js?v=20171123"></script>

</body>
</html>