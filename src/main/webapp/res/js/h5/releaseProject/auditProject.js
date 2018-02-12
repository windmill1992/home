var base=window.baseurl;
var dataUrl={
	SSO_MobileCode:base+'user/phoneCode.do',//验证码请求
	Audit_Project:base+'uCenterProject/addAuditProject.do'//提交证实信息
};

var reg = {
	type:0,
	timer:null,
	timer1:null,
	photoData:[],
	init:function(){
		var that = this;
		
		$('body').on('click','.fsyz',function(){
			
			if($(this).hasClass('disabled')) return;
			var phone = $('#mobileNum').val();
			if(phone == '' || !that.telValidate(phone)){
				that.showTips('请输入正确的手机号！');
				$('#mobileNum').focus();
				return;
			}
			that.rashCode();
			$('#yzmDialog').addClass('show');
			$('#yzmCode').val('').focus();
			
		}).on('click','#cancel',function(){
				
			$('#yzmDialog').addClass('hide');
			setTimeout(function(){
				$('#yzmDialog').removeClass('show hide');
				$('#yzmCode').val('');
			},400);
			
		}).on('click','#sure',function(){
			var code = $('#yzmCode').val(), 
				phone = $('#mobileNum').val();
			if(code == '' || !that.isCode(code)){
				$('#yzmCode').focus();
				return;
			}
			$('#yzmDialog').addClass('hide');
			setTimeout(function(){
				$('#yzmDialog').removeClass('show hide');
				that.sendcode($('.fsyz'),code,phone,1);
			},400);
			
		}).on('click','.refreshCode',function(){
			that.rashCode();
			
		}).on('click','.footer_btn',function(){
			
			if($(this).hasClass('disabled')) return;
			that.verify();
		});
	},
	//提交
	verify:function(){
		var that = this,
			person_sel = $('.person_sel').val(),
			realName = $('#realName').val(),
			idCard = $('#idCard').val(),
			mcode = $.trim($('.person_input1').val()),
			ycode = $("#yzmCode").val();
			mobileNum = $('#mobileNum').val(),
			information = $('#information').val(),
			projectId = $('#projectId').val();
		if(realName == '' || realName.length < 2){
			that.showTips('请输入真实姓名！');
			$('#realName').focus();
			return false;
		}
		if(idCard == '' || !checkCard(idCard)){
			that.showTips('请输入正确的身份证号！');
			$('#idCard').focus();
			return false;
		}
		if(information == ''){
			that.showTips('请介绍一些详细情况！');
			$('#information').focus();
			return false;
		}
		if(mobileNum == '' || !that.telValidate(mobileNum)){
			that.showTips('请输入正确的手机号！');
			$('#mobileNum').focus();
			return false;
		}
		if(mcode == ''){
			that.showTips('请输入手机验证码！');
			return false;
		}
		if(!that.isMobelCode(mcode)){
			that.showTips('手机验证码错误！');
			return false;
		}
		$.ajax({
			url:dataUrl.Audit_Project,
			type:'post',
			data:{
				projectId:projectId,
				relationship:person_sel,
				realName:realName,
				idCard:idCard,
				phoneCode:mcode,
				code:ycode,
				mobileNum:mobileNum,
				information:information
			},
			success: function(result){
				if(result.flag == 1){//成功
					that.showTips('恭喜您，证实成功！');
					setTimeout(function(){
						var type = $('#type').val();
						if(type==0){
							location.href = base +"uCenterProject/project_details.do?projectId="+ projectId;
						}else{
							location.href = base +"newReleaseProject/project_detail.do?projectId="+ projectId;
						}
					},2000);
				}else if(result.flag == 0){//失败
					that.showTips(result.errorMsg);
					return ;
				}else{//未登录
					location.href = 'http://www.17xs.org/ucenter/user/Login_H5.do';
				}
			}
		});
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
	//发送手机验证码
	sendcode:function(obj,code,phone,type){
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
					that.countDown(obj,60);
					$('.footer_btn').removeClass('disabled');
				} else if(result.flag == 0){
					that.showTips('验证码错误！');
					that.rashCode();
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
	//手机号格式验证
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(!reg.test(tel)){
	        return false;
	    }else{
	    	return true;
	    }
	},
	countDown:function(obj,second){
        var that = this;
		var str = "s后重新<br>获得验证码";
		$(obj).addClass('disabled');
		var setext = second + str;
		$(obj).html(setext);
		that.timer1 = setInterval(function(){
			second--;
			if(second >= 0){
				setext = second + str;
				$(obj).html(setext);
			}else{
				$(obj).removeClass('disabled');
				$(obj).html('发送验证码');
				clearInterval(that.timer1);
			}
		},1000);
    },
    //刷新验证码
    rashCode:function(){
		var _random;
		_random = base +'/user/code.do?t='+ new Date().getTime() + 'a' + Math.random();
		$('#codepic').attr('src', _random);
	},
    showTips:function(txt){
    	var $msg = $('#msg');
    	if(!$msg.is(':hidden')){
    		$msg.hide();
    		this.timer = null;
    	}
    	$msg.html(txt).fadeIn();
    	this.timer = setTimeout(function(){
    		$msg.fadeOut();
    	},2000);
    }
};

$(function(){
	reg.init();
});
