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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/indexxiangqingye.css">
</head>
<body>
<!--头部 start-->
	<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
 <div class="ts_location">
   <ul>
     <li>当前位置: </li>
     <li><a href="<%=resource_url%>index/indexService.do" style="color:#000000;">客服中心 </a></li>
     <li class="fenlan">> <li>
     <li><c:if test="${acs.type == 0}">咨询详情</c:if>
	     <c:if test="${acs.type == 1}">建议详情</c:if>
	     <c:if test="${acs.type == 2}">投诉详情</c:if></li>
   </ul>
 </div>
 <div class="fenleibox">
   <div class="main_question">
	   <div class="fenlei_biaoti">
	    <p style="padding-left:10px;">分类：<span class="wenti_leibie">
	    <c:if test="${acs.type == 0}">咨询</c:if>
	    <c:if test="${acs.type == 1}">建议</c:if>
	    <c:if test="${acs.type == 2}">投诉</c:if></span></p>
	   </div>
	   <div class="wangyouwen">     
	    <div class="wangyouwen_tx">
	    <c:if test="${acs.headUrl == null}"><img  style="width:80px;height:80px;"  src="<%=resource_url%>res/images/kefuzhongxin/touxiang5.png"></c:if>
	    <c:if test="${acs.headUrl != null}"><img  style="width:80px;height:80px;"  src="${acs.headUrl}"></c:if>
	         <span>${acs.userNickName}</span>
	       </div>
	       <div class="wangyouwen_nr">
	         <div class="wangyouwen_nr1"><span>${acs.content }</span></div>
	         <div class="wangyouwen_nr2">提问时间：<span style="margin-left:10px;"><fmt:formatDate  value="${acs.createTime}" pattern="yyyy-MM-dd HH:mm"/></span></div>
	       </div>    
	   </div>
   </div>
   
   <div class="main_answer" id="serviceDetail">
	  <!-- 数据 -->
   </div> 
 
  </div>
 
 
  <div class="fabiaopinglunbox">
   <div class="fabiaopinglun_biaoti">
     <p style="padding-left:10px;">发表评论</p>
   </div>
   <div class="fb_yk">
   <div class="fb_yk_left_tx">
   <c:if test="${userId == null }"><img  style="width:80px;height:80px;"  src="<%=resource_url%>res/images/kefuzhongxin/touxiang5.png"><span>爱心人士</span></c:if>
   <c:if test="${userId !=null }">
   <c:if test="${user.coverImageUrl == null }"><img  style="width:80px;height:80px;"  src="<%=resource_url%>res/images/kefuzhongxin/touxiang5.png"></c:if>
   <c:if test="${user.coverImageUrl != null }"><img  style="width:80px;height:80px;"  src="${user.coverImageUrl }"></c:if><span>${user.nickName }</span></c:if>
   </div>
   <div class="fb_yk_right">
      <div class="fb_ykrdl">
      <c:if test="${userId ==null }"><a href="http://www.17xs.org/user/sso.do">注册</a><span>/</span><a href="http://www.17xs.org/user/sso/login.do?entrance=%20http%3A%2F%2Fwww.17xs.org%2Fuser%2FtoServiceDetail.do?id=${acs.id }" >登录</a>
      </c:if></div>
      <div class="fb_yktx">
        <textarea class="neiroufb" placeholder="请输入评论内容" id="reply" name="reply"></textarea>      
      </div>
 <div class="name_phone"> 
 <div class="xingming">
   <ul>
     <li class="bitian" style="color:#ff0000;margin-top:1px;font-weight:bold;font-size:14px;margin-right:10px;">*</li>
     <li class="ts_xingming">我的姓名 :</li>
     <li class="ts_xingming_tx"><input placeholder="请输入您的真实姓名" id="user_txt" name="author" class="yourname" value="" type="text"></li>
   </ul>
 </div>
 
 <div  class="lianxi">
   <ul>
     <li class="bitian" style="color:#ff0000;margin-top:1px;font-weight:bold;font-size:14px;margin-right:10px;">*</li>
     <li class="ts_lianxi">我的联系方式 ：</li>
     <li class="ts_lianxi_tx1"><input placeholder="请输入您的电话/QQ/微信" id="phone_txt" name="phone_number" class="cellphone" value="" type="text"></li>
     
   </ul>
 </div>
 </div>
 <div class="name_phone_wrong" >
 <div class="wrong_cuo2" >
   <ul style="display:none" id="wrong_cuo2">
     <li style="margin-top:2px;margin-right:3px;"><img src="<%=resource_url%>res/images/kefuzhongxin/cuowu.png"></li>
     <li>请输入姓名</li>
   </ul>
 </div>
  <div class="wrong_cuo3" >
   <ul style="display:none" id="wrong_cuo3">
     <li style="margin-top:2px;margin-right:3px;"><img src="<%=resource_url%>res/images/kefuzhongxin/cuowu.png"></li>
     <li>请输入您的电话/QQ/微信</li>  
   </ul>
 </div>
 </div>

 <div class="tijiao_box">
   <a class="tijiao_button" id="tijiao_button">提交</a>    
 </div>      
   
   
   </div>
   </div>
  </div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<input type="hidden" id="serviceId" name="id" value="${acs.id }">
<input type="hidden" id="userId" name="userId" value="${user.id }">
<input type="hidden" id="orderBy" name="orderBy" value="">
<input type="hidden" id="orderDirection" name="orderDirection" value="">
<script data-main="<%=resource_url%>res/js/dev/customerServiceDetail.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
