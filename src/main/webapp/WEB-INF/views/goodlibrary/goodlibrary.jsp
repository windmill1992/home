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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/library.css">
</head> 
<!--头部 end-->
<!--主体 start-->
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyNew">
  	<div class="indexBnner" id="slide_div">
    	<div class="w1000 ">
    	<a class="sbtn prev" style="display: none;"></a>
        <a class="sbtn next" style="display: none;"></a>
        </div>
        <div class="bnnerpic bnnerpic1" id="scroll" style="background: url('${goodLibrary.bannerUrl }') top center no-repeat">
        <%--<img src="${goodLibrary.bannerUrl }" width="100%" alt=""></a>--%>
        </div>
    </div>
    <div class="indexSum">
    	<div class="w1000 w1130">
        	<span class="sumStart">
                <i class="sumIcon1"></i>
                      <div class="xiangmuleiji">捐助项目<br><em>${totalProjectNum }</em>个
                     </div>
            </span>
            <span class="sumMiddle"><i class="sumIcon2"></i>
                      <div class="xiangmuleiji">捐助金额<br><em><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${totalMoney }"/></em>元
                      </div>
            </span>
            <span class="sumMiddle1"><i class="sumIcon4"></i>
                      <div class="xiangmuleiji">行善次数<br><em>${totalDonateNum }</em>次
                      </div>
            </span>
           <span class="sumEnd"><i class="sumIcon3"></i>
                      <div class="xiangmuleiji">号召人数<br><em>${totalPeople }</em>人
                      </div>
            </span>
        </div>
    </div>
   </div>
<div class="mainNew w1000">
    <div class="library_box1">
    <a class="prev_fl fl" href="javaScript:void(0)"><img src="<%=resource_url%>res/images/goodlibrary/prev.png"></a>
        <ul class="library_ul">
            <li><h2>相关公益项目</h2></li>
            <li><a href="javascript:void(0)">${goodLibrary.familyAddress}</a></li>
            <c:forTokens items="${goodLibrary.tag }" delims="," var="tag"><li><a href="javascript:void(0)">${tag }</a></li></c:forTokens>
        </ul>
        <div class="library_list">

            <ul class="list_z">
            <c:forEach var="lc" items="${gLibrary}">
            	<li><a href="${lc.linkUrl }" title="" target="_blank"><img src="${lc.url}"  alt=""/><p>${lc.description }</p></a></li>
            </c:forEach>
            <c:forEach var="newUrl" items="${newUrl}">
            	<li><a href="<%=resource_url%>project/view/?projectId=${newUrl.id}" title="" target="_blank"><img src="${newUrl.coverImageUrl}"  alt=""/><p>${newUrl.coverImageDecription }</p></a></li>
            </c:forEach>
            </ul>

        </div>
    <a class="next_fr fr"><span><img src="<%=resource_url%>res/images/goodlibrary/next_y.png"></span></a>
    </div>
    <!--行善记录-->
    <div class="library_box2">
        <ul class="library_ul1">
            <li><h2>行善记录</h2></li>
            <li><a href="javaScript:void(0);" id="2016" class="year_a1">2016</a></li>
            <li><a href="javaScript:void(0);" id="2015" class="year_a2">2015</a></li>
        </ul>
        <div class="library_time" id="libraryList">
            <!-- 数据 -->
        </div>
        <div id="lstPages" class="lstPages bac_form" style="">
            <span class="lstP-con">
                <a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a>
                <span class="pages">
                    <i class="curPage" p="1">1</i>
                    <i p="2">2</i><i p="3">3</i>
                    <i p="4">4</i><i p="5">5</i>
                    <i p="6">6</i><i p="7">7</i>
                </span>
                <a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a>
            </span>
        </div>
    </div>

</div>
<input type="hidden" id="currentDate" value="2016">
<input type="hidden" id="userId" value="${userId }">
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/goodLibrary.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>