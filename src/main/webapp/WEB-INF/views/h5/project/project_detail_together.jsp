<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>一起捐</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
<meta name="format-detection" content="telephone=no">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<link rel="stylesheet" type="text/css" href="/res/css/h5/base.css?v=201512111">
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=20171220" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/dialog.css" />
<link type="text/css" rel="stylesheet" href="/res/css/h5/project_detail_together.css" />
</head>
<body>
	<div class="donateItemBox">
		<h3 class="donateTitle clearfix">
		    <span class="fl">筹款项目</span>
		    <span class="fr"></span>
		</h3>
		<div class="donateItemInfo">
			<a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }">
				<div class="clearfix">
					<div class="donateItemImg">
						<img src="${project.coverImageUrl }">
					</div>
					<div class="donateItemCon">
						<h3>${project.title }</h3>
						<p></p>
					</div>
				</div>
			</a>
			<p class="donateMoney">项目已筹款：${project.donatAmount }元&nbsp;&nbsp;${project.donationNum }人捐款</p>
		</div>
	</div>

	<!--选择筹款目标-->
	<div class="pay">
		<!-- 可用红包 strat -->
		<div class="redPaperWrap" style="display:none;" id="redPackets">
			<div class="redPaperTop">
				<h1>点 击 红 包 捐 献 爱 心</h1>
				<span class="close" id="redclose"></span>
			</div>
			<ul class="redPaperList" id="redPaperList"></ul>
		</div>

		<!-- 可用红包end -->
		<div class="t">
			<span>请选择或输入筹款目标</span>
		</div>
		<div class="box">
			<table class="tb" id="selectTb">
				<tbody>
					<tr>
						<td>
							<div t="20" class="sel l">
								<span>20</span>元<b></b>
							</div>
						</td>
						<td>
							<div t="50" class="sel l">
								<span>50</span>元<b></b>
							</div>
						</td>
						<td>
							<div t="100" class="sel l">
								<span>100</span>元<b></b>
							</div>
						</td>
						<td>
							<div t="500" class="sel l">
								<span>500</span>元<b></b>
							</div>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<div style="height:10px;"></div>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<div t="0" class="sel l"><input type="number" id="money2" placeholder="请输入其它金额" class="money"></div>
						</td>
					</tr>
					<tr>
						<td colspan="4">
							<div style="height:10px;"></div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!--发起说明-->
	<div class="project_sm">
		<div class="sm_text">发起说明（140字以内）</div>
		<textarea name="txtcontent" id="launchExplain" class="f24" placeholder="我要为这个项目发起筹款，为一个善意而美好的世界，我们大家一起来。"></textarea>
	</div>
	<div class="pro_btn">
		<a href="javascript:;" class="btn_a" id="btn_submit">
			<div class="btn">下一步</div>
		</a>
	</div>
</body>
<input type="hidden" id="projectId" value="${project.id }" />
<input type="hidden" id="extensionDonateAmount" value="${extensionDonateAmount }" />
<div class="cue2" style="display:none" id="msg"></div>
<script src="/res/js/jquery-1.8.2.min.js" /></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
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
			title: '${project.title}', // 分享标题
			desc: '${desc}', // 分享描述
			link: 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1', // 分享链接
			imgUrl: '${project.coverImageUrl}', // 分享图标
			type: 'link', // 分享类型,music、video或link，不填默认为link
			dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
			success: function() {
				//用户确认分享后执行的回调函数
				window.location.href = 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1';
			},
			cancel: function() {
				//用户取消分享后执行的回调函数
			}
		});

		wx.onMenuShareTimeline({
			title: '${project.title}', // 分享标题
			link: 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1', // 分享链接
			imgUrl: '${project.coverImageUrl}', // 分享图标
			success: function() {
				// 用户确认分享后执行的回调函数
				window.location.href = 'http://www.17xs.org/project/view_share_h5.do?projectId=${project.id}&extensionPeople=${extensionUser.id}&shareType=1';
			},
			cancel: function() {
				// 用户取消分享后执行的回调函数
			}
		});
	});
</script>
<script data-main="/res/js/h5/togetherDonate.js?v=20150129100" src="/res/js/require.min.js"></script>

</html>