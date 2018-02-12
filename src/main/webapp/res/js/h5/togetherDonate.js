var base=window.baseurl;
var dataUrl={
	togetherDonate:base+'/project/saveTogetherDonate.do'
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
		"ajaxform"  : "util/ajaxform",
		"pages":"dev/common/pages",
		"pageCommon":"dev/common/pageCommon"
	},
	urlArgs:"v=20150511"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	//window.uCENnavEtSite="p-donationdetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var page=1;
	var togetherDonate={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			$('.sel').click(function(){
		        $(this).closest('td').siblings('td').find('b').removeClass('ed');
		        $(this).children("b").addClass("ed");
		    });
			$("#money2").click(function(){
				$(".sel>b").removeClass("ed");
			});
			$('#btn_submit').click(function(){
				var money=$('.ed').parent().attr('t'),launchExplain=$('#launchExplain').val()
				extensionDonateAmount=$('#extensionDonateAmount').val();
				if(typeof(money)=="undefined"){
					money=$('#money2').val();
				}
				if(money==''||typeof(money)=="undefined"){
					$("#msg").html('<p>请输入目标金额！</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
				if(money<0||money==0){
					$("#msg").html('<p>请输入正确的目标金额！</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
				if(Number(money)>Number(extensionDonateAmount)){
					$("#msg").html('<p>输入目标金额不能大于筹款金额！</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
				if(launchExplain==''||typeof(launchExplain)=='undefined'){
					$("#msg").html('<p>请输入发起说明！</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
				that.submitTogetherDonate(money,launchExplain);
			});
		},
		submitTogetherDonate:function(money,launchExplain){
			var projectId = $('#projectId').val();
			$.ajax({
				url:dataUrl.togetherDonate,
				data:{projectId:projectId,pointMoney:money,launchExplain:launchExplain,t:new Date().getTime()},
				success: function(result){
					if(result.flag==1){//配置成功
						var url="http://www.17xs.org/project/view_share_h5.do?projectId="+projectId+"&shareType=0";
						window.location=url;
					}
					else{//配置失败
						$("#msg").html('<p>'+result.errorMsg+'</p>');
						$("#msg").show();
						setTimeout(function () {
				            $("#msg").hide();
				        }, 2000);
						return false;
					}
				},
				error:function(result){//配置失败
					$("#msg").html('<p>'+result.errorMsg+'</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
			});
		}
	};
	 
	togetherDonate.init();
});