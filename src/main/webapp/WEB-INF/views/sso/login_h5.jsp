<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="./../common/file_url.jsp" %>
<meta name="keywords" content=""/>
<meta name="description" content=""/>
<meta name="viewport"/>
<title>用户中心</title>
<link href="<%=resource_url%>res/css/h5/login.css" rel="stylesheet" type="text/css">
<script src="<%=resource_url%>res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
<script>
/*
	$(document).ready(function(){
		$(".login_tabs a").each(function(index,domEle){ 
			$(domEle).click(function(){	
				$(".login_tabs a").removeClass("cur");
				$(this).addClass("cur");
				$(".login_box ul").hide();		
				$(".login_box #form_"+Math.floor(index+1)).show();
  			});
		});
	});	
	*/
</script>
</head>

<body>
<div id="pageContainer">
<!--  <div class="top_logo"><img src="images/logo_login.jpg" width="174" height="100"></div>-->
  <div class="login_box">
  	<div class="login_tabs"><a href="#" class="tab_name cur">普通登录</a><a href="#" class="tab_tel">手机登录</a></div>
    	<ul id="form_1">
	    <li>
	      <input type="text" class="usr" value="输入用户名" id="username">
        </li>
	    <li>
	      <input type="text" class="psw" value="输入密码" id="pwd">
        </li>
	    <li>
	      <input type="button" class="confirm" value="确定" id="goin">
        </li>
      </ul>
	
      <div class="login_ft">
      	<div class="reg_link">还没有账号？<a href="#">注册新用户</a></div>
        <div class="other_link"><span>其它登录方式：</span><a href="#" class="qq"></a><a href="#" class="wx"></a></div>
      </div>
	  <div class="clear"></div>
  </div>
   <!--
  <div class="login_box">
    <div class="login_tabs"><a href="#" class="tab_name">普通登录</a><a href="#" class="tab_tel cur">手机登录</a></div>
    <ul>
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="get_code" value="重新获取">
        </li>
	    <li style="height:60px;padding:5px 0px;">
	      <input type="button" class="confirm" value="确定">
        </li>
      </ul>
	  <div class="clear"></div>
  </div>
	<div class="login_box">
	  <div class="login_tabs"><a href="#" class="tab_name">普通登录</a><a href="#" class="tab_tel cur">手机登录</a></div>
	  <ul>
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="reget_code" value="60秒后重新发送">
        </li>
	    <li style="height:45px;padding:5px 0px;">
	      <input type="button" class="confirm" value="确定">
        </li>
	  </ul>
	  <div class="clear"></div>
  </div>
	<div class="register_box">
	  <ul>
      	<li class="title">用户注册</li>
	    <li>
	      <input type="text" class="tel" value="输入手机号">
        </li>
	    <li>
	      <input type="text" class="psw" value="输入密码">
        </li>
	    <li>
	      <input type="text" class="code" value="输入短信验证码">
	      <input type="button" class="reget_code" value="60秒后重新发送">
        </li>
	    <li style="height:45px;padding:5px 0px;">
	      <input type="button" class="confirm" value="确定">
        </li>
      </ul>
	  <div class="clear"></div>
  </div>
 -->
</div>
</body>
<script type="text/javascript">
	var base=window.baseurl;
	var WebAppUrl={
		SSO_Login    : base+'user/login.do',//登陆
		SSO_SignOut   : base+'user/loginout.do',//退出登陆
		personCenter   : base+'ucenter/core/personalCenter_h5.do'//退出登陆
	};
	var login={
		init:function()
		{
			var that=this;
			$("body").on("click","#goin",function(){
				 that.goIn($(this));   
			}).on("click","#signout",function(){
				 that.signOut($(this));
			})
		},
		goIn:function(obj,type){
		       var name = $("#username"),namev=name.val(),pwd = $("#pwd"),pwdv=pwd.val(),code=$("#code"),codev=code.val(); 
			   if(namev=='' || namev.length<4||namev.length>32){
			         alert('账号填写错误，账号长度为4～32个字符。');
					 return;
			   } 
			   if(pwdv=='' || pwdv.length<6||pwdv.length>12){
			        alert('密码填写错误，密码长度必须为6～12字符。');
					return;
			   }
			  this.login(namev,pwdv,codev,'',type);
			   
		}, 
		login:function(namev,pwdv,codev,di,type){//登入  nameObj,pwdObj,codeObj  
		         var that = this;  
				 !type?type=1:'';
				 $.ajax({
					url     : WebAppUrl.SSO_Login,
					data    : {name:namev,passWord:pwdv,code:codev,type:type,t:new Date().getTime()}, 
					cache   : false, 
					success : function(result){ 
						if(!result.flag){
							//$('#warn').html("<em></em>"+result.errorMsg);
							alert('登陆失败请稍候再试...');
						}else{
							
							window.location = WebAppUrl.personCenter;
							//d.close(di);
							//that.isIn = true; 
							//that.errorTimes=0; 
							//that.signIn(true,result.obj.name,result.obj.id); 
						   // that._lgCallBack&&that._lgCallBack();//登录后回调函数 
						}
					},
					error   : function(r){ 
						//d.alert({content:'登录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
					}
				}); 
		  },
		  signOut:function(){
			    var that = this; 
				this.isIn=false;
				$.ajax({ 
					url     :WebAppUrl.SSO_SignOut,
					data    :{ajax:true,t:new Date().getTime()},
					cache   : false,
					success : function(result){
						if(result.flag==1){
							window.location = WebAppUrl.HOME_URL;
							that.signIn(false,"");
						}
					}.bind(this),
					error   : function(result){d.alert({content:'登录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});}
				});
			 
		  }  
	}
	login.init();
</script>
</html>