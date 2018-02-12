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
<title>善园网—一起行善，互联网公益平台</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNewlunbo.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodnotes.css">
</head>
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyNew gdWherelist backd">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="">首页</a> &gt;  行善记录
    </div>
    <div class="notes w1000">
        <a href="javaScript:void(0);" class="prev_fl"><img src="<%=resource_url%>res/images/goodlibrary/prev.png"></a>
        <div class="notes_head">

            <div class="notes_fl"><img src="<%=resource_url%>res/images/goodlibrary/notes_log.png"></div>
            <div class="notes_fr">
                <p class="notes_p1">公益善行记录-善库</p>
                <p class="notes_p2"></p>
            </div>
        </div>
        <div class="notes_nav">
            <li class="notes_li1 notes_q notes_gg"><a href="javaScript:void (0);">企业</a></li>
            <li class="notes_li1 notes_c"><a href="javaScript:void (0);">慈善家</a></li>
            <li class="notes_li1 notes_m"><a href="javaScript:void (0);">善管家</a></li>
        </div>
        <!-- 企业 -->
        <div class="notes_banner" id="company" >
              <!-- 数据 -->
              <c:if test="${pageNum >1}">
              	<c:forEach var="i" begin="1" end="${pageNum }">
              		<ul>
              			<c:forEach var="goodlibrary" items="${goodlibrary }" varStatus="status" begin="${(i-1)*12 }" end="${(i-1)*12+11 }">
              				<li><a href="http://www.17xs.org/goodlibrary/getGoodLibraryView.do?lirbraryId=${goodlibrary.id }"><img src="${goodlibrary.logoUrl }"></a></li>
              			</c:forEach>
		            </ul>
              	</c:forEach>
              </c:if>
              <c:if test="${pageNum ==1}">
              	<ul>
              		<c:forEach var="goodlibrary" items="${goodlibrary }" varStatus="status" end="11">
           				<li><a href="http://www.17xs.org/goodlibrary/getGoodLibraryView.do?lirbraryId=${goodlibrary.id }"><img src="${goodlibrary.logoUrl }"></a></li>
           			</c:forEach>
              	</ul>
              </c:if>
        </div>
        <!-- 善管家 -->
        <div class="notes_banner" id="goodPeople" style="display:none">
              <!-- 数据 -->
              <c:if test="${pageNumGP >1}">
              	<c:forEach var="i" begin="1" end="${pageNumGP }">
              		<ul>
              			<c:forEach var="goodPeople" items="${goodPeople }" varStatus="status" begin="${(i-1)*12 }" end="${(i-1)*12+11 }">
              				<li><a href="http://www.17xs.org/project/index/?keyWords=${goodPeople.userName }" target="_blank"><img src="${goodPeople.coverImageUrl }"></a></li>
              			</c:forEach>
		            </ul>
              	</c:forEach>
              </c:if>
              <c:if test="${pageNumGP ==1}">
              	<ul>
              		<c:forEach var="goodPeople" items="${goodPeople }" varStatus="status" end="11">
           				<li><a href="http://www.17xs.org/project/index/?keyWords=${goodPeople.userName }" target="_blank"><img src="${goodPeople.coverImageUrl }"></a></li>
           			</c:forEach>
              	</ul>
              </c:if>
        </div>
        <a href="javaScript:void(0);" class="next_fr"><img src="<%=resource_url%>res/images/goodlibrary/next_y.png"></a>
    </div>
</div>
<input type="hidden" id="currentPage" value="${pageNum }">
<input type="hidden" id="type" value="${type }">
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/goodLibrary_list.js?v=20160527" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>