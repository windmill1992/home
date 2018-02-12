require.config({
	baseUrl:window.baseurl+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"  : "dev/common/util", 
		"dialog"  : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry"],function($,d,h,en){
	window.sitePage="p-shanhe";
	h.init(); 
	d.init(); 
	en.init();
});