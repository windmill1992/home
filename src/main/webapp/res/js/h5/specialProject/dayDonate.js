/*日日捐*/
$(function(){
	$('.entered li:gt(5)').remove();
	var $box = $('.pay .box .content').eq(0);
	if($box.height() > 240){
		$('.mask-bg').show();
		$box.css({'padding-bottom':'20px'});
	}
	/*点击查看全文*/
	setTimeout(function(){
		var oldH = $(".xiangqing").height();
		if(oldH > 450) {
			$(".xiangqing_box").addClass("addp");
			$(".detail-mask").show();
			$('.xiangqing').height(450);
		} else {
			$(".xiangqing_box").removeClass("addp");
			$(".detail-mask").hide();
		}
	},100);
	
	$(".last_btn").click(function() {
		if($(this).text() == "查看全文") {
			$(this).text("收起");
			$(".xiangqing_box").removeClass("addp");
			$(".detail-mask").removeClass("detail-mask-bg");
			$(".xiangqing").css({height:'auto'});
		} else {
			$(this).text("查看全文");
			$(".xiangqing_box").addClass("addp");
			$(".detail-mask").addClass("detail-mask-bg");
			$(".xiangqing").height(450);
			$('#a')[0].scrollIntoView();
		}
	});
	$('#donateTab .tab').on('click', function() {
		$(this).addClass('active').siblings().removeClass('active');
		$('#payTips .box').removeClass('show').eq($(this).index()).addClass('show');
		if($(this).index() == 0){
			$('#redPackets').hide();
			$('#redPaperList').height(0);
		}else{
			if(hasRed){
				$('#redPackets').show();
			}else{
				$('#redPackets').hide();
			}
		}
	});
	$('#dayDonate .ppd-ul li').on('click', function() {
		$('#dayDonate .ppd-ul li').removeClass('on');
		$(this).addClass('on');
		$('#dMoney').val('').parent().removeClass('on');
		var num = 1;
		var days = parseInt($('#xsDays').val());
		var s = $(this).find('span');
		num = s.text();
		$('#payMoneyNum').text(num + '元');
		getSum();
	});
	$('#minus').on('click', function() {
		var day = parseInt($(this).next().val());
		if(day <= 10) {
			return;
		} else {
			day--;
			$(this).next().val(day + '天');
		}
		getSum();
	});
	$('#add').on('click', function() {
		var day = parseInt($(this).prev().val());
		day++;
		$(this).prev().val(day + '天');
		getSum();
	});
	$('#xsDays').on('focus', function() {
		var days = parseInt($(this).val());
		$(this).val(days);
	}).on('blur', function() {
		var days = parseInt($(this).val());
		if(days && !isNaN(days) && days >= 10) {
			$(this).val(days + '天');
		} else {
			$(this).val('10天');
		}
		getSum();
	});
	$('#dMoney').on('focus', function() {
		$('#dayDonate li.on').removeClass('on');
		$(this).parent().addClass('on');
		var value = $(this).val();
		if(isNaN(parseFloat(value))) {
			value = value.split('.')[0];
			$(this).val(value);
		} else {
			$(this).val(value);
		}
		getSum();
	}).on('blur', function() {
		var value = $(this).val();
		if($(this).parent().hasClass('on')) {
			if(value) {
				if(isNaN(parseFloat(value))) {
					value = value.split('.')[0];
					$(this).val(value);
				} else {
					value = value > 1?value:1;
					$(this).val(value);
				}
			} else {
				$(this).val(1);
			}
			getSum();
		} else {
			$(this).val('');
		}
	}).on('keyup', function() {
		var v = $(this).val();
		if(v>1 && !isNaN(v)) {
			num = v;
		} else {
			num = 1;
		}
		$('#payMoneyNum').text(num + '元');
		getSum();
	});

	function getSum() {
		var o = $('#dayDonate .on');
		var s = o.find('span');
		var i = o.find('input');
		var num = 0;
		if(s.length > 0) {
			num = parseInt(s.text());
		} else if(i.length > 0) {
			num = parseFloat(i.val()).toFixed(2);
			if(isNaN(num) || num < 2) {
				num = 1;
			}
		}
		var days = 0;
		days = parseInt($('#xsDays').val());
		$('#payMoneyNum').text(parseFloat(num * days).toFixed(2) + '元');
	}
	getSum();
	
});
