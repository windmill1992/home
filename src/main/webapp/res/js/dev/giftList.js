var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt"
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
		"pages"   :"dev/common/pages"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages"],function($,d,h,en,uc,p){
	window.uCENnavEtSite="p-donationdetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		GIFTLIST:base+"enterprise/coredata/donationlist.do",//捐款明细列表
		project:base+'project/view.do?projectId=',
		garden:base+'project/gardenview.do?projectId='
	};
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		legalize.listShow(p.curPage,10);
	};
	uc.selectCallback=function(){
		legalize.listShow(1,10);
	};
	var legalize={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			that.listShow(1);	
		},
		listShow:function(page,pageNum){
			var that=this,listObj=$('#gflist');
			var time=$(".sel-val").attr('v');
			var flag=false;
			if(this.time!=time){
				flag=true;
				this.time=time; 
			} 
			if(!page){page=1;} 
			$.ajax({
				url     : WebAppUrl.GIFTLIST,
				data:{type:time,page:page,pageNum:pageNum,t:new Date().getTime()},
				cache   : false, 
				success : function(result){ 
					if(result.flag==1){
						var data=result.obj,listdata=data.data,html=[],bgcolor;
						that.totle=data.total;that.pageCurrent=data.page;
						
						if(data.total > 0){
							for(var i=0;i<listdata.length;i++){
									bgcolor=(i%2)?"":'bgcolor1';
									html.push('<ul class="'+bgcolor+'" title="'+listdata[i].title+'" id="'+listdata[i].id+'">');
									if(listdata[i].field == 'garden'){
										html.push(' <li class="lst-col1"><a href="'+WebAppUrl.garden+listdata[i].pid+'" title="">'+listdata[i].title+'</a></li>');
									}else{
										html.push(' <li class="lst-col1"><a href="'+WebAppUrl.project+listdata[i].pid+'" title="">'+listdata[i].title+'</a></li>');
									}
									html.push('<li class="lst-col2">￥'+$.formatNum(listdata[i].dMoney)+'</li>');
									html.push('<li class="lst-col4"><div class="donation-progress"><span class="progress-bar" style="width:'+listdata[i].process+'%"></span>');
									html.push('<span class="progress-text"><span class="l">'+listdata[i].process+'%</span><span class="r">￥'+$.formatNum(listdata[i].donatAmountpt)+'/￥'+$.formatNum(listdata[i].cryMoney)+'</span></span></div></li>');
									html.push('<li class="lst-col5 lst-br">'+(new Date(listdata[i].dTime)).pattern("yyyy/MM/dd")+'<br>'+(new Date(listdata[i].dTime)).pattern("HH:mm")+'</li>');
									html.push('<li class="lst-col6 lst-opear">');
									if(listdata[i].field == 'garden'){
										html.push('<a href="'+WebAppUrl.garden+listdata[i].pid+'" class="colorGreen">'+listdata[i].feedBackAmount+'</a>条</li>');
									}else{
										html.push('<a href="'+WebAppUrl.project+listdata[i].pid+'" class="colorGreen">'+listdata[i].feedBackAmount+'</a>条</li>');
									}
									html.push('</ul>');
									
							}
						}else{
							html.push('<div class="listno yh">抱歉，该列表暂时没有数据 <a title="">请查看其它类别~</a></div>');
						}
						$('#gflist').html(html.join(''));
						data.total>1?$('.pageNum').css('bottom','-10px'):$('.pageNum').css('bottom','0px');
						if(data.total>0){
							$('.pageNum').html('总计'+data.nums+'项  累计捐赠<span class="color4">'+$.formatNum(data.info)+'</span>元');
						}
						data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
						data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
						}else if(result.flag==2){
							listObj.html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
						}else if(result.flag==0){
							listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						}else if(result.flag==-1){
							listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
							if(!en.isIn&&$(".loginDialog").size()==0){
								en.show(that.listShow);
							}
						}
				},
				error   : function(r){ 
					d.alert({content:'获取员工信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		}	
		
	};
	 
	legalize.init();
});