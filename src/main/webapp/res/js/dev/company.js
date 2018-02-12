var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt",
	items:"http://www.17xs.org/project/list.do",
	viewItem:"http://www.17xs.org/project/view.do",
	gardenItem:"http://www.17xs.org/project/newGardenView.do",
	donationlist:base+'project/donationlist.do',//捐款列表
	zhuShanList:base+'enproject/zhuShanList.do',//助善项目
	edsituation:base+'enproject/edsituation.do'//善员工动态
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
		"pages"   :"dev/common/pages"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages"],function($,d,h,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		GOODHELPLIST:base+"/"//企业助善列表
	}
	p.init({afterObj:$("#itemlist")});
	p.changeCallback=function(){
		company.getItem(p.curPage);
	};
	var company={
		totle:0,
		pageCurrent:1,
		typeFlag:-1,
		init:function(){
			var that=this;
			that.getItem(1);
			$(".items-tab li").click(function(){
				var i=$(".items-tab li").index(this);
				$(".items-tab li").removeClass('selected').eq(i).addClass('selected');
				that.getItem(1);
			})
			
			$('.listone .smark').hover(function(){
				$(this).children('em').show();
			},function(){
				$(this).children('em').hide();
			})
			$("body").on("click",".qqim",function(){ 
				 that.bshare(event,'qqim',0);
			}).on("click",".qzone",function(){ 
				 that.bshare(event,'qzone',0);
			}).on("click",".sinaminiblog",function(){ 
				 that.bshare(event,'sinaminiblog',0);
			}).on("click",".weixin",function(){ 
				 that.bshare(event,'weixin',0);
			}).on("mousover",".delLogo",function(){ 
				 $(this).children('em').show();
			}).on("mousout",".delLogo",function(){ 
				 $(this).children('em').hide();
			}).on("click",".Invitation",function(){ 
				var data=$(this).attr('data');
				$('#projectID').val(data);
				 that.share();
				 
			})
			
			
		},
		share:function(){
			var html=[],v=$('#projectID').val().split(','),m=$('#companyCode').val(),name=$.cookie.get("sjj_username");
			html.push('<h2>邀请内容</h2>');
			html.push('<textarea class="area">'+name+'，邀请您一起行善！注册善园基金会，企业即以您的名义，助捐'+v[1]+'元！(注册邀请码：'+m+')</textarea>');
			html.push('<h2>传播至：</h2>');
			html.push('<div class="shareWeblist">');
			html.push('<a class="qqim" >QQ好友</a>');
			html.push('<a class="qzone">QQ空间</a>');
			html.push('<a class="sinaminiblog" >新浪微博</a>');
			html.push('<a class="weixin">微信</a>');
			html.push('</div>');
			var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
			d.open(o,css,"Delshare"); 	
		},
		bshare:function(event,o){
			var v=$('#projectID').val().split(','),m=$('#companyCode').val(),name=$.cookie.get("sjj_username");
			 bShare.addEntry({
			  title: "【善园基金会】",
			  url: base+"/user/sso.do?projectId="+v[0],
			  summary:name+"，邀请您一起行善！注册善园基金会，企业即以您的名义，助捐"+v[1]+"元！(注册邀请码："+m+")" ,
			  pic: ""
			});
			bShare.share(event,o,0);
		},		
		getItem:function(page){
			var that=this,curTabs=$(".items-tab li.selected"),
				type=curTabs.eq(0).attr("v");
			var flag=false;
			if(this.typeFlag!=type){
				flag=true;
				this.typeFlag=type;
				$("#lstPages").hide();
			} 
			if(!page){page=1;} 
			if(type==0){
				that.donationlist(page,flag);
			}else if(type==1){
				that.zhuShanList(page,flag);
			}else{
				that.edsituation(page,flag);
			}
		},
		donationlist:function(page,flag){
			var itemsObj=$("#itemlist"),that=this,flag=flag,companyUserId=$("#companyUserId").val();
			companyUserId=(companyUserId?companyUserId:null);
			$.ajax({
				url:dataUrl.donationlist,
				data:{page:page,userId:companyUserId,type:0,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.flag==1){
							var data=data.obj;
							var total=data.total,pageNum=data.pageNum,items=data.data,len=items.length,html=[],itemDtil;
							len=len>pageNum?pageNum:len;
							html.push('<div class="list yh">');
							html.push('<ul class="list-hd">');
							html.push('<li class="lst-col1">项目名称</li>');
							html.push('<li class="lst-col2">捐赠金额</li>');
							html.push('<li class="lst-col3">捐赠进度</li>');
							html.push('<li class="lst-col4">捐赠时间</li>');
							html.push('</ul><div id="list-bd" class="list-bd">');
							
							for(var i=0;i<len;i++){
								var _item=items[i];
								_item.field=="garden"?itemDtil=dataUrl.gardenItem+"?projectId=":itemDtil=dataUrl.viewItem+"?projectId=";  
								bgcolor=(i%2)?"":'bgcolor1';
								html.push('<ul class="'+bgcolor+'">');
								html.push('<li class="lst-col1">&nbsp;<a href="'+itemDtil+_item.pid+'">'+_item.title+'</a></li>');
								html.push('<li class="lst-col2">￥'+$.formatNum(_item.dMoney)+'</li>');
								html.push('<li class="lst-col3">');
								html.push('<div class="donation-progress">');
								html.push('<span class="progress-bar" style="width:'+_item.process+'%"></span>');
								html.push('<span class="progress-text">'+_item.process+'%</span>');
								html.push('</div></li>');
								html.push('<li class="lst-col4 lst-br">'+new Date(_item.dTime).pattern("yyyy/MM/dd<br>HH:mm")+'</li>');
								html.push('</ul>');
							}  
							html.push('</div>');
							html.push('<div class="clear"></div>');
							itemsObj.html(html.join(''));
							
							if(total>1&&flag){ 
								p.pageInit({pageLen:total,isShow:true}); 
							}
							$("html,body").scrollTop(0);//.animate({scrollTop: "0px"}, 500); 
						}else if(data.flag==2){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有募捐中项目 <a title="">请查看其它类别~</a></div></li>');
						}else if(data.flag==0){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');	
						}else if(data.flag==-1){
								en.show(that.donationlist);
						}
					}
				}
			});
		},
		zhuShanList:function(page,flag){
			var itemsObj=$("#itemlist"),that=this,companyUserId=$("#companyUserId").val();
			companyUserId=(companyUserId?companyUserId:null);
			$.ajax({
				url:dataUrl.zhuShanList,
				data:{page:page,type:'abc',userId:companyUserId,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.flag==1){
							var data=data.obj;
							var total=data.total,pageNum=data.pageNum,items=data.data,len=items.length,html=[];
							len=len>pageNum?pageNum:len;
							var itemDtil=dataUrl.viewItem+"?projectId=";  
							html.push('<div class="list yh listhelp">');
							html.push('<ul class="list-hd">');
							html.push('<li class="lst-col1">项目名称</li>');
							html.push('<li class="lst-col2">捐赠金额/人</li>');
							html.push('<li class="lst-col3">已号召人数</li>');
							html.push('<li class="lst-col4">项目进展（已助善/总预算捐赠）</li>');
							html.push('<li class="lst-col5">发起时间</li>');
							html.push('<li class="lst-col6">善播</li>');
							html.push('</ul><div id="list-bd" class="list-bd">');
							for(var i=0;i<len;i++){
								var _item=items[i];
								bgcolor=(i%2)?"":'bgcolor1';
								html.push('<ul class="'+bgcolor+'">');
								html.push('<li class="lst-col1">&nbsp;<a href="'+itemDtil+_item.pId+'">'+_item.pName+'</a></li>');
								html.push('<li class="lst-col2">￥'+$.formatNum(_item.perMoney)+'</li>');
								html.push('<li class="lst-col3">'+_item.peopleNums+'</li>');
								html.push('<li class="lst-col4">');
								html.push('<div class="donation-progress">');
								html.push('<span class="progress-bar" style="width:'+_item.process+'%"></span>');
								html.push('<span class="progress-text"><span class="l">'+_item.process+'%</span><span class="r">￥'+$.formatNum(_item.payMoney)+'/￥'+$.formatNum(_item.totalMoney)+'</span></span>');
								html.push('</div></li>');
								html.push('<li class="lst-col5 lst-br">'+new Date(_item.cTime).pattern("yyyy/MM/dd<br>HH:mm")+'</li>');
								html.push('<li class="lst-col6"><a class="Invitation" title="" data="'+_item.pId+','+_item.perMoney+'">邀请好友</a></li>');
								html.push('</ul>');
							}  
							html.push('</div>');
							html.push('<div class="clear"></div>');
							itemsObj.html(html.join(''));
							
							if(total>1&&flag){ 
								p.pageInit({pageLen:total,isShow:true}); 
							}
							$("html,body").scrollTop(0);//.animate({scrollTop: "0px"}, 500); 
						}else if(data.flag==2){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有募捐中项目 <a title="">请查看其它类别~</a></div></li>');
						}else if(data.flag==0){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');	
						}else if(data.flag==-1){
								en.show(that.donationlist);
						}
					}
				}
			});
		},
		edsituation:function(page,flag){
			var itemsObj=$("#itemlist"),that=this,companyUserId=$("#companyUserId").val();
			companyUserId=(companyUserId?companyUserId:null);
			$.ajax({
				url:dataUrl.edsituation,
				data:{page:page,userId:companyUserId,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.flag==1){
							var data=data.obj;
							var total=data.total,pageNum=data.pageNum,items=data.data,len=items.length,html=[],itemDtil;
							len=len>pageNum?pageNum:len;
							html.push('<div class="list yh">');
							html.push('<ul class="list-hd">');
							html.push('<li class="lst-col1">帐号名</li>');
							html.push('<li class="lst-col2">捐赠名称</li>');
							html.push('<li class="lst-col3">捐赠进度</li>');
							html.push('<li class="lst-col4">捐赠时间</li>');
							html.push('</ul><div id="list-bd" class="list-bd">');
							for(var i=0;i<len;i++){
								var _item=items[i];
								_item.field=="garden"?itemDtil=dataUrl.gardenItem+"?projectId=":itemDtil=dataUrl.viewItem+"?projectId=";  
								bgcolor=(i%2)?"":'bgcolor1';
								html.push('<ul class="'+bgcolor+'">');
								html.push('<li class="lst-col1">'+_item.eName+'</li>');
								html.push('<li class="lst-col2">&nbsp;<a href="'+itemDtil+_item.pId+'">'+_item.pName+'</a></li>');
								html.push('<li class="lst-col3">');
								html.push('<div class="donation-progress">');
								html.push('<span class="progress-bar" style="width:'+_item.pProcess+'%"></span>');
								html.push('<span class="progress-text">'+_item.pProcess+'%</span>');
								html.push('</div></li>');
								html.push('<li class="lst-col4 lst-br">'+new Date(_item.pCtime).pattern("yyyy/MM/dd<br>HH:mm")+'</li>');
								html.push('</ul>');
							}  
							html.push('</div>');
							html.push('<div class="clear"></div>');
							itemsObj.html(html.join(''));
							
							if(total>1&&flag){ 
								p.pageInit({pageLen:total,isShow:true}); 
							}
							$("html,body").scrollTop(0);//.animate({scrollTop: "0px"}, 500); 
						}else if(data.flag==2){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表暂时没有募捐中项目 <a title="">请查看其它类别~</a></div></li>');
						}else if(data.flag==0){
							itemsObj.html('<li class="prompt"><div class="listno yh">抱歉，该列表读取数据出错<a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">请联系客服</a></div></li>');	
						}else if(data.flag==-1){
								en.show(that.donationlist);
						}
					}
				}
			});
		}
		
		
	}
	 
	company.init();
});