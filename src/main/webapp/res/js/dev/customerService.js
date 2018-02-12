var base=window.baseurl;
var dataUrl={
	itemList:base+"user/customerServiceList.do", //客服中心列表
	itemDetail:base+"user/toServiceDetail.do?id=" // 详情页
};

require.config({
	baseUrl:baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter": "dev/common/userCenter",
		"pages"   :"dev/common/pages"
	},
	urlArgs:"v=20151116"
});

define(["extend","head","entry","userCenter","pages"],function($,h,en,uc,p){
	//window.sitePage="p-doGood";
	h.init();
	en.init();
	uc.init();
	p.init({afterObj:$("#serviceList")});
	p.changeCallback=function(){ items.getItem(p.curPage);} 
	p.changeCallback=function(){
		items.getItem(p.curPage);
	};
	uc.selectCallback=function(){
		items.getItem(p.curPage);
	};
 
	var itemsObj=$("#serviceList");
    var items={ 
		init:function(){
			var that=this;
			that.getItem();
			$(".xuanzelan_qh li").click(function(){
				 $(".xuanzelan_qh li").removeClass("lancu");
				 $(this).addClass("lancu");	
				 that.getItem();
			 })
		},
		getItem:function(page){ 
			var that=this,type=$(".lancu").attr('v'),Type={'0':'咨询','1':'建议','2':'投诉'},Status={'300':'未回复','302':'已回复'};
			var flag=false;
			if(this.type!=type){
				flag=true;
				this.type=type; 
			}
			if(type==-1){type = null}
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.itemList,
				data:{pageNum:10,page:page,type:type,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.result==0){
							var total=data.total,pageNum=data.pageNum,items=data.items,len=items.length,html=[];
							len=len>pageNum?pageNum:len;
							for(var i=0;i<len;i++){
								var _item=items[i];
								var _itemDetail = dataUrl.itemDetail+_item.id;
								if(_item.visit == null){_item.visit =0}
								html.push('<ul>');
								if(_item.type == 0){
									html.push('<li class="col_1"><a href="'+_itemDetail+'"><span class="zixunbiaoqian">【'+Type[_item.type]+'】</span>'+$.ellipsis(_item.content,14,'...')+'</a></li>');
								}else if(_item.type == 1){
									html.push('<li class="col_1"><a href="'+_itemDetail+'"><span class="jianyibiaoqian">【'+Type[_item.type]+'】</span>'+$.ellipsis(_item.content,14,'...')+'</a></li>');
								}else{
									html.push('<li class="col_1"><a href="'+_itemDetail+'"><span class="tousubiaoqian">【'+Type[_item.type]+'】</span>'+$.ellipsis(_item.content,14,'...')+'</a></li>');
								}
								
								html.push('<li class="col_2">'+(new Date(_item.createTime)).pattern("yyyy-MM-dd HH:mm:ss")+'</li>');
								if(_item.state == 300){
									html.push('<li class="col_3_weihuifu">'+Status[_item.state]+'</li>');
								}else{
									html.push('<li class="col_3_huifu">'+Status[_item.state]+'</li>');
								}
								
								html.push('<li class="col_4">'+_item.visit+'</li>');
								html.push('</ul>');
							}  
							itemsObj.html(html.join(''));
							
							if(total>1&&flag){ 
								p.pageInit({pageLen:total,isShow:true}); 
							}
							if(total==1&&flag){ 
								p.pageInit({pageLen:total,isShow:false}); 
							}
						}else if(data.result==1){
							if(status==0){
								itemsObj.html('<li class="prompt"><div class="listno yh">暂时没有数据！！！</div></li>');
							}else if(status==1){
								itemsObj.html('<li class="prompt"><div class="listno yh">暂时没有数据！！！</div></li>');
							}else{
								itemsObj.html('<li class="prompt"><div class="listno yh">暂时没有数据！！！</div></li>');
							}
						}else if(data.result==2){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');
							
						}
					}
				}
			});
		}
	};
	
	items.init();
	
});