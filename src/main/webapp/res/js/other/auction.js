var base=window.baseurl;
var dataUrl={
	projectList:base+'project/gylist.do',//竞拍列表页
	realname:base+'project/gyrealname.do',//实名
	auctionToproject:base+'project/gydoauction.do',
	auction:base+'project/gyauction.do',
	bidList:base+'project/gyauctiondata.do',//竞价列表
	SSO_MobileCode:base+'user/phoneCode.do',//验证码请求
	verify:base+'project/gyresult.do',//竞价列表
	pay:base+'visitorAlipay/tenpay/jpPay.do',//支付提交
	nowtime:base+'project/nowtime.do'//当前时间
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
	urlArgs:"v=20150515"
});

define(["extend","dialog","head","entry","userCenter","ajaxform","pages","pageCommon"],function($,d,h,en,uc,f,p,p1){
	h.init();
	d.init();
	en.init(); 
	uc.init();
	var wait=60,click=60,countdown=60;
	p.init({afterObj:$("#pageNum")});
	p.changeCallback=function(){
		h5detail.delRecordList(p.curPage,10);
	};
	uc.selectCallback=function(){
		h5detail.delRecordList(1,10);
	};
	var auction={
		init:function(){
			var that=this;
			$('#min').click(function(){
				var price=$('#text_box').val();
				price = (parseFloat(price)-parseFloat(10)).toFixed(2);
				if(price <= 0){
					d.alert({content:"不能小于0!",type:'error'});
					return;
				}
				$('#text_box').val(price);
			});
			
			$('#add').click(function(){
				var price=$('#text_box').val();
				price = (parseFloat(price)+parseFloat(10)).toFixed(2);
				$('#text_box').val(price);
			});
			
			$('#auction_submit').click(function(){
				d.alert({content:"您确定参与此作品的竞拍",type:'ok',jp:'1',okFn:function(){ that.auctionToproject();}});
			});
			$('#auction_submit_pay').click(function(){
				var id=$('#auctionId').val();
				window.location.href = dataUrl.pay+"?id="+id;
			});
			$('#submit').click(function(){
				that.verify();
			});
			$('#more').click(function(){
				var pageNum = $('#pageNum').val();
				that.bidList(1,parseInt(10)+parseInt(pageNum));
			});
			$('#pSend').click(function(){
				var phone = $('#phone').val(),msgObj=$('#phoneE');
				var c=en.checkMobileStr(phone);
				if(!c){
					msgObj.html("您填写的手机号码格式错误~");
					return false;
				}
				that.sendcode($(this),'#phone','#wrcode','#phoneE',1);
				if(countdown != 60){return;}
				that.timeOut(this);
			});
			that.countDown('#divdown1');
			window.setInterval(function(){that.countDown('#divdown1');}, 1000);
			that.bidList(1,10);
		},
		auctionToproject:function(){
			var price=$('#text_box').val(),auctionId=$('#auctionId').val();
			$.ajax({
				url:dataUrl.auctionToproject,
				data:{id:auctionId,price:price,t:new Date().getTime()},
				success: function(result){
					if(result.flag == 1){
						d.alert({content:"竞拍成功!",type:'ok',okFn:function(){ window.location.href = dataUrl.auction+"?id="+auctionId;}});
					}else if(result.flag == -1){
						d.alert({content:"请先授权登录!",type:'error',okFn:function(){ location.reload();}});
						//window.location.href = dataUrl.projectList;//竞拍列表页
					}else if(result.flag == 0){
						if(result.errorCode == 1023){//未实名
							 window.location.href = dataUrl.realname+"?auctionId="+auctionId;
						}else if(result.errorCode == 1022){//未实名
							d.alert({content:"竞拍结束!",type:'error',okFn:function(){ location.reload();}});
						}else if(result.errorCode == 1021){
							d.alert({content:"价格过低!",type:'error',okFn:function(){ window.location.href = dataUrl.auction+"?id="+auctionId;}});
						}else {
							window.location.href = dataUrl.auction+"?id="+auctionId;
						}
					}
				}
			});
		},
		bidList:function(page,pageNum){
			var auctionId=$('#auctionId').val(),size=pageNum;
			$.ajax({
				url:dataUrl.bidList,
				data:{id:auctionId,page:page,pageNum:pageNum,t:new Date().getTime()},
				cache:false,
				success: function(data){
					if(!!data){
						data = eval(data); 
						if(data.flag==1){
							var total=data.obj.total,pageNum=data.pageNum,items=data.obj.resultData,len=items.length,html=[];
							html.puslen=len>pageNum?pageNum:len;
							html.push('<dt><li>状态</li><li>竞买号</li><li>价格</li><li>时间</li></dt>');
							for(var i=0;i<len;i++)
							{
								var _item=items[i];
								if(i%2 != 0){
									html.push('<dd class="record_dd">');
								}else{
									html.push('<dd>');
								}
								if(i == 0){
									if(_item.state == 202){//已到期
										html.push('<li><span id="record_span3"></span></li>');
									}else if(_item.state == 204){//已支付
										html.push('<li><span id="record_span3"></span></li>');
									}else if(_item.state == 203){//流标
										html.push('<li><span id="record_span2"></span></li>');
									}else{
										html.push('<li><span id="record_span1"></span></li>');
									}
								}else{
									html.push('<li><span></span></li>');
								}
								html.push('<li><a>'+_item.mobileNum.substring(0,3)+"****"+_item.mobileNum.substring(7,11)+'</a></li>');
								html.push('<li class="record_li">￥'+_item.price+'</li>');
								html.push('<li>'+(new Date(_item.createtime)).pattern("MM-dd HH:mm:ss")+'</li>');
								html.push('</dd>');
							}  
							$('#record').html(html.join(''));
							$('#pageNum').val(len);
							var htmlMore=[];
							if(total > size){
								htmlMore.push('<a href="javascript:void(0)"alt="" class="center_5_1"><p>点击加载更多</p>');
								htmlMore.push('<span></span></a>');
								$('#more').html(htmlMore.join(''));
							}else{
								$('#more').html(htmlMore.join(''));
							}
						}
					}
				}
			});
			
		},
		verify:function(){
			var that=this,realname=$('#uName').val(),phone=$('#phone').val(),auctionId=$('#auctionId').val(),
				code=$('#code').val(),msgphone=$('#phoneE'),msguName=$('#uNameE'),msgcode=$('#codeE');
			msguName.html('');
			msgphone.html('');
			msgcode.html('');
			if(!realname){
				msguName.html("您填写的姓名错误~");
				return ;
			}
			var c=en.checkMobileStr(phone);
			if(!c){
				msgphone.html("您填写的手机号码格式错误~");
				return;
			}
			var mc = that.isMobelCode($("#code"), 1);
			 if (!mc) {
				 msgcode.html("您填写的验证码错误~");
				 return ;
			}
			$.ajax({
				url:dataUrl.verify,
				data:{realname:realname,phone:phone,phoneCode:code,t:new Date().getTime()},
				success: function(result){
					if(result.flag == 1){
						d.alert({content:"添加成功!",type:'ok',okFn:function(){ window.location.href = dataUrl.auction+"?id="+auctionId;}});
					}else{
						msgcode.html("您填写的验证码错误~");
						return ;
					}
				}
			});
		},
		sendcode:function(obj,phoneobj,codeobj,phonecodeboj,type){
			var that=this;
			if(wait <60){ return false; }
			var phone = $(phoneobj).val(),msgObj=$(phonecodeboj),code='';
			var cause = "用户注册验证码";
			if(!type){
				var codes=that.isCode($(codeobj),0);
				if(!codes){return;} 
			}else{
				var c=en.checkMobileStr(phone);
				if(!c){
					msgObj.html("您填写的手机号码格式错误~");
					return false;
				}
				types="certification";
			}
			$.ajax( {
				url: dataUrl.SSO_MobileCode,
				cache   : false,
				type: "POST",
				async: false,
				data: {code:code,phone:phone,cause:cause,type:types},
				success : function(result) { 
						if(result.flag==1){
							msgObj.html('手机验证码发送成功，请查收！');
							return true;
						}else{
							msgObj.html('发送失败，请稍后重试。');
							 $(phoneobj).focus();
							 return false;
						}
				},
				error : function(result) { 
					msgObj.html('发送失败，请稍后重试。');
				}
			});
		},
		isMobelCode:function(obj,type){
			var nameObj=$(obj),nameVal = nameObj.val(),msgObj=$("#codeE");
			var len = nameVal.cnLength(); 
			var reg=/^\d{6}$/;
			if(reg.test(nameVal)){
				return true;	
			}else{
				return false;
			}
			
		},
		countDown:function(divname){
					var that = this;
					var time = $("#nowtime").val();
					var endtime = $("#endtime").val();
					if (endtime - time > 0) {
						if (click > 0) {
							var time = $("#nowtime").val();
							$("#nowtime").val(parseInt(time) + parseInt(1000));
							that.timeView(time, divname);
							click -= 1;
						} else {
							$.ajax({
								url : dataUrl.nowtime,
								cache : false,
								type : "POST",
								data : {
									t : new Date().getTime()
								},
								success : function(result) {
									if (result.flag == 1) {
										var now = result.obj;
										that.timeView(time, divname);
										$("#nowtime").val(parseInt(now));
										click = 60;
									}
								},
								error : function(result) {
									msgObj.html('发送失败，请稍后重试。');
								}
							});
						}
					} else {
						$(divname).html("0天0小时0分0秒");
					}
		},
		timeView:function(now,divname){
	            var endDate = $("#endtime").val();
	            var leftTime=endDate-now;
	            var leftsecond = parseInt(leftTime/1000);
	            var day1=Math.floor(leftsecond/(60*60*24));
	            var hour=Math.floor((leftsecond-day1*24*60*60)/3600);
	            var minute=Math.floor((leftsecond-day1*24*60*60-hour*3600)/60);
	            var second=Math.floor(leftsecond-day1*24*60*60-hour*3600-minute*60);
	            $(divname).html(day1+"天"+hour+"小时"+minute+"分"+second+"秒");
		},
		timeOut:function(obj){
			var that =this;
			if (countdown == 0) { 
					 countdown = 60;   
					 $('#pSend').html('发送验证码');
			         return;   
			} else { 
				obj.setAttribute("disabled", true);
			  $('#pSend').html('重新发送('+countdown+')');
		            countdown--;   
				  } setTimeout(function() { that.timeOut(obj);} ,1000);
		}
	};
	auction.init();
	
});