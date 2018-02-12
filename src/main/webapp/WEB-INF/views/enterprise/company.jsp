<!DOCTYPE html>
<%@page import="org.apache.commons.lang.StringUtils"%>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/company.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp"%>
<div class="bodyer page goodhelp"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="cpcon yh">
            	<div class="cpcom-hd">
                	<div class="cpcomlogo">
                    	<img src="${company.conpanyImageUrl}" alt="">
                    </div>
                    <div class="cpcomDetail">
                    	<h2>${company.name}</h2>
                        <p>${company.infomation}</p>
                    </div>
                </div>
            	<div class="cpcom-bd">
                	<div class="listone">
                    	<div class="ltt"><span class="line"></span>累计捐赠（含善员工）</div>
                        <div class="ltb">
                           <c:if test="${companyDto!=null}">
                              <span><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${companyDto.donateTotalAmount}"/></span>元
                           </c:if>
                        </div>
                    </div>
                    <div class="listone">
                    	<div class="ltt"><span class="line"></span>企业助善</div>
                        <div class="ltb">
                           <c:if test="${companyDto!=null}">
                        	  <span><fmt:formatNumber type="number" pattern="#,##0.00#" value = "${companyDto.goodHelpTotalAmount}"/></span>元
                           </c:if>
                        </div>
                    </div>
                    <div class="listone">
                    	<div class="ltt"><span class="line"></span>帮助家庭(含善员工)<span class="smark"><em><i></i>含企业捐赠项目数、企业助善
项目数、善员工捐赠项目数</em></span></div>
                        <div class="ltb">
                           <c:if test="${companyDto!=null}">
                        	  <span>${companyDto.helpFamilyNumber}</span>家
                        	</c:if>
                        </div>
                    </div>
                    <div class="clear"></div>
                </div>
            </div>  
            <div class="cplist">
            	<div class="cpl-hd">
                	<ul class="items-tab">
                        <li v="0"  class="selected"><span>募捐项目</span></li>
                        <li v="1" ><span>助善项目</span></li>
                        <li v="2" ><span class="nobdrR">善员工动态</span></li>
                	</ul>
                </div>
                <div class="cpl-bd">
                	<div class="itemlist" id="itemlist">
                    
                    </div>
                </div>
              </div>   
        </div>
    </div> 
</div>
<input id="companyCode" type="hidden" value="${company.goodPassWord}">
<input id="companyUserId" type="hidden" value="${company.userId}">
<input id="projectID" type="hidden" value="">
<%@ include file="./../common/newfooter.jsp" %>
<script type="text/javascript" src="<%=resource_url%>res/js/jquery-1.8.2.min.js"></script>
<!-- <script type="text/javascript" src="http://static.bshare.cn/b/buttonLite.js#uuid=48224106-d04a-4cbc-aecb-132a3f608fda&style=-1"></script> -->
<script data-main="<%=resource_url%>res/js/dev/company.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>