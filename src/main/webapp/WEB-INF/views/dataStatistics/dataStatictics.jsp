<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>行善排行榜</title>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="../../../../res/css/dev/head.css"/>
<link type="text/css" rel="stylesheet" href="../../../../res/css/dev/dataStatistics/goods_charts.css" />
</head>
<body>
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="chart_head">
    <p>当前位置>首页>行善记录</p>
</div>
<!--折线图-->
<div class="chart_top">
    <div class="chart_backgtound">
        <div class="chartImg">
            <img src="../../../../res/images/dataStatistics/chart_time.png" />
            <div class="chart_fr">
                <span></span>
                <p>累计捐款${Itotal }元</p>
            </div>
        </div>
        <div id="line" style="width:900px;height:350px; margin: auto"></div>
    </div>
</div>
<!--内容-->
<div class="chartContent">
    <!--爱心捐款排行-->
    <div class="chartMargin">
        <div class="loveImg1"></div>
        <!--排行列表-->
        <ul class="chartUl">
            <li>
                <dl id="weeked">
                    <dt class="chartLi1">
                        <h3>上周</h3>
                        <span>${weekTotalDonate }元</span>
                    </dt>
                    <%-- <c:forEach items="${weekDonate }" var="wDonate" varStatus="index">
                    	<c:choose>
                    		<c:when test="${index.index==0 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB1"></b>
                            		<img src="${wDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${wDonate.userName }</p>
                            		<p>捐款<span>${wDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index==1 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB2"></b>
                            		<img src="${wDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${wDonate.userName }</p>
                            		<p>捐款<span>${wDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index==2 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB3"></b>
                            		<img src="${wDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${wDonate.userName }</p>
                            		<p>捐款<span>${wDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:otherwise>
                    			<dd>
                        			<div class="ddFl">
                            		<b>${index.index+1 }</b>
                            		<img src="${wDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${wDonate.userName }</p>
                            		<p>捐款<span>${wDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:otherwise>
                    	</c:choose>
                    </c:forEach> --%>
                    
                </dl>
            </li>
            <li>
                <dl id="monthed">
                    <dt class="chartLi2">
                    <h3>上月</h3>
                    <span>${monthTotalDonate }元</span>
                    </dt>
                   <%--  <c:forEach items="${monthDonate }" var="mDonate" varStatus="index">
                    	<c:choose>
                    		<c:when test="${index.index ==0}">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB1"></b>
                            		<img src="${mDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${mDonate.userName }</p>
                            		<p>捐款<span>${mDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index ==1}">
                    			<dd>
                        			 <div class="ddFl">
                           			 <b class="ddB2"></b>
                            		 <img src="${mDonate.coverImageurl }" />
                        			 </div>
                        			 <div class="ddFr">
                            		 <p>${mDonate.userName }</p>
                            		 <p>捐款<span>${mDonate.donatAmount }元</span></p>
                        			 </div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index ==2}">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB3"></b>
                            		<img src="${mDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${mDonate.userName }</p>
                            		<p>捐款<span>${mDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:otherwise>
                    			<dd>
                        			<div class="ddFl">
                            		<b>${index.index+1}</b>
                            		<img src="${mDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${mDonate.userName }</p>
                            		<p>捐款<span>${mDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:otherwise>
                    	</c:choose>
                    </c:forEach> --%>
                    
                </dl>
            </li>
            <li>
                <dl id="yeared">
                    <dt class="chartLi1">
                    <h3>2017年度</h3>
                    <span>${yearTotalDonate }元</span>
                    </dt>
                    <%-- <c:forEach items="${yearDonate }" var="yDonate" varStatus="index">
                    	<c:choose>
                    		<c:when test="${index.index==0 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB1"></b>
                            		<img src="${yDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${yDonate.userName }</p>
                            		<p>捐款<span>${yDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index==1 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB2"></b>
                            		<img src="${yDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${yDonate.userName }</p>
                            		<p>捐款<span>${yDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index==2 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB3"></b>
                            		<img src="${yDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${yDonate.userName }</p>
                            		<p>捐款<span>${yDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:otherwise>
                    			<dd>
                        			<div class="ddFl">
                            		<b>${index.index+1 }</b>
                            		<img src="${yDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${yDonate.userName }</p>
                            		<p>捐款<span>${yDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:otherwise>
                    	</c:choose>
                    </c:forEach> --%>
                </dl>
            </li>
            <li>
                <dl id="total">
                    <dt class="chartLi2">
                    <h3>全部</h3>
                    <span>${Itotal }元</span>
                    </dt>
                    <%-- <c:forEach items="${totalDonate }" var="tDonate" varStatus="index">
                    	<c:choose>
                    		<c:when test="${index.index==0 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB1"></b>
                            		<img src="${tDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${tDonate.userName }</p>
                            		<p>捐款<span>${tDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index==1 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB2"></b>
                            		<img src="${tDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${tDonate.userName }</p>
                            		<p>捐款<span>${tDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:when test="${index.index==2 }">
                    			<dd>
                        			<div class="ddFl">
                            		<b class="ddB3"></b>
                            		<img src="${tDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${tDonate.userName }</p>
                            		<p>捐款<span>${tDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:when>
                    		<c:otherwise>
                    			<dd>
                        			<div class="ddFl">
                            		<b>${index.index+1 }</b>
                            		<img src="${tDonate.coverImageurl }" />
                        			</div>
                        			<div class="ddFr">
                            		<p>${tDonate.userName }</p>
                            		<p>捐款<span>${tDonate.donatAmount }元</span></p>
                        			</div>
                    			</dd>
                    		</c:otherwise>
                    	</c:choose>
                    </c:forEach> --%>
                </dl>
            </li>
            <div style="clear: both"></div>
        </ul>
    </div>
</div>
<!--爱心企业排行榜-->
<div class="company">
    <div class="companyMargin">
        <div class="companyContent">
            <div class="companyImg">
                <p class="comP">爱心企业</p>
                <img src="../../../../res/images/dataStatistics/loveRed.png"/>
            </div>
            <ul id="goodlibrary">
            	<%-- <c:forEach  items="${goodlibrary }" var="glibrary" varStatus="index">
            		<a href="http://www.17xs.org/goodlibrary/getGoodLibraryView.do?lirbraryId=${glibrary.id }"><li><img src="${glibrary.logoUrl }"></li></a>
            	</c:forEach> --%>
            </ul>
        </div>
    </div>
    <div class="companyMargin">
        <div class="companyContent">
            <div class="companyImg">
                <p class="comP">善管家</p>
                <img src="../../../../res/images/dataStatistics/loveRed.png"/>
            </div>
            <ul id="goodPeople">
            	<%-- <c:forEach  items="${goodPeople }" var="gpeople" varStatus="index">
            		<a href="http://www.17xs.org/project/index/?keyWords=${gpeople.userName }"><li><img src="${gpeople.coverImageUrl }"></li></a>
            	</c:forEach> --%>
            </ul>
        </div>
    </div>
</div>
<script type="text/javascript" src="../../../../res/js/jquery-1.7.2-min.js"></script>
<script type="text/javascript" src="../../../../res/js/dev/dataStatistics/echarts.min.js"></script>
<script type="text/javascript" src="../../../../res/js/dev/dataStatistics/goods_charts.js"></script>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="../../../../res/js/dev/dataStatistics/dataStatistics.js?v=2016032" src="../../../../res/js/require.min.js"></script>
<input type="hidden" id="money" value="${money }"/>
<input type="hidden" id="time" value="${time }"/>
<input type="hidden" id="num" value="${num }"/>
</body>
</html>
