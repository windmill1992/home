<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport" />
<title>善园 - 用户中心</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/legalize.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/userCenter.css" />
</head>
<body>
	<%@ include file="./../common/head.jsp"%>
	<div class="bodyer userCenter giftList"> 
	<div class="page"> 
         <%@ include file="./../common/model1.jsp"%>
        <div class="page-bd">
            <div class="uCEN-L">
            	<div class="uCEN-L">
            	<%@ include file="./../common/eleft_menu.jsp"%>
            </div>
            </div>
            <div class="uCEN-R"> 
                <div class="uCEN-list list">
                	<div class="uCEN-hd">
                    	<span class="uCEN-tit">助善明细</span> 
                        <dl class="select">
                        <dt class="sel-checked"> 
                            <input class="sel-val" v="0" type="text" value="三个月内" readonly="">
                            <span class="triangle"><i class="triangle-up"></i></span> 
                            <input id="sel-checked" type="hidden" value="0">
                        </dt>
                        <dd class="sel-options">
                            <a class="sel-option sel-check">三个月内</a>
                            <a class="sel-option">半年内</a>
                            <a class="sel-option">一年内</a>
                            <a class="sel-option">全部</a>
                        </dd>
                    </dl>
                    </div>
                    <div class="uCEN-bd">
                        <div class="list wlist-data"> 
                        	 <ul class="list-hd">
                             	<li class="lst-col1">项目名称</li>
                                <li class="lst-col2">捐赠金额</li>
                                <li class="lst-col4">捐赠进度</li>
                                <li class="lst-col5">捐赠时间</li>
                                <li class="lst-col6">受助人最新动态</li>
                             </ul>
                             <div class="list-bd" id="gflist">
                             </div>
                             <p class="pageNum" id='pageNum'></p>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
	<input type="hidden" value="${page}" id="page"/>
	<%@ include file="./../common/footer.jsp"%>
	<script data-main="<%=resource_url%>res/js/dev/giftList.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
