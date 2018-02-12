<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="cache-control" content="no-store"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"%>
<%@ include file="./../common/file_url.jsp" %>
<meta name="viewport" />
<title>注册</title>
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/base.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/dialog.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/entReg.css" />
<link rel="stylesheet" type="text/css" href="<%=resource_url%>res/css/dev/head.css" />
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
<div class="wreg wzc" id="register">
	<div class="start1" style="display:block">
        <div class="rl">
            <div class="rlitem rltyp">
                <div class="rltext rltype" id="rltype">
                    <span class="on">个人注册</span>
                    <span class="">机构注册</span>
                    
                </div>
            </div>
            <div class="rlitem" id="personalName">
                <label>账号注册：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <em class="rname"></em>
                        <input type="text" class="rgray" dvalue="用户名/手机号码" value="用户名/手机号码" id="uName" maxlength="32"/>
                    </div>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem" id="entName">
                <label>单位名称：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <em class="rname"></em>
                        <input type="text" class="rgray" dvalue="请与贵单位登记证书注册名保持一致" value="请与贵单位登记证书注册名保持一致" id="entUName" maxlength="32"/>
                    </div>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem">
                <label>密码：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <em class="rpw"></em>
                        <input type="password" placeholder="密码" value="" id="uPw"/>
                    </div>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem">
                <label>确认密码：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <em class="rcpw"></em>
                        <input type="password" placeholder="确认密码" value="" id="cPw"/>
                    </div>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem rlcode">
                <label>验证码：</label>
                <div class="rltext">
                    <div class="rlinput">
                        <input type="text" class="" value="" id="wrcode"/>
                    </div>
                    <img src="<%=resource_url%>user/code.do?" id="codepic" class="codepic" alt="" />
                    <a title="" class="refreshCode"></a>
                    <span class="wrinfo"></span>
                </div>
            </div>
            <div class="rlitem rlcode" id="pCode">
            <label>手机验证码：</label>
            <div class="rltext">
                <div class="rlinput">
                    <input type="text" class="" value="" id="mcode"/>
                </div>
                <a class="wrsdcode" id="pSend">获取手机验证码</a>
                <span class="wrsdpt"></span>
            </div>
       		</div>
       		<input type="hidden" class=""  id="spreadCode" value="${companyCode}"/>
        	<%-- <div class="rlitem rlcode rlword">
                <label>助善口令：<span>（选项）</span></label>
                <div class="rltext">
                    <div class="rlinput">
                        <input type="text" class=""  id="spreadCode" value="${companyCode}"/>
                    </div>
                    <span class="wrsdpt">企业助善号召口令</span>
                    <span class="wrinfo"></span>
                </div>
            </div> --%>
            <div class="rlitem rlptl">
                <label></label>
                <div class="rltext ">
                    <label for="protocol"><input type="checkbox"  checked="checked" id="rule"/>我已同意善园基金会<a href="http://www.17xs.org/help/userService/" title="" target="_blank">《用户服务条款》</a></label>
                </div>
            </div>
            <div class="rlitem">
                <label></label>
                <div class="rltext rlpl">
                    <input type="button" id="signin" class="rlbtn yh"  value="立即注册"/>
                    <div class="rbtnPrim" id="rbtnPrim" style="display:none" >
                    认证所需资料:影印件必须为原件的扫描件和数码照
                    	<div class="">
                        	<p>·单位营业执照影印件</p>
                            <p>·组织机构代码证影印件</p>
                            <p>·对公银行账户（基本户、一般户均可）</p>
                            <p>·法人代表人的身份证影印件</p>
                        </div>
                    </div>
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
			<a href="http://www.17xs.org/" target="_blank">首页</a><i></i> <a href="http://www.17xs.org/help/about.do" target="_blank">关于善园基金会</a><i></i>
			<a href="http://www.17xs.org/help/service.do" target="_blank">服务协议</a><i></i> 
			<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">联系客服</a><i></i> 
			<a target="_blank" href="http://www.miitbeian.gov.cn/">浙ICP备15018913号-1</a>
		</p>
		<p>&nbsp;&nbsp;Copyright&nbsp;&nbsp;©&nbsp;&nbsp;&nbsp;&nbsp;宁波市善园公益基金会&nbsp;&nbsp;版权所有</p>
		<p>杭州智善网络科技有限公司&nbsp;&nbsp;提供技术支持</p>
</div>
<script data-main='<%=resource_url%>res/js/dev/entReg.js?v=2018' src="<%=resource_url%>res/js/require.min.js"></script> 
</body>
</html>