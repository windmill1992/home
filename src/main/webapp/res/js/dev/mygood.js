var base=window.baseurl;
var dataUrl={
	donationlist:base+"project/donationlist.do",
	itemDetail:base+'project/view.do',
	itemDetail2:base+'project/gardenview.do'
};
require.config({
	baseUrl:base+"/res/js",
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

define(["extend","head","entry","userCenter","pages"],function($,h,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-jk";//如需导航显示当前页则加此设置
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#list-bd")});
	p.changeCallback=function(){
		mygood.getData(p.curPage);
	};
	uc.selectCallback=function(){
		mygood.getData(p.curPage);
	};
	 
	var listObj=$("#list-bd"); 
	var mygood={
		time:null, 
		init:function(){
			$("body").on("click","#itemRefrash",function(){
				mygood.getData();
			});
			mygood.getData();
		},
		getData:function(page){ 
			var time=$(".sel-val").attr('v');
			var flag=false;
			if(this.time!=time){
				flag=true;
				this.time=time; 
			} 
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.donationlist,//http://127.0.0.1:8080/data/goodlist.json
				data:{type:time,page:page,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							if(idata.field=="garden"){
								itemDtil=dataUrl.itemDetail2+'?projectId=';
							}else{
								itemDtil=dataUrl.itemDetail+'?projectId=';
							}
							html.push('<ul '+((i+1)%2==0?'class="bgcolor1"':'')+'>');
							html.push('<li class="lst-col1">&nbsp;<a href="'+itemDtil+idata.pid+'">'+idata.title+'</a></li>');
							html.push('<li class="lst-col2">'+(new Date(idata.dTime)).pattern("yyyy-MM-dd")+'</li>');
							html.push('<li class="lst-col3">'+(Number(idata.dMoney)).toFixed(2)+'元</li>');
							var process=idata.process;
							if(idata.field=="garden"){
								process=100;
							}
							if(process==100){
								html.push('<li class="lst-col4">已完成</li>');
							}else{
								html.push('<li class="lst-col4">');
								html.push('<p class="schedule-tit">'+process+'%</p>')
								html.push('<span class="schedule-show"><b style="width:'+process+'%"></b></span>'); 
								html.push('</li>');
							}
							html.push('<li class="lst-col5"><a href="'+itemDtil+idata.pid+'">查看详情</a></li>');
							/*if(idata.dStatus==302){ 
								html.push('<li class="lst-col5"><a href="'+itemDtil+idata.pid+'">查看详情</a></li>');
							}else if(idata.dStatus==300){
								html.push('<li class="lst-col5"><a href="'+itemDtil+idata.pid+'">还未捐款</a></li>');
							}else if(idata.dStatus==301){
								html.push('<li class="lst-col5"><a href="'+itemDtil+idata.pid+'">捐款失败</a></li>');
							}*/
							
							html.push('</ul>');
						}  
						listObj.html(html.join(''));
						
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
					}else if(result.flag==2){
						listObj.html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(mygood.getData);
						}
					}
				}
			});
		}
	};
	
	mygood.init(); 
	 
});