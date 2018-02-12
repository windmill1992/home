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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
	<div class="page"> 
        <div class="page-bd">
			<div class="uCEN-L">
			<c:choose>
			<c:when test="${user.userType=='enterpriseUsers'}">
         		<%@ include file="./../common/eleft_menu.jsp"%>
         	</c:when>
         	<c:otherwise>
         		<%@ include file="./../common/pleft_menu.jsp"%>
         	</c:otherwise>
         	</c:choose>
			</div>
			
<!-- =======================新版捐赠记录======================= -->
			
			<div class="uCEN-R donation_right">
           		
    			<div class="uCEN-hd uCEN-hd1">
                    <span class="uCEN-tit">捐赠记录</span>
                    <div class="bindBox">
	                    <ul class="bindList">
	                        <li><a class="mobile"></a></li>
	                        <li><a class="email"></a></li>
	                        <li><a class="qq"></a></li>
	                        <li><a class="wx"></a></li>
	                        <li><a class="wb"></a></li>
	                        <li><a class="zfb"></a></li>
	                    </ul>
	                </div>
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
                    <a href="<%=resource_url%>ucenter/getInvoicePage.do" class="invoiceBtn" style="position: absolute; right: 130px; top: 15px;">索取发票</a>
                </div>
                
                <div class="donation_conn" id="listShow">
                    <dl>
                        <dt>
	                        <span class="naidt_01">主题</span>
	                        <span class="naidt_02">捐助金额</span>
	                        <span class="naidt_03">受助人</span>
	                        <span class="naidt_04">反馈</span>
	                        <span class="naidt_05">捐助时间</span>
	                        <span class="naidt_06">捐助类型</span>
                        </dt>
                    </dl>
                    <div id="list-bd" class="list-bd">
                    	<div class="prompt"><div class="loading"></div></div>
                    </div>
                </div>
                
            </div>
			
			
			
			
<!-- =====================原版捐赠记录===================================== -->
			
          <!--   <div class="uCEN-R"> 
                <div class="uCEN-hd">
                    <span class="uCEN-tit">您捐赠的项目列表</span>
                    <dl class="select">
                        <dt class="sel-checked"> 
                            <input class="sel-val" v="0" type="text" value="全部" readonly/>
                            <span class="triangle"><i class="triangle-up"></i></span> 
                            <input id="sel-checked" type="hidden" value="0"/>
                        </dt>
                        <dd class="sel-options">
                            <a class="sel-option sel-check">全部</a>
                            <a class="sel-option">三个月内</a>
                            <a class="sel-option">半年内</a>
                            <a class="sel-option">一年内</a>
                        </dd>
                    </dl>
                </div>
                <div class="uCEN-bd">
                    <div class="list"> 
                         <ul class="list-hd">
                            <li class="lst-col1">项目名称</li>
                            <li class="lst-col2">捐赠时间</li>
                            <li class="lst-col3">捐赠金额</li>
                            <li class="lst-col4">捐款进度</li>
                            <li class="lst-col5">项目反馈</li>
                         </ul>
                         <div id="list-bd" class="list-bd">
                            <div class="prompt"><div class="loading"></div></div>
                         </div>
                       
                    </div> 
                </div> 
            </div> -->
        </div>
    </div> 
</div> 
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/use_donation.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
