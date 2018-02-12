<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体" />
<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人" />
<meta name="viewport" />
<title>善园 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/home.css?v=20180211" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/home_xin.css" />
<style type="text/css">
	#wrapper {
		position: absolute;z-index: 1;top: 0px;bottom: 0;left: 0;width: 100%;overflow: auto;
	}
	#scroller {
		position: relative;-webkit-tap-highlight-color: rgba(0, 0, 0, 0);float: left;width: 100%;padding: 0;
	}
	#pullDown, #pullUp {
		font-weight: bold;font-size: 12px;color: #666;text-align: center;
	}
	.load-more {
		text-align: center;width: 100%;height: 30px;line-height: 30px;font-size: 12px;
		color: #666;border-radius: 4px;background: #fff;
	}
	.aload {
		text-decoration: none;display: block;
	}
	@media only screen and (min-width: 320px) and (max-width: 640px) {
		.scroll_box {max-height: 100px;}
	}
	#total .num {
		display: inline-block;;margin: 0 4px;
	}
	#total .num i {
		width: 16px;height: 17px;display: inline-block;border: 1px solid #ccc;margin-left: 1px;
		background: url(<%=resource_url%>res/images/h5/images/number1.png?v=2018) no-repeat;background-position: 0 1px;
		text-indent: -999em;background-size: 16px;vertical-align: -5px;
	}
	.site_info .ucen {
		display: inline-block;margin-right: 5px;-webkit-tap-highlight-color: transparent;
	}
	.site_info .ucen img{
		vertical-align: middle;
	}
