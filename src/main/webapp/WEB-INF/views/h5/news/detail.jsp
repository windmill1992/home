<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
	<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
	<title>${type }</title>
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/news/synav.css"/>
</head>
<body>
		
	<header>
		<div class="new-head">
			<a href="javascript:;" onclick="self.location=document.referrer;" class="back"><</a>
			<span>${type }</span>
		</div>
	</header>
	<div class="new-body">
		<h2>${news.title }</h2>
		<p class="pub-time">发布时间  : <span class="time"><fmt:formatDate value="${news.createtime }" pattern="yyyy-MM-dd"/></span></p>
		<div class="summary">
			摘要:${news.abstracts }
		</div>
		<div class="news-detail">
			<!-- <div class="pic">
				<img src="" width="100%" />
			</div> -->
			<div class="content">
				${news.content }
				<br />
				<c:set value="${ fn:split(videos, ',') }" var="video" />
				<c:forEach items="${ video }" var="v">
					<p class="pic">
						<iframe src="${v}" allowfullscreen="" frameborder="0" width="100%" height="400"> </iframe>
					</p>
				</c:forEach>
				<%-- <c:forEach items="${videos}" var="video">
					<p class="pic">
						<iframe src="${video.description}" allowfullscreen="" frameborder="0" width="100%" height="400"> </iframe>
					</p>
				</c:forEach> --%>
			</div>
		</div>
		
	</div>
</body>
</html>