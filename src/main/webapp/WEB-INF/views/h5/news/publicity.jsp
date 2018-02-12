<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
	<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
	<title>公告</title>
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/mui.min.css"/>
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/news/synav.css"/>
	<script type="text/javascript" src="../../../../res/js/jquery-1.8.2.min.js" ></script>
	<script type="text/javascript" src="../../../../res/js/h5/mui.min.js" ></script>
	<script type="text/javascript" src="../../../../res/js/common/time.js" ></script>
</head>
<style>
	.mui-scroll-wrapper {top: 90px;}
	.mui-content{background:#fff}
</style>
<body>
	<header>
		<div class="logo-head">
			<a href="http://www.17xs.org"><img src="../../../../res/images/h5/images/news/logo-lt.jpg" height="100%"/></a>
		</div>
	</header>
	<nav>
		<div class="nav">
			<ul>
				<a href="http://www.17xs.org/h5News/news_view.do?type=慈善知识" id="cszs"><li>慈善知识</li></a>
				<a href="http://www.17xs.org/h5News/news_view.do?type=善园快讯" id="sykx"><li>善园快讯</li></a>
				<a href="http://www.17xs.org/h5News/news_view.do?type=活动" id="hd"><li>活动</li></a>
				<a href="http://www.17xs.org/h5News/news_view.do?type=公告" id="gonggao"><li class="on">公告</li></a>
				<a href="http://www.17xs.org/h5News/news_view.do?type=爱心故事" id="axgs"><li>爱心故事</li></a>
			</ul>
		</div>
	</nav>
	<div class="news-body mui-content mui-scroll-wrapper scroll">
		<div class="news-list scroll-list mui-scroll">
			<p class="load-more">上拉加载更多>></p>
		</div>
		
		<div id="pullUp">
    		<span class="pullUpIcon"></span>
    		<span class="pullUpLabel"></span>
	    </div>
	</div>
	<input type="hidden" value="1" id="currentPage" />
	<input type="hidden" value="${type }" id="type" />
	<!-- <script type="text/javascript" src="../../../../res/js/jquery-1.8.2.min.js" ></script> -->
	<script data-main="../../../../res/js/h5/news/synav.js?v=201706091009" src="../../../../res/js/require.min.js"></script>
</body>
</html>