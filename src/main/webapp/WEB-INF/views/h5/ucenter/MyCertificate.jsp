<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>善园 - 一起行善，温暖前行！</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/userBinding.css?v=20180125">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<style type="text/css">
	#ceName {
		text-decoration: underline;
	}
	.ceInfo p span {
		color: #ee7a00;
	}
	.ceInfo {
		position: absolute;top: 1.95rem;left: 1.2rem;width: 4.7rem;line-height: 1.5;
	}
	.ceBot {
		position: absolute;right: 1.2rem;bottom: 0.9rem;text-align: right;
	}
	.ceBot span {
		text-decoration: underline;
	}
</style>
</head>
<body style="background-color: #f4f3f0;">
	<div class="popup" id="share" style="display: none">
		<div class="investTip">
			点击右上角，赶紧和朋友分享您获得的荣誉吧！<span class="point"></span>
		</div>
	</div>
	<div class="topBg">
		捐赠证书<a href="/ucenter/userCenter_h5.do" class="topBack"></a>
	</div>
	<div class="certificateBox">
		<div class="certificateInfo">
			<div class="ceInfo">
				<p id="ceName">${user.nickName}&nbsp;&nbsp;&nbsp;</p>
				<p style="text-indent: 2em;"> 感谢您对公益事业的关心和支持，截止<span> ${year} </span>年<span> ${month} </span>月，您在善园网已行善<span> ${donationNum} </span>次，捐助善款<span> ${totalAmount} </span>元，共帮助了<span> ${myDonatePro} </span>户家庭。</p>
				<p>特此颁证，深表谢意!</p>
			</div>
			<div class="ceBot">
				<p>宁波市善园公益基金会</p>
				<p><span> ${year} </span>年<span> ${month} </span>月<span> ${day} </span>日</p>
			</div>
		</div>
	</div>
	<div class="cedonateList">
		<div class="title">捐助过的项目
			<a href="/ucenter/myDonate.do?userId=${user.id}" class="lookAllBtn">查看全部</a>
		</div>
		<ul class="cedonateItem clearfix">
			<c:forEach items="${list}" var="item">
				<li>
					<c:choose>
						<c:when test="${item.field == 'garden'}">
							<a href="http://www.17xs.org/project/gardenview_h5.do?projectId=${item.projectId}">
								<img src="${item.coverImageurl}">
								<p>${item.projectTitle}</p>
							</a>
						</c:when>
						<c:otherwise>
							<a href="/project/view_h5.do?projectId=${item.projectId}">
								<img src="${item.coverImageurl!=null?item.coverImageurl:'/res/images/logo-def.jpg'}">
								<p>${item.projectTitle}</p>
							</a>
						</c:otherwise>
					</c:choose>
				</li>
			</c:forEach>
		</ul>
	</div>
	<div class="messageBoard">
		<h2 class="title">留言板</h2>
		<div class="roadMoreBox" style="margin-top: 0">
			<a href="javascript:;" class="loadMoreA no" id="loadMoreA">暂无留言信息</a>
		</div>
	</div>
	<c:if test="${browser == 'wx'}">
		<div class="donateBtn flex spb">
			<c:if test="${shareType == 1}">
				<a href="/ucenter/myCertificate.do?shareType=0&userId=${user.id}" class="left-btn">分享一下</a>
			</c:if>
			<c:if test="${shareType == 2}">
				<a href="/index/index_h5.do" class="left-btn">我要行善</a>
			</c:if>
		</div>	
	</c:if>
	
	<input type="hidden" value="${shareType}" id="shareType">
	
	<script src="/res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		$(function() {
			var shareType = $('#shareType').val();
			if(shareType == 0) {
				$('#share').show();
			} else {
				$('#share').hide();
			}
		});
		wx.config({
			debug: false,
			appId: '${appId}',
			timestamp: '${timestamp}',
			nonceStr: '${noncestr}',
			signature: '${signature}',
			jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline']
		});
		wx.ready(function() {
			wx.onMenuShareAppMessage({
				title: '${user.nickName}的捐赠证书', // 分享标题
				desc: '${desc}', // 分享描述
				link: 'http://www.17xs.org/ucenter/myCertificate.do?userId=${user.id}&shareType=2', // 分享链接
				imgUrl: '${user.coverImageUrl}', // 分享图标
				type: 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
				success: function() {
					//用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/ucenter/myCertificate.do?userId=${user.id}&shareType=2';
				},
				cancel: function() {
					//用户取消分享后执行的回调函数
				}
			});
			wx.onMenuShareTimeline({
				title: '${user.nickName}的捐赠证书', // 分享标题
				link: 'http://www.17xs.org/ucenter/myCertificate.do?userId=${user.id}&shareType=2', // 分享链接
				imgUrl: '${user.coverImageUrl}', // 分享图标
				success: function() {
					// 用户确认分享后执行的回调函数
					location.href = 'http://www.17xs.org/ucenter/myCertificate.do?userId=${user.id}&shareType=2';
				},
				cancel: function() {
					// 用户取消分享后执行的回调函数
				}
			});
		});
	</script>
</body>
</html>