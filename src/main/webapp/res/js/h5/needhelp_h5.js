var base=window.baseurl;
var dataUrl={
	appealByCustom:base+"project/appealByCustom.do",//客服代发
	appealSubmit:base+"ucenter/coreHelp/publish_submit.do",
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
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform"],function($,d,h,en,uc,f){
	window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var needhelp={
		photoData:[],
		init:function(){
			var that=this;
			
			$('#publish_help').click(function(){
				that.needhelpNext();
			});
			
			
			$('#needhelptype a').click(function(){
				var i=$('#needhelptype a').index(this);
				$('#needhelptype a').removeClass('selected').eq(i).addClass('selected');
			});
			
			$("#name").click(function(){
				var name = $('#name').val();
				if(name == '您的姓名：'){
					$('#name').val('');
				}
			});
			$("#name").blur(function(){
				var name = $('#name').val();
				if(name == ''){
					$('#name').val('您的姓名：');
				}
			});
			$("#qq").click(function(){
				var qq = $('#qq').val();
				if(qq == 'qq/微信：'){
					$('#qq').val('');
				}
			});
			$("#qq").blur(function(){
				var qq = $('#qq').val();
				if(qq == ''){
					$('#qq').val('qq/微信：');
				}
			});
			$("#phone").click(function(){
				var phone = $('#phone').val();
				if(phone == '您的电话：'){
					$('#phone').val('');
				}
			});
			$("#phone").blur(function(){
				var phone = $('#phone').val();
				if(phone == ''){
					$('#phone').val('您的电话：');
				}
			});
			$("#reasion").click(function(){
				var reasion = $('#reasion').val();
				if(reasion == '求助原因：'){
					$('#reasion').val('');
				}
			});
			$("#reasion").blur(function(){
				var reasion = $('#reasion').val();
				if(reasion == ''){
					$('#reasion').val('求助原因：');
				}
			});
			
			$('#back').click(function(){
				history.go(-1);
			});
		},	
	
		needhelpNext:function(){
			var that=this,IsLogin=$.cookie.get("sjj_username");			
			var type=$('#needhelptype a').index($('#needhelptype a.selected')),name=$('#name').val(),qq=$('#qq').val(),phone=$('#phone').val(),reasion=$('#reasion').val();
			var typeName=$('#needhelptype a').eq(type).attr("data");
			var tName=$('#needhelptype a').eq(type).html();
			if(type==-1){
				return d.alert({content:'请选择求助类型！',type:'error'});
			}
		
				if(!name||name == '您的姓名：'){d.alert({content:'姓名不能为空！',type:'error'});return false;}
				if(!phone||phone == '您的电话：'){d.alert({content:'电话号码不能为空！',type:'error'});return false;}
				if(!reasion||reasion == '求助原因：'){d.alert({content:'求救原因不能为空！',type:'error'});return false;}
				if(!qq||qq == 'qq/微信：'){d.alert({content:'qq/微信不能为空！',type:'error'});return false;}

				var isPhone=that.phone(phone);
				if(!isPhone){d.alert({content:'电话号码格式不正确',type:'error'});return false;}
			
				$.ajax({
					url:dataUrl.appealByCustom,
					data:{name:name,qq:qq,phone:phone,t:new Date().getTime(),reasion:reasion},
					success: function(result){ 
						if(result.flag==1){
							 window.location.href=dataUrl.appealSubmit+'?typeName='+typeName+'&tName='+tName;//发布
						}else{
							d.alert({content:result.errorMsg,type:'error'});
							return false;
						}
					}
				}); 
			
		},
		phone:function(str){
			 var cellphone = /^((0?1[358]\d{9})|((0(10|2[1-3]|[3-9]\d{2}))?[1-9]\d{6,7}))$/;
			if(cellphone.test(str)){
				return true;
			}else{
				return false;
			}
		}
		
		
		
	};
	 
	needhelp.init();
});