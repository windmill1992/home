<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
		<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
		<title>捐赠记录</title>
		<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/messageWall/leaveWord.css"/>
	</head>
	<body>
		<div id="pageContainer">
			<!--<div class="rc-head">
				<a href="javascript:void(0);" onclick="history.go(-1)" class="back"></a>
				<h3>捐赠记录</h3>
			</div>-->
			
			<div class="rc-bd">
				<div class="faqiren-list" id="recordList"></div>
				
				<div class="loadmore">
					<a href="javascript:void(0);">点击加载更多</a>
				</div>
			</div>
		</div>
		<input type="hidden" id="projectId" value="${projectId }"/>
		<script src="../../../../res/js/jquery-1.8.2.min.js"></script>
		<script src="../../../../res/js/h5/messageWall/record.js"></script>
	</body>
</html>