<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<title>${project.title}</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css"/>
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail.css?v=20171201" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=20171201">
<link rel="stylesheet" type="text/css" href="/res/css/h5/specialProject/fuwa-fund.css?v=20171201"/>
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<style type="text/css">
.load-more {
	text-align: center;width: 100%;height: 30px;line-height: 30px;font-size: 12px;color: #ff6810;
}
.aload {
	text-decoration: none;display: block;
}
.project .hot{
	position: relative;
}
.project .hot .hot-title{
	position: absolute;left: 10px;bottom: 10px;font-size: 15px;color: #fff;font-weight: 600;
}
</style>
</head>
<body>
	<div id="pageContainer">
		<div id="pageScroll">
			<div class="project">
				<!--焦点图//-->
				<div class="sdt-head">
					<div class="proWin">
						<div class="proWin-cover">
							<div class="swiper-container">
								<div class="swiper-wrapper">
									<div class="swiper-slide">
										<c:forEach items="${imgs }" var="img">
											<img src="${img }"/>
										</c:forEach>
									</div>
								</div>
							</div>
						</div>
						<div class="c-cell proWin-box">
							<div class="c-cell-m proWin-box-title">
								<h2>${project.title }&nbsp;
									<span class="proWin-state">进行中</span>
								</h2>
								<p>发起机构：${fabu.workUnit }</p>
							</div>
							<div class="proWin-box-data">
								<p class="total-help">累计帮扶</p>
								<p><span class="help-num">${countOneAid }</span>名</p>
							</div>
						</div>
					</div>
					<div class="c-cell">
						<div class="c-cell-m">${project.subTitle }</div>
					</div>
				</div>
				
				<!------按钮//---->
				<div class="toSupport">
					<c:choose>
						<c:when test="${isFinshed==0 }">
							<a href="javascript:;" class="btn" style="background:#ccc">帮扶已完成</a>
						</c:when>
						<c:otherwise>
							<a href="/uCenterProject/planSet_view.do?projectId=${project.id }&extensionPeople=${extensionPeople }" class="btn">我要资助</a>
						</c:otherwise>
					</c:choose>
				</div>
				
				<div class="clear"></div>
				<!--详情信息//-->
				<div class="detail_info">
					<!--个人信息卡//-->
					<div class="clear_w"></div>
					<!--关注项目//-->
					<ul class="content_tabs">
						<a href="javascript:;" data-id="a"><li class="cur" id="xiangq">项目介绍</li></a>
						<a href="javascript:;" data-id="b"><li id="dongt">项目动态<span></span></li></a>
						<a href="javascript:;" data-id="c"><li id="jil">捐助记录<span></span></li></a>
					</ul>
					<div class="clear"></div>
					<!-- 项目详情 -->
					<div class="xiangqing" id="a">
						<div class="xiangqing_box">
							<div class="pos"></div>
							<div id="content1" style="color:#999;padding: 4%">${project.content}</div>
							<%--项目详情图片--%>
							<c:forEach items="${project.bfileList}" var="img">
								<div class="xqImg">
									<c:if test="${img.fileType !='video'}">
										<img src="${img.url}" width="100%" alt="">
										<div class="imgText">${img.description }</div>
									</c:if>
									<c:if test="${img.fileType =='video'}">
										<iframe src="${img.description}" allowfullscreen=""
											frameborder="0" width="100%" height="100%"> </iframe>
									</c:if>
								</div>
							</c:forEach>
						</div>
						<div class="detail-mask detail-mask-bg">
							<div class="last_btn">查看全文</div>
						</div>
					</div>
					<!--发起人证实//-->
					<div class="detres">
						<div class="detail_reselse">
							<div class="det_top">
							 	 发起单位：<span>${fabu.workUnit }</span>
							</div>
							<div class="det_bottom">
								<div style="padding: 0 5px;background: #f3f3f3;line-height: 24px;">
									${fabu.info }
								</div>
							</div>
						</div>
						
						<div class="detail_reselse">
							<div class="det_top">
								受助群众：<span>${shouzhu.realName }</span>
							</div>
						</div>
						
						<div class="detail_reselse">
							<div class="det_top">
								<p>善款接收：宁波市善园公益基金会（公募）
									<a href="/resposibilityReport/resposibilityReport.do?id=368" class="link">
										<img src="/res/images/h5/images/donateStep/wen.png"/>
									</a>
								</p>
							</div>
						</div>
					</div>
					
					<div style="clear:both"></div>
					<!--项目动态//-->
					<div class="list" id="list_1">
						<div id="list_3" style="">
							<div class="record_list" id="b">项目动态</div>
							<div class="redBorder"></div>
							<!--2、反馈//-->
							<div class="status" id="list_4" style=""></div>
						</div>
					</div>
	
					<div class="clear"></div>
	
					<div class="" id="list_5">
						<div class="record_list" id="c">捐助记录</div>
						<div class="redBorder"></div>
						<div class="list-box"></div>
						<div class="load-more">
							<a href="javascript:;" class="aload">点击加载更多</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="popup" style="display: none;"></div>
	<div class="confirmBox" style="display: none;">
		<span class="close" id="closeBtn"></span>
		<div class="confirmInner">
			<div class="top">
				<img src="${project.coverImageUrl}">
			</div>
			<div class="bottom">
				<h4>${project.title}</h4>
				<p>${project.information}</p>
				<p class="hasDonate">
					已募集<i><fmt:formatNumber type="number" pattern="#,##0.00#"
							value="${project.donatAmount}" /></i>元,共捐款<i>${peopleNum}</i>人次
				</p>
			</div>
			<p class="otherInfo">
				为TA捐助<span><i id="payamount"></i>元</span>行善红包
			</p>
			<input type="button" value="确 定" class="btn" id="confirmBtn">
		</div>
	</div>
	<input type="hidden" id="pprim" value="${project.title}">
	<input type="hidden" id="browser" value="${browser }">
	<input id="projectId" type="hidden" value="${project.id}">
	<input id="extensionPeople" type="hidden" value="${extensionPeople}">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script>
		$(function() {
			(function(){
				$(".user_pic li").each(function(){
					$(this).css({"marginLeft":-5+"px"});
				});
			})();
			<%--点击查看全文--%>
			//找到p的高度
			//判断当p>200时隐藏
			//当点击按钮时p全部显示
			var len = $(".xiangqing").height();
			var oldContect = $("#content1").html();
			var content = $("#content1").text().trim();
			if(len > 150) {
				$(".xiangqing_box").addClass("addp");
				$(".detail-mask").show();
				$(".xuhua").show();
				$(".xqImg").addClass("xqImg1");
				$(".xqImg>img").addClass("xqImg2");
				$(".imgText").hide();
				if(content.length >= 200) {
					content = content.substring(0, 200) + '......';
					$("#content1").html('<p style="text-indent:2em;">'+content+'</p>');
				}
			} else {
				$(".xiangqing_box").removeClass("addp");
				$(".xqImg").removeClass("xqImg1");
				$(".xqImg>img").removeClass("xqImg2");
				$(".detail-mask").hide();
				$(".imgText").hide();
				$(".xuhua").hide();
			}
			$(".last_btn").click(function() {
				if($(this).text()=="查看全文"){
					$(this).text("收起");
					$(".xiangqing_box").removeClass("addp");
					$(".xqImg").removeClass("xqImg1");
					$(".xqImg>img").removeClass("xqImg2")
					$(".imgText").show();
					$(".xuhua").hide();
					$(".detail-mask").removeClass("detail-mask-bg");
					$("#content1").html(oldContect);
					var imgT=$(".imgText").text();
					if(imgT==""){
		 				$(".imgText").hide();
					}else{
						$(".imgText").show();
					}
				} else {
					$(this).text("查看全文");
					$(".xiangqing_box").addClass("addp");
					$(".xqImg").addClass("xqImg1");
					$(".xqImg>img").addClass("xqImg2")
					$(".imgText").hide();
					$(".xuhua").show();
					$(".detail-mask").addClass("detail-mask-bg");
					$("#content1").html(content);
					$(".imgText").hide();
					$('#a').scrollIntoView();
				}
			});
			wx.config({
				debug : false,
				appId : '${appId}',
				timestamp : '${timestamp}',
				nonceStr : '${noncestr}',
				signature : '${signature}',
				jsApiList : [ 'onMenuShareAppMessage', 'onMenuShareTimeline','previewImage' ]
			});
			wx.ready(function() {
				wx.onMenuShareAppMessage({
					title : '${project.title}', // 分享标题
					desc : '${desc}', // 分享描述
					link : 'http://www.17xs.org/uCenterProject/oneAid.do?projectId=${project.id}',
					imgUrl : '${project.coverImageUrl}', // 分享图标
					type : 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
					success : function() {
						//用户确认分享后执行的回调函数
					},
					cancel : function() {
						//用户取消分享后执行的回调函数
					}
				});
				wx.onMenuShareTimeline({
					title : '${project.title}', // 分享标题
					link : 'http://www.17xs.org/uCenterProject/oneAid.do?projectId=${project.id}',
					imgUrl : '${project.coverImageUrl}', // 分享图标
					success : function() {
						// 用户确认分享后执行的回调函数
					},
					cancel : function() {
						// 用户取消分享后执行的回调函数
					}
				});
			});
		});
		var loginUrl = "/ucenter/user/Login_H5.do?flag=special_${project.id}_extensionPeople_${extensionPeople}";
	</script>
	<script src="/res/js/h5/donateWordFn.js"></script>
	<script data-main="/res/js/h5/specialProject/userCenterProjectdetail.js?v=20171201" src="/res/js/require.min.js"></script>
	<%@ include file="./../cs.jsp"%>
	<% CS cs = new CS(1257726653);
		cs.setHttpServlet(request, response);
		String imgurl = cs.trackPageView();
	%>
	<img src="<%=imgurl%>" width="0" height="0" />
	<!--回复留言-->
	<div class="moved_up" style="display: none">
		<div class="back"></div>
		<div class="end_center">
			<div class="end_hfk">
				<span class="end_r">发表评论：</span>
				<textarea class="end_text" id="content" placeholder=""></textarea>
			</div>
			<div class="end_foot">
				<a href="javascript:;">
					<div class="end_fl1 fl" id="leaveWordSubmit">确定</div>
				</a> <a href="javascript:;">
					<div class="end_fl1 fr">取消</div>
				</a>
			</div>
		</div>
	</div>
    <!-- 发表评论 -->
    
	<%--提示信息--%>
	<div class="cue2" id="msg"></div>
	<input type="hidden" value="${itemType}" id="itemType">
	<input id="projectDonateId" type="hidden">
	<input id="projectFeedbackId" type="hidden">
	<input id="leavewordUserId" type="hidden">
	<input id="replyUserId" type="hidden">
	<input id="leavewordName" type="hidden">
	<input id="index" type="hidden">
	<input id="replyName" type="hidden">
	<input id="donateWord" type="hidden">
	<!-- 存储返回信息的当前页 -->
	<input id="currentPageForFeedBack" type="hidden" value="1">
	<input id="judge" type="hidden">
	<input id="auditProjectCount" type="hidden" value="${auditProjectCount }">
       
</body>
</html>