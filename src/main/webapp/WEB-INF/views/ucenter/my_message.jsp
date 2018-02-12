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
<title>善园 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css" />
</head>
<body>
	<%@ include file="./../common/newhead.jsp"%>
	<input type="hidden" value="${error}" name="msg" id="msg">
	<input type="hidden" value="${state}" name="state" id="state">
	<div class="bodyer userCenter myMsg">
		<div class="page">
			<div class="page-bd">
				<c:choose>
				    <c:when test="${userType=='enterpriseUsers'}">
				        <div class="uCEN-L">
                   		<%@ include file="./../common/eleft_menu.jsp"%>
                   		</div>
                    </c:when>
                    <c:otherwise>
	                    <div class="uCEN-L">
						<%@ include file="./../common/pleft_menu.jsp"%>
					   </div>
                    </c:otherwise>  
				</c:choose>
				<div class="uCEN-R">
		                <div class="uCEN-hd">
		                	<span class="uCEN-tit">系统信息</span>
		                    <dl class="select">
		                        <dt class="sel-checked">
                                    <input class="sel-val" v="0" type="text" value="全部" readonly>
                                    <span class="triangle"><i class="triangle-up"></i></span>
                                    <input id="sel-checked" type="hidden" value="0">
                                </dt>
                                <dd class="sel-options" style="display: none;">
									<a class="sel-option">全部</a>
                                    <a class="sel-option sel-check">三个月内</a>
                                    <a class="sel-option">半年内</a>
                                    <a class="sel-option">一年内</a>
                                </dd>
		                    </dl>
		                </div>
		                <div class="uCEN-bd">
		                    <div class="list listmsg">
		                            <!--<div class="list-prompt">提示信息</div>-->
		                         <ul class="list-hd">
		                            <li class="lst-col1">序号</li>
		                            <li class="lst-col2">发件人</li>
		                            <li class="lst-col3">时间</li>
		                            <li class="lst-col4">标题</li>
		                            <li class="lst-col5">操作</li>
		                         </ul>
		                         <input id="delMsgId" type="hidden" value=""/>
		                         <div id="list-bd" class="list-bd">
									<div class="prompt">
		                            	<div class="loading"></div>
		                         	</div>
		                         </div>
		                    </div>
		                </div>
				</div>

			</div>
		</div>
    </div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/mymsg.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
