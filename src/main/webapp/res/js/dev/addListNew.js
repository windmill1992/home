var base=window.baseurl;
var dataUrl={
	addList:base+'news/list.do',//公告列表
	addUrl:base+'news/center.do'//公告详情连接
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
	p.init({afterObj:$("#adList")});
	p.changeCallback=function(){
		addListNew.dataList(p.curPage,10);
	};
	addListNew={
		init:function(){
			var that=this;
			that.dataList(1,10);
		},
		dataList:function(page,num){
			var that=this,html=[],userId='',flag=false;
			if(page<2){flag=true};
			$.ajax({
				url:dataUrl.addList,
				data:{type:1,page:page,pageSize:num,t:new Date()},
				success: function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.dataList);
					}else{ 
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length;
						len=len>pageNum?pageNum:len; 
						if(data.total>0){
							for(var i=0;i<len;i++){
							    var titleurl=dataUrl.addUrl+"?id="+datas[i].projectId;
								html.push('<p><a href="'+titleurl+'" title=""><span class="listIcon"></span>【公告】'+$.ellipsis(datas[i].title,14,'...')+'（5）</a><span class="more">'+(new Date(datas[i].createtime)).pattern("yyyy-MM-dd")+'</span></p>');
							}
						}else{
							html.push('<div class="listno yh">抱歉， 该列表暂时没有相关公告列表</div>');
						}
						$('#listShow').html(html.join(''));			
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
					}
				},
				error   : function(r){ 
					d.alert({content:'获取公告列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			})
		}
	}
	addListNew.init();
});