</style>
</head>
<body>
	<div id="pageContainer">
		<div class="home">
			<div class="site_info flex spb fcen">
				<span class="logo">
	  				<img src="<%=resource_url%>res/images/h5/images/logo-lg.jpg" width="100%">
	  			</span>
				<!--<span class="txt">捐助<label id="lab1"><fmt:formatNumber type="number" pattern="####,###,###" value = "${totalMoney}"/></label>元</span>-->
				<span class="txt" id="total">捐助<span class="num flex fcen"></span>元</span>
				<a href="<%=resource_url%>ucenter/userCenter_h5.do" class="ucen">
					<img src="<%=resource_url%>res/images/h5/images/usercen.png" width="20" alt="用户中心" />
				</a>
			</div>
			<div style="clear: both;"></div>
			<!--焦点图//-->
			<!--scroll-->
			<div class="scroll relative">
				<div class="scroll_box" id="scroll_img">
					<ul class="scroll_wrap">
						<c:forEach items="${bannerList}" var="img">
							<li>
								<a href="${img.linkUrl}"><img src="${img.url}" width="100%" /></a>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
			<!--scroll-->
			<!--分类//-->
			<div class="prsee_xin">
				<ul>
					<li class="pxin_l">
						<a href="<%=resource_url%>project/batch_list.do?extensionPeople=${extensionPeople }" class="pxin_a">
							<span class="pxin_s pxin_s1"></span>
							<p class="pxin_p1">批量捐</p>
						</a>
					</li>
					<li class="pxin_l">
						<a href="<%=resource_url%>user/getDonateTime.do" class="pxin_a">
							<span class="pxin_s pxin_s2"></span>
							<p class="pxin_p2">月捐</p>
						</a>
					</li>

					<li class="pxin_l">
						<a href="<%=resource_url%>ucenter/myDonate.do" class="pxin_a">
							<span class="pxin_s pxin_s3"></span>
							<p class="pxin_p3">捐助记录</p>
						</a>
					</li>
					<li class="pxin_l pxin">
						<a href="<%=resource_url%>project/releaseH5Project.do" class="pxin_a">
							<span class="pxin_s pxin_s4"></span>
							<p class="pxin_p4">发起求助</p>
						</a>
					</li>
				</ul>
			</div>
			<!--推荐主题//-->
			<div class="recommend">
				<div class="hot" id="hot"></div>
			</div>

			<!--主题列表//-->
			<div class="list" id="list">
				<div class="list-box"></div>
				<div class="load-more">
					<a class="aload" href="<%=resource_url%>h5/list/?extensionPeople=${extensionPeople }">查看更多项目</a>
				</div>
			</div>
		</div>
	</div>

	<div id="category" class="category">
		<div class="scroll_wrap">
			<c:if test="${size > 6}">
				<c:forEach begin="0" end="${size}" varStatus="status" step="6">
					<ul class="categoryList clearfix">
						<c:forEach var="record" items="${atc}" begin="${status.index }" end="${status.index+5 }" varStatus="status">
							<c:if test="${record.typeName_e != 'good'}">
								<li>
									<a href="<%=resource_url%>h5/list/?field=${status.index+1}&extensionPeople=${extensionPeople }">
										<img src="${record.coverImageUrl}">
										<span>${record.typeName}</span>
									</a>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</c:forEach>
			</c:if>
			<c:if test="${size <= 6}">
				<ul class="categoryList clearfix">
					<c:forEach var="record" items="${atc}" varStatus="status">
						<c:if test="${record.typeName_e != 'good'}">
							<li>
								<a href="<%=resource_url%>h5/list/?field=${status.index+1}&extensionPeople=${extensionPeople }">
									<img src="${record.coverImageUrl}">
									<span>${record.typeName}</span>
								</a>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</c:if>
		</div>
	</div>
	<div class="footer_fk">
		<div class="footer_t">
			<a href="http://www.17xs.org?flag=pc">电脑版&nbsp;|&nbsp;</a>
			<a href="http://www.17xs.org/user/userAdvice.do">用户反馈</a>
		</div>
		<div class="footer_f">
			<p>Copyright ©宁波市善园公益基金会 版权所有 浙ICP备15018913号-1</p>
		</div>
	</div>
	<input type="hidden" id="extensionPeople" value="${extensionPeople }">
	<input type="hidden" name="cur_num" id="cur_num" value="${totalMoney}" />
	
	<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type='text/javascript'>
		window.BWEUM || (BWEUM = {});
		BWEUM.info = {
			"stand": true,
			"agentType": "browser",
			"agent": "bi-collector.oneapm.com/static/js/bw-send-411.4.5.js",
			"beaconUrl": "bi-collector.oneapm.com/beacon",
			"licenseKey": "I66F7~vv2ai6V3Nn",
			"applicationID": 2282681
		};
	</script>
	<!--<script type="text/javascript" src="//bi-collector.oneapm.com/static/js/bw-loader-411.4.5.js"></script>-->
	<script src="<%=resource_url%>res/js/h5/iscroll.js"></script>
	<script src='<%=resource_url%>res/js/h5/hhSwipe.js'></script>
	<script src="<%=resource_url%>res/js/h5/number.js"></script>
	<script data-main="<%=resource_url%>res/js/h5/index.js?v=201801241431" src="<%=resource_url%>res/js/require.min.js"></script>
	<script type="text/javascript">
		$(function(){
			var ua = navigator.userAgent.toLowerCase();
			if(ua.match(/MicroMessenger/i) == 'micromessenger'){
				wx.config({
					debug: false,
					appId: '${appId}',
					timestamp: '${timestamp}',
					nonceStr: '${noncestr}',
					signature: '${signature}',
					jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline', 'previewImage']
				});
		
				wx.ready(function() {
					wx.onMenuShareAppMessage({
						title: '善园网-公益众筹平台，0收费', // 分享标题
						desc: '善园网依托善园公益基金会开展网络募捐服务,通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动', // 分享描述
						link: 'http://www.17xs.org', // 分享链接
						imgUrl: 'http://res.17xs.org/picture/personhead/20171102131523/20171102131523_kefu_hulu.jpg', // 分享图标
						type: 'link', // 分享类型,music、video或link，不填默认为link
						dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
						success: function() {
							//用户确认分享后执行的回调函数
						},
						cancel: function() {
							//用户取消分享后执行的回调函数
						}
					});
		
					wx.onMenuShareTimeline({
						title: '善园网-公益众筹平台，0收费', // 分享标题
						link: 'http://www.17xs.org', // 分享链接
						imgUrl: 'http://res.17xs.org/picture/personhead/20171102131523/20171102131523_kefu_hulu.jpg', // 分享图标
						success: function() {
							// 用户确认分享后执行的回调函数
						},
						cancel: function() {
							// 用户取消分享后执行的回调函数
						}
					});
				});	
			}
		});
		
	</script>
	<%@ include file="cs.jsp" %>
	<%CS cs = new CS(1257726653);cs.setHttpServlet(request,response);
	String imgurl = cs.trackPageView();%>
</body>
</html>