<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
		<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
		<title>${project.title }</title>
		<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/messageWall/leaveWord.css"/>
	</head>
	<body style="background: #F2F2F2;padding-bottom: 70px;">
		
		<div class="dt-head fd-head">
			<div id="banner">
				<div class="hd">
					<ul></ul>
				</div>
				<div class="bd">
					<ul>
						<c:forEach items="${bannerList}" var="img">
							<li><a href="${img.linkUrl}"><img src="${img.url}" width="100%" /></a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		
		<div class="dt-body">
			<div class="profile clearFix">
				<ul class="data-list clearFix">
					<a><li><i>访客数</i><b><span class="num">${totalWordNum }人</span></b></li></a>
					<a><li><i>行善人次</i><b><span class="num">${project.donationNum }人</span></b></li></a>
					<a><li><i>募捐金额</i><b><span class="num">${project.donatAmount }元</span></b></li></a>
				</ul>
			</div>
			
			<div class="dt-content" id="dt-content">
				<h3>${project.title }<a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }" class="fr">></a></h3>
				<div class="content">
					${project.content }
				</div>
				<div class="detail-mask detail-mask-bg">
					<div class="lookmore">查看全文</div>
				</div>
			</div>
			
			<div class="dt-progress">
				<div class="progress-bar">
					<span class="track">
						<i class="fill"></i>
					</span>
				</div>
				<div class="progress-dest">
					<label>今日目标<span class="destination">10000</span>块砖，已完成<span class="complete">${todayDonateAmount }</span>块</label>
				</div>
			</div>
			
			<div class="dt-faqiren">
				<h3>今日访客<span class="visitors">${todayWordNum }位访客，${todayDonateCount }人为善园添砖</span></h3>
				<div class="faqiren-list"></div>
				
				<div class="loadmore">
					<a href="javascript:void(0);">点击加载更多</a>
				</div>
			</div>
			
			<div class="dt-users">
				<h3>共有<span class="userNum">${totalDonateNum }</span>位爱心人士为善园添砖加瓦<a href="http://www.17xs.org/together/record_view.do?projectId=${project.id }" class="usersList">></a></h3>
				<div class="user-pic">
					<ul>
						<c:forEach items="${donateHeadImg }" var="item">
							<li><img src="${item.coverImageurl }"/><p>${item.nickName }</p></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		
		<div class="dt-foot">
			<a href="http://www.17xs.org/message/messageWall_view.do?projectId=${project.id }" class="leaveWord btns"><img src="../../../../res/images/h5/images/messageWall/pen.png" width="25"/><br />留言板</a>
			<a href="javascript:;" class="btn">我为善园添砖</a>
			<a href="http://mp.weixin.qq.com/s/ZI5nAp-qAQ3kOdI00EosAA" class="look btns"><img src="../../../../res/images/h5/images/messageWall/logo-img.png" width="25"/><br />参观善园</a>
		</div>
		
		<div class="dt-dest">
			<p class="title">筹款目标</p>
			
			<div class="dt-money">
				<div class="zhuan-num">
					<a href="javascript:;" class="btn min fl">-</a>
					<a href="javascript:;" class="num2 fl">支持<span id="number2">10</span>块砖</a>
					<a href="javascript:;" class="btn add fl">+</a>
				</div>
			</div>
				
			<div class="total">
				合计<span id="total-money">16.80</span>元
			</div>
			
			<div class="userinfo">
				<p class="title">其他信息(非必填)</p>
				<div class="info">
					<p><input type="text" name="tel" id="realName" value="${user.realName }" placeholder="您的姓名" /></p>
					<p><input type="text" name="tel" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" /></p>
					<p><input type="text" name="tel" id="donateWord" value="" placeholder="${leaveWord }" /></p>
				</div>
			</div>
			
			<div class="toBtn">
				<a href="javascript:void(0);">立即捐款</a>
			</div>
			<i class="close5">&times;</i>
		</div>
		
		<div class="dt-selectTo">
			<div class="select">
				<a href="javascript:;" class="toDonate">我为善园添块砖</a>
				<a href="http://www.17xs.org/together/together_view.do?projectId=${project.id }" class="toInvite">邀朋友一起捐</a>
			</div>
		</div>
		<div id="overLay"></div>
		<input type="hidden" id="projectId" value="${project.id }"/>
		<input type="hidden" id="userId" value="${userId }"/>
		<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
		<script src="../../../../res/js/jquery-1.8.2.min.js"></script>
		<script type="text/javascript" src="../../../../res/layer/layer.js" ></script>
		<script src="../../../../res/js/common/TouchSlide.1.1.js"></script>
		<script src="../../../../res/js/h5/messageWall/detail.js"></script>
		<script>
			document.body.style.minHeight = screen.height+'px';
			TouchSlide({ slideCell:"#banner",titCell:".hd ul",mainCell:".bd ul",effect:"leftLoop",autoPlay:true,autoPage:true });
		</script>
	</body>
</html>