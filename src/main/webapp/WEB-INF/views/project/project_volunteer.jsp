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
<title>善园网-志愿者报名</title>

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/infoblocknew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/index_volunteer.css"/>
<style>

</style>
</head>
<body>
<!--头部 start-->
	<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="volunteer_location">
   <ul>
     <li>当前位置：</li>
     <li><a  style="color:#000000;">项目详情</a></li>
     <li class="fenlan">><li>
     <li>志愿者报名</li>
   </ul>
 </div>
 <div class="volunteer_box">
    <div class="volunteer_choice">
        
        <div class="choice_0">志愿者类型：</div>
        <ul class="volunteer_choicebox">
            <li class="choice_1">个人</li>
            <li class="choice_2">组织</li>
        </ul>
    </div>
    <div class="volunteer_form changebox">
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1" style="letter-spacing:8px;">姓&nbsp;名</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="name"></input></li>
            <li style="display:none;"><i class="warn1">*</i><strong class="warn2">请输入姓名</strong></li>
        </ul>
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1">联系电话</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="mobile"></input></li>
            <li style="display:none;"><i class="warn1">*</i><strong class="warn2">请输入电话</strong></li>
        </ul>
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1">身份证号</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="indentity"></input></li>
            <li style="display:none;"><i class="warn1">*</i><strong class="warn2">请输入身份证</strong></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="letter-spacing:8px;">性&nbsp;别</span>
                <span class="term2">:</span>
            </li>
            <li class="write_1"><input type="radio" checked="checked" value="男" name="sex"></input>男<input type="radio"  value="女" name="sex"></input>女</li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="letter-spacing:8px;">单&nbsp;位</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="position"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1">电子邮件</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="email"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1">联系地址</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input style="width:400px" id="address"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="width:70px;">服务时间段</span>
                <span class="term2" style="margin-left:13px;">:</span>
            </li>
            <li class="write"><input style="width:400px" id="serviceTime"></input></li>
        </ul>
        <ul class="" style="height:100px;">
            <li class="need"></li>
            <li class="term">
                <span class="term1" style="width:84px;">历史服务记录</span>
                <span class="term2" style="margin-left:0px;">:</span>
            </li>
            <li class="write_2"><textarea id="historyService"></textarea></li>
        </ul>
        <div class="enrollbox">
            <span  class="enroll" id="personSubmit">确认报名</span>
        </div>
    </div>
    <div class="volunteer_form changebox">
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1">组织名称</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="groupName"></input></li>
            <li style="display:none;"><i class="warn1">*</i><strong class="warn2">请输入名称</strong></li>
        </ul>
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1">联系电话</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="linkMobile"></input></li>
            <li style="display:none;"><i class="warn1">*</i><strong class="warn2">请输入电话</strong></li>
        </ul>
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1">身份证号</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="indetity2"></input></li>
<!--            <li><i class="warn1">*</i><strong class="warn2">请输入身份证</strong></li>-->
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1">组织类型</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="groupType"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1">参加人数</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="number"></input></li>
        </ul>
        <ul class="">
            <li class="need">*</li>
            <li class="term">
                <span class="term1" style="width:70px;">联系人姓名</span>
                <span class="term2" style="margin-left:13px;">:</span>
            </li>
            <li class="write"><input id="linkName"></input></li>
        </ul>
        <ul class="">
            <li class="need"></li>
            <li class="term">
                <span class="term1">电子邮件</span>
                <span class="term2">:</span>
            </li>
            <li class="write"><input id="email2"></input></li>
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
                <span class="term1" style="width:70px;">服务时间段</span>
                <span class="term2" style="margin-left:13px;">:</span>
            </li>
            <li class="write"><input style="width:400px" id="serviceTime2"></input></li>
        </ul>
        <div class="enrollbox">
            <span class="enroll" id="groupSubmit">确认报名</span>
        </div>
    </div>
 </div>
 <script>
$(".changebox").hide();
$(".changebox").first().show();
n=0;
 $(".volunteer_choicebox li").click(function(){
     $(".volunteer_choicebox li").removeClass("choice_1");
     $(this).addClass("choice_1");
     /*$(this).addClass("cu");
     $(this).siblings().removeClass("cu");*/ 
    
n=$(this).index();
$(".changebox").hide();
$(".changebox").eq(n).show();
 })
</script>    
     
 
     
     
<!--主体 end-->
	<input type="hidden" id = "projectId" value="${projectId}">
<!--底部 start-->
	<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/addProjectVolunteer.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
