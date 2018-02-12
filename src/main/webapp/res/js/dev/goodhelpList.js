var base=window.baseurl;
var dataUrl={
	items:"/DATA/items.txt",
	showDetailList:base+'ucenter/coredata/callList.do'
};
require.config({
	baseUrl:base+"res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend",
		"dialog" : "dev/common/dialog",
		"util"   : "dev/common/util",
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"userCenter"  : "dev/common/userCenter" ,
		"pages"   :"dev/common/pages",
		"pageCommon"   :"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","pages","pageCommon"],function($,d,h,en,uc,p,pc){
	window.uCENnavEtSite="p-ehelpgoodarea";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
		GOODHELPLIST:base+"enproject/zhuShanList.do",//企业助善列表
		UPDATEGOODHELP:base+"enproject/coredata/updateZhuShan.do",
		STOPGOODHELP:base+"enproject/coredata/stopZhuShan.do"
	}
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		legalize.listShow(p.curPage,10);
	};
	var legalize={
		totle:0,
		pageCurrent:1,
		id:'',
		init:function(){
			var that=this;
			that.listShow(1, 10);
			$('#entExplain').click(function(){
				var html=[];
				html.push('<h2>企业助善</h2>');
				html.push('<p class="text">通过微信、微博等互联网平台，号召网友在平台注册，企业将善款发放给网友，以网友名义进行捐款，企业和注册网友数据双向统计。</p>');
				html.push('<div class="line"></div>');
				html.push('<h2>发起助善</h2>');
				html.push('<p><img src="'+base+'/res/images/Enterprise/explainpic_03.jpg"></p>');
				html.push('<div class="center"><a href="'+base+'/project/index.do" title="" class="btn">发起助善</a></div>');
				var o ={t:"企业助善：",c:html.join("")},css={width:'600px',height:'385px'};  
				d.open(o,css,"entExplain"); 
			})	
		    $("body").on("click",".gdend",function(){ 
		    	var data = $(this).parents('li').attr("data"); 
		    	that.gdend(data);
			}).on("click",".gdModify",function(){ 
				var data = $(this).parents('li').attr("data"); 
				that.gdModify(data);
			}).on('click','#endSubmit',function(){
				 var di=$(this).parents(".cp2yDialogBox").attr("data");
				 var id = $(this).attr("data");
				that.stopZhuShan(di, id);
			}).on('click','#ModifySubmit',function(){
				 var di=$(this).parents(".cp2yDialogBox").attr("data");
				 var id = $(this).attr("data");
				 that.updateZhuShan(di,id);
			}).on('click','.hReason',function(){
				var data=$(this).attr('data').split('|');
				
				if(data){
					that.hReason(data[0],data[1]);
				}
				
			}).on("click","#closeBtn",function(){
				var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("keyup","#gdmoney",function(){
				var v=$(this).val();v=v.replace(/[^0-9.]+/,'');
				 $(this).val(v);
			}).on("blur","#gdmoney",function(){
				var dM=$(this).val(),sMym=$(this).attr('data').split('|'),joinNum=$('#numPerson'),sM=sMym[0],yM=sMym[1];
				if(!dM||!sM||dM==0){
					joinNum.html('0');
				}else{
					var jN=parseInt((sM-yM)/dM),z=(sM-yM)%dM;
					if(dM>1){
						if(z==0){
							joinNum.html(jN);
						}else{
							joinNum.html(jN+1);
						}
					}else{
						if((sM-yM)/dM===jN){
							joinNum.html(jN);
						}else{
							joinNum.html(jN+1);
						}
					}
				}
			}).on("click",".showDetail",function(){ 
				var data=$(this).attr('data').split('|');
				that.showDialog(data[1],data[0]);
				pc.init({afterObj:$("#itemListd"),listId:1});
				pc.changeCallback=function(){
					legalize.showDetail(pc.curPage,that.id);
				};
				
			});

			
				
		},	
		hReason:function(type,data){
			var that=this,html=[],txt='';
			if(type==0){
				txt="结束";
			}else{
				txt="未审核通过";
			}
			html.push('<div class="tit-group tit-Guide yh">');
			html.push('抱歉，您发起的助善活动由于以下原因'+txt+'。');
			html.push('</div>');
			html.push('<div class="con-group">');
			html.push('<textarea id="txtCon">'+data+'</textarea>');
			html.push('</div>');  
			html.push('<div class="tit-group">');
			html.push('</div>');
			html.push('<a  id="closeBtn" class="dlg-btn">关闭</a> ');  
			d.open({t:txt+'原因',c:html.join('')},{},"itemDlga");

		},
		gdend:function(data){
			var temp = data.split(",");
			var html=[];
				html.push('<div class="gddialog yh">');
				html.push('<div class="rlgroup"><label>项目名称</label><div class="controls controlsn"><div class="rlinputText">'+temp[0]+'</div></div></div>');
				html.push('<div class="rlgroup"><label>终止原因</label><div class="controls controlsn"><textarea class="tarea" id="stopReason" placeholder="请输入50字以内的提前终止原因。"></textarea></div>');
				html.push('<div class="rlgroup"><label>当前已捐赠</label><div class="controls controlsn"><div class="rlinputText">'+temp[2]+'元</div></div></div>');
				html.push('<div class="gdinfo">因为您的助善，已有'+temp[4]+'人加入我们，一起行善！</div>')
				html.push('<div class="rlitem"><label></label><div class="rltext"><input type="button" class="rlbtnok yh" id="endSubmit"  data="'+temp[5]+'" value="确认终止"/></div></div>')
				html.push('</div>')
				
				var o ={t:"确认终止助善",c:html.join("")},css={width:'600px'};  
				d.open(o,css,"gdendDialog");
		},
		gdModify:function(data){
			var temp = data.split(",");
			var html=[];
				html.push('<div class="gddialog yh">');
				html.push('<div class="rlgroup"><label>项目名称</label><div class="controls controlsn"><div class="rlinputText">'+temp[0]+'</div></div></div>');
				html.push('<div class="rlgroup"><label>奖励金额/人</label><div class="controls controlsn"><input type="text" class="gdmoney" id="gdmoney" data="'+temp[3]+'|'+temp[2]+'" placeholder="'+temp[1]+'">元</div>');
				html.push('<div class="rlgroup"><label>奖励总额/预算总额</label><div class="controls controlsn"><div class="rlinputText">'+temp[2]+'元/'+temp[3]+'元</div></div></div>');
				html.push('<div class="gdinfo">因为您的助善，将会有<span id="numPerson">0</span>人加入我们，一起行善！</div>')
				html.push('<div class="rlitem"><label></label><div class="rltext"><input type="button" class="rlbtnok yh" id="ModifySubmit" data="'+temp[5]+'" value="确定"/></div></div>')
				html.push('</div>')
				
				var o ={t:"企业助善修改",c:html.join("")},css={width:'600px'};  
				d.open(o,css,"gdendDialog gdModifyD");
		},
		listShow:function(page,pageNum){
			var that=this,statData={'200':'未填写实名验证信息','201':'实名验证未审','202':'实名验证审核未通过','203':'实名验证审核通过'},flag=false;
			if(page<2){flag=true};
			$.ajax({
				url     : WebAppUrl.GOODHELPLIST,
				data:{page:page,pageNum:pageNum,t:new Date().getTime()},
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.listShow);
					}else{
						var data=result.obj,listdata=data.data,html=[],bgcolor;
						that.totle=data.total;that.pageCurrent=data.page,info=data.info;
						if(data.total>0){
							for(var i=0;i<listdata.length;i++){
									bgcolor=(i%2)?"":'bgcolor1';
									html.push('<ul class="'+bgcolor+'" tle="'+listdata[i].pName+'" id="'+listdata[i].aid+'">');
									html.push(' <li class="lst-col1"><a href="'+base+"project/view.do?projectId="+listdata[i].pId+'" target="_blank" title="">'+listdata[i].pName+'</a></li>');
									html.push('<li class="lst-col2">￥'+$.formatNum(listdata[i].perMoney)+'</li>');
									html.push('<li class="lst-col3">'+listdata[i].peopleNums+'</li>');
									html.push('<li class="lst-col4"><div class="donation-progress"><span class="progress-bar" style="width:'+listdata[i].process+'%"></span> <span class="progress-text"><span class="l">'+listdata[i].process+'%</span><span class="r">￥'+$.formatNum(listdata[i].payMoney)+'/￥'+$.formatNum(listdata[i].totalMoney)+'</span></span></div></li>');
									html.push('<li class="lst-col5 lst-br">'+(new Date(listdata[i].cTime)).pattern("yyyy/MM/dd<br>HH:mm")+'</li>');
									if(listdata[i].state==201){
										html.push('<li class="lst-col6 lst-opear" data="'+listdata[i].pName+","+listdata[i].perMoney+","+listdata[i].payMoney+","+listdata[i].totalMoney+","+listdata[i].peopleNums+","+listdata[i].aid+'"><span>审核中</span><a class="gdend" title="">终止助善</a></li>');
									}else if(listdata[i].state==202){
										html.push('<li class="lst-col6 lst-opear"><span>审核失败</span><a  title="" class="hReason" data="'+1+"|"+listdata[i].stopReason+'">原因</a></li>');
									}else if(listdata[i].state==203){
										html.push('<li class="lst-col6 lst-opear" data="'+listdata[i].pName+","+listdata[i].perMoney+","+listdata[i].payMoney+","+listdata[i].totalMoney+","+listdata[i].peopleNums+","+listdata[i].aid+'"><span class="gdModify">修改</span><a class="gdend" title="">终止助善</a></li>');
									}else if(listdata[i].state==207){
										html.push('<li class="lst-col6 lst-opear"><span>结束</span><a class="hReason" data="0|'+listdata[i].stopReason+'">原因</a></li>');
									}else{
										html.push('<li class="lst-col6 lst-opear"><span>完成</span></li>');
									}
									html.push('</ul>');
									
							}
							info?info=info:info=0.00;
						}else{
							html.push('<div class="listno yh">抱歉，该列表暂时没有数据 <a title="">请查看其它类别~</a></div>');
						}
						if(data.total>1){
							$('.pageNum').css('bottom','-10px');
						}else{
							$('.pageNum').css('bottom','0px');
						}
						if(data.total>0){
							$('.pageNum').html('总计'+data.nums+'项  累计助善<span class="color4">'+$.formatNum(info)+'</span>元');
						}
						$('#ghlist').html(html.join(''));
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
						
						
					}
				},
				error   : function(r){ 
					d.alert({content:'获取助善列表失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		},
		showDialog:function(title,id){
			var that=this,html=[];	
			html.push('<div class="diabox-details_funds ">');
			html.push('<div class="details_funds-list" id="itemListd">');
			html.push('</div>')	;
			var o ={t:title+" 助善捐款名单",c:html.join("")},css={width:'600px',height:'500px'};  
			d.open(o,css,"dialogContentNone");
			that.showDetail(1,id);
		},	
		showDetail:function(page,id){
			var that=this,html=[],flag=false;
			page==1?flag=true:'';
			id?that.id=id:'';
			html.push('<ul class="ul1">');
			html.push('<li class="">项目标题</li>');
			html.push('<li class="">助善时间</li>');
			html.push('<li class="">用户名</li>');
			html.push('<li class="li1">助善金</li>');
			html.push('</ul>');
			$.ajax({
				url:dataUrl.showDetailList,
				data:{page:page,projectId:id,pageNum:9,t:new Date()},
				success: function(result){
					result = eval(result);  
					if(result.flag==1){
						var data=result.obj,total=result.total,pageNum=data.pageSize,datas=data,len=datas.resultData.length;
						//len=len>pageNum?pageNum:len; 
						var itemDtil=dataUrl.itemDetail+'?projectId=';
						for(var i=0;i<len;i++){
							var idata=datas.resultData[i]; 
							html.push('<ul '+((i+1)%2==0?'class="bgcolor1 listone"':'listone')+'>');
							html.push('<li class=" ">项目标题标题</li>');
							html.push('<li class="">'+(new Date(idata.donatTime)).pattern("yyyy/MM/dd HH:mm")+'</li>');
							html.push('<li class=" ">'+idata.userName+'</li>');
							html.push('<li class="li1 ">'+idata.donatAmount+'</li>');
							html.push('</ul>');
						}  
						$('#itemListd').html(html.join(''));
						data.pages>1&&flag?pc.pageInit({pageLen:data.pages,isShow:true,listId:1}):'';
						
					}else if(result.flag==2){
						html.push('<div class="prompt"><div class="listno yh">抱歉， 该列表暂时没有募捐项目 <a  title="">请查看其它类别~</a></div></div>');
						$('#itemListd').html(html.join(''));
					}else if(result.flag==0){
						html.push('<div class="prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						$('#itemListd').html(html.join(''));
					}else if(result.flag==-1){
						html.push('<div class="item-prompt"><div class="wrong"><a id="itemRefrash" class="link-refrash" href="javascript:void(0);"></a><a class="link-contact" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes"></a></div></div>');
						$('#itemListd').html(html.join(''));
						if(!en.isIn&&$(".loginDialog").size()==0){
							en.show(detail.showDetail);
						}
					}
					
					
				}
			});
			
		},
		updateZhuShan : function(di,id){
			var perMoney = $('#gdmoney').val();
			var that = this;
			if(!perMoney){
				d.alert({content:'奖励金额/人不能为空。',type:'error'});
				return ;
			}
			$.ajax({
				url     : WebAppUrl.UPDATEGOODHELP,
				data:{aid:id,perMoney:perMoney,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						 that.listShow(that.pageCurrent, 10);
						 d.close(di);
					}
				},
				error   : function(r){ 
					d.alert({content:'修改助善信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		},
        stopZhuShan : function(di,id){
        	var stopReason = $('#stopReason').val();
        	var that = this;
        	if(!stopReason){
				d.alert({content:'终止原因不能为空。',type:'error'});
				return ;
			}
        	$.ajax({
				url     : WebAppUrl.STOPGOODHELP,
				data:{aid:id,reason:stopReason,t:new Date().getTime()},
				cache   : false, 
				success : function(result){
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show();
					}else{
						 that.listShow(that.pageCurrent, 10);
						 d.close(di);
					}
				},
				error   : function(r){ 
					d.alert({content:'停止助善项目失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			});
		}
		
	}
	
	legalize.init();
});