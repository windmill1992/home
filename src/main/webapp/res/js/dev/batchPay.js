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
		ChARGELIST:base+"batchpay/alipay/deposit.do",//批量捐款支付宝支付
		ChARGEWEIXIN:base+"batchpay/tenpay/deposit.do"//批量捐款微信支付
	};
	var Pcancel=[];
	
	var alipay={
		init:function(){
			var that=this,usertype=$('#userType').val();
			usertype = 'enterpriseUsers';
			$('#userType').val(usertype);
			// 校验选中金额大小
			var arr= location.search.split("=")[1].split(",");
			var total = arr.length;
			$(function(){
				var v,i=$('#ulMoneyList li').index(this),needMoney=Number($('#NeedMoney').val()),usableMoney=Number($('#usableMoney').val());
				if(needMoney<20){
					$('#ulMoneyList li').removeClass('selected').eq(4).addClass('selected');
					$(".amount").html(0);
				}else{
					v=Number($('#ulMoneyList li').eq(0).attr('v'))*total;
					$('#ulMoneyList li').removeClass('selected').eq(0).addClass('selected');
					$(".amount").eq(0).html(20);
					$(".amount").eq(1).html(20*total);
					if(v<usableMoney){
						$('#stillMoney').html(0);
					}else{
						var stillMoney=$.Subtr(v,usableMoney);
						$('#stillMoney').html(stillMoney);
						var usertype=$('#userType').val();
						if(usertype == 'enterpriseUsers'){
							$('#chargePay').show();
							$('#directPay').hide();
						}
						
					}
					
				}
			});
			
			var sM=$('#stillMoney').html();
			
			if(sM>0){
					$('#chargePay').show();
					$('#directPay').hide();
				}else{
					$('#isUsable').attr("checked",true);
					$('#directPay').show();
					$('#chargePay').hide();
					
				}
			
			if(usertype != 'enterpriseUsers'){
				$('#directPay').hide();
				$('.pay-hdm').hide();
				$('#chargePay').show();
			}
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
			$(".amountguanbi").click(function(){
				if(total <=2){
					d.alert({content:'批量捐项目不能少于2个！',type:'error'});
					return false;
				}
				$(this).closest('.amountlanmu').hide();
				
				var i=	$('.amountguanbi').index(this);
				var pid =Number($('.amountguanbi').eq(i).attr('v'));
				Pcancel.push(pid);
				total -= 1;
				$("#ulMoneyList .selected").trigger("click");

			});
			//显示银行默认展开
			$(".change-bank").show();
			$("body").on("click","#chargeReBtn",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("click","#chargeOkBtn",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("click","#chargeErBtn",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			});
			
			
	
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
			$('#txtOtherMoney').blur(function(){
				var v=$.formatNum1($(this).val()),needMoney=Number($('#NeedMoney').val());
				if(v>needMoney){
					$(this).val(needMoney);
				}else{
					$(this).val(v);
				}
				if(usertype == 'enterpriseUsers'){
					that.joinInvi();
				}
			});
			
			$('#isUsable').change(function(){
				that.alipayOpear();
			});
			
			$('#otherBtn').click(function(){
				var sM=$('#sumMoney').val();
				if(!sM){
					$('#chargePay').show();
					$('#directPay').hide();
				}else{
					$('#isUsable').attr("checked",false);
					$('#chargePay').show();
					$('#directPay').hide();
					that.alipayOpear();
				}
				
			});
			/*ulMoneyList*/
			$('#ulMoneyList li').click(function(){
				var i=$('#ulMoneyList li').index(this),v=Number($(this).attr('v')),needMoney=Number($('#NeedMoney').val()),isCheck=$('#isUsable').attr("checked");
				var usableMoney = 0 ;
				if(v>needMoney){
					return ;
				}
				if(isCheck){
					usableMoney=Number($('#usableMoney').val());
				}
				if(v == 0){
					v = Number($('#txtOtherMoney').val());
				}
				var stillMoney = $.Subtr(v,usableMoney);
				if(v<=needMoney){
					var totalamount = (total*v).toFixed(2);
					$('#ulMoneyList li').removeClass('selected').eq(i).addClass('selected');
					$(".amount").eq(0).html(v);
					$(".amount").eq(1).html(totalamount);
					var isCheck=$('#isUsable').is(':checked');
					if(isCheck){
						var pMoney = (parseFloat(totalamount) - parseFloat($('#usableMoney').val())).toFixed(2)
						if(pMoney > 0){
							$('#stillMoney').html(pMoney);
						}else{
							$('#stillMoney').html(0);
						}
					}
				}else{
					$('#stillMoney').html(stillMoney);
				}
			});
			$('#txtOtherMoney').blur(function(){
				$('#ulMoneyList li').click();
			});
			/*ulMoneyList*/
			$('#directBtn').click(function(){
				//直接支付
				var Cmoney=Number($('#totalamount').html());
				if(!Cmoney||Cmoney < 0){
					d.alert({content:'请输入认捐金额！',type:'error'});
					return false;
				}
				that.chargeListOnline(2);
			});
			//批量捐款
			$('#submitPayList').click(function(){

				var Cmoney=Number($('#totalamount').html());
				if(!Cmoney||Cmoney < 0){
					d.alert({content:'请输入认捐金额！',type:'error'});
					return false;
				}
				that.chargeListOnline(2);
			});
			$('.charge-alipay .pay-options a').click(function(){
				$(this).addClass('paySelect').siblings().removeClass('paySelect');
			});
		},
		joinInvi:function(){
			var that=this,dM=$('#donateMoney').val(),sM=$('#sumMoney').val(),joinNum=$('#joinNum');
			if(!dM||!sM||dM==0){
				joinNum.html('0');
			}else{
				var jN=parseInt(sM/dM),z=sM%dM;
				if(z==0){
					joinNum.html(jN);
				}else{
					joinNum.html(jN+1);
				}
			}
			that.alipayOpear();
		},
		alipayOpear:function(){
			var stillMoney=0,isCheck=$('#isUsable').attr("checked"),usMoney=$('#usableMoney').val(),needMoney=$('#NeedMoney').val(),sumMoney=$('#totalamount').text();
			if(sumMoney == 0){
				sumMoney = $('#txtOtherMoney').val();
			}
			if(sumMoney){
				if(isCheck){
					var s=$.Subtr(sumMoney,usMoney);
					s>0?stillMoney=s:stillMoney=0;
				}else{
					stillMoney=sumMoney;
				}
			}else{
				d.alert({content:'请输入认捐金额！',type:'error'});
				return false;
			}
			$('#stillMoney').html($.formatNum1(stillMoney));
			if(stillMoney>0){
				$('#chargePay').show();
				$('#directPay').hide();
			}else{
				$('#chargePay').hide();
				$('#directPay').show();
			}
		},
		//列表批量捐款
		chargeListOnline:function(type){
			var that=this,p=[],v=$('.charge-alipay .pay-options a').index($('.paySelect'));
			var sumMoney =$('#totalamount').html();
			var amount=Number($('#totalamount').html());
			var pList=$("#pList").val();
			var isCheck=$('#isUsable').is(':checked');
			if(isCheck){
				var usableMoney=Number($('#usableMoney').val());
				amount=$.Subtr(sumMoney,usableMoney);
				if(amount < 0){
					amount =0;
				}
			}
			p.push("amount="+amount);
			p.push("sumMoney="+sumMoney);
			p.push("pList="+pList.replace("]","").replace("[",""));
			p.push("cList="+Pcancel);
			p.push("payType="+5);//支付宝充值
			p.push("rnd="+Math.random());
			p.push("extensionPeople="+$('#extensionPeople').val());
			that.chargeOk();
			var url;
			if(v == 1){
				url =  WebAppUrl.ChARGEWEIXIN+"?"+p.join('&');
			}else{
				url =  WebAppUrl.ChARGELIST+"?"+p.join('&');
			}
			window.open(url);
		},
		chargeOk:function(){
			var that=this,html=[],usertype=$('#userType').val();
			html.push('<p>支付完成前请不要关闭此窗口。完成支付后请根据你的情况点击下面的按钮：</p>');
			html.push('<div class="quick-form-btn">');
			if(usertype != 'enterpriseUsers'){
				html.push('<a class="charge-btn charge-btn-green" href="'+base+'ucenter/core/mygood.do" id="chargeOkBtn">已完成支付</a>');
			}else{
				html.push('<a class="charge-btn charge-btn-green" href="'+base+'ucenter/core/mygood.do" id="chargeOkBtn">已完成支付</a>');
			}
			html.push('<a class="charge-btn charge-btn-gray" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes" id="chargeErBtn">支付遇到问题</a><a class="charge-btn " id="chargeReBtn">返回重新支付</a>');
			html.push('</div>');
			var o ={t:"请您在新打开的网上银行页面上完成支付",c:html.join("")},css={width:'520px',height:'160px'};  
			d.open(o,css,"chargeOk");
		}
	}
	alipay.init();
});