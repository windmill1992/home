<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<meta property="wb:webmaster" content="8375316845f5cb81" />
<meta property="qc:admins" content="3641564675617036727" />
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>善园网—一起行善，互联网公益平台</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/theheaderimg.css"/>
</head>
<body>
<!--头部 start-->
<%@ include file="./head.jsp" %>
<!--头部 end-->
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="#" title="" target="_blank">服务项目</a>
    </div>
    <div class="w1000">
        <div class="filterNew mt15">
        	<div class="sort">
            	<a href="javascript:void(0);" title="" v="0" class="on">默认排序</a>
                <a href="javascript:void(0);" title="" v="1">金额<em class="desc"></em></a>
                <a href="javascript:void(0);" title="" v="2">人气<em class="desc"></em></a>
                <a href="javascript:void(0);" title="" v="3">最新反馈</a>
            </div>
            <div class="opear">
<!--            	<a href="javascript:void(0);" class="batch">批量捐款</a>-->
                <span>全选项目<em class="checkbox"></em></span>
            </div>
        </div>
        <div  class="mt15 doGoodList" >
	        <ul id="doGoodList">
	        	<!--10条数据-->
	        </ul>        
        	<div id="lstPages" class="lstPages" style="">
	        	<span class="lstP-con">
	        	<a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a>
	        	<span class="pages"><i class="curPage" p="1">1</i><i p="2">2</i></span>
	        	<a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a>
	        	</span>
        	</div>
		</div>
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./footer.jsp" %>
<!--底部 end-->
<input type="hidden" id = "typeName" value="${typeName }">
<input type="hidden" id = "province" value="${province }">
<input type="hidden" id="userId" value="${userId }">
<script data-main="<%=resource_url%>res/js/other/doGoodNew.js?v=20160323" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
