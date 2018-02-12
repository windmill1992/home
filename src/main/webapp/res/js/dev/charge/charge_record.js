var base = window.baseurl;
var dataUrl = {
	items : "/DATA/items.txt"
};
require.config({
	baseUrl : base + "/res/js",
	paths : {
		"jquery" : [ "jquery-1.8.2.min" ],
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util" : "dev/common/util",
		"head" : "dev/common/headNew",
		"entry" : "dev/common/entryNew",
		"userCenter" : "dev/common/userCenter",
		"pages" : "dev/common/pages"
	},
	urlArgs : "v=20150511"
});

define(
		[ "extend", "dialog", "head", "entry", "userCenter", "pages" ],
		function($, d, h, en, uc, p) {
			window.uCENnavEtSite="p-recharge";//如需导航显示当前页则加此设置
			h.init();
			d.init();
			en.init();
			uc.init();
			WebAppUrl = {
				CHARGEDATA : base+ "/ucenter/core/chargeRecordData.do"// 充值的数据记录
			};
			p.init({afterObj:$("#pageNum")});
			p.changeCallback=function(){
				record.listShow(p.curPage,8);
			};
			uc.selectCallback=function(){
				record.listShow(1,8);
			};
			var record={
				totle:0,
				pageCurrent:1,
				init:function(){
					var that=this;
					that.listShow(1,8);
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
						url     : WebAppUrl.CHARGEDATA,
						data:{type:time,page:page,pageNum:pageNum,t:new Date().getTime()},
						cache   : false, 
						success : function(result){ 
							if(result.reslut=='0'){
								d.alert({content:result.errorMsg,type:'error'});
								return false;
							}else if(result.reslut=='-1'){
								en.show(that.listShow);
							}else{
								var tmoney=result.obj.TchargeMoney,data=result.obj.data,listdata=data.resultData,html=[],bgcolor;
								that.totle=data.total;that.pageCurrent=data.page;
								
								if(data.pages > 0){
									for(var i=0;i<listdata.length;i++){
											bgcolor=(i%2)?"":'bgcolor1';
											html.push('<ul class="'+bgcolor+'">');
											html.push('<li class="li1">'+(new Date(listdata[i].createTime)).pattern("yyyy/MM/dd  HH:mm:ss")+'</li>');
											if(listdata[i].payType == 'bankpay'){
												html.push('<li class="li2 ">网上银行</li>');
											}else if(listdata[i].payType == 'back'){
												html.push('<li class="li2 ">系统退款</li>');
											}else if(listdata[i].payType == 'lianlian'){
												html.push('<li class="li2 ">连连支付</li>');
											}else if(listdata[i].payType == 'freezType'){
												html.push('<li class="li2 ">冻结支付</li>');
											}else if(listdata[i].payType == 'yinhanghuikuan'){
												html.push('<li class="li2 ">银行汇款</li>');
											}else if(listdata[i].payType == 'alipay'){
												html.push('<li class="li2 ">支付宝</li>');
											}else {
												html.push('<li class="li2 ">其它支付</li>');
											}
											html.push('<li class="li3 color">'+$.formatNum(listdata[i].money)+'</li>');
											html.push('<li class="li4">'+listdata[i].tranNum+'</li>');
											html.push('<li class="li5">成功</li>');
											html.push('</ul>');
									}
								}else{
									html.push('<div class="listno yh">抱歉，该列表暂时没有数据 <a title="">请查看其它类别~</a></div>');
								}
								$('#tmoney').html($.formatNum(tmoney));
								$('#czlist').html(html.join(''));
								if(data.total>1){
									$('.pageNum').css('bottom','-10px');
								}else{
									$('.pageNum').css('bottom','0px');
								}
								//分页
								data.pages>1&&flag?p.pageInit({pageLen:data.pages,isShow:true}):'';
								data.pages==1&&flag?p.pageInit({pageLen:data.pages,isShow:false}):'';
								if(result.flag==2){
									listObj.html('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
								}else if(result.flag==0){
									listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
								}else if(result.flag==-1){
									listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
									if(!en.isIn&&$(".loginDialog").size()==0){
										en.show(that.listShow);
									}
								}
								
							}
						},
						error   : function(r){ 
							d.alert({content:'获取员工信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
						}
					}); 
				}	
				
			};
			record.init();
		});