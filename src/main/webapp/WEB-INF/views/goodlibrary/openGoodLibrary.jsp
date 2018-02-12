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
<title>善库</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<%-- <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNewlunbo.css"> --%>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/goodlibrary/good_librarypc.css" />
</head> 
<!--头部 end-->
<!--主体 start-->
<body>
<%@ include file="./../common/newhead.jsp" %><!-- <div class="bodyer userCenter mygood"></div>  -->
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
            <div class="library">
                <p class="libraryone">开通善库</p>
                <p class="librarytwo">“善库”是指一个群体的行善数据库；可以以公司或者家庭的名义发起，发起方及成员可以不定期为善库存入资金，善库成员可以使用善库中的资金并选择项目进行捐赠，捐赠记录会记录在善库</p>
            </div>
            <div class="librarythree">
                <div class="rlitem">
                    <label>善库名称：</label>
                    <div class="rltext">
                        <input type="text" placeholder="请输入公司或者家庭的名称" class="inputtext inputcode" id="nickName" value="${goodlibrary.nickName }">
                        <span class="wrinfo"></span>
                    </div>
                </div>
                <div class="rlitem">
                    <label>行善金额：</label>
                    <div class="rltext">
                        <input type="number" placeholder="所有善库成员都可以使用该资金，不低于100元" class="inputtext inputcode" id="balance" value="${goodlibrary.balance }">
                        <span class="wrinfo"></span>
                    </div>
                </div>
                <div class="rlitem rlitem1">
                    <label>简介：</label>
                    <div class="rltext">
                        <textarea class="personFr1" placeholder="简单介绍下公司或者家庭情况（选填）" id="introduction" value="${goodlibrary.introduction }">${goodlibrary.introduction }</textarea>
                        <span class="wrinfo"></span>
                    </div>
                </div>
                <div class="rlitem">
                    <label>所在地：</label>
                    <div class="rltext">
                        <div class="rlitem data_o main">
                            <select id="province" class="prov_city"><option value="省份">省份</option></select>
                            <select id="city" class="prov_city"><option value="地级市">地级市</option></select>
                            <select id="county" class="prov_city"><option value="区/县级市">区/县级市</option></select>
                        </div>
                    </div>
                </div>
                <div class="rlitem rlitem1">
                    <label>上传头像：</label>
                    <div class="rltext">
                        <span class="value">
                             <form id="form" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                                 <div class="add_list" id="imgList">
                                 	<c:choose>
                                 		<c:when test="${goodlibrary.logoUrl!=null&&goodlibrary.logoUrl!='' }">
                                 			<div class="item add">
                         						<i id="img">×</i>
                         						<img src="${goodlibrary.logoUrl }" id="${goodlibrary.logoId }" style="width:80px;height: 80px" class="io" id="io"/>
                         					</div>
                                 		</c:when>
                                 		<c:otherwise>
                                 			<div class="item add">
                                         	<a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile" class="file-input">
                                            <input type="hidden" name="type" id="type" value="4"></a>
                                     </div>
                                 		</c:otherwise>
                                 	</c:choose>
                                 </div>
                             </form>
                        </span>
                    </div>
                </div>
                <a href="javascript:void(0);" id="openGoodLibrary_button"><div class="rltext "><p class="data_button">开通善库</p></div></a>
            </div>
        </div>

    </div>
    </div>
<%@ include file="./../common/newfooter.jsp" %>
<input type="hidden" id="goodlibraryId" value="${goodlibrary.id }"/> 
<script data-main="<%=resource_url%>res/js/dev/goodlibrary/openGoodLibrary.js?v=1" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>