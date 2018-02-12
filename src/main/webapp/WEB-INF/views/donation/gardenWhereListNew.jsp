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
<title>善园 - 善款去向</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/commNew.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<!--主体 start-->
<div class="bodyNew gdWherelist">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="" >首页</a> >  善款去向
    </div>
    <div class="w1000">
        <div class="listBorder">
            <div class="listSearch">
                <div class="form">
                    <input type="text" id="smsg" placeholder="请输入主题名称/受助人姓名..."/>
                    <button id = "select">搜索</button>
                </div>
            </div>
            <div class="list-list">
                <ul class="list-hd">
                    <li class="lst-col1">汇款时间</li>
                    <li class="lst-col7">受助人</li>
                    <li class="lst-col3">收款方</li>
                    <li class="lst-col4">收款账号</li>
                    <li class="lst-col2">项目</li>
                    <li class="lst-col5">汇款金额（元）</li>
                    <li class="lst-col6">汇款凭证</li>
                 </ul>
                
                 <div class="list-bd" id="dtList">
                  <!--20行-->

                 </div>
                 <div id="lstPages" class="lstPages" style=""><span class="lstP-con"><a dir="-1" id="lstP-prev" class="nomrgL lstP-prev" href="javascript:void(0);" style="display:none;">上一页</a><span class="pages"><i class="curPage" p="1">1</i><i p="2">2</i></span><a dir="1" id="lstP-next" class="lstP-next" href="javascript:void(0);">下一页</a></span></div>

            </div>
            
        </div>
    </div>
</div>
<!--主体 end-->
<!--底部 start-->
<%@ include file="./../common/newfooter.jsp" %>
<!--底部 end-->

<script data-main="<%=resource_url%>res/js/dev/gardenWhereListNew.js?v=201510131039" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
