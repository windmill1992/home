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
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=201801241900" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail.css?v=<%=detail_version%>" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=<%=detail_version%>">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<style type="text/css">
	.load-more {
		text-align: center;width: 100%;height: 30px;line-height: 30px;font-size: 13px;color: #ff6810;
	}
	.aload {
		text-decoration: none;display: block;
	}
</style>
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
							<div id="content1" style="color:#999;padding: 3%;">${project.content}</div>
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
					
					<div class="list" id="list_2">
						<div class="record_list">发起人
							<c:if test="${isCompany == 1}">
								<a href="/webhtml/view/h5/project/publisher.html?userId=${project.userId}"></a>
							</c:if>
						</div>
						<div class="redBorder"></div>
						<div class="pub-info">
							<ul>
								<li class="title_size">${fabu.workUnit}</li>
								<li><span>联系人：${fabu.realName}</span>
									<span>
										<c:if test="${fabu.vocation==null || fabu.vocation==''}"></c:if>
										<c:if test="${fabu.vocation!=null && fabu.vocation!=''}">，职业：${fabu.vocation}</c:if>
									</span>
									<c:if test="${isCompany == 2}">
										<a href="javascript:;" class="s-btn" id="personal">个人求助<img src="/res/images/h5/images/donateStep/wen.png"/></a>
									</c:if>
								</li>
								<li>电话：${fabu.linkMobile}</li>
								<li>地址：${fabu.familyAddress}</li>
								<li>善款接收：宁波市善园公益基金会
									<a href="/resposibilityReport/resposibilityReport.do?id=368" class="link">
										<img src="/res/images/h5/images/donateStep/wen.png"/>
									</a>
								</li>
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
						<div class="record_list" id="c">捐助记录
							<a href="/together/record_view.do?projectId=${project.id}">
								<img src="/res/images/h5/images/projectTopic/aixin.png"/>爱心贡献榜
							</a>
						</div>
						<div class="redBorder"></div>
						<div class="list-box"></div>
						<div class="load-more">
							<a href="javascript:;" class="aload">点击加载更多</a>
						</div>
					</div>
				</div>
	
				<div class="footer">
					<p>
						<span>©宁波市善园公益基金会</span><br />
						<span>电话：0571-87165191</span>
					</p>
					<a href="/"><img src="/res/images/h5/images/min-logo.jpg"></a>
				</div>
			</div>
			
		</div>
		
		<!------按钮//---->
		<div class="btns">
			<div>
				<table style="width:100%;" cellspacing="0">
					<tbody>
						<c:choose>
							<c:when test="${ (!empty project.formId && !empty project.buttonName) || project.isNeedVolunteer == 1}">
								<c:choose>
									<c:when test="${owner == false || empty owner}">
										<tr>
											<c:if test="${project.state == 240 || project.state == 210}">
												<td width="15%">
													<a href="/together/together_view.do?projectId=${project.id }" id="btn_submit2">
														<div class="btn invitebtn flex fcol fcen spc" style="font-size: 13px;">
															<img src="/res/images/h5/images/share.png" class="share-icon">转发
														</div>
													</a>
												</td>
												<c:if test="${project.isNeedVolunteer == 1}">
													<td width="40%">
														<a href="/project/volunteer.do?projectId=${project.id}" id="btn_submit2">
															<div class="btn invitebtn flex fcen spc" style="border-left: 1px solid #eee;">
																<img src="/res/images/h5/images/join_vol.png" class="vol-icon">加入志愿者(${number})
															</div>
														</a>
													</td>
												</c:if>
												<c:if test="${!empty project.formId && !empty project.buttonName}">
													<td>
														<a href="/resposibilityReport/entryFormNewView.do?id=${project.formId }" id="btn_submit2">
															<div class="btn invitebtn flex fcen spc" style="border-left: 1px solid #eee;">
																<img src="/res/images/h5/images/join_vol.png" class="vol-icon">${project.buttonName }
															</div>
														</a>
													</td>
												</c:if>
											</c:if>
											<td width="44%">
												<c:choose>
													<c:when test="${project.state == 260}">
														<div class="btn single" id="finishedbtn">
															<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">募捐已结束
														</div>
													</c:when>
													<c:when test="${project.state == 230}">
														<div class="btn single" id="nopass">
															<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">审核未通过
														</div>
													</c:when>
													<c:when test="${project.state == 220}">
														<div class="btn single" id="finishedbtn">
															<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">未发布
														</div>
													</c:when>
													<c:otherwise>
														<div class="btn" id="paybtn">
															<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要捐款
														</div>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr class="self">
											<td width="50%">
												<a href="javascript:;" id="inviteFriends3">
													<div class="btn">
														<img src="/res/images/h5/images/releaseProject/zhuanfa.png">开始筹款
													</div>
												</a>
											</td>
											<td width="25%">
												<a href="/uCenterProject/uCenterProjectList.do?state=${project.state}&currentPage=1">
													<div class="btn invitebtn">
														<p><img src="/res/images/h5/images/releaseProject/manage.png"></p><p>项目管理</p>
													</div>
												</a>
											</td>
											<td width="25%">
												<a href="/uCenterProject/projectFeedBack.do?projectId=${project.id}&state=${project.state}">
													<div class="btn invitebtn">
														<p><img src="/res/images/h5/images/releaseProject/fankui.png"></p><p>项目反馈</p>
													</div>
												</a>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${owner == false || empty owner}">
										<tr>
											<c:if test="${(project.state == 240 || project.state == 210) && browser == 'wx'}">
												<td width="50%">
													<a href="/together/together_view.do?projectId=${project.id }" id="btn_submit2">
														<div class="btn invitebtn flex fcen spc">
															<img src="/res/images/h5/images/releaseProject/invite.png">邀朋友一起捐
														</div>
													</a>
												</td>
											</c:if>
											<td width="50%">
													<c:choose>
														<c:when test="${project.state == 260}">
															<div class="btn single" id="finishedbtn">
																<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">募捐已结束
															</div>
														</c:when>
														<c:when test="${project.state == 230}">
															<div class="btn single" id="nopass">
																<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">审核未通过
															</div>
														</c:when>
														<c:when test="${project.state == 220}">
															<div class="btn single" id="finishedbtn">
																<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">未发布
															</div>
														</c:when>
														<c:otherwise>
															<div class="btn" id="paybtn">
																<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要捐款
															</div>
														</c:otherwise>
													</c:choose>
											</td>
										</tr>
									</c:when>
									<c:otherwise>
										<tr class="self">
											<td width="50%">
												<a href="javascript:;" id="inviteFriends3">
													<div class="btn">
														<img src="/res/images/h5/images/releaseProject/zhuanfa.png">开始筹款
													</div>
												</a>
											</td>
											<td width="25%">
												<a href="/uCenterProject/uCenterProjectList.do?state=${project.state}&currentPage=1">
													<div class="btn invitebtn">
														<p><img src="/res/images/h5/images/releaseProject/manage.png"></p><p>项目管理</p>
													</div>
												</a>
											</td>
											<td width="25%">
												<a href="/uCenterProject/projectFeedBack.do?projectId=${project.id}&state=${project.state}">
													<div class="btn invitebtn">
														<p><img src="/res/images/h5/images/releaseProject/fankui.png"></p><p>项目反馈</p>
													</div>
												</a>
											</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
		
		<!--------支付窗-----//-->
		<div id="payTips">
			<c:choose>
				<c:when test="${!empty moneyConfigs}">
					<div class="pay">
						<div class="t">
							<a class="close" id="closeDetail" href="javascript:;">
								<img src="/res/images/h5/images/icon_close.png">
							</a>
							<span>请输入捐款金额</span>
						</div>
						<div class="box">
							<div class="ppd-money">
								<ul class="ppd-ul clearFix">
									<c:forEach items="${moneyConfigs }" var="conf" varStatus="money">
										<c:choose>
											<c:when test="${money.first }">
												<li class="on"><span t="${conf.money }">${conf.money }</span>元</li>
											</c:when>
											<c:otherwise>
												<li><span t="${conf.money }">${conf.money }</span>元</li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
									<li class="user-defined special">自定义</li>
								</ul>
								<div class="diyMoney">
									<input type="text" name="dMoney" id="dMoney" value="" readonly="readonly" />
								</div>
							</div>

							<div class="ppd-otherinfo">
								<p>其他信息(非必填)</p>
								<input type="text" name="" id="realName" value="${user.realName }" placeholder="您的姓名" />
								<input type="text" name="" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" />
								<input class="zfText" id="donateWords" placeholder="${leaveWord }" />
							</div>

							<div class="ppd-pay">
								<div class="btn">
									<div class="btn-link" id="btn_submit">立即捐赠</div>
								</div>
							</div>

							<div class="ppd-prop">
								<input type="checkbox" name="chk" id="chk" checked="checked" />
								<label for="chk">我已阅读并同意</label>
								<span class="p">
									<a class="prop" href="/redPackets/getAgreement.do">《捐赠协议》</a>
								</span>
							</div>
						</div>	
					</div>
				</c:when>
				<c:otherwise>
					<div class="pay">
						<!-- 可用红包 strat -->
						<div class="redPaperWrap" style="display:none;" id="redPackets">
							<div class="redPaperTop">
								<h1>点 击 红 包 捐 献 爱 心</h1>
								<span class="close" id="redclose"></span>
							</div>
							<ul class="redPaperList" id="redPaperList"></ul>
						</div>
	
						<!-- 可用红包end -->
						<div class="t">
							<a class="close" id="closeDetail" href="javascript:;">
								<img src="/res/images/h5/images/icon_close.png">
							</a>
							<span>请输入捐款金额</span>
						</div>
						<div class="box">
							<div class="ppd-money" id="selectMoney">
								<ul class="ppd-ul clearFix">
									<li class="on"><span t="10">10</span>元</li>
									<li><span t="20">20</span>元</li>
									<li><span t="50">50</span>元</li>
									<li><span t="100">100</span>元</li>
									<li><span t="500">500</span>元</li>
									<li class="user-defined">自定义</li>
								</ul>
								<div class="diyMoney">
									<input type="text" name="dMoney" id="dMoney" value="" />
								</div>
							</div>
	
							<div class="ppd-otherinfo">
								<p>其他信息(非必填)</p>
								<input type="text" name="" id="realName" value="${user.realName }" placeholder="您的姓名" />
								<input type="text" name="" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" />
								<input class="zfText" id="donateWords" placeholder="${leaveWord }" />
							</div>
							<div class="btn">
								<div class="btn-link" id="btn_submit">立即捐赠</div>
							</div>
							<div class="ppd-prop">
								<input type="checkbox" name="chk" id="chk" checked="checked" />
								<label for="chk">我已阅读并同意</label>
								<span class="p">
									<a class="prop" href="/redPackets/getAgreement.do">《捐赠协议》</a>
								</span>
							</div>
						</div>
					</div>	
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div id="inviteDialog">
		<span>点击右上角分享给朋友或朋友圈吧</span>
		<img src="/res/images/h5/images/point.gif" />
	</div>
	<div class="dialog personal-dialog" id="personalDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h3>个人求助</h3>
			<ul>
				<li>● 该筹款项目信息由个人发布，不属于慈善公开募捐信息，善园提醒您了解项目后再帮助TA</li>
				<li>● 项目筹款最长不超过30天</li>
				<li>● 发起人需要提供相关材料进行审核</li>
				<li>● 可以邀请个人亲友帮忙传播筹款</li>
				<li>● 善款由受助人或者家属申请提取</li>
				<li>● 善园不收取任何费用，善款100%用于救助</li>
			</ul>
			<div class="db-btn">
				<a href="javascript:;" class="closeDialog">我知道了</a>
				<a href="/test/gotoProtocolRelease.do" class="closeDialog">我也要筹款</a>
			</div>
			<a href="javascript:;" class="closeLink closeDialog">
				<span class="close"><img src="/res/images/h5/images/icon_close.png"/></span>
			</a>
		</div>
	</div>
	<div class="dialog raiseIntroDialog" id="raiseIntroDialog">
		<div class="mask"></div>
		<div class="dialog_inner">
			<h3>筹款小技巧</h3>
			<ul>
				<li>
					<h4>1、人脉-亲朋好友助力</h4>
					<p>将受助人的亲朋好友拉到一个微信群，跟他们介绍情况，邀请他们一起帮忙转发推广；同时也可以和他们一起商量，听听大家的推广建议；另外由同学、同事、客户、记者帮忙转发，可以获得更多的信任与支持</p>
				</li>
				<li>
					<h4>2、时间-最佳推广时间</h4>
					<p>中午11-13点，晚上18-21点，此段时间转发会有更多的人看到</p>
				</li>
				<li>
					<h4>3、文案-转发时候的描述</h4>
					<p>筹款项目无论转发到群里或者朋友圈，不能只转发链接，与受助人关系、目前情况、呼吁性文字都是必要的</p>
				</li>
				<li>
					<h4>4、反馈-与捐赠人互动</h4>
					<p>不定时更新动态说明受助人近况或表示感谢；邀请亲朋帮您实名认证可以完善资料，并增加可信度</p>
				</li>
				<li>
					<h4>5、信心-筹款的决心</h4>
					<p>有恒心、有毅力才会得到更多的帮助；祝您或者家人早日康复</p>
				</li>
			</ul>
			<div class="db-btn">
				<a href="javascript:;" class="closeDialog">再等等</a>
				<a href="javascript:;" class="closeDialog share">已了解，开始筹款</a>
			</div>
			<a href="javascript:;" class="closeLink closeDialog">
				<span class="close"><img src="/res/images/h5/images/icon_close.png"/></span>
			</a>
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
	<input type="hidden" id="projectId" value="${project.id}">
	<input type="hidden" id="extensionPeople" value="${extensionPeople}">
	<input type="hidden" name="slogans" id="slogans" value="${slogans}" />
	<input type="hidden" id="needmoney" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${(project.cryMoney-project.donatAmount)<0?0.00:(project.cryMoney-project.donatAmount)}"/>" />
	<input type="hidden" id="overMoney" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.overMoney-project.donatAmount}"/>" />
	<input type="hidden" id="isOverMoney" value="${project.isOverMoney}" />
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
					link: 'http://www.17xs.org/project/view_h5.do?projectId=${project.id}',
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
									alert("分享成功！");
								} else {
									alert("分享失败！");
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
					link: 'http://www.17xs.org/project/view_h5.do?projectId=${project.id}',
					imgUrl: '${project.coverImageUrl}', // 分享图标
					success: function() {
						// 用户确认分享后执行的回调函数
						var projectId = $('#projectId').val();
						$.ajax({
							url: "http://www.17xs.org/uCenterProject/saveRedisShareCount.do",
							data: {projectId: projectId},
							success: function(result) {
								if(result.flag == 1) { //成功
									alert("分享成功！");
								} else {
									alert("分享失败！");
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
		var loginUrl = "/ucenter/user/Login_H5.do?flag=projectId_${project.id}_extensionPeople_${extensionPeople}";
	</script>
	<script src="/res/js/h5/donateWordFn.js?v=20171130"></script>
	<script data-main="/res/js/h5/detail.js?v=201801241330" src="/res/js/require.min.js"></script>
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
					<div class="cue_fl pub" id="cue_fl">
						<p class="cue_pl">登录</p>
					</div>
					<div class="cue_fr" id="cue_fr">
						<p class="cue_pr">匿名捐助</p>
					</div>
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
	<input type="hidden" name="type1" id="type1" value="${type}" />
	<input type="hidden" name="gotoType" id="gotoType" value="${gotoType}" />
</body>
</html>