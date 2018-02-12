var base = window.baseurl;
var dataUrl = {
	SSO_Login: base + 'user/login.do'
};
require.config({
	baseUrl: window.baseurl + "res/js",
	paths: {
		"jquery": ["jquery-1.8.2.min"],
		"extend": "dev/common/extend",
		"dialog": "dev/common/dialog",
		"util": "dev/common/util",
		"head": "dev/common/head",
		"entry": "dev/common/entry",
		"slide": "util/slide"
	},
	urlArgs: "v=20180206"
});
define(["extend", "dialog", "head", "entry", "slide"], function($, d, h, en, sld) {
	h.init();
	d.init();
	en.init();
	var login = {
		type: 0,
		init: function() {
			var that = this;
			if($('#username').val() != '用户名/手机号码') {
				$('#username').removeClass('rgray');
			}
			$('#username').focus(function() {
//				$('#username').parent().next().html('<p class="info_normal">请输入用户名、手机号</p>');
				en.StrokesChange($(this), 'rgray', 0);
			});
			$('#username').blur(function() {
				en.StrokesChange($(this), 'rgray', 1);
//				that.isValidName($('#username'));
			});
			/*$('#pwd').focus(function() {
				$('#pwd').parent().next().html('<p class="info_normal info">8~24个字符，限数字、大小写字母和下划线至少两种组合，不能以数字开头</p>');
			});
			$('#pwd').blur(function() {
				en.isValidPwd($('#pwd'));
			});*/
			$('#code').blur(function() {
				en.isCode($('#code'), 0);
			});
			$('.refreshCode').click(function() {
				en.rashCode('login');
			});
			$('#signin').click(function() {
				that.goIn($(this), 2);
			});
			$('body').keypress(function(e) {
				if(e.which == 13 && $("#signin").size() > 0) {
					that.goIn($("#signin"), 2);
				}
			});
		},
		getQueryString: function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return r.input.replace("entrance=", "");
			return null;
		},
		goIn: function(obj, type) {
			var that = this,
				name = $("#username"),
				namev = name.val(),
				pwd = $("#pwd"),
				pwdv = pwd.val(),
				code = $("#code"),
				codev = code.val();
			
			var codes = en.isCode($('#code'), 0);
			if(!codes) {
				return false;
			}
			var returnUrl = that.getQueryString("entrance");
			that.login(namev, pwdv, codev, returnUrl, type);
		},
		login: function(namev, pwdv, codev, returnUrl, type) {
			!type ? type = 1 : '';
			var that = this;
			$.ajax({
				url: dataUrl.SSO_Login,
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
						d.alert({
							content: result.errorMsg,
							type: 'error'
						});
						en.rashCode('login');
					} else {
						en.isIn = true;
						en.errorTimes = 0;
						if(returnUrl != null) {
							returnUrl = unescape(returnUrl);
							window.location = returnUrl;
						} else {
							window.location = base;
						}
					}
				},
				error: function(r) {
					en.rashCode('login');
					d.alert({
						content: '登录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
						type: 'error'
					});
				}
			});
		}
	}
	login.init();
});