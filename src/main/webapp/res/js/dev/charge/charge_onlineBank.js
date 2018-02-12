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
				DEPOSITDATA : base+ "/ucenter/core/WithdrawDepositRecordData.do",// 提款的数据记录
				ChARGE : base+ "/eAlipay/deposit.do"// 网银充值
			};

			charge = {
				totle : 0,
				pageCurrent : 1,
				init : function() {
					var that = this;
					$(".bank-limit").hide();
					$(".select-placeholder").on({
						click : function() {
							$(".change-bank").show();
						}
					});
					//显示银行默认展开
					$(".change-bank").show();
//					$("body")
//							.on(
//									"click",
//									function(e) {
//										var target = $(e.target);
//										if ((!(target
//												.closest(".select-placeholder").length)
//												&& !(target
//														.closest(".change-bank").length) && $(
//												".change-bank").is(":visible"))) {
//											$(".change-bank").hide();
//										}
//									});

					$('.change-bank').on({
						click : function() {
							var bankVal = $(this).find("input").attr("id");
							$(".select-placeholder").attr("data-id", bankVal);
							var imgSrc = $(this).find("img").attr("src");
							var imgHtml = "<img src='" + imgSrc + "'/>";
							$(".select-placeholder span").html(imgHtml);
							$(".weak-remind").show();
							$(".link-limit").show();
							$(".change-bank").hide();
							$(".bank-limit").hide();
						}
					}, "label");
					$(".link-limit").on(
							{
								click : function() {
									var bankId = $(".select-placeholder").attr(
											"data-id");
									$(".bank-limit[data-id='" + bankId + "']")
											.show();
								}
							});
							$("#chargeOnline").click(function(){
									that.chargeOnline(1);
							})
					/*$('#amount').blur(function(){
						var tmptxt=$(this).val();    
						if(tmptxt=='' || tmptxt <0.01){
							d.alert({content:'充值金额不能少于0.01',type:'error'});
						}else if(isNaN(tmptxt)){
							d.alert({content:'您输入的格式不正确！',type:'error'});
						}
						return false;
					});*/
					$('#submitPay').click(function(){
						var Cmoney=$('#amount').val();
						if(!Cmoney){
							d.alert({content:'请输入充值金额！',type:'error'});
							return false;
						}
						that.chargeOnline(2);
					});
					
					$("body").on("click","#chargeOkBtn",function(){ 
				//window.location.href="";
				var di=$(this).parents(".cp2yDialogBox").attr("data");
						 d.close(di);
					}).on("click","#chargeErBtn",function(){ 
						//window.location.href="";
						var di=$(this).parents(".cp2yDialogBox").attr("data");
						 d.close(di);
					}).on("click","#chargeReBtn",function(){ 
						var di=$(this).parents(".cp2yDialogBox").attr("data");
						 d.close(di);
					})
				},
				
			chargeOnline:function(type){
				var that=this,p=[];
				var amount=$("#amount").val();
				if(amount=='' || amount <0.01){
					d.alert({content:'充值金额不能少于0.01',type:'error'});
					return false;
				}else if(isNaN(amount)){
					d.alert({content:'您输入的格式不正确！',type:'error'});
					return false;
				}
				if(type == 1){
					var bankId = $(".select-placeholder").attr("data-id");
					if(bankId==''){
						d.alert({content:'请选择银行卡类别',type:'error'});
						return false;
					}
					var bankCode = $("#"+bankId).val();
					p.push("bank="+bankCode);
				}
				if(Number(amount) > 10000000){
					d.alert({content:'充值的金额太大。。'});
					return;
				}
				p.push("amount="+amount);
				p.push("payType="+4);
				p.push("rnd="+Math.random());
				that.chargeOk();
				var url =  WebAppUrl.ChARGE+"?"+p.join('&');
				window.open(url);
			},
			chargeOk:function(){
			var that=this,html=[];
			html.push('<p>充值完成前请不要关闭此窗口。完成充值后请根据你的情况点击下面的按钮：</p>');
			html.push('<div class="quick-form-btn">');
			html.push('<a class="charge-btn charge-btn-green" id="chargeOkBtn"  href="'+base+'ucenter/core/chargeRecord.do">已完成充值</a><a class="charge-btn charge-btn-gray" id="chargeErBtn" target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=2777819027&amp;site=qq&amp;menu=yes">充值遇到问题</a><a class="charge-btn " id="chargeReBtn">返回重新充值</a>');
			html.push('</div>');
			var o ={t:"请您在新打开的网上银行页面上完成充值",c:html.join("")},css={width:'520px',height:'160px'};  
			d.open(o,css,"chargeOk");
			}
		};
			charge.init();
		});