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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css?v=20180209" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css?v=20180209" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/password.css?v=20180209" />
</head>
<body>
	<%@ include file="./../common/newhead.jsp"%>
	<input type="hidden" value="${error}" name="msg2" id="msg2">
	<input type="hidden" value="${state}" name="state" id="state">
	<div class="bodyer userCenter updataPwd">
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
                <div class="password">
                    <div class="pwCon">
                        <div class="rlitem">
                            <label>旧的登录密码：</label>
                            <div class="rltext">
                                <input type="password" class="inputtext inputcode" id="oPw" >
                            </div>
                        </div>
                        <div class="rlitem">
                            <label>新的登录密码：</label>
                            <div class="rltext">
                                <input type="password" class="inputtext inputcode" id="uPw" >
                                <span class="wrinfo"></span>
                            </div>
                        </div>
                        <div class="rlitem">
                            <label>确认新的登录密码：</label>
                            <div class="rltext">
                                <input type="password" class="inputtext inputcode" id="cPw" >
                                <span class="wrinfo"></span>
                            </div>
                        </div>
                        <div class="rlitem">
                            <label>您的手机号：</label>
                            <div class="rltext">
                                <input type="text" class="inputtext inputcode" id="phone" value="" >
                                <a href="javascript:" class="sendPhone" id="pSend">获取验证码</a>
                                <a href="javascript:;" id="pSend1"></a>
                            </div>
                        </div>
                        <div class="rlitem">
                            <label>手机验证码：</label>
                            <div class="rltext">
                                <input type="number" class="inputtext inputcode" id="mcode" readonly="readonly" >
                                <span class="wrinfo"></span>
                            </div>
                        </div>
                        <div class="rlitem">
                            <div class="rltext">
                            	<a href="javascript:;" class="nextbtn" id="updataPawBtn">修改完成</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="dialog" id="yzmDialog">
			<div class="mask"></div>
			<div class="dialog_inner">
				<h3>请输入验证码</h3>
				<div class="yzm-pic">
					<img src="" id="codepic"/>
					<a href="javascript:void(0);" class="refreshCode"></a>
					<input type="number" name="yzmCode" id="yzmCode" maxlength="4" value="" />
				</div>
				<div class="close">
					<a href="javascript:void(0);" class="close-a left" id="cancel">取消</a>
					<a href="javascript:void(0);" class="close-a right" id="sure">确定</a>
				</div>
			</div>
		</div>
    </div> 
</div>
<div id="msg" class="cue2"></div>
<input type="hidden" id="phoneNum" value="${user.mobileNum}" />
<input type="hidden" id="newpsPage" value="" />
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/password.js?v=20180209" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
