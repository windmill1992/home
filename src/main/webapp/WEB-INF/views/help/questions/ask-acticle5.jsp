<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园-捐款方式</title>
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
        当前位置：<a href="http://www.17xs.org/" title="" >首页</a> > 捐款方式
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight aboutCon">
            <div class="boederbox">
            	<h2>
                	<span class="lText">捐款方式有哪些 ?</span>
                </h2>
      			<div>
                  <p>
                    <span style="font-family:Microsoft YaHei;"><span style="font-size: 12px;">l<span style="font-size: 7pt;">&nbsp; </span></span><span style="font-size: 12px;">个人捐：个人针对单个项目一次捐款，单个项目的多人众筹。</span></span>
                </p>
                <p>
                    <span style="font-family:Microsoft YaHei;"><span style="font-size: 12px;"><br>
                    </span></span>
                </p>
                <p>
                    <span style="font-family:Microsoft YaHei;"><span style="font-size: 12px;">l<span style="font-size: 7pt;">&nbsp; </span></span><span style="font-size: 12px;">企业助善区：通过微信、微博等移动互联网平台邀请网友在平台注册，企业将善款发放给网友，以网友名义进行捐款，企业和注册网友数据双向统计。</span></span>
                </p>
                <p>
                    <span style="font-family:Microsoft YaHei;"><span style="font-size: 12px;"><br>
                    </span></span>
                </p>
                <p>
                    <span style="font-family:Microsoft YaHei;"><span style="font-size: 12px;">l<span style="font-size: 7pt;">&nbsp; </span></span><span style="font-size: 12px;">企业捐：企业针对单个项目一次捐款，单个项目的多个企业众筹。</span></span>
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