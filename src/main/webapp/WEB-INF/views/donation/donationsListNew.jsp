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
<title>善园 - 一起行善，温暖前行！</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
</head> 
<style type="text/css">
	.select .sel-checked{
		margin-top: 2px;
	}
</style>
<body>
<%@ include file="./../common/newhead.jsp" %>
<!--主体 start-->
<!--主体 start-->
<div class="bodyNew dtlist">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="">首页</a> >  最新捐款
    </div>
    <div class="w1000">
        <div class="listBorder">
            <div class="listSearch">
             	<dl class="select">
                    <dt class="sel-checked">
                        <input class="sel-val" v="0" type="text" value="一个月内" readonly>
                        <span class="triangle"><i class="triangle-up"></i></span>
                        <input id="sel-checked" type="hidden" value="0">
                    </dt>
                    <dd class="sel-options" style="display: none;">
                    	<a class="sel-option" href="javascript:;" v=''>一个月内</a>
                        <a class="sel-option" href="javascript:;" v='2017'>2017</a>
                        <a class="sel-option" href="javascript:;" v='2016'>2016</a>
                        <a class="sel-option" href="javascript:;" v='2015'>2015</a>
                    </dd>
                </dl> 
                <div class="form">
                    <input type="text" id="projectTitle" placeholder="请输入项目名称/捐款人/受助人姓名..."/>
                    <button id = "search">搜索</button>
                </div>
            </div>
            <div class="list-list">
                <ul class="list-hd">
                    <li class="lst-col1">捐赠时间</li>
                    <li class="lst-col2">捐赠人</li>
                    <li class="lst-col3">捐赠主题</li>
                    <li class="lst-col4">受捐人</li>
                    <li class="lst-col5">金额（元）</li>
                 </ul>
                
                 <div class="list-bd" id="dtList"></div>
                 <div id="lstPages" class="lstPages" style=""><span class="lstP-con"><a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a><span class="pages"><i class="curPage" p="1">1</i><i p="2">2</i></span><a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a></span></div>

            </div>
            
        </div>
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->

<script data-main="<%=resource_url%>res/js/dev/donationsListNew.js?v=201510131035" src="<%=resource_url%>res/js/require.min.js"></script>
<script>
	(function(){
	    var bp = document.createElement('script');
	    var curProtocol = window.location.protocol.split(':')[0];
	    if (curProtocol === 'https') {
	        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';        
	    }
	    else {
	        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
	    }
	    var s = document.getElementsByTagName("script")[0];
	    s.parentNode.insertBefore(bp, s);
	})();
</script>
</body>
</html>
