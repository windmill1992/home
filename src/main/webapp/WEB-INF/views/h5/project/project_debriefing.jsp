<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>${project.title }项目进展</title>
<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/projectDebrief/debriefing.css"/>
</head> 
	<body>
		<div id="pageContainer">
			<div class="page-wrapper" id="page-wrapper">
				<div class="pd-hd" id="proj-info">
					<a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }" class="projInfo flex">
						<div class="pic">
							<img src="${project.coverImageUrl }" width="100%" height="100%"/>
						</div>
						<div class="title flex1">
							<h3>${project.title }</h3>
							<p class="f12 cb2b">
								已有<span class="cff9">&nbsp;${project.donationNum }&nbsp;</span>名爱心网友参与捐款
							</p>
						</div>
						<div class="detail"></div>
					</a>
				</div>
				
				<div class="pd-bd" id="proj-process">
					<div class="pp-title">
						<h3>项目进展</h3>
					</div>
					<div class="pp-poster flex">
						<div class="pic">
							<img src="${feedback.headImageUrl }" width="100%" height="100%"/>
						</div>
						<div class="poster flex1">
							<h3 title="${feedback.nickName }">由&nbsp;${feedback.nickName }&nbsp;发布</h3>
							<%-- <p class="f12 cb2b">${feedback.userName }</p> --%>
							<p class="f12 cb2b"><fmt:formatDate value="${feedback.feedbackTime}" pattern="yyyy年MM月dd日 "/></p>
						</div>
					</div>
					<div class="pp-content">
						${feedback.content }
						<!-- <p>【湖北96岁老兵终于有了卫生间，好开心】近日，志愿者帮助96岁黄埔老兵王辉文，在家中修建一个9.3平方米的卫生间，就要完工了，目前热水器，
							马桶等均已安装好，爷爷再也不用跑到外面的公共厕所了，第一次使用淋浴喷头，老兵开心的笑了。王辉文爷爷无子女，一直独居，病痛缠身，需要拐杖行动不便。
						</p> -->
						<div class="imgList-wrap">
							<div class="imgList">
								<c:forEach items="${feedback.contentImageUrl }" var="contentImageUrls">
									<a href="javascript:;"><img src="${contentImageUrls }"/></a>
								</c:forEach>
							</div>
						</div>
					</div>
					<div class="pp-other">
						<a href="http://www.17xs.org/project/project_debriefDetail_view.do?projectId=${project.id }" class="btn-view">查看该项目其他进展</a>
					</div>
				</div>
				
				<div class="pd-ft">
					<img src="../../../../res/images/h5/images/min-logo.jpg"/>
				</div>
			</div>
		</div>
		
		<div id="overLay"></div>
		<div class="bigPic">
			<div id="banner">
				<div class="hd">
					<ul></ul>
				</div>
				<div class="bd"></div>
			</div>
		</div>
		<script src="../../../../res/js/jquery-1.8.2.min.js"></script>
		<script src="../../../../res/js/common/TouchSlide.1.1.js"></script>
		<script src="../../../../res/js/h5/projectDebrief/debriefing.js"></script>
		<script>
			document.getElementById('page-wrapper').style.minHeight = screen.height+'px';
		</script>
	</body>
</html>