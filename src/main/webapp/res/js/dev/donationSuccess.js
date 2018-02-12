var base=window.baseurl;
var dataUrl={
		
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entryNew",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform" ,
		"pages"   :"dev/common/pages" 
	},
	urlArgs:"v=20151010"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages"],function($,d,h,en,uc,f,p){
	//window.mCENnavEtSite="m-acccountTracking";
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#dtList")});
	p.changeCallback=function(){
		
	};
	donat={
		init:function(){
			var that=this;
			$('.changeNickName').click(function () {
				location.href = "http://www.17xs.org/ucenter/getUserDetail.do";
			})
		
		}

	}
	donat.init();
});