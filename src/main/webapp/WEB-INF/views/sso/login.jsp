<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="cache-control" content="no-store" />
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="./../common/file_url.jsp"%>
<meta name="keywords" content="" />
<meta name="description" content="" />
<meta name="viewport" />
<title>善园-登录</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/reg.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css?v=20180207" />
</head>

<body>
<div class="w1000 wrnav">
	<h1><a href="http://www.17xs.org" title=""><img src="<%=resource_url%>res/images/logo-lg.jpg" alt="" /></a></h1>
    <div class="wrnavlb yh">
    	<span><em></em>捐款方式多元化</span>
        <span><em></em>捐款过程透明、可追溯</span>
        <span><em></em>社会大众多元参与</span>

    </div>
</div>
<div class="wreg wzc wlogin" id="register">
	<div class="start1" style="display:block">
        <div class="rl">
            <div class="rlitem">
                <label>账户登录：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <em class="rname"></em>
                        <input type="text" class="rgray" dvalue="用户名/手机号码" value="用户名/手机号码" id="username"/>
                    </div>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem">
                <label>密码：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <em class="rpw"></em>
                        <input type="password" placeholder="密码" value="" id="pwd"/>
                    </div>
                    <span class="wrinfo"></span>
                </div>
            </div>

            <div class="rlitem rlcode">
                <label>验证码：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <input type="text" class="" value="" id="code"/>
                    </div>
                    <img src="<%=resource_url%>user/code.do?type=login" id="codepic" class="codepic" alt="" />
                    <a href="javascript:;" class="refreshCode"></a>
                    <span class="wrinfo"></span>
                </div>
            </div>

            <div class="rlitem rllogin">
                <label></label>
                <div class="rltext rlpl">
                    <input type="button" id="signin" class="rlbtn yh"  value="登  录"/>
                </div>
            </div>
            <div class="rlitem lfoot">
            	<label></label>
                <div class="rltext">
                  		<a class="wjpw" href="http://www.17xs.org/user/changepassword.do">忘记密码&gt;&gt;</a>还没有注册善园基金会？
                  		<a href="http://www.17xs.org//user/sso.do">立即注册&gt;&gt;</a>
                </div>
            </div>
            </div>
        <div class="rr">
            <a title="" class="rrfocus"><img src="<%=resource_url%>res/images/login/lrad_03.jpg" /></a>
            <p>我们传递爱，因为我们相信爱</p>
            <p>爱让我们感动，感动让我们传递爱</p>
            <p>善良是生命中用之不尽的黄金，帮助别人，就是善待自己</p>
            <p>让我们一起行善，让爱与温暖携手并进</p>
        </div>
    </div>
</div>
<div class="wrroot">
	<p>
		<a href="<%=resource_url%>" target="_blank">首页</a><i></i> <a
			href="<%=resource_url%>help/about.do" target="_blank">关于善园基金会</a><i></i>
		<a href="<%=resource_url%>help/service.do" target="_blank">服务协议</a><i></i>
		<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2777819027&site=qq&menu=yes">联系客服</a><i></i>
		<a target="_blank" href="http://www.miitbeian.gov.cn/">浙ICP备15018913号-1</a>
	</p>
	<p>&nbsp;&nbsp;Copyright&nbsp;&nbsp;©&nbsp;&nbsp;&nbsp;&nbsp;宁波市善园公益基金会&nbsp;&nbsp;版权所有</p>
	<p>杭州智善网络科技有限公司&nbsp;&nbsp;提供技术支持</p>
</div>
<script data-main='<%=resource_url%>res/js/dev/login.js?v=20180206' src="<%=resource_url%>res/js/require.min.js"></script> 
</body>
</html>
