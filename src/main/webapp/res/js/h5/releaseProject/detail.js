var base=window.baseurl;
var dataUrl={
	helpsubmit:base+'/project/addfeedback.do',
	helpphotoDel:base+'file/image/delete.do',
	hostList:base+'/project/H5careProjectList.do',//反馈
	toCommon:base+'/project/addfeedback.do',
	FeedbackList:base+'/project/leaveWordList.do',
	addleaveWord:base+'/project/addleaveWord.do',
	donationlist:base+'project/gardendonationlist.do',
	queryRedPackets:base+'redPackets/queryRedpacket.do',//取红包
	payRedpacket:base+'redPackets/payRedpacket.do',//红包支付
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
	var page=1;
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
						$("#list_3").html("<div class='list-box'></div><div class='load-more'><a class='aload'>点击加载更多</a></div>");
						that.delRecordList(1,10);
					}else if(index == 3){
						that.hosList(1);
					}
				});
			});
			//===图片查看 ===//
			$('#list_1 img').click(function(){
		        var imgArray = [];
		        var curImageSrc = $(this).attr('src');
		        var oParent = $(this).parent();
		        if (curImageSrc && !oParent.attr('href')) {
		            $('#list_1 img').each(function(index, el) {
		                var itemSrc = $(this).attr('src');
		                imgArray.push(itemSrc);
		            });
		             wx.previewImage({
		                current: curImageSrc,
		                urls: imgArray
		            });
		        }
		    });
			//===========item定位start=============//
				var itemType = Number($('#itemType').val());
				if(itemType == 1)
				{
					$(".content_tabs li").removeClass("cur");
					$(".content_tabs li").eq(1).addClass("cur");
					$("#list_1").hide();
					$("#list_2").show();
				}
				else if(itemType == 2)
				{
					$(".content_tabs li").removeClass("cur");
					$(".content_tabs li").eq(2).addClass("cur");
					$("#list_1").hide();
					$("#list_3").show();
					that.delRecordList(1,10);
				}
				else if(itemType == 3)
				{
					$(".content_tabs li").removeClass("cur");
					$(".content_tabs li").eq(3).addClass("cur");
					$("#list_1").hide();
					$("#list_4").show();
					that.hosList(1);
				}
	
			//===========item定位end=============//
			if(Number(ndmoney) <= 0){
				$("#paybtn").hide();
				$("#finishedbtn").show();
			}
			
			$('#share').click(function(){
				var pTle=$('#pprim').val();
				that.share1(pTle);
			});
			
			
			$('#paybtn').click(function(){
				var userId = $("#userId").val(),browser = $("#browser").val();//判断是否已登录
				if((userId == null || userId == '')&& browser!='wx'){
					$('#loginCue').show();
				}else{
					that.queryRedPackets();//红包
					$('#payTips').show();
				}
				
			});
			$('#cue_fr').click(function(){
				that.queryRedPackets();//红包
				$('#loginCue').hide();
				$('#payTips').show();
			});
			$('#cue_fl').click(function(){
				window.location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=projectId_'+$("#projectId").val()+'_extensionPeople_'+$("#extensionPeople").val();
			});
			
			$('#closeDetail').click(function(){
				$('#payTips').hide();
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
			
			$('#selectTb .sel').click(function(){
				$('#selectTb .sel').removeClass('ed');
				$(this).addClass('ed');
				
				//红包样式处理
				$(".redPaperList li").removeClass("curTab");
			});
			$("#payMoney_promptConfirm").click(function(){
				$("#payMoney_prompt").hide();
			})
			$('#btn_submit').click(function(){

				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val();
				
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
				
				var paymoney = Number(money),pName = $("#pprim").val(); 
				var extensionPeople = $('#extensionPeople').val();
				var goodLibrary = $('#goodLibrary').val();
				if(isNaN(paymoney)){
					//d.alert({content:"捐款金额不低于0.01元",type:'error'});
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				if(paymoney <= 0){
					money = $("#money2").val();
					if(money < 0.01){
						//d.alert({content:"捐款金额不低于0.01元",type:'error'});
						$('#payMoney_content').html("捐款金额不低于0.01元");
						$('#payMoney_prompt').show();
						return;
					}
					money = Number(money);
					if(!isNaN(money)){
						if(needmoney < money){
							//d.alert({content:"最多捐款金额"+needmoney+"元",type:'error'});
							$('#payMoney_content').html("最多捐款金额"+needmoney+"元");
							$('#payMoney_prompt').show();
							return;
						}
						/*if((userId == null || userId == '')){
							window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
								+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
						}else{
							window.location.href="http://www.17xs.org/WapAlipay/optPayWay.do?projectId="
								+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
						}*/
						if((userId == null || userId == '')){
							window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
								+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
						}else if(goodLibrary == 0){
							window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
								+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
						}else if(goodLibrary == 1){
							$('#goodLibrary_prompt').show();
						}
						
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
					/*if((userId == null || userId == '')){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					}else{
						window.location.href="http://www.17xs.org/WapAlipay/optPayWay.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					}*/
					if((userId == null || userId == '')){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					}else if(goodLibrary == 0){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					}else if(goodLibrary == 1){
						$('#goodLibrary_prompt').show();
					}
				}
			});
			$('#rightNowPay').click(function(){

				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val();
				
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
				
				var paymoney = Number(money),pName = $("#pprim").val(); 
				var extensionPeople = $('#extensionPeople').val();
				var goodLibrary = $('#goodLibrary').val();
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
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					
				}
			});
			$('#goodLibraryPay').click(function(){
				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val();
				
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
				
				var paymoney = Number(money),pName = $("#pprim").val(); 
				var extensionPeople = $('#extensionPeople').val();
				var goodLibrary = $('#goodLibrary').val();
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
						window.location.href="http://www.17xs.org/WapAlipay/optPayWay.do?projectId="
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
					window.location.href="http://www.17xs.org/WapAlipay/optPayWay.do?projectId="
						+projectId+"&amount="+money+"&extensionPeople="+extensionPeople;
					
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
			}).on("click",".redPaperList li ",function(){ 
				$(this).addClass("curTab").siblings().removeClass("curTab");
				var amount=$(".redPaperList li.curTab a").eq(0).attr("m");
				$('#payamount').html(amount);
				//红包支付
				$(".confirmBox").show();
				$(".popup, .confirmBox").css("transform", "scale(1,1)");
			});
		},
		delRecordList:function(page,pageNum){
			var that=this,projectId=$('#projectId').val(),flag=false;
			if(page==1){flag=true;} 
			$.ajax({
				url:dataUrl.donationlist,
				data:{id:projectId,page:page,pageNum:10,t:new Date().getTime()},
				success: function(result){
					if(result.flag==1){
						if(result.obj.data.length<10){
							$(".load-more").html("没有更多数据了");
						}
						if(result.obj.data.length>0){
							var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
							len=len>pageNum?pageNum:len; 
							var html=[];
							for(var i=0;i<len;i++){
							html.push('<a href="javascript:void(0);">');
							html.push('<div class="item bg2 record"><span class="thumb">');
							if(datas[i].imagesurl != null ){
								html.push('<img osrc="'+datas[i].imagesurl+'" class="fl" src="'+datas[i].imagesurl+'"></span>');
							}else{
								html.push('<img osrc="'+base+'res/images/detail/people_avatar.jpg" class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></span>');
							}
							html.push('<div class="name"><p class="name_p1">'+$.ellipsis(datas[i].name,7,'..')+'</p>');
							html.push('<p class="name_p2">捐<label>￥'+$.formatNum(datas[i].dMoney)+'</label></p></div>');
							html.push(' <div class="money"><p class="money_p1">'+(datas[i].leaveWord==null?"":datas[i].leaveWord)+'<span class="money_p2">['+datas[i].showTime+']</span></p>');
							html.push('</div><div class="clear"></div></div>');
							html.push('</a>');
							}
							$('.list-box').append(html.join(''));	
							data.total>1&&flag?p.pageInit({pageLen:data.total,isShow:true}):'';
							data.total==1?p.pageInit({pageLen:data.total,isShow:false}):'';
						}
						
					}
				}
			})
			
		},
		hosList:function(page){
			var that=this,id=$('#projectId').val();
			$.ajax({ 
					url:dataUrl.hostList, 
					data:{projectId:id,page:page,pageNum:10,t:new Date().getTime()},
					success: function(result) { 
						if(result.flag==0){
							d.alert({content:result.errorMsg,type:'error'});
							
						}else if(result.flag==-1){
							en.show(that.hosList);
						}else if(result.flag==2){
							$('#pagetotle').html("共0页");
							$('.bdback-page .page').hide();
							$('#list_4').html('');
							return false;						
						}else{
							var datalist=result.obj.data,html=[];
							that.total=page=result.obj.total,that.currpage=result.obj.page,that.hosData=datalist;
							$('#pagenum').val(that.currpage);
							$('#feedNum').html(result.obj.nums);
							for(var i=0;i<datalist.length;i++){
								var imgs = datalist[i].imgs;
								var temp="";
								html.push('<div class="item bg2"><div class="avatar">');
								if(datalist[i].userImageUrl != null){
									html.push('<img class="fl" src="'+datalist[i].userImageUrl+'"></div>');
								}else{
									if(datalist[i].uName != null){
										html.push('<img class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></div>');
									}else{
										html.push('<img class="fl" src="'+base+'res/images/h5/images/kefu_hulu.jpg"></div>');
									}
								}
								html.push('<div class="detail">');
								if(datalist[i].source == 'home')
								{
									html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
								}
								else if(datalist[i].source == 'admin')
								{
									html.push('<div class="title"><span class="name">善园基金会</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
								}
								else
								{
									if(datalist[i].uName == null)
									{
										html.push('<div class="title"><span class="name">善园基金会</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
									}
									else
									{
										html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span><span class="time">'+(new Date(datalist[i].cTime)).pattern("yyyy/MM/dd HH:mm")+'</span></div>');
									}
								}
								html.push('<div class="info">');
								if(imgs&&imgs.length>0){
									html.push(' <p class="info_text">'+datalist[i].content+'</p>');
									temp+="";
									for(var j=0;j<imgs.length;j++){
										temp+=('<p><img src="'+imgs[j]+'" width="268" height="357"/></p>');
									}	
									html.push(temp);
								}else{
									html.push('<p>'+datalist[i].content+'</p>');
								}
								html.push('<div class="clear"></div> </div></div>');
								html.push('<div class="clear"></div></div>');
							}
							if(datalist.length<1&&that.currpage==1){
								$('.bdback-page .page').hide();
							}else{
							$('#list_4').html(html.join(''));
							if(that.currpage==1){
								$('#pageprev').attr("disabled","disabled").addClass('pagedefalut');
							}else{
								$('#pageprev').removeAttr("disabled").removeClass('pagedefalut');
							}
							if(that.currpage==that.totalpage){
								$('#pagenext').attr("disabled","disabled").addClass('pagedefalut');
								
							}else{
								$('#pagenext').removeAttr("disabled").removeClass('pagedefalut');
							}
							}
							$('#pagetotle').html("共"+that.totalpage+"页");
							
							//===图片查看 ===//
							$('#list_4 .info img').click(function(){
						        var imgArray = [];
						        var curImageSrc = $(this).attr('src');
						        var oParent = $(this).parent();
						        if (curImageSrc && !oParent.attr('href')) {
						            $('#list_4 .info img').each(function(index, el) {
						                var itemSrc = $(this).attr('src');
						                imgArray.push(itemSrc);
						            });
						             wx.previewImage({
						                current: curImageSrc,
						                urls: imgArray
						            });
						        }
						    });
						}
						
					},
					share:function(){
						var html=[],v=$('#pprim').attr('data').split('|');
						html.push('<h2>邀请内容</h2>');
						html.push('<textarea class="area">'+v[2]+'，邀请您一起行善！注册善园基金会，企业即以您的名义，助捐'+v[0]+'元！(注册邀请码：'+v[1]+')</textarea>');
						html.push('<h2>传播至：</h2>');
						html.push('<div class="shareWeblist">');
						html.push('<a class="qqim" tel="1" >QQ好友</a>');
						html.push('<a class="qzone" tel="1">QQ空间</a>');
						html.push('<a class="sinaminiblog" tel="1" >新浪微博</a>');
						html.push('<a class="weixin" tel="1">微信</a>');
						html.push('</div>');
						var o ={t:"企业助善邀请",c:html.join("")},css={width:'600px',height:'385px'};  
						d.open(o,css,"Delshare"); 	
					},
				});	
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
									html.push('<li>');
									html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
									html.push('<i>'+data.amount+'</i>元</a></li>');
								}
								else
								{
									html.push('<li>');
									html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
									html.push('<i>'+data.amount+'</i>元</a></li>');
								}
							
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
								+title+"&amount="+money+"&red=1&projectId="+projectId+"&tradeNo="+result.obj.data;
						}else{
							//alert(1111);
						}
				},
				error : function(result) { 
				}
			});
		},
	};
	 
	h5detail.init();
	$(".aload").live("click",function(){
		page++;
		h5detail.delRecordList(page,10);
	});
});