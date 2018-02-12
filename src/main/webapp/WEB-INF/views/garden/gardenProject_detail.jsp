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
<title>善园 - 公益众筹</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/cips.css"/>
</head>
<body>
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org" title="" >首页</a> &gt; <a href="#" title="" target="_blank">公益众筹</a> &gt;<span class="grayText">  ${project.title }</span>
    </div>
    <div class="w1000 viewNew">
    	<div class="cipsTop">
        	<img src="${project.coverImageUrl }" alt="">
			
            <div class="cipsCon">
            	<p>每份：<strong id="perMoney"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.perMoney}"/></strong> 元</p>
                <p>剩余：<strong id="maxNum">${project.leaveCopies}</strong> 份</p>
                <p>认捐：<span class="cipsnum"><em class="reduce">-</em><input style="height:37px;line-height:37px;" type="text" value="1" id="cipnum" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9.]+/,'');}).call(this)"><em class="add">+</em></span>份</p>
                <p>总计：<strong class="yellowText" id="donateAmount"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.perMoney}"/></strong> 元</p>
                <p class="center">
                	<c:choose>
                		<c:when test="${project.state==260 || project.leaveCopies == 0}">
                			<li style="position: relative;left: 50px;">募捐完成</li>
                		</c:when>
	                	<c:otherwise>
		                	<a href="javascript:void(0)" id="payBt1">立即支持</a>
	                	</c:otherwise>
                	</c:choose>
                </p>
                <p class="right">已筹集：${project.totalCopies-project.leaveCopies } 份</p>
            </div>
        </div>
        <div class="viewCon">
        	<div class="lNew boederbox" id="cipsCon">
            	<div class="conTab" id="tab-hd">
                	<span class="on">项目概况</span>
                    <span>项目进展</span>
                    <span>众筹记录</span>
                </div>
            	<div class="conOne">
                	<h2>
                		<span class="lText">项目概况</span>
                	</h2>
                    <div class="surveyCon">
						${project.content}
						<br/>
                    	 <c:forEach items="${project.bfileList}" var="img">
					        <p class="pic"><img src="${img.url}" width="590px" alt=""><i>${img.description}</i></p>
					    </c:forEach>
                    </div>
                    <div class="cipsSupport">
                    	<c:choose>
	                		<c:when test="${project.state==260 || project.leaveCopies == 0}">
	                			<li>募捐完成</li>
	                		</c:when>
		                	<c:otherwise>
			                	<a href="javascript:void(0)" id="payBt2">我要支持</a>
		                	</c:otherwise>
                		</c:choose>
                    </div>
                </div>
            	<div class="conOne viewList" style="display:none;">
                	<h2>
                		<span class="lText">项目进展</span>
                	</h2>
                    <ul class="cipsProject">
						 <c:if test="${reports!=null}">
	                         	 <c:forEach items="${reports}" var="report">
		                             <li>
			                        	<em></em>
			                        	<h3>
			                        	</h3>
			                            <p>${report.content}
<!--			                            	<a href="#" title=""> 详情>></a>-->
			                            </p>
										<p>
