<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport" />
<title>善园 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/todogo.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head>
<body>
	<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter"> 
	<div class="page">
        <div class="page-bd funlist_main">
            <div class="uCEN-L funlist_left">
                <div class="fun_profile">
        			<%@ include file="./../common/pleft_menu.jsp"%>
        		</div>  
            </div>
            <div class="uCEN-R wopro_right">
                <div class="wop_sona">
                    <div class="sona_left">
                        <div class="sona_head">
                        	<div class="sona_img" id="pic1">
							<div class="edit">编辑头像</div>
                        	<img src="${user.coverImageUrl}" width="71" height="71" />
                            <form id="formLogo" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                             <input type="file" name="file" hidefocus="true" id="imgFileLogo" class="file-input">
                             <input type="hidden" name="type" id="type" value="8">
                             </form>
                        	</div>
                        	<span><c:if test="${fn:length(user.userName)>=8 }">${fn:substring(user.userName,0,8)}...</c:if>
                        	<c:if test="${fn:length(user.userName)<8 }">${user.userName}</c:if>(个人)</span>
                        </div>
                    </div>
                    <div class="sona_right">
                        <div class="sona_mongy_ts">
                            <ul>
                                <li class="active"><h2><i><fmt:formatNumber value="${user.balance}" pattern="0.00"/></i>元</h2><p>剩余善款</p></li>
                                <li><h2><i><fmt:formatNumber value="${totalAmount}" pattern="0.00"/></i>元</h2><p>捐助金额</p></li>
                                <li><h2><i>${donationNum}</i>次</h2><p>行善次数</p></li>
                                <li><h2><i><fmt:formatNumber value="${tiqu}" pattern="0.00"/></i>元</h2><p>获捐金额</p></li>
                            </ul>
                            <div class="clearer"></div>
                        </div>
                    </div>
                </div>
                <div class="son_related">
                   <span>与我相关：</span><a href="<%=resource_url%>ucenter/core/donatelistByuser.do">我的求助（0）</a> 
                   			 <a href="<%=resource_url%>ucenter/core/focusProject.do">我的捐助（${fktotal}）</a>
                   			 <a href="<%=resource_url%>enterprise/core/enterpriseCharityDetail.do">我的助善（${zstotal}）</a>
                   			 <a href="<%=resource_url%>ucenter/core/msg.do">系统信息（0）</a>
                </div>
                
                
                <div class="son_informat">
                    <ul>
                        <li><span>用户名：</span><em>${user.userName}</em></li>
                         <c:choose>
					        <c:when test="${user.realState == 201}">
							   <li><span>姓名：</span><input style="line-height:40px" class="info_inp_text" id="realname" readonly="readonly" type="text" value="${user.realName}" /><span class="informat_ok">审核中</span></li>
							</c:when>
							<c:when test="${user.realState == 203}">
							  <li><span>姓名：</span><input style="line-height:40px" class="info_inp_text" id="realname" readonly="readonly" type="text" value="${user.realName}"/><span class="informat_ok">已认证</span></li>
							</c:when>
							<c:otherwise >
							   <li><span>姓名：</span><input style="line-height:40px" class="info_inp_text" id="realname" name="" type="text" value="${user.realName}" /><a href="<%=resource_url%>user/realname.do" title="" class="informat_btn">实名认证</a></li>
							</c:otherwise>
						</c:choose>
						<li><span>职业：</span><input style="line-height:40px" class="info_inp_text" id="persition"  type="text" value="${user.persition}" />
						<c:choose>
					        <c:when test="${user.loveState == 201}">
							   <span class="informat_ok">善管家审核中</span>
							</c:when>
							<c:when test="${user.loveState == 203}">
							  <span class="informat_ok">善管家已认证</span>
							</c:when>
							<c:otherwise >
							   <a href="<%=resource_url%>project/joinGood.do" title="" class="informat_btn">申请善管家</a>
							</c:otherwise>
						</c:choose>
						</li>
                        <li><span>手机号：</span><input style="line-height:40px" class="info_inp_text" id="mobileNum" value="${user.mobileNum}" type="text" /></li>
                        <li><span>QQ/微信：</span><input style="line-height:40px" class="info_inp_text" id="weixin" value="${user.qqOrWx}" type="text" /></li>
                        <li><span>所在地：</span>
                        <select class="wop_ad" id="province" ><option value="所在省">所在省</option></select>
                        <select class="wop_ad" id="city"><option value="地区">地区</option></select>
                        <select class="wop_ad" id="county"  ><option value="县/市/区">县/市/区</option></select></li>
                        <li><span>工作单位：</span><input style="line-height:40px" class="info_inp_text" id="workUnit"  value="${user.workUnit}" type="text" /></li>
                        <li><input class="wop_btn" name="" type="button" id="submitData" value="保存"></li>
                    </ul>
                </div>
                
            </div>
            
        </div>
    </div> 
</div>


<div class="clearer"></div>
	<input type="hidden" value="${user.hj_area}" id="addressNum"/>
	<input type="hidden" value="${page}" id="page"/>
	<%@ include file="./../common/newfooter.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/entcentral.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
