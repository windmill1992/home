var base = 'http://www.17xs.org/';
var dataUrl = {
	getAddrList: base +'user/userAddressList.do',	//获取所有地址
	deleteAdress: base +'user/deleteAdress.do'		//批量删除地址
};
var page = 1;
var addrList = {
	timer:null,
	init:function(){
		var that = this;
		
		that.getAddrList(page,50);
		
		var onOff = true, $obj = null;
		
		$('body').on('click','#manageAddr',function(){
			var forid = '', $addr = $('.addressLeft');
			if(onOff){
				$('.addressRight').css({'right':'.24rem'});
				$('.addAddressBox').css({'bottom':'0'});
				$('.select').css({'left':'.24rem'});
				$('.select2').css({'bottom':'1rem'});
				$addr.css({'margin-left':'.45rem'});
				$addr.each(function(){
					forid = $(this).attr('data-for');
					$(this).attr('for',forid);
				});
				$(this).text('取消');
			}else{
				$('.addressRight').css({'right':'-.91rem'});
				$('.addAddressBox').css({'bottom':'-2.1rem'});
				$('.select').css({'left':'-.35rem'});
				$('.select2').css({'bottom':'-1.1rem'});
				$('.addressLeft').css({'margin-left':'0'});
				$addr.each(function(){
					$(this).attr('for','');
				});
				$('.chk').each(function(){
					this.checked = false;
				});
				$('#selReverse').fadeOut().next().fadeOut().end().get(0).checked = false;
				$('#delSelected').fadeOut();
				$(this).text('管理');
			}
			onOff = !onOff;
			
		}).on('click', '#addAddress', function() {
			location.href = "/user/toAddress.do?type=3";
			
		}).on('click', '.addressItem .edit', function() {
			var id = $(this).closest('.addressItem').attr("id");
			location.href = "/user/toAddress.do?type=6&id="+ id;
			
		}).on('click', '.addressItem .del', function() {
			$('#delDialog').addClass('show').find('p').html('您确定删除这个地址吗？');
			$obj = $(this).closest('.addressItem');
			
		}).on('click', '#sure', function() {
			var ids = [];
			$obj.each(function(){
				ids.push(this.id);
			});
			that.delAddr($obj,ids.join(','));
			$('#delDialog').addClass('hide');
			setTimeout(function(){
				$('#delDialog').removeClass('show hide');
			},400);
			
		}).on('click', '#cancel', function() {
			$('#delDialog').addClass('hide');
			setTimeout(function(){
				$('#delDialog').removeClass('show hide');
			},400);
			
		}).on('change','.addressItem .chk',function(){
			var len1 = $('.addressItem .chk').length,
				len2 = $('.addressItem .chk:checked').length;
			if(len1 == len2){
				$('#selAll')[0].checked = true;
				$('#selReverse').fadeOut().next().fadeOut().end().get(0).checked = false;
				$('#delSelected').fadeIn();
			}else{
				$('#selAll')[0].checked = false;
				if(len2 == 0){
					$('#selReverse').fadeOut().next().fadeOut().end().get(0).checked = false;
					$('#delSelected').fadeOut();
				}else{
					$('#selReverse').fadeIn().next().fadeIn();
					$('#delSelected').fadeIn();
				}
			}
		}).on('change','#selAll',function(){
			var check = this.checked;
			$('.addressItem .chk').each(function(){
				this.checked = check;
			});
			$('#selReverse').fadeOut().next().fadeOut().end().get(0).checked = false;
			if(check){
				$('#delSelected').fadeIn();
			}else{
				$('#delSelected').fadeOut();
			}
		}).on('change','#selReverse',function(){
			$('.addressItem .chk').each(function(){
				this.checked = !this.checked;
			});
		}).on('click', '#delSelected', function() {
			$('#delDialog').addClass('show').find('p').html('您确定删除这些地址吗？');
			$obj = $('.addressItem .chk:checked').parent().parent();
		});
	},
	getAddrList:function(pageNum,pageSize){
		var that = this;
		$.ajax({
			url: dataUrl.getAddrList,
			type: 'get',
			data: {pageNum: pageSize,page: pageNum},
			success: function(data) {
				if(data.flag == 1) {
					var ret = data.obj.data,
						address = '',
						html = [],
						r = '';
					for(var i=0;i<ret.length;i++) {
						r = ret[i];
						address = r.province + r.city + r.area;
						html.push('<li class="addressItem" id="'+ r.id +'"><div class="select">');
						html.push('<input type="checkbox" name="addr" id="chk'+ (i+1) +'" class="chk"/>')
						html.push('</div><label class="addressLeft" data-for="chk'+ (i+1) +'">');
						html.push('<p class="txt1">'+ r.name +'&nbsp;&nbsp;'+ r.mobile +'</p><p class="txt2">'+ address +'</p>');
						html.push('<p class="txt2">'+ r.detailAddress +'</p>');
						html.push('</label><div class="addressRight flex fcen spb">');
						html.push('<a href="javascript:void(0);" class="edit icon-a"></a>');
						html.push('<a href="javascript:void(0);" class="del icon-a"></a></li>');
					}
					$('.addressList').append(html.join(''));
					if(data.errorCode == '0002' || data.obj.nums == 0){
						$('#manageAddr').remove();
						$('.addAddressBox').css({'bottom':'0'});
						$('#nodata').html('暂无地址信息~').show();
					}else{
						$('#manageAddr').show();
					}
				}else{
					alert(data.errorMsg);
				}
			}
		});
	},
	delAddr:function($objs,ids){
		var that = this;
		$.ajax({
			type:"post",
			url:dataUrl.deleteAdress,
			data:{addressIds:ids},
			success:function(res){
				if(res.flag == 1){
					that.showTips('删除成功！');
					$objs.each(function(){
						$(this).fadeOut(function(){
							$(this).remove();
							if($('.addressList li').length == 0){
								$('#nodata').html('暂无地址信息~').show();
								$('#manageAddr').remove();
								$('.select2').remove();
							}
						});
					});
				}else if(res.flag == -1){
					location.href = '/ucenter/user/Login_H5.do';
				}else{
					that.showTips('删除失败！');
				}
			}
		});
	},
	showTips:function(txt){
		var $msg = $('#msg');
    	if(!$msg.is(':hidden')){
    		$msg.hide();
    		clearTimeout(this.timer);
    		this.timer = null;
    	}
    	$msg.html(txt).fadeIn();
    	this.timer = setTimeout(function(){
    		$msg.fadeOut();
    	},2000);
	}
};

$(function(){
	addrList.init();
});
