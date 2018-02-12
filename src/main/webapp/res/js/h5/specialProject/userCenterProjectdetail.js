var base=window.baseurl;
//var base="http://www.17xs.org:8080/";
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
	User_replyLeaveWord:base+"h5ProjectDetails/addNewLeaveWord.do",//提交评论
	wxLogin:base+"uCenterProject/wxLogin.do",//判断登录
	view:base+"uCenterProject/dayDonateProject_details.do"
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
var hasRed = false;
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
		loginUrl:'',
		init:function(){
			var that=this;
			//初始化加载项目反馈
			that.hosList(1);
			that.delRecordList(1,10);
			that.bindEvent();
			if($('#dayDonatePage').length > 0){
				that.loginUrl = '/ucenter/user/Login_H5.do?flag=dayDonate_'+$('#projectId').val()+'_extensionPeople_'+$('#extensionPeople').val();
			}else{
				that.loginUrl = '/ucenter/user/Login_H5.do?flag=special_'+$('#projectId').val()+'_extensionPeople_'+$('#extensionPeople').val();
				that.pageScroll();
			}
			$('#content1 p').each(function(){
				if($(this).find('img').length > 0){
					$(this).css({'text-indent':0});
				}
			});
		},
		scrollPage:function (obj){
			var _this = $(obj);
			_this.closest('.content_tabs').find('.cur').removeClass('cur');
			_this.addClass("cur");
			var $e = $("#" + _this.parent().attr("data-id"));
			$e.get(0).scrollIntoView();
			var top = $('#pageScroll').scrollTop();
			if($('.content_tabs').hasClass('tabs_add')){
				if(obj.id == 'xiangq'){
					$('#pageScroll').scrollTop(top - 2);
				}else{
					$('#pageScroll').scrollTop(top - 2 - 34);
				}
			}else{
				$('#pageScroll').scrollTop(top - 43);
			}
		},
		pageScroll:function(){
			var th = 0;
			setTimeout(function(){
				var $title = $('.detail_info .title'),
					$swipe = $('#mySwipe'),
					$prog = $('.progress'),
					$prof = $('.profile'),
					$apply = $('.apply_box'),
					$sdtHead = $('.sdt-head');
				if($title.length > 0){
					th += $title.height() + 10;
				}
				if($swipe.length > 0){
					th += $swipe.height();
				}
				if($prog.length > 0){
					th += $prog.height() + 10;
				}
				if($prof.length > 0){
					th += $prof.height() + 10;
				}
				if($apply.length > 0){
					th += $apply.height() + 30;
				}
				if($sdtHead.length > 0){
					th += $sdtHead[0].offsetHeight + 10;
				}
				$('#pageScroll').scroll(function() {
					if($(this).scrollTop() > th) {
						$(".content_tabs").addClass("tabs_add");
						$('.pos').show();
					} else {
						$(".content_tabs").removeClass("tabs_add");
						$('.pos').hide();
					}
				});
			},250);
		},
		bindEvent:function(){
			var that = this;
			$('body').on('click','#list_4 .info img',function(){
				that.previewImg($('#list_4 .info img'),$(this));
				
			}).on('click','#bigImg',function(){
				$(this).hide().find('.bd ul').remove();
				
			}).on('click','#paybtn',function(){
				var userId = $("#userId").val(),browser = $("#browser").val();//判断是否已登录
				if((userId == null || userId == '')&& browser!='wx'){
					$('#loginCue').show();
				}else{
					that.queryRedPackets();		//红包
					$('#payTips').show();
				}
			}).on('click','#cue_fr',function(){
				that.queryRedPackets();//红包
				$('#loginCue').hide();
				$('#payTips').show();
				
			}).on('click','#cue_fl',function(){
				location.href = that.loginUrl;
					
			}).on('click','#closeDetail',function(){
				$('#payTips').hide();
				
			}).on('click', '#redPaperClick', function() { //显示红包
				if($('#redPaperList').height() == 0){
					$('#redPaperList').height('auto');
					$(this).text('点 击 红 包 捐 献 爱 心');
				}else{
					$('#redPaperList').height(0);
					$(this).text('使 用 红 包 捐 献 爱 心');
					$('#redPaperList li.curTab').removeClass('curTab');
					if($('#onceDonate .on').length == 0){
						$('#onceDonate li:first-child').addClass('on');
					}
				}
			}).on('click','#redclose',function(){
				$('#redPackets').hide();
				$('#redPaperList li.on').removeClass('curTab');
				if($('#onceDonate li.on').length == 0){
					$('#onceDonate li:first-child').addClass('on');
				}
			}).on('click','#closeBtn',function(){
				$(".popup, .confirmBox").css("transform", "scale(0,0)").hide();
				
			}).on('click','#confirmBtn',function(){
				$(".popup, .confirmBox").css("transform", "scale(0,0)").hide();
				$('#payTips').hide();
				that.payRedpacket();
				
			}).on('click','#onceDonate .ppd-ul li',function(){
				$('#onceDonate .on').removeClass('on');
				$(this).addClass('on');
				if($('#redPaperList').height() > 0){
					$('#redPaperClick').click();
				}
				//红包样式处理
				$(".redPaperList li").removeClass("curTab");
				
			}).on('click','#money2',function(){
				$('#onceDonate li.on').removeClass('on');
				$(this).parent().addClass('on');
				if($('#redPaperList').height() > 0){
					$('#redPaperClick').click();
				}
				$(".redPaperList li").removeClass("curTab");
				
			}).on("click","#redPaperList li ",function(){ 
				$(this).addClass("curTab").siblings().removeClass("curTab");
				var amount=$("#redPaperList li.curTab a").eq(0).attr("m");
				$('#payamount').html(amount);
				$('#onceDonate .on').removeClass('on');
				//红包支付
				$(".confirmBox").show();
				$(".popup, .confirmBox").show().css("transform", "scale(1,1)");
				
			}).on('click','#payMoney_promptConfirm',function(){
				$("#payMoney_prompt").hide();
				
			}).on('click','#leaveWordSubmit',function(){
				var content = $('#content').val();
				if(content==''){
					that.showTips('请输入评论！');
					return false;
				}
				that.verify();
				
			}).on('click','.end_foot .fr',function(){
				$(".moved_up").hide();
				
			}).on('click','#btn_submit',function(){
				if($(this).parent().hasClass('disable'))return;
				that.paySubmit(true,'');
				
			}).on('click','#rightNowPay',function(){
				that.paySubmit(false,'');
				
			}).on('click','#goodLibraryPay',function(){
				that.paySubmit(false,'http://www.17xs.org/WapAlipay/optPayWay.do');
				
			}).on('click','#openDayDonate',function(){
				if($(this).parent().hasClass('disable'))return;
				var money=$('#dayDonate .ppd-ul li.on span').attr('t'),
					money2=$("#dMoney").val(),
					paymoney = $('#payMoneyNum').text().replace('元',''),
					projectId=$("#projectId").val(),
					needmoney=$("#needmoney").val(),
					userId = $("#userId").val(),
					userName=$('#nickName').val(),
					dayNum = $('#xsDays').val().replace('天',''), 
					tel = $('#tel_day').val(),
					extensionPeople=$('#extensionPeople').val(),
					urls=dataUrl.view+"?projectId="+projectId+"&extensionPeople="+extensionPeople;
				//判断登录
				$.ajax({
					url:dataUrl.wxLogin,
					type:'GET',
					data:{projectId:projectId,extensionPeople:extensionPeople,url:urls},
					success:function(res){
						if(res.errorCode=='0000'){//已登录
							if((money <= 0 || !money) && money2){
								money = Number(money2);
								if(money <= 0 || isNaN(money)){
									$('#payMoney_content').html("捐款金额不低于0.01元");
									$('#payMoney_prompt').show();
									return false;
								}
							}
							if(tel.length != 11){
								$('#payMoney_content').html("请填写正确的电话号码");
								$('#payMoney_prompt').show();
								return false;
							}
							var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
							if (!reg.test(tel)) {
								$('#payMoney_content').html("请填写正确的电话号码");
								$('#payMoney_prompt').show();
								return false;
							}
					    	$.ajax({
					    		url: 'http://www.17xs.org/user/addDonateNewTime.do',
					    		dataType: 'json',
					    		type: 'post',
					    		data: {
					    			type:0,
					    			category:'field',
					    			notice:0,
					    			mobileNum:tel,
					    			money:money,
					    			projectIds:projectId,
					    			dayNumber:dayNum
					    		},
					    		success: function(result) {
					    		   if(result.errorCode=='0000'){//余额充足
					    			   //捐出一笔
					    			   $.ajax({
					    				   url:'http://www.17xs.org/uCenterProject/dayDonate.do',
					    				   type:'POST',
					    				   data:{projectId:projectId},
					    				   success:function(res){
					    					   if(res.code==1){
					    						   location.href='http://www.17xs.org/user/getMyDonateTime.do';
					    					   }else if(res.code==0){//未登录
					    						   location.href = that.loginUrl;
					    					   }
					    				   }
					    			   });
					    			}else if(result.errorCode=='0007'){//余额不足 充值
					    				location.href = '/visitorAlipay/tenpay/rechargedeposit.do?slogans=dayDonate_' + 
					    					userId+'_'+projectId+'&amount='+paymoney+'&touristName='+userName+'&touristMobile='+tel;
					    			}else if(result.errorCode=='0001'){//未登录
				    					location.href = that.loginUrl;
					    			}
					    		}				
					    	});
						}else if(res.errorCode=='0001'){//跳转衔接
							location.href = res.errorMsg;
						}
					}
				});
			}).on('click','.aload',function(){
				if($(this).html() == '没有更多数据了'){return;}
				h5detail.delRecordList(++page,10);
				
			}).on('click','#share',function(){
				var pTle=$('#pprim').val();
				that.share1(pTle);
				
			}).on("click",".qqim",function(){ 
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
				
			}).on("change","#payTips :checkbox",function(){ 
				if(!this.checked){
					$(this).closest('.box').find('.btn').addClass('disable');
				}else{
					$(this).closest('.box').find('.btn').removeClass('disable');
				}
			}).on('click','#xiangq,#dongt,#jil',function(){		//选项卡切换
				that.scrollPage(this);
				
			});
		},
		paySubmit:function(flag,u){
			var that = this;
			var money = 0;
			var $on = $('#onceDonate').find(".on");
			if($on.find('span').length > 0){
				money = parseInt($on.find('span').text());
			}else if($on.find('input').length > 0){
				money = parseInt($on.find('input').val());
			}
			var projectId=$("#projectId").val(),
				needmoney=$("#needmoney").val(),
				information=$("#information").val();
			if(information==''){
				information=$('#information').attr('placeholder');
			}
			$("#donateWord").val(information);
			// 增加红包支付的校验>>start
			if(money>0){}
			else{
				var amountHb=$(".redPaperList li.curTab a").eq(0).attr("m");
				if(amountHb > 0){
					$('#payamount').html(amountHb);
					//红包支付
					$(".popup, .confirmBox").show().css("transform", "scale(1,1)");
					return ;
				}
			}
			// 增加红包支付的校验>>end
			
			var paymoney = Number(money),
				pName = $("#pprim").val(),
				extensionPeople = $('#extensionPeople').val();
			if(isNaN(paymoney)){
				$('#payMoney_content').html("捐款金额不低于0.01元");
				$('#payMoney_prompt').show();
				return;
			}
			var userId = $("#userId").val(), 
				goodLibrary = $('#goodLibrary').val(),
				uu = u!=''?u:'http://www.17xs.org/visitorAlipay/tenpay/deposit.do';
			if(paymoney <= 0){
				money = $("#money2").val();
				if(money < 0.01){
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
					$('#payTips').hide();
					if(flag){
						if((userId == null || userId == '') || goodLibrary == 0){
							location.href = uu + "?projectId="
								+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information+"&slogans="+$('#slogans').val();
						}else if(goodLibrary == 1){
							$('#goodLibrary_prompt').show();
						}	
					}else{
						location.href = uu + "?projectId="+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information+"&slogans="+$('#slogans').val();
					}
				}else{
					$('#payMoney_content').html("请输入数字");
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
				$('#payTips').hide();
				if(flag){
					if((userId == null || userId == '') || goodLibrary == 0){
						location.href = uu + "?projectId="
							+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information+"&slogans="+$('#slogans').val();
					}else if(goodLibrary == 1){
						$('#goodLibrary_prompt').show();
					}	
				}else{
					location.href = uu + "?projectId="+projectId+"&amount="+money+"&extensionPeople="+extensionPeople+"&donateWord="+information+"&slogans="+$('#slogans').val();
				}
			}
		},
		previewImg:function(objs,obj){
			var ua = $('#browser').val();
			if(ua == 'wx'){
				var imgArray = [],
		        	curImageSrc = obj.attr('src'),
		        	href = obj.parent().attr('href'),
		        	hasHref = !href || href=='javascript:;' || href=='javascript:void(0);';
		        if (curImageSrc && hasHref) {
		            objs.each(function(index, el) {
		                var itemSrc = $(el).attr('src');
		                imgArray.push(itemSrc);
		            });
		             wx.previewImage({
		                current: curImageSrc,
		                urls: imgArray
		            });
		        }
			}else{
				var html = [];
				html.push('<ul>');
				html.push('<li><img src="'+obj[0].src+'" /></li>');
				html.push('</ul>');
				$('#bigImg .bd').append(html.join(''));
				var winh = $(window).height();
				$('#bigImg').show();
				$('#bigImg').find('.bd li').each(function(){
					var h = $(this).height();
					$(this).css({'margin-top':(winh - h) / 2 + 'px'});
				});	
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
						if(result.obj.data.length==0){
							$(".aload").html("");
						}else if(result.obj.data.length<10){
							$(".aload").html("没有更多数据了").css({'color':'#999'});
						}
						if(result.obj.data.length>0){
							var data=result.obj,total=data.total,pageNum=data.pageNum,datas=data.data,len=datas.length,html=[];
							len=len>pageNum?pageNum:len; 
							var html=[];
							//接受留言信息
							var leavewords=result.obj1;
							for(var i=0;i<len;i++){
								html.push('<a href="javascript:;">');
								html.push('<div class="item record"><span class="thumb">');
								if(datas[i].imagesurl != null ){
									html.push('<img osrc="'+datas[i].imagesurl+'" class="fl" src="'+datas[i].imagesurl+'"></span>');
								}else{
									html.push('<img osrc="'+base+'res/images/detail/people_avatar.jpg" class="fl" src="'+base+'res/images/detail/people_avatar.jpg"></span>');
								}
								html.push('<div class="name"><p class="name_p1">'+$.ellipsis(datas[i].name,7,'..')+'</p>');
								html.push('<p class="name_p2">');
								if(datas[i].field=='enterpriseUsers'){
									html.push('企业捐');
								}else if(datas[i].field=='monthlyDonation'){
									html.push('月捐');
								}else if(datas[i].field=='daylyDonation'){
									html.push('日捐');
								}else if(datas[i].field=='weRunDonation'){
									html.push('运动捐');
								}else if(datas[i].field=='enterpriseDonation'){
									html.push('配捐');
								}else{
									html.push('捐');
								}
								
								html.push('<label>￥'+$.formatNum(datas[i].dMoney)+'</label></p></div>');
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
    							}else{
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
			});
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
							if(datalist[i].source == 'home'){
								html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span></div>');
							}
							else if(datalist[i].source == 'admin'){
								html.push('<div class="title"><span class="name">善园基金会</span></div>');
							}else{
								if(datalist[i].uName == null){
									html.push('<div class="title"><span class="name">善园基金会</span></div>');
								}
								else{
									html.push('<div class="title"><span class="name">'+datalist[i].uName+'</span></div>');
								}
							}
							html.push('<div class="info">');
							if(imgs&&imgs.length>0){
								html.push(' <p class="info_text">'+datalist[i].content+'</p><div style="clear: both"></div>');
								temp+="";
								for(var j=0;j<imgs.length;j++){
									temp+=('<p><a href="javascript:;><img src="'+imgs[j]+'" /></a></p>');
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
							}else{
								html.push('<span class="more'+i+'  danote1" onclick="loadMore('+i+','+datalist[i].id+','+0+')">查看更多</span>');
							}	
							html.push('</div></div></div>');
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
									$('.more'+k).hide();
								}
							}
							$("#list_4").show();
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
			var that = this;
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
							if(i == 0){
								html.push('<li>');
								html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
								html.push('<i>'+data.amount+'</i>元</a></li>');
							}else{
								html.push('<li>');
								html.push('<a href="javascript:;" class="payitem" v="'+data.id+'" m="'+data.amount+'">');
								html.push('<i>'+data.amount+'</i>元</a></li>');
							}
						}
						$('#redPaperList').html(html.join('')).height(0);
						if($('#dayDonatePage').length == 0){
							$('#redPackets').show();
						}else{
							hasRed = true;
						}
					}else{
						hasRed = false;
					}
				},
				error : function(result) {}
			});
		},
		payRedpacket:function(){
			var title = $("#pprim").val(),
				money = $(".redPaperList li.curTab a").eq(0).attr("m"),
				rId=$(".redPaperList li.curTab a").eq(0).attr("v"),
				projectId = $("#projectId").val();
			$.ajax({
				url: dataUrl.payRedpacket,
				cache   : false,
				type: "POST",
				data: {uredId:rId,projectId:projectId,t:new Date().getTime()},
				success : function(result) { 
					if(result.flag==1){
						location.href="http://www.17xs.org/project/paysuccess_h5/?pName="
							+title+"&amount="+money+"&red=1&projectId="+projectId+"&tradeNo="+result.obj.data;
					}
				}
			});
		},
		//提交评论
		verify:function(){
			var that=this,
				projectId=$('#projectId').val(),
				projectDonateId=$('#projectDonateId').val(),
				projectFeedbackId=$('#projectFeedbackId').val(),
				leavewordUserId=$('#leavewordUserId').val(),
				replyUserId=$('#replyUserId').val(),
				leavewordName=$('#leavewordName').val(),
				replyName=$('#replyName').val(),
				content=$('#content').val(),
				index=$("#index").val();
			if(content==''){
				that.showTips('请输入评论！');
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
				data:{
					projectId:projectId,
					projectDonateId:projectDonateId,
					projectFeedbackId:projectFeedbackId,
					leavewordUserId:leavewordUserId,
					replyUserId:replyUserId,
					leavewordName:leavewordName,
					replyName:replyName,
					content:content
				},
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
										that.showTips(result.errorMsg);
										return;
									}
								}
							});
						}else if(projectDonateId!=null && projectFeedbackId==null && projectDonateId!=""){
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
											}else{
											   //$('.danoteMore'+index).html("查看更多");
												html.push('<span  class="danoteMore'+index+' danote1" onclick="donateLoadMore('+index+','+projectDonateId+','+0+')">查看更多</span>');
											}
										}
										//追加数据
										$('.message_listj'+index).html(html.join(''));
										$('.message_listj'+index).css("background","#f3f3f3");
									} else {//失败
										that.showTips(result.errorMsg);
										return;
									}
								}
							});
						}
					}else if(result.errorCode == "0001"){//未登录，账号密码登录
						that.showTips('您还未登录！');
						setTimeout(function(){
							location.href = '/ucenter/user/Login_H5.do?flag=special_'+projectId+'_extensionPeople_'+$('#extensionPeople').val();
						},2000);
					}else{//失败
						that.showTips(result.errorMsg);
						return ;
					}
				}
			});
		},
		showTips:function(txt){
			var $msg = $('#msg');
			if(!$msg.is(':hidden')){
				$msg.hide().html('');
			}
			$msg.html(txt).fadeIn();
			setTimeout(function(){
				$msg.fadeOut();
			},2000);
		}
	};
	 
	h5detail.init();
	
});
