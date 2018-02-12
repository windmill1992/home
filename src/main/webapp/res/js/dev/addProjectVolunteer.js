var base=window.baseurl;
var dataUrl={
	addVolunteer:base+"project/addVolunteer.do",
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
			$('#personSubmit').click(function(){
				that.personSubmit();
			});
			
			$('#groupSubmit').click(function(){
				that.groupSubmit();
			});	
			
		},	
		personSubmit:function(){
				var that=this,name=$('#name').val(),mobile=$('#mobile').val(),indentity = $('#indentity').val(),
				position = $('#position').val(),birthday = $('#birthday').val(),email = $('#email').val(),
				address = $('#address').val(), serviceTime = $('#serviceTime').val(),historyService = $('#historyService').val(),
				sex = $("input[name='sex']:checked").val(),projectId = $('#projectId').val();
			
		
				if(!name){d.alert({content:"姓名不能为空",type:'error'});return ;};
		
				if(!indentity){d.alert({content:"身份证号不能为空",type:'error'});return false;};
				var isIndentity=en.isVaildId($("#indentity")); 
				if(!isIndentity){d.alert({content:"请输入正确定的身份证件号码！",type:'error'});return false;}
				
				
				if(!mobile){d.alert({content:"联系电话不能为空",type:'error'});return false;};
				var isMobile=that.phone(mobile);
				if(!isMobile){d.alert({content:"联系电话格式不正确！",type:'error'});return false;};
				
			
			$.ajax({ 
					url:dataUrl.addVolunteer,
					type: 'POST',
					/*
					data:{name:encodeURI(name),projectId:encodeURI(projectId),mobile:encodeURI(mobile),indentity:encodeURI(indentity),
						position:encodeURI(position),birthday:encodeURI(birthday),email:encodeURI(email),address:encodeURI(address),
						serviceTime:encodeURI(serviceTime),historyService:encodeURI(historyService),sex:encodeURI(sex),
						projectId:encodeURI(projectId),personType:0,t:new Date().getTime()},
						*/
					data:{name:name,projectId:projectId,mobile:mobile,indentity:indentity,
						position:position,birthday:birthday,email:email,address:address,
						serviceTime:serviceTime,historyService:historyService,sex:sex,
						projectId:projectId,personType:0,t:new Date().getTime()},
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
		groupSubmit:function(){
			var that=this,groupName=$('#groupName').val(),mobile=$('#linkMobile').val(),indentity = $('#indetity2').val(),
			groupType = $('#groupType').val(),number = $('#number').val(),email = $('#email2').val(),
			linkAddress = $('#linkAddress').val(), serviceTime = $('#serviceTime2').val(),linkName = $('#linkName').val(),
			projectId = $('#projectId').val();
			
	
			if(!groupName){d.alert({content:"组织名称不能为空",type:'error'});return ;};
	
			if(!indentity){d.alert({content:"身份证号不能为空",type:'error'});return false;};
			var isIndentity=en.isVaildId($("#indetity2")); 
			if(!isIndentity){d.alert({content:"请输入正确定的身份证件号码！",type:'error'});return false;}
			
			
			if(!mobile){d.alert({content:"联系电话不能为空",type:'error'});return false;};
			var isMobile=that.phone(mobile);
			if(!isMobile){d.alert({content:"联系电话格式不正确！",type:'error'});return false;};
			
			
		    if(isNaN(number))
		    {
		    	d.alert({content:"参加人数请填写正确的数字",type:'error'});return false;
		    }
		    
		    if(!linkName)
		    {
		    	{d.alert({content:"联系人姓名不能为空",type:'error'});return ;};
		    }
			
		
		$.ajax({ 
				url:dataUrl.addVolunteer,
				type: 'POST',
				/*
				data:{groupName:encodeURI(groupName),projectId:encodeURI(projectId),mobile:encodeURI(mobile),indentity:encodeURI(indentity),
					email:encodeURI(email),address:encodeURI(linkAddress),groupType:encodeURI(groupType),number:encodeURI(number),
					serviceTime:encodeURI(serviceTime),linkMan:encodeURI(linkName),projectId:encodeURI(projectId),personType:1,t:new Date().getTime()},
					*/
				data:{groupName:groupName,projectId:projectId,mobile:mobile,indentity:indentity,
					email:email,address:linkAddress,groupType:groupType,number:number,
					serviceTime:serviceTime,linkMan:linkName,projectId:projectId,personType:1,t:new Date().getTime()},
				chase:false,
				success: function(result){
					if(result.flag==1){
//						d.alert({content:"报名成功！"});
//							that.wait();
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