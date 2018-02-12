var base=window.baseurl;
var dataUrl={
	projectList:base+'/project/list_h5.do',
	latestdonationlist:base+'/project/latestdonationlist.do'
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	window.uCENnavEtSite="p-donationdetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		h5detail.delRecordList(p.curPage,10);
	};
	uc.selectCallback=function(){
		h5detail.delRecordList(1,10);
	};
	
	var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			that.Latestdonationlist();
		},
		Latestdonationlist:function(){
			$.ajax({
				url:dataUrl.latestdonationlist,
				data:{page:1,pageSize:20,t:new Date().getTime()},
				success: function(result){
					if(result.flag==1){
						var data=result.obj.resultData,datas=data,len=datas.length,html=[];
						var html=[];
						for(var i=0;i<len;i++){
						html.push('<div class="item bg2 record"><span class="thumb">');
						if(datas[i].coverImageurl != null ){
							html.push('<img class="fl" src="'+datas[i].coverImageurl+'"></span>');
						}else{
							html.push('<img class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></span>');
						}
						if(datas[i].nickName.indexOf("游客") != -1){
							html.push('<div class="name">爱心人士</div>');
						}else{
							html.push('<div class="name">'+$.ellipsis(datas[i].nickName,5,'..')+'</div>');
						}
						html.push(' <div class="money">捐助');
						html.push('<label>￥'+$.formatNum(datas[i].donatAmount)+'</label>');
						html.push('</div><div class="clear"></div></div>');
						}
						$('#list_latest').html(html.join(''));		
					}
				}
			});
			
		},
	};
	 
	h5detail.init();
});