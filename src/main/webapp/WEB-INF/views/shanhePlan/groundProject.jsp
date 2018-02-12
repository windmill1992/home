<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport" />
<title>善园-善荷计划</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
</head>

<body>
	<%@ include file="./../common/newhead.jsp" %>
	<!--主体 start-->
	<div class="bodyNew">
		<div class="w1000 breadcrumb">
			当前位置：
			<a href="http://www.17xs.org/" title="">首页</a> > 落地项目
		</div>
		<div class="w1000 aboutNew">
			<!--left start-->
			<%@ include file="./../common/shanhe.jsp" %>
			<!--left end-->
			<!--right start-->
			<div class="aNRight aboutCon">
				<div class="boederbox">
					<h2>
		            	<span class="lText">落地项目</span>
		           </h2>
		           <p>暂无</p>
				</div>
			</div>
			<!--right end-->
		</div>
	</div>
	<!--主体 end-->
	<%@ include file="./../common/newfooter.jsp" %>
	<script data-main="<%=resource_url%>res/js/dev/shanhe.js" src="<%=resource_url%>res/js/require.min.js"></script>
	<script>
		(function() {
			var bp = document.createElement('script');
			var curProtocol = window.location.protocol.split(':')[0];
			if(curProtocol === 'https') {
				bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
			} else {
				bp.src = 'http://push.zhanzhang.baidu.com/push.js';
			}
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(bp, s);
		})();
	</script>
</body>
</html>