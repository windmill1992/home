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
<title>善园网 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>

</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div class="bodyer userCenter manageList"> 
	<div class="page"> 
        <div class="page-bd">
            <div class="uCEN-L">
            	<%@ include file="./../common/eleft_menu.jsp"%>
            </div>
            <div class="uCEN-R"> 
                <div class="mgPrompt">
                <span id="mgPromptText"></span>
                <a title="" id="mgPok">是</a>
                <a title="" class="gray" id="mgPno">否</a>
                </div>
                <div class="uCEN-list list">
                	<div class="uCEN-hd">
                    	<span class="uCEN-tit">善员工管理</span> 
                        <a class="more" title="" id="mgDialog">什么是善员工</a>  
                    </div>
                    <div class="uCEN-bd">
                        <div class="list wlist-data"> 
                        	 <ul class="list-hd">
                             	<li class="lst-col1">账户名</li>
                                <li class="lst-col2">姓名</li>
                                <li class="lst-col3">善级</li>
                             </ul>
                             <div class="list-bd" id="mglist"></div>
							 <p class="pageNum" id="pageNum"></p>
                             <div id="lstPages" class="lstPages slstPages" >
                                 <span class="lstP-con">
                                 <a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);">上一页</a>
                                 <span class="pages">
                                     <i class="curPage" p="1">1</i>
                                     <i p="2">2</i>
                                     <i p="3">3</i>
                                 </span>
                                 <a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a>
                                 </span>
                             </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/mye_manageList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>