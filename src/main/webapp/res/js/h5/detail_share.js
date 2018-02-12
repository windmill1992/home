var base=window.baseurl;
var dataUrl={
		extensionDonation:base+'project/extensionDonation.do',
		queryRedPackets:base+'redPackets/queryRedpacket.do',//取红包
		payRedpacket:base+'redPackets/payRedpacket.do'//红包支付
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
	
	h.init();
	d.init();
	en.init(); 
	uc.init();
	p.init({afterObj:$("#pageNum")});

	var page=1;
	var h5detail_share={
		totle:0,
		pageCurrent:1,
		init:function(){
			var that=this;
			
			var ndmoney=$("#needmoney").val();
			if(Number(ndmoney) <= 0)
			{
				$("#paybtnd").hide();
				$("#finishedbtn").show();
			}
			
			var shareType = $('#shareType').val();
			/*
			 * shareType : 0 分享指引
			 * shareType : 1 分享成功
			 */
			if(shareType != "" && shareType == 0)
			{
				$('#notiesInfo').show();
			}

		
			$('#share').click(function(){
				var pTle=$('#pprim').val();
				that.share1(pTle);
			});
			
			$('#paybtn').click(function(){
				that.queryRedPackets();
				$('#payTips').show();
			});
			$("#donate-btn").click(function() {
				$(".payTips").show();
			});
			$('.close-btn').click(function(){
				$('.payTips').hide();
			});

			$('#redclose').click(function(){
				$('#redPackets').hide();
			});
			
			$('#closeBtn').click(function(){
				$(".popup, .confirmBox").css("transform", "scale(0,0)");
				$(".confirmBox").hide();
			});
			
			$('#confirmBtn').click(function(){
				$(".popup, .confirmBox").css("transform", "scale(0,0)");
				$(".confirmBox").hide();
				$('#payTips').hide();
				that.payRedpacket();
			});
			$('#selectTb .payItem').click(function(){
				$('#selectTb .payItem').removeClass('active');
				$(this).addClass('active');
				
				//红包样式处理
				$(".redPaperList li").removeClass("curTab");
			});
			
			$("#money2").click(function(){
				$('#selectTb .payItem').removeClass('active');
				
				//红包样式处理
				$(".redPaperList li").removeClass("curTab");
			});
			
			$('#btn_submit').click(function(){
				
				$('#payTips').hide();
				var money=$(".active").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val();
				
				// 增加红包支付的校验>>start
				if(money>0)
				{
					
				}
				else
				{
					var amountHb=$(".redPaperList li.curTab a").eq(0).attr("m");
					if(amountHb > 0)
					{
						$('#payamount').html(amountHb);
						//红包支付
						$(".confirmBox").show();
						$(".popup, .confirmBox").css("transform", "scale(1,1)");
						return ;
					}
				}
				// 增加红包支付的校验>>end
				
				var extensionPeople = $('#extensionPeople').val();
				if(money=='' || money == undefined){
					money = 0 ;
				}
				var paymoney = Number(money),pName = $("#pprim").val(); 
				if(isNaN(paymoney)){
					d.alert({content:"捐款金额不低于0.01元",type:'error'});
					return;
				}
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
							return;
						}
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					}else{
						d.alert({content:"请输入数字",type:'error'});
						return;
					}
				}else{
					needmoney = Number(needmoney);
					if(needmoney < money){
						d.alert({content:"最多捐款金额"+needmoney+"元",type:'error'});
						return;
					}
					window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
						+projectId+"&amount="+paymoney+"&extensionPeople="+extensionPeople;
				}
			});
			
			$("body").on("click",".qqim",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qqim',1): that.bshare(event,'qqim',0);
			}).on("click",".qzone",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'qzone',1): that.bshare(event,'qzone',0);
			}).on("click",".sinaminiblog",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'sinaminiblog',1): that.bshare(event,'sinaminiblog',0);
			}).on("click",".weixin",function(){ 
				var type=$(this).attr('tel');
				type? that.bshare(event,'weixin',1): that.bshare(event,'weixin',0);
			}).on("click",".redPaperList li ",function(){ 
				$(this).addClass("curTab").siblings().removeClass("curTab");
				var amount=$(".redPaperList li.curTab a").eq(0).attr("m");
				$('#payamount').html(amount);
				//红包支付
				$(".confirmBox").show();
				$(".popup, .confirmBox").css("transform", "scale(1,1)");
			});
			
			h5detail_share.dList(1,10);
		},
		dList:function(page,pageNum){
			var that=this,projectId=$('#projectId').val(),flag=false,extensionPeople=$("#extensionPeople").val();
			if(page==1){flag=true;} 
			$.ajax({
				url:dataUrl.extensionDonation,
				data:{projectId:projectId,extensionPeople:extensionPeople,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result){
					if(result.flag==1){
						if(result.obj.data.length>0){
							var data=result.obj,total=data.total,pageNum=data.pageNum,page=data.page,datas=data.data,len=datas.length,html=[];
							//len=len>pageNum?pageNum:len; 
							var nums = data.nums;
							 $('#page').text(page);
							 $('#pageNum').text(pageNum);
							var html=[];
							html.push('<h3 class="donateTitle">捐助记录</h3>');
							for(var i=0;i<len;i++)
							{
								html.push('<li class="clearfix">');
								html.push('<div class="donateListImg">');
								if(datas[i].imagesurl == null || datas[i].imagesurl == '')
								{
									html.push('<img src="http://www.17xs.org/res/images/user/4.jpg">');
								}
								else
								{
									html.push('<img src="'+datas[i].imagesurl+'">');
								}
								html.push('</div>');
								html.push('<div class="donateListCon">');
								html.push('<h3 class="listConTitle clearfix">');
								html.push('<span class="fl">'+datas[i].name+'</span>');
								html.push('<span class="fr">捐助<i>'+datas[i].dMoney+'</i>元</span>');
								html.push('</h3>');
								html.push('<div class="listCon clearfix">');
								html.push('<div class="listConMes fl">'+datas[i].leaveWord+'</div>');
								html.push('<div class="listConDate fr">'+datas[i].showTime+'</div>');
								html.push('</div>');
								html.push('</div>');
								html.push('</li>');
							}
							$('#donatList').html(html.join(''));
							if(nums > pageNum){
								   $(".details_add").css('display','block'); 
							}
							data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
							data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
						}
						
					}
					else{
						
					}
				}
			})
			//加载更多
			$('#loadMore').click(function(){
				var page = $("#page").text() ;
			    page = Number(page) + 1;
				   $.ajax({
					   url:dataUrl.extensionDonation,
						data:{projectId:projectId,extensionPeople:extensionPeople,page:page,pageNum:10,t:new Date().getTime()},
						success: function(result){
							var data=result.obj;
							if(result.flag==1){
                       		var nums = data.nums;
					   		var page = data.page;
					   		$('#page').text(page);
					   		var sum = data.page*data.pageNum;
					   		if(result.obj.data.length>0){
								var data=result.obj,total=data.total,pageNum=data.pageNum,page=data.page,datas=data.data,len=datas.length,html=[];
								//len=len>pageNum?pageNum:len; 
								var html=[];
								for(var i=0;i<len;i++)
								{
									html=html+'<li class="clearfix"><div class="donateListImg">';
									if(datas[i].imagesurl == null || datas[i].imagesurl == '')
									{
										html=html+'<img src="http://www.17xs.org/res/images/user/4.jpg">';
									}
									else
									{
										html=html+'<img src="'+datas[i].imagesurl+'">';
									}
									html=html+'</div><div class="donateListCon"><h3 class="listConTitle clearfix"><span class="fl">'+datas[i].name+'</span><span class="fr">捐助<i>'+datas[i].dMoney+'</i>元</span></h3>'+
									'<div class="listCon clearfix"><div class="listConMes fl">'+datas[i].leaveWord+'</div><div class="listConDate fr">'+datas[i].showTime+'</div></div></div></div></li>'
								}
								$('#donatList').append(html);
								if(sum >= nums){
									   $(".details_add").css('display','none'); 
								}
								//data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
								//data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
							}
							
					    	}else{
							
							}	
				    	}					
			    	});
				})
			
		},
		share1:function(ptle){
			var html=[];
			html.push('<h2>邀请内容</h2>');
			html.push('<textarea class="area">'+ptle+'</textarea>');
			html.push('<h2>传播至：</h2>');
			html.push('<div class="shareWeblist">');
			html.push('<a class="qqim" >QQ好友</a>');
			html.push('<a class="qzone">QQ空间</a>');
			html.push('<a class="sinaminiblog" >新浪微博</a>');
			html.push('<a class="weixin">微信</a>');
			html.push('</div>');
			var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
			d.open(o,css,"Delshare"); 	
		},
		queryRedPackets:function(){
			$.ajax( {
				url: dataUrl.queryRedPackets,
				cache   : false,
				type: "POST",
				data: {t:new Date().getTime()},
				success : function(result) { 
						if(result.flag==1 && result.obj.total > 0){
							var datalist=result.obj.resultData,html=[],data;
							for(var i=0;i<datalist.length;i++){
								data = datalist[i];
								if(i == 0)
								{
									html.push('<li class="curTab">');
									html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
									html.push('<i>'+data.amount+'</i>元</a></li>');
								}
								else
								{
									html.push('<li>');
									html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
									html.push('<i>'+data.amount+'</i>元</a></li>');
								}
								/*
								html.push('<li>');
								html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
								html.push('<i>'+data.amount+'</i>元</a></li>');
								*/
							}
							$('#redPaperList').html(html.join(''));
							$('#redPackets').show();
						}
				},
				error : function(result) { 
				}
			});
		},
		payRedpacket:function(){
			var title = $("#pprim").val(),money = $(".redPaperList li.curTab a").eq(0).attr("m"),
			rId=$(".redPaperList li.curTab a").eq(0).attr("v"),projectId = $("#projectId").val();
			$.ajax( {
				url: dataUrl.payRedpacket,
				cache   : false,
				type: "POST",
				data: {uredId:rId,projectId:projectId,t:new Date().getTime()},
				success : function(result) { 
						if(result.flag==1){
							window.location.href="http://www.17xs.org/project/paysuccess_h5/?pName="
								+title+"&amount="+money+"&red=1"+ "&projectId="+projectId+"&tradeNo="+result.obj.data;
						}else{
						//	alert(1111);
						}
				},
				error : function(result) { 
				}
			});
		},
	};
	 
	h5detail_share.init();

});