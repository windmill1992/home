var dataUrl={
	items:"http://www.17xs.org/project/list.do",
	viewItem:"http://www.17xs.org/project/view.do"
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
 
	/*====items====*/ 
	/*200:保存(保存未提交); 201:提交(提交未审核); 202:审核未通过; 203:审核通过; 204:募捐中; 205:募捐完成; 206:执行中;*/     
    var items={ 
		typeFlag:-1,
		statusFlag:-1,
		getItem:function(page){ 
			var curTabs=$(".items-tab a.curTab"),
				type=curTabs.eq(0).attr("v"),
				status=curTabs.eq(1).attr("v");  
			var flag=false;
			if(this.typeFlag!=type||this.statusFlag!=status){
				flag=true;
				this.typeFlag=type;
				this.statusFlag=status;
				$("#lstPages").hide();
			} 
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.items,
				data:{typeName:type,status:status,page:page,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.result==0){
							var total=data.total,pageNum=data.pageNum,items=data.items,len=items.length,html=[];
							len=len>pageNum?pageNum:len;
							var itemDtil=dataUrl.viewItem+"?projectId=";  
							for(var i=0;i<len;i++){
								var _item=items[i];
								var donateMoney=_item.donaAmount,helpMoney=_item.cryMoney,schedule;
									schedule=((donateMoney/helpMoney)*100);

								
								schedule=schedule>100?100:parseInt(schedule); 
								
								html.push('<li class="item '+((i+1)%3==0?"nomrgR":"")+'">');
								html.push('<h3 class="item-tit">'+_item.title+'</h3>');
								html.push('<div class="item-con"><div class="item-show"><a target="_blank" href="'+itemDtil+_item.itemId+'">');
							 
								if(!!_item.imageurl){
									html.push('<img src="'+_item.imageurl+'"/>');
								} 
								html.push('</a>'); 
								var left=(itemW*schedule/100)-22; 
								html.push('<div class="item-schedule"><div class="schedule"><span style="left:'+left+'px;"><i>'+schedule+'%</i></span><p><b style="width:'+schedule+'%"></b></p></div></div>');
								html.push('</div>');
								html.push('<p class="item-credits"><i>捐款进度</i><span>'+$.formatNum(donateMoney)+' 元 / '+$.formatNum(helpMoney)+' 元</span></p>');
								html.push('<p class="item-prief">'+_item.information+'</p>');
								html.push('<div class="item-btn">');
								if(schedule==100 || _item.state != 240){
									html.push('<a target="_blank" href="'+itemDtil+_item.itemId+'">募捐完成</a>');
								}else{
							    html.push('<a target="_blank" href="'+itemDtil+_item.itemId+'">立即捐款</a>');
								}
								html.push('</div></div></li>'); 
							}  
							html.push('<div class="clear"></div>');
							itemsObj.html(html.join(''));
							
							if(total>1&&flag){ 
								/*var firstObj=$("#items").children().first(),
									itemH=firstObj.height();
									itemGapT=firstObj.css("margin-top"),
									itemGapB=firstObj.css("margin-bottom");
									itemH=itemH+parseInt(itemGapT)+parseInt(itemGapB); 
								
								$("#items").height(itemH*3+"px"); */ 
								p.pageInit({pageLen:total,isShow:true}); 
							}
							$("html,body").scrollTop(0);//.animate({scrollTop: "0px"}, 500); 
						}else if(data.result==1){
							//itemsObj.html('<li class="prompt"><div class="nodata"></div></li>');
							if(status==0){
								itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有募捐中项目 <a title="">请查看其它类别~</a></div></li>');
							}else if(status==1){
								itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有执行中项目 <a title="">请查看其它类别~</a></div></li>');
							}else{
								itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有已完成项目 <a title="">请查看其它类别~</a></div></li>');
							}
						}else if(data.result==2){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');
							
						}
					}
				}
			});
		}
	};
	
	var itemsObj=$("#items"),itemW=280;
	$(".items-tab").first().children("a").first().addClass("curTab");
	$(".items-tab").last().children("a").first().addClass("curTab");
	
	items.getItem();
	$(".items-tab").children('a').on("click",function(){
		if(!$(this).hasClass("curTab")){
			$(this).addClass("curTab").siblings().removeClass("curTab");
		    items.getItem();
		}
	});
});