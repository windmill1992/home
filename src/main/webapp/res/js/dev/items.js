require.config({
	baseUrl:"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"  : "dev/common/util", 
		"dialog"  : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry" 
	},
	urlArgs:"v=20150511"
});

define(["extend","head","entry"],function($,h,en){
	window.sitePage="p-doGood";
	h.init();
	en.init(); 
});