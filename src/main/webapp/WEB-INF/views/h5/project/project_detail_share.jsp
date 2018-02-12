<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园网 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/base.css?v=201512111">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/donate.css?v=201512111">
	<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
	<script type="text/javascript">
	   console.log(document.documentElement.clientWidth);
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body>
	<div class="payTips" id="notiesInfo" style="display: none">
		<div class="investTip">
			点击右上角，邀请朋友一起参与公益<span class="point"></span>
		</div>
	</div>
	<div class="payTips" id="notiesInfo2" style="display: none">
		<div class="investTip2">分享成功</div>
	</div>
	<div class="container" style="background-color: #f4f3f0;">
		<div class="siteTopBox">
			<img src="${extensionUser.coverImageUrl }" class="topUserImg">
			<p class="topUserName">${extensionUser.nickName } 邀您一起行善</p>
			<p class="topUserInfo">${project.launchExplain }</p>
		</div>
		<div class="siteMainBox">
			<div class="basicInfoBox">
				<img src="<%=resource_url%>res/images/h5/images/donate.png" class="yqjPic">
				<div class="clearfix">
					<div class="basicInfo basicInfoLeft">
						<span><i>${peopleNum }</i>人</span>
						<span>参与人次</span>
					</div>
					<div class="basicInfo basicInfoRight">
						<span><i>${extensionDonateAmount }</i>元</span>
						<span>捐款金额</span>
					</div>
				</div>
