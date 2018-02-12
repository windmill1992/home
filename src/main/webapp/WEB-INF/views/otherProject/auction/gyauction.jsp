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
    <!--中间列表页-->
    <div class="aution_center">
        <div class="center_1">
            <c:if test="${auction.coverImageUrl == null}">
        	<img src="http://www.17xs.org/res/images/aution/jp-logo-bgm.jpg"alt=""/>
        	</c:if>
        	<c:if test="${auction.coverImageUrl != null}">
        	<img src="${auction.coverImageUrl}"alt=""/>
        	</c:if>
            <div class="center_1_1">
            	<c:if test="${auction.state == 201}">
        			<img src="<%=resource_url%>res/images/aution/icon1.png" alt="">
        		</c:if>
        		<c:if test="${auction.state == 203}">
        			<img src="<%=resource_url%>res/images/aution/icon2.png" alt="">
        		</c:if>
        		<c:if test="${auction.state == 200}">
        			<img src="<%=resource_url%>res/images/aution/icon2.png" alt="">
        		</c:if>
        		<c:if test="${auction.state == 202}">
        			<img src="<%=resource_url%>res/images/aution/icon2-2.png" alt="">
        		</c:if>
        		<c:if test="${auction.state == 204}">
        			<img src="<%=resource_url%>res/images/aution/icon2-2.png" alt="">
        		</c:if>
                <div class="center_time">
                    <p>倒计时：<span id="divdown1"></span></p>
                </div>
            </div>
            <p class="aution_xq"><a href="http://www.17xs.org/project/gydetail.do?id=${auction.id}">${auction.title}</a></p>
            <div class="center_now">
                <p class="now_left">当前价：<span>￥<fmt:formatNumber type="number" pattern="###0.00#" value = "${price}"/></span></p>
                <p class="now_right">报名：<span>${auction.peopleNum}</span>人</p>
            </div>
        </div>
        <!--作品详情-->
        <div class="center_2">
            <div class="icon_y"></div><span>出价记录(${auction.number})</span>
        </div>
        <div class="center_3">
            <dl class="record" id="record">
            </dl>
            <div class="center_5" id="more">
            </div>
        </div>

    </div>
    <!--竞拍底部 footer-->
    <div class="aution_footer">
        <form action="" class="footer_1">
            <input id="min" name="" type="button" value="-" class="prev" />
            <input id="text_box" name="" type="text" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${auction.currentPrice}" />" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)"class="center" />
            <input id="add" name="" type="button" value="+" class="prev"/>
        </form>
        <c:if test="${auction.state == 201}">
        	<a class="footer_2" id="auction_submit" ><p>我要出价</p></a>
        </c:if>
        <c:if test="${auction.state == 202}">
        	<c:if test="${status != 0}">
        		<a class="footer_2_cg" ><p>我要出价</p></a>
        	</c:if>
        	<c:if test="${status == 0}">
        		<a class="footer_2" id="auction_submit_pay" ><p>请支付</p></a>
        	</c:if>
        </c:if>
        <c:if test="${auction.state == 203}">
        	<a class="footer_2"><p>已成功</p></a>
        </c:if>
        <c:if test="${auction.state == 200}">
        	<a class="footer_2_cg" ><p>我要出价</p></a>
        </c:if>
        <c:if test="${auction.state == 204}">
        	<a class="footer_2_cg" ><p>我要出价</p></a>
        </c:if>
    </div>

</div>
<input type="hidden" id="pageNum" value="1">
<input type="hidden" id="endtime" value="${endtime}">
<input type="hidden" id="nowtime" value="${nowtime}">
<input type="hidden" id="auctionId" value="${auction.id}">
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
					title : '${auction.title}', // 分享标题
					desc : '主办单位：华师大宁波艺术实验学校主板，协办单位：善园基金会、夏加儿美术教育、甬派善园公益+', // 分享描述
					link : 'http://www.17xs.org/project/gyauction.do?id=${auction.id}', // 分享链接
					imgUrl : '${auction.coverImageUrl}', // 分享图标
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
			title : '${auction.title}', // 分享标题
			link : 'http://www.17xs.org/project/gyauction.do?id=${auction.id}', // 分享链接
			imgUrl : '${auction.coverImageUrl}', // 分享图标
			success : function() {
				// 用户确认分享后执行的回调函数
			},
			cancel : function() {
				// 用户取消分享后执行的回调函数
			}
	});
		});
</script>
<script data-main="<%=resource_url%>res/js/other/auction.js?v=6" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>