var base=window.baseurl;
var dataUrl={
	appealFirestData:base+"project/appealFirestData.do",//不同项目类型的条件
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
			that.appealFirestData();
			$('#back').click(function(){
				history.go(-1);
			});
		},	
	
		appealFirestData:function(){
			var that=this,html=[],typeName=$('#typeName').val(),tName = $('#tName').val();
			$.ajax({ 
					url:dataUrl.appealFirestData,
					type: 'POST',
					data:{type:typeName,t:new Date().getTime()},
					chase:false,
					success: function(result){
						if(result.flag==1){
							var data=result.obj;
							html.push('<span>'+tName+'：</span>');
							for(var i=0;i<data.length;i++){
								html.push('<span>'+(i+1)+'、'+data[i].configValue+'</span>');							
							}
							$('#typeFile').html(html.join(''));
							
						}else if(result.flag==0){
							return d.alert({content:result.errorMsg,type:'error'});
						}else if(result.flag==-1){
							return en.show(that.delproject);
						}
					},
					error: function(){
						return d.alert({content:"网络异常,请联系客服"});
					}
				});
		}
		
		
	};
	 
	needhelp.init();
});