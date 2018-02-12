<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体" />
<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人" />
<title>${projectTitle}</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/project/wish.css"/>
</head>
<body>
	<div id="pageContainer" v-cloak>
		<div class="head">
			<a href="javascript:;" onclick="history.go(-1);" class="back"></a>
			<span>捐赠详情</span>
		</div>
		<div class="wpd-hd">
			<a href="/project/view_h5.do?projectId=${projectId}">
				<div class="project">
					<div class="pic">
						<img src="${projectLogo!=null?projectLogo:'/res/images/logo-def.jpg'}" />
					</div>
					<div class="info">
						<div class="title">
							<p>${projectTitle}</p>
						</div>
						<div class="support">
							支持人数 ：<span class="num">${donationNum}</span>
						</div>
						<div class="support">
							受助人数 ：<span class="num">${count}</span>
						</div>
					</div>
				</div>
			</a>
			<div class="other">
				<span class="left">发起人 : </span>
				<span class="unit">${realName}</span>
			</div>
			<div class="other">
				<span class="left">善款接收 : </span>
				<span class="unit">宁波市善园公益基金会
					<a href="/resposibilityReport/resposibilityReport.do?id=368">
						<img src="/res/images/h5/images/donateStep/wen.png"/>
					</a>
				</span>
			</div>	
		</div>
		
		<div class="wpd-bd">
			<div class="wishes" id="wishList">
				<div class="item" :data-id="wish.id" v-for="wish in wishList">
					<div class="top flex spb fcen">
						<div class="t-left">
							<input type="checkbox" name="wish" :id="'wish'+wish.id" class="chk" />
							<label :for="'wish'+wish.id"><strong>{{wish.introduction}}</strong></label>
						</div>
						<div class="t-right">
							<span class="price">￥{{wish.total_money}}</span>
						</div>
					</div>
					<div class="middle">
						<p>{{wish.name}}，{{wish.age}}岁<template v-if="wish.location!=null && wish.location.trim()!=''">，{{wish.location}}</template></p>
						<a href="javascript:;" class="down"></a>
					</div>
					<div class="foot">
						<p class="title">{{wish.introduction}}</p>
					</div>
				</div>
			</div>
			<template v-if="hasMore == 2">
				<a href="javascript:;" class="load-more cff6" id="loadmore">点击查看更多</a>
			</template>
			<template v-else-if="hasMore == 1">
				<a href="javascript:;" class="load-more c999">没有更多数据了</a>
			</template>
			<template v-else-if="hasMore == 0">
				<a href="/" class="load-more cff6">暂无心愿单~去看看其他项目吧</a>
			</template>
			<div class="user-info">
				<input type="text" id="realName" placeholder="您的姓名" value="${user.realName}" />
				<input type="text" id="mobile" placeholder="您的电话" value="${user.mobileNum}" />
				<textarea id="leaveWords" placeholder="${leaveWord}"></textarea>
			</div>
		</div>
		
		<div class="wpd-ft" v-if="hasMore==1 || hasMore==2">
			<div class="other-op flex spb" id="otherOp">
				<a href="javascript:;" class="donate-num" data-num="0" id="selAll">全选</a>
				<a href="javascript:;" class="donate-num" data-num="1">随机选1个</a>
				<a href="javascript:;" class="donate-num" data-num="2">随机选2个</a>
			</div>
			<div class="kong"></div>
			<div class="btn flex spb">
				<a href="javascript:;">合计：<span id="total">0</span>元</a>
				<a href="javascript:;" id="donate" class="disable">
					<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">立即捐赠
				</a>
			</div>
		</div>
		<div id="tips"></div>
	</div>
	<div id="nopage">
		<div class="head">
			<a href="javascript:;" onclick="history.go(-1);" class="back"></a>
			<span>捐赠详情</span>
		</div>
		<p>项目不存在！</p>
	</div>
	<script src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="/res/js/h5/donateStep/vue.min.js"></script>
	<script src="/res/js/h5/project/wish.js?v=20171207"></script>
</body>
</html>