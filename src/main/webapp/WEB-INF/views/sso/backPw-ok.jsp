<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="cache-control" content="no-store" />
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="viewport" />
<title>重置密码</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entReg.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/password.css"/>
</head>

<body>
	<div class="w1000 wrnav">
		<h1>
			<a href="http://www.17xs.org" title=""><img
				src="<%=resource_url%>res/images/logo-lg.jpg" alt="" /></a>
		</h1>
		<div class="wrnavlb yh">
			<span><em></em>捐款方式多元化</span> <span><em></em>捐款过程透明、可追溯</span> <span><em></em>社会大众多元参与</span>

		</div>
	</div>
<div class="wPs" id="password" style="width:888px;">
	<div class="password">
    
            <div class="number-prim_ko">
            <dl class="number first">
                <dt class="s-num">1</dt>
                <dd class="s-text s-stext">验证身份</dd>
            </dl>
            <dl class="number secend">
                <dt class="s-num_pw2">2</dt>
                <dd class="s-text_pw2">修改登录密码</dd>
            </dl>
            <dl class="number end">
                <dt class="s-ok_ko"></dt>
                <dd class="s-text_ko">完成</dd>
            </dl>
        </div>
    
    
    
    
    	<div class="pwOk mt20">
        	重置成功，请牢记新的登录密码
            <p><a href="http://www.17xs.org/user/sso/login.do" title="">返回登录</a></p>
        </div>
    </div> 
</div>	
	<div class="wrroot">
		<p>
			<a href="<%=resource_url%>" target="_blank">首页</a><i></i> <a
				href="<%=resource_url%>help/about.do" target="_blank">关于善园基金会</a><i></i>
			<a href="<%=resource_url%>help/service.do" target="_blank">服务协议</a><i></i>
			<a target="_blank"
				href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes">联系客服</a><i></i>
			<a target="_blank" href="http://www.miitbeian.gov.cn/">浙ICP备15018913号-1</a>
		</p>
		<p>&nbsp;&nbsp;Copyright&nbsp;&nbsp;©&nbsp;&nbsp;&nbsp;&nbsp;宁波市善园公益基金会&nbsp;&nbsp;版权所有</p>
		<p>杭州智善网络科技有限公司&nbsp;&nbsp;提供技术支持</p>
	</div>
	<script data-main='<%=resource_url%>res/js/dev/password.js'
		src="<%=resource_url%>res/js/require.min.js" charset="UTF-8"></script>
</body>
</html>
