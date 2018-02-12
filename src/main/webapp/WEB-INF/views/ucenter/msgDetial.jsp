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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css" />
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter msgDtil"> 
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
                <div class="uCEN-list list msgDtil">
                    <div class="uCEN-hd">
                    <a class="back" href="http://www.17xs.org/ucenter/core/msg.do" target="_self">返回</a> 
                </div>
                <div class="uCEN-bd"> 
                		<c:choose>
						<c:when test="${errorcode==1}">
						<div class="msgd-hd">
                             <div class="msgd-tit"><b>${data.title}</b><span>
                             <c:if test="${data.pre}">
                                  <a href="http://www.17xs.org/message/msgdetail.do?id=${data.id}&type=pre" >上一封</a>
                             </c:if>
                             <c:if test="${data.next}">
                                  <a href="http://www.17xs.org/message/msgdetail.do?id=${data.id}&type=next" >下一封</a>
                             </c:if>
                             <a href="http://www.17xs.org/message/dmsgandnext.do?id=${data.id}">删除</a></span></div>
                         	 <div class="msgd-info"><span>发件人：${data.sender}</span><span class="nomrgR">时间：${data.cTimeFormat}</span></div>
                         </div>
                         <div class="msgd-bd nobdrB">
                           ${data.content}
                         </div>
                    </div> 
						</c:when>
						<c:otherwise>
						${errormsg}
						</c:otherwise>
						</c:choose>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/mymsg.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
