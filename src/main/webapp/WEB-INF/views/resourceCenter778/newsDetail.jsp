<!DOCTYPE html>
<html lang="zh-cmn-Hans">
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
		<%@ page contentType="text/html;charset=UTF-8" language="java" %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<meta name="keywords" content=""/>
		<meta name="description" content="${news.title }"/>
		<meta name="viewport"/>
        <meta http-equiv="x-ua-compatible" content="IE=8,IE=9,IE=10,IE=11,Chrome=1">
		<title>${type }</title>
		<link rel="shortcut icon" href="/res/images/favicon.ico">
		<link rel="stylesheet" type="text/css" href="/res/css/dev/resourceCenter778/res_center.css"/>
    </head>
   <body>
		<div class="pageContainer ">
			<div class="res-head w100p layout">
				<div class="w1200 layout">
					<div class="w100p rh-txt">
						<span>您好，欢迎来到宁波778创业资源中心</span>
					</div>
				</div>
			</div>
			<div class="res-nav w1200 layout">
				<div class="logo fl layout">
					<a href="/778/index.do"><img src="/res/images/resourceCenter778/logo.png"/></a>
					<span class="line"></span>
					<span class="text">宁波778创业资源中心</span>
				</div>
				<div class="t-nav fr layout">
					<ul id="nav">
						<li>
							<a href="/778/index.do"><h2>网站首页</h2></a>
						</li>
						<li class="${type=='中心大事件'?'active':'' }">
							<a href="/778/news.do?type=中心大事件"><h2>中心大事件</h2></a>
						</li>
						<li class="${type=='公益科普'?'active':'' }">
							<a href="/778/news.do?type=公益科普"><h2>公益科普</h2></a>
						</li>
						<li>
							<a href="/778/about.do"><h2>关于我们</h2></a>
						</li>
						<li>
							<a href="/778/relation.do"><h2>联系我们</h2></a>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="res-body">
				<div class="position w1200 layout">
					<p>当前位置：<a href="/778/index.do">首页</a>><span class="title">${type }</span></p>
				</div>	
				<div class="be-detail w1200 layout">
					<div class="detail-hd">
						<div class="title">
							<h2>${news.title }</h2>
						</div>
						<div class="pub-info">
							发布时间：<span class="time"><fmt:formatDate  value="${news.createtime }" pattern="yyyy-MM-dd"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							来源：<a href=""><span class="resource">${news.source==0?'pc':(news.source==1?'3G':'android') }</span></a>
						</div>
						<div class="share">
							<div class="bshare-custom">
								<div class="bsPromo bsPromo1"></div>
								<a title="分享到新浪微博" class="bshare-sinaminiblog" href="javascript:void(0);"></a>
								<a title="分享到微信" class="bshare-weixin" href="javascript:void(0);"></a>
								<a title="分享到腾讯微博" class="bshare-qqmb" href="javascript:void(0);"></a>
								<a title="分享到QQ空间" class="bshare-qzone"></a>
							</div>
							<a class="bshareDiv" onclick="javascript:return false;"></a>
						</div>
					</div>
					<div class="detail-bd">
						${news.content }
					</div>
				</div>
			</div>
			<div class="res-footer layout">
				<div class="w1200 layout">
					<div class="text layout">
						<p>联系电话：0574-87412436&nbsp;&nbsp;&nbsp;&nbsp;地址：宁波市鄞州区泰康西路399号（宁波·善园）</p>
						<p>Copyright © 宁波市善园公益基金会 版权所有 浙ICP备15018913号-1  杭州智善网络科技有限公司  提供技术支持</p>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/buttonLite.js#style=-1&amp;uuid=&amp;pophcol=2&amp;lang=zh"></script>
		<script type="text/javascript" charset="utf-8" src="http://static.bshare.cn/b/bshareC2.js"></script>
		<script type="text/javascript" charset="utf-8">
			bShare.addEntry({
			    summary: "${type}-${news.title}",
			});
		</script>
	</body>
</html>