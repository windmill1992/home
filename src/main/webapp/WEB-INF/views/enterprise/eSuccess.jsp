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
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entLegalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
             <%@ include file="./../common/eleft_menu.jsp"%>
            </div>
            <div class="uCEN-R"> 
                <div class="enfState">
                	<h2>企业认证</h2>
                    <div class="enfSbox">
                    	<div class="enfTle">
                    	               恭喜，企业认证成功！我要去
                    	      <c:choose>
                    	         <c:when test="${company.coverImageUrl!=null}">
                    	               <a href="<%=resource_url%>enterprise/core/perfectOk.do" title="">查看公司信息，参加企业助善，一起行善。</a>
                    	         </c:when>
                    	         <c:otherwise>
                    	         	  <a href="<%=resource_url%>enterprise/core/perfect.do" title="">完善公司信息，参加企业助善，一起行善。</a>
                    	         </c:otherwise>
                    	      </c:choose>
                    	</div>
                        <div class="enfCon">
                        	<div class="enfgroup">
                                <label>企业注册号：</label>
                                <span class="enfText">${company.registerNo}</span>
                            </div>
                            <div class="enfgroup">
                                <label>组织结构代码：</label>
                                <span class="enfText">${company.groupCode}</span>
                            </div>
                            <div class="enfgroup">
                                <label>法定代表人：</label>
                                <span class="enfText">${company.legalName}</span>
                            </div>
                            <div class="enfgroup">
                                <label>身份证号码：</label>
                                <span class="enfText">${company.identity}</span>
                            </div>
                            <div class="enfgroup">
                                <label>联系人姓名：</label>
                                <span class="enfText">${company.head}</span>
                            </div>
                            <div class="enfgroup">
                                <label>手机号：</label>
                                <span class="enfText">${company.mobile}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/entlegalize.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>