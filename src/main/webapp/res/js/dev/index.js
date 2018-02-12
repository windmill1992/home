var dataUrl={
		items:"http://www.17xs.org/index/donationlist.do",
		itemDtil:"http://www.17xs.org"
	};
require.config({
	baseUrl:window.baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"util"  : "dev/common/util", 
		"dialog"  : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry",
		"banner"  : "util/banner",
		"slide"  : "util/slide"
	},
	urlArgs:"v=20150511"
});

define(["extend",'dialog',"head","entry","banner","slide"],function($,d,h,en,ba,sld){
	window.sitePage="p-index";
	d.init();
	h.init();
	en.init();
	ba.init({wrapBox:$(".banner"),imgsWrap:$(".banner-imgs"),prev:$(".prev-btn"),next:$(".next-btn"),slideTxt:$('.slide-txt'),isAuto:true,time:6000});
	sld.init({wrapBox:$(".slide"),imgsWrap:$(".slide-imgs"),time:6000});
	
	
	//ajax----====  //0：热门	 1：教育 2：救灾 3：扶贫	 4：环保	5：疾病 
	var items={
		itemObj:$("#items-list"),
		init:function(){
			var that=this;
			$(".items-tab").children().first().addClass('selected');
			$(".items-tab").children().click(function(){ 
				$(".items-tab").children().children().css({"border-right-width":"1px"});
				if(!$(this).hasClass("selected")){
					$(this).prev().children().css({"border-right-width":0});
					$(this).addClass("selected").siblings().removeClass("selected");
					//ajax----====
					var index = $(".items-tab").children().index($(this));
					that.getItems(index);
				} 
				return;
			});
			
			$("body").on("click","#itemRefrash",function(){
				var index=$(".items-tab").children().index($(".items-tab").children().filter(".selected"));
				that.getItems(index);
			});
			
			that.getItems(0); 
			that.slideInit();
		},
		curPage:0,//当前页
		pageLen:0,//总页数
		itemsLen:0,//项目元素个数
		slideInit:function(){
			var that=this; 
			$(".item-slideBtn").on("click",".toL",function(){ 
				if(that.curPage+1<=that.pageLen){ 
					that.curPage++;  
					that.itemsMove();
					return false;
				} 
				
			}).on("click",".toR",function(){ 
				if(that.curPage>0){ 
					that.curPage--; 
					that.itemsMove(); 
					return false;
				}  
			});
		},
		slideSet:function(){
			this.curPage=0;
			var itemsLen=$("#items-list").children().length,
				pageLen=Math.ceil(itemsLen/3), 
				pageW=$(".items-bd").width();
				this.itemsLen=itemsLen;
				this.pageLen=pageLen;
				this.pageW=pageW;
			 if(itemsLen>0&&pageLen>0){
				$(".item-slideBtn .toR").hide();
			 }else{
				$(".item-slideBtn .toR,.item-slideBtn .toL").hide();
			 }  
			 $("#items-list").width(pageLen*pageW).css("left",0);
		},
		itemsMove:function(){
				var that=this,
					curPage=this.curPage,
					pageLen=this.pageLen,
					pageW=this.pageW;
				
				if(curPage==pageLen){//pageDir 移动方向  1：显示项目最左端   -1：显示项目最右端
					pageDir=-1;
				}else if(curPage==1){
					pageDir=1;
				}
				
				var left=0;
				if(curPage<pageLen&&pageDir==1){
					left=-curPage*pageW;
				}else{
					left=$("#items-list").position().left+pageW;
				}
				$("#items-list").animate({"left":left+"px"});
				
				if(curPage+1==pageLen){
					$(".item-slideBtn .toR").show();
					$(".item-slideBtn .toL").hide();
				}else if(curPage+1==1){
					$(".item-slideBtn .toR").hide();
					$(".item-slideBtn .toL").show();
				}else{
					$(".item-slideBtn .toR").show();
					$(".item-slideBtn .toL").show();
				} 
		},
		getItems:function(type){
			if(type=="" || type==null){type=0;}
			var that=this,itemsObj=this.itemObj;
			$.ajax({
				url:dataUrl.items,
				data:{"type":type,t:new Date().getTime()}, 
				cache:false,
				beforeSend: function(){
					$(".toL,.toR").hide();
					itemsObj.html('<li><div class="loading"><img src="res/images/prompt/loading.gif"/></div></li>');
				},
				success: function(data){
					data = eval(data); 
					if(!data.result){
						var items=data.items,len=items.length,html=[]; 
						if(len/3>1){
							$(".toL").show();
						}
						len=len>9?9:len;
						var dtilUrl=dataUrl.itemDtil+"?projectId="
						for(var i=0;i<len;i++){
							var _item=items[i];
							html.push('<li class="item '+((i+1)%3==0?'nomrgR':'')+'">');
							html.push('<div class="item-img"><a href="http://www.17xs.org/project/view.do?projectId='+_item.itemId+'" target="_blank" >'); 
						if(!!_item.imageurl){
							html.push('<img src="'+_item.imageurl+'" />');
						} 
						html.push('</a></div>');
							html.push('<div class="item-con">');
							html.push('<h3 class="item-tit">'+_item.title+'</h3>');
							var donateMoney=_item.donaAmount,helpMoney=_item.cryMoney,schedule;
							schedule=((donateMoney/helpMoney)*100).toFixed(0);
							schedule==100?schedule=100:schedule=schedule;
							
							html.push('<p class="item-credits"><i>捐款进度</i><span>'+$.formatNum(donateMoney)+' 元 / '+$.formatNum(helpMoney)+' 元</span></p>');
							html.push('<div class="item-schedule"><i>'+schedule+'%</i><p><b style="width:'+schedule+'%"></b></p></div>');
							html.push('<p class="item-prief">'+_item.information+'</p>');
							html.push('<div class="item-btn">');
							schedule==100?html.push('<a target="_blank" href="'+window.baseurl+'project/view.do?projectId='+_item.itemId+'">募捐完成</a>'):html.push('<a target="_blank" href="'+window.baseurl+'project/view.do?projectId='+_item.itemId+'">立即捐款</a>');
							html.push('</div>');
							html.push('</div>');
							html.push('</li>'); 
						} 
						itemsObj.html(html.join('')); 
						that.slideSet();
					}else if(data.result==1){
						itemsObj.html('<li class="item-prompt"><div class="nodata"></div></li>');
					}else if(data.result==2){
						itemsObj.html('<li class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></li>');
						
					}
					
				},
				error:function(e){ 
					itemsObj.html('<li class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></li>');
				}
			});
		}
	};
	items.init(); 
});