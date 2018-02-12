var base=window.baseurl;
var dataUrl={
	SSO_ExistName:base+"user/existuser.do",//检查用户名
	SSO_FastIn   : base+'user/register.do' ,//注册请求
	SSO_MobileCode:base+'user/phoneCode.do'//验证码请求
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20180206"
});
define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	h.init();
	d.init();
	en.init(); 
	uc.init();
	
	var reg = {
		type:0,
		timer:null,
		timer2:null,
		init:function(){
			var that = this;
			
			$('body').on('click','#pSend',function(){
					
				if($(this).hasClass('disabled')) return;
				
				var uname = $('#nickName').val(), phone = $('#uPhone').val();
				if(uname == ''){
					that.showTips('请输入昵称！');
					$('#nickName').focus();
					return;
				}
				if(phone == '' || !that.checkMobileStr(phone)){
					that.showTips('请输入正确的手机号！');
					return;
				}
				if(!that.isValidName(phone)){
					that.showTips('用户已存在！');
					$('#uPhone').focus();
					return;
				}
				en.rashCode();
				$('#yzmDialog').addClass('show');
				$('#yzmCode').val('').focus();
				
			}).on('click','#cancel',function(){
				
				$('#yzmDialog').addClass('hide');
				setTimeout(function(){
					$('#yzmDialog').removeClass('show hide');
					$('#yzmCode').val('');
				},400);
				
			}).on('click','#sure',function(){
				var code = $('#yzmCode').val();
				var phone = $('#uPhone').val();
				if(code == '' || !that.isCode(code)){
					$('#yzmCode').focus();
					return;
				}
				$('#yzmDialog').addClass('hide');
				setTimeout(function(){
					$('#yzmDialog').removeClass('show hide');
					that.sendcode2(code,phone,1);
				},400);
				
			}).on('click','.refreshCode',function(){
				en.rashCode();
				
			}).on('click','#confirmRegister',function(){
				
				if($(this).hasClass('disabled')) return;
				that.verify();
				
			});
		},
		countDown:function(second) {
			var that = this;
			var str = "s后重新<br>获得验证码";
			$('#pSend').addClass('disabled');
			var setext = second + str;
			$("#pSend").html(setext);
			that.timer2 = setInterval(function(){
				second--;
				if(second >= 0){
					setext = second + str;
					$("#pSend").html(setext);
				}else{
					$('#pSend').removeClass('disabled');
					$("#pSend").html('发送验证码');
					clearInterval(that.timer2);
				}
			},1000);
		},
		sendcode2:function(code,phone,type){
			var that = this, types = '';
			if(type){
				types = 'register';
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
						$('#confirmRegister').removeClass('disabled');
					} else if(result.flag == 0){
						that.showTips('验证码错误！');
						en.rashCode();
						setTimeout(function(){
							$('#yzmDialog').addClass('show');
							$('#yzmCode').val('').focus();
						},2000);
					}
				},
				error: function() {
					that.showTips('手机验证码发送失败，请稍后再试！');
				}
			});
		},
		//注册用户是否已经存在
		isValidName: function(nameObj) {//nameId, promptId
	    	var that = this, nameVal = nameObj, ret;
	    	var data = {name:nameVal,t:new Date().getTime()};
			$.ajax({
				url:dataUrl.SSO_ExistName,
				data:data,
				async:false,//同步
				success:function(result){
					if(!result.flag){
						ret = false;
					}else {
						ret = true;
					} 
				} 
			});
			return ret;
	    }, 
		//提交注册
		verify:function(){
			var that = this,
				nickName = $('#nickName').val(),
				phone = $('#uPhone').val(),
				psd = $('#password').val(),
				ycode = $('#yzmCode').val(),
				mcode = $.trim($('#code').val()),
				redirectFlag = $('#flag').val(),
				huzhu = "";
			if(nickName == ''){
				that.showTips('请输入昵称！');
				$('#nickName').focus();
				return;
			}
			if(phone == '' || !that.checkMobileStr(phone)){
				that.showTips('请输入正确的手机号！');
				return;
			}
			if(!that.isValidName(phone)){
				that.showTips('用户已存在！');
				$('#uPhone').focus();
				return;
			}
			if(mcode == '' || !that.isMobelCode(mcode)){
				that.showTips('手机验证码错误！');
				$('#code').focus();
				return;
			}
			if(psd == '' || !that.isValidPsd(psd)){
				that.showTips('密码格式为8-24位英文字母,数字或下划线至少两种组合！');
				$('#password').focus();
				return;
			}
			if((redirectFlag != null || redirectFlag !='') && redirectFlag.substring(0,5) == 'huzhu'){
				huzhu = "huzhu";
			}
			$.ajax({
				url:dataUrl.SSO_FastIn,
				data:{
					huzhu:huzhu,
					nickName:nickName,
					phone:phone,
					code:ycode,
					phoneCode:mcode,
					passWord:psd,
					t:new Date().getTime()
				},
				success: function(result){
					if(result.flag == 1){
						if((redirectFlag != null || redirectFlag !='') && redirectFlag.substring(0,9) == 'projectId'){
							location.href = 'http://www.17xs.org/project/view_h5.do?projectId='+redirectFlag.substring(10);
						}else if((redirectFlag != null || redirectFlag !='') && redirectFlag.substring(0,5) == 'huzhu'){
							location.href = 'http://hz.17xs.org/huzhu/index';
						}else{
							location.href = 'http://www.17xs.org/ucenter/userCenter_h5.do';
						}
					}else{
						that.showTips(result.errorMsg);
						return ;
					}
				}
			});
		},
		isValidPsd:function(psd){
			var len = psd.length;
			if (len==0 || len < 8) { 
	            return false;
	        }
			var checkReg = new RegExp("[\u4e00-\u9fa5]+");
	        if (checkReg.test(psd)) { 
				return false;
	        }
	        var reg = /^[a-zA-Z_](?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?![_]+$)[_0-9A-Za-z]{7,23}$/;
	        if(!reg.test(psd)){
				return false;
	        }else{
				return true;
	        }
		},
		isCode:function(code){
			var reg = /^\d{4}$/;
			if(reg.test(code)){
				return true;	
			}else{
				return false;
			}
		},
		isMobelCode:function(code){
			var reg = /^\d{6}$/;
			if(reg.test(code)){
				return true;	
			}else{
				return false;
			}
		},
		checkMobileStr:function(str) {
			var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
			if (!reg.test(str)) {
				return false;
			}
			return true;
		},
	    showTips(txt){
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
});