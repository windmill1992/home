var base=window.baseurl;
var dataUrl={
	helpsubmit:base+'/project/addfeedback.do',
	helpphotoDel:base+'file/image/delete.do',
	hostList:base+'/h5ProjectDetails/H5ProjectFeedBackList.do',//反馈
	toCommon:base+'/project/addfeedback.do',
	FeedbackList:base+'/project/leaveWordList.do',
	addleaveWord:base+'/project/addleaveWord.do',
	donationlist:base+'h5ProjectDetails/projectDeatilsdonationlist.do',
	queryRedPackets:base+'redPackets/queryRedpacket.do',//取红包
	payRedpacket:base+'redPackets/payRedpacket.do',//红包支付
	isOrNotAuditProject:base+"uCenterProject/isOrNotAuditProject.do",  //是否证实
	gotoAuditProject:base+"uCenterProject/gotoAuditProject.do",  //跳到证实页面
	User_replyLeaveWord:base+"h5ProjectDetails/addNewLeaveWord.do",//提交评论
	moneyConfig:base+'h5GardenProject/getMoneyConfigList.do',//捐款金额配置查询
	openDayDonate:base+'h5GardenProject/openDayDonate.do'//开通日捐
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
			//初始化加载项目反馈
			//that.hosList(1);
			that.delRecordList(1,10);
			var ndmoney=$("#needmoney").val();
			
			/*if(Number(ndmoney) <= 0){
				$("#paybtn").hide();
				$(".btnFr .fr").hide();
				$("#payendbtn").show();
			}*/
			/**点击金额框，金额消失，手动输入*/
			$('#money2').click(function(){
				$('#paytipsCon').val('输入金额，随喜结缘');
				$('#money2').val('');
			});
			/**金额配置+-*/
			$('#paytipsAdd').click(function(){
				var money=$('#money2').val();
				var projectId=$('#projectId').val();
				var sort=$('#sort').val();
				if(money==''){
					that.moneyAddOrSub(projectId,-1,'add');
				}
				else{
					that.moneyAddOrSub(projectId,sort,'add');
				}
				return;
			});
			$('#paytipsSub').click(function(){
				var projectId=$('#projectId').val();
				var sort=$('#sort').val();
				that.moneyAddOrSub(projectId,sort,'sub');
				return;
			});
			
			$('#dayDonate').click(function(){
				var userId = $("#userId").val(),browser = $("#browser").val();//判断是否已登录
				if((userId == null || userId == '')&& browser!='wx'){
					$('#donateType').val(1);
					$('#loginCue').show();
				}else{
					$('#dayShan').show();
				}
				
			});
			/**日行一善，我要结缘*/
			$('#dayBtn').click(function(){
				var money=$('#dayMoney').val();
				var projectId=$('#projectId').val();
				if(isNaN(Number(money))){
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				if(Number(money)<0.01){
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				that.openDayDonate(money,projectId);
				return;
			});
			$('.cha1').click(function(){
				$('#payTips').hide();
			});
			$('#share').click(function(){
				var pTle=$('#pprim').val();
				that.share1(pTle);
			});
			
			/**我要结缘*/
			$('#paybtn').click(function(){
				var userId = $("#userId").val(),browser = $("#browser").val();//判断是否已登录
				if((userId == null || userId == '')&& browser!='wx'){
					$('#donateType').val(0);
					$('#loginCue').show();
				}else{
					//that.queryRedPackets();//红包
					$('#payTips').show();
				}
			});
			$('#cue_fr').click(function(){
				//that.queryRedPackets();//红包
				var donateType=$('#donateType').val();
				if(donateType==0){
					$('#payTips').show();
				}
				else{
					$('#dayShan').show();
				}
				$('#loginCue').hide();
				
			});
			$('#cue_fl').click(function(){
				window.location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=gardenProjectId_'+$("#projectId").val()+'_extensionPeople_'+$("#extensionPeople").val();
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
			
			/*$('#selectTb .sel').click(function(){
				$('#selectTb .sel').removeClass('ed');
				$(this).addClass('ed');
				
				//红包样式处理
				$(".redPaperList li").removeClass("curTab");
			});*/
			$("#payMoney_promptConfirm").click(function(){
				$("#payMoney_prompt").hide();
			})
			$('#btn_submit').click(function(){
				$('#payTips').hide();
				var money=$('.ed').parent().attr('t'),money2=$('#money2').val(),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val(),
				realName=$("#realName").val(),mobileNum=$("#mobileNum").val();
				var information=$("#donateWord").val();
				if(typeof(money)!=undefined&&money>0){
					
				}
				else{
					money=money2;
				}
				var paymoney = Number(money);
				var extensionPeople = $('#extensionPeople').val();
				var goodLibrary = 0;
				if(isNaN(paymoney)){
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				if(paymoney <= 0){
					if(paymoney <= 0){
					$('#payMoney_content').html("捐款金额不低于0.01元");
					$('#payMoney_prompt').show();
					return;
				}
				money = Number(money);
				if(!isNaN(money)){
					if(needmoney < money){
						$('#payMoney_content').html("最多捐款金额"+needmoney+"元");
						$('#payMoney_prompt').show();
						return;
					}
					if((userId == null || userId == '')){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information;
					}else if(goodLibrary == 0){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information;
					}else if(goodLibrary == 1){
						//$('#goodLibrary_prompt').show();
						return;
					}
					}else{
						$('#payMoney_content').html("网络异常！");
						$('#payMoney_prompt').show();
						return;
					}
				}else{
					needmoney = Number(needmoney);
					if(needmoney < money){
						$('#payMoney_content').html("最多捐款金额"+needmoney+"元");
						$('#payMoney_prompt').show();
						return;
					}
					if((userId == null || userId == '')){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information;
					}else if(goodLibrary == 0){
						window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information;
					}else if(goodLibrary == 1){
						//$('#goodLibrary_prompt').show();
						return;
					}
				}
			});
			 /*充值*/
		    $("#confirCharge").click(function(){
		    	var chargeMomeny = Number($('#chargeMomeny').val()),userName = $('#userName').text(),mobile = $('#mobile').text(),
		    	browser=$('#browser').val(),dayMoney=$('#dayMoney').val(),projectId=$("#projectId").val(),message=$('#message').val();
		    	if(chargeMomeny <= 0){
		    		$('#payMoney_content').html("请输入充值金额");
					$('#payMoney_prompt').show();
		    		return false;
		    	}
		    	if(browser=='wx'){
		    		window.location.href='http://www.17xs.org/visitorAlipay/tenpay/rechargedeposit_sy.do?amount='+chargeMomeny+'&touristName='+userName+'&touristMobile='+mobile+'&projectId='+projectId+'&donateTimeId='+message;
		    	}
		    	else{
		    		window.location.href='http://www.17xs.org/rechargeWapAlipay/deposit.do?amount='+chargeMomeny+'&payType=5&touristName='+userName+'&touristMobile='+mobile;
		    	}
		    });
		    $('#concelCharge').click(function(){
		    	$('.cue_f').hide();
		    	$('#dayShan').hide();
		    });
		    $('.backBj').click(function(){
		    	$('#dayShan').hide();
		    });
			$('#rightNowPay').click(function(){

				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val();
				var ShanhuInformation=$("#donateWord").val();
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
								+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+ShanhuInformation;
						
						
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
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+ShanhuInformation;
					
				}
			});
			$('#goodLibraryPay').click(function(){
				$('#payTips').hide();
				var money=$(".ed").attr('t'),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val();
				var ShanhuInformation=$("#donateWord").val();
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
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+ShanhuInformation;
						
						
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
						+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+ShanhuInformation;
					
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
			//回复按钮
			$('#leaveWordSubmit').click(function(){
				var content = $('#content').val();
				if(content==''){
					$("#msg").html('<p>请输入评论！</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
				that.verify();
			});
			
			$('.fr').click(function(){
				$(".moved_up").hide();
			});
			var donateTimeId2 = $('#message2').val();
			if(donateTimeId2!=''&&donateTimeId2!=0){
				$('#payMoney_content').html("日捐开通成功！");
				$('#payMoney_prompt').show();
			}
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
							//接受留言信息
							var leavewords=result.obj1;
							for(var i=0;i<len;i++){
								html.push('<a href="javascript:void(0);">');
								html.push('<div class="item record"><span class="thumb">');
								if(datas[i].imagesurl != null ){
									html.push('<img osrc="'+datas[i].imagesurl+'" class="fl" src="'+datas[i].imagesurl+'"></span>');
								}else{
									html.push('<img osrc="'+base+'res/images/detail/people_avatar.jpg" class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></span>');
								}
								html.push('<div class="name"><p class="name_p1">'+$.ellipsis(datas[i].name,7,'..')+'</p>');
								html.push('<p class="name_p2">捐<label>￥'+$.formatNum(datas[i].dMoney)+'</label></p></div>');
								html.push('<div class="money">');
								html.push('<p class="money_p1">'+(datas[i].leaveWord==null?"":datas[i].leaveWord)+'</p>');
								html.push('<div class="clear"></div>');
								//增加留言信息
                                var itemss=leavewords[i];
                                html.push('<div class="timeMessagej">');
    							if(itemss.length>0){
    									html.push('<span class="timeSpj">'+datas[i].showTime+'</span>');
    									html.push('<div class="messagej"><span class="messageSpj" onclick="donateLeaveWord('+datas[i].id+','+i+')"></span>');
    									html.push('</div></div>');
    							}else{
    									html.push('<span class="timeSpj">'+datas[i].showTime+'</span>');
    									html.push('<div class="messagej"><span class="messageSpj" onclick="donateLeaveWord('+datas[i].id+','+i+')"></span>');
    									html.push('</div></div>');
    							}
    							if(itemss.length>0){
    								html.push('<div class="message_boardj  message_listj'+i+'">')
    							}
    							else{
    								html.push('<div class="message_boardj  message_listj'+i+'" style="background:none">')
    							}
    							for(var j=itemss.length-1;j>=0;j--){
    									if(itemss[j].replyUserId==null){
    										html.push('<p class="board_textj" ><span onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+i+')" >'+itemss[j].leavewordName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');

    									}else{

    										html.push('<p class="board_textj" ><span  onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+i+')" >'+itemss[j].leavewordName+'<b class="hf">回复</b>'+itemss[j].replyName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
    									}	
    								}
    							if(itemss.length>0){
    									html.push('<span  class="danoteMore'+i+' danote1" onclick="donateLoadMore('+i+','+datas[i].id+','+itemss[0].total+')">查看更多</span>');
    								}else{
    									html.push('<span  class="danoteMore'+i+' danote1" onclick="donateLoadMore('+i+','+datas[i].id+','+0+')">查看更多</span>');
    								}	
    							html.push('</div>');
								html.push('</div><div style="clear:both"></div>');
								html.push('</div><div class="clear"></div></div>');
								html.push('</a>');
							}
							$('.list-box').append(html.join(''));
							//是否显示更多
							/*for(var k=0;k<datas.length;k++){
								itemsss=leavewords[k];
								if(itemsss.length>0){
									if(itemsss[0].total<=(itemsss[0].currentPage-1)*20+itemsss.length)
										$('.danote1').html('查看更多');
								}else{
									$('.danote1').hide()
								}
								
							}*/
							for(var k=0;k<datas.length;k++){
								itemsss=leavewords[k];
								if(itemsss.length>0){
									if(itemsss[0].total<=(itemsss[0].currentPage-1)*20+itemsss.length){
										$('.danoteMore'+k).hide();
									}else{
										$('.danoteMore'+k).html("查看更多");
									}	
								}else{
									$('.danoteMore'+k).hide();
								}
							}
							
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
							var leaveWordDataList=result.obj1;
							for(var i=0;i<datalist.length;i++){
								var imgs = datalist[i].imgs;
								var temp="";
								html.push('<div class="item bg2">');
								html.push('<div class="avatar" >');
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
									html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span></div>');
								}
								else if(datalist[i].source == 'admin')
								{
									html.push('<div class="title"><span class="name">善园基金会</span></div>');
								}
								else
								{
									if(datalist[i].uName == null)
									{
										html.push('<div class="title"><span class="name">善园基金会</span></div>');
									}
									else
									{
										html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span></div>');
									}
								}
								html.push('<div class="info">');
								if(imgs&&imgs.length>0){
									html.push(' <p class="info_text">'+datalist[i].content+'</p><div style="clear: both"></div>');
									temp+="";
									for(var j=0;j<imgs.length;j++){
										temp+=('<p><img src="'+imgs[j]+'" width="268" height="357"/></p>');
									}	
									html.push(temp);
								}else{
									html.push('<p>'+datalist[i].content+'</p>');
								}
								html.push('<div class="clear"></div>');
								var itemss=leaveWordDataList[i];
								if(itemss.length>0){
									html.push('<div class="timeMessage"><span class="timeSp">'+datalist[i].showTime+'</span><div class="message" ><span class="messageSp" onclick="feedBackleaveword('+datalist[i].id+','+i+')"></span></div></div>');
								}else{
									html.push('<div class="timeMessage"><span class="timeSp">'+datalist[i].showTime+'</span><div class="message" ><span class="messageSp" onclick="feedBackleaveword('+datalist[i].id+','+i+')"></span></div></div>');

								}
								if(itemss.length>0){
									html.push('<div class="message_board  message_list'+i+'">');
								}
								else{
									html.push('<div class="message_board  message_list'+i+'" style="background:none">');
								}
								//循环插入信息  
									for(var j=itemss.length-1;j>=0;j--){
											if(itemss[j].replyUserId==null){
												html.push('<p class="board_text" ><span onclick="reply('+itemss[j].leavewordUserId+','+itemss[j].projectFeedbackId+','+i+')" >'+itemss[j].leavewordName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
	
											}else{
												html.push('<p class="board_text" ><span  onclick="reply('+itemss[j].leavewordUserId+','+itemss[j].projectFeedbackId+','+i+')" >'+itemss[j].leavewordName+'<b class="hf">回复</b>'+itemss[j].replyName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
											}
												
									}
											//在此处插入一条加载更多,查看更多的信息	
									if(itemss.length>0){
										html.push('<span class="more'+i+'  danote1" onclick="loadMore('+i+','+datalist[i].id+','+itemss[0].total+')">查看更多</span>');
									}
									else{
										html.push('<span class="more'+i+'  danote1" onclick="loadMore('+i+','+datalist[i].id+','+0+')">查看更多</span>');
									}	
								html.push('</div>');
								
								html.push('</div></div>');
								html.push('<div class="clear"></div></div>');
							}
							if(datalist.length<1&&that.currpage==1){
								$('.bdback-page .page').hide();
							}else{
							$('#list_4').html(html.join(''));
							//是否显示更多
							for(var k=0;k<datalist.length;k++){
								itemsss=leaveWordDataList[k];
								if(itemsss.length>0){
									if(itemsss[0].total<=(itemsss[0].currentPage-1)*20+itemsss.length){
										$('.more'+k).hide();
									}else{
										$('.more'+k).html("查看更多");
									}	
								}else{
									//$('.more'+k).html("");
									$('.more'+k).hide();
								}
							}
							$("#list_4").show();
							
							//===图片查看 ===//
							/*$('#list_4 .info img').click(function(){
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
						    });*/
						}
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
		//提交评论
		verify:function(){
				var that=this,projectId=$('#projectId').val(),
				projectDonateId=$('#projectDonateId').val(),projectFeedbackId=$('#projectFeedbackId').val(),
				leavewordUserId=$('#leavewordUserId').val(),replyUserId=$('#replyUserId').val(),
				leavewordName=$('#leavewordName').val(),replyName=$('#replyName').val(),
				content=$('#content').val();
				var index=$("#index").val();
				if(content==''){
					$("#msg").html('<p>请输入评论！</p>');
					$("#msg").show();
					setTimeout(function () {
			            $("#msg").hide();
			        }, 2000);
					return false;
				}
				if(projectDonateId==''||projectDonateId==undefined){
					projectDonateId=null;
				}
				if(projectFeedbackId==''||projectFeedbackId==undefined){
					projectFeedbackId=null;
				}
				if(leavewordUserId==''||leavewordUserId==undefined){
					leavewordUserId=null;
				}
				if(replyUserId==''||replyUserId==undefined){
					replyUserId=null;
				}
				if(leavewordName==''||leavewordName==undefined){
					leavewordName=null;
				}
				if(replyName==''||replyName==undefined){
					replyName=null;
				}
				$.ajax({
					url:dataUrl.User_replyLeaveWord,
					data:{projectId:projectId,projectDonateId:projectDonateId,projectFeedbackId:projectFeedbackId,leavewordUserId:leavewordUserId,replyUserId:replyUserId,leavewordName:leavewordName,replyName:replyName,content:content},
					success: function(result){
						if(result.errorCode == "0000"){//成功
							$(".moved_up").hide();
							//回复或者发表评论成功之后需要清空页面隐藏的值
							$('#replyUserId').val("");
							$('#leavewordUserId').val("");
							$('#leavewordName').val("");
							$('#replyName').val("");
							$('#content').val("");
							$('#projectDonateId').val("");
							$('#projectFeedbackId').val("");
							//判断回复之后刷新
							if(projectDonateId==null && projectFeedbackId!=null && projectFeedbackId!=""){
								$.ajax({
									url : "http://www.17xs.org/h5ProjectDetails/refleshLeaveWord.do",
									data : {
										type : 0,
										projectId : projectId,
										projectFeedbackId : projectFeedbackId,
										currentPage : 1
									},
									success : function(result) {
										if (result.flag == "1") {
											//成功
											var html=[];
										
											itemss = result.obj;
											for (var j = itemss.length - 1; j >= 0; j--) {
												if (itemss[j].replyUserId == null) {
													html.push('<p class="board_text" onclick="reply('+ itemss[j].leavewordUserId+ ','+ itemss[j].projectFeedbackId+','+index+')"><span>'+ itemss[j].leavewordName+ '：</span>'+ itemss[j].content+ '</p><div class="clear"></div>');
													} else {
													html.push('<p class="board_text" onclick="reply('+ itemss[j].leavewordUserId+ ','+ itemss[j].projectFeedbackId+','+index+ ')"><span>'+ itemss[j].leavewordName+ '<b class="hf">回复</b>'+ itemss[j].replyName+ '：</span>'+ itemss[j].content+ '</p><div class="clear"></div>');
													}
												}
												//判断是否显示加载更多
												if(itemss.length>0){
													// $('.messageTs'+index).html(itemss[0].total);
													//回复之后回复的总的条数+1
													if (itemss[0].total <= (itemss[0].currentPage - 1)*20 + itemss.length) {
													   $('.more' + index).hide();
													}
													else{
														 // $('.more'+index).html("查看更多");
														html.push('<span class="more'+index+'  danote1" onclick="loadMore('+index+','+projectFeedbackId+','+0+')">查看更多</span>');
													}
												}
												//追加数据
												$('.message_list'+index).html(html.join(''));
												$('.message_list'+index).css("background","#f3f3f3");
												
											} else {//失败
												$("#msg").html('<p>' + result.errorMsg + '</p>');
												$("#msg").show();
												setTimeout(function() {
													$("#msg").hide();
												}, 2000);
												return;
											}
										}
									});
							}
							else if(projectDonateId!=null && projectFeedbackId==null && projectDonateId!=""){
								$.ajax({
									url : "http://www.17xs.org/h5ProjectDetails/refleshLeaveWord.do",
									data : {
										type : 0,
										projectId : projectId,
										projectDonateId : projectDonateId,
										currentPage : 1
									},
									success : function(result) {
										if (result.flag == "1") {
											//成功
											var html=[];
											itemss = result.obj;
											for (var j = itemss.length - 1; j >= 0; j--) {
													if(itemss[j].replyUserId==null){
														html.push('<p class="board_textj" ><span onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+index+')" >'+itemss[j].leavewordName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');
	
													}else{

														html.push('<p class="board_textj" ><span  onclick="donateReply('+itemss[j].leavewordUserId+','+itemss[j].projectDonateId+','+index+')" >'+itemss[j].leavewordName+'<b class="hf">回复</b>'+itemss[j].replyName+'：</span>'+itemss[j].content+'</p><div class="clear"></div>');

													}	
												}
												
												//判断是否显示加载更多
												if(itemss.length>0){
													// $('.messageTsj'+index).html(itemss[0].total);
													if (itemss[0].total <= (itemss[0].currentPage - 1)*20 + itemss.length) {
													   $('.danoteMore' + index).hide();
													}
													else{
													   //$('.danoteMore'+index).html("查看更多");
														html.push('<span  class="danoteMore'+index+' danote1" onclick="donateLoadMore('+index+','+projectDonateId+','+0+')">查看更多</span>');
													}
												}
												//追加数据
												$('.message_listj'+index).html(html.join(''));
												$('.message_listj'+index).css("background","#f3f3f3");
												
											} else {//失败
												$("#msg").html('<p>' + result.errorMsg + '</p>');
												$("#msg").show();
												setTimeout(function() {
													$("#msg").hide();
												}, 2000);
												return;
											}
										}
									});
								
							}
						}
						else if(result.errorCode == "0001"){//未登录，账号密码登录
							$("#msg").html('<p>您还未登录！</p>');
							$("#msg").show();
							setTimeout(function () {
					            $("#msg").hide();
					        }, 2000);
							window.location='http://www.17xs.org/ucenter/user/Login_H5.do';
						}
						else{//失败
							alert(-1)
							$("#msg").html('<p>'+result.errorMsg+'</p>');
							$("#msg").show();
							setTimeout(function () {
					            $("#msg").hide();
					        }, 2000);
							return ;
						}
					}
				});
		},
		//捐款金额的+、-
		moneyAddOrSub:function(projectId,sort,type){
				$.ajax({
					url:dataUrl.moneyConfig,
					data:{projectId:projectId,priority:sort,type:type},
					success: function(result){
						if(result.flag == 1){//成功
							var data=result.obj;
							if(data!=null){
								$('#paytipsCon').val(data.content);
								$('#money2').val(data.money);
								$('#sort').val(data.priority);
							}
						}
						else{//失败
							$('#payMoney_content').html(result.errorMsg);
							$('#payMoney_prompt').show();
						}
					}
				});
		},
		//开通日捐
		openDayDonate:function(money,projectId){
				$.ajax({
					url:dataUrl.openDayDonate,
					data:{projectIds:projectId,money:money,category:'good'},
					success: function(result){
						if(result.errorCode == "0000"){//成功
							$('#payMoney_content').html("日捐开通成功！");
							$('#dayShan').hide();
							$('#payMoney_prompt').show();
						}
						else if(result.errorCode == "0001"){//未登录
							window.location.href='http://www.17xs.org/ucenter/user/Login_H5.do?flag=gardenProjectId_'+$("#projectId").val()+'_extensionPeople_'+$("#extensionPeople").val();
						}
						else if(result.errorCode=="0002"){//微信授权登陆
							window.location.href=result.errorMsg;
						}
						else if(result.errorCode=="0003"){//余额不足，进行充值
							//var moneys = Number(money)*10;
							$('#chargeMomeny').val(Number(money));
							$('#message').val(result.errorMsg)
							$('.cue_f').show();
						}
						else{//失败
							$('#payMoney_content').html(result.errorMsg);
							$('#dayShan').hide();
							$('#payMoney_prompt').show();
						}
					}
				});
		}
	};
	 
	h5detail.init();
	$(".aload").live("click",function(){
		page++;
		h5detail.delRecordList(page,10);
	});
});
