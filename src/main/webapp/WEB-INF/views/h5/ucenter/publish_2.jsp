<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/publish.css"/>

</head>

<body>
<div id="pageContainer">
  <div class="publish">
  	<!--标题//-->
    <div class="top">
    	<a href="javascript:void(0);" class="back" id="back"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAABBJREFUeNpi/P//PwNAgAEACQEC/2m8kPAAAAAASUVORK5CYII=" class="back_img back_icon_6"></a>
    	<div class="title">我要求助</div>
     </div>
     
     <!----成功提示//-->
    	<div class="success">
        	<div class="icon"><img src="<%=resource_url%>res/images/success_icon.png" width="77" height="77"></div>
            <div class="txt">
            	<span class="txt1">您的求助已经提交成功！</span>
                <span class="txt2">客服将在24小时之内联系您！</span>
            </div>
        </div>	
        <div class="material">
        	<div class="title">根据您的求助原因，请准备好以下材料</div>
            <div class="body" id="typeFile">
            	<!-- 
           		<span>求学：</span>
                <span>1、证明人工作单位、职务及联系电话（如校长、老师）；</span>
                <span>2、清晰的受助人身份证或户口本照片；</span>
                <span>3、清晰的受助人手写求助信照片；</span>
                <span>4、清晰的受助人学校证明照片（学校盖章）；</span>
                <span>5、清晰的受助人贫困状况照片19张以上（不重复不类似）；</span>
                -->
            </div>
        </div>
      <!--页脚
      <div class="footer">
      	<span>© 2015 杭州智善  版权所有</span>
        <img src="images/min-logo.jpg"></div>//-->
  </div>
</div>
	<input type="hidden" value="${typeName}" id="typeName">
	<input type="hidden" value="${tName}" id="tName">
</body>
<script data-main="<%=resource_url%>res/js/h5/needhelp_submit.js?v=201510131034" src="<%=resource_url%>res/js/require.min.js"></script>
</html>