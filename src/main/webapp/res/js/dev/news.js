var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt",
	showList:base+'news/list.do',
	newsUrl:base+'news/center.do?id='
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
		"pages"   : "dev/common/pages" 
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","pages"],function($,d,h,en,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init();
	p.init({afterObj:$("#newsListBox")});
	p.changeCallback=function(){ newsList.showList(p.curPage);} 	
	var newsList={
		init:function(){
			var that=this;
			that.showList(1);
			
		},
		showList:function(page){
			var type=$('#newsType').val(),itemsObj=$('#newsList'),flag=false;
			page==1?flag=true:'';
			$.ajax({
				url     : dataUrl.showList,
				data:{type:type,page:page,pageSize:13,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(newsList.showList());
					}else{
						var data=result.obj,total=data.pages,pageNum=data.pageNum,items=data.resultData,len=items.length,html=[];
						for(var i=0;i<len;i++){
							var _item=items[i];
							html.push('<li>');
							html.push('<a href="'+dataUrl.newsUrl+_item["id"]+'" title="">'+_item["title"]+'</a>');
							html.push('<span class="newstime">'+ (new Date(_item["createtime"])).pattern("yyyy-MM-dd")+'</span>');
							html.push('</li>');
						}
						itemsObj.html(html.join(''));
						if(total>1&&flag){ 
								p.pageInit({pageLen:total,isShow:true}); 
						}
					}
				},
				error   : function(r){ 
					d.alert({content:'新闻列表加载失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
			
		}
		
	}
	newsList.init();
});