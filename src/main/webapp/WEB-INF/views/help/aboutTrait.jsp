<!DOCTYPE>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport"/>
<title>善园-善园基金会特色</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/form.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/aboutNew.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/>
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<!--主体 start-->
<div class="bodyNew">
    <div class="w1000 breadcrumb">
        当前位置：<a href="http://www.17xs.org/" title="" >首页</a> > 善园基金会特色
    </div>
    <div class="w1000 aboutNew">
    	<!--left start-->
    	<%@ include file="./../common/about.jsp" %>
        <!--left end-->
        <!--right start-->
        <div class="aNRight aboutCon">
            <div class="boederbox">
            	<h2>
                	<span class="lText">善园基金会特色</span>
                </h2>
                <dl class="statelist trait-bd">
                 	<dt>多元捐款方式：</dt>
                    <dd>
                    	<ul>
                        	<li>个人捐：个人针对单个项目一次捐款，单个项目的多人众筹</li>
                            <li>月捐：个人每月定额捐赠10元或100元至指定捐款领域，平台自动匹配项目，并及时反馈。</li>
                            <li>企业助善区：通过微信、微博等移动互联网平台邀请网友在平台注册，企业将善款发放给网友，以网友名义进行捐款，企业和注册网友数据双向统计。</li>
                            <li>一起捐:个人为心仪项目号召更多人加入捐款，并生成 “一起捐” 影响力排行。</li> 
                        </ul>
                    </dd> 
                    <dt>“善管家”项目督导</dt>
                    <dd>
                    	<ul>
                            <li>系统自动为每个捐助申请自动匹配就近的善管家，负责项目的真实性审核；</li>
                            <li>善管家同时负责为项目故事润色，提高项目呈现质量；</li>
                            <li>同时负责项目执行期的监督反馈。</li> 
                        </ul>
                    </dd>
                    <dt>全方位的捐款人服务</dt>
                    <dd>
                    	<ul>
                        	<li>客服支持：在线客服即时回复捐款相关问题，提高捐款“成单率”</li>
                            <li>智能项目推荐：根据全平台捐赠数据进行数据分析整合，通过邮件、微信等多种渠道，为捐款人持续推荐符合其捐赠偏爱的项目</li>
                            <li>捐款人影响力报告：为捐款人生成影响力地图与相应成果报告，提高捐款人捐款热情</li>
                            <li>善顾问服务</li>
                        </ul>
                    </dd>
                    <dt>捐款、项目可追溯</dt>
                    <dd>
                    	<ul>
                        	<li>募款完成后，每笔资金拨付的时间与对应金额都会通过系统进行及时反映，向捐款人保障透明、可追溯、可问责。</li>
                        </ul>
                    </dd>
                    <dt>大数据随时可提取</dt>
                    <dt>全平台配套运营</dt>
                    <dd>
                    	<ul>
                        	<li>基于统一数据库，开发电脑版、移动版两个版本，实现主流设备的完美显示；</li>
							<li>同时开发微信服务号“善园基金会”，实现捐款人绑定功能，每周通过微信向捐款人推送，同时支持微信直接捐款，提高捐赠客户体验；</li>
						</ul>
                    </dd>
                 </dl>
            </div>
        </div>
        <!--right end-->
    </div>
</div>
<!--主体 end-->
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/about.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
