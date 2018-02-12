<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/h5/publish.css"/>

</head>

<body>
<div id="pageContainer">
  <div class="publish">
  	<!--标题//-->
    <%--<div class="top">--%>
    	<%--<a href="javascript:void(0);" class="back" id="back"><img src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAABBJREFUeNpi/P//PwNAgAEACQEC/2m8kPAAAAAASUVORK5CYII=" class="back_img back_icon_6"></a>--%>
    	<%--<div class="title">我要求助</div>--%>
     <%--</div>--%>
     <%----%>
     <!----表单填写//-->
    	<div class="form_box">
        	<ul>
            	<li class="input"><input type="text" value="求助原因：" id="reasion"></li>
                <li class="input"><input type="text" value="您的姓名：" id="name"></li>
                <li class="input"><input type="text" value="您的电话：" id="phone"></li>
                <li class="input"><input type="text" value="qq/微信：" id="qq"></li>
                <li class="type">
<!--                	<a href="#" class="selected">求学</a>-->
<!--                	<a href="#">济困</a>-->
<!--                	<a href="#">安老</a>-->
				<p id="needhelptype">
                	 <c:forEach var="record" items="${atc}" varStatus="status">
                	 	<c:choose>
                	 		<c:when test="${record.sort==1}">
                	 			<a data="${record.typeName_e}" href="javascript:void(0);"  class="selected">${record.typeName}</a>
                	 		</c:when>
                	 		<c:otherwise>
                	 			<a data="${record.typeName_e}" href="javascript:void(0);">${record.typeName}</a>
                	 		</c:otherwise>
                	 	</c:choose>
               		 </c:forEach>
               	</p>
                </li>
                <li class="btns"><a href="javascript:void(0);" class="pub_btn" id="publish_help">发布求助</a></li>
            </ul>
        </div>
        <div class="contact">
        	<p>如需帮助，请联系客服</p>
            <p>热线：0571-87165191<br/>客服QQ：2777819027</p>
        </div>
      <!--页脚
      <div class="footer">
      	<span>© 2015 杭州智善  版权所有</span>
        <img src="images/min-logo.jpg"></div>//-->
  </div>
</div>
</body>
<script data-main="<%=resource_url%>res/js/h5/needhelp_h5.js?v=201510131035" src="<%=resource_url%>res/js/require.min.js"></script>
</html>