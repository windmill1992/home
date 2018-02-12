var base = window.baseurl;
var dataUrl = {
	SSO_FastIn   : base+'goodlibrary/bindUser.do' ,//注册请求
	SSO_MobileCode:base+'goodlibrary/phoneCode.do',//验证码请求
	SSO_LibraryJoin:base+'goodlibrary/gotoLibraryJoin.do'
};

var reg = {
	type:0,
	timer:null,
	timer1:null,
	init:function(){
		var that = this;
		
	    $('body').on('click','#pSend',function(){
		
			if($(this).hasClass('disabled')) return;
			var phone = $('#phone').val();
			if(phone == '' || !that.checkMobileStr(phone)){
				that.showTips('请输入正确的手机号！');
				$('#phone').focus();
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
				phone = $('#phone').val();
			if(code == '' || !that.isCode(code)){
				$('#yzmCode').focus();
				return;
			}
			$('#yzmDialog').addClass('hide');
			setTimeout(function(){
				$('#yzmDialog').removeClass('show hide');
				that.sendcode($('#pSend'),code,phone,1);
			},400);
			
		}).on('click','.refreshCode',function(){
			that.rashCode();
			
		}).on('click','.foot_qt',function(){
			that.verify();
			
		}).on('click','#submit',function(){
			//验证是善库成员弹框提示
			$('.prompt_box').hide();
			location.href = 'http://www.17xs.org/index/index_h5.do';
			
		}).on('click','#cancel1',function(){
			$('.prompt_box').hide();
	    	return false;
		});
	},
	//提交注册
	verify:function(){
		var that = this,
			id = $('#userId').val(),
			relName = $('#relName').val(),
			phone = $('#phone').val(),
			mcode = $('#code').val(),
			ycode = $("#yzmCode").val(),
			lirbraryId = $('#lirbraryId').val();
		if(relName == '' || relName == "请输入您的姓名"){
			that.showTips('请输入您的姓名！');
			return false;
		}
		if(phone=='' || !that.checkMobileStr(phone)){
			that.showTips('请输入正确的手机号码！');
			return false;
		} 
		if(code == '' || code == "请输入验证码"){
			that.showTips('请输入手机验证码！');
			return false;
		}
		var mobleCode = that.isMobelCode("#code");
		if(!mobleCode){
			that.showTips('手机验证码错误！');
			return false;
		}
		$.ajax({
			url:dataUrl.SSO_FastIn,
			type:"POST",
			data:{
				id:id,
				realname:relName,
				phone:phone,
				phoneCode:mcode,
				code:ycode,
				lirbraryId:lirbraryId,
				t:new Date().getTime()
			},
			success: function(result){
				if(result.flag == 1){
					location.href = dataUrl.SSO_LibraryJoin+'?userId='+id+'&lirbraryId='+lirbraryId;
				}else if(result.flag == 3){
					$('.prompt_box').show();
					return ;
				}else{
					that.showTips(result.errorMsg);
					return ;
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
	checkMobileStr:function(str) {
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

reg.init();
