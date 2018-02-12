<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园-个人实名认证</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
</head> 
<body>
<%@ include file="./../../common/newhead.jsp" %>
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="" >首页</a> > 个人实名认证
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight aboutCon">
            <div class="boederbox">
            	<h2>
                	<span class="lText">个人实名认证</span>
                </h2>
      			<div>
                	<p>
                    <span style="font-family:Microsoft YaHei;font-size:12px;">1、请务必填写真实姓名、身份证号码和手机号码</span>
                	</p>
                    <p>
                    <span style="font-family:Microsoft YaHei;font-size:12px;"><span style="color: rgb(51, 51, 51);"><br>
                    </span></span>
                    </p>
                    <p>
                        <span style="font-family:Microsoft YaHei;font-size:12px;"><span style="color: rgb(51, 51, 51);">2</span><span style="color: rgb(51, 51, 51);">、请填写真实有效的手机号码，以便收取手机验证码以及在项目中能及时与您取得联系。</span></span>
                    </p>
                    <p>
                        <span style="font-family:Microsoft YaHei;font-size:12px;"><span style="color: rgb(51, 51, 51);"><br>
                        </span></span>
                    </p>
                    <p>
                        <span style="font-family:Microsoft YaHei;font-size:14px;"><span style="color: rgb(51, 51, 51);">3</span><span style="color: rgb(51, 51, 51);">、请务必提供与示例一样的身份证验证图片以便您进行身份验证。</span></span>
                    </p>
                </div>          
                
                
            </div>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<%@ include file="./../../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/about.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>