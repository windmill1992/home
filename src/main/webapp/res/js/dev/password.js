var base = window.baseurl;
var dataUrl = {
	changepsd: base + "user/changepassword.do",
	SSO_ExistName: base + "user/existuser.do", //检查用户名
	viewReset: base + "user/viewReset.do",
	cpassword: base + "user/cpassword.do",
	psdCheck: baseurl + 'user/psdCheck.do',
	SSO_MobileCode: base + 'user/phoneCode.do', //验证码请求
	backOk: base + 'user/bankReset.do', //返回成功页面
	sendEmail: base + '', //发送邮箱验证码
	EmailSubmit: base + '' //邮箱找回请求
};
require.config({
	baseUrl: base + "/res/js",
	paths: {
		"jquery": ["jquery-1.8.2.min"],
		"extend": "dev/common/extend",
		"dialog": "dev/common/dialog",
		"util": "dev/common/util",
		"head": "dev/common/headNew",
		"userCenter": "dev/common/userCenter",
		"entry": "dev/common/entryNew"
	},
	urlArgs: "v=20180209"
});
define(["extend", "dialog", "head", "userCenter", "entry"], function($, d, h, uc, en) {
	window.uCENnavEtSite = "p-password"; //如需导航显示当前页则加此设置
	window.mCENnavEtSite = "m-pwd"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init();
	uc.init();
	var wait = 60;
	var reg = {
		timer:null,
		timer2:null,
		init: function() {
			var that = this;
			var phone1 = $('#phoneNum').val(), phone2 = '';
			if($('#pageType').length > 0){
				if(phone1 == '' || phone1 == null){
					$('#pSend').addClass('disabled');
					$('#signin1').addClass('disabled');
					d.alert({content:'您输入的账号没有绑定手机<br/>请返回选择其他方式密码找回',type:'error',okFn:function(){
						location.href = 'http://www.17xs.org/user/changepassword.do?status=2&name='+ $('#username').val();
					}});
					/*setTimeout(function(){
						location.href = 'http://www.17xs.org/user/changepassword.do?status=2&name='+ $('#username').val();
					},2000);*/
					return;
				}	
				phone2 = phone1.substr(0,3) +'****'+ phone1.substr(7,4);
				$('#phone').val(phone2);
			}
			
			if($('#newpsPage').length > 0){
				if(phone1 != '' && phone1 != null)
				phone2 = phone1.substr(0,3) +'****'+ phone1.substr(7,4);
				$('#phone').val(phone2);
			}
			
			if($('#getWay').length > 0) {
				$('#getWay').attr('href', base + 'user/changepassword.do?status=2&name='+ $('#username').val());
			}
			$('#uName').focus(function() {
				$('#uName').next().html('<p class="info_normal">请输入登录名</p>');
				that.StrokesChange($(this), 'rgray', 0);
			});
			$('#uName').blur(function() {
				that.StrokesChange($(this), 'rgray', 1);
				isValidName($(this));
			});
			/*$('#phone').focus(function(){
				$('#phone').parent().next().html('<p class="info_normal">请输入手机号</p>');
				en.StrokesChange($(this),'rgray',0);
			});
			$('#phone').blur(function(){
				en.StrokesChange($(this),'rgray',1);
			});*/
			$('#mcode').focus(function() {
				$(this).next().html('');
			})
			$('#mcode').blur(function() {
//				that.isMobelCode($('#mcode'));
			})
			/*email*/
			$('#email').focus(function() {
				$(this).next().html('');
			})
			$('#email').blur(function() {
				that.emailCheck($('#email'), $('#email').next());
			})
			$('#emailCode').focus(function() {
				$(this).next().html('');
			})
			$('#emailCode').blur(function() {
				that.isMobelCode($('#emailCode'), 1)
			})
			$('#emailBtn').click(function() {
				that.emailSubmit();
			})
			/*updtaePwd*/
			$('#updataPawBtn').click(function() {
				that.updataPawData();
			})
			$('#oPw').focus(function() {
				$('#oPw').next().html('<p class="info_normal info">8~24个字符，限数字、大小写字母和下划线至少两种组合，不能以数字开头</p>');
			});
			$('#oPw').blur(function() {
				that.isOldPwd($(this));
			});
			/*id*/
			$('#realname').focus(function() {
				var msgObj = $(this).next('.wrinfo');
				msgObj.html('');
				$('#realname').next().html('<p class="info_normal">请输入至少2个中文</p>');
				that.StrokesChange($(this), 'rgray', 0);
			});
			$('#realname').blur(function() {
				var obj = $(this),
					v = obj.val(),
					msgObj = obj.next('.wrinfo');
				if(!v) {
					msgObj.html('<i class="error"></i>请输入真实姓名');
					return false;
				} else {
					msgObj.html('<i class="right"></i>');
					return true;
				}
			});
			$('#idCard').focus(function() {
				$('#idCard').next().html('<p class="info_normal">请输入18位的身份证号</p>');
			});
			$('#idCard').blur(function() {
				that.isVaildId($(this));
			});
			$('#uPw').focus(function() {
				$('#uPw').next().html('<p class="info_normal info">8~24个字符，限数字、大小写字母和下划线至少两种组合，不能以数字开头</p>');
			});
			$('#uPw').blur(function() {
				that.isValidPwd($(this));
			});
			$('#uPw').focus(function() {
				$('#uPw').parent().next().html('<p class="info_normal">请再次输入密码</p>');
			});
			$('#cPw').blur(function() {
				that.checkSurePwd($(this), $("#uPw"));
			});
			$('.refreshCode').click(function() {
				en.rashCode();
			});
			$('#signin').click(function() {
				queryPassWord($(this));
			});
			$('#signin1').click(function() {
				if($(this).hasClass('disabled')) return;
				checkTool($(this), 1);
			});
			$('#signin2').click(function() {
				checkTool($(this), 2);
			});
			$('#signin3').click(function() {
				Reset($(this));
			});
			$('#signin4').click(function() {
				newReset($(this));
			});
			$('#wrcode').blur(function() {
				that.isCode($('#wrcode'), 0);
			});
			$('body').keypress(function(e) {
				if(e.which == 13 && $("#signin").size() > 0) {
					en.fastSign($('#signin'));
				}
			});
			$('#psdPhone').hide();
			$('#psdrealname').hide();
			$('#psd_phone').click(function() {
				$('#psdPhone').show();
				$('#psdrealname').hide();
				$('#psdWay').hide();
			});
			$('#psd_sfz').click(function() {
				$('#psdrealname').show();
				$('#psdWay').hide();
				$('#psdPhone').hide();
			});
			
			$('body').on('click','#pSend',function(){
				
				if($(this).hasClass('disabled')) return;
				en.rashCode();
				$('#yzmDialog').addClass('show');
				
			}).on('click','#cancel',function(){
				
				$('#yzmDialog').removeClass('show');
				
			}).on('click','#sure',function(){
				var code = $('#yzmCode').val();
				var phone = $('#phoneNum').val();
				if($('#newpsPage').length > 0){
					var p = $('#phone').val();
					if(p.indexOf('*') == -1 && en.checkMobileStr(p)){
						phone = p;
					}
				}
				$('#yzmDialog').removeClass('show');
				that.sendcode2(code,phone,1);
				$('#mcode').removeAttr('readonly');
				
			}).on('click','.refreshCode',function(){
				en.rashCode();
			});
		},
		countDown:function(second) {
			var that = this;
			var str = "秒后重新获得验证码";
			$('#pSend').addClass('disabled');
			var setext = second + str;
			$("#pSend1").html(setext).fadeIn();
			that.timer2 = setInterval(function(){
				second--;
				if(second >= 0){
					setext = second + str;
					$("#pSend1").html(setext);
				}else{
					$('#pSend').removeClass('disabled');
					$("#pSend1").hide();
					clearInterval(that.timer2);
				}
			},1000);
		},
		StrokesChange: function(nameObj, cls, type) {
			var nameObj = $(nameObj),
				V = nameObj.val(),
				defaultV = nameObj.attr('dvalue');
			type ? V == '' ? nameObj.val(defaultV).addClass(cls) : '' : V == defaultV ? nameObj.val('').removeClass(cls) : '';
		},
		isCode: function(nameObj, type) {
			var nameObj = $(nameObj),
				msgObj = nameObj.parent().children('.wrinfo'),
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
		sendcode: function(obj, phoneobj, codeobj, phonecodeboj, type) {
			var that = this;
			if(wait < 60) {
				return false;
			}
			var phone = $(phoneobj).val(),
				msgObj = $(obj).next('.wrsdpt'),
				msgerror = $(codeobj).parent().children('.wrinfo'),
				msgerrorleg = $(phonecodeboj).parent().children('.wrinfo'),
				types, code;
			codeobj ? code = $(codeobj).val() : '';
			var cause = "用户注册验证码";
			if(!type) {
				var codes = that.isCode($(codeobj), 0);
				if(!codes) {
					return;
				}
			} else {
				var c = en.checkMobileStr(phone);
				if(!c) {
					return false;
				}
				types = "certification";
			}
			$.ajax({
				url: dataUrl.SSO_MobileCode,
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
		sendcode2:function(code,phone,type){
			var that = this, types = '';
			if(type){
				types = 'certification';
			}
			$.ajax({
				url: dataUrl.SSO_MobileCode,
				cache: false,
				type: "POST",
				data: {
					code: code,
					phone: phone,
					type: types
				},
				success: function(result) {
					if(result.flag == 1) {
						that.showTips('手机验证码发送成功，请查收！');
						that.countDown(60);
						
					} else if(result.flag == 0){
						that.showTips('验证码错误！');
						en.rashCode();
						setTimeout(function(){
							$('#yzmDialog').addClass('show');
						},2000);
					}
				},
				error: function() {
					that.showTips('手机验证码发送失败，请稍后再试！');
				}
			});
		},
		isMobelCode: function(obj, type) {
			var nameObj = $(obj),
				nameVal = nameObj.val(),
				msgObj = nameObj.next('.wrinfo');
			var len = nameVal.cnLength();
			var reg = /^\d{6}$/;
			if(reg.test(nameVal)) {
				msgObj.html('<i class="right"></i>');
				return true;
			} else {
				type ? msgObj.html('<i class="error"></i>邮箱验证码出错') : msgObj.html('<i class="error"></i>手机验证码出错');
				return false;
			}
		},
		isVaildId: function(IdObj) {
			IdObj = $(IdObj);
			var msgObj = IdObj.next(),
				IdVal = $.trim(IdObj.val()).toUpperCase();
			var rst = en.checkId(IdObj);
			if(rst != null) {
				msgObj.html('<i class="error"></i><p>' + rst + '</p>');
				return false;
			}
			if(IdVal == "" || IdVal.empty()) {
				msgObj.html('<i class="error"></i><p>请输入正确定的身份证件号码</p>');
				return false;
			} else {
				msgObj.html('<i class="right"></i>');
				return true;
			}
		},
		isValidRealname: function(nameObj) {
			nameObj = $(nameObj);
			var msgObj = nameObj.next();
			var rst = en.checkName(nameObj);
			if(rst) {
				msgObj.html('<i class="error"></i><p>' + rst + '</p>');
				return false;
			} else {
				msgObj.html('<i class="right"></i>');
				return true;
			}
		},
		emailCheck: function(obj, msgobj) {
			if(obj.val() == "") {
				msgobj.html('<i class="error"></i>邮箱不能为空!')
				return false;
			}
			if(!obj.val().match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/)) {
				msgobj.html('<i class="error"></i>邮箱格式不正确!')
				return false;
			}
			msgobj.html('<i class="right"></i>');
			return true;
		},
		sendEmail: function(obj) {
			var that = this,
				data, email = $(obj).val(),
				msgObj = $(obj).next('.wrsdpt'),
				msgerror = $(codeobj).parent().children('.wrinfo');
			data = {
				email: email,
				t: new Date().getTime()
			};
			$.ajax({
				url: dataUrl.sendEmail,
				data: data,
				success: function(result) {
					if(result.flag == 1) {
						msgObj.html('邮箱验证码发送成功，请查收！');
						msgerror.html('').hide();
						if(wait == 60) {
							$(obj).addClass("wrstate");
							en.time();
						}
					} else {
						msgerror.html(result.errorMsg).show();
					}
				},
				error: function(result) {
					msgerror.html('发送失败，请稍后重试。');
				}
			});
		},
		emailSubmit: function() {
			var that = this,
				email = $('#email').val(),
				code = $('#emailCode').val;
			var e = that.emailCheck($('#email'), $('#email').next());
			if(!e) {
				return false;
			}
			var c = that.isMobelCode($('#emailCode'), 1);
			if(!c) {
				return false;
			}
			$.ajax({
				url: dataUrl.EmailSubmit,
				data: data,
				success: function(result) {
					if(result.flag == 1) {
						//成功跳转
					} else {
						d.alert(result.errorMsg);
					}
				},
				error: function(result) {
					d.alert('发送失败，请稍后重试。');
				}
			});
		},
		isValidPwd: function(passObj) { //passId, promptId
			var passObj = $(passObj),
				msgObj = passObj.next(),
				passVal = passObj.val(),
				len = passVal.length;
			msgObj.html('');
			if(len == 0) {
				msgObj.html('<i class="error"></i><p>请输入新密码</p>');
				return false;
			} else if(len < 8) {
				msgObj.html('<i class="error"></i><p>您输入的新密码不足8位</p>');
				return false;
			}
			var checkReg = new RegExp("[\u4e00-\u9fa5]+");
			if(checkReg.test(passVal)) {
				msgObj.html('<i class="error"></i><p>新密码请勿包含中文</p>');
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
		},
		checkSurePwd: function(sureObj, passObj) { //checkConfirmPwd: function (confirmPassId, passId, promptId) {
			var sureObj = $(sureObj),
				passObj = $(passObj),
				msgObj = sureObj.next(),
				sureVal = sureObj.val(),
				pwdVal = passObj.val();
			if(sureVal.empty() || pwdVal != sureVal) {
				msgObj.html('<i class="error"></i><p>您两次输入的密码不一致</p>');
				return false;
			}
			msgObj.html('<i class="right"></i>');
			return true;
		},
		isOldPwd: function(passObj) { //passId, promptId
			var passObj = $(passObj),
				msgObj = passObj.next(),
				passVal = passObj.val(),
				len = passVal.length;
			msgObj.html('');
			if(len == 0 || len < 6 || len > 12) {
				msgObj.html('<i class="error"></i><p>请输入正确的旧密码</p>');
				return false;
			}
			var checkReg = new RegExp("[\u4e00-\u9fa5]+");
			if(checkReg.test(passVal)) {
				msgObj.html('<i class="error"></i><p>请输入正确的旧密码</p>');
				return false;
			}
			if(passVal == '123123' || passVal == '112233' || passVal == '111222' || passVal == 'abcabc' || passVal == 'aabbcc' || passVal == 'aaabbb') {
				msgObj.html('<i class="error"></i><p>请输入正确的旧密码</p>');
				return false;
			}
			if(passVal.replaceAll(passVal.charAt(0), '') == '') {
				msgObj.html('<i class="error"></i><p>请输入正确的旧密码</p>');
				return false;
			}
			if(isNaN(passVal)) { //非纯数字
				msgObj.html('<i class="right"></i>');
				return true;
			}
			//
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
				msgObj.html('<i class="error"></i><p>请输入正确的旧密码</p>');
				return false;
			};
			msgObj.html('<i class="right"></i>');
			return true;
		},
		updataPawData: function() {
			var dataArr = [],
				that = this,
				newpassWord = $('#uPw').val(),
				passWord = $('#oPw').val(),
				phone = $('#phoneNum').val(),
				mcode = $('#mcode').val(),
				code = $('#yzmCode').val();
			if($('#newpsPage').length > 0){
				var p = $('#phone').val();
				if(p.indexOf('*') == -1 && en.checkMobileStr(p)){
					phone = p;
				}
			}
			var isoPwd = that.isOldPwd($("#oPw"));
			if(!isoPwd) {
				that.showTips('请输入正确的旧密码！');
				$('#oPw').focus();
				return;
			}
			var isPwd = that.isValidPwd($("#uPw"));
			if(!isPwd) {
				$("#uPw").focus();
				return;
			}
			var isPwdSure = that.checkSurePwd($('#cPw'), $("#uPw"));
			if(!isPwdSure) {
				$('#cPw').focus();
				return;
			}
			if(!mcode || !/\d{6}/.test(mcode)){
				that.showTips('手机验证码错误！');
				$('#mcode').focus();
				return;
			}
			dataArr.push('status=5');
			dataArr.push('passWord=' + passWord);
			dataArr.push('newpassWord=' + newpassWord);
			dataArr.push('phone='+phone);
			dataArr.push('phoneCode='+mcode);
			dataArr.push('code='+code);
			// 执行重置密码
			$("#signin4").val("重置密码...").attr("disabled", "disabled");
			$.ajax({
				type: 'POST',
				url: dataUrl.cpassword,
				data: dataArr.join('&') + "&t=" + new Date().getTime(),
				cache: false,
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					if(!result.flag) {
						d.alert({
							content: result.errorMsg,
							type: 'error'
						});
						return false;
					} else {
						d.alert({
							content: "密码修改成功!",
							type: 'ok',
							okFn: function() {
								location.reload()
							}
						});
					}
				},
				error: function(result) {
					en.rashCode();
					d.alert({
						content: '修改密码失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
						type: 'error'
					});
				}
			});
		},
		showTips:function(txt){
			var that = this;
			var $msg = $('#msg');
			if(!$msg.is(':hidden')){
				$msg.hide();
				clearTimeout(that.timer);
				that.timer = null;
			}
			$msg.html(txt).fadeIn();
			that.timer = setTimeout(function(){
				$msg.fadeOut();
			},2000);
		}
	};
	reg.init();

	function isValidName(nameObj) { //nameId, promptId
		var that = this,
			nameObj = $(nameObj),
			msgObj = nameObj.next(''),
			nameVal = nameObj.val(),
			len = nameVal.cnLength();
		if(len == 0) {
			msgObj.html('<i class="error"></i><p>请输入用户名</p>').show();
			return false;
		}
		if(len < 4 || len > 32) {
			msgObj.html('<i class="error"></i><p>用户名长度应为4-32位字符</p>').show();
			return false;
		}
		var nameReg = new RegExp("^([a-z|A-Z]+|[ \u4e00-\u9fa5]+|[0-9]+|[_|_]+)+$");
		if(!nameReg.test(nameVal)) {
			msgObj.html('<i class="error"></i><p>账号由中文、数字、英文（区分大小写）组成</p>');
			return false;
		}
		var reg = /^[1-9]\d*$|^0$/,
			c = en.checkMobileStr(nameVal);
		if(reg.test(nameVal) == true && len != 11 || !c && len == 11 && reg.test(nameVal) == true) {
			msgObj.html('<i class="error"></i><p>用户名不能为除手机号码以外的纯数字</p>');
			return false;
		}
		var data = {},
			temp = false;
		data = {
			name: nameVal,
			t: new Date().getTime()
		};
		$.ajax({
			url: dataUrl.SSO_ExistName,
			data: data,
			async: false,
			success: function(result) {
				if(result.flag == 0) {
					msgObj.html('<i class="right"></i>');
					temp = true;
				} else {
					msgObj.html('<i class="error"></i><p>用户名不存在</p>');
					temp = false;
				}
			}
		});
		return temp;
	}

	function queryPassWord(obj) {
		var that = this,
			p = [],
			name = $('#uName').val(),
			passWord = $('#uPw').val(),
			code = $('#wrcode').val(),
			phoneCode = $('#mcode').val(),
			isName = isValidName($("#uName"));
		if(!isName) {
			$("#signin").val("立即注册").removeAttr("disabled");
			return;
		}
		var codes = en.isCode($("#wrcode"), 0);
		if(!codes) {
			$("#signin").val("立即注册").removeAttr("disabled");
			return;
		}
		p.push('name=' + name);
		p.push('code=' + code);
		p.push('status=2');
		// 执行注册
		$.ajax({
			type: 'POST',
			url: dataUrl.psdCheck,
			data: p.join('&') + "&t=" + new Date().getTime(),
			cache: false,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success: function(result) {
				if(!result.flag) {
					d.alert({
						content: result.errorMsg,
						type: 'error'
					});
					en.rashCode();
				} else {
					location.href = dataUrl.changepsd + "?status=" + result.obj.status + "&name=" + result.obj.name;
				}
				$("#signin").removeAttr("disabled");
			},
			error: function(result) {
				en.rashCode();
				d.alert({
					content: '注册失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
					type: 'error'
				});
				$("#signin").val("立即注册").removeAttr("disabled");
			}
		});
	}

	function checkTool(obj, way) {
		var p = [],
			name = $('#username').val(),
			code = $('#wrcode').val(),
			phoneCode = $('#mcode').val(),
			phone = $('#phone').val(),
			rn = $('#realname').val(),
			icd = $('#idCard').val();
		p.push('name=' + name);
		if(way == 1) {
			var pe = en.isphone($("#phoneNum"));
			if(!pe) {
				return;
			}
			var mc = reg.isMobelCode($("#mcode"));
			if(!mc) {
				return;
			}
			if($('#pageType').length > 0){
				phone = $('#phoneNum').val();
				code = $('#yzmCode').val();
			}
			p.push('phone=' + phone);
			p.push('phoneCode=' + phoneCode);
			p.push('status=4');
		} else if(way == 2) {
			var realname = reg.isValidRealname($("#realname"));
			if(!realname) {
				return;
			}
			var idCard = reg.isVaildId($("#idCard"));
			if(!idCard) {
				return;
			}
			var codes = reg.isCode($("#wrcode"), 0);
			if(!codes) {
				return;
			}
			p.push('realname=' + rn);
			p.push('idCard=' + icd);
			p.push('code=' + code);
			p.push('status=3');
		} else {
			d.alert({
				content: '验证失败，缺少参数。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
				type: 'error'
			});
		}
		// 执行注册
		$.ajax({
			type: 'POST',
			url: dataUrl.psdCheck,
			data: p.join('&') + "&t=" + new Date().getTime(),
			cache: false,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success: function(result) {
				if(!result.flag) {
					d.alert({
						content: result.errorMsg,
						type: 'error'
					});
					en.rashCode();
				} else {
					if(result.obj == 1) {
						d.alert({
							content: result.errorMsg + "身份证",
							type: 'success'
						});
					} else if(result.obj == 2) {
						d.alert({
							content: result.errorMsg + "手机",
							type: 'success'
						});
					}
					location.href = dataUrl.viewReset + "?" + p.join('&');
				}
			},
			error: function(result) {
				en.rashCode();
				d.alert({
					content: '注册失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
					type: 'error'
				});
			}
		});
	}

	function Reset(obj) {
		var p = [],
			that = this,
			name = $('#name').val(),
			code = $('#wrcode').val(),
			phoneCode = $('#mcode').val(),
			phone = $('#phone').val(),
			rn = $('#realname').val(),
			passWord = $('#uPw').val(),
			icd = $('#idCard').val(),
			status = $('#status').val();
		var isPwd = en.isValidPwd($("#uPw"));
		if(!isPwd) {
			return;
		}
		var isPwdSure = reg.checkSurePwd($('#cPw'), $("#uPw"));
		if(!isPwdSure) {
			return;
		}
		p.push('name=' + name);
		p.push('phone=' + phone);
		p.push('phoneCode=' + phoneCode);
		p.push('realname=' + rn);
		p.push('idCard=' + icd);
		p.push('code=' + code);
		p.push('status=' + status);
		p.push('passWord=' + passWord);
		// 执行重置密码
		$("#signin3").val("重置密码...").attr("disabled", "disabled");
		$.ajax({
			type: 'POST',
			url: dataUrl.cpassword,
			data: p.join('&') + "&t=" + new Date().getTime(),
			cache: false,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success: function(result) {
				if(!result.flag) {
					d.alert({
						content: result.errorMsg,
						type: 'error'
					});
					en.rashCode();
				} else {
					var di = $(obj).parents(".cp2yDialogBox").attr("data");
					that.isClose = false;
					window.location.href = dataUrl.backOk;
				}
				$("#signin3").val("下一步").removeAttr("disabled");
			},
			error: function(result) {
				en.rashCode();
				d.alert({
					content: '注册失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
					type: 'error'
				});
				$("#signin").val("立即注册").removeAttr("disabled");
			}
		});
	}

	function GetRequest() {
		var url = location.search; //获取url中"?"符后的字串 
		var theRequest = new Object();
		if(url.indexOf("?") != -1) {
			var str = url.substr(1);
			strs = str.split("&");
			for(var i = 0; i < strs.length; i++) {
				theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
			}
		}
		return theRequest;
	}

	function newReset(obj) {
		var p = [],
			that = this,
			newpassWord = $('#uPw').val(),
			passWord = $('#oPw').val(),
			isPwd = en.isValidPwd($("#uPw"));
		if(!isPwd) {
			return;
		}
		var isoPwd = en.isValidPwd($("#oPw"));
		if(!isoPwd) {
			return;
		}
		var isPwdSure = en.checkSurePwd($('#cPw'), $("#uPw"));
		if(!isPwdSure) {
			return;
		}
		p.push('status=5');
		p.push('passWord=' + passWord);
		p.push('newpassWord=' + newpassWord);
		// 执行重置密码
		$("#signin4").val("重置密码...").attr("disabled", "disabled");
		$.ajax({
			type: 'POST',
			url: dataUrl.cpassword,
			data: p.join('&') + "&t=" + new Date().getTime(),
			cache: false,
			contentType: "application/x-www-form-urlencoded; charset=utf-8",
			success: function(result) {
				if(!result.flag) {
					d.alert({
						content: result.errorMsg,
						type: 'error'
					});
					en.rashCode();
				} else {
					var di = $(obj).parents(".cp2yDialogBox").attr("data");
					that.isClose = false;
				}
			},
			error: function(result) {
				en.rashCode();
				d.alert({
					content: '注册失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',
					type: 'error'
				});
				$("#signin").val("立即注册").removeAttr("disabled");
			}
		});
	}
});