<!DOCTYPE html>
<html lang="zh-cmn-Hans">
	<head>
		<meta charset="utf-8" />
		<%@ page contentType="text/html;charset=UTF-8" language="java" %>
		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
		<meta name="keywords" content=""/>
		<meta name="description" content=""/>
		<meta name="viewport"/>
        <meta http-equiv="x-ua-compatible" content="IE=8,IE=9,IE=10,IE=11,Chrome=1">
		<title>宁波778创业资源中心</title>
		<link rel="shortcut icon" href="/res/images/favicon.ico">
		<link rel="stylesheet" type="text/css" href="/res/css/dev/resourceCenter778/res_center.css"/>
    </head>
    <body>
		<div class="pageContainer">
			<div class="res-head w100p layout">
				<div class="w1200 layout">
					<div class="w100p rh-txt">
						<span>您好，欢迎来到宁波778创业资源中心</span>
					</div>
				</div>
			</div>
			<div class="res-nav w1200 layout">
				<div class="logo fl layout">
					<a href="index.do"><img src="/res/images/resourceCenter778/logo.png"/></a>
					<span class="line"></span>
					<span class="text">宁波778创业资源中心</span>
				</div>
				<div class="t-nav fr layout">
					<ul id="nav">
						<li class="active">
							<a href="/778/index.do"><h2>网站首页</h2></a>
						</li>
						<li>
							<a href="/778/news.do?type=中心大事件"><h2>中心大事件</h2></a>
						</li>
						<li>
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
			<div class="res-banner" id="banner">
				<div class="slideBox">
					<div class="hd">
						<!--有几张图就放几个li-->
						<ul>
						<c:forEach items="${bannerList }" var="banner">
							<li></li>
						</c:forEach>
						</ul>
					</div>
					<div class="bd">
						<ul><!--图片大小要一样-->
							<c:forEach items="${bannerList }" var="banner">
								<li><a href="${banner.linkUrl }" target="_blank"><img src="${banner.url }" /></a></li>
							</c:forEach>
						</ul>
					</div>

					<!-- 下面是前/后按钮代码，如果不需要删除即可 -->
					<a class="prev" href="javascript:void(0)"></a>
					<a class="next" href="javascript:void(0)"></a>

				</div>
			</div>
			<div class="res-news layout">
				<div class="w1200 layout">
					<div class="Title">
						<h2><a href="/778/news.do?type=中心大事件" class="bgf6"><img src="/res/images/resourceCenter778/title-1.png"/></a></h2>
					</div>
					<div class="news picScroll-left picScroll1 layout">
						<div class="bd">
							<ul class="layout">
								<c:forEach items="${bigEvents }" var="bigEvent">
									<li>
										<div class="pic">
											<a href="/778/news/detail.do?id=${bigEvent.id }&type=中心大事件"><img src="${bigEvent.coverImageUrl }"/></a>
										</div>
										<div class="title layout">
											<h3><a href="/778/news/detail.do?id=${bigEvent.id }&type=中心大事件">${bigEvent.title }</a></h3>
											<div class="time"><fmt:formatDate  value="${bigEvent.createtime }" pattern="yyyy-MM-dd"/></div>
										</div>
										<div class="info-news layout">
											<a href="/778/news/detail.do?id=${bigEvent.id }&type=中心大事件">
												<p class="p5">${bigEvent.abstracts }</p>
											</a>
											<div class="btn-news layout">
												<a href="/778/news/detail.do?id=${bigEvent.id }&type=中心大事件" style="display: none;">查看详情</a>
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
						</div>
						<div class="btn-more">
							<a href="/778/news.do?type=中心大事件">查看更多</a>
						</div>
					</div>
				</div>
			</div>
			
			<div class="res-project w1200 layout">
				<div class="Title mt20">
					<h2><a href="/project/index/"><img src="/res/images/resourceCenter778/title-2.png"/></a></h2>
				</div>
				<div class="content">
					<div class="c-nav">
						<div class="c-name active" href="raise">众筹项目</div>
						<div class="c-name" href="special">专项基金</div>
					</div>
					<div class="c-summary">
						<div class="dg-container showBox" name="raise" id="proj_box">
							<div id="d_tab1" class="d_tab layout">
								<ul class="d_img">
									<!--默认是第二个居中，加class active-->
									<c:forEach items="${rdList }" var="project" varStatus="status">
										<li class="d_pos${status.count==2?'2 active': status.count}">
											<div class="thumb">
												<a href="/project/view.do?projectId=${project.id }"><img src="${project.coverImageUrl }" alt="${project.title }" /></a>	
											</div>
											<div class="titles">
												<a href="/project/view.do?projectId=${project.id }" target="_blank">
													<c:if test="${fn:length(project.title)>20 }">
														${fn:substring(project.title,0,20 }...
													</c:if>
													<c:if test="${fn:length(project.title)<=20 }">
														${project.title }
													</c:if>
												</a>
											</div>
											<div class="brief">${project.subTitle }</div>
											<div class="btn-ok">
												<a href="/project/view.do?projectId=${project.id }" target="_blank">查看详情</a>
											</div>
										</li>
									</c:forEach>
								</ul>
								<a href="javascript:;" class="d_prev arrow"></a>
								<a href="javascript:;" class="d_next arrow"></a>
							</div>
							
							<div class="btn-more">
								<a href="/project/index/">查看更多</a>
							</div>
						</div>
						<div class="wrap dg-container " name="special" id="funds-box">
							<div id="d_tab2" class="d_tab layout">
								<ul class="d_img">
									<!--放一个li class为d_pos2 , 两个 class为d_pos1和d_pos3 , 三个为1,2,3-->
									<c:forEach items="${specialFundList }" var="specialFund" varStatus="status">
										
										<li class="d_pos${status.count==2?'2 active': status.count} ">
											<div class="thumb">
												<a href="/uCenterProject/specialProject_details.do?projectId=${specialFund.id }"><img src="${specialFund.coverImageUrl }" alt="${specialFund.title }" /></a>	
											</div>
											<div class="titles">
												<a href="/uCenterProject/specialProject_details.do?projectId=${specialFund.id }" target="_blank">
													<c:if test="${fn:length(specialFund.title)>20 }">
														${fn:substring(specialFund.title,0,20) }...
													</c:if>
													<c:if test="${fn:length(specialFund.title)<=20 }">
														${specialFund.title }
													</c:if>
												</a>
											</div>
											<div class="brief">${specialFund.subTitle }</div>
											<div class="btn-ok">
												<a href="/uCenterProject/specialProject_details.do?projectId=${specialFund.id }" target="_blank">查看详情</a>
											</div>
										</li>
									</c:forEach>
									
								</ul>
								<a href="javascript:;" class="d_prev arrow"></a>
								<a href="javascript:;" class="d_next arrow"></a>
							</div>
							
							<div class="btn-more">
								<a href="/project/index/">查看更多</a>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="res-news layout">
				<div class="w1200 layout">
					<div class="Title">
						<h2><a href="/778/news.do?type=公益科普" class="bgf6"><img src="/res/images/resourceCenter778/title-5.png"/></a></h2>
					</div>
					<div class="news picScroll-left picScroll2 layout">
						<div class="bd">
							<ul class="layout">
								<c:forEach items="${sicencePopulation }" var="population">
									<li>
										<div class="pic">
											<a href="/778/news/detail.do?id=${population.id }&type=公益科普"><img src="${population.coverImageUrl }"/></a>
										</div>
										<div class="title layout">
											<h3><a href="/778/news/detail.do?id=${population.id }&type=公益科普">${population.title }</a></h3>
											<div class="time"><fmt:formatDate  value="${population.createtime }" pattern="yyyy-MM-dd"/></div>
										</div>
										<div class="info-news layout">
											<a href="/778/news/detail.do?id=${population.id }&type=公益科普">
												<p class="p5">${population.abstracts }</p>
											</a>
											<div class="btn-news layout">
												<a href="/778/news/detail.do?id=${population.id }&type=公益科普" style="display: none;">查看详情</a>
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>
						</div>
						<div class="btn-more">
							<a href="news.do?type=公益科普">查看更多</a>
						</div>
					</div>
				</div>
			</div>
			
			<div class="res-partner w1200 layout">
				<div class="Title mt20">
					<h2><a href="javascript:;"><img src="/res/images/resourceCenter778/title-4.png"/></a></h2>
				</div>
				<div class="partner-list layout">
					<div class="picMarquee-left">
						<div class="bd">
							<ul>
								<c:forEach items="${linkImgs }" var="linkImg" varStatus="status" step="1">
									<c:if test="${status.index % 2 == 0}">
										<li>
											<a href="${linkImg.linkUrl }" target="_blank" class="pic"><img src="${linkImg.url }" title="" alt=""/></a>
									</c:if>
									<c:if test="${status.index % 2 == 1}">
											<a href="${linkImg.linkUrl }" target="_blank" class="pic"><img src="${linkImg.url }" title="" alt=""/></a>
										</li>
									</c:if>
								</c:forEach>
							</ul>	
						</div>
						
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
		
		<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js" ></script>
		<script type="text/javascript" src="/res/js/common/jquery.SuperSlide.2.1.1.js" ></script>
		<script type="text/javascript" src="/res/js/dev/resourceCenter778/banner3d.js" ></script>
		<script type="text/javascript" src="/res/js/dev/resourceCenter778/res_center.js" ></script>
		<script type="text/javascript">
			$(".slideBox").slide({mainCell:".bd ul",effect:"leftLoop",autoPlay:true,trigger:"click",delayTime:700});
		</script>
	</body>
</html>