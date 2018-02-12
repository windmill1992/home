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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/indexkefulist.css">
</head>
<body>
<!--头部 start-->
	<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="kfzx_entrance">
 <ul>
  <li class="wyzx"><a href="<%=resource_url%>index/toAddService.do?type=0"><img src="<%=resource_url%>res/images/kefuzhongxin/woyaozixun.png"></a></li>
  <li class="wyjy"><a href="<%=resource_url%>index/toAddService.do?type=1"><img src="<%=resource_url%>res/images/kefuzhongxin/woyaojianyi.png"></a></li>
  <li class="wyts"><a href="<%=resource_url%>index/toAddService.do?type=2"><img src="<%=resource_url%>res/images/kefuzhongxin/woyaotousu.png"></a></li>
  <li class="zxkf"><a href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes"><img src="<%=resource_url%>res/images/kefuzhongxin/zaixiankefu.png"></a></li>
 </ul>
</div>
<div class="main_box_wen">
 <div class="dajiawen">
   <h2 class="text_biaoti">
     <span class="text_wen">大家正在问</span>
   </h2>
 </div>
 <div class="xuanzelan">
   <ul class="xuanzelan_qh">
     <li class="lancu" v="-1">全部</li>
     <li v="0">咨询</li>
     <li v="1">建议</li>
     <li v="2">投诉</li>
   </ul>
 </div>
 <div class="nav_table_lan">
   <ul>
     <li class="wtms">问题描述</li>
     <li class="twsj">提问时间</li>
     <li class="hfzt">回复状态</li>
     <li class="lll">浏览量</li>
   </ul>
 </div>
 <div class="datalist_box" id="serviceList">
 	<!-- 十条数据 -->
 </div>
</div>
<!--主体 end-->
<!--底部 start-->
	<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/customerService.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
