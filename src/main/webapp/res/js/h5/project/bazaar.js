var base = 'http://www.17xs.org/';

var material = {
	pid:'',
	init:function(){
		var that = this;
		var pid = that.GetQueryString('projectId');
		if(pid == null || pid == '' || isNaN(pid)){
			$('#pageContainer').hide();
			$('#nopage').show();
		}else{
			that.pid = pid;
			var $size = $('#materialList .size');
			$size.each(function(){
				var txt = $(this).text().trim();
				$(this).attr('data-intro',txt); 
				var str = txt.replace('&nbsp;',' ');
				if(str.length > 15){
					$(this).text(str.substr(0,15)+'...');
				}	
			});
			
			that.bindEvent();
		}
	},
	bindEvent:function(){
		var that = this;
		$('body').on('click','.add',function(){
			var $num = $(this).prev();
			var n = parseInt($num.text());
			n++;
			$num.text(n);
			that.getSum();
			$('.diy-money').hide();
		}).on('click','.minus',function(){
			var $num = $(this).next();
			var n = parseInt($num.text());
			if(n == 0){
				return;
			}
			n--;
			$num.text(n);
			that.getSum();
			$('.diy-money').hide();
		}).on('click','#otherOp .donate-num',function(){
			var $num = $('.item .num');
			var n = $(this).attr('data-num');
			$num.text(n);
			that.getSum();
			$('.diy-money').hide();
		}).on('click','#otherOp .random',function(){
			var $num = $('.item .num');
			var n = Math.floor(Math.random() * $num.length);
			$num.text(0).eq(n).text(1);
			that.getSum();
			$('.diy-money').hide();
		}).on('click','#otherOp .other-money',function(){
			$('.diy-money').show();
			$('#dMoney').focus();
			$('#supportMoney').text('0.00');
			$('#support').parent().addClass('disable');
		}).on('input propertychange','#dMoney',function(){
			var num = $(this).val();
			if(num == '' || isNaN(num)){
				num = 0;
				$(this).val('');
				$('#support').parent().addClass('disable');
			}else if(num == 0){
				$('#support').parent().addClass('disable');
			}else{
				$('#support').parent().removeClass('disable');
			}
			num = parseFloat(num).toFixed(2);
			$('#supportMoney').text(num);
		}).on('click','#support',function(){
			if($(this).parent().hasClass('disable')) return;
			var money = Number($('#supportMoney').text()),
				pid = that.pid,
				gift = '',
				realName = $('#realName').val(),
				tel = $('#mobile').val(),
				word = $('#leaveWord').val(),
				$dm = $('#dMoney').parent(),
				reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
			if(tel && !reg.test(tel)){
				that.showTips('手机号格式不正确！');return;
			}
			if(word == ''){
				word = $('#leaveWord')[0].placeholder;
			}
			if($dm.is(':hidden')){
				gift = 'bazaar';
				$('.item').each(function(){
					var num = Number($(this).find('.num').text());
					if(num > 0){
						gift += '_' + $(this).attr('data-id') + '@' + num;
					}
				});
			}
			location.href = 'http://www.17xs.org/visitorAlipay/tenpay/deposit.do?projectId='+pid + 
				'&amount='+money+'&realName='+realName+'&mobileNum='+tel+'&donateWord='+word+'&slogans='+gift;
		}).on('click','.preview',function(){
			var $img = $(this).find('img'),
				$item = $(this).closest('.item');
			var t = $item.find('.title').text();
			var i = $item.find('.size').attr('data-intro');
			that.previewImg(t,i,$img);
		}).on('click','#bigImg',function(){
			$(this).hide().find('.bd ul').remove().end().find('.intro').html('');
		});
	},
	getSum:function(){
		var $sm = $('#supportMoney'),
			$item = $('#materialList .item'),
			sum = 0;
		$item.each(function(){
			var p = Number(parseFloat($(this).find('.p-num').text()).toFixed(2));
			var n = parseInt($(this).find('.num').text());
			sum += p * 100 * n / 100;
		});
		if(sum == 0){
			$('#support').parent().addClass('disable');
		}else{
			$('#support').parent().removeClass('disable');
		}
		sum = parseFloat(sum).toFixed(2);
		$sm.text(sum);
	},
	previewImg:function(t,i,obj){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger'){
			var imgArray = [];
			var curImgSrc = obj[0].src;
			imgArray.push(curImgSrc);
			wx.previewImage({
				current: curImgSrc,
				urls: imgArray
			});
		}else{
			var html = [];
			html.push('<ul>');
			html.push('<li><img src="'+obj[0].src+'" /></li>');
			html.push('</ul>');
			$('#bigImg .bd').append(html.join(''));
			$('#bigImg .intro').append('<p>'+t+'</p><p>'+i+'</p>');
			var winh = $(window).height();
			$('#bigImg').show();
			$('#bigImg').find('.bd li').each(function(){
				var h = $(this).height();
				$(this).css({'margin-top':(winh - h) / 2 - 50 + 'px'});
			});	
		}
	},
	showTips:function(txt){
		var $tips = $('#tips');
		$tips.text(txt).fadeIn();
		setTimeout(function(){
			$tips.fadeOut();
		},2000);
	},
	//获取url参数值
	GetQueryString:function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if(r != null)
			return decodeURI(r[2]);
		return null;
	}
};

$(function(){
	material.init();
});
