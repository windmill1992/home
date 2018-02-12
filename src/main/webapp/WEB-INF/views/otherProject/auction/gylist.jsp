<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园 - 一起行善，温暖前行！</title>

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/auction.css"/>

</head> 
<body>
<div class="aution">
    <!--头部-->
<div class="aution_top"><a href="<%=resource_url%>/h5/"><img src="<%=resource_url%>res/images/aution/logo.png" alt="" title=""></a><p>华师艺术实验学校-公益竞拍
    </p></div>
    <div class="clear"></div>
    <div class="apply_line"></div>

<!--中间center-->
    <div class="aution_2_center">
        <ul id="gyList">
        </ul>
        <div class="center_5" id="more">
    	</div>
    </div>

</div>
<input type="hidden" id="pageNum" value="1">
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
					title : '爱-绘画慈善拍卖', // 分享标题
					desc : '主办单位：华师大宁波艺术实验学校主板，协办单位：善园基金会、夏加儿美术教育、甬派善园公益+', // 分享描述
					link : 'http://www.17xs.org/project/gylist.do', // 分享链接
					imgUrl : 'http://www.17xs.org/res/images/aution/jp-logo-bgm.jpg', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
// 						window.location.href = 'http://www.17xs.org/';
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				
		wx.onMenuShareTimeline({
			title : '爱-绘画慈善拍卖', // 分享标题
			link : 'http://www.17xs.org/project/gylist.do', // 分享链接
			imgUrl : 'http://www.17xs.org/res/images/aution/jp-logo-bgm.jpg', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
	});
		});
</script>
<script data-main="<%=resource_url%>res/js/other/gylist.js?v=2" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
