<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
    <meta name="keywords" content=""/>
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>请选择募捐项目领域</title>
    <link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/releaseNav.css?v=20171128">
</head>
<body>
	<div class="header">
    	<a href="javascript:history.go(-1)" class="back">返回</a>
    	<div>请选择募捐项目领域</div>
	</div>
	<div class="release_lis">
	    <c:forEach var="record" items="${listTypeConfig}" varStatus="status">
		    <a href="javascript:;" class="donateFieldtype" onclick="donateField(${status.count })" >
		        <div class="lis_two lis_g flex spb fcen">
	       			<div class="lis_fl">
	               		<input type="hidden" id="typeName${status.count}" name="typeName${status.count}" value="${record.typeName_e }">
		            	<p class="lis_text">${record.typeName}</p>
		            	<p class="lis_text1">${record.explain }</p>
	        		</div>
	        		<span class="lis_sp"></span>
		        </div>
		    </a>
	    </c:forEach>
	</div>
	<input type="hidden" id="type" name="type" value="${type }">
	<input type="hidden" id="zlId" name="zlId" value="${zlId }">
	<input type="hidden" id="helpType" name="helpType" value="${helpType }">
	
	<script src="/res/js/jquery-1.8.2.min.js"></script> 
	<script type="text/javascript">
		$(function(){
			var urls = ['/test/personAided.do','/test/familyAided.do','/test/othersAided.do','/test/instituteToOne.do','/test/publicReleaseList.do'];
			$('body').on('click','.donateFieldtype',function(){
				var helpType = Number($("#helpType").val()),
					type = $("#type").val(),
					zlId = $("#zlId").val(),
					typeName = $(this).find('input').val();
				location.href = urls[helpType-1] + '?type=' + type +'&helpType=' + helpType + '&typeName_e=' + typeName + '&zlId=' + zlId;
			});
		});
	</script>
</body>
</html>