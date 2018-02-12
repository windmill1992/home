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
<title>开通专题</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<%-- <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/> --%>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/projectTopic/seminarSetup.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/projectTopic/seminar_success.css" />
</head> 
<body style="font-family: 微软雅黑">
<%@ include file="./../common/newhead.jsp" %>
<div class="page">
    <div class="page-bd">
    	<div class="uCEN-L">
			<c:choose>
			<c:when test="${user.userType=='enterpriseUsers'}">
         		<%@ include file="./../common/eleft_menu.jsp"%>
         	</c:when>
         	<c:otherwise>
         		<%@ include file="./../common/pleft_menu.jsp"%>
         	</c:otherwise>
         	</c:choose>
		</div>
        <div class="uCEN-R">
            <div class="library">
                <p class="libraryone">创建成功</p>
                <div class="cleartwo">
                    <img class="cleartwo_fl" src="<%=resource_url%>res/images/goodlibrary/succ1.png">
                    <div class="cleartwo_fr">
                        <p class="text1">恭喜您，您的专题页已创建成功~</p>
                        <p class="text2">微信扫一扫二维码查看^_^！</p>
                    </div>
                </div>
                <div id="qrcode" class="pr_ewm" style=""></div>
                <input type="text" id="getval" style="display:none" />
				<div class="success_lj">
					<p>我的专题详情链接：</p>
					<a href="http://www.17xs.org/projectTopic/gotoProjectTopicDetail.do?id=${projectTopicId }" target="view_window">http://www.17xs.org/projectTopic/gotoProjectTopicDetail.do?id=${projectTopicId }</a>
				</div>
            </div>
        </div>
    </div>
</div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="projectTopicId" value="${projectTopicId }"/>
<script type="text/javascript" src="<%=resource_url%>res/js/qrcode.js"></script>
<script data-main="<%=resource_url%>res/js/dev/projectTopic/projectTopic.js" src="<%=resource_url%>res/js/require.min.js"></script>
<script>
	window.onload = function() {
		var projectTopicId = $('#projectTopicId').val();
		var qrcode = new QRCode(document.getElementById("qrcode"), {
		width : 200,//设置宽高
		height : 200
		});
		var url = 'http://www.17xs.org/projectTopic/gotoProjectTopicDetail.do?id='
				+ projectTopicId;
		qrcode.makeCode(url);
	}
</script>
</body>
</html>
