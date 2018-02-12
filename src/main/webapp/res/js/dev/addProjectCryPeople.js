var base=window.baseurl;
var dataUrl={
	addCryPeople:base+"project/addCryPeople.do",
	detailUrl:base+'project/view/'
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform"],function($,d,h,en,uc,f){
	window.sitePage="p-doGood"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var needhelp={
		photoData:[],
		init:function(){
			var that=this;
			$('#crySubmit').click(function(){
				that.crySubmit();
			});
			
		
			
		},	
		crySubmit:function(){
				var that=this,name=$('#name').val(),mobile=$('#mobile').val(),workUnit = $('#workUnit').val(),
				linkAddress = $('#linkAddress').val(),actualAdress = $('#actualAdress').val(),remark = $('#remark').val(),
				projectId = $('#projectId').val();
			
		
				if(!name){d.alert({content:"姓名不能为空",type:'error'});return ;};

				if(!mobile){d.alert({content:"联系电话不能为空",type:'error'});return false;};
				var isMobile=that.phone(mobile);
				if(!isMobile){d.alert({content:"联系电话格式不正确！",type:'error'});return false;};
				
			$.ajax({ 
					url:dataUrl.addCryPeople,
					type: 'POST',

					data:{name:name,projectId:projectId,mobile:mobile,workUnit:workUnit,
					linkAddress:linkAddress,actualAdress:actualAdress,remark:remark,
						
						projectId:projectId,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(result.flag==1){
//								setTimeout(function(){
//									d.alert({content:"报名成功！"});
//								}, 3000);
							window.location.href=dataUrl.detailUrl+'?projectId='+projectId;
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}
					},
					error: function(){ 
						return d.alert({content:"网络异常,请联系客服"});
					}
			});
			
		}
		,

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