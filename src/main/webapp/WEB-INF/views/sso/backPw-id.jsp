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
	<div class="wPs wPsphone" id="password">
	<div class="password">
        <div class="number-prim">
            <dl class="number first">
                <dt class="s-num">1</dt>
                <dd class="s-text s-stext">验证身份</dd>
            </dl>
            <dl class="number secend">
                <dt class="s-num_pw">2</dt>
                <dd class="s-text">修改登录密码</dd>
            </dl>
            <dl class="number end">
                <dt class="s-num s-ok"></dt>
                <dd class="s-text">完成</dd>
            </dl>
        </div>
        <h2>您正在使用身份证验证身份，请完成以下操作</h2>
        <div class="pwCon">
            <div class="rlitem">
                <label>真实姓名：</label>
                <div class="rltext">
                    <input type="text" class="inputtext" id="realname" >
                    <span class="wrinfo"></span>
                    <p class="brNext">请填写申请人真实姓名</p>
                </div>
            </div>
            <div class="rlitem">
                <label>身份证号码：</label>
                <div class="rltext">
                    <input type="text" class="inputtext" id="idCard">
                    <span class="wrinfo"></span>
                    <p class="brNext">请输入身份证号码</p>
                </div>
            </div>
            
            <div class="rlitem rlcode">
                <label>验证码：</label>
                <div class="rltext">
                    <input type="text" class="inputtext inputcode" id="wrcode">
                    <img src="http://www.17xs.org/user/code.do?" id="codepic" class="codepic" alt="">
                    <a title="" class="refreshCode"></a>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem">
                <label></label>
                <div class="rltext">
                    <a class="nextbtn" id="signin2">下一步</a>
                </div>
            </div>
        </div>
    </div> 
    <input type="hidden" id="username" value="${name}"/>
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
