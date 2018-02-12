<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="cache-control" content="no-store" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="keywords" content="善园网、${shouzhu.realName }、${fabu.realName}、${fabu.workUnit}" />
<meta name="description" content="善园网、${project.subTitle }" />
<meta name="viewport" />
<title>善园网-${project.title}</title>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/headNew.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/goodDetail.css?v=201601211406" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/commNew.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/infoblocknew.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/layer.css" />
</head>
<body>

	<%@ include file="./../common/newhead.jsp"%>
	<div class="bodyer doGdDetail">
		<div class="w1000 breadcrumb">
			当前位置：<a href="#" title="" target="_blank">项目详情</a>
		</div>

		<div class="maintitle">
			<h1 class="title">${project.title}</h1>
			<p class="time">
				<fmt:formatDate value="${project.issueTime}" pattern="yyyy MM-dd" />
				发布 &nbsp;&nbsp;&nbsp;&nbsp;
				<c:if test="${project.tag != null && project.tag != '-1'}">
        标签：<c:forTokens items="${project.tag }" delims="," var="tag">
						<em class="time_bq">${tag }</em>
					</c:forTokens>
				</c:if>
			</p>
		</div>

		<div class="gDtil-bd">
			<div class="gDtil-bdL">
				<div class="gDtil-show">
					<div class="gDtil-show-img">
						<img src="${project.coverImageUrl}" alt="" />
					</div>

				</div>
				<div class="gDtil-tab yh">
					<div class="tab-hd" id="tab-hd">
						<span class="on">项目概况</span>
						<c:choose>
							<c:when test="${reports!=null}">
								<span>执行进度(<c:out value="${fn:length(reports)}"></c:out>)
								</span>
							</c:when>
							<c:otherwise>
								<span>执行进度(0)</span>
							</c:otherwise>
						</c:choose>
						<span>项目反馈(<em id="feedNum">0</em>)
						</span> <span>捐款记录</span>
					</div>
					<div class="tab-bd" id="tab-bd">
						<div class="bdcon survey" id="photosDemo">
							${project.content} <br />
							<!--优酷视频的连接  embed/XNTcyMzIwMTE2-->
							<!-- 							<div style="text-align: center"> -->
							<!-- 								<iframe  src="http://player.youku.com/embed/XNTcyMzIwMTE2" -->
							<!-- 									allowfullscreen="" frameborder="0" width="500" height="400"></iframe> -->
							<!-- 							</div> -->
							<!-- 腾讯视频的连接  vid=m0019dui6y5-->
							<!-- 							<div style="text-align: center"> -->
							<!-- 								<iframe class="video_iframe" style="z-index:1;" -->
							<!-- 									src="http://v.qq.com/iframe/player.html?vid=m0019dui6y5&amp;width=500&amp;height=375&amp;auto=0" -->
							<!-- 									allowfullscreen="" frameborder="0" width="500" height="400"></iframe> -->
							<!-- 							</div> -->
							<c:forEach items="${project.bfileList}" var="img">
								<p class="pic">
									<c:if test="${img.fileType !='video'}">
										<img src="${img.url}" width="590px" alt="">
										<c:if test="${img.description!='1'}">
											<i>${img.description}</i>
										</c:if>
									</c:if>
									<c:if test="${img.fileType =='video'}">
										<iframe src="${img.description}" allowfullscreen=""
											frameborder="0" width="100%" height="400"> </iframe>
									</c:if>
								</p>
							</c:forEach>
						</div>
						<div class="bdcon bdpgrass" id="bdpgrass" style="display:none">
							<%------------执行进度--------------%>
							<c:if test="${reports!=null}">
							<c:forEach items="${reports}" var="report">
								<div class="bdback-group">
									<div class="picimg"><img src="http://www.17xs.org/res/images/user/user-icon-gt.png"></div>
									<div class="name">${report.reportPeopleName}<span><fmt:formatDate value="${report.operatorTime}" pattern="yyyy MM-dd" /></span></div>
									<p id="564|1256">${report.content}
									<c:if test="${report.contentImageUrl!=null}">
											<div class="list-group">
												<c:forEach items="${report.contentImageUrl}" var="img">
													<img src="${img}" alt="" />
													<br />
												</c:forEach>
											</div>
										</c:if><br>
									</p>
								</div>
							</c:forEach>
							</c:if>
						</div>
          				<%---------项目反馈-----%>
						<div class="bdcon bdback" id="bdback" style="display:none">
							<div id="hoslist">
								<div class="bdback-group">
									<div class="name">
										赵铁柱<span>2014-04-29 23:46</span>
									</div>
									<p id="12">
										感谢大家的厚爱，我已经好很多了
										<!--<a class="more close">收起祝福</a>-->
										<a class="more open">查看祝福</a>
									</p>
									<div class="comList" id="comList12">
										<div class="comListOne" id="comListOne12"></div>
									</div>
								</div>
							</div>
							<div class="bdback-page">
								<div class="page">
									<a title="" class="prev" id="pageprev"></a> <input type="text"
										value="1" id="pagenum" /> <a title="" class="next"
										id="pagenext"></a>
								</div>
								<p id="pagetotle">共2页</p>
							</div>
							<c:if test="${owner!=null}">
								<div class="bdback-msg">
									发表您的留言
									<textarea id="msg-text"></textarea>

									<div class="msg-btn">
										<a title="" class="msgbtn msgpic" id="msgpic"><em></em>上传图片</a>
										<a title="" class="msgbtn msgsubmit" id="msgsubmit">发表</a>
										<form id="form1" action="http://www.17xs.org/file/upload3.do"
											method="post" enctype="multipart/form-data">
											<ul class="imgList" id="msg-pic">
												<li class="last">+<input type="file" name="file"
													hidefocus="true" id="picaddImg" class="file-input"><input
													type="hidden" name="type" id="type" value="1"></li>
											</ul>
										</form>

									</div>
									</form>
								</div>
							</c:if>
						</div>
						<div class="bdcon bdrecord" style="display:none">
							<ul class="list-hd">
								<li class="lst-col1">捐赠人</li>
								<li class="lst-col2">捐赠金额</li>
								<li class="lst-col3">捐赠时间</li>
							</ul>
							<div class="g-scroll" id="grecordList">
								<div class="scrList">
								</div>
							</div>
						</div>
					</div>

				</div>
				<c:if test="${goodhelps!=null}">
					<div class="support_corp">
						<div class="corp_hd">
							<i></i>以下企业已参加助善行动，选择任意企业，点击转发求助信息，求助者即可获得企业的助捐
						</div>
						<ul class="corp_bd">
							<c:forEach items="${goodhelps}" var="goodhelp">
								<li class="item">
									<ul>
										<li><img src="${goodhelp.logoUrl}" width="69" height="69"></li>
										<li class="line1">${goodhelp.companyName}</li>
										<li class="line2">总善款<label><fmt:formatNumber
													type="number" pattern="#,##0.00#"
													value="${goodhelp.freezAmount}" /></label>元
										</li>
										<li class="line3">未捐出<label><fmt:formatNumber
													type="number" pattern="#,##0.00#"
													value="${goodhelp.freezAmount-goodhelp.goodHelpAmount}" /></label>元
										</li>
										<li class="line4"><a class="forward_btn"
											data="${goodhelp.perMoney}|${goodhelp.goodPassWord}|${goodhelp.companyName}">立即转发</a></li>
									</ul>
								</li>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</div>
			<div class="gDtil-bdR">


				<div class="infoblock_new" style="display:black;">
					<div class="newtop">
						<div class="newtop_1">
							<c:choose>
								<c:when test="${project.state == 260}">
									<i class="iconsum1">已完成</i>
								</c:when>
								<c:otherwise>
									<i class="iconsum2">筹集中</i>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="newtop_2">
							<ul>
								<li><p class="wenzi">已 募 集</p>
									<strong>：</strong><span class="num1"><fmt:formatNumber
											type="number" pattern="#,##0.00#"
											value="${project.donatAmount}" /> 元</span></li>
								<li><p class="wenzi">支持人数</p>
									<strong>：</strong><span class="num2">${donate1.goodHelpCount }
										人/次</span></li>
								<li><p class="wenzi">目标金额</p>
									<strong>：</strong><span class="num3"><fmt:formatNumber
											type="number" pattern="#,##0.00#" value="${project.cryMoney}" />
										元</span><span class="num4">完成：<i>${processbfb}</i>%
								</span></li>
							</ul>
						</div>
						<span class="schedule-bar"> <i class="schedule-current"
							style="width:${process}%;"></i>
						</span>
					</div>
					<div class="newmiddle">
						<ul>
							<li><p class="wenzi">受 助 人</p>
								<strong>：</strong>${shouzhu.realName}</li>
							<li style="line-height: 20px;"><p class="wenzi"
									style="line-height:20px;height:20px;">项目地点</p>
								<strong>：</strong>${shouzhu.familyAddress}</li>
							<c:choose>
								<%--<c:when test="${process<100 && project.state == 240}">--%>
								<c:when test="${project.state == 240}">
									<li class="btn"><a class="btn_1" id="gDtile-btn">立即捐款</a></li>
								</c:when>
								<%--<c:when test="${project.state == 260 || process>=100}">--%>
								<c:when test="${project.state == 260}">
									<li class="btn"><span class="btn_2">募捐结束</span></li>
								</c:when>
								<c:otherwise>
									<li class="btn"><span class="btn_2" title="">未发布</span></li>
								</c:otherwise>
							</c:choose>
							<c:if test="${project.isNeedVolunteer == 1}">
								<li class="btn"><a class="btn_1"
									href="http://www.17xs.org/project/addVolunteerView.do?itemId=${project.id}">志愿者报名</a></li>
							</c:if>
							<c:if test="${project.isCanHelp == 1}">
								<li class="btn"><a class="btn_1"
									href="http://www.17xs.org/project/addCryPeopleView.do?itemId=${project.id}">项目求助</a></li>
							</c:if>
						</ul>
					</div>
					<div class="newfoot">
						<div id="qrcode" class="pr_ewm" style="display:none"></div>
						<input type="text" id="getval" style="display:none" />
						<ul>
							<li><p class="wenzi" style="height:20px;line-height:20px;">分享到</p>
								<strong>：</strong></li>
							<li class="btn_fx" style="margin-left:9px;"><a
								href="javascript:void(0);" class="wx" id="send"></a> <a
								href="javascript:void(0);" class="qb" id="send"></a> <a
								href="javascript:void(0);" class="kj" id="send"></a> <a
								href="javascript:void(0);" class="qq" id="send"></a></li>
							<li class="jubao"><a
								href="http://www.17xs.org/index/toAddService.do?projectId=${project.id}"
								" class="jubao1">举报</a></li>
						</ul>
					</div>
				</div>


				<c:if test="${matchDonate.size() > 0}">
					<div class="info_companybox">
						<div class="info_company1">
							<div class="company_hd">
								<i></i> 爱心企业
							</div>
						<c:forEach items="${matchDonate }" var="matchDonate" varStatus="status">
							<div class="company_bd" style="margin-bottom: 15px">
								<div class="company_head">
									<c:if
										test="${matchDonate.status == 501 ||matchDonate.status == 502}">
										<i class="peijuanzhong"></i>
									</c:if>
									<c:if test="${matchDonate.status == 503}">
										<i class="yijieshu"></i>
									</c:if>
									<div class="company_logo">
										<img width="70" height="70"
											src="${matchDonate.coverImageUrl }">
									</div>
									<div class="company_namebox">
										<p class="company_name">${matchDonate.companyname }</p>
									</div>
								</div>
								<div class="company_body">
									<ul>
										<li style="margin-bottom:0px;">已支持配捐<span class="cishu">${matchDonate.number }</span>次
										</li>
										<li>已配捐金额<span class="jine">${matchDonate.usedamount }</span>元
										</li>
										<li><p
												style="color:#bbbbbb;font-size:12px;line-height:20px;">注：配捐是指爱心企业根据爱心网友捐赠的金额，按一定比例进行捐赠；如您捐助1元，爱心企业同时捐助1元。</p></li>
									</ul>
								</div>
							</div>
							</c:forEach>
						</div>
					</div>
				</c:if>

				<c:choose>
					<c:when test="${fabu.helpType==1||fabu.helpType==2||fabu.helpType==3||fabu.helpType==4||fabu.helpType==5 }">
						<c:choose>
							<c:when test="${project.type=='enterpriseProject' }">
								<div class="info_bigbox2">
						<div class="info_block6">
							<div class="block_hd">
								<i></i>发起方/执行方
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${fabu.headImageUrl != null }">
												<img src="${fabu.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">

										<p class="line1">${fabu.workUnit}</p>
										<p class="line3">${fabu.familyAddress}</p>

									</div>
								</div>
							</div>
						</div>


						<div class="info_block5">
							<div class="block_hd">
								<i></i>项目联系人
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${zhengming.headImageUrl != null }">
												<img src="${zhengming.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">

										<p class="line1">${fabu.realName}<i>${fabu.relation}</i>
										</p>

									</div>
								</div>
								<div class="contact_info">
									<P>
										<c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 身份证：${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
									</P>

									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span>
									</p>
									<p>
										<span>QQ /邮箱：${fabu.qqOrWx}</span>
									</p>
								</div>
							</div>
						</div>
						<div class="info_block5">
								<div class="block_hd">
								<i></i>善款接收
								</div>
								<div class="block_bd">
								<p class="line11">宁波市善园公益基金会</p>
								</div>
								</div>
					</div>
							</c:when>
							<c:otherwise>
								<div class="info_bigbox1">
						<div class="info_block2">
							<div class="block_hd">
								<i></i>项目发起人
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${fabu.headImageUrl != null }">
												<img src="${fabu.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">
										<p class="line1">${fabu.realName}<%-- <i>${fabu.relation}</i> --%>
										</p>
									</div>
								</div>
								<div class="contact_info">
									<P>
										<c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 身份证：${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
									</P>
									<p class="line3">地&nbsp;&nbsp;&nbsp;址：${fabu.familyAddress}</p>
									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span>
									</p>
									<p>
										<span>QQ /邮箱：${fabu.qqOrWx}</span>
									</p>
								</div>
							</div>
						</div>
						<div class="info_block4">
							<div class="block_hd">
								<i></i>证明单位
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${zhengming.headImageUrl != null }">
												<img src="${zhengming.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">
										<p class="line1">${zhengming.workUnit}</p>
									</div>
								</div>
								<div class="contact_info">
									<p>联系人：${zhengming.realName}</p>
									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${zhengming.linkMobile}</span>
									</p>

								</div>
							</div>
						</div>
						<div class="info_block5">
								<div class="block_hd">
								<i></i>善款接收
								</div>
								<div class="block_bd">
								<p class="line11">宁波市善园公益基金会</p>
								</div>
								</div>
					</div>
							</c:otherwise>
						</c:choose>
						
					</c:when>
					<c:when test="${fabu.helpType==6||fabu.helpType==7||fabu.helpType==8 }">
						<div class="info_bigbox1">
						<div class="info_block2">
							<div class="block_hd">
								<i></i>项目发起人
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${fabu.headImageUrl != null }">
												<img src="${fabu.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">
										<p class="line1">${fabu.realName}<%-- <i>${fabu.relation}</i> --%>
										</p>
									</div>
								</div>
								<div class="contact_info">
									<P>
										<c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 身份证：${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
									</P>
									<p class="line3">地&nbsp;&nbsp;&nbsp;址：${fabu.familyAddress}</p>
									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span>
									</p>
									<p>
										<span>QQ /邮箱：${fabu.qqOrWx}</span>
									</p>
								</div>
							</div>
						</div>
						<div class="info_block4">
							<div class="block_hd">
								<i></i>证明单位
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${zhengming.headImageUrl != null }">
												<img src="${zhengming.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">
										<p class="line1">${zhengming.workUnit}</p>
									</div>
								</div>
								<div class="contact_info">
									<p>联系人：${zhengming.realName}</p>
									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${zhengming.linkMobile}</span>
									</p>

								</div>
							</div>
						</div>
						<div class="info_block5">
								<div class="block_hd">
								<i></i>善款接收
								</div>
								<div class="block_bd">
								<p class="line11">宁波市善园公益基金会</p>
								</div>
								</div>
					</div>
					</c:when>
					<c:when test="${fabu.helpType==9||fabu.helpType==10 }">
						<div class="info_bigbox2">
						<div class="info_block6">
							<div class="block_hd">
								<i></i>发起方/执行方
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${fabu.headImageUrl != null }">
												<img src="${fabu.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">

										<p class="line1">${fabu.workUnit}</p>
										<p class="line3">${fabu.familyAddress}</p>

									</div>
								</div>
							</div>
						</div>


						<div class="info_block5">
							<div class="block_hd">
								<i></i>项目联系人
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${zhengming.headImageUrl != null }">
												<img src="${zhengming.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">



										<p class="line1">${fabu.realName}<i>${fabu.relation}</i>
										</p>

									</div>
								</div>
								<div class="contact_info">
									<P>
										<c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 身份证：${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
									</P>

									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span>
									</p>
									<p>
										<span>QQ /邮箱：${fabu.qqOrWx}</span>
									</p>
								</div>
							</div>
						</div>
						<div class="info_block5">
								<div class="block_hd">
								<i></i>善款接收
								</div>
								<div class="block_bd">
								<p class="line11">宁波市善园公益基金会</p>
								</div>
								</div>
					</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${faqiren_type=='individualUsers' }">
								<div class="info_bigbox1">
						<div class="info_block2">
							<div class="block_hd">
								<i></i>项目发起人
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${fabu.headImageUrl != null }">
												<img src="${fabu.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">
										<p class="line1">${fabu.realName}<%-- <i>${fabu.relation}</i> --%>
										</p>
									</div>
								</div>
								<div class="contact_info">
									<P>
										<c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 身份证：${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
									</P>
									<p class="line3">地&nbsp;&nbsp;&nbsp;址：${fabu.familyAddress}</p>
									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span>
									</p>
									<p>
										<span>QQ /邮箱：${fabu.qqOrWx}</span>
									</p>
								</div>
							</div>
						</div>
						<div class="info_block4">
							<div class="block_hd">
								<i></i>证明单位
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${zhengming.headImageUrl != null }">
												<img src="${zhengming.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">
										<p class="line1">${zhengming.workUnit}</p>
									</div>
								</div>
								<div class="contact_info">
									<p>联系人：${zhengming.realName}</p>
									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${zhengming.linkMobile}</span>
									</p>

								</div>
							</div>
						</div>
						<div class="info_block5">
								<div class="block_hd">
								<i></i>善款接收
								</div>
								<div class="block_bd">
								<p class="line11">宁波市善园公益基金会</p>
								</div>
								</div>
					</div>
							</c:when>
							<c:otherwise>
								<div class="info_bigbox2">
						<div class="info_block6">
							<div class="block_hd">
								<i></i>发起方/执行方
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${fabu.headImageUrl != null }">
												<img src="${fabu.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">

										<p class="line1">${fabu.workUnit}</p>
										<p class="line3">${fabu.familyAddress}</p>

									</div>
								</div>
							</div>
						</div>


						<div class="info_block5">
							<div class="block_hd">
								<i></i>项目联系人
							</div>
							<div class="block_bd">
								<div class="basic_info">
									<div class="thumb">
										<c:choose>
											<c:when test="${zhengming.headImageUrl != null }">
												<img src="${zhengming.headImageUrl }" width="70" height="70">
											</c:when>
											<c:otherwise>
												<img
													src="../../../../res/images/detail/people_avatar.jpg"
													width="70" height="70">
											</c:otherwise>
										</c:choose>
									</div>
									<div class="info">



										<p class="line1">${fabu.realName}<i>${fabu.relation}</i>
										</p>

									</div>
								</div>
								<div class="contact_info">
									<P>
										<c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 身份证：${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
									</P>

									<p>
										<span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span>
									</p>
									<p>
										<span>QQ /邮箱：${fabu.qqOrWx}</span>
									</p>
								</div>
							</div>
						</div>
								<div class="info_block5">
								<div class="block_hd">
								<i></i>善款接收
								</div>
								<div class="block_bd">
								<p class="line11">宁波市善园公益基金会</p>
								</div>
								</div>
					</div>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>


				<c:if test="${donates!=null}">
					<div class="gDtil-item gDtil-logo yh">
						<div class="gDtil-title">
							<i></i>爱心人士
						</div>
						<div class="gDtil-Donation">
							<ul class="list-hd">
								<li class="lst-col1">捐赠人</li>
								<li class="lst-col2">捐赠金额</li>
								<li class="lst-col3">捐赠时间</li>
							</ul>
							<div class="g-scroll">
								<ul>
									<c:forEach var="donate" items="${donates}">
										<li><span class="lst-col1"> <c:choose>
													<c:when test="${fn:startsWith(donate.nickName,'游客')}">
									          爱心人士
									  </c:when>
													<c:when test="${fn:length(donate.nickName)<=8}">
										          ${donate.nickName}
									  </c:when>
													<c:otherwise>
										${fn:substring(donate.nickName, 0, 8)}...
									  </c:otherwise>
												</c:choose>
										</span> <span class="lst-col2">￥<fmt:formatNumber
													type="number" pattern="#,##0.00#"
													value="${donate.donatAmount}" /></span> <span class="lst-col3"><fmt:formatDate
													value="${donate.donatTime}" pattern="MM/dd HH:mm" /></span></li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</div>
				</c:if>
				<div class="gDtil-item yh">
					<div class="gDtil-title">
						<i></i>常见问题
					</div>
					<div class="gDtil-list">
						<p>
							<a href="../../../../help/questions.do?no=0">实名认证时需要注意什么？</a>
						</p>
						<p>
							<a href="../../../../help/questions.do?no=2">忘记密码怎么找回？</a>
						</p>
						<p>
							<a href="../../../../help/questions.do?no=3">怎么求助？</a>
						</p>
						<p>
							<a href="../../../../help/questions.do?no=5">捐款方式有哪些？</a>
						</p>
					</div>
				</div>
				<c:if test="${loveNews!=null}">
					<div class="gDtil-item yh">
						<div class="gDtil-title">
							<i></i>慈善资讯
						</div>
						<div class="gDtil-list">
							<c:forEach items="${loveNews}" var="news">
								<p data="${news.id}">
									<c:if test="${fn:length(news.title)>16}">
										<a href="<%=web_url%>news/center.do?id=${news.id}">
											${fn:substring(news.title, 0, 16)}...</a>
									</c:if>
									<c:if test="${fn:length(news.title)<=16}">
										<a href="<%=web_url%>news/center.do?id=${news.id}">
											${news.title}</a>
									</c:if>
									<span><fmt:formatDate value="${news.createtime}"
											pattern="yyyy-MM" /></span>
								</p>
							</c:forEach>
						</div>
					</div>
				</c:if>
			</div>
		</div>
	</div>
	
	<%@ include file="./../common/newfooter.jsp"%>
	<input type="hidden" id="extensionPeople" value="${extensionPeople }">
	<input type="hidden" id="pprim" data="">
	<input id="itemType" type="hidden" value="${itemType}">
	<input id="projectId" type="hidden" value="${project.id}">
	<input id="visitor" type="hidden" value="1">
	<input id="pName" type="hidden" value="${project.title}">
	<c:choose>
		<c:when test="${project.isOverMoney == 1 && project.overMoney > 0}">
			<input id="amount" type="hidden" value="<fmt:formatNumber value="${(project.overMoney-project.donatAmount)<0?0.00:(project.overMoney-project.donatAmount)}" type="NUMBER" pattern="#0.00"/>">
		</c:when>
		<c:otherwise>
			<input id="amount" type="hidden" value="<fmt:formatNumber value="${(project.cryMoney-project.donatAmount)<0?0.00:(project.cryMoney-project.donatAmount)}" type="NUMBER" pattern="#0.00"/>">
		</c:otherwise>
	</c:choose>

	<script type="text/javascript" src="../../../../res/js/jquery-1.8.2.min.js"></script>
	<script type="text/javascript" src="../../../../res/js/layer.js"></script>
	<script data-main="../../../../res/js/dev/goodDetail.js" src="../../../../res/js/require.min.js"></script>
	<script type="text/javascript" src="../../../../res/js/qrcode.js"></script>
	<script>
		window.onload = function() {
			var qrcode = new QRCode(document.getElementById("qrcode"), {
				width : 96,//设置宽高
				height : 96
			});
			var url = 'http://www.17xs.org/project/view_h5_'+$("#projectId").val()+'/';
			qrcode.makeCode(url);
			document.getElementById("send").onclick = function() {
				$("#qrcode").show();
		};
			$("#send").mouseout(function() {
				$("#qrcode").hide();
			});
		};
	</script>
	<script>
		layer.ready(function() {

			layer.photos({
				photos : '#photosDemo',
				shift : 5,
				closeBtn : 1

			});

			layer.photos({
				photos : '#bdpgrass',
				shift : 5,
				closeBtn : 1
			});
		});
	</script>
	<script type="text/javascript">
		function donateReply(leavewordUserId, donateId,index){//alert(donateId);
			var projectId = $("#projectId").val();
			$("#index").val(index);
			$("#projectDonateId").val(donateId);
			$("#projectFeedbackId").val("");
			$.ajax({
					url : "http://www.17xs.org/h5ProjectDetails/gotoAlterReplyPC.do",
					data : {
						type : 1,
						projectId : projectId,
						projectDonateId : donateId,
						leavewordUserId : leavewordUserId
					},
					success : function(result) {
						if (result.flag == "101") {//成功
							$("#leavewordName").val(result.obj.leavewordName);
							if($('#answer_box'+donateId).css("display")=='none'){
								$('#answer_box'+donateId).show();
							}
							else{
								$('#answer_box'+donateId).hide();
							}
							//$(".end_text").focus();
						} else if (result.flag == "102") {
							$("#leavewordName").val(result.obj.leavewordName);
							$("#replyUserId").val(result.obj.replyUserId);
							$("#replyName").val(result.obj.replyName);
							$("#leavewordUserId").val(result.obj.leavewordUserId);
							if($('#answer_box'+donateId).css("display")=='none'){
								$('#answer_box'+donateId).show();
							}
							else{
								$('#answer_box'+donateId).hide();
							}
							//$(".end_text").focus();
						} else if (result.flag == "201") {
							$("#leavewordName").val(result.obj.leavewordName);
							if($('#answer_box'+donateId).css("display")=='none'){
								$('#answer_box'+donateId).show();
							}
							else{
								$('#answer_box'+donateId).hide();
							}
							//$(".end_text").focus();
						} else if (result.flag == "202") {
							$("#leavewordName").val(result.obj.leavewordName);
							$("#replyUserId").val(result.obj.replyUserId);
							$("#replyName").val(result.obj.replyName);
							$("#leavewordUserId").val(result.obj.leavewordUserId);
							if($('#answer_box'+donateId).css("display")=='none'){
								$('#answer_box'+donateId).show();
							}
							else{
								$('#answer_box'+donateId).hide();
							}
							//$(".end_text").focus();
						} else if (result.errorCode == "0001") {//未登录
							$("#msg").html('<p>您还未登录！</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							//window.location = 'http://www.17xs.org/ucenter/user/Login_H5.do';
						}
						else if(result.flag==-2){
							window.location =result.errorMsg;
						}
						 else {//失败
							$("#msg").html('<p>' + result.errorMsg + '</p>');
							$("#msg").show();
							setTimeout(function() {
								$("#msg").hide();
							}, 2000);
							return;
						}
					}
				});
		}
		function donateLoadMore(i, projectDonateId,surplusTotal) {
		var currentPage = $("#currentPageForFeedBack").val();
		
		var projectId = $("#projectId").val();
		//var leaveWords = $('.donateLoadMore' + i).html();
		var html = [];
		$.ajax({
				url : "http://www.17xs.org/h5ProjectDetails/loadMoreLeaveWord.do",
				data : {
					type : 0,
					projectId : projectId,
					projectDonateId : projectDonateId,
					currentPage : currentPage,
					surplusTotal:surplusTotal
				},
				success : function(result) {
					if (result.flag == "1") {
						//成功
						itemss = result.obj;
						for (var j = itemss.length - 1; j >= 0; j--) {
							if (itemss[j].replyUserId == null) {
								html.push('<p class="board_textj" ><span style="color:#019045" onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+i+')" >'+itemss[j].leavewordName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
								} else {
									html.push('<p class="board_textj" ><span style="color:#019045"  onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+i+')" >'+itemss[j].leavewordName+'<b class="hf" style="color:#999">回复</b>'+itemss[j].replyName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
								}
							}
							html.push('<div class="answer_box" style="width: 589px;display: none" id="answer_box'+itemss[0].projectDonateId+'">');
							html.push('<textarea class="area"></textarea>');
							html.push('<p><a class="answerBtn leaveWord">马上评论</a></p>');
							html.push('</div>');
							html.push('<div style="clear: both"></div>');
							//追加数据
							$('.message_listj'+i).html(html.join(''));
							//判断是否显示加载更多
							$('.danoteMore' + i).hide();
						} else {//失败
							alert(result.errorMsg);
							return;
						}
					}
				});
	}
	function zhankai(donateId){
		var text = $(".message_a"+donateId).text();
		if(text=='展开'){
			$(".messkuang"+donateId).removeClass("messkuang");
			$(".message_a"+donateId).html("收起");
		}
		else{
			$(".messkuang"+donateId).addClass("messkuang");
			$(".message_a"+donateId).html("展开");
		}
		
	}
	</script>
	<script>
		(function(){
		    var bp = document.createElement('script');
		    var curProtocol = window.location.protocol.split(':')[0];
		    if (curProtocol === 'https') {
		        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        
		    }
		    else {
		        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
		    }
		    var s = document.getElementsByTagName("script")[0];
		    s.parentNode.insertBefore(bp, s);
		})();
	</script>
	<input type="hidden" id="index" />
	<input type="hidden" id="projectDonateId" />
	<input type="hidden" id="projectFeedbackId" />
	<input type="hidden" id="replyUserId" />
	<input type="hidden" id="replyName" />
	<input type="hidden" id="leavewordUserId" />
	<input type="hidden" id="leavewordName" />
	<input type="hidden" id="currentPageForFeedBack" value="0" />
	<input type="hidden" id="special_fund_id" value="${project.special_fund_id }"/>
</body>
</html>