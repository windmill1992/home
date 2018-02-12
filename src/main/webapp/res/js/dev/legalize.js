var dataUrl={
	garden:'/data/garden/list.json',
	joinGood:'/data/joinGood.txt',
	toLegalize:baseurl+'/applyLegalize.html',
	toInfo:baseurl+'/applyInfo.html',
	toRst:baseurl+'/applyResult.html'
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

define(["extend","head","entry","dialog"],function($,h,en,d){
	//window.sitePage="p-seekHelp";  --加入善管家-捐款人 -个人中心
	h.init();
	en.init(); 
	
	$("#rlbokbtn").on("click",function(){
		window.location=dataUrl.toInfo;
	});
	 
});