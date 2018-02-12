<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java"%>
	<%@ include file="./../../common/file_url.jsp"%>
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<meta name="viewport" />
	<title>${project.title}</title>
	<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=<%=base_version%>" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail.css?v=<%=detail_version%>" />
	<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=<%=detail_version%>">
	<link rel="stylesheet" type="text/css" href="/res/css/h5/specialProject/fuwa-fund.css?v=20171131" />
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<body>
	<div id="pageContainer">
		<div id="pageScroll">
			<div class="project">
				<!--焦点图//-->
				<div id="mySwipe" style="margin:0px auto;" class="swipe">
					<div class="swipe-wrap">
						<div><a href="">
							<div class="hot">
								<img src="${project.coverImageUrl}" class="img_big">
							</div>
						</a></div>
					</div>
				</div>
				
				<div class="clear"></div>
				<!--详情信息//-->
				<div class="detail_info">
					<p class="title">${project.title}</p>
					<!--个人信息卡//-->
					<div class="profile clearfix">
						<ul class="data_list clearfix">
							<li><i>用户捐赠/元</i><b>
								<span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value="${project.donatAmount}" /></span></b>
							</li>
							<li class="last"><i>捐款人次/次</i><b><span class="num">${peopleNum}</span></b></li>
							<li class="last"><i>受助人数/人</i><b><span class="num">${sFund.helpnum }</span></b></li>
						</ul>
					</div>
	
					<!-- 志愿者start -->
					<c:if test="${project.isNeedVolunteer == 1}">
						<div class="apply_box">
							<div class="apply_mast fr">
								<p class="fl">
									已有<span>${number }</span>个志愿者报名
								</p>
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
					<!-- 志愿者end -->
	
					<div class="clear_w"></div>
					<!--关注项目//-->
					<ul class="content_tabs">
						<a href="javascript:;" data-id="a">
							<li class="cur" id="xiangq">项目介绍</li>
						</a>
						<a href="javascript:;" data-id="b">
							<li id="dongt">项目动态<span></span></li>
						</a>
						<a href="javascript:;" data-id="c">
							<li id="jil">捐助记录<span></span></li>
						</a>
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
										<iframe src="${img.description}" allowfullscreen="" frameborder="0" width="100%" height="100%"> </iframe>
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
								发起单位：<span>${sFund.name }</span>
							</div>
							<div class="det_bottom">
								<div style="padding: 0 5px;background: #f3f3f3;line-height: 24px;">
									${sFund.content }
								</div>
							</div>
						</div>
	
						<div class="detail_reselse">
							<div class="det_top">
								受助群众：<span>${sFund.reserve1 }</span>
							</div>
						</div>
	
						<div class="detail_reselse">
							<div class="det_top">
								<p>善款接收：宁波市善园公益基金会（公募）</p>
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
						<div class="record_list" id="c">捐助记录
							<a href="/together/record_view.do?projectId=${project.id}">
								<img src="/res/images/h5/images/projectTopic/aixin.png"/>爱心贡献榜
							</a>
						</div>
						<div class="redBorder"></div>
						<div class="list-box">
						</div>
						<div class="loadmore">
							<a href="javascript:;" class="aload">点击加载更多</a>
						</div>
					</div>
	
					<div class="entered" style="margin-bottom:5%">
						<div class="record_list">
							已报名
							<span class="enter-user-num"><label id="entered-num">${totalUser }</label>人</span>
						</div>
						<div class="user_pic">
							<ul><c:forEach items="${cUsers }" var="cUsers">
								<c:choose>
									<c:when test="${cUsers.headImage==null || cUsers.headImage=='' }">
										<li><img src="http://www.17xs.org/res/images/detail/people_avatar.jpg" /></li>
									</c:when>
									<c:otherwise>
										<li><img src="${cUsers.headImage }" /></li>
									</c:otherwise>
								</c:choose>
							</c:forEach></ul>
							<a href=""></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!--------支付窗-----//-->
		<div id="payTips">
			<div class="pay">
				<!-- 可用红包 strat -->
				<div class="redPaperWrap" style="display:none;" id="redPackets">
					<div class="redPaperTop">
						<h1><a href="javascript:;" id="redPaperClick">使 用 红 包 捐 献 爱 心</a></h1>
						<a href="javascript:;" class="close" id="redclose"></a>
					</div>
					<ul class="redPaperList" id="redPaperList"></ul>
				</div>
				<!-- 可用红包end -->

				<div class="t">
					<a class="close" id="closeDetail" href="javascript:;">
						<img src="/res/images/h5/images/icon_close.png">
					</a>
					<span>请选择捐款金额</span>
				</div>
				<div class="box">
					<div class="content">
						<c:if test="${projectId == 429}">
							<div class="ppd-money" id="onceDonate">
								<ul class="ppd-ul">
									<li class="on"><span t="68">68</span>元</li>
									<li><span t="340">340</span>元</li>
									<li><span t="680">680</span>元</li>
									<li><span t="1360">1360</span>元</li>
								</ul>
								<div class="diyMoney">
									<input type="number" id="money2" placeholder="请输入其它金额" class="money"/>
								</div>
								<textarea placeholder="您可输入祝福语" rows="2" class="lytext" id="information"></textarea>
							</div>	
						</c:if>
						<c:if test="${projectId != 429}">
							<div class="ppd-money" id="onceDonate">
								<ul class="ppd-ul">
									<li class="on"><span t="20">20</span>元</li>
									<li><span t="50">50</span>元</li>
									<li><span t="100">100</span>元</li>
									<li><span t="500">500</span>元</li>
								</ul>
								<div class="diyMoney">
									<input type="number" id="money2" placeholder="请输入其它金额" class="money"/>
								</div>
								<textarea placeholder="${leaveWord}" rows="2" class="lytext" id="information"></textarea>
							</div>	
						</c:if>
					</div>
					
					<div class="btn">
						<a href="javascript:;" id="btn_submit">立即捐款</a>
					</div>
					
					<div class="prot">
						<input type="checkbox" id="check" name="check" checked="checked">
						<label for="check">同意并接受</label>
						<span class="p">
							<a href="/redPackets/getAgreement.do">《捐赠协议》</a>
						</span>
					</div>
				</div>
			</div>
		</div>
		<!------按钮//---->
		<div class="btns">
			<a href="/resposibilityReport/entryFormNewView.do?id=${sFund.form_id }" class="enter-btn">免费报名</a>
			<a href="javascript:;" id="paybtn" class="enter-btn">支持公益</a>
		</div>
		
		<div class="popup" style="display: none;"></div>
		<div class="confirmBox" style="display: none;">
			<a href="javascript:;" class="close" id="closeBtn"></a>
			<div class="confirmInner">
				<%--<div class="top">
					<img src="${project.coverImageUrl}">
				</div>--%>
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
				<a href="javascript:;" class="btn" id="confirmBtn">确&nbsp;&nbsp;定</a>
			</div>
		</div>
		<div id="bigImg">
			<div class="hd">
				<ul></ul>
			</div>
			<div class="bd"></div>
			<div class="pageState"></div>
		</div>
	</div>
	
	<input type="hidden" id="goodLibrary" value="${goodLibrary}">
	<input type="hidden" id="pprim" value="${project.title}">
	<input type="hidden" id="browser" value="${browser }">
	<input type="hidden" id="userId" value="${userId}">
	<input type="hidden" id="projectId" value="${project.id}">
	<input type="hidden" id="extensionPeople" value="${extensionPeople}">
	<input type="hidden" name="slogans" id="slogans" value="${slogans}" />
	<input type="hidden" id="needmoney" value="<fmt:formatNumber type="number" pattern="###0.00#" value="${project.cryMoney-project.donatAmount} "/>">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript">
		$(function() {
			(function() {
				$(".user_pic li").each(function() {
					$(this).css({"marginLeft": -5 + "px"});
				});
			})();
			<%--点击查看全文--%>
			setTimeout(function(){
				var oldH = $(".xiangqing").height();
				if(oldH > 450) {
					$(".xiangqing_box").addClass("addp");
					$(".detail-mask").show();
					$('.xiangqing').height(450);
				} else {
					$(".xiangqing_box").removeClass("addp");
					$(".detail-mask").hide();
				}
			},100);
			
			$(".last_btn").click(function() {
				if($(this).text() == "查看全文") {
					$(this).text("收起");
					$(".xiangqing_box").removeClass("addp");
					$(".detail-mask").removeClass("detail-mask-bg");
					$(".xiangqing").css({height:'auto'});
				} else {
					$(this).text("查看全文");
					$(".xiangqing_box").addClass("addp");
					$(".detail-mask").addClass("detail-mask-bg");
					$(".xiangqing").height(450);
					$('#a')[0].scrollIntoView();
				}
			});
		});
	</script>
	<script type="text/javascript">
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger') {
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
					link: 'http://www.17xs.org/uCenterProject/specialProject_details.do?projectId=${project.id}',
					imgUrl: '${project.coverImageUrl}', // 分享图标
					type: 'link', // 分享类型,music、video或link，不填默认为link
					dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
					success: function() {
						//用户确认分享后执行的回调函数
						var projectId = $('#projectId').val();
						$.ajax({
							url: "http://www.17xs.org/uCenterProject/saveRedisShareCount.do",
							data: {projectId: projectId},
							success: function(result) {
								if(result.flag == 1) { //成功
									alter("分享成功！");
								} else {
									alter("分享失败！");
								}
							}
						});
					},
					cancel: function() {
						//用户取消分享后执行的回调函数
					}
				});

				wx.onMenuShareTimeline({
					title: '${project.title}', // 分享标题
					link: 'http://www.17xs.org/uCenterProject/project_details.do?projectId=${project.id}',
					imgUrl: '${project.coverImageUrl}', // 分享图标
					success: function() {
						// 用户确认分享后执行的回调函数
						var projectId = $('#projectId').val();
						$.ajax({
							url: "http://www.17xs.org/uCenterProject/saveRedisShareCount.do",
							data: {projectId: projectId},
							success: function(result) {
								if(result.flag == 1) { //成功
									alter("分享成功！");
								} else {
									alter("分享失败！");
								}
							}
						});
					},
					cancel: function() {
						// 用户取消分享后执行的回调函数
					}
				});
			});
		}
		var loginUrl = "/ucenter/user/Login_H5.do?flag=special_${project.id}_extensionPeople_${extensionPeople}";
	</script>
	<script src="/res/js/h5/donateWordFn.js?v=20171130"></script>
	<script data-main="/res/js/h5/specialProject/userCenterProjectdetail.js?v=20180103" src="/res/js/require.min.js"></script>
	<%@ include file="./../cs.jsp"%>
	<% CS cs = new CS(1257726653);
	cs.setHttpServlet(request, response);
	String imgurl = cs.trackPageView();%>
	<img src="<%=imgurl%>" width="0" height="0" />
	<%--提示框--%>
	<div class="prompt_box" style="display:none" id="loginCue">
		<div class="cue_back"></div>
		<div class="cue1">
			<div class="cue_center">
				<div class="cue_center1">
					<p class="cue_p1">捐款确认</p>
					<p class="cue_p3">您目前还没有登录善园网，是否继续匿名捐助</p>
				</div>
				<div class="cue_center2">
					<a href="javascript:;">
						<div class="cue_fl" id="cue_fl">
							<p class="cue_pl">登录</p>
						</div>
					</a>
					<a href="javascript:;">
						<div class="cue_fr" id="cue_fr">
							<p class="cue_pr">匿名捐助</p>
						</div>
					</a>
				</div>
			</div>
		</div>
	</div>
	<!--善库充值弹框提示-->
	<div class="prompt_box" style=" display:none" id="goodLibrary_prompt">
		<div class="cue_back"></div>
		<div class="cue1">
			<div class="cue_center" style="height: 158px;">
				<div class="cue_center1">
					<p class="cue_p1">请选择支付方式</p>
					<p class="cue_p3">您已经是善库成员，您可用使用善库中的善款进行捐赠， 请选择本次捐赠的支付方式。</p>
				</div>
				<div class="cue_center2">
					<a href="javascript:;" class="ui-link">
						<div class="cue_fl" id="goodLibraryPay">
							<p class="cue_pl">使用善库支付余额</p>

						</div>
					</a>
					<a href="javascript:;" class="ui-link">
						<div class="cue_fr" id="rightNowPay">
							<p class="cue_pr">立即支付</p>
						</div>
					</a>
				</div>
			</div>
		</div>
	</div>
	<!--余额不足时弹框提示-->
	<div class="prompt_box" style="display:none" id="payMoney_prompt">
		<div class="cue_back"></div>
		<div class="cue1">
			<div class="cue_center" style="height: 158px;">
				<div class="cue_center1">
					<p class="cue_p1">捐款提示</p>
					<p class="cue_p3" id="payMoney_content">捐款金额不低于0.01元</p>
				</div>
				<div class="cue_center2">
					<a href="javascript:;" class="ui-link" id="payMoney_promptConfirm">
						<div class="cue1_flr">
							<p class="cue_pl1">确认</p>
						</div>
					</a>
				</div>
			</div>
		</div>
	</div>
	<!--回复留言-->
	<div class="moved_up" style="display: none">
		<div class="back"></div>
		<div class="end_center">
			<div class="end_hfk">
				<span class="end_r">发表评论：</span>
				<textarea class="end_text" cols="45" rows="7" id="content" placeholder=""></textarea>
			</div>
			<div class="end_foot">
				<a href="javascript:;">
					<div class="end_fl1 fl" id="leaveWordSubmit">确定</div>
				</a>
				<a href="javascript:;">
					<div class="end_fl1 fr">取消</div>
				</a>
			</div>
		</div>
	</div>
	<!-- 发表评论 -->

	<%--提示信息--%>
	<div class="cue2" style="display: none" id="msg"></div>
	<input type="hidden" value="${itemType}" id="itemType">

	<%-- <input id="projectId" type="hidden" value="${leaveWord.projectId }"> --%>
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