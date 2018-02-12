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
<title>善园网 - 索取发票</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css" />
</head>
<body>
<!--头部 start-->
<%@ include file="./../../common/newhead.jsp" %>
<!--头部 end-->

<!--主体 start-->
<div class="bodyer userCenter">
    <div class="page">
   <%--  <%@ include file="./../../common/model1.jsp"%> --%>
    <!-- left start -->
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
        <!-- left end -->
        <!--right start-->
        <div class="uCEN-R invoice_right">
            <div class="uCEN-hd uCEN-hd1">
                <span class="uCEN-tit">索取发票</span>
                <div class="bindBox">
                    <ul class="bindList">
                        <li><a class="mobile"></a></li>
                        <li><a class="email"></a></li>
                        <li><a class="qq"></a></li>
                        <li><a class="wx"></a></li>
                        <li><a class="wb"></a></li>
                        <li><a class="zfb"></a></li>
                    </ul>
                    <span>(绑定账号更方便了解捐助信息)</span>
                </div>
            </div>
            <div class="">
                <table width="100%" class="uCenterTable" id="listShow">
                    <colgroup>
                        <col width="15%"></col>
                        <col width="25%"></col>
                        <col width="16%"></col>
                        <col width="16%"></col>
                        <col width="28%"></col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th><input type="checkbox" id="checkAll"name="" style="margin:0px 20px;">全选</th>
                            <th>捐助项目</th>
                            <th>捐助金额</th>
                            <th>捐助时间</th>
                            <th>订单号</th>
                        </tr>
                    </thead>
                    <tbody id="listShow">
                        <!-- 数据 -->
                    </tbody>
                    
                </table>
                <!-- 分页调用原先的 start -->
                <div class="panigation" style="height: 75px;"></div>
                <!-- 分页 end -->
            </div>
            <div>
                <div class="uCEN-hd uCEN-hd1" style="border-top: 1px solid #e0e0e0;">
                    <span class="uCEN-tit">邮寄地址</span>
                    <div class="bindBox">
                        <span>（开票金额少于100元邮寄费用需爱心人士自行承担，造成不便敬请谅解）</span>
                    </div>
                    <c:if test="${address == null }"><a href="javascript:;" class="invoiceBtn" id="addAddress">添加收件地址</a></c:if>
                    <c:if test="${address != null }"><a href="javascript:;" class="invoiceBtn" id="editAddress">编辑收件地址</a></c:if>
                </div>
                <table width="100%" class="uCenterTable">
                    <colgroup>
                        <col width="21%"></col>
                        <col width="12%"></col>
                        <col width="14%"></col>
                        <col width="16%"></col>
                        <col width="35%"></col>
                        
                    </colgroup>
                    <thead>
                        <tr>
                            <th>发票抬头</th>
                            <th>开票金额</th>
                            <th>收件人</th>
                            <th>电话</th>
                            <th>收件地址</th>
                            <!-- <th></th> -->
                        </tr>
                    </thead>
                    <tbody>
                        <tr id="editAddr" style="display:none">
                            <td class="pl26"><input type="text" id="invoiceHead1" name="invoiceHead" value="" style="height:30px;"/></td>
                            <td class="sum" id="invoiceAmount1" name="invoiceAmount">0.00</td>
                            <td><input type="text" id="name" name="name" value="${address.name }" style="height:30px;"/></td>
                            <td><input type="text" id="mobile" name="mobile" value="${address.mobile }" style="height:30px;"/></td>
                            <td><input type="text" id="detailAddress" name="detailAddress" value="${address.province }${address.city }${address.area }${address.detailAddress }" style="height:30px;"/></td>
                            <!-- <td><a href="javascript:;" class="ui-btn saveBtn">保存</a><a href="javascript:;" class="ui-btn cancleBtn">取消</a></td> -->
                        </tr>
                        <tr id="addr">
                            <td class="text-left pl26"><input type="text" id="invoiceHead2" name="invoiceHead" value=""/></td>
                            <td class="sum" id="invoiceAmount2" name="invoiceAmount">0.00</td>
                            <td>${address.name }</td>
                            <td>${address.mobile }</td>
                            <td class="text-left">${address.province }${address.city }${address.area }${address.detailAddress }</td>
                            <!-- <td><a href="javascript:;" class="ui-btn selectBtn">选择</a><a href="javascript:;" class="ui-btn delBtn">删除</a></td> -->
                        </tr>
                    </tbody>
                </table>
                <a class="backBtn" id="confirm">确认提交</a>
            </div>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../../common/newfooter.jsp" %>
<input type="hidden" id="addressId" name="addressId" value="${address.id }">
<input type="hidden" id="info" name="info" value="">
<input type="hidden" id="totalMoney" name="totalMoney" value="">
<input type="hidden" id="donateInfo" name="donateInfo" value="">
<input type="hidden" id="flag" value="-1">
<!--底部 end-->
<script data-main="<%=resource_url%>res/js/dev/getInvoice.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
