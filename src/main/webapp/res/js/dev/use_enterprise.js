var base=window.baseurl;
var dataUrl={
	applyInfo:base+''/*提交请求*/
};
	
require.config({
	baseUrl:window.baseurl+"/res/js", 
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"area"	: "util/area",
		"ajaxform": "util/ajaxform" 
	},
	urlArgs:"v=20150511" 
});

define(["extend","head","entry","dialog","area","ajaxform"],function($,h,en,d,a,af){
	window.sitePage="p-joinGood";
	//window.sitePage="p-seekHelp";  --加入善管家-捐款人 -个人中心
	d.init();
	h.init();
	en.init(); 
	a.init({selectsId:["province","city","county"],def:[12,5,6]});
 
 	
	var use_enterprise={
		init:function(){
			var that=this; 
			$("#submitData").on("click",function(){
				that.submitData();
			}); 
		},
		submitData:function(){ 
			var that=this,address=a.getArea(),msgObj=$("#dataMsg");
			if(!address){
				return that.msgShow("您的地址不完整"); 
			}
			var field=this.applyField().join(' ');
			if(!field.length){ 
				return that.msgShow("请选择您感兴趣的领域"); 
			} 
			var reason=this.applyReason();
			if(!reason){return;} 
			$("#dataMsg").hide();
			
			var imgList=$("#proofImgList").children().not(".add"),
				img=[],
				len=imgList.length;  
			for(var i=0;i<len;i++){
				img.push($(imgList[i]).attr("id"));
			} 
			$.ajax({
				url:dataUrl.applyInfo,
				data:{field:field,address:address,reason:reason,imageId:img.join(',')},
				success: function(result){
					if(!!result){
						if(result.flag==1){			
							window.location.href=dataUrl.three;
						}else if(result.flag==0){
							d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							en.show(that.submitData);
							//window.location.href=dataUrl.login;
						} 
						return;
					}
				} 
			}); 
		} 
	}; 
	 
	use_enterprise.init();
});