<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>${project.title}</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodDetail.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer doGdDetail"> 
    <div class="gDtil-hd yh"><h2 class="w1000">&nbsp;我要行善 </h2></div>
    <div class="gDtil-bd">
        <div class="gDtil-bdL">
            <div class="gDtil-show">
            	<div class="gDtil-show-img">
            	<img src="${project.coverImageUrl}" alt="" />
                </div>
                <div class="gDtil-prgress">
                	<div class="gDtil-bar" style="width:${process}%"></div>
                    <span class="gDtil-text" style="left:${process}%"><i></i><fmt:formatNumber value="${processbfb/100}" type="percent" pattern="#0%"/></span>
                </div>
                <h2 class="yh">${project.title}</h2>
                <p  class="yh">
                    <fmt:formatDate  value="${project.registrTime}" pattern="yyyy MM-dd"/>发布&nbsp;
                    ${project.fieldChinese}
				    &nbsp;|&nbsp;${fn:substringBefore(project.location, "#")}
                </p>
				 <div class="line3 yh">
				 ${project.subTitle}
				 </div>
            </div>
            <div class="gDtil-tab yh">
            	<div class="tab-hd" id="tab-hd">
                	<span class="on">项目概况</span>
                	<c:choose>
					    <c:when test="${reports!=null}">
					      <span>执行进度(<c:out value="${fn:length(reports)}"></c:out>)</span>
					    </c:when>
					    <c:otherwise>
					      <span>执行进度(0)</span>
					    </c:otherwise>   
					  </c:choose>
                    <span>受助人反馈(<em id="feedNum">0</em>)</span>
					<span>捐款记录</span>
                </div>
                <div class="tab-bd" id="tab-bd">
                	<div class="bdcon survey"  >
                    	${project.content}
                      <br/>
                    	 <c:forEach items="${project.bfileList}" var="img">
					        <p class="pic"><img src="${img.url}" width="500px"  alt=""><c:if test="${img.description!='1'}"><i>${img.description}</i></c:if></p>
					    </c:forEach>
                    </div>
                    <div class="bdcon bdpgrass" id="bdpgrass" style="display:none"> 
                    	<h2>详细报告</h2>
                        <div class="grass-title">
                        	<span></span>
                        </div>
                        <ul class="list-data-hd">
                            <li class="wlst-col1">时间</li>
                            <li class="wlst-col2">事件</li>
                            <li class="wlst-col3">详细描述</li>
                           <!--  <li class="wlst-col4">本次花费（元）</li> -->
                            <li class="wlst-col5 last">提交人</li>
                         </ul>
                         <div class="list-data-bd">
                         	 <c:if test="${reports!=null}">
	                         	 <c:forEach items="${reports}" var="report">
							          <ul>
		                                <li class="wlst-col1"><fmt:formatDate  value="${report.operatorTime}" pattern="yyyy MM-dd"/></li>
		                                  <c:choose>
											<c:when test="${report.type==1}">
											   <li class="wlst-col2">调查报告</li>
											</c:when>
											<c:when test="${report.type==2}">
											   <li class="wlst-col2">打款报告</li>
											</c:when>
											<c:when test="${report.type==3}">
											   <li class="wlst-col2">考察报告 </li>
											</c:when>
											<c:when test="${report.type==4}">
											   <li class="wlst-col2">执行报告</li>
											</c:when>
											<c:when test="${report.type==5}">
											   <li class="wlst-col2">关闭报告</li>
											</c:when>
											<c:when test="${report.type==6}">
											   <li class="wlst-col2">执行进度</li>
											</c:when>
											<c:otherwise>
											  ${report.type}
											</c:otherwise>
										   </c:choose>
		                                <li class="wlst-col3">${report.content}</li>
		                               <!-- <li class="wlst-col4 wlst-onet">0</li> --> 
		                                <li class="wlst-col5 wlst-onet last">
		                                      ${report.reportPeopleName}
		                                </li>
		                            </ul>
		                            <c:if test="${report.contentImageUrl!=null}">
		                              <div class="list-group">
		                                 <c:forEach items="${report.contentImageUrl}" var="img">
										        	<img src="${img}" alt="" /><br/>
										  </c:forEach>
		                              </div>
		                            </c:if>
							     </c:forEach>
                         	 </c:if>
                         </div>
                    </div>
                    <div class="bdcon bdback" id="bdback" style="display:none">
                    	<div id="hoslist">
                            <div class="bdback-group">
                                <div class="name">赵铁柱<span>2014-04-29 23:46</span></div>
                                <p id="12">感谢大家的厚爱，我已经好很多了<!--<a class="more close">收起祝福</a>--> <a class="more open">查看祝福</a></p>
                                <div class="comList" id="comList12">
                                	<div class="comListOne" id="comListOne12">
                                    	
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="bdback-page">
                        	<div class="page">
                            	<a title="" class="prev" id="pageprev"></a>
                                <input type="text" value="1" id="pagenum"/>
                                <a title="" class="next" id="pagenext"></a>
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
                                <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">	
                                <ul class="imgList" id="msg-pic">
                                    <li class="last">+<input type="file" name="file" hidefocus="true" id="picaddImg" class="file-input"><input type="hidden" name="type" id="type" value="1"></li>
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
                            <ul>
                               
                            </ul>
                        </div>
                    </div>
                </div>
				
            </div>
            <c:if test="${goodhelps!=null}">
               <div class="support_corp">
                	<div class="corp_hd"><i></i>以下企业已参加助善行动，选择任意企业，点击转发求助信息，求助者即可获得企业的助捐</div>
                  <ul class="corp_bd">
                   	  <c:forEach items="${goodhelps}" var="goodhelp">
	                   	  <li class="item">
	                       	  <ul>
	                           	  <li><img src="${goodhelp.logoUrl}" width="69" height="69"></li>
	                              <li class="line1">${goodhelp.companyName}</li>
	                              <li class="line2">总善款<label><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${goodhelp.freezAmount}"/></label>元</li>
	                              <li class="line3">未捐出<label><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${goodhelp.freezAmount-goodhelp.goodHelpAmount}"/></label>元</li>
	                              <li class="line4"><a class="forward_btn" data="${goodhelp.perMoney}|${goodhelp.goodPassWord}|${goodhelp.companyName}">立即转发</a></li>
	                          </ul>
	                      </li>
                      </c:forEach>
                  </ul>
               </div>
            </c:if>
        </div>
        <div class="gDtil-bdR">
            <div class="info_block1">
                <div class="block_hd"><i></i>受助人:${shouzhu.realName}</div>
                <div class="block_bd">
                    <ul>
                        <li>地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：${shouzhu.familyAddress}</li>
                        <li>求助原因：${project.cryCause}</li>
                        <c:choose>
								<c:when test="${project.state == 260}">
								   <li class="line1"><span class="label">已筹金额</span><span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/>元</span></li>
								</c:when>
								<c:otherwise >
								   <li class="line1"><span class="label">已筹金额</span><span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/>元</span></li>
								</c:otherwise>
						</c:choose>
                        <li class="line2"><p class="progress">捐款进度<i><b style="width:${process}%"></b></i><label>${processbfb}%</label></p><p>${peopleNum}位爱心人士共捐款<fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.donatAmount}"/>元</p></li>
                        <li class="line3">需要善款：<label><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.cryMoney}"/>元</label></li>
                        <li class="line4">
                               <c:choose>
									<c:when test="${process<100 && project.state == 240}">
									   <a class="btn1" id="gDtile-btn" title="" >立即捐款</a>
									</c:when>
									<c:when test="${project.state == 260}">
									   <a class="btn1"  title="" >募捐完成</a>
									</c:when>
									<c:otherwise >
									   <a class="btn1"   title="">未发布</a>
									</c:otherwise>
								</c:choose>
                                |<a href="#" class="btn2" id="share" <c:if test="${firstgh!=null}"> data="${firstgh.perMoney}|${firstgh.goodPassWord}|${firstgh.companyName}" </c:if> ptitle="${project.title}">爱心传播</a>
                         </li>
                    </ul>
                </div>
            </div>
			<div class="info_block2">
                	<div class="block_hd"><i></i>发起人信息</div>
                    <div class="block_bd">
                   	  <div class="basic_info">
                        	<div class="thumb">
                        	<c:choose>
                        		<c:when test="${fabu.headImageUrl != null }"><img src="${fabu.headImageUrl }" width="92" height="92"></c:when>
                        		<c:otherwise><img src="<%=resource_url%>res/images/detail/people_avatar.jpg" width="92" height="92"></c:otherwise>
                        	</c:choose>
                        	</div>
                            <div class="info">
                            	<p class="line1">${fabu.realName}<i>${fabu.relation}</i></p>
                                <p class="line2"><c:if test="${fabu.vocation!=null&&fabu.vocation!=''}">${fabu.vocation}</c:if></p>
                                <p class="line3">${fabu.familyAddress}</p>
                            </div>
                        </div>
                        <div class="contact_info">
                        	<P>身份证：
                        	 <c:if test="${fabu.indetity!=null && fabu.indetity!=''}">
                        	 ${fn:substring(fabu.indetity, 0, 10)}******${fn:substring(fabu.indetity,16,fn:length(fabu.indetity))}
                        	 </c:if>
                        	</P>
                            <p><span>电&nbsp;&nbsp;&nbsp;话：${fabu.linkMobile}</span></p>
                            <p><span>QQ | 微信：${fabu.qqOrWx}</span></p>
                        </div>
                    </div>
            </div>
			<div class="info_block2">
                	<div class="block_hd"><i></i>证明人信息</div>
                	<div class="block_bd">
                   	  <div class="basic_info">
                        	<div class="thumb">
                        	<c:choose>
                        		<c:when test="${zhengming.headImageUrl != null }"><img src="${zhengming.headImageUrl }" width="92" height="92"></c:when>
                        		<c:otherwise><img src="<%=resource_url%>res/images/detail/people_avatar.jpg" width="92" height="92"></c:otherwise>
                        	</c:choose>
                        	</div>
                            <div class="info">
                            	<p class="line1">${zhengming.realName}<i>${zhengming.persition}</i></p>
                                <p class="line2">${zhengming.workUnit}</p>
                                <p class="line3">电   话：${zhengming.linkMobile}</p>
                            </div>
                        </div>
                    </div>
                </div>
			<div class="info_block3">
                	<div class="block_hd"><i></i>爱心企业
                	   <c:if test="${isComapny}">
                	    <a href="<%=resource_url%>enterprise/core/entcreatehelpgood.do?projectId=${project.id}&title=${project.title}" id="blockAdd">企业加入</a>
                	  </c:if>
                    </div>
                    <div class="block_bd">
                        <c:if test="${goodhelps!=null}">
	                    	<ul class="list1">
	                    	    <c:forEach items="${goodhelps}" var="goodhelp">
			                   	 <li>
			                   	     <span class="col1">
			                   	     	<c:choose>
										  <c:when test="${fn:length(goodhelp.companyName)<=8}">
										          ${goodhelp.companyName}
										  </c:when>
										  <c:otherwise>
												${fn:substring(goodhelp.companyName, 0, 8)}...
										  </c:otherwise>
										</c:choose>
			                   	      </span>
			                   	     <span class="col2"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${goodhelp.freezAmount}"/>元</span><span class="col3"><a class="forward_btn" data="${goodhelp.perMoney}|${goodhelp.goodPassWord}|${goodhelp.companyName}">一起助善</a></span></li>
		                        </c:forEach>	
	                            <li class="more" style="display: none;"><a href="#">更多>></a></li>
	                        </ul>
                        </c:if>
                    </div>
                  <!--   <div class="block_hd">爱心人士</div>
                    <div class="block_bd">
                    <dl class="list2">
                    	<dt><span class="col1">捐赠人</span><span class="col2">捐赠金额</span><span class="col3">捐赠时间</span></dt>
                        <dd><span class="col1">黄小仙</span><span class="col2">￥1000</span><span class="col3">07/14 14:00</span></dd>
                     </dl>
                    </div> -->
                </div>
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
                            <li>
                            	<span class="lst-col1">
	                            	<c:choose>
									  <c:when test="${fn:startsWith(donate.nickName,'游客')}">
									          游客
									  </c:when>
									  <c:when test="${fn:length(donate.nickName)<=8}">
										          ${donate.nickName}
									  </c:when>
									  <c:otherwise>
										${fn:substring(donate.nickName, 0, 8)}...
									  </c:otherwise>
									</c:choose>
                            	</span>
                                <span class="lst-col2">￥<fmt:formatNumber type="number" pattern="#,##0.00#" value = "${donate.donatAmount}"/></span>
                                <span class="lst-col3"><fmt:formatDate value="${donate.donatTime}" pattern="MM/dd HH:mm" /></span>
                                
                            </li>
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
                	<p><a href="<%=resource_url%>help/questions.do?no=0">实名认证时需要注意什么？</a></p>
                    <p><a href="<%=resource_url%>help/questions.do?no=2">忘记密码怎么找回？</a></p>
                    <p><a href="<%=resource_url%>help/questions.do?no=3">怎么求助？</a></p>
                    <p><a href="<%=resource_url%>help/questions.do?no=5">捐款方式有哪些？</a></p>
                </div>
            </div>
            <c:if test="${loveNews!=null}">
	             <div class="gDtil-item yh">
	            	<div class="gDtil-title">
	                	<i></i>善园基金会爱心故事
	                </div>
	                <div class="gDtil-list">
	                <c:forEach items="${loveNews}" var="news">
                    	   <p data="${news.id}">
                    	     <c:if test="${fn:length(news.title)>16}">
						       <a href="<%=web_url%>news/center.do?id=${news.id}" > ${fn:substring(news.title, 0, 16)}...</a>
						    </c:if>
						    <c:if test="${fn:length(news.title)<=16}">
						       <a href="<%=web_url%>news/center.do?id=${news.id}" > ${news.title}</a>
						    </c:if>
                    	   <span><fmt:formatDate  value="${news.createtime}" pattern="yyyy-MM"/></span></p>
                    </c:forEach>
	                </div>
	            </div>
                </c:if>
        </div>
    </div> 
</div>
<input type="hidden" id="pprim" data="">
<%@ include file="./../common/footer.jsp" %>
<input id="projectId" type="hidden" value="${project.id}">
<input id="visitor" type="hidden" value="1">
<input id="pName" type="hidden" value="${project.title}">
<input id="amount" type="hidden" value="<fmt:formatNumber value="${project.cryMoney - project.donatAmount}" type="NUMBER" pattern="#0.00"/>">
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="http://static.bshare.cn/b/buttonLite.js#uuid=48224106-d04a-4cbc-aecb-132a3f608fda&style=-1"></script>
<script data-main="<%=resource_url%>res/js/dev/goodDetail.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>