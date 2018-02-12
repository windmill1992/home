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
		"pages"   :"dev/common/pages",
		"area":"util/area"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","area"],function($,d,h,en,uc,a){
	window.uCENnavEtSite="p-withdraw";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	a.init({selectsId:["province","city","county"],def:[0,0,0]});
	WebAppUrl={
		BOUNDBANKCARD:base+"ucenter/coredata/boundbankcard.do",//绑定银行卡
		CARDBIN:base+"lianlian/coredata/cardbin.do",
		CARDLIST:base+"ucenter/coredata/boundbankcardlist.do",//获取绑定的银行卡
		DELBANK:base+"ucenter/coredata/deletebankcard.do",//解除绑定的银行卡
		DEFAULTBANK:base+"ucenter/coredata/defaultbankcard.do"//默认
	};
	bankType={'2':'借记卡','3':'信用卡'};
	bankImage={'中国工商银行':'01','建设银行':'02','农行银行':'03','招商银行':'04','中国银行':'05',
				'邮政银行':'06','交通银行':'07','广发银行':'08','广大银行':'09','兴业银行':'10','平安银行':'11',
				'中兴银行':'12','浦发银行':'13','民生银行':'14','北京银行':'15','上海银行':'16','杭州银行':'17',
				'宁波银行':'18','富镇银行':'19','北京农业银行':'20'};
	bankTime={'中国工商银行':'2小时内到账','建设银行':'2小时内到账','建设银行':'02','农行银行':'03','招商银行':'04','中国银行':'05',
			'邮政银行':'06','交通银行':'07','广发银行':'08','广大银行':'09','兴业银行':'10','平安银行':'11',
			'中兴银行':'12','浦发银行':'13','民生银行':'14','北京银行':'15','上海银行':'16','杭州银行':'17',
			'宁波银行':'18','富镇银行':'19','北京农业银行':'20'};
	withdraw={
		addBank:0,
		init:function(){
			var that=this;
			that.banklist();
			 $('#managePrim').hover(function(){
				$(this).children('span').show(); 
			 },function(){
				 $(this).children('span').hide();  
			 });
			 $(".select-placeholder").on({
				click : function(){
					$(".bank-list").show();
				}
			});
			$("body").on("click",function(e){
				var target = $(e.target);
				if((!(target.closest(".select-placeholder").length) && !(target.closest(".bank-list").length) && $(".bank-list").is(":visible"))){
					$(".bank-list").hide();
				}
			});
			
			$('.bank-list').on({
            click : function(){
                var bankVal = $(this).find("input").attr("id");
                $(".select-placeholder").attr("data-id",bankVal);
                var imgSrc = $(this).find("img").attr("src"),spanText=$(this).find("span").html();
                var imgHtml = "<img src='"+imgSrc+"'/>";
				var spanHtml='<span>'+spanText+'</span>';
                $(".select-placeholder label").html(imgHtml+spanHtml);
                $(".bank-list").hide();
            }
        	},"label");
			
			$("#cardNo").blur(function() {
				that.addBank=0;
				that.checkCardNumberFun("cardNo",false);
			});
			
			$("#add-bank").click(function(){
					that.submitdata();
			});
			
			$(".bankDefaut").click(function(){
				//默认	
				var bid=$(this).parents('.listone').attr('data');
				that.defaultBank(bid);
			});
			$(".bankdel").on("click",function(){
				//删除	
				var bid=$(this).parents('.listone').attr('data');
				that.delBank(bid);
			});
		},
		checkCardNumberFun:function(cardnumber,type){
			var that=this;
			if(!that.checkBankAccount(cardnumber,type))
			{//银行卡号不合法 
				  return false;
			}
			else
			{ 
			  //如果是失去焦点时间或者校验不通过
				 $.ajax({
					  url: WebAppUrl.CARDBIN,
					  type:"post",
					  context:this,
					  async: false,
					  data: {cardno:$("#"+cardnumber).val(),type:0,rnd: Math.random()},
					  success: function (result){
					     if(result.flag=='0'){
							d.alert({content:result.errorMsg,type:'error'});
							return false;
						 }else if(result.flag=='-1'){
							en.show(that.submitdata);
						 }else{
							//成功
							var bankname=result.obj.bank_name;
							if($('.select-placeholder').attr('data-id')!=bankname){
								d.alert({content:"输入银行卡的开户行与所选的开户行不一致！",type:'error'});
								return false;
							}
							that.addBank=1;
							$("#cardType").val(result.obj.card_type);
						 }
					  }
					});	
				 return true;
			}
		},
		checkBankAccount:function(bankAccountId,type){
			var bankAccount = $("#" + bankAccountId);
			if (bankAccount.val().length < 10 || !bankAccount.val().isNumber()) {        
				return false;
			}
			return true;
		},
		checkCardNo:function(id,cid){
			var n=$(id).val(),cn=$(cid).val();
			if(n!=cn){
				return false;
			}
			return true;
		},
		submitdata:function(){
			var that=this,bankName=$('.select-placeholder').attr('data-id'),province=a.getArea(),bankType=$("#cardType").val(),cardNo=$('#cardNo').val(),p=[];
			if(!bankName){
				d.alert({content:"请选择开户行！",type:'error'});
				return false;
			}
			if(!province){
				d.alert({content:"请选择开户行所在地！",type:'error'});
				return false;
			}
			var isCard=that.checkCardNumberFun("cardNo",false);
			if(!isCard){
				d.alert({content:"银行卡输入错误！",type:'error'});
				return false;
			}
			var cCard=that.checkCardNo('#cardNo','#cardNoC');
			if(!cCard){
				d.alert({content:"两次银行卡号输入不一样！",type:'error'});
				return false;
			}
			if(that.addBank == 0){
				return false;
			}
			p.push('bankType='+bankType);
			p.push('cardNo='+cardNo);
			p.push('bankName='+bankName);
			p.push('province='+province);
			
			$.ajax({
				url     : WebAppUrl.BOUNDBANKCARD,
				data    : p.join('&') + '&t='+new Date().getTime(),
				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.submitdata);
					}else{
						//成功
						window.location.href=window.location.href;  	
					}
				},
				error   : function(r){ 
					d.alert({content:'绑定银行卡失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 	
			
		},
		banklist:function(){
			var that=this;
			 $.ajax({
				  url: WebAppUrl.CARDLIST,
				  type:"post",
				  context:this,
				  async: false,
				  data: {rnd: Math.random()}, 
				  success: function (result){
					 if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					 }else if(result.flag=='-1'){
						en.show(that.banklist);
					 }else{
						//成功
						var data=result.obj.data,html=[];
						for(var i=0;i<data.length;i++){
							var cls='',defaultText="";
							if(data[i].isdefault==0){cls="listSelect"}
							html.push('<div class="listone '+cls+'" data="'+data[i].id+'">');
							html.push('<span><img src="'+base+'res/images/charge/bank-'+bankImage[data[i].bankName]+'.png"></span>');
							html.push('<span class="font14">' + data[i].bankCard+'</span>');
							html.push('<span>'+bankType[data[i].cardType]+'</span>');
							html.push('<span class="gray">2小时内到账</span>');
							html.push('<span class="more font14"><a title="" href="http://www.17xs.org/ucenter/core/viewWithdrawDeposit.do?bid='+data[i].id+'" class="bankcash">提现</a><em></em>');
							if(data[i].isdefault!=0&&data.length > 1){
								//(data[i].isdefault 0:默认 1：不是
								html.push('<a title="" class="bankDefaut">默认</a><em></em>');
							}
							html.push('<a  title="" class="bankdel">删除</a></span>');
							html.push('</div>');
						}
						$('#manage-list').html(html.join(''));
					 }
				  }
			});	
		},
		delBank:function(bid){
			var that=this;
			$.ajax({
					  url: WebAppUrl.DELBANK,
					  type:"post",
					  context:this,
					  async: false,
					  data: {'bid':bid,rnd: Math.random()},
					  success: function (result){
					     if(result.flag=='0'){
							d.alert({content:result.errorMsg,type:'error'});
							return false;
						 }else if(result.flag=='-1'){
							en.show(that.submitdata);
						 }else{
							//成功
							that.banklist();  
						 }
					  }
					});	
		},
		defaultBank:function(bid){
			$.ajax({
			  url: WebAppUrl.DEFAULTBANK,
			  type:"post",
			  context:this,
			  async: false,
			  data: {'bid':bid,rnd: Math.random()},
			  success: function (result){
				 if(result.flag=='0'){
					d.alert({content:result.errorMsg,type:'error'});
					return false;
				 }else if(result.flag=='-1'){
					en.show(that.submitdata);
				 }else{
					//成功
					that.banklist();  
				 }
			  }
			});
		}
		
	};
	withdraw.init();
});