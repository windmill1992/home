var base=window.baseurl;
var dataUrl={
	addRedPackets:base+'redPackets/addRedpackets.do'
};

var h5RedPackets={
	timer:null,
	init:function(){
		var that = this;
		var total = $('#totalAmount').val();
		if(total > 0){
			$('#sendRedPackets .red_center4').removeClass('disable');
		}else{
			$('#sendRedPackets .red_center4').addClass('disable');
			total = '0.00';
		}
		$('#amount').html('￥'+total);
		$('#sendRedPackets').click(function(){
			if($(this).find('.disable').length > 0)return;
			that.addRedPackets();
		});
		
		$('#totalAmount').bind('input propertychange', function() {
			var total = $('#totalAmount').val();
			if(total > 0){
				$('#sendRedPackets .red_center4').removeClass('disable');
			}else{
				$('#sendRedPackets .red_center4').addClass('disable');
				total = '0.00';
			}
			$('#amount').html('￥'+total);
		});
		
		$('#closeRed').click(function(){
			$('#redpacketsSuccess').hide();
			window.location.reload();
		});
		
		$('#shareRed').click(function(){
			var redPacketsId = $('#redpacketsId').val();
			$('#sharePersonRed').show();
			location.href="/redPackets/getPersonRedPaket.do?id="+redPacketsId+"&type=110";
		});
	},
	addRedPackets:function(){
		var that = this,
			totalNum = Number($('#totalNum').val()),
			totalAmount = Number($('#totalAmount').val()),
			slogans = $('#slogans').val(),
			balance = Number($('#balance').val()),
			text = '';
		if(totalNum == '' || !that.numberCheck(totalNum)){
			that.showTips('请填写正确的红包个数！');
			return ;
		}
		var checkAmount = totalAmount/totalNum;
		if(checkAmount < 0.01){
			that.showTips('请填写正确的红包金额！');
			return ;
		}
		if(balance < totalAmount){
			location.href='/visitorAlipay/tenpay/rechargedeposit2.do?amount='
				+totalAmount+'&slogans='+slogans+'&pcount='+totalNum+'&payType='+4;
			return ;
		}
		$.ajax({
			url:dataUrl.addRedPackets,
			data:{totalnum:totalNum,totalamount:totalAmount,slogans:slogans,t:new Date().getTime()},
			success: function(result){ 
				if(result.flag==1){
					$('#redpacketsSuccess').show();
					var id = result.obj.data;
					$('#redpacketsId').val(id);
				}else if(result.flag==2){
					that.showTips('余额不足，请先充值！');
				}else{
					that.showTips('系统异常，请联系客服。');
				}
			}
		}); 
	},
	showTips:function(txt){
    	var $info = $('#info');
    	if(!$info.is(':hidden')){
    		$info.hide();
    		this.timer = null;
    	}
    	$info.html(txt).fadeIn();
    	this.timer = setTimeout(function(){
    		$info.fadeOut();
    	},2000);
    },
	numberCheck: function(value){
		return /(^\d+$)/.test(value);
	}
};

$(function(){
	h5RedPackets.init();
});

