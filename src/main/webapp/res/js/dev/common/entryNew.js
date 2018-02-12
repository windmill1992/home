define(["extend", "dialog"], function($, d) {
	var base = window.baseurl;
	var WebAppUrl = {
		SSO_ExistName: base + "user/existuser.do", //检查用户名
		SSO_FastIn: base + 'user/register.do', //注册请求
		SSO_Login: base + 'user/login.do', //登陆
		SSO_SignOut: base + 'user/loginout.do', //退出登陆
		SSO_MobileCode: base + 'user/phoneCode.do', //验证码请求
		HOME_URL: base,
		HOME_USER_Balance: base + 'user/ajaxGetAvailableMoney.htm',
		HOME_CheckLogin: base + 'user/checkLogin.do', //回调函数 
		spreadCode: base + 'enterprise/verifyHelpCode.do' //口令
	};
	var wait = 60;
	var entry = {
		signWay: 0,
		init: function() {
			var that = this;
			that.checkLogin();
			$("body").on("click", "#hd-login", function() {
				that.show();
			}).on("click", "#goin", function() {
				that.goIn($(this));
			}).on("click", "#signout", function() {
				that.signOut($(this));
			}).on("mouseover", ".hd-user", function() {
				$("#hd-quit").show();
				$("#hd-userName").hide();
			}).on("mouseout", ".hd-user", function() {
				$("#hd-quit").hide();
				$("#hd-userName").show();
			}).on("click", ".closeDialogd", function() {
				var di = $(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("click", "#personalCenter", function() {
				var name = $.cookie.get("user_type");
				if(name == 'enterpriseUsers') {
					window.location.href = window.baseurl + "ucenter/core/enterpriseCenter.do";
				} else {
					window.location.href = window.baseurl + "ucenter/core/personalCenter.do";
				}
			}).on("focus", "#username", function() {
				var v = $(this).val();
				dv = $(this).attr('dValue');
				v == dv ? $(this).val('').removeClass('rgray') : '';
			}).on("blur", "#username", function() {
				var v = $(this).val();
				dv = $(this).attr('dValue');
				v == '' ? $(this).val(dv).addClass('rgray') : '';
			})
			$('body').keypress(function(e) {
				if(e.which == 13 && $(".loginDialog").size() > 0) {
					that.goIn($("#goin"));
				}
			});
		},
		checkLogin: function() { //是否登录  url:参数  type:0 是否登录  1:登录后回调函数
			var that = this,
				html = [];
			$.ajax({
				url: WebAppUrl.HOME_CheckLogin,
				data: {
					t: new Date().getTime()
				},
				success: function(result) { //eval(result);//回调函数  参数后台来 
					var isIn = false,
						name = $.cookie.get("sjj_username");
					if(result.flag == 1) {
						isIn = true;
					}
					that.signIn(isIn, name);
				}
			});
		},
		StrokesChange: function(nameObj, cls, type) {
			var nameObj = $(nameObj),
				V = nameObj.val(),
				defaultV = nameObj.attr('dvalue');
			type ? V == '' ? nameObj.val(defaultV).addClass(cls) : '' : V == defaultV ? nameObj.val('').removeClass(cls) : '';
		},
		toAttest: function(name, type, donate) {
			var html = [],
				pConnection = base + "ucenter/core/mygood.do";
			html.push('<div class="wrokbx yh">');
			if(donate) {
				html.push('<div class="title"><a href="' + pConnection + '" title="">' + donate.userName + '</a> 已成功为您助捐<span class="red">' + donate.perMoney + '</span>元，助捐项目——”<a href="' + base + "project/view.do?projectId=" + donate.projectId + '" class="fblod" title="">' + donate.project_title + '</a>“，感谢有你！</div>');
			}
			html.push('<div class="wrok yh">');
			html.push('<h2><span class="red">' + name + '</span>，恭喜您注册成功</h2>');
			html.push('<p>实名认证会使您的账户更安全 <a href="' + WebAppUrl.HOME_URL + 'user/realname.do" title="">[立即验证]</a></p>');
			html.push('<div class="wrokft">');
			html.push('<a href="' + WebAppUrl.HOME_URL + 'project/index.do" title="" class="wrokbtn">行善大厅</a><a href="' + WebAppUrl.HOME_URL + '/project/appealFirest.do" title="" class="wrokhome">我要求助</a>');
			html.push('</div>');
			html.push('</div></div>');
			$("#register").html(html.join(''));
		},
		spreadCode: function(obj) {
			var that = this,
				nameObj = $(obj),
				nameVal = nameObj.val(),
				len = nameVal.cnLength(),
				msgObj = nameObj.parent().parent().children('.wrinfo'),
				primObj = nameObj.parent().parent().children('.wrsdpt');
			if(len == 10) {
				return true;
			} else {
				primObj.html('');
				msgObj.html('<i class="error"></i><p>助善号格式不正确</p>');
				return false;
			}
		},
		fastSign: function(obj, type) {
			var that = this,
				p = {},
				name, type = type,
				passWord = $('#uPw').val(),
				code = $('#wrcode').val(),
				phoneCode = $('#mcode').val(),
				spreadCode = $('#spreadCode ').val();
			if(type == 0) {
				name = $.trim($('#uName').val());
				var isName = this.isValidName($("#uName"));
				if(!isName) {
					return;
				}
			} else {
				name = $.trim($('#entUName').val());
				var isName = this.isValidEntName($("#entUName"));
				if(!isName) {
					return;
				}
			}
			var isPwd = this.isValidPwd($("#uPw"));
			if(!isPwd) {
				return;
			}
			var isPwdSure = this.checkSurePwd($('#cPw'), $("#uPw"));
			if(!isPwdSure) {
				return;
			}
			p.type = type;
			if(!this.signWay || type == 1) {
				var codes = this.isCode($("#wrcode"), 0);
				if(!codes) {
					return;
				}
				p.name = name;
			} else {
				var codes = this.isCode($("#wrcode"), 0);
				if(!codes) {
					return;
				}
				var mobelcode = this.isMobelCode($("#mcode"));
				if(!mobelcode) {
					return;
				}
				p.phone = name;
				p.phoneCode = phoneCode;
			}
			if(type == 0 && spreadCode.length == 10) {
				var d1 = that.spreadCode($('#spreadCode'));
				if(!d1) {
					return false;
				}
			}
			var isRule = $("#rule").attr("checked");
			if(!isRule) {
				$("#signin").val("立即注册").removeAttr("disabled");
				d.alert({
					content: "对不起，请先同意我们的服务条款才可以完成注册"
				});
				return false;
			}
			// p.push('passWord='+passWord);
			//p.push('code='+code);
			p.passWord = passWord;
			p.code = code;
			//type==0?p.push('spreadCode ='+spreadCode):'';
			type == 0 ? (p.spreadCode = spreadCode) : '';
			p.t = new Date().getTime();
			if(type == 0 && spreadCode.length == 10) {
				var nameObj = $('#spreadCode'),
					primObj = nameObj.parent().parent().children('.wrsdpt'),
					msgObj = nameObj.parent().parent().children('.wrinfo');
				$.ajax({
					url: WebAppUrl.spreadCode,
					data: {
						code: $('#spreadCode').val(),
						t: new Date().getTime()
					},
					success: function(result) { //eval(result);//回调函数  参数后台来 
						if(result.flag == 0) {
							primObj.html('');
							msgObj.html('<i class="error"></i><p>' + result.errorMsg + '</p>');
							return false;
						} else {
							$.ajax({
								type: 'POST',
								url: WebAppUrl.SSO_FastIn,
								data: p,
								cache: false,
								contentType: "application/x-www-form-urlencoded; charset=utf-8",
								success: function(result) {
									if(!result.flag) {
										d.alert({
											content: result.errorMsg,
											type: 'error'
										});
										that.rashCode();
									} else {
										if(type == 0) {
											var di = $(obj).parents(".cp2yDialogBox").attr("data");
											that.isClose = false;
											that.login(result.obj.name, passWord, '', di);
											that.toAttest(result.obj.name, 1, result.donate);
										} else {
											window.location.href = "http://www.17xs.org/enterprise/core/realName.do"; //跳转企业认证页面
										}
									}
								},
								error: function(result) {
									that.rashCode();
									d.alert({
										content: '注册失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
										type: 'error'
									});
									$("#signin").val("立即注册").removeAttr("disabled");
								}
							});
						}
					}
				});
			} else {
				// 执行注册
				$.ajax({
					type: 'POST',
					url: WebAppUrl.SSO_FastIn,
					//	data    : p.join('&')+"&t="+new Date().getTime(),
					data: p,
					cache: false,
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(result) {
						if(!result.flag) {
							d.alert({
								content: result.errorMsg,
								type: 'error'
							});
							that.rashCode();
						} else {
							if(type == 0) {
								var di = $(obj).parents(".cp2yDialogBox").attr("data");
								that.isClose = false;
								that.login(result.obj.name, passWord, '', di);
								that.toAttest(result.obj.name, 1, result.donate);
							} else {
								window.location.href = "http://www.17xs.org/enterprise/core/realName.do"; //跳转企业认证页面
							}
						}
					},
					error: function(result) {
						that.rashCode();
						d.alert({
							content: '注册失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
							type: 'error'
						});
						$("#signin").val("立即注册").removeAttr("disabled");
					}
				});
			}
		},
		login0penUrl: function(url) { //登录->回调函数
			var that = this;
			$.ajax({
				url: WebAppUrl.HOME_CheckLogin1,
				data: {
					booleanValue: true,
					t: new Date().getTime()
				},
				success: function(result) {
					var openUrl = url.indexOf("?") == -1 ? url + "?t=" + new Date().getTime() : url + "&t=" + new Date().getTime();
					//openUrl = WebAppUrl.HOME_URL+openUrl;
					openUrl = 'http://www.cp2y.com' + openUrl;
					if(result != 'false') { //登录直接跳转 
						window.location = openUrl;
					} else { //未登录 登录后跳转
						that._lgCallBack = function() {
							window.location = openUrl;
						};
						that.show();
					}
				}
			});
		},
		show: function(callback) {
			var redirect_url = window.location.href;
			if(redirect_url == 'http://www.17xs.org/') {
				redirect_url = "";
			} else {
				redirect_url = redirect_url.replace("http://www.17xs.org", "").replace(/&/g, "!");
			}
			var html = [],
				visitor = $('#visitor').val();
			html.push('<a href="javascript:;" class="closeDialogd"></a>');
			html.push('<h2></h2>');
			html.push('<p class="yh">一起行善，让爱与温暖携手共进</p>');
			html.push('<div class="lprim" id="warn"></div>');
			html.push('<div class="group"><input type="text" id="username" dvalue="用户名/手机号码" value="用户名/手机号码" class="rgray"></div>');
			html.push('<div class="group"><input type="password" id="pwd"  dvalue="密码" value=""  placeholder="密码" ></div>');
			html.push('<div class="group"><input type="number" id="code" placeholder="验证码" />');
			html.push('<div class="code-pic"><img src="http://www.17xs.org/user/code.do?type=login" id="codepic" /><a href="javascript:;" id="refreshCode"></a></div></div>');
			visitor ? html.push('<div class="group ogroup"><input id="goin" class="goin goinDetail" type="button" value="登录"><span class="line"></span><a href="' + WebAppUrl.HOME_URL + 'visitorAlipay/visitorpay.do?projectId=' + $('#projectId').val() + '&pName=' + $('#pName').val() + '&amount=' + $('#amount').val() + '" title="快捷捐赠" class="aothor">快捷捐赠</a></div>') : html.push('<div class="group"><input id="goin" class="goin" type="button" value="登录"></div>');;
			html.push('<div class="group lfoot"><a href="' + WebAppUrl.HOME_URL + 'user/changepassword.do" class="wjpw" target="_blank">忘记密码>></a>还没有注册善园基金会？<a href="' + WebAppUrl.HOME_URL + 'user/sso.do">立即注册>></a></div>');
			html.push('<div id="login_container" ></div>');
			//html.push('<p class="pc_text">微信扫一扫登录善园</p>');
			var o = {
					t: "",
					c: html.join("")
				},
				css = {
					width: '525px'
				};
			if(callback) {
				this._lgCallBack = callback;
			}
			d.open(o, css, "loginDialog");
			//取到上次登录的用户名
			var cookeName = $.cookie.get("sjj_username");
			if(cookeName) {
				$("#username").val(cookeName).removeClass('rgray');
			}
			var obj = new WxLogin({
				id: "login_container",
				appid: "wxe4a86dee2b974b99",
				scope: "snsapi_login",
				redirect_uri: "http%3a%2f%2fwww.17xs.org%2fsso%2fwxLoad.do",
				//state : "STATE#wechat_redirect",
				state: redirect_url,
				style: "",
				href: ""
			});
			$('#login_container').val(obj);
		},
		goIn: function(obj, type) {
			var that = this,
				name = $("#username"),
				namev = name.val(),
				pwd = $("#pwd"),
				pwdv = pwd.val(),
				code = $("#code"),
				codev = code.val();
				
			if(code.length > 0 && (isNaN(codev) || codev.length != 4)){
				$('#warn').html('<em></em>验证码错误').show();
				code.focus();
				return;
			}
			$('#warn').html('正在登录，请稍后...').show();
			var di = $(obj).parents(".cp2yDialogBox").attr("data");
			setTimeout(function(){
				that.login(namev, pwdv, codev, di, type);
			},1000);
		},
		login: function(namev, pwdv, codev, di, type) { //登入  nameObj,pwdObj,codeObj  
			var that = this;
			!type ? type = 2 : '';
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
						$('#warn').html("<em></em>" + result.errorMsg);
					} else {
						d.close(di);
						that.isIn = true;
						that.errorTimes = 0;
						that.signIn(true, result.obj.name, result.obj.id);
						that._lgCallBack && that._lgCallBack(); //登录后回调函数 
					}
				},
				error: function(r) {
					d.alert({
						content: '登录失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
						type: 'error'
					});
				}
			});
		},
		parseInRst: function(data) {
			if(data.match("^-1")) {
				return loginResult = {
					state: -1,
					error: data.substring(3, data.length).split("|")[0],
					errorTimes: data.substring(3, data.length).split("|")[1]
				};
			}
			if(data.match("^-2")) {
				return loginResult = {
					state: -2,
					error: data.substring(3, data.length).split("|")[0],
					errorTimes: data.substring(3, data.length).split("|")[1]
				};
			}
			return {
				state: 0,
				returnUrl: data.split("|")[0],
				loginname: data.split("|")[1]
			}
		},
		signIn: function(isIn, name, id) {
			var that = this,
				html = [],
				head = $.cookie.get("user_head");
			this.isIn = isIn;
			if(isIn) {
				if(head && head != "good") {
					html.push('<span class="onLogin"><a id="personalCenter"><em class="blackText" id="hd-userName">' + that.autoAddEllipsis(name, 10) + '</em></a><a href="javascript:;" title="退出" class="blackText" id="signout">退出</a></span>')
				} else {
					html.push('<span class="onLogin"><a id="personalCenter"><em class="blackText" id="hd-userName">' + that.autoAddEllipsis(name, 10) + '</em></a><a href="javascript:;" title="退出" class="blackText" id="signout">退出</a></span>')
				}
				$("#enter").html(html.join(''));
			} else {
				//newhead onlogin
				html.push('<span class="noLogin"><a href="' + WebAppUrl.HOME_URL + 'user/sso.do" target="_blank" title="注册" class="regBtn"><em class="iconNew iconPen"></em>注册</a><a href="javascript:;" id="hd-login" title="登录" class="loginBtn"><em class="iconNew iconPerson"></em>登录</a></span>');
				$("#enter").html(html.join(''));
			}
			this.inMustFun && this.inMustFun(); //必调函数
		},
		autoAddEllipsis: function(pStr, pLen) {
			var that = this;
			var _ret = that.cutString(pStr, pLen);
			var _cutFlag = _ret.cutflag;
			var _cutStringn = _ret.cutstring;
			if("1" == _cutFlag) {
				return _cutStringn + "...";
			} else {
				return _cutStringn;
			}
		},
		cutString: function(pStr, pLen) {
			var that = this;
			// 原字符串长度
			var _strLen = pStr.length;
			var _tmpCode;
			var _cutString;
			// 默认情况下，返回的字符串是原字符串的一部分
			var _cutFlag = "1";
			var _lenCount = 0;
			var _ret = false;
			if(_strLen <= pLen / 2) {
				_cutString = pStr;
				_ret = true;
			}
			if(!_ret) {
				for(var i = 0; i < _strLen; i++) {
					if(that.isFull(pStr.charAt(i))) {
						_lenCount += 2;
					} else {
						_lenCount += 1;
					}
					if(_lenCount > pLen) {
						_cutString = pStr.substring(0, i);
						_ret = true;
						break;
					} else if(_lenCount == pLen) {
						_cutString = pStr.substring(0, i + 1);
						_ret = true;
						break;
					}
				}
			}
			if(!_ret) {
				_cutString = pStr;
				_ret = true;
			}
			if(_cutString.length == _strLen) {
				_cutFlag = "0";
			}
			return {
				"cutstring": _cutString,
				"cutflag": _cutFlag
			};
		},
		isFull: function(pChar) {
			if((pChar.charCodeAt(0) > 128)) {
				return true;
			} else {
				return false;
			}
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
		sendcode: function(obj, phoneobj, codeobj, phonecodeboj, type) {
			var that = this;
			if(wait < 60) {
				return false;
			}
			var phone = $(phoneobj).val(),
				msgObj = $(obj).next('.wrsdpt'),
				msgerror = $(codeobj).parent().parent().children('.wrinfo'),
				msgerrorleg = $(phonecodeboj).parent().parent().children('.wrinfo'),
				types, code;
			codeobj ? code = $(codeobj).val() : '';
			var cause = "用户注册验证码";
			if(phone == '' || phone == '用户名/手机号码') {
				msgerror.html('<i class="error"></i>请输入正确的手机号码！');
				$(phoneobj).focus();
				return;
			}
			if(type == 0) {
				var codes = this.isCode($(codeobj), 0);
				if(!codes) {
					return;
				}
			} else if(type == 1) {
				var c = that.checkMobileStr(phone);
				if(!c) {
					return false;
				}
				types = "certification";
			} else {
				var c = that.checkMobileStr(phone);
				if(!c) {
					return false;
				}
				types = "enterprise_validate";
			}
			$.ajax({
				url: WebAppUrl.SSO_MobileCode,
				cache: false,
				type: "POST",
				data: {
					code: code,
					phone: phone,
					cause: cause,
					type: types
				},
				success: function(result) {
					if(result.flag == 1) {
						msgObj.html('手机验证码发送成功，请查收！');
						msgerrorleg.html('').hide();
						//d.alert({content: "手机验证码发送成功，请查收！"});
						if(wait == 60) {
							$(obj).addClass("wrstate");
							that.time();
						}
					} else {
						msgerrorleg.html(result.errorMsg).show();
						$(phonecodeboj).focus();
					}
				},
				error: function(result) {
					msgerrorleg.html('发送失败，请稍后重试。');
				}
			});
		},
		time: function() {
			if(wait == 0) {
				$("#pSend").html("获取手机验证码"); //改变对象中的内容
				$("#pSend").next('.wrsdpt').html('');
				$("#pSend").removeClass("wrstate");
				wait = 60;
			} else {
				wait--;
				setTimeout(function() {
					entry.time();
				}, 1000); //循环调用
				$("#pSend").html(wait + "秒后重新获取验证码"); //改变对象中的内容
			}
		},
		isCode: function(nameObj, type) {
			var nameObj = $(nameObj),
				msgObj = nameObj.parent().parent().children('.wrinfo'),
				nameVal = nameObj.val();
			var len = nameVal.cnLength();
			if(isNaN(nameVal)) { //非纯数字
				msgObj.html('<i class="error"></i><p>验证码不正确</p>').show();
				return false;
			} else {
				if(len < 4 || len > 4) {
					msgObj.html('<i class="error"></i><p>验证码不正确</p>').show();
					return false;
				} else {
					msgObj.html('').hide();
					return true;
				}
			}
		},
		isMobelCode: function(obj) {
			var nameObj = $(obj),
				nameVal = nameObj.val();
			var len = nameVal.cnLength();
			var reg = /^\d{6}$/;
			if(reg.test(nameVal)) {
				return true;
			} else {
				d.alert({
					content: "手机验证码出错"
				});
				return false;
			}
		},
		isphone: function(nameObj) {
			var that = this,
				nameObj = $(nameObj),
				msgObj = nameObj.parent().next(''),
				nameVal = nameObj.val();
			var len = nameVal.cnLength();
			var reg = /^[1-9]\d*$|^0$/;
			if(reg.test(nameVal) == true && len != 11) {
				msgObj.html('<i class="error"></i><p>手机号码格式不正确</p>');
				return false;
			}
			var data = {},
				c = that.checkMobileStr(nameVal);
			if(!c) {
				msgObj.html('<i class="error"></i><p>请输入正确的手机号码</p>');
				return false;
			} else {
				msgObj.html('<i class="right"></i>');
				return true;
			}
		},
		checkMobileStr: function(str) {
			if(str.length != 11) return false;
			var re = /^(13[0-9]|14[579]|15[0-3,5-9]|166|17[0135678]|18[0-9])\d{8}$/;
			if(!re.test(str)) {
				return false;
			}
			return true;
		},
		isValidName: function(nameObj) { //nameId, promptId
			var that = this,
				nameObj = $(nameObj),
				msgObj = nameObj.parent().next(''),
				nameVal = nameObj.val();
			var len = nameVal.cnLength();
			if(len == 0) {
				//nameObj.val('用户名/手机号码').addClass('gray'); 
				msgObj.html('<i class="error"></i><p>请输入用户名</p>').show();
				return false;
			}
			if(len < 4 || len > 32) {
				msgObj.html('<i class="error"></i><p>用户名长度应为4-32位字符</p>').show();
				return false;
			}
			var nameReg = new RegExp("^([a-z|A-Z]+|[ \u4e00-\u9fa5]+|[0-9]+|[_|_]+)+$");
			if(!nameReg.test(nameVal)) {
				msgObj.html('<i class="error"></i><p>账号可用中文、数字、英文（区分大小写）组成</p>');
				return false;
			}
			var reg = /^[1-9]\d*$|^0$/,
				c = that.checkMobileStr(nameVal);
			if(reg.test(nameVal) == true && len != 11 || !c && len == 11 && reg.test(nameVal) == true) {
				msgObj.html('<i class="error"></i><p>用户名不能为除手机号码以外的纯数字</p>');
				return false;
			}
			var data = {};
			if(c == true) {
				this.signWay = 1;
			} else {
				this.signWay = 0;
			}
			data = {
				name: nameVal,
				t: new Date().getTime()
			};
			$.ajax({
				url: WebAppUrl.SSO_ExistName,
				data: data,
				//async:false,//同步
				success: function(result) {
					if(!result.flag) {
						msgObj.html('<i class="error"></i><p>' + result.errorMsg + '</p>');
					} else {
						msgObj.html('<i class="right"></i>').show();
						if(entry.signWay == 1) {
							$("#pCode").show();
						} else {
							$("#pCode").hide();
						}
					}
				}
			});
			return true;
		},
		isValidEntName: function(nameObj) { //nameId, promptId
			var that = this,
				nameObj = $(nameObj),
				msgObj = nameObj.parent().next(''),
				nameVal = nameObj.val();
			var len = nameVal.cnLength();
			if(nameVal == "请与贵公司营业执照注册名保持一致") {
				msgObj.html('<i class="error"></i><p>请输入公司名称！</p>').show();
				return false;
			}
			if(len < 4 || len > 36) {
				//nameObj.val('用户名/手机号码').addClass('gray'); 
				msgObj.html('<i class="error"></i><p>公司名称长度应为4-32个字符组成！</p>').show();
				return false;
			}
			data = {
				name: nameVal,
				t: new Date().getTime()
			};
			$.ajax({
				url: WebAppUrl.SSO_ExistName,
				data: data,
				//async:false,//同步
				success: function(result) {
					if(!result.flag) {
						msgObj.html('<i class="error"></i><p>' + result.errorMsg + '</p>');
					} else {
						msgObj.html('<i class="right"></i>').show();
					}
				}
			});
			return true;
		},
		rashCode: function(v) {
			var _random;
			if(v) {
				_random = WebAppUrl.HOME_URL + '/user/code.do?type=' + v + '&t=' + new Date().getTime() + 'a' + Math.random();
			} else {
				_random = WebAppUrl.HOME_URL + '/user/code.do?t=' + new Date().getTime() + 'a' + Math.random();
			}
			$('#codepic').attr('src', _random); //刷新验证码
		},
		isValidRealname: function(nameObj) {
			nameObj = $(nameObj);
			var msgObj = nameObj.parent().next();
			var rst = this.checkName(nameObj);
			if(rst) {
				msgObj.html('<i class="error"></i><p>' + rst + '</p>');
				return false;
			} else {
				msgObj.html('<i class="right"></i>');
				return true;
			}
		},
		isVaildId: function(IdObj) {
			IdObj = $(IdObj);
			var msgObj = IdObj.parent().next(),
				IdVal = $.trim(IdObj.val()).toUpperCase();
			var rst = this.checkId(IdObj);
			if(rst != null) {
				msgObj.html('<i class="error"></i><p>' + rst + '</p>');
				return false;
			}
			if(IdVal == "" || IdVal.empty()) {
				msgObj.html('<i class="error"></i><p>请输入正确的身份证件号码</p>');
				return false;
			} else {
				msgObj.html('<i class="right"></i>');
				return true;
			}
		},
		checkSureId: function(sureObj, passObj) { //checkConfirmPwd: function (confirmPassId, passId, promptId) {
			var sureObj = $(sureObj),
				passObj = $(passObj),
				msgObj = sureObj.next(),
				sureVal = sureObj.val(),
				pwdVal = passObj.val();
			if(sureVal.empty() || pwdVal != sureVal) {
				msgObj.html('<i class="error"></i><p>您两次输入的身份证号码不一致</p>');
				return false;
			}
			msgObj.html('<i class="right"></i>');
			return true;
		},
		isValidPwd: function(passObj) { //passId, promptId
			var passObj = $(passObj),
				msgObj = passObj.parent().next(),
				passVal = passObj.val(),
				len = passVal.length;
			msgObj.html('');
			if (len==0) { 
				msgObj.html('<i class="error"></i><p>请输入密码</p>'); 
	            return false;
	        }else if (len < 8) { 
				msgObj.html('<i class="error"></i><p>您输入的密码不足8位</p>'); 
	            return false;
	        }
			var checkReg = new RegExp("[\u4e00-\u9fa5]+");
	        if (checkReg.test(passVal)) { 
				msgObj.html('<i class="error"></i><p>密码请勿包含中文</p>'); 
				return false;
	        }
	        var reg = /^[a-zA-Z_](?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?![_]+$)[_0-9A-Za-z]{7,23}$/;
	        if(!reg.test(passVal)){
	        	msgObj.html('<i class="error"></i><p>您输入的密码格式不正确，请重新输入</p>'); 
				return false;
	        }else{
	        	msgObj.html('<i class="right"></i>');  
				return true;
	        }
			/*if(passVal == '123123' || passVal == '112233' || passVal == '111222' || passVal == 'abcabc' || passVal == 'aabbcc' || passVal == 'aaabbb') {
				msgObj.html('<i class="error"></i><p>您输入的密码太简单，请重新输入</p>');
				return false;
			}
			if(passVal.replaceAll(passVal.charAt(0), '') == '') {
				msgObj.html('<i class="error"></i><p>您输入的密码太简单，请重新输入</p>');
				return false;
			}
			if(isNaN(passVal)) { //非纯数字
				msgObj.html('<i class="right"></i>');
				return true;
			}
			var array = passVal.split("");
			var sortDirection = 1; //默认升序
			var len = array.length;
			var n0 = parseInt(array[0]);
			if(parseInt(array[0]) > parseInt(array[len - 1])) {
				sortDirection = -1; //降序
			}
			var isnotContinuation = false;
			for(var i = 0; i < len; i++) {
				if(parseInt(array[i]) !== (n0 + i * sortDirection)) {
					isnotContinuation = true;
					break;
				}
			}
			if(!isnotContinuation) {
				msgObj.html('<i class="error"></i><p>您输入的密码太简单，请重新输入</p>');
				return false;
			};
			msgObj.html('<i class="right"></i>');
			return true;*/
		},
		checkSurePwd: function(sureObj, passObj) { //checkConfirmPwd: function (confirmPassId, passId, promptId) {
			var sureObj = $(sureObj),
				passObj = $(passObj),
				msgObj = sureObj.parent().next(),
				sureVal = sureObj.val(),
				pwdVal = passObj.val();
			if(sureVal.empty() || pwdVal != sureVal) {
				msgObj.html('<i class="error"></i><p>您两次输入的密码不一致</p>');
				return false;
			}
			msgObj.html('<i class="right"></i>');
			return true;
		},
		checkName: function(nameObj) {
			nameObj = $(nameObj);
			var name = $.trim(nameObj.val()),
				len = name.length;
			if(len == 0) {
				return '请填写您的真实姓名';
			}
			if(name.cnLength() < 3 || name.cnLength > 30) {
				return '请填写您的真实姓名';
			}
			var nameReg = new RegExp("^([a|A-z|Z]+|[ \u4e00-\u9fa5]+|[\s]+|[\.|\·]+)+$");
			if(!nameReg.test(name)) {
				return "请填写您的真实姓名";
			}
			return null;
		},
		checkId: function(IdObj) {
			IdObj = $(IdObj);
			var IdVal = $.trim(IdObj.val()),
				len = IdVal.length;
			if(len < 4) {
				return '请正确输入身份证号';
			}
			if(len != 15 && len != 18) {
				return '身份证号码应为15位或18位';
			}
			var idResult = this.checkIDCard(IdVal);
			if(idResult != 0) {
				return "身份证号码有误，请检查确认";
			}
			if(len == 18) {
				var age = 0;
				var date = new Date();
				var year = date.getFullYear();
				var month = date.getMonth() + 1;
				var day = date.getDate();
				if(month > parseInt(IdVal.substr(10, 2))) // 判断当前月分与编码中的月份大小
					age = year - IdVal.substr(6, 4);
				else if(month = parseInt(IdVal.substr(10, 2)) && day >= parseInt(IdVal.substr(12, 2))) age = year - IdVal.substr(6, 4);
				else age = year - IdVal.substr(6, 4) - 1;
				//不需要判断年龄
				/*if(age<18)
				  return "未满18周岁不允许购买彩票";*/
			}
			return null;
		},
		checkIDCard: function(idcard) {
			var vcity = {
				11: "北京",
				12: "天津",
				13: "河北",
				14: "山西",
				15: "内蒙古",
				21: "辽宁",
				22: "吉林",
				23: "黑龙江",
				31: "上海",
				32: "江苏",
				33: "浙江",
				34: "安徽",
				35: "福建",
				36: "江西",
				37: "山东",
				41: "河南",
				42: "湖北",
				43: "湖南",
				44: "广东",
				45: "广西",
				46: "海南",
				50: "重庆",
				51: "四川",
				52: "贵州",
				53: "云南",
				54: "西藏",
				61: "陕西",
				62: "甘肃",
				63: "青海",
				64: "宁夏",
				65: "新疆",
				71: "台湾",
				81: "香港",
				82: "澳门",
				91: "国外"
			};
			var errors = ['0', '身份证号码位数不对!', '身份证号码出生日期超出范围或含有非法字符!', '身份证号码校验错误!', '身份证地区非法!'];
			checkCard = function(obj) {
				//校验长度，类型 
				if(isCardNo(obj) === false) {
					return errors[1];
				}
				//检查省份 
				if(checkProvince(obj) === false) {
					return errors[4];
				}
				//校验生日 
				if(checkBirthday(obj) === false) {
					return errors[2];
				}
				//检验位的检测 
				if(checkParity(obj) === false) {
					return errors[3];
				}
				return errors[0];
			};
			//检查号码是否符合规范，包括长度，类型 
			isCardNo = function(obj) {
				//身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X 
				var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/;
				if(reg.test(obj) === false) {
					return false;
				}
				return true;
			};
			//取身份证前两位,校验省份 
			checkProvince = function(obj) {
				var province = obj.substr(0, 2);
				if(vcity[province] == undefined) {
					return false;
				}
				return true;
			};
			//检查生日是否正确 
			checkBirthday = function(obj) {
				var len = obj.length;
				//身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字 
				if(len == '15') {
					var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
					var arr_data = obj.match(re_fifteen);
					var year = arr_data[2];
					var month = arr_data[3];
					var day = arr_data[4];
					var birthday = new Date('19' + year + '/' + month + '/' + day);
					return verifyBirthday('19' + year, month, day, birthday);
				}
				//身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X 
				if(len == '18') {
					var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
					var arr_data = obj.match(re_eighteen);
					var year = arr_data[2];
					var month = arr_data[3];
					var day = arr_data[4];
					var birthday = new Date(year + '/' + month + '/' + day);
					return verifyBirthday(year, month, day, birthday);
				}
				return false;
			};
			//校验日期 
			verifyBirthday = function(year, month, day, birthday) {
				var now = new Date();
				var now_year = now.getFullYear();
				//年月日是否合理 
				if(birthday.getFullYear() == year && (birthday.getMonth() + 1) == month && birthday.getDate() == day) {
					//判断年份的范围（3岁到100岁之间) 
					var time = now_year - year;
					if(time >= 0 && time <= 130) {
						return true;
					}
					return false;
				}
				return false;
			};
			//校验位的检测 
			checkParity = function(obj) {
				//15位转18位 
				obj = changeFivteenToEighteen(obj);
				var len = obj.length;
				if(len == '18') {
					var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
					var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
					var cardTemp = 0,
						i, valnum;
					for(i = 0; i < 17; i++) {
						cardTemp += obj.substr(i, 1) * arrInt[i];
					}
					valnum = arrCh[cardTemp % 11];
					if(valnum == obj.substr(17, 1)) {
						return true;
					}
					return false;
				}
				return false;
			};
			//15位转18位身份证号 
			changeFivteenToEighteen = function(obj) {
				if(obj.length == '15') {
					var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
					var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
					var cardTemp = 0,
						i;
					obj = obj.substr(0, 6) + '19' + obj.substr(6, obj.length - 6);
					for(i = 0; i < 17; i++) {
						cardTemp += obj.substr(i, 1) * arrInt[i];
					}
					obj += arrCh[cardTemp % 11];
					return obj;
				}
				return obj;
			};
			return checkCard(idcard);
		},
		isIDFormat: function(idcard) {
			var errors = ['0', '身份证号码位数不对!', '身份证号码出生日期超出范围或含有非法字符!', '身份证号码校验错误!', '身份证地区非法!'];
			var area = {
				11: "北京",
				12: "天津",
				13: "河北",
				14: "山西",
				15: "内蒙古",
				21: "辽宁",
				22: "吉林",
				23: "黑龙江",
				31: "上海",
				32: "江苏",
				33: "浙江",
				34: "安徽",
				35: "福建",
				36: "江西",
				37: "山东",
				41: "河南",
				42: "湖北",
				43: "湖南",
				44: "广东",
				45: "广西",
				46: "海南",
				50: "重庆",
				51: "四川",
				52: "贵州",
				53: "云南",
				54: "西藏",
				61: "陕西",
				62: "甘肃",
				63: "青海",
				64: "宁夏",
				65: "新疆",
				71: "台湾",
				81: "香港",
				82: "澳门",
				91: "国外"
			};
			var idcard, Y, JYM, S, M;
			var idcard_array = idcard.split('');
			//地区检验
			if(!area[parseInt(idcard.substr(0, 2))]) return errors[4];
			//身份号码位数及格式检验
			switch(idcard.length) {
				case 15:
					var ereg;
					if((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 || ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;
					else ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;
					if(ereg.test(idcard)) return errors[0];
					else return errors[2];
					break;
				case 18:
					if(parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; //闰年出生日期的合
					else ereg = /^[1-9][0-9]{5}(19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; //平年出生日期的合法性正则表达式
					if(ereg.test(idcard)) {
						S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7 + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9 + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10 + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5 + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8 + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4 + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2 + parseInt(idcard_array[7]) * 1 + parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9]) * 3;
						Y = S % 11;
						M = "F";
						JYM = "10X98765432";
						M = JYM.substr(Y, 1); //判断校验位 
						//下面if else 校验身份证最后一位 00后身份证校验不成功 暂时去除判断
						/*if(M == idcard_array[17])
							return errors[0]; //检测ID的校验位 
						else 
							return errors[3];*/
						return errors[0];
					} else return errors[2];
					break;
				default:
					return errors[1];
			}
		}
	};
	return entry;
});