<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网 - 开票详情</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res//css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res//css/dev/todogo.css" />
</head>
<body>
<!--头部 start-->
<%@ include file="./../../common/newhead.jsp" %>
<!--头部 end-->

<!--主体 start-->
<div class="bodyer userCenter">
    <div class="page">
        <div class="page-bd funlist_main">
            <div class="page-bd funlist_main">
            <div class="uCEN-L funlist_left">
                <div class="uCEN-L">
					<c:choose>
					<c:when test="${user.userType=='enterpriseUsers'}">
		         		<%@ include file="./../../common/eleft_menu.jsp"%>
		         	</c:when>
		         	<c:otherwise>
		         		<%@ include file="./../../common/pleft_menu.jsp"%>
		         	</c:otherwise>
		         	</c:choose>
				</div>
            </div>
        	</div>
        </div>
        <!-- left end -->
        <!--right start-->
        <div class="uCEN-R invoice_right">
            <div class="uCEN-hd uCEN-hd1">
                <span class="uCEN-tit">开票详情</span>
            </div>
            <div class="invoiceDetail">
                <ul>
                    <li>
                        <label>快递公司</label>
                        <span id='mailCompany'>${invoice.mailCompany }</span>
                    </li>
                    <li>
                        <label>快递单号</label>
                        <span id='mailCode'>${invoice.mailCode }</span>
                    </li>
                    <li>
                        <label>快递费用</label>
                        <span id='mailAmount'>
                        <c:if test="${invoice.mailAmount != null && invoice.mailAmount != ''}"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${invoice.mailAmount }"/>元</c:if>
                        <c:if test="${invoice.mailAmount == 0}">包邮</c:if></span>
                    </li>
                </ul>
                <ul>
                    <li>
                        <label>提交时间</label>
                        <span id='lastUpdateTime'><fmt:formatDate value="${invoice.createTime }" pattern="yyyy-MM-dd"/></span>
                    </li>
                    <li>
                        <label>开票抬头</label>
                        <span id='invoiceHead'>${invoice.invoiceHead }</span>
                    </li>
                    <li>
                        <label>开票内容</label>
                        <span id='content'>爱心捐助</span>
                    </li>
                    <li>
                        <label>开票金额</label>
                        <span><i style="color: #f97b00;" id='invoiceAmount'><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${invoice.invoiceAmount }"/></i>元</span>
                    </li>
                    <li>
                        <label>开票张数</label>
                        <span>1张</span>
                    </li>
                </ul>
                <ul>
                    <li>
                        <label>收件人名</label>
                        <span id='name'>${userAddress.name }</span>
                    </li>
                    <li>
                        <label>收件电话</label>
                        <span id='mobile2'>${userAddress.mobile }</span>
                    </li>
                    <li>
                        <label>收件地址</label>
                        <span id='address2'>${userAddress.province }${userAddress.city }${userAddress.area }${userAddress.detailAddress }</span>
                    </li>
                </ul>
            </div>
            <ul class="donateList">
            	<c:forEach var="list" items="${list}">
            		<li>为"<span>${list.projectTitle }</span>" 捐助了<i class="sum"><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${list.donatAmount}"/></i>元</p></li>
            	</c:forEach>
            </ul>
            <a href="<%=resource_url%>ucenter/getInvoicList.do" class="backBtn">返回开票记录</a>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../../common/newfooter.jsp" %>
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/invoiceList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
