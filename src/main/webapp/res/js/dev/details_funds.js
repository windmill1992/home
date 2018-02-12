var base=window.baseurl;
var dataUrl={
	fundslist:base+'enterprise/coredata/entDetailsFundsData.do',//资金明细
	showDetailList:"/DATA/funts.json"
	//showDetailList:base+'enterprise/coredata/donationlist.do'//捐款明细列表
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
			$("body").on("click",".listone",function(){ 
				var next=$(this).next('listTwo');
				 if(next.is(":hidden")){
					next.show();
				}else{
					next.hide();
				}
			}).on("click",".showDetail",function(){ 
				that.showDialog();
				p.init({afterObj:$("#itemListd")});
				p.changeCallback=function(){
					detail.showDetail(p.curPage);
				};
				uc.selectCallback=function(){
					detail.showDetail(p.curPage);
				};
			}).on("click",".detailShow",function(){ 
				var listTwo=$(this).parents('ul').next('.listTwo');
				if(listTwo.is(":hidden")){
					listTwo.show();
				}else{
					listTwo.hide();
				}
				
			})
			
		},
		showDialog:function(){
			var that=this,html=[];	
			html.push('<div class="diabox-details_funds ">');
			html.push('<div class="details_funds-list" id="itemListd">');
			html.push('</div>')	;
			var o ={t:"我要上学 助善捐款名单",c:html.join("")},css={width:'600px',height:'500px'};  
			d.open(o,css,"dialogContentNone");
			that.showDetail(1);
		},	
		showDetail:function(page){
			var that=this,html=[];	
			html.push('<ul class="ul1">');
			html.push('<li class="">助善时间</li>');
			html.push('<li class="">用户名</li>');
			html.push('<li class="li1">助善金</li>');
			html.push('</ul>');
			$.ajax({
				url:dataUrl.showDetailList,
				data:{page:1,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=result.total,pageNum=result.pageNum,datas=data,len=datas.length;
						len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							html.push('<ul '+((i+1)%2==0?'class="bgcolor1 listone"':'listone')+'>');
							
							html.push('<li class="">2015/06/18  12:53</li>');
							html.push('<li class=" ">募捐</li>');
							html.push('<li class="li1 ">-100</li>');
							html.push('</ul>');
						}  
						if(total>1){
							p.pageInit({pageLen:total,isShow:true}); 
						}  
						
					}else if(result.flag==2){
						html.push('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
					}else if(result.flag==0){
						html.push('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
					}else if(result.flag==-1){
						html.push('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(detail.showDetail);
						}
					}
				$('#itemListd').html(html.join(''));	
					
				}
			});
			
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
				url:dataUrl.fundslist,
				data:{type:time,page:page,pageNum:pageSize,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj.baseLsit,adtlist=result.obj.adtList,total=data.pages,pageNum=data.pageSize,datas=data.resultData,len=datas.length,html=[];
						len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						html.push('<ul class="color-bg ul1">');
						html.push('<li class="li1">时间</li>');
						html.push('<li class="li2">类型</li>');
						html.push('<li class="li3">入账/出账（元）</li>');
						html.push('<li class="li4">余额（元）</li>');
						html.push('<li class="li5">详情</li>');
						html.push('</ul>');
						for(var i=0;i<len;i++){
							var idata=datas[i]; 
							var addata=adtlist[i];
							html.push('<ul '+((i+1)%2!=0?'class="bgcolor1 listone"':'class="listone"')+'>');
							html.push('<li class="li1">'+(new Date(idata.createTime)).pattern("yyyy/MM/dd HH:mm")+'</li>');
							if(idata.inType == 0){
								html.push('<li class="li2 ">募捐</li>');
								html.push('<li class="li3 color4">-'+$.formatNum(idata.money)+'</li>');
							}else if(idata.inType == 1 || idata.inType == 8){
								html.push('<li class="li2 ">充值</li>');
								html.push('<li class="li3 color3">+'+$.formatNum(idata.money)+'</li>');
							}else if(idata.inType == 2){
								html.push('<li class="li2 ">助善</li>');
								html.push('<li class="li3 color4">-'+$.formatNum(idata.money)+'</li>');
							}else if(idata.inType == 3){
								html.push('<li class="li2 ">提款</li>');
								html.push('<li class="li3 color4">-'+$.formatNum(idata.money)+'</li>');
							}else if(idata.inType == 4 || idata.inType == 5 || idata.inType == 6 || idata.inType == 7){
								html.push('<li class="li2 ">退款</li>');
								html.push('<li class="li3 color3">+'+$.formatNum(idata.money)+'</li>');
							}else{
								html.push('<li class="li2 ">未知</li>');
								html.push('<li class="li3 color4">'+$.formatNum(idata.money)+'</li>');
							}
							html.push('<li class="li4">'+$.formatNum(idata.balance)+'</li>');
							html.push('<li class="li5"><a class="detailShow">查看</a></li>');
							html.push('</ul>');
							html.push('<ul '+((i+1)%2==0?'class="color-bg listTwo"':'class="listTwo"')+'>');
							if(addata !=null){
								if(idata.inType == 0){//募捐
									if(addata.field == 'garden'){
										html.push('<li class="lidetail">详情：项目<span><a  class="color4" href="http://www.17xs.org/project/newGardenView.do?projectId='+addata.projectId+'">['+addata.projectTitle+']</a></span>捐赠</li>');
									}else if(addata.field == 'good'){
										html.push('<li class="lidetail">详情：项目<span><a  class="color4" href="http://sy.17xs.org/project/syview.do?projectId='+addata.projectId+'">['+addata.projectTitle+']</a></span>捐赠</li>');
									}else{
										html.push('<li class="lidetail">详情：项目<span><a  class="color4" href="http://www.17xs.org/project/view.do?projectId='+addata.projectId+'">['+addata.projectTitle+']</a></span>捐赠</li>');
									}
								}else if(idata.inType == 1){//充值
									if(idata.payType == 'bankpay'){
										if(idata.payNum == null || idata.payNum == ""){
											html.push('<li class="lidetail">详情：网上银行，流水号：'+idata.tranNum+'</li>');
										}else{
											html.push('<li class="lidetail">详情：网上银行：'+idata.payNum+'，流水号：'+idata.tranNum+'</li>');
										}
									}else if(idata.payType == 'yinhanghuikuan'){
										if(idata.payNum == null || idata.payNum == ""){
											html.push('<li class="lidetail">详情：银行汇款，流水号：'+idata.tranNum+'</li>');
										}else{
											html.push('<li class="lidetail">详情：银行汇款：'+idata.payNum+'，流水号：'+idata.tranNum+'</li>');
										}
									}else if(idata.payType == 'back'){
										html.push('<li class="lidetail">详情：系统退款，流水号：'+idata.tranNum+'</li>');
									}else if(idata.payType == 'lianlian'){
										html.push('<li class="lidetail">详情：连连支付，流水号：'+idata.tranNum+'</li>');
									}else if(idata.payType == 'freezType'){
										html.push('<li class="lidetail">详情：冻结支付，流水号：'+idata.tranNum+'</li>');
									}else if(idata.payType == 'alipay'){
										html.push('<li class="lidetail">详情：支付宝支付，流水号：'+idata.tranNum+'</li>');
									}else{
										html.push('<li class="lidetail">详情：其它支付，流水号：'+idata.tranNum+'</li>');
									}
								}else if(idata.inType == 2){//助善
									if(addata.field == 'garden'){
										html.push('<li class="lidetail">详情：企业助善项目<a href="http://www.17xs.org/project/newGardenView.do?projectId='+addata.projectId+'"><span class="color4">['+addata.projectTitle+']</span></a></li>');
									}else if(addata.field == 'good'){
										html.push('<li class="lidetail">详情：企业助善项目<a href="http://sy.17xs.org/project/syview.do?projectId='+addata.projectId+'"><span class="color4">['+addata.projectTitle+']</span></a></li>');
									}else{
										html.push('<li class="lidetail">详情：企业助善项目<a href="http://www.17xs.org/project/view.do?projectId='+addata.projectId+'"><span class="color4">['+addata.projectTitle+']</span></a></li>');
									}
								}else if(idata.inType == 3){//提款
									html.push('<li class="lidetail">详情：提款至xxx银行5128383388***39393</li>');
								}else if(idata.inType == 4){//终止退款(客服)
									html.push('<li class="lidetail">详情：企业助善项目<a href="http://www.17xs.org/project/view.do?projectId='+addata.projectId+'"><span class="color4">['+addata.projectTitle+']</span></a>提前终止，企业助善剩余资金退回  </li>');
								}else if(idata.inType == 5){// 审核不通过退款
									html.push('<li class="lidetail">详情：企业助善审核未通过，企业助善剩余资金退回.</li>');
								}else if(idata.inType == 6){// 募捐已完成退款
									html.push('<li class="lidetail">详情：项目<a href="http://www.17xs.org/project/view.do?projectId='+addata.projectId+'"><span class="color4">['+addata.projectTitle+']</span></a>善款已募捐完成，企业助善剩余资金退回  </li>');
								}else if(idata.inType == 7){// 募捐项目提前终止(善管家)
									html.push('<li class="lidetail">详情：项目<a href="http://www.17xs.org/project/view.do?projectId='+addata.projectId+'"><span class="color4">['+addata.projectTitle+']</span></a>募捐提前终止，企业助善剩余资金退回  </li>');
								}else{//未知
									html.push('<li class="lidetail">未知的详情</li>');
								}
							}
							html.push('</ul>');
						}  
						listObj.html(html.join(''));
						//分页
						total>1&&flag?p.pageInit({pageLen:total,isShow:true}):'';
						total==1?p.pageInit({pageLen:total,isShow:false}):'';
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