<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>善园 - 一起行善，温暖前行！</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<meta name="format-detection" content="telephone=no">
	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ include file="./../../common/file_url.jsp" %>
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/base.css">
	<link rel="stylesheet" type="text/css" href="../../../../res/css/h5/userBinding.css?v=20180207">
	<script src="../../../../res/js/jquery-1.8.2.min.js" type="text/javascript"></script>
	<script type="text/javascript">
		document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.2 + 'px';
	</script>
</head>
<style type="text/css">
	.wx_bindInfo {
		margin: 40px auto 0;
		width: 1.1rem;
		text-align: center;
		font-size: 0.25rem;
	}
	.wx_bindInfo img {
		width: 0.9rem;
		border: none;
	}
</style>

<body>
	<div class="loginBg">
		<img src="../../../../res/images/h5/images/topbg2.png">
	</div>
	<div class="logoForm">
		<div class="form-item">
			<i class="user-icon"></i>
			<input type="text" placeholder="请输入账号" id="username">
		</div>
		<div class="form-item">
			<i class="pwd-icon"></i>
			<input type="password" placeholder="请输入密码" id="pwd">
			<a href="../../../../user/changepassword.do" class="findPwsA">忘记密码</a>
		</div>
		<div class="form-item">
			<i class="yzm-icon"></i>
			<input type="number" placeholder="验证码" id="code">
			<a href="javascript:;" class="refresh"><img src="http://www.17xs.org/user/code.do?type=login" id="codepic" /></a>
		</div>
		<div class="bindInfo bindInfo1"><input type="button" value="登&nbsp;&nbsp;&nbsp;录" class="loginBtn" id="goin"></div>
		<p class="bindInfo">
			<a href="../../../../user/sso_h5.do?flag=${flag}">您还没有账号，马上注册</a>
		</p>
		<c:if test="${browser=='wx' }">
			<div class="wx_bindInfo">
				<c:choose>
					<c:when test="${flag=='huzhu' }">
						<a href="../../../../mutualAid/wxLogin.do">
							<img src="../../../../res/images/h5/images/userBinding/wxlogin.png" />
							<p>微信登录</p>
						</a>
					</c:when>
					<c:otherwise>
						<a href="../../../../ucenter/userCenter_h5.do">
							<img src="../../../../res/images/h5/images/userBinding/wxlogin.png" />
							<p>微信登录</p>
						</a>
					</c:otherwise>
				</c:choose>
			</div>
		</c:if>
	</div>
	<input type="hidden" value="${flag }" id="flag">
	<%--<ul class="otherLogin clearfix">--%>
	<%--<li class="loginItem">--%>
	<%--<a href="" id = "wxLogin">--%>
	<%--<img src="../../../../res/images/h5/images/userBinding/wx-icon.png">--%>
	<%--<p>微信登录</p>--%>
	<%--</a>--%>
	<%--</li>--%>
	<%--<!-- --%>
	<%--<li class="loginItem">--%>
	<%--<a href="">--%>
	<%--<img src="../../../../res/images/h5/images/userBinding/zfb-icon.png">--%>
	<%--<p>支付宝登录</p>--%>
	<%--</a>--%>
	<%--</li>--%>
	<%--<li class="loginItem">--%>
	<%--<a href="">--%>
	<%--<img src="../../../../res/images/h5/images/userBinding/qq-icon.png">--%>
	<%--<p>QQ登录</p>--%>
	<%--</a>--%>
	<%--</li>--%>
	<%---->--%>
	<%--</ul>--%>
	<!-- 注册错误信息提示 -->
	<div class="errorBox" style="display: none; top: 3.4rem;" id="checkInfo"></div>
