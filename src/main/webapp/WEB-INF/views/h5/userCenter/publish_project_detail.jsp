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
<link rel="stylesheet" type="text/css" href="/res/css/h5/basic.css?v=<%=base_version%>" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail.css?v=<%=detail_version%>" />
<link rel="stylesheet" type="text/css" href="/res/css/h5/releaseProject/detail_new.css?v=<%=detail_version%>">
<script type="text/javascript">
	document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
</script>
<style type="text/css">
	.load-more {
		text-align: center;width: 100%;height: 30px;line-height: 30px;font-size: 13px;color: #FF6810;
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
				<div class="pd-head">
					<div class="hd-left">
						<div class="left-pic">
							<img src="${headImgUrl==null?'/res/images/detail/people_avatar.jpg':headImgUrl }" />
						</div>
						<div class="left-info">
							<h3>${nickName }</h3>
							<p>发起筹款</p>
						</div>
					</div>
					<div class="hd-right">
						<p class="right-p">
							<a href="javascript:;" id="personal">个人求助<img src="/res/images/h5/images/donateStep/wen.png"/></a>
						</p>
						<p>剩余${oddDayNum }天</p>
					</div>
				</div>
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
							<div class="last_btn">查看全文</div>
						</div>
					</div>
					<c:if test="${submitState==0 || submitState==1}">
						<!--自己看到-->
						<div class="certificateMaterial">
							<div class="record_list">信息审核</div>
							<div class="redBorder"></div>
	
							<div class="approve">
								<div class="wrap">
									<c:if test="${submitState==1 }">
										<div class="approved">
											<div class="item">
												<h3>受助人：${userInfo.realName }</h3>
												<p><c:choose>
													<c:when test="${userInfo.auditState==203 }">
														<i class="icon gou"></i>身份信息已核实
													</c:when>
													<c:otherwise>
														<i class="icon tan"></i>审核中
													</c:otherwise>
												</c:choose></p>
											</div>
											<div class="item">
												<h3>所患疾病：${prove.diseaseName }</h3>
												<p><c:choose>
													<c:when test="${prove.state==203 }">
														<i class="icon gou"></i>疾病情况属实，目前在${recommendedPerson==null?prove.hospitalBedNumber:hospital }就诊。
													</c:when>
													<c:otherwise>
														<i class="icon tan"></i>审核中
													</c:otherwise>
												</c:choose></p>
											</div>
										</div>
									</c:if>
									<c:if test="${submitState==0 }">
										<div class="noApprove">
											<img src="/res/images/h5/images/material2.png" />
										</div>
										<p class="tipinfo">提交证明材料可以
											<a href="http://www.17xs.org/webhtml/view/h5/releaseProject/perfectData.html?type=0&projectId=${project.id }&recommendedPerson=${project.recommendedPerson}">获得更多的帮助机会</a>
										</p>
										<div class="sbtn">
											<a href="http://www.17xs.org/webhtml/view/h5/releaseProject/perfectData.html?type=0&projectId=${project.id }&recommendedPerson=${project.recommendedPerson}">提交验证材料</a>
										</div>
									</c:if>
									<c:if test="${prove!=null && ((prove.houseHoldIncome!=null && prove.houseHoldIncome!=''||
						    			prove.houseDetail!=null && prove.houseDetail!='')||(prove.carDetail!=null && prove.carDetail!=''
						    			||(prove.medicalInsurance!=null && prove.medicalInsurance!='')
						    			||(prove.houseDetailImageId!=null && prove.houseDetailImageId!=''))) }">
										<div class="record_list inner">补充资料</div>
										<div class="bd-bot"></div>
	
										<div class="supplementInfo">
											<p>房产：${(prove.houseDetail!=null && prove.houseDetail!='')?prove.houseDetail:'未填写'}</p>
											<p>车辆：${(prove.carDetail!=null && prove.carDetail!='')?prove.carDetail:'未填写'}</p>
											<p>保险：${(prove.medicalInsurance!=null && prove.medicalInsurance!='')?prove.medicalInsurance:'未填写'}</p>
											<p>贫困证明：${(prove.houseDetailImageId!=null && prove.houseDetailImageId!='')?'已提交':'未提交'}</p>
										</div>
									</c:if>
	
									<div class="record_list inner">其他资料</div>
									<div class="bd-bot"></div>
									<div class="detail_reselse inner">
										<div class="det_top">
											<div class="detTop_fl">
												<p>善款监管：宁波市善园公益基金会</p>
											</div>
										</div>
										<p class="intro">具有公开募捐资质的基金会，0手续费</p>
										<div class="det_top">
											<div class="detTop_fl">
												<p>收款人：${hospital }</p>
											</div>
										</div>
										<p class="intro">所有善款用于受助人的救治</p>
									</div>
	
									<div class="warntip">
										<p class="p1"><i class="icon"></i></p>
										<p class="p2">该项目信息不属于慈善公开募捐信息，真实性由发布个人负责，善园提示您了解项目后再帮助TA。</p>
									</div>
								</div>
								<div class="line"></div>
								<c:choose>
									<c:when test="${auditProjectCount>0 }">
										<p class="small big">有<span class="approveNum">${auditProjectCount }</span>人实名为我证实
											<a href="http://www.17xs.org/uCenterProject/lookConfirmPeople.do?projectId=${project.id }&type=1" class="moreApprove"></a>
										</p>
										<div class="approveList">
											<ul>
												<c:forEach items="${auditProjectList }" var="ap">
													<a href="javascript:void(0);">
														<li><img src="${ap.headUrl }" data-id="${ap.id}" /></li>
													</a>
												</c:forEach>
											</ul>
										</div>
										<div class="approveWord" id="approveWord" style="display: block">
											<span class="triangle"></span>
											<p>${auditProject.information }</p>
										</div>
										<div class="sbtn">
											<a href="javascript:void(0);" id="inviteFriends">邀请好友为我证实</a>
										</div>
									</c:when>
									<c:otherwise>
										<p class="small">有<span class="approveNum">0</span>人实名为我证实</p>
										<div class="noApprove">
											<img src="/res/images/h5/images/invite.png" />
										</div>
										<p class="tipinfo">邀请好友帮忙证实，您将获得更多的信任与支持</p>
										<div class="sbtn">
											<a href="javascript:void(0);" id="inviteFriends">邀请好友为我证实</a>
										</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</c:if>
	
					<c:if test="${submitState==2 }">
						<!--其他人或未登录看到-->
						<div class="certificateMaterial">
							<div class="record_list">信息审核
								<a href="http://www.17xs.org/user/userAdvice.do">我要举报</a>
							</div>
							<div class="redBorder"></div>
							<div class="approve">
								<div class="wrap">
									<c:choose>
										<c:when test="${userInfo==null || userInfo.getIndetity()==null || 
												userInfo.indetity=='' || prove==null || 
												prove.diseaseName==null || prove.diseaseName=='' }">
											<div class="onready">
												<p>验证资料准备中......</p>
											</div>
										</c:when>
										<c:otherwise>
											<div class="approved">
												<div class="item">
													<h3>受助人：${userInfo.realName }</h3>
													<p><c:choose>
														<c:when test="${userInfo.auditState==203 }">
															<i class="icon gou"></i>身份信息已核实
														</c:when>
														<c:otherwise>
															<i class="icon tan"></i>审核中
														</c:otherwise>
													</c:choose></p>
												</div>
												<div class="item">
													<h3>所患疾病：${prove.diseaseName }</h3>
													<p><c:choose>
														<c:when test="${prove.state==203 }">
															<i class="icon gou"></i>疾病情况属实，目前在${hospital }就诊。
														</c:when>
														<c:otherwise>
															<i class="icon tan"></i>审核中
														</c:otherwise>
													</c:choose></p>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
	
									<c:if test="${prove!=null && ((prove.houseHoldIncome!=null && prove.houseHoldIncome!=''||
						    			prove.houseDetail!=null && prove.houseDetail!='')||(prove.carDetail!=null && prove.carDetail!=''
						    			||(prove.medicalInsurance!=null && prove.medicalInsurance!='')
						    			||(prove.houseDetailImageId!=null && prove.houseDetailImageId!=''))) }">
										<div class="record_list inner">补充资料</div>
										<div class="bd-bot"></div>
	
										<div class="supplementInfo">
											<p>房产：${(prove.houseDetail!=null && prove.houseDetail!='')?prove.houseDetail:'未填写'}</p>
											<p>车辆：${(prove.carDetail!=null && prove.carDetail!='')?prove.carDetail:'未填写'}</p>
											<p>保险：${(prove.medicalInsurance!=null && prove.medicalInsurance!='')?prove.medicalInsurance:'未填写'}</p>
											<p>贫困证明：${(prove.houseDetailImageId!=null && prove.houseDetailImageId!='')?'已提交':'未提交'}</p>
										</div>
									</c:if>
	
									<div class="record_list inner">其他资料</div>
									<div class="bd-bot"></div>
									<div class="detail_reselse inner">
										<div class="det_top">
											<div class="detTop_fl">
												<p><a href="/resposibilityReport/resposibilityReport.do?id=368">善款监管：宁波市善园公益基金会（公募）<img src="/res/images/h5/images/donateStep/wen.png" /></a></p>
											</div>
										</div>
										<p class="intro">具有公开募捐资质的基金会，0手续费</p>
										<div class="det_top">
											<div class="detTop_fl">
												<p>收款人：${hospital }</p>
											</div>
										</div>
										<p class="intro">所有善款用于受助人的救治</p>
									</div>
	
									<div class="warntip">
										<p class="p1"><i class="icon"></i></p>
										<p class="p2">该项目信息不属于慈善公开募捐信息，真实性由发布人负责，善园提示您证实项目后再帮助TA。</p>
									</div>
								</div>
	
								<div class="line"></div>
								<c:choose>
									<c:when test="${auditProjectCount>0 }">
										<p class="small big">有<span class="approveNum">${auditProjectCount }</span>人实名为TA证实
											<a href="http://www.17xs.org/uCenterProject/lookConfirmPeople.do?projectId=${project.id }&type=1" class="moreApprove"></a>
										</p>
										<div class="approveList">
											<ul>
												<c:forEach items="${auditProjectList }" var="ap">
													<a href="javascript:void(0);">
														<li><img src="${ap.headUrl }" data-id="${ap.id}" /></li>
													</a>
												</c:forEach>
											</ul>
										</div>
										<div class="approveWord" id="approveWord" style="display: block">
											<span class="triangle"></span>
											<p>${auditProject.realName }：${auditProject.information }</p>
										</div>
										<c:choose>
											<c:when test="${auState==0}">
												<div class="sbtn">
													<a href="javascript:void(0);" class="approve_fr">我为TA证实</a>
												</div>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${userId==null}">
														<div class="sbtn">
															<a href="javascript:void(0);" class="approve_fr">我为TA证实</a>
														</div>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${browser == 'wx'}">
																<div class="sbtn">
																	<a href="javascript:void(0);" id="inviteFriends2">邀请好友为TA证实</a>
																</div>		
															</c:when>
															<c:otherwise>
																<div class="sbtn">
																	<a href="javascript:void(0);">您已经证实过了</a>
																</div>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<p class="small">有<span class="approveNum">0</span>人实名为TA证实</p>
										<div class="noApprove">
											<img src="/res/images/h5/images/material.png" />
										</div>
										<c:if test="${auState==0 }">
											<div class="sbtn">
												<a href="javascript:void(0);" class="approve_fr">我为TA证实</a>
											</div>
										</c:if>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
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
					<div class="ppd-money" id="selectMoney">
						<ul class="ppd-ul clearFix">
							<c:choose>
								<c:when test="${!empty moneyConfigs}">
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
								</c:when>
								<c:otherwise>
									<li class="on"><span t="10">10</span>元</li>
									<li><span t="20">20</span>元</li>
									<li><span t="50">50</span>元</li>
									<li><span t="100">100</span>元</li>
									<li><span t="500">500</span>元</li>
								</c:otherwise>
							</c:choose>
							<li class="user-defined">自定义</li>
						</ul>
						<div class="diyMoney">
							<input type="text" name="dMoney" id="dMoney" value="" />
						</div>
					</div>

					<div class="ppd-otherinfo">
						<p>其他信息(非必填)</p>
						<input type="text" id="realName" value="${user.realName }" placeholder="您的姓名" />
						<input type="text" id="mobileNum" value="${user.mobileNum }" placeholder="您的电话" />
						<input type="text" id="donateWords" placeholder="${leaveWord }" />
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
		</div>
		<!------按钮//---->
		<div class="btns">
			<div>
				<table style="width:100%;" cellspacing="0">
					<tbody>
						<c:choose>
							<c:when test="${owner == false || empty owner}">
								<tr>
									<c:if test="${browser == 'wx' && (project.state == 240 || project.state == 210)}">
										<td width="50%">
											<a href="http://www.17xs.org/together/together_view.do?projectId=${project.id }" id="btn_submit2">
												<div class="btn invitebtn flex fcol fcen spc">
													<img src="/res/images/h5/images/releaseProject/invite.png">邀朋友一起捐
												</div>
											</a>
										</td>
									</c:if>
									<td width="50%">
										<a href="javascript:;">
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
										</a>
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
										<a href="http://www.17xs.org/uCenterProject/projectFeedBack.do?projectId=${project.id}&state=${project.state}">
											<div class="btn invitebtn">
												<p><img src="/res/images/h5/images/releaseProject/fankui.png"></p><p>项目反馈</p>
											</div>
										</a>
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="inviteDialog">
		<span>点击右上角分享给朋友或朋友圈吧</span>
		<img src="/res/images/h5/images/point.gif" />
	</div>
	
	<div class="dialog personalDialog" id="personalDialog">
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
				<a href="/webhtml/view/h5/releaseProject/index.html" class="closeDialog">我也要筹款</a>
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
	<input type="hidden" name="" id="owner" value="${owner}" />
	<input type="hidden" id="goodLibrary" value="${goodLibrary}">
	<input type="hidden" id="pprim" value="${project.title}">
	<input type="hidden" id="browser" value="${browser }">
	<input type="hidden" id="userId" value="${userId}">
	<input type="hidden" id="projectId" value="${project.id}">
	<input type="hidden" id="extensionPeople" value="${extensionPeople}">
	<input type="hidden" name="slogans" id="slogans" value="${slogans}" />
	<input type="hidden" id="needmoney" value="<fmt:formatNumber type="number" pattern="###0.00#" value="${(project.cryMoney-project.donatAmount)<0?0.00:(project.cryMoney-project.donatAmount)} "/>">
	<input type="hidden" name="information" id="information" value="${leaveWord}" />
	
	<script type="text/javascript" src="/res/js/jquery-1.8.2.min.js"></script>
	<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script>
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
				link: 'http://www.17xs.org/newReleaseProject/project_detail.do?projectId=${project.id}',
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
				link: 'http://www.17xs.org/newReleaseProject/project_detail.do?projectId=${project.id}',
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
		var loginUrl = "/ucenter/user/Login_H5.do?flag=pub_1_projectId_${project.id}_extensionPeople_${extensionPeople}";
	</script>
	<script src="/res/js/h5/donateWordFn.js?v=20171130"></script>
	<script data-main="/res/js/h5/releaseProject/userCenterProjectdetail.js?v=201801241450" src="/res/js/require.min.js"></script>
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
	<input type="hidden" name="gotoType" id="gotoType" value="${gotoType}" />
</body>
</html>