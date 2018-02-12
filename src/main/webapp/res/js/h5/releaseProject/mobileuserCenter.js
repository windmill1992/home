var base=window.baseurl;
var dataUrl={
		projectShenHe:base+"uCenterProject/uCenterProjectList.do?state=210"    //审核项目的列表  
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
		"ajaxform"  : "util/ajaxform"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","util","head","entry","ajaxform"],function($,d,u,h,en,f){
	window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	var uCenter={
		
		init:function(){
        var that=this;	
        //初始化加载审核的项目
//        window.location.href=dataUrl.projectShenHe;
        $('#shenhe').click(function(){
        	that.shenhe();
    	});
	},
	
	
	};
	uCenter.init();
});