<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<title>受助人身份验证</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/personTest.css">
</head>
<body>
	<div class="detail_head">
		<a href="javascript:;" class="to-index" onclick="history.go(-1);">返回</a>
		<div class="detailconter">
			<img src="/res/images/releaseProject/det_log.png">
			<span>受助人信息</span>
		</div>
		<div class="detailfr">
			<a href="/test/gotoProtocolRelease.do">
				<img src="/res/images/releaseProject/det_pr.png">
				<span>发布</span>
			</a>
		</div>
	</div>
	<div class="personTest">
		<c:if test="${userInfo.linkMan != null}">
			<div class="person_one">
				<div class="perone_fl">真实姓名:<span>${userInfo.realName }</span></div>
				<c:choose>
					<c:when test="${project.state == 240 || project.state == 260}">
						<div class="perone_fr"><i class="yyz detImg1"></i><span>已验证</span></div>
					</c:when>
					<c:otherwise>
						<div class="perone_fr"><i class="yyz detImg2"></i><span>未验证</span></div>
					</c:otherwise>
				</c:choose>
			</div>
			<div style="clear:both"></div>
		</c:if>
		<c:if test="${userInfo.indetity != null}">
			<div class="person_two">
				身份证号:<span>${userInfo.indetity }</span>
			</div>
		</c:if>
		<c:if test="${userInfo.linkMobile != null}">
			<div class="person_two">
				联系人电话:<span>${userInfo.linkMobile }</span>
			</div>
		</c:if>
		<c:if test="${userInfo.realName != null}">
			<div class="person_two">
				身份证照片:<span>已上传</span>
			</div>
		</c:if>
		<c:if test="${project.cryCause != null}">
			<div class="person_one">
				<div class="perone_fl">求助的原因：<span></span></div>
				<div class="perone_fr"><i class="yyz detImg1"></i><span>已核实</span></div>
			</div>
			<div style="clear:both"></div>
		</c:if>
		<c:if test="${userInfo.familyAddress != null}">
			<div class="person_two">
				当前所在地：<span>${userInfo.familyAddress }</span>
			</div>
		</c:if>
		<div class="person_two">
			相关证明：<span>已上传</span>
		</div>
		<!--按钮-->
		<a href="http://www.17xs.org/user/userAdvice.do">
			<div class="person_btn">我要质疑</div>
		</a>
	</div>
</body>
</html>