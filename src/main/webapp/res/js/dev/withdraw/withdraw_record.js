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
	window.uCENnavEtSite="p-withdraw";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		DEPOSITDATA:base+"/ucenter/core/WithdrawDepositRecordData.do"//提款的数据记录
	}
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		withdraw.listShow(p.curPage,8);
	};
	uc.selectCallback=function(){
		withdraw.listShow(1,8);
	};
	withdraw={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			 that.listShow(1,8);
		},
		listShow:function(page,pageSize){
			var that=this,statData={'300':'处理中','301':'取消','302':'成功'};
			var time=$(".sel-val").attr('v');
			var flag=false,that=this;
			if(this.time!=time){
				flag=true;
				this.time=time; 
			} 
			if(!page){page=1;} 
			$.ajax({
				url     : WebAppUrl.DEPOSITDATA,
				data:{typeDt:time,page:page,pageSize:pageSize,t:new Date().getTime()},
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.listShow)
					}else{
						var data=result.obj,listdata=data.data,html=[],bgcolor;
						that.totle=data.total;that.pageCurrent=data.page;
						html.push('<ul class="color-bg ul1">');
						html.push('<li class="li1">时间</li>');
						html.push('<li class="li2">提现银行卡</li>');
						html.push('<li class="li3">金额（元）</li>');
						html.push('<li class="li5">状态</li>');
						html.push(' </ul>');
						if(data.pages > 0){
							for(var i=0;i<listdata.length;i++){
									bgcolor=(i%2)?"":'color-bg';
									html.push('<ul class="'+bgcolor+'" title="'+listdata[i].title+'" id="'+listdata[i].id+'">');
									html.push(' <li class="li1">'+(new Date(listdata[i].createtime)).pattern("yyyy/MM/dd hh:mm")+'</li>');
									html.push('<li class="li2">'+listdata[i].bank+listdata[i].bankNumber+'</li>');
									html.push('<li class="li3 colorGreen">'+$.formatNum(listdata[i].money)+'</li>');
									html.push('<li class="li5">'+statData[listdata[i].state]+'</li>');
									html.push('</ul>');	
							}
						}else{
							html.push('<div class="listno yh">抱歉，该列表暂时没有数据 <a title="">请查看其它类别~</a></div>');
						}
						$('#record-list').html(html.join(''));
						if(data.pages>1&&flag){
							p.pageInit({pageLen:data.pages,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
						
						
					}
				},
				error   : function(r){ 
					d.alert({content:'获取员工信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		}
		
	}
	withdraw.init();
});