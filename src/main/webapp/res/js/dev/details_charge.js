var base=window.baseurl;
var dataUrl={
	chargelist:base+'/ucenter/core/chargeRecordData.do'//充值明细
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
	window.uCENnavEtSite="p-capitaldetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#itemList")});
	p.changeCallback=function(){
		detail.getItem(p.curPage,8);
	};
	uc.selectCallback=function(){
		detail.getItem(1,8);
	};
	var detail={
		totle:0,
		pageCurrent:1,
		typeFlag:-1,
		init:function(){
			var that=this;
			that.getItem(1,8);
		},		
		getItem:function(page,pageSize){
			var time=$(".sel-val").attr('v'),listObj=$('#itemList');
			var flag=false;
			if(this.time!=time){
				flag=true;
				this.time=time; 
			} 
			if(!page){page=1;} 
			$.ajax({
				url:dataUrl.chargelist,
				data:{type:time,page:page,pageNum:pageSize,t:new Date().getTime()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var tmoney=result.obj.TchargeMoney,data=result.obj.data,total=data.pages,pageNum=data.pageSize,datas=data.resultData,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						html.push('<ul class="color-bg ul1">');
						html.push('<li class="li1">时间</li>');
						html.push('<li class="li2">充值方式</li>');
						html.push('<li class="li3">金额（元）</li>');
						html.push('<li class="li4">流水号</li>');
						html.push('<li class="li5">状态</li>');
						html.push('</ul>');
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							if(idata.field=="garden"){
								itemDtil=dataUrl.itemDetail2+'?projectId=';
							}else{
								itemDtil=dataUrl.itemDetail+'?projectId=';
							}
							html.push('<ul '+((i+1)%2!=0?'class="bgcolor1"':'')+'>');
							html.push('<li class="li1">'+(new Date(idata.createTime)).pattern("yyyy/MM/dd HH:mm")+'</li>');
							if(idata.payType == 'alipay'){
								html.push('<li class="li2 ">支付宝</li>');
							}else if(idata.payType == 'bankpay'){
								html.push('<li class="li2 ">网上银行</li>');
							}else if(idata.payType == 'back'){
								html.push('<li class="li2 ">系统退款</li>');
							}else if(idata.payType == 'lianlian'){
								html.push('<li class="li2 ">连连支付</li>');
							}else if(idata.payType == 'freezType'){
								html.push('<li class="li2 ">冻结支付</li>');;
							}else if(idata.payType == 'yinhanghuikuan'){
								html.push('<li class="li2 ">银行汇款</li>');;
							}else{
								html.push('<li class="li2 ">其它支付</li>');
							}
							html.push('<li class="li3 color">'+$.formatNum(idata.money)+'</li>');
							html.push('<li class="li4">'+idata.tranNum+'</li>');
							html.push('<li class="li5">成功</li>');
							html.push('</ul>');
						}  
						$('#itemList').html(html.join(''));
						$('#tmoney').html($.formatNum(tmoney));
						data.pages>1&&flag?p.pageInit({pageLen:data.pages,isShow:true}):'';
					}else if(result.flag==2){
						$('#itemList').html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
					}else if(result.flag==0){
						$('#itemList').html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						$('#itemList').html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(mygood.getData);
						}
					}
				}
			});
		}	
	}
	 
	detail.init();
});