var base=window.baseurl;
var dataUrl={
		invoiceList:base+"user/invoiceList.do",//用户发票信息列表
		invoiceDetail:base+"ucenter/getInvoiceDetail.do?id="// 发票详情
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
	window.mCENnavEtSite="m-invoiceList";
	h.init();
	en.init();   
	uc.init();
	p.init({afterObj:$("#listShow")});
	p.changeCallback=function(){
		invoiceList.getData(p.curPage);
	};
	uc.selectCallback=function(){
		invoiceList.getData(p.curPage);
	};
	 
	var listObj=$("#listShow"); 
	var invoiceList={
		time:null, 
		init:function(){	
			var that=this;
			that.getData();
		},
		getData:function(page){ 
			if(!page){page=1;} 
			var flag=false;
			if(page<2){flag=true;}
			$.ajax({
				url:dataUrl.invoiceList,
				data:{page:page,pageNum:10,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
						var status={'300':'待发货','302':'已发货'};
						len=len>pageNum?pageNum:len; 
						html.push('<colgroup><col width="18%"></col><col width="12%"></col><col width="10%"></col><col width="15%"></col><col width="18%"></col><col width="14%"></col><col width="13%"></col></colgroup>');
						html.push('<thead><tr><th>发票抬头</th><th>发票金额</th><th>收件人</th><th>电话</th><th>收件地址</th><th>索票时间</th><th>状态</th></tr></thead>');
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							var itemUrl = dataUrl.invoiceDetail+idata.id;
							html.push('<tr>');
							html.push('<td class="text-left pl26"><a href="'+itemUrl+'">'+$.ellipsis(idata.invoiceHead,12,'...')+'</a></td>');
							html.push('<td class="sum">￥'+$.formatNum(idata.invoiceAmount)+'</td>');
							html.push('<td>'+idata.name+'</td>');
							html.push('<td>'+idata.mobile+'</td>');
							html.push('<td class="text-left">'+$.ellipsis(idata.province+idata.city+idata.area+idata.detailAddress,14,'...')+'</td>');
							html.push('<td>'+(new Date(idata.createTime)).pattern("yyyy-MM-dd HH:mm:ss")+'</td>');
							if(idata.state == 300){
								html.push('<td class="noSend">'+status[idata.state]+'</td>');
							}else{
								html.push('<td class="hasSend">'+status[idata.state]+'</td>');
							}
							
							html.push('<tr>');
						}  
						listObj.html(html.join(''));
						
						if(total>1&&flag){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
					}else if(result.flag==2){
						listObj.html('<div class="prompt"><div class="listno yh">您还没有开票记录</div></div>');
					}else if(result.flag==0){
						listObj.html('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						listObj.html('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(invoiceList.getData);
						}
					}
				}
			});
		}
	};
	
	invoiceList.init(); 
	 
});



