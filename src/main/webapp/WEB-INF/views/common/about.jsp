<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
	<!--left start-->
<div class="aNleft">
   	<dl>
       	<dt>简介</dt>
       	<c:choose>
       		<c:when test="${flag=='about'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/about/" title="" >基金会介绍</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/about/" title="" >基金会介绍</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
        <c:choose>
       		<c:when test="${flag=='aboutTrait'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/aboutTrait/" title="">基金会特色</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/aboutTrait/" title="">基金会特色</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag=='aboutPromise'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/aboutPromise/" title="">基金会承诺</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/aboutPromise/" title="">基金会承诺</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
      <%--   <c:choose>
       		<c:when test="${flag=='aboutMembers'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/aboutMembers.do" title="">一届理事会成员</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/aboutMembers.do" title="">一届理事会成员</a>
	            </dd>
       		</c:otherwise>
       	</c:choose> --%>
        <c:choose>
       		<c:when test="${flag=='aboutDonation'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/aboutDonation/" title="">捐款渠道与须知</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/aboutDonation/" title="">捐款渠道与须知</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<dt>活动/公告</dt>
        <c:choose>
       		<c:when test="${notice=='notice'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/center/?type=1" title="">新闻公告</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/center/?type=1" title="">新闻公告</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
        <c:choose>
       		<c:when test="${activity=='activity'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/center/?type=3" title="">慈善资讯</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/center/?type=3" title="">慈善资讯</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${project_end=='project_end'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>news/center/?type=7" title="">结项报告</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>news/center/?type=7" title="">结项报告</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
        <dt>常见问题</dt>
        <c:choose>
       		<c:when test="${flag0==0}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/questions/?no=0" title="">个人实名认证</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/questions/?no=0" title="">个人实名认证</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag1==1}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/questions/?no=1" title="">企业实名认证</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/questions/?no=1" title="">企业实名认证</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag2==2}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/questions/?no=2" title="">忘记密码</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/questions/?no=2" title="">忘记密码</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
        <c:choose>
       		<c:when test="${flag3==3}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/questions/?no=3" title="">怎么求助？</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/questions/?no=3" title="">怎么求助？</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag5==5}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/questions/?no=5" title="">捐款方式</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/questions/?no=5" title="">捐款方式</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag6==6}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/questions/?no=6" title="">捐款安全性</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/questions/?no=6" title="">捐款安全性</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<dt>协议</dt>
       	<c:choose>
       		<c:when test="${flag=='userService'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/userService/" title="">用户协议</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/userService/" title="">用户协议</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag=='supplicantService'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/supplicantService/" title="">求助者协议</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/supplicantService/" title="">求助者协议</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag=='donorsService'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/donorsService/" title="">捐助者协议</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/donorsService/" title="">捐助者协议</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       	<c:choose>
       		<c:when test="${flag=='gooderService'}">
       			<dd class="on">
	           	 <a href="<%=resource_url%>help/gooderService/" title="">善管家协议</a>
	            </dd>
       		</c:when>
       		<c:otherwise>
       			<dd>
	           	 <a href="<%=resource_url%>help/gooderService/" title="">善管家协议</a>
	            </dd>
       		</c:otherwise>
       	</c:choose>
       </dl>
   </div>