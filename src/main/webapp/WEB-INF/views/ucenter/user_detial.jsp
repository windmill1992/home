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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/password.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/my_data.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/select2.css" />
<%-- <script type="text/javascript" src="<%=resource_url%>res/js/util/location.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/util/select2.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/util/select2_locale_zh-CN.js"></script>
<script type="text/javascript" src="<%=resource_url%>res/js/util/jQuery.js"></script> --%>
</head>
<body>
	<%@ include file="./../common/newhead.jsp"%>
	<div class="bodyer userCenter updataPwd">
		<div class="page">
			<div class="page-bd">
				<c:choose>
				    <c:when test="${user.userType=='enterpriseUsers'}">
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
            	<div class="uCEN-R" id="before_update" style="display:none">
        			<div class="uCEN-hd uCEN-yh">
            			<span class="uCEN-tit">用户中心</span>
        			</div>
        			<div class="data password">
            		<div class="data_left pwCon">
                		<div class="rlitem data_o">
                    		<label><span class="qth">*</span>用户名：</label>
                    		<div class="rltext">
                    			<c:choose>
                    				<c:when test="${fn:startsWith(user.userName,'weixin')}"><input type="text" class="inputtext inputcode datainput" placeholder="" id="userName" value="${user.userName }"></c:when>
                    				<c:when test="${fn:startsWith(user.userName,'游客')}"><input type="text" class="inputtext inputcode datainput" placeholder="" id="userName" value="${user.userName }"></c:when>
                    				<c:otherwise><em type="text" class="datainput data_em">${user.userName }</em></c:otherwise>
                    			</c:choose>
                    			<%-- <c:if test="${fn:startsWith(user.userName,'weixin')}">
                    				<input type="text" class="inputtext inputcode datainput" placeholder="" id="userName" value="${user.userName }">
                    			</c:if>
                    			<c:if test="${!fn:startsWith(user.userName,'weixin')}">
                    				<em type="text" class="datainput data_em">${user.userName }</em>
                        		</c:if> --%>
                        		<span class="wrinfo wrinfo1"></span>
                    		</div>
                		</div>
                		<c:if test="${fn:startsWith(user.userName,'weixin')}">
               			<div class="rlitem data_o">
                    		<label><span class="qth">*</span>密码：</label>
                    		<div class="rltext">
                        		<input type="password" class="inputtext inputcode datainput" placeholder="" id="password1" >
                        		<span class="wrinfo wrinfo2"></span>
                    		</div>
                		</div>
                		<div class="rlitem data_o">
                    		<label><span class="qth">*</span>确认密码：</label>
                    		<div class="rltext">
                        		<input type="password" class="inputtext inputcode datainput" placeholder="" id="password2">
                        		<span class="wrinfo wrinfo3"></span>
                    		</div>
                		</div></c:if>
                		<div class="rlitem data_o">
                    		<label><span class="qth">*</span>昵称：</label>
                    		<div class="rltext">
                        		<input type="text" class="inputtext inputcode datainput" placeholder="" id="petname" value="${user.nickName }">
                        		<span class="wrinfo wrinfo4"></span>
                    		</div>
                		</div>
                		<div class="rlitem data_o">
                    		<label>职业：</label>
                    		<div class="rltext">
                        		<input type="text" class="inputtext inputcode datainput" placeholder="" id="profession" value="${user.persition }">
                        		<span class="wrinfo wrinfo5"></span>
                    		</div>
                		</div>
                		<div class="rlitem data_o">
                    		<label>手机号：</label>
                    		<div class="rltext">
                        		<input type="text" class="inputtext inputcode datainput" placeholder="" id="telnumber" value="${user.mobileNum }">
                        		<span class="wrinfo wrinfo5"></span>
                    		</div>
                		</div>
                		<div class="rlitem data_o">
                    		<label>QQ/微信：</label>
                    		<div class="rltext">
                        		<input type="text" class="inputtext inputcode datainput" placeholder="" id="wxnumber" value="${user.qqOrWx }">
                        		<span class="wrinfo wrinfo6"></span>
                    		</div>
                		</div>
                		<div class="rlitem data_o main">
                    		<label>所在地：</label>
                    		<select id="province" style="margin-left: 10px;" class="prov_city"><option value="所在省">所在省</option></select>
                        	<select id="city" style=" margin-left: 10px" class="prov_city"><option value="地区">地区</option></select>
                        	<select id="county"  style="margin-left: 10px" class="prov_city"><option value="县/市/区">县/市/区</option></select>
                		</div>
                		<div class="rlitem data_o">
                    		<label>详细地址：</label>
                    		<div class="rltext">
                        		<input type="text" class="inputtext inputcode datainput" placeholder="" id="detailAddress" value="${user.familyAddress }">
                        		<span class="wrinfo wrinfo7"></span>
                    		</div>
               			</div>
                		<div class="rlitem data_o">
                    		<label>工作单位：</label>
                    		<div class="rltext">
                        		<input type="text" class="inputtext inputcode datainput" placeholder="" id="employer" value="${user.workUnit }">
                        		<span class="wrinfo wrinfo8"></span>
                    		</div>
                		</div>
                		<div class="rlitem data_o data_t">
                    		<a href="javaScript:void(0)"><div class="rltext"><p class="data_button" id="dataButton">保存</p></div></a>
                		</div>
        			</div>
        		 	</div>
        		</div>
        		
        		<div class="uCEN-R" id="after_update" style="display:none">
			        <div class="uCEN-hd uCEN-yh">
			            <span class="uCEN-tit">用户中心</span>
			        </div>
			        <div class="data password">
			            <div class="data_left pwCon">
			                <div class="rlitem data_o">
			                    <label>用户名：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.userName }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>昵称：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.nickName }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>职业：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.persition }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>手机号：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.mobileNum }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>QQ/微信：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.qqOrWx }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>所在地：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">
			                        ${user.province } <c:if test="${user.city != user.province}">${user.city}</c:if>  ${user.area }
			                        </em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>详细地址：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.familyAddress }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o">
			                    <label>工作单位：</label>
			                    <div class="rltext">
			                        <em type="text" class="datainput data_em">${user.workUnit }</em>
			                    </div>
			                </div>
			                <div class="rlitem data_o data_t">
			                    <a href="javascript:void(0);" id="editorUser"><div class="rltext "><p class="data_button">编辑信息</p></div></a>
			                </div>
			            </div>
			        </div>
			    </div>
        		
        	</div>
    	</div> 
	</div>
<input type="hidden" id="uName" value="${user.userName }">
<input type="hidden" value="${user.hj_area}" id="addressNum"/>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/user_detail.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
