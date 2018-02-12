var dataUrl={
	garden:'http://www.17xs.org/project/gardenlist.do',
	//grdDtil:'http://www.17xs.org/project/gardenview.do' 
	grdDtil:'http://www.17xs.org/project/newGardenView/'
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
 
define(["extend","dialog","head","entry","pages"],function($,d,h,en,p){
	window.sitePage="p-goodGarden";
	d.init();
	h.init();
	en.init();  
	p.init({afterObj:$("#data-list")});
	p.changeCallback=function(){
		list.getData(p.curPage);
	};
	
	var roll={
		postHtml:[],
		objH:0,
		len:0,
		timer:null,
		rollWrap:null,
		init:function(){
			var that=this;
			that.rollWrap=$("#rollList");
			
			var rollWrap=that.rollWrap;
			var objH=$("#rollBox").height(),len=rollWrap.children().length; 
			rollWrap.height(objH*len+"px");
			
			that.objH=objH,that.len=len;
			
			var postHtml=[],nodes=rollWrap.children('li'),nodesLen=nodes.length;
			for(var i=0;i<nodesLen;i++){
				postHtml.push("<li>"+nodes[i].innerHTML+"</li>");  
			} 
			that.html=postHtml;
			 
			$("#rollUp,#rollDown").on("mouserover",function(){
				clearTimeout(that.timer);
			}).on("click",function(){
				 clearTimeout(that.timer);
				 var html=that.html,rollObj=that.rollWrap;;
				 var dir=$(this).attr("id");
				 if(dir.indexOf("rollDown")!=-1){
					rollObj.css("margin-top",0);
					rollObj.animate({"margin-top":-objH+"px"}); 
					rollObj.html(html.join(''));
					that.html.push(html.shift());//reSort();  
				 }else{  
					rollObj.css("margin-top",-objH+"px");
					rollObj.animate({"margin-top":0});
					rollObj.html(html.join(''));
					that.html.unshift(html.pop());		 
				 } 
		   });
		   
		   that.move();
		},
		move:function(){
			var that=this,html=that.html,rollObj=that.rollWrap;
			clearTimeout(that.timer);
			that.timer=setTimeout(function(){ 
					rollObj.css("margin-top",0); 
					rollObj.animate({"margin-top":-that.objH+"px"},1000); 
					rollObj.html(html.join(''));
					that.html.push(html.shift()); //reSort(); 
					that.move();
			},3000); 
		}
			
	};
		
	roll.init();
 	
	var list={
		listObj:$("#data-list"),
		firstload:1,
		init:function(){  
			var that=this;
			that.getData();
			$("body").on("click","#dataRefrash",function(){
				that.getData();
			});
		},
		isGet:0,
		priceChange:function(v){
			var p=String(v);
			if(v>=10000){
				//v=p.substr(0,p.length-4)+'万';
				v=v/10000+'万';
			}
			return v;
		},
		getData:function(page){
			var that=this,listObj=this.listObj; 
			if(!page){page=1;} 
			if(!this.isGet){
				that.isGet=1;
				$.ajax({
					url:dataUrl.garden,
					data:{page:page},
					success: function(result){ 
						if(!!result){
							var rst=eval(result);
							if(!rst.result){
								var html=[],datas=rst.items,len=datas.length,total=rst.total,pageShow=rst.pageNum;
								len=len>pageShow?pageShow:len;
								
								var dtilUrl=dataUrl.grdDtil+'?projectId=';
								for(var i=0;i<len;i++){
									var idata=datas[i];
									html.push('<li'+((i+1)%3==0?' class="nomrgR"':'')+'>'); 
									html.push('<a href="'+dtilUrl+idata.itemId+'" target="_self">');
									if(!Number(idata.surplusnum) || idata.state == 260){
										html.push('<div class="soldOut"><div class="outBg"></div><i>已完成</i></div>');
									} 
									/*html.push('<div class="data-show">');
									if(!!idata.imageurl){
										html.push('<img src="'+idata.imageurl+'"/>');
									}
									html.push('</div>'); */
									
									if(!!idata.imageurl){
										html.push('<img class="data-show" src="'+idata.imageurl+'"/>');
									}else{
										html.push('<div class="data-show"></div>');
									} 
									html.push('<p class="data-brief">'+idata.information+'</p>');
									html.push('<div class="data-sale"><img class="logo-img" src="'+window.baseurl+'res/images/logo-img.png">剩余份数：<b>'+idata.surplusnum+'</b>份 <span class="fltR">'+that.priceChange(idata.price)+'元</span></div>');
									html.push('</a></li>');
								}
								
								listObj.html(html.join(''));
								if(total>1&&that.firstload){
									that.firstload=false;
									var firstObj=$("#data-list").children().first(),
									    itemH=firstObj.height();
										itemGapT=firstObj.css("margin-top"),
										itemGapB=firstObj.css("margin-bottom");
										itemH=itemH+parseInt(itemGapT)+parseInt(itemGapB); 
									$("#data-list").height(itemH*3+"px"); 
									p.pageInit({pageLen:total,isShow:true}); 
								} 
							}else if(rst.result==1){
								listObj.html('<li class="prompt"><div class="nodata"></div></li>');
							}else if(rst.result==2){
								listObj.html('<li class="prompt"><div class="wrong"><a id="dataRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></li>');
								
							} 
						}
						that.isGet=0;
					},
					error:function(){
						that.isGet=0;
					}
				});
			}
		}
	};
	
	list.init();
});






