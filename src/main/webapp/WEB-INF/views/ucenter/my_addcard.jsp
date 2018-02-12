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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/cashManage.css"/>
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
            <div class="uCEN-R">
            	<!----资金管理-///---> 
                <div class="setting_account">
                	<div class="prompt_tip"><span>您还未设置提现帐号，请先填写收款帐号。</span></div>
                    <div class="setting_form">
                    	<ul class="switch_tab">
                        	<li class="tab cur" tel="1" >个人账户</li>
                            <li class="tab" tel="0">对公账户</li>
                        </ul>
                        <ul class="form_box">
                        	<li><span class="label" id="bankLabel">开户名：</span><span class="value"><input type="text"  id = "name" size="20"><i class="tip" id ="prompt">开户名必须和求助者或发起者一致</i></span></li>
                            <li><span class="label">开户行：</span><span class="value">
                            <input type="text" id="bankname" size="20"></span></li>
                            <li><span class="label">卡号：</span><span class="value"><input type="text" id="cardNo" size="30"></span></li>
                            <li><span class="label">开户行所在地：</span><span class="value">
                              <label for="select"></label>
                              <select name="select" id="province">
                                <option>浙江</option>
                              </select>
                              省
                              <select name="select2" id="city">
                                <option>金华</option>
                              </select>
                              市
                              <select name="select3" id="county">
                                <option>婺城</option>
                              </select>
                              区/县
                            </span></li>
                            <li class="btn_line"><a class="add_btn">添加</a></li>
                        </ul>
                    </div>                
                </div>
            </div>
        </div>
    </div> 
</div>
<input type="hidden"  id ="cashNo" value ="1"/>
<input type="hidden" id="projectId" value="${projectId}">
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/cash_manage.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
