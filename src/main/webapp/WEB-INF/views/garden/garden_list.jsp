<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport" />
<title>善园 - 善园众筹</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodGarden.css" />
</head>
<body>
<%@ include file="./../common/head.jsp" %>
	<div class="bodyer goodGarden">
		<div class="page">
			<div class="page-hd">
				<div class="page-tit">
					<h2 class="w1000">
						善园众筹
					</h2>
				</div>
			</div>
			<div class="page-bd">
				<div class="gdGrd-ad">一砖一瓦写历史，一草一木绘风景。和谐善园，你我共建！让我们一起行善，让爱与温暖携手并进！</div>
				<div class="gdGrd-post">
					<i>最新认捐信息：</i>
					<div id="rollBox" class="post-list">
						<ul id="rollList">
							<c:forEach items="${record}" var="data" >
								<li><a href="<%=web_url%>project/newGardenView.do?projectId=${data.projectId}">"${data.userName}"认捐了${data.projectTitle}，份数：${data.donateCopies}，总金额<fmt:formatNumber value="${data.donatAmount}" pattern="0.00"/>元</a></li>
							</c:forEach>
						</ul>
					</div>
				</div>
				<div class="gdGrd-data">
					<ul id="data-list" class="data-list">
						<li class="prompt loading"></li>
						</ul>
				</div>
				<div class="gdGrd-honor">
                	<h3 class="honor-tit">捐赠爱心企业&amp;个人</h3>
                	<div class="honor-txt"></div>
            	</div>

			</div>
		</div>
	</div>
<%@ include file="./../common/footer.jsp" %>
	<script data-main="<%=resource_url%>res/js/dev/goodGarden.js"
		src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
