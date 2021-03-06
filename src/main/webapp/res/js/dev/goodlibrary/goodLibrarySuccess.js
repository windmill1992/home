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
	//window.sitePage="p-seekHelp"; //如需导航显示当前页则加此设置
	h.init(); 
	d.init(); 
	en.init();
	var reg={
		init:function(){
			var that=this;
			$('#addGoodLibraryPerson').click(function(){
				d.alert({content:"请用微信扫一扫左边的二维码！",type:'success'});
			})
			}
		}
	reg.init();
});