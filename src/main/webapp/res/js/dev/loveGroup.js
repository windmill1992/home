require.config({
	baseUrl:window.baseurl+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry" 
	},
	urlArgs:"v=20150511"
});

define(["extend","head","entry"],function(ex,h,en,ba,sld){
	window.sitePage="p-loveGroup";
	h.init();
	en.init(); 
});