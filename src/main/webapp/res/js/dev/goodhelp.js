var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt"
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
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		ENTERPRISE:base+""//企业完善资料接口
	}
	goodhelp={
		init:function(){
			var that=this;
			$(".items-tab").children().first().addClass('selected');
			$(".items-tab").children().click(function(){ 
				$(".items-tab").children().children().css({"border-right-width":"1px"});
				if(!$(this).hasClass("selected")){
					$(this).prev().children().css({"border-right-width":0});
					$(this).addClass("selected").siblings().removeClass("selected");
					//ajax----====
					var index = $(".items-tab").children().index($(this));
					that.getItems(index);
				} 
				return;
			});
		}
	}
	goodhelp.init();
});