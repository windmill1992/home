<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="UTF-8">
<title>受助人信息</title>
<meta charset="UTF-8"><meta name="keywords" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="description" content=""/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<link rel="stylesheet" type="text/css" href="/res/css/util/city.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/bind.css?v=201712211100">
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/projectRelease.css?v=201712211100">
</head>
<style type="text/css">
	.addList .item{
		float: left;margin-right: 20px;
	}
</style>
<body>
	<div class="header">
		<a href="javascript:history.go(-1)" class="back">返回</a>
	    <div>受助人信息</div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输入您与受助人的关系" id="relation" class="release_in"  value="">
	    </div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输入受助人姓名" name="appealName" class="release_in" id="appealName"  value="">
	    </div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输受助人身份证" class="release_in" name="appealIdcard" id="appealIdcard"  value="">
	    </div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输入联系电话" class="release_in" name="appealPhone" id="appealPhone"  value="">
	    </div>
	</div>
    <div class="bind_b1">
		<div class="bind_fr1 release_fr1">
			<input type="text" name="addr" id="addr" placeholder="所在地" class="release_in" readonly/>
	    	<div id="addrContainer"></div>
	    </div>
	   </div>
    <div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	    <input type="text" placeholder="请输入详细地址" class="release_in" name="detailAddress" id="detailAddress"  value="${user.qqOrWx }">
	    </div>
    </div>
    <p class="form_text">请上传受助人户口本或结婚证</p>
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
        <a href="javascript:;" id="comfirmSubmit">下一步</a>
    </div>
    <div id="bigImg">
    	<div class="bd"></div>
    </div>
    <%--提示信息--%>
    <div class="cue2" id="msg"></div>
	<input type="hidden" id="institutionType" name="type" value="${type }">
	<input type="hidden" id="zlId" name="zlId" value="${zlId }">
	<input type="hidden" id="helpType" name="helpType" value="${helpType }"  />
	<input type="hidden" id="projectId" name="projectId" value="${projectId }"  />
	<input type="hidden" id="typeName_e" name="typeName_e" value="${typeName_e }"  />
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script src="/res/js/util/ajaxform.js"></script>
	<script src="/res/js/h5/releaseProject2/card.js"></script>
	<script src="/res/js/common/city.js"></script>
	<script src="/res/js/common/city2.min.js"></script>
	<script src="/res/js/h5/releaseProject/mobileneedhelp.js?v=20171123"></script>
	<script type="text/javascript">
		new MultiPicker({
			input:"addr",
			container:"addrContainer",
			jsonData:$city,
			success:function(arr){
				if(arr.length==3){
					$("#addr").val(arr[0].value +""+arr[1].value+""+arr[2].value);
				}else{$("#addr").val(arr[0].value +""+arr[1].value);}
			}
		});
	</script>
</body>
</html>