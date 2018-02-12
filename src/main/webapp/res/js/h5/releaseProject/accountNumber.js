var base = window.baseurl;
var dataUrl = {
	SSO_MobileCode: base + 'user/phoneCode.do',			//验证码请求
	User_AccountNum: base + 'uCenterProject/addAccountNum.do'	//提交收款账号
};

var reg = {
	timer:null,
	timer1:null,
	init:function(){
		var that=this;
		that.bindEvent();
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','#next, #saveNext',function(){
			that.verify();
			
		}).on('click','.fsyz',function(){
			if($(this).hasClass('disabled')) return;
			var phone = $('#mobile').val();
			if(phone == '' || !that.telValidate(phone)){
				that.showTips('请联系客服，项目发起人的手机号不正确！！');
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
				phone = $('#mobile').val();
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
			
		}).on('click','.bj_fr',function(){
			$('.bjqr').hide();
			$('.save-next').show();
			$('#accountName').removeAttr("readonly");
			$('#accountBank').removeAttr("readonly");
			$('#collectNum').removeAttr("readonly");
			$('#yzm').show();
		});
	},
	//提交
	verify:function(){
		var that = this,
			accountName = $('#accountName').val(),
			accountBank = $('#accountBank').val(),
			collectNum = $('#collectNum').val(),
			mcode = $.trim($('#code').val()),
			ycode = $('#yzmCode').val(),
			mobile = $('#mobile').val(),
			projectId = $('#projectId').val(),
			money = $('#money').val();
		if(accountName==''){
			that.showTips('请输入开户名！');
			$('#accountName').focus();
			return false;
		}
		if(accountBank==''){
			that.showTips('请输入开户行！');
			$('#accountBank').focus();
			return false;
		}
		if(collectNum==''){
			that.showTips('请输入银行卡号！');
			$('#collectNum').focus();
			return false;
		}else if(!that.luhmCheck(collectNum)){
			that.showTips('请输入正确的银行卡号！');
			$('#collectNum').focus();
			return false;
		}
		if(mobile=='' || !that.telValidate(mobile)){
			that.showTips('请联系客服，项目发起人的手机号不正确！');
			return false;
		}
		if(mcode == ''){
			that.showTips('请输入手机验证码！');
			$('#code').focus();
			return false;
		}
		if(!that.isMobelCode(mcode)){
			that.showTips('手机验证码错误！');
			$('#code').focus();
			return false;
		}
		$.ajax({
			url:dataUrl.User_AccountNum,
			data:{id:projectId,accountName:accountName,bankName:accountBank,card:collectNum,phoneCode:mcode,code:ycode,mobile:mobile},
			success: function(result){
				if(result.flag == 1){//成功
					location.href = '/uCenterProject/gotoAccountMoney.do?projectId='+projectId+'&money='+money;
				}else if(result.flag == 0){//失败
					that.showTips(result.errorMsg);
					return ;
				}
				else{//未登录
					location.href = '/ucenter/user/Login_H5.do';
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
	telValidate:function(tel){
		var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
		if(reg.test(tel)){
			return true;
		}else{
			return false;
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
    //校验银行卡号
	luhmCheck:function(bankno){
		var that = this;
		if (bankno.length < 16 || bankno.length > 19) {
			that.showTips('银行卡号长度必须在16到19之间！');
			return false;
		}
		var num = /^\d*$/;  //全数字
		if (!num.exec(bankno)) {
			that.showTips('银行卡号必须全为数字！');
			return false;
		}
		//开头6位
		var strBin="10,18,30,35,37,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,58,60,62,65,68,69,84,87,88,94,95,98,99";    
		if (strBin.indexOf(bankno.substring(0, 2))== -1) {
			that.showTips('银行卡号开头6位不符合规范！');
			return false;
		}
        var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）
    
        var first15Num=bankno.substr(0,bankno.length-1);//前15或18位
        var newArr=new Array();
        for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组
            newArr.push(first15Num.substr(i,1));
        }
        var arrJiShu=new Array();  //奇数位*2的积 <9
        var arrJiShu2=new Array(); //奇数位*2的积 >9
        
        var arrOuShu=new Array();  //偶数位数组
        for(var j=0;j<newArr.length;j++){
            if((j+1)%2==1){//奇数位
                if(parseInt(newArr[j])*2<9)
                arrJiShu.push(parseInt(newArr[j])*2);
                else
                arrJiShu2.push(parseInt(newArr[j])*2);
            }
            else //偶数位
            arrOuShu.push(newArr[j]);
        }
        
        var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数
        var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数
        for(var h=0;h<arrJiShu2.length;h++){
            jishu_child1.push(parseInt(arrJiShu2[h])%10);
            jishu_child2.push(parseInt(arrJiShu2[h])/10);
        }        
        
        var sumJiShu=0; //奇数位*2 < 9 的数组之和
        var sumOuShu=0; //偶数位数组之和
        var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
        var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和
        var sumTotal=0;
        for(var m=0;m<arrJiShu.length;m++){
            sumJiShu=sumJiShu+parseInt(arrJiShu[m]);
        }
        
        for(var n=0;n<arrOuShu.length;n++){
            sumOuShu=sumOuShu+parseInt(arrOuShu[n]);
        }
        for(var p=0;p<jishu_child1.length;p++){
            sumJiShuChild1 = sumJiShuChild1 + parseInt(jishu_child1[p]);
            sumJiShuChild2 = sumJiShuChild2 + parseInt(jishu_child2[p]);
        }      
        //计算总和
        sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);
        
        //计算Luhm值
        var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;        
        var luhm= 10-k;
        
        if(lastNum!=luhm){
        	that.showTips('请输入正确的银行卡号！');
	        return false;
        }
        return true;
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
