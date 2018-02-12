<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href='<%=resource_url%>/res/css/dev/indextousu.css'/>
</head> 

<body>
<!--头部 start-->
	<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->

 <div class="ts_location">
   <ul>
     <li>当前位置：</li>
     <li><a href="<%=resource_url%>index/indexService.do" style="color:#000000;">客服中心 </a></li>
     <li class="fenlan">> <li>
     <li>${item}</li>
   </ul>
 </div>
 <c:if test="${userId==null }">
 <div class="jianyidenglu_box">
  <div class="jianyidenglu">
  <span class="jianyitubiao"><img src="<%=resource_url%>res/images/kefuzhongxin/jianyidenglu1.png"></span><span style="font-size:12px;">建议您先<span>
  <a  class="denglujian" href="http://www.17xs.org/user/sso/login.do?entrance=%20http%3A%2F%2Fwww.17xs.org%2Findex%2FtoAddService.do?type=2">登录</a></span>，登录后以便您查看投诉回复进度。</span>
  </div>
 </div></c:if>
 <div class="neirong">
   <ul>
     <li class="bitian" style="color:#ff0000;font-weight:bold;font-size:14px;margin-right:10px;">*</li>
     <li class="ts_neirong">投诉内容 :</li>
     <li class="ts_tx"><textarea class="neiroujy_1" id="content" placeholder="请详细描述您的诉求内容，为确保您的个人信息安全，请勿在内容中填写个人账户、密码、联系方式等信息。"></textarea></li>
   </ul>
 </div>
 <div class="wrong_cuo1" style="display:none" id="wrong_cuo1">
   <ul>
     <li style="margin-top:2px;margin-right:3px;"><img src="<%=resource_url%>res/images/kefuzhongxin/cuowu.png"></li>
     <li>请输入投诉内容</li>
   </ul>
 </div>
 <div class="fangan">
   <ul>
     
     <li class="ts_fangan">我的解决方案 :</li>
     <li class="ts_fangan_tx">
     <textarea class="neiroujy_2" id="scheme" placeholder="请告诉我们您的解决方案，以便我们参考，谢谢。为确保您的个人信息安全，请勿在内容中填写个人账户、密码、联系方式等信息。" ></textarea>
     </li>
   </ul>
 </div>
 <div class="xingming">
   <ul>
     <li class="bitian" style="color:#ff0000;font-weight:bold;font-size:14px;margin-right:10px;margin-top:3px;">*</li>
     <li class="ts_xingming">我的姓名 :</li>
     <li class="ts_xingming_tx"><input placeholder="请输入您的真实姓名" id="user_txt" name="author" class="yourname" value="" type="text"></li>
   </ul>
 </div>
 <div class="wrong_cuo2" style="display:none" id="wrong_cuo2">
   <ul>
     <li style="margin-top:2px;margin-right:3px;"><img src="<%=resource_url%>res/images/kefuzhongxin/cuowu.png"></li>
     <li>请输入姓名</li>
   </ul>
 </div>
 <div  class="lianxi">
   <ul>
     <li class="bitian" style="color:#ff0000;font-weight:bold;font-size:14px;margin-right:10px;margin-top:3px;">*</li>
     <li class="ts_lianxi">我的联系方式 ：</li>
     <li class="ts_lianxi_tx1"><input placeholder="请输入您的电话/QQ/微信" id="phone_txt" name="phone_number" class="cellphone" value="" type="text"></li>
     
   </ul>
 </div>
  <div class="wrong_cuo3" style="display:none" id="wrong_cuo3">
   <ul>
     <li style="margin-top:2px;margin-right:3px;"><img src="<%=resource_url%>res/images/kefuzhongxin/cuowu.png"></li>
     <li>请输入您的电话/QQ/微信</li>
   </ul>
 </div>
 <div class="yanzheng_box">
 <div class="yanzheng">
   <ul>
     <li class="ts_yanzheng">验证码 :</li>
     <li class="ts_yanzheng_tx"><input class="yanzhengnumber" value="" type="text" id="codeNum"></li>
     <li class="yz_tu"><img src="<%=resource_url%>user/code.do" id="codepic" class="codepic" alt="" /></li>
     <li class="huantu" style="cursor:pointer;text-decoration:underline;font-size:14px;color:#aaa;font-weight:normal;margin-top:1px">看不清，换一张</li>
   </ul>
 </div>
 <div class="wrong_cuo4" style="display:none" id="wrong_cuo4">
   <ul>
     <li style="margin-top:1px;margin-right:3px;"><img style="margin-top:2px;" src="<%=resource_url%>res/images/kefuzhongxin/cuowu.png"></li>
     <li style="margin-top:2px;"><span id="wrcode">验证码错误</span></li>
   </ul>
 </div>
 </div>
 <div class="tijiao_box">
   <a class="tijiao_ts" id="tijiao_ts">提交投诉</a>
     
 </div>
 
<!--主体 end-->
<!--底部 start-->
	<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<input type="hidden" value="${type}" name="type" id="type">
<input type="hidden" value="${userId}" name="userId" id="userId">
<input type="hidden" value="${projectId }" name="projectId" id="projectId">
<script data-main="<%=resource_url%>res/js/dev/addService.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
