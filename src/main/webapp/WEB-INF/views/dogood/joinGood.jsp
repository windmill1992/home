<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport" />
<title>善园 - 加入善管家</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css?v=<%=version%>"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css"/> 
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/layout.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/joinGood.css"/>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/headNew.css?v=<%=version%>"/> 
</head> 
<body>
<%@ include file="./../common/newhead.jsp" %>
<div id="bodyer" class="bodyer joinGood">
	<div class="page">
    	<!-- <div class="page-hd">
        	<div class="page-tit"><h2 class="w1000">加入善管家</h2></div>
        </div> -->
        <div class="page-bd">
        	<div class="joinGdL">
            	<ul class="joinGd-Nav">
                	<li><a class="anchor" href="javascript:void(0);">什么是善管家</a></li>
                    <li class="nobdrB"><a class="anchor" id="toProcess" href="javascript:void(0);">申请流程</a></li>
                </ul> 
                <div class="btn applyBtn">现在申请</div>
            </div>
            <div class="joinGdR">
            	<div class="joinGd-main">
                    <a name="goodKeeper" class="joinGd-tit">什么是善管家</a>
                    <dl class="joinGd-info">
                    <dt> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;善园基金会为每个捐助申请项目匹配就近的善管家。善管家负责项目的真实性审核，负责为项目故事润色，提高项目呈现质量。善园基金会会全程跟进每个捐助项目实施过程，由善管家对整个项目进行督导并实时反馈，确保项目真实有效，让每一笔善款阳光透明。</dt>
                    	<dt>所有注册有捐款人账号的用户均有机会申请成为善管家成员，符合以下条件的用户将得到优先考虑：</dt>
                        <dd class="listNum">爱心企业员工(企业账号可提交善管家候选人名单与邮箱进行比对)</dd>
                        <dd class="listNum">平台活跃用户(捐款次数超过3次，且累计金额超过5000元)</dd>
                        <dd class="listNum">大病救助、结对助学等相关公益领域资深志愿者</dd>
                        <dd>所有“善管家”成员需要进一步完善个人资料，为后续项目追溯提供保障。</dd>
                    </dl> 
                    <div class="joinGd-img"></div>
                    <a name="applyProcess" id="applyProcess" class="joinGd-tit">申请流程</a>
                     <dl class="joinGd-info">
                    	<dt class="listNum">实名认证：</dt>
                        <dd>在“善园基金会”网站注册账号，进行个人实名认证。</dd>
                    </dl>
                    <dl class="joinGd-info">
                    	<dt class="listNum">完善资料： </dt>
                        <dd>完善个人资料，申请善管家，并填写相关信息。</dd>
                    </dl>
                    <dl class="joinGd-info">
                    	<dt class="listNum">等待审核：</dt>
                        <dd>提交信息等待审核。</dd>
                    </dl>
                    <dl class="joinGd-info">
                    	<dt class="listNum">审核完成：</dt>
                        <dd>恭喜您成为善管家成员。</dd>
                    </dl>
                    <div class="btnBox"><div class="applyBtn btn">现在申请</div></div>
            	</div>
            </div>
        </div>
    </div>
<input type="hidden" id="state" value="${state}">
</div>
<%@ include file="./../common/newfooter.jsp" %>
<script data-main="<%=resource_url%>res/js/dev/joinGood.js" src="<%=resource_url%>res/js/require.min.js"></script>
</body>
</html>
