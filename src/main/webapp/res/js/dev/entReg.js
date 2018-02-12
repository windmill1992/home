var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt",
	spreadCode:base+'enterprise/verifyHelpCode.do'//口令	
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry"
	},
	urlArgs:"v=20180206"
});

define(["extend","dialog","head","entry"],function($,d,h,en){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	
	var reg={
		type:0,
		init:function(){
			var that=this;
			$('#uName').focus(function(){
				$('#uName').parent().next().html('<p class="info_normal">请输入用户名、手机号</p>');
				en.StrokesChange($(this),'rgray',0);
			})
			$('#uName').blur(function(){
				en.StrokesChange($(this),'rgray',1)
				en.isValidName($(this));
			})
			$('#entUName').focus(function(){
				$('#entUName').parent().next().html('<p class="info_normal">请输入公司名称</p>');
				en.StrokesChange($(this),'rgray',0);
			})
			$('#entUName').blur(function(){
				en.StrokesChange($(this),'rgray',1)
				en.isValidEntName($(this));
			})
			$('#uPw').focus(function(){
				$('#uPw').parent().next().html('<p class="info_normal info">8~24个字符，限数字、大小写字母和下划线至少两种组合，不能以数字开头</p>');
			})
			$('#uPw').blur(function(){
				en.isValidPwd($(this));
			})
			$('#cPw').focus(function(){
				$('#cPw').parent().next().html('<p class="info_normal">请再次输入密码</p>');
			})
			$('#cPw').blur(function(){
				en.checkSurePwd($(this),$("#uPw"));
			})
			
			
		   $('.refreshCode').click(function(){
			 en.rashCode(); 
		   })
		   
		   $('#rltype span').click(function(){
			   if($(this).hasClass('on'))return false;
			   var i=$('#rltype span').index(this),uPw=$('#uPw'),cPw=$('#cPw'),wcode=$('#wrcode');
			   $('#rltype span').removeClass('on').eq(i).addClass('on');
			   uPw.val('');
			   cPw.val('');
			   wcode.val('');
			   $('.wrinfo').html('');
			   that.type=i;
			   if(i==0){
				   $('#personalName').show();
				   $('#entName').hide();
				   $('.rlword').show();
				   $('#signin').val('立即注册');
				   $('#rbtnPrim').hide();
				   $('#pCode').hide();
			   }else{
				   $('#personalName').hide();
				   $('#entName').show();
				   $('.rlword').hide();
				   $('#signin').val('开始认证');
				   $('#uName').val('用户名/手机号码').addClass('rgray');
				   $('#rbtnPrim').show();
				   $('#pCode').hide();
			   }
		   })

		   $('#signin').click(function(){
			   var type=that.type;
			  en.fastSign($(this),type);
			})
			
			$('#wrcode').blur(function(){
				en.isCode($('#wrcode'),0);
			})
			$('#pSend').click(function(){
				en.sendcode($(this),'#uName','#wrcode','#wrcode',0);	
			})
			
			$('body').keypress(function(e){
				if (e.which == 13&&$("#signin").size()>0) { 
					 en.fastSign($('#signin'),that.type);
				}
			}); 
			

			$('#pSendT').click(function(){
				en.sendcode($(this),'#mnumber','','#mtcode',1);	
			})
			$('#spreadCode').focus(function(){
				var nameObj=$(this);msgObj=nameObj.parent().parent().children('.wrinfo'),primObj=nameObj.parent().parent().children('.wrsdpt');
				msgObj.html('');
				primObj.html('企业助善号召口令');
			})
			/*$('#spreadCode').blur(function(){
				var nameObj=$(this),nameVal = nameObj.val(),msgObj=nameObj.parent().parent().children('.wrinfo'),primObj=nameObj.parent().parent().children('.wrsdpt');
				var d=en.spreadCode($(this));	
				if(!d){
					msgObj.html('助善号召失效');
					primObj.html('');
				}else{
					 $.ajax({
					      url:dataUrl.spreadCode,
						  data:{code:nameVal,t:new Date().getTime()}, 
						  success: function(result){ //eval(result);//回调函数  参数后台来 
							    if(result.flag==0){
							       primObj.html('');
								   msgObj.html('<i class="error"></i><p>'+result.errorMsg+'</p>');  
								   return false;
							    }else{
							    	return true;
							    }
						  }
					 });  
				}
			});	*/
		}
		
	}
	
	reg.init()
})