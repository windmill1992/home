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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/enterpris.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head>
<body>
	<%@ include file="./../common/newhead.jsp" %>
	<div class="bodyer userCenter"> 
	<div class="page">
        <div class="page-bd funlist_main">
            <div class="uCEN-L funlist_left">
                <div class="fun_profile">
        			<%@ include file="./../common/eleft_menu.jsp"%>
        		</div>  
            </div>
            <div class="enterprise_right">
    	<div class="enprise_cair">
        	<div class="prcai_left">
            	<div class="proe">
		            	<div class="sona_img" id="pic1">
							<div class="edit">编辑头像</div>
	                    	<img src="${user.coverImageUrl}" width="71" height="71" />
	                        <form id="formLogo" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
	                         <input type="file" name="file" hidefocus="true" id="imgFileLogo" class="file-input">
	                         <input type="hidden" name="type" id="type" value="8">
	                         </form>
                    	</div>
                    	<span><c:if test="${fn:length(user.userName)>=8}">${fn:substring(user.userName,0,8)}...</c:if>
                    	<c:if test="${fn:length(user.userName)<8}">${user.userName}</c:if>
                    	</span>
            	</div>
            </div>
            <div class="prcai_right">
           			 <ul>
                    	<li class="active"><h2><i><fmt:formatNumber value="${balance}" pattern="0.00"/></i>元</h2><p>剩余善款<a href="<%=resource_url%>ucenter/core/chargeonlineBank.do">充值</a></p></li>
                        <li><h2><i><fmt:formatNumber value="${totalAmount}" pattern="0.00"/></i>元</h2><p>捐助金额</p></li>
                        <li><h2><i>${donationNum}</i>次</h2><p>行善次数</p></li>
                        <li><h2><i><fmt:formatNumber value="${tiqu}" pattern="0.00"/></i>元</h2><p>获捐金额<a href="javascript:void(0)">提现</a></p></li>
                    </ul>
            
            </div>
            <div class="clearer"></div>
        </div>
			<div class="enprise_related">
				<span>与我相关：</span>
				<a href="<%=resource_url%>ucenter/core/donatelistByuser.do">我的项目（100）</a>
				<a href="<%=resource_url%>ucenter/core/focusProject.do">我的捐助（${dnList}）</a>
				<a href="<%=resource_url%>enterprise/core/enterpriseCharityDetail.do">我的助善（${agList}）</a> 
				<a href="<%=resource_url%>ucenter/core/msg.do">系统信息（${newsRead}）</a>
				<a href="<%=resource_url%>enterprise/core/employeList.do"> 善员工（${employee}）</a>
			</div>
			<c:choose>
	        <c:when test="${company.state == 203}">
	        	<!-- 成功 -->
			   <div class="enpri_conn">
			      		<div class="enp_cnn_ico"><i class="en_wsan"></i>恭喜您，您的机构认证已经审核通过</div>
			           <div class="enp_cnn_list">
			           	  <ul>
			               	   <li><span>单位名称：</span><em>${user.userName}</em></li>
			                   <li><span>地址：</span>${company.address}</li>
			                   <li><span>单位类型：</span>${entType}</li>
			                   <li><span>法人姓名：</span>${company.legalName}</li>
			                   <li><span>联系人：</span>${company.head}</li>
			                   <li><span>手机：</span>${company.mobile}</li>
			              </ul>
			           </div>
			      </div>
			</c:when>
			<c:when test="${company.state == 201}">
				<!--审核中 -->
			   <div class="enpri_conn">
			      		<div class="enp_cnn_ico"><i class="en_wsan"></i>您提交的申请已进入审核期</div>
			           <div class="enp_cnn_list">
			           	 <ul>
			               	   <li><span>单位名称：</span><em>${user.userName}</em></li>
			                   <li><span>地址：</span>${company.address}</li>
			                   <li><span>单位类型：</span>${entType}</li>
			                   <li><span>法人姓名：</span>${company.legalName}</li>
			                   <li><span>联系人：</span>${company.head}</li>
			                   <li><span>手机：</span>${company.mobile}</li>
			              </ul>
			           </div>
			      </div>
			</c:when>
			<c:otherwise >
			   <div class="enpri_conn">
			      		<div class="enp_cnn_ico"><i class="en_wsan"></i>您的机构还没有提交认证，请您完善资料</div>
			           <div class="enp_cnn_list">
			           	<ul>
			               	<li><span>单位名称：</span><em>${user.userName}</em><a class="inst_cat" href="http://www.17xs.org/enterprise/core/realName.do">机构认证</a></li>
			                   <li><span>地址：</span>未认证</li>
			                   <li><span>单位类型：</span>未认证</li>
			                   <li><span>法人姓名：</span>未认证</li>
			                   <li><span>联系人：</span>未认证</li>
			                   <li><span>手机：</span>未认证</li>
			               </ul>
			           </div>
			      </div>
			</c:otherwise>
		</c:choose>
    </div>
            
        </div>
    </div> 
</div>


<div class="clearer"></div>
	<input type="hidden" value="${page}" id="page"/>
	<%@ include file="./../common/newfooter.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/entcentral.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
