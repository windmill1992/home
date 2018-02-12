var base=window.baseurl;
var dataUrl={
	addService:base+'user/addCustomerService.do'
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"util"   : "dev/common/util",
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter" : "dev/common/userCenter",		
		"area"	: "util/area",
		"pages"   :"dev/common/pages",
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages","area","ajaxform"],function($,d,h,en,uc,p,a,f){
	//window.uCENnavEtSite="p-centralaccount";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	
	var reg={
		type:0,
		id:'',
		init:function(){
			var that=this;
			
			$('.huantu').click(function(){
				 en.rashCode(); 
			   })
			$('#tijiao_ts').click(function(){
				if($.trim($('#content').val()) ==null || $.trim($('#content').val())==''){
					$('#wrong_cuo1').show();
					return false;
				}
				if($.trim($('#user_txt').val()) == null || $.trim($('#user_txt').val()) == ''){
					$('#wrong_cuo2').show();
					return false;
				}
				if($.trim($('#phone_txt').val()) == null || $.trim($('#phone_txt').val()) == ''){
					$('#wrong_cuo3').show();
					return false;
				}
				if(reg.isCode()){
					reg.addService();
				}
			})
			
		},
		addService:function(){
			var that = this,p={},userId=$('#userId').val(),type=$('#type').val(),content=$('#content').val(),scheme=$('#scheme').val(),
			name=$('#user_txt').val(),mobile=$('#phone_txt').val(),codeNum=$('#codeNum').val();
			
			
			p.userId=userId;p.type=type;p.content=content;p.scheme=scheme;
			p.name=name;p.mobile=mobile,p.code=codeNum;
			$.ajax({
				url: dataUrl.addService,
				data:p,
				cache   : false,
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						that.rashCode();
						return false;
					}else{
						window.location.href="http://www.17xs.org/index/indexService.do";
					}
				},
				error   : function(r){ 
					d.alert({content:'提交失败！，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
		isCode:function(){
			var nameVal = $.trim($('#codeNum').val());
			if($.trim(nameVal) == null || $.trim(nameVal) == ''){
				$('#wrong_cuo4').show();
				return false;
			}
			var len = nameVal.cnLength();
			if(isNaN(nameVal)){//非纯数字
				$('#wrong_cuo4').show();
				return false;
			}else{
				if(len<4||len>4){
					$('#wrong_cuo4').show();
					return false;
				}else{
					return true;
				}
			}
			
		},
	}
	reg.init();
});