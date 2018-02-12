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
<title>善园 - 我要求助</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/applyStep.css"/>
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div id="bodyer" class="bodyer applySteps applyRslt">
	<div class="page">
    	<div class="page-hd">
        	<div class="page-tit">&nbsp;我要求助 </div>
        </div>
        <div class="page-bd"> 
            <div class="applyStp-main">
            	<div class="applyStp-hd">
                	<div class="applyStp-tit">我要求助申请流程</div>
                    <ul class="applyStp-tab">
                    	<li><i>1</i><span>实名认证</span></li>
                        <li><i>2</i><span>完善资料</span></li>
                        <li class="curTab"><i>3</i><span>等待审核</span></li>
                    </ul>
                    
                </div>
                <div class="applyStp-bd">
                	<div class="applyStp-rslt">
                    	<i class="ok"></i>
                        <div class="rslt-con">
                            <h3>您提交的申请已进入审核期</h3>
                            <p>我们的工作人员会在3个工作日内通过站内信告知您审核结果</p>
                        </div>
                    </div> 
                    <div class="btnBox"><a class="btn" href="<%=resource_url%>ucenter/core/mygood.do">个人中心</a><a class="btn" href="<%=resource_url%>project/index.do">我要行善</a></div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/helpinfo.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
