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
<title>善园 - 善园众筹详情</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodDetail.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer doGdDetail"> 
    <div class="gDtil-hd yh"><h2 class="w1000">&nbsp;善园众筹</h2></div>
    <div class="gDtil-bd">
        <div class="gDtil-bdL">
            <div class="gDtil-show">
            	<c:choose>
				<c:when test="${project!=null}">
				<div class="gDtil-show-img">
					<img src="${project.coverImageUrl}" alt="">
				</div>
					
                 <h2 class="yh">${project.title}</h2>
                <p  class="yh">${project.subTitle}<span class="rt">剩余：<span id="leaveNumber">${project.leaveCopies}</span>份</span></p>
                <div class="gDtil-quata gDtil-sy yh">
                	<div class="listone">单价：<span id="unitPrice" p ="${project.perMoney}"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${project.perMoney}"/></span>元</div>
                    <div class="listone listop">
                    <span>认捐</span>
                        <div class="gDtil-wrap">
                        	<a title="" class="btn-reduce" id="btn-reduce">-</a>
                            <a title="" class="btn-add" id="btn-add">+</a>
                            <input type="text" value="1" id="num">
                        </div>  
                    <span>份</span></div>
                    <div class="listone">共计<span id="total"><fmt:formatNumber type="number" value = "${project.perMoney}"/></span>元</div>
                     <c:choose>
					<c:when test="${project.leaveCopies>0}">
					   <a class="gDtile-btn" id="gDtile-btn" title="立即认捐">立即认捐</a>
					</c:when>
					<c:otherwise>
					  <span class="gDtile-btn gDtile-backbtn" title="">已完成</span>
					</c:otherwise>
				</c:choose>
                </div>
				</c:when>
				<c:otherwise>
				 没有信息
				</c:otherwise>
				</c:choose>
            </div>
            <div class="gDtil-tab yh">
            	<div class="tab-hd" id="tab-hd">
                	<span class="on">项目概况</span>
                    <span>认捐记录</span>
                </div>
                <div class="tab-bd garden-bd" id="tab-bd">
                	<div class="bdcon">
                    	${project.content}
                    	<br/>
                    	  <c:forEach items="${project.bfileList}" var="img">
					        <p class="pic"><img src="${img.url}" width="590px" alt=""><i>${img.description}</i></p>
					    </c:forEach>
                    </div>
                    <div class="bdcon bdback bdhoslist" style="display:none">
                    	<ul class="list-data-hd">
                            <li class="wlst-col1">认捐人</li>
                            <li class="wlst-col2">认捐数量</li>
                            <li class="wlst-col3">认捐金额</li>
                            <li class="wlst-col4 last">认捐时间</li>
                         </ul>
                         <div class="list-data-bd" id="hoslist">
                         	 
                         </div>
                         <div class="bdback-page">
                        	<div class="page">
                            	<a title="" class="prev pagedefalut" id="pageprev"></a>
                                <input type="text" value="1" id="pagenum"/>
                                <a title="" class="next" id="pagenext"></a>
                            </div>
                            <p id="pagetotle"></p>
                        </div>
                    </div>
                    
                </div>
            </div>
        </div>
        <div class="gDtil-bdR"> 
            
            <div class="gDtil-item yh">
            	<div class="gDtil-title">
                	认捐排行榜
                </div>
               <div class="gDtil-grdenlist gDtil-grdensort">
               		<c:choose>
					<c:when test="${mostDonat!=null}">
					    <c:forEach items="${mostDonat}" var="donation" varStatus="status">
					        <div class="grdenone <c:if test="${status.first}">grdenfirst</c:if>"><em class="grdenicon${status.index + 1}"></em>
					        <h3><c:choose>
					        <c:when test="${fn:startsWith(donation.nickName,'游客') }">爱心人士</c:when>
					        <c:otherwise>${donation.nickName}</c:otherwise>
					        </c:choose></h3>
					        <p>认捐总金额<fmt:formatNumber type="number" pattern="#,##0.00#"  value = "${donation.donatAmount}"/>元</p></div>
					    </c:forEach> 
					</c:when>
					<c:otherwise>
					 暂无数据
					</c:otherwise>
				</c:choose>    
                </div>
            </div>
            <div class="gDtil-item yh">
            	<div class="gDtil-title">
                	最新认捐消息
                </div>
                <div class="gDtil-grdenlist">
                <c:choose>
					<c:when test="${newDonat!=null}">
					    <c:forEach items="${newDonat}" var="donation" varStatus="status">
					        <div class="grdenone <c:if test="${status.first}">grdenfirst</c:if>">
					        <c:choose>
					        <c:when test="${fn:startsWith(donation.nickName,'游客') }">爱心人士</c:when>
					        <c:otherwise>${donation.nickName}</c:otherwise>
					        </c:choose>
					        <br>认捐了${donation.projectTitle}，${donation.donateCopies}份，总金额<fmt:formatNumber type="number" pattern="#,##0.00#"  value = "${donation.donatAmount}"/>元</div>
					    </c:forEach> 
					</c:when>
					<c:otherwise>
					 正在查询最新认捐消息
					</c:otherwise>
				</c:choose>         
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/footer.jsp" %>
<input id="projectId" type="hidden" value="${project.id}">
<script data-main="<%=resource_url%>res/js/dev/goodgardendetail.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
