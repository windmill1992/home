<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园网</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/projectTopic/seminarPublicGood.css">
</head>
<body>
	<div class="seminPubGood">
<!--头部-->

<!-- <div class="header">
    <p class="head_p">善园公益</p>
</div> -->
<!--banner-->
<div class="seminar_banner">
    <!--banner背景图-->
    <img class="banner_img" src="${topic.toppicPicUrl }" alt="">
    <div class="sembackground">
        <div class="sem_p1"><span></span><p>
        <c:choose>
        	<c:when test="${fn:length(topic.topicName)>20 }">
        		${fn:substring(topic.topicName ,0,20 )}
        	</c:when>
        	<c:otherwise>
        		${topic.topicName }
        	</c:otherwise>
        </c:choose>
        </p></div>
        <p class="sem_p2">
        <c:choose>
        	<c:when test="${fn:length(topic.topicInfo)>95 }">
        		${fn:substring(topic.topicInfo ,0,95 )}...
        	</c:when>
        	<c:otherwise>
        		${topic.topicInfo }
        	</c:otherwise>
        </c:choose>
        </p>
        <img class="semImg" src="<%=resource_url%>res/images/projectTopic/sem_back.png" alt="">
    </div>
</div>
<!--类目-->
<div class="seminar_list">
    <li>
        <p class="seminarMoney">募集善款</p>
        <p clss="seminarPerson"><span>${donate.goodHelpAmount==null?0:donate.goodHelpAmount }</span>元</p>
    </li>
    <li class="borRight">
        <p class="seminarMoney">捐款人次</p>
        <p clss="seminarPerson"><span>${donate.goodHelpCount }</span>人</p>
    </li>
</div>
<!--内容-->
<div class="seminar_content">
    <%-- <a href="javascript:void();">
        <div class="seCont">
            <h4>公益组织创新工坊公益组织创新工坊公益组织创新工坊公益组织创新工坊</h4>
            <div class="seTenter">
                <div class="seImgfl"><img src="<%=resource_url%>res/images/h5/images/projectTopic/list_banner.png"></div>
                <div class="seFr">
                    <p class="seText1">目标金额：10000元</p>
                    <p class="seText1">已筹金额：10000元</p>
                    <div class="setPr">
                    <div class="sePerson"><i class="seIcon"></i><p class="seIcon_fr">共&nbsp;<span>12345678</span>&nbsp;人捐助</p></div>
                    <a href="#" class="sePersonfr">立即捐助</a>
                    </div>
                </div>
            </div>
            <div style="clear: both"></div>
        </div>
    </a> --%>
</div>
<a href="javascript:void(0);" class="jzgd">点击加载更多</a>
    <div class="topBtn">
        <a href="<%=resource_url%>project/batch_list.do?topicId=${topic.id }">批量捐</a>
    </div>
</div>
<input type="hidden" id="topicId" value="${topic.id }" />
<inout type="hidden" id="currentPage" />
<script data-main="<%=resource_url%>res/js/h5/projectTopic/projectTopic.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>