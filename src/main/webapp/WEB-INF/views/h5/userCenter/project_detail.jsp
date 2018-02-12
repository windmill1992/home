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
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=20171131" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail.css?v=201712271100" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=201712201400">
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
</style>
</head>
<body>
	<div id="pageContainer">
		<div id="pageScroll">
			<div class="detail_head">
				<a href="/" class="to-index">首页</a>
				<div class="detailconter">
					<img src="/res/images/releaseProject/det_log.png">
					<span>善园网</span>
				</div>
				<div class="detailfr"">
					<a href="/test/gotoProtocolRelease.do">
						<img src="/res/images/releaseProject/det_pr.png">
						<span>发布</span>
					</a>
				</div>
			</div>
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
					<p class="title">${project.title }</p>
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
							<li><i>目标金额</i>
								<b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.#" value="${project.cryMoney}" /></span>元</b>
							</li>
							<li><i>已筹金额</i>
								<b><span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value="${project.donatAmount}" /></span>元</b>
							</li>
							<li class="last"><i>捐款人次</i><b><span class="num">${peopleNum}</span>次</b></li>
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
						<a href="javascript:;" data-id="a"><li class="cur" id="xiangq">项目详情</li></a>
						<a href="javascript:;" data-id="b"><li id="dongt">项目动态</li></a>
						<a href="javascript:;" data-id="c"><li id="jil">捐助记录</li></a>
					</ul>
					<div class="clear"></div>
					<!-- 				项目详情 -->
					<div class="xiangqing" id="a">
						<div class="xiangqing_box">
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
							    <c:choose>
								     <c:when test="${fabu.helpType==4 || fabu.helpType==5}">
								     	  <div class="detTop_fl fl">
												发起单位：<span>${fabu.workUnit }</span>
										  </div>
								     </c:when>
								     <c:when test="${fabu.helpType==1 || fabu.helpType==2 || fabu.helpType==3}">
								         <div class="detTop_fl fl">
												发起人：<span>${frontUser.nickName }</span>
										  </div>
								     </c:when>
								     <c:otherwise>
								          <c:choose>
								              <c:when test="${individal==0 }">
								              		<div class="detTop_fl fl">
													    发起单位：<span>${fabu.workUnit }</span>
											        </div>
								              </c:when>
									          <c:otherwise>
									                <div class="detTop_fl fl">
													    发起人：<span>${frontUser.nickName }</span>
											        </div>
									          </c:otherwise>
										  </c:choose>
								     </c:otherwise>
								</c:choose>
								<c:choose>
								     <c:when test="${fabu.helpType==4 || fabu.helpType==5}">
								     	  <div class="detTop_fr fr" id="institutionReleasePerson">查看信息&gt;</div>
								     </c:when>
								     <c:when test="${individal==0 }">
								          <div class="detTop_fr fr" id="PCinstitutionReleasePerson">查看信息&gt;</div>
								     </c:when>
								     <c:otherwise>
								          <div class="detTop_fr fr" id="releasePerson">查看信息&gt;</div>
								     </c:otherwise>
								</c:choose>
							</div>
							<div class="clear"></div>
							<div class="det_bottom">
								<div class="detBottom_fl">
								    <c:choose>
								        <c:when test="${fabu.helpType==4 || fabu.helpType==5}">
							                <c:choose>
					        					<c:when test="${company.state==201}">
					        					      <i class="detImg detImg2"></i>机构信息未审核
												</c:when>
												<c:when test="${company.state==203}">
					        					      <i class="detImg detImg1"></i>机构信息已审核
												</c:when>
												<c:otherwise >
													 <i class="detImg detImg2"></i>机构信息审核未通过
												</c:otherwise>
									         </c:choose>
								        </c:when>
								        <c:when test="${fabu.helpType==9 || fabu.helpType==10}">
							                <c:choose>
					        					<c:when test="${frontUser.realState==201}">
					        					      <i class="detImg detImg2"></i>机构信息未审核
												</c:when>
												<c:when test="${frontUser.realState==203}">
					        					      <i class="detImg detImg1"></i>机构信息已审核
												</c:when>
												<c:otherwise >
													 <i class="detImg detImg2"></i>机构信息审核未通过
												</c:otherwise>
									         </c:choose>
								        </c:when>
								        <c:when test="${fabu.helpType==1 || fabu.helpType==2 || fabu.helpType==3 || fabu.helpType==6 || fabu.helpType==7 || fabu.helpType==8}">
								            <c:choose>
						        					<c:when test="${frontUser.realState==201}">
						        					      <i class="detImg detImg2"></i>个人信息未审核
													</c:when>
													<c:when test="${frontUser.realState==203}">
						        					      <i class="detImg detImg1"></i>个人信息已审核
													</c:when>
													<c:otherwise >
														 <i class="detImg detImg2"></i>个人信息审核未通过
													</c:otherwise>
										     </c:choose>
								        </c:when>
								        <c:otherwise>
								             <c:choose>
					        					<c:when test="${frontUser.userType eq 'individualUsers'}">
					        					   <c:choose>
						        					  	<c:when test=" ${frontUser.realState==201 }">
						        					      <i class="detImg detImg2"></i>个人信息未审核
														</c:when>
														<c:when test="${frontUser.realState==203 }">
						        					      <i class="detImg detImg1"></i>个人信息已审核
														</c:when>
														<c:otherwise >
														 <i class="detImg detImg2"></i>个人信息审核未通过
														</c:otherwise>
													</c:choose>
												</c:when>
												
												<c:otherwise >
												   <c:choose>
														 <c:when test="${frontUser.realState==201}">
						        					      	<i class="detImg detImg2"></i>机构信息未审核
														</c:when>
														<c:when test="${frontUser.realState==203}">
						        					      	<i class="detImg detImg1"></i>机构信息已审核
														 </c:when>
														 <c:otherwise >
														 	<i class="detImg detImg2"></i>机构信息审核未通过
														 </c:otherwise>
													 </c:choose>
												</c:otherwise>
										     </c:choose>
								        </c:otherwise>
								    </c:choose>	    
								</div>
								<div class="detBottom_fr">
								    <c:choose>
			        					<c:when test="${fabu.helpType==1 || fabu.helpType==6 }">
										</c:when>
										<c:when test="${fabu.helpType ==2 || fabu.helpType==7}">
											<i class="detImg detImg1"></i>关系证明已提交
										</c:when>
										<c:otherwise >
											 <i class="detImg detImg1"></i>已提交委托书
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<c:if test="${shouzhu.helpType!= 5 && shouzhu.helpType!= 10}">
							<div class="detail_reselse">
								<div class="det_top">
									<div class="detTop_fl fl">
										受助人：<span>${shouzhu.realName }</span>
									</div>
									<div class="detTop_fr fr" id="appealPerson">查看信息&gt;</div>
								</div>
								<div class="clear"></div>
								<div class="det_bottom">
									<div class="detBottom_fl">
									    <c:choose>
			        						<c:when test="${project.state == 210}">
					   							<i class="detImg detImg2"></i>个人信息还未审核
											</c:when>
											<c:when test="${project.state == 230}">
												<i class="detImg detImg2"></i>个人信息审核未通过
											</c:when>
											<c:otherwise >
											     <i class="detImg detImg1"></i>个人信息已审核
											</c:otherwise>
										 </c:choose>
									</div>
									<div class="detBottom_fr">
										<c:choose>
			        						<c:when test="${project.state == 210}">
					   							<i class="detImg detImg2"></i>事件还未审核
											</c:when>
											<c:when test="${project.state == 230}">
												<i class="detImg detImg2"></i>事件审核未通过
											</c:when>
											<c:otherwise >
											     <i class="detImg detImg1"></i>事件已审核
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</c:if>
						<div class="detail_reselse" style="height:30px">
							<div class="det_top">
								<div class="detTop_fl">
									<p>善款接收：宁波市善园公益基金会（公募）</p>
								</div>
							</div>
						</div>
					</div>
					<c:if test="${shouzhu.helpType!= 5 && shouzhu.helpType!= 10}">
						<div class="approve">
							<div class="approve_top">
								<div class="approve_fl">
									已有<span>${auditProjectCount }</span>位爱心人士证实
								</div>
								<div class="approve_fr">我也来证实&gt;</div>
							</div>
							<c:choose>
							    <c:when test="${auditProjectCount>0 }">
							    	<div class="approve_center" >
										<div class="approveImg">
											<c:forEach items="${auditProjectList }" var="ap">
												<img src="${ap.headUrl }" onclick="onclickAuditPerson(${ap.id})" class="boree">
											</c:forEach>
										</div>
									<span class="approveSp">&gt;</span>
									<div style="clear:both"></div>
									<%--头像描述--%>
									<div class="txms">
										<p class="txmsone"><span id="auditPersonName">皮卡丘</span>，<span id="relations">朋友</span></p>
										<div style="clear:both"></div>
										<p class="txmstwo" id="auditDirection" ></p>
										<div style="clear:both"></div>
									</div>
										<div style="clear:both"></div>
							        </div>
									<div style="clear:both"></div>
							    </c:when>
							    <c:otherwise>
							    	<span class="tipss">如果您了解受助人情况可以为ta证实</span>
							    </c:otherwise>
							</c:choose>
						</div>
						<div style="clear:both"></div>
					</c:if>
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
					
					<div class="footer">
						<p>
							<span>©宁波市善园公益基金会</span><br />
							<span>电话：0571-87165191</span>
						</p>
						<a href="/resposibilityReport/resposibilityReport.do?id=368"><img src="/res/images/h5/images/min-logo.jpg"></a>
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
						<h1>点 击 红 包 捐 献 爱 心</h1>
						<a class="close" id="redclose" href="javascript:;"></a>
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
					<div class="content">
						<c:if test="${projectId == 429}">
							<div class="ppd-money" id="selectMoney">
								<ul class="ppd-ul">
									<li class="on"><span t="68">68</span>元</li>
									<li><span t="340">340</span>元</li>
									<li><span t="680">680</span>元</li>
									<li><span t="1360">1360</span>元</li>
									<li class="user-defined">自定义</li>
								</ul>
								<div class="diyMoney">
									<input type="number" id="dMoney" placeholder="请输入其它金额" class="money"/>
								</div>
								<textarea placeholder="您可输入祝福语" rows="2" class="lytext" id="information"></textarea>
							</div>	
						</c:if>
						<c:if test="${projectId != 429}">
							<div class="ppd-money" id="selectMoney">
								<ul class="ppd-ul">
									<li class="on"><span t="20">20</span>元</li>
									<li><span t="50">50</span>元</li>
									<li><span t="100">100</span>元</li>
									<li><span t="500">500</span>元</li>
									<li class="user-defined">自定义</li>
								</ul>
								<div class="diyMoney">
									<input type="number" id="dMoney" placeholder="请输入其它金额" class="money"/>
								</div>
								<textarea placeholder="您可输入祝福语" rows="2" class="lytext" id="information"></textarea>
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
							<a href="/redPackets/getAgreement.do">《善园基金会用户协议》</a>
						</span>
					</div>
				</div>
			</div>
		</div>
		<!------按钮//---->
		<div class="btns">
			<div>
				<table style="width:100%;" cellspacing="0">
					<tbody>
						<tr>
							<td width="50%"><a href="javascript:;">
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
									<c:when test="${project.state == 230}">
										<div class="btn single" id="finishedbtn"><img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">未发布</div>
									</c:when>
									<c:otherwise>
										<div class="btn" id="paybtn">
											<img src="http://mat1.gtimg.com/gongyi/m/wx/btn_icon1.png">我要捐款
										</div>
									</c:otherwise>
								</c:choose>
							</a></td>
							<c:if test="${project.state == 240 && browser == 'wx'}">
								<c:if test="${project.isNeedVolunteer == 0 || project.isNeedVolunteer == null}">
									<td width="50%"><%-- <a href="http://www.17xs.org/project/view_share_h5.do?projectId=${project.id }&shareType=0" id="btn_submit2"> --%>
									<%-- <a href="http://www.17xs.org/project/togetherDonate_view.do?projectId=${project.id }" id="btn_submit2"> --%>
									<a href="http://www.17xs.org/together/together_view.do?projectId=${project.id }" id="btn_submit2">
											<div class="btn invitebtn">
												<img src="/res/images/h5/images/releaseProject/invite.png">邀朋友一起捐
											</div>
									</a></td>
								</c:if>
							</c:if>
							<c:if test="${project.state == 210 && browser == 'wx'}">
								<c:if test="${project.isNeedVolunteer == 0 || project.isNeedVolunteer == null}">
									<td width="50%"><%-- <a href="http://www.17xs.org/project/view_share_h5.do?projectId=${project.id }&shareType=0" id="btn_submit2"> --%>
									<a href="http://www.17xs.org/together/together_view.do?projectId=${project.id }" id="btn_submit2">
											<div class="btn invitebtn">
												<img src="/res/images/h5/images/releaseProject/invite.png">邀朋友一起捐
											</div>
									</a></td>
								</c:if>
							</c:if>
							<c:if test="${project.isNeedVolunteer == 1}">
								<td width="50%"><a
									href="http://www.17xs.org/project/volunteer.do?projectId=${project.id}">
										<div class="btn invitebtn">
											<img src="/res/images/h5/images/releaseProject/invite.png">加入志愿者(${number})
										</div>
								</a></td>
							</c:if>
						</tr>
					</tbody>
				</table>
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
	<input type="hidden" id="goodLibrary" value="${goodLibrary}">
	<input type="hidden" id="pprim" value="${project.title}">
	<input type="hidden" id="browser" value="${browser }">
	<input type="hidden" id="userId" value="${userId}">
	<input type="hidden" id="projectId" value="${project.id}">
	<input type="hidden" id="extensionPeople" value="${extensionPeople}">
	<input type="hidden" name="slogans" id="slogans" value="${slogans}" />
	<input type="hidden" id="needmoney" value="<fmt:formatNumber type="number" pattern="###0.00#" value = "${project.cryMoney-project.donatAmount}"/>">
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script>
		$(function() {
			$("#releasePerson").click(function() {
				releasePersonDetails();
			});
			$("#appealPerson").click(function() {
				appealPersonDetails();
			});
			
			$("#institutionReleasePerson").click(function() {
				var projectId = $("#projectId").val();
				location.href = window.baseurl
					+ "uCenterProject/releaseAndAppealDetails.do?projectId="
					+ projectId + "&personType=2"+"&type=1";
			});
			//原来项目当发起人为机构时的信息
			$("#PCinstitutionReleasePerson").click(function() {
				var projectId = $("#projectId").val();
				location.href = "/uCenterProject/gotoinstitutioned.do?projectId=" + projectId + "&personType=2"+"&type=1";
			});

			function releasePersonDetails(){
				var projectId = $("#projectId").val();
				location.href = "/uCenterProject/releaseAndAppealDetails.do?projectId=" + projectId + "&personType=2"+"&type=0";
			}
			function appealPersonDetails() {
				var projectId = $("#projectId").val();
				location.href = "/uCenterProject/releaseAndAppealDetails.do?projectId=" + projectId + "&personType=0";
			}
			<%--点击查看全文--%>
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
	</script>
	<script type="text/javascript">
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
				link : 'http://www.17xs.org/uCenterProject/project_details.do?projectId=${project.id}',
				imgUrl : '${project.coverImageUrl}', // 分享图标
				type : 'link', // 分享类型,music、video或link，不填默认为link
				dataUrl : '', // 如果type是music或video，则要提供数据链接，默认为空
				success : function() {
					//用户确认分享后执行的回调函数
					var projectId=$('#projectId').val();
					$.ajax({
						url : "http://www.17xs.org/uCenterProject/saveRedisShareCount.do",
						data : {projectId : projectId},
						success : function(result) {
							if (result.flag == 1) {//成功
								alter("分享成功！");
							}
							else{
								alter("分享失败！");
							} 
						}
					});
				},
				cancel : function() {
					//用户取消分享后执行的回调函数
				}
			});

			wx.onMenuShareTimeline({
				title : '${project.title}', // 分享标题
				link : 'http://www.17xs.org/uCenterProject/project_details.do?projectId=${project.id}',
				imgUrl : '${project.coverImageUrl}', // 分享图标
				success : function() {
					// 用户确认分享后执行的回调函数
					var projectId=$('#projectId').val();
					$.ajax({
						url : "http://www.17xs.org/uCenterProject/saveRedisShareCount.do",
						data : {projectId : projectId},
						success : function(result) {
							if (result.flag == 1) {//成功
								alter("分享成功！");
							}
							else{
								alter("分享失败！");
							} 
						}
					});
				},
				cancel : function() {
					// 用户取消分享后执行的回调函数
				}
			});
		});
		var loginUrl = "/ucenter/user/Login_H5.do?flag=audit_projectId_${project.id}_type_0_extensionPeople_${extensionPeople}";
	</script>
	<script src="/res/js/h5/donateWordFn.js?v=20171130"></script>
	<script data-main="/res/js/h5/releaseProject/userCenterProjectdetail.js?v=201712201400" src="/res/js/require.min.js"></script>
	<%@ include file="./../cs.jsp"%>
	<% CS cs = new CS(1257726653);
		cs.setHttpServlet(request, response);
		String imgurl = cs.trackPageView(); %>
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
				</a> <a href="javascript:;">
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
	<input id="type" type="hidden" value="0">
       
</body>
</html>