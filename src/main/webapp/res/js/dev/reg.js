var base=window.baseurl;
require.config({
	baseUrl:window.baseurl+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"slide"  : "util/slide"
	},
	urlArgs:"v=20150511"
});
define(["extend","dialog","head","entry","slide"],function($,d,h,en,sld){
	h.init();
	d.init(); 
	en.init();
	
	var reg={
		init:function(){
			var that=this;
			$('#uName').focus(function(){
				$('#uName').parent().next().html('<p class="info_normal">请输入用户名、手机号</p>');
				en.StrokesChange($(this),'rgray',0);
			});
			$('#uName').blur(function(){
				en.StrokesChange($(this),'rgray',1)
				en.isValidName($(this));
			});
			$('#uPw').focus(function(){
				$('#uPw').parent().next().html('<p class="info_normal info">6-12个字符，建议使用字母、数字组合，混合大小写</p>');
			});
			$('#uPw').blur(function(){
				en.isValidPwd($(this));
			});
			$('#cPw').focus(function(){
				$('#cPw').parent().next().html('<p class="info_normal">请再次输入密码</p>');
			});
			$('#cPw').blur(function(){
				en.checkSurePwd($(this),$("#uPw"));
			});
			
		   $('.refreshCode').click(function(){
			 en.rashCode(); 
		   });
		   $('#signin').click(function(){
			  en.fastSign($(this));
			});
			
			$('#wrcode').blur(function(){
				en.isCode($('#wrcode'),0);
			});
			$('#pSend').click(function(){
				en.sendcode($(this),'#uName','#wrcode','#wrcode',0);	
			});
			
			$('body').keypress(function(e){
				if (e.which == 13&&$("#signin").size()>0) { 
					 en.fastSign($('#signin'));
				}
			}); 
		}
	}
	reg.init()
})