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
<title>项目发布</title>
<%-- <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/> --%>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/newReleaseProject/releaseProjectpc.css">
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head>
<body style="background: #f7f7f7">
<!--头部 start-->
<%@ include file="./../common/newhead.jsp" %>
<!--头部 end-->

<!--主体 start-->
<div class="relProject">

    <div class="relImg relImg1"></div>
    <p class="relnav">信息完善</p>
    <!--我是个人-->
    <div class="personRelease">
        <div class="personNav">
            <div class="personFl">发起人身份：</div>
            <div class="personFr">
                <a href="#"><div class="geren navgg">我是个人</div></a>
                <a href="#"><div class="jigou">我代表机构/组织</div></a>
            </div>
        </div>
        <div class="wsgr">
        <div class="personNav">
            <div class="personFl">您的姓名：</div>
            <div class="personFr">
               <input type="text" class="personInp" id="userName" value="${user.realName }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">请输入身份证号：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="idCard" value="${user.idCard }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">请输入手机号：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="MobileNum" value="${user.mobileNum }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">请输入验证码：</div>
            <div class="personFr">
                <div class="personYz">
                    <input type="text" class="personInp1" id="personalVerCode">
                    <a href="javaScript:void();" class="yzm" id="usercode">发送验证码</a>
                </div>
            </div>
        </div>
        <!--上传照片-->
        <div class="personNav1">
            <div class="personFl">请上传手持身份证照片：</div>
            <div class="personFr">
                <span class="value">
                 <form id="form1" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                     <div class="add_list" id="imgList1">
                     <c:choose>
         				<c:when test="${userimg1==null||userimg1=='' }">
                         <div class="item add">
                             <a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile1" class="file-input"><input type="hidden" name="type" id="type" value="4"></a>
                             <span>身份证正面</span>
                         </div>
                         </c:when>
                         <c:otherwise>
                         	<div class="item add">
                         		<i id="img1">×</i>
                         		<img src="${userimg1 }" id="${userimgid1 }" class="io1" name="io1"/>
                             	<span>身份证正面</span>
                         	</div>
                         </c:otherwise>
                     </c:choose>
                     </div>
                 </form>
                </span>

    <span class="value">
    <form id="form2" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
    <div class="add_list" id="imgList2">
    <c:choose>
        <c:when test="${userimg2==null||userimg2=='' }">
            <div class="item add">
            <a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile2" class="file-input"><input type="hidden" name="type" id="type" value="4"></a>
            <span>身份证反面</span>
            </div>
        </c:when>
        <c:otherwise>
            <div class="item add">
            <i id="img2">×</i>
            <img src="${userimg2 }" id="${userimgid2 }" class="io2" name="io2"/>
            <span>身份证反面</span>
            </div>
        </c:otherwise>
    </c:choose>
    </div>
    </form>
    </span>

    <span class="value">
    <form id="form3" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
    <div class="add_list" id="imgList3">
    <c:choose>
        <c:when test="${userimg3==null||userimg3=='' }">
            <div class="item add">
            <a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile3" class="file-input"><input type="hidden" name="type" id="type" value="4"></a>
            <span>手持身份证</span>
            </div>
        </c:when>
        <c:otherwise>
            <div class="item add">
            <i id="img3">×</i>
            <img src="${userimg3 }" id="${userimgid3 }" class="io3" name="io3"/>
            <span>手持身份证</span>
            </div>
        </c:otherwise>
    </c:choose>
    </div>
    </form>
    </span>

    <span class="value">
    <div class="add_list" id="imgList">
    <div class="item add">
    <div class="imgCon2"><img src="<%=resource_url%>res/images/newReleaseProject/shili.jpg"><span>点击查看示例</span></div>
    </div>
    </div>
    </span>
    </div>
        </div>
        </div>
    </div>

    <!--我是机构-->
    <div class="wsjg"style="display: none">
        <div class="personNav">
            <div class="personFl">名称：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="name" value="${company.name }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">组织机构代码：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="groupCode" value="${company.groupCode }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">地址：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="address" value="${company.address }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">联系人：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="head" value="${company.head }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">联系电话：</div>
            <div class="personFr">
                <input type="text" class="personInp" id="mobile" value="${company.mobile }">
            </div>
        </div>
        <div class="personNav">
            <div class="personFl">请输入验证码：</div>
            <div class="personFr">
                <div class="personYz">
                    <input type="text" class="personInp1" id="teamVerCode">
                    <a href="javascript:void();" class="yzm" id="sendTeamVerCode">发送验证码</a>
                </div>
            </div>
        </div>
        <!--上传照片-->
        <div class="personNav1">
            <div class="personFl">单位资质证明：</div>
            <div class="personFr">
                <span class="value">
                 <form id="form4" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                     <div class="add_list" id="imgList4">
                     	<c:choose>
                     		<c:when test="${companyimg1==null||companyimg1=='' }">
                     			<div class="item add">
                             		<a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile4" class="file-input"><input type="hidden" name="type" id="type" value="4"></a>
                             		<span>机构证件</span>
                         		</div>
                     		</c:when>
                     		<c:otherwise>
                     			<div class="item add">
                     				<i id="img4">×</i>
                         			<img src="${companyimg1 }" id="${companyimgid1 }" class="io4" name="io4"/>
                             		<span>机构证件</span>
                             	</div>
                     		</c:otherwise>
                     	</c:choose>
                     </div>
                 </form>
                </span>
                <span class="value">
                 <form id="form5" action="http://www.17xs.org/file/upload3.do" method="post" enctype="multipart/form-data">
                     <div class="add_list" id="imgList5">
                     <c:choose>
                     		<c:when test="${companyimg2==null||companyimg2=='' }">
                     			<div class="item add">
                             		<a class="add_btn">+<input type="file" name="file" hidefocus="true" id="imgFile5" class="file-input"><input type="hidden" name="type" id="type" value="4"></a>
                             		<span>单位授权书</span>
                         		</div>
                     		</c:when>
                     		<c:otherwise>
                     			<div class="item add">
                     				<i id="img5">×</i>
                         			<img src="${companyimg2 }" id="${companyimgid2 }" class="io5" name="io5"/>
                             		<span>单位授权书</span>
                             	</div>
                     		</c:otherwise>
                     	</c:choose>
                         
                     </div>
                 </form>
                </span>
                <span class="value">
                     <div class="add_list" id="imgList7">
                         <div class="item add">
                             <div class="imgCon2"><img src="<%=resource_url%>res/images/newReleaseProject/shili.jpg"><span>点击查看示例</span></div>
                         </div>
                     </div>
                </span>
            </div>
        </div>
    </div>
    <div style="clear: both"></div>
    <div class="foot"><a href="javaScript:void (0);" class="footBtn" id="footButtom">确认，下一步</a></div>
</div>
<div class="cue2" style="display: none;" id="msg"><p>请输入姓名！</p></div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->
<input id="typestate" type="hidden" value="0" />
<script data-main="<%=resource_url%>res/js/dev/newReleaseProject/releaseProjectpc.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
