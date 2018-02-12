<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport"/>
<c:choose>
  <c:when test="${type==3}">
     <title>善园 - 爱心故事</title>
  </c:when>
  <c:otherwise>
    <title>善园 - 新闻中心</title>
  </c:otherwise>
</c:choose>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>

<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/news.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer news"> 
   <div class="page">
		<div class="page-hd">
		   <c:choose>
		      <c:when test="${type==3}">
		        <div class="page-tit"><h2 class="w1000">爱心故事</h2></div>
		      </c:when>
		      <c:otherwise>
		        <div class="page-tit"><h2 class="w1000">新闻中心</h2></div>
		      </c:otherwise>
		   </c:choose>
        </div>
        <div class="page-bd" id="newsListBox">
        	<div class="newsL">
	        	 <c:choose>
			      <c:when test="${type==3}">
			         <div class="newsTle">爱心故事<em></em></div>
			      </c:when>
			      <c:otherwise>
			        <div class="newsTle">新闻中心<em></em></div>
			      </c:otherwise>
			    </c:choose>
                <ul class="newsList" id="newsList">
                	
                </ul>
            </div>
                 <div class="newsR">
                	<c:if test="${hotNews!=null}">
                	<div class="news-item">
	                	<div class="news-title">热点新闻<a href="<%=web_url%>news/center.do?type=2" class="more" title="">更多..</a></div>
	                    <div class="news-list">
	                    	<c:forEach items="${hotNews}" var="news">
	                    	   <p data="${news.id}">
	                    	   <c:if test="${fn:length(news.title)>16}">
							       <a href="<%=web_url%>news/center.do?id=${news.id}" >${fn:substring(news.title, 0, 16)}...</a>
							    </c:if>
							    <c:if test="${fn:length(news.title)<=16}">
							          <a href="<%=web_url%>news/center.do?id=${news.id}" >${news.title}</a>
							    </c:if>
	                    	   <span><fmt:formatDate  value="${news.createtime}" pattern="yyyy-MM"/></span></p>
	                    	</c:forEach>
	                    </div>
	                 </div>
                	</c:if>
                <c:if test="${latestNews!=null}">
                <div class="news-item">
                	<div class="news-title">最新新闻<a href="<%=web_url%>news/center.do?type=1" class="more" title="">更多..</a></div>
                    <div class="news-list">
                    	<c:forEach items="${latestNews}" var="news">
	                    	 <p data="${news.id}">
	                    	 <c:if test="${fn:length(news.title)>16}">
							          <a href="<%=web_url%>news/center.do?id=${news.id}" >${fn:substring(news.title, 0, 16)}...</a>
							    </c:if>
							    <c:if test="${fn:length(news.title)<=16}">
							         <a href="<%=web_url%>news/center.do?id=${news.id}" >${news.title}</a>
							    </c:if>
	                    	 <span><fmt:formatDate  value="${news.createtime}" pattern="yyyy-MM"/></span></p>
	                    </c:forEach>
                    </div>
                </div>
                </c:if>
                  <c:if test="${loveNews!=null}">
                <div class="news-item">
                	<div class="news-title">善园基金会爱心故事<a href="<%=web_url%>news/center.do?type=3" class="more" title="">更多..</a></div>
                    <div class="news-list">
                    	<c:forEach items="${loveNews}" var="news">
                    	   <p data="${news.id}">
                    	     <c:if test="${fn:length(news.title)>16}">
						          <a href="<%=web_url%>news/center.do?id=${news.id}" >${fn:substring(news.title, 0, 16)}...</a>
						    </c:if>
						    <c:if test="${fn:length(news.title)<=16}">
						         <a href="<%=web_url%>news/center.do?id=${news.id}" >${news.title}</a>
						    </c:if>
                    	   <span><fmt:formatDate  value="${news.createtime}" pattern="yyyy-MM"/></span></p>
                    	</c:forEach>
                    </div>
                </div>
                </c:if>
            </div>
        </div>
   </div>
</div>
<input type="hidden" value="${type}" id="newsType">
<%@ include file="./../common/footer.jsp" %>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<!-- <script type="text/javascript" src="http://static.bshare.cn/b/buttonLite.js#uuid=48224106-d04a-4cbc-aecb-132a3f608fda&style=-1"></script> -->
<script data-main="<%=resource_url%>res/js/dev/news.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
