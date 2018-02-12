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
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	WebAppUrl={
			DEPOSITDATA : base+ "ucenter/core/WithdrawDepositRecordData.do",// 提款的数据记录
			ChARGE : base+ "eAlipay/deposit.do",// 网银充值
			BALANCE : base+ "enproject/core/zhuShanCreate.do",//余额付款
			ChARGEWEIXIN:base+"tenpay/deposit.do"//微信支付
	};
	
	var alipay={
		init:function(){
			var that=this;
			$('.charge-menu ul li').click(function(){
				var i=$('.charge-menu ul li').index(this);
				$('.charge-menu ul li').removeClass('active').eq(i).addClass('active');
				$('.charge-wrap .chargeList').hide().eq(i).show();
			});
			
			 $(".select-placeholder").on({
				click : function(){
					$(".change-bank").show();
				}
			});
			//显示银行默认展开
			$(".change-bank").show();
//			$("body").on("click",function(e){
//				var target = $(e.target);
//				if((!(target.closest(".select-placeholder").length) && !(target.closest(".change-bank").length) && $(".change-bank").is(":visible"))){
//					$(".change-bank").hide();
//				}
//			});
			
			$('.change-bank').on({
				click : function(){
					var bankVal = $(this).find("input").attr("id");
					$(".select-placeholder").attr("data-id",bankVal);
					var imgSrc = $(this).find("img").attr("src");
					var imgHtml = "<img src='"+imgSrc+"'/>";
					$(".select-placeholder span").html(imgHtml);
					$(".weak-remind").show();
					$(".link-limit").show();
					$(".change-bank").hide();
					$(".bank-limit").hide();
				}
			},"label");
			$(".link-limit").on({
				click : function(){
					var bankId = $(".select-placeholder").attr("data-id");
					$(".bank-limit[data-id='"+bankId+"']").show();
				}
			});
			
			$('#donateMoney').blur(function(){
				that.joinInvi();
			});
			$('#sumMoney').blur(function(){
				var v=parseInt($(this).val()),needMoney=$('#NeedMoney').val();
				if(v>needMoney){
					$(this).val(needMoney);
				}
				that.joinInvi();
			});
			$("#chargeOnline").on(
				{
					click : function() {
					$("#chargeOnline").attr("disabled","disabled");
					that.chargeOnline(1);
					}
				}
			);
			$('#submitPay').click(function(){
				var Cmoney=$('#stillMoney').html();
				if(!Cmoney){
					d.alert({content:'请输入充值金额！',type:'error'});
					return false;
				}
				that.chargeOnline(2);
			});
			$('#isUsable').change(function(){
				that.alipayOpear();
			});
			
			$('#otherBtn').click(function(){
				var perMoney=$("#donateMoney").val();
				var sumMoney=$("#sumMoney").val();
				if(!perMoney){
					d.alert({content:'请输入企业助捐金额！'});
					return false;
				}
				if(!sumMoney){
					d.alert({content:'请输入助善预算总额！'});
					return false;
				}
				$('#isUsable').attr("checked",false);
				$('#chargePay').show();
				$('#directPay').hide();
				that.alipayOpear();
				
			});
			
			$('#directBtn').click(function(){
				//直接支付
				var p=[];
				var perMoney=$("#donateMoney").val();
				var sumMoney=$("#sumMoney").val();
				var projectId=$("#projectId").val();
				if(!perMoney){
					d.alert({content:'请输入企业助捐金额！'});
					return false;
				}
				if(!sumMoney){
					d.alert({content:'请输入助善预算总额！'});
					return false;
				}
				p.push("perMoney="+perMoney);
				p.push("totalMoney="+sumMoney);
				p.push("pId="+projectId);
				p.push("rnd="+Math.random());
				that.chargeOk();
				//d.alert({content:'正在充值中。。。。'});
				var url =  WebAppUrl.BALANCE+"?"+p.join('&');
				window.open(url);
			});
					
			
			$("body").on("click","#chargeOkBtn",function(){ 
				var di=$(this).parents(".cp2yDialogBox").attr("data");
				 d.close(di);
			}).on("click","#chargeErBtn",function(){ 
				var di=$(this).parents(".cp2yDialogBox").attr("data");
				 d.close(di);
			}).on("click","#chargeReBtn",function(){ 
				var di=$(this).parents(".cp2yDialogBox").attr("data");
				 d.close(di);
			})
			
			$('.charge-alipay .pay-options a').click(function(){
				$(this).addClass('paySelect').siblings().removeClass('paySelect');
			})

		},
		joinInvi:function(){
			var that=this,dM=$('#donateMoney').val(),sM=$('#sumMoney').val(),joinNum=$('#joinNum');
			if(!dM||!sM||dM==0){
				joinNum.html('0');
			}else{
				var jN=parseInt(sM/dM),z=sM%dM;
				if(dM>1){
					if(z==0){
						joinNum.html(jN);
					}else{
						joinNum.html(jN+1);
					}
				}else{
					if(sM/dM===jN){
						joinNum.html(jN);
					}else{
						joinNum.html(jN+1);
					}
				}
			}
			that.alipayOpear();
		},
		alipayOpear:function(){
			var stillMoney=0,isCheck=$('#isUsable').attr("checked"),usMoney=$('#usableMoney').val(),needMoney=$('#NeedMoney').val(),sumMoney=$('#sumMoney').val();
			if(sumMoney){
				if(isCheck){
					var s=$.Subtr(sumMoney,usMoney);
					s>0?stillMoney=s:stillMoney=0;
				}else{
					stillMoney=sumMoney;
				}
			}
			$('#stillMoney').html($.formatNum(stillMoney));
			if(stillMoney>0){
				$('#chargePay').show();
				$('#directPay').hide();
			}else{
				$('#chargePay').hide();
				$('#directPay').show();
			}
		},
		chargeOnline:function(type){
			var that=this,p=[],v=$('.charge-alipay .pay-options a').index($('.paySelect'));
			if(type == 1){
				var bankId = $(".select-placeholder").attr(
				"data-id");
				var bankCode = $("#"+bankId).val();
				p.push("bank="+bankCode);
			}
			var amount=$("#stillMoney").html();
			var sumMoney=$("#sumMoney").val();
			var projectId=$("#projectId").val();
			var perMoney=$("#donateMoney").val();
			p.push("amount="+amount);
			p.push("sumMoney="+sumMoney);
			p.push("perMoney="+perMoney);
			p.push("projectId="+projectId);
			p.push("payType="+5);
			p.push("rnd="+Math.random());
			that.chargeOk();
			var url =  WebAppUrl.ChARGE+"?"+p.join('&');
			if(type==2&&v==1){
				url=WebAppUrl.ChARGEWEIXIN+"?"+p.join('&');
			}
			window.open(url);
		},
		chargeOk:function(){
			var that=this,html=[];
			html.push('<p>支付完成前请不要关闭此窗口。完成支付后请根据你的情况点击下面的按钮：</p>');
			html.push('<div class="quick-form-btn">');
			html.push('<a class="charge-btn charge-btn-green" href="'+base+'enproject/core/zhuShanList.do" id="chargeOkBtn">已完成支付</a><a class="charge-btn charge-btn-gray" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes" id="chargeErBtn">支付遇到问题</a><a class="charge-btn " id="chargeReBtn">返回重新支付</a>');
			html.push('</div>');
			var o ={t:"请您在新打开的网上银行页面上完成支付",c:html.join("")},css={width:'520px',height:'160px'};  
			d.open(o,css,"chargeOk");
		}
		
		
	};
	alipay.init();
});