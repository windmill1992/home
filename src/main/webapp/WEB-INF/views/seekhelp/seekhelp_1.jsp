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
<title>善园网 - 我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/needhelp.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/> 
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer">
  <div class="page clear">
    <!-- <div class="page-hd">
      <h2 class="w1000">我要求助</h2>
    </div> -->
    <div class="page-bd">
    	<div class="publish">
    		<div class="step-progress step1">            	
            </div>
            <div class="step-detail">
            	<div class="select-type">
                	<ul>
                    	<li><i>请选择求助类型：</i><p id="needhelptype">
                    	<c:forEach var="record" items="${atc}" varStatus="status">
                    	<a data="${record.typeName_e}" href="javascript:void(0);">${record.typeName}</a>
                    	<c:if test="${status.last !=true}">|</c:if>
                    	</c:forEach>
                    	</p></li>
                        <li><i>选择发布人类型：</i><p id="ReleaseType"><a href="javascript:void(0);">我帮别人发布</a>|<a href="javascript:void(0);">我为自己发布</a>|<a href="javascript:void(0);">请客服帮忙发布</a></p></li>
                    </ul>
                </div>
                <div class="select-detail" id="needhelpCon">
                	<dl >
                    	<dt>为了您的求助能尽快发布，请选择求助类型和发布人类型。</dt>
                	</dl>             
                </div>
                <div class="nextbtn"><a  class="next" id="needhelpNext">下一步</a><span id="needhelpOther" style="display:none"><i>|</i><a class="help" target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes">我不会，邀请善管家帮忙</a></span></div>         	
          </div>
        </div>
    </div>
  </div>
</div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="userId" value="${userId }"/>
<script data-main="<%=resource_url%>res/js/dev/needhelp.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
