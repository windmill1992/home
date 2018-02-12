<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
    <title>${activity.name }</title>
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/activity/activityTrackway.css">
</head>
<body>
    <div class="actAut">
<!--背景-->
<div class="trackBack">
    <img src="${activity.logoUrl }">
</div>
<!--详情介绍-->
<div class="trackContent">
    <div class="centerImg">
        <img src="${user.coverImageUrl }">
    </div>
    <h3><span>${user.nickName }</span>，${activity.welcomeLanguag }</h3>
    <div class="details">
    	${activity.content }
        <%-- <p>恐惧肯定v健康局势的考虑就是口技刷卡v惧肯定v健康局势的考虑就是口技刷卡v关键是看绿色可拒付款v及反馈率积分 捐款v能否打开v京东方科技惧肯定v健康局势的考虑就是口技刷卡v关键是看绿色可拒付款v及反馈率积分 捐款v能否打开v京东方科技惧肯定v健康局势的考虑就是口技刷卡v关键是看绿色可拒付款v及反馈率积分 捐款v能否打开v京东方科技惧肯定v健康局势的考虑就是口技刷卡v关键是看绿色可拒付款v及反馈率积分 捐款v能否打开v京东方科技惧肯定v健康局势的考虑就是口技刷卡v关键是看绿色可拒付款v及反馈率积分 捐款v能否打开v京东方科技关键是看绿色可拒付款v及反馈率积分 捐款v能否打开v京东方科技靠邻居放得开v减肥的</p>
        <img src="<%=resource_url%>res/images/h5/images/activity/psb.jpg"> --%>
    </div>
</div>
<!--白色渐变-->
<div class="hide" style="">
    <div class="btnHide"><a href="javaScript:void(0);">查看全文</a></div>
    <div class="white"></div>
</div>
<!--项目banner-->
<%--<div class="TrackBanner">
    <div class="bannerImg">
        <a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }"><img src="${project.coverImageUrl }"></a>
    </div>
    <a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }"><p class="bannerText">${project.title }</p></a>
</div>--%>
    <div class="donateItemBox">
        <h4 class="donateTitle clearfix">
            <span class="fl">捐助的项目</span>
            <span class="fr">还需捐款：${project.cryMoney-project.donatAmount }元</span>
        </h4>
        <div class="donateItemInfo">
            <a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }">
                <div class="clearfix">
                    <div class="donateItemImg">
                        <img src="${project.coverImageUrl }">
                    </div>
                    <div class="donateItemCon">
                        <h4>${project.title }</h4>
                     <p>${project.subTitle}</p>

                    </div>
                </div>
            </a>
    <div style="clear:both"></div>
             <p class="donateMoney">项目总筹款：${project.donatAmount }元&nbsp;&nbsp;${num }人捐款</p>
        </div>
    </div>
<!--记录-->
<div class="trackReccod">
    <div class="renshu">已得到<span>${num }</span>人支持</div>
    <div id="user_list">
    </div>
    <div class="jiazai">加载更多</div>
</div>
<!--button按钮-->
<!--button按钮-->
<div class="trackBtn">
    <a class="track_fl" href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">支持${activity.btnName }<%-- ${fn:substring(project.title ,0,10 )} --%></a>
    <%-- <a class="track_fr" href="http://www.17xs.org/project/view_share_h5.do?projectId=${project.id }&shareType=0"> --%><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon2.png">邀朋友一起参与</a>
<a class="track_fr" href="http://www.17xs.org/project/togetherDonate_view.do?projectId=${project.id }">
</div>
<div class=""></div>
    </div>
<input type="hidden" id="currentPage" />
<input type="hidden" id="projectId" value="${project.id }"/>
<script data-main="<%=resource_url%>res/js/h5/activity/activity.js?v=201510131033" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>