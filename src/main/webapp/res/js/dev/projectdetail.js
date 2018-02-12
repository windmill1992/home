require.config({
	baseUrl:window.baseurl+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter"],function($,d,h,en,uc){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		LEGALIZE:"http://www.17xs.org/user/realname.do",//实名认证
		PHOTOFILE:"http://www.17xs.org/file/toupload.do" //实名认证
	}
	
	var detail={
		init:function(){
			var that=this;
			$('#tab-hd span').click(function(){
				var i=$('#tab-hd span').index(this);
				$('#tab-hd span').removeClass('on').eq(i).addClass('on');
				$('#tab-bd .bdcon').hide().eq(i).show();
			})
		},
	}
	detail.init();	
});