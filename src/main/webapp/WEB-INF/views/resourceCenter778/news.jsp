<!DOCTYPE html>
<html lang="zh-cmn-Hans">
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<%@ page contentType="text/html;charset=UTF-8" language="java" %>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<meta name="keywords" content=""/>
		<meta name="description" content=""/>
		<meta name="viewport"/>
        <meta http-equiv="x-ua-compatible" content="IE=8,IE=9,IE=10,IE=11,Chrome=1">
		<title>中心大事件</title>
		<link rel="shortcut icon" href="/res/images/favicon.ico">
		<link rel="stylesheet" type="text/css" href="/res/css/dev/resourceCenter778/res_center.css"/>
    </head>
   <body>
		<div class="pageContainer ">
			<div class="res-head w100p layout">
				<div class="w1200 layout">
					<div class="w100p rh-txt">
						<span>您好，欢迎来到宁波778创业资源中心</span>
					</div>
				</div>
			</div>
			<div class="res-nav w1200 layout">
				<div class="logo fl layout">
					<a href="/778/index.do"><img src="/res/images/resourceCenter778/logo.png"/></a>
					<span class="line"></span>
					<span class="text">宁波778创业资源中心</span>
				</div>
				<div class="t-nav fr layout">
					<ul id="nav">
						<li>
							<a href="/778/index.do"><h2>网站首页</h2></a>
						</li>
						<li class="${type=='中心大事件'?'active':'' }">
							<a href="/778/news.do?type=中心大事件"><h2>中心大事件</h2></a>
						</li>
						<li class="${type=='公益科普'?'active':'' }">
							<a href="/778/news.do?type=公益科普"><h2>公益科普</h2></a>
						</li>
						<li>
							<a href="/778/about.do"><h2>关于我们</h2></a>
						</li>
						<li>
							<a href="/778/relation.do"><h2>联系我们</h2></a>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="res-body">
				<div class="position w1200 layout">
					<p>当前位置：<a href="/778/index.do">首页</a>><span class="title">${type }</span></p>
				</div>	
				<div class="be-list w1200 layout">
					<div class="title">
						<span>${type }</span>
					</div>
					<div class="event-list layout"></div>
					<div class="loadmore"><a href="javascript:;">点击加载更多</a></div>
				</div>
			</div>
			
			<div class="res-footer layout">
				<div class="w1200 layout">
					<div class="text layout">
						<p>联系电话：0574-87412436&nbsp;&nbsp;&nbsp;&nbsp;地址：宁波市鄞州区泰康西路399号（宁波·善园）</p>
						<p>Copyright © 宁波市善园公益基金会 版权所有 浙ICP备15018913号-1  杭州智善网络科技有限公司  提供技术支持</p>
					</div>
				</div>
			</div>
		</div>
		
		<input type="hidden" id="type" value="${type }">
		
		<script src="/res/js/jquery-1.8.2.min.js"></script>
		<script src="/res/js/dev/resourceCenter778/res_center.js"></script>
	</body>
</html>