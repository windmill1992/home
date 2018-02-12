<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ include file="./../../common/file_url.jsp" %>
    <title>发起人身份验证</title>
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/releaseProject/detail_new.css">
    <link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/releaseProject/personTest.css">
</head>
<script type="text/javascript">
     function back(){
          window.location.href="javascript:history.go(-1)";
     }

</script>
<body>
<div class="detail_head">
    <div class="detailFl" onclick="back()">&lt;返回</div>
    <div class="detailconter">
        <img src="<%=resource_url%>res/images/releaseProject/det_log.png">
        <span>发起机构信息</span>
    </div>
    <a href="<%=resource_url%>test/gotoProtocolRelease.do">
	    <div class="detailfr">
	        <img src="<%=resource_url%>res/images/releaseProject/det_pr.png">
	        <span>发布</span>
	    </div>
	</a>
</div>
<div class="personTest">
   <%--  <div class="initTest">
        <img src="${company.coverImageUrl }">
        <span></span>
    </div> --%>
   <c:if test="${company.name != null}"> 
    <div class="person_one">
        <div class="perone_fl">发起方：<span>${company.name }</span></div>
    </div>
   </c:if>
    <div style="clear:both"></div>
    <div style="clear:both"></div>
   <c:if test="${company.head != null}"> 
	    <div class="person_two">
	        联系人：<span>${company.head}</span>
	    </div>
    </c:if>
    <c:if test="${company.mobile != null}"> 
	    <div class="person_two">
	        电话：<span>${company.mobile }</span>
	    </div>
    </c:if>
    <c:if test="${company.address != null}"> 
    <div class="person_two">
      	  单位地址：<span>${company.address }</span>
    </div>
   </c:if>
    <div class="person_one">
        <div class="perone_fl">相关资料：</div>
        <div class="perone_nx">
            <span>营业执照</span><div class="perone_fr"><c:choose><c:when test="${company.state==203 }"><i class="yyz detImg1"></i><span>已验证</span></c:when><c:otherwise><i class="yyz detImg2"></i><span>未验证</span></c:otherwise></c:choose></div>
        </div>
        <div class="perone_nx">
            <span>组织机构代码</span><div class="perone_fr"><c:choose><c:when test="${company.state==203 }"><i class="yyz detImg1"></i><span>已验证</span></c:when><c:otherwise><i class="yyz detImg2"></i><span>未验证</span></c:otherwise></c:choose></div>
        </div>
         <div class="perone_nx">
            <span>法人身份证</span><div class="perone_fr"><c:choose><c:when test="${company.state==203 }"><i class="yyz detImg1"></i><span>已验证</span></c:when><c:otherwise><i class="yyz detImg2"></i><span>未验证</span></c:otherwise></c:choose></div>
        </div>

    </div>
    <div style="clear:both"></div>
    <!--按钮-->
    <a href="http://www.17xs.org/user/userAdvice.do"><div class="person_btn">我要质疑</div></a>
</div>
</body>
</html>