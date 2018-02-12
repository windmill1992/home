<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge" ></meta>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport" />
<title>善园 - 我要行善</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/doGood.css"/>
</head>
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer doGood"> 
	<div class="page">
    	<div class="page-hd">
           <div class="page-tit"><h2 class="w1000">我要行善</h2></div>
        </div>
        <div class="page-bd items">
        	<div class="items-hd">
            	<div class="items-tab" condition="type"><b>按领域筛选：</b>
            		<a v="" href="javascript:void(0);">全部</a><i></i>
                  	<c:forEach var="record" items="${atc}" varStatus="status">
                  	<a v="${record.typeName_e}" href="javascript:void(0);">${record.typeName}</a>
                  	<c:if test="${status.last !=true}"><i></i></c:if>
                  	</c:forEach>
            	</div>
                <div class="items-tab" condition="status"><b>按项目状态筛选：</b><a v="0" href="javascript:void(0);">募捐中</a><i></i><a v="2" href="javascript:void(0);">已结束</a></div>
            </div>
            <div class="items-bd">
            	<ul id="items" class="items-list">
                	<li class="prompt loading"></li> 
                </ul> 
            </div>
        </div> 
    </div>
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/doGood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>