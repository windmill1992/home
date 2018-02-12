<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="../../common/file_url.jsp"%>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体"/>
	<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人"/>
	<title>留言墙</title>
	<link rel="stylesheet" type="text/css" href="c../../../../res/css/h5/messageWall/leaveWord.css"/>
	<script type="text/javascript" src="../../../../res/js/jquery-1.8.2.min.js" ></script>
	<script type="text/javascript" src="../../../../res/js/common/time.js" ></script>
</head>
<body class="bg">
		<div class="fd-head">
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
		<div class="fd-body">
			<div class="fd-content"></div>
			<div class="loadmore">
				<a href="javascript:;">点击加载更多</a>
			</div>
		</div>
		
		<div class="fd-foot">
			<a href="javascript:;" class="share btns"><img src="../../../../res/images/h5/images/messageWall/share.png" width="30"/><br />分享</a>
			<a href="http://www.17xs.org/together/together_view.do?projectId=${projectId }" class="btn">我为善园添砖</a>
			<a href="javascript:;" class="look btns"><img src="../../../../res/images/h5/images/messageWall/logo-img.png" width="30"/><br />参观善园</a>
		</div>
		
		<div class="leaveWordContent">
			<i class="close3">&times;</i>
		</div>
		<div class="leaveWordEdit">
			<!-- <form action="" method="post" id="form1"> -->
				<input type="text" name="" id="word" placeholder="#来这里留言说点什么吧#" />
				<input type="submit" id="pub" value="发表"/>
			<!-- </form> -->
			<i class="close2">&times;</i>
		</div>
		<div class="shareTips">
			点击右上角分享出去吧。<img src="../../../../res/images/h5/images/messageWall/arrow.png" width="30" />
		</div>
		<div class="leaveWordTips" style="display:none">
			点击任意空白砖块留言吧。
			<i class="close">&times;</i>
		</div>
		<div class="selectTo">
			<p>请选择一个您想做的：</p>
			<div class="select">
				<a href="javascript:;" class="toLeaveWord">留个言</a>
				<a href="http://www.17xs.org/together/together_view.do?projectId=${projectId }" class="toDonate">一起捐</a>
			</div>
			<i class="close4">&times;</i>
		</div>
		<div id="overLay"></div>
		<input type="hidden" id="projectId" value="${projectId}"/>
		<input type="hidden" id="userId" value="${userId }">
		<input type="hidden" id="show" value="${show }"/>
		
		<script type="text/javascript" src="../../../../res/js/common/TouchSlide.1.1.js" ></script>
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<script type="text/javascript" src="../../../../res/layer/layer.js" ></script>
		<script data-main="../../../../res/js/h5/messageWall/leaveWord.js?v=201706271604" src="../../../../res/js/require.min.js"></script>
		<script>
			TouchSlide({ slideCell:"#banner",titCell:".hd ul",mainCell:".bd ul",effect:"leftLoop",autoPlay:true,autoPage:true });
			document.body.style.minHeight = screen.height+'px';
			
			
			wx.config({
				debug : false,
				appId : '${appId}',
				timestamp : '${timestamp}',
				nonceStr : '${noncestr}',
				signature : '${signature}',
				jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline','previewImage' ]
			});
	
			wx.ready(function(){
				wx.onMenuShareAppMessage({
							title : '留言墙', // 分享标题
							desc : '', // 分享描述
							link : 'http://www.17xs.org/message/messageWall_view.do?projectId=${projectId}', // 分享链接
							imgUrl : 'http://www.17xs.org/res/images/h5/images/goodlibrary/library_login.png', // 分享图标
							type : 'link', // 分享类型,music、video或link，不填默认为link
							dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
							success : function() {
								//用户确认分享后执行的回调函数
		 						window.location.href = 'http://www.17xs.org/message/messageWall_view.do?show=0&projectId=${projectId}';
							},
							cancel : function() {
								//用户取消分享后执行的回调函数
							}
						});
						
				wx.onMenuShareTimeline({
					title : '留言墙', // 分享标题
					link : 'http://www.17xs.org/message/messageWall_view.do?projectId=${projectId}', // 分享链接
					imgUrl : 'http://www.17xs.org/res/images/h5/images/goodlibrary/library_login.png', // 分享图标
					success : function() {
						// 用户确认分享后执行的回调函数
						window.location.href = 'http://www.17xs.org/message/messageWall_view.do?show=0&projectId=${projectId}';
					},
					cancel : function() {
						// 用户取消分享后执行的回调函数
					}
				});
			});
		</script>
		
	</body>
</html>