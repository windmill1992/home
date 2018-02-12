var base=window.baseurl;
var dataUrl={
	helpsubmit:base+'/project/addfeedback.do',
	helpphotoDel:base+'file/image/delete.do',
	hostList:base+'/project/careProjectList.do',//反馈
	toCommon:base+'/project/addfeedback.do',
	FeedbackList:base+'/project/leaveWordList.do',
	addleaveWord:base+'/project/addleaveWord.do',
	donationlist:base+'project/gardendonationlist.do'
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
	window.uCENnavEtSite="p-donationdetail";//如需导航显示当前页则加此设置
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		h5detail.delRecordList(p.curPage,10);
	};
	uc.selectCallback=function(){
		h5detail.delRecordList(1,10);
	};
	
	var h5detail={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			var ndmoney=$("#needmoney").val();
			$(".content_tabs li").each(function(index,domEle){ 
				$(domEle).click(function(){	
					$(".content_tabs li").removeClass("cur");
					$(this).addClass("cur");
					$(".detail_info .list").hide();		
					$(".detail_info #list_"+Math.floor(index+1)).show();
					if(index == 2){
						that.delRecordList(1,10);
					}else if(index == 3){
						that.hosList(1);
					}
				});
			});
			if(Number(ndmoney) <= 0){
				$("#paybtn").hide();
				$("#finishedbtn").show();
			}
			
			$('#share').click(function(){
				var pTle=$('#pprim').val();
				that.share1(pTle);
			});
			$('#paybtn').click(function(){
				$('#payTips').show();
			});
			$('#closeDetail').click(function(){
				$('#payTips').hide();
			});
			
			$('#selectTb .sel').click(function(){
				$('#selectTb .sel').removeClass('ed');
				$(this).addClass('ed');
				
			});
			$('#btn_submit').click(function(){
				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val();
				var paymoney = Number(money),pName = $("#pprim").val(); 
				if(paymoney <= 0){
					money = $("#money2").val();
					if(money < 0.01){
						d.alert({content:"捐款金额不低于0.01元",type:'error'});
						return;
					}
					money = Number(money);
					if(!isNaN(money)){
						if(needmoney < money){
							d.alert({content:"最多捐款金额"+needmoney+"元",type:'error'});
						}
//						window.location.href="http://www.17xs.org/project/payView.do?projectId="
//							+projectId+"&amount="+money+"&pName="+pName;
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money;
					}else{
						d.alert({content:"请输入数字",type:'error'});
					}
				}else{
					if(needmoney < money){
						d.alert({content:"最多捐款金额"+needmoney+"元",type:'error'});
					}
//					window.location.href="http://www.17xs.org/project/payView.do?projectId="
//						+projectId+"&amount="+paymoney;
					window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
						+projectId+"&amount="+paymoney;
				}
			});
			$("body").on("click",".qqim",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qqim',1): that.bshare(event,'qqim',0);
				 //that.bshare(event,'qqim',0);
			}).on("click",".qzone",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qzone',1): that.bshare(event,'qzone',0);
				 //that.bshare(event,'qzone',0);
			}).on("click",".sinaminiblog",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'sinaminiblog',1): that.bshare(event,'sinaminiblog',0);
				// that.bshare(event,'sinaminiblog',0);
			}).on("click",".weixin",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'weixin',1): that.bshare(event,'weixin',0);
				 //that.bshare(event,'weixin',0);
			});
		},
	};
	 
	h5detail.init();
});