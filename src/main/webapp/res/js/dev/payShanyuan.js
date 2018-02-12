var base=window.baseurl;
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
			ChARGE:base+"alipay/deposit.do",//企业的募捐
			ChARGEWEIXIN:base+"tenpay/deposit.do",//微信支付
			RASHFRESH:base+"tenpay/result.do",//微信支付请求
			RASHFRESHLIST:base+"batchpay/tenpay/result.do",//微信支付请求
			PAYSUCCESS:base+"project/donateSuccess.do"
	};
	
	var alipay={
		maxCount:$('input[name="leaveCopies"]').val(),
		countObj:$('input[name="payCount"]'),
		price:$('input[name="permoney"]').val(),
		count:$('input[name="copies"]'),
		payMoney:$('#payMoney'),
		amount:$('input[name="amount"]'),
		init:function(){
			var that=this,usertype=$('#userType').val();
			usertype = 'enterpriseUsers';
			that.alipayOpear();
			
			//判断是否是微信页面
			var isWx=$('#weixinAlipay').val();
			if(isWx){
				var countdown = setInterval(that.CountDown, 3000);
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
			//显示银行默认展开
			$(".change-bank").show();
//			$("body").on("click",function(e){
//				var target = $(e.target);
//				if((!(target.closest(".select-placeholder").length) && !(target.closest(".change-bank").length) && $(".change-bank").is(":visible"))){
//					$(".change-bank").hide();
//				}
//			});
			$("body").on("click","#chargeReBtn",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("click","#chargeOkBtn",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			}).on("click","#chargeErBtn",function(){ 
			    var di=$(this).parents(".cp2yDialogBox").attr("data");
				d.close(di);
			})
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
				var v=parseInt($(this).val()),needMoney=parseInt($('#NeedMoney').val());
				if(v>needMoney){
					$(this).val(needMoney);
				}
				that.joinInvi();
			});
			
			$('#isUsable').change(function(){
				that.alipayOpear();
			});
			
			$('#otherBtn').click(function(){
				$('#isUsable').attr("checked",false);
				$('#chargePay').show();
				$('#directPay').hide();
				that.alipayOpear();
				
			});
			
			$('#directBtn').click(function(){
				//直接支付
				that.chargeOnline(2);
			});
			$("#chargeOnline").click(function(){
				var bankId = $(".select-placeholder").attr("data-id");
				if(!bankId){
					d.alert({content:'请选择银行卡类别！',type:'error'});
					return false;
				}
				that.chargeOnline(1);
				
			})
			$('#submitPay').click(function(){
					var Cmoney=$('#stillMoney').html();
					if(!Cmoney){
						d.alert({content:'请输入充值金额！',type:'error'});
						return false;
					}
					that.chargeOnline(2);
				});
			
			var msgVal=$("input[name='msg']").val();
			if(!!msgVal){
				d.alert({content:msgVal});
			}
			
			$("input[name='donateWord']").on("change",function(){
				var val=$(this).val(); 
				if($.chartLen(val)>40){
					d.alert({content:"最多可填写20个字"});
					$(this).blur();
				}
			}); 
			
			$('#btn-reduce').click(function(){
					that.priceCount(-1);
			});
			$('#btn-add').click(function(){
				that.priceCount(1);
			}); 
			$('#num').blur(function(){
				var tmptxt=$(this).val();     
				$(this).val(tmptxt.replace(/\D|^0/g,'')); 
				tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
				if(tmptxt==''||isNaN(tmptxt)){
					tmptxt=1;
					$(this).val(1);
				}
				that.priceCount(0);
			});
			$('.charge-alipay .pay-options a').click(function(){
				$(this).addClass('paySelect').siblings().removeClass('paySelect');
			})
		},
		CountDown:function(){
			var di=$(".cp2yDialogBox").attr("data"),list =$('#list').val();
			d.close(di);
			$.ajax({
					url     : (list == 1)?WebAppUrl.RASHFRESHLIST:WebAppUrl.RASHFRESH,
					data    : {OrderId:$('#tradeNo').val(),t:new Date().getTime()}, 
					cache   : false, 
					async	: false,
					success : function(result){ 
						if(!result.flag){
							$('#warn').html("<em></em>"+result.errorMsg);
						}else{
							var data = result.obj;
							var nickName = $('#nickName').val();
							var tradeNo = $('#tradeNo').val();
							var amount = $('#amount').val();
							var title = $('#title').val();
							var date = $('#date').val();
							var projectId = $('#projectId').val();
							location.href = "http://www.17xs.org/project/donateSuccess.do?nickName="+nickName+"&tradeNo="+tradeNo+"&pName="+title+"&projectId="+projectId+"&amount="+amount;
							/*
							var p=[];
							p.push("nickName="+nickName);
							p.push("tradeNo="+tradeNo);
							p.push("amount="+amount);
							p.push("projectId="+projectId);
							p.push("pName="+pName);
							p.push("time="+date);
							location.href = WebAppUrl.PAYSUCCESS+"?"+p.join('&');
							*/
							/*
							if(data == 'individualUsers'){
								location.href = "http://www.17xs.org/ucenter/core/mygood.do";
							}else if(data == 'enterpriseUsers'){
								location.href = "http://www.17xs.org/ucenter/core/mygood.do";
							}else{
								var payType = $("#weixinAlipay").val();
								if(payType == "garden")
								{
									location.href = "http://www.17xs.org/project/newGardenView.do?projectId="+data;
								}
								else
								{
									location.href = "http://www.17xs.org/project/view.do?projectId="+data;
								}
							}
							*/
						}
					},
					error   : function(r){ 
						d.alert({content:'微信支付失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
					}
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
			var stillMoney=0,isCheck=$('#isUsable').attr("checked"),usMoney=$('#usableMoney').val(),needMoney=$('#NeedMoney').val(),sumMoney=$('#payMoney').html();
			if(sumMoney){
				sumMoney=sumMoney.replace(",","");
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
		priceCount:function(flag){
				var that=this,obj=this.countObj,
					v=obj.formatInt(),
					maxCount=this.maxCount,
					price=this.price; 
				
				v+=flag; 
				if(v>maxCount){d.alert({content:'不能超过总份数',type:'error'});}
				if(v<1){d.alert({content:'不能低于1份',type:'error'});}
				v= v>maxCount?maxCount:(v<1?1:v); 
				$(obj).val(v);
				
				var amount=v*price;
				this.count.val(v);
				this.amount.val(amount);
				this.payMoney.html($.formatNum(amount));  
				that.alipayOpear();
		},
		chargeOnline:function(type){
			var that=this,p=[],v=$('.charge-alipay .pay-options a').index($('.paySelect'));
			if(type == 1){
				var bankId = $(".select-placeholder").attr("data-id");
				var bankCode = $("#"+bankId).val();
				p.push("bank="+bankCode);
			}
			var amount=$("#stillMoney").html();
			amount?amount=amount.replace(",",""):'';
			var sumMoney=$("#payMoney").html();
			sumMoney?sumMoney=sumMoney.replace(",",""):'';
			var projectId=$("#projectId").val();
			var perMoney=$("#permoney").val();
			var donateName=$("#donateName").val();
			var donateWord=$("#donateWord").val();
			var copies = $("#num").val();
			var nickName = $('#nickName').val();
			p.push("amount="+amount);
			p.push("sumMoney="+sumMoney);
			p.push("copies="+copies);
			p.push("projectId="+projectId);
			p.push("payType="+1);//善园
			p.push("donateName="+donateName);//善园
			p.push("donateWord="+donateWord);//善园
			p.push("rnd="+Math.random());
			p.push("extensionPeople="+$('#extensionPeople').val())//推广人id
			that.chargeOk();
			//校验是否已登录
			var url = "" ;
			if(en.isIn){ 
				 url =  WebAppUrl.ChARGE+"?"+p.join('&');
				if(type==2&&v==1){
					url=WebAppUrl.ChARGEWEIXIN+"?"+p.join('&');
				}
			}
			else
			{
				url = base+"visitorAlipay/deposit.do?"+p.join('&');
				if(type==2&&v==1){
					url = base+"visitorAlipay/weixin/deposit.do?"+p.join('&');//微信支付
				}
			}
			window.open(url);
		},
		chargeOk:function(){
			var that=this,html=[],usertype=$('#userType').val();
			html.push('<p>支付完成前请不要关闭此窗口。完成支付后请根据你的情况点击下面的按钮：</p>');
			html.push('<div class="quick-form-btn">');
			if(usertype != 'enterpriseUsers'){
				var projectId=$("#projectId").val();
				if(usertype == 'individualUsers')
				{
					html.push('<a class="charge-btn charge-btn-green" href="'+base+'ucenter/core/mygood.do" id="chargeOkBtn">已完成支付</a>');	
				}
				else
				{
					html.push('<a class="charge-btn charge-btn-green" href="http://www.17xs.org/project/newGardenView.do?projectId='+projectId+'" id="chargeOkBtn">已完成支付</a>');
				}
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