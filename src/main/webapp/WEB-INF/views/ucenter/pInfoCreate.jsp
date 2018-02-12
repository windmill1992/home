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
                <div class="leprim">实名认证成功！您还可以完善所在公司信息，加入企业善员工，一起行善</div>
                <div class="rlgroup">
                        <label>个人头像：</label>
                        <div class="controls controlsn">
                            <div class="rlinput">
                               <a class="entLogo" id="entLogo">
                                 <img src="${user.coverImageUrl}" data="${user.coverImageId}" alt="">
                               头像上传 
								 <div class="entfileLogo">
                                     <form  id="formLogo" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                         <input type="file" name="file" hidefocus="true" id="imgFileLogo" class="file-input">
                                         <input type="hidden" name="type" id="type" value="3"/>
                                     </form>
                               		 </div>
							   </a>
                               <span class="entLogoText">* 图片尺寸要求300X300px,大小要求2M以内
  支持jpg、jpeg、png、bmp</span>
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
                            <div class="rlinput">
                               <select class="itemselect" id="companyId">
                                    <c:if test="${companys!=null}">
                                      <c:forEach items="${companys}" var="company" >
									     <option tel="${company.id}">${company.userName}</option>
									  </c:forEach>
                                    </c:if>
                                    <option tel="0"  selected="selected">其它</option>
                                  </select>
                                  <input type="text" id="companyName" class="itemtext textmoney"  placeholder="输入正确公司名称，以便系统匹配至该企业善员工">
                               </div>
                            </div>
                            
                        </div>
                    
                    <div class="rlgroup">
                            <label>公司地点：</label>
                            <div class="controls controlsn leAdress">
                                <div class="rlinput">
                                   <select name="province_id" id="province"></select>
                                    <select class="city" id="city"></select>
                                    <select class="area" id="county"></select>                                    
                                </div>
                                <div class="leDetail">
                                        <input type="text" id="detailAddress" class="itemtext textmoney">
                                </div>
                                
                            </div>
                        </div>
                        <div class="rlitem rlitemUrl mt15">
                            <label>职位：</label>
                            <div class="rltext">
                                <div class="rlinput">
                                    <input type="text" value="" id="postion" placeholder=""/>
                                </div>
                               
                            </div>
                       </div>
                       <div class="rlitem">
                            <label></label>
                            <div class="rltext">
                                <input type="button" class="rlbtnok yh" id="rlbokbtn" value="提交">
                            </div>
                     </div>
             	</div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/my_Legalize.js?v=20160307" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>