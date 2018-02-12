<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
	<!--left start-->
<div class="aNleft">
   	<dl>
       	<dt>信息披露</dt>
        <c:choose>
       		<c:when test="${rules=='rules'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=7&item=章程" title="">章程</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	<a href="<%=resource_url%>news/infoByItem/?type=7&item=章程" title="">章程</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
        <c:choose>
       		<c:when test="${baseInfo=='baseInfo'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=8&item=基本信息" title="">基本信息</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoByItem/?type=8&item=基本信息" title="">基本信息</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	 <c:choose>
       		<c:when test="${workUnit=='workUnit'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=9&item=理事信息" title="">理事信息</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoByItem/?type=9&item=理事信息" title="">理事信息</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
          <c:choose>
       		<c:when test="${cwxx=='cwxx'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoCenter/?type=4" title="">财务信息</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoCenter/?type=4" title="">财务信息</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	 <c:choose>
       		<c:when test="${xmxx=='xmxx'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoCenter/?type=5" title="">项目信息</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoCenter/?type=5" title="">项目信息</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	 <c:choose>
       		<c:when test="${xmglzd=='xmglzd'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=10&item=项目管理制度" title="">项目管理制度</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoByItem/?type=10&item=项目管理制度" title="">项目管理制度</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	 <c:choose>
       		<c:when test="${caiwu=='caiwu'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=11&item=财务管理办法" title="">财务管理办法</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoByItem/?type=11&item=财务管理办法" title="">财务管理办法</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	 <c:choose>
       		<c:when test="${renshi=='renshi'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=6&item=人事管理制度" title="">人事管理制度</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoByItem/?type=6&item=人事管理制度" title="">人事管理制度</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${lishi=='lishi'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/infoByItem/?type=12&item=其他信息" title="">其他信息</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/infoByItem/?type=12&item=其他信息" title="">其他信息</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
     
       </dl>
   </div>