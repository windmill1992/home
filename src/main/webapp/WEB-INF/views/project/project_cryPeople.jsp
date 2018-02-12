<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网-项目求助</title>

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/infoblocknew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/index_help.css"/>
<style>

</style>
</head>
<body>
<!--头部 start-->
	<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="help_location">
   <ul>
     <li>当前位置：</li>
     <li><a  style="color:#000000;">项目详情</a></li>
     <li class="fenlan">><li>
     <li>项目求助</li>
   </ul>
 </div>
 <div class="help_box">
    <div class="help_form">
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1" style="letter-spacing:8px;">姓&nbsp;名</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="name"></input></li>
<!--            <li><i class="warn1">*</i><strong class="warn2">请输入姓名</strong></li>-->
        </ul>
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1">联系电话</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="mobile"></input></li>
<!--            <li><i class="warn1">*</i><strong class="warn2">请输入电话</strong></li>-->
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="letter-spacing:8px;">单&nbsp;位</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="workUnit"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1">联系地址</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input style="width:400px" id="linkAddress"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="width:84px;">项目落实地址</span>
                <span class="term2" style="margin-left:0px;">:</span>
            </li>
            <li class="write"><input style="width:400px" id="actualAdress"></input></li>
        </ul>
        <ul class="" style="height:100px;">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="width:84px;letter-spacing:8px;">备&nbsp;注</span>
                <span class="term2" style="margin-left:0px;">:</span>
            </li>
            <li class="write_2"><textarea id="remark"></textarea></li>
        </ul>
        <div class="enrollbox">
            <span href="" class="enroll" id = "crySubmit">确认求助</span>
        </div>
    </div>
 </div>

     
     
     
     
     
     
     
<!--主体 end-->
<!--底部 start-->
	<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<input type="hidden" id ="projectId" value="${projectId }">
<script data-main="<%=resource_url%>res/js/dev/addProjectCryPeople.js" src="<%=resource_url%>res/js/require.min.js"></script>

</body>
</html>
