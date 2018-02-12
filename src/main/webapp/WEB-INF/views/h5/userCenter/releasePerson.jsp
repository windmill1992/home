<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ include file="./../../common/file_url.jsp" %>
    <title>发起人身份验证</title>
    <link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css">
    <link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/personTest.css">
</head>
<body>
	<div class="detail_head">
		<a href="javascript:;" class="to-index" onclick="history.go(-1);">返回</a>
		<div class="detailconter">
			<img src="/res/images/releaseProject/det_log.png">
			<span>发起人信息</span>
		</div>
		<div class="detailfr">
			<a href="/test/gotoProtocolRelease.do">
				<img src="/res/images/releaseProject/det_pr.png">
				<span>发布</span>
			</a>
		</div>
	</div>
	<div class="personTest">
	    <div class="initTest">
	        <img src="${user.coverImageUrl }">
	        <span>${user.nickName }</span>
	    </div>
		<c:if test="${userInfo.realName != null}"> 
		    <div class="person_one">
		        <div class="perone_fl">发布人姓名：<span>${userInfo.realName }</span></div>
			        <div class="perone_fr">
			        <c:choose>
			        	<c:when test="${user.realState==203 }">
			        		<i class="yyz detImg1"></i>
			        		<span>已验证</span>
			        	</c:when>
			        	<c:otherwise>
			        		<i class="yyz detImg2"></i>
			        		<span>未验证</span>
			        	</c:otherwise>
			        </c:choose>
				</div>
		    </div>
	   </c:if>
	    <div style="clear:both"></div>
	   <c:if test="${userInfo.indetity != null}"> 
		    <div class="person_two">
		        身份证号：<span>${userInfo.indetity }</span>
		    </div>
		    <div class="person_two">
	        	身份证照片：<span>已上传</span>
	    	</div>
	    </c:if>
	   <c:if test="${userInfo.linkMobile != null}"> 
		    <div class="person_two">
		        联系人电话：<span>${userInfo.linkMobile }</span>
		    </div>
	    </c:if>
	   <c:if test="${userInfo.relation != null}">  
		    <div class="person_one">
		        <div class="perone_fl">您与受助人的关系：<span>${userInfo.relation }</span></div>
		        <div class="perone_fr">
		        <c:choose>
		        <c:when test="${project.state==240||project.state==260 }"><i class="yyz detImg1"></i><span>已核实</span></c:when>
		        <c:otherwise><i class="yyz detImg2"></i><span>未核实</span></c:otherwise></c:choose></div>
		    </div>
	 	</c:if>
	    <div class="person_two">
	        关系证明：<span>已上传</span>
	    </div>
	    <!--按钮-->
	    <a href="http://www.17xs.org/user/userAdvice.do"><div class="person_btn">我要质疑</div></a>
	</div>
</body>
</html>