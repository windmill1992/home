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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entLegalize.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter mygood"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../common/eleft_menu.jsp"%>
            </div>
            <div class="uCEN-R"> 
                <div class="entInfo">
                    <h2>完善公司信息<span>（完善公司信息）</span><a  id="entExplain" class="more">什么是企业助善</a></h2>
                     <div class="rlgroup">
                        <label>公司名称：</label>
                        <div class="controls controlsn">
                            <div class="rlinput font14">
                              ${company.name}
                            </div>
                        </div>
                    </div>
                    <div class="rlgroup">
                        <label>公司头像：</label>
                        <div class="controls controlsn">
                            <div class="rlinput">
                               <a class="entLogo" id="pic1">头像上传 
                               <img src="${company.coverImageUrl}" data="${company.coverImageId}" alt="">
								 <div class="entfileLogo">
                                     <form  id="formLogo" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                         <input type="file" name="file" hidefocus="true" id="imgFileLogo" class="file-input">
                                         <input type="hidden" name="type" id="type" value="7"/>
                                     </form>
                               		 </div>
							   </a>
                               <span class="entLogoText">* 图片尺寸要求300X300px,大小要求2M以内支持jpg、jpeg、png、bmp</span>
                            </div>
                            
                        </div>
                    </div>
                    <div class="rlgroup">
                        <label>公司LOGO：</label>
                        <div class="controls controlsn">
                            <div class="rlinput">
                            	<a class="entfileBtn" id="pic2">
                            	  <img src="${company.conpanyImageUrl}" data="${company.conpanyImageId}"  alt="">
                                	公司LOGO上传<br>(企业助善区)
                                     <div class="entfile">
                               		 <form  id="formFile" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                          <input type="file" name="file" hidefocus="true" id="imgFileFile" class="file-input">
                                          <input type="hidden" name="type" id="type" value="7"/>
                                     </form>
                               		 </div>
                                </a>
                              	<div class="entFileText">* 图片要求白底或透明底，尺寸要求600X100px，大小要求2M以内,支持jpg、jpeg、png、bmp</div>
                            </div>
                            
                        </div>
                    </div>
                    <div class="rlitem rlitemUrl mt15">
                            <label>网址：</label>
                            <div class="rltext">
                                <div class="rlinput">
                                    <input type="text" value="${company.url}" id="url" placeholder="http://"/>
                                </div>
                               
                            </div>
                    </div>
                    <div class="rlgroup">
                        <label>公司简介：</label>
                        <div class="controls controlsn conborderbom">
                            <div class="rlinput">
                                <textarea class="info" id="info" placeholder="请输入150字内公司简介，参加企业助善活动后，会显示此简介。">${company.infomation}</textarea>
                            </div>
                           
                        </div>
                    </div>
                    <div class="rlitem">
                        <label></label>
                        <div class="rltext">
                            <input type="button" class="rlbtnok yh" id="entSubmit"  value="提交"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
 <input type="hidden" id="vid"  value="${company.id}"/>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/entInfo.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>