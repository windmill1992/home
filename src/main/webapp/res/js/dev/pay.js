require.config({
	baseUrl:window.baseurl+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend", 
		"util"   : "dev/common/util", 
		"dialog" : "dev/common/dialog",
		"head"   : "dev/common/head",
		"entry"  : "dev/common/entry"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry"],function($,d,h,en){
	//window.sitePage="p-"+pageName //如需导航显示当前页则加此设置
	d.init();
	h.init();
	en.init();
	
	//pay page Error
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
	
	//donate count
	var donateCount={
			maxCount:$('input[name="leaveCopies"]').val(),
			countObj:$('input[name="payCount"]'),
			price:$('input[name="permoney"]').val(),
			count:$('input[name="copies"]'),
			payMoney:$('#payMoney'),
			amount:$('input[name="amount"]'),
			init:function(){
				var that=this;
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
								
				$('#pay-tab span').click(function(){
					var i=$('#pay-tab span').index(this);
					$('#pay-tab span').removeClass('on').eq(i).addClass('on');
					$('.pay-bd .pay-con').hide().eq(i).show();
				})
			},
			priceCount:function(flag){
				var obj=this.countObj,
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
				this.payMoney.html(amount+"元");  
			}
	};
	
	donateCount.init();
});