var base=window.baseurl;
var dataUrl={
		SSO_ExistName:base+"user/existuser.do",//检查用户名
		userInfo:base+'ucenter/coredata/personalUserInfo.do'
	};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"userCenter"  : "dev/common/userCenter" ,
		"entry"  : "dev/common/entryNew",
		"area"	: "util/area"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","userCenter","entry","area"],function($,d,h,uc,en,a){
	//window.uCENnavEtSite="p-password";//如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-Mydetail";//如需导航显示当前页则加此设置
	h.init();
	d.init(); 
	en.init();
	uc.init();
	var addressNum = $('#addressNum').val();
	if(addressNum){
		var adr =addressNum.split(" ");
		a.init({selectsId:["province","city","county"],def:[adr[0],adr[1],adr[2]]});
	}else{
		if($('#province').length>0){
			a.init({selectsId:["province","city","county"],def:[0,0,0]});
		}
	}
	var reg={
		init:function(){
			var that=this,uName = $("#uName").val();
			
			if(uName.substring(0,6)=='weixin' || uName.substring(0,2)=='游客'){
				$("#before_update").show();
				$("#after_update").hide();
			}else{
				$("#after_update").show();
				$("#before_update").hide();
			}
			$("#editorUser").click(function(){
				$("#before_update").show();
				$("#after_update").hide();
			})
			if(uName.substring(0,6)=='weixin' || uName.substring(0,2)=='游客'){
				$("#password1").click(function(){
					var ret = that.isValidName($('#userName').val());
					if(!ret){
						return false;
					}else{
						$(".wrinfo1").html(''); 
					}
				});
			}
			$("#password2").click(function(){
				var ret = that.isValidPwd("#password1");
				if(!ret){
					return false;
				}else{
					$(".wrinfo1").html(''); 
				}
			});
			$("#petname").click(function(){
				var ret = that.checkSurePwd("#password2", "#password1");
				if(!ret){
					return false;
				}else{
					$(".wrinfo1").html(''); 
				}
			});
			
			$("#dataButton").click(function(){
				that.userInfo();
			})
		},
		//修改用户信息
		userInfo:function(){
			var userName = $('#userName').val(),nickName=$('#petname').val(),persition = $('#profession').val(),
			mobileNum = $('#telnumber').val(),password1 = $("#password1").val(),detailAddress = $('#detailAddress').val(),
			weixin = $('#wxnumber').val(),workUnit = $('#employer').val(),address=a.getArea(),uName = $("#uName").val();
			
			if(nickName == ''){
				$(".wrinfo4").html('<i class="error"></i><p>请输入昵称</p>');
				return false;
			}
			if(uName.substring(0,6)=='weixin'){
				var ret = this.isValidName(userName)
				if(!ret){
					return false;
				}
				ret = this.isValidPwd("#password1");
				if(!ret){
					return false;
				}
				ret = this.checkSurePwd("#password2", "#password1");
				if(!ret){
					return false;
				}
			}
			
			$.ajax({
				url : dataUrl.userInfo,
				data:{name:userName,phone:mobileNum,vocation:persition,passWord:password1,nickName:nickName,
					detailAddress:detailAddress,workUnit:workUnit,weixin:weixin,address:address,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						//d.alert({content:"保存成功",type:'ok'});
						window.location.href='http://www.17xs.org/ucenter/getUserDetail.do';
					}
				},
				error   : function(r){ 
					d.alert({content:'修改助善信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
			
		},
		isValidName:function(nameObj) {
	    	var that=this;
	         
	        var len = nameObj.cnLength(); 
			if (len==0) {
				$(".wrinfo1").html('<i class="error"></i><p>请输入用户名</p>');
	            return false;
	        } 
			
	        if(len<4 || len>32){ 
	        	$(".wrinfo1").html('<i class="error"></i><p>用户名长度应为4-32位字符</p>').show();
	            return false;
	        }
			
			var nameReg = new RegExp("^([a-z|A-Z]+|[ \u4e00-\u9fa5]+|[0-9]+|[_|_]+)+$");
	        if (!nameReg.test(nameObj)) {
	        	$(".wrinfo1").html('<i class="error"></i><p>账号可用中文、数字、英文（区分大小写）组成</p>'); 
	            return false;
	        }
			
			var reg=/^[1-9]\d*$|^0$/,c=en.checkMobileStr(nameObj);  
	        if(reg.test(nameObj)==true&&len!=11||!c&&len==11&&reg.test(nameVal)==true){ 
	        	$(".wrinfo1").html('<i class="error"></i><p>用户名不能为除手机号码以外的纯数字</p>'); 
			    return false; 
	        }
			
	       var data={},temp=false;
		   data={name:nameObj,t:new Date().getTime()};
		   $.ajax({
				url:dataUrl.SSO_ExistName,
				data:data,
				async: false,
				success:function(result){
				   if(result.flag == 0){
					   $(".wrinfo1").html('<i class="error"></i><p>用户名已存在</p>');
					   temp=false;
				   }else{
					   temp=true;
					} 
				} 
			});
			return temp;
	},
	isValidPwd: function (passObj) {//passId, promptId
	    var passObj=$(passObj),msgObj=passObj.parent().next(),passVal=passObj.val(),len=passVal.length; 
		msgObj.html('');
        if (len==0) { 
        	$(".wrinfo2").html('<i class="error"></i><p>请输入密码</p>'); 
            return false;
        }else if (len < 6) { 
        	$(".wrinfo2").html('<i class="error"></i><p>您输入的密码不足6位</p>'); 
            return false;
        }else if(len > 12) {
        	$(".wrinfo2").html('<i class="error"></i><p>您输入的密码超过12位</p>');  
            return false;
        }
		var checkReg = new RegExp("[\u4e00-\u9fa5]+");
        if (checkReg.test(passVal)) { 
        	$(".wrinfo2").html('<i class="error"></i><p>密码请勿包含中文</p>'); 
			return false;
        } 
        if(passVal=='123123'||passVal=='112233'||passVal=='111222'||passVal=='abcabc'||passVal=='aabbcc'||passVal=='aaabbb'){
        	$(".wrinfo2").html('<i class="error"></i><p>您输入的密码太简单，请重新输入</p>'); 
			return false;
		}
		if(passVal.replaceAll(passVal.charAt(0),'')==''){ 
			$(".wrinfo2").html('<i class="error"></i><p>您输入的密码太简单，请重新输入</p>'); 
			return false;
		}
		if(isNaN(passVal)){//非纯数字
			$(".wrinfo2").html('<i class="right"></i>');  
			return true;
		}
		//
		var array=passVal.split("");
		var sortDirection=1;//默认升序
		var len=array.length;
	    var n0=parseInt(array[0]);
	    if(parseInt(array[0])>parseInt(array[len-1])){ 
	        sortDirection=-1; //降序
	    }
	    var isnotContinuation=false;
	    for(var i=0;i<len;i++){
	        if(parseInt(array[i])!==(n0+i*sortDirection)){
	        	isnotContinuation=true;
	            break;
	        }
	    }
		if (!isnotContinuation){ 
			$(".wrinfo2").html('<i class="error"></i><p>您输入的密码太简单，请重新输入</p>');  
            return false;
		};
		
		$(".wrinfo2").html('<i class="right"></i>');  
		return true;
		
    },
    checkSurePwd: function (sureObj, passObj) {//checkConfirmPwd: function (confirmPassId, passId, promptId) {
		var sureObj=$(sureObj),passObj=$(passObj),msgObj=sureObj.parent().next(),sureVal =sureObj.val(),pwdVal = passObj.val();
       
        if (sureVal == '' || pwdVal != sureVal) { 
        	$(".wrinfo3").html('<i class="error"></i><p>您两次输入的密码不一致</p>'); 
            return false;
        }  
        $(".wrinfo3").html('<i class="right"></i>'); 
        return true;
    }
		
	}
	reg.init();
})