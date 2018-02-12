<%@page import="com.guangde.home.utils.UserUtil"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园基金会</title>
<link rel="stylesheet" type="text/css" href="/res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/dev/userCenter.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer userCenter mygood">
	<div class="mygood">
    	<div class="uCEN-hd"><h2 class="w1000">&nbsp;王小二，您好！欢迎回来！这里是您的捐款人页面 : ) </h2></div>
        <div class="uCEN-bd">
        	<div class="uCEN-bdL">
            	<div class="uCEN-user">
                	<div class="uCEN-user-show"><i class="user-icon-gt">
                	   <% 
		                   String url = UserUtil.getUserHead(request, response);
		                   if(url!=null){
		                     out.print("<img src='"+url+"' width='120px' height='120px'>");
		                   }
		                %>
                	</i></div>
                    <div class="uCEN-user-info">
                    	Hi王小二
                        ，<br/>在过去的198天里
                        <br/>您一共参与了<i class="em-color">4个项目</i>
                        <br/>您总共捐赠了<i class="em-color">1800元</i>
                        <br/>高于<i class="em-color">90%</i>的广德网友
                        <br/><i class="biger em-color">爱心爆棚！</i>
                    	<!--Hi王小二，<br/>在过去的198天里
                        <p>您一共参与了<i class="em-color">4个项目</i></p>
                        <p>您总共捐赠了<i class="em-color">1800元</i></p>
                        <p>高于<i class="em-color">90%</i>的广德网友</p>
                        <p class="biger em-color">爱心爆棚！</p>-->
                    </div>
                </div>
                <ul id="uCENNav" class="uCEN-nav"> 
                	<li class="uCENCur">捐款明细</li>
                    <li>消息与通知</li>
                    <li>善管家</li>
                    <li>实名认证</li>
                </ul> 
            </div>
            <div class="uCEN-bdR">
            	<div class="uCEN-list list">
                	<div class="list-hd">
                    	<span class="list-tit">您捐赠的项目列表</span>
                    	<dl class="select">
                        	<dt class="sel-checked">
                            	<span class="sel-val">一个月内</span><span class="triangle"><i class="sel-dir"></i></span>
                            	<input class="checked" type="hidden" value=""/>
                            </dt>
                            <dd class="sel-options">
                            	<a class="sel-option sel-check">一个月内</a>
                                <a class="sel-option">三个月内</a>
                                <a class="sel-option">半年内</a>
                                <a class="sel-option">一年内</a>
                            </dd>
                        </dl>
                    	
                    </div>
                    <div class="list-bd">
                        <div class="list-data">
                             	<!--<div class="list-prompt">提示信息</div>-->
                        	 <ul class="list-data-hd">
                             	<li class="lst-col1">项目名称</li>
                                <li class="lst-col2">捐赠时间</li>
                                <li class="lst-col3">捐赠金额</li>
                                <li class="lst-col4">捐款形式</li>
                                <li class="lst-col5">项目反馈</li>
                             </ul>
                             <div class="list-data-bd">
                             	<ul>
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5 lst-br">募款进行中<br/>暂无反馈</li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul>
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul>
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul>
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul>
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul>
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                                <ul class="bgcolor1">
                                    <li class="lst-col1">&nbsp;<a href="javascript:void(0);">014号公益人韩思远“光头挑战”接力赛</a></li>
                                    <li class="lst-col2">2015-05-12</li>
                                    <li class="lst-col3">10.00元</li>
                                    <li class="lst-col4">普通捐款</li>
                                    <li class="lst-col5"><a href="javascript:void(0);">点击查看</a></li>
                                </ul>
                             </div>
                        </div>
                        <div class="list-page">
                        	<a class="nomrgL" href="javascript:void(0);">上一页</a>
                            <i class="curPage">1</i><i>2</i><i>3</i><i>4</i><i>…</i><i>9</i>
                            <a href="javascript:void(0);">下一页</a>
                        </div>
                        <!--<div class="list-page">
                        	<input type="button" value="上一页">
                            <i>1</i><i>2</i><i>3</i><i>4</i><i>…</i><i>9</i>
                            <input type="button" value="下一页">
                        </div>-->
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/mygood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