<!--											<img src="res/images/newIndex/view/cipspic_03.jpg" alt="">-->
											   <c:forEach items="${report.contentImageUrl}" var="img">
										        	<img src="${img}" alt="" style="max-width: 460px;overflow: hidden;"/><br/>
										  		</c:forEach>
										</p>
			                        </li>
							     </c:forEach>
                        </c:if>
                    </ul>
                </div>
                <div class="conOne bdback" style="display:none">
                	<h2>
                		<span class="lText">众筹记录</span>
                	</h2>
                    <div class="copsCord" id="grecordList">
                    </div>
                </div>
                <div class="conOne bdrecord" style="display:none" >
                	<h2>
                		<span class="lText">捐款记录</span>
                	</h2>
                    <div class="list-list" id="grecordList">
                    	<ul class="list-hd">
                            <li class="lst-col1">捐赠人</li>
                            <li class="lst-col2">捐赠金额（元）</li>
                            <li class="lst-col3">捐赠时间</li>
                        </ul>
                        <div class="list-bd" id="cordList">
                        </div>
                    </div>
                </div>
                
            </div>
            <div class="rNew">
            	<div class="boederbox info_block2">
                	<h2>
                		<span class="lText">发起人</span>
                	</h2>
                	<div class="basic_info" style="margin-top:20px;">
                        <div class="thumb">
	                        <c:choose>
	                        	<c:when test="${fabu.headImageUrl != null }">
	                        		<img src="${fabu.headImageUrl }" width="68" height="68" >
	                        	</c:when>
	                        	<c:otherwise>
	                        		<img src="<%=resource_url%>res/images/detail/people_avatar.jpg" width="68" height="68">
	                        	</c:otherwise>
	                        </c:choose>
                        </div>
                        <div class="info">
                            <p class="line1">${fabu.realName}</p>
                            <p class="line2">${fabu.familyAddress}</p>
                            <p class="line3">电话：${fabu.linkMobile}</p>
                        </div>
                    </div>
                    <div class="contact_info">
						<p id="smallContent">
						  ${fn:substring(fabu.info, 0, 50)}<a href="javascript:" onclick="javascript:fullContent()" > 详情&gt;&gt;</a>
						</p>
                         <p id="fullContent" style="display:none;">
						  ${fabu.info}<a href="javascript:" onclick="javascript:smallContent()" > 详情收起</a></p>
                     </div>
                </div>
                
                <div class="boederbox mt15">
                	<h2>
                		<span class="lText">最新筹集</span>
                	</h2>
                    <div class="dtList" id="dtList">
						<ul>
							<c:forEach items="${newDonat}" var="donation" varStatus="status">
								<li>
									<dl>
										<dt>
											<c:if test="${donation.coverImageurl != null }"><img src="${donation.coverImageurl }" alt=""></c:if>
											<c:if test="${donation.coverImageurl == null }"><img src="<%=resource_url%>res/images/detail/people_avatar.jpg" alt=""></c:if>
										</dt>
										<dd style="width:225px">
											<p class="title">
												<c:choose>
													<c:when test="${fn:startsWith(donation.nickName,'游客') }">爱心人士</c:when>
													<c:otherwise>
														<c:if test="${fn:length(donation.nickName)>=8}">${fn:substring(donation.nickName,0,8)}...</c:if>
														<c:if test="${fn:length(donation.nickName)<8}">${donation.nickName}</c:if>
													</c:otherwise>
												</c:choose>
												<span class="time fontArial">
													${donation.timeStamp }
												</span>
											</p>
											<p>认捐${donation.donateCopies }份
											<span class="num"><fmt:formatNumber type="number" pattern="#,##0.00#"  value = "${donation.donatAmount}"/></span>元</p>
										</dd>
									</dl>
								</li>
							</c:forEach> 
						</ul>
                    </div>
                </div>
                <div class="boederbox mt15">
                	<h2>
                		<span class="lText">新闻公告</span>
                	</h2>
                	<div class="adList">
                    <c:forEach items="${latestNews}" var="news">
	                    	 <p data="${news.id}">
	                    	    <c:if test="${fn:length(news.title)>14}">
	                    	    	<a href="<%=web_url%>news/center/?id=${news.id}&type=1" title=""><span class="listIcon"></span>${fn:substring(news.title, 0, 14)}...</a>
							    </c:if>
							    <c:if test="${fn:length(news.title)<=14}">
							       <a href="<%=web_url%>news/center/?id=${news.id}&type=1" title=""><span class="listIcon"></span>${news.title}</a>
							    </c:if>
							    </p>
	                    </c:forEach>
                	</div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="projectId" value="${project.id }">
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/cipsView.js" src="<%=resource_url%>res/js/require.min.js"></script>
<script>
function fullContent(){
 $("#fullContent").css("display","block");
 $("#smallContent").css("display","none");
}

function smallContent(){
 $("#fullContent").css("display","none");
 $("#smallContent").css("display","block");
}
</script>
</body>
</html>