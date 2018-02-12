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
<meta name="keywords" content="善园网、${ oneNew.keywords}"/>
<meta name="description" content="善园网、${ oneNew.abstracts}"/>
<meta name="viewport"/>
<title>善园-${oneNew.title}</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
</head> 
<body>
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="">首页</a> > 关于善园基金会
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight detailNew">
            <div class="boederbox">
            	<h1>${oneNew.title}</h1>
                <p class="detSource"><span>来源：善园基金会</span>     <span>发布时间：<fmt:formatDate  value="${oneNew.createtime}" pattern="yyyy-MM-dd HH:mm"/></span></p>
                <p class="detShare">
                	<span>
                    	<a href="#" class="sinaminiblog" title=""></a>
                        <a href="#" class="qzone" title=""></a>
                        <a href="#" class="qqim" title=""></a>
                        <a href="#" class="weixin" title=""></a>
                    </span>
                </p>
                <div class="detCon">
                	${oneNew.content}
                     <br/>
                   	 <c:forEach items="${oneNew.bfileList}" var="img">
				        <p class="pic"><img style="margin-left:70px;"  src="${img.url}" width="590px"  alt=""><c:if test="${img.description!='1'}"><br/><i>${img.description}</i></c:if></p>
				    </c:forEach>
                </div>
            </div>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/activityList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>