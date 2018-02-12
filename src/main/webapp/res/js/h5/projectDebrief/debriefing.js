/*
 * 2017-7-27
 * 
 */

var proDebrief = {
	title:$('title').html(),
	init:function(){
		this.bindEvent();
		var that = this;
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','.pp-content .imgList img',function(){
			var len = $('.pp-content .imgList img').length;
			var html = [];
			var _this = $(this).parent();
			html.push('<ul>');
			for(var i=0;i<len;i++){
				html.push('<li><img src="'+$('.pp-content .imgList img').eq(i).attr('src')+'" width="100%"/></li>')
			}
			html.push('</ul>');
			that.showBigPic(html,_this.index(),len);
		}).on('click','.bigPic',function(){
			$('#overLay').hide();
			$(this).fadeOut();
			$('.bigPic .bd').html('');
			that.updateWxTitle();
			$('title').html(that.title);
			TouchSlide(null);
		}).on('click','#donateBtn1,.toDonate',function(){
			//判断项目已结束
			var state=$('#state').val();
			if(state==240){
				$('#payBox').fadeIn();
			}
		}).on('click','#payBox .close',function(){
			$('#payBox').fadeOut();
		}).on('click','#pay_donate .money-ul li',function(){
			$(this).siblings().removeClass('on');
			$('.otherMoney').removeClass('on');
			$(this).addClass('on');
		}).on('click','#pay_donate .otherMoney',function(){
			$(this).addClass('on');
			$('#pay_donate .money-ul li.on').removeClass('on');
			$('#diyMoney').focus();
		}).on('click','#btn_submit',function(){
			var word = $('#payBox .word').text();
			var money = 0;
			if($('#pay_donate .money-ul li.on').length>0){
				money = Number($('#pay_donate .money-ul li.on span').text());
			}else if($('#pay_donate .otherMoney.on').length>0){
				money = parseFloat($('#diyMoney').val()).toFixed(2);
			}
			var check = $('.prot :checked').length;
			if(money<=0){
				that.showTips('捐款金额不正确！');
				return;
			}
			if(check<1){
				that.showTips('请同意善园用户协议！');
				return;
			}
			
			//支付
			var money=$(".on").find('span').text(),money2=$('#diyMoney').val(),projectId=$("#projectId").val(),needmoney=$("#needmoney").val(),userId = $("#userId").val()
			,realName=$("#realName").val(),mobileNum=$("#mobileNum").val();donateWord=$("#donateWord").val();
			if(donateWord==''){
				donateWord=$('#donateWord').attr('placeholder');
			}
			
			if(realName==undefined){
				realName='';
			}
			if(mobileNum==undefined){
				mobileNum='';
			}
			if(typeof(money)!=undefined&&money>0){
				
			}else{
				money=money2;
			}
			
			var paymoney = Number(money),pName = $(".flex12 h3").text();
			
			if(isNaN(paymoney)){
				that.showTips('请输入正确的金额！');
				return;
			}
			if(money < 0.01){
				that.showTips('请输入正确的金额！');
				return;
			}
			money = Number(money);
			if(!isNaN(money)){
				if(needmoney < money){
					that.showTips("最多捐款金额"+needmoney+"元");
					return;
				}
				window.location.href="http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId="
					+projectId+"&amount="+money+"&realName="+realName+"&mobileNum="+mobileNum+"&donateWord="+donateWord;
			}else{
				that.showTips('请输入数字！');
				return;
			}
			
		}).on('click','.toShare',function(){
			$('#overLay').show();
			$('.tishi').show().animate({'top':0},'swing');
		}).on('click','#progress-list .picList-img img',function(){
			var len = $(this).parents('li').find('.picList-img').length;
			var html = [];
			var _this = $(this).parent().parent();
			html.push('<ul>');
			for(var i=0;i<len;i++){
				html.push('<li><img src="'+_this.parent().find('.picList-img').eq(i).find('img').attr('src')+'" width="100%"/></li>')
			}
			html.push('</ul>');
			that.showBigPic(html,_this.index(),len);
		}).on('click','.reportContent .picList-img img',function(){
			var len = $(this).parents('.reportContent').parent().find('.picList-img').length;
			var html = [];
			var _this = $(this).parent().parent();
			html.push('<ul>');
			for(var i=0;i<len;i++){
				html.push('<li><img src="'+_this.parent().find('.picList-img').eq(i).find('img').attr('src')+'" width="100%"/></li>')
			}
			html.push('</ul>');
			that.showBigPic(html,_this.index(),len);
		}).on('click','#overLay',function(){
			$(this).hide();
			$('.tishi').animate({'top':'-50px'},'swing',function(){
				$(this).hide();
			});
		});
		
		$(document).on('scroll',function(){
			if ($(this).scrollTop()>100) {
				$('.bot-btn').addClass('show');
			} else{
				$('.bot-btn').removeClass('show');
			}
		});
		if($('.bigPic').length>0){
			$('.bigPic')[0].addEventListener('touchmove',function(event){event.preventDefault()},false);
		}
	},
	showTips:function(text){
		if(!$('.tips').is(':hidden')){return;}
		$('.tips').html(text).show().animate({'bottom':'100px'},'swing',function(){
			var _this = $(this);
			setTimeout(function(){
				_this.fadeOut(function(){
					_this.css({'bottom':'-50px'});
				})
			},2000);
		});
	},
	showBigPic:function(htmls,index,len){
		var that = this;
		$('#overLay').show();
		var h = $(window).height();
		$('.bigPic .bd').html(htmls.join(''));
		$('.bigPic').fadeIn();
		that.updateWxTitle();
		$('title').html(index+1+'/'+len);
		TouchSlide({
			slideCell:"#banner",
			titCell:".hd ul",
			mainCell:".bd ul",
			effect:"left",
			autoPlay:false,
			autoPage:true,
			defaultIndex:index,
			endFun:function(i,c){
				that.updateWxTitle();
				$('title').html((i+1)+'/'+c);
			}
		});
		$('#banner .bd img').each(function(){
			var h2 = $(this).height();
			$(this).css({'margin-top':(h-h2)/2+'px'});
		});
	},
	updateWxTitle:function(){
		var $iframe = $('<iframe src="//m.baidu.com/favicon.ico" style="display:none;"></iframe>');
		
		$iframe.on('load',function() {
		  setTimeout(function() {
		      $iframe.off('load').remove();
		  }, 0);
		}).appendTo($('body'));
	}
};

proDebrief.init();