</body>
<script type="text/javascript">
	var base = window.baseurl;
	var WebAppUrl = {
		SSO_Login: base + 'user/login.do', //登陆
		SSO_SignOut: base + 'user/loginout.do', //退出登陆
		personCenter: base + 'ucenter/userCenter_h5.do',
		//WechatLogin   : base+'ucenter/user/login_wechat.do'
		WechatLogin: base + 'ucenter/wechat_login.do'
	};
	var login = {
		init: function() {
			var that = this;
			$("body").on("click", "#goin", function() {
				that.goIn($(this),2);

			}).on("click", "#signout", function() {
				that.signOut($(this));

			}).on("click", "#codepic", function() {
				that.rashCode('login');

			}).on("click", "#username, #pwd, #code", function() {
				$('#checkInfo').hide();

			}).on("click", "#wxLogin", function() {
				that.login_wx();

			});
		},
		goIn: function(obj, type) {
			var name = $("#username"),
				namev = name.val(),
				pwd = $("#pwd"),
				pwdv = pwd.val(),
				code = $("#code"),
				codev = code.val();
				
			if(codev == '' || codev.length != 4){
				$('#checkInfo').html('验证码错误');
				$('#checkInfo').show();	
				return false;
			}
			this.login(namev, pwdv, codev, '', type);

		},
		login: function(namev, pwdv, codev, di, type) { //登入  nameObj,pwdObj,codeObj  
			var that = this,
				redirectFlag = $("#flag").val();
			!type ? type = 1 : '';
			$.ajax({
				url: WebAppUrl.SSO_Login,
				data: {
					name: namev,
					passWord: pwdv,
					code: codev,
					type: type,
					t: new Date().getTime()
				},
				cache: false,
				success: function(result) {
					if(!result.flag) {
						$('#checkInfo').html(result.errorMsg);
						$('#checkInfo').show();
					} else {
						if(redirectFlag == 'monthlyDonate') {
							window.location = 'http://www.17xs.org/user/getDonateTime.do';
						} else if(redirectFlag.substring(0, 9) == 'projectId') {
							var strings = redirectFlag.split('_');

							if(redirectFlag.indexOf("_extensionPeople_") != -1) {
								window.location = 'http://www.17xs.org/project/view_h5.do?projectId=' + redirectFlag.substring(10, redirectFlag.indexOf("_extensionPeople_")) +
									'&extensionPeople=' + redirectFlag.substring(redirectFlag.lastIndexOf("_") + 1);
							} else {
								window.location = 'http://www.17xs.org/project/view_h5.do?projectId=' + strings[1];
							}
						} else if(redirectFlag.substring(0, 3) == 'pub') {
							window.location = 'http://www.17xs.org/newReleaseProject/project_detail.do?projectId=' + redirectFlag.substring(16, redirectFlag.indexOf("_extensionPeople_")) +
								'&extensionPeople=' + redirectFlag.substring(redirectFlag.lastIndexOf("_") + 1);
						} else if(redirectFlag == 'myMonthlyDonate') {
							window.location = 'http://www.17xs.org/user/getMyDonateTime.do';
						} else if(redirectFlag == 'batchDonate') {
							window.location = 'http://www.17xs.org/project/batch_list.do?extensionPeople=';
						} else if(redirectFlag == 'releaseH5Project') {
							window.location = 'http://www.17xs.org/project/releaseH5Project.do';
						} else if(redirectFlag.substring(0, 5) == 'audit') { //传进来的redirectFlag格式为audit_projectId_1279_type_0_extensionPeople_1032
							//0:new的项目详情页面  1:医院发布的项目详情页面
							var strings = [];
							strings = redirectFlag.split('_');

							$.ajax({
								url: 'http://www.17xs.org/uCenterProject/isOrNotAuditProject.do',
								async: false,
								data: {
									projectId: strings[2],
									type: strings[4]
								},
								success: function(result) {
									if(result.flag == 1) {
										//已经证实
										var url = 'http://www.17xs.org/newReleaseProject/project_detail.do?projectId=' + strings[2];
										if(strings[6] != "" && strings[6] != undefined) {
											url = url + '&extensionPeople=' + strings[4];
										}
										window.location = url;
									} else if(result.flag == 0) {
										window.location.href = result.errorMsg;
									} else if(result.flag == -2) {
										//未登录,先登录
										window.location.href = result.errorMsg;
									} else if(result.flag == -1) {
										window.location.href = result.errorMsg;
									}
								},
								error: function() {
									//请求异常
									alert("请求发生异常")
								}
							});
						} else if(redirectFlag.substring(0, 8) == 'leadword') {
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/newReleaseProject/project_detail.do?projectId=' + strings[1];
							window.location = url;
						} else if(redirectFlag == "protocolRelease") {
							window.location = 'http://www.17xs.org/test/gotoProtocolRelease.do';
						} else if(redirectFlag.substring(0, 8) == "activity") {
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/activity/activityDetail.do?id=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 15) == 'gardenProjectId') {
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/h5GardenProject/projectDetail.do?projectId=' + strings[1];
							window.location = url;
						} else if(redirectFlag == 'gardenList') {
							window.location = 'http://www.17xs.org/h5GardenProject/project_list.do';
						} else if(redirectFlag.substring(0, 12) == 'entryFormNew') { //报名entryFormNew_15
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/resposibilityReport/entryFormNewView.do?id=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 15) == 'appointmentForm') { //预约appointmentForm_16
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/resposibilityReport/appointmentForm_view.do?id=' + strings[1];
							window.location = url;
						} else if(redirectFlag == 'huzhu') { //互助首页huzhu
							var url = 'http://hz.17xs.org/huzhu/index';
							window.location = url;
						} else if(redirectFlag.substring(0, 6) == 'oneAid') { //一对一帮扶
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/uCenterProject/planSet_view.do?projectId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 6) == 'survey') { //问答
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/survey/survey.html?formId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 8) == 'entryPay') { //活动报名
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/visitorAlipay/tenpay/entryPay.html?activityId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 11) == 'entryDetail') { //活动报名明细
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/uCenter/entryDetail.html?activityId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 13) == 'managementpub') { //发布活动团队详情
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/uCenter/pubManagement.html';
							window.location = url;
						} else if(redirectFlag.substring(0, 15) == 'managemententry') { //报名活动详情
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/uCenter/entryManagement.html';
							window.location = url;
						} else if(redirectFlag.substring(0, 7) == 'charity') { //团队详情
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/uCenter/charity.html?activityUserId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 6) == 'signIn') { //签到记录
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/uCenter/groupSignInRecord.html?activityId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 7) == 'editAct') { //活动发布
							var strings = [];
							strings = redirectFlag.split('_');
							if(strings[1]) {
								var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/activity/activityEdit.html?activityId=' + strings[1];
							} else {
								var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/activity/activityEdit.html';
							}
							window.location = url;
						} else if(redirectFlag.substring(0, 9) == 'condition') { //报名条件
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/activity/entryCondition.html?activityId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 6) == 'volReg') { //志愿者登记
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/register/volReg.html';
							window.location = url;
						} else if(redirectFlag.substring(0, 8) == 'groupReg') { //团队登记
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/webhtml/view/h5/volunteer/register/groupReg.html';
							window.location = url;
						} else if(redirectFlag.substring(0, 6) == 'export') { //云湖项目
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/export/view_h5.do?projectId=' + strings[1] + '&extensionPeople=' + strings[3];
							window.location = url;
						} else if(redirectFlag.substring(0, 11) == 'yunhuDonate') { //云湖项目捐赠
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/export/donate_detail_view.do?projectId=' + strings[1] + '&extensionPeople=' + strings[3];
							window.location = url;
						} else if(redirectFlag.substring(0, 7) == 'special') { //专项基金
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/uCenterProject/specialProject_details.do?projectId=' + strings[1] + '&extensionPeople=' + strings[3];
							window.location = url;
						} else if(redirectFlag.substring(0, 9) == 'dayDonate') { //日日捐
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/uCenterProject/dayDonateProject_details.do?projectId=' + strings[1] + '&extensionPeople=' + strings[3];
							window.location = url;
						} else if(redirectFlag.substring(0, 11) == 'raiseDetail') { //日日捐
							var strings = [];
							strings = redirectFlag.split('_');
							var url = 'http://www.17xs.org/together/raiseDetail_view.do?projectId=' + strings[1];
							window.location = url;
						} else if(redirectFlag.substring(0, 10) == 'toAddress') { //新增地址
							var url = 'http://www.17xs.org/user/toAddress.do?type=6';
							window.location = url;
						} else {
							window.location = WebAppUrl.personCenter;
						}
					}
				},
				error: function(r) {
					//d.alert({content:'登录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
		signOut: function() {
			var that = this;
			this.isIn = false;
			$.ajax({
				url: WebAppUrl.SSO_SignOut,
				data: {
					ajax: true,
					t: new Date().getTime()
				},
				cache: false,
				success: function(result) {
					if(result.flag == 1) {
						window.location = WebAppUrl.HOME_URL;
						that.signIn(false, "");
					}
				}.bind(this),
				error: function(result) {
					d.alert({
						content: '登录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
						type: 'error'
					});
				}
			});

		},
		login_wx: function() { //登入 
			var that = this;
			$.ajax({
				url: WebAppUrl.WechatLogin,
				//data    : {name:namev,passWord:pwdv,code:codev,type:type,t:new Date().getTime()}, 
				cache: false,
				success: function(result) {
					if(!result.flag) {
						window.location = 'http://www.17xs.org/ucenter/login_H5.do';
					} else {
						window.location = WebAppUrl.personCenter;
					}

				},
				error: function(r) {
					window.location = 'http://www.17xs.org/ucenter/login_H5.do';
				}
			});
		},
		rashCode: function(v) {
			var _random;
			if(v) {
				_random = base + '/user/code.do?type=' + v + '&t=' + new Date().getTime() + 'a' + Math.random();
			} else {
				_random = base + '/user/code.do?t=' + new Date().getTime() + 'a' + Math.random();
			}
			$('#codepic').attr('src', _random); //刷新验证码
		},
	}
	login.init();
</script>
</html>