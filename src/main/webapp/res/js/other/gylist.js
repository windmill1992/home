var dataUrl={
	items:"http://www.17xs.org/project/gylistdata.do",
	viewItem:"http://www.17xs.org/project/gyauction.do"
};

require.config({
	baseUrl:baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"pages"   : "dev/common/pages" 
	},
	urlArgs:"v=20150511"
});

define(["extend","head","entry","pages"],function($,h,en,p){
	window.sitePage="p-doGood";
	h.init();
	en.init();
	p.init({afterObj:$("#items")});
	p.changeCallback=function(){ items.getItem(p.curPage);} 
 
    var items={ 
		typeFlag:-1,
		statusFlag:-1,
		init:function(){
			var that=this;
			$('#more').click(function(){
				var pageNum = $('#pageNum').val();
				that.getItem(1,parseInt(20)+parseInt(pageNum));
			});
			that.getItem(1,20);
		},
		getItem:function(page,pageNum){ 
			var curTabs=$(".items-tab a.curTab"),
				type=curTabs.eq(0).attr("v"),
				status=curTabs.eq(1).attr("v");  
			var flag=false,size = pageNum;
			if(this.typeFlag!=type||this.statusFlag!=status){
				flag=true;
				this.typeFlag=type;
				this.statusFlag=status;
				$("#lstPages").hide();
			} 
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.items,
				data:{typeName:type,status:status,page:page,pageNum:pageNum,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.flag==1){
							var total=data.obj.total,pageNum=data.pageNum,items=data.obj.resultData,len=items.length,html=[];
							//len=len>pageNum?pageNum:len;
							if(len > pageNum)
							{
								len = pageNum ;
							}
							var itemDtil=dataUrl.viewItem+"?id=";  
							for(var i=0;i<len;i++)
							{
								var _item=items[i];
								html.push('<li class="aution_2_li">');
								html.push('<a href="'+itemDtil+_item.id+'">');
								if(_item.coverImageUrl == null)
								{
									html.push('<img src="http://www.17xs.org/res/images/aution/jp-logo-bgm.jpg"  alt="">');
								}
								else
								{
									html.push('<img src="'+_item.coverImageUrl+'"  alt="">');
								}
								html.push('<dl>');
								html.push('<dt>'+_item.title+'</dt>');
								
								if(_item.number == null)
								{
									_item.number = 0 ;
								}
								
								html.push('<dd><p class="aution_2_p1">￥'+_item.currentPrice+'</p><p class="aution_2_p2"><span>'+_item.number+'</span>次出价</p></dd>');
								if(_item.state == 201)
								{
									html.push('<dd><img src="http://www.17xs.org/res/images/aution/icon1.png" alt=""><p class="aution_2_p3">'+(new Date(_item.deadline)).pattern("MM-dd HH:mm")+' 结束</p></dd>');
								}
								else if(_item.state == 202)
								{
									html.push('<dd><img src="http://www.17xs.org/res/images/aution/icon2-2.png" alt=""><p class="aution_2_p3">'+(new Date(_item.deadline)).pattern("MM-dd HH:mm")+' 结束</p></dd>');
								}
								else 
								{
									html.push('<dd><img src="http://www.17xs.org/res/images/aution/icon2.png" alt=""><p class="aution_2_p3">'+(new Date(_item.deadline)).pattern("MM-dd HH:mm")+' 结束</p></dd>');
								}
								html.push('</dl>');
								html.push('</a>');
								html.push('</li>');
							
							}  
							$('#gyList').html(html.join(''));
							
							if(total>1&&flag){ 
								
								p.pageInit({pageLen:total,isShow:true}); 
							}
							
							$('#pageNum').val(len);
							var htmlMore=[];
							if(total > size){
								htmlMore.push('<a href="javascript:void(0)"alt="" class="center_5_1"><p>点击加载更多</p>');
								htmlMore.push('<span></span></a>');
								$('#more').html(htmlMore.join(''));
							}else{
								$('#more').html(htmlMore.join(''));
							}
						}
						else 
						{
							// 提示
						}
					}
				}
			});
		}
	};
	
	items.init();

});