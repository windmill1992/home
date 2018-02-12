<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../../common/file_url.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<meta name="keywords" content="善园网，扶贫济困，公益援助，求助，募捐，医疗救助，教育救助，贫困救助，灾害救助，公益众筹，特殊群体" />
<meta name="description" content="善园网，依托善园公益基金会开展网络募捐服务，通过互联网实施扶贫济困、慈善救助、公益援助等领域的救助行动；善园网承诺善款100%公开，100%用于受助人" />
<title>${project.title}</title>
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=20171201" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail.css?v=<%=detail_version%>" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=<%=detail_version%>">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
</head>
<style type="text/css">
.load-more {
	text-align: center;margin: 10px auto;height: 40px;line-height: 40px;
	font-size: 14px;color: #3CC4C4;text-align: center;
}
.aload {
	text-decoration: none;display: block;
}
.end_center .end_foot .end_fl1{
	background-color: #3CC4C4;
}
.detail_info .content_tabs li.cur {
    color: #3CC4C4;border-bottom: 2px solid #3CC4C4;
}
.detail_info .record .name .name_p2{
	color: #3cc4c4;
}
.message_boardj .board_textj span{
	color: #3cc4c4;
}
.progress .progress_bar .track .fill {
    background-color: #3CC4C4;
}
.profile .data_list li b .num{
	color: #3cc4c4;
}
.detail-mask .last_btn:after{
	border-color: #3cc4c4;
}
.project .footer{
	text-align: center;line-height: 2;color: #666;
}
.redBorder{
	border-color: #3cc4c4;
}
.last_btn{
	color: #3cc4c4;
}
</style>
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
					<!--进度信息//-->
					<div class="progress">
						<div class="progress_bar">
							<span class="track"><i class="fill" data-width="${process}"></i></span>
							<label class="c3cc">${processbfb}%</label>
						</div>
					</div>
					<!--个人信息卡//-->
					<div class="profile clearfix">
						<ul class="data_list clearfix">
							<li>
								<i>目标金额/元</i>
								<b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.#" value="${project.cryMoney}" /></span></b>
							</li>
							<li>
								<i>已筹金额/元</i>
								<b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value="${project.donatAmount}" /></span></b>
							</li>
							<li class="last">
								<i>捐款人次/次</i>
								<b><span class="num">${peopleNum}</span></b>
							</li>
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
							<li class="cur" id="xiangq">项目详情</li>
						</a>
						<a href="javascript:;" data-id="b">
							<li id="dongt">项目动态</li>
						</a>
						<a href="javascript:;" data-id="c">
							<li id="jil">捐助记录</li>
						</a>
					</ul>
					<div class="clear"></div>
					<!-- 项目详情 -->
					<div class="xiangqing" id="a">
						<div class="xiangqing_box">
							<div class="pos"></div>
							<div id="content1" style="color:#999;padding: 3%">${project.content}</div>
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
							<div class="last_btn c3cc">查看全文</div>
						</div>
					</div>
					
					<div class="list" id="list_2">
						<div class="record_list">发起人</div>
						<div class="redBorder"></div>
						<div class="pub-info">
							<ul>
								<li class="title_size">${fabu.workUnit}</li>
								<li><span>联系人：${fabu.realName}</span>
									<span>
										<c:if test="${fabu.vocation==null || fabu.vocation==''}"></c:if>
										<c:if test="${fabu.vocation!=null && fabu.vocation!=''}">，职业：${fabu.vocation}</c:if>
									</span>
								</li>
								<li>电话：${fabu.linkMobile}</li>
								<li>地址：${fabu.familyAddress}</li>
								<li>善款接收：宁波市善园公益基金会</li>
							</ul>
						</div>
					</div>
					
					<!--项目动态//-->
					<div class="list" id="list_1">
						<div id="list_3">
							<div class="record_list" id="b">项目动态</div>
							<div class="redBorder"></div>
							<!--2、反馈//-->
							<div class="status" id="list_4"></div>
						</div>
					</div>
					<div class="clear"></div>
					<div class="" id="list_5" style="margin-bottom:40px">
						<div class="record_list" id="c">捐助记录</div>
						<div class="redBorder"></div>
						<div class="list-box"></div>
						<div class="load-more">
							<a href="javascript:;" class="aload">点击加载更多</a>
						</div>
					</div>
				</div>
	
				<div class="footer">
					<a href="http://yhbk.tw0577.com/">
						<span>©云湖帮困  版权所有 <br />技术支持：泰顺县困境儿童帮扶平台/智善网络</span>
					</a>
				</div>
			</div>
		</div>
		
		<!------按钮//---->
		<div class="btns">
			<div>
				<table style="width:100%;" cellspacing="0">
					<tbody>
						<tr>
							<c:if test="${project.isNeedVolunteer == 1}">
								<td width="50%">
									<a href="/project/volunteer.do?projectId=${project.id}" id="btn_submit2">
										<div class="btn invitebtn flex fcen spc">
											<img src="/res/images/h5/images/join_vol.png">加入志愿者(${number})
										</div>
									</a>
								</td>
							</c:if>	
							
							<td width="50%">
								<c:choose>
									<c:when test="${project.state == 260}">
										<a href="javascript:;">
											<div class="btn" style="background:#3CC4C4;border-color: #3CC4C4;" >
												<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">募捐已结束
											</div>
										</a>
									</c:when>
									<c:when test="${project.state == 230}">
										<a href="javascript:;">
											<div class="btn" style="background:#3CC4C4;border-color: #3CC4C4;" >
												<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">审核未通过
											</div>
										</a>
									</c:when>
									<c:when test="${project.state == 220}">
										<a href="javascript:;">
											<div class="btn" style="background:#3CC4C4;border-color: #3CC4C4;" >
												<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">未发布
											</div>
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:;" id="btn_submit" class="export">
											<div class="btn" style="background:#3CC4C4;border-color: #3CC4C4;" >
												<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要捐款
											</div>
										</a>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div id="bigImg">
		<div class="hd">
			<ul></ul>
		</div>
		<div class="bd"></div>
		<div class="pageState"></div>
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
	<input type="hidden" name="" id="owner" value="${owner}" />
	<input type="hidden" id="goodLibrary" value="${goodLibrary}">
	<input type="hidden" id="pprim" value="${project.title}">
	<input type="hidden" id="browser" value="${browser }">
	<input type="hidden" id="userId" value="${userId}">
	<input id="projectId" type="hidden" value="${project.id}">
	<input id="extensionPeople" type="hidden" value="${extensionPeople}">
	<input type="hidden" name="slogans" id="slogans" value="${slogans}" />
	<input id="needmoney" type="hidden" value="<fmt:formatNumber type=" number " pattern="###0.00# " value = "${(project.cryMoney-project.donatAmount)<0?0.00:(project.cryMoney-project.donatAmount)} "/>">
	<input type="hidden" name="information" id="information" value="${leaveWord}" />
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script>
		var baseUrl = 'http://www.17xs.org/';
		$(function() {
			(function(){
				var con = $('#content1').html();
				con = con.replace(/\n/g,'<br/>').replace(/\r\n/g,'<br/>').replace(/\s{2}/g,'&nbsp;&nbsp;');
				$('#content1').html(con);
				var h = $(window).width();
				$(".pic").find("iframe").css({
					height: h / 2 + 2 + "px"
				});
			})();
			/*点击查看全文*/
			//找到p的高度
			//判断当p>200时隐藏
			//当点击按钮时p全部显示
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
					title: '${project.title}', // 分享标题
					desc: '${desc}', // 分享描述
					link: 'http://www.17xs.org/export/view_h5.do?projectId=${project.id}&extensionPeople=${extensionPeople}',
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
									layer.msg("感谢您，转发成功");
								} else {
									layer.msg("转发失败");
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
					link: 'http://www.17xs.org/export/view_h5.do?projectId=${project.id}&extensionPeople=${extensionPeople}',
					imgUrl: '${project.coverImageUrl}', // 分享图标
					success: function() {
						// 用户确认分享后执行的回调函数
						var projectId = $('#projectId').val();
						$.ajax({
							url: "http://www.17xs.org/uCenterProject/saveRedisShareCount.do",
							data: {projectId: projectId},
							success: function(result) {
								if(result.flag == 1) { //成功
									layer.msg("感谢您，转发成功");
								} else {
									layer.msg("转发失败！");
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
		var loginUrl = "/ucenter/user/Login_H5.do?flag=export_${project.id}_extensionPeople_${extensionPeople}";
	</script>
	<script src="/res/layer/layer.js"></script>
	<script src="/res/js/h5/donateWordFn.js?v=20171130"></script>
	<script data-main="/res/js/h5/detail.js?v=201712201400" src="/res/js/require.min.js"></script>
	<%@ include file="./../cs.jsp"%>
	<% CS cs = new CS(1257726653);
		cs.setHttpServlet(request, response);
		String imgurl = cs.trackPageView();
	%>
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
						<div class="cue_fl pub" id="cue_fl">
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
	<input id="type" type="hidden" value="1">
</body>
</html>