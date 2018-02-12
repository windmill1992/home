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
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/seekHelp.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
</head> 
<body>
<%@ include file="./../common/head.jsp" %>
<div class="bodyer seekHelp">
	<div class="page">
		<div class="page-hd">
           <div class="page-tit"><h2 class="w1000">我要求助</h2></div>
        </div>
        <div class="page-bd"> 
            <div class="skHlp-hd">
               <p class="skHlp-info">善园基金会专注于慈善公益，运用互联网技术构建全国首家透明化网络爱心救助平台。全程跟进每个捐助项目实施过程，由善管家对整个项目进行督导并实时反馈，保证项目真实有效，让每一笔善款阳光透明。确保您的每笔善款能“公开！透明！全额！即时！直接！”到达受助人手中！</p>
            </div>
            <div class="skHlp-bd">
                <ul class="skHlp-steps">
                    <li class="dirR">
                        <h3 class="steps-tit">募捐</h3>
                        <i class="steps-num">04</i>
                        <div class="steps-brief">
                            在线接受公众捐款
                        </div>
                    </li>
                    <li class="dirR">
                        <h3 class="steps-tit">审核</h3>
                        <i class="steps-num">03</i>
                        <div class="steps-brief">
                            系统匹配“善管家”审核项目，并进一步修改项目文案
                        </div>
                    </li>
                    <li class="dirR">
                        <h3 class="steps-tit">发起</h3>
                        <i class="steps-num">02</i>
                        <div class="steps-brief">
                            在线提交项目图文内容
                        </div>
                    </li>
                    <li class="dirR dirREm">
                        <h3 class="steps-tit">注册</h3>
                        <i class="steps-num">01</i>
                        <div class="steps-brief">
                            个人进行实名认证
                        </div>
                    </li>
                    <li class="dirL">
                        <h3 class="steps-tit">执行</h3>
                        <i class="steps-num">05</i>
                        <div class="steps-brief">
                            达到项目所需捐款 金额或达到项目截止时间
                        </div>
                    </li>
                    <li class="dirL">
                        <h3 class="steps-tit">结束</h3>
                        <i class="steps-num">06</i>
                        <div class="steps-brief">
                            基金会把募款打到项目所在机构账号
                        </div>    
                    </li>
                    <li>
                        <a class="skHlp-btn" href="<%=resource_url%>ucenter/pcreate.do">现在申请</a>
                    </li>
                </ul> 
            </div> 
        </div>
	</div>       
</div>
<%@ include file="./../common/footer.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/seekHelp.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
