<!DOCTYPE html>
<html lang="en">
<head>	
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0">
<title>报名表</title>
<link rel="stylesheet" href="../../res/css/dev/entryForm.css">
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
</head>
  
<body>
<div class="entry">
    <h2>${apiCommonForm.title }</h2>
    <div class="entry_margin">
        <p class="top_p"><em><fmt:formatDate value="${apiCommonForm.createTime }" pattern="yyyy-MM-dd"/></em><span class="ren_s">${apiCommonForm.orgnazition }</span></p>
        <div class="entry_head">
            <p class="head_p1">报名截止时间：<span class="head_s1"><fmt:formatDate value="${apiCommonForm.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
            <p class="head_p1">已报名人数：<span class="head_s2">${apiCommonForm.number==null?'0':apiCommonForm.number}</span>人</p>
            <p class="head_p1">${apiCommonForm.content==null?'':apiCommonForm.content }</p>
        </div>
        <c:choose>
        	<c:when test="${apiCommonForm.endTime>=currentTime && apiCommonForm.number<=apiCommonForm.limit}">
        		<div class="entry_center">
        		<c:if test="${apiCommonForm.form!=null }">
        		<c:forTokens items="${apiCommonForm.form }" delims="," var="form">
        			<div class="center1">
                		<p class="center_text1"><span>*</span>${form }</p>
                		<input type="text" class="center_input" id="${form }"/>
           			 </div>
        		</c:forTokens>
        		</c:if>
           	 	<a href="javaScript:void(0)" >
                	<div class="footer" id="submitbtn">提交</div>
            	</a>
        		</div>
        	</c:when>
        	<c:otherwise>
        		<div class="entry_center">报名已结束或报名人数已满！</div>
        	</c:otherwise>
        </c:choose>
    </div>
</div>
<div class="tishi" id="success" style="display: none;">恭喜您报名成功！</div>
<div class="tishi" id="fail" style="display: none;">报名失败！</div>
<input type="hidden" id="forms" value="${apiCommonForm.form }" />
<input type="hidden" id="formId" value="${apiCommonForm.id }" />
<input type="hidden" id="userId" value="${userId }" />
</body>
<script data-main="<%=resource_url%>res/js/dev/commonForm.js?v=201510131035" src="<%=resource_url%>res/js/require.min.js"></script>
</html>
