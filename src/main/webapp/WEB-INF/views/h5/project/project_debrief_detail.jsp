<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>${project.title }项目进展详情</title>
<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/projectDebrief/debriefing.css"/>
</head> 

<body>
	<div id="pageContainer">
		<div class="page-wrapper" id="process">
			<div class="pdd-hd">
				<div class="proInfo">
					<div class="pic flex1">
						<img src="${project.coverImageUrl }" width="100%" height="90px"/>
					</div>
					<div class="detail flex12">
						<h3>${project.title }</h3>
						<p class="f13 c999">
						<c:choose>
							<c:when test="${fn:length(project.subTitle)>35}">
								${fn:substring(project.subTitle, 0, 35)}...
							</c:when>
							<c:otherwise>
								${project.subTitle }
							</c:otherwise>
						</c:choose>
							<a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }">详情>></a>
						</p>
					</div>
				</div>
				<div class="proData">
					<div class="progress">
						<div class="raised">
							<p class="flex1">已筹：<span class="moneyNum cff9">${project.donatAmount }</span>元</p>
						</div>
						<div class="donateBtnWrap flex1">
							<a href="javascript:void(0);" class="donateBtn" id="donateBtn1">
								<c:choose>
									<c:when test="${project.state == 260}">已结束</c:when>
									<c:when test="${project.state == 240}">捐款</c:when>
									<c:otherwise >未发布</c:otherwise>
								</c:choose>
							</a>
						</div>
					</div>
					<div class="donateData">
						<p>善款接收：善园公益基金会</p>
						<p>项目发起：${project.nickName }</p>
					</div>
				</div>
			</div>
			
			<div class="pdd-bd">
				<div class="projReport">
					<h3 class="sectionTitle"><span class="title">项目报告</span></h3>
					<c:if test="${!empty report }">
					<div class="report">
						<div class="reportInfo">
							<div class="pic">
								<img src="../../../../res/images/h5/images/kefu_hulu.jpg" width="100%" height="100%"/>
							</div>
							<div class="title flex1">
								<p class="name">善园公益基金会</p>
								<p class="role cb2b">${report.reportPeopleName }</p>
							</div>
							<span class="reportPublishTime"><fmt:formatDate value="${report.operatorTime}" pattern="yyyy年MM月 "/></span>
						</div>
						<div class="reportContent">
							<h3><fmt:formatDate value="${report.operatorTime}" pattern="yyyy年MM月月度反馈报告"/></h3>
							<p>${report.content }</p>
							<div class="content-picList clearFix">
								<c:forEach items="${report.contentImageUrl }" var="contentImageUrls">
									<div class="picList-img">
										<a href="javascript:;"><img src="${contentImageUrls }"/></a>
									</div>
								</c:forEach>
							</div>
							<div class="wholeReport">
								<a href="http://www.17xs.org/project/view_h5.do?projectId=${project.id }&itemType=3" class="link">查看完整项目报告 >></a>
							</div>
						</div>
					</div>
					</c:if>
				</div>
				
				<div class="projMoment">
					<h3 class="sectionTitle"><span class="title">项目动态</span></h3>
					<c:if test="${!empty feedbacks }">
					<div class="recordList">
						<ul id="progress-list">
							<c:forEach items="${feedbacks }" var="feedback">
								<li>
									<span class="leftIcon"></span>
									<div class="content">
										<div class="content-info">
											<p><fmt:formatDate value="${feedback.feedbackTime}" pattern="yyyy年MM月dd日"/></p>
											<p class="content-info-publish f13"> 由${feedback.nickName }发布</p>
										</div>
										<p class="content-words f14">
											${feedback.content }
										</p>
										<div class="content-picList clearFix">
											<c:forEach items="${feedback.contentImageUrl }" var="contentImageUrls">
												<div class="picList-img">
													<a href="javascript:;"><img src="${contentImageUrls }"/></a>
												</div>
											</c:forEach>
										</div>
									</div>
									<span class="approveBtn active" style="display: block;">-</span>
								</li>
							</c:forEach>
						</ul>
					</div>
					</c:if>
				</div>
				
				<div class="projSusstion">
					<h3 class="sectionTitle"><span class="title">他们也需要您的帮助</span></h3>
					<ul class="list-detail" id="recommend-proj">
						<c:forEach items="${rdList }" var="hotProject">
							<a href="http://www.17xs.org/project/view_h5.do?projectId=${hotProject.id }">
								<li class="list-detail-item clearFix">
									<img src="${hotProject.coverImageUrl }"/>
									<div class="list-detail-content">
										<h3> 
											<c:choose>
												<c:when test="${fn:length(hotProject.title)>11}">
													${fn:substring(hotProject.title, 0, 11)}...
												</c:when>
												<c:otherwise>
													${hotProject.title }
												</c:otherwise>
											</c:choose>
										</h3>
										
										<p>
											<c:choose>
												<c:when test="${fn:length(hotProject.subTitle)>24}">
													${fn:substring(hotProject.subTitle, 0, 24)}...
												</c:when>
												<c:otherwise>
													${hotProject.subTitle }
												</c:otherwise>
											</c:choose>
										</p>
									</div>
								</li>
							</a>
						</c:forEach>
					</ul>
				</div>
			</div>
			
			<div class="pdd-ft">
				<img src="../../../../res/images/h5/images/min-logo.jpg"/>
			</div>
		</div>
		
		<div class="bot-btn">
			<c:if test="${browser=='wx' }"><a href="javascript:void(0);" class="btn toShare flex1">分享进展</a></c:if>
			<a href="javascript:void(0);" class="btn toDonate flex1">
				<c:choose>
			        <c:when test="${project.state == 260}"> 募捐已结束</c:when>
					<c:when test="${project.state == 240}">我要捐款</c:when>
					<c:otherwise >未发布</c:otherwise>
				</c:choose>
			</a>
		</div>
	</div>
	<div id="payBox">
		<div class="pay">
			<div class="top">
				<h3>请输入捐款金额</h3>
				<i class="close">&times;</i>
			</div>
			<div class="box" id="pay_donate">
				<div class="donateMoney">
					<p class="word">小善大爱，爱是一种情怀！</p>
					<ul class="money-ul">
						<li class="on"><span>20</span>元</li>
						<li><span>50</span>元</li>
						<li><span>100</span>元</li>
						<li><span>500</span>元</li>
					</ul>
					<div class="otherMoney">
						<span>其他</span>
						<input type="number" name="diyMoney" id="diyMoney" value="" />
						<span>元</span>
					</div>
				</div>
				<div class="otherInfo">
					<p>其他信息(非必填)</p>
					<input type="text" name="realName" id="realName" value="${user.realName }" placeholder="您的姓名" />
					<input type="text" name="mobileNum" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" />
					<input type="text" name="donateWord" id="donateWord" value="" placeholder="${leaveWord }" />
				</div>
				<a href="javascript:void(0);" class="btn_a" id="btn_submit">立即捐款</a>
				<div class="prot">
					<input type="checkbox" name="agree" id="agree" checked="checked" /><label for="agree">同意并接受</label>
					<a href="http://www.17xs.org/redPackets/getAgreement.do" class="prot-link">《捐赠协议》</a>
				</div>
			</div>
		</div>
	</div>
	<div id="overLay"></div>
	<div class="bigPic">
		<div id="banner">
			<div class="hd"><ul></ul></div>
			<div class="bd"></div>
		</div>
	</div>
	<div class="tishi">点击右上角，邀请朋友一起参加公益<img src="../../../../res/images/h5/images/together/arrow.png" /></div>
	<div class="tips"></div>
	<input type="hidden" id="state" value="${project.state }"/>
	<input type="hidden" name="projectId" id="projectId" value="${project.id}" />
	<input id="needmoney" type="hidden" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
	<script src="../../../../res/js/jquery-1.8.2.min.js"></script>
	<script src="../../../../res/js/common/TouchSlide.1.1.js"></script>
	<script src="../../../../res/js/h5/projectDebrief/debriefing.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script>
		document.getElementById('process').style.minHeight = screen.height+'px';
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
						title : '${project.title}', // 分享标题
						desc : '${project.subTitle}', // 分享描述
						link : 'http://www.17xs.org/project/project_debriefDetail_view.do?projectId=${project.id}', // 分享链接
						imgUrl : '${project.coverImageUrl}', // 分享图标
						type : 'link', // 分享类型,music、video或link，不填默认为link
						dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
						success : function() {
							//用户确认分享后执行的回调函数
							$('#payBox').fadeOut();
							$('.tishi').hide();
//	 						window.location.href = 'http://www.17xs.org/';
						},
						cancel : function() {
							//用户取消分享后执行的回调函数
							$('#payBox').fadeOut();
							$('.tishi').hide();
						}
					});
					
			wx.onMenuShareTimeline({
				title : '${project.title}', // 分享标题
				link : 'http://www.17xs.org/project/project_debriefDetail_view.do?projectId=${project.id}', // 分享链接
				imgUrl : '${project.coverImageUrl}', // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
					$('#payBox').fadeOut();
					$('.tishi').hide();
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
					$('#payBox').fadeOut();
					$('.tishi').hide();
				}
		});
	});
	</script>
</body>
</html>