<!--				<p class="goalMoney">捐款目标：<span>${project.cryMoney }</span>元</p>-->
				<ul class="personList">
					
					<c:forEach items="${drList}" varStatus="status" var = "drUser">
							<li><img src="${drUser.coverImageurl }"></li>
					</c:forEach>
				
				</ul>
				<p class="donateAll">共有 <i>${peopleNum }</i> 位小伙伴一起参与</p>
			</div>
			<div class="donateItemBox">
				<h3 class="donateTitle clearfix">
					<span class="fl">捐助的项目</span>
					<span class="fr">已募集：<fmt:formatNumber type="number"  pattern="#,##0" value = "${project.donatAmount }"/>元</span>
				</h3>
				<div class="donateItemInfo">
					<a href = "http://www.17xs.org/project/view_h5.do?projectId=${project.id }&extensionPeople=${extensionUser.id}">
					<div class="clearfix">
						<div class="donateItemImg">
							<img src="${project.coverImageUrl }">
						</div>
						<div class="donateItemCon">
							<h3>${project.title }</h3>
						<!--	<p>${project.content }</p>-->
							<p>${project.subTitle}</p>
						<p class="donateMoney">项目总筹款：${project.donatAmount }元&nbsp;&nbsp;${project.donationNum }人捐款</p>
						</div>

					</div>
					</a>
				</div>
	<p class="donateNumber"><span class="numberSp1">已筹${extensionDonateAmount }元</span><span class="numberSp2">/目标${project.pointMoney }元</span></p>
	<!--进度信息//-->
	<c:choose>
		<c:when test="${shareType==1 }">
			<div class="progress">
			<div class="progress_bar"><span  class="track"><i style="width:${processPoint>100?100:processPoint}%" class="fill"></i></span>
			<label>${processbPointfb>100?100:processbPointfb}%</label>
			</div>
	</div>
		</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	
			</div>
			<ul class="donateListBox" id = "donatList">
			<!--
				<h3 class="donateTitle">捐助记录</h3>
				<li class="clearfix">
					<div class="donateListImg">
						
					</div>
					<div class="donateListCon">
						<h3 class="listConTitle clearfix">
							<span class="fl">小感叹</span>
							<span class="fr">捐助<i>20</i>元</span>
						</h3>
						<div class="listCon clearfix">
							<div class="listConMes fl">公益公益公益公益公益公益公益公益公益公益公益公益</div>
							<div class="listConDate fr">12-11</div>
						</div>
					</div>
				</li>
			 -->
			</ul>
			<a href="javascript:void(0);" id="loadMore">
        <div class="details_add " style="display: none;">
    	<p class="fl">点击查看更多</p>
        
    </div>
    </a>
		</div>
	</div>
	<div class="donateBtn clearfix" id = "paybtnd">
		<a href="javascript:;" class="left-btn fl" id="paybtn">我要捐款</a>
		<%-- <a href="http://www.17xs.org/project/view_share_h5.do?projectId=${project.id }&shareType=0" class="right-btn fr">邀请朋友一起行善</a> --%>
		<a href="http://www.17xs.org/project/togetherDonate_view.do?projectId=${project.id }" class="right-btn fr">邀请朋友一起行善</a>
	</div>
	 <div class="donateBtn clearfix" id="finishedbtn" style="display: none">
	 	募捐已结束，感谢您的支持
	 </div>
	 <div class="payTips" style="display: none;" id = "payTips">
		<div class="payBox">
			<!-- 可用红包 strat -->
			<div class="redPaperWrap" style="display:none;" id = "redPackets">
				<div class="redPaperTop">
					<h1>点 击 红 包 捐 献 爱 心</h1>
					<span class="close" id="redclose"></span>
			</div>
				<ul class="redPaperList" id = "redPaperList">
				</ul>
			</div>
			<!-- 可用红包end -->
			
			<div class="payTipsTitle">请输入捐款金额<span class="close-btn"></span></div>
			<div class="payTip" id = "selectTb">
				<a href="javascript:;" class="payItem" t="20"><span></span><i>20</i>元</a>
				<a href="javascript:;" class="payItem" t="50"><span></span><i>50</i>元</a>
				<a href="javascript:;" class="payItem" t="100"><span></span><i>100</i>元</a>
				<a href="javascript:;" class="payItem" t="500"><span></span><i>500</i>元</a>
				<input type="number" id="money2" placeholder="请输入其它金额" class="payMoney">
				<a href="javascript:;" class="submit-btn" id="btn_submit">立即捐款</a>
			</div>
			<p class="aggrement"><i></i><a href="http://www.17xs.org/redPackets/getAgreement.do">同意并接受《善园基金会用户协议》</a></p>
		</div>
	</div>
	<div class="popup" style="display: none;"></div>
	<div class="confirmBox" style="display: none;">
		<span class="close" id="closeBtn"></span>
		<div class="confirmInner">
			<div class="top">
				<img src="${project.coverImageUrl}">
			</div>
			<div class="bottom">
				<h4>${project.title}</h4>
				<p>${shouzhu.realName }，${shouzhu.sex }，${shouzhu.age }岁,${shouzhu.familyAddress }</p>
				<p class="hasDonate">已募集<i><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/></i>元,共捐款<i>${peopleNum}</i>人次</p>
			</div>
			<p class="otherInfo">为TA捐助<span><i id="payamount"></i>元</span>行善红包</p>
			<input type="button" value="确 定" class="btn" id="confirmBtn">
		</div>
	</div>
	 <div style="display:none">
		<span id="page"></span>
		<span id="pageNum"></span>
	</div >
	<input type="hidden" id="pprim" value="${project.title}">
	<input type="hidden" id="extensionPeople" value = "${extensionUser.id}">
	<input type="hidden" id="projectId" value = "${project.id}">
	<input type="hidden" id="shareType" value = "${shareType}">
	<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
</body>
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" /></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
	wx.config({
		debug : false,
		appId : '${appId}',
		timestamp : '${timestamp}',
		nonceStr : '${noncestr}',
		signature : '${signature}',
		jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline' ]
	});

	wx.ready(function(){
		wx.onMenuShareAppMessage({
					title : '${project.title}', // 分享标题
					desc : '${desc}', // 分享描述
					link : 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1', // 分享链接
					imgUrl : '${project.coverImageUrl}', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
 						window.location.href = 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1';
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				
		wx.onMenuShareTimeline({
			title : '${project.title}', // 分享标题
			link : 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1', // 分享链接
			imgUrl : '${project.coverImageUrl}', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
				window.location.href = 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1';
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
	});
		});
</script>
<script type="text/javascript">
		/*
		$(document).ready(function(){
			$("#donate-btn").click(function() {
				$(".payTips").show();
			});
			$('.close-btn').click(function(){
				$('.payTips').hide();
			});
		});
		*/
</script>
<script data-main="<%=resource_url%>res/js/h5/detail_share.js?v=20150129100" src="<%=resource_url%>res/js/require.min.js"></script>
</html>