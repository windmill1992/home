<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- 是导航上的标题的提示语 -->
 <div class="page-hd">
 <h2 class="w1000">
 &nbsp;
 <!-- =================新版导航上的标签语===================== -->
<c:choose>
<c:when test="${user.userType=='enterpriseUsers'}">
    <a href="<%=resource_url%>ucenter/core/enterpriseCenter.do"><font color="white">用户中心</font></a>
 </c:when>
 <c:otherwise>
 	<a href="<%=resource_url%>ucenter/core/personalCenter.do"><font color="white">用户中心</font></a>
 </c:otherwise>
 </c:choose>
 &gt;
 <c:choose>
    <c:when test="${guideName!=null}">
       ${guideName}
    </c:when>
    <c:when test="${guideName==null}">
       提现记录
    </c:when>
    <c:otherwise>
    
    </c:otherwise>   

  </c:choose>
 </h2>
 </div>