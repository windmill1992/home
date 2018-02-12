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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp"%>
<div class="bodyer userCenter legalizeOk">
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
            	<div class="uCEN-hd">
                    <span class="uCEN-tit">实名认证</span>
                </div>
                <div class="leprim"></div>
                <div class="rlgroup">
                        <label>个人头像：</label>
                        <div class="controls controlsn">
                            <div class="rlinput">
                               <a class="entLogo" id="entLogo">
                               <img src="${user.coverImageUrl}" data="${user.coverImageId}" alt="">
                               头像上传 
								 <div class="entfileLogo">
				                     <form  id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
				                           <input type="file" name="file" hidefocus="true" id="imgFileLogo" class="file-input">
				                           <input type="hidden" name="type" id="type" value="3"/>
                                     </form>
                               		 </div>
							   </a>
                               
                            </div>
                            
                        </div>
                    </div>
                <div class="uCEN-hd mt50">
                    <span class="uCEN-tit">完善所属公司信息</span>
                </div>
                <div class="PerfectInfo">
                	<div class="rlgroup">
                        <label>公司名称：</label>
                        <div class="controls controlsn">
                            <div class="rlinputText">
                              ${employee.companyName}
                              <c:choose>
                                 <c:when test="${cState==0}">
                                 <span class="rlTextPrim">您所属的企业还未加入善园基金会，<a id="pInvitation" title="" data="${employee.companyName}|${companyId}">我去邀请</a>，一起行善</span>
                                 </c:when>
                                  <c:when test="${cState==1}">
                                  <span class="rlTextPrim">您所述的企业还未认证。<a id="remind" title="" data="${employee.companyName}|${companyId}">我去提醒</a>，一起行善</span>
                                 </c:when>
                                  <c:when test="${cState==2}">
                                   <span class="rlTextPrim">您所属的企业已通过认证。<a  title="" >一起行善</a>，让爱与温暖携手共进。</span>
                                 </c:when>
                                 <c:otherwise>
                                 
                                 </c:otherwise>
                              </c:choose>
						       </div>
                            </div>
                            <input type="hidden" id="company" data=""/>
                        </div>
                    
                    <div class="rlgroup">
                            <label>公司地点：</label>
                            <div class="controls controlsn leAdress">
                                <div class="rlinputText">
                                     ${employee.province}${employee.address}
                                </div>
                            </div>
                        </div>
                        <div class="rlgroup">
                        <label>职位：</label>
                        <div class="controls controlsn">
                            <div class="rlinputText">
                              ${employee.position}
                            </div>
                        </div>
                            
                        </div>
                       <div class="rlitem">
                            <label></label>
                            <div class="rltext">
                                <a href="<%=resource_url%>/enterprise/core/pInfoUpdate.do" class="rlbtnok yh" id="">修改</a>
                                <span class="rgray">|</span>
                                <a href="<%=resource_url%>project/index.do" title="" class="entlink">行善大厅</a>
                            </div>
                     </div>
             	</div>
            </div>
        </div>
    </div> 
</div>

<%@ include file="./../common/newfooter.jsp" %>
<input id="pprim" type="hidden" data="SDFSGBDGSS|5" value=''>
<input id="projectId" type="hidden" value="52">
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<!-- <script type="text/javascript" src="http://static.bshare.cn/b/buttonLite.js#uuid=48224106-d04a-4cbc-aecb-132a3f608fda&style=-1"></script> -->

<script data-main="<%=resource_url%>res/js/dev/my_Legalize.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>