<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园 - 加入善管家</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/applyStep.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css"/> 
</head> 
<body>
<%@ include file="./../common/newhead.jsp"%>
<div id="bodyer" class="bodyer applySteps applyRslt">
	<div class="page">
    	<!-- <div class="page-hd">
        	<div class="page-tit"><h2 class="w1000">加入善管家</h2></div>
        </div> -->
        <div class="page-bd"> 
            <div class="applyStp-main">
            	<div class="applyStp-hd">
                	<div class="applyStp-tit">善管家申请流程</div>
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
                    <div class="btnBox">
                    	<c:if test="${user.userType == 'enterpriseUsers'}">
                    		<a class="btn" href="http://www.17xs.org/ucenter/core/enterpriseCenter.do">个人中心</a>
                    	</c:if>
                    	<c:if test="${user.userType != 'enterpriseUsers'}">
                    		<a class="btn" href="http://www.17xs.org/ucenter/core/personalCenter.do">个人中心</a>
						</c:if>
                    <a class="btn" href="http://www.17xs.org/project/index.do">我要行善</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>
<%@ include file="./../common/newfooter.jsp"%>
<script data-main="<%=resource_url%>res/js/dev/joinGood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
