<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
	<!--left start-->
<div class="aNleft">
   	<dl>
       	<dt>善荷计划</dt>
       	<c:choose>
       		<c:when test="${flag=='shanhe'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/shanhe.do" title="" >项目介绍</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/shanhe.do" title="" >项目介绍</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
        <c:choose>
       		<c:when test="${flag=='groundProject'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/groundProject.do" title="">落地项目</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/groundProject.do" title="">落地项目</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
    </dl>
   </div>