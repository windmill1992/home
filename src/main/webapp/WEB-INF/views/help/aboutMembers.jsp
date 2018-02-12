<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园-一届理事会成员</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="">首页</a> > 一届理事会成员
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight A-members">
            <div class="boederbox">
            	<h2>
                	<span class="lText">一届理事会成员</span>
                </h2>
                <ul class="members">
                 	 <li class="unit">
                        <div class="avatar"><img src="http://www.17xs.org/res/images/about/1yanyina.jpg" alt=""></div>
                        <p class="jobTit">理事长 严意娜</p>
                        <p class="status">善园基金会发起人</p>
                     </li>
                        <li class="unit">
                        <div class="avatar"><img src="http://www.17xs.org/res/images/about/2douruigang.jpg" alt=""></div>
                        <p class="jobTit">理事 窦瑞刚</p>
                        <p class="status">腾讯公益慈善园基金会会<br>执行秘书长</p>
                     </li>
                     <li class="unit">
                        <div class="avatar"><img src="http://www.17xs.org/res/images/about/3beixiaochao.jpg" alt=""></div>
                        <p class="jobTit">理事 贝晓超</p>
                        <p class="status">新浪微博社会责任部<br>总监</p>
					</li>
                    <li class="unit">
                        <div class="avatar"><img src="http://www.17xs.org/res/images/about/4lichengbo.jpg" alt=""></div>
                        <p class="jobTit">理事 李成波</p>
                        <p class="status">浙江宾果信息科技有限公司<br>董事长</p>
					</li>
                    <li class="unit">
                        <div class="avatar"><img src="http://www.17xs.org/res/images/about/5xiongjikai.jpg" alt=""></div>
                        <p class="jobTit">理事 熊基凯</p>
                        <p class="status">银亿集团<br>执行董事</p>
                    </li>
                    <li class="unit">
                        <div class="avatar"><img src="http://www.17xs.org/res/images/about/6zhanghelian.jpg" alt=""></div>
                        <p class="jobTit">理事 张荷莲</p>
                        <p class="status">北京汇银信通投资有限公司<br>执行董事</p>
                    </li>
                 </ul>
            </div>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/about.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
