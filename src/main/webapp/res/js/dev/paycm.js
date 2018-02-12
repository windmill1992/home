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
	
	$('#pay-tab span').click(function(){
		var i=$('#pay-tab span').index(this);
		$('#pay-tab span').removeClass('on').eq(i).addClass('on');
		$('.pay-bd .pay-con').hide().eq(i).show();
	})
	
	$('#Cmoney').blur(function(){
		var tmptxt=$(this).val(),Smoney=$('#Smoney').attr('data'),name,pName;     
		if(tmptxt != 0.01){
			$(this).val(tmptxt.replace(/\D|^0/g,'')); 
			tmptxt=parseInt(tmptxt.replace(/\D|^0/g,''));
		}
		name=$.cookie.get("sjj_username")
		pName=$('#pName').val();
		if(tmptxt==''){
			tmptxt=1;
			$('.cp2yDialogBox').length<1?d.alert({content:'亲爱的'+name+'，爱心请再多一点点',type:'error'}):'';
		}else if(isNaN(tmptxt)){
			tmptxt=1;
			$('.cp2yDialogBox').length<1?d.alert({content:'亲爱的，您输入的格式不正确！',type:'error'}):'';
		}
		if(tmptxt>Smoney){
			tmptxt=Smoney;
			$('.cp2yDialogBox').length<1?d.alert({content:'亲爱的'+name+'，您对"'+pName+'"的爱已经溢出来了~~',type:'error'}):'';
		}	
		$(this).val(tmptxt);
		return false;
	})
	
	$('#submitPay').click(function(){
		var Cmoney=$('#Cmoney').val();
		if(!Cmoney){
			d.alert({content:'请输入认捐金额！',type:'error'})
			return false;
		}
	})

	
	
});