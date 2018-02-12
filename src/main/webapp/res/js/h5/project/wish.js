var base = 'http://www.17xs.org/';
var dataUrl = {
	getOneAidHelpList: base +'uCenterProject/getOneAidHelpList.do'			//一对一愿望捐赠详情接口
};
var vm = new Vue({
	el:'#pageContainer',
	data:{
		hasMore:-1,
		wishList:[]
	}
});
var wishes = {
	page: 1,
	init:function(){
		var that = this;
		var pid = that.GetQueryString('projectId');
		if(pid == null || pid == '' || isNaN(pid)){
			$('#pageContainer').hide();
			$('#nopage').show();
		}else{
			vm.projectId = pid;
			that.getWishList(that.page,10);
			that.bindEvent();
		}
	},
	bindEvent:function(){
		var that = this;
		
		$('body').on('click','#wishList .down',function(){
			var $foot1 = $(this).closest('.item').find('.foot'),
				$p = $(this).prev();
			if($foot1.is(':hidden')){
				var $foot2 = $('#wishList .foot:not(:hidden)');
				$foot2.slideUp();
				$foot1.slideDown();
			}else{
				$foot1.slideUp();
			}
		}).on('click','#otherOp .donate-num',function(){
			var num = $(this).attr('data-num'),
				$checks = $('#wishList .chk'),
				len = $checks.length;
			if(num == 0){
				if($(this).hasClass('all')){
					$checks.each(function(){
						this.checked = false;
					});
				}else{
					$checks.each(function(){
						this.checked = true;
					});	
				}
			}else{
				var arr = that.getRandom(num,len);
				$checks.each(function(){
					this.checked = false;
				});
				for(var i=0;i<arr.length;i++){
					$checks.get(arr[i]).checked = true;
				}
			}
			that.getSum();
			
		}).on('change','#wishList .chk',function(){
			that.getSum();
			
		}).on('click','#loadmore',function(){
			that.getWishList(++that.page,10);
			
		}).on('click','#donate',function(){
			if($(this).hasClass('disable')) return;
			var money = Number($('#total').text()),
				realName = $('#realName').val(),
				tel = $('#mobile').val(),
				words = $('#leaveWords').val(),
				ids = [],
				reg = /^(13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\d{8}$/;
			if(tel && !reg.test(tel)){
				that.showTips('手机号格式不正确！');return;
			}
			ids.push('oneAid');
			$('#wishList .item').each(function(){
				if($(this).find(':checked').length > 0){
					ids.push($(this).attr('data-id'));
				}
			});
			if(words == ''){
				words = $('#leaveWords').attr('placeholder');
			}
			location.href = '/visitorAlipay/tenpay/deposit.do?projectId='+ vm.projectId + 
				'&amount='+ money +'&realName='+ realName +'&mobileNum='+ tel +'&donateWord='+ words +'&slogans='+ ids.join('_');
			
		});
	},
	getWishList:function(pageNum,pageSize){
		$.ajax({
			type:"get",
			url:dataUrl.getOneAidHelpList,
			data:{projectId:vm.projectId,pageNum:pageNum,pageSize:pageSize},
			success:function(res){
				if(res.code == 1){
					var total = res.result.count;
					vm.wishList = vm.wishList.concat(res.result.data);
					if(total > pageNum * pageSize){
						vm.hasMore = 2;
					}else if(total > 0 && total <= pageNum * pageSize){
						vm.hasMore = 1;
					}else{
						vm.hasMore = 0;
					}
				}else{
					alert(res.msg);
				}
			}
		});
	},
	getSum:function(){
		var $total = $('#total'),
			$chk = $('#wishList :checked'),
			price = 0,
			$price = null,
			sum = 0,
			len1 = $('#wishList .chk').length,
			len2 = $('#wishList :checked').length;
		$chk.each(function(){
			$price = $(this).parent().next().find('.price');
			price = Number($price.text().substr(1))*100;
			sum += price;
		});
		sum /= 100;
		if(sum == 0){
			$('#donate').addClass('disable');
		}else{
			$('#donate').removeClass('disable');
		}
		if(len1 != len2){
			$('#selAll').removeClass('all');
		}else{
			$('#selAll').addClass('all');
		}
		$total.text(sum);
	},
	getRandom:function(num,len){
		var idx = 0, temp = -1, arr = [];
		for(var i=0;i<num;i++){
			idx = Math.floor(Math.random() * len);
			if(temp == idx){
				i--;
				continue;
			}else{
				temp = idx;
				arr.push(idx);
			}
		}
		return arr;
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
	wishes.init();
});
