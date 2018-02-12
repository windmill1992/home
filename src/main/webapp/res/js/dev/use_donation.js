var base=window.baseurl;
var dataUrl={
	dataList:base+'project/donationlist.do',//捐款记录列表
	itemDetail:base+'project/view.do',     //项目详情页
	itemDetail2:base+'project/newGardenView.do',  //善园详情页
	itemDetail3:base+'ucenter/core/focusProject.do', //关注的项目
	itemDetail4:'http://sy.17xs.org/project/syview.do'     //善缘项目详情页
};
require.config({
	baseUrl:base+"/res/js",
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
	urlArgs:"v=20151010"
});

define(["extend","head","entry","userCenter","pages"],function($,h,en,uc,p){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	window.mCENnavEtSite="m-jk";
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		use_donation.getData(p.curPage);
	};
	uc.selectCallback=function(){
		use_donation.getData(p.curPage);
	};
	 
	var listObj=$("#listShow"); 
	var use_donation={
		time:null, 
		init:function(){	
			var that=this;
			that.getData();
		},
		getData:function(page){ 
			var that=this,html=[],time=$(".sel-val").attr('v'),userId=1111,Status={'240':'募捐中','260':'已结束'},dType={'1':'助善','2':'捐款'};//用户ID;
			var flag=false;
			if(this.time!=time){
				flag=true;
				this.time=time; 
			} 
			if(!page){page=1;} 
			html.push('<dl><dt><span class="naidt_01">项目标题</span><span class="naidt_02">捐助金额</span><span class="naidt_03">受助人</span><span class="naidt_04">反馈</span><span class="naidt_05">捐助时间</span><span class="naidt_06">捐助类型</span></dt>');
			$.ajax({
				url:dataUrl.dataList,
				data:{type:time,page:page,pageNum:10,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length;
						len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						var itemDtil2=dataUrl.itemDetail3+'?projectId=';
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							if(idata.field=="garden"){
								itemDtil=dataUrl.itemDetail2+'?projectId=';
							}else if(idata.field=="good"){
								itemDtil=dataUrl.itemDetail4+'?projectId=';
							}
							html.push('<dd><div class="naidd_hkp">');
							html.push('<span class="naidd_01"><a href="'+itemDtil+idata.pid+'" target="_blank">'+$.ellipsis(datas[i].title,14,'...')+'</a></span>');
							html.push('<span class="naidd_02">'+$.formatNum(datas[i].dMoney)+'元</span>');
							html.push('<span class="naidd_03">'+datas[i].recipients+'</span>');
							if(datas[i].feedBackAmount==0){
								html.push('<span class="naidd_04">无反馈</span>');
							}else{
								html.push('<span class="naidd_04"><a href="'+itemDtil2+idata.pid+'">'+datas[i].feedBackAmount+'</a>条</span>');
							}
							html.push('<span class="naidd_05">'+(new Date(datas[i].dTime)).pattern("yyyy-MM-dd")+'</span>');
							html.push('<span class="naidd_06">'+dType[(datas[i].dType)]+'</span>');
							html.push('</div>');
							if(datas[i].status==240){
								html.push('<div class="naidd_gde"><i class="nde_puo">'+Status[datas[i].status]+'</i>已募集<em>'+datas[i].donatAmountpt+'</em>元</div>');
							}
							if(datas[i].status==260){
								html.push('<div class="naidd_gde"><i class="nde_puo">'+Status[datas[i].status]+'</i>筹得<em>'+$.formatNum(datas[i].donatAmountpt)+'</em>元</div>');
							}
							
							html.push('</dd>');
						}  
						html.push('</dl>');
						listObj.html(html.join(''));
						
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
					}else if(result.flag==2){
						//html.push('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有捐款记录</div></div>');
						listObj.html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有捐款记录</div></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(that.getData);
						}
					}
				}
			});
		}
	};	
	use_donation.init(); 
	 
});