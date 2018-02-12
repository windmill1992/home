<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" />
<title>${project.title}</title>
<link rel="stylesheet" type="text/css" href="/res/css/dev/base.css?v=20160323" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="/res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/mon_donation.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/donate.css">
<link rel="stylesheet" type="text/css" href="/res/css/h5/detail.css?v=20171117" />
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<style type="text/css">
	.load-more {
		text-align: center;margin: 10px auto;height: 40px;line-height: 40px;font-size: 14px;
	}
	.aload {
		text-decoration: none;display: block;color: #FF6810;
	}
</style>
</head>
<body>
	<div id="pageContainer">
		<div id="pageScroll">
			<div class="project">
				<!--焦点图//-->
				<div id="mySwipe" style="margin:0px auto; overflow: hidden; visibility: visible;" class="swipe">
					<div class="swipe-wrap">
						<div><a href="">
							<div class="hot"> <img src="${project.coverImageUrl}" class="img_big"> </div>
						</a></div>
					</div>
				</div>
				
				<!--详情信息//-->
				<div class="detail_info">
					<c:choose>
						<c:when test="${project.state == 260}">
							<div class="finish"><i>已结束</i><b>${project.title}</b></div>
						</c:when>
						<c:otherwise>
							<div class="info"><i>筹款中</i><b>${project.title}</b></div>
						</c:otherwise>
					</c:choose>
					<!--进度信息//-->
					<div class="progress">
						<div class="progress_bar">
							<span class="track"><i class="fill" data-width="${process}"></i></span>
							<label>${processbfb}%</label>
						</div>
					</div>
					<!--个人信息卡//-->
					<div class="profile clearfix">
						<ul class="data_list clearfix">
							<li><i>目标金额/元</i><b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.#" value = "${project.cryMoney}"/></span></b></li>
							<li><i>已筹金额/元</i><b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/></span></b></li>
							<li><i>支持人数/人</i><b><span class="num">${peopleNum}</span></b></li>
						</ul>
					</div>
	
					<div class="clear"></div>
					<!-- 志愿者start -->
					<c:if test="${project.isNeedVolunteer == 1}">
						<div class="apply_box">
							<div class="apply_mast fr">
								<p class="fl">已有<span>${number }</span>个志愿者报名</p>
							</div>
							<div class="clear"></div>
	
							<div class="apply_volunteer">
								<div class="apply_zyz">
									<c:forEach items="${alist}" var="volunteer">
										<div class="apply_raise cut20 fl tc">
											<img src="/res/images/h5/images/tx1.png" />
											<c:if test="${volunteer.personType == 1}">
												<p>${volunteer.groupName}</p>
											</c:if>
											<c:if test="${volunteer.personType == 0}">
												<p>${volunteer.name}</p>
											</c:if>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</c:if>
					<div class="apply_kong"></div>
					<!-- 志愿者end -->
					<div class="clear_w"></div>
					<!--关注项目//-->
					<ul class="content_tabs">
						<li class="cur">详情</li>
						<li>发起人</li>
						<li>捐助记录</li>
						<li>反馈</li>
					</ul>
					<div class="clear"></div>
					<!--详情//-->
					<div class="list" id="list_1">
						<div class="content">
							<p>${project.content}</p><br/>
							<c:forEach items="${project.bfileList}" var="img">
								<p class="pic">
									<c:if test="${img.fileType !='video'}">
										<img src="${img.url}" width="100%" alt="">
										<c:if test="${img.description!='1'}">
											<i>${img.description}</i>
										</c:if>
									</c:if>
									<c:if test="${img.fileType =='video'}">
										<iframe src="${img.description}" allowfullscreen="" frameborder="0" width="100%" height="100%"></iframe>
									</c:if>
								</p>
							</c:forEach>
							<div class="clear"></div>
						</div>
					</div>
	
					<!--发起人//-->
					<div class="list" id="list_2" style="display:none;">
						<div class="publisher">
							<div class="item">
								<c:if test="${fabu.headImageUrl != null}">
									<div class="thumb"><img src="${fabu.headImageUrl}"></div>
								</c:if>
								<c:if test="${fabu.headImageUrl == null}">
									<div class="thumb"><img src="/res/images/detail/people_avatar.jpg"></div>
								</c:if>
								<ul class="info-ul">
									<li class="title">发起人
										<c:if test="${fabu.relation==null||fabu.relation==''}"></c:if>
										<c:if test="${fabu.relation!=null&&fabu.relation!=''}"><i>与求助者关系是${fabu.relation}</i></c:if>
									</li>
									<li>${fabu.workUnit}</li>
									<li><span>联系人：${fabu.realName}</span><span><c:if test="${fabu.vocation==null||fabu.vocation==''}"></c:if><c:if test="${fabu.vocation!=null&&fabu.vocation!=''}">职业：${fabu.vocation}</c:if></span></li>
									<li>电话：${fabu.linkMobile}</li>
									<li>地址：${fabu.familyAddress}</li>
								</ul>
							</div>
							<div class="clear"></div>
						</div>
					</div>
					<!----捐助人--//-->
					<div class="list" id="list_3" style="display:none;">
						<div class="list-box"></div>
						<div class="load-more">
							<a href="javascript:;" class="aload">点击加载更多</a>
						</div>
					</div>
	
					<!--反馈//-->
					<div class="list status" id="list_4" style="display:none;"></div>
					<!--页脚//-->
					<div class="footer"> <span>© 2015 杭州智善  版权所有</span> <img src="/res/images/h5/images/min-logo.jpg"></div>
				</div>
			</div>
		</div>
		
		<!------按钮//---->
		<div class="btns">
			<div>
				<table style="width:100%;" cellspacing="0">
					<tbody>
						<tr>
							<td width="50%">
								<a href="javascript:;">
									<c:choose>
										<c:when test="${project.state == 260}">
											<div class="btn single"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">募捐已结束</div>
										</c:when>
										<c:when test="${project.state == 240}">
											<div class="btn" id="paybtn"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要支持</div>
										</c:when>
										<c:otherwise>
											<div class="btn single"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">未发布</div>
										</c:otherwise>
									</c:choose>
								</a>
							</td>
							<c:if test="${project.isNeedVolunteer == 1}">
								<td width="50%">
									<a href="/project/volunteer.do?projectId=${project.id}">
										<div class="btn"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon2.png">加入志愿者(${number})</div>
									</a>
								</td>
							</c:if>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		
		<div id="bigImg">
			<div class="hd">
				<ul></ul>
			</div>
			<div class="bd"></div>
			<div class="pageState"></div>
		</div>
		
		<input type="hidden" id="pprim" value="${project.title}">
		<input type="hidden" id="browser" value="${browser }">
		<input type="hidden" id="userId" value="${userId}">
		<input type="hidden" id="projectId" value="${project.id}">
		<input type="hidden" id="extensionPeople" value="${extensionPeople}">
		<input type="hidden" name="slogans" id="slogans" value="${slogans}" />
		<input type="hidden" id="needmoney" value="<fmt:formatNumber type="number" pattern="###0.00#" value="${project.cryMoney-project.donatAmount} "/>">
		<script src="/res/js/jquery-1.8.2.min.js" /></script>
		<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<script type="text/javascript">
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
					title: '${project.title}', // 分享标题
					desc: '${desc}', // 分享描述
					link: 'http://www.17xs.org/project/view_h5.do?projectId=${project.id}', // 分享链接
					imgUrl: '${project.coverImageUrl}', // 分享图标
					type: 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
					success: function() {
						//用户确认分享后执行的回调函数
						//window.location.href = 'http://www.17xs.org/';
					},
					cancel: function() {
						//用户取消分享后执行的回调函数
					}
				});

				wx.onMenuShareTimeline({
					title: '${project.title}', // 分享标题
					link: 'http://www.17xs.org/project/view_h5.do?projectId=${project.id}', // 分享链接
					imgUrl: '${project.coverImageUrl}', // 分享图标
					success: function() {
						// 用户确认分享后执行的回调函数
					},
					cancel: function() {
						// 用户取消分享后执行的回调函数
					}
				});
			});
			$(function() {
				$(".tabBox li").click(function() {
					$(".infoBox").eq($(this).index()).show().siblings(".infoBox").hide();
				});
				$("#ask").toggle(function() {
					$(".donateSpec").show();
				}, function() {
					$(".donateSpec").hide();
				});

			});
		</script>
		<script data-main="/res/js/h5/detail_crowdfunding.js?v=20171117" src="/res/js/require.min.js"></script>
		<%@ include file="./../cs.jsp" %>
		<%CS cs = new CS(1257726653);cs.setHttpServlet(request,response);
			String imgurl = cs.trackPageView();%>
		<img src="<%= imgurl %>" width="0" height="0" />
		<%--提示框--%>
		<div class="prompt_box" style="display:none" id="loginCue">
			<div class="cue_back"></div>
			<div class="cue1">
				<div class="cue_center">
					<div class="cue_center1">
						<p class="cue_p1">捐款确认</p>
						<p class="cue_p3">您目前还没有登录善园网，请先登录</p>
					</div>
					<div class="cue_center2">
						<a href="javascript:;">
							<div class="cue_fl" id="cue_fl">
								<p class="cue_pl">登录</p>
							</div>
						</a>
						<a href="javascript:;">
							<div class="cue_fr" id="cue_fr">
								<p class="cue_pr">取消</p>
							</div>
						</a>
					</div>
				</div>
			</div>
		</div>
	<input type="hidden" value="${itemType}" id="itemType">
</body>
</html>