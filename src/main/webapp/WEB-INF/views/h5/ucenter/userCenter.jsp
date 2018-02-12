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
	<link rel="stylesheet" type="text/css" href="/res/css/h5/userCenter.css?v=<%=uc_version%>">
	<script src="/res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body style="background-color: #f4f3f0;">
	<!-- 公共头部 -->
	<%@ include file="./../common/head.jsp" %>
	<!-- 数据展示 -->
	<ul class="userDate flex">
		<li class="stateItem w33">
			<p>捐赠次数</p>
			<span>${donationNum}次</span>
		</li>
		<li class="stateItem w33">
			<p>捐赠金额</p>
			<span>${totalAmount}元</span>
		</li>
		<li class="stateItem w33">
			<a href="/redPackets/myRedPackets.do">
				<p>我的红包</p>
				<span>${redPaperNum }个</span>
			</a>
		</li>
	</ul>
	<div class="mainBox">
		<div class="mainTitle flex spb fcen">
			<div class="left flex fcen">
				<i class="function-icon icon1"></i>
				<h2>捐助的项目</h2>	
			</div>
			<a href="/ucenter/core/personalCenter_h5.do?itemType=1" class="moreInfo">查看全部捐助</a>
		</div>
		<ul class="donateState flex">
			<li class="stateItem w25">
				<a href="javascript:;">
					<p>待付款</p>
					<span>${donationWait}笔</span>
				</a>
			</li>
			<li class="stateItem w25">
				<a href="/ucenter/core/personalCenter_h5.do?itemType=1&pstate=240">
					<p>筹款中</p>
					<span>${donatingNum}笔</span>
				</a>
			</li>
			<li class="stateItem w25">
				<a href="/ucenter/core/personalCenter_h5.do?itemType=1&pstate=260">
					<p>已结束</p>
					<span>${donatedNum}笔</span>
				</a>
			</li>
			<li class="stateItem w25">
				<a href="/ucenter/core/personalCenter_h5.do?itemType=2">
					<p>最新反馈</p>
					<span>${careCount}条</span>
				</a>
			</li>
		</ul>
	</div>
	<!-- 功能按钮 -->
	<div class="fuctionBox">
		<ul class="mainBox">
			<li class="mainTitle">
				<a href="javascript:;" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon2"></i>
						<h2>可用善款</h2>	
					</div>
					<span class="moreInfo no">账户余额：${user.balance}元 </span>
				</a>
			</li>
			<li class="mainTitle last">
				<a href="/ucenter/myInvoice.do" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon3"></i>
						<h2>开票记录</h2>	
					</div>
					<span class="moreInfo">可开票金额：${ticketAmount}元 </span>
				</a>
			</li>
		</ul>
		<ul class="mainBox">
			<li class="mainTitle">
				<c:if test="${monthlyDonate == '已开通'}"><a href="/user/getMyDonateTime.do" class="flex spb fcen"></c:if>
				<c:if test="${monthlyDonate == '未开通'}"><a href="/user/getDonateTime.do" class="flex spb fcen"></c:if>
					<div class="left flex fcen">
						<i class="function-icon icon4"></i>
						<h2>月捐计划</h2>	
					</div>
					<span class="moreInfo">${monthlyDonate}</span>
				</a>
			</li>
			<li class="mainTitle">
				<a href="/ucenter/myCertificate.do?shareType=1" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon5"></i>
						<h2>我的证书</h2>	
					</div>
					<span class="moreInfo">拥有证书：1件 </span>
				</a>
			</li>
			<li class="mainTitle last">
				<a href="javascript:;" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon6"></i>
						<h2>我的家族</h2>	
					</div>
					<span class="moreInfo no">家族成员：开发中</span>
				</a>
			</li>
		</ul>
		<ul class="mainBox">
			<li class="mainTitle">
				<a href="/ucenter/myShare.do" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon7"></i>
						<h2>我的分享</h2>	
					</div>
					<span class="moreInfo">共有${peoNum}位小伙伴参与 </span>
				</a>
			</li>
			<li class="mainTitle">
				<a href="/uCenterProject/uCenterProjectList.do?state=210&currentPage=1" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon8"></i>
						<h2>我的求助</h2>	
					</div>
					<span class="moreInfo">共有${cryDonatNum}位好心人捐助 </span>
				</a>
			</li>
			<li class="mainTitle last">
				<a href="/uCenterProject/myEntryFormList.do?state=0&currentPage=-1" class="flex spb fcen">
					<div class="left flex fcen">
						<i class="function-icon icon8"></i>
						<h2>我的报名</h2>	
					</div>
				</a>
			</li>
		</ul>
	</div>
	<!-- 公共底部 -->
	<%@ include file="./../common/footer.jsp" %>
</body>
</html>