window.baseurl="http://127.0.0.1:8080";
var dataUrl={
	urlName:''
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
		"userCenter": "dev/common/userCenter",
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","pages"],function($,d,h,en,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	d.init();
	h.init();
	en.init();  
	p.init({afterObj:$("#data-list")});
	p.changeCallback=function(){
		list.getData(p.curPage);
	};
	
	
	var list={
	   getData:function(){
	   		$.ajax({
				url:dataUrl.urlName,
				data:{},
				success: function(){
				
				},
				error:function(){
					
				}
			});
	   }
	};
});