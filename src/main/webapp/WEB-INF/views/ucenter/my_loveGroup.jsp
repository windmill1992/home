<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="cache-control" content="no-store" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" />
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/helpList.css" />	
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head>
<body>
	<%@ include file="./../common/newhead.jsp"%>
	<div class="bodyer userCenter myloveGrp">
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
					<c:if test="${mylovegroupStatus == 2}">
						<div class="uCEN-list">
							<div class="uCEN-hd">
								<span class="uCEN-tit">善管家</span>
								<p class="uCEN-post">亲，是时候为善园基金会项目把关啦！</p>
							</div>
							<div class="uCEN-bd">
								<div class="tab">
									<ul class="tab-hd">
										  <li type="1">待办事务</li>
										  <li type="2">审核中（<em id="audits">${audits}</em>）</li>
										  <li type="3">执行中（<em id="back">${collect}</em>）</li>
										  <li type="5">未通过（<em id="collect">${back}</em>）</li>
										  <li type="6">已结束（<em id="end">${end}</em>）</li>
										  <li type="7" class="last">全部</li> 
									</ul>
									<div id="list" class="tab-bd">
										<div class="prompt">
											<div class="loading"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:if>
					<c:if test="${mylovegroupStatus == 1}">
						<div class="lgl-state yh">
							<em></em>善管家认证进入审核中...
						</div>
					</c:if>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden">
	<%@ include file="./../common/newfooter.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/myloveGroup.js"
		src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
