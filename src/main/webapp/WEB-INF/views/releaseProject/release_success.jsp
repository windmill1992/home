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
    <title>发布成功</title>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/releaseProjectpc.css">
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/releaseSucc.css">
    <script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head>
<body>
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->

<div class="relProject">
    <div class="relImg relImg3"></div>
    <div class="releaseSucc">
        <div id="qrcode" class="pr_ewm" style=""></div>
        <input type="text" id="getval" style="display:none" />
        <div class="succFr">
            <img src="<%=resource_url%>res/images/newReleaseProject/succ1.png" class="succFrimg">
            <div class="succFrtext">
                <p class="succP1">项目已提交审核  </p>
                <p class="succP2">工作人员会在一个工作日内审查完毕，审核结果会通过手机短信通知您，敬请关注</p>
            </div>
            <a href="<%=resource_url%>project/view.do?projectId=${projectId}" class="projectBtn" target="_blank">查看我发布的项目</a>
            <a href="<%=resource_url%>project/appealSecond.do?projectId=${projectId}" class="projectBtn" target="_blank">编辑项目</a>
            <img src="<%=resource_url%>res/images/newReleaseProject/list123.png" class="succFrimg1">
            <ul class="succUl">
                <li>通过审核（1个工作日)</li>
                <li style="padding-left: 23px;">分享项目，开始筹款</li>
                <li style="padding-left: 10px;">筹款成功，申请打款</li>
                <li style="padding-left: 6px;">24小时内打款</li>
            </ul>
        </div>    
    </div>        
</div>
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<input type="hidden" id="projectId" value="${projectId }"/>
<script type="text/javascript" src="<%=resource_url%>res/js/qrcode.js"></script>
<script data-main="<%=resource_url%>res/js/dev/newReleaseProject/releaseProjectSuccess.js" src="<%=resource_url%>res/js/require.min.js"></script>
<script>
		window.onload = function() {
			var projectId=$('#projectId').val();
			var qrcode = new QRCode(document.getElementById("qrcode"), {
				//width : 96,//设置宽高
				//height : 96
			});
			var url = 'http://www.17xs.org/project/view_h5.do?projectId='+projectId;
			qrcode.makeCode(url);
		}
	</script>
	
</body>
</html>