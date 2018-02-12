var base=window.baseurl;
var dataUrl={
	withdrawallist:base+'/ucenter/core/WithdrawDepositRecordData.do'//提款明细
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
	window.uCENnavEtSite="p-capitaldetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#pageNum")});
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
				url:dataUrl.withdrawallist,
				data:{type:time,page:page,pageSize:pageSize,t:new Date().getTime()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
							var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
							len=len>pageNum?pageNum:len; 
							html.push('<ul class="color-bg ul1">');
							html.push('<li class="li1">时间</li>');
							html.push('<li class="li6">提现银行卡</li>');
							html.push('<li class="li3">金额（元）</li>');
							html.push('<li class="li5">状态</li>');
							html.push('</ul>');
							if(data.pages > 0){
							for(var i=0;i<len;i++){
								var idata=datas[i]; 
								html.push('<ul '+((i+1)%2!=0?'class="color-bg"':'')+'>');
								
								html.push('<li class="li1">'+(new Date(idata.createtime)).pattern("yyyy/MM/dd HH:mm")+'</li>');
								html.push('<li class="li6 ">'+idata.bank+idata.bankNumber+'</li>');
								html.push('<li class="li3 color4">'+$.formatNum(idata.money)+'</li>');
								if(idata.state == 302){
									html.push('<li class="li5">成功</li>');
								}else if(idata.state == 301){
									html.push('<li class="li5">取消</li>');
								}else{
									html.push('<li class="li5">处理中</li>');
								}
								html.push('</ul>');	
							} 
							}else{
								html.push('<div class="listno yh">抱歉，该列表暂时没有数据 <a title="">请查看其它类别~</a></div>');
							}
							
							listObj.html(html.join(''));
							//分页
							data.pages>1?p.pageInit({pageLen:data.pages,isShow:true}):p.pageInit({pageLen:data.pages,isShow:false});
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
	detail.init();
});