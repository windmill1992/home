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
		DEPOSIT:base+"/ucenter/coredata/WithdrawDeposit.do"//提款申请
	}
	withdraw={
		init:function(){
			var that=this,balance =$("#userbalance").val();
			 var listlen=$('.bank-list label').length;
			 if(listlen<1){
				 $('#withdraw-submit').removeClass('charge-btn-green').addClass('charge-btn-gray');
			 }
			 
			$(".select-placeholder").on({
				click : function(){
					$(".bank-list").show();
				}
			});
			$("#balance").html($.formatNum(balance));
			$("body").on("click",function(e){
				var target = $(e.target);
				if((!(target.closest(".select-placeholder").length) && !(target.closest(".bank-list").length) && $(".bank-list").is(":visible"))){
					$(".bank-list").hide();
				}
			})
			
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
			
		 $('#money').blur(function(){
				var tmptxt=$(this).val(),name,pName;     
				if(tmptxt != 0.01){
					$(this).val(tmptxt.replace(/\D|^0/g,'')); 
					tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
				}
				if(tmptxt==''){
					tmptxt=1;
				}else if(isNaN(tmptxt)){
					tmptxt=1;
					$('.cp2yDialogBox').length<1?d.alert({content:'亲爱的，您输入的金额格式不正确！',type:'error'}):'';
				}
				if(tmptxt>1000){
					tmptxt=1000;
					$('.cp2yDialogBox').length<1?d.alert({content:'一次可提现金额不能超过1000元',type:'error'}):'';
				}	
				$(this).val(tmptxt);
				return false;
			})
		
			$('.charge-btn-green').click(function(){
				that.submitdata();
			})
		},
		submitdata:function(){
			var that=this,bid=$('.select-placeholder').attr('data-id'),money=$('#money').val(),p=[];
			if(!bid){
				d.alert({content:'请选择银行卡号',type:'error'});
				return false;
			}
			if(!money){
				d.alert({content:'请输入要提现的金额',type:'error'});
				return false;
			}
			p.push('bid='+bid);
			p.push('money='+money);
			$.ajax({
				url     : WebAppUrl.DEPOSIT,
				data    : p.join('&') + '&t='+new Date().getTime(),
				//data    : {name:namev,passWord:pwdv,t:new Date().getTime()}, 
				cache   : false, 
				success : function(result){ 
					if(result.flag=='0'){
						d.alert({content:result.errorMsg,type:'error'});
						return false;
					}else if(result.flag=='-1'){
						en.show(that.submitdata)
					}else{
						//成功
						window.location.href=""//提现页面连接   	
					}
				},
				error   : function(r){ 
					d.alert({content:'实名认证失败，请检查网络是否畅通。<br/>如果确认网络是通的，请联系客服人员帮助解决。',type:'error'});
				}
			}); 
		}
		
	}
	withdraw.init();
});