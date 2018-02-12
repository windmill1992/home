var base=window.baseurl;
var dataUrl={
	addUserProjectPrize:base+'user/addUserProjectPrize.do'//项目众筹奖励记录入库
};
require.config({
	baseUrl:base+"/res/js",
	paths:{
		"jquery" : ["jquery-1.8.2.min"], 
		"extend" : "dev/common/extend"
	},
	urlArgs:"v=20170511"
});
define(["extend"],function($){
	
	var reg={
		init:function(){
			var that=this,name = $("#name").text(),mobile = $("#mobile").text(),address = $("#address").text();
			if(name == ''&&mobile == ''&&address == ''){
				$("#prompt").show();
			}
			$(".cue_fr").click(function(){
				$("#prompt").hide();
			})
			$(".order_foot").click(function(){
				/*if(name == ''&&mobile == ''&&address == ''){
					$("#prompt").show();
					return false;
				}*/
				that.addUserProjectPrize(); 
			});
			$(".cue_fl").click(function(){
				var projectId = $("#projectId").val(),giftId = $("#giftId").val();
				window.location.href='http://www.17xs.org/user/toAddress.do?type='+projectId+'_'+giftId;
			});
		},
		addUserProjectPrize:function(){
			var that = this,projectId = $("#projectId").val(),giftId = $("#giftId").val(),
				addressId = $("#addressId").val(),money = $("#money").val(),extensionPeople = $("#extensionPeople").val();
			if(addressId == null || addressId == '') addressId = -1; 
			$.ajax({
				url : dataUrl.addUserProjectPrize,
				data : {projectId:projectId,giftId:giftId,addressId:addressId},
				success : function(result){
					if(result.flag == 1){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&crowdFunding="+result.obj;
					}else{
						alert(result.errorMsg);
					}
				}
			});
		},
	};
	
	reg.init();
});