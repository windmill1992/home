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
<body>
	<div class="header">
    	<a id="back" href="javascript:history.go(-1);" class="back">返回</a>
	    <div>受助人信息</div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输入受助人姓名" name="appealName" class="release_in" id="appealName"  value="${user.realName }">
	    </div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输入受助人身份证号" class="release_in" name="appealIdcard" id="appealIdcard"  value="${user.idCard }">
	    </div>
	</div>
	<div class="bind_b1">
	    <div class="bind_fr1 release_fr1">
	        <input type="text" placeholder="请输入联系电话" class="release_in" name="appealPhone" id="appealPhone"  value="${user.mobileNum }">
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
	        <input type="text" placeholder="请输入详细地址" class="release_in" name="detailAddress" id="detailAddress"  value="">
	    </div>
	 </div>
	</div>
   <div class="foot_qt">
       <a href="javascript:void(0);" id="comfirmSubmit">下一步</a>
    </div>
	 <%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" id="institutionType" name="type" value="${type }">
	<input type="hidden" id="zlId" name="zlId" value="${zlId }">
	<input type="hidden" id="helpType" name="helpType" value="${helpType }"  />
	<input type="hidden" id="projectId" name="projectId" value="${projectId }"  />
	<input type="hidden" id="typeName_e" name="typeName_e" value="${typeName_e }"  />
	
	<script src="/res/js/jquery-1.8.2.min.js"></script>
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