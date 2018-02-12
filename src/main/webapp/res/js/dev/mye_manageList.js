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
		"head"   : "dev/common/headNew",
		"entry"  : "dev/common/entryNew",
		"pages"   :"dev/common/pages",
		"userCenter"  : "dev/common/userCenter" ,
		"ajaxform"  : "util/ajaxform" 
	},
	urlArgs:"v=20150511"
});

define(["extend","head","dialog","entry","userCenter","pages"],function($,h,d,en,uc,p){
	window.uCENnavEtSite="p-staffmanagement";//如需导航显示当前页则加此设置
	h.init();
	en.init();   
	uc.init();
	d.init();
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		manageList.listShow(p.curPage,10);
	};
	WebAppUrl={
		EXPLOYE:base+"/enterprise/coredata/employe.do",// 获取企业员工接口
		VERIFYEMPLOYE:base+"/enterprise/coredata/verifyEmploye.do",//获取验证企业员工
		DOVERIFY:base+"/enterprise/coredata/doVerify.do"//
	}
	manageList={
		totle:0,
		pageCurrent:1,
		vTotle:0,
		vPageCurrent:1,
		init:function(){
			var that=this;
			that.listShow(1,10);
			that.verifyEmploye(1,1);
			$('#mgDialog').click(function(){
				var html=[];
				html.push('<div class="tle yh">为提高企业影响力，善园基金会支持企业结合员工一起行善双向数据统计。</div>');
				html.push('<div class="bntcter"><a href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes" target="_blank" title="" id="mclose1">联系客服</a><a id="mclose" title="">懂了</a></div>');
				
				var o ={t:"什么是善员工",c:html.join("")},css={width:'600px'};  
				d.open(o,css,"mgDialog"); 
			})
			$("body").on("click","#mclose",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("click","#mclose1",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			})
			
			$('#mgPok').click(function(){
				var id=$('#mgPromptText').attr('eId');
				that.doVerify(id,1);
			})
			$('#mgPno').click(function(){
				var id=$('#mgPromptText').attr('eId');
				that.doVerify(id,0);
			})
				
		},
		listShow:function(page,pageNum,t){
			var that=this,oldtotle=t,statData={'200':'未填写实名验证信息','201':'实名验证未审','202':'实名验证审核未通过','203':'实名验证审核通过'},flag=false;
			if(page<2){flag=true};
			$.ajax({
				url     : WebAppUrl.EXPLOYE,
				data:{page:page,pageNum:pageNum,t:new Date().getTime()},
				cache   : false, 
				success : function(result){ 
					if(result.reslut=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.reslut=='-1'){
						en.show(that.listShow)
					}else{
						var data=result.obj,listdata=data.data,html=[],bgcolor;
						that.totle=data.total;that.pageCurrent=data.page,info=data.info;
						if(that.totle<1){
							$('#mglist').html('<div class="listno yh">抱歉，该列表暂时没有数据 <a title="">请查看其它类别~</a></div>');
						}else{
						for(var i=0;i<listdata.length;i++){
								bgcolor=(i%2)?"":'bgcolor1';
								html.push('<ul class="'+bgcolor+'">');
								html.push('<li class="lst-col1">'+listdata[i].name+'</li>');
								html.push('<li class="lst-col2">'+listdata[i].realName+'</li>');
								html.push('<li class="lst-col3 lst-green"><span class="logoicon"></span>'+listdata[i].level+'</li>');
								html.push('</ul>');
								
						}
						if(oldtotle){
							if(oldtotle>data.total){
								p.init({afterObj:$("#mglist")});
								p.curPage>data.total?p.curPage=data.total:'';
								p.pageInit({pageLen:data.total,isShow:true,pageChange:p.curPage}); 
							}
						}
						$('#mglist').html(html.join(''));
						if(data.total>1){
							$('.pageNum').css('bottom','-10px');
						}else{
							$('.pageNum').css('bottom','0px');
						}
						info?info=info:info=0.00;
						if(data.total>0){
							$('.pageNum').html('总计'+data.nums+'项  累计捐赠<span class="color4">'+$.formatNum(info)+'</span>元');
						}
						if(data.total>1&&flag){
							p.pageInit({pageLen:data.total,isShow:true}); 
						}else if(data.total<2){
							p.pageInit({pageLen:data.total,isShow:false}); 
						} 
						
					 }
					}
				},
				error   : function(r){ 
					d.alert({content:'获取员工信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		},
		verifyEmploye:function(page,pageNum){
			var that=this;
			$.ajax({
				url     : WebAppUrl.VERIFYEMPLOYE,
				data:{page:page,pageNum:pageNum,t:new Date().getTime()},
				cache   : false, 
				success : function(result){ 
					if(result.reslut=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.reslut=='-1'){
						en.show(that.verifyEmploye)
					}else{
						var data=result.obj,listdata=data.data,html,bgcolor;
						that.vTotal=data.total;that.vPageCurrent=data.page;
						
						if(that.vTotal<1){
							$('.mgPrompt').hide();
						}else{
						html='验证消息：用户“'+$.ellipsis(listdata[0].name,5)+'”（姓名：'+listdata[0].realName+'）发来企业善员工通过请求，请验证。';
						$('#mgPromptText').html(html);
						$('#mgPromptText').attr('eId',listdata[0].eId)
						}
					}
				},
				error   : function(r){ 
					d.alert({content:'获取员工信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		},
		doVerify:function(id,type){
			var that=this;
			$.ajax({
				url     : WebAppUrl.DOVERIFY,
				data:{eId:id,ok:type,t:new Date().getTime()},
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.doVerify)
					}else{
						if(that.vPageCurrent==that.vTotle){
							$('.mgPrompt').hide();
						}else{
							that.verifyEmploye(that.vPageCurrent+1,1);
							that.listShow(1,10);
						}
					}
				},
				error   : function(r){ 
					d.alert({content:'获取员工信息失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		}
	}
	manageList.init();